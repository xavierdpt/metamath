package xd.metamath;

public class metamath {

  static final String MVERSION = D.MVERSION;


  public static void main(String[] args) {
    mmdata.g_listMode.set(0);
    mmdata.g_toolsMode.set(mmdata.g_listMode);
    if (!mmdata.g_listMode.asBoolean()) {

    print2("Metamath - Version %s%s", MVERSION, space(27 - (long)strlen(MVERSION)));
  }
   print2("Type HELP for help, EXIT to exit.\n");


    mmdata.initBigArrays();



  mmvstr.let(&mmdata.g_contributorName, hmmdata.DEFAULT_CONTRIBUTOR);


  command(args);


  if (mmdata.g_listMode.asBoolean() && mminou.g_listFile_fp != null) {
    fclose(mminou.g_listFile_fp);
  }

  System.exit(0);
}




static void command(String[] args)
{
  long argsProcessed = 0;

  long  i, j, k, m, l, n, p, q, r, s ;
  long stmt, step;
  int subType = 0;
static final long SYNTAX =D.SYNTAX;
  vstring str1 = "", str2 = "", str3 = "", str4 = "", str5= "";
  nmbrString *nmbrTmpPtr;
  nmbrString *nmbrTmp = NULL_NMBRSTRING;
  nmbrString *nmbrSaveProof = NULL_NMBRSTRING;

  pntrString *pntrTmp = NULL_PNTRSTRING;
  pntrString *expandedProof = NULL_PNTRSTRING;
  flag tmpFlag;

  flag proofSavedFlag = 0;


  flag pipFlag;
  long outStatement;
  flag explicitTargets;
  long startStep; long endStep;

  long endIndent;
  flag essentialFlag;
  flag renumberFlag;
  flag unknownFlag;
  flag notUnifiedFlag;
  flag reverseFlag;
  long detailStep;
  flag noIndentFlag;
  long splitColumn;
  flag skipRepeatedSteps;
  flag texFlag;
  flag saveFlag;
  flag fastFlag;
  long indentation;
  vstring labelMatch = "";

  flag axiomFlag;
  flag treeFlag;
  flag countStepsFlag;
  flag matchFlag;
  vstring matchList = "";
  vstring traceToList = "";
  flag recursiveFlag;
  long fromLine, toLine;
  flag joinFlag;
  long searchWindow;
  File type_fp;
  long maxEssential;
  nmbrString *essentialFlags = NULL_NMBRSTRING;

  long improveDepth;
  flag searchAlg;
  flag searchUnkSubproofs;
  flag dummyVarIsoFlag;
  long improveAllIter;
  flag proofStepUnk;

  flag texHeaderFlag;
  flag commentOnlyFlag;
  flag briefFlag;
  flag linearFlag;
  vstring bgcolor = "";


  flag verboseMode, mayGrowFlag ;
  long prntStatus;
  flag hasWildCard;
  long exceptPos;
  flag mathboxFlag;
  long thisMathboxStartStmt;
  flag forwFlag;
  long forbidMatchPos;
  vstring forbidMatchList = "";
  long noNewAxiomsMatchPos;
  vstring noNewAxiomsMatchList = "";
  long allowNewAxiomsMatchPos;
  vstring allowNewAxiomsMatchList = "";
  vstring traceProofFlags = "";
  vstring traceTrialFlags = "";
  flag overrideFlag;

  struct pip_struct saveProofForReverting = {
       NULL_NMBRSTRING, NULL_PNTRSTRING, NULL_PNTRSTRING, NULL_PNTRSTRING };

  long origCompressedLength;
  long oldCompressedLength = 0;
  long newCompressedLength = 0;
  long forwardCompressedLength = 0;
  long forwardLength = 0;
  vstring saveZappedProofSectionPtr;
  long saveZappedProofSectionLen;
  flag saveZappedProofSectionChanged;

  struct pip_struct saveOrigProof = {
       NULL_NMBRSTRING, NULL_PNTRSTRING, NULL_PNTRSTRING, NULL_PNTRSTRING };

  struct pip_struct save1stPassProof = {
       NULL_NMBRSTRING, NULL_PNTRSTRING, NULL_PNTRSTRING, NULL_PNTRSTRING };

  long forwRevPass;

  long sourceStatement;

  flag showLemmas;
  flag noVersioning;
  long theoremsPerPage;


  flag commandProcessedFlag = 0;
  File list1_fp;
  File list2_fp;
  File list3_fp;
  vstring list2_fname = "", list2_ftmpname = "";
  vstring list3_ftmpname = "";
  vstring oldstr = "", newstr = "";
  long lines, changedLines, oldChangedLines, twoMatches, p1, p2;
  long firstChangedLine;
  flag cmdMode, changedFlag, outMsgFlag;
  double sum;
  vstring bufferedLine = "";
  vstring tagStartMatch = "";
  long tagStartCount = 0;
  vstring tagEndMatch = "";
  long tagEndCount = 0;
  long tagStartCounter = 0;
  long tagEndCounter = 0;






  double timeTotal = 0;
  double timeIncr = 0;
  flag printTime;


  flag defaultScrollMode = 1;

  p = 0;
  q = 0;
  s = 0;
  texHeaderFlag = 0;
  firstChangedLine = 0;
  tagStartCount = 0;
  tagEndCount = 0;
  tagStartCounter = 0;
  tagEndCounter = 0;

  while (true) {

    if (g_listMode) {

      if (argc > 1 && commandProcessedFlag &&
             g_commandFileNestingLevel == 0) return;
    }

    g_errorCount = 0;


    let(&str1,"");
    let(&str2,"");
    let(&str3,"");
    let(&str4,"");
    let(&str5,"");
    nmbrLet(&nmbrTmp, NULL_NMBRSTRING);
    pntrLet(&pntrTmp, NULL_PNTRSTRING);
    nmbrLet(&nmbrSaveProof, NULL_NMBRSTRING);
    nmbrLet(&essentialFlags, NULL_NMBRSTRING);
    j = nmbrLen(g_rawArgNmbr);
    if (j != g_rawArgs) bug(1110);
    j = pntrLen(g_rawArgPntr);
    if (j != g_rawArgs) bug(1111);
    g_rawArgs = 0;
    for (i = 0; i < j; i++) let((vstring *)(&g_rawArgPntr[i]), "");
    pntrLet(&g_rawArgPntr, NULL_PNTRSTRING);
    nmbrLet(&g_rawArgNmbr, NULL_NMBRSTRING);
    j = pntrLen(g_fullArg);
    for (i = 0; i < j; i++) let((vstring *)(&g_fullArg[i]),"");
    pntrLet(&g_fullArg,NULL_PNTRSTRING);
    j = pntrLen(expandedProof);
    if (j) {
      for (i = 0; i < j; i++) {
        let((vstring *)(&expandedProof[i]),"");
      }
     pntrLet(&expandedProof,NULL_PNTRSTRING);
    }

    let(&list2_fname, "");
    let(&list2_ftmpname, "");
    let(&list3_ftmpname, "");
    let(&oldstr, "");
    let(&newstr, "");
    let(&labelMatch, "");


    g_midiFlag = 0;

    if (g_memoryStatus) {


      print2("Memory:  string %ld xxxString %ld\n",db,db3);

      getPoolStats(&i, &j, &k);
      print2("Pool:  free alloc %ld  used alloc %ld  used actual %ld\n",i,j,k);
    }

    if (!g_toolsMode) {
      if (g_PFASmode) {
        let(&g_commandPrompt,"MM-PA> ");
      } else {
        let(&g_commandPrompt,"MM> ");
      }
    } else {
      if (g_listMode) {
        let(&g_commandPrompt,"Tools> ");
      } else {
        let(&g_commandPrompt,"TOOLS> ");
      }
    }

    let(&g_commandLine,"");

    if (!commandProcessedFlag && argc > 1 && argsProcessed < argc - 1
        && g_commandFileNestingLevel == 0) {

      if (g_listMode) {
        for (i = 1; i < argc; i++) {
          argsProcessed++;
          if (instr(1, argv[i], " ") || instr(1, argv[i], "\t")
              || instr(1, argv[i], "\"") || instr(1, argv[i], "'")
              || (argv[i])[0] == 0) {

            if (instr(1, argv[i], "\"")) {
              let(&str1, cat("'", argv[i], "'", null));
            } else {

              let(&str1, cat("\"", argv[i], "\"", null));
            }
          } else {
            let(&str1, argv[i]);
          }
          let(&g_commandLine, cat(g_commandLine, (i == 1) ? "" : " ", str1, null));
        }
      } else {
        argsProcessed++;
        g_scrollMode = 0;
        let(&g_commandLine, cat(g_commandLine, argv[argsProcessed], null));
        if (argc == 2 && instr(1, argv[1], " ") == 0) {
          if (instr(1, g_commandLine, "\"") || instr(1, g_commandLine, "'")) {

            let(&g_commandLine, cat("READ ", g_commandLine, null));
          } else {

            let(&g_commandLine, cat("READ \"", g_commandLine, "\"", null));
          }


        }
      }
      print2("%s\n", cat(g_commandPrompt, g_commandLine, null));
    } else {

      g_commandLine = cmdInput1(g_commandPrompt);
    }
    if (argsProcessed == argc && !commandProcessedFlag) {
      commandProcessedFlag = 1;
      g_scrollMode = defaultScrollMode;
    }
    if (argsProcessed == argc - 1) {
      argsProcessed++;
      if (g_toolsMode) {
        commandProcessedFlag = 1;
      }
    }



    if (g_commandLine[0] == '\'' || g_commandLine[0] == '\"') {

      if (!system(null)) {
        print2("?This computer does not accept an operating system command.\n");
        continue;
      } else {

        let(&str1, right(g_commandLine, 2));
        if (g_commandLine[0]) {
          if (g_commandLine[0] == g_commandLine[strlen(g_commandLine) - 1]) {
            let(&str1, left(str1, (long)(strlen(str1)) - 1));
          }
        }

        (void)system(str1);

        continue;
      }
    }

    parseCommandLine(g_commandLine);
    if (g_rawArgs == 0) {
      continue;
    }
    if (!processCommandLine()) {
      continue;
    }

    if (g_commandEcho || (g_toolsMode && g_listFile_fp != null)) {

      k = pntrLen(g_fullArg);
      let(&str1,"");
      for (i = 0; i < k; i++) {
        if (instr(1, g_fullArg[i], " ") || instr(1, g_fullArg[i], "\t")
            || instr(1, g_fullArg[i], "\"") || instr(1, g_fullArg[i], "'")
            || ((char *)(g_fullArg[i]))[0] == 0) {
          if (instr(1, g_fullArg[i], "\"")) {
            let(&str1, cat(str1, "'", g_fullArg[i], "' ", null));
          } else {

            let(&str1, cat(str1, "\"", g_fullArg[i], "\" ", null));
          }
        } else {
          let(&str1, cat(str1, g_fullArg[i], " ", null));
        }
      }
      let(&str1, left(str1, (long)(strlen(str1)) - 1));
      if (g_toolsMode && g_listFile_fp != null) {

        fprintf(g_listFile_fp, "%s\n", str1);
      }
      if (g_commandEcho) {

        let(&str1, cat("!", str1, null));
        printLongLine(str1, "~", " ");
      }
    }

    if (cmdMatches("BEEP") || cmdMatches("B")) {
      print2("%c",7);
      continue;
    }

    if (cmdMatches("HELP")) {

      k = pntrLen(g_fullArg);
      let(&str1,"");
      for (i = 0; i < k; i++) {
        let(&str1, cat(str1, g_fullArg[i], " ", null));
      }
      let(&str1, left(str1, (long)(strlen(str1)) - 1));
      if (g_toolsMode) {
        help0(str1);
        help1(str1);
      } else {
        help1(str1);
        help2(str1);
        help3(str1);
      }
      continue;
    }


    if (cmdMatches("SET SCROLL")) {
      if (cmdMatches("SET SCROLL CONTINUOUS")) {
        defaultScrollMode = 0;
        g_scrollMode = 0;
        print2("Continuous scrolling is now in effect.\n");
      } else {
        defaultScrollMode = 1;
        g_scrollMode = 1;
        print2("Prompted scrolling is now in effect.\n");
      }
      continue;
    }

    if (cmdMatches("EXIT") || cmdMatches("QUIT")
        || cmdMatches("_EXIT_PA")) {


      if (cmdMatches("_EXIT_PA")) {
        if (!g_PFASmode || (g_toolsMode && !g_listMode)) bug(1127);

      }

      if (g_toolsMode && !g_listMode) {

        if (!g_PFASmode) {
          print2(
 "Exiting the Text Tools.  Type EXIT again to exit Metamath.\n");
        } else {
          print2(
 "Exiting the Text Tools.  Type EXIT again to exit the Proof Assistant.\n");
        }
        g_toolsMode = 0;
        continue;
      }

      if (g_PFASmode) {

        if (g_proofChanged &&
              (processUndoStack(null, PUS_GET_STATUS, "", 0)
                 || proofSavedFlag)) {
          print2(
              "Warning:  You have not saved changes to the proof of \"%s\".\n",
              g_Statement[g_proveStatement].labelName);

          if (switchPos("/ FORCE") == 0) {
            str1 = cmdInput1("Do you want to EXIT anyway (Y, N) <N>? ");
            if (str1[0] != 'y' && str1[0] != 'Y') {
              print2("Use SAVE NEW_PROOF to save the proof.\n");
              continue;
            }
          } else {

            print2("Do you want to EXIT anyway (Y, N) <N>? Y\n");
          }
        }

        g_proofChanged = 0;
        processUndoStack(null, PUS_INIT, "", 0);
        proofSavedFlag = 0;

        print2(
 "Exiting the Proof Assistant.  Type EXIT again to exit Metamath.\n");


        deallocProofStruct(&g_ProofInProgress);


        g_PFASmode = 0;
        continue;
      } else {
        if (g_sourceChanged) {
          print2("Warning:  You have not saved changes to the source.\n");

          if (switchPos("/ FORCE") == 0) {
            str1 = cmdInput1("Do you want to EXIT anyway (Y, N) <N>? ");
            if (str1[0] != 'y' && str1[0] != 'Y') {
              print2("Use WRITE SOURCE to save the changes.\n");
              continue;
            }
          } else {

            print2("Do you want to EXIT anyway (Y, N) <N>? Y\n");
          }
          g_sourceChanged = 0;
        }

        if (g_texFileOpenFlag) {
          print2("The %s file \"%s\" was closed.\n",
              g_htmlFlag ? "HTML" : "LaTeX", g_texFileName);
          printTexTrailer(texHeaderFlag);
          fclose(g_texFilePtr);
          g_texFileOpenFlag = 0;
        }
        if (g_logFileOpenFlag) {
          print2("The log file \"%s\" was closed %s %s.\n",g_logFileName,
              date(),time_());
          fclose(g_logFilePtr);
          g_logFileOpenFlag = 0;
        }



        freeCommandLine();
        freeInOu();
        memFreePoolPurge(0);
        eraseSource();
        freeData();
        let(&g_commandPrompt,"");
        let(&g_commandLine,"");
        let(&g_input_fn,"");
        let(&g_contributorName, "");

        return;
      }
    }

    if (cmdMatches("SUBMIT")) {
      if (g_commandFileNestingLevel == MAX_COMMAND_FILE_NESTING) {
        printf("?The SUBMIT nesting level has been exceeded.\n");
        continue;
      }
      g_commandFilePtr[g_commandFileNestingLevel + 1] = fSafeOpen(g_fullArg[1], "r",
          0);
      if (!g_commandFilePtr[g_commandFileNestingLevel + 1]) continue;

      g_commandFileNestingLevel++;
      g_commandFileName[g_commandFileNestingLevel] = "";
      let(&g_commandFileName[g_commandFileNestingLevel], g_fullArg[1]);


      g_commandFileSilent[g_commandFileNestingLevel] = 0;
      if (switchPos("/ SILENT")
          || g_commandFileSilentFlag ) {
        g_commandFileSilent[g_commandFileNestingLevel] = 1;
      } else {
        g_commandFileSilent[g_commandFileNestingLevel] = 0;
      }
      g_commandFileSilentFlag = g_commandFileSilent[g_commandFileNestingLevel];
      if (!g_commandFileSilentFlag)
        print2("Taking command lines from file \"%s\"...\n",
            g_commandFileName[g_commandFileNestingLevel]);

      continue;
    }

    if (g_toolsMode) {

      static final long ADD_MODE =D.ADD_MODE;
      static final long DELETE_MODE =D.DELETE_MODE;
      static final long CLEAN_MODE =D.CLEAN_MODE;
      static final long SUBSTITUTE_MODE =D.SUBSTITUTE_MODE;
      static final long SWAP_MODE =D.SWAP_MODE;
      static final long INSERT_MODE =D.INSERT_MODE;
      static final long BREAK_MODE =D.BREAK_MODE;
      static final long BUILD_MODE =D.BUILD_MODE;
      static final long MATCH_MODE =D.MATCH_MODE;
      static final long RIGHT_MODE =D.RIGHT_MODE;
      static final long TAG_MODE =D.TAG_MODE;
      cmdMode = 0;
      if (cmdMatches("ADD")) cmdMode = ADD_MODE;
      else if (cmdMatches("DELETE")) cmdMode = DELETE_MODE;
      else if (cmdMatches("CLEAN")) cmdMode = CLEAN_MODE;
      else if (cmdMatches("SUBSTITUTE") || cmdMatches("S"))
        cmdMode = SUBSTITUTE_MODE;
      else if (cmdMatches("SWAP")) cmdMode = SWAP_MODE;
      else if (cmdMatches("INSERT")) cmdMode = INSERT_MODE;
      else if (cmdMatches("BREAK")) cmdMode = BREAK_MODE;
      else if (cmdMatches("BUILD")) cmdMode = BUILD_MODE;
      else if (cmdMatches("MATCH")) cmdMode = MATCH_MODE;
      else if (cmdMatches("RIGHT")) cmdMode = RIGHT_MODE;
      else if (cmdMatches("TAG")) cmdMode = TAG_MODE;
      if (cmdMode) {
        list1_fp = fSafeOpen(g_fullArg[1], "r", 0);
        if (!list1_fp) continue;
        if (cmdMode == RIGHT_MODE) {

          p = 0;
          while (linput(list1_fp, null, &str1)) {
            if (p < (signed)(strlen(str1))) p = (long)(strlen(str1));
          }
          rewind(list1_fp);
        }
        let(&list2_fname, g_fullArg[1]);
        if (list2_fname[strlen(list2_fname) - 2] == '~') {
          let(&list2_fname, left(list2_fname, (long)(strlen(list2_fname)) - 2));
          print2("The output file will be called %s.\n", list2_fname);
        }
        let(&list2_ftmpname, "");
        list2_ftmpname = fGetTmpName("zz~tools");
        list2_fp = fSafeOpen(list2_ftmpname, "w", 0);
        if (!list2_fp) continue;
        lines = 0;
        changedLines = 0;
        twoMatches = 0;
        changedFlag = 0;
        outMsgFlag = 0;
        switch (cmdMode) {
          case ADD_MODE:
            break;
          case TAG_MODE:
            let(&tagStartMatch, g_fullArg[4]);
            tagStartCount = (long)val(g_fullArg[5]);
            if (tagStartCount == 0) tagStartCount = 1;
            let(&tagEndMatch, g_fullArg[6]);
            tagEndCount = (long)val(g_fullArg[7]);
            if (tagEndCount == 0) tagEndCount = 1;
            tagStartCounter = 0;
            tagEndCounter = 0;
            break;
          case DELETE_MODE:
            break;
          case CLEAN_MODE:
            let(&str4, edit(g_fullArg[2], 32));
            q = 0;
            if (instr(1, str4, "P") > 0) q = q + 1;
            if (instr(1, str4, "D") > 0) q = q + 2;
            if (instr(1, str4, "G") > 0) q = q + 4;
            if (instr(1, str4, "B") > 0) q = q + 8;
            if (instr(1, str4, "R") > 0) q = q + 16;
            if (instr(1, str4, "C") > 0) q = q + 32;
            if (instr(1, str4, "E") > 0) q = q + 128;
            if (instr(1, str4, "Q") > 0) q = q + 256;
            if (instr(1, str4, "L") > 0) q = q + 512;
            if (instr(1, str4, "T") > 0) q = q + 1024;
            if (instr(1, str4, "U") > 0) q = q + 2048;
            if (instr(1, str4, "V") > 0) q = q + 4096;
            break;
          case SUBSTITUTE_MODE:
            let(&newstr, g_fullArg[3]);
            if (((vstring)(g_fullArg[4]))[0] == 'A' ||
                ((vstring)(g_fullArg[4]))[0] == 'a') {
              q = -1;
            } else {
              q = (long)val(g_fullArg[4]);
              if (q == 0) q = 1;
            }
            s = instr(1, g_fullArg[2], "\\n");
            if (s) {

              q = 1;
            }
            if (!strcmp(g_fullArg[3], "\\n")) {
              let(&newstr, "\n");
            }
            break;
          case SWAP_MODE:
            break;
          case INSERT_MODE:
            p = (long)val(g_fullArg[3]);
            break;
          case BREAK_MODE:
            outMsgFlag = 1;
            break;
          case BUILD_MODE:
            let(&str4, "");
            outMsgFlag = 1;
            break;
          case MATCH_MODE:
            outMsgFlag = 1;
        }
        let(&bufferedLine, "");
        while (true) {
          if (bufferedLine[0]) {

            let(&str1, bufferedLine);
            let(&bufferedLine, "");
          } else {
            if (!linput(list1_fp, null, &str1)) break;
          }
          lines++;
          oldChangedLines = changedLines;
          let(&str2, str1);
          switch (cmdMode) {
            case ADD_MODE:
              let(&str2, cat(g_fullArg[2], str1, g_fullArg[3], null));
              if (strcmp(str1, str2)) changedLines++;
              break;
            case TAG_MODE:
              if (tagStartCounter < tagStartCount) {
                if (instr(1, str1, tagStartMatch)) tagStartCounter++;
              }
              if (tagStartCounter == tagStartCount &&
                  tagEndCounter < tagEndCount) {
                let(&str2, cat(g_fullArg[2], str1, g_fullArg[3], null));
                if (strcmp(str1, str2)) changedLines++;
                if (instr(1, str1, tagEndMatch)) tagEndCounter++;
              }
              break;
            case DELETE_MODE:
              p1 = instr(1, str1, g_fullArg[2]);
              if (strlen(g_fullArg[2]) == 0) p1 = 1;
              p2 = instr(p1, str1, g_fullArg[3]);
              if (strlen(g_fullArg[3]) == 0) p2 = (long)strlen(str1) + 1;
              if (p1 != 0 && p2 != 0) {
                let(&str2, cat(left(str1, p1 - 1), right(str1, p2
                    + (long)strlen(g_fullArg[3])), null));
                changedLines++;
              }
              break;
            case CLEAN_MODE:
              if (q) {
                let(&str2, edit(str1, q));
                if (strcmp(str1, str2)) changedLines++;
              }
              break;
            case SUBSTITUTE_MODE:
              let(&str2, str1);
              p = 0;
              p1 = 0;

              k = 1;

              if (((vstring)(g_fullArg[5]))[0] != 0) {
                if (!instr(1, str2, g_fullArg[5])) {

                  k = 0;
                }
              }

              if (s && k) {

                if (linput(list1_fp, null, &bufferedLine)) {

                  if (instr(1, cat(str1, "\\n", bufferedLine, null),
                      g_fullArg[2])) {
                    let(&str2, cat(str1, "\\n", bufferedLine, null));
                    let(&bufferedLine, "");
                  } else {
                    k = 0;
                  }
                } else {
                  print2("Warning: file %s has an odd number of lines\n",
                      g_fullArg[1]);
                }
              }

              while (k) {
                p1 = instr(p1 + 1, str2, g_fullArg[2]);
                if (!p1) break;
                p++;
                if (p == q || q == -1) {
                  let(&str2, cat(left(str2, p1 - 1), newstr,
                      right(str2, p1 + (long)strlen(g_fullArg[2])), null));
                  if (newstr[0] == '\n') {

                    lines++;
                    changedLines++;
                  }
                  p1 = p1 + (long)strlen(newstr) - 1;

                  if (q != -1) break;
                }
              }
              if (strcmp(str1, str2)) changedLines++;
              break;
            case SWAP_MODE:
              p1 = instr(1, str1, g_fullArg[2]);
              if (p1) {
                p2 = instr(p1 + 1, str1, g_fullArg[2]);
                if (p2) twoMatches++;
                let(&str2, cat(right(str1, p1) + (long)strlen(g_fullArg[2]),
                    g_fullArg[2], left(str1, p1 - 1), null));
                if (strcmp(str1, str2)) changedLines++;
              }
              break;
            case INSERT_MODE:
              if ((signed)(strlen(str2)) < p - 1)
                let(&str2, cat(str2, space(p - 1 - (long)strlen(str2)), null));
              let(&str2, cat(left(str2, p - 1), g_fullArg[2],
                  right(str2, p), null));
              if (strcmp(str1, str2)) changedLines++;
              break;
            case BREAK_MODE:
              let(&str2, str1);
              changedLines++;
              for (i = 0; i < (signed)(strlen(g_fullArg[2])); i++) {
                p = 0;
                while (true) {
                  p = instr(p + 1, str2, chr(((vstring)(g_fullArg[2]))[i]));
                  if (!p) break;

                  let(&str2, cat(left(str2, p - 1), " ",
                      mid(str2, p, 1),
                      " ", right(str2, p + 1), null));

                  p += 2;
                }
              }
              let(&str2, edit(str2, 8 + 16 + 128));
              for (p = (long)strlen(str2) - 1; p >= 0; p--) {
                if (str2[p] == ' ') {
                  str2[p] = '\n';
                  changedLines++;
                }
              }
              if (!str2[0]) changedLines--;
              break;
            case BUILD_MODE:
              if (str2[0] != 0) {
                if (str4[0] == 0) {
                  let(&str4, str2);
                } else {
                  if ((long)strlen(str4) + (long)strlen(str2) > 72) {
                    let(&str4, cat(str4, "\n", str2, null));
                    changedLines++;
                  } else {
                    let(&str4, cat(str4, " ", str2, null));
                  }
                }
                p = instr(1, str4, "\n");
                if (p) {
                  let(&str2, left(str4, p - 1));
                  let(&str4, right(str4, p + 1));
                } else {
                  let(&str2, "");
                }
              }
              break;
            case MATCH_MODE:
              if (((vstring)(g_fullArg[2]))[0] == 0) {

                p = str1[0];
              } else {
                p = instr(1, str1, g_fullArg[2]);
              }
              if (((vstring)(g_fullArg[3]))[0] == 'n' ||
                  ((vstring)(g_fullArg[3]))[0] == 'N') {
                p = !p;
              }
              if (p) changedLines++;
              break;
            case RIGHT_MODE:
              let(&str2, cat(space(p - (long)strlen(str2)), str2, null));
              if (strcmp(str1, str2)) changedLines++;
              break;
          }
          if (lines == 1) let(&str3, left(str2, 79));
          if (oldChangedLines != changedLines && !changedFlag) {
            changedFlag = 1;
            let(&str3, left(str2, 79));
            firstChangedLine = lines;
            if ((cmdMode == SUBSTITUTE_MODE && newstr[0] == '\n')
                || cmdMode == BUILD_MODE)  {
              firstChangedLine = 1;
            }
          }
          if (((cmdMode != BUILD_MODE && cmdMode != BREAK_MODE)
              || str2[0] != 0)
              && (cmdMode != MATCH_MODE || p))
            fprintf(list2_fp, "%s\n", str2);
        }
        if (cmdMode == BUILD_MODE) {
          if (str4[0]) {

            fprintf(list2_fp, "%s\n", str4);
            changedLines++;
            if (!str3[0]) {
              let(&str3, str4);
            }
          }
        }

        p = instr(1, str3, "\n");
        if (p) let(&str3, left(str3, p - 1));
        if (!outMsgFlag) {

          if (!changedFlag) {
            if (!lines) {
              print2("The file %s has no lines.\n", g_fullArg[1]);
            } else {
              print2(
"The file %s has %ld line%s; none were changed.  First line:\n",
                list2_fname, lines, (lines == 1) ? "" : "s");
              print2("%s\n", str3);
            }
          } else {
            print2(
"The file %s has %ld line%s; %ld w%s changed.  First changed line is %ld:\n",
                list2_fname,
                lines,  (lines == 1) ? "" : "s",
                changedLines,  (changedLines == 1) ? "as" : "ere",
                firstChangedLine);
            print2("%s\n", str3);
          }
          if (twoMatches > 0) {

            print2(
"Warning:  %ld line%s more than one \"%s\".  The first one was used.\n",
                twoMatches, (twoMatches == 1) ? " has" : "s have", g_fullArg[2]);
          }
        } else {

          print2(
"The input had %ld line%s, the output has %ld line%s.%s\n",
              lines, (lines == 1) ? "" : "s",
              changedLines, (changedLines == 1) ? "" : "s",
              (changedLines == 0) ? "" : " First output line:");
          if (changedLines != 0) print2("%s\n", str3);
        }
        fclose(list1_fp);
        fclose(list2_fp);
        fSafeRename(list2_ftmpname, list2_fname);

        let(&tagStartMatch, "");
        let(&tagEndMatch, "");
        continue;
      }

      static final long SORT_MODE =D.SORT_MODE;
      static final long UNDUPLICATE_MODE =D.UNDUPLICATE_MODE;
      static final long DUPLICATE_MODE =D.DUPLICATE_MODE;
      static final long UNIQUE_MODE =D.UNIQUE_MODE;
      static final long REVERSE_MODE =D.REVERSE_MODE;
      cmdMode = 0;
      if (cmdMatches("SORT")) cmdMode = SORT_MODE;
      else if (cmdMatches("UNDUPLICATE")) cmdMode = UNDUPLICATE_MODE;
      else if (cmdMatches("DUPLICATE")) cmdMode = DUPLICATE_MODE;
      else if (cmdMatches("UNIQUE")) cmdMode = UNIQUE_MODE;
      else if (cmdMatches("REVERSE")) cmdMode = REVERSE_MODE;
      if (cmdMode) {
        list1_fp = fSafeOpen(g_fullArg[1], "r", 0);
        if (!list1_fp) continue;
        let(&list2_fname, g_fullArg[1]);
        if (list2_fname[strlen(list2_fname) - 2] == '~') {
          let(&list2_fname, left(list2_fname, (long)strlen(list2_fname) - 2));
          print2("The output file will be called %s.\n", list2_fname);
        }
        let(&list2_ftmpname, "");
        list2_ftmpname = fGetTmpName("zz~tools");
        list2_fp = fSafeOpen(list2_ftmpname, "w", 0);
        if (!list2_fp) continue;


        lines = 0;
        while (linput(list1_fp, null, &str1)) lines++;
        if (cmdMode != SORT_MODE  && cmdMode != REVERSE_MODE) {
          print2("The input file has %ld lines.\n", lines);
        }


        fclose(list1_fp);
        list1_fp = fSafeOpen(g_fullArg[1], "r", 0);

        pntrLet(&pntrTmp, pntrSpace(lines));

        for (i = 0; i < lines; i++) linput(list1_fp, null,
            (vstring *)(&pntrTmp[i]));


        if (cmdMode != REVERSE_MODE) {
          if (cmdMode == SORT_MODE) {
            g_qsortKey = g_fullArg[2];
          } else {
            g_qsortKey = "";
          }
          qsort(pntrTmp, (size_t)lines, sizeof(void *), qsortStringCmp);
        } else {
          for (i = lines / 2; i < lines; i++) {
            g_qsortKey = pntrTmp[i];
            pntrTmp[i] = pntrTmp[lines - 1 - i];
            pntrTmp[lines - 1 - i] = g_qsortKey;
          }
        }


        changedLines = 0;
        let(&str3, "");
        for (i = 0; i < lines; i++) {
          j = 0;
          switch (cmdMode) {
            case SORT_MODE:
            case REVERSE_MODE:
              j = 1;
              break;
            case UNDUPLICATE_MODE:
              if (i == 0) {
                j = 1;
              } else {
                if (strcmp((vstring)(pntrTmp[i - 1]), (vstring)(pntrTmp[i]))) {
                  j = 1;
                }
              }
              break;
            case DUPLICATE_MODE:
              if (i > 0) {
                if (!strcmp((vstring)(pntrTmp[i - 1]), (vstring)(pntrTmp[i]))) {
                  if (i == lines - 1) {
                    j = 1;
                  } else {
                    if (strcmp((vstring)(pntrTmp[i]),
                        (vstring)(pntrTmp[i + 1]))) {
                      j = 1;
                    }
                  }
                }
              }
              break;
            case UNIQUE_MODE:
              if (i < lines - 1) {
                if (strcmp((vstring)(pntrTmp[i]), (vstring)(pntrTmp[i + 1]))) {
                  if (i == 0) {
                    j = 1;
                  } else {
                    if (strcmp((vstring)(pntrTmp[i - 1]),
                        (vstring)(pntrTmp[i]))) {
                      j = 1;
                    }
                  }
                }
              } else {
                if (i == 0) {
                  j = 1;
                } else {
                  if (strcmp((vstring)(pntrTmp[i - 1]),
                        (vstring)(pntrTmp[i]))) {
                      j = 1;
                  }
                }
              }
              break;
          }
          if (j) {
            fprintf(list2_fp, "%s\n", (vstring)(pntrTmp[i]));
            changedLines++;
            if (changedLines == 1)
              let(&str3, left((vstring)(pntrTmp[i]), 79));
          }
        }
        print2("The output file has %ld lines.  The first line is:\n",
            changedLines);
        print2("%s\n", str3);


        for (i = 0; i < lines; i++) let((vstring *)(&pntrTmp[i]), "");
        pntrLet(&pntrTmp,NULL_PNTRSTRING);

        fclose(list1_fp);
        fclose(list2_fp);
        fSafeRename(list2_ftmpname, list2_fname);
        continue;
      }

      if (cmdMatches("PARALLEL")) {
        list1_fp = fSafeOpen(g_fullArg[1], "r", 0);
        list2_fp = fSafeOpen(g_fullArg[2], "r", 0);
        if (!list1_fp) continue;
        if (!list2_fp) continue;
        let(&list3_ftmpname, "");
        list3_ftmpname = fGetTmpName("zz~tools");
        list3_fp = fSafeOpen(list3_ftmpname, "w", 0);
        if (!list3_fp) continue;

        p1 = 1; p2 = 1;
        p = 0; q = 0;
        j = 0;
        let(&str3, "");
        while (true) {
          let(&str1, "");
          if (p1) {
            p1 = linput(list1_fp, null, &str1);
            if (p1) p++;
            else let(&str1, "");
          }
          let(&str2, "");
          if (p2) {
            p2 = linput(list2_fp, null, &str2);
            if (p2) q++;
            else let(&str2, "");
          }
          if (!p1 && !p2) break;
          let(&str4, cat(str1, g_fullArg[4], str2, null));
          fprintf(list3_fp, "%s\n", str4);
          if (!j) {
            let(&str3, str4);
            j = 1;
          }
        }
        if (p == q) {
          print2(
"The input files each had %ld lines.  The first output line is:\n", p);
        } else {
          print2(
"Warning: file \"%s\" had %ld lines while file \"%s\" had %ld lines.\n",
              g_fullArg[1], p, g_fullArg[2], q);
          if (p < q) p = q;
          print2("The output file \"%s\" has %ld lines.  The first line is:\n",
              g_fullArg[3], p);
        }
        print2("%s\n", str3);

        fclose(list1_fp);
        fclose(list2_fp);
        fclose(list3_fp);
        fSafeRename(list3_ftmpname, g_fullArg[3]);
        continue;
      }


      if (cmdMatches("NUMBER")) {
        list1_fp = fSafeOpen(g_fullArg[1], "w", 0);
        if (!list1_fp) continue;
        j = (long)strlen(str(val(g_fullArg[2])));
        k = (long)strlen(str(val(g_fullArg[3])));
        if (k > j) j = k;
        for (i = (long)val(g_fullArg[2]); i <= val(g_fullArg[3]);
            i = i + (long)val(g_fullArg[4])) {
          let(&str1, str((double)i));
          fprintf(list1_fp, "%s\n", str1);
        }
        fclose(list1_fp);
        continue;
      }

      if (cmdMatches("COUNT")) {
        list1_fp = fSafeOpen(g_fullArg[1], "r", 0);
        if (!list1_fp) continue;
        p1 = 0;
        p2 = 0;
        lines = 0;
        q = 0;
        i = 0;
        j = 0;
        sum = 0.0;
        firstChangedLine = 0;
        while (linput(list1_fp, null, &str1)) {
          lines++;


          if (q < (signed)(strlen(str1))) {
            q = (long)strlen(str1);
            let(&str4, str1);
            i = lines;
            j = 0;
          }

          if (q == (signed)(strlen(str1))) {
            j++;
          }

          if (instr(1, str1, g_fullArg[2])) {
            if (!firstChangedLine) {
              firstChangedLine = lines;
              let(&str3, str1);
            }
            p1++;
            p = 0;
            while (true) {
              p = instr(p + 1, str1, g_fullArg[2]);
              if (!p) break;
              p2++;
            }
          }
          sum = sum + val(str1);
        }
        print2(
"The file has %ld lines.  The string \"%s\" occurs %ld times on %ld lines.\n",
            lines, g_fullArg[2], p2, p1);
        if (firstChangedLine) {
          print2("The first occurrence is on line %ld:\n", firstChangedLine);
          print2("%s\n", str3);
        }
        print2(
"The first longest line (out of %ld) is line %ld and has %ld characters:\n",
            j, i, q);
        printLongLine(str4, "    ", "");


        printLongLine(cat(
            "Stripping all but digits, \".\", and \"-\", the sum of lines is ",
            str((double)sum), null), "    ", " ");
        fclose(list1_fp);
        continue;
      }

      if (cmdMatches("TYPE") || cmdMatches("T")) {
        list1_fp = fSafeOpen(g_fullArg[1], "r", 0);
        if (!list1_fp) continue;
        if (g_rawArgs == 2) {
          n = 10;
        } else {
          if (((vstring)(g_fullArg[2]))[0] == 'A' ||
              ((vstring)(g_fullArg[2]))[0] == 'a') {
            n = -1;
          } else {
            n = (long)val(g_fullArg[2]);
          }
        }
        for (i = 0; i < n || n == -1; i++) {
          if (!linput(list1_fp, null, &str1)) break;
          if (!print2("%s\n", str1)) break;
        }
        fclose(list1_fp);
        continue;
      }

      if (cmdMatches("UPDATE")) {
        list1_fp = fSafeOpen(g_fullArg[1], "r", 0);
        list2_fp = fSafeOpen(g_fullArg[2], "r", 0);
        if (!list1_fp) continue;
        if (!list2_fp) continue;
        if (!getRevision(g_fullArg[4])) {
          print2(
"?The revision tag must be of the form /*nn*/ or /*#nn*/.  Please try again.\n");
          continue;
        }
        let(&list3_ftmpname, "");
        list3_ftmpname = fGetTmpName("zz~tools");
        list3_fp = fSafeOpen(list3_ftmpname, "w", 0);
        if (!list3_fp) continue;

        revise(list1_fp, list2_fp, list3_fp, g_fullArg[4],
            (long)val(g_fullArg[5]));

        fSafeRename(list3_ftmpname, g_fullArg[3]);
        continue;
      }

      if (cmdMatches("COPY") || cmdMatches("C")) {
        let(&list2_ftmpname, "");
        list2_ftmpname = fGetTmpName("zz~tools");
        list2_fp = fSafeOpen(list2_ftmpname, "w", 0);
        if (!list2_fp) continue;
        let(&str4, cat(g_fullArg[1], ",", null));
        lines = 0;
        j = 0;
        while (true) {
          if (!str4[0]) break;
          p = instr(1, str4, ",");
          let(&str3, left(str4, p - 1));
          let(&str4, right(str4, p + 1));
          list1_fp = fSafeOpen((str3), "r", 0);
          if (!list1_fp) {
            j = 1;
            break;
          }
          n = 0;
          while (linput(list1_fp, null, &str1)) {
            lines++; n++;
            fprintf(list2_fp, "%s\n", str1);
          }
          if (instr(1, g_fullArg[1], ",")) {
            print2("The input file \"%s\" has %ld lines.\n", str3, n);
          }
          fclose(list1_fp);
        }
        if (j) continue;
        fclose(list2_fp);
        print2("The output file \"%s\" has %ld lines.\n", g_fullArg[2], lines);
        fSafeRename(list2_ftmpname, g_fullArg[2]);
        continue;
      }

      print2("?This command has not been implemented yet.\n");
      continue;
    }

    if (cmdMatches("TOOLS")) {
      print2(
"Entering the Text Tools utilities.  Type HELP for help, EXIT to exit.\n");
      g_toolsMode = 1;
      continue;
    }

    if (cmdMatches("READ")) {


      if (g_sourceHasBeenRead == 1) {
        printLongLine(cat(
            "?Sorry, reading of more than one source file is not allowed.  ",
            "The file \"", g_input_fn, "\" has already been READ in.  ",

            "You may type ERASE to start over.  Note that additional source ",
        "files may be included in the source file with \"$[ <filename> $]\".",

            null),"  "," ");
        continue;
      }

      let(&g_input_fn, g_fullArg[1]);


      let(&str1, cat(g_rootDirectory, g_input_fn, null));
      g_input_fp = fSafeOpen(str1, "r", 0);
      if (!g_input_fp) continue;
      fclose(g_input_fp);

      readInput();


      if (switchPos("/ VERIFY")) {
        verifyProofs("*",1);
      } else {

      }



      if (g_sourceHasBeenRead == 1) {
        if (!g_errorCount) {
          let(&str1, "No errors were found.");
          if (!switchPos("/ VERIFY")) {
              let(&str1, cat(str1,
         "  However, proofs were not checked.  Type VERIFY PROOF *",
         " if you want to check them.",
              null));
          }
          printLongLine(str1, "", " ");
        } else {
          print2("\n");
          if (g_errorCount == 1) {
            print2("One error was found.\n");
          } else {
            print2("%ld errors were found.\n", (long)g_errorCount);
          }
        }
      }

      continue;
    }

    if (cmdMatches("WRITE SOURCE")) {
      let(&g_output_fn, g_fullArg[2]);







      if (switchPos("/ REWRAP") > 0) {
        r = 2;
      } else if (switchPos("/ FORMAT") > 0) {
        r = 1;
      } else {
        r = 0;
      }


      i = switchPos("/ EXTRACT");
      if (i > 0) {
        let(&str1, g_fullArg[i + 1]);
        if (r > 0
            || switchPos("/ SPLIT") > 0
            || switchPos("/ KEEP_INCLUDES") > 0) {
          print2(
"?You may not use / SPLIT, / REWRAP, or / KEEP_INCLUDES with / EXTRACT.\n");
          continue;
        }
      } else {
        let(&str1, "");
      }


      writeSource((char)r,
        ((switchPos("/ SPLIT") > 0) ? 1 : 0),
        ((switchPos("/ NO_VERSIONING") > 0) ? 1 : 0),
        ((switchPos("/ KEEP_INCLUDES") > 0) ? 1 : 0),
        str1
          );

      g_sourceChanged = 0;

      let(&str1, "");
      continue;
    }


    if (cmdMatches("WRITE THEOREM_LIST")) {

      static final long THEOREMS_PER_PAGE =D.THEOREMS_PER_PAGE;


      i = switchPos("/ THEOREMS_PER_PAGE");
      if (i != 0) {
        theoremsPerPage = (long)val(g_fullArg[i + 1]);
      } else {
        theoremsPerPage = THEOREMS_PER_PAGE;
      }
      showLemmas = (switchPos("/ SHOW_LEMMAS") != 0);
      noVersioning = (switchPos("/ NO_VERSIONING") != 0);


      g_htmlFlag = 1;
      if (switchPos("/ HTML") != 0) {
        if (switchPos("/ ALT_HTML") != 0) {
          print2("?Please specify only one of / HTML and / ALT_HTML.\n");
          continue;
        }
        g_altHtmlFlag = 0;
      } else {
        if (switchPos("/ ALT_HTML") != 0) g_altHtmlFlag = 1;
      }

      if (2 == readTexDefs(0 ,
          0  )) {
        continue;
      }


      writeTheoremList(theoremsPerPage, showLemmas,
          noVersioning);
      continue;
    }


    if (cmdMatches("WRITE BIBLIOGRAPHY")) {




      writeBibliography(g_fullArg[2],
          "*",
          0,
          0);
      continue;
    }


    if (cmdMatches("WRITE RECENT_ADDITIONS")) {


      static final long RECENT_COUNT =D.RECENT_COUNT;


      i = switchPos("/ LIMIT");
      if (i) {
        i = (long)val(g_fullArg[i + 1]);
      } else {
        i = RECENT_COUNT;
      }


      g_htmlFlag = 1;
      if (switchPos("/ HTML") != 0) {
        if (switchPos("/ ALT_HTML") != 0) {
          print2("?Please specify only one of / HTML and / ALT_HTML.\n");
          continue;
        }
        g_altHtmlFlag = 0;
      } else {
        if (switchPos("/ ALT_HTML") != 0) g_altHtmlFlag = 1;
      }


      if (2 == readTexDefs(0 ,
          0   )) {
        continue;
      }

      tmpFlag = 0;
      list1_fp = fSafeOpen(g_fullArg[2], "r", 0);
      if (list1_fp == null) {

        continue;
      }
      fclose(list1_fp);

      list2_fp = fSafeOpen(g_fullArg[2], "w", 0);
      if (list2_fp == null) {

        continue;
      }
      list1_fp = fSafeOpen(cat(g_fullArg[2], "~1", null), "r",
          0);
      if (list1_fp == null) bug(1117);


      while (true) {
        if (!linput(list1_fp, null, &str1)) {
          print2(
"?Error: Could not find \"<!-- #START# -->\" line in input file \"%s\".\n",
              g_fullArg[2]);
          tmpFlag = 1;
          break;
        }

        if (!strcmp(left(str1, 21), "<!-- last updated -->")) {
          let(&str1, cat(left(str1, 21), " <I>Last updated on ", date(),



            " at ", time_(), " ET.</I>", null));
        }
        fprintf(list2_fp, "%s\n", str1);
        if (!strcmp(str1, "<!-- #START# -->")) break;
      }
      if (tmpFlag) C.go2("wrrecent_error");


      parseDate(date(), &k , &l , &m );


      static final long START_YEAR=D.START_YEAR;
      n = 0;
      while (n < i  && m > START_YEAR + 1900 - 1) {




        buildDate(k, l, m, &str1);



        for (stmt = g_statements; stmt >= 1; stmt--) {

          if (g_Statement[stmt].type != (char)p_
                && g_Statement[stmt].type != (char)a_) {
            continue;
          }



          let(&str2, "");
          str2 = getContrib(stmt, MOST_RECENT_DATE);



          if (!strcmp(str2, str1)) {

            n++;
            let(&str3, "");
            str3 = getDescription(stmt);
            let(&str4, "");
            str4 = pinkHTML(stmt);


            let(&g_printString, "");
            g_outputToString = 1;
            print2("\n");
            printLongLine(cat(



                (stmt < g_extHtmlStmt)
                   ? "<TR>"
                   : (stmt < g_mathboxStmt)
                       ? cat("<TR BGCOLOR=", PURPLISH_BIBLIO_COLOR, ">",
                           null)
                       : cat("<TR BGCOLOR=", SANDBOX_COLOR, ">", null),

                "<TD NOWRAP>",



                str2,
                "</TD><TD ALIGN=CENTER><A HREF=\"",
                g_Statement[stmt].labelName, ".html\">",
                g_Statement[stmt].labelName, "</A>",
                str4, "</TD><TD ALIGN=LEFT>", null),

              " ",
              "\"");

            g_showStatement = stmt;
            g_outputToString = 0;
            g_texFilePtr = list2_fp;



            printTexComment(str3,
                0,
                PROCESS_EVERYTHING,
                0   );
            g_texFilePtr = null;
            g_outputToString = 1;


            let(&str4, "");
            str4 = getTexOrHtmlHypAndAssertion(stmt);
            printLongLine(cat("</TD></TR><TR",



                  (stmt < g_extHtmlStmt)
                     ? ">"
                     : (stmt < g_mathboxStmt)
                         ? cat(" BGCOLOR=", PURPLISH_BIBLIO_COLOR, ">",
                             null)
                         : cat(" BGCOLOR=", SANDBOX_COLOR, ">", null),


                "<TD COLSPAN=3 ALIGN=CENTER>",
                str4, "</TD></TR>", null),

                " ",
                "\"");

            g_outputToString = 0;
            fprintf(list2_fp, "%s", g_printString);
            let(&g_printString, "");

            if (n >= i ) break;


            g_outputToString = 1;
            printLongLine(cat("<TR BGCOLOR=white><TD COLSPAN=3>",
                "<FONT SIZE=-3>&nbsp;</FONT></TD></TR>", null),
                " ",
                "\"");



            j = 0;
            for (q = stmt - 1; q >= 1; q--) {
              if (g_Statement[q].type == (char)p_ ||
                  g_Statement[q].type == (char)a_ ) {
                j = q;
                break;
              }
            }
            if (j) print2("<!-- For script: previous = %s -->\n",
                g_Statement[j].labelName);

            print2("<!-- For script: current = %s -->\n",
                g_Statement[stmt].labelName);

            j = 0;
            for (q = stmt + 1; q <= g_statements; q++) {
              if (g_Statement[q].type == (char)p_ ||
                  g_Statement[q].type == (char)a_ ) {
                j = q;
                break;
              }
            }
            if (j) print2("<!-- For script: next = %s -->\n",
                g_Statement[j].labelName);


            g_outputToString = 0;
            fprintf(list2_fp, "%s", g_printString);
            let(&g_printString, "");

          }
        }

        if (k > 1) {
          k--;
        } else {
          k = 31;
          if (l > 1) {
            l--;
          } else {
            l = 12;
            m --;
          }
        }
      }



      while (true) {
        if (!linput(list1_fp, null, &str1)) {
          print2(
"?Error: Could not find \"<!-- #END# -->\" line in input file \"%s\".\n",
              g_fullArg[2]);
          tmpFlag = 1;
          break;
        }
        if (!strcmp(str1, "<!-- #END# -->")) {
          fprintf(list2_fp, "%s\n", str1);
          break;
        }
      }
      if (tmpFlag) C.go2("wrrecent_error");


      while (true) {
        if (!linput(list1_fp, null, &str1)) {
          break;
        }



        if (!strcmp("This page was last updated on ", left(str1, 30))) {
          let(&str1, cat(left(str1, 30), date(), ".", null));
        }

        fprintf(list2_fp, "%s\n", str1);
      }

      print2("The %ld most recent theorem(s) were written.\n", n);

     C.label("wrrecent_error");
      fclose(list1_fp);
      fclose(list2_fp);
      if (tmpFlag) {

        remove(g_fullArg[2]);
        rename(cat(g_fullArg[2], "~1", null), g_fullArg[2]);

        print2("?The file \"%s\" was not modified.\n", g_fullArg[2]);
      }
      continue;
    }


    if (cmdMatches("SHOW LABELS")) {
      linearFlag = 0;
      if (switchPos("/ LINEAR")) linearFlag = 1;
      if (switchPos("/ ALL")) {
        m = 1;
        print2(
"The labels that match are shown with statement number, label, and type.\n");
      } else {
        m = 0;
        print2(
"The assertions that match are shown with statement number, label, and type.\n");
      }
      j = 0;
      k = 0;
      let(&str2, "");
      static final long COL =D.COL;
      static final long MIN_SPACE =D.MIN_SPACE;
      for (i = 1; i <= g_statements; i++) {
        if (!g_Statement[i].labelName[0]) continue;
        if (!m && g_Statement[i].type != (char)p_ &&
            g_Statement[i].type != (char)a_) continue;

        if (!matchesList(g_Statement[i].labelName, g_fullArg[2], '*', '?')) {
          continue;
        }


        let(&str1, cat(str((double)i), " ",
            g_Statement[i].labelName, " $", chr(g_Statement[i].type),
            null));
        if (!str2[0]) {
          j = 0;
        }
        k = ((long)strlen(str2) + MIN_SPACE > j * COL)
            ? (long)strlen(str2) + MIN_SPACE : j * COL;

        if (k + (long)strlen(str1) > g_screenWidth || linearFlag) {
          if (j == 0) {

            printLongLine(str1, "", " ");
          } else {

            print2("%s\n", str2);
            let(&str2, str1);
            j = 1;
          }
        } else {

          if (j == 0) {
            let(&str2, str1);
          } else {
            let(&str2, cat(str2, space(k - (long)strlen(str2)), str1, null));
          }
          j++;
        }


      }

      if (str2[0]) {
        print2("%s\n", str2);
        let(&str2, "");
      }
      let(&str1, "");
      continue;
    }

    if (cmdMatches("SHOW DISCOURAGED")) {
      showDiscouraged();
      continue;
    }

    if (cmdMatches("SHOW SOURCE")) {


      s = getStatementNum(g_fullArg[2],
          1,
          g_statements + 1  ,
          1,
          1,
          1,
          1,
          0,
          1);
      if (s == -1) {
        continue;
      }
      g_showStatement = s;


      let(&str1, "");
      str1 = outputStatement(g_showStatement,
          0 );
      let(&str1,edit(str1,128));
      if (str1[strlen(str1)-1] == '\n') let(&str1, left(str1,
          (long)strlen(str1) - 1));
      printLongLine(str1, "", "");
      let(&str1,"");
      continue;
    }


    if (cmdMatches("SHOW STATEMENT") && (
        switchPos("/ HTML")
        || switchPos("/ BRIEF_HTML")
        || switchPos("/ ALT_HTML")
        || switchPos("/ BRIEF_ALT_HTML"))) {



      noVersioning = (switchPos("/ NO_VERSIONING") != 0);
      i = 5;
      if (noVersioning) i = i + 2;
      if (switchPos("/ TIME")) i = i + 2;
      if (g_rawArgs != i) {
        printLongLine(cat("?The HTML qualifiers may not be combined with",
            " others except / NO_VERSIONING and / TIME.\n", null), "  ", " ");
        continue;
      }


      printTime = 0;
      if (switchPos("/ TIME") != 0) {
        printTime = 1;
      }


      g_htmlFlag = 1;

      if (switchPos("/ BRIEF_HTML") || switchPos("/ BRIEF_ALT_HTML")) {
        if (strcmp(g_fullArg[2], "*")) {
          print2(
              "?For BRIEF_HTML or BRIEF_ALT_HTML, the label must be \"*\"\n");
          C.go2("htmlDone");
        }
        g_briefHtmlFlag = 1;
      } else {
        g_briefHtmlFlag = 0;
      }

      if (switchPos("/ ALT_HTML") || switchPos("/ BRIEF_ALT_HTML")) {
        g_altHtmlFlag = 1;
      } else {
        g_altHtmlFlag = 0;
      }

      q = 0;


      if (((char *)(g_fullArg[2]))[0] == '*' || g_briefHtmlFlag) {

        s = -2;
      } else {
        s = 1;
      }

      for (s = s + 0; s <= g_statements; s++) {

        if (s > 0 && g_briefHtmlFlag) break;


        if (s > 0) {
          if (!g_Statement[s].labelName[0]) continue;

          if (!matchesList(g_Statement[s].labelName, g_fullArg[2], '*', '?'))
            continue;
          if (g_Statement[s].type != (char)a_
              && g_Statement[s].type != (char)p_) continue;
        }

        q = 1;

        if (s > 0) {
          g_showStatement = s;
        } else {
          g_showStatement = 1;
        }



        g_htmlFlag = 1;

        switch (s) {
          case -2:
            let(&g_texFileName, "mmascii.html");
            break;
          case -1:
            let(&g_texFileName, "mmtheoremsall.html");
            break;
          case 0:
            let(&g_texFileName, "mmdefinitions.html");
            break;
          default:
            let(&g_texFileName, cat(g_Statement[g_showStatement].labelName, ".html",
                null));
        }
        print2("Creating HTML file \"%s\"...\n", g_texFileName);
        g_texFilePtr = fSafeOpen(g_texFileName, "w",
            noVersioning );
        if (!g_texFilePtr) C.go2("htmlDone");
        g_texFileOpenFlag = 1;
        printTexHeader((s > 0) ? 1 : 0 );
        if (!g_texDefsRead) {
          print2("?HTML generation was aborted due to the error above.\n");
          s = g_statements + 1;
          C.go2("ABORT_S");
        }

        if (s <= 0) {
          g_outputToString = 1;
          if (s == -2) {
            printLongLine(cat("<CENTER><FONT COLOR=", GREEN_TITLE_COLOR,
                "><B>",
                "Symbol to ASCII Correspondence for Text-Only Browsers",
                " (in order of appearance in $c and $v statements",
                     " in the database)",
                "</B></FONT></CENTER><P>", null), "", "\"");
          }

          if (!g_briefHtmlFlag) print2("<CENTER>\n");
          print2("<TABLE BORDER CELLSPACING=0 BGCOLOR=%s\n",
              MINT_BACKGROUND_COLOR);

          switch (s) {
            case -2:
              print2("SUMMARY=\"Symbol to ASCII correspondences\">\n");
              break;
            case -1:
              print2("SUMMARY=\"List of theorems\">\n");
              break;
            case 0:
              print2("SUMMARY=\"List of syntax, axioms and definitions\">\n");
              break;
          }
          switch (s) {
            case -2:
              print2("<TR ALIGN=LEFT><TD><B>\n");
              break;
            case -1:
              print2(
         "<CAPTION><B>List of Theorems</B></CAPTION><TR ALIGN=LEFT><TD><B>\n");
              break;
            case 0:
              printLongLine(cat(


                  "<CAPTION><B>List of Syntax, ",
                  "Axioms (<FONT COLOR=", GREEN_TITLE_COLOR, ">ax-</FONT>) and",
                  " Definitions (<FONT COLOR=", GREEN_TITLE_COLOR,
                  ">df-</FONT>)", "</B></CAPTION><TR ALIGN=LEFT><TD><B>",
                  null), "", "\"");
              break;
          }
          switch (s) {
            case -2:
              print2("Symbol</B></TD><TD><B>ASCII\n");
              break;
            case -1:
              print2(
                  "Ref</B></TD><TD><B>Description\n");
              break;
            case 0:
              printLongLine(cat(
                  "Ref</B></TD><TD><B>",
                "Expression (see link for any distinct variable requirements)",
                null), "", "\"");
              break;
          }
          print2("</B></TD></TR>\n");
          m = 0;
          let(&str3, "");
          let(&bgcolor, MINT_BACKGROUND_COLOR);
          for (i = 1; i <= g_statements; i++) {




            if (s != -2 && (i == g_extHtmlStmt || i == g_mathboxStmt)) {
              if (i == g_extHtmlStmt) {
                let(&bgcolor, PURPLISH_BIBLIO_COLOR);
              } else {
                let(&bgcolor, SANDBOX_COLOR);
              }
              printLongLine(cat("<TR BGCOLOR=", bgcolor,
                  "><TD COLSPAN=2 ALIGN=CENTER><A NAME=\"startext\"></A>",
                  "The list of syntax, axioms (ax-) and definitions (df-) for",
                  " the <B><FONT COLOR=", GREEN_TITLE_COLOR, ">",
                  (i == g_extHtmlStmt) ?
                          C.label("g_extHtmlTitle") ;


                    "User Mathboxes",
                  "</FONT></B> starts here</TD></TR>", null), "", "\"");
            }

            if (g_Statement[i].type == (char)p_ ||
                g_Statement[i].type == (char)a_ ) m++;
            if ((s == -1 && g_Statement[i].type != (char)p_)
                || (s == 0 && g_Statement[i].type != (char)a_)
                || (s == -2 && g_Statement[i].type != (char)c_
                    && g_Statement[i].type != (char)v_)
                ) continue;
            switch (s) {
              case -2:

                for (j = 0; j < g_Statement[i].mathStringLen; j++) {
                  let(&str1, g_MathToken[(g_Statement[i].mathString)[j]].tokenName);

                  if (!instr(1, str3, cat(" ", str1, " ", null))) {
                    let(&str3, cat(str3, " ", str1, " ", null));
                    let(&str2, "");
                    str2 = tokenToTex(g_MathToken[(g_Statement[i].mathString)[j]
                        ].tokenName, i);
                    if (!str2[0]) continue;

                    for (k = 0; k < (signed)(strlen(str1)); k++) {
                      if (str1[k] == '&') {
                        let(&str1, cat(left(str1, k), "&amp;",
                            right(str1, k + 2), null));
                        k = k + 4;
                      }
                      if (str1[k] == '<') {
                        let(&str1, cat(left(str1, k), "&lt;",
                            right(str1, k + 2), null));
                        k = k + 3;
                      }
                      if (str1[k] == '>') {
                        let(&str1, cat(left(str1, k), "&gt;",
                            right(str1, k + 2), null));
                        k = k + 3;
                      }
                    }
                    printLongLine(cat("<TR ALIGN=LEFT><TD>",
                        (g_altHtmlFlag ? cat("<SPAN ", g_htmlFont, ">", null) : ""),

                        str2,
                        (g_altHtmlFlag ? "</SPAN>" : ""),
                        "&nbsp;",
                        "</TD><TD><TT>",
                        str1,
                        "</TT></TD></TR>", null), "", "\"");
                  }
                }

                fprintf(g_texFilePtr, "%s", g_printString);
                let(&g_printString, "");
                break;
              case -1:
              case 0:


                let(&str1, "");
                if (s == 0 || g_briefHtmlFlag) {
                  let(&str1, "");

                  str1 = getTexOrHtmlHypAndAssertion(i);

                  let(&str1, cat(str1, "</TD></TR>", null));
                }

                if (g_briefHtmlFlag) {

                  k = ((g_Statement[i].pinkNumber - 1) /
                      THEOREMS_PER_PAGE) + 1;
                  let(&str2, cat("<TR ALIGN=LEFT><TD ALIGN=LEFT>",

                      "<FONT COLOR=ORANGE>",
                      str((double)(g_Statement[i].pinkNumber)), "</FONT> ",
                      "<FONT COLOR=GREEN><A HREF=\"",
                      "mmtheorems", (k == 1) ? "" : str((double)k), ".html#",
                      g_Statement[i].labelName,
                      "\">", g_Statement[i].labelName,
                      "</A></FONT>", null));
                  let(&str1, cat(str2, " ", str1, null));
                } else {

                  let(&str4, "");
                  str4 = pinkHTML(i);
                  let(&str2, cat("<TR BGCOLOR=", bgcolor,
                      " ALIGN=LEFT><TD><A HREF=\"",
                      g_Statement[i].labelName,
                      ".html\">", g_Statement[i].labelName,
                      "</A>", str4, null));
                  let(&str1, cat(str2, "</TD><TD>", str1, null));
                }


                print2("\n");
                printLongLine(str1, "", "\"");

                if (s == 0 || g_briefHtmlFlag) {


                } else {
                  let(&str1, "");
                  str1 = getDescription(i);
                  if (strlen(str1) > 29)
                    let(&str1, cat(left(str1, 26), "...", null));
                  let(&str1, cat(str1, "</TD></TR>", null));
                  printLongLine(str1, "", "\"");
                }

                fprintf(g_texFilePtr, "%s", g_printString);
                let(&g_printString, "");
                break;
            }
          }
          g_outputToString = 0;
          let(&bgcolor, "");

        } else {


          if (printTime == 1) {
            getRunTime(&timeIncr);
          }


          typeStatement(g_showStatement,
              0 ,
              0 ,
              1 ,
              1 );


          if (printTime == 1) {
            getRunTime(&timeIncr);
            print2("SHOW STATEMENT run time = %6.2f sec for \"%s\"\n",
                timeIncr,
                g_texFileName);
          }

        }

        C.label("ABORT_S");

        printTexTrailer(1 );
        fclose(g_texFilePtr);
        g_texFileOpenFlag = 0;
        let(&g_texFileName,"");

      }

      if (!q) {

        printLongLine(cat("?There is no statement whose label matches \"",
            g_fullArg[2], "\".  ",
            "Use SHOW LABELS for a list of valid labels.", null), "", " ");
        continue;
      }

      C.label("htmlDone"):
      continue;
    }




    if (cmdMatches("SHOW STATEMENT") && switchPos("/ MNEMONICS")) {


      g_htmlFlag = 1;
      g_altHtmlFlag = 1;

      if (2 == readTexDefs(0 ,
          0   )) {
        continue;
      }

      let(&g_texFileName, "mnemosyne.txt");
      g_texFilePtr = fSafeOpen(g_texFileName, "w", 0);
      if (!g_texFilePtr) {

        continue;
      }
      print2("Creating Mnemosyne file \"%s\"...\n", g_texFileName);

      for (s = 1; s <= g_statements; s++) {
        g_showStatement = s;
        if (!g_Statement[s].labelName[0]) continue;

        if (!matchesList(g_Statement[s].labelName, g_fullArg[2], '*', '?'))
          continue;
        if (g_Statement[s].type != (char)a_
            && g_Statement[s].type != (char)p_)
          continue;

        let(&str1, cat("<CENTER><B><FONT SIZE=\"+1\">",
            " <FONT COLOR=", GREEN_TITLE_COLOR,
            " SIZE = \"+3\">", g_Statement[g_showStatement].labelName,
            "</FONT></FONT></B>", "</CENTER>", null));
        fprintf(g_texFilePtr, "%s", str1);

        let(&str1, cat("<TABLE>",null));
        fprintf(g_texFilePtr, "%s", str1);

        j = nmbrLen(g_Statement[g_showStatement].reqHypList);
        for (i = 0; i < j; i++) {
          k = g_Statement[g_showStatement].reqHypList[i];
          if (g_Statement[k].type != (char)e_
              && !(subType == SYNTAX
              && g_Statement[k].type == (char)f_))
            continue;

          let(&str1, cat("<TR ALIGN=LEFT><TD><FONT SIZE=\"+2\">",
              g_Statement[k].labelName, "</FONT></TD><TD><FONT SIZE=\"+2\">",
              null));
          fprintf(g_texFilePtr, "%s", str1);


          let(&str1, "");
          str1 = getTexLongMath(g_Statement[k].mathString,
              k);
          fprintf(g_texFilePtr, "%s</FONT></TD>", str1);
        }


        let(&str1, "</TABLE>");
        fprintf(g_texFilePtr, "%s", str1);

        let(&str1, "<BR><FONT SIZE=\"+2\">What is the conclusion?</FONT>");
        fprintf(g_texFilePtr, "%s\n", str1);

        let(&str1, "<FONT SIZE=\"+3\">");
        fprintf(g_texFilePtr, "%s", str1);

        let(&str1, "");

        str1 = getTexLongMath(g_Statement[s].mathString, s);
        fprintf(g_texFilePtr, "%s", str1);

        let(&str1, "</FONT>");
        fprintf(g_texFilePtr, "%s\n",str1);
      }

      fclose(g_texFilePtr);
      g_texFileOpenFlag = 0;
      let(&g_texFileName,"");
      let(&str1,"");
      let(&str2,"");

      continue;
    }


    if (cmdMatches("SHOW STATEMENT") && !switchPos("/ HTML")) {

      texFlag = 0;

      if (switchPos("/ TEX") || switchPos("/ OLD_TEX")
          || switchPos("/ HTML"))
        texFlag = 1;

      briefFlag = 1;
      g_oldTexFlag = 0;
      if (switchPos("/ TEX")) briefFlag = 0;

      if (switchPos("/ OLD_TEX")) briefFlag = 0;
      if (switchPos("/ OLD_TEX")) g_oldTexFlag = 1;
      if (switchPos("/ FULL")) briefFlag = 0;

      commentOnlyFlag = 0;
      if (switchPos("/ COMMENT")) {
        commentOnlyFlag = 1;
        briefFlag = 1;
      }


      if (switchPos("/ FULL")) {
        briefFlag = 0;
        commentOnlyFlag = 0;
      }

      if (texFlag) {
        if (!g_texFileOpenFlag) {
          print2(
      "?You have not opened a %s file.  Use the OPEN TEX command first.\n",
              g_htmlFlag ? "HTML" : "LaTeX");
          continue;
        }
      }

      if (texFlag && (commentOnlyFlag || briefFlag)) {
        print2("?TEX qualifier should be used alone\n");
        continue;
      }

      q = 0;

      for (s = 1; s <= g_statements; s++) {
        if (!g_Statement[s].labelName[0]) continue;


        if (!matchesList(g_Statement[s].labelName, g_fullArg[2], '*', '?'))
          continue;

        if (briefFlag || commentOnlyFlag || texFlag) {

          if (g_Statement[s].type != (char)p_
              && g_Statement[s].type != (char)a_ && (instr(1, g_fullArg[2], "*")
                  || instr(1, g_fullArg[2], "?")))
            continue;
        }

        if (q && !texFlag) {

          if (!print2("%s\n", string(g_screenWidth, '-')))
            break;
        }
        if (texFlag) print2("Outputting statement \"%s\"...\n",
            g_Statement[s].labelName);

        q = 1;

        g_showStatement = s;


        typeStatement(g_showStatement,
            briefFlag,
            commentOnlyFlag,
            texFlag,
            g_htmlFlag);
      }

      if (!q) {

        printLongLine(cat("?There is no statement whose label matches \"",
            g_fullArg[2], "\".  ",
            "Use SHOW LABELS for a list of valid labels.", null), "", " ");
        continue;
      }

      if (texFlag && !g_htmlFlag) {
        print2("The LaTeX source was written to \"%s\".\n", g_texFileName);

        g_oldTexFlag = 0;
      }
      continue;
    }

    if (cmdMatches("SHOW SETTINGS")) {
      print2("Metamath settings on %s at %s:\n",date(),time_());
      if (g_commandEcho) {
        print2("(SET ECHO...) Command ECHO is ON.\n");
      } else {
        print2("(SET ECHO...) Command ECHO is OFF.\n");
      }
      if (defaultScrollMode == 1) {
        print2("(SET SCROLL...) SCROLLing mode is PROMPTED.\n");
      } else {
        print2("(SET SCROLL...) SCROLLing mode is CONTINUOUS.\n");
      }
      print2("(SET WIDTH...) Screen display WIDTH is %ld.\n", g_screenWidth);
      print2("(SET HEIGHT...) Screen display HEIGHT is %ld.\n",
          g_screenHeight + 1);
      if (g_sourceHasBeenRead == 1) {
        print2("(READ...) %ld statements have been read from \"%s\".\n",
            g_statements, g_input_fn);
      } else {
        print2("(READ...) No source file has been read in yet.\n");
      }
      print2("(SET ROOT_DIRECTORY...) Root directory is \"%s\".\n",
          g_rootDirectory);
      print2(
     "(SET DISCOURAGEMENT...) Blocking based on \"discouraged\" tags is %s.\n",
          (g_globalDiscouragement ? "ON" : "OFF"));
      print2(
     "(SET CONTRIBUTOR...) The current contributor is \"%s\".\n",
          g_contributorName);
      if (g_PFASmode) {
        print2("(PROVE...) The statement you are proving is \"%s\".\n",
            g_Statement[g_proveStatement].labelName);
      }
      print2("(SET UNDO...) The maximum number of UNDOs is %ld.\n",
          processUndoStack(null, PUS_GET_SIZE, "", 0));
      print2(
    "(SET UNIFICATION_TIMEOUT...) The unification timeout parameter is %ld.\n",
          g_userMaxUnifTrials);
      print2(
    "(SET SEARCH_LIMIT...) The SEARCH_LIMIT for the IMPROVE command is %ld.\n",
          g_userMaxProveFloat);
      if (g_minSubstLen) {
        print2(
     "(SET EMPTY_SUBSTITUTION...) EMPTY_SUBSTITUTION is not allowed (OFF).\n");
      } else {
        print2(
          "(SET EMPTY_SUBSTITUTION...) EMPTY_SUBSTITUTION is allowed (ON).\n");
      }
      if (g_hentyFilter) {
        print2(
              "(SET JEREMY_HENTY_FILTER...) The Henty filter is turned ON.\n");
      } else {
        print2(
             "(SET JEREMY_HENTY_FILTER...) The Henty filter is turned OFF.\n");
      }
      if (g_showStatement) {
        print2("(SHOW...) The default statement for SHOW commands is \"%s\".\n",
            g_Statement[g_showStatement].labelName);
      }
      if (g_logFileOpenFlag) {
        print2("(OPEN LOG...) The log file \"%s\" is open.\n", g_logFileName);
      } else {
        print2("(OPEN LOG...) No log file is currently open.\n");
      }
      if (g_texFileOpenFlag) {
        print2("The %s file \"%s\" is open.\n", g_htmlFlag ? "HTML" : "LaTeX",
            g_texFileName);
      }

      print2(
  "(SHOW STATEMENT.../[TEX,HTML,ALT_HTML]) Current output mode is %s.\n",
          g_htmlFlag
             ? (g_altHtmlFlag ? "ALT_HTML" : "HTML")
             : "TEX (LaTeX)");

      print2("The program is compiled for a %ld-bit CPU.\n",
          (long)(8 * sizeof(long)));
      print2(
 "sizeof(short)=%ld, sizeof(int)=%ld, sizeof(long)=%ld, sizeof(size_t)=%ld.\n",
        (long)(sizeof(short)),
        (long)(sizeof(int)), (long)(sizeof(long)), (long)(sizeof(size_t)));
      continue;
    }

    if (cmdMatches("SHOW MEMORY")) {

      j = 32000000;

      i = getFreeSpace(j);

      if (i > j-3) {
        print2("At least %ld bytes of memory are free.\n",j);
      } else {
        print2("%ld bytes of memory are free.\n",i);
      }
      continue;
    }


    if (cmdMatches("SHOW ELAPSED_TIME")) {
      timeTotal = getRunTime(&timeIncr);
      print2(
      "Time since last SHOW ELAPSED_TIME command = %6.2f s; total = %6.2f s\n",
          timeIncr, timeTotal);
      continue;
    }


    if (cmdMatches("SHOW TRACE_BACK")) {



      essentialFlag = 0;
      axiomFlag = 0;
      endIndent = 0;
      i = switchPos("/ ESSENTIAL");
      if (i) essentialFlag = 1;
      i = switchPos("/ ALL");
      if (i) essentialFlag = 0;
      i = switchPos("/ AXIOMS");
      if (i) axiomFlag = 1;
      i = switchPos("/ DEPTH");
      if (i) endIndent = (long)val(g_fullArg[i + 1]);


      i = switchPos("/ COUNT_STEPS");
      countStepsFlag = (i != 0 ? 1 : 0);
      i = switchPos("/ TREE");
      treeFlag = (i != 0 ? 1 : 0);
      i = switchPos("/ MATCH");
      matchFlag = (i != 0 ? 1 : 0);
      if (matchFlag) {
        let(&matchList, g_fullArg[i + 1]);
      } else {
        let(&matchList, "");
      }
      i = switchPos("/ TO");
      if (i != 0) {
        let(&traceToList, g_fullArg[i + 1]);
      } else {
        let(&traceToList, "");
      }
      if (treeFlag) {
        if (axiomFlag) {
          print2(
              "(Note:  The AXIOMS switch is ignored in TREE mode.)\n");
        }
        if (countStepsFlag) {
          print2(
              "(Note:  The COUNT_STEPS switch is ignored in TREE mode.)\n");
        }
        if (matchFlag) {
          print2(
  "(Note: The MATCH list is ignored in TREE mode.)\n");
        }
      } else {
        if (endIndent != 0) {
          print2(
  "(Note:  The DEPTH is ignored if the TREE switch is not used.)\n");
        }
        if (countStepsFlag) {
          if (matchFlag) {
            print2(
  "(Note: The MATCH list is ignored in COUNT_STEPS mode.)\n");
          }
        }
      }


      g_showStatement = 0;
      for (i = 1; i <= g_statements; i++) {
        if (g_Statement[i].type != (char)p_)
          continue;

        if (!matchesList(g_Statement[i].labelName, g_fullArg[2], '*', '?'))
          continue;

        g_showStatement = i;


        if (treeFlag) {
          traceProofTree(g_showStatement, essentialFlag, endIndent);
        } else {
          if (countStepsFlag) {
            countSteps(g_showStatement, essentialFlag);
          } else {
            traceProof(g_showStatement,
                essentialFlag,
                axiomFlag,
                matchList,
                traceToList,
                0  );
          }
        }


      }
      if (g_showStatement == 0) {
        printLongLine(cat("?There are no $p labels matching \"",
            g_fullArg[2], "\".  ",
            "See HELP SHOW TRACE_BACK for matching rules.", null), "", " ");
      }

      let(&matchList, "");
      let(&traceToList, "");
      continue;
    }


    if (cmdMatches("SHOW USAGE")) {


      if (switchPos("/ ALL")) {
        m = 1;
      } else {
        m = 0;
      }

      g_showStatement = 0;
      for (i = 1; i <= g_statements; i++) {


        if (!g_Statement[i].labelName[0]) continue;
        if (!m && g_Statement[i].type != (char)p_ &&
            g_Statement[i].type != (char)a_
            && (instr(1, g_fullArg[2], "*")
              || instr(1, g_fullArg[2], "?")))
          continue;

        if (!matchesList(g_Statement[i].labelName, g_fullArg[2], '*', '?'))
          continue;

        g_showStatement = i;
        recursiveFlag = 0;
        j = switchPos("/ RECURSIVE");
        if (j) recursiveFlag = 1;
        j = switchPos("/ DIRECT");
        if (j) recursiveFlag = 0;

        let(&str1, "");
        str1 = traceUsage(g_showStatement,
            recursiveFlag,
            0 );










        k = 0;
        if (str1[0] == 'Y') {

          for (j = g_showStatement + 1; j <= g_statements; j++) {
            if (str1[j] == 'Y') {
              k++;
            } else {
              if (str1[j] != 'N') bug(1124);
            }
          }
        } else {
          if (str1[0] != 'N') bug(1125);
        }

        if (k == 0) {
          printLongLine(cat("Statement \"",
              g_Statement[g_showStatement].labelName,
              "\" is not referenced in the proof of any statement.", null),
              "", " ");
        } else {
          if (recursiveFlag) {
            let(&str2, "\" directly or indirectly affects");
          } else {
            let(&str2, "\" is directly referenced in");
          }
          if (k == 1) {
            printLongLine(cat("Statement \"",
                g_Statement[g_showStatement].labelName,
                str2, " the proof of ",
                str((double)k), " statement:", null), "", " ");
          } else {
            printLongLine(cat("Statement \"",
                g_Statement[g_showStatement].labelName,
                str2, " the proofs of ",
                str((double)k), " statements:", null), "", " ");
          }
        }

        if (k != 0) {
          let(&str3, " ");
          for (j = g_showStatement + 1; j <= g_statements; j++) {
            if (str1[j] == 'Y') {
              if ((long)strlen(str3) + 1 +
                  (long)strlen(g_Statement[j].labelName) > g_screenWidth) {

                print2("%s\n", str3);
                let(&str3, " ");
              }
              let(&str3, cat(str3, " ", g_Statement[j].labelName, null));
            }
          }
          if (strlen(str3) > 1) print2("%s\n", str3);
          let(&str3, "");
        } else {
          print2("  (None)\n");
        }



      }

      if (g_showStatement == 0) {
        printLongLine(cat("?There are no labels matching \"",
            g_fullArg[2], "\".  ",
            "See HELP SHOW USAGE for matching rules.", null), "", " ");
      }
      continue;
    }


    if (cmdMatches("SHOW PROOF")
        || cmdMatches("SHOW NEW_PROOF")
        || cmdMatches("SAVE PROOF")
        || cmdMatches("SAVE NEW_PROOF")
        || cmdMatches("MIDI")) {
      if (switchPos("/ HTML")) {
        print2("?HTML qualifier is obsolete - use SHOW STATEMENT * / HTML\n");
        continue;
      }


      if (cmdMatches("SAVE NEW_PROOF")
          && getMarkupFlag(g_proveStatement, PROOF_DISCOURAGED)) {
        if (switchPos("/ OVERRIDE") == 0 && g_globalDiscouragement == 1) {

          print2(
">>> ?Error: Attempt to overwrite a proof whose modification is discouraged.\n");
          print2(
   ">>> Use SAVE NEW_PROOF ... / OVERRIDE if you really want to do this.\n");

          continue;
        } else {

          print2(
">>> ?Warning: You are overwriting a proof whose modification is discouraged.\n");

        }
      }

      if (cmdMatches("SHOW PROOF") || cmdMatches("SAVE PROOF")) {
        pipFlag = 0;
      } else {
        pipFlag = 1;
      }
      if (cmdMatches("SHOW")) {
        saveFlag = 0;
      } else {
        saveFlag = 1;
      }


      printTime = 0;
      if (switchPos("/ TIME") != 0) {
        printTime = 1;
      }


      i = switchPos("/ OLD_COMPRESSION");
      if (i) {
        if (!switchPos("/ COMPRESSED")) {
          print2("?/ OLD_COMPRESSION must be accompanied by / COMPRESSED.\n");
          continue;
        }
      }


      i = switchPos("/ FAST");
      if (i) {
        if (!switchPos("/ COMPRESSED") && !switchPos("/ PACKED")) {
          print2("?/ FAST must be accompanied by / COMPRESSED or / PACKED.\n");
          continue;
        }
        fastFlag = 1;
      } else {
        fastFlag = 0;
      }


      if (switchPos("/ EXPLICIT")) {
        if (switchPos("/ COMPRESSED")) {
          print2("?/ COMPRESSED and / EXPLICIT may not be used together.\n");
          continue;
        } else if (switchPos("/ NORMAL")) {
          print2("?/ NORMAL and / EXPLICIT may not be used together.\n");
          continue;
        }
      }
      if (switchPos("/ NORMAL")) {
        if (switchPos("/ COMPRESSED")) {
          print2("?/ NORMAL and / COMPRESSED may not be used together.\n");
          continue;
        }
      }



      startStep = 0;
      endStep = 0;

      endIndent = 0;

      essentialFlag = 1;
      renumberFlag = 0;
      unknownFlag = 0;
      notUnifiedFlag = 0;
      reverseFlag = 0;
      detailStep = 0;
      noIndentFlag = 0;
      splitColumn = DEFAULT_COLUMN;
      skipRepeatedSteps = 0;
      texFlag = 0;

      i = switchPos("/ FROM_STEP");
      if (i) startStep = (long)val(g_fullArg[i + 1]);
      i = switchPos("/ TO_STEP");
      if (i) endStep = (long)val(g_fullArg[i + 1]);
      i = switchPos("/ DEPTH");
      if (i) endIndent = (long)val(g_fullArg[i + 1]);
      i = switchPos("/ ALL");
      if (i) essentialFlag = 0;
      if (i && switchPos("/ ESSENTIAL")) {
        print2("?You may not specify both / ESSENTIAL and / ALL.\n");
        continue;
      }
      i = switchPos("/ RENUMBER");
      if (i) renumberFlag = 1;
      i = switchPos("/ UNKNOWN");
      if (i) unknownFlag = 1;
      i = switchPos("/ NOT_UNIFIED");
      if (i) notUnifiedFlag = 1;
      i = switchPos("/ REVERSE");
      if (i) reverseFlag = 1;
      i = switchPos("/ LEMMON");
      if (i) noIndentFlag = 1;
      i = switchPos("/ START_COLUMN");
      if (i) splitColumn = (long)val(g_fullArg[i + 1]);
      i = switchPos("/ NO_REPEATED_STEPS");
      if (i) skipRepeatedSteps = 1;


      if (skipRepeatedSteps == 1 && noIndentFlag == 0) {
        print2("?You must specify / LEMMON with / NO_REPEATED_STEPS\n");
        continue;
      }

      i = switchPos("/ TEX") || switchPos("/ HTML")

          || switchPos("/ OLD_TEX");
      if (i) texFlag = 1;


      g_oldTexFlag = 0;
      if (switchPos("/ OLD_TEX")) g_oldTexFlag = 1;

      if (cmdMatches("MIDI")) {
        g_midiFlag = 1;
        pipFlag = 0;
        saveFlag = 0;
        let(&labelMatch, g_fullArg[1]);
        i = switchPos("/ PARAMETER");
        if (i) {
          let(&g_midiParam, g_fullArg[i + 1]);
        } else {
          let(&g_midiParam, "");
        }
      } else {
        g_midiFlag = 0;
        if (!pipFlag) let(&labelMatch, g_fullArg[2]);
      }


      if (texFlag) {
        if (!g_texFileOpenFlag) {
          print2(
     "?You have not opened a %s file.  Use the OPEN %s command first.\n",
              g_htmlFlag ? "HTML" : "LaTeX",
              g_htmlFlag ? "HTML" : "TEX");
          continue;
        }
      }

      i = switchPos("/ DETAILED_STEP");
      if (i) {
        detailStep = (long)val(g_fullArg[i + 1]);
        if (!detailStep) detailStep = -1;
      }





      if (switchPos("/ COMPRESSED") && fastFlag
          && !strcmp("*", labelMatch)) {
        print2(
            "Reformatting and saving (but not recompressing) all proofs...\n");
      }


      q = 0;
      for (stmt = 1; stmt <= g_statements; stmt++) {

        if (!pipFlag) {
          if (g_Statement[stmt].type != (char)p_) continue;

          if (!matchesList(g_Statement[stmt].labelName, labelMatch, '*', '?'))
            continue;
          g_showStatement = stmt;
        }

        q = 1;

        if (detailStep) {

          showDetailStep(g_showStatement, detailStep);
          continue;
        }

        if (switchPos("/ STATEMENT_SUMMARY")) {

          proofStmtSumm(g_showStatement, essentialFlag, texFlag);
          continue;
        }


        if (switchPos("/ SIZE")) {

          let(&str1, space(g_Statement[g_showStatement].proofSectionLen));
          memcpy(str1, g_Statement[g_showStatement].proofSectionPtr,
              (size_t)(g_Statement[g_showStatement].proofSectionLen));
          n = instr(1, str1, "$.");
          if (n == 0) {

            n = g_Statement[g_showStatement].proofSectionLen;
          } else {
            n = n - 1;
          }
          print2("The proof source for \"%s\" has %ld characters.\n",
              g_Statement[g_showStatement].labelName, n);
          continue;
        }

        if (switchPos("/ PACKED") || switchPos("/ NORMAL") ||
            switchPos("/ COMPRESSED") || switchPos("/ EXPLICIT") || saveFlag) {



          if (saveFlag) {
            if (printTime == 1) {
              getRunTime(&timeIncr);
            }
          }

          if (!pipFlag) {
            outStatement = g_showStatement;
          } else {
            outStatement = g_proveStatement;
          }

          explicitTargets = (switchPos("/ EXPLICIT") != 0) ? 1 : 0;


          indentation = 2 + getSourceIndentation(outStatement);

          if (!pipFlag) {
            parseProof(g_showStatement);
            if (g_WrkProof.errorSeverity > 1) {


              print2(
          "?The proof has a severe error and cannot be displayed or saved.\n");
              continue;
            }

            if (fastFlag) {


              nmbrLet(&nmbrSaveProof, g_WrkProof.proofString);
            } else {

              nmbrLet(&nmbrSaveProof, nmbrUnsquishProof(g_WrkProof.proofString));
            }
          } else {
            nmbrLet(&nmbrSaveProof, g_ProofInProgress.proof);
          }
          if (switchPos("/ PACKED")  || switchPos("/ COMPRESSED")) {
            if (!fastFlag) {
              nmbrLet(&nmbrSaveProof, nmbrSquishProof(nmbrSaveProof));
            }
          }

          if (switchPos("/ COMPRESSED")) {
            let(&str1, compressProof(nmbrSaveProof,
                outStatement,
                (switchPos("/ OLD_COMPRESSION")) ? 1 : 0
                ));
          } else {
            let(&str1, nmbrCvtRToVString(nmbrSaveProof,

                explicitTargets,
                outStatement ));
          }


          if (saveFlag) {

            if (g_printString[0]) bug(1114);
            let(&g_printString, "");

            g_outputToString = 1;

          } else {
            if (!print2("Proof of \"%s\":\n", g_Statement[outStatement].labelName))
              break;
            print2(
"---------Clip out the proof below this line to put it in the source file:\n");



          }
          if (switchPos("/ COMPRESSED")) {
            printLongLine(cat(space(indentation), str1, " $.", null),
              space(indentation), "& ");
          } else {
            printLongLine(cat(space(indentation), str1," $.", null),
              space(indentation), " ");
          }


          l = (long)(strlen(str1));


          if  (1

              && !nmbrElementIn(1, nmbrSaveProof, -(long)'?')) {



            let(&str2, "");
            str2 = getContrib(outStatement, CONTRIBUTOR);
            if (str2[0] == 0) {


              getProofDate(outStatement, &str3, &str4);
              if (str4[0] != 0) {
                let(&str5, str3);
                let(&str3, str4);
              } else {
                let(&str5, "");
              }

              if (str3[0] == 0) let(&str3, date());
              let(&str4, cat("\n", space(indentation + 1),

                  "(Contributed by ", g_contributorName,
                      ", ", str3, ".) ", null));
              if (str5[0] != 0) {
                let(&str4, cat(str4, "\n", space(indentation + 1),
                    "(Revised by ", DEFAULT_CONTRIBUTOR,
                        ", ", str5, ".) ", null));
              }

              let(&str3, space(g_Statement[outStatement].labelSectionLen));

              memcpy(str3, g_Statement[outStatement].labelSectionPtr,
                  (size_t)(g_Statement[outStatement].labelSectionLen));
              i = rinstr(str3, "$)");
              if (i != 0
                  && saveFlag) {

                let(&str3, cat(left(str3, i - 1), str4, right(str3, i), null));
                if (g_Statement[outStatement].labelSectionChanged == 1) {

                  let(&str4, "");
                  str4 = g_Statement[outStatement].labelSectionPtr;
                  let(&str4, "");
                }

                g_Statement[outStatement].labelSectionChanged = 1;
                g_Statement[outStatement].labelSectionLen = (long)strlen(str3);
                g_Statement[outStatement].labelSectionPtr = str3;
                str3 = "";

                str3 = getContrib(outStatement, GC_RESET_STMT);
              }

#ifdef DATE_BELOW_PROOF


              let(&str3, space(g_Statement[outStatement + 1].labelSectionLen));

              memcpy(str3, g_Statement[outStatement + 1].labelSectionPtr,
                  (size_t)(g_Statement[outStatement + 1].labelSectionLen));
              let(&str5, "");
              if (instr(1, str3, "$( [") == 0) {


                let(&str5, cat(space(indentation),
                    "$( [", date(), "] $)", null));

                if (saveFlag) {

                  if (g_Statement[outStatement + 1].labelSectionChanged == 1) {

                    let(&str4, "");
                    str4 = g_Statement[outStatement + 1].labelSectionPtr;
                    let(&str4, "");
                  }
                  let(&str3, edit(str3, 8));
                  if (str3[0] != '\n') let(&str3, cat("\n", str3, null));

                  let(&str3, cat("\n", str5, str3, null));

                  g_Statement[outStatement + 1].labelSectionChanged = 1;
                  g_Statement[outStatement + 1].labelSectionLen
                      = (long)strlen(str3);
                  g_Statement[outStatement + 1].labelSectionPtr = str3;
                  str3 = "";

                  str3 = getContrib(outStatement + 1, GC_RESET_STMT);
                }
              }

#endif

            }



          }

          if (saveFlag) {
            g_sourceChanged = 1;
            g_proofChanged = 0;
            if (processUndoStack(null, PUS_GET_STATUS, "", 0)) {

              proofSavedFlag = 1;
            }



            let(&g_printString, cat("\n", g_printString, null));
            if (g_Statement[outStatement].proofSectionChanged == 1) {

              let(&str1, "");
              str1 = g_Statement[outStatement].proofSectionPtr;
              let(&str1, "");
            }

            g_Statement[outStatement].proofSectionChanged = 1;
            if (strcmp(" $.\n",
                right(g_printString, (long)strlen(g_printString) - 3))) {
              bug(1128);
            }
            g_Statement[outStatement].proofSectionLen
                = (long)strlen(g_printString) - 3;
            g_Statement[outStatement].proofSectionPtr = g_printString;
            g_printString = "";
            g_outputToString = 0;


            if (!pipFlag) {
              if (!(fastFlag && !strcmp("*", labelMatch))) {
                printLongLine(
                    cat("The proof of \"", g_Statement[outStatement].labelName,
                    "\" has been reformatted and saved internally.",
                    null), "", " ");
              }
            } else {
              printLongLine(cat("The new proof of \"", g_Statement[outStatement].labelName,
                  "\" has been saved internally.",
                  null), "", " ");
            }


            if (printTime == 1) {
              getRunTime(&timeIncr);
              print2("SAVE PROOF run time = %6.2f sec for \"%s\"\n", timeIncr,
                  g_Statement[outStatement].labelName);
            }

          } else {

#ifdef DATE_BELOW_PROOF


            if (str5[0] != 0) print2("%s\n", str5);

#endif




            print2(cat(
                "---------The proof of \"", g_Statement[outStatement].labelName,


                "\" (", str((double)l), " bytes) ends above this line.\n", null));
          }
          nmbrLet(&nmbrSaveProof, NULL_NMBRSTRING);
          if (pipFlag) break;
          continue;
        }

        if (saveFlag) bug(1112);

        if (!pipFlag) {
          outStatement = g_showStatement;
        } else {
          outStatement = g_proveStatement;
        }
        if (texFlag) {
          g_outputToString = 1;
          if (!g_htmlFlag) {
            if (!g_oldTexFlag) {

              print2("\\begin{proof}\n");
              print2("\\begin{align}\n");
            } else {
              print2("\n");
              print2("\\vspace{1ex} %%1\n");
              printLongLine(cat("Proof of ",
                  "{\\tt ",
                  asciiToTt(g_Statement[outStatement].labelName),
                  "}:", null), "", " ");
              print2("\n");
              print2("\n");
            }
          } else {
            bug(1118);

          }
          g_outputToString = 0;

          fprintf(g_texFilePtr, "%s", g_printString);
          let(&g_printString, "");
        } else {

          if (!pipFlag) {

            if (instr(1, labelMatch, "*") || instr(1, labelMatch, "?")) {
              if (!print2("Proof of \"%s\":\n", g_Statement[outStatement].labelName))
                break;
            }
          }
        }


        if (texFlag) print2("Outputting proof of \"%s\"...\n",
            g_Statement[outStatement].labelName);

        typeProof(outStatement,
            pipFlag,
            startStep,
            endStep,
            endIndent,
            essentialFlag,


            (texFlag ? 1 : renumberFlag),


            unknownFlag,
            notUnifiedFlag,
            reverseFlag,


            (texFlag ? 1 : noIndentFlag),


            splitColumn,
            skipRepeatedSteps,
            texFlag,
            g_htmlFlag);
        if (texFlag) {
          if (!g_htmlFlag) {

            if (!g_oldTexFlag) {
              g_outputToString = 1;
              print2("\\end{align}\n");
              print2("\\end{proof}\n");
              print2("\n");
              g_outputToString = 0;
              fprintf(g_texFilePtr, "%s", g_printString);
              let(&g_printString, "");
            } else {
            }
          } else {
            g_outputToString = 1;
            print2("</TABLE>\n");
            print2("</CENTER>\n");

            g_outputToString = 0;
          }
        }




         if (0) {
          printLongLine(nmbrCvtRToVString(g_WrkProof.proofString,

                0,
                0 )," "," ");
          print2("\n");

          nmbrLet(&nmbrSaveProof, nmbrSquishProof(g_WrkProof.proofString));
          printLongLine(nmbrCvtRToVString(nmbrSaveProof,

                0,
                0 )," "," ");
          print2("\n");

          nmbrLet(&nmbrTmp, nmbrUnsquishProof(nmbrSaveProof));
          printLongLine(nmbrCvtRToVString(nmbrTmp,

                0,
                0 )," "," ");

          nmbrLet(&nmbrTmp, nmbrGetTargetHyp(nmbrSaveProof,g_showStatement));
          printLongLine(nmbrCvtAnyToVString(nmbrTmp)," "," "); print2("\n");

          nmbrLet(&nmbrTmp, nmbrGetEssential(nmbrSaveProof));
          printLongLine(nmbrCvtAnyToVString(nmbrTmp)," "," "); print2("\n");

          cleanWrkProof();
        }

        if (pipFlag) break;
      }
      if (!q) {

        printLongLine(cat("?There is no $p statement whose label matches \"",
            (cmdMatches("MIDI")) ? g_fullArg[1] : g_fullArg[2],
            "\".  ",
            "Use SHOW LABELS to see list of valid labels.", null), "", " ");
      } else {
        if (saveFlag) {
          print2("Remember to use WRITE SOURCE to save changes permanently.\n");
        }
        if (texFlag) {
          print2("The LaTeX source was written to \"%s\".\n", g_texFileName);

         g_oldTexFlag = 0;
        }
      }

      continue;
    }



    if (cmdMatches("DBG")) {
      print2("DEBUGGING MODE IS FOR DEVELOPER'S USE ONLY!\n");
      print2("Argument:  %s\n", g_fullArg[1]);
      nmbrLet(&nmbrTmp, parseMathTokens(g_fullArg[1], g_proveStatement));
      for (j = 0; j < 3; j++) {
        print2("Trying depth %ld\n", j);
        nmbrTmpPtr = proveFloating(nmbrTmp, g_proveStatement, j, 0, 0,
            1, 1);
        if (nmbrLen(nmbrTmpPtr)) break;
      }

      print2("Result:  %s\n", nmbrCvtRToVString(nmbrTmpPtr,

                0,
                0 ));
      nmbrLet(&nmbrTmpPtr, NULL_NMBRSTRING);

      continue;
    }


    if (cmdMatches("PROVE")) {



      i = getStatementNum(g_fullArg[1],
          1,
          g_statements + 1  ,
          0,
          1,
          0,
          0,
          0,
          1);
      if (i == -1) {
        continue;
      }
      g_proveStatement = i;




      overrideFlag = ( (switchPos("/ OVERRIDE")) ? 1 : 0)
         || g_globalDiscouragement == 0;
      if (getMarkupFlag(g_proveStatement, PROOF_DISCOURAGED)) {
        if (overrideFlag == 0) {

          print2(
 ">>> ?Error: Modification of this statement's proof is discouraged.\n"
              );
          print2(
 ">>> You must use PROVE ... / OVERRIDE to work on it.\n");

          continue;
        }
      }



      print2(
"Entering the Proof Assistant.  HELP PROOF_ASSISTANT for help, EXIT to exit.\n");


      g_PFASmode = 1;

      parseProof(g_proveStatement);
      verifyProof(g_proveStatement);
      if (g_WrkProof.errorSeverity > 1) {
        print2(
             "The starting proof has a severe error.  It will not be used.\n");
        nmbrLet(&nmbrSaveProof, nmbrAddElement(NULL_NMBRSTRING, -(long)'?'));
      } else {
        nmbrLet(&nmbrSaveProof, g_WrkProof.proofString);
      }
      cleanWrkProof();



      initProofStruct(&g_ProofInProgress, nmbrSaveProof, g_proveStatement);



      print2("You will be working on statement (from \"SHOW STATEMENT %s\"):\n",
          g_Statement[g_proveStatement].labelName);
      typeStatement(g_proveStatement ,
          1 ,
          0 ,
          0 ,
          0 );

      if (!nmbrElementIn(1, g_ProofInProgress.proof, -(long)'?')) {
        print2(
        "Note:  The proof you are starting with is already complete.\n");
      } else {

        print2(
     "Unknown step summary (from \"SHOW NEW_PROOF / UNKNOWN\"):\n");
        typeProof(g_proveStatement,
            1 ,
            0 ,
            0 ,
            0 ,
            1 ,
            0 ,
            1 ,
            0 ,
            0 ,
            0 ,
            0 ,
            0 ,
            0 ,
            0 );

      }


      if (getMarkupFlag(g_proveStatement, PROOF_DISCOURAGED)) {

        print2(
">>> ?Warning: Modification of this statement's proof is discouraged.\n"
            );

      }

      processUndoStack(null, PUS_INIT, "", 0);
      processUndoStack(&g_ProofInProgress, PUS_PUSH, "", 0);
      continue;
    }



    if (cmdMatches("UNDO")) {
      processUndoStack(&g_ProofInProgress, PUS_UNDO, "", 0);
      g_proofChanged = 1;
      typeProof(g_proveStatement,
          1 ,
          0 ,
          0 ,
          0 ,
          1 ,
          0 ,
          1 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 );

      continue;
    }


    if (cmdMatches("REDO")) {
      processUndoStack(&g_ProofInProgress, PUS_REDO, "", 0);
      g_proofChanged = 1;
      typeProof(g_proveStatement,
          1 ,
          0 ,
          0 ,
          0 ,
          1 ,
          0 ,
          1 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 );

      continue;
    }

    if (cmdMatches("UNIFY")) {
      m = nmbrLen(g_ProofInProgress.proof);
      g_proofChangedFlag = 0;
      if (cmdMatches("UNIFY STEP")) {

        s = (long)val(g_fullArg[2]);
        if (s > m || s < 1) {
          print2("?The step must be in the range from 1 to %ld.\n", m);
          continue;
        }

        interactiveUnifyStep(s - 1, 1);




        autoUnify(1);
        if (g_proofChangedFlag) {
          g_proofChanged = 1;
          processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);
        }
        continue;
      }


      if (!switchPos("/ INTERACTIVE")) {
        autoUnify(1);
        if (!g_proofChangedFlag) {
          print2("No new unifications were made.\n");
        } else {
          print2(
  "Steps were unified.  SHOW NEW_PROOF / NOT_UNIFIED to see any remaining.\n");
          g_proofChanged = 1;
          processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);
        }
      } else {
        q = 0;
        while (true) {
          g_proofChangedFlag = 0;
          autoUnify(0);
          for (s = m - 1; s >= 0; s--) {
            interactiveUnifyStep(s, 0);
          }
          autoUnify(1);
          if (!g_proofChangedFlag) {
            if (!q) {
              print2("No new unifications were made.\n");
            } else {
              print2(
  "Steps were unified.  SHOW NEW_PROOF / NOT_UNIFIED to see any remaining.\n");
              g_proofChanged = 1;
              processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);
            }
            break;
          } else {
            q = 1;
          }
        }
      }
      typeProof(g_proveStatement,
          1 ,
          0 ,
          0 ,
          0 ,
          1 ,
          0 ,
          1 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 );

      continue;
    }

    if (cmdMatches("MATCH")) {

      maxEssential = -1;
      i = switchPos("/ MAX_ESSENTIAL_HYP");
      if (i) maxEssential = (long)val(g_fullArg[i + 1]);

      if (cmdMatches("MATCH STEP")) {

        s = (long)val(g_fullArg[2]);
        m = nmbrLen(g_ProofInProgress.proof);
        if (s > m || s < 1) {
          print2("?The step must be in the range from 1 to %ld.\n", m);
          continue;
        }
        if ((g_ProofInProgress.proof)[s - 1] != -(long)'?') {
          print2(
    "?Step %ld is already assigned.  Only unknown steps can be matched.\n", s);
          continue;
        }

        interactiveMatch(s - 1, maxEssential);
        n = nmbrLen(g_ProofInProgress.proof);
        if (n != m) {
          if (s != m) {
            printLongLine(cat("Steps ", str((double)s), ":",
                str((double)m), " are now ", str((double)(s - m + n)), ":",
                str((double)n), ".",
                null),
                "", " ");
          } else {
            printLongLine(cat("Step ", str((double)m), " is now step ", str((double)n), ".",
                null),
                "", " ");
          }
        }

        autoUnify(1);
        g_proofChanged = 1;
        processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);

        continue;
      }

      if (cmdMatches("MATCH ALL")) {

        m = nmbrLen(g_ProofInProgress.proof);

        k = 0;
        g_proofChangedFlag = 0;

        if (switchPos("/ ESSENTIAL")) {
          nmbrLet(&nmbrTmp, nmbrGetEssential(g_ProofInProgress.proof));
        }

        for (s = m; s > 0; s--) {

          if ((g_ProofInProgress.proof)[s - 1] != -(long)'?') continue;

          if (switchPos("/ ESSENTIAL")) {
            if (!nmbrTmp[s - 1]) continue;
          }

          interactiveMatch(s - 1, maxEssential);
          if (g_proofChangedFlag) {
            k = s;
            g_proofChangedFlag = 0;
          }
          print2("\n");
        }
        if (k) {
          g_proofChangedFlag = 1;
          g_proofChanged = 1;
          processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);
          print2("Steps %ld and above have been renumbered.\n", k);
        }
        autoUnify(1);

        continue;
      }
    }

    if (cmdMatches("LET")) {

      g_errorCount = 0;
      nmbrLet(&nmbrTmp, parseMathTokens(g_fullArg[4], g_proveStatement));
      if (g_errorCount) {

        g_errorCount = 0;
        continue;
      }

      if (cmdMatches("LET VARIABLE")) {
        if (((vstring)(g_fullArg[2]))[0] != '$') {
          print2(
   "?The target variable must be of the form \"$<integer>\", e.g. \"$23\".\n");
          continue;
        }
        n = (long)val(right(g_fullArg[2], 2));
        if (n < 1 || n > g_pipDummyVars) {
          print2("?The target variable must be between $1 and $%ld.\n",
              g_pipDummyVars);
          continue;
        }

        replaceDummyVar(n, nmbrTmp);

        autoUnify(1);


        g_proofChangedFlag = 1;
        g_proofChanged = 1;
        processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);

      }

      if (cmdMatches("LET STEP")) {


        s = getStepNum(g_fullArg[2], g_ProofInProgress.proof,
            0 );
        if (s == -1) continue;



        if (!checkMStringMatch(nmbrTmp, s - 1)) {
          printLongLine(cat("?Step ", str((double)s), " cannot be unified with \"",
              nmbrCvtMToVString(nmbrTmp),"\".", null), " ", " ");
          continue;
        }


        nmbrLet((nmbrString **)(&((g_ProofInProgress.user)[s - 1])), nmbrTmp);

        autoUnify(1);
        g_proofChangedFlag = 1;
        g_proofChanged = 1;
        processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);
      }
      typeProof(g_proveStatement,
          1 ,
          0 ,
          0 ,
          0 ,
          1 ,
          0 ,
          1 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 );

      continue;
    }


    if (cmdMatches("ASSIGN")) {


      s = getStepNum(g_fullArg[1], g_ProofInProgress.proof,
          0 );
      if (s == -1) continue;



      overrideFlag = ( (switchPos("/ OVERRIDE")) ? 1 : 0)
         || g_globalDiscouragement == 0;




      k = getStatementNum(g_fullArg[2],
          1,
          g_proveStatement  ,
          1,
          1,
          1,
          1,
          1,
          1);
      if (k == -1) {
        continue;
      }



      if (getMarkupFlag(k, USAGE_DISCOURAGED)) {
        if (overrideFlag == 0) {

          print2(
">>> ?Error: Attempt to assign a statement whose usage is discouraged.\n");
          print2(
       ">>> Use ASSIGN ... / OVERRIDE if you really want to do this.\n");

          continue;
        } else {

          print2(
">>> ?Warning: You are assigning a statement whose usage is discouraged.\n");

        }
      }

      m = nmbrLen(g_ProofInProgress.proof);




      if ((g_ProofInProgress.proof)[s - 1] != -(long)'?') {
        print2(
        "?Step %ld is already assigned.  You can only assign unknown steps.\n"
            , s);
        continue;
      }


      if (!checkStmtMatch(k, s - 1)) {
        print2("?Statement \"%s\" cannot be unified with step %ld.\n",
          g_Statement[k].labelName, s);
        continue;
      }

      assignStatement(k , s - 1 );

      n = nmbrLen(g_ProofInProgress.proof);
      autoUnify(1);


      if (switchPos("/ NO_UNIFY") == 0) {
        interactiveUnifyStep(s - m + n - 1, 2);
      }





      assignMathboxInfo();
      if (k > g_mathboxStmt && g_proveStatement > g_mathboxStmt) {
        if (k < g_mathboxStart[getMathboxNum(g_proveStatement) - 1]) {
          printLongLine(cat("\"", g_Statement[k].labelName,
                "\" is in the mathbox for ",
                g_mathboxUser[getMathboxNum(k) - 1], ".",
                ull),
              "", " ");
        }
      }

      typeProof(g_proveStatement,
          1 ,
          0 ,
          0 ,
          0 ,
          1 ,
          0 ,
          1 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 );



      g_proofChangedFlag = 1;
      g_proofChanged = 1;
      processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);
      continue;

    }


    if (cmdMatches("REPLACE")) {





      overrideFlag = ( (switchPos("/ OVERRIDE")) ? 1 : 0)
         || g_globalDiscouragement == 0;


      step = getStepNum(g_fullArg[1], g_ProofInProgress.proof,
          0 );
      if (step == -1) continue;




      stmt = getStatementNum(g_fullArg[2],
          1,
          g_proveStatement  ,
          1,
          1,
          0,
          0,
          1,
          1);
      if (stmt == -1) {
        continue;
      }



      if (getMarkupFlag(stmt, USAGE_DISCOURAGED)) {
        if (overrideFlag == 0) {

          print2(
">>> ?Error: Attempt to assign a statement whose usage is discouraged.\n");
          print2(
       ">>> Use REPLACE ... / OVERRIDE if you really want to do this.\n");

          continue;
        } else {

          print2(
">>> ?Warning: You are assigning a statement whose usage is discouraged.\n");

        }
      }

      m = nmbrLen(g_ProofInProgress.proof);





      if (nmbrElementIn(1, g_ProofInProgress.proof, -(long)'?')) {
        p = 1;
      } else {
        p = 0;
      }


      if (!checkStmtMatch(stmt, step - 1)) {
        print2("?Statement \"%s\" cannot be unified with step %ld.\n",
          g_Statement[stmt].labelName, step);
        continue;
      }




      dummyVarIsoFlag = checkDummyVarIsolation(step - 1);



      nmbrTmpPtr = replaceStatement(stmt ,
          step - 1 ,
          g_proveStatement,
          0,
          0,
          1,
          1,
          overrideFlag,
          1
          );
      if (!nmbrLen(nmbrTmpPtr)) {
        print2(
           "?Hypotheses of statement \"%s\" do not match known proof steps.\n",
            g_Statement[stmt].labelName);
        continue;
      }


      q = subproofLen(g_ProofInProgress.proof, step - 1);
      deleteSubProof(step - 1);
      addSubProof(nmbrTmpPtr, step - q);



      assignKnownSubProofs();

      i = nmbrLen(g_ProofInProgress.proof);
      for (j = 0; j < i; j++) {
        if (!nmbrLen((g_ProofInProgress.source)[j])) {
          initStep(j);
        }
      }

      autoUnify((char)p);


      nmbrLet(&nmbrTmpPtr, NULL_NMBRSTRING);

      n = nmbrLen(g_ProofInProgress.proof);
      if (nmbrElementIn(1, g_ProofInProgress.proof, -(long)'?')) {

        if (m == n) {
          print2("Step %ld was replaced with statement %s.\n",
            step, g_Statement[stmt].labelName);
        } else {
          if (step != m) {
            printLongLine(cat("Step ", str((double)step),
                " was replaced with statement ", g_Statement[stmt].labelName,
                ".  Steps ", str((double)step), ":",
                str((double)m), " are now ", str((double)(step - m + n)), ":",
                str((double)n), ".",
                null),
                "", " ");
          } else {
            printLongLine(cat("Step ", str((double)step),
                " was replaced with statement ", g_Statement[stmt].labelName,
                ".  Step ", str((double)m), " is now step ", str((double)n), ".",
                null),
                "", " ");
          }
        }
      }



      g_proofChangedFlag = 1;
      g_proofChanged = 1;
      processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);



      if (dummyVarIsoFlag == 2 && g_proofChangedFlag) {
        printLongLine(cat(
     "Assignments to shared working variables ($nn) are guesses.  If "
     "incorrect, UNDO then assign them manually with LET ",
      "and try REPLACE again.",
              null),
              "", " ");
      }


      if (g_proofChangedFlag)
        typeProof(g_proveStatement,
            1 ,
            0 ,
            0 ,
            0 ,
            1 ,
            0 ,
            1 ,
            0 ,
            0 ,
            0 ,
            0 ,
            0 ,
            0 ,
            0 );



      continue;

    }


    if (cmdMatches("IMPROVE")) {

      improveDepth = 0;
      i = switchPos("/ DEPTH");
      if (i) improveDepth = (long)val(g_fullArg[i + 1]);
      if (switchPos("/ NO_DISTINCT")) p = 1; else p = 0;


      searchAlg = 1;
      if (switchPos("/ 1")) searchAlg = 1;
      if (switchPos("/ 2")) searchAlg = 2;
      if (switchPos("/ 3")) searchAlg = 3;

      searchUnkSubproofs = 0;
      if (switchPos("/ SUBPROOFS")) searchUnkSubproofs = 1;

      mathboxFlag = (switchPos("/ INCLUDE_MATHBOXES") != 0);

      assignMathboxInfo();
      if (g_proveStatement > g_mathboxStmt) {

        i = getMathboxNum(g_proveStatement);
        if (i <= 0) bug(1130);
        thisMathboxStartStmt = g_mathboxStart[i - 1];
      } else {
        thisMathboxStartStmt = g_mathboxStmt;
      }



      overrideFlag = ( (switchPos("/ OVERRIDE")) ? 1 : 0)
         || g_globalDiscouragement == 0;


      s = getStepNum(g_fullArg[1], g_ProofInProgress.proof,
          1 );
      if (s == -1) continue;

      if (s != 0) {


        m = nmbrLen(g_ProofInProgress.proof);




        q = subproofLen(g_ProofInProgress.proof, s - 1);
        nmbrLet(&nmbrTmp, nmbrSeg(g_ProofInProgress.proof, s - q + 1, s));



        if (!nmbrElementIn(1, nmbrTmp, -(long)'?')) {
          print2(
              "?Step %ld already has a proof and cannot be improved.\n",
              s);
          continue;
        }



        dummyVarIsoFlag = checkDummyVarIsolation(s - 1);

        if (dummyVarIsoFlag == 2) {
          print2(
  "?Step %ld target has shared dummy variables and cannot be improved.\n", s);
          continue;
        }



        if (dummyVarIsoFlag == 0) {

          nmbrTmpPtr = proveFloating((g_ProofInProgress.target)[s - 1],
              g_proveStatement, improveDepth, s - 1, (char)p,
              overrideFlag,
              mathboxFlag
              );
        } else {
          nmbrTmpPtr = NULL_NMBRSTRING;
        }
        if (!nmbrLen(nmbrTmpPtr)) {



          if (searchAlg == 2 || searchAlg == 3) {
            nmbrTmpPtr = proveByReplacement(g_proveStatement,
              s - 1,
              (char)p,
              dummyVarIsoFlag,
              (char)(searchAlg - 2),
              improveDepth,
              overrideFlag,
              mathboxFlag
              );
          }
          if (!nmbrLen(nmbrTmpPtr)) {
            print2("A proof for step %ld was not found.\n", s);

            continue;
          }
        }


        if (q > 1) deleteSubProof(s - 1);
        addSubProof(nmbrTmpPtr, s - q);
        assignKnownSteps(s - q, nmbrLen(nmbrTmpPtr));
        nmbrLet(&nmbrTmpPtr, NULL_NMBRSTRING);

        n = nmbrLen(g_ProofInProgress.proof);
        if (m == n) {
          print2("A 1-step proof was found for step %ld.\n", s);
        } else {
          if (s != m || q != 1) {
            printLongLine(cat("A ", str((double)(n - m + 1)),
                "-step proof was found for step ", str((double)s),
                ".  Steps ", str((double)s), ":",
                str((double)m), " are now ", str((double)(s - q + 1 - m + n)),
                ":", str((double)n), ".",
                null),
                "", " ");
          } else {
            printLongLine(cat("A ", str((double)(n - m + 1)),
                "-step proof was found for step ", str((double)s),
                ".  Step ", str((double)m), " is now step ", str((double)n), ".",
                null),
                "", " ");
          }
        }

        autoUnify(1);
        g_proofChanged = 1;
        processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);


      } else {




        if (!nmbrElementIn(1, g_ProofInProgress.proof, -(long)'?')) {
          print2("The proof is already complete.\n");
          continue;
        }

        n = 0;

        g_proofChangedFlag = 0;

        for (improveAllIter = 1; improveAllIter <= 4; improveAllIter++) {

          if (improveAllIter == 1 && (searchAlg == 2 || searchAlg == 3))
            print2("Pass 1:  Trying to match cut-free statements...\n");
          if (improveAllIter == 2) {
            if (searchAlg == 2) {
              print2("Pass 2:  Trying to match all statements...\n");
            } else {
              print2(
"Pass 2:  Trying to match all statements, with cut-free hypothesis matches...\n"
                  );
            }
          }
          if (improveAllIter == 3 && searchUnkSubproofs)
            print2("Pass 3:  Trying to replace incomplete subproofs...\n");
          if (improveAllIter == 4) {
            if (searchUnkSubproofs) {
              print2("Pass 4:  Repeating pass 1...\n");
            } else {
              print2("Pass 3:  Repeating pass 1...\n");
            }
          }




          if (improveAllIter == 3 && !searchUnkSubproofs) continue;

          m = nmbrLen(g_ProofInProgress.proof);

          for (s = m; s > 0; s--) {

            proofStepUnk = ((g_ProofInProgress.proof)[s - 1] == -(long)'?')
                ? 1 : 0;

            if (improveAllIter == 1 || searchAlg == 1) {
              if (!proofStepUnk) {
                if (nmbrEq((g_ProofInProgress.target)[s - 1],
                    (g_ProofInProgress.source)[s - 1])) continue;
              }
            }


            q = subproofLen(g_ProofInProgress.proof, s - 1);
            if (proofStepUnk && q != 1) {
              bug(1120);
            }
            nmbrLet(&nmbrTmp, nmbrSeg(g_ProofInProgress.proof, s - q + 1, s));


            if (!nmbrElementIn(1, nmbrTmp, -(long)'?')) continue;

            nmbrLet(&nmbrTmp, NULL_NMBRSTRING);



            dummyVarIsoFlag = checkDummyVarIsolation(s - 1);

            if (dummyVarIsoFlag == 2) continue;


            if (dummyVarIsoFlag == 0
                && (improveAllIter == 1
                  || improveAllIter == 4)) {


              nmbrTmpPtr = proveFloating((g_ProofInProgress.target)[s - 1],
                  g_proveStatement, improveDepth, s - 1,
                  (char)p,
                  overrideFlag,
                  mathboxFlag
                  );
            } else {
              nmbrTmpPtr = NULL_NMBRSTRING;
            }
            if (!nmbrLen(nmbrTmpPtr)) {



              if ((searchAlg == 2 || searchAlg == 3)
                  && ((improveAllIter == 2 && proofStepUnk)
                    || (improveAllIter == 3 && !proofStepUnk)
                    )) {
                nmbrTmpPtr = proveByReplacement(g_proveStatement,
                  s - 1,
                  (char)p,
                  dummyVarIsoFlag,
                  (char)(searchAlg - 2),
                  improveDepth,
                  overrideFlag,
                  mathboxFlag
                  );

              }
              if (!nmbrLen(nmbrTmpPtr)) {

                continue;
              }
            }

            if (q > 1) deleteSubProof(s - 1);
            addSubProof(nmbrTmpPtr, s - q);
            assignKnownSteps(s - q, nmbrLen(nmbrTmpPtr));
            print2("A proof of length %ld was found for step %ld.\n",
                nmbrLen(nmbrTmpPtr), s);
            if (nmbrLen(nmbrTmpPtr) || q != 1) n = s - q + 1;

            nmbrLet(&nmbrTmpPtr, NULL_NMBRSTRING);
            g_proofChangedFlag = 1;
            s = s - q + 1;
          }

          if (g_proofChangedFlag) {
            autoUnify(0);
          }

          if (!g_proofChangedFlag
              && ( (improveAllIter == 2 && !searchUnkSubproofs)
                 || improveAllIter == 3
                 || searchAlg == 1)) {
            print2("No new subproofs were found.\n");
            break;
          }
          if (g_proofChangedFlag) {
            g_proofChanged = 1;
          }

          if (!nmbrElementIn(1, g_ProofInProgress.proof, -(long)'?')) {
            break;
          }

          if (searchAlg == 1) break;

        }

        if (g_proofChangedFlag) {
          if (n > 0) {
            print2("Steps %ld and above have been renumbered.\n", n);
          }
          processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);
        }
        if (!nmbrElementIn(1, g_ProofInProgress.proof, -(long)'?')) {
          autoUnify(1);
        }

      }

      if (g_proofChangedFlag)
        typeProof(g_proveStatement,
            1 ,
            0 ,
            0 ,
            0 ,
            1 ,
            0 ,
            1 ,
            0 ,
            0 ,
            0 ,
            0 ,
            0 ,
            0 ,
            0 );


      continue;

    }


    if (cmdMatches("MINIMIZE_WITH")) {


      printTime = 0;
      if (switchPos("/ TIME") != 0) {
        printTime = 1;
      }
      if (printTime == 1) {
        getRunTime(&timeIncr);
      }


      prntStatus = 0;


      verboseMode = (switchPos("/ VERBOSE") != 0);

      if (!(instr(1, g_fullArg[1], "*") || instr(1, g_fullArg[1], "?"))) i = 1;
      mayGrowFlag = (switchPos("/ MAY_GROW") != 0);


      exceptPos = switchPos("/ EXCEPT");



      allowNewAxiomsMatchPos = switchPos("/ ALLOW_NEW_AXIOMS");
      if (allowNewAxiomsMatchPos != 0) {
        let(&allowNewAxiomsMatchList, g_fullArg[allowNewAxiomsMatchPos + 1]);
      } else {
        let(&allowNewAxiomsMatchList, "");
      }


      noNewAxiomsMatchPos = switchPos("/ NO_NEW_AXIOMS_FROM");
      if (noNewAxiomsMatchPos != 0) {
        let(&noNewAxiomsMatchList, g_fullArg[noNewAxiomsMatchPos + 1]);
      } else {
        let(&noNewAxiomsMatchList, "");
      }


      forbidMatchPos = switchPos("/ FORBID");
      if (forbidMatchPos != 0) {
        let(&forbidMatchList, g_fullArg[forbidMatchPos + 1]);
      } else {
        let(&forbidMatchList, "");
      }

      mathboxFlag = (switchPos("/ INCLUDE_MATHBOXES") != 0);



      overrideFlag = (switchPos("/ OVERRIDE") != 0)
           || g_globalDiscouragement == 0;



      hasWildCard = 0;



      if (strpbrk(g_fullArg[1], "*?~%,") != null) {
        hasWildCard = 1;
      }

      g_proofChangedFlag = 0;




      assignMathboxInfo();
      if (g_proveStatement > g_mathboxStmt) {

        i = getMathboxNum(g_proveStatement);
        if (i <= 0) bug(1130);
        thisMathboxStartStmt = g_mathboxStart[i - 1];
      } else {
        thisMathboxStartStmt = g_mathboxStmt;
      }


      copyProofStruct(&saveOrigProof, g_ProofInProgress);


      nmbrLet(&nmbrSaveProof, g_ProofInProgress.proof);
      nmbrLet(&nmbrSaveProof, nmbrSquishProof(g_ProofInProgress.proof));

      let(&str1, compressProof(nmbrSaveProof,
          g_proveStatement,
          0
          ));
      origCompressedLength = (long)strlen(str1);
      print2(
"Bytes refer to compressed proof size, steps to uncompressed length.\n");



      for (forwRevPass = 1; forwRevPass <= 2; forwRevPass++) {


        if (forwRevPass == 1) {
          if (hasWildCard) print2("Scanning forward through statements...\n");
          forwFlag = 1;
        } else {

          if (mayGrowFlag) break;

          if (!g_proofChangedFlag) break;

          if (!hasWildCard) break;
          print2("Scanning backward through statements...\n");
          forwFlag = 0;

          copyProofStruct(&save1stPassProof, g_ProofInProgress);
          forwardLength = nmbrLen(g_ProofInProgress.proof);
          forwardCompressedLength = oldCompressedLength;

          copyProofStruct(&g_ProofInProgress, saveOrigProof);
        }



        copyProofStruct(&saveProofForReverting, g_ProofInProgress);

        oldCompressedLength = origCompressedLength;



        for (k = forwFlag ? 1 : (g_proveStatement - 1);
             k * (forwFlag ? 1 : -1) < (forwFlag ? g_proveStatement : 0);
             k = k + (forwFlag ? 1 : -1)) {




          if (!mathboxFlag && k >= g_mathboxStmt && k < thisMathboxStartStmt) {
            continue;
          }

          if (g_Statement[k].type != (char)p_ && g_Statement[k].type != (char)a_)
            continue;

          if (!matchesList(g_Statement[k].labelName, g_fullArg[1], '*', '?'))
            continue;


          if (exceptPos != 0) {


            if (matchesList(g_Statement[k].labelName, g_fullArg[exceptPos + 1],
                '*', '?'))
              continue;
          }


          if (forbidMatchList[0]) {
            if (matchesList(g_Statement[k].labelName, forbidMatchList, '*', '?'))
              continue;
          }


          if (!overrideFlag) {
            if (getMarkupFlag(k, USAGE_DISCOURAGED)) {
              continue;
            }
          }


          if (prntStatus == 0) prntStatus = 1;

          m = nmbrLen(g_ProofInProgress.proof);
          nmbrLet(&nmbrTmp, g_ProofInProgress.proof);
          minimizeProof(k ,
              g_proveStatement ,
              (char)mayGrowFlag );

          n = nmbrLen(g_ProofInProgress.proof);
          if (!nmbrEq(nmbrTmp, g_ProofInProgress.proof)) {


            if (forbidMatchList[0]) {
              if (g_Statement[k].type == (char)p_) {

                if (traceProof(k,
                    0,
                    0,
                    forbidMatchList,
                    "",
                    1 )) {


                  copyProofStruct(&g_ProofInProgress, saveProofForReverting);

                  continue;
                }
              }
            }



            if (n == n + 0) {

              if (traceProofFlags[0] == 0) {





                nmbrLet(&nmbrSaveProof, nmbrSquishProof(saveProofForReverting.proof));
                let(&str1, compressProof(nmbrSaveProof,
                    g_proveStatement,
                    0
                    ));
                saveZappedProofSectionPtr
                    = g_Statement[g_proveStatement].proofSectionPtr;
                saveZappedProofSectionLen
                    = g_Statement[g_proveStatement].proofSectionLen;
                saveZappedProofSectionChanged
                    = g_Statement[g_proveStatement].proofSectionChanged;

                g_Statement[g_proveStatement].proofSectionChanged = 1;

                let(&str1, cat(" ", str1, " $.", null));

                g_Statement[g_proveStatement].proofSectionLen
                    = (long)strlen(str1) - 2;
                g_Statement[g_proveStatement].proofSectionPtr = str1;


                traceProofWork(g_proveStatement,
                    1 ,
                    "",
                    &traceProofFlags,
                    &nmbrTmp );
                nmbrLet(&nmbrTmp, NULL_NMBRSTRING);



                g_Statement[g_proveStatement].proofSectionPtr
                    = saveZappedProofSectionPtr;
                g_Statement[g_proveStatement].proofSectionLen
                    = saveZappedProofSectionLen;
                g_Statement[g_proveStatement].proofSectionChanged
                    = saveZappedProofSectionChanged;


              }
              let(&traceTrialFlags, "");
              traceProofWork(k,
                  1 ,
                  "",
                  &traceTrialFlags,
                  &nmbrTmp );
              nmbrLet(&nmbrTmp, NULL_NMBRSTRING);
              j = 1;
              for (i = 1; i < g_proveStatement; i++) {
                if (g_Statement[i].type != (char)a_) continue;
                if (traceProofFlags[i] == 'Y') continue;
                if (matchesList(g_Statement[i].labelName, allowNewAxiomsMatchList,
                    '*', '?') == 1
                      &&
                    matchesList(g_Statement[i].labelName, noNewAxiomsMatchList,
                    '*', '?') != 1) {
                  continue;
                }
                if (traceTrialFlags[i] == 'Y') {
                  j = 0;
                  break;
                }
              }
              if (j == 0) {


                copyProofStruct(&g_ProofInProgress, saveProofForReverting);

                continue;
              }
            }


            if (nmbrLen(g_Statement[k].reqDisjVarsA) || !mayGrowFlag) {
              nmbrLet(&nmbrSaveProof, g_ProofInProgress.proof);
              nmbrLet(&nmbrSaveProof, nmbrSquishProof(g_ProofInProgress.proof));
              let(&str1, compressProof(nmbrSaveProof,
                  g_proveStatement,
                  0
                  ));
              newCompressedLength = (long)strlen(str1);
              if (!mayGrowFlag && newCompressedLength > oldCompressedLength) {

                if (verboseMode) {
                  print2(
 "Reverting \"%s\": Uncompressed steps:  old = %ld, new = %ld\n",
                      g_Statement[k].labelName,
                      m, n );
                  print2(
 "    but compressed size:  old = %ld bytes, new = %ld bytes\n",
                      oldCompressedLength, newCompressedLength);
                }
                copyProofStruct(&g_ProofInProgress, saveProofForReverting);

                continue;
              }
            }




            if (nmbrLen(g_Statement[k].reqDisjVarsA)) {
              saveZappedProofSectionPtr
                  = g_Statement[g_proveStatement].proofSectionPtr;
              saveZappedProofSectionLen
                  = g_Statement[g_proveStatement].proofSectionLen;



              saveZappedProofSectionChanged
                  = g_Statement[g_proveStatement].proofSectionChanged;

              g_Statement[g_proveStatement].proofSectionChanged = 1;

              let(&str1, cat(" ", str1, " $.", null));

              g_Statement[g_proveStatement].proofSectionLen = (long)strlen(str1) - 2;
              g_Statement[g_proveStatement].proofSectionPtr = str1;


              g_outputToString = 1;




              parseProof(g_proveStatement);
              verifyProof(g_proveStatement);
              i = (g_WrkProof.errorSeverity > 1);
              j = instr(1, g_printString,
                  "There is a disjoint variable ($d) violation");
              g_outputToString = 0;
              let(&g_printString, "");
              cleanWrkProof();
              g_Statement[g_proveStatement].proofSectionPtr
                  = saveZappedProofSectionPtr;
              g_Statement[g_proveStatement].proofSectionLen
                  = saveZappedProofSectionLen;
              g_Statement[g_proveStatement].proofSectionChanged
                  = saveZappedProofSectionChanged;
              if (i != 0 || j != 0) {

                copyProofStruct(&g_ProofInProgress, saveProofForReverting);

                continue;
              }
            }







            if (getMarkupFlag(k, USAGE_DISCOURAGED)) {
              if (!overrideFlag) bug(1126);

              print2(
                  ">>> ?Warning: Overriding discouraged usage of \"%s\".\n",
                  g_Statement[k].labelName);

            }

            if (!mayGrowFlag) {
              if (newCompressedLength < oldCompressedLength) {
                print2(
     "Proof of \"%s\" decreased from %ld to %ld bytes using \"%s\".\n",
                    g_Statement[g_proveStatement].labelName,
                    oldCompressedLength, newCompressedLength,
                    g_Statement[k].labelName);
              } else {
                if (newCompressedLength > oldCompressedLength) bug(1123);
                print2(
     "Proof of \"%s\" stayed at %ld bytes using \"%s\".\n",
                    g_Statement[g_proveStatement].labelName,
                    oldCompressedLength,
                    g_Statement[k].labelName);
                print2(
    "    (Uncompressed steps decreased from %ld to %ld).\n",
                    m, n );
              }

              oldCompressedLength = newCompressedLength;
            }

            if (n < m && (mayGrowFlag || verboseMode)) {
              print2(
      "%sProof of \"%s\" decreased from %ld to %ld steps using \"%s\".\n",
                (mayGrowFlag ? "" : "    "),
                g_Statement[g_proveStatement].labelName,
                m, n, g_Statement[k].labelName);
            }

            if (m < n) print2(
      "Proof of \"%s\" increased from %ld to %ld steps using \"%s\".\n",
                g_Statement[g_proveStatement].labelName,
                m, n, g_Statement[k].labelName);

            if (m == n) print2(
                "Proof of \"%s\" remained at %ld steps using \"%s\".\n",
                g_Statement[g_proveStatement].labelName,
                m, g_Statement[k].labelName);





            assignMathboxInfo();
            if (k > g_mathboxStmt && g_proveStatement > g_mathboxStmt) {
              if (k < g_mathboxStart[getMathboxNum(g_proveStatement) - 1]) {
                printLongLine(cat("\"", g_Statement[k].labelName,
                      "\" is in the mathbox for ",
                      g_mathboxUser[getMathboxNum(k) - 1], ".",
                      null),
                    "  ", " ");
              }
            }

            prntStatus = 2;
            g_proofChangedFlag = 1;


            copyProofStruct(&saveProofForReverting, g_ProofInProgress);

          }

        }


        if (g_proofChangedFlag && forwRevPass == 2) {

          if (verboseMode) {
            print2(
"Forward vs. backward: %ld vs. %ld bytes; %ld vs. %ld steps\n",
                      forwardCompressedLength,
                      oldCompressedLength,
                      forwardLength,
                      nmbrLen(g_ProofInProgress.proof));
          }
          if (oldCompressedLength < forwardCompressedLength
               || (oldCompressedLength == forwardCompressedLength &&
                   nmbrLen(g_ProofInProgress.proof) < forwardLength)) {

            print2("The backward scan results were used.\n");
          } else {
            copyProofStruct(&g_ProofInProgress, save1stPassProof);
            print2("The forward scan results were used.\n");
          }
        }

      }

      if (prntStatus == 1 && !mayGrowFlag)
        print2("No shorter proof was found.\n");
      if (prntStatus == 1 && mayGrowFlag)
        print2("The proof was not changed.\n");
      if (!prntStatus )
        print2("?No earlier %s$p or $a label matches \"%s\".\n",
            (overrideFlag ? "" : "(allowed) "),
            g_fullArg[1]);

      if (!mathboxFlag && g_proveStatement >= g_mathboxStmt) {
        print2(
  "(Other mathboxes were not checked.  Use / INCLUDE_MATHBOXES to include them.)\n");
      }


      if (printTime == 1) {
        getRunTime(&timeIncr);
        print2("MINIMIZE_WITH run time = %7.2f sec for \"%s\"\n", timeIncr,
            g_Statement[g_proveStatement].labelName);
      }


      let(&str1, "");
      nmbrLet(&nmbrSaveProof, NULL_NMBRSTRING);


      let(&traceProofFlags, "");
      let(&traceTrialFlags, "");


      if (allowNewAxiomsMatchList[0]) {
        let(&allowNewAxiomsMatchList, "");
      }


      if (noNewAxiomsMatchList[0]) {
        let(&noNewAxiomsMatchList, "");
      }


      if (forbidMatchList[0]) {

        let(&forbidMatchList, "");
      }


      deallocProofStruct(&saveProofForReverting);
      deallocProofStruct(&saveOrigProof);
      deallocProofStruct(&save1stPassProof);

      if (g_proofChangedFlag) {
        g_proofChanged = 1;
        processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);
      }
      continue;

    }



    if (cmdMatches("EXPAND")) {

      g_proofChangedFlag = 0;
      nmbrLet(&nmbrSaveProof, g_ProofInProgress.proof);
      s = compressedProofSize(nmbrSaveProof, g_proveStatement);

      for (i = g_proveStatement - 1; i >= 1; i--) {
        if (g_Statement[i].type != (char)p_) continue;

        if (!matchesList(g_Statement[i].labelName, g_fullArg[1], '*', '?')) {
          continue;
        }
        sourceStatement = i;

        nmbrTmp = expandProof(nmbrSaveProof,
            sourceStatement );

        if (!nmbrEq(nmbrTmp, nmbrSaveProof)) {
          g_proofChangedFlag = 1;
          n = compressedProofSize(nmbrTmp, g_proveStatement);
          printLongLine(cat("Proof of \"",
            g_Statement[g_proveStatement].labelName, "\" ",
            (s == n ? cat("stayed at ", str((double)s), null)
                : cat((s < n ? "increased from " : " decreased from "),
                    str((double)s), " to ", str((double)n), null)),
            " bytes after expanding \"",
            g_Statement[sourceStatement].labelName, "\".", null), " ", " ");
          s = n;
          nmbrLet(&nmbrSaveProof, nmbrTmp);
        }
      }

      if (g_proofChangedFlag) {
        g_proofChanged = 1;

        deallocProofStruct(&g_ProofInProgress);

        initProofStruct(&g_ProofInProgress, nmbrTmp, g_proveStatement);

        processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);
      } else {
        print2("No expansion occurred.  The proof was not changed.\n");
      }
      nmbrLet(&nmbrSaveProof, NULL_NMBRSTRING);
      nmbrLet(&nmbrTmp, NULL_NMBRSTRING);
      continue;
    }


    if (cmdMatches("DELETE STEP") || (cmdMatches("DELETE ALL"))) {

      if (cmdMatches("DELETE STEP")) {
        s = (long)val(g_fullArg[2]);
      } else {
        s = nmbrLen(g_ProofInProgress.proof);
      }
      if ((g_ProofInProgress.proof)[s - 1] == -(long)'?') {
        print2("?Step %ld is unknown and cannot be deleted.\n", s);
        continue;
      }
      m = nmbrLen(g_ProofInProgress.proof);
      if (s > m || s < 1) {
        print2("?The step must be in the range from 1 to %ld.\n", m);
        continue;
      }

      deleteSubProof(s - 1);
      n = nmbrLen(g_ProofInProgress.proof);
      if (m == n) {
        print2("Step %ld was deleted.\n", s);
      } else {
        if (n > 1) {
          printLongLine(cat("A ", str((double)(m - n + 1)),
              "-step subproof at step ", str((double)s),
              " was deleted.  Steps ", str((double)s), ":",
              str((double)m), " are now ", str((double)(s - m + n)), ":",
              str((double)n), ".",
              null),
              "", " ");
        } else {
          print2("The entire proof was deleted.\n");
        }
      }

      typeProof(g_proveStatement,
          1 ,
          0 ,
          0 ,
          0 ,
          1 ,
          0 ,
          1 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 ,
          0 );


      g_proofChanged = 1;
      processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);

      continue;

    }

    if (cmdMatches("DELETE FLOATING_HYPOTHESES")) {


      nmbrLet(&nmbrTmp, nmbrGetEssential(g_ProofInProgress.proof));

      m = nmbrLen(g_ProofInProgress.proof);

      n = 0;
      g_proofChangedFlag = 0;

      for (s = m; s > 0; s--) {


        if (nmbrTmp[s - 1] == 1) continue;
        if ((g_ProofInProgress.proof)[s - 1] == -(long)'?') continue;


        q = subproofLen(g_ProofInProgress.proof, s - 1);

        deleteSubProof(s - 1);

        n = s - q + 1;
        g_proofChangedFlag = 1;
        s = s - q + 1;
      }

      if (g_proofChangedFlag) {
        print2("All floating-hypothesis steps were deleted.\n");

        if (n) {
          print2("Steps %ld and above have been renumbered.\n", n);
        }

        typeProof(g_proveStatement,
            1 ,
            0 ,
            0 ,
            0 ,
            1 ,
            0 ,
            1 ,
            0 ,
            0 ,
            0 ,
            0 ,
            0 ,
            0 ,
            0 );


        g_proofChanged = 1;
        processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);
      } else {
        print2("?There are no floating-hypothesis steps to delete.\n");
      }

      continue;

    }

    if (cmdMatches("INITIALIZE")) {

      if (cmdMatches("INITIALIZE ALL")) {
        i = nmbrLen(g_ProofInProgress.proof);


        g_pipDummyVars = 0;


        for (j = 0; j < i; j++) {
          initStep(j);
        }


        assignKnownSubProofs();

        print2("All steps have been initialized.\n");
        g_proofChanged = 1;
        processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);
        continue;
      }


      if (cmdMatches("INITIALIZE USER")) {
        i = nmbrLen(g_ProofInProgress.proof);

        for (j = 0; j < i; j++) {
          nmbrLet((nmbrString **)(&((g_ProofInProgress.user)[j])),
              NULL_NMBRSTRING);
        }
        print2(
      "All LET STEP user assignments have been initialized (i.e. deleted).\n");
        g_proofChanged = 1;
        processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);
        continue;
      }



      s = (long)val(g_fullArg[2]);
      if (s > nmbrLen(g_ProofInProgress.proof) || s < 1) {
        print2("?The step must be in the range from 1 to %ld.\n",
            nmbrLen(g_ProofInProgress.proof));
        continue;
      }

      initStep(s - 1);


      nmbrLet((nmbrString **)(&((g_ProofInProgress.user)[s - 1])),
              NULL_NMBRSTRING);

      print2(
          "Step %ld and its hypotheses have been initialized.\n",
          s);

      g_proofChanged = 1;
      processUndoStack(&g_ProofInProgress, PUS_PUSH, g_fullArgString, 0);
      continue;

    }


    if (cmdMatches("SEARCH")) {
      if (switchPos("/ ALL")) {
        m = 1;
      } else {
        m = 0;
      }


      if (switchPos("/ JOIN")) {
        joinFlag = 1;
      } else {
        joinFlag = 0;
      }

      if (switchPos("/ COMMENTS")) {
        n = 1;
      } else {
        n = 0;
      }

      let(&str1, g_fullArg[2]);

      if (n) {
        let(&str1, edit(str1, 8 + 16 + 128 + 32));
      } else {

        let(&str1, edit(str1, 8 + 16 + 128));


        q = (long)strlen(str1);
        let(&str3, space(q + q));
        s = 0;
        for (p = 0; p < q; p++) {
          str3[p + s] = str1[p];
          if (str1[p] == ' ') {
            s++;
            str3[p + s] = str1[p];
          }
        }
        let(&str1, left(str3, q + s));

        while (true) {
          p = instr(1, str1, "$?");
          if (!p) break;
          let(&str1, cat(left(str1, p - 1), chr(3), right(str1, p + 2), null));
        }

        while (true) {
          p = instr(1, str1, "?");
          if (!p) break;
          let(&str1, cat(left(str1, p - 1), chr(3), right(str1, p + 1), null));
        }




        while (true) {
          p = instr(1, str1, " $* ");
          if (!p) break;

          let(&str1, cat(left(str1, p - 1), chr(2), right(str1, p + 4), null));
        }
        while (true) {
          p = instr(1, str1, "$*");
          if (!p) break;

          let(&str1, cat(left(str1, p - 1), chr(2), right(str1, p + 2), null));
        }

        while (true) {
          p = instr(1, str1, " $ ");
          if (!p) break;
          let(&str1, cat(left(str1, p - 1), chr(2), right(str1, p + 3), null));
        }
        while (true) {
          p = instr(1, str1, "$");
          if (!p) break;
          let(&str1, cat(left(str1, p - 1), chr(2), right(str1, p + 1), null));
        }


        let(&str1, cat(chr(2), " ", str1, " ", chr(2), null));
      }

      for (i = 1; i <= g_statements; i++) {
        if (!g_Statement[i].labelName[0]) continue;
        if (!m && g_Statement[i].type != (char)p_ &&
            g_Statement[i].type != (char)a_) {
          continue;
        }

        if (!matchesList(g_Statement[i].labelName, g_fullArg[1], '*', '?'))
          continue;
        if (n) {
          let(&str2, "");
          str2 = getDescription(i);

          j = instr(1, edit(str2, 4 + 8 + 16 + 128 + 32), str1);
          if (!j) {
            let(&str2, "");
            continue;
          }

          let(&str2, edit(str2, 4 + 8 + 16 + 128));
          j = j + ((long)strlen(str1) / 2);
          p = g_screenWidth - 7 - (long)strlen(str((double)i)) - (long)strlen(g_Statement[i].labelName);

          q = (long)strlen(str2);
          if (q <= p) {
            let(&str3, str2);
          } else {
            if (q - j <= p / 2) {
              let(&str3, cat("...", right(str2, q - p + 4), null));
            } else {
              if (j <= p / 2) {
                let(&str3, cat(left(str2, p - 3), "...", null));
              } else {
                let(&str3, cat("...", mid(str2, j - p / 2, p - 6), "...",
                    null));
              }
            }
          }
          print2("%s\n", cat(str((double)i), " ", g_Statement[i].labelName, " $",
              chr(g_Statement[i].type), " \"", str3, "\"", null));
          let(&str2, "");
        } else {
          let(&str2,nmbrCvtMToVString(g_Statement[i].mathString));


          tmpFlag = 0;
          if (joinFlag && (g_Statement[i].type == (char)p_ ||
              g_Statement[i].type == (char)a_)) {

            k = nmbrLen(g_Statement[i].reqHypList);
            for (j = k - 1; j >= 0; j--) {
              p = g_Statement[i].reqHypList[j];
              if (g_Statement[p].type == (char)e_) {
                let(&str2, cat("$e ",
                    nmbrCvtMToVString(g_Statement[p].mathString),
                    tmpFlag ? "" : cat(" $", chr(g_Statement[i].type), null),
                    " ", str2, null));
                tmpFlag = 1;
              }
            }
          }


          q = (long)strlen(str2);
          let(&str3, space(q + q));
          s = 0;
          for (p = 0; p < q; p++) {
            str3[p + s] = str2[p];
            if (str2[p] == ' ') {
              s++;
              str3[p + s] = str2[p];
            }
          }
          let(&str2, left(str3, q + s));

          let(&str2, cat(" ", str2, " ", null));

          if (!matches(str2, str1, 2,
              3))
            continue;
          let(&str2, edit(str2, 8 + 16 + 128));
          printLongLine(cat(str((double)i)," ",
              g_Statement[i].labelName,
              tmpFlag ? "" : cat(" $", chr(g_Statement[i].type), null),
              " ", str2,
              null), "    ", " ");
        }
      }
      continue;
    }


    if (cmdMatches("SET ECHO")) {
      if (cmdMatches("SET ECHO ON")) {
        g_commandEcho = 1;

        print2("!SET ECHO ON\n");
        print2("Command line echoing is now turned on.\n");
      } else {
        g_commandEcho = 0;
        print2("Command line echoing is now turned off.\n");
      }
      continue;
    }

    if (cmdMatches("SET MEMORY_STATUS")) {
      if (cmdMatches("SET MEMORY_STATUS ON")) {
        print2("Memory status display has been turned on.\n");
        print2("This command is intended for debugging purposes only.\n");
        g_memoryStatus = 1;
      } else {
        g_memoryStatus = 0;
        print2("Memory status display has been turned off.\n");
      }
      continue;
    }


    if (cmdMatches("SET JEREMY_HENTY_FILTER")) {
      if (cmdMatches("SET JEREMY_HENTY_FILTER ON")) {
        print2("The unification equivalence filter has been turned on.\n");
        print2("This command is intended for debugging purposes only.\n");
        g_hentyFilter = 1;
      } else {
        print2("This command is intended for debugging purposes only.\n");
        print2("The unification equivalence filter has been turned off.\n");
        g_hentyFilter = 0;
      }
      continue;
    }


    if (cmdMatches("SET EMPTY_SUBSTITUTION")) {
      if (cmdMatches("SET EMPTY_SUBSTITUTION ON")) {
        g_minSubstLen = 0;
        print2("Substitutions with empty symbol sequences is now allowed.\n");
        continue;
      }
      if (cmdMatches("SET EMPTY_SUBSTITUTION OFF")) {
        g_minSubstLen = 1;
        printLongLine(cat("The ability to substitute empty expressions",
            " for variables  has been turned off.  Note that this may",
            " make the Proof Assistant too restrictive in some cases.",
            null),
            "", " ");
        continue;
      }
    }


    if (cmdMatches("SET SEARCH_LIMIT")) {
      s = (long)val(g_fullArg[2]);
      print2("IMPROVE search limit has been changed from %ld to %ld\n",
          g_userMaxProveFloat, s);
      g_userMaxProveFloat = s;
      continue;
    }

    if (cmdMatches("SET WIDTH")) {
      s = (long)val(g_fullArg[2]);




      if (s < 3) s = 3;
      i = g_screenWidth;
      g_screenWidth = s;
      print2("Screen width has been changed from %ld to %ld.\n",
          i, s);
      continue;
    }


    if (cmdMatches("SET HEIGHT")) {
      s = (long)val(g_fullArg[2]);
      if (s < 2) s = 2;
      i = g_screenHeight;
      g_screenHeight = s - 1;
      print2("Screen height has been changed from %ld to %ld.\n",
          i + 1, s);
      continue;
    }



    if (cmdMatches("SET DISCOURAGEMENT")) {
      if (!strcmp(g_fullArg[2], "ON")) {
        g_globalDiscouragement = 1;
        print2("\"(...is discouraged.)\" markup tags are now honored.\n");
      } else if (!strcmp(g_fullArg[2], "OFF")) {
        print2(
    "\"(...is discouraged.)\" markup tags are no longer honored.\n");

        print2(
">>> ?Warning: This setting is intended for advanced users only.  Please turn\n");
        print2(
">>> it back ON if you are not intimately familiar with this database.\n");

        g_globalDiscouragement = 0;
      } else {
        bug(1129);
      }
      continue;
    }



    if (cmdMatches("SET CONTRIBUTOR")) {
      print2("\"Contributed by...\" name was changed from \"%s\" to \"%s\"\n",
          g_contributorName, g_fullArg[2]);
      let(&g_contributorName, g_fullArg[2]);
      continue;
    }



    if (cmdMatches("SET ROOT_DIRECTORY")) {
      let(&str1, g_rootDirectory);
      let(&g_rootDirectory, edit(g_fullArg[2], 2));
      if (g_rootDirectory[0] != 0) {

        if (instr(1, g_rootDirectory, "\\") != 0
            || instr(1, g_input_fn, "\\") != 0
            || instr(1, g_output_fn, "\\") != 0 ) {
          if (g_rootDirectory[strlen(g_rootDirectory) - 1] != '\\') {
            let(&g_rootDirectory, cat(g_rootDirectory, "\\", null));
          }
        } else {
          if (g_rootDirectory[strlen(g_rootDirectory) - 1] != '/') {
            let(&g_rootDirectory, cat(g_rootDirectory, "/", null));
          }
        }
      }
      if (strcmp(str1, g_rootDirectory)){
        print2("Root directory was changed from \"%s\" to \"%s\"\n",
            str1, g_rootDirectory);
      }
      let(&str1, "");
      continue;
    }



    if (cmdMatches("SET UNDO")) {
      s = (long)val(g_fullArg[2]);
      if (s < 0) s = 0;

      if (processUndoStack(null, PUS_GET_SIZE, "", 0) != s) {
        print2(
            "The maximum number of UNDOs was changed from %ld to %ld\n",
            processUndoStack(null, PUS_GET_SIZE, "", 0), s);
        processUndoStack(null, PUS_NEW_SIZE, "", s);
        if (g_PFASmode == 1) {
          processUndoStack(&g_ProofInProgress, PUS_PUSH, "", 0);
        }
      } else {
        print2("The maximum number of UNDOs was not changed.\n");
      }
      continue;
    }


    if (cmdMatches("SET UNIFICATION_TIMEOUT")) {
      s = (long)val(g_fullArg[2]);
      print2("Unification timeout has been changed from %ld to %ld\n",
          g_userMaxUnifTrials,s);
      g_userMaxUnifTrials = s;
      continue;
    }


    if (cmdMatches("OPEN LOG")) {

        let(&g_logFileName, g_fullArg[2]);
        g_logFilePtr = fSafeOpen(g_logFileName, "w", 0);
        if (!g_logFilePtr) continue;
        g_logFileOpenFlag = 1;
        print2("The log file \"%s\" was opened %s %s.\n",g_logFileName,
            date(),time_());
        continue;
    }

    if (cmdMatches("CLOSE LOG")) {

        if (!g_logFileOpenFlag) {
          print2("?Sorry, there is no log file currently open.\n");
        } else {
          print2("The log file \"%s\" was closed %s %s.\n",g_logFileName,
              date(),time_());
          fclose(g_logFilePtr);
          g_logFileOpenFlag = 0;
        }
        let(&g_logFileName,"");
        continue;
    }

    if (cmdMatches("OPEN TEX")) {


      if (g_texDefsRead) {



        if (g_htmlFlag) {
          print2("?You cannot use both LaTeX and HTML in the same session.\n");
          print2(
              "?You must EXIT and restart Metamath to switch to the other.\n");
          continue;
        }
      }




      let(&g_texFileName,g_fullArg[2]);
      if (switchPos("/ NO_HEADER")) {
        texHeaderFlag = 0;
      } else {
        texHeaderFlag = 1;
      }

      if (switchPos("/ OLD_TEX")) {
        g_oldTexFlag = 1;
      } else {
        g_oldTexFlag = 0;
      }
      g_texFilePtr = fSafeOpen(g_texFileName, "w", 0);
      if (!g_texFilePtr) continue;
      g_texFileOpenFlag = 1;

      print2("Created %s output file \"%s\".\n",
          g_htmlFlag ? "HTML" : "LaTeX", g_texFileName);
      printTexHeader(texHeaderFlag);
      g_oldTexFlag = 0;
      continue;
    }


    if (cmdMatches("CLOSE TEX")) {

      if (!g_texFileOpenFlag) {
        print2("?Sorry, there is no LaTeX file currently open.\n");
      } else {
        print2("The LaTeX output file \"%s\" has been closed.\n",
            g_texFileName);
        printTexTrailer(texHeaderFlag);
        fclose(g_texFilePtr);
        g_texFileOpenFlag = 0;
      }
      let(&g_texFileName,"");
      continue;
    }


    if (cmdMatches("MORE")) {
      list1_fp = fSafeOpen(g_fullArg[1], "r", 0);
      if (!list1_fp) continue;
      while (true) {
        if (!linput(list1_fp, null, &str1)) break;

        if (!print2("%s\n", str1)) break;
      }
      fclose(list1_fp);
      continue;
    }


    if (cmdMatches("FILE SEARCH")) {


      type_fp = fSafeOpen(g_fullArg[2], "r", 0);
      if (!type_fp) continue;
      fromLine = 0;
      toLine = 0;
      searchWindow = 0;
      i = switchPos("/ FROM_LINE");
      if (i) fromLine = (long)val(g_fullArg[i + 1]);
      i = switchPos("/ TO_LINE");
      if (i) toLine = (long)val(g_fullArg[i + 1]);
      i = switchPos("/ WINDOW");
      if (i) searchWindow = (long)val(g_fullArg[i + 1]);

      if (i) print2("Sorry, WINDOW has not be implemented yet.\n");

      let(&str2, g_fullArg[3]);
      let(&str2, edit(str2, 32));

      tmpFlag = 0;


      pntrLet(&pntrTmp, pntrSpace(searchWindow));

      j = 0;
      m = 0;
      while (linput(type_fp, null, &str1)) {
        j++;
        if (j > toLine && toLine != 0) break;
        if (j >= fromLine || fromLine == 0) {
          let(&str3, edit(str1, 32));
          if (instr(1, str3, str2)) {
            if (!tmpFlag) {
              tmpFlag = 1;
              print2(
                    "The line number in the file is shown before each line.\n");
            }
            m++;
            if (!print2("%ld:  %s\n", j, left(str1,
                MAX_LEN - (long)strlen(str((double)j)) - 3))) break;
          }
        }
        for (k = 1; k < searchWindow; k++) {
          let((vstring *)(&pntrTmp[k - 1]), pntrTmp[k]);
        }
        if (searchWindow > 0)
            let((vstring *)(&pntrTmp[searchWindow - 1]), str1);
      }
      if (!tmpFlag) {
        print2("There were no matches.\n");
      } else {
        if (m == 1) {
          print2("There was %ld matching line in the file %s.\n", m,
              g_fullArg[2]);
        } else {
          print2("There were %ld matching lines in the file %s.\n", m,
              g_fullArg[2]);
        }
      }

      fclose(type_fp);


      for (i = 0; i < searchWindow; i++) {
        let((vstring *)(&pntrTmp[i]), "");
      }
      pntrLet(&pntrTmp, NULL_PNTRSTRING);


      continue;
    }


    if (cmdMatches("SET UNIVERSE") || cmdMatches("ADD UNIVERSE") ||
        cmdMatches("DELETE UNIVERSE")) {


    }



    if (cmdMatches("SET DEBUG FLAG")) {
      print2("Notice:  The DEBUG mode is intended for development use only.\n");
      print2("The printout will not be meaningful to the user.\n");
      i = (long)val(g_fullArg[3]);
      if (i == 4) db4 = 1;
      if (i == 5) db5 = 1;
      if (i == 6) db6 = 1;
      if (i == 7) db7 = 1;
      if (i == 8) db8 = 1;
      if (i == 9) db9 = 1;
      continue;
    }
    if (cmdMatches("SET DEBUG OFF")) {
      db4 = 0;
      db5 = 0;
      db6 = 0;
      db7 = 0;
      db8 = 0;
      db9 = 0;
      print2("The DEBUG mode has been turned off.\n");
      continue;
    }

    if (cmdMatches("ERASE")) {
      if (g_sourceChanged) {
        print2("Warning:  You have not saved changes to the source.\n");
        str1 = cmdInput1("Do you want to ERASE anyway (Y, N) <N>? ");
        if (str1[0] != 'y' && str1[0] != 'Y') {
          print2("Use WRITE SOURCE to save the changes.\n");
          continue;
        }
        g_sourceChanged = 0;
      }
      eraseSource();
      g_sourceHasBeenRead = 0;
      g_showStatement = 0;
      g_proveStatement = 0;
      print2("Metamath has been reset to the starting state.\n");
      continue;
    }

    if (cmdMatches("VERIFY PROOF")) {
      if (switchPos("/ SYNTAX_ONLY")) {
        verifyProofs(g_fullArg[2],0);
      } else {
        verifyProofs(g_fullArg[2],1);
      }
      continue;
    }




    if (cmdMatches("VERIFY MARKUP")) {
      i = (switchPos("/ DATE_SKIP") != 0) ? 1 : 0;
      j = (switchPos("/ TOP_DATE_SKIP") != 0) ? 1 : 0;
      k = (switchPos("/ FILE_SKIP") != 0) ? 1 : 0;
      l = (switchPos("/ UNDERSCORE_SKIP") != 0) ? 1 : 0;
      m = (switchPos("/ MATHBOX_SKIP") != 0) ? 1 : 0;
      n = (switchPos("/ VERBOSE") != 0) ? 1 : 0;
      if (i == 1 && j == 1) {
        printf(
            "?Only one of / DATE_SKIP and / TOP_DATE_SKIP may be specified.\n");
        continue;
      }
      verifyMarkup(g_fullArg[2],
          (flag)i,
          (flag)j,
          (flag)k,
          (flag)l,
          (flag)m,
          (flag)n);
      continue;
    }


    if (cmdMatches("MARKUP")) {
      g_htmlFlag = 1;
      g_altHtmlFlag = (switchPos("/ ALT_HTML") != 0);
      if ((switchPos("/ HTML") != 0) == (switchPos("/ ALT_HTML") != 0)) {
        print2("?Please specify exactly one of / HTML and / ALT_HTML.\n");
        continue;
      }
      i = 0;
      i = ((switchPos("/ SYMBOLS") != 0) ? PROCESS_SYMBOLS : 0)
          + ((switchPos("/ LABELS") != 0) ? PROCESS_LABELS : 0)
          + ((switchPos("/ NUMBER_AFTER_LABEL") != 0) ? ADD_COLORED_LABEL_NUMBER : 0)
          + ((switchPos("/ BIB_REFS") != 0) ? PROCESS_BIBREFS : 0)
          + ((switchPos("/ UNDERSCORES") != 0) ? PROCESS_UNDERSCORES : 0);
      processMarkup(g_fullArg[1],
          g_fullArg[2],
          (switchPos("/ CSS") != 0),
          i);
      continue;
    }

    print2("?This command has not been implemented.\n");
    continue;

  }
}


}