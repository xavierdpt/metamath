package xd.metamath;

public class mmvstr {












long db1=0;
#ifdef NDEBUG
# define INCDB1(x)
#else
# define INCDB1(x) db1 += (x)
#endif

#define MAX_ALLOC_STACK 100
long g_tempAllocStackTop = 0;
long g_startTempAllocStack = 0;
void *tempAllocStack[MAX_ALLOC_STACK];

static void freeTempAlloc(void)
{

  long i;
  for (i = g_startTempAllocStack; i < g_tempAllocStackTop; i++) {
INCDB1(-1 - (long)strlen(tempAllocStack[i]));

    free(tempAllocStack[i]);
  }
  g_tempAllocStackTop = g_startTempAllocStack;
}


static void pushTempAlloc(void *mem)
{
  if (g_tempAllocStackTop >= (MAX_ALLOC_STACK-1)) {
    printf("*** FATAL ERROR ***  Temporary string stack overflow\n");
#if __STDC__
    fflush(stdout);
#endif
    bug(2201);
  }
  tempAllocStack[g_tempAllocStackTop++] = mem;
}


static void* tempAlloc(long size)
{
  void* memptr = malloc((size_t)size);
  if (!memptr || size == 0) {
    printf("*** FATAL ERROR ***  Temporary string allocation failed\n");
#if __STDC__
    fflush(stdout);
#endif
    bug(2202);
  }
  pushTempAlloc(memptr);
INCDB1(size);

  return memptr;
}



void makeTempAlloc(vstring s)
{
  pushTempAlloc(s);
INCDB1((long)strlen(s) + 1);
db-=(long)strlen(s) + 1;

}



void let(vstring *target, vstring source)





{

  size_t sourceLength = strlen(source);
  size_t targetLength = strlen(*target);
if (targetLength) {
  db -= (long)targetLength+1;

}
if (sourceLength) {
  db += (long)sourceLength+1;

}
  if (targetLength < sourceLength) {

    if (targetLength)
      free(*target);
    *target = malloc(sourceLength + 1);
    if (!*target) {
      printf("*** FATAL ERROR ***  String memory couldn't be allocated\n");
#if __STDC__
      fflush(stdout);
#endif
      bug(2204);
    }
  }
  if (sourceLength) {
    strcpy(*target, source);
  } else {

    if (targetLength) {
      free(*target);
    }
    *target= "";
  }

  freeTempAlloc();

}

vstring cat(vstring string1,...)
#define MAX_CAT_ARGS 50
{
  va_list ap;
  vstring arg[MAX_CAT_ARGS];
  size_t argPos[MAX_CAT_ARGS];
  vstring result;
  int i;
  int numArgs = 0;

  size_t pos = 0;
  char* curArg = string1;

  va_start(ap, string1);
  do {

    if (numArgs >= MAX_CAT_ARGS) {
      printf("*** FATAL ERROR ***  Too many cat() arguments\n");
#if __STDC__
      fflush(stdout);
#endif
      bug(2206);
    }
    arg[numArgs] = curArg;
    argPos[numArgs] = pos;
    pos += strlen(curArg);
  } while (++numArgs, (curArg = va_arg(ap,char *)) != 0);
  va_end(ap);


  result = tempAlloc((long)pos+1);

  for (i = 0; i < numArgs; ++i)
    strcpy(result + argPos[i], arg[i]);
  return result;
}





int linput(File stream, const char* ask, vstring *target)
{
  char f[10001];
  int result = 0;
  int eol_found = 0;
  if (ask) {
    printf("%s", ask);
#if __STDC__
    fflush(stdout);
#endif
  }
  if (stream == NULL) stream = stdin;
  while (!eol_found && fgets(f, sizeof(f), stream))
  {
    size_t endpos = strlen(f) - 1;
    eol_found = (f[endpos] == '\n');
    if (eol_found)
      f[endpos] = 0;
    if (result)

      let(target , cat(*target, f, NULL));
    else
      let(target, f);
    result = 1;
  }
  return result;
}



long len(vstring s)
{
  return ((long)strlen(s));
}



vstring seg(vstring sin, long start, long stop)
{
  if (start < 1) start = 1;
  return mid(sin, start, stop - start + 1);
}



vstring mid(vstring sin, long start, long length)
{
  vstring sout;
  if (start < 1) start = 1;
  if (length < 0) length = 0;
  sout=tempAlloc(length + 1);
  strncpy(sout,sin + start - 1, (size_t)length);

  sout[length] = 0;
  return (sout);
}



vstring left(vstring sin,long n)
{
  return mid(sin, 1, n);
}



vstring right(vstring sin, long n)
{
  return seg(sin, n, (long)(strlen(sin)));
}



vstring edit(vstring sin,long control)
#define isblank_(c) ((c == ' ') || (c == '\t'))

#define isblankorlf_(c) ((c == ' ') || (c == '\t') || (c == '\n'))

{
  vstring sout;
  long i, j, k, m;
  int last_char_is_blank;
  int trim_flag, discardctrl_flag, bracket_flag, quote_flag, case_flag;
  int alldiscard_flag, leaddiscard_flag, traildiscard_flag,
      traildiscardLF_flag, reduce_flag;
  int processing_inside_quote=0;
  int lowercase_flag, tab_flag, untab_flag, screen_flag, discardcr_flag;
  unsigned char graphicsChar;


  trim_flag = control & 1;
  alldiscard_flag = control & 2;
  discardctrl_flag = control & 4;
  leaddiscard_flag = control & 8;
  reduce_flag = control & 16;
  case_flag = control & 32;
  bracket_flag = control & 64;
  traildiscard_flag = control & 128;
  traildiscardLF_flag = control & 16384;
  quote_flag = control & 256;


  lowercase_flag = control & 512;
  tab_flag = control & 1024;
  untab_flag = control & 2048;
  screen_flag = control & 4096;
  discardcr_flag = control & 8192;


  i = (long)strlen(sin) + 1;
  if (untab_flag) i = i * 7;
  sout=tempAlloc(i);
  strcpy(sout,sin);


  i=0;
  if (leaddiscard_flag)
    while ((sout[i] != 0) && isblank_(sout[i]))
      sout[i++] = 0;


  while (sout[i] != 0) {


    if (quote_flag && ((sout[i] == '"') || (sout[i] == '\'')))
       processing_inside_quote = ~ processing_inside_quote;
    if (processing_inside_quote) {

       i++; continue;
    }


    if ((alldiscard_flag) && isblank_(sout[i]))
        sout[i] = 0;


    if (trim_flag)
       sout[i] = sout[i] & 0x7F;


    if ((discardctrl_flag) && (
         (sout[i] == '\015') ||
         (sout[i] == '\012') ||
         (sout[i] == '\014') ||
         (sout[i] == '\033') ||

         (sout[i] == '\010')))
      sout[i] = 0;


    if ((discardcr_flag) && (
         (sout[i] == '\015')))
      sout[i] = 0;


    if ((case_flag) && (sout[i] >= 'a' && sout[i] <= 'z'))
       sout[i] = (char)(sout[i] - ('a' - 'A'));


    if ((bracket_flag) && (sout[i] == '['))
       sout[i] = '(';
    if ((bracket_flag) && (sout[i] == ']'))
       sout[i] = ')';


    if ((lowercase_flag) && (sout[i] >= 'A' && sout[i] <= 'Z'))
       sout[i] = (char)(sout[i] + ('a' - 'A'));


    if (screen_flag) {
      graphicsChar = (unsigned char)sout[i];

      if (graphicsChar >= 234 && graphicsChar <= 237) sout[i] = '+';
      if (graphicsChar == 241) sout[i] = '-';
      if (graphicsChar == 248) sout[i] = '|';
      if (graphicsChar == 166) sout[i] = '|';

      if (graphicsChar == 218  || graphicsChar == 217
          || graphicsChar == 191  || graphicsChar == 192 )
        sout[i] = '+';
      if (graphicsChar == 196) sout[i] = '-';
      if (graphicsChar == 179) sout[i] = '|';
    }


    i++;
  }



  for (j = 0, k = 0; j <= i; j++)
    if (sout[j]!=0) sout[k++]=sout[j];
  sout[k] = 0;



  if (traildiscard_flag) {
    --k;
    while ((k >= 0) && isblank_(sout[k])) --k;
    sout[++k] = 0;
  }



  if (traildiscardLF_flag) {
    --k;
    while ((k >= 0) && isblankorlf_(sout[k])) --k;
    sout[++k] = 0;
  }


  if (reduce_flag) {
    i = j = last_char_is_blank = 0;
    while (i <= k - 1) {
      if (!isblank_(sout[i])) {
        sout[j++] = sout[i++];
        last_char_is_blank = 0;
      } else {
        if (!last_char_is_blank)
          sout[j++]=' ';
        last_char_is_blank = 1;
        i++;
      }
    }
    sout[j] = 0;
  }


  if (untab_flag || tab_flag) {





    k = (long)strlen(sout);
    m = 0;
    for (i = 1; i <= k; i++) {
      if (sout[i - 1] == '\n') {
        m = 0;
        continue;
      }
      m++;
      if (sout[i - 1] != '\t') continue;
      for (j = k; j >= i; j--) {
        sout[j + 8 - ((m - 1) & 7) - 1] = sout[j];
      }
      for (j = i; j < i + 8 - ((m - 1) & 7); j++) {
        sout[j - 1] = ' ';
      }
      k = k + 8 - ((m - 1) & 7);
    }
  }



  if (tab_flag) {


    k = (long)strlen(sout);
    for (i = 8; i < k; i = i + 8) {
      j = i;


      m = i - 2;

      while (sout[j - 1] == ' ' && j > i - 8) j--;

      if (j <= m) {
        sout[j] = '\t';
        j = i;
        while (sout[j - 1] == ' ' && j > i - 8 + 1) {
          sout[j - 1] = 0;
          j--;
        }
      }
    }
    i = k;


    for (j = 0, k = 0; j <= i; j++)
      if (sout[j] != 0) sout[k++] = sout[j];
    sout[k] = 0;

  }

  return (sout);
}



vstring string(long n, char c)
{
  vstring sout;
  long j = 0;
  if (n < 0) n = 0;
  sout=tempAlloc(n + 1);
  while (j < n) sout[j++] = c;
  sout[j] = 0;
  return (sout);
}



vstring space(long n)
{
  return (string(n, ' '));
}



vstring chr(long n)
{
  vstring sout;
  sout = tempAlloc(2);
  sout[0] = (char)(n & 0xFF);
  sout[1] = 0;
  return(sout);
}





long instr(long start_position, vstring string1, vstring string2)
{
  char *sp1, *sp2;
  long ls1, ls2;
  long found = 0;
  if (start_position < 1) start_position = 1;
  ls1 = (long)strlen(string1);
  ls2 = (long)strlen(string2);
  if (start_position > ls1) start_position = ls1 + 1;
  sp1 = string1 + start_position - 1;
  while ((sp2 = strchr(sp1, string2[0])) != 0) {
    if (strncmp(sp2, string2, (size_t)ls2) == 0) {
      found = sp2 - string1 + 1;
      break;
    } else
      sp1 = sp2 + 1;
  }
  return (found);
}





long rinstr(vstring string1, vstring string2)
{
  long pos = 0;
  long savePos = 0;

  while (1) {
    pos = instr(pos + 1, string1, string2);
    if (!pos) break;
    savePos = pos;
  }
  return (savePos);
}


vstring xlate(vstring sin,vstring table)
{
  vstring sout;
  long len_table, len_sin;
  long i, j;
  long table_entry;
  char m;
  len_sin = (long)strlen(sin);
  len_table = (long)strlen(table);
  sout = tempAlloc(len_sin+1);
  for (i = j = 0; i < len_sin; i++)
  {
    table_entry = 0x000000FF & (long)sin[i];
    if (table_entry < len_table)
      if ((m = table[table_entry])!='\0')
        sout[j++] = m;
  }
  sout[j]='\0';
  return (sout);
}



long ascii_(vstring c)
{
  return ((long)c[0]);
}



double val(vstring s)
{
  double v = 0;
  char signFound = 0;
  double power = 1.0;
  long i;
  for (i = (long)strlen(s); i >= 0; i--) {
    switch (s[i]) {
      case '.':
        v = v / power;
        power = 1.0;
        break;
      case '-':
        signFound = 1;
        break;
      case '0': case '1': case '2': case '3': case '4':
      case '5': case '6': case '7': case '8': case '9':
        v = v + ((double)(s[i] - '0')) * power;
        power = 10.0 * power;
        break;
    }
  }
  if (signFound) v = - v;
  return v;
}



vstring date()
{
  vstring sout;
  struct tm *time_structure;
  time_t time_val;
  char *month[12];



  month[0] = "Jan";
  month[1] = "Feb";
  month[2] = "Mar";
  month[3] = "Apr";
  month[4] = "May";
  month[5] = "Jun";
  month[6] = "Jul";
  month[7] = "Aug";
  month[8] = "Sep";
  month[9] = "Oct";
  month[10] = "Nov";
  month[11] = "Dec";

  time(&time_val);
  time_structure = localtime(&time_val);
  sout = tempAlloc(15);


  sprintf(sout,"%d-%s-%04d",
      time_structure->tm_mday,
      month[time_structure->tm_mon],


      (int)((time_structure->tm_year) + 1900));
  return(sout);
}



vstring time_()
{
  vstring sout;
  struct tm *time_structure;
  time_t time_val;
  int i;
  char *format;
  char *format1 = "%d:%d %s";
  char *format2 = "%d:0%d %s";
  char *am_pm[2];


  am_pm[0] = "AM";
  am_pm[1] = "PM";

  time(&time_val);
  time_structure = localtime(&time_val);
  if (time_structure->tm_hour >= 12)
    i = 1;
  else
    i = 0;
  if (time_structure->tm_hour > 12)
    time_structure->tm_hour -= 12;
  if (time_structure->tm_hour == 0)
    time_structure->tm_hour = 12;
  sout = tempAlloc(12);
  if (time_structure->tm_min >= 10)
    format = format1;
  else
    format = format2;
  sprintf(sout,format,
      time_structure->tm_hour,
      time_structure->tm_min,
      am_pm[i]);
  return(sout);
}



vstring str(double f)
{




  vstring s;
  long i;
  s = tempAlloc(50);
  sprintf(s,"%f", f);
  if (strchr(s, '.') != 0) {
    for (i = (long)strlen(s) - 1; i > 0; i--) {
      if (s[i] != '0') break;
      s[i] = 0;
    }
    if (s[i] == '.') s[i] = 0;
INCDB1(-(49 - (long)strlen(s)));
  }
  return (s);
}



vstring num1(double f)
{
  return (str(f));
}




vstring num(double f)
{
  return (cat(" ",str(f)," ",NULL));
}









vstring entry(long element, vstring list)
{
  vstring sout;
  long commaCount, lastComma, i, length;
  if (element < 1) return ("");
  lastComma = -1;
  commaCount = 0;
  i = 0;
  while (list[i] != 0) {
    if (list[i] == ',') {
      commaCount++;
      if (commaCount == element) {
        break;
      }
      lastComma = i;
    }
    i++;
  }
  if (list[i] == 0) commaCount++;
  if (element > commaCount) return ("");
  length = i - lastComma - 1;
  if (length < 1) return ("");
  sout = tempAlloc(length + 1);
  strncpy(sout, list + lastComma + 1, (size_t)length);
  sout[length] = 0;
  return (sout);
}


long lookup(vstring expression, vstring list)
{
  long i, exprNum, exprPos;
  char match;

  match = 1;
  i = 0;
  exprNum = 0;
  exprPos = 0;
  while (list[i] != 0) {
    if (list[i] == ',') {
      exprNum++;
      if (match) {
        if (expression[exprPos] == 0) return exprNum;
      }
      exprPos = 0;
      match = 1;
      i++;
      continue;
    }
    if (match) {
      if (expression[exprPos] != list[i]) match = 0;
    }
    i++;
    exprPos++;
  }
  exprNum++;
  if (match) {
    if (expression[exprPos] == 0) return exprNum;
  }
  return 0;
}



long numEntries(vstring list)
{
  long i, commaCount;
  if (list[0] == 0) {
    commaCount = -1;
  } else {
    commaCount = 0;
    i = 0;
    while (list[i] != 0) {
      if (list[i] == ',') commaCount++;
      i++;
    }
  }
  return (commaCount + 1);
}

long entryPosition(long element, vstring list)
{
  long commaCount, lastComma, i;
  if (element < 1) return 0;
  lastComma = -1;
  commaCount = 0;
  i = 0;
  while (list[i] != 0) {
    if (list[i] == ',') {
      commaCount++;
      if (commaCount == element) {
        break;
      }
      lastComma = i;
    }
    i++;
  }
  if (list[i] == 0) {
    if (i == 0) return 0;
    if (list[i - 1] == ',') return 0;
    commaCount++;
  }
  if (element > commaCount) return (0);
  if (list[lastComma + 1] == ',') return 0;
  return (lastComma + 2);
}




}