package xd.metamath;

public class mminou {












    static final long QUOTED_SPACE=D.QUOTED_SPACE;


int g_errorCount = 0;


flag g_logFileOpenFlag = 0;
File g_logFilePtr;
File g_listFile_fp = NULL;

flag g_outputToString = 0;
vstring g_printString = "";

long g_commandFileNestingLevel = 0;
File g_commandFilePtr[MAX_COMMAND_FILE_NESTING + 1];
vstring g_commandFileName[MAX_COMMAND_FILE_NESTING + 1];
flag g_commandFileSilent[MAX_COMMAND_FILE_NESTING + 1];
flag g_commandFileSilentFlag = 0;


FILE  *g_input_fp ;


vstring  g_input_fn="", g_output_fn="";

long g_screenWidth = MAX_LEN;
long g_screenHeight = SCREEN_HEIGHT;
int printedLines = 0;
flag g_scrollMode = 1;
flag g_quitPrint = 0;
flag localScrollMode = 1;


pntrString *backBuffer = NULL_PNTRSTRING;
long backBufferPos = 0;
flag backFromCmdInput = 0;

flag print2(char* fmt,...)
{

  va_list ap;
  char c;
  long nlpos, lineLen, charsPrinted;

  long i;



  char *printBuffer;

#ifdef __LCC__
  long bufsiz;
#else
  ssize_t bufsiz;
#endif


  if (backBufferPos == 0) {


    if (pntrLen(backBuffer)) {
      printf("*** BUG #1501\n");
#if __STDC__
      fflush(stdout);
#endif
    }
    backBufferPos = 1;
    pntrLet(&backBuffer, pntrAddElement(backBuffer));

  }

  if ((!g_quitPrint && g_commandFileNestingLevel == 0 && (g_scrollMode == 1
           && localScrollMode == 1)

      && printedLines >=  g_screenHeight && !g_outputToString)
      || backFromCmdInput) {

    while(true) {
      if (backFromCmdInput && backBufferPos == pntrLen(backBuffer))
        break;
      if (backBufferPos < 1 || backBufferPos > pntrLen(backBuffer)) {

        printf("*** BUG #1502 %ld\n", backBufferPos);
#if __STDC__
        fflush(stdout);
#endif
      }
      if (backBufferPos == 1) {
        printf(
"Press <return> for more, Q <return> to quit, S <return> to scroll to end... "
          );
#if __STDC__
        fflush(stdout);
#endif
      } else {
        printf(
"Press <return> for more, Q <ret> quit, S <ret> scroll, B <ret> back up... "
         );
#if __STDC__
        fflush(stdout);
#endif
      }
      c = (char)(getchar());
      if (c == '\n') {
        if (backBufferPos == pntrLen(backBuffer)) {

          break;
        } else {

          backBufferPos++;
          printf("%s", (vstring)(backBuffer[backBufferPos - 1]));
#if __STDC__
          fflush(stdout);
#endif
          continue;
        }
      }
      if (getchar() == '\n') {
        if (c == 'q' || c == 'Q') {
          if (!backFromCmdInput)
            g_quitPrint = 1;
          break;
        }
        if (c == 's' || c == 'S') {

          if (backBufferPos < pntrLen(backBuffer)) {


            while (backBufferPos + 1 <= pntrLen(backBuffer)) {
              backBufferPos++;
              printf("%s", (vstring)(backBuffer[backBufferPos - 1]));
#if __STDC__
              fflush(stdout);
#endif
            }
          }
          if (!backFromCmdInput)
            localScrollMode = 0;
          break;

        }
        if (backBufferPos > 1) {
          if (c == 'b' || c == 'B') {
            backBufferPos--;
            printf("%s", (vstring)(backBuffer[backBufferPos - 1]));
#if __STDC__
            fflush(stdout);
#endif
            continue;
          }
        }

        printf("%c", 7);
#if __STDC__
        fflush(stdout);
#endif
        continue;
      }
      while (c != '\n') c = (char)(getchar());
    }
    if (backFromCmdInput)
      C.go2("PRINT2_RETURN");
    printedLines = 0;
    if (!g_quitPrint) {
      backBufferPos++;
      pntrLet(&backBuffer, pntrAddElement(backBuffer));

    }
  }



  if (g_quitPrint && !g_outputToString) {
    C.go2("PRINT2_RETURN");
  }



  va_start(ap, fmt);
  bufsiz = vsnprintf(NULL, 0, fmt, ap);
  va_end(ap);
  if (bufsiz == -1) bug(1527);
  printBuffer = malloc((size_t)bufsiz + 1);

  va_start(ap, fmt);
  charsPrinted = vsprintf(printBuffer, fmt, ap);
  va_end(ap);
  if (charsPrinted != bufsiz) {

    printf("For bug #1528: charsPrinted = %ld != bufsiz = %ld\n", charsPrinted,
        (long)bufsiz);
    bug(1528);
  }


  nlpos = instr(1, printBuffer, "\n");
  lineLen = (long)strlen(printBuffer);

  for (i = 0; i < lineLen; i++) {
    if (printBuffer[i] == QUOTED_SPACE) printBuffer[i] = ' ';
  }

  if ((lineLen > g_screenWidth + 1)
         && !g_outputToString   ) {

    if (!nlpos) {

      printLongLine(left(printBuffer, lineLen), "", "");
    } else {
      printLongLine(left(printBuffer, lineLen - 1), "", "");
    }
    C.go2("PRINT2_RETURN");
  }

  if (!g_outputToString && !g_commandFileSilentFlag) {
    if (nlpos == 0) {


      printf("%s", printBuffer);




#if __STDC__
      fflush(stdout);
#endif

    } else {
      printf("%s", printBuffer);
#if __STDC__
      fflush(stdout);
#endif
      printedLines++;
      if (!(g_scrollMode == 1 && localScrollMode == 1)) {

        if (printedLines >  g_screenHeight) {
          printedLines = 1;
          backBufferPos++;
          pntrLet(&backBuffer, pntrAddElement(backBuffer));

        }
      }
    }


    if (backBufferPos < 1) {
      printf("*** PROGRAM BUG #1504\n");
#if __STDC__
      fflush(stdout);
#endif
    }
    let((vstring *)(&(backBuffer[backBufferPos - 1])), cat(
        (vstring)(backBuffer[backBufferPos - 1]), printBuffer, NULL));
  }

  if (g_logFileOpenFlag && !g_outputToString ) {
    fprintf(g_logFilePtr, "%s", printBuffer);
#if __STDC__

    fflush(g_logFilePtr);
#endif
  }

  if (g_listMode && g_listFile_fp != NULL && !g_outputToString) {

    fprintf(g_listFile_fp, "! %s", printBuffer);
  }

  if (g_outputToString) {
    let(&g_printString, cat(g_printString, printBuffer, NULL));
  }


  if (lineLen > g_screenWidth + 1) {


    printf("*** PROGRAM BUG #1505 (not serious, but please report it)\n");
    printf("Line exceeds screen width; caller should use printLongLine.\n");
    printf("%ld %s\n", lineLen, printBuffer);

#if __STDC__
    fflush(stdout);
#endif
  }


  if (nlpos != 0 && nlpos != lineLen) {
    printf("*** PROGRAM BUG #1506\n");
#if __STDC__
    fflush(stdout);
#endif
  }

  free(printBuffer);

    C.label("PRINT2_RETURN");
  return (!g_quitPrint);
}










void printLongLine(vstring line, vstring startNextLine, vstring breakMatch)
{
  vstring longLine = "";
  vstring multiLine = "";
  vstring prefix = "";
  vstring startNextLine1 = "";
  vstring breakMatch1 = "";
  long i, j, p;
  long startNextLineLen;
  flag firstLine;
  flag tildeFlag = 0;
  flag treeIndentationFlag = 0;




  flag quoteMode = 0;


  long saveScreenWidth;

  long saveTempAllocStack;


  if (!line[0]) {
    let(&longLine, "");
    print2("\n");
    return;
  }

  saveTempAllocStack = g_startTempAllocStack;
  g_startTempAllocStack = g_tempAllocStackTop;


  let(&multiLine, line);
  let(&startNextLine1, startNextLine);
  let(&breakMatch1, breakMatch);

  g_startTempAllocStack = saveTempAllocStack;




  if (breakMatch1[0] == 1) {
    treeIndentationFlag = 1;
    breakMatch1[0] = ' ';
  }


  j = (long)strlen(multiLine);

  for (i = 0; i < j; i++) {
    if (multiLine[i] == QUOTED_SPACE) bug(1514);
  }
  if (breakMatch1[0] == '\"') {

    breakMatch1[0] = ' ';

    quoteMode = 0;

    i = 0;
    while (multiLine[i]) {
      if (multiLine[i] == '"' && i > 0) {
        if (!quoteMode && multiLine[i - 1] == '=')
          quoteMode = 1;
        else
          quoteMode = 0;
      }
      if (multiLine[i] == ' ' && quoteMode)
        multiLine[i] = QUOTED_SPACE;
      i++;
    }



#ifdef OBSOLETE_QUOTE_HANDLING_CODE
    for (i = 0; i < j; i++) {

      if (i == i) break;


      if (multiLine[i] == '"' && i >= 5
          && !strncmp("ALT='\"'\"", multiLine + i - 5, 7))
        continue;
      if (!quoteMode) {


        if (multiLine[i] == '"') {
          quoteMode = 1;
          quoteStartPos = i;
          quoteChar = multiLine[i];
        }
      } else {
        if (multiLine[i] == quoteChar) {
          quoteMode = 0;
        }
      }
      if (quoteMode == 1 && multiLine[i] == ' ') {

        multiLine[i] = QUOTED_SPACE;
      }
    }
    if (quoteMode == 1) {
      for (i = quoteStartPos; i < j; i++) {
        if (multiLine[i] == QUOTED_SPACE) multiLine[i] = ' ';
      }
    }
    i = 0;
    while (true) {
      i = instr(i + 1, multiLine, "Arial Narrow");
      if (i) multiLine[i + 4] = QUOTED_SPACE;
      else break;
    }
#endif


  }


  if (startNextLine1[0] == '~') {
    tildeFlag = 1;
    let(&startNextLine1, " ");
  }


  while (multiLine[0]) {


    p = instr(1, multiLine, "\n");
    if (p) {

      let(&longLine, left(multiLine, p - 1));


       let(&multiLine, right(multiLine, p + 1));
    } else {
      let(&longLine, multiLine);
      let(&multiLine, "");
    }

    saveScreenWidth = g_screenWidth;
      C.label("HTML_RESTART");
    firstLine = 1;

    startNextLineLen = (long)strlen(startNextLine1);

    if (startNextLineLen > g_screenWidth - 4) {
      startNextLineLen = g_screenWidth - 4;
      let(&startNextLine1, left(startNextLine1, g_screenWidth - 4));
    }

    while ((signed)(strlen(longLine)) + (1 - firstLine) * startNextLineLen >
        g_screenWidth - (long)tildeFlag - (long)(breakMatch1[0] == '\\')) {

      p = g_screenWidth - (long)tildeFlag - (long)(breakMatch1[0] == '\\') + 1;
      if (!firstLine) p = p - startNextLineLen;

      if (p < 4) bug(1524);

      if (breakMatch1[0] == '&'
          && ((!instr(p, left(longLine, (long)strlen(longLine) - 3), " ")
              && longLine[p - 3] != ' ')

            || longLine[p - 4] == ')'))  {

        p = p + 0;

        if (longLine[p - 2] == ' ') p--;
      } else {
        if (!breakMatch1[0]) {
          p = p + 0;
        } else {
          if (breakMatch1[0] == '&') {


          }
          if (p <= 0) bug(1518);





          while (strchr(breakMatch1[0] != '\\' ? breakMatch1 : " ",
              longLine[p - 1]) == NULL) {
            p--;
            if (!p) break;
          }

          if (p <= 0) {
            g_screenWidth++;

            C.go2("HTML_RESTART");
          }

          if (breakMatch1[0] == '&') {


          }
        }
      }

      if (p <= 0) {
        p = g_screenWidth - (long)tildeFlag  - (long)(breakMatch1[0] == '\\')+ 1;
        if (!firstLine) p = p - startNextLineLen;
        if (p <= 0) p = 1;
      }
      if (!p) bug(1515);

      if (p == 1 && longLine[0] != ' ') bug(1516);
      if (firstLine) {
        firstLine = 0;
        let(&prefix, "");
      } else {
        let(&prefix, startNextLine1);
        if (treeIndentationFlag) {
          if (startNextLineLen + p - 1 < g_screenWidth) {

            let(&prefix, cat(prefix, space(g_screenWidth - startNextLineLen
                - p + 1), NULL));
          }
        }
      }
      if (!tildeFlag) {
          print2("%s\n",cat(prefix, left(longLine,p - 1), NULL));
      } else {
        print2("%s\n",cat(prefix, left(longLine,p - 1), "~", NULL));
      }
      if (longLine[p - 1] == ' ' &&
          breakMatch1[0] ) {

        if (longLine[p] == ' ') {

          let(&longLine, right(longLine, p + 2));
        } else {
          let(&longLine, right(longLine,p + 1));
        }
      } else {
        let(&longLine, right(longLine,p));
      }
    }
    if (!firstLine) {
      if (treeIndentationFlag) {

        print2("%s\n",cat(startNextLine1, space(g_screenWidth
            - startNextLineLen - (long)(strlen(longLine))), longLine, NULL));
      } else {
        print2("%s\n",cat(startNextLine1, longLine, NULL));
      }
    } else {
      print2("%s\n",longLine);
    }
    g_screenWidth = saveScreenWidth;

  }

  let(&multiLine, "");
  let(&longLine, "");
  let(&prefix, "");
  let(&startNextLine1, "");
  let(&breakMatch1, "");

  return;
}


vstring cmdInput(File stream, vstring ask)
{
  vstring g = "";
  long i;
    static final long  CMD_BUFFER_SIZE =D.CMD_BUFFER_SIZE;

  while (true) {
    if (ask != NULL && !g_commandFileSilentFlag) {
      printf("%s",ask);
#if __STDC__
      fflush(stdout);
#endif
    }
    let(&g, space(CMD_BUFFER_SIZE));
    if (g[CMD_BUFFER_SIZE]) bug(1520);
    g[CMD_BUFFER_SIZE - 1] = 0;
    if (!fgets(g, CMD_BUFFER_SIZE, stream)) {

      let(&g, "");
      return NULL;
    }
    if (g[CMD_BUFFER_SIZE - 1]) {


      printf("***BUG #1508\n");
#if __STDC__
      fflush(stdout);
#endif
    }
    i = (long)strlen(g);
db = db - (CMD_BUFFER_SIZE - i);

    if (!i) {
      printf("***BUG #1507\n");
#if __STDC__
      fflush(stdout);
#endif

    } else {
      if (g[i - 1] != '\n') {

        if (!feof(stream)) {
          printf("***BUG #1525\n");
#if __STDC__
          fflush(stdout);
#endif
        }

        let(&g, cat(g, chr('\n'), NULL));
db = db + (CMD_BUFFER_SIZE - i);
        i++;
      }


    }

    if (g[1]) {
      i--;
      if (g[i] != '\n') {
        printf("***BUG #1519\n");
#if __STDC__
        fflush(stdout);
#endif
      }
      g[i]=0;
db = db - 1;
    } else {
      if (g[0] != '\n') {
        printf("***BUG #1521\n");
#if __STDC__
        fflush(stdout);
#endif
      }
      let(&g, "");
    }

    if ((!strcmp(g, "B") || !strcmp(g, "b"))
        && pntrLen(backBuffer) > 1
        && g_commandFileNestingLevel == 0
        && (g_scrollMode == 1 && localScrollMode == 1)
        && !g_outputToString) {

      backBufferPos = pntrLen(backBuffer) - 1;
      printf("%s", (vstring)(backBuffer[backBufferPos - 1]));
#if __STDC__
      fflush(stdout);
#endif
      backFromCmdInput = 1;
      print2("");
      backFromCmdInput = 0;
    } else {
      if (g_commandFileNestingLevel > 0) break;
      if (ask == NULL) {
        printf("***BUG #1523\n");
#if __STDC__
        fflush(stdout);
#endif
      }
      if (g[0]) break;
      if (ask != NULL &&

          strcmp(ask, g_commandPrompt)) {
        break;
      }
    }
  }

  return g;
}

vstring cmdInput1(vstring ask)
{

  vstring commandLn = "";
  vstring ask1 = "";
  long p, i;

  let(&ask1, ask);

  while ((signed)(strlen(ask1)) > g_screenWidth) {
    p = g_screenWidth - 1;
    while (ask1[p] != ' ' && p > 0) p--;
    if (!p) p = g_screenWidth - 1;
    print2("%s\n", left(ask1, p));
    let(&ask1, right(ask1, p + 1));
  }

  if ((signed)(strlen(ask1)) > g_screenWidth - 10) {
    p = g_screenWidth - 11;
    while (ask1[p] != ' ' && p > 0) p--;
    if (p) {
      print2("%s\n", left(ask1, p));
      let(&ask1, right(ask1, p + 1));
    }
  }

  printedLines = 0;
  g_quitPrint = 0;
  localScrollMode = 1;

  while (true) {
    if (g_commandFileNestingLevel == 0) {
      commandLn = cmdInput(stdin, ask1);
      if (!commandLn) {
        commandLn = "";




        if (strcmp(left(ask1, 2), "Do")) {

          let(&commandLn, "EXIT");
        } else {


          let(&commandLn, "Y");
        }
        printf("%s\n", commandLn);

      }
      if (g_logFileOpenFlag) fprintf(g_logFilePtr, "%s%s\n", ask1, commandLn);


      for (i = 0; i < pntrLen(backBuffer); i++) {
        let((vstring *)(&(backBuffer[i])), "");
      }
      backBufferPos = 1;
      pntrLet(&backBuffer, NULL_PNTRSTRING);
      pntrLet(&backBuffer, pntrAddElement(backBuffer));



      let((vstring *)(&(backBuffer[backBufferPos - 1])), cat(
          (vstring)(backBuffer[backBufferPos - 1]), ask1,
          commandLn, "\n", NULL));

      if (g_listMode && g_listFile_fp != NULL) {

        fprintf(g_listFile_fp, "! %s\n", commandLn);
      }

    } else {
      commandLn = cmdInput(g_commandFilePtr[g_commandFileNestingLevel], NULL);
      if (!commandLn) {
        fclose(g_commandFilePtr[g_commandFileNestingLevel]);
        print2("%s[End of command file \"%s\".]\n", ask1,
            g_commandFileName[g_commandFileNestingLevel]);
        let(&(g_commandFileName[g_commandFileNestingLevel]), "");

        g_commandFileNestingLevel--;
        commandLn = "";
        if (g_commandFileNestingLevel == 0) {
          g_commandFileSilentFlag = 0;
        } else {
          g_commandFileSilentFlag = g_commandFileSilent[g_commandFileNestingLevel];

        }
        break;
      }


      let(&commandLn, edit(commandLn, 8192));

      print2("%s%s\n", ask1, commandLn);
    }
    break;
  }

  let(&ask1, "");
  return commandLn;
}


void errorMessage(vstring line, long lineNum, long column, long tokenLength,
  vstring error, vstring fileName, long statementNum, flag severity)
{
  vstring errorPointer = "";
  vstring tmpStr = "";
  vstring prntStr = "";
  vstring line1 = "";
  int j;







  let(&tmpStr,error);
  error = "";
  let(&error,tmpStr);


  if (line) {
    if (line[strlen(line) - 1] != '\n') {
      let(&line1, line);
    } else {
      bug(1509);
    }
  } else {
    line1 = NULL;
  }

  if (fileName) {

    print2("\n");
  }

  switch (severity) {
    case (char)notice_:
      let(&prntStr, "?Notice"); break;
    case (char)warning_:
      let(&prntStr, "?Warning"); break;
    case (char)error_:
      let(&prntStr, "?Error"); break;
    case (char)fatal_:
      let(&prntStr, "?Fatal error"); break;
  }
  if (lineNum) {
    let(&prntStr, cat(prntStr, " on line ", str((double)lineNum), NULL));
    if (fileName) {
      let(&prntStr, cat(prntStr, " of file \"", fileName, "\"", NULL));
    }
  } else {
    if (fileName) {
      let(&prntStr, cat(prntStr, " in file \"", fileName, "\"", NULL));
    }
  }
  if (statementNum) {
    let(&prntStr, cat(prntStr, " at statement ", str((double)statementNum), NULL));
    if (g_Statement[statementNum].labelName[0]) {
      let(&prntStr, cat(prntStr, ", label \"",
          g_Statement[statementNum].labelName, "\"", NULL));
    }
    let(&prntStr, cat(prntStr, ", type \"$", chr(g_Statement[statementNum].type),
        "\"", NULL));
  }
  printLongLine(cat(prntStr, ":", NULL), "", " ");
  if (line1) printLongLine(line1, "", "");
  if (line1 && column && tokenLength) {
    let(&errorPointer,"");
    for (j=0; j<column-1; j++) {
      if (line1[j] == '\t') let (&errorPointer,cat(errorPointer,"\t",NULL));
      else let(&errorPointer,cat(errorPointer," ",NULL));
    }
    for (j=0; j<tokenLength; j++)
      let(&errorPointer,cat(errorPointer,"^",NULL));
    printLongLine(errorPointer, "", "");
  }
  printLongLine(error,""," ");
  if (severity == 2) g_errorCount++;







  if (severity == 3) {
    print2("Aborting Metamath.\n");
    exit(0);
  }
  let(&errorPointer,"");
  let(&tmpStr,"");
  let(&prntStr,"");
  let(&error,"");
  if (line1) let(&line1,"");
}







File fSafeOpen(vstring fileName, vstring mode, flag noVersioningFlag)
{
  File fp;
  vstring prefix = "";
  vstring postfix = "";
  vstring bakName = "";
  vstring newBakName = "";
  long v;
  long lastVersion;

  if (!strcmp(mode, "r")) {
    fp = fopen(fileName, "r");
    if (!fp) {
      print2("?Sorry, couldn't open the file \"%s\".\n", fileName);
    }
    return (fp);
  }

  if (!strcmp(mode, "w")
      || !strcmp(mode, "d")) {
    if (noVersioningFlag) C.go2("skip_backup");

    fp = fopen(fileName, "r");

    if (fp) {
      fclose(fp);

static final long VERSIONS =D.VERSIONS;



      let(&prefix, cat(fileName, "~", NULL));
      let(&postfix, "");




      let(&bakName, cat(prefix, str(1), postfix, NULL));
      fp = fopen(bakName, "r");
      if (fp) {
        fclose(fp);



        lastVersion = 0;
        for (v = 1; v <= VERSIONS; v++) {
          let(&bakName, cat(prefix, str((double)v), postfix, NULL));
          fp = fopen(bakName, "r");
          if (!fp) break;
          fclose(fp);
          lastVersion = v;
        }


        if (lastVersion == VERSIONS) {
          let(&bakName, cat(prefix, str((double)VERSIONS), postfix, NULL));
          fp = fopen(bakName, "r");
          if (fp) {
            fclose(fp);
            remove(bakName);
          }
          lastVersion--;
        }

        for (v = lastVersion; v >= 1; v--) {
          let(&bakName, cat(prefix, str((double)v), postfix, NULL));
          fp = fopen(bakName, "r");
          if (!fp) continue;
          fclose(fp);
          let(&newBakName, cat(prefix, str((double)v + 1), postfix, NULL));
          rename(bakName, newBakName);
        }

      }
      let(&bakName, cat(prefix, str(1), postfix, NULL));
      rename(fileName, bakName);

    }
      C.label("skip_backup");

    if (!strcmp(mode, "w")) {
      fp = fopen(fileName, "w");
      if (!fp) {
        print2("?Sorry, couldn't open the file \"%s\".\n", fileName);
      }
    } else {
      if (strcmp(mode, "d")) {
        bug(1526);
      }

      fp = NULL;


      if (noVersioningFlag) {
        if(remove(fileName) != 0) {
          print2("?Sorry, couldn't delete the file \"%s\".\n", fileName);
        }
      }
    }


    let(&prefix, "");
    let(&postfix, "");
    let(&bakName, "");
    let(&newBakName, "");

    return (fp);
  }

  bug(1510);
  return(NULL);

}


int fSafeRename(vstring oldFileName, vstring newFileName)
{
  int error = 0;
  int i;
  File fp;

  fp = fSafeOpen(newFileName, "w", 0);
  if (!fp) error = -1;

  if (!error) {
    error = fclose(fp);
    if (error) print2("?Empty \"%s\" couldn't be closed.\n", newFileName);
  }
  if (!error) {
    error = remove(newFileName);

    if (error) {
      for (i = 2; i < 1000; i++) {
        error = remove(newFileName);
        if (!error) break;
      }
      if (!error)
        print2("OS WARNING: File delete succeeded only after %i attempts.", i);
    }

    if (error) print2("?Empty \"%s\" couldn't be deleted.\n", newFileName);
  }

  if (!error) {
    error = rename(oldFileName, newFileName);
    if (error) print2("?Rename of \"%s\" to \"%s\" failed.\n", oldFileName,
        newFileName);
  }
  if (error) {
    print2("?Sorry, couldn't rename the file \"%s\" to \"%s\".\n", oldFileName,
        newFileName);
    print2("\"%s\" may be empty; try recovering from \"%s\".\n", newFileName,
        oldFileName);
  }
  return error;
}


vstring fGetTmpName(vstring filePrefix)
{
  File fp;
  vstring fname = "";
  static long counter = 0;
  while (true) {
    counter++;
    let(&fname, cat(filePrefix, str((double)counter), ".tmp", NULL));
    fp = fopen(fname, "r");
    if (!fp) break;


    fclose(fp);

    if (counter > 1000) {
      print2("?Warning: too many %snnn.tmp files - will reuse %s\n",
          filePrefix, fname);
      break;
    }
  }
  return fname;
}




vstring readFileToString(vstring fileName, char verbose, long *charCount) {
  File inputFp;
  long fileBufSize;

  char *fileBuf;
  long i, j;



  inputFp = fopen(fileName, "rb");
  if (!inputFp) {
    if (verbose) print2("?Sorry, couldn't open the file \"%s\".\n", fileName);
    return (NULL);
  }
#ifndef SEEK_END

    static final long  SEEK_END=D.SEEK_END;
#endif
  if (fseek(inputFp, 0, SEEK_END)) {

    if (verbose) print2(
        "?Sorry, \"%s\" doesn't seem to be a regular file.\n",
        fileName);
    return (NULL);
  }
  fileBufSize = ftell(inputFp);



  fclose(inputFp);
  inputFp = fopen(fileName, "r");
  if (!inputFp) bug(1512);


  fileBufSize = fileBufSize + 10;

  fileBuf = malloc((size_t)fileBufSize);
  if (!fileBuf) {
    if (verbose) print2(
        "?Sorry, there was not enough memory to read the file \"%s\".\n",
        fileName);
    fclose(inputFp);
    return (NULL);
  }


  (* charCount) = (long)fread(fileBuf, sizeof(char), (size_t)fileBufSize - 2,
      inputFp);
  if (!feof(inputFp)) {
    print2("Note:  This bug will occur if there is a disk file read error.\n");
    bug(1513);
  }
  fclose(inputFp);

  fileBuf[(*charCount)] = 0;



  if ((*charCount) > 1) {
    if (fileBuf[0] == '\377' && fileBuf[1] == '\376') {

      if (2 * ((*charCount) / 2) != (*charCount)) {
        if (verbose) print2(
"?Sorry, there are an odd number of characters (%ld) %s \"%s\".\n",
            (*charCount), "in Unicode file", fileName);
        free(fileBuf);
        return (NULL);
      }
      i = 0;
      j = 2;
      while (j < (*charCount)) {
        if (fileBuf[j + 1] != 0) {
          if (verbose) print2(
              "?Sorry, the Unicode file \"%s\" %s %ld at byte %ld.\n",
              fileName, "has a non-ASCII \ncharacter code",
              (long)(fileBuf[j]) + ((long)(fileBuf[j + 1]) * 256), j);
          free(fileBuf);
          return (NULL);
        }
        if (fileBuf[j] == 0) {
          if (verbose) print2(
              "?Sorry, the Unicode file \"%s\" %s at byte %ld.\n",
              fileName, "has a null character", j);
          free(fileBuf);
          return (NULL);
        }
        fileBuf[i] = fileBuf[j];

        if (fileBuf[i] == '\r') {
          i--;
        }
        i++;
        j = j + 2;
      }
      fileBuf[i] = 0;
      (*charCount) = i;
    }
  }


  if (strchr(fileBuf, '\r') != NULL) {
    if (verbose) print2(
       "?Warning: the file \"%s\" has carriage-returns.  Cleaning them up...\n",
        fileName);

    i = 0;
    j = 0;
    while (j <= (*charCount)) {
      if (fileBuf[j] == '\r') {
        if (fileBuf[j + 1] == '\n') {

          j++;
        } else {

          fileBuf[j] = '\n';
        }
      }
      fileBuf[i] = fileBuf[j];
      i++;
      j++;
    }
    (*charCount) = i - 1;
  }


  if (fileBuf[(*charCount) - 1] != '\n') {
    if (verbose) print2(
        "?Warning: the last line in file \"%s\" is incomplete.\n",
        fileName);

    fileBuf[(*charCount)] = '\n';
    (*charCount)++;
    fileBuf[(*charCount)] = 0;
  }

  if (fileBuf[(*charCount)] != 0) {
    bug(1522);
  }


  i = (long)strlen(fileBuf);
  if ((*charCount) != i) {
    if (verbose) {
      print2(
          "?Warning: the file \"%s\" is not an ASCII file.\n",
          fileName);
      print2(
          "Its size is %ld characters with null at character %ld.\n",
          (*charCount), strlen(fileBuf));
    }
  }
db = db + i;


  return ((char *)fileBuf);
}



double getRunTime(double *timeSinceLastCall) {
#ifdef CLOCKS_PER_SEC
  static clock_t timePrevious = 0;
  clock_t timeNow;
  timeNow = clock();
  *timeSinceLastCall = (double)((1.0 * (double)(timeNow - timePrevious))
          /CLOCKS_PER_SEC);
  timePrevious = timeNow;
  return (double)((1.0 * (double)timeNow)/CLOCKS_PER_SEC);


#else
  print2("The clock() function is not implemented on this computer.\n");
  *timeSinceLastCall = 0;
  return 0;
#endif
}



void freeInOu() {
  long i, j;
  j = pntrLen(backBuffer);
  for (i = 0; i < j; i++) let((vstring *)(&backBuffer[i]), "");
  pntrLet(&backBuffer, NULL_PNTRSTRING);
}
}