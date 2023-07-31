package xd.metamath;

public class mmcmdl {











pntrString *g_rawArgPntr = NULL_PNTRSTRING;
nmbrString *g_rawArgNmbr = NULL_NMBRSTRING;
long g_rawArgs = 0;
pntrString *g_fullArg = NULL_PNTRSTRING;
vstring g_fullArgString = "";
vstring g_commandPrompt = "";
vstring g_commandLine = "";
long g_showStatement = 0;
vstring g_logFileName = "";
vstring g_texFileName = "";
flag g_PFASmode = 0;
flag g_queryMode = 0;
flag g_sourceChanged = 0;
flag g_proofChanged = 0;
flag g_commandEcho = 0;
flag g_memoryStatus = 0;

flag g_sourceHasBeenRead = 0;

vstring g_rootDirectory = "";



flag processCommandLine()
{
  vstring defaultArg = "";
  vstring tmpStr = "";
  long i;
  g_queryMode = 0;
  pntrLet(&g_fullArg, NULL_PNTRSTRING);

  if (!g_toolsMode) {

    if (!g_PFASmode) {

      let(&tmpStr, cat("DBG|",
          "HELP|READ|WRITE|PROVE|SHOW|SEARCH|SAVE|SUBMIT|OPEN|CLOSE|",

          "SET|FILE|BEEP|EXIT|QUIT|ERASE|VERIFY|MARKUP|MORE|TOOLS|",
          "MIDI|<HELP>",
          NULL));
    } else {

      let(&tmpStr, cat("DBG|",
          "HELP|WRITE|SHOW|SEARCH|SAVE|SUBMIT|OPEN|CLOSE|",

          "SET|FILE|BEEP|EXIT|_EXIT_PA|QUIT|VERIFY|INITIALIZE|ASSIGN|REPLACE|",

          "LET|UNIFY|IMPROVE|MINIMIZE_WITH|EXPAND|MATCH|DELETE|UNDO|REDO|",

          "MARKUP|MORE|TOOLS|MIDI|<HELP>",
          NULL));
    }
    if (!getFullArg(0,tmpStr)) {
      C.go2("pclbad");
    }

    if (cmdMatches("HELP")) {

      if (!getFullArg(1, cat("LANGUAGE|PROOF_ASSISTANT|MM-PA|",
          "BEEP|EXIT|QUIT|READ|ERASE|",
          "OPEN|CLOSE|SHOW|SEARCH|SET|VERIFY|SUBMIT|SYSTEM|PROVE|FILE|WRITE|",

          "MARKUP|ASSIGN|REPLACE|MATCH|UNIFY|LET|INITIALIZE|DELETE|IMPROVE|",

          "MINIMIZE_WITH|EXPAND|UNDO|REDO|SAVE|DEMO|INVOKE|CLI|EXPLORE|TEX|",
          "LATEX|HTML|COMMENTS|BIBLIOGRAPHY|MORE|",
          "TOOLS|MIDI|$|<$>", NULL))) C.go2("pclbad");
      if (cmdMatches("HELP OPEN")) {


        if (!getFullArg(2, "LOG|TEX|<LOG>")) C.go2("pclbad");
        C.go2("pclgood");
      }
      if (cmdMatches("HELP CLOSE")) {


        if (!getFullArg(2, "LOG|TEX|<LOG>")) C.go2("pclbad");
        C.go2("pclgood");
      }
      if (cmdMatches("HELP SHOW")) {
        if (!getFullArg(2, cat("MEMORY|SETTINGS|LABELS|SOURCE|STATEMENT|",
            "PROOF|NEW_PROOF|USAGE|TRACE_BACK|ELAPSED_TIME|",
            "DISCOURAGED|<MEMORY>",
            NULL)))
          C.go2("pclbad");
        C.go2("pclgood");
      }
      if (cmdMatches("HELP SET")) {
        if (!getFullArg(2, cat(
            "ECHO|SCROLL|WIDTH|HEIGHT|UNDO|UNIFICATION_TIMEOUT|",
            "DISCOURAGEMENT|",
            "CONTRIBUTOR|",
            "ROOT_DIRECTORY|",
            "EMPTY_SUBSTITUTION|SEARCH_LIMIT|JEREMY_HENTY_FILTER|<ECHO>",
            NULL)))
          C.go2("pclbad");
        C.go2("pclgood");
      }
      if (cmdMatches("HELP VERIFY")) {
        if (!getFullArg(2, "PROOF|MARKUP|<PROOF>"))
          C.go2("pclbad");
        C.go2("pclgood");
      }
      if (cmdMatches("HELP WRITE")) {
        if (!getFullArg(2,
            "SOURCE|THEOREM_LIST|BIBLIOGRAPHY|RECENT_ADDITIONS|<SOURCE>"))
          C.go2("pclbad");
        C.go2("pclgood");
      }
      if (cmdMatches("HELP FILE")) {
        if (!getFullArg(2, "SEARCH"))
          C.go2("pclbad");
        C.go2("pclgood");
      }
      if (cmdMatches("HELP SAVE")) {
        if (!getFullArg(2,
            "PROOF|NEW_PROOF|<PROOF>"))
          C.go2("pclbad");
        C.go2("pclgood");
      }
      C.go2("pclgood");
    }

    if (cmdMatches("READ")) {
      if (!getFullArg(1, "& What is the name of the source input file? "))
        C.go2("pclbad");

      i = 1;
      while (1) {
        i++;
        if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
        if (lastArgMatches("/")) {
          i++;
          if (!getFullArg(i, "VERIFY|<VERIFY>")) C.go2("pclbad");
        } else {
          break;
        }
        break;
      }
      C.go2("pclgood");
    }

    if (cmdMatches("WRITE")) {
      if (!getFullArg(1,
          "SOURCE|THEOREM_LIST|BIBLIOGRAPHY|RECENT_ADDITIONS|<SOURCE>"))
        C.go2("pclbad");
      if (cmdMatches("WRITE SOURCE")) {
        if (g_sourceHasBeenRead == 0) {
          print2("?No source file has been read in.  Use READ first.\n");
          C.go2("pclbad");
        }
        if (!getFullArg(2, cat(
            "* What is the name of the source output file <",
            g_input_fn, ">? ", NULL)))
          C.go2("pclbad");
        if (!strcmp(g_input_fn, g_fullArg[2])) {
          print2(
          "The input file will be renamed %s~1.\n", g_input_fn);
        }


        i = 2;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (!getFullArg(i, cat(

                "FORMAT|REWRAP",


                "|SPLIT|NO_VERSIONING|KEEP_INCLUDES|EXTRACT",
                "|<REWRAP>", NULL)))
              C.go2("pclbad");

            if (lastArgMatches("EXTRACT")) {
              i++;
              if (!getFullArg(i, "* What statement label? "))
                C.go2("pclbad");
            }
          } else {
            break;
          }

        }

        C.go2("pclgood");
      }
      if (cmdMatches("WRITE THEOREM_LIST")) {
        if (g_sourceHasBeenRead == 0) {
          print2("?No source file has been read in.  Use READ first.\n");
          C.go2("pclbad");
        }

        i = 1;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (!getFullArg(i, cat(
                "THEOREMS_PER_PAGE|SHOW_LEMMAS|HTML|ALT_HTML|NO_VERSIONING",
                "|<THEOREMS_PER_PAGE>", NULL)))
              C.go2("pclbad");
            if (lastArgMatches("THEOREMS_PER_PAGE")) {
              i++;
              if (!getFullArg(i, "# How many theorems per page <100>? "))
                C.go2("pclbad");
            }
          } else {
            break;
          }

        }
        C.go2("pclgood");
      }
      if (cmdMatches("WRITE BIBLIOGRAPHY")) {
        if (g_sourceHasBeenRead == 0) {
          print2("?No source file has been read in.  Use READ first.\n");
          C.go2("pclbad");
        }
        if (!getFullArg(2, cat(
            "* What is the bibliography HTML input/output file <",
            "mmbiblio.html", ">? ", NULL)))
          C.go2("pclbad");
        print2(
          "The old file will be renamed %s~1.\n", g_fullArg[2]);
        C.go2("pclgood");
      }
      if (cmdMatches("WRITE RECENT_ADDITIONS")) {
        if (g_sourceHasBeenRead == 0) {
          print2("?No source file has been read in.  Use READ first.\n");
          C.go2("pclbad");
        }
        if (!getFullArg(2, cat(
            "* What is the Recent Additions HTML input/output file <",
            "mmrecent.html", ">? ", NULL)))
          C.go2("pclbad");
        print2(
          "The old file will be renamed %s~1.\n", g_fullArg[2]);


        i = 2;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (!getFullArg(i, cat(
                "LIMIT|HTML|ALT_HTML",
                "|<LIMIT>", NULL)))
              C.go2("pclbad");
            if (lastArgMatches("LIMIT")) {
              i++;
              if (!getFullArg(i, "# How many most recent theorems <100>? "))
                C.go2("pclbad");
            }
          } else {
            break;
          }

        }
        C.go2("pclgood");
      }
    }

    if (cmdMatches("OPEN")) {


      if (!getFullArg(1, "LOG|TEX|<LOG>")) C.go2("pclbad");
      if (cmdMatches("OPEN LOG")) {
        if (g_logFileOpenFlag) {
          printLongLine(cat(
              "?Sorry, the log file \"", g_logFileName, "\" is currently open.  ",
  "Type CLOSE LOG to close the current log if you want to open another one."
              , NULL), "", " ");
          C.go2("pclbad");
        }
        if (!getFullArg(2, "* What is the name of logging output file? "))
          C.go2("pclbad");
      }
      if (cmdMatches("OPEN TEX")) {
        if (g_sourceHasBeenRead == 0) {
          print2("?No source file has been read in.  Use READ first.\n");
          C.go2("pclbad");
        }
        if (g_texFileOpenFlag) {
          printLongLine(cat(
              "?Sorry, the LaTeX file \"", g_texFileName, "\" is currently open.  ",
              "Type CLOSE TEX to close the current LaTeX file",
              " if you want to open another one."
              , NULL), "", " ");
          C.go2("pclbad");
        }
        if (!getFullArg(2, "* What is the name of LaTeX output file? "))
          C.go2("pclbad");


        i = 2;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (!getFullArg(i, cat(
                "NO_HEADER|OLD_TEX|<NO_HEADER>", NULL)))
              C.go2("pclbad");
          } else {
            break;
          }

        }

      }


      C.go2("pclgood");
    }

    if (cmdMatches("CLOSE")) {


      if (!getFullArg(1, "LOG|TEX|<LOG>")) C.go2("pclbad");
      C.go2("pclgood");
    }

    if (cmdMatches("FILE")) {
      if (!getFullArg(1, cat("SEARCH", NULL))) C.go2("pclbad");

      if (cmdMatches("FILE SEARCH")) {
        if (!getFullArg(2, "& What is the name of the file to search? "))
          C.go2("pclbad");
        if (!getFullArg(3, "* What is the string to search for? "))
          C.go2("pclbad");



        i = 3;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (i == 4) {
              if (!getFullArg(i, cat(
                  "FROM_LINE|TO_LINE|<FROM_LINE>", NULL)))
                C.go2("pclbad");
            } else {
              if (!getFullArg(i, cat(
                  "FROM_LINE|TO_LINE|<TO_LINE>", NULL)))
                C.go2("pclbad");
            }
            if (lastArgMatches("FROM_LINE")) {
              i++;
              if (!getFullArg(i, "# From what line number <1>? "))
                C.go2("pclbad");
            }
            if (lastArgMatches("TO_LINE")) {
              i++;
              if (!getFullArg(i, "# To what line number <999999>? "))
                C.go2("pclbad");
            }
            if (lastArgMatches("WINDOW")) {
              i++;
              if (!getFullArg(i, "# How big a window around matched lines <0>? "))
                C.go2("pclbad");
            }
          } else {
            break;
          }

        }


        C.go2("pclgood");
      }
      C.go2("pclgood");
    }

    if (cmdMatches("SHOW")) {
      if (!g_PFASmode) {
        if (!getFullArg(1, cat(
     "SETTINGS|LABELS|STATEMENT|SOURCE|PROOF|MEMORY|TRACE_BACK|",
     "USAGE|ELAPSED_TIME|DISCOURAGED|<SETTINGS>", NULL)))
          C.go2("pclbad");
      } else {
        if (!getFullArg(1, cat("NEW_PROOF|",
     "SETTINGS|LABELS|STATEMENT|SOURCE|PROOF|MEMORY|TRACE_BACK|",
     "USAGE|ELAPSED_TIME|DISCOURAGED|<SETTINGS>",
            NULL)))
          C.go2("pclbad");
      }
      if (g_showStatement) {
        if (g_showStatement < 1 || g_showStatement > g_statements) bug(1110);
        let(&defaultArg, cat(" <",g_Statement[g_showStatement].labelName, ">",
            NULL));
      } else {
        let(&defaultArg, "");
      }


      if (cmdMatches("SHOW TRACE_BACK")) {
        if (g_sourceHasBeenRead == 0) {
          print2("?No source file has been read in.  Use READ first.\n");
          C.go2("pclbad");
        }
        if (!getFullArg(2,
            cat("* What is the statement label", defaultArg, "? ", NULL)))
          C.go2("pclbad");


        i = 2;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (!getFullArg(i, cat(

                "ALL|ESSENTIAL|AXIOMS|TREE|DEPTH|COUNT_STEPS|MATCH|TO",
                "|<ALL>", NULL)))
              C.go2("pclbad");
            if (lastArgMatches("DEPTH")) {
              i++;
              if (!getFullArg(i, "# How many indentation levels <999>? "))
                C.go2("pclbad");
            }

            if (lastArgMatches("MATCH")) {
              i++;
              if (!getFullArg(i, "* What statement label? "))
                C.go2("pclbad");
            }

            if (lastArgMatches("TO")) {
              i++;
              if (!getFullArg(i, "* What statement label? "))
                C.go2("pclbad");
            }
          } else {
            break;
          }

        }

        C.go2("pclgood");
      }

      if (cmdMatches("SHOW USAGE")) {
        if (g_sourceHasBeenRead == 0) {
          print2("?No source file has been read in.  Use READ first.\n");
          C.go2("pclbad");
        }
        if (!getFullArg(2,
            cat("* What is the statement label", defaultArg, "? ", NULL)))
          C.go2("pclbad");


        i = 2;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (!getFullArg(i, cat(
                "DIRECT|RECURSIVE|ALL",
                "|<DIRECT>", NULL)))
              C.go2("pclbad");
          } else {
            break;
          }

        }

        C.go2("pclgood");
      }


      if (cmdMatches("SHOW LABELS")) {
        if (g_sourceHasBeenRead == 0) {
          print2("?No source file has been read in.  Use READ first.\n");
          C.go2("pclbad");
        }
        if (!getFullArg(2,
            "* What are the labels to match (* = wildcard) <*>?"))
          C.go2("pclbad");

        i = 2;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (!getFullArg(i, cat("ALL|LINEAR|<ALL>", NULL)))
              C.go2("pclbad");
          } else {
            break;
          }

        }
        C.go2("pclgood");
      }
      if (cmdMatches("SHOW STATEMENT")) {
        if (g_sourceHasBeenRead == 0) {
          print2("?No source file has been read in.  Use READ first.\n");
          C.go2("pclbad");
        }
        if (!getFullArg(2,
            cat("* What is the statement label", defaultArg, "? ", NULL)))
          C.go2("pclbad");

        i = 2;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (!getFullArg(i, cat(
                "FULL|COMMENT|TEX|OLD_TEX|HTML|ALT_HTML|TIME|BRIEF_HTML",

                "|BRIEF_ALT_HTML|MNEMONICS|NO_VERSIONING|<FULL>", NULL)))
              C.go2("pclbad");
          } else {
            break;
          }

        }
        C.go2("pclgood");
      }
      if (cmdMatches("SHOW SOURCE")) {
        if (g_sourceHasBeenRead == 0) {
          print2("?No source file has been read in.  Use READ first.\n");
          C.go2("pclbad");
        }
        if (!getFullArg(2,
            cat("* What is the statement label", defaultArg, "? ", NULL))) {
          C.go2("pclbad");
        }
        C.go2("pclgood");
      }


      if (cmdMatches("SHOW PROOF")) {
        if (g_sourceHasBeenRead == 0) {
          print2("?No source file has been read in.  Use READ first.\n");
          C.go2("pclbad");
        }
        if (!getFullArg(2,
            cat("* What is the statement label", defaultArg, "? ", NULL)))
          C.go2("pclbad");


        i = 2;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (!getFullArg(i, cat(
                "ESSENTIAL|ALL|UNKNOWN|FROM_STEP|TO_STEP|DEPTH",

                "|REVERSE|VERBOSE|NORMAL|PACKED|COMPRESSED|EXPLICIT",
                "|FAST",
                "|OLD_COMPRESSION",

                "|STATEMENT_SUMMARY|DETAILED_STEP|TEX|OLD_TEX|HTML",
                "|LEMMON|START_COLUMN|NO_REPEATED_STEPS",
                "|RENUMBER|SIZE|<ESSENTIAL>", NULL)))
              C.go2("pclbad");
            if (lastArgMatches("FROM_STEP")) {
              i++;
              if (!getFullArg(i, "# From what step <1>? "))
                C.go2("pclbad");
            }
            if (lastArgMatches("TO_STEP")) {
              i++;
              if (!getFullArg(i, "# To what step <9999>? "))
                C.go2("pclbad");
            }
            if (lastArgMatches("DEPTH")) {
              i++;
              if (!getFullArg(i, "# How many indentation levels <999>? "))
                C.go2("pclbad");
            }
            if (lastArgMatches("DETAILED_STEP")) {
              i++;
              if (!getFullArg(i, "# Display details of what step <1>? "))
                C.go2("pclbad");
            }
            if (lastArgMatches("START_COLUMN")) {
              i++;
              if (!getFullArg(i, cat(
                  "# At what column should the formula start <",
                  str((double)DEFAULT_COLUMN), ">? ", NULL)))
                C.go2("pclbad");
            }
          } else {
            break;
          }

        }
        C.go2("pclgood");
      }


      if (cmdMatches("SHOW NEW_PROOF")) {
        if (g_sourceHasBeenRead == 0) {
          print2("?No source file has been read in.  Use READ first.\n");
          C.go2("pclbad");
        }


        i = 1;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (!getFullArg(i, cat(
                "ESSENTIAL|ALL|UNKNOWN|FROM_STEP|TO_STEP|DEPTH",

                "|REVERSE|VERBOSE|NORMAL|PACKED|COMPRESSED|EXPLICIT",
                "|OLD_COMPRESSION",
                "|NOT_UNIFIED|TEX|HTML",
                "|LEMMON|START_COLUMN|NO_REPEATED_STEPS",
                "|RENUMBER|<ESSENTIAL>", NULL)))
              C.go2("pclbad");
            if (lastArgMatches("FROM_STEP")) {
              i++;
              if (!getFullArg(i, "# From what step <1>? "))
                C.go2("pclbad");
            }
            if (lastArgMatches("TO_STEP")) {
              i++;
              if (!getFullArg(i, "# To what step <9999>? "))
                C.go2("pclbad");
            }
            if (lastArgMatches("DEPTH")) {
              i++;
              if (!getFullArg(i, "# How many indentation levels <999>? "))
                C.go2("pclbad");
            }
            if (lastArgMatches("START_COLUMN")) {
              i++;
              if (!getFullArg(i, cat(
                  "# At what column should the formula start <",
                  str((double)DEFAULT_COLUMN), ">? ", NULL)))
                C.go2("pclbad");
            }
          } else {
            break;
          }

        }
        C.go2("pclgood");
      }


      C.go2("pclgood");
    }

    if (cmdMatches("SEARCH")) {
      if (g_sourceHasBeenRead == 0) {
        print2("?No source file has been read in.  Use READ first.\n");
        C.go2("pclbad");
      }
      if (!getFullArg(1,
          "* What are the labels to match (* = wildcard) <*>?"))
        C.go2("pclbad");
      if (!getFullArg(2, "* Search for what math symbol string? "))
        C.go2("pclbad");

      i = 2;
      while (1) {
        i++;
        if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
        if (lastArgMatches("/")) {
          i++;
          if (!getFullArg(i, cat("ALL|COMMENTS|JOIN|<ALL>", NULL)))
            C.go2("pclbad");
        } else {
          break;
        }

      }
      C.go2("pclgood");

    }


    if (cmdMatches("SAVE")) {
      if (!g_PFASmode) {
        if (!getFullArg(1,
            "PROOF|<PROOF>"))
          C.go2("pclbad");
      } else {
        if (!getFullArg(1, cat("NEW_PROOF|",
            "PROOF|<NEW_PROOF>",
            NULL)))
          C.go2("pclbad");
      }
      if (g_showStatement) {
        if (g_showStatement < 0) bug(1111);
        let(&defaultArg, cat(" <",g_Statement[g_showStatement].labelName, ">", NULL));
      } else {
        let(&defaultArg, "");
      }


      if (cmdMatches("SAVE PROOF")) {
        if (g_sourceHasBeenRead == 0) {
          print2("?No source file has been read in.  Use READ first.\n");
          C.go2("pclbad");
        }
        if (!getFullArg(2,
            cat("* What is the statement label", defaultArg, "? ", NULL)))
          C.go2("pclbad");


        i = 2;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (!getFullArg(i, cat(
                "NORMAL|PACKED|COMPRESSED|EXPLICIT",
                "|FAST|OLD_COMPRESSION",
                "|TIME|<NORMAL>", NULL)))
              C.go2("pclbad");
          } else {
            break;
          }

        }
        C.go2("pclgood");
      }


      if (cmdMatches("SAVE NEW_PROOF")) {
        if (g_sourceHasBeenRead == 0) {
          print2("?No source file has been read in.  Use READ first.\n");
          C.go2("pclbad");
        }


        i = 1;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (!getFullArg(i, cat(
                "NORMAL|PACKED|COMPRESSED|EXPLICIT",


                "|OLD_COMPRESSION|OVERRIDE",
                "|<NORMAL>", NULL)))
              C.go2("pclbad");
          } else {
            break;
          }

        }
        C.go2("pclgood");
      }


      C.go2("pclgood");
    }


    if (cmdMatches("PROVE")) {
      if (g_sourceHasBeenRead == 0) {
        print2("?No source file has been read in.  Use READ first.\n");
        C.go2("pclbad");
      }
      if (!g_proveStatement) g_proveStatement = g_showStatement;
      if (g_proveStatement) {
        let(&defaultArg, cat(" <",g_Statement[g_proveStatement].labelName, ">", NULL));
      } else {
        let(&defaultArg, "");
      }
      if (!getFullArg(1,
          cat("* What is the label of the statement you want to try proving",
          defaultArg, "? ", NULL)))
        C.go2("pclbad");



      i = 1;
      while (1) {
        i++;
        if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
        if (lastArgMatches("/")) {
          i++;
          if (!getFullArg(i, "OVERRIDE|<OVERRIDE>")) C.go2("pclbad");
        } else {
          break;
        }
        break;
      }

      C.go2("pclgood");
    }



    if (cmdMatches("MATCH")) {
      if (!getFullArg(1,
          "STEP|ALL|<ALL>")) C.go2("pclbad");
      if (cmdMatches("MATCH STEP")) {
        if (!getFullArg(2, "# What step number? ")) C.go2("pclbad");

        i = 2;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (!getFullArg(i, cat(
                "MAX_ESSENTIAL_HYP|<MAX_ESSENTIAL_HYP>", NULL)))
              C.go2("pclbad");
            if (lastArgMatches("MAX_ESSENTIAL_HYP")) {
              i++;
              if (!getFullArg(i,
  "# Maximum number of essential hypotheses to allow for a match <0>? "))
                C.go2("pclbad");
            }
          } else {
            break;
          }
          break;
        }
        C.go2("pclgood");
      }
      if (cmdMatches("MATCH ALL")) {

        i = 1;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (!getFullArg(i, cat(
                "ESSENTIAL|MAX_ESSENTIAL_HYP|<ESSENTIAL>", NULL)))
              C.go2("pclbad");
            if (lastArgMatches("MAX_ESSENTIAL_HYP")) {
              i++;
              if (!getFullArg(i,
  "# Maximum number of essential hypotheses to allow for a match <0>? "))
                C.go2("pclbad");
            }
          } else {
            break;
          }

        }
        C.go2("pclgood");
      }
      C.go2("pclgood");
    }

    if (cmdMatches("INITIALIZE")) {
      if (!getFullArg(1,
          "STEP|ALL|USER|<ALL>")) C.go2("pclbad");
      if (cmdMatches("INITIALIZE STEP")) {
        if (!getFullArg(2, "# What step number? ")) C.go2("pclbad");
      }
      C.go2("pclgood");
    }

    if (cmdMatches("IMPROVE")) {
      if (!getFullArg(1,
        "* What step number, or FIRST, or LAST, or ALL <ALL>? ")) C.go2("pclbad");


      i = 1;
      while (1) {
        i++;
        if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
        if (lastArgMatches("/")) {
          i++;
          if (!getFullArg(i,


         "DEPTH|NO_DISTINCT|1|2|3|SUBPROOFS|OVERRIDE|INCLUDE_MATHBOXES|<DEPTH>")
              ) C.go2("pclbad");
          if (lastArgMatches("DEPTH")) {
            i++;
            if (!getFullArg(i,
  "# What is maximum depth for searching statements with $e hypotheses <0>? "))
              C.go2("pclbad");
          }
        } else {
          break;
        }

      }
      C.go2("pclgood");
    }



    if (cmdMatches("MINIMIZE_WITH")) {
      if (!getFullArg(1, "* What statement label? ")) C.go2("pclbad");

      i = 1;
      while (1) {
        i++;
        if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
        if (lastArgMatches("/")) {
          i++;
          if (!getFullArg(i, cat(
              "VERBOSE|MAY_GROW|EXCEPT|OVERRIDE|INCLUDE_MATHBOXES|",
              "ALLOW_NEW_AXIOMS|NO_NEW_AXIOMS_FROM|FORBID|TIME|<VERBOSE>",
              NULL)))






            C.go2("pclbad");


          if (lastArgMatches("EXCEPT")) {
            i++;
            if (!getFullArg(i, "* What statement label match pattern? "))
              C.go2("pclbad");
          }

          if (lastArgMatches("ALLOW_NEW_AXIOMS")) {
            i++;
            if (!getFullArg(i, "* What statement label match pattern? "))
              C.go2("pclbad");
          }

          if (lastArgMatches("NO_NEW_AXIOMS_FROM")) {
            i++;
            if (!getFullArg(i, "* What statement label match pattern? "))
              C.go2("pclbad");
          }

          if (lastArgMatches("FORBID")) {
            i++;
            if (!getFullArg(i, "* What statement label match pattern? "))
              C.go2("pclbad");
          }
        } else {
          break;
        }

      }
      C.go2("pclgood");
    }


    if (cmdMatches("EXPAND")) {
      if (!getFullArg(1, "* What statement label? ")) C.go2("pclbad");
      C.go2("pclgood");
    }

    if (cmdMatches("UNIFY")) {
      if (!getFullArg(1,
          "STEP|ALL|<ALL>")) C.go2("pclbad");
      if (cmdMatches("UNIFY STEP")) {
        if (!getFullArg(2, "# What step number? ")) C.go2("pclbad");
        C.go2("pclgood");
      }
      if (cmdMatches("UNIFY ALL")) {

        i = 1;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (!getFullArg(i, cat(
                "INTERACTIVE|<INTERACTIVE>", NULL)))
              C.go2("pclbad");
          } else {
            break;
          }
          break;
        }
        C.go2("pclgood");
      }
    }

    if (cmdMatches("DELETE")) {
      if (!getFullArg(1,
          "STEP|ALL|FLOATING_HYPOTHESES|<STEP>")) C.go2("pclbad");
      if (lastArgMatches("STEP")) {
        if (!getFullArg(2, "# What step number? ")) C.go2("pclbad");
        C.go2("pclgood");
      }
      C.go2("pclgood");
    }


    if (cmdMatches("ADD")) {
      if (!getFullArg(1,
          "UNIVERSE|<UNIVERSE>")) C.go2("pclbad");

    }

    if (cmdMatches("REPLACE")) {

      if (!getFullArg(1, "* What step number, or FIRST, or LAST <LAST>? "))
        C.go2("pclbad");
      if (!getFullArg(2, "* With what statement label? ")) C.go2("pclbad");

      i = 2;


      while (1) {
        i++;
        if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
        if (lastArgMatches("/")) {
          i++;
          if (!getFullArg(i, cat(
              "OVERRIDE|<OVERRIDE>", NULL)))
            C.go2("pclbad");
        } else {
          break;
        }
        break;
      }

      C.go2("pclgood");
    }

    if (cmdMatches("LET")) {
      if (!getFullArg(1, "STEP|VARIABLE|<STEP>")) C.go2("pclbad");
      if (cmdMatches("LET STEP")) {
        if (!getFullArg(2, "* What step number, or FIRST, or LAST <LAST>? "))
          C.go2("pclbad");
      }
      if (cmdMatches("LET VARIABLE")) {
        if (!getFullArg(2, "* Assign what variable (format $nn)? ")) C.go2("pclbad");
      }
      if (!getFullArg(3, "=|<=>")) C.go2("pclbad");
      if (!getFullArg(4, "* With what math symbol string? "))
        C.go2("pclbad");
      C.go2("pclgood");
    }

    if (cmdMatches("ASSIGN")) {
      if (!getFullArg(1,
          "* What step number, or FIRST, or LAST <LAST>? ")) C.go2("pclbad");

      if (!getFullArg(2, "* With what statement label? ")) C.go2("pclbad");

      i = 2;
      while (1) {
        i++;
        if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
        if (lastArgMatches("/")) {
          i++;
          if (!getFullArg(i, cat(
              "NO_UNIFY|OVERRIDE|<NO_UNIFY>", NULL)))
            C.go2("pclbad");
        } else {
          break;
        }

      }
      C.go2("pclgood");
    }

    if (cmdMatches("UNDO")) {
      C.go2("pclgood");
    }

    if (cmdMatches("REDO")) {
      C.go2("pclgood");
    }

    if (cmdMatches("SET")) {
      let(&tmpStr, cat(

          "WIDTH|HEIGHT|UNDO|ECHO|SCROLL|",
          "DEBUG|MEMORY_STATUS|SEARCH_LIMIT|UNIFICATION_TIMEOUT|",
          "DISCOURAGEMENT|",
          "CONTRIBUTOR|",
          "ROOT_DIRECTORY|",
          "EMPTY_SUBSTITUTION|JEREMY_HENTY_FILTER|<WIDTH>", NULL));
      if (!getFullArg(1,tmpStr)) C.go2("pclbad");
      if (cmdMatches("SET DEBUG")) {
        if (!getFullArg(2, "FLAG|OFF|<OFF>")) C.go2("pclbad");
        if (lastArgMatches("FLAG")) {
          if (!getFullArg(3, "4|5|6|7|8|9|<5>")) C.go2("pclbad");
        }
        C.go2("pclgood");
      }

      if (cmdMatches("SET ECHO")) {
        if (g_commandEcho) {
          if (!getFullArg(2, "ON|OFF|<OFF>")) C.go2("pclbad");
        } else {
          if (!getFullArg(2, "ON|OFF|<ON>")) C.go2("pclbad");
        }
        C.go2("pclgood");
      }

      if (cmdMatches("SET SCROLL")) {
        if (g_scrollMode == 1) {
          if (!getFullArg(2, "CONTINUOUS|PROMPTED|<CONTINUOUS>")) C.go2("pclbad");
        } else {
          if (!getFullArg(2, "CONTINUOUS|PROMPTED|<PROMPTED>")) C.go2("pclbad");
        }
        C.go2("pclgood");
      }

      if (cmdMatches("SET DISCOURAGEMENT")) {
        if (g_globalDiscouragement) {
          if (!getFullArg(2, "ON|OFF|<OFF>")) C.go2("pclbad");
        } else {
          if (!getFullArg(2, "ON|OFF|<ON>")) C.go2("pclbad");
        }
        C.go2("pclgood");
      }

      if (cmdMatches("SET MEMORY_STATUS")) {
        if (g_memoryStatus) {
          if (!getFullArg(2, "ON|OFF|<OFF>")) C.go2("pclbad");
        } else {
          if (!getFullArg(2, "ON|OFF|<ON>")) C.go2("pclbad");
        }
        C.go2("pclgood");
      }


      if (cmdMatches("SET JEREMY_HENTY_FILTER")) {
        if (g_hentyFilter) {
          if (!getFullArg(2, "ON|OFF|<OFF>")) C.go2("pclbad");
        } else {
          if (!getFullArg(2, "ON|OFF|<ON>")) C.go2("pclbad");
        }
        C.go2("pclgood");
      }

      if (cmdMatches("SET CONTRIBUTOR")) {
        if (!getFullArg(2, cat(
            "* What is the contributor name for SAVE (NEW_)PROOF <",
            g_contributorName, ">? ", NULL)))
          C.go2("pclbad");
        C.go2("pclgood");
      }

      if (cmdMatches("SET ROOT_DIRECTORY")) {
        if (!getFullArg(2, cat(
            "* What is the root directory path (use space if none) <",
            g_rootDirectory, ">? ", NULL)))
          C.go2("pclbad");
        C.go2("pclgood");
      }

      if (cmdMatches("SET SEARCH_LIMIT")) {
        if (!getFullArg(2, cat(
            "# What is search limit for IMPROVE command <",
            str((double)g_userMaxProveFloat), ">? ", NULL)))
          C.go2("pclbad");
        C.go2("pclgood");
      }

      if (cmdMatches("SET UNIFICATION_TIMEOUT")) {
        if (!getFullArg(2, cat(
           "# What is maximum number of unification trials <",
            str((double)g_userMaxUnifTrials), ">? ", NULL)))
          C.go2("pclbad");
        C.go2("pclgood");
      }

      if (cmdMatches("SET WIDTH")) {
        if (!getFullArg(2, cat(
           "# What is maximum line length on your screen <",
            str((double)g_screenHeight), ">? ", NULL)))
          C.go2("pclbad");
        C.go2("pclgood");
      }

      if (cmdMatches("SET HEIGHT")) {
        if (!getFullArg(2, cat(
           "# What is number of lines your screen displays <",
            str((double)g_screenHeight), ">? ", NULL)))
          C.go2("pclbad");
        C.go2("pclgood");
      }

      if (cmdMatches("SET UNDO")) {
        if (!getFullArg(2, cat(
           "# What is the maximum number of UNDOs <",
            str((double)(processUndoStack(NULL, PUS_GET_SIZE, "", 0))),
            ">? ", NULL)))
          C.go2("pclbad");
        C.go2("pclgood");
      }

      if (cmdMatches("SET EMPTY_SUBSTITUTION")) {
        if (g_minSubstLen == 0) {
          if (!getFullArg(2, "ON|OFF|<OFF>")) C.go2("pclbad");
        } else {
          if (!getFullArg(2, "ON|OFF|<ON>")) C.go2("pclbad");
        }
        C.go2("pclgood");
      }

    }


    if (cmdMatches("ERASE")) {
      C.go2("pclgood");
    }

    if (cmdMatches("MORE")) {
      if (!getFullArg(1,
         "* What is the name of the file to display? "))
        C.go2("pclbad");
      C.go2("pclgood");
    }

    if (cmdMatches("TOOLS")) {
      C.go2("pclgood");
    }

    if (cmdMatches("VERIFY")) {
      if (!getFullArg(1,
          "PROOF|MARKUP|<PROOF>"))
        C.go2("pclbad");
      if (cmdMatches("VERIFY PROOF")) {
        if (g_sourceHasBeenRead == 0) {
          print2("?No source file has been read in.  Use READ first.\n");
          C.go2("pclbad");
        }
        if (!getFullArg(2,
            "* What are the labels to match (* = wildcard) <*>?"))
          C.go2("pclbad");


        i = 2;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (!getFullArg(i, cat(
                "SYNTAX_ONLY",
                "|<SYNTAX_ONLY>", NULL)))
              C.go2("pclbad");
          } else {
            break;
          }
          break;
        }

        C.go2("pclgood");
      }


      if (cmdMatches("VERIFY MARKUP")) {
        if (g_sourceHasBeenRead == 0) {
          print2("?No source file has been read in.  Use READ first.\n");
          C.go2("pclbad");
        }
        if (!getFullArg(2,
            "* What are the labels to match (* = wildcard) <*>?"))
          C.go2("pclbad");


        i = 2;
        while (1) {
          i++;
          if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
          if (lastArgMatches("/")) {
            i++;
            if (!getFullArg(i, cat(
                "DATE_SKIP|FILE_SKIP|TOP_DATE_SKIP|VERBOSE",
                "|UNDERSCORE_SKIP|MATHBOX_SKIP|<DATE_SKIP>", NULL)))
              C.go2("pclbad");
          } else {
            break;
          }

        }

        C.go2("pclgood");
      }
    }

    if (cmdMatches("DBG")) {
      if (!getFullArg(1, "* What is the debugging string? "))
        C.go2("pclbad");
      C.go2("pclgood");
    }


    if (cmdMatches("MARKUP")) {
      if (g_sourceHasBeenRead == 0) {
        print2("?No source file has been read in.  Use READ first.\n");
        C.go2("pclbad");
      }
      if (!getFullArg(1,
          "* What is the name of the input file with markup? "))
        C.go2("pclbad");
      if (!getFullArg(2,
          "* What is the name of the HTML output file? "))
        C.go2("pclbad");


      i = 2;
      while (1) {
        i++;
        if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
        if (lastArgMatches("/")) {
          i++;
          if (!getFullArg(i, cat(
              "HTML|ALT_HTML|SYMBOLS|LABELS|NUMBER_AFTER_LABEL|BIB_REFS",
              "|UNDERSCORES|CSS|<ALT_HTML>", NULL)))
            C.go2("pclbad");
        } else {
          break;
        }

      }
      C.go2("pclgood");
    }

    if (cmdMatches("MIDI")) {
      if (g_sourceHasBeenRead == 0) {
        print2("?No source file has been read in.  Use READ first.\n");
        C.go2("pclbad");
      }
      if (!getFullArg(1,
         "* Statement label to create MIDI for (* matches any substring) <*>?"))
        C.go2("pclbad");

      i = 1;
      while (1) {
        i++;
        if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
        if (lastArgMatches("/")) {
          i++;
          if (!getFullArg(i, cat("PARAMETER|<PARAMETER>", NULL)))
            C.go2("pclbad");
          i++;
          if (!getFullArg(i,
              "* What is the parameter string <FSH>?"))
            C.go2("pclbad");
        } else {
          break;
        }
        break;
      }
      C.go2("pclgood");
    }

    if (cmdMatches("EXIT") || cmdMatches("QUIT")
        || cmdMatches("_EXIT_PA")) {


      i = 0;
      while (1) {
        i++;
        if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
        if (lastArgMatches("/")) {
          i++;
          if (!getFullArg(i, cat(
              "FORCE|<FORCE>", NULL)))
            C.go2("pclbad");
        } else {
          break;
        }
        break;
      }

      C.go2("pclgood");
    }

  } else {

    let(&tmpStr, cat(
          "HELP|SUBMIT|",
          "ADD|DELETE|SUBSTITUTE|S|SWAP|CLEAN|INSERT|BREAK|BUILD|MATCH|SORT|",
          "UNDUPLICATE|DUPLICATE|UNIQUE|REVERSE|RIGHT|PARALLEL|NUMBER|COUNT|",
          "COPY|C|TYPE|T|TAG|UPDATE|BEEP|B|EXIT|QUIT|<HELP>", NULL));
    if (!getFullArg(0,tmpStr))
      C.go2("pclbad");

    if (cmdMatches("HELP")) {
      if (!getFullArg(1, cat(
          "ADD|DELETE|SUBSTITUTE|S|SWAP|CLEAN|INSERT|BREAK|BUILD|MATCH|SORT|",
          "UNDUPLICATE|DUPLICATE|UNIQUE|REVERSE|RIGHT|PARALLEL|NUMBER|COUNT|",
          "TYPE|T|TAG|UPDATE|BEEP|B|EXIT|QUIT|",
          "COPY|C|SUBMIT|SYSTEM|CLI|",
          "$|<$>", NULL))) C.go2("pclbad");
      C.go2("pclgood");
    }
    if (cmdMatches("ADD") || cmdMatches("TAG")) {
      if (!getFullArg(1, "& Input/output file? "))
        C.go2("pclbad");
      if (!getFullArg(2, "* String to add to beginning of each line <>? "))
        C.go2("pclbad");
      if (!getFullArg(3, "* String to add to end of each line <>? "))
        C.go2("pclbad");
      if (cmdMatches("TAG")) {
        if (!getFullArg(4,
            "* String to match to start range (null = any line) <>? "))
          C.go2("pclbad");
        if (!getFullArg(5,
            "# Which occurrence of start match to start range <1>? "))
          C.go2("pclbad");
        if (!getFullArg(6,
            "* String to match to end range (null = any line) <>? "))
          C.go2("pclbad");
        if (!getFullArg(7,
            "# Which occurrence of end match to end range <1>? "))
          C.go2("pclbad");
      }
      C.go2("pclgood");
    }
    if (cmdMatches("DELETE")) {
      if (!getFullArg(1, "& Input/output file? "))
        C.go2("pclbad");
      if (!getFullArg(2,
"* String from which to start deleting (CR = beginning of line) <>? "))
        C.go2("pclbad");
      if (!getFullArg(3,
"* String at which to stop deleting (CR = end of line) <>? "))
        C.go2("pclbad");
      C.go2("pclgood");
    }
    if (cmdMatches("CLEAN")) {
      if (!getFullArg(1, "& Input/output file? "))
        C.go2("pclbad");
      if (!getFullArg(2,
          "* Subcommand(s) (D,B,E,R,Q,T,U,P,G,C,L,V) <B,E,R>? "))
        C.go2("pclbad");
      C.go2("pclgood");
    }
    if (cmdMatches("SWAP")) {
      if (!getFullArg(1, "& Input/output file? "))
        C.go2("pclbad");
      if (!getFullArg(2,
"* Character string to match between the halves to be swapped? "))
        C.go2("pclbad");
      C.go2("pclgood");
    }
    if (cmdMatches("SUBSTITUTE") || cmdMatches("S")) {
      if (!getFullArg(1, "& Input/output file? "))
        C.go2("pclbad");
      if (!getFullArg(2, "* String to replace? "))
        C.go2("pclbad");
      if (!getFullArg(3, "* Replace it with <>? "))
        C.go2("pclbad");
      if (!getFullArg(4,
"* Which occurrence in the line (1,2,... or ALL or EACH) <1>? "))
        C.go2("pclbad");
      if (!getFullArg(5,
"* Additional match required on line (null = match all) <>? "))
        C.go2("pclbad");
      C.go2("pclgood");
    }

    if (cmdMatches("INSERT")) {
      if (!getFullArg(1, "& Input/output file? "))
        C.go2("pclbad");
      if (!getFullArg(2, "* String to insert in each line <!>? "))
        C.go2("pclbad");
      if (!getFullArg(3, "# Column at which to insert the string <1>? "))
        C.go2("pclbad");
      C.go2("pclgood");
    }
    if (cmdMatches("BREAK")) {
      if (!getFullArg(1, "& Input/output file? "))
        C.go2("pclbad");
      if (!getFullArg(2,
          "* Special characters to use as token delimiters <()[],=:;{}>? "))
        C.go2("pclbad");
      C.go2("pclgood");
    }
    if (cmdMatches("MATCH")) {
      if (!getFullArg(1, "& Input/output file? "))
        C.go2("pclbad");
      if (!getFullArg(2,
"* String to match on each line (null = any non-blank line) <>? "))
        C.go2("pclbad");
      if (!getFullArg(3,
"* Output those lines containing the string (Y) or those not (N) <Y>? "))
        C.go2("pclbad");
      C.go2("pclgood");
    }
    if (cmdMatches("SORT")) {
      if (!getFullArg(1, "& Input/output file? "))
        C.go2("pclbad");
      if (!getFullArg(2,
          "* String to start key on each line (null string = column 1) <>? "))
        C.go2("pclbad");
      C.go2("pclgood");
    }
    if (cmdMatches("UNDUPLICATE") || cmdMatches("DUPLICATE") ||
        cmdMatches("UNIQUE") || cmdMatches("REVERSE") || cmdMatches("BUILD")
        || cmdMatches("RIGHT")) {
      if (!getFullArg(1, "& Input/output file? "))
        C.go2("pclbad");
      C.go2("pclgood");
    }

    if (cmdMatches("COUNT")) {
      if (!getFullArg(1, "& Input file? "))
        C.go2("pclbad");
      if (!getFullArg(2,
"* String to count <;>? "))
        C.go2("pclbad");
      C.go2("pclgood");
    }

    if (cmdMatches("COPY") || cmdMatches("C")) {
      if (!getFullArg(1, "* Comma-separated list of input files? "))
        C.go2("pclbad");
      if (!getFullArg(2, "* Output file? "))
        C.go2("pclbad");
      C.go2("pclgood");
    }


    if (cmdMatches("NUMBER")) {
      if (!getFullArg(1, "* Output file <n.tmp>? "))
        C.go2("pclbad");
      if (!getFullArg(2, "# First number <1>? "))
        C.go2("pclbad");
      if (!getFullArg(3, "# Last number <10>? "))
        C.go2("pclbad");
      if (!getFullArg(4, "# Increment <1>? "))
        C.go2("pclbad");
      C.go2("pclgood");
    }
    if (cmdMatches("TYPE") || cmdMatches("T")) {
      if (!getFullArg(1, "& File to display on the screen? "))
        C.go2("pclbad");
      if (!getFullArg(2, "* Num. lines to type or ALL (nothing = 10) <$>? "))
        C.go2("pclbad");
      C.go2("pclgood");
    }


    if (cmdMatches("UPDATE")) {
      print2(
"Warning: Do not comment out code - delete it before running UPDATE!  If\n");
      print2(
"rerunning UPDATE, do not tamper with \"start/end of deleted section\" comments!\n");
      print2(
"Edit out tag on header comment line!  Review the output file!\n");
      if (!getFullArg(1, "& Original (reference) program input file? "))
        C.go2("pclbad");
      if (!getFullArg(2, "& Edited program input file? "))
        C.go2("pclbad");
      if (!getFullArg(3, cat(
"* Edited program output file with revisions tagged <",
          g_fullArg[2], ">? ", NULL)))
        C.go2("pclbad");
      if (!strcmp(g_fullArg[2], g_fullArg[3])) {
        print2(
"The input file will be renamed %s~1.\n", g_fullArg[2]);
      }
      if (!getFullArg(4,
          cat("",
          str((double)(highestRevision(g_fullArg[1]) + 1)), " */>? ", NULL)))
        C.go2("pclbad");
      if (!getFullArg(5,
"# Successive lines required for match (more = better sync) <3>? "))
        C.go2("pclbad");
      C.go2("pclgood");
    }

    if (cmdMatches("PARALLEL")) {
      if (!getFullArg(1, "& Left file? "))
        C.go2("pclbad");
      if (!getFullArg(2, "& Right file? "))
        C.go2("pclbad");
      if (!getFullArg(3, cat("* Output file <",
          g_fullArg[1], ">? ", NULL)))
        C.go2("pclbad");
      if (!getFullArg(4,
          cat("* String to insert between the 2 input lines <>? ", NULL)))
        C.go2("pclbad");
      C.go2("pclgood");
    }


    if (cmdMatches("EXIT") || cmdMatches("QUIT")) {
      C.go2("pclgood");
    }


  }

  if (cmdMatches("SUBMIT")) {
    if (g_toolsMode) {
      let(&tmpStr, " <tools.cmd>");
    } else {
      let(&tmpStr, " <mm.cmd>");
    }
    if (!getFullArg(1, cat("& What is the name of command file to run",
        tmpStr, "? ", NULL))) {
      C.go2("pclbad");
    }



    i = 1;
    while (1) {
      i++;
      if (!getFullArg(i, "/|$|<$>")) C.go2("pclbad");
      if (lastArgMatches("/")) {
        i++;
        if (!getFullArg(i, cat(
            "SILENT",
            "|<SILENT>", NULL)))
          C.go2("pclbad");
      } else {
        break;
      }
      break;
    }

    C.go2("pclgood");
  }

  if (cmdMatches("BEEP") || cmdMatches("B")) {
    C.go2("pclgood");
  }


  print2("?This command has not been implemented yet.\n");
  print2("(This is really a bug--please report it.)\n");
  C.go2("pclbad");





  C.label("pclgood");

  if (!strcmp(g_fullArg[pntrLen(g_fullArg) - 1], chr(3))) {
    let((vstring *)(&g_fullArg[pntrLen(g_fullArg) - 1]), "");
    pntrLet(&g_fullArg, pntrLeft(g_fullArg, pntrLen(g_fullArg) - 1));
  }

  if (pntrLen(g_fullArg) > g_rawArgs) bug(1102);
  if (pntrLen(g_fullArg) < g_rawArgs) {
    let(&tmpStr, cat("?Too many arguments.  Use quotes around arguments with special",
        " characters and around Unix file names with \"/\"s.", NULL));
    printCommandError(cat(g_commandPrompt, g_commandLine, NULL), pntrLen(g_fullArg),
        tmpStr);
    C.go2("pclbad");
  }


  let(&g_fullArgString, "");
  for (i = 0; i < pntrLen(g_fullArg); i++) {
    let(&g_fullArgString, cat(g_fullArgString, " ", g_fullArg[i], NULL));
  }
  let(&g_fullArgString, right(g_fullArgString, 2));



  let(&defaultArg, "");
  let(&tmpStr, "");
  return (1);

  C.label("pclbad");

  let(&defaultArg, "");
  let(&tmpStr, "");
  return (0);

}



flag getFullArg(long arg, vstring cmdList1)
{


  pntrString *possCmd = NULL_PNTRSTRING;
  long possCmds, i, j, k, m, p, q;
  vstring defaultCmd = "";
  vstring infoStr = "";
  vstring tmpStr = "";
  vstring tmpArg = "";
  vstring errorLine = "";
  vstring keyword = "";
  vstring cmdList = "";
  File tmpFp;

  let(&cmdList,cmdList1);

  let(&errorLine, cat(g_commandPrompt,g_commandLine, NULL));


  if (cmdList[0] == '#') {
    let(&defaultCmd,
        seg(cmdList,instr(1,cmdList, "<"),instr(1,cmdList, ">")));


    if (g_rawArgs <= arg) {
      pntrLet(&g_rawArgPntr, pntrAddElement(g_rawArgPntr));
      nmbrLet(&g_rawArgNmbr, nmbrAddElement(g_rawArgNmbr, 0));
      g_rawArgs++;
      if (g_rawArgs <= arg) bug(1103);

      g_queryMode = 1;
      tmpArg = cmdInput1(right(cmdList,3));
      let(&errorLine,right(cmdList,3));
      if (tmpArg[0] == 0) {
        let(&tmpArg, seg(defaultCmd,2,len(defaultCmd) - 1));
      }
      let((vstring *)(&g_rawArgPntr[arg]), tmpArg);
      g_rawArgNmbr[arg] = len(cmdList) - 1;

    }


    let(&tmpArg,g_rawArgPntr[arg]);
    if (tmpArg[0] == 0) {

      let(&tmpArg, seg(defaultCmd,2,len(defaultCmd) - 1));
    }
    let(&tmpStr, str(val(tmpArg)));
    let(&tmpStr, cat(string(len(tmpArg)-len(tmpStr),'0'), tmpStr, NULL));
    if (strcmp(tmpStr, tmpArg)) {
      printCommandError(errorLine, arg,
          "?A number was expected here.");
      C.go2("return0");
    }

    let(&keyword, str(val(tmpArg)));
    C.go2("return1");
  }






  if (cmdList[0] == '*' || cmdList[0] == '&') {
    let(&defaultCmd,
        seg(cmdList,instr(1,cmdList, "<"),instr(1,cmdList, ">")));


    if (g_rawArgs <= arg) {
      if (!strcmp(defaultCmd, "<$>")) {

        let(&keyword,chr(3));
        C.go2("return1");
      }
      g_rawArgs++;
      pntrLet(&g_rawArgPntr, pntrAddElement(g_rawArgPntr));
      nmbrLet(&g_rawArgNmbr, nmbrAddElement(g_rawArgNmbr, 0));
      if (g_rawArgs <= arg) bug(1104);
      g_queryMode = 1;
      tmpArg = cmdInput1(right(cmdList,3));


      let(&errorLine,right(cmdList,3));
      if (tmpArg[0] == 0) {
        let(&tmpArg, seg(defaultCmd,2,len(defaultCmd) - 1));
      }
      let((vstring *)(&g_rawArgPntr[arg]), tmpArg);
      g_rawArgNmbr[arg] = len(cmdList) - 1;

    }

    let(&keyword,g_rawArgPntr[arg]);


    if (cmdList[0] == '*') {
      if ((keyword[0] == 'f' || keyword[0] == 'F')
          && instr(1, cmdList, " FIRST") != 0)
        let(&keyword, "FIRST");
      if ((keyword[0] == 'l' || keyword[0] == 'L')
          && instr(1, cmdList, " LAST") != 0)
        let(&keyword, "LAST");
      if ((keyword[0] == 'a' || keyword[0] == 'A')
          && instr(1, cmdList, " ALL") != 0)
        let(&keyword, "ALL");
    }

    if (keyword[0] == 0) {

      let(&keyword, seg(defaultCmd,2,len(defaultCmd) - 1));
    }
    if (cmdList[0] == '&') {

      let(&tmpStr, cat(g_rootDirectory, keyword, NULL));
      tmpFp = fopen(tmpStr, "r");
      if (!tmpFp) {
        let(&tmpStr,  cat(

  "?Sorry, couldn't open the file \"", tmpStr, "\".", NULL));
        printCommandError(errorLine, arg, tmpStr);
        C.go2("return0");
      }
      fclose(tmpFp);
    }
    C.go2("return1");
  }




  possCmds = 0;
  p = 0;
  while (1) {
    q = p;
    p = instr(p + 1, cat(cmdList, "|", NULL), "|");
    if (!p) break;
    pntrLet(&possCmd,pntrAddElement(possCmd));
    let((vstring *)(&possCmd[possCmds]),seg(cmdList,q+1,p-1));
    possCmds++;
  }
  if (!strcmp(left(possCmd[possCmds - 1],1), "<")) {

    defaultCmd = possCmd[possCmds - 1];
    if (!strcmp(defaultCmd, "<$>")) {
      let(&defaultCmd, "<nothing>");
    }
    pntrLet(&possCmd,pntrLeft(possCmd,possCmds - 1));
    possCmds--;
  }
  if (!strcmp(possCmd[possCmds - 1], "$")) {

    let((vstring *)(&possCmd[possCmds - 1]), "nothing");
  }


  if (possCmds < 1) bug(1105);
  if (possCmds == 1) {
    let(&infoStr,possCmd[0]);
  }
  if (possCmds == 2) {
    let(&infoStr, cat(possCmd[0], " or ",
        possCmd[1], NULL));
  }
  if (possCmds > 2) {
    let(&infoStr, "");
    for (i = 0; i < possCmds - 1; i++) {
      let(&infoStr, cat(infoStr,possCmd[i], ", ", NULL));
    }
    let(&infoStr, cat(infoStr, "or ",possCmd[possCmds - 1], NULL));
  }


  if (g_rawArgs <= arg && (strcmp(possCmd[possCmds - 1], "nothing")
      || g_queryMode == 1)) {

    let(&tmpStr, infoStr);
    if (defaultCmd[0] != 0) {
      let(&tmpStr, cat(tmpStr, " ",defaultCmd, NULL));
    }
    let(&tmpStr, cat(tmpStr, "? ", NULL));
    g_queryMode = 1;
    if (possCmds != 1) {
      tmpArg = cmdInput1(tmpStr);
    } else {


      if (!strcmp(cmdList, "$|<$>")) {
        let(&tmpArg, possCmd[0]);
        print2("The command so far is:  ");
        for (i = 0; i < arg; i++) {
          print2("%s ", g_fullArg[i]);
        }
        print2("%s\n", tmpArg);
      }
    }
    let(&errorLine,tmpStr);
    if (tmpArg[0] == 0) {
      let(&tmpArg, seg(defaultCmd,2,len(defaultCmd) - 1));
    }

    if (strcmp(tmpArg, "nothing")) {
      pntrLet(&g_rawArgPntr, pntrAddElement(g_rawArgPntr));
      nmbrLet(&g_rawArgNmbr, nmbrAddElement(g_rawArgNmbr, 0));
      g_rawArgs++;
      if (g_rawArgs <= arg) bug(1106);
      let((vstring *)(&g_rawArgPntr[arg]), tmpArg);
      g_rawArgNmbr[arg] = len(tmpStr) + 1;
    }

  }

  if (g_rawArgs <= arg) {

    let(&keyword,chr(3));
    C.go2("return1");
  }


  let(&tmpArg,edit(g_rawArgPntr[arg], 32));
  j = 0;
  k = 0;
  m = len(tmpArg);
  let(&tmpStr, "");

  for (i = 0; i < possCmds; i++) {
    if (!strcmp(possCmd[i], tmpArg)) {
      k = 1;
      j = i;
      break;
    }
    if (!strcmp(left(possCmd[i], m), tmpArg)) {
      if (!k) {
        let(&tmpStr, possCmd[i]);
      } else {
        let(&tmpStr, cat(tmpStr, ", ", possCmd[i], NULL));
      }
      j = i;
      k++;
    }
  }
  if (k < 1 || k > 1) {
    if (k < 1) {
      let(&tmpStr, cat("?Expected ", infoStr, ".", NULL));
    } else {
      if (k == 2) {
        p = instr(1,tmpStr, ", ");
        let(&tmpStr, cat(left(tmpStr,p-1), " or",right(tmpStr,p+1), NULL));
      } else {
        p = len(tmpStr) - 1;
        while (tmpStr[p] != ',') p--;
        let(&tmpStr, cat(left(tmpStr,p+1), " or",right(tmpStr,p+2), NULL));
      }
      let(&tmpStr, cat("?Ambiguous keyword - please specify ",tmpStr, ".", NULL));
    }
    printCommandError(errorLine, arg, tmpStr);
    C.go2("return0");
  }

  let(&keyword,possCmd[j]);
  C.go2("return1");

  C.label("return1");
  if (keyword[0] == 0) {
    if (g_rawArgs > arg && strcmp(defaultCmd, "<>")) {

      printCommandError("", arg,
          "?No default answer is available - please be explicit.");
      C.go2("return0");
    }
  }

  pntrLet(&g_fullArg,pntrAddElement(g_fullArg));
  if (pntrLen(g_fullArg) != arg + 1) bug(1107);
  let((vstring *)(&g_fullArg[arg]),keyword);


  j = pntrLen(possCmd);
  for (i = 0; i < j; i++) let((vstring *)(&possCmd[i]), "");
  pntrLet(&possCmd, NULL_PNTRSTRING);
  let(&defaultCmd, "");
  let(&infoStr, "");
  let(&tmpStr, "");
  let(&tmpArg, "");
  let(&errorLine, "");
  let(&keyword, "");
  let(&cmdList, "");
  return(1);

  C.label("return0");

  j = pntrLen(possCmd);
  for (i = 0; i < j; i++) let((vstring *)(&possCmd[i]), "");
  pntrLet(&possCmd, NULL_PNTRSTRING);
  let(&defaultCmd, "");
  let(&infoStr, "");
  let(&tmpStr, "");
  let(&tmpArg, "");
  let(&errorLine, "");
  let(&keyword, "");
  let(&cmdList, "");
  return(0);

}



void parseCommandLine(vstring line)
{


  vstring tokenWhiteSpace = " \t\n";
  vstring tokenComment = "!";


  vstring tmpStr = "";
  flag mode;
  long tokenStart, i, p, lineLen;

  vstring specialOneCharTokens = "";

  tokenStart = 0;

  if (!g_toolsMode) {
    let(&specialOneCharTokens, "/=");
  } else {
    let(&specialOneCharTokens, "");
  }

  lineLen = len(line);
  mode = 0;
  for (p = 0; p < lineLen; p++) {
    let(&tmpStr, "");
    if (mode == 0) {

      if (instr(1,tokenWhiteSpace,chr(line[p]))) {
        continue;
      }

      if (p == 0 &&  instr(1,tokenComment,chr(line[p]))) {
        break;
      }

      if (instr(1,specialOneCharTokens,chr(line[p]))) {
        pntrLet(&g_rawArgPntr, pntrAddElement(g_rawArgPntr));
        nmbrLet(&g_rawArgNmbr, nmbrAddElement(g_rawArgNmbr, p+1));

        let((vstring *)(&g_rawArgPntr[g_rawArgs]), chr(line[p]));
        g_rawArgs++;
        continue;
      }

      if (line[p] == '\'') {
        mode = 2;
        tokenStart = p + 2;
        continue;
      }
      if (line[p] == '\"') {
        mode = 3;
        tokenStart = p + 2;
        continue;
      }

      mode = 1;
      tokenStart = p + 1;
      continue;
    }
    if (mode == 1) {

      if (instr(1,tokenWhiteSpace,chr(line[p]))) {
        pntrLet(&g_rawArgPntr, pntrAddElement(g_rawArgPntr));
        nmbrLet(&g_rawArgNmbr, nmbrAddElement(g_rawArgNmbr, tokenStart));

        let((vstring *)(&g_rawArgPntr[g_rawArgs]), seg(line, tokenStart, p));
        g_rawArgs++;
        mode = 0;
        continue;
      }


      if (instr(1,specialOneCharTokens,chr(line[p]))) {
        pntrLet(&g_rawArgPntr, pntrAddElement(g_rawArgPntr));
        nmbrLet(&g_rawArgNmbr, nmbrAddElement(g_rawArgNmbr, tokenStart));

        let((vstring *)(&g_rawArgPntr[g_rawArgs]),seg(line, tokenStart, p));
        g_rawArgs++;
        pntrLet(&g_rawArgPntr, pntrAddElement(g_rawArgPntr));
        nmbrLet(&g_rawArgNmbr, nmbrAddElement(g_rawArgNmbr, p + 1));

        let((vstring *)(&g_rawArgPntr[g_rawArgs]), chr(line[p]));
        g_rawArgs++;
        mode = 0;
        continue;
      }

      if (line[p] == '\'') {
        pntrLet(&g_rawArgPntr, pntrAddElement(g_rawArgPntr));
        nmbrLet(&g_rawArgNmbr, nmbrAddElement(g_rawArgNmbr, tokenStart));

        let((vstring *)(&g_rawArgPntr[g_rawArgs]),seg(line, tokenStart,p));
        g_rawArgs++;
        mode = 2;
        tokenStart = p + 2;
        continue;
      }
      if (line[p] == '\"') {
        pntrLet(&g_rawArgPntr, pntrAddElement(g_rawArgPntr));
        nmbrLet(&g_rawArgNmbr, nmbrAddElement(g_rawArgNmbr, tokenStart));

        let((vstring *)(&g_rawArgPntr[g_rawArgs]),seg(line, tokenStart,p));
        g_rawArgs++;
        mode = 3;
        tokenStart = p + 2;
        continue;
      }

      continue;
    }
    if (mode == 2 || mode == 3) {

      if (line[p] == '\'' && mode == 2) {
        mode = 0;
        pntrLet(&g_rawArgPntr, pntrAddElement(g_rawArgPntr));
        nmbrLet(&g_rawArgNmbr, nmbrAddElement(g_rawArgNmbr, tokenStart));

        let((vstring *)(&g_rawArgPntr[g_rawArgs]), seg(line,tokenStart,p));
        g_rawArgs++;
        continue;
      }
      if (line[p] == '\"' && mode == 3) {
        mode = 0;
        pntrLet(&g_rawArgPntr, pntrAddElement(g_rawArgPntr));
        nmbrLet(&g_rawArgNmbr, nmbrAddElement(g_rawArgNmbr, tokenStart));

        let((vstring *)(&g_rawArgPntr[g_rawArgs]),seg(line, tokenStart,p));
        g_rawArgs++;
        continue;
      }

      continue;
    }
  }


  if (mode != 0) {
    pntrLet(&g_rawArgPntr, pntrAddElement(g_rawArgPntr));
    nmbrLet(&g_rawArgNmbr, nmbrAddElement(g_rawArgNmbr, tokenStart));

    let((vstring *)(&g_rawArgPntr[g_rawArgs]),seg(line,tokenStart,p));
    g_rawArgs++;
  }

  for (i = 0; i < g_rawArgs; i++) {
    g_rawArgNmbr[i] = g_rawArgNmbr[i] + len(g_commandPrompt);
  }


  let(&specialOneCharTokens, "");
}


flag lastArgMatches(vstring argString)
{

  if (!strcmp(argString, g_fullArg[pntrLen(g_fullArg)-1])) {
    return (1);
  } else {
    return (0);
  }
}

flag cmdMatches(vstring cmdString)
{
  long i, j, k;
  vstring tmpStr = "";

  k = len(cmdString);
  j = 0;
  for (i = 0; i < k; i++) {
    if (cmdString[i] == ' ') j++;
  }
  k = pntrLen(g_fullArg);
  for (i = 0; i <= j; i++) {
    if (j >= k) {

      let(&tmpStr, "");
      return (0);
    }
    let(&tmpStr, cat(tmpStr, " ", g_fullArg[i], NULL));
  }
  if (!strcmp(cat(" ", cmdString, NULL), tmpStr)) {
    let(&tmpStr, "");
    return (1);
  } else {
    let(&tmpStr, "");
    return (0);
  }
}


long switchPos(vstring swString)
{
  long i, j, k;
  vstring tmpStr = "";
  vstring swString1 = "";

  if (swString[0] != '/') bug(1108);


  if (swString[1] != ' ') {
    let(&swString1, cat("/ ", right(swString,2), " ", NULL));
  } else {
    let(&swString1,swString);
  }


  k = pntrLen(g_fullArg);
  for (i = 0; i < k; i++) {
    let(&tmpStr, cat(tmpStr,g_fullArg[i], " ", NULL));
  }

  k = instr(1,tmpStr,swString1);
  if (!k) {
    let(&swString1, "");
    let(&tmpStr, "");
    return (0);
  }

  let(&tmpStr,left(tmpStr,k));

  k = len(tmpStr);
  j = 0;
  for (i = 0; i < k; i++) {
    if (tmpStr[i] == ' ') j++;
  }
  let(&tmpStr, "");
  let(&swString1, "");
  return (j + 1);
}


void printCommandError(vstring line1, long arg, vstring errorMsg)
{
  vstring errorPointer = "";
  vstring line = "";
  long column, tokenLength, j;

  let(&line,line1);
  if (!line[0]) {

    print2("%s\n", errorMsg);
    let(&line, "");
    return;
  }
  column = g_rawArgNmbr[arg];
  tokenLength = len(g_rawArgPntr[arg]);
  for (j = 0; j < column - 1; j++) {
    if (j >= len(line)) bug(1109);
    if (line[j] == '\t') {
      let(&errorPointer, cat(errorPointer, "\t", NULL));
    } else {
      if (line[j] == '\n') {
        let(&errorPointer, "");
      } else {
        let(&errorPointer, cat(errorPointer, " ", NULL));
      }
    }
  }
  for (j = 0; j < tokenLength; j++)
    let(&errorPointer, cat(errorPointer, "^", NULL));
  print2("%s\n", errorPointer);
  printLongLine(errorMsg, "", " ");
  let(&errorPointer, "");
  let(&line, "");
}


void freeCommandLine() {
  long i, j;
  j = pntrLen(g_rawArgPntr);
  for (i = 0; i < j; i++) let((vstring *)(&g_rawArgPntr[i]), "");
  j = pntrLen(g_fullArg);
  for (i = 0; i < j; i++) let((vstring *)(&g_fullArg[i]), "");
  pntrLet(&g_fullArg, NULL_PNTRSTRING);
  pntrLet(&g_rawArgPntr, NULL_PNTRSTRING);
  nmbrLet(&g_rawArgNmbr, NULL_NMBRSTRING);
  let(&g_fullArgString, "");
}
}