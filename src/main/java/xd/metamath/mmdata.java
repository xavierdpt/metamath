package xd.metamath;

public class mmdata {









long db=0,db0=0,db2=0,db3=0,db4=0,db5=0,db6=0,db7=0,db8=0,db9=0;
flag g_listMode = 0;
flag g_toolsMode = 0;




vstring g_proofDiscouragedMarkup = "";
vstring g_usageDiscouragedMarkup = "";
flag g_globalDiscouragement = 1;


vstring g_contributorName = "";


int g_currentScope = 0;


long g_MAX_STATEMENTS = 1;
long g_MAX_MATHTOKENS = 1;
long g_MAX_INCLUDECALLS = 2;
struct statement_struct *g_Statement = NULL;
long *g_labelKey = NULL;
struct mathToken_struct *g_MathToken;
long *g_mathKey = NULL;
long g_statements = 0, labels = 0, g_mathTokens = 0;


struct includeCall_struct *g_IncludeCall = NULL;
long g_includeCalls = -1;

char *g_sourcePtr = NULL;
long g_sourceLen;



struct nullNmbrStruct g_NmbrNull = {-1, sizeof(long), sizeof(long), -1};


struct nullPntrStruct g_PntrNull = {-1, sizeof(long), sizeof(long), NULL};

nmbrString *nmbrTempAlloc(long size);

void nmbrCpy(nmbrString *sout, nmbrString *sin);
void nmbrNCpy(nmbrString *s, nmbrString *t, long n);

pntrString *pntrTempAlloc(long size);

void pntrCpy(pntrString *sout, pntrString *sin);
void pntrNCpy(pntrString *s, pntrString *t, long n);

vstring g_qsortKey;



  static final long  MEM_POOL_GROW =D.MEM_POOL_GROW;

long poolAbsoluteMax = 1000000;
long poolTotalFree = 0;
long i1,j1_,k1;
void **memUsedPool = NULL;
long memUsedPoolSize = 0;
long memUsedPoolMax = 0;
void **memFreePool = NULL;
long memFreePoolSize = 0;
long memFreePoolMax = 0;

void *poolFixedMalloc(long size )
{
  void *ptr;
  void *ptr2;



if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("a0: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
  if (!memFreePoolSize) {
    ptr = malloc( 3 * sizeof(long) + (size_t)size);
    if (!ptr) outOfMemory(
        cat("#25 (poolFixedMalloc ", str((double)size), ")", NULL));

    ptr = (long *)ptr + 3;
    ((long *)ptr)[-1] = size;
    ((long *)ptr)[-2] = size;
    ((long *)ptr)[-3] = -1;
    return (ptr);
  } else {
    memFreePoolSize--;
    ptr = memFreePool[memFreePoolSize];
    poolTotalFree = poolTotalFree - ((long *)ptr)[-2];
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("a: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
    if (size <= ((long *)ptr)[-2]) {
      ptr2 = realloc( (long *)ptr - 3, 3 * sizeof(long) + (size_t)size);

      if (!ptr2) bug(1382);
      ptr = ptr2;
    } else {
      free((long *)ptr - 3);
      ptr = malloc( 3 * sizeof(long) + (size_t)size);
    }
    if (!ptr) {

      print2("Memory is low.  Deallocating storage pool...\n");
      memFreePoolPurge(0);
      ptr = malloc( 3 * sizeof(long) + (size_t)size);
      if (!ptr) outOfMemory(
          cat("#26 (poolMalloc ", str((double)size), ")", NULL));

    }
    ptr = (long *)ptr + 3;
    ((long *)ptr)[-1] = size;
    ((long *)ptr)[-2] = size;
    ((long *)ptr)[-3] = -1;
    return (ptr);
  }
}



void *poolMalloc(long size )
{
  void *ptr;
  long memUsedPoolTmpMax;
  void *memUsedPoolTmpPtr;


  if (poolTotalFree > poolAbsoluteMax) {
    memFreePoolPurge(1);
  }

if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("b0: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
  if (!memFreePoolSize) {
    ptr = malloc( 3 * sizeof(long) + (size_t)size);
    if (!ptr) {
      outOfMemory(cat("#27 (poolMalloc ", str((double)size), ")", NULL));
    }
    ptr = (long *)ptr + 3;
    ((long *)ptr)[-1] = size;
    ((long *)ptr)[-2] = size;
    ((long *)ptr)[-3] = -1;
    return (ptr);
  } else {
    memFreePoolSize--;
    ptr = memFreePool[memFreePoolSize];
    poolTotalFree = poolTotalFree - ((long *)ptr)[-2];
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("b: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
    if (size <= ((long *)ptr)[-2]) {
      ((long *)ptr)[-1] = size;
      ((long *)ptr)[-3] = -1;
    } else {
      free((long *)ptr - 3);
      ptr = malloc( 3 * sizeof(long) + (size_t)size);
      if (!ptr) {

        print2("Memory is low.  Deallocating storage pool...\n");
        memFreePoolPurge(0);
        ptr = malloc( 3 * sizeof(long) + (size_t)size);
        if (!ptr) outOfMemory(
            cat("#28 (poolMalloc ", str((double)size), ")", NULL));

      }
      ptr = (long *)ptr + 3;
      ((long *)ptr)[-1] = size;
      ((long *)ptr)[-2] = size;
      ((long *)ptr)[-3] = -1;
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("bb: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
      return (ptr);
    }
  }
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("bc: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
  if (((long *)ptr)[-1] == ((long *)ptr)[-2]) return (ptr);

  poolTotalFree = poolTotalFree + ((long *)ptr)[-2] - ((long *)ptr)[-1];
  if (memUsedPoolSize >= memUsedPoolMax) {
    memUsedPoolTmpMax = memUsedPoolMax + MEM_POOL_GROW;
if(db9)printf("Growing used pool to %ld\n",memUsedPoolTmpMax);
    if (!memUsedPoolMax) {

      memUsedPoolTmpPtr = malloc((size_t)memUsedPoolTmpMax
          * sizeof(void *));
      if (!memUsedPoolTmpPtr) bug(1303);
    } else {

      memUsedPoolTmpPtr = realloc(memUsedPool,
          (size_t)memUsedPoolTmpMax * sizeof(void *));
    }
    if (!memUsedPoolTmpPtr) {
      outOfMemory(cat("#29 (poolMalloc ", str((double)memUsedPoolTmpMax), ")", NULL));
    } else {

      memUsedPool = memUsedPoolTmpPtr;
      memUsedPoolMax = memUsedPoolTmpMax;
    }
  }
  memUsedPool[memUsedPoolSize] = ptr;
  ((long *)ptr)[-3] = memUsedPoolSize;
  memUsedPoolSize++;
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("c: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
  return (ptr);
}


void poolFree(void *ptr)
{
  void *ptr1;
  long usedLoc;
  long memFreePoolTmpMax;
  void *memFreePoolTmpPtr;

if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("c0: pool %ld stat %ld\n",poolTotalFree,i1+j1_);

  usedLoc = ((long *)ptr)[-3];
  if (usedLoc >= 0) {
    poolTotalFree = poolTotalFree - ((long *)ptr)[-2] + ((long *)ptr)[-1];
    memUsedPoolSize--;



    if (usedLoc < memUsedPoolSize) {
      memUsedPool[usedLoc] = memUsedPool[memUsedPoolSize];
      ptr1 = memUsedPool[usedLoc];
      ((long *)ptr1)[-3] = usedLoc;
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("d: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
    }

  }



  if (memFreePoolSize >= memFreePoolMax) {
    memFreePoolTmpMax = memFreePoolMax + MEM_POOL_GROW;
if(db9)printf("Growing free pool to %ld\n",memFreePoolTmpMax);
    if (!memFreePoolMax) {

      memFreePoolTmpPtr = malloc((size_t)memFreePoolTmpMax
          * sizeof(void *));
      if (!memFreePoolTmpPtr) bug(1304);
    } else {

      memFreePoolTmpPtr = realloc(memFreePool,
          (size_t)memFreePoolTmpMax * sizeof(void *));
    }
    if (!memFreePoolTmpPtr) {
if(db9)printf("Realloc failed\n");
      outOfMemory(cat("#30 (poolFree ", str((double)memFreePoolTmpMax), ")", NULL));
    } else {

      memFreePool = memFreePoolTmpPtr;
      memFreePoolMax = memFreePoolTmpMax;
    }
  }

  memFreePool[memFreePoolSize] = ptr;
  ((long *)ptr)[-3] = -2;
  memFreePoolSize++;
  poolTotalFree = poolTotalFree + ((long *)ptr)[-2];
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("e: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
  return;
}



void addToUsedPool(void *ptr)
{
  long memUsedPoolTmpMax;
  void *memUsedPoolTmpPtr;
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("d0: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
  if (((long *)ptr)[-1] == ((long *)ptr)[-2]) bug(1305);
  if (((long *)ptr)[-1] == ((long *)ptr)[-2]) return;

  if (memUsedPoolSize >= memUsedPoolMax) {
    memUsedPoolTmpMax = memUsedPoolMax + MEM_POOL_GROW;
if(db9)printf("1Growing used pool to %ld\n",memUsedPoolTmpMax);
    if (!memUsedPoolMax) {

      memUsedPoolTmpPtr = malloc((size_t)memUsedPoolTmpMax
          * sizeof(void *));
      if (!memUsedPoolTmpPtr) bug(1362);
    } else {

      memUsedPoolTmpPtr = realloc(memUsedPool, (size_t)memUsedPoolTmpMax
          * sizeof(void *));
    }
    if (!memUsedPoolTmpPtr) {
      outOfMemory("#31 (addToUsedPool)");
    } else {

      memUsedPool = memUsedPoolTmpPtr;
      memUsedPoolMax = memUsedPoolTmpMax;
    }
  }
  memUsedPool[memUsedPoolSize] = ptr;
  ((long *)ptr)[-3] = memUsedPoolSize;
  memUsedPoolSize++;
  poolTotalFree = poolTotalFree + ((long *)ptr)[-2] - ((long *)ptr)[-1];
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("f: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
  return;
}


void memFreePoolPurge(flag untilOK)
{
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("e0: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
  while (memFreePoolSize) {
    memFreePoolSize--;

    poolTotalFree = poolTotalFree -
        ((long *)(memFreePool[memFreePoolSize]))[-2];
    free((long *)(memFreePool[memFreePoolSize]) - 3);
    if (untilOK) {

      if (poolTotalFree <= poolAbsoluteMax) return;
    }
  }

  if (memFreePoolMax != MEM_POOL_GROW) {

    if (memFreePool) free(memFreePool);
    memFreePool = malloc(MEM_POOL_GROW
        * sizeof(void *));
    memFreePoolMax = MEM_POOL_GROW;
  }
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("g: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
  return;
}



void getPoolStats(long *freeAlloc, long *usedAlloc, long *usedActual)
{
  long i;
  *freeAlloc = 0;
  *usedAlloc = 0;
  *usedActual = 0;
  for (i = 0; i < memFreePoolSize; i++) {
    *freeAlloc = *freeAlloc +  ((long *)(memFreePool[i]))[-2];
  }
  for (i = 0; i < memUsedPoolSize; i++) {
    *usedActual = *usedActual + 12 + ((long *)(memUsedPool[i]))[-1];
    *usedAlloc = *usedAlloc + ((long *)(memUsedPool[i]))[-2] -
        ((long *)(memUsedPool[i]))[-1];
  }
 if (!db9)print2("poolTotalFree %ld  alloc %ld\n", poolTotalFree, *freeAlloc +
   *usedAlloc);
}



void initBigArrays(void)
{


  g_Statement = malloc((size_t)g_MAX_STATEMENTS * sizeof(struct statement_struct));

  if (!g_Statement) {
    print2("*** FATAL ***  Could not allocate g_Statement space\n");
    bug(1363);
    }
  g_MathToken = malloc((size_t)g_MAX_MATHTOKENS * sizeof(struct mathToken_struct));

  if (!g_MathToken) {
    print2("*** FATAL ***  Could not allocate g_MathToken space\n");
    bug(1364);
    }
  g_IncludeCall = malloc((size_t)g_MAX_INCLUDECALLS * sizeof(struct includeCall_struct));

  if (!g_IncludeCall) {
    print2("*** FATAL ***  Could not allocate g_IncludeCall space\n");
    bug(1365);
    }
}


long getFreeSpace(long max)
{
  long i , j, k;
  char *s;
  i = 0;
  j = max + 2;
  while (i < j - 2) {
    k = (i + j) / 2;
    s = malloc((size_t)k);
    if (s) {
      free(s);
      i = k;
    } else {
      j = k;
    }
  }
  return (i);
}


void outOfMemory(vstring msg)
{
  vstring tmpStr = "";
  print2("*** FATAL ERROR:  Out of memory.\n");
  print2("Internal identifier (for technical support):  %s\n",msg);
  print2(
"To solve this problem, remove some unnecessary statements or file\n");
  print2(
"inclusions to reduce the size of your input source.\n");
  print2(
"Monitor memory periodically with SHOW MEMORY.\n");

  print2("\n");
  print2("Press <return> to exit Metamath.\n");
  tmpStr = cmdInput1("");

  let(&tmpStr, left(tmpStr, 0));

  if (g_logFileOpenFlag) {
    fclose(g_logFilePtr);
    g_logFileOpenFlag = 0;
  }

  exit(1);
}




void bug(int bugNum)
{
  vstring tmpStr = "";
  flag oldMode;
  long wrongAnswerCount = 0;
  static flag mode = 0;


  flag saveOutputToString = g_outputToString;
  g_outputToString = 0;

  if (mode == 2) {

    print2("?BUG CHECK:  *** DETECTED BUG %ld, IGNORING IT...\n", (long)bugNum);
    return;
  }

  print2("?BUG CHECK:  *** DETECTED BUG %ld\n", (long)bugNum);
  if (mode == 0) {
    print2("\n");
    print2(
  "To get technical support, please send Norm Megill (%salum.mit.edu) the\n",
        "nm@");
    print2(
  "detailed command sequence or a command file that reproduces this bug,\n");
    print2(
  "along with the source file that was used.  See HELP LOG for help on\n");
    print2(
  "recording a session.  See HELP SUBMIT for help on command files.  Search\n");
    print2(
  "for \"bug(%ld)\" in the m*.c source code to find its origin.\n", bugNum);

    print2(
  "If earlier errors were reported, try fixing them first, because they\n");
    print2(
  "may occasionally lead to false bug detection\n");
    print2("\n");
  }

  let(&tmpStr, "?");
  while (strcmp(tmpStr, "A") && strcmp(tmpStr, "a")
      && strcmp(tmpStr, "S") && strcmp(tmpStr, "s")
      && strcmp(tmpStr, "I") && strcmp(tmpStr, "i")
      ) {
    if (wrongAnswerCount > 6) {
      print2(
"Too many wrong answers; program will be aborted to exit scripting loops.\n");
      break;
    }
    if (wrongAnswerCount > 0) {
      let(&tmpStr, "");
      tmpStr = cmdInput1("Please answer I, S, or A:  ");
    } else {
      print2(
 "Press S <return> to step to next bug, I <return> to ignore further bugs,\n");
      let(&tmpStr, "");
      tmpStr = cmdInput1("or A <return> to abort program:  ");
    }
    ***** 8-Nov-03 This loop caused an infinite loop in a cron job when bug
      detection was triggered.  Now, when the loop breaks above,
      the program will abort. *******/
    wrongAnswerCount++;
  }
  oldMode = mode;
  mode = 0;
  if (!strcmp(tmpStr, "S") || !strcmp(tmpStr, "s")) mode = 1;
  if (!strcmp(tmpStr, "I") || !strcmp(tmpStr, "i")) mode = 2;
  if (oldMode == 0 && mode > 0) {

    print2("\n");
    print2(
    "Warning!!!  A bug was detected, but you are continuing anyway.\n");
    print2(
    "The program may be corrupted, so you are proceeding at your own risk.\n");
    print2("\n");
    let(&tmpStr, "");
  }
  if (mode > 0) {

    g_outputToString = saveOutputToString;
    return;
  }
  let(&tmpStr, "");


  print2("\n");

  if (g_logFileOpenFlag) {
    print2("The log file \"%s\" was closed %s %s.\n", g_logFileName,
        date(), time_());
    fclose(g_logFilePtr);
    g_logFileOpenFlag = 0;
  }
  print2("The program was aborted.\n");
  exit(1);
}


  static final long  M_MAX_ALLOC_STACK =D.M_MAX_ALLOC_STACK;


flag matchesList(vstring testString, vstring pattern, char wildCard,
    char oneCharWildCard) {
  long entries, i;
  flag matchVal = 0;
  vstring entryPattern = "";


  long saveTempAllocStack;
  saveTempAllocStack = g_startTempAllocStack;
  g_startTempAllocStack = g_tempAllocStackTop;

  entries = numEntries(pattern);
  for (i = 1; i <= entries; i++) {
    let(&entryPattern, entry(i, pattern));
    matchVal = matches(testString, entryPattern, wildCard, oneCharWildCard);
    if (matchVal) break;
  }

  let(&entryPattern, "");
  g_startTempAllocStack = saveTempAllocStack;
  return (matchVal);
}







flag matches(vstring testString, vstring pattern, char wildCard,
    char oneCharWildCard) {
  long i, ppos, pctr, tpos, s1, s2, s3;
  vstring tmpStr = "";


  if (wildCard == '*') {


    i = instr(1, pattern, "~");
    if (i != 0) {
      if (i == 1) {
        s1 = 1;
      } else {
        s1 = lookupLabel(left(pattern, i - 1));
      }
      s2 = lookupLabel(testString);
      if (i == (long)strlen(pattern)) {
        s3 = g_statements;
      } else {
        s3 = lookupLabel(right(pattern, i + 1));
      }
      let(&tmpStr, "");
      return ((s1 >= 1 && s2 >= 1 && s3 >= 1 && s1 <= s2 && s2 <= s3)
          ? 1 : 0);
    }


    if (pattern[0] == '#') {
      s1 = (long)val(right(pattern, 2));
      if (s1 < 1 || s1 > g_statements)
        return 0;
      if (!strcmp(g_Statement[s1].labelName, testString)) {
        return 1;
      } else {
        return 0;
      }
    }


    if (pattern[0] == '@') {
      s1 = lookupLabel(testString);
      if (s1 < 1) return 0;
      s2 = (long)val(right(pattern, 2));
      if (g_Statement[s1].pinkNumber == s2) {
        return 1;
      } else {
        return 0;
      }
    }


    if (!strcmp(pattern,"=")) {
      s1 = lookupLabel(testString);


      return (g_proveStatement == s1);
    }


    if (!strcmp(pattern,"%")) {
      s1 = lookupLabel(testString);
      if (s1 > 0) {
        if (g_Statement[s1].type == (char)p_) {


          if (g_Statement[s1].proofSectionChanged == 1) {
            return 1;
          }
        }
      }
      return 0;
    }
  }


  ppos = 0;

  while ((pattern[ppos] == testString[ppos] ||
          (pattern[ppos] == oneCharWildCard && testString[ppos] != 0))
      && pattern[ppos] != 0) ppos++;
  if (pattern[ppos] == 0) {
    if (testString[ppos] != 0) {
      return (0);
    } else {
      return (1);
    }
  }
  if (pattern[ppos] != wildCard) {
    return (0);
  }
  tpos = ppos;


  pctr = 0;
  i = 0;
  while (1) {
    if (pattern[ppos + 1 + i] == wildCard) {
      tpos = tpos + pctr + i;
      ppos = ppos + 1 + i;
      i = 0;
      pctr = 0;
      continue;
    }
    if (pattern[ppos + 1 + i] != testString[tpos + pctr + i]
          && (pattern[ppos + 1 + i] != oneCharWildCard
              || testString[tpos + pctr + i] == 0)) {
      if (testString[tpos + pctr + i] == 0) {
        return (0);
      }
      pctr++;
      i = 0;
      continue;
    }
    if (pattern[ppos + 1 + i] == 0) {
      return(1);
    }
    i++;
  }
  bug(1375);
  return (0);
}








long g_nmbrTempAllocStackTop = 0;
long g_nmbrStartTempAllocStack = 0;
nmbrString *nmbrTempAllocStack[M_MAX_ALLOC_STACK];


nmbrString *nmbrTempAlloc(long size)

{




  if (size) {
    if (g_nmbrTempAllocStackTop>=(M_MAX_ALLOC_STACK-1)) {

      outOfMemory("#105 (nmbrString stack array)");
    }
    if (!(nmbrTempAllocStack[g_nmbrTempAllocStackTop++]=poolMalloc(size
        *(long)(sizeof(nmbrString)))))

db2=db2+size*(long)(sizeof(nmbrString));
    return (nmbrTempAllocStack[g_nmbrTempAllocStackTop-1]);
  } else {


    while(g_nmbrTempAllocStackTop != g_nmbrStartTempAllocStack) {
db2=db2-(nmbrLen(nmbrTempAllocStack[g_nmbrTempAllocStackTop-1])+1)
                                              *(long)(sizeof(nmbrString));
      poolFree(nmbrTempAllocStack[--g_nmbrTempAllocStackTop]);
    }

    g_nmbrTempAllocStackTop=g_nmbrStartTempAllocStack;
    return (0);
  }
}



void nmbrMakeTempAlloc(nmbrString *s)
{
    if (g_nmbrTempAllocStackTop>=(M_MAX_ALLOC_STACK-1)) {
      printf(
      "*** FATAL ERROR ***  Temporary nmbrString stack overflow in nmbrMakeTempAlloc()\n");
#if __STDC__
      fflush(stdout);
#endif
      bug(1368);
    }
    if (s[0] != -1) {

      nmbrTempAllocStack[g_nmbrTempAllocStackTop++] = s;
    }
db2=db2+(nmbrLen(s)+1)*(long)(sizeof(nmbrString));
db3=db3-(nmbrLen(s)+1)*(long)(sizeof(nmbrString));
}


void nmbrLet(nmbrString **target,nmbrString *source)






{
  long targetLength,sourceLength;
  long targetAllocLen;
  long poolDiff;
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k0: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
  sourceLength=nmbrLen(source);
  targetLength=nmbrLen(*target);
  targetAllocLen=nmbrAllocLen(*target);
if (targetLength) {

  db3 = db3 - (targetLength+1)*(long)(sizeof(nmbrString));
}
if (sourceLength) {

  db3 = db3 + (sourceLength+1)*(long)(sizeof(nmbrString));
}
  if (targetAllocLen) {
    if (sourceLength) {

      if (targetAllocLen >= sourceLength) {
        nmbrCpy(*target,source);



        poolDiff = ((long *)(*target))[-1] - ((long *)source)[-1];
        ((long *)(*target))[-1] = ((long *)source)[-1];
        if (((long *)(*target))[-1] != ((long *)(*target))[-2]) {
          if (((long *)(*target))[-1] > ((long *)(*target))[-2]) bug(1325);
          if (((long *)(*target))[-3] == -1) {

            addToUsedPool(*target);
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k0aa: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
          } else {

            poolTotalFree = poolTotalFree + poolDiff;
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k0ab: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
          }
        } else {
          if (((long *)(*target))[-3] != -1) {


            poolTotalFree = poolTotalFree + poolDiff;
          }
        }


if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k0a: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
      } else {

        poolFree(*target);

        *target=poolMalloc((sourceLength + 1) * (long)(sizeof(nmbrString)) * 2);

        nmbrCpy(*target,source);



        poolDiff = ((long *)(*target))[-1] - ((long *)source)[-1];
        ((long *)(*target))[-1] = ((long *)source)[-1];

        if (((long *)(*target))[-1] != ((long *)(*target))[-2]) {
          if (((long *)(*target))[-1] > ((long *)(*target))[-2]) bug(1326);
          if (((long *)(*target))[-3] == -1) {

            addToUsedPool(*target);
          } else {

            poolTotalFree = poolTotalFree + poolDiff;
          }
        } else {
          if (((long *)(*target))[-3] != -1) {


            poolTotalFree = poolTotalFree + poolDiff;
          }
        }
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k0b: pool %ld stat %ld\n",poolTotalFree,i1+j1_);

      }

    } else {
      poolFree(*target);
      *target= NULL_NMBRSTRING;
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k0c: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
    }
  } else {
    if (sourceLength) {
      *target=poolMalloc((sourceLength + 1) * (long)(sizeof(nmbrString)));


      nmbrCpy(*target,source);
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k0d: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
    } else {

if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k0e: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
    }
  }

if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k1: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
  nmbrTempAlloc(0);

}



nmbrString *nmbrCat(nmbrString *string1,...)
  static final long  M_MAX_CAT_ARGS =D.M_MAX_CAT_ARGS;
{
  va_list ap;
  nmbrString *arg[M_MAX_CAT_ARGS];
  long argLength[M_MAX_CAT_ARGS];
  int numArgs=1;
  int i;
  long j;
  nmbrString *ptr;
  arg[0]=string1;

  va_start(ap,string1);
  while ((arg[numArgs++]=va_arg(ap,nmbrString *)))

    if (numArgs>=M_MAX_CAT_ARGS-1) {
      printf("*** FATAL ERROR ***  Too many cat() arguments\n");
#if __STDC__
      fflush(stdout);
#endif
      bug(1369);
    }
  va_end(ap);

  numArgs--;


  j = 0;
  for (i = 0; i < numArgs; i++) {
    argLength[i]=nmbrLen(arg[i]);
    j=j+argLength[i];
  }

  ptr=nmbrTempAlloc(j+1);

  j = 0;
  for (i = 0; i < numArgs; i++) {
    nmbrCpy(ptr+j,arg[i]);
    j=j+argLength[i];
  }
  return (ptr);

}




long nmbrLen(nmbrString *s)
{

  return (((long)(((long *)s)[-1] - (long)(sizeof(nmbrString))))
              / (long)(sizeof(nmbrString)));
}



long nmbrAllocLen(nmbrString *s)
{

  return (((long)(((long *)s)[-2] - (long)(sizeof(nmbrString))))
              / (long)(sizeof(nmbrString)));
}



void nmbrZapLen(nmbrString *s, long length) {
  if (((long *)s)[-3] != -1) {

    poolTotalFree = poolTotalFree + ((long *)s)[-1]
        - (length + 1) * (long)(sizeof(nmbrString));
  }
  ((long *)s)[-1] = (length + 1) * (long)(sizeof(nmbrString));
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("l: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
}




void nmbrCpy(nmbrString *s,nmbrString *t)
{
  long i;
  i = 0;
  while (t[i] != -1) {
    s[i] = t[i];
    i++;
  }
  s[i] = t[i];
}





void nmbrNCpy(nmbrString *s,nmbrString *t,long n)
{
  long i;
  i = 0;
  while (t[i] != -1) {
    if (i >= n) break;
    s[i] = t[i];
    i++;
  }
  s[i] = t[i];
}



int nmbrEq(nmbrString *s,nmbrString *t)
{
  long i;
  if (nmbrLen(s) != nmbrLen(t)) return 0;
  for (i = 0; s[i] == t[i]; i++)
    if (s[i] == -1)
      return 1;
  return 0;
}



nmbrString *nmbrSeg(nmbrString *sin, long start, long stop)
{
  nmbrString *sout;
  long length;
  if (start < 1) start = 1;
  if (stop < 1) stop = 0;
  length=stop - start + 1;
  if (length < 0) length = 0;
  sout = nmbrTempAlloc(length + 1);
  nmbrNCpy(sout, sin + start - 1, length);
  sout[length] = *NULL_NMBRSTRING;
  return (sout);
}


nmbrString *nmbrMid(nmbrString *sin, long start, long length)
{
  nmbrString *sout;
  if (start < 1) start = 1;
  if (length < 0) length = 0;
  sout = nmbrTempAlloc(length + 1);
  nmbrNCpy(sout, sin + start - 1, length);
  sout[length] = *NULL_NMBRSTRING;
  return (sout);
}


nmbrString *nmbrLeft(nmbrString *sin,long n)
{
  nmbrString *sout;
  if (n < 0) n = 0;
  sout=nmbrTempAlloc(n + 1);
  nmbrNCpy(sout, sin, n);
  sout[n] = *NULL_NMBRSTRING;
  return (sout);
}


nmbrString *nmbrRight(nmbrString *sin,long n)
{

  nmbrString *sout;
  long i;
  if (n < 1) n = 1;
  i = nmbrLen(sin);
  if (n > i) return (NULL_NMBRSTRING);
  sout = nmbrTempAlloc(i - n + 2);
  nmbrCpy(sout, &sin[n - 1]);
  return (sout);
}



nmbrString *nmbrSpace(long n)
{
  nmbrString *sout;
  long j = 0;
  if (n < 0) bug(1327);
  sout = nmbrTempAlloc(n + 1);
  while (j < n) {

    sout[j] = 0;
    j++;
  }
  sout[j] = *NULL_NMBRSTRING;
  return (sout);
}


long nmbrInstr(long start_position,nmbrString *string1,
  nmbrString *string2)
{
   long ls1, ls2, i, j;
   if (start_position < 1) start_position = 1;
   ls1 = nmbrLen(string1);
   ls2 = nmbrLen(string2);
   for (i = start_position - 1; i <= ls1 - ls2; i++) {
     for (j = 0; j < ls2; j++) {
       if (string1[i+j] != string2[j])
         break;
     }
     if (j == ls2) return (i+1);
   }
   return (0);

}



long nmbrRevInstr(long start_position,nmbrString *string1,
    nmbrString *string2)
{
   long ls1, ls2;
   nmbrString *tmp = NULL_NMBRSTRING;
   ls1 = nmbrLen(string1);
   ls2 = nmbrLen(string2);
   if (start_position > ls1 - ls2 + 1) start_position = ls1 - ls2 + 2;
   if (start_position<1) return 0;
   while (!nmbrEq(string2, nmbrMid(string1, start_position, ls2))) {
     start_position--;
     nmbrLet(&tmp, NULL_NMBRSTRING);

     if (start_position < 1) return 0;
   }
   return (start_position);
}



vstring nmbrCvtMToVString(nmbrString *s)
{
  long i, j, outputLen, mstrLen;
  vstring tmpStr = "";
  vstring ptr;
  vstring ptr2;

  long saveTempAllocStack;
  saveTempAllocStack = g_startTempAllocStack;
  g_startTempAllocStack = g_tempAllocStackTop;

  mstrLen = nmbrLen(s);

  outputLen = -1;
  for (i = 0; i < mstrLen; i++) {
    outputLen = outputLen + (long)strlen(g_MathToken[s[i]].tokenName) + 1;
  }
  let(&tmpStr, space(outputLen));

  ptr = tmpStr;
  for (i = 0; i < mstrLen; i++) {
    ptr2 = g_MathToken[s[i]].tokenName;
    j = (long)strlen(ptr2);
    memcpy(ptr, ptr2, (size_t)j);
    ptr = ptr + j + 1;
  }

  g_startTempAllocStack = saveTempAllocStack;
  if (tmpStr[0]) makeTempAlloc(tmpStr);
  return (tmpStr);
}




vstring nmbrCvtRToVString(nmbrString *proof,

    flag explicitTargets,
    long statemNum)
{
  long i, j, plen, maxLabelLen, maxLocalLen, step, stmt;
  long maxTargetLabelLen;
  vstring proofStr = "";
  vstring tmpStr = "";
  vstring ptr;
  nmbrString *localLabels = NULL_NMBRSTRING;
  nmbrString *localLabelNames = NULL_NMBRSTRING;
  long nextLocLabNum = 1;
  void *voidPtr;

  nmbrString *targetHyps = NULL_NMBRSTRING;

  long saveTempAllocStack;
  long nmbrSaveTempAllocStack;
  saveTempAllocStack = g_startTempAllocStack;
  g_startTempAllocStack = g_tempAllocStackTop;
  nmbrSaveTempAllocStack = g_nmbrStartTempAllocStack;

  g_nmbrStartTempAllocStack = g_nmbrTempAllocStackTop;

  plen = nmbrLen(proof);


  if (explicitTargets == 1) {

    if (statemNum <= 0) bug(1388);
    nmbrLet(&targetHyps, nmbrGetTargetHyp(proof, statemNum));
  }


  maxLocalLen = 0;
  i = plen;
  while (i) {
    i = i / 10;
    maxLocalLen++;
  }



  maxLabelLen = 0;
  maxTargetLabelLen = 0;
  for (step = 0; step < plen; step++) {
    stmt = proof[step];
    if (stmt <= -1000) {
      stmt = -1000 - stmt;
      if (!nmbrElementIn(1, localLabels, stmt)) {
        nmbrLet(&localLabels, nmbrAddElement(localLabels, stmt));
      }
    } else {


      if (stmt < 1 || stmt > g_statements) {
        maxLabelLen = 100;
        maxTargetLabelLen = 100;
        continue;
      }

      if (stmt > 0) {
        if ((signed)(strlen(g_Statement[stmt].labelName)) > maxLabelLen) {
          maxLabelLen = (long)strlen(g_Statement[stmt].labelName);
        }
      }
    }


    if (explicitTargets == 1) {

      stmt = targetHyps[step];
      if (stmt <= 0) bug(1390);
      if ((signed)(strlen(g_Statement[stmt].labelName)) > maxTargetLabelLen) {
        maxTargetLabelLen = (long)strlen(g_Statement[stmt].labelName);
      }
    }

  }

  nmbrLet(&localLabelNames, nmbrSpace(plen));


  let(&proofStr, space(plen * (2 + maxLabelLen
      + ((explicitTargets == 1) ? maxTargetLabelLen + 1 : 0)

      + maxLocalLen)));
  ptr = proofStr;
  for (step = 0; step < plen; step++) {
    stmt = proof[step];
    if (stmt < 0) {
      if (stmt <= -1000) {
        stmt = -1000 - stmt;

        let(&tmpStr, cat(


            ((explicitTargets == 1) ? g_Statement[targetHyps[step]].labelName : ""),
            ((explicitTargets == 1) ? "=" : ""),

            str((double)(localLabelNames[stmt])), " ", NULL));


      } else if (stmt != -(long)'?') {
        let(&tmpStr, cat("??", str((double)stmt), " ", NULL));

      } else {
        if (stmt != -(long)'?') bug(1391);
        let(&tmpStr, cat(


            ((explicitTargets == 1) ? g_Statement[targetHyps[step]].labelName : ""),
            ((explicitTargets == 1) ? "=" : ""),

            chr(-stmt), " ", NULL));
      }


    } else if (stmt < 1 || stmt > g_statements) {
      let(&tmpStr, cat("??", str((double)stmt), " ", NULL));

    } else {
      let(&tmpStr,"");
      if (nmbrElementIn(1, localLabels, step)) {

        let(&tmpStr, str((double)nextLocLabNum));
        while (1) {
          voidPtr = (void *)bsearch(tmpStr,
              g_allLabelKeyBase, (size_t)g_numAllLabelKeys,
              sizeof(long), labelSrchCmp);
          if (!voidPtr) break;
          nextLocLabNum++;
          let(&tmpStr, str((double)nextLocLabNum));
        }
        localLabelNames[step] = nextLocLabNum;
        let(&tmpStr, cat(tmpStr, ":", NULL));
        nextLocLabNum++;
      }
      let(&tmpStr, cat(tmpStr,


          ((explicitTargets == 1) ? g_Statement[targetHyps[step]].labelName : ""),
          ((explicitTargets == 1) ? "=" : ""),

          g_Statement[stmt].labelName, " ", NULL));
    }
    j = (long)strlen(tmpStr);
    memcpy(ptr, tmpStr, (size_t)j);
    ptr = ptr + j;
  }

  if (ptr - proofStr) {

    let(&proofStr, left(proofStr, ptr - proofStr - 1));
  } else {
    let(&proofStr, "");
  }
  let(&tmpStr, "");
  nmbrLet(&localLabels, NULL_NMBRSTRING);
  nmbrLet(&localLabelNames, NULL_NMBRSTRING);

  g_startTempAllocStack = saveTempAllocStack;
  g_nmbrStartTempAllocStack = nmbrSaveTempAllocStack;
  if (proofStr[0]) makeTempAlloc(proofStr);
  return (proofStr);
}


nmbrString *nmbrGetProofStepNumbs(nmbrString *reason)
{
  nmbrString *stepNumbs = NULL_NMBRSTRING;
  long rlen, start, end, i, step;

  rlen = nmbrLen(reason);
  nmbrLet(&stepNumbs,nmbrSpace(rlen));
  if (!rlen) return (stepNumbs);
  if (reason[1] == -(long)'=') {

    start = 2;
    if (rlen == 3) {
      end = rlen;
    } else {
      end = rlen - 1;
    }
  } else {
    start = 1;
    end = rlen;
  }
  step = 0;
  for (i = start; i < end; i++) {
    if (i == 0) {
      step++;
      stepNumbs[0] = step;
      continue;
    }
    if (reason[i] < 0 && reason[i] != -(long)'?') continue;
    if (reason[i - 1] == -(long)'('
        || reason[i - 1] == -(long)'{'
        || reason[i - 1] == -(long)'=') {
      step++;
      stepNumbs[i] = step;
    }
  }
  return (stepNumbs);
}


vstring nmbrCvtAnyToVString(nmbrString *s)
{
  long i;
  vstring tmpStr = "";

  long saveTempAllocStack;
  saveTempAllocStack = g_startTempAllocStack;
  g_startTempAllocStack = g_tempAllocStackTop;

  for (i = 1; i <= nmbrLen(s); i++) {
    let(&tmpStr,cat(tmpStr," ", str((double)(s[i-1])),NULL));
  }

  g_startTempAllocStack = saveTempAllocStack;
  if (tmpStr[0]) makeTempAlloc(tmpStr);
  return (tmpStr);
}



nmbrString *nmbrExtractVars(nmbrString *m)
{
  long i, j, length;
  nmbrString *v;
  length = nmbrLen(m);
  v=nmbrTempAlloc(length + 1);
  v[0] = *NULL_NMBRSTRING;
  j = 0;
  for (i = 0; i < length; i++) {

    if (m[i] < 0 || m[i] > g_mathTokens) bug(1328);
    if (g_MathToken[m[i]].tokenType == (char)var_) {
      if (!nmbrElementIn(1, v, m[i])) {
        v[j] = m[i];
        j++;
        v[j] = *NULL_NMBRSTRING;
      }
    }
  }
  nmbrZapLen(v, j);
db2=db2-(length-nmbrLen(v))*(long)(sizeof(nmbrString));
  return v;
}


long nmbrElementIn(long start, nmbrString *g, long element)
{
  long i = start - 1;
  while (g[i] != -1) {
    if (g[i] == element) return(i + 1);
    i++;
  }
  return(0);
}



nmbrString *nmbrAddElement(nmbrString *g, long element)
{
  long length;
  nmbrString *v;
  length = nmbrLen(g);
  v = nmbrTempAlloc(length + 2);
  nmbrCpy(v, g);
  v[length] = element;
  v[length + 1] = *NULL_NMBRSTRING;
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("bbg2: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
  return(v);
}


nmbrString *nmbrUnion(nmbrString *m1, nmbrString *m2)
{
  long i,j,len1,len2;
  nmbrString *v;
  len1 = nmbrLen(m1);
  len2 = nmbrLen(m2);
  v=nmbrTempAlloc(len1+len2+1);
  nmbrCpy(v,m1);
  nmbrZapLen(v, len1);
  j = 0;
  for (i = 0; i < len2; i++) {
    if (!nmbrElementIn(1, v, m2[i])) {
      nmbrZapLen(v, len1 + j + 1);
      v[len1 + j] = m2[i];
      j++;
      v[len1 + j] = *NULL_NMBRSTRING;
    }
  }
  v[len1 + j] = *NULL_NMBRSTRING;
  nmbrZapLen(v, len1 + j);
db2=db2-(len1+len2-nmbrLen(v))*(long)(sizeof(nmbrString));
  return(v);
}


nmbrString *nmbrIntersection(nmbrString *m1,nmbrString *m2)
{
  long i,j,len2;
  nmbrString *v;
  len2 = nmbrLen(m2);
  v=nmbrTempAlloc(len2+1);
  j = 0;
  for (i = 0; i < len2; i++) {
    if (nmbrElementIn(1,m1,m2[i])) {
      v[j] = m2[i];
      j++;
    }
  }

  v[j] = *NULL_NMBRSTRING;
  nmbrZapLen(v, j);
db2=db2-(len2-nmbrLen(v))*(long)(sizeof(nmbrString));
  return v;
}


nmbrString *nmbrSetMinus(nmbrString *m1,nmbrString *m2)
{
  long i,j,len1;
  nmbrString *v;
  len1 = nmbrLen(m1);
  v=nmbrTempAlloc(len1+1);
  j = 0;
  for (i = 0; i < len1; i++) {
    if (!nmbrElementIn(1,m2,m1[i])) {
      v[j] = m1[i];
      j++;
    }
  }

  v[j] = *NULL_NMBRSTRING;
  nmbrZapLen(v, j);
db2=db2-(len1-nmbrLen(v))*(long)(sizeof(nmbrString));
  return v;
}


long nmbrGetSubproofLen(nmbrString *proof, long step)
{
  long stmt, hyps, pos, i;
  char type;

  if (step < 0) bug(1329);
  stmt = proof[step];
  if (stmt < 0) return (1);
  type = g_Statement[stmt].type;
  if (type == f_ || type == e_) return (1);
  hyps = g_Statement[stmt].numReqHyp;
  pos = step - 1;
  for (i = 0; i < hyps; i++) {
    pos = pos - nmbrGetSubproofLen(proof, pos);
  }
  return (step - pos);
}




nmbrString *nmbrSquishProof(nmbrString *proof)
{
  nmbrString *newProof = NULL_NMBRSTRING;
  nmbrString *dummyProof = NULL_NMBRSTRING;
  nmbrString *subProof = NULL_NMBRSTRING;
  long step, dummyStep, subPrfLen, matchStep, plen;
  flag foundFlag;

  nmbrLet(&newProof,proof);
  plen = nmbrLen(newProof);
  dummyStep = 0;
  nmbrLet(&dummyProof, newProof);
  for (step = 0; step < plen; step++) {
    subPrfLen = nmbrGetSubproofLen(dummyProof, dummyStep);
    if (subPrfLen <= 1) {
      dummyStep++;
      continue;
    }
    nmbrLet(&subProof, nmbrSeg(dummyProof, dummyStep - subPrfLen + 2,
        dummyStep + 1));
    matchStep = step + 1;
    foundFlag = 0;
    while (1) {
      matchStep = nmbrInstr(matchStep + 1, newProof, subProof);
      if (!matchStep) break;
      foundFlag = 1;

      nmbrLet(&newProof,
            nmbrCat(nmbrAddElement(nmbrLeft(newProof, matchStep - 1),
            -1000 - step), nmbrRight(newProof, matchStep + subPrfLen), NULL));
      matchStep = matchStep - subPrfLen + 1;
    }
    if (foundFlag) {
      plen = nmbrLen(newProof);


      dummyStep = dummyStep + 1 - subPrfLen;
      nmbrLet(&dummyProof,
          nmbrCat(nmbrAddElement(nmbrLeft(dummyProof, dummyStep),
          -1000 - step), nmbrRight(newProof, step + 2), NULL));
    }
    dummyStep++;
  }
  nmbrLet(&subProof, NULL_NMBRSTRING);
  nmbrLet(&dummyProof, NULL_NMBRSTRING);
  nmbrMakeTempAlloc(newProof);
  return (newProof);
}


nmbrString *nmbrUnsquishProof(nmbrString *proof)
{
  nmbrString *newProof = NULL_NMBRSTRING;
  nmbrString *subProof = NULL_NMBRSTRING;
  long step, plen, subPrfLen, stmt;

  nmbrLet(&newProof, proof);
  plen = nmbrLen(newProof);
  for (step = plen - 1; step >= 0; step--) {
    stmt = newProof[step];
    if (stmt > -1000) continue;

    stmt = -1000 - stmt;
    subPrfLen = nmbrGetSubproofLen(newProof, stmt);
    nmbrLet(&newProof, nmbrCat(nmbrLeft(newProof, step),
        nmbrSeg(newProof, stmt - subPrfLen + 2, stmt + 1),
        nmbrRight(newProof, step + 2), NULL));
    step = step + subPrfLen - 1;
  }
  nmbrLet(&subProof, NULL_NMBRSTRING);
  nmbrMakeTempAlloc(newProof);
  return (newProof);
}


nmbrString *nmbrGetIndentation(nmbrString *proof,
  long startingLevel)
{
  long plen, stmt, pos, splen, hyps, i, j;
  char type;
  nmbrString *indentationLevel = NULL_NMBRSTRING;
  nmbrString *subProof = NULL_NMBRSTRING;
  nmbrString *nmbrTmp = NULL_NMBRSTRING;

  plen = nmbrLen(proof);
  stmt = proof[plen - 1];
  nmbrLet(&indentationLevel, nmbrSpace(plen));
  indentationLevel[plen - 1] = startingLevel;
  if (stmt < 0) {
    if (plen != 1) bug(1330);
    nmbrMakeTempAlloc(indentationLevel);
    return (indentationLevel);
  }
  type = g_Statement[stmt].type;
  if (type == f_ || type == e_) {
    if (plen != 1) bug(1331);
    nmbrMakeTempAlloc(indentationLevel);
    return (indentationLevel);
  }

  if (type != a_ && type != p_) bug(1332);
  hyps = g_Statement[stmt].numReqHyp;
  pos = plen - 2;
  for (i = 0; i < hyps; i++) {
    splen = nmbrGetSubproofLen(proof, pos);
    nmbrLet(&subProof, nmbrSeg(proof, pos - splen + 2, pos + 1));
    nmbrLet(&nmbrTmp, nmbrGetIndentation(subProof, startingLevel + 1));
    for (j = 0; j < splen; j++) {
      indentationLevel[j + pos - splen + 1] = nmbrTmp[j];
    }
    pos = pos - splen;
  }
  if (pos != -1) bug (333);

  nmbrLet(&subProof,NULL_NMBRSTRING);
  nmbrLet(&nmbrTmp, NULL_NMBRSTRING);
  nmbrMakeTempAlloc(indentationLevel);
  return (indentationLevel);
}


nmbrString *nmbrGetEssential(nmbrString *proof)
{
  long plen, stmt, pos, splen, hyps, i, j;
  char type;
  nmbrString *essentialFlags = NULL_NMBRSTRING;
  nmbrString *subProof = NULL_NMBRSTRING;
  nmbrString *nmbrTmp = NULL_NMBRSTRING;
  nmbrString *nmbrTmpPtr2;

  plen = nmbrLen(proof);
  stmt = proof[plen - 1];
  nmbrLet(&essentialFlags, nmbrSpace(plen));
  essentialFlags[plen - 1] = 1;
  if (stmt < 0) {
    if (plen != 1) bug(1334);
    if (stmt != -(long)'?' && stmt > -1000) bug(1335);
    nmbrMakeTempAlloc(essentialFlags);
    return (essentialFlags);
  }
  type = g_Statement[stmt].type;
  if (type == f_ || type == e_) {
    if (plen != 1) bug(1336);
    nmbrMakeTempAlloc(essentialFlags);
    return (essentialFlags);
  }

  if (type != a_ && type != p_) bug(1337);
  hyps = g_Statement[stmt].numReqHyp;
  pos = plen - 2;
  nmbrTmpPtr2 = g_Statement[stmt].reqHypList;
  for (i = 0; i < hyps; i++) {
    splen = nmbrGetSubproofLen(proof, pos);
    if (g_Statement[nmbrTmpPtr2[hyps - i - 1]].type == e_) {
      nmbrLet(&subProof, nmbrSeg(proof, pos - splen + 2, pos + 1));
      nmbrLet(&nmbrTmp, nmbrGetEssential(subProof));
      for (j = 0; j < splen; j++) {
        essentialFlags[j + pos - splen + 1] = nmbrTmp[j];
      }
    }
    pos = pos - splen;
  }
  if (pos != -1) bug (1338);

  nmbrLet(&subProof,NULL_NMBRSTRING);
  nmbrLet(&nmbrTmp, NULL_NMBRSTRING);
  nmbrMakeTempAlloc(essentialFlags);
  return (essentialFlags);
}


nmbrString *nmbrGetTargetHyp(nmbrString *proof, long statemNum)
{
  long plen, stmt, pos, splen, hyps, i, j;
  char type;
  nmbrString *targetHyp = NULL_NMBRSTRING;
  nmbrString *subProof = NULL_NMBRSTRING;
  nmbrString *nmbrTmp = NULL_NMBRSTRING;

  plen = nmbrLen(proof);
  stmt = proof[plen - 1];
  nmbrLet(&targetHyp, nmbrSpace(plen));
  if (statemNum) {
    targetHyp[plen - 1] = statemNum;
  }
  if (stmt < 0) {
    if (plen != 1) bug(1339);
    if (stmt != -(long)'?') bug(1340);
    nmbrMakeTempAlloc(targetHyp);
    return (targetHyp);
  }
  type = g_Statement[stmt].type;
  if (type == f_ || type == e_) {
    if (plen != 1) bug(1341);
    nmbrMakeTempAlloc(targetHyp);
    return (targetHyp);
  }

  if (type != a_ && type != p_) bug(1342);
  hyps = g_Statement[stmt].numReqHyp;
  pos = plen - 2;
  for (i = 0; i < hyps; i++) {
    splen = nmbrGetSubproofLen(proof, pos);
    if (splen > 1) {
      nmbrLet(&subProof, nmbrSeg(proof, pos - splen + 2, pos + 1));
      nmbrLet(&nmbrTmp, nmbrGetTargetHyp(subProof,
          g_Statement[stmt].reqHypList[hyps - i - 1]));
      for (j = 0; j < splen; j++) {
        targetHyp[j + pos - splen + 1] = nmbrTmp[j];
      }
    } else {

      targetHyp[pos] = g_Statement[stmt].reqHypList[hyps - i - 1];
    }
    pos = pos - splen;
  }
  if (pos != -1) bug (343);

  nmbrLet(&subProof,NULL_NMBRSTRING);
  nmbrLet(&nmbrTmp, NULL_NMBRSTRING);
  nmbrMakeTempAlloc(targetHyp);
  return (targetHyp);
}


vstring compressProof(nmbrString *proof, long statemNum,
    flag oldCompressionAlgorithm)
{
  vstring output = "";
  long outputLen;
  long outputAllocated;
  nmbrString *saveProof = NULL_NMBRSTRING;
  nmbrString *labelList = NULL_NMBRSTRING;
  nmbrString *hypList = NULL_NMBRSTRING;
  nmbrString *assertionList = NULL_NMBRSTRING;
  nmbrString *localList = NULL_NMBRSTRING;
  nmbrString *localLabelFlags = NULL_NMBRSTRING;
  long hypLabels, assertionLabels, localLabels;
  long plen, step, stmt, labelLen, lab, numchrs;

  long i, j, k;


  long lettersLen, digitsLen;
  static char *digits = "0123456789";
  static char *letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  static char labelChar = ':';


  nmbrString *explList = NULL_NMBRSTRING;
  long explLabels;
  nmbrString *explRefCount = NULL_NMBRSTRING;
  nmbrString *labelRefCount = NULL_NMBRSTRING;
  long maxExplRefCount;
  nmbrString *explComprLen = NULL_NMBRSTRING;
  long explSortPosition;
  long maxExplComprLen;
  vstring explUsedFlag = "";
  nmbrString *explLabelLen = NULL_NMBRSTRING;
  nmbrString *newExplList = NULL_NMBRSTRING;
  long newExplPosition;
  long indentation;
  long explOffset;
  long explUnassignedCount;
  nmbrString *explWorth = NULL_NMBRSTRING;
  long explWidth;
  vstring explIncluded = "";



  letters = "ABCDEFGHIJKLMNOPQRST";
  digits = "UVWXY";
  labelChar = 'Z';

  lettersLen = (long)strlen(letters);
  digitsLen = (long)strlen(digits);

  nmbrLet(&saveProof, proof);

  if (g_Statement[statemNum].type != (char)p_) bug(1344);
  plen = nmbrLen(saveProof);


  nmbrLet(&labelList, g_Statement[statemNum].reqHypList);



  nmbrLet(&labelList, nmbrUnion(labelList, saveProof));


  labelLen = nmbrLen(labelList);
  nmbrLet(&hypList, nmbrSpace(labelLen));
  nmbrLet(&assertionList, nmbrSpace(labelLen));
  nmbrLet(&localLabelFlags, nmbrSpace(plen));
  hypLabels = 0;
  assertionLabels = 0;
  localLabels = 0;
  for (lab = 0; lab < labelLen; lab++) {
    stmt = labelList[lab];
    if (stmt < 0) {
      if (stmt <= -1000) {
        if (-1000 - stmt >= plen) bug(345);
        localLabelFlags[-1000 - stmt] = 1;
        localLabels++;
      } else {
        if (stmt != -(long)'?') bug(1346);
      }
    } else {
      if (g_Statement[stmt].type != (char)a_ &&
          g_Statement[stmt].type != (char)p_) {
        hypList[hypLabels] = stmt;
        hypLabels++;
      } else {
        assertionList[assertionLabels] = stmt;
        assertionLabels++;
      }
    }
  }
  nmbrLet(&hypList, nmbrLeft(hypList, hypLabels));
  nmbrLet(&assertionList, nmbrLeft(assertionList, assertionLabels));


  nmbrLet(&localList, nmbrSpace(localLabels));
  lab = 0;
  for (step = 0; step < plen; step++) {
    if (localLabelFlags[step]) {
      localList[lab] = -1000 - step;
      lab++;
    }
  }
  if (lab != localLabels) bug(1347);


  if (oldCompressionAlgorithm) goto OLD_ALGORITHM;







  nmbrLet(&explList, nmbrCat(

      nmbrRight(hypList, g_Statement[statemNum].numReqHyp + 1),

      assertionList, NULL));
  explLabels = nmbrLen(explList);


  nmbrLet(&explRefCount, nmbrSpace(explLabels));



  nmbrLet(&labelRefCount, nmbrSpace(statemNum));
  for (step = 0; step < plen; step++) {
    if (saveProof[step] > 0) {
      if (saveProof[step] < statemNum) {
        labelRefCount[saveProof[step]]++;
      } else {
        bug(1380);
      }
    }
  }
  maxExplRefCount = 0;

  for (i = 0; i < explLabels; i++) {
    explRefCount[i] = labelRefCount[explList[i]];
    if (explRefCount[i] <= 0) bug(1381);
    if (explRefCount[i] > maxExplRefCount) {
      maxExplRefCount = explRefCount[i];
    }
  }

  nmbrLet(&labelRefCount, NULL_NMBRSTRING);


  nmbrLet(&explComprLen, nmbrSpace(explLabels));
  explSortPosition = 0;
  maxExplComprLen = 0;

  for (i = maxExplRefCount; i >= 1; i--) {
    for (j = 0; j < explLabels; j++) {
      if (explRefCount[j] == i) {


        lab = g_Statement[statemNum].numReqHyp + explSortPosition;

        numchrs = 1;
        k = lab / lettersLen;
        while (1) {
          if (!k) break;
          numchrs++;
          k = (k - 1) / digitsLen;
        }

        explComprLen[j] = numchrs;
        if (numchrs > maxExplComprLen) {
          maxExplComprLen = numchrs;
        }
        explSortPosition++;
      }
    }
  }

  let(&explUsedFlag, string(explLabels, 'n'));
  nmbrLet(&explLabelLen, nmbrSpace(explLabels));

  for (i = 0; i < explLabels; i++) {
    stmt = explList[i];
    explLabelLen[i] = (long)(strlen(g_Statement[stmt].labelName)) + 1;

  }


  nmbrLet(&newExplList, nmbrSpace(explLabels));
  nmbrLet(&explWorth, nmbrSpace(explLabels));
  let(&explIncluded, string(explLabels, '?'));
  newExplPosition = 0;

  indentation =  2 + getSourceIndentation(statemNum);
  explOffset = 2;

  for (i = 1; i <= maxExplComprLen; i++) {
    explUnassignedCount = 0;

    for (j = 0; j < explLabels; j++) {
      if (explComprLen[j] == i) {
        if (explUsedFlag[j] == 'y') bug(1382);
        explWorth[j] = explLabelLen[j];
        explUnassignedCount++;
      } else {
        explWorth[j] = -1;
      }
    }
    while (explUnassignedCount > 0) {


      explWidth = g_screenWidth - indentation - explOffset + 1;


      j = knapsack01(explLabels ,
          explLabelLen ,
          explWorth ,
          explWidth ,
          explIncluded );
      if (j < 0) bug(1383);

      explUnassignedCount = 0;

      for (j = 0; j < explLabels; j++) {
        if (explIncluded[j] == 'y') {
          if (explComprLen[j] != i) bug(1384);
          newExplList[newExplPosition] = explList[j];
          newExplPosition++;
          explUsedFlag[j] = 'y';
          if (explWorth[j] == -1) bug(1385);
          explWorth[j] = -1;
          explOffset = explOffset + explLabelLen[j];
        } else {
          if (explComprLen[j] == i && explUsedFlag[j] == 'n') {
            explUnassignedCount++;
            if (explWorth[j] != explLabelLen[j]) bug(1386);
          }
        }
      }
      if (explUnassignedCount > 0) {
        explOffset = 0;
      }
    }
  }
  if (newExplPosition != explLabels) bug(1387);


  nmbrLet(&hypList, nmbrLeft(hypList, g_Statement[statemNum].numReqHyp));
  nmbrLet(&assertionList, newExplList);




 OLD_ALGORITHM:

  nmbrLet(&labelList, nmbrCat(hypList, assertionList, localList, NULL));


  outputLen = 0;
  static final long COMPR_INC =D.COMPR_INC;
  let(&output, space(COMPR_INC));
  outputAllocated = COMPR_INC;

  plen = nmbrLen(saveProof);
  for (step = 0; step < plen; step++) {

    stmt = saveProof[step];

    if (stmt == -(long)'?') {

      if (outputLen + 1 > outputAllocated) {

        let(&output, cat(output, space(outputLen + 1 - outputAllocated +
            COMPR_INC), NULL));
        outputAllocated = outputLen + 1 + COMPR_INC;


        if (output[outputAllocated - 1] == 0 ||
            output[outputAllocated] != 0) bug(1348);
      }
      output[outputLen] = '?';
      outputLen++;
      continue;
    }

    lab = nmbrElementIn(1, labelList, stmt);
    if (!lab) bug(1349);
    lab--;





    if (lab < 0) bug(1373);
    numchrs = 1;
    i = lab / lettersLen;
    while (1) {
      if (!i) break;
      numchrs++;
      i = (i - 1) / digitsLen;
    }


    if (outputLen + numchrs > outputAllocated) {

      let(&output, cat(output, space(outputLen + numchrs - outputAllocated +
          COMPR_INC), NULL));
      outputAllocated = outputLen + numchrs + COMPR_INC;


      if (output[outputAllocated - 1] == 0 ||
          output[outputAllocated] != 0) bug(1350);
    }
    outputLen = outputLen + numchrs;




    j = lab + 1;
    i = 1;
    output[outputLen - i] = letters[(j - 1) % lettersLen];
    j = (j - 1) / lettersLen;
    while (1) {
      if (!j) break;
      i++;
      output[outputLen - i] = digits[(j - 1) % digitsLen];
      j = (j - 1) / digitsLen;
    }
    if (i != numchrs) bug(1374);




    if (!localLabelFlags[step]) continue;
    if (outputLen + 1 > outputAllocated) {

      let(&output, cat(output, space(outputLen + 1 - outputAllocated +
          COMPR_INC), NULL));
      outputAllocated = outputLen + 1 + COMPR_INC;


      if (output[outputAllocated - 1] == 0 ||
          output[outputAllocated] != 0) bug(1352);
    }
    output[outputLen] = labelChar;
    outputLen++;

  }


  let(&output, cat("( ", nmbrCvtRToVString(nmbrCat(

      nmbrRight(hypList, g_Statement[statemNum].numReqHyp + 1),
      assertionList, NULL),

                0,
                0 ),
      " ) ", left(output, outputLen), NULL));

  nmbrLet(&saveProof, NULL_NMBRSTRING);
  nmbrLet(&labelList, NULL_NMBRSTRING);
  nmbrLet(&hypList, NULL_NMBRSTRING);
  nmbrLet(&assertionList, NULL_NMBRSTRING);
  nmbrLet(&localList, NULL_NMBRSTRING);
  nmbrLet(&localLabelFlags, NULL_NMBRSTRING);



  nmbrLet(&explList, NULL_NMBRSTRING);
  nmbrLet(&explRefCount, NULL_NMBRSTRING);
  nmbrLet(&labelRefCount, NULL_NMBRSTRING);
  nmbrLet(&explComprLen, NULL_NMBRSTRING);
  let(&explUsedFlag, "");
  nmbrLet(&explLabelLen, NULL_NMBRSTRING);
  nmbrLet(&newExplList, NULL_NMBRSTRING);
  nmbrLet(&explWorth, NULL_NMBRSTRING);
  let(&explIncluded, "");

  makeTempAlloc(output);
  return(output);
}




long compressedProofSize(nmbrString *proof, long statemNum) {
  vstring tmpStr = "";
  nmbrString *tmpNmbr = NULL_NMBRSTRING;
  long bytes;
  nmbrLet(&tmpNmbr, nmbrSquishProof(proof));
  let(&tmpStr, compressProof(tmpNmbr,
          statemNum,
          0
          ));
  bytes = (long)strlen(tmpStr);

  let(&tmpStr, "");
  nmbrLet(&tmpNmbr, NULL_NMBRSTRING);
  return bytes;
}







long g_pntrTempAllocStackTop = 0;
long g_pntrStartTempAllocStack = 0;
pntrString *pntrTempAllocStack[M_MAX_ALLOC_STACK];


pntrString *pntrTempAlloc(long size)

{




  if (size) {
    if (g_pntrTempAllocStackTop>=(M_MAX_ALLOC_STACK-1))

      outOfMemory("#109 (pntrString stack array)");
    if (!(pntrTempAllocStack[g_pntrTempAllocStackTop++]=poolMalloc(size
        *(long)(sizeof(pntrString)))))

db2=db2+(size)*(long)(sizeof(pntrString));
    return (pntrTempAllocStack[g_pntrTempAllocStackTop-1]);
  } else {


    while(g_pntrTempAllocStackTop != g_pntrStartTempAllocStack) {
db2=db2-(pntrLen(pntrTempAllocStack[g_pntrTempAllocStackTop-1])+1)
                                              *(long)(sizeof(pntrString));
      poolFree(pntrTempAllocStack[--g_pntrTempAllocStackTop]);
    }

    g_pntrTempAllocStackTop=g_pntrStartTempAllocStack;
    return (0);
  }
}



void pntrMakeTempAlloc(pntrString *s)
{
    if (g_pntrTempAllocStackTop>=(M_MAX_ALLOC_STACK-1)) {
      printf(
      "*** FATAL ERROR ***  Temporary pntrString stack overflow in pntrMakeTempAlloc()\n");
#if __STDC__
      fflush(stdout);
#endif
      bug(1370);
    }
    if (s[0] != NULL) {
      pntrTempAllocStack[g_pntrTempAllocStackTop++] = s;
    }
db2=db2+(pntrLen(s)+1)*(long)(sizeof(pntrString));
db3=db3-(pntrLen(s)+1)*(long)(sizeof(pntrString));
}


void pntrLet(pntrString **target,pntrString *source)






{
  long targetLength,sourceLength;
  long targetAllocLen;
  long poolDiff;
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k0: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
  sourceLength=pntrLen(source);
  targetLength=pntrLen(*target);
  targetAllocLen=pntrAllocLen(*target);
if (targetLength) {

  db3 = db3 - (targetLength+1)*(long)(sizeof(pntrString));
}
if (sourceLength) {

  db3 = db3 + (sourceLength+1)*(long)(sizeof(pntrString));
}
  if (targetAllocLen) {
    if (sourceLength) {

      if (targetAllocLen >= sourceLength) {
        pntrCpy(*target,source);



        poolDiff = ((long *)(*target))[-1] - ((long *)source)[-1];
        ((long *)(*target))[-1] = ((long *)source)[-1];
        if (((long *)(*target))[-1] != ((long *)(*target))[-2]) {
          if (((long *)(*target))[-1] > ((long *)(*target))[-2]) bug(1359);
          if (((long *)(*target))[-3] == -1) {

            addToUsedPool(*target);
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k0aa: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
          } else {

            poolTotalFree = poolTotalFree + poolDiff;
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k0ab: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
          }
        } else {
          if (((long *)(*target))[-3] != -1) {


            poolTotalFree = poolTotalFree + poolDiff;
          }
        }


if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k0a: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
      } else {

        poolFree(*target);

        *target=poolMalloc((sourceLength + 1) * (long)(sizeof(pntrString)) * 2);

        pntrCpy(*target,source);



        poolDiff = ((long *)(*target))[-1] - ((long *)source)[-1];
        ((long *)(*target))[-1] = ((long *)source)[-1];

        if (((long *)(*target))[-1] != ((long *)(*target))[-2]) {
          if (((long *)(*target))[-1] > ((long *)(*target))[-2]) bug(1360);
          if (((long *)(*target))[-3] == -1) {

            addToUsedPool(*target);
          } else {

            poolTotalFree = poolTotalFree + poolDiff;
          }
        } else {
          if (((long *)(*target))[-3] != -1) {


            poolTotalFree = poolTotalFree + poolDiff;
          }
        }
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k0b: pool %ld stat %ld\n",poolTotalFree,i1+j1_);

      }

    } else {
      poolFree(*target);
      *target= NULL_PNTRSTRING;
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k0c: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
    }
  } else {
    if (sourceLength) {
      *target=poolMalloc((sourceLength + 1) * (long)(sizeof(pntrString)));


      pntrCpy(*target,source);
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k0d: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
    } else {

if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k0e: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
    }
  }

if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("k1: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
  pntrTempAlloc(0);

}



pntrString *pntrCat(pntrString *string1,...)
{
  va_list ap;
  pntrString *arg[M_MAX_CAT_ARGS];
  long argLength[M_MAX_CAT_ARGS];
  int numArgs=1;
  int i;
  long j;
  pntrString *ptr;
  arg[0]=string1;

  va_start(ap,string1);
  while ((arg[numArgs++]=va_arg(ap,pntrString *)))

    if (numArgs>=M_MAX_CAT_ARGS-1) {
      printf("*** FATAL ERROR ***  Too many cat() arguments\n");
#if __STDC__
      fflush(stdout);
#endif
      bug(1371);
    }
  va_end(ap);

  numArgs--;


  j = 0;
  for (i = 0; i < numArgs; i++) {
    argLength[i]=pntrLen(arg[i]);
    j=j+argLength[i];
  }

  ptr=pntrTempAlloc(j+1);

  j = 0;
  for (i = 0; i < numArgs; i++) {
    pntrCpy(ptr+j,arg[i]);
    j=j+argLength[i];
  }
  return (ptr);

}




long pntrLen(pntrString *s)
{

  return ((((long *)s)[-1] - (long)(sizeof(pntrString)))
      / (long)(sizeof(pntrString)));
}



long pntrAllocLen(pntrString *s)
{
  return ((((long *)s)[-2] - (long)(sizeof(pntrString)))
    / (long)(sizeof(pntrString)));
}



void pntrZapLen(pntrString *s, long length) {
  if (((long *)s)[-3] != -1) {

    poolTotalFree = poolTotalFree + ((long *)s)[-1]
        - (length + 1) * (long)(sizeof(pntrString));
  }
  ((long *)s)[-1] = (length + 1) * (long)(sizeof(pntrString));
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("l: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
}




void pntrCpy(pntrString *s, pntrString *t)
{
  long i;
  i = 0;
  while (t[i] != NULL) {
    s[i] = t[i];
    i++;
  }
  s[i] = t[i];
}





void pntrNCpy(pntrString *s,pntrString *t,long n)
{
  long i;
  i = 0;
  while (t[i] != NULL) {
    if (i >= n) break;
    s[i] = t[i];
    i++;
  }
  s[i] = t[i];
}



int pntrEq(pntrString *s,pntrString *t)
{
  long i;
  for (i = 0; s[i] == t[i]; i++)
    if (s[i] == NULL)
      return 1;
  return 0;
}



pntrString *pntrSeg(pntrString *sin, long start, long stop)
{
  pntrString *sout;
  long length;
  if (start < 1 ) start = 1;
  if (stop < 1 ) stop = 0;
  length = stop - start + 1;
  if (length < 0) length = 0;
  sout = pntrTempAlloc(length + 1);
  pntrNCpy(sout, sin + start - 1, length);
  sout[length] = *NULL_PNTRSTRING;
  return (sout);
}


pntrString *pntrMid(pntrString *sin, long start, long length)
{
  pntrString *sout;
  if (start < 1) start = 1;
  if (length < 0) length = 0;
  sout = pntrTempAlloc(length + 1);
  pntrNCpy(sout, sin + start-1, length);
  sout[length] = *NULL_PNTRSTRING;
  return (sout);
}


pntrString *pntrLeft(pntrString *sin,long n)
{
  pntrString *sout;
  if (n < 0) n = 0;
  sout=pntrTempAlloc(n+1);
  pntrNCpy(sout,sin,n);
  sout[n] = *NULL_PNTRSTRING;
  return (sout);
}


pntrString *pntrRight(pntrString *sin,long n)
{

  pntrString *sout;
  long i;
  if (n < 1) n = 1;
  i = pntrLen(sin);
  if (n > i) return (NULL_PNTRSTRING);
  sout = pntrTempAlloc(i - n + 2);
  pntrCpy(sout, &sin[n-1]);
  return (sout);
}




pntrString *pntrSpace(long n)
{
  pntrString *sout;
  long j = 0;
  if (n<0) bug(1360);
  sout=pntrTempAlloc(n+1);
  while (j<n) {

    sout[j] = "";
    j++;
  }
  sout[j] = *NULL_PNTRSTRING;
  return (sout);
}

pntrString *pntrNSpace(long n)
{
  pntrString *sout;
  long j = 0;
  if (n<0) bug(1361);
  sout=pntrTempAlloc(n+1);
  while (j<n) {

    sout[j] = NULL_NMBRSTRING;
    j++;
  }
  sout[j] = *NULL_PNTRSTRING;
  return (sout);
}

pntrString *pntrPSpace(long n)
{
  pntrString *sout;
  long j = 0;
  if (n<0) bug(1372);
  sout=pntrTempAlloc(n+1);
  while (j<n) {

    sout[j] = NULL_PNTRSTRING;
    j++;
  }
  sout[j] = *NULL_PNTRSTRING;
  return (sout);
}


long pntrInstr(long start_position,pntrString *string1,
  pntrString *string2)
{
   long ls1,ls2,i,j;
   if (start_position<1) start_position=1;
   ls1=pntrLen(string1);
   ls2=pntrLen(string2);
   for (i=start_position - 1; i <= ls1 - ls2; i++) {
     for (j = 0; j<ls2; j++) {
       if (string1[i+j] != string2[j])
         break;
     }
     if (j == ls2) return (i+1);
   }
   return (0);

}



long pntrRevInstr(long start_position,pntrString *string1,
    pntrString *string2)
{
   long ls1,ls2;
   pntrString *tmp = NULL_PNTRSTRING;
   ls1=pntrLen(string1);
   ls2=pntrLen(string2);
   if (start_position>ls1-ls2+1) start_position=ls1-ls2+2;
   if (start_position<1) return 0;
   while (!pntrEq(string2,pntrMid(string1,start_position,ls2))) {
     start_position--;
     pntrLet(&tmp,NULL_PNTRSTRING);

     if (start_position < 1) return 0;
   }
   return (start_position);
}



pntrString *pntrAddElement(pntrString *g)
{
  long length;
  pntrString *v;
  length = pntrLen(g);
  v = pntrTempAlloc(length + 2);
  pntrCpy(v, g);
  v[length] = "";
  v[length + 1] = *NULL_PNTRSTRING;
if(db9)getPoolStats(&i1,&j1_,&k1); if(db9)printf("bbg3: pool %ld stat %ld\n",poolTotalFree,i1+j1_);
  return(v);
}



pntrString *pntrAddGElement(pntrString *g)
{
  long length;
  pntrString *v;
  length = pntrLen(g);
  v = pntrTempAlloc(length + 2);
  pntrCpy(v, g);
  v[length] = NULL_PNTRSTRING;
  v[length + 1] = *NULL_PNTRSTRING;
  return(v);
}










long knapsack01(long items,
    long *size,
    long *worth,
    long maxSize,
    char *itemIncluded )
    {
  long witem, wsize, a, b;


  long **maxWorth;
  maxWorth = alloc2DMatrix((size_t)items + 1, (size_t)maxSize + 1);



  for (wsize = 0; wsize <= maxSize; wsize++) {
    maxWorth[0][wsize] = 0;
  }
  for (witem = 1; witem <= items; witem++) {
    for (wsize = 0; wsize <= maxSize; wsize++) {
      if (wsize >= size[witem - 1]) {

        a = maxWorth[witem - 1][wsize];
        b = maxWorth[witem - 1][wsize - size[witem - 1]] + worth[witem - 1];

        maxWorth[witem][wsize] = (a > b) ? a : b;
      } else {
        maxWorth[witem][wsize] = maxWorth[witem - 1][wsize];
      }
    }
  }


  wsize = maxSize;
  for (witem = items; witem > 0; witem--) {
    itemIncluded[witem - 1] = 'n';
    if (wsize > 0) {
      if (maxWorth[witem][wsize] != maxWorth[witem - 1][wsize]) {
        itemIncluded[witem - 1] = 'y';
        wsize = wsize - size[witem - 1];
      }
    }
  }

  a = maxWorth[items][maxSize];
  free2DMatrix(maxWorth, (size_t)items + 1 );
  return a;
}



long **alloc2DMatrix(size_t xsize, size_t ysize)
{
  long **matrix;
  long i;
  matrix = malloc(xsize * sizeof(long *));
  if (matrix == NULL) {
    fprintf(stderr,"?FATAL ERROR 1376 Out of memory\n");
    exit(1);
  }
  for (i = 0; i < (long)xsize; i++) {
    matrix[i] = malloc(ysize * sizeof(long));
    if (matrix[i] == NULL) {
      fprintf(stderr,"?FATAL ERROR 1377 Out of memory\n");
      exit(1);
    }
  }
  return matrix;
}



void free2DMatrix(long **matrix, size_t xsize )
{
  long i;
  for (i = (long)xsize - 1; i >= 0; i--) {
    if (matrix[i] == NULL) bug(1378);
    free(matrix[i]);
  }
  if (matrix == NULL) bug(1379);
  free(matrix);
  return;
}


long getSourceIndentation(long statemNum) {
  char *fbPtr;
  char *startLabel;
  long indentation = 0;

  fbPtr = g_Statement[statemNum].mathSectionPtr;
  if (fbPtr[0] == 0) return 0;
  startLabel = g_Statement[statemNum].labelSectionPtr;
  if (startLabel[0] == 0) return 0;
  while (1) {
    if (fbPtr <= startLabel) break;
    if (fbPtr[0] == '\n') break;
    if (fbPtr[0] == ' ') {
      indentation++;
    } else {
      indentation = 0;
    }
    fbPtr--;
  }
  return indentation;
}


vstring getDescription(long statemNum) {
  vstring description = "";
  long p1, p2;

  let(&description, space(g_Statement[statemNum].labelSectionLen));
  memcpy(description, g_Statement[statemNum].labelSectionPtr,
      (size_t)(g_Statement[statemNum].labelSectionLen));
  p1 = rinstr(description, "$(");
  p2 = rinstr(description, "$)");
  if (p1 == 0 || p2 == 0 || p2 < p1) {
    let(&description, "");
    return description;
  }
  let(&description, edit(seg(description, p1 + 2, p2 - 1),
      8 + 128 ));
  return description;


}



vstring getDescriptionAndLabel(long stmt) {
  vstring descriptionAndLabel = "";
  long p1, p2;
  flag dontUseComment = 0;

  let(&descriptionAndLabel, space(g_Statement[stmt].labelSectionLen));
  memcpy(descriptionAndLabel, g_Statement[stmt].labelSectionPtr,
      (size_t)(g_Statement[stmt].labelSectionLen));
  p1 = rinstr(descriptionAndLabel, "$(");
  p2 = rinstr(descriptionAndLabel, "$)");
  if (p1 == 0 || p2 == 0 || p2 < p1) {
    return descriptionAndLabel;
  }

  p1--;
  while (p1 != 0) {
    if (descriptionAndLabel[p1 - 1] != ' '
          && descriptionAndLabel[p1 - 1] != '\n') break;
    p1--;
  }
  let(&descriptionAndLabel, right(descriptionAndLabel, p1 + 1));


  if (instr(1, descriptionAndLabel, cat("\n", TINY_DECORATION, NULL)) != 0
      || instr(1, descriptionAndLabel, cat("\n", SMALL_DECORATION, NULL)) != 0
      || instr(1, descriptionAndLabel, cat("\n", BIG_DECORATION, NULL)) != 0
      || instr(1, descriptionAndLabel, cat("\n", HUGE_DECORATION, NULL)) != 0) {

    dontUseComment = 1;
  }

  if (instr(1, descriptionAndLabel, "$[") != 0) {

    dontUseComment = 1;
  }


  if (instr(1, descriptionAndLabel, "$j") != 0) {

    dontUseComment = 1;
  }


  if (dontUseComment == 1) {

    p2 = rinstr(descriptionAndLabel, "$)");
    if (p2 == 0) bug(1401);
    let(&descriptionAndLabel, right(descriptionAndLabel, p2 + 2));
  }

  return descriptionAndLabel;
}




flag getMarkupFlag(long statemNum, flag mode) {
  static char init = 0;
  static vstring commentSearchedFlags = "";
  static vstring proofFlags = "";
  static vstring usageFlags = "";
  vstring str1 = "";

  if (mode == RESET) {
    let(&commentSearchedFlags, "");
    let(&proofFlags, "");
    let(&usageFlags, "");
    init = 0;
    return 0;
  }

  if (init == 0) {
    init = 1;
    if (g_proofDiscouragedMarkup[0] == 0) {
      let(&g_proofDiscouragedMarkup, PROOF_DISCOURAGED_MARKUP);
    }
    if (g_usageDiscouragedMarkup[0] == 0) {
      let(&g_usageDiscouragedMarkup, USAGE_DISCOURAGED_MARKUP);
    }

    let(&commentSearchedFlags, string(g_statements + 1, 'N'));
    let(&proofFlags, space(g_statements + 1));
    let(&usageFlags, space(g_statements + 1));
  }

  if (statemNum < 1 || statemNum > g_statements) bug(1392);

  if (commentSearchedFlags[statemNum] == 'N') {
    if (g_Statement[statemNum].type == f_
        || g_Statement[statemNum].type == e_  ) {

      proofFlags[statemNum] = 'N';
      usageFlags[statemNum] = 'N';
    } else {
      if (g_Statement[statemNum].type != a_ && g_Statement[statemNum].type != p_) {
        bug(1393);
      }
      str1 = getDescription(statemNum);

      let(&str1, edit(str1, 4 + 8 + 16 + 128));
      if (instr(1, str1, g_proofDiscouragedMarkup)) {
        proofFlags[statemNum] = 'Y';
      } else {
        proofFlags[statemNum] = 'N';
      }
      if (instr(1, str1, g_usageDiscouragedMarkup)) {
        usageFlags[statemNum] = 'Y';
      } else {
        usageFlags[statemNum] = 'N';
      }
      let(&str1, "");
    }
    commentSearchedFlags[statemNum] = 'Y';
  }

  if (mode == PROOF_DISCOURAGED) return (proofFlags[statemNum] == 'Y') ? 1 : 0;
  if (mode == USAGE_DISCOURAGED) return (usageFlags[statemNum] == 'Y') ? 1 : 0;
  bug(1394);
  return 0;
}





vstring getContrib(long stmtNum, char mode) {


  static char init = 0;

  vstring contributor = "";
  vstring contribDate = "";
  vstring reviser = "";
  vstring reviseDate = "";
  vstring shortener = "";
  vstring shortenDate = "";
  vstring mostRecentDate = "";

  static vstring commentSearchedFlags = "";
  static pntrString *contributorList = NULL_PNTRSTRING;
  static pntrString *contribDateList = NULL_PNTRSTRING;
  static pntrString *reviserList = NULL_PNTRSTRING;
  static pntrString *reviseDateList = NULL_PNTRSTRING;
  static pntrString *shortenerList = NULL_PNTRSTRING;
  static pntrString *shortenDateList = NULL_PNTRSTRING;
  static pntrString *mostRecentDateList = NULL_PNTRSTRING;

  long cStart = 0, cMid = 0, cEnd = 0;
  long rStart = 0, rMid = 0, rEnd = 0;
  long sStart = 0, sMid = 0, sEnd = 0;
  long firstR = 0, firstS = 0;
  vstring description = "";
  vstring tmpDate0 = "";
  vstring tmpDate1 = "";
  vstring tmpDate2 = "";
  long stmt, p, dd, mmm, yyyy;
  flag errorCheckFlag = 0;
  flag err = 0;
  vstring returnStr = "";
  static final String CONTRIB_MATCH = D.CONTRIB_MATCH;
  static final String REVISE_MATCH = D.REVISE_MATCH;
  static final String SHORTEN_MATCH = D.SHORTEN_MATCH;
  static final String END_MATCH = D.END_MATCH;


  if (mode == GC_ERROR_CHECK_SILENT || mode == GC_ERROR_CHECK_PRINT) {
    errorCheckFlag = 1;
  }


  if (mode == GC_RESET) {

    if (init != 0) {
      if ((long)strlen(commentSearchedFlags) != g_statements + 1) {
        bug(1395);
      }
      if (stmtNum != 0) {
        bug(1400);
      }
      for (stmt = 1; stmt <= g_statements; stmt++) {
        if (commentSearchedFlags[stmt] == 'Y') {

          let((vstring *)(&(contributorList[stmt])), "");
          let((vstring *)(&(contribDateList[stmt])), "");
          let((vstring *)(&(reviserList[stmt])), "");
          let((vstring *)(&(reviseDateList[stmt])), "");
          let((vstring *)(&(shortenerList[stmt])), "");
          let((vstring *)(&(shortenDateList[stmt])), "");
          let((vstring *)(&(mostRecentDateList[stmt])), "");
        }
      }

      pntrLet(&contributorList, NULL_PNTRSTRING);
      pntrLet(&contribDateList, NULL_PNTRSTRING);
      pntrLet(&reviserList, NULL_PNTRSTRING);
      pntrLet(&reviseDateList, NULL_PNTRSTRING);
      pntrLet(&shortenerList, NULL_PNTRSTRING);
      pntrLet(&shortenDateList, NULL_PNTRSTRING);
      pntrLet(&mostRecentDateList, NULL_PNTRSTRING);
      let(&commentSearchedFlags, "");
      init = 0;
    }
    return "";
  }


  if (mode == GC_RESET_STMT) {
    if (init != 0) {
      if ((long)strlen(commentSearchedFlags) != g_statements + 1) {
        bug(1398);
      }
      if (stmtNum < 1 || stmtNum > g_statements + 1) {
        bug(1399);
      }
      if (commentSearchedFlags[stmtNum] == 'Y') {

        let((vstring *)(&(contributorList[stmtNum])), "");
        let((vstring *)(&(contribDateList[stmtNum])), "");
        let((vstring *)(&(reviserList[stmtNum])), "");
        let((vstring *)(&(reviseDateList[stmtNum])), "");
        let((vstring *)(&(shortenerList[stmtNum])), "");
        let((vstring *)(&(shortenDateList[stmtNum])), "");
        let((vstring *)(&(mostRecentDateList[stmtNum])), "");
        commentSearchedFlags[stmtNum] = 'N';
      }
    }
    return "";
  }


  if (g_Statement[stmtNum].type != a_ && g_Statement[stmtNum].type != p_) {
    goto RETURN_POINT;
  }

  if (init == 0) {
    init = 1;

    let(&commentSearchedFlags, string(g_statements + 1, 'N'));

    pntrLet(&contributorList, pntrSpace(g_statements + 1));
    pntrLet(&contribDateList, pntrSpace(g_statements + 1));
    pntrLet(&reviserList, pntrSpace(g_statements + 1));
    pntrLet(&reviseDateList, pntrSpace(g_statements + 1));
    pntrLet(&shortenerList, pntrSpace(g_statements + 1));
    pntrLet(&shortenDateList, pntrSpace(g_statements + 1));
    pntrLet(&mostRecentDateList, pntrSpace(g_statements + 1));
  }

  if (stmtNum < 1 || stmtNum > g_statements) bug(1396);

  if (commentSearchedFlags[stmtNum] == 'N'
      || errorCheckFlag == 1 ) {


    let(&description, "");
    description = getDescription(stmtNum);
    let(&description, edit(description,
        4 + 8 + 16 + 128));
    let(&description, cat(" ", description, " ", NULL));

    cStart = instr(1, description, CONTRIB_MATCH);
    if (cStart != 0) {
      cStart = cStart + (long)strlen(CONTRIB_MATCH);
      cEnd = instr(cStart, description, END_MATCH);
      cMid = cEnd;
      if (cMid != 0) {
        while (description[cMid - 1] != ' ') {
          cMid--;
          if (cMid == 0) break;
        }
      }
      let((vstring *)(&(contributorList[stmtNum])),
          seg(description, cStart, cMid - 2));
      let((vstring *)(&(contribDateList[stmtNum])),
          seg(description, cMid + 1, cEnd - 1));
    } else {
    }

    rStart = 0;
    do {
      p = instr(rStart + 1, description, REVISE_MATCH);
      if (p != 0) {
        rStart = p;
        if (firstR == 0) firstR = p + (long)strlen(REVISE_MATCH);

      }
    } while (p != 0);
    if (rStart != 0) {
      rStart = rStart + (long)strlen(REVISE_MATCH);
      rEnd = instr(rStart, description, END_MATCH);
      rMid = rEnd;
      if (rMid != 0) {
        while (description[rMid - 1] != ' ') {
          rMid--;
          if (rMid == 0) break;
        }
      }
      let((vstring *)(&(reviserList[stmtNum])),
          seg(description, rStart, rMid - 2));
      let((vstring *)(&(reviseDateList[stmtNum])),
          seg(description, rMid + 1, rEnd - 1));
    } else {
    }

    sStart = 0;
    do {
      p = instr(sStart + 1, description, SHORTEN_MATCH);
      if (p != 0) {
        sStart = p;
        if (firstS == 0) firstS = p + (long)strlen(SHORTEN_MATCH);

      }
    } while (p != 0);
    if (sStart != 0) {
      sStart = sStart + (long)strlen(SHORTEN_MATCH);
      sEnd = instr(sStart, description, END_MATCH);
      sMid = sEnd;
      if (sMid != 0) {
        while (description[sMid - 1] != ' ') {
          sMid--;
          if (sMid == 0) break;
        }
      }
      let((vstring *)(&(shortenerList[stmtNum])),
          seg(description, sStart, sMid - 2));
      let((vstring *)(&(shortenDateList[stmtNum])),
         seg(description, sMid + 1, sEnd - 1));
    } else {
    }



    let((vstring *)(&(mostRecentDateList[stmtNum])),
        (vstring)(contribDateList[stmtNum]));

    if (compareDates((vstring)(mostRecentDateList[stmtNum]),
        (vstring)(reviseDateList[stmtNum])) == -1) {
      let((vstring *)(&(mostRecentDateList[stmtNum])),
          (vstring)(reviseDateList[stmtNum]));
    }
    if (compareDates((vstring)(mostRecentDateList[stmtNum]),
        (vstring)(shortenDateList[stmtNum])) == -1) {
      let((vstring *)(&(mostRecentDateList[stmtNum])),
          (vstring)(shortenDateList[stmtNum]));
    }


    commentSearchedFlags[stmtNum] = 'Y';
  }


  if (errorCheckFlag == 1) {
    let(&contributor, (vstring)(contributorList[stmtNum]));
    let(&contribDate, (vstring)(contribDateList[stmtNum]));
    let(&reviser, (vstring)(reviserList[stmtNum]));
    let(&reviseDate, (vstring)(reviseDateList[stmtNum]));
    let(&shortener, (vstring)(shortenerList[stmtNum]));
    let(&shortenDate, (vstring)(shortenDateList[stmtNum]));
    let(&mostRecentDate, (vstring)(mostRecentDateList[stmtNum]));
  } else {

    switch (mode) {
      case CONTRIBUTOR:
        let(&returnStr, (vstring)(contributorList[stmtNum])); break;
      case CONTRIB_DATE:
        let(&returnStr, (vstring)(contribDateList[stmtNum])); break;
      case REVISER:
        let(&returnStr, (vstring)(reviserList[stmtNum])); break;
      case REVISE_DATE:
        let(&returnStr, (vstring)(reviseDateList[stmtNum])); break;
      case SHORTENER:
        let(&returnStr, (vstring)(shortenerList[stmtNum])); break;
      case SHORTEN_DATE:
        let(&returnStr, (vstring)(shortenDateList[stmtNum])); break;
      case MOST_RECENT_DATE:
        let(&returnStr, (vstring)(mostRecentDateList[stmtNum])); break;
      default: bug(1397);
    }
  }


  if (errorCheckFlag == 0) goto RETURN_POINT;

  if (g_Statement[stmtNum].type == a_
      && strcmp(left(g_Statement[stmtNum].labelName, 3), "df-")
      && strcmp(left(g_Statement[stmtNum].labelName, 3), "ax-")) {
    goto RETURN_POINT;
  }

  if (cStart == 0) {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: There is no \"", edit(CONTRIB_MATCH, 8+128),
        "...)\" in the comment above statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }

  if (instr(cStart + 1, description, CONTRIB_MATCH) != 0) {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: There is more than one \"", edit(CONTRIB_MATCH, 8+128),
        "...)\" ",
        "in the comment above statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }


  if (cStart != 0 && description[cMid - 2] != ',') {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: There is no comma between contributor and date",
        ", or period is missing,",
        " in the comment above statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }
  if (rStart != 0 && description[rMid - 2] != ',') {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: There is no comma between reviser and date",
        ", or period is missing,",
        " in the comment above statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }
  if (sStart != 0 && description[sMid - 2] != ',') {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: There is no comma between proof shortener and date",
        ", or period is missing,",
        " in the comment above statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }
  if (instr(1, contributor, ",") != 0) {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: There is a comma in the contributor name \"",
        contributor,
        "\" in the comment above statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }
  if (instr(1, reviser, ",") != 0) {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: There is a comma in the reviser name \"",
        reviser,
        "\" in the comment above statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }
  if (instr(1, shortener, ",") != 0) {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: There is a comma in the proof shortener name \"",
        shortener,
        "\" in the comment above statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }



  if ((firstR != 0 && firstR < cStart)
      || (firstS != 0 && firstS < cStart)) {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: \"", edit(CONTRIB_MATCH, 8+128),
        "...)\" is placed after \"",
        edit(REVISE_MATCH, 8+128) , "...)\" or \"",
        edit(SHORTEN_MATCH, 8+128) ,
        "...)\" in the comment above statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }

  if ((cStart !=0 && (cMid == 0 || cEnd == 0 || cMid == cEnd
          || contributor[0] == 0 || contribDate[0] == 0))
      || (rStart !=0 && (rMid == 0 || rEnd == 0 || rMid == rEnd
          || reviser[0] == 0 || reviseDate[0] == 0))
      || (sStart !=0 && (sMid == 0 || sEnd == 0 || sMid == sEnd
          || shortener[0] == 0 || shortenDate[0] == 0))) {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: There is a formatting error in a",
        " \"", edit(CONTRIB_MATCH, 8+128),  "...)\", \"",
        edit(REVISE_MATCH, 8+128) , "...)\", or \"",
        edit(SHORTEN_MATCH, 8+128),
        "...)\" entry in the comment above statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }

  if (contribDate[0] != 0) {
    parseDate(contribDate, &dd, &mmm, &yyyy);
    buildDate(dd, mmm, yyyy, &tmpDate0);
    if (strcmp(contribDate, tmpDate0)) {
      err = 1;
      if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
          "?Warning: There is a formatting error in the \"",
          edit(CONTRIB_MATCH, 8+128),  "...)\" date \"", contribDate, "\""
          " in the comment above statement ",
          str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
          NULL), "    ", " ");
    }
  }

  if (reviseDate[0] != 0) {
    parseDate(reviseDate, &dd, &mmm, &yyyy);
    buildDate(dd, mmm, yyyy, &tmpDate0);
    if (strcmp(reviseDate, tmpDate0)) {
      err = 1;
      if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
          "?Warning: There is a formatting error in the \"",
          edit(REVISE_MATCH, 8+128) , "...)\" date \"", reviseDate, "\""
          " in the comment above statement ",
          str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
          NULL), "    ", " ");
    }
  }

  if (shortenDate[0] != 0) {
    parseDate(shortenDate, &dd, &mmm, &yyyy);
    buildDate(dd, mmm, yyyy, &tmpDate0);
    if (strcmp(shortenDate, tmpDate0)) {
      err = 1;
      if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
          "?Warning: There is a formatting error in the \"",
          edit(SHORTEN_MATCH, 8+128) , "...)\" date \"", shortenDate, "\""
          " in the comment above statement ",
          str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
          NULL), "    ", " ");
    }
  }

  if (contribDate[0] != 0 &&
     ((reviseDate[0] != 0
         && compareDates(contribDate, reviseDate) != -1)
     || (shortenDate[0] != 0
         && compareDates(contribDate, shortenDate) != -1))) {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: The \"", edit(CONTRIB_MATCH, 8+128),
        "...)\" date is not earlier than the \"",
        edit(REVISE_MATCH, 8+128), "...)\" or \"",
        edit(SHORTEN_MATCH, 8+128),
        "...)\" date in the comment above statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }

  if (reviseDate[0] != 0 && shortenDate[0] != 0) {
    p = compareDates(reviseDate, shortenDate);
    if ((rStart < sStart && p == 1)
        || (rStart > sStart && p == -1)) {
      err = 1;
      if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
          "?Warning: The \"", edit(REVISE_MATCH, 8+128), "...)\" and \"",
          edit(SHORTEN_MATCH, 8+128),
         "...)\" dates are in the wrong order in the comment above statement ",
          str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
          NULL), "    ", " ");
    }
  }

  #ifdef DATE_BELOW_PROOF

  if (g_Statement[stmtNum].type != p_) {
    goto RETURN_POINT;
  }
  getProofDate(stmtNum, &tmpDate1, &tmpDate2);
  if (tmpDate1[0] == 0) {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: There is no date below the proof in statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }
  if (tmpDate2[0] == 0
      && (reviseDate[0] != 0 || shortenDate[0] != 0)) {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: The comment has \"",
        edit(REVISE_MATCH, 8+128), "...)\" or \"",
        edit(SHORTEN_MATCH, 8+128),
        "...)\" but there is only one date below the proof",
        " in statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }
  if (tmpDate2[0] != 0 && reviseDate[0] == 0 && shortenDate[0] == 0) {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: There are two dates below the proof but no \"",
        edit(REVISE_MATCH, 8+128), "...)\" or \"",
        edit(SHORTEN_MATCH, 8+128),
        "...)\" entry in statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }
  if (tmpDate2[0] != 0
      && (reviseDate[0] != 0 || shortenDate[0] != 0)
      && strcmp(tmpDate1, reviseDate)
      && strcmp(tmpDate1, shortenDate)) {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: Neither a \"",
        edit(REVISE_MATCH, 8+128), "...)\" date ",
        "nor a \"", edit(SHORTEN_MATCH, 8+128), "...)\" date ",
        "matches the date ", tmpDate1,
        " below the proof in statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }
  if (tmpDate2[0] != 0
      && reviseDate[0] != 0
      && compareDates(tmpDate1, reviseDate) == -1) {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: The \"",
        edit(REVISE_MATCH, 8+128), "...)\" date ", reviseDate,
        " is later than the date ", tmpDate1,
        " below the proof in statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }
  if (tmpDate2[0] != 0
      && shortenDate[0] != 0
      && compareDates(tmpDate1, shortenDate) == -1) {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: The \"",
        edit(SHORTEN_MATCH, 8+128), "...)\" date ", shortenDate,
        " is later than the date ", tmpDate1,
        " below the proof in statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }
  if (tmpDate2[0] != 0 && compareDates(tmpDate2, tmpDate1) != -1) {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: The first date below the proof, ", tmpDate1,
        ", is not newer than the second, ", tmpDate2,
        ", in statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }
  if (tmpDate2[0] == 0) {
    let(&tmpDate0, tmpDate1);
  } else {
    let(&tmpDate0, tmpDate2);
  }
  if (contribDate[0] != 0
      && tmpDate0[0] != 0 && strcmp(contribDate, tmpDate0)) {
    err = 1;
    if (mode == GC_ERROR_CHECK_PRINT) printLongLine(cat(
        "?Warning: The \"", edit(CONTRIB_MATCH, 8+128), "...)\" date ",
        contribDate,
        " doesn't match the date ", tmpDate0,
        " below the proof in statement ",
        str((double)stmtNum), ", label \"", g_Statement[stmtNum].labelName, "\".",
        NULL), "    ", " ");
  }

#endif

  if (err == 1) {
    let(&returnStr, "F");
  } else {
    let(&returnStr, "P");
  }


 RETURN_POINT:

  let(&description, "");

  if (errorCheckFlag == 1) {
    let(&contributor, "");
    let(&contribDate, "");
    let(&reviser, "");
    let(&reviseDate, "");
    let(&shortener, "");
    let(&shortenDate, "");
    let(&mostRecentDate, "");
    let(&tmpDate0, "");
    let(&tmpDate1, "");
    let(&tmpDate2, "");
  }

  return returnStr;
}





void getProofDate(long stmtNum, vstring *date1, vstring *date2) {
  vstring textAfterProof = "";
  long p1, p2;
  let(&textAfterProof, space(g_Statement[stmtNum + 1].labelSectionLen));
  memcpy(textAfterProof, g_Statement[stmtNum + 1].labelSectionPtr,
      (size_t)(g_Statement[stmtNum + 1].labelSectionLen));
  let(&textAfterProof, edit(textAfterProof, 2));
  p1 = instr(1, textAfterProof, "$([");
  p2 = instr(p1, textAfterProof, "]$)");
  if (p1 && p2) {
    let(&(*date1), seg(textAfterProof, p1 + 3, p2 - 1));
    p1 = instr(p2, textAfterProof, "$([");
    p2 = instr(p1, textAfterProof, "]$)");
    if (p1 && p2) {
      let(&(*date2), seg(textAfterProof, p1 + 3, p2 - 1));
    } else {
      let(&(*date2), "");
    }
  } else {
    let(&(*date1), "");
    let(&(*date2), "");
  }
  let(&textAfterProof, "");
  return;
}





flag parseDate(vstring dateStr, long *dd, long *mmm, long *yyyy) {
  long j;
  flag err = 0;
  j = instr(1, dateStr, "-");
  *dd = (long)val(left(dateStr, j - 1));
  static final String  MONTHS=D. MONTHS;
  *mmm = ((instr(1, MONTHS, mid(dateStr, j + 1, 3)) - 1) / 3) + 1;
  j = instr(j + 1, dateStr, "-");
  *yyyy = (long)val(right(dateStr, j + 1));
  if (*yyyy < 100) {
    static final long  START_YEAR =D.START_YEAR;
    if (*yyyy < START_YEAR) {
      *yyyy = *yyyy + 2000;
    } else {
      *yyyy = *yyyy + 1900;
    }
  }
  if (*dd < 1 || *dd > 31 || *mmm < 1 || *mmm > 12) err = 1;
  return err;
}



void buildDate(long dd, long mmm, long yyyy, vstring *dateStr) {
  let(&(*dateStr), cat(str((double)dd), "-", mid(MONTHS, mmm * 3 - 2, 3), "-",
      str((double)yyyy), NULL));
  return;
}



flag compareDates(vstring date1, vstring date2) {
  long d1, m1, y1, d2, m2, y2, dd1, dd2;


  if (date1[0] == 0 || date2[0] == 0) {
    if (date1[0] == 0 && date2[0] == 0) {
      return 0;
    } else if (date1[0] == 0) {
      return -1;
    } else {
      return 1;
    }
  }

  parseDate(date1, &d1, &m1, &y1);
  parseDate(date2, &d2, &m2, &y2);

  dd1 = d1 + m1 * 32 + y1 * 500;
  dd2 = d2 + m2 * 32 + y2 * 500;
  if (dd1 < dd2) {
    return -1;
  } else if (dd1 == dd2) {
    return 0;
  } else {
    return 1;
  }
}






int qsortStringCmp(const void *p1, const void *p2)
{
  vstring tmp = "";
  long n1, n2;
  int r;

  if (g_qsortKey[0] == 0) {

    return strcmp(*(char * const *)p1, *(char * const *)p2);
  } else {
    n1 = instr(1, *(char * const *)p1, g_qsortKey);
    n2 = instr(1, *(char * const *)p2, g_qsortKey);
    r = strcmp(
        right(*(char * const *)p1, n1),
        right(*(char * const *)p2, n2));
    let(&tmp, "");
    return r;
  }
}


void freeData() {

  free(g_IncludeCall);
  free(g_Statement);
  free(g_MathToken);
  free(memFreePool);
  free(memUsedPool);
}
}