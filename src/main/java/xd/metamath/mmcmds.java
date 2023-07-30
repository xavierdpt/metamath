package xd.metamath;

public class mmcmds {













vstring bigAdd(vstring bignum1, vstring bignum2);
vstring bigSub(vstring bignum1, vstring bignum2);


flag g_printHelp = 0;


vstring g_printStringForReferencedBy = "";


flag g_midiFlag = 0;
vstring g_midiParam = "";


void typeStatement(long showStmt,
  flag briefFlag,
  flag commentOnlyFlag,
  flag texFlag,
  flag htmlFlg)
{
  long i, j, k, m, n;
  vstring str1 = "", str2 = "", str3 = "";
  nmbrString *nmbrTmpPtr1;
  nmbrString *nmbrTmpPtr2;
  nmbrString *nmbrDDList = NULL_NMBRSTRING;
  flag q1, q2;
  flag type;
  flag subType;
  vstring htmlDistinctVars = "";
  char htmlDistinctVarsCommaFlag = 0;
  vstring str4 = "";
  vstring str5 = "";
  long distVarGrps = 0;


  long zapStatement1stToken;
  static long wffToken = -1;

  subType = 0;

  if (!showStmt) bug(225);

  if (!commentOnlyFlag && !briefFlag) {
    assignStmtFileAndLineNum(showStmt);
    let(&str1, cat("Statement ", str((double)showStmt),
        " is located on line ", str((double)(g_Statement[showStmt].lineNum)),
        " of the file ", NULL));
    if (!texFlag) {
      assignMathboxInfo();
      printLongLine(cat(str1,
        "\"", g_Statement[showStmt].fileName,
        "\".",

        (g_Statement[showStmt].pinkNumber == 0) ?
           "" :
           cat("  Its statement number for HTML pages is ",
               str((double)(g_Statement[showStmt].pinkNumber)), ".", NULL),

        ((getMathboxUser(showStmt))[0] == 0) ?
           "" :
           cat("  It is in the mathbox for ", getMathboxUser(showStmt), ".",
               NULL),
        NULL), "", " ");
    } else {
      if (!htmlFlg) let(&g_printString, "");
      g_outputToString = 1;

      if (!(htmlFlg && texFlag)) {
      } else {
         static final long SYNTAX =D.SYNTAX2;
         static final long DEFINITION =D.DEFINITION;
         static final long AXIOM =D.AXIOM;
         static final long THEOREM =D.THEOREM;
        if (g_Statement[showStmt].type == (char)p_) {
          subType = THEOREM;
        } else {

          if (g_Statement[showStmt].type != (char)a_) bug(228);
          if (strcmp("|-", g_MathToken[
              (g_Statement[showStmt].mathString)[0]].tokenName)) {
            subType = SYNTAX;
          } else {
            if (!strcmp("ax-", left(g_Statement[showStmt].labelName, 3))) {
              subType = AXIOM;
            } else {
              subType = DEFINITION;
            }
          }
        }
        switch (subType) {
          case SYNTAX:
            let(&str1, "Syntax Definition"); break;
          case DEFINITION:
            let(&str1, "Definition"); break;
          case AXIOM:
            let(&str1, "Axiom"); break;
          case THEOREM:
            let(&str1, "Theorem"); break;
          default:
            bug(229);
        }


        let(&str2, "");
        str2 = pinkHTML(showStmt);
        printLongLine(cat("<CENTER><B><FONT SIZE=\"+1\">", str1,
            " <FONT COLOR=", GREEN_TITLE_COLOR,
            ">", g_Statement[showStmt].labelName,
            "</FONT></FONT></B>", str2, "</CENTER>", NULL), "", "\"");
      }
      g_outputToString = 0;
    }
  }

  if (!briefFlag || commentOnlyFlag) {
    let(&str1, "");
    str1 = getDescription(showStmt);
    if (!str1[0]

#ifdef DATE_BELOW_PROOF

        || (str1[0] == '[' && str1[strlen(str1) - 1] == ']')

        || (strlen(str1) > 1 &&
            str1[1] == '[' && str1[strlen(str1) - 2] == ']')

#endif

        ) {
      print2("?Warning: Statement \"%s\" has no comment\n",
          g_Statement[showStmt].labelName);

      if (texFlag && !htmlFlg && !g_oldTexFlag) {
        let(&str1, "TO DO: PUT DESCRIPTION HERE");
      }
    }
    if (str1[0]) {
      if (!texFlag) {
        printLongLine(cat("\"", str1, "\"", NULL), "", " ");
      } else {

        if (!htmlFlg) {
          if (!g_oldTexFlag) {





            if (g_Statement[showStmt].type == a_) {
              if (!strcmp(left(g_Statement[showStmt].labelName, 3), "ax-")) {
                let(&str3, "axiom");
              } else {
                let(&str3, "definition");
              }
            } else {
              let(&str3, "theorem");
            }
            let(&str1, cat("\\begin{", str3, "}\\label{",
                left(str3, 3), ":",
                g_Statement[showStmt].labelName, "} ", str1, NULL));

          } else {

            let(&str1, cat("\n\\vspace{1ex} %2\n\n", str1, NULL));
          }
        }


        printTexComment(str1,
            1,
            PROCESS_EVERYTHING,
            0  );
      }
    }
  }
  if (commentOnlyFlag && !briefFlag) goto returnPoint;

  if ((briefFlag && !texFlag) ||
       (htmlFlg && texFlag) ) {





    if (htmlFlg && texFlag) {
      let(&htmlDistinctVars, "");
      htmlDistinctVarsCommaFlag = 0;
    }

    let(&str1, "");
    nmbrTmpPtr1 = g_Statement[showStmt].reqDisjVarsA;
    nmbrTmpPtr2 = g_Statement[showStmt].reqDisjVarsB;
    i = nmbrLen(nmbrTmpPtr1);
    if (i ) {
      nmbrLet(&nmbrDDList, NULL_NMBRSTRING);
      for (k = 0; k < i; k++) {

        if (!nmbrElementIn(1, nmbrDDList, nmbrTmpPtr1[k]) &&
            !nmbrElementIn(1, nmbrDDList, nmbrTmpPtr2[k])) {

          if (!(htmlFlg && texFlag)) {
            if (k == 0) let(&str1, "$d");
            else let(&str1, cat(str1, " $.  $d", NULL));
          } else {


            let(&htmlDistinctVars, cat(htmlDistinctVars, " &nbsp; ",
                NULL));
            htmlDistinctVarsCommaFlag = 0;
            distVarGrps++;

          }
          nmbrLet(&nmbrDDList, NULL_NMBRSTRING);
        }
        for (n = 0; n < nmbrLen(nmbrDDList); n++) {
          if (nmbrDDList[n] != nmbrTmpPtr1[k] &&
              nmbrDDList[n] != nmbrTmpPtr2[k]) {
            q1 = 0; q2 = 0;
            for (m = 0; m < i; m++) {
              if ((nmbrTmpPtr1[m] == nmbrDDList[n] &&
                      nmbrTmpPtr2[m] == nmbrTmpPtr1[k]) ||
                  (nmbrTmpPtr2[m] == nmbrDDList[n] &&
                      nmbrTmpPtr1[m] == nmbrTmpPtr1[k])) {
                q1 = 1;
              }
              if ((nmbrTmpPtr1[m] == nmbrDDList[n] &&
                      nmbrTmpPtr2[m] == nmbrTmpPtr2[k]) ||
                  (nmbrTmpPtr2[m] == nmbrDDList[n] &&
                      nmbrTmpPtr1[m] == nmbrTmpPtr2[k])) {
                q2 = 1;
              }
              if (q1 && q2) break;
            }
            if (!q1 || !q2) {
              if (!(htmlFlg && texFlag)) {
                if (k == 0) let(&str1, "$d");
                else let(&str1, cat(str1, " $.  $d", NULL));
              } else {


                let(&htmlDistinctVars, cat(htmlDistinctVars, " &nbsp; ",
                    NULL));
                htmlDistinctVarsCommaFlag = 0;
                distVarGrps++;

              }
              nmbrLet(&nmbrDDList, NULL_NMBRSTRING);
              break;
            }
          }
        }

        if (!nmbrElementIn(1, nmbrDDList, nmbrTmpPtr1[k])) {
          if (!(htmlFlg && texFlag)) {
            let(&str1, cat(str1, " ", g_MathToken[nmbrTmpPtr1[k]].tokenName,
                NULL));
          } else {


            if (htmlDistinctVarsCommaFlag) {
              let(&htmlDistinctVars, cat(htmlDistinctVars, ",", NULL));
            }
            htmlDistinctVarsCommaFlag = 1;
            let(&str2, "");
            str2 = tokenToTex(g_MathToken[nmbrTmpPtr1[k]].tokenName, showStmt);

            let(&htmlDistinctVars, cat(htmlDistinctVars, str2, NULL));

          }
          nmbrLet(&nmbrDDList, nmbrAddElement(nmbrDDList, nmbrTmpPtr1[k]));
        }
        if (!nmbrElementIn(1, nmbrDDList, nmbrTmpPtr2[k])) {
          if (!(htmlFlg && texFlag)) {
            let(&str1, cat(str1, " ", g_MathToken[nmbrTmpPtr2[k]].tokenName,
                NULL));
          } else {


            if (htmlDistinctVarsCommaFlag) {
              let(&htmlDistinctVars, cat(htmlDistinctVars, ",", NULL));
            }
            htmlDistinctVarsCommaFlag = 1;
            let(&str2, "");
            str2 = tokenToTex(g_MathToken[nmbrTmpPtr2[k]].tokenName, showStmt);

            let(&htmlDistinctVars, cat(htmlDistinctVars, str2, NULL));

          }
          nmbrLet(&nmbrDDList, nmbrAddElement(nmbrDDList, nmbrTmpPtr2[k]));
        }
      }

      if (!(htmlFlg && texFlag)) {
        let(&str1, cat(str1, " $.", NULL));
        printLongLine(str1, "  ", " ");
      } else {





      }
    }
  }

  if (briefFlag || texFlag ) {



    j = nmbrLen(g_Statement[showStmt].reqHypList);
    k = 0;
    for (i = 0; i < j; i++) {

      if (g_Statement[g_Statement[showStmt].reqHypList[i]].type
        == (char)e_) k++;


      if (subType == SYNTAX && (texFlag && htmlFlg)) {
        if (g_Statement[g_Statement[showStmt].reqHypList[i]].type
          == (char)f_) k++;
      }

    }
    if (k) {
      if (texFlag) {

        g_outputToString = 1;
      }
      if (texFlag && htmlFlg) {
        print2("<CENTER><TABLE BORDER CELLSPACING=0 BGCOLOR=%s\n",
            MINT_BACKGROUND_COLOR);

        print2("SUMMARY=\"%s\">\n", (k == 1) ? "Hypothesis" : "Hypotheses");
        print2("<CAPTION><B>%s</B></CAPTION>\n",
            (k == 1) ? "Hypothesis" : "Hypotheses");
        print2("<TR><TH>Ref\n");
        print2("</TH><TH>Expression</TH></TR>\n");
      }
      for (i = 0; i < j; i++) {
        k = g_Statement[showStmt].reqHypList[i];
        if (g_Statement[k].type != (char)e_


            && !(subType == SYNTAX && (texFlag && htmlFlg)
                && g_Statement[k].type == (char)f_)

            ) continue;

        if (!texFlag) {
          let(&str2, cat(str((double)k), " ", NULL));
        } else {
          let(&str2, "  ");
        }
        let(&str2, cat(str2, g_Statement[k].labelName,
            " $", chr(g_Statement[k].type), " ", NULL));
        if (!texFlag) {
          printLongLine(cat(str2,
              nmbrCvtMToVString(g_Statement[k].mathString), " $.", NULL),
              "      "," ");
        } else {
          if (!(htmlFlg && texFlag)) {
            if (!g_oldTexFlag) {

            } else {
              let(&str3, space((long)strlen(str2)));
              printTexLongMath(g_Statement[k].mathString,
                  str2, str3, 0, 0);
            }
          } else {
            g_outputToString = 1;
            print2("<TR ALIGN=LEFT><TD>%s</TD><TD>\n",
                g_Statement[k].labelName);

            printTexLongMath(g_Statement[k].mathString, "", "", 0, 0);
          }
        }
      }
      if (texFlag && htmlFlg) {
        g_outputToString = 1;
        print2("</TABLE></CENTER>\n");
      }
    }
  }

  let(&str1, "");
  type = g_Statement[showStmt].type;
  if (type == p_) let(&str1, " $= ...");
  if (!texFlag)
    let(&str2, cat(str((double)showStmt), " ", NULL));
  else
    let(&str2, "  ");
  let(&str2, cat(str2, g_Statement[showStmt].labelName,
      " $",chr(type), " ", NULL));
  if (!texFlag) {
    printLongLine(cat(str2,
        nmbrCvtMToVString(g_Statement[showStmt].mathString),
        str1, " $.", NULL), "      ", " ");
  } else {
    if (!(htmlFlg && texFlag)) {
      if (!g_oldTexFlag) {

        g_outputToString = 1;
        print2("\\begin{align}\n");
        let(&str3, "");

        str3 = getTexOrHtmlHypAndAssertion(showStmt);
        printLongLine(cat(str3,
              "\\label{eq:",
              g_Statement[showStmt].labelName,
              "}",




              "\\tag{",
              g_Statement[showStmt].labelName,
              "}",

              NULL), "    ", " ");

        print2("\\end{align}\n");





        if (g_Statement[showStmt].type == a_) {
          if (!strcmp(left(g_Statement[showStmt].labelName, 3), "ax-")) {
            let(&str3, "axiom");
          } else {
            let(&str3, "definition");
          }
        } else {
          let(&str3, "theorem");
        }
        print2("%s\n", cat("\\end{", str3, "}", NULL));

        fprintf(g_texFilePtr, "%s", g_printString);
        let(&g_printString, "");
        g_outputToString = 0;

      } else {
        let(&str3, space((long)strlen(str2)));
        printTexLongMath(g_Statement[showStmt].mathString,
            str2, str3, 0, 0);
      }
    } else {
      g_outputToString = 1;
      print2("<CENTER><TABLE BORDER CELLSPACING=0 BGCOLOR=%s\n",
          MINT_BACKGROUND_COLOR);

      print2("SUMMARY=\"Assertion\">\n");
      print2("<CAPTION><B>Assertion</B></CAPTION>\n");
      print2("<TR><TH>Ref\n");
      print2("</TH><TH>Expression</TH></TR>\n");
      printLongLine(cat(
       "<TR ALIGN=LEFT><TD><FONT COLOR=",
          GREEN_TITLE_COLOR, "><B>", g_Statement[showStmt].labelName,
          "</B></FONT></TD><TD>", NULL), "      ", " ");
      printTexLongMath(g_Statement[showStmt].mathString, "", "", 0, 0);
      g_outputToString = 1;
      print2("</TABLE></CENTER>\n");
    }
  }

  if (briefFlag) goto returnPoint;

  switch (type) {
    case a_:
    case p_:
      if (texFlag && htmlFlg) {
        g_outputToString = 1;
        print2("\n");
        g_outputToString = 0;
      }


      if (!texFlag) {
        print2("Its mandatory hypotheses in RPN order are:\n");
      }

      j = nmbrLen(g_Statement[showStmt].reqHypList);
      for (i = 0; i < j; i++) {
        k = g_Statement[showStmt].reqHypList[i];
        if (g_Statement[k].type != (char)e_ && (!htmlFlg && texFlag))
          continue;
        let(&str2, cat("  ",g_Statement[k].labelName,
            " $", chr(g_Statement[k].type), " ", NULL));
        if (!texFlag) {
          printLongLine(cat(str2,
              nmbrCvtMToVString(g_Statement[k].mathString), " $.", NULL),
              "      "," ");
        } else {
          if (!(htmlFlg && texFlag)) {



          }
        }
      }
      if (texFlag && htmlFlg) {
        g_outputToString = 1;
        print2("\n");
        g_outputToString = 0;
      }

      if (j == 0 && !texFlag) print2("  (None)\n");
      let(&str1, "");
      nmbrTmpPtr1 = g_Statement[showStmt].reqDisjVarsA;
      nmbrTmpPtr2 = g_Statement[showStmt].reqDisjVarsB;
      i = nmbrLen(nmbrTmpPtr1);
      if (i) {
        for (k = 0; k < i; k++) {
          if (!texFlag) {
            let(&str1, cat(str1, ", <",
                g_MathToken[nmbrTmpPtr1[k]].tokenName, ",",
                g_MathToken[nmbrTmpPtr2[k]].tokenName, ">", NULL));
          } else {
            if (htmlFlg && texFlag) {
              let(&str2, "");
              str2 = tokenToTex(g_MathToken[nmbrTmpPtr1[k]].tokenName, showStmt);

              let(&str1, cat(str1, " &nbsp; ", str2, NULL));
              let(&str2, "");
              str2 = tokenToTex(g_MathToken[nmbrTmpPtr2[k]].tokenName, showStmt);
              let(&str1, cat(str1, ",", str2, NULL));
            }
          }
        }
        if (!texFlag)
          printLongLine(cat(
              "Its mandatory disjoint variable pairs are:  ",
              right(str1,3),NULL),"  "," ");
      }
      if (type == p_ &&
          nmbrLen(g_Statement[showStmt].optHypList)
          && !texFlag) {
        printLongLine(cat(
           "Its optional hypotheses are:  ",
            nmbrCvtRToVString(
            g_Statement[showStmt].optHypList,

                0,
                0 ), NULL),
            "      "," ");
      }
      nmbrTmpPtr1 = g_Statement[showStmt].optDisjVarsA;
      nmbrTmpPtr2 = g_Statement[showStmt].optDisjVarsB;
      i = nmbrLen(nmbrTmpPtr1);
      if (i && type == p_) {
        if (!texFlag) {
          let(&str1, "");
        } else {
          if (htmlFlg && texFlag) {
          }
        }
        for (k = 0; k < i; k++) {
          if (!texFlag) {
            let(&str1, cat(str1, ", <",
                g_MathToken[nmbrTmpPtr1[k]].tokenName, ",",
                g_MathToken[nmbrTmpPtr2[k]].tokenName, ">", NULL));
          }
        }
        if (!texFlag) {
          printLongLine(cat(
              "Its optional disjoint variable pairs are:  ",
              right(str1,3),NULL),"  "," ");
        }
      }




      if (texFlag && htmlFlg) {

        if (htmlDistinctVars[0] != 0) {
          g_outputToString = 1;
          printLongLine(cat(
              "<CENTER>",
              "<A HREF=\"",



              (instr(1, g_htmlHome, "mmset.html") > 0) ?
                  "mmset.html" :
                  "../mpeuni/mmset.html",

              "#distinct\">Distinct variable</A> group",

              distVarGrps > 1 ? "s" : "",
              ": ",
              (g_altHtmlFlag ? cat("<SPAN ", g_htmlFont, ">", NULL) : ""),

              htmlDistinctVars,
              (g_altHtmlFlag ? "</SPAN>" : ""),
              "</CENTER>",
              NULL), "", "\"");
          g_outputToString = 0;
        }




        let(&str2, "");
        str2 = htmlAllowedSubst(showStmt);
        if (str2[0] != 0) {
          g_outputToString = 1;

          printLongLine(str2, "", "\"");
          g_outputToString = 0;
        }

      }

      if (texFlag) {
        g_outputToString = 1;
        if (htmlFlg && texFlag) print2("<HR NOSHADE SIZE=1>\n");
        g_outputToString = 0;
        break;
      }
      let(&str1, nmbrCvtMToVString(
          g_Statement[showStmt].reqVarList));
      if (!strlen(str1)) let(&str1, "(None)");
      printLongLine(cat(
          "The statement and its hypotheses require the variables:  ",
          str1, NULL), "      ", " ");
      if (type == p_ &&
          nmbrLen(g_Statement[showStmt].optVarList)) {
        printLongLine(cat(
            "These additional variables are allowed in its proof:  "
            ,nmbrCvtMToVString(
            g_Statement[showStmt].optVarList),NULL),"      ",
            " ");

      }
      let(&str1, nmbrCvtMToVString(
          g_Statement[showStmt].reqVarList));
      if (!strlen(str1)) let(&str1, "(None)");
      printLongLine(cat("The variables it contains are:  ",
          str1, NULL),
          "      ", " ");
      break;
    default:
      break;
  }
  if (texFlag) {
    g_outputToString = 0;
  }


  if (htmlFlg && texFlag) {

    if (subType == SYNTAX) {
      for (i = showStmt + 1; i <= g_statements; i++) {
        if (g_Statement[i].type == (char)a_) {
          if (!strcmp("|-", g_MathToken[
              (g_Statement[i].mathString)[0]].tokenName)) {

            j = 1;

            for (k = 1; k < g_Statement[showStmt].mathStringLen; k++) {
              if (g_MathToken[(g_Statement[showStmt].mathString)[k]].
                  tokenType == (char)con_) {
                if (!nmbrElementIn(1, g_Statement[i].mathString,
                    (g_Statement[showStmt].mathString)[k])) {
                  j = 0;
                  break;
                }
              }
            }
            if (j) {

              g_outputToString = 1;
              let(&str1, left(g_Statement[i].labelName, 3));
              let(&str2, "");
              str2 = pinkHTML(i);
              if (!strcmp(str1, "ax-")) {
                printLongLine(cat(
                    "<CENTER>This syntax is primitive.",
                    "  The first axiom using it is <A HREF=\"",
                    g_Statement[i].labelName, ".html\">",
                    g_Statement[i].labelName,
                    "</A>", str2, ".</CENTER><HR NOSHADE SIZE=1>",
                    NULL), "", "\"");
              } else {
                printLongLine(cat(
                    "<CENTER>See definition <A HREF=\"",
                    g_Statement[i].labelName, ".html\">",
                    g_Statement[i].labelName, "</A>", str2,
                    " for more information.</CENTER><HR NOSHADE SIZE=1>",
                    NULL), "", "\"");
              }



              printLongLine(cat(
                  "<CENTER><TABLE CELLSPACING=7><TR><TD ALIGN=LEFT><FONT SIZE=-1>",
                  "<B>Colors of variables:</B> ",
                  g_htmlVarColor, "</FONT></TD></TR>",
                  NULL), "", "\"");
              g_outputToString = 0;
              break;
            }
          }
        }
      }
    }


    if (subType == DEFINITION || subType == AXIOM) {


      if (wffToken == -1) {
        wffToken = -2;
        for (i = 0; i < g_mathTokens; i++) {
          if (!strcmp("wff", g_MathToken[i].tokenName)) {
            wffToken = i;
            break;
          }
        }
      }

      if (wffToken >= 0) {

        if (g_Statement[showStmt].type != (char)a_) bug(231);
        g_Statement[showStmt].type = (char)p_;
        zapStatement1stToken = (g_Statement[showStmt].mathString)[0];
        (g_Statement[showStmt].mathString)[0] = wffToken;
        if (strcmp("|-", g_MathToken[zapStatement1stToken].tokenName)) bug(230);

        nmbrTmpPtr1 = NULL_NMBRSTRING;
        nmbrLet(&nmbrTmpPtr1, g_Statement[showStmt].mathString);


        nmbrTmpPtr2 = proveFloating(nmbrTmpPtr1 ,
            showStmt , 0 ,
            0,
            0,

            2,
            1
            );

        if (nmbrLen(nmbrTmpPtr2)) {


          nmbrLet(&nmbrTmpPtr2, nmbrSquishProof(nmbrTmpPtr2));

          if (strcmp(g_Statement[showStmt].proofSectionPtr, "")) bug(231);
          if (g_Statement[showStmt].proofSectionLen != 0) bug(232);
          let(&str1, nmbrCvtRToVString(nmbrTmpPtr2,

                0,
                0 ));

          g_Statement[showStmt].proofSectionPtr = str1;
          g_Statement[showStmt].proofSectionLen = (long)strlen(str1) - 1;


          typeProof(showStmt,
              0 ,
              0 ,
              0 ,
              0 ,
              0 ,
              1 ,
              0 ,
              0 ,
              0 ,
              1 ,
              0 ,
              0 ,
              1 ,
              1 );


          g_Statement[showStmt].proofSectionPtr = "";
          g_Statement[showStmt].proofSectionLen = 0;


          let(&str1, "");
          nmbrLet(&nmbrTmpPtr2, NULL_NMBRSTRING);

        } else {


          if (g_outputToString != 0) bug(246);
          printLongLine(cat(
              "?Warning: Unable to generate syntax breakdown for \"",
              g_Statement[showStmt].labelName,
              "\".", NULL), "    ", " ");
        }



        g_Statement[showStmt].type = (char)a_;
        (g_Statement[showStmt].mathString)[0] = zapStatement1stToken;


        nmbrLet(&nmbrTmpPtr1, NULL_NMBRSTRING);

      }

    }


  }




  if (htmlFlg && texFlag) {
    fprintf(g_texFilePtr, "%s", g_printString);
    let(&g_printString, "");

    if (g_outputToString != 0) bug(242);
    g_outputToString = 1;
    if (subType != SYNTAX) {
      let(&str1, "");
      g_outputToString = 0;
      str1 = traceUsage(showStmt,
          0,
          0 );
      g_outputToString = 1;










      switch (subType) {
        case AXIOM:  let(&str3, "axiom"); break;
        case DEFINITION: let(&str3, "definition"); break;
        case THEOREM: let(&str3, "theorem"); break;
        default: bug(233);
      }

      let(&str2, cat("<TR><TD ALIGN=LEFT><FONT SIZE=-1><B>This ", str3,
          " is referenced by:</B>", NULL));

      if (str1[0] == 'Y') {




        let(&str5, "");

        for (m = showStmt + 1; m <= g_statements; m++) {

          if (str1[m] != 'Y') continue;

          let(&str3, g_Statement[m].labelName);

          if (g_Statement[m].type != p_) bug(241);

          let(&str4, "");
          str4 = pinkHTML(m);

          let(&str2, cat(str2, " &nbsp;<A HREF=\"",
              str3, ".html\">",

              str3, "</A>\n", str4, NULL));

          if (strlen(str2) > 5000) {
            let(&str5, cat(str5, str2, NULL));
            let(&str2, "");
          }

        }



      } else {

        let(&str5, "");
        let(&str2, cat(str2, " (None)", NULL));

      }


      let(&str2, cat(str5, str2, "</FONT></TD></TR>", NULL));

      if (g_printString[0]) {
        bug(256);
      }
      let(&g_printString, str2);
    }
    if (subType == THEOREM) {
      let(&g_printStringForReferencedBy, g_printString);
      let(&g_printString, "");
    }


    g_outputToString = 0;
  }



  if (htmlFlg && texFlag) {

    if (g_Statement[showStmt].type == (char)p_) {
      typeProof(showStmt,
          0 ,
          0 ,
          0 ,
          0 ,
          1 ,
          1 ,
          0 ,
          0 ,
          0 ,
          1 ,
          0 ,
          0 ,
          1 ,
          1 );
    }
  }



  if (g_printStringForReferencedBy[0]) bug(243);

 returnPoint:

  nmbrLet(&nmbrDDList, NULL_NMBRSTRING);
  let(&str1, "");
  let(&str2, "");
  let(&str3, "");
  let(&str4, "");
  let(&str5, "");
  let(&htmlDistinctVars, "");
}





vstring htmlDummyVars(long showStmt)
{
  nmbrString *optDVA;
  nmbrString *optDVB;
  long numDVs;
  nmbrString *optHyp;
  long numOptHyps;
  vstring str1 = "";
  long k, l, n, hypStmt;


  long dummyVarCount;
  vstring dummyVarUsed = "";
  vstring htmlDummyVarList = "";
  long dummyVar;



  if (!g_htmlFlag) bug(261);

  if (g_Statement[showStmt].type != p_) bug(262);
  if (strcmp("|-", g_MathToken[
            (g_Statement[showStmt].mathString)[0]].tokenName)) {

    goto RETURN_POINT;
  }

  optDVA = g_Statement[showStmt].optDisjVarsA;
  optDVB = g_Statement[showStmt].optDisjVarsB;
  numDVs = nmbrLen(optDVA);
  optHyp = g_Statement[showStmt].optHypList;
  numOptHyps = nmbrLen(optHyp);

  if (numDVs == 0) {

    goto RETURN_POINT;
  }

  dummyVarCount = 0;
  if (numDVs != 0) {

    parseProof(showStmt);

    let(&dummyVarUsed, string(g_mathTokens, 'N'));
    for (k = 0; k < numDVs; k++) {
      for (l = 1; l <= 2; l++) {
        if (l == 1) {
          dummyVar = optDVA[k];
        } else {
          dummyVar = optDVB[k];
        }

        if (dummyVarUsed[dummyVar] == 'N') {
          for (n = 0; n < numOptHyps; n++) {
            hypStmt = g_Statement[showStmt].optHypList[n];
            if (g_Statement[hypStmt].mathString[1] == dummyVar) {



              if (nmbrElementIn(1, g_WrkProof.proofString, hypStmt) == 0) {
                break;
              }

              dummyVarUsed[dummyVar] = 'Y';
              dummyVarCount++;

              let(&str1, "");

              str1 = tokenToTex(g_MathToken[dummyVar].tokenName,
                  showStmt);
              let(&htmlDummyVarList, cat(htmlDummyVarList, " ", str1, NULL));
              break;
            }
          }
        }
      }
    }
  }

  if (dummyVarCount > 0) {
    let(&htmlDummyVarList, cat(
        "<CENTER>",
         "<A HREF=\"",



         (instr(1, g_htmlHome, "mmset.html") > 0) ?
             "mmset.html" :
             "../mpeuni/mmset.html",

        "#dvnote1\">Dummy variable",

        dummyVarCount > 1 ? "s" : "",
        "</A> ",
        (g_altHtmlFlag ? cat("<SPAN ", g_htmlFont, ">", NULL) : ""),

        htmlDummyVarList,
        (g_altHtmlFlag ? "</SPAN>" : ""),
        dummyVarCount > 1 ? " are mutually distinct and"
            : " is",
        " distinct from all other variables.",
        "</CENTER>",
        NULL));
  }


 RETURN_POINT:

  let(&dummyVarUsed, "");
  let(&str1, "");

  return htmlDummyVarList;
}





vstring htmlAllowedSubst(long showStmt)
{
  nmbrString *reqHyp;
  long numReqHyps;
  nmbrString *reqDVA;
  nmbrString *reqDVB;
  long numDVs;
  nmbrString *setVar = NULL_NMBRSTRING;
  char *strptr;
  vstring str1 = "";
  long setVars;
  long wffOrClassVar;
  vstring setVarDVFlag = "";
  flag found, first;
  long i, j, k;
  vstring htmlAllowedList = "";
  long countInfo = 0;

  reqDVA = g_Statement[showStmt].reqDisjVarsA;
  reqDVB = g_Statement[showStmt].reqDisjVarsB;
  numDVs = nmbrLen(reqDVA);

  reqHyp = g_Statement[showStmt].reqHypList;
  numReqHyps = nmbrLen(reqHyp);



  if (!g_htmlFlag) bug(250);

  if (g_Statement[showStmt].mathStringLen < 1) bug(254);
  if (strcmp("|-", g_MathToken[
            (g_Statement[showStmt].mathString)[0]].tokenName)) {

    goto RETURN_POINT;
  }

  if (numDVs == 0) {

    goto RETURN_POINT;
  }



  setVars = 0;
  for (i = 0; i < numReqHyps; i++) {

    if (g_Statement[reqHyp[i]].type == (char)e_) continue;
    if (g_Statement[reqHyp[i]].type != (char)f_) bug(251);
    if (g_Statement[reqHyp[i]].mathStringLen != 2)
      bug(252);
    strptr = g_MathToken[
              (g_Statement[reqHyp[i]].mathString)[0]].tokenName;

    if (strcmp("setvar", strptr)) continue;

    setVars++;
  }

  j = 0;
  nmbrLet(&setVar, nmbrSpace(setVars));
  for (i = 0; i < numReqHyps; i++) {

    if (g_Statement[reqHyp[i]].type == (char)e_) continue;
    strptr = g_MathToken[
              (g_Statement[reqHyp[i]].mathString)[0]].tokenName;
    if (strcmp("setvar", strptr)) continue;

    setVar[j] = (g_Statement[reqHyp[i]].mathString)[1];
    j++;
  }
  if (j != setVars) bug(253);


  for (i = 0; i < numReqHyps; i++) {

    if (g_Statement[reqHyp[i]].type == (char)e_) continue;
    strptr = g_MathToken[
              (g_Statement[reqHyp[i]].mathString)[0]].tokenName;
    if (strcmp("wff", strptr) && strcmp("class", strptr)) continue;

    wffOrClassVar = (g_Statement[reqHyp[i]].mathString)[1];
    let(&setVarDVFlag, string(setVars, 'N'));

    for (j = 0; j < numDVs; j++) {
      found = 0;
      if (wffOrClassVar == reqDVA[j]) {
        for (k = 0; k < setVars; k++) {
          if (setVar[k] == reqDVB[j]) {
            setVarDVFlag[k] = 'Y';
            found = 1;
            break;
          }
        }
      }
      if (found) continue;

      if (wffOrClassVar == reqDVB[j]) {
        for (k = 0; k < setVars; k++) {
          if (setVar[k] == reqDVA[j]) {
            setVarDVFlag[k] = 'Y';
            break;
          }
        }
      }
    }



    found = 0;
    for (j = 0; j < setVars; j++) {
      if (setVarDVFlag[j] == 'N') {
        found = 1;
        break;
      }
    }
    if (found == 0) continue;

    let(&str1, "");
    str1 = tokenToTex(g_MathToken[wffOrClassVar].tokenName, showStmt);

    countInfo++;
    let(&htmlAllowedList, cat(htmlAllowedList, " &nbsp; ",
        str1, "(", NULL));
    first = 1;
    for (j = 0; j < setVars; j++) {
      if (setVarDVFlag[j] == 'N') {
        let(&str1, "");
        str1 = tokenToTex(g_MathToken[setVar[j]].tokenName, showStmt);
        let(&htmlAllowedList, cat(htmlAllowedList,
            (first == 0) ? "," : "", str1, NULL));
        if (first == 0) countInfo++;
        first = 0;
      }
    }
    let(&htmlAllowedList, cat(htmlAllowedList, ")", NULL));

  }

 RETURN_POINT:

  if (htmlAllowedList[0] != 0) {
    let(&htmlAllowedList, cat("<CENTER>",
        "<A HREF=\"",



        (instr(1, g_htmlHome, "mmset.html") > 0) ?
            "mmset.html" :
            "../mpeuni/mmset.html",

        "#allowedsubst\">Allowed substitution</A> hint",
        ((countInfo != 1) ? "s" : ""), ": ",
        (g_altHtmlFlag ? cat("<SPAN ", g_htmlFont, ">", NULL) : ""),

        htmlAllowedList,
        (g_altHtmlFlag ? "</SPAN>" : ""),
        "</CENTER>", NULL));
  }


  nmbrLet(&setVar, NULL_NMBRSTRING);
  let(&str1, "");
  let(&setVarDVFlag, "");

  return htmlAllowedList;
}




void typeProof(long statemNum,
  flag pipFlag,
  long startStep, long endStep,
  long endIndent,
  flag essentialFlag,
  flag renumberFlag,
  flag unknownFlag,
  flag notUnifiedFlag,
  flag reverseFlag,
  flag noIndentFlag,
  long splitColumn,
  flag skipRepeatedSteps,
  flag texFlag,
  flag htmlFlg

  )
{
  long i, j, plen, step, stmt, lens, lent, maxStepNum;
  vstring tmpStr = "";
  vstring tmpStr1 = "";
  vstring locLabDecl = "";
  vstring tgtLabel = "";
  vstring srcLabel = "";
  vstring startPrefix = "";
  vstring tgtPrefix = "";
  vstring srcPrefix = "";
  vstring userPrefix = "";
  vstring contPrefix = "";
  vstring statementUsedFlags = "";
  vstring startStringWithNum = "";
  vstring startStringWithoutNum = "";
  nmbrString *proof = NULL_NMBRSTRING;
  nmbrString *localLabels = NULL_NMBRSTRING;
  nmbrString *localLabelNames = NULL_NMBRSTRING;
  nmbrString *indentationLevel = NULL_NMBRSTRING;
  nmbrString *targetHyps = NULL_NMBRSTRING;
  nmbrString *essentialFlags = NULL_NMBRSTRING;
  nmbrString *stepRenumber = NULL_NMBRSTRING;
  nmbrString *notUnifiedFlags = NULL_NMBRSTRING;
  nmbrString *unprovedList = NULL_NMBRSTRING;
  nmbrString *relativeStepNums = NULL_NMBRSTRING;
  long maxLabelLen = 0;
  long maxStepNumLen = 1;
  long maxStepNumOffsetLen = 0;
  char type;
  flag stepPrintFlag;
  long fromStep, toStep, byStep;
  vstring hypStr = "";
  nmbrString *hypPtr;
  long hyp, hypStep;

  static long wffToken = -1;
  nmbrString *nmbrTmpPtr1;
  nmbrString *nmbrTmpPtr2;



  if (htmlFlg && texFlag) skipRepeatedSteps = 1;



  if (htmlFlg && texFlag) {




    g_outputToString = 1;
    if (essentialFlag) {


      let(&tmpStr, "");
      tmpStr = htmlDummyVars(statemNum);
      if (tmpStr[0] != 0) {
        print2("<CENTER><B>Proof of Theorem <FONT\n");
        printLongLine(cat("   COLOR=", GREEN_TITLE_COLOR, ">",
            asciiToTt(g_Statement[statemNum].labelName),
            "</FONT></B></CENTER>", NULL), "", "\"");

        printLongLine(tmpStr, "", "\"");
        let(&tmpStr, "");
        print2("<CENTER><TABLE BORDER CELLSPACING=0 BGCOLOR=%s\n",
            MINT_BACKGROUND_COLOR);
        print2("SUMMARY=\"Proof of theorem\">\n");
      } else {



        print2("<CENTER><TABLE BORDER CELLSPACING=0 BGCOLOR=%s\n",
            MINT_BACKGROUND_COLOR);
        print2("SUMMARY=\"Proof of theorem\">\n");
        print2("<CAPTION><B>Proof of Theorem <FONT\n");
        printLongLine(cat("   COLOR=", GREEN_TITLE_COLOR, ">",
            asciiToTt(g_Statement[statemNum].labelName),
            "</FONT></B></CAPTION>", NULL), "", "\"");
      }
    } else {
      print2("<CENTER><TABLE BORDER CELLSPACING=0 BGCOLOR=%s\n",
          MINT_BACKGROUND_COLOR);
      if (!strcmp("ax-", left(g_Statement[g_showStatement].labelName, 3))) {

        print2("SUMMARY=\"Detailed syntax breakdown of axiom\">\n");
        print2("<CAPTION><B>Detailed syntax breakdown of Axiom <FONT\n");
      } else {

        print2("SUMMARY=\"Detailed syntax breakdown of definition\">\n");
        print2("<CAPTION><B>Detailed syntax breakdown of Definition <FONT\n");
      }
      printLongLine(cat("   COLOR=", GREEN_TITLE_COLOR, ">",
          asciiToTt(g_Statement[statemNum].labelName),
          "</FONT></B></CAPTION>", NULL), "", "\"");
    }
    print2(
        "<TR><TH>Step</TH><TH>Hyp</TH><TH>Ref\n");
    print2("</TH><TH>Expression</TH></TR>\n");
    g_outputToString = 0;
  }

  if (!pipFlag) {
    parseProof(g_showStatement);
    if (g_WrkProof.errorSeverity > 1) {
      if (htmlFlg && texFlag) {

        g_outputToString = 1;
        print2(
      "<TD COLSPAN=4><B><FONT COLOR=RED>WARNING: Proof has a severe error.\n");
        print2("</FONT></B></TD></TR>\n");
        g_outputToString = 0;


        let(&g_printStringForReferencedBy, "");
      }
      return;
    }
    verifyProof(g_showStatement);
  }

  if (!pipFlag) {
    nmbrLet(&proof, g_WrkProof.proofString);
    if (g_midiFlag) {

      nmbrLet(&proof, nmbrUnsquishProof(proof));
    }
  } else {
    nmbrLet(&proof, g_ProofInProgress.proof);
  }
  plen = nmbrLen(proof);

  if (htmlFlg && texFlag && !noIndentFlag ) {

    bug(218);
  }


  if (skipRepeatedSteps) {
    for (step = 0; step < plen; step++) {
      stmt = proof[step];
      if (stmt < 0) continue;
      type = g_Statement[stmt].type;
      if (type == f_ || type == e_
          || g_Statement[stmt].numReqHyp == 0) {
        for (i = 0; i < step; i++) {
          if (stmt == proof[i]) {
            proof[step] = -1000 - i;
            break;
          }
        }
      }
    }
  }



  for (step = 0; step < plen; step++) {
    stmt = proof[step];
    if (stmt <= -1000) {
      stmt = -1000 - stmt;
      if (!nmbrElementIn(1, localLabels, stmt)) {
        nmbrLet(&localLabels, nmbrAddElement(localLabels, stmt));
      }
    }
  }

  nmbrLet(&localLabelNames, nmbrSpace(plen));


  nmbrLet(&indentationLevel, nmbrGetIndentation(proof, 0));


  nmbrLet(&targetHyps, nmbrGetTargetHyp(proof, statemNum));


  if (essentialFlag || g_midiFlag) {
    nmbrLet(&essentialFlags, nmbrGetEssential(proof));
  } else {
    nmbrLet(&essentialFlags, NULL_NMBRSTRING);
  }

  if (g_midiFlag) {
    outputMidi(plen, indentationLevel,
        essentialFlags, g_midiParam, g_Statement[statemNum].labelName);
    goto typeProof_return;
  }


  nmbrLet(&stepRenumber, nmbrSpace(plen));
  i = 0;
  maxStepNum = 0;
  for (step = 0; step < plen; step++) {
    stepPrintFlag = 1;
    if (renumberFlag && essentialFlag) {
      if (!essentialFlags[step]) stepPrintFlag = 0;
    }


    if (skipRepeatedSteps && proof[step] < 0) stepPrintFlag = 0;

    if (stepPrintFlag) {
      i++;
      stepRenumber[step] = i;
      maxStepNum = i;
    }
  }



  if (unknownFlag) {
    relativeStepNums = getRelStepNums(g_ProofInProgress.proof);
  }


  if (notUnifiedFlag) {
    if (!pipFlag) bug(205);
    nmbrLet(&notUnifiedFlags, nmbrSpace(plen));
    for (step = 0; step < plen; step++) {
      notUnifiedFlags[step] = 0;
      if (nmbrLen(g_ProofInProgress.source[step])) {
        if (!nmbrEq(g_ProofInProgress.target[step],
            g_ProofInProgress.source[step])) notUnifiedFlags[step] = 1;
      }
      if (nmbrLen(g_ProofInProgress.user[step])) {
        if (!nmbrEq(g_ProofInProgress.target[step],
            g_ProofInProgress.user[step])) notUnifiedFlags[step] = 1;
      }
    }
  }


  i = maxStepNum;
  while (i >= 10) {
    i = i/10;
    maxStepNumLen++;
  }


  if (unknownFlag) {
    maxStepNumOffsetLen = 3;
    j = 0;
    for (i = 0; i < plen; i++) {
      j = relativeStepNums[i];
      if (j <= 0) break;
    }
    while (j <= -10) {
      j = j/10;
      maxStepNumOffsetLen++;
    }
  }





  for (step = 0; step < plen; step++) {
    lent = (long)strlen(g_Statement[targetHyps[step]].labelName);
    stmt = proof[step];
    if (stmt < 0) {
      if (stmt <= -1000) {
        stmt = -1000 - stmt;

        lens = (long)strlen(str((double)(localLabelNames[stmt])));
        let(&tmpStr1, "");
      } else {
        if (stmt != -(long)'?') bug (219);
        lens = 1;
      }
    } else {
      if (nmbrElementIn(1, localLabels, step)) {

        localLabelNames[step] = stepRenumber[step];

      }
      lens = (long)strlen(g_Statement[stmt].labelName);
    }

    if (maxLabelLen < lent + 1 + lens) {
      maxLabelLen = lent + 1 + lens;
    }
  }


  if (reverseFlag
      && !g_midiFlag
      ) {
    fromStep = plen - 1;
    toStep = -1;
    byStep = -1;
  } else {
    fromStep = 0;
    toStep = plen;
    byStep = 1;
  }
  for (step = fromStep; step != toStep; step = step + byStep) {


    stepPrintFlag = 1;
    if (startStep > 0) {
      if (step + 1 < startStep) stepPrintFlag = 0;
    }
    if (endStep > 0) {
      if (step + 1 > endStep) stepPrintFlag = 0;
    }
    if (endIndent > 0) {
      if (indentationLevel[step] + 1 > endIndent) stepPrintFlag = 0;
    }
    if (essentialFlag) {
      if (!essentialFlags[step]) stepPrintFlag = 0;
    }
    if (notUnifiedFlag) {
      if (!notUnifiedFlags[step]) stepPrintFlag = 0;
    }
    if (unknownFlag) {
      if (proof[step] != -(long)'?') stepPrintFlag = 0;
    }




    if (skipRepeatedSteps) {
      if (stepRenumber[step] == 0) stepPrintFlag = 0;
    }


    if (g_midiFlag) stepPrintFlag = 1;

    if (!stepPrintFlag) continue;

    if (noIndentFlag) {
      let(&tgtLabel, "");
    } else {
      let(&tgtLabel, g_Statement[targetHyps[step]].labelName);
    }
    let(&locLabDecl, "");
    stmt = proof[step];
    if (stmt < 0) {
      if (stmt <= -1000) {
        stmt = -1000 - stmt;


        if (skipRepeatedSteps) bug(220);

        if (noIndentFlag) {
          let(&srcLabel, cat("@", str((double)(localLabelNames[stmt])), NULL));
        } else {
          let(&srcLabel, cat("=", str((double)(localLabelNames[stmt])), NULL));
        }
        type = g_Statement[proof[stmt]].type;
      } else {
        if (stmt != -(long)'?') bug(206);
        if (noIndentFlag) {
          let(&srcLabel, chr(-stmt));
        } else {
          let(&srcLabel, cat("=", chr(-stmt), NULL));
        }
        type = '?';
      }
    } else {
      if (nmbrElementIn(1, localLabels, step)) {

        if (noIndentFlag) {


          if (!(skipRepeatedSteps)) {
            let(&locLabDecl, cat("@", str((double)(localLabelNames[step])), ":", NULL));
          }
        } else {
          let(&locLabDecl, cat(str((double)(localLabelNames[step])), ":", NULL));
        }
      }

      if (noIndentFlag) {
        let(&srcLabel, g_Statement[stmt].labelName);


        let(&hypStr, "");
        hypStep = step - 1;
        hypPtr = g_Statement[stmt].reqHypList;
        for (hyp = g_Statement[stmt].numReqHyp - 1; hyp >=0; hyp--) {
          if (!essentialFlag || g_Statement[hypPtr[hyp]].type == (char)e_) {
            i = stepRenumber[hypStep];
            if (i == 0) {


              if (!(skipRepeatedSteps)) bug(221);
              if (proof[hypStep] != -(long)'?') {
                if (proof[hypStep] > -1000) bug(222);
                if (localLabelNames[-1000 - proof[hypStep]] == 0) bug(223);
                if (localLabelNames[-1000 - proof[hypStep]] !=
                    stepRenumber[-1000 - proof[hypStep]]) bug(224);

                i = stepRenumber[-1000 - proof[hypStep]];
              } else {

                i = -(long)'?';
              }
            }
            if (!hypStr[0]) {
              if (i != -(long)'?') {
                let(&hypStr, str((double)i));
              } else {
                let(&hypStr, "?");
              }
            } else {

              if (i != -(long)'?') {
                let(&hypStr, cat(str((double)i), ",", hypStr, NULL));
              } else {
                let(&hypStr, cat("?", ",", hypStr, NULL));
              }
            }
          }
          if (hyp < g_Statement[stmt].numReqHyp) {

            hypStep = hypStep - subproofLen(proof, hypStep);
          }
        }

        if (hypStr[0]) {

          let(&srcLabel, cat(hypStr, " ", srcLabel, NULL));
        }

      } else {
        let(&srcLabel, cat("=", g_Statement[stmt].labelName, NULL));
      }
      type = g_Statement[stmt].type;
    }


     static final long PF_INDENT_INC =D.PF_INDENT_INC;

    if (stepPrintFlag) {

      if (noIndentFlag) {
        let(&startPrefix, cat(
            space(maxStepNumLen - (long)strlen(str((double)(stepRenumber[step])))),
            str((double)(stepRenumber[step])),
            " ",
            srcLabel,
            space(splitColumn - (long)strlen(srcLabel) - (long)strlen(locLabDecl) - 1
                - maxStepNumLen - 1),
            " ", locLabDecl,
            NULL));
        if (pipFlag) {
          let(&tgtPrefix, startPrefix);
          let(&srcPrefix, cat(
              space(maxStepNumLen - (long)strlen(str((double)(stepRenumber[step])))),
              space((long)strlen(str((double)(stepRenumber[step])))),
              " ",
              space(splitColumn - 1
                  - maxStepNumLen),
              NULL));
          let(&userPrefix, cat(
              space(maxStepNumLen - (long)strlen(str((double)(stepRenumber[step])))),
              space((long)strlen(str((double)(stepRenumber[step])))),
              " ",
              "(User)",
              space(splitColumn - (long)strlen("(User)") - 1
                  - maxStepNumLen),
              NULL));
        }
        let(&contPrefix, space((long)strlen(startPrefix) + 4));
      } else {


        let(&tmpStr, "");
        if (unknownFlag) {
          if (relativeStepNums[step] < 0) {
            let(&tmpStr, cat(" ", str((double)(relativeStepNums[step])), NULL));
          }
          let(&tmpStr, cat(tmpStr, space(maxStepNumOffsetLen
              - (long)(strlen(tmpStr))), NULL));
        }

        let(&startStringWithNum, cat(
            space(maxStepNumLen - (long)strlen(str((double)(stepRenumber[step])))),
            str((double)(stepRenumber[step])),
            tmpStr,
            " ", NULL));
        let(&startStringWithoutNum, space(maxStepNumLen + 1));


        let(&startPrefix, cat(
            startStringWithNum,
            space(indentationLevel[step] * PF_INDENT_INC
                - (long)strlen(locLabDecl)),
            locLabDecl,
            tgtLabel,
            srcLabel,
            space(maxLabelLen - (long)strlen(tgtLabel)
                - (long)strlen(srcLabel)),
            NULL));
        if (pipFlag) {
          let(&tgtPrefix, cat(
              startStringWithNum,
              space(indentationLevel[step] * PF_INDENT_INC - (long)strlen(locLabDecl)),
              locLabDecl,
              tgtLabel,
              space((long)strlen(srcLabel)),
              space(maxLabelLen - (long)strlen(tgtLabel) - (long)strlen(srcLabel)),
              NULL));
          let(&srcPrefix, cat(
              startStringWithoutNum,
              space(indentationLevel[step] * PF_INDENT_INC - (long)strlen(locLabDecl)),
              space((long)strlen(locLabDecl)),
              space((long)strlen(tgtLabel)),
              srcLabel,
              space(maxLabelLen - (long)strlen(tgtLabel) - (long)strlen(srcLabel)),
              NULL));
          let(&userPrefix, cat(
              startStringWithoutNum,
              space(indentationLevel[step] * PF_INDENT_INC - (long)strlen(locLabDecl)),
              space((long)strlen(locLabDecl)),
              space((long)strlen(tgtLabel)),
              "=(User)",
              space(maxLabelLen - (long)strlen(tgtLabel) - (long)strlen("=(User)")),
              NULL));
        }
        let(&contPrefix, "");
      }

      if (!pipFlag) {

        if (!texFlag) {
          if (!g_midiFlag) {
            printLongLine(cat(startPrefix," $", chr(type), " ",
                nmbrCvtMToVString(g_WrkProof.mathStringPtrs[step]),
                NULL),
                contPrefix,
                chr(1));

          }
        } else {
          printTexLongMath(g_WrkProof.mathStringPtrs[step],
              cat(startPrefix, " $", chr(type), " ", NULL),
              contPrefix, stmt, indentationLevel[step]);
        }

      } else {
        if (texFlag) {
          print2("?Unsupported:  HTML or LaTeX proof for NEW_PROOF.\n");
          bug(244);
        }

        if (!nmbrEq(g_ProofInProgress.target[step], g_ProofInProgress.source[step])
            && nmbrLen(g_ProofInProgress.source[step])) {

          if (!texFlag) {
            printLongLine(cat(tgtPrefix, " $", chr(type), " ",
                nmbrCvtMToVString(g_ProofInProgress.target[step]),
                NULL),
                contPrefix,
                chr(1));
            printLongLine(cat(srcPrefix,"  = ",
                nmbrCvtMToVString(g_ProofInProgress.source[step]),
                NULL),
                contPrefix,
                chr(1));
          } else {
            printTexLongMath(g_ProofInProgress.target[step],
                cat(tgtPrefix, " $", chr(type), " ", NULL),
                contPrefix, 0, 0);
            printTexLongMath(g_ProofInProgress.source[step],
                cat(srcPrefix, "  = ", NULL),
                contPrefix, 0, 0);
          }
        } else {
          if (!texFlag) {
            printLongLine(cat(startPrefix, " $", chr(type), " ",
                nmbrCvtMToVString(g_ProofInProgress.target[step]),
                NULL),
                contPrefix,
                chr(1));
          } else {
            printTexLongMath(g_ProofInProgress.target[step],
                cat(startPrefix, " $", chr(type), " ", NULL),
                contPrefix, 0, 0);
          }

        }
        if (nmbrLen(g_ProofInProgress.user[step])) {

          if (!texFlag) {
            printLongLine(cat(userPrefix, "  = ",
                nmbrCvtMToVString(g_ProofInProgress.user[step]),
                NULL),
                contPrefix,
                chr(1));
          } else {
            printTexLongMath(g_ProofInProgress.user[step],
                cat(userPrefix, "  = ", NULL),
                contPrefix, 0, 0);
          }

        }
      }
    }


  }

  if (!pipFlag) {
    cleanWrkProof();
  }

  if (htmlFlg && texFlag) {
    g_outputToString = 1;
    print2("</TABLE></CENTER>\n");



    printLongLine(cat(
        "<CENTER><TABLE CELLSPACING=5><TR><TD ALIGN=LEFT><FONT SIZE=-1>",
        "<B>Colors of variables:</B> ",
        g_htmlVarColor, "</FONT></TD></TR>",
        NULL), "", "\"");

    if (essentialFlag) {


      let(&statementUsedFlags, string(g_statements + 1, 'N'));
      for (step = 0; step < plen; step++) {
        stmt = proof[step];

        if (stmt > 0) {
          if (g_Statement[stmt].type == a_) {
            if (strcmp("|-", g_MathToken[
                (g_Statement[stmt].mathString)[0]].tokenName)) {
              statementUsedFlags[stmt] = 'Y';
            }
          }
        }
      }




      if (wffToken == -1) {
        wffToken = -2;
        for (i = 0; i < g_mathTokens; i++) {
          if (!strcmp("wff", g_MathToken[i].tokenName)) {
            wffToken = i;
            break;
          }
        }
      }

      if (wffToken >= 0) {

        for (i = -1; i < g_Statement[statemNum].numReqHyp; i++) {

          if (i == -1) {

            if (g_Statement[statemNum].type != (char)p_) bug(245);
            nmbrTmpPtr1 = NULL_NMBRSTRING;
            nmbrLet(&nmbrTmpPtr1, g_Statement[statemNum].mathString);
          } else {

            if (g_Statement[g_Statement[statemNum].reqHypList[i]].type
                == (char)f_) continue;

            if (g_Statement[g_Statement[statemNum].reqHypList[i]].type
                != (char)e_) bug(234);
            nmbrTmpPtr1 = NULL_NMBRSTRING;
            nmbrLet(&nmbrTmpPtr1,
                g_Statement[g_Statement[statemNum].reqHypList[i]].mathString);
          }
          if (strcmp("|-", g_MathToken[nmbrTmpPtr1[0]].tokenName)) {
            nmbrTmpPtr2 = NULL_NMBRSTRING;
            break;

          }

          nmbrTmpPtr1[0] = wffToken;


          nmbrTmpPtr2 = proveFloating(nmbrTmpPtr1 ,
              statemNum , 0 ,
              0,
              0,

              2,
              1
              );
          if (!nmbrLen(nmbrTmpPtr2)) {
            break;

          }


          for (step = 0; step < nmbrLen(nmbrTmpPtr2); step++) {
            stmt = nmbrTmpPtr2[step];

            if (stmt > 0) {
              if (statementUsedFlags[stmt] == 'N') {
                if (g_Statement[stmt].type == a_) {
                  if (strcmp("|-", g_MathToken[
                      (g_Statement[stmt].mathString)[0]].tokenName)) {
                    statementUsedFlags[stmt] = 'Y';
                  } else {

                    bug(237);
                  }
                }
              }
            } else {

              bug(238);
            }
          }


          nmbrLet(&nmbrTmpPtr2, NULL_NMBRSTRING);
          nmbrLet(&nmbrTmpPtr1, NULL_NMBRSTRING);
        }

        nmbrLet(&nmbrTmpPtr2, NULL_NMBRSTRING);
        nmbrLet(&nmbrTmpPtr1, NULL_NMBRSTRING);
      }



      let(&tmpStr, "");
      for (stmt = 1; stmt <= g_statements; stmt++) {
        if (statementUsedFlags[stmt] == 'Y') {
          if (!tmpStr[0]) {
            let(&tmpStr,


               "<TR><TD ALIGN=LEFT><FONT SIZE=-1><B>Syntax hints:</B> ");
          }


          let(&tmpStr1, "");
          for (i = 1 ; i < g_Statement[stmt].mathStringLen; i++) {
            if (g_MathToken[(g_Statement[stmt].mathString)[i]].tokenType ==
                (char)con_) {

              if (strcmp(g_MathToken[(g_Statement[stmt].mathString)[i]
                      ].tokenName, "(")
                  && strcmp(g_MathToken[(g_Statement[stmt].mathString)[i]
                      ].tokenName, ",")
                  && strcmp(g_MathToken[(g_Statement[stmt].mathString)[i]
                      ].tokenName, ")")
                  && strcmp(g_MathToken[(g_Statement[stmt].mathString)[i]
                      ].tokenName, ":")

                  && !(!strcmp(g_MathToken[(g_Statement[stmt].mathString)[i]
                      ].tokenName, "e.")
                      && (!strcmp(g_Statement[stmt].labelName, "cmpt")
                          || !strcmp(g_Statement[stmt].labelName, "cmpt2")))
                  ) {
                tmpStr1 =
                    tokenToTex(g_MathToken[(g_Statement[stmt].mathString)[i]
                    ].tokenName, stmt);

                let(&tmpStr1, cat(
                    (g_altHtmlFlag ? cat("<SPAN ", g_htmlFont, ">", NULL) : ""),

                    tmpStr1,
                    (g_altHtmlFlag ? "</SPAN>" : ""),
                    NULL));
                break;
              }
            }
          }

          if (!strcmp(g_Statement[stmt].labelName, "wbr"))
            let(&tmpStr1, "<i> class class class </i>");
          if (!strcmp(g_Statement[stmt].labelName, "cv"))
            let(&tmpStr1, "[set variable]");

          if (!strcmp(g_Statement[stmt].labelName, "cv"))
            continue;
          if (!strcmp(g_Statement[stmt].labelName, "co"))
            let(&tmpStr1, "(<i>class class class</i>)");
          let(&tmpStr, cat(tmpStr, " &nbsp;", tmpStr1, NULL));


          let(&tmpStr1, "");
          tmpStr1 = pinkHTML(stmt);
          let(&tmpStr, cat(tmpStr, "<A HREF=\"",
              g_Statement[stmt].labelName, ".html\">",
              g_Statement[stmt].labelName, "</A>", tmpStr1, NULL));


        }
      }
      if (tmpStr[0]) {
        let(&tmpStr, cat(tmpStr,
            "</FONT></TD></TR>", NULL));
        printLongLine(tmpStr, "", "\"");
      }








      let(&statementUsedFlags, "");
      traceProofWork(statemNum,
          1,
          "",
          &statementUsedFlags,
          &unprovedList );
      if ((signed)(strlen(statementUsedFlags)) != g_statements + 1) bug(227);


      let(&tmpStr, "");
      for (stmt = 1; stmt <= g_statements; stmt++) {
        if (statementUsedFlags[stmt] == 'Y' && g_Statement[stmt].type == a_) {
          let(&tmpStr1, left(g_Statement[stmt].labelName, 3));
          if (!strcmp(tmpStr1, "ax-")) {
            if (!tmpStr[0]) {
              let(&tmpStr,
 "<TR><TD ALIGN=LEFT><FONT SIZE=-1><B>This theorem was proved from axioms:</B>");
            }
            let(&tmpStr1, "");
            tmpStr1 = pinkHTML(stmt);
            let(&tmpStr, cat(tmpStr, " &nbsp;<A HREF=\"",
                g_Statement[stmt].labelName, ".html\">",
                g_Statement[stmt].labelName, "</A>", tmpStr1, NULL));
          }
        }
      }
      if (tmpStr[0]) {
        let(&tmpStr, cat(tmpStr, "</FONT></TD></TR>", NULL));
        printLongLine(tmpStr, "", "\"");
      }


      let(&tmpStr, "");
      for (stmt = 1; stmt <= g_statements; stmt++) {
        if (statementUsedFlags[stmt] == 'Y' && g_Statement[stmt].type == a_) {
          let(&tmpStr1, left(g_Statement[stmt].labelName, 3));
          if (!strcmp(tmpStr1, "df-")) {
            if (!tmpStr[0]) {
              let(&tmpStr,
 "<TR><TD ALIGN=LEFT><FONT SIZE=-1><B>This theorem depends on definitions:</B>");
            }
            let(&tmpStr1, "");
            tmpStr1 = pinkHTML(stmt);
            let(&tmpStr, cat(tmpStr, " &nbsp;<A HREF=\"",
                g_Statement[stmt].labelName, ".html\">",
                g_Statement[stmt].labelName, "</A>", tmpStr1, NULL));
          }
        }
      }
      if (tmpStr[0]) {
        let(&tmpStr, cat(tmpStr, "</FONT></TD></TR>", NULL));
        printLongLine(tmpStr, "", "\"");
      }


      if (nmbrLen(unprovedList)) {
        if (nmbrLen(unprovedList) == 1 &&
            !strcmp(g_Statement[unprovedList[0]].labelName,
            g_Statement[statemNum].labelName)) {
          printLongLine(cat(
"<TR><TD ALIGN=left >&nbsp;<B><FONT COLOR=\"#FF6600\">",
"WARNING: This theorem has an incomplete proof.</FONT></B><BR></TD></TR>",
              NULL), "", "\"");

        } else {
          printLongLine(cat(
"<TR><TD ALIGN=left >&nbsp;</TD><TD><B><FONT COLOR=\"#FF6600\">",
"WARNING: This proof depends on the following unproved theorem(s): ",
              NULL), "", "\"");
          let(&tmpStr, "");
          for (i = 0; i < nmbrLen(unprovedList); i++) {
            let(&tmpStr, cat(tmpStr, " <A HREF=\"",
                g_Statement[unprovedList[i]].labelName, ".html\">",
                g_Statement[unprovedList[i]].labelName, "</A>",
                NULL));
          }
          printLongLine(cat(tmpStr, "</B></FONT></TD></TR>", NULL), "", "\"");
        }
      }




      if (g_printStringForReferencedBy[0]) {



        if (g_outputToString != 1) bug(257);



        printLongLine(g_printStringForReferencedBy, "", "\"");
        let(&g_printStringForReferencedBy, "");


      } else {
        bug(263);
      }

    }



    g_outputToString = 0;
  }

 typeProof_return:
  let(&tmpStr, "");
  let(&tmpStr1, "");
  let(&statementUsedFlags, "");
  let(&locLabDecl, "");
  let(&tgtLabel, "");
  let(&srcLabel, "");
  let(&startPrefix, "");
  let(&tgtPrefix, "");
  let(&srcPrefix, "");
  let(&userPrefix, "");
  let(&contPrefix, "");
  let(&hypStr, "");
  let(&startStringWithNum, "");
  let(&startStringWithoutNum, "");
  nmbrLet(&unprovedList, NULL_NMBRSTRING);
  nmbrLet(&localLabels, NULL_NMBRSTRING);
  nmbrLet(&localLabelNames, NULL_NMBRSTRING);
  nmbrLet(&proof, NULL_NMBRSTRING);
  nmbrLet(&targetHyps, NULL_NMBRSTRING);
  nmbrLet(&indentationLevel, NULL_NMBRSTRING);
  nmbrLet(&essentialFlags, NULL_NMBRSTRING);
  nmbrLet(&stepRenumber, NULL_NMBRSTRING);
  nmbrLet(&notUnifiedFlags, NULL_NMBRSTRING);
  nmbrLet(&relativeStepNums, NULL_NMBRSTRING);
}


void showDetailStep(long statemNum, long detailStep) {

  long i, j, plen, step, stmt, sourceStmt, targetStmt;
  vstring tmpStr = "";
  vstring tmpStr1 = "";
  nmbrString *proof = NULL_NMBRSTRING;
  nmbrString *localLabels = NULL_NMBRSTRING;
  nmbrString *localLabelNames = NULL_NMBRSTRING;
  nmbrString *targetHyps = NULL_NMBRSTRING;
  long nextLocLabNum = 1;
  void *voidPtr;
  char type;


  i = parseProof(statemNum);
  if (i) {
    printLongLine("?The proof is incomplete or has an error", "", " ");
    return;
  }
  plen = nmbrLen(g_WrkProof.proofString);
  if (plen < detailStep || detailStep < 1) {
    printLongLine(cat("?The step number should be from 1 to ",
        str((double)plen), NULL), "", " ");
    return;
  }


  getStep.stepNum = detailStep;
  parseProof(statemNum);
  verifyProof(statemNum);


  nmbrLet(&proof, g_WrkProof.proofString);
  plen = nmbrLen(proof);


  for (step = 0; step < plen; step++) {
    stmt = proof[step];
    if (stmt <= -1000) {
      stmt = -1000 - stmt;
      if (!nmbrElementIn(1, localLabels, stmt)) {
        nmbrLet(&localLabels, nmbrAddElement(localLabels, stmt));
      }
    }
  }

  nmbrLet(&localLabelNames, nmbrSpace(plen));


  nmbrLet(&targetHyps, nmbrGetTargetHyp(proof, statemNum));


  for (step = 0; step < plen; step++) {
    stmt = proof[step];
    if (stmt >= 0) {
      if (nmbrElementIn(1, localLabels, step)) {

        let(&tmpStr1, str((double)nextLocLabNum));
        while (1) {
          voidPtr = (void *)bsearch(tmpStr,
              g_allLabelKeyBase, (size_t)g_numAllLabelKeys,
              sizeof(long), labelSrchCmp);
          if (!voidPtr) break;
          nextLocLabNum++;
          let(&tmpStr1, str((double)nextLocLabNum));
        }
        localLabelNames[step] = nextLocLabNum;
        nextLocLabNum++;
      }
    }
  }


  let(&tmpStr, g_Statement[targetHyps[detailStep - 1]].labelName);
  let(&tmpStr1, "");
  stmt = proof[detailStep - 1];
  if (stmt < 0) {
    if (stmt <= -1000) {
      stmt = -1000 - stmt;

      let(&tmpStr, cat(tmpStr,"=", str((double)(localLabelNames[stmt])), NULL));
      type = g_Statement[proof[stmt]].type;
    } else {
      if (stmt != -(long)'?') bug(207);
      let(&tmpStr, cat(tmpStr,"=",chr(-stmt), NULL));
      type = '?';
    }
  } else {
    if (nmbrElementIn(1, localLabels, detailStep - 1)) {

      let(&tmpStr1, cat(str((double)(localLabelNames[detailStep - 1])), ":",
          NULL));
    }
    let(&tmpStr, cat(tmpStr, "=", g_Statement[stmt].labelName, NULL));
    type = g_Statement[stmt].type;
  }


  printLongLine(cat("Proof step ",
      str((double)detailStep),
      ":  ",
      tmpStr1,
      tmpStr,
      " $",
      chr(type),
      " ",
      nmbrCvtMToVString(g_WrkProof.mathStringPtrs[detailStep - 1]),
      NULL),
      "  ",
      " ");


  let(&tmpStr, cat("This step assigns ", NULL));
  let(&tmpStr1, "");
  stmt = proof[detailStep - 1];
  sourceStmt = stmt;
  if (stmt < 0) {
    if (stmt <= -1000) {
      stmt = -1000 - stmt;

      let(&tmpStr, cat(tmpStr, "step ", str((double)stmt),
          " (via local label reference \"",
          str((double)(localLabelNames[stmt])), "\") to ", NULL));
    } else {
      if (stmt != -(long)'?') bug(208);
      let(&tmpStr, cat(tmpStr, "an unknown statement to ", NULL));
    }
  } else {
    let(&tmpStr, cat(tmpStr, "source \"", g_Statement[stmt].labelName,
        "\" ($", chr(g_Statement[stmt].type), ") to ", NULL));
    if (nmbrElementIn(1, localLabels, detailStep - 1)) {

      let(&tmpStr1, cat("  This step also declares the local label ",
          str((double)(localLabelNames[detailStep - 1])),
          ", which is used later on.",
          NULL));
    }
  }
  targetStmt = targetHyps[detailStep - 1];
  if (detailStep == plen) {
    let(&tmpStr, cat(tmpStr, "the final assertion being proved.", NULL));
  } else {
    let(&tmpStr, cat(tmpStr, "target \"", g_Statement[targetStmt].labelName,
    "\" ($", chr(g_Statement[targetStmt].type), ").", NULL));
  }

  let(&tmpStr, cat(tmpStr, tmpStr1, NULL));

  if (sourceStmt >= 0) {
    if (g_Statement[sourceStmt].type == a_
        || g_Statement[sourceStmt].type == p_) {
      j = nmbrLen(g_Statement[sourceStmt].reqHypList);
      if (j != nmbrLen(getStep.sourceHyps)) bug(209);
      if (!j) {
        let(&tmpStr, cat(tmpStr,
            "  The source assertion requires no hypotheses.", NULL));
      } else {
        if (j == 1) {
          let(&tmpStr, cat(tmpStr,
              "  The source assertion requires the hypothesis ", NULL));
        } else {
          let(&tmpStr, cat(tmpStr,
              "  The source assertion requires the hypotheses ", NULL));
        }
        for (i = 0; i < j; i++) {
          let(&tmpStr, cat(tmpStr, "\"",
              g_Statement[g_Statement[sourceStmt].reqHypList[i]].labelName,
              "\" ($",
              chr(g_Statement[g_Statement[sourceStmt].reqHypList[i]].type),
              ", step ", str((double)(getStep.sourceHyps[i] + 1)), ")", NULL));
          if (i == 0 && j == 2) {
            let(&tmpStr, cat(tmpStr, " and ", NULL));
          }
          if (i < j - 2 && j > 2) {
            let(&tmpStr, cat(tmpStr, ", ", NULL));
          }
          if (i == j - 2 && j > 2) {
            let(&tmpStr, cat(tmpStr, ", and ", NULL));
          }
        }
        let(&tmpStr, cat(tmpStr, ".", NULL));
      }
    }
  }

  if (detailStep < plen) {
    let(&tmpStr, cat(tmpStr,
         "  The parent assertion of the target hypothesis is \"",
        g_Statement[getStep.targetParentStmt].labelName, "\" ($",
        chr(g_Statement[getStep.targetParentStmt].type),", step ",
        str((double)(getStep.targetParentStep)), ").", NULL));
  } else {
    let(&tmpStr, cat(tmpStr,
        "  The target has no parent because it is the assertion being proved.",
        NULL));
  }

  printLongLine(tmpStr, "", " ");

  if (sourceStmt >= 0) {
    if (g_Statement[sourceStmt].type == a_
        || g_Statement[sourceStmt].type == p_) {
      print2("The source assertion before substitution was:\n");
      printLongLine(cat("    ", g_Statement[sourceStmt].labelName, " $",
          chr(g_Statement[sourceStmt].type), " ", nmbrCvtMToVString(
          g_Statement[sourceStmt].mathString), NULL),
          "        ", " ");
      j = nmbrLen(getStep.sourceSubstsNmbr);
      if (j == 1) {
        printLongLine(cat(
            "The following substitution was made to the source assertion:",
            NULL),""," ");
      } else {
        printLongLine(cat(
            "The following substitutions were made to the source assertion:",
            NULL),""," ");
      }
      if (!j) {
        print2("    (None)\n");
      } else {
        print2("    Variable  Substituted with\n");
        for (i = 0; i < j; i++) {
          printLongLine(cat("     ",
              g_MathToken[getStep.sourceSubstsNmbr[i]].tokenName," ",
              space(9 - (long)strlen(
                g_MathToken[getStep.sourceSubstsNmbr[i]].tokenName)),
              nmbrCvtMToVString(getStep.sourceSubstsPntr[i]), NULL),
              "                ", " ");
        }
      }
    }
  }

  if (detailStep < plen) {
    print2("The target hypothesis before substitution was:\n");
    printLongLine(cat("    ", g_Statement[targetStmt].labelName, " $",
        chr(g_Statement[targetStmt].type), " ", nmbrCvtMToVString(
        g_Statement[targetStmt].mathString), NULL),
        "        ", " ");
    j = nmbrLen(getStep.targetSubstsNmbr);
    if (j == 1) {
      printLongLine(cat(
          "The following substitution was made to the target hypothesis:",
          NULL),""," ");
    } else {
      printLongLine(cat(
          "The following substitutions were made to the target hypothesis:",
          NULL),""," ");
    }
    if (!j) {
      print2("    (None)\n");
    } else {
      print2("    Variable  Substituted with\n");
      for (i = 0; i < j; i++) {
        printLongLine(cat("     ",
            g_MathToken[getStep.targetSubstsNmbr[i]].tokenName, " ",
            space(9 - (long)strlen(
              g_MathToken[getStep.targetSubstsNmbr[i]].tokenName)),
            nmbrCvtMToVString(getStep.targetSubstsPntr[i]), NULL),
            "                ", " ");
      }
    }
  }

  cleanWrkProof();
  getStep.stepNum = 0;


  j = pntrLen(getStep.sourceSubstsPntr);
  for (i = 0; i < j; i++) {
    nmbrLet((nmbrString **)(&getStep.sourceSubstsPntr[i]),
        NULL_NMBRSTRING);
  }
  j = pntrLen(getStep.targetSubstsPntr);
  for (i = 0; i < j; i++) {
    nmbrLet((nmbrString **)(&getStep.targetSubstsPntr[i]),
        NULL_NMBRSTRING);
  }
  nmbrLet(&getStep.sourceHyps, NULL_NMBRSTRING);
  pntrLet(&getStep.sourceSubstsPntr, NULL_PNTRSTRING);
  nmbrLet(&getStep.sourceSubstsNmbr, NULL_NMBRSTRING);
  pntrLet(&getStep.targetSubstsPntr, NULL_PNTRSTRING);
  nmbrLet(&getStep.targetSubstsNmbr, NULL_NMBRSTRING);


  let(&tmpStr, "");
  let(&tmpStr1, "");
  nmbrLet(&localLabels, NULL_NMBRSTRING);
  nmbrLet(&localLabelNames, NULL_NMBRSTRING);
  nmbrLet(&proof, NULL_NMBRSTRING);
  nmbrLet(&targetHyps, NULL_NMBRSTRING);

}


void proofStmtSumm(long statemNum, flag essentialFlag, flag texFlag) {

  long i, j, k, pos, stmt, plen, slen, step;
  char type;
  vstring statementUsedFlags = "";
  vstring str1 = "";
  vstring str2 = "";
  vstring str3 = "";
  nmbrString *statementList = NULL_NMBRSTRING;
  nmbrString *proof = NULL_NMBRSTRING;
  nmbrString *essentialFlags = NULL_NMBRSTRING;

  if (texFlag && g_htmlFlag) bug(239);

  if (!texFlag) {
    print2("Summary of statements used in the proof of \"%s\":\n",
        g_Statement[statemNum].labelName);
  } else {
    g_outputToString = 1;
    if (!g_htmlFlag) {
      print2("\n");
      print2("\\vspace{1ex} %%3\n");
      printLongLine(cat("Summary of statements used in the proof of ",
          "{\\tt ",
          asciiToTt(g_Statement[statemNum].labelName),
          "}:", NULL), "", " ");
    } else {
      printLongLine(cat("Summary of statements used in the proof of ",
          "<B>",
          asciiToTt(g_Statement[statemNum].labelName),
          "</B>:", NULL), "", "\"");
    }
    g_outputToString = 0;
    fprintf(g_texFilePtr, "%s", g_printString);
    let(&g_printString, "");
  }

  if (g_Statement[statemNum].type != p_) {
    print2("  This is not a provable ($p) statement.\n");
    return;
  }


  if (parseProof(statemNum) > 1) {

    nmbrLet(&proof, nmbrAddElement(NULL_NMBRSTRING, -(long)'?'));
  } else {
    nmbrLet(&proof, g_WrkProof.proofString);
  }

  plen = nmbrLen(proof);

  if (essentialFlag) {
    nmbrLet(&essentialFlags, nmbrGetEssential(proof));
  }

  for (step = 0; step < plen; step++) {
    if (essentialFlag) {
      if (!essentialFlags[step]) continue;
    }
    stmt = proof[step];
    if (stmt < 0) {
      continue;
    }
    if (1) {
      if (g_Statement[stmt].type != a_ && g_Statement[stmt].type != p_) {
        continue;
      }
    }

    if (!nmbrElementIn(1, statementList, stmt)) {
      nmbrLet(&statementList, nmbrAddElement(statementList, stmt));
    }
  }


  slen = nmbrLen(statementList);
  let(&statementUsedFlags, string(g_statements + 1, 'N'));
  for (pos = 0; pos < slen; pos++) {
    stmt = statementList[pos];
    if (stmt > statemNum || stmt < 1) bug(210);
    statementUsedFlags[stmt] = 'Y';
  }

  for (stmt = 1; stmt < statemNum; stmt++) {
    if (statementUsedFlags[stmt] == 'Y') {
      assignStmtFileAndLineNum(stmt);
      let(&str1, cat(" is located on line ",
          str((double)(g_Statement[stmt].lineNum)),
          " of the file ", NULL));
      if (!texFlag) {
        print2("\n");
        printLongLine(cat("Statement ", g_Statement[stmt].labelName, str1,
          "\"", g_Statement[stmt].fileName,
          "\".",NULL), "", " ");
      } else {
        g_outputToString = 1;
        if (!g_htmlFlag) {
          print2("\n");
          print2("\n");
          print2("\\vspace{1ex} %%4\n");
          printLongLine(cat("Statement {\\tt ",
              asciiToTt(g_Statement[stmt].labelName), "} ",
              str1, "{\\tt ",
              asciiToTt(g_Statement[stmt].fileName),
              "}.", NULL), "", " ");
          print2("\n");
        } else {
          printLongLine(cat("Statement <B>",
              asciiToTt(g_Statement[stmt].labelName), "</B> ",
              str1, " <B>",
              asciiToTt(g_Statement[stmt].fileName),
              "</B> ", NULL), "", "\"");
        }
        g_outputToString = 0;
        fprintf(g_texFilePtr, "%s", g_printString);
        let(&g_printString, "");
      }


      let(&str1, "");
      str1 = getDescription(stmt);
      if (str1[0]) {
        if (!texFlag) {
          printLongLine(cat("\"", str1, "\"", NULL), "", " ");
        } else {


          printTexComment(str1,
              1,
              PROCESS_EVERYTHING,
              0 );
        }
      }


      j = nmbrLen(g_Statement[stmt].reqHypList);
      for (i = 0; i < j; i++) {
        k = g_Statement[stmt].reqHypList[i];
        if (!essentialFlag || g_Statement[k].type != f_) {
          let(&str2, cat("  ",g_Statement[k].labelName,
              " $", chr(g_Statement[k].type), " ", NULL));
          if (!texFlag) {
            printLongLine(cat(str2,
                nmbrCvtMToVString(g_Statement[k].mathString), " $.", NULL),
                "      "," ");
          } else {
            let(&str3, space((long)strlen(str2)));
            printTexLongMath(g_Statement[k].mathString,
                str2, str3, 0, 0);
          }
        }
      }

      let(&str1, "");
      type = g_Statement[stmt].type;
      if (type == p_) let(&str1, " $= ...");
      let(&str2, cat("  ", g_Statement[stmt].labelName,
          " $",chr(type), " ", NULL));
      if (!texFlag) {
        printLongLine(cat(str2,
            nmbrCvtMToVString(g_Statement[stmt].mathString),
            str1, " $.", NULL), "      ", " ");
      } else {
        let(&str3, space((long)strlen(str2)));
        printTexLongMath(g_Statement[stmt].mathString,
            str2, str3, 0, 0);
      }

    }
  }

  let(&statementUsedFlags, "");
  let(&str1, "");
  let(&str2, "");
  let(&str3, "");
  nmbrLet(&statementList, NULL_NMBRSTRING);
  nmbrLet(&proof, NULL_NMBRSTRING);
  nmbrLet(&essentialFlags, NULL_NMBRSTRING);

}





flag traceProof(long statemNum,
  flag essentialFlag,
  flag axiomFlag,
  vstring matchList,
  vstring traceToList,
  flag testOnlyFlag )
{

  long stmt, pos;
  vstring statementUsedFlags = "";
  vstring outputString = "";
  nmbrString *unprovedList = NULL_NMBRSTRING;
  flag foundFlag = 0;


  if (g_Statement[statemNum].type != (char)p_) bug(249);

  if (!testOnlyFlag) {
    if (axiomFlag) {
      print2(
  "Statement \"%s\" assumes the following axioms ($a statements):\n",
          g_Statement[statemNum].labelName);
    } else  if (traceToList[0] == 0) {
      print2(
  "The proof of statement \"%s\" uses the following earlier statements:\n",
          g_Statement[statemNum].labelName);
    } else {
      print2(
  "The proof of statement \"%s\" traces back to \"%s\" via:\n",
          g_Statement[statemNum].labelName, traceToList);
    }
  }

  traceProofWork(statemNum,
      essentialFlag,
      traceToList,
      &statementUsedFlags,
      &unprovedList);
  if ((signed)(strlen(statementUsedFlags)) != g_statements + 1) bug(226);


  let(&outputString, "");
  for (stmt = 1; stmt < statemNum; stmt++) {
    if (statementUsedFlags[stmt] == 'Y') {


      if (matchList[0]) {

        if (!matchesList(g_Statement[stmt].labelName, matchList, '*', '?'))
          continue;
      }


      foundFlag = 1;
      if (testOnlyFlag) {
        goto TRACE_RETURN;
      }
      if (axiomFlag) {
        if (g_Statement[stmt].type == a_) {
          let(&outputString, cat(outputString, " ", g_Statement[stmt].labelName,
              NULL));
        }
      } else {
        let(&outputString, cat(outputString, " ", g_Statement[stmt].labelName,
            NULL));
        switch (g_Statement[stmt].type) {
          case a_: let(&outputString, cat(outputString, "($a)", NULL)); break;
          case e_: let(&outputString, cat(outputString, "($e)", NULL)); break;
          case f_: let(&outputString, cat(outputString, "($f)", NULL)); break;
        }
      }
    }
  }


  if (testOnlyFlag) {
    goto TRACE_RETURN;
  }

  if (outputString[0]) {
    let(&outputString, cat(" ", outputString, NULL));
  } else {
    let(&outputString, "  (None)");
  }


  printLongLine(outputString, "  ", " ");


  if (nmbrLen(unprovedList)) {
    print2("Warning: The following traced statement(s) were not proved:\n");
    let(&outputString, "");
    for (pos = 0; pos < nmbrLen(unprovedList); pos++) {
      let(&outputString, cat(outputString, " ", g_Statement[unprovedList[
          pos]].labelName, NULL));
    }
    let(&outputString, cat("  ", outputString, NULL));
    printLongLine(outputString, "  ", " ");
  }

 TRACE_RETURN:

  let(&outputString, "");
  let(&statementUsedFlags, "");
  nmbrLet(&unprovedList, NULL_NMBRSTRING);
  return foundFlag;
}

void traceProofWork(long statemNum,
  flag essentialFlag,
  vstring traceToList,
  vstring *statementUsedFlagsP,
  nmbrString **unprovedListP)
{

  long pos, stmt, plen, slen, step;
  nmbrString *statementList = NULL_NMBRSTRING;
  nmbrString *proof = NULL_NMBRSTRING;
  nmbrString *essentialFlags = NULL_NMBRSTRING;
  vstring traceToFilter = "";
  vstring str1 = "";
  long j;



  if (traceToList[0] != 0) {
    let(&traceToFilter, string(g_statements + 1, 'N'));

    for (stmt = 1; stmt <= g_statements; stmt++) {
      if (g_Statement[stmt].type != (char)a_
          && g_Statement[stmt].type != (char)p_)
        continue;

      if (!matchesList(g_Statement[stmt].labelName, traceToList, '*', '?'))
        continue;
      let(&str1, "");
      str1 = traceUsage(stmt ,
          1,
          statemNum );
      traceToFilter[stmt] = 'Y';
      if (str1[0] == 'Y') {
        for (j = stmt + 1; j <= g_statements; j++) {

          if (str1[j] == 'Y') traceToFilter[j] = 'Y';
        }
      }
    }
  }

  nmbrLet(&statementList, nmbrSpace(g_statements));
  statementList[0] = statemNum;
  slen = 1;
  nmbrLet(&(*unprovedListP), NULL_NMBRSTRING);
  let(&(*statementUsedFlagsP), string(g_statements + 1, 'N'));
  (*statementUsedFlagsP)[statemNum] = 'Y';
  for (pos = 0; pos < slen; pos++) {
    if (g_Statement[statementList[pos]].type != p_) {
      continue;
    }


    if (parseProof(statementList[pos]) > 1) {

      nmbrLet(&proof, nmbrAddElement(NULL_NMBRSTRING, -(long)'?'));
    } else {
      nmbrLet(&proof, g_WrkProof.proofString);
    }

    plen = nmbrLen(proof);

    if (essentialFlag) {
      nmbrLet(&essentialFlags, nmbrGetEssential(proof));
    }
    for (step = 0; step < plen; step++) {
      if (essentialFlag) {
        if (!essentialFlags[step]) continue;
      }
      stmt = proof[step];
      if (stmt < 0) {
        if (stmt > -1000) {

          if (!nmbrElementIn(1, *unprovedListP, statementList[pos])) {
            nmbrLet(&(*unprovedListP), nmbrAddElement(*unprovedListP,
                statementList[pos]));
          }
        }
        continue;
      }
      if (1) {
        if (g_Statement[stmt].type != a_ && g_Statement[stmt].type != p_) {
          continue;
        }
      }

      if ((*statementUsedFlagsP)[stmt] == 'N') {



        if (traceToList[0] == 0) {
          statementList[slen] = stmt;
          slen++;
          (*statementUsedFlagsP)[stmt] = 'Y';
        } else {
          if (traceToFilter[stmt] == 'Y') {
            statementList[slen] = stmt;
            slen++;
            (*statementUsedFlagsP)[stmt] = 'Y';
          }
        }
      }
    }
  }


  nmbrLet(&essentialFlags, NULL_NMBRSTRING);
  nmbrLet(&proof, NULL_NMBRSTRING);
  nmbrLet(&statementList, NULL_NMBRSTRING);
  let(&str1, "");
  let(&str1, "");
  return;

}

nmbrString *stmtFoundList = NULL_NMBRSTRING;
long indentShift = 0;


void traceProofTree(long statemNum,
  flag essentialFlag, long endIndent)
{
  if (g_Statement[statemNum].type != p_) {
    print2("Statement %s is not a $p statement.\n",
        g_Statement[statemNum].labelName);
    return;
  }

  printLongLine(cat("The proof tree traceback for statement \"",
      g_Statement[statemNum].labelName,
      "\" follows.  The statements used by each proof are indented one level in,",
      " below the statement being proved.  Hypotheses are not included.",
      NULL),
      "", " ");
  print2("\n");

  nmbrLet(&stmtFoundList, NULL_NMBRSTRING);
  indentShift = 0;
  traceProofTreeRec(statemNum, essentialFlag, endIndent, 0);
  nmbrLet(&stmtFoundList, NULL_NMBRSTRING);
}


void traceProofTreeRec(long statemNum,
  flag essentialFlag, long endIndent, long recursDepth)
{
  long i, pos, stmt, plen, slen, step;
  vstring outputStr = "";
  nmbrString *localFoundList = NULL_NMBRSTRING;
  nmbrString *localPrintedList = NULL_NMBRSTRING;
  flag unprovedFlag = 0;
  nmbrString *proof = NULL_NMBRSTRING;
  nmbrString *essentialFlags = NULL_NMBRSTRING;


  let(&outputStr, "");
  outputStr = getDescription(statemNum);
  let(&outputStr, edit(outputStr, 8 + 16 + 128));
  slen = len(outputStr);
  for (i = 0; i < slen; i++) {

    if (outputStr[i] == '\n') {
      outputStr[i] = ' ';
    }
  }

  static final long INDENT_INCR =D.INDENT_INCR;
  static final long MAX_LINE_LEN =D.MAX_LINE_LEN;

  if ((recursDepth * INDENT_INCR - indentShift) >
      (g_screenWidth - MAX_LINE_LEN) + 50) {
    indentShift = indentShift + 40 + (g_screenWidth - MAX_LINE_LEN);
    print2("****** Shifting indentation.  Total shift is now %ld.\n",
      (long)indentShift);
  }
  if ((recursDepth * INDENT_INCR - indentShift) < 1 && indentShift != 0) {
    indentShift = indentShift - 40 - (g_screenWidth - MAX_LINE_LEN);
    print2("****** Shifting indentation.  Total shift is now %ld.\n",
      (long)indentShift);
  }

  let(&outputStr, cat(space(recursDepth * INDENT_INCR - indentShift),
      g_Statement[statemNum].labelName, " $", chr(g_Statement[statemNum].type),
      "  \"", edit(outputStr, 8 + 128), "\"", NULL));

  if (len(outputStr) > MAX_LINE_LEN + (g_screenWidth - MAX_LINE_LEN)) {
    let(&outputStr, cat(left(outputStr,
        MAX_LINE_LEN + (g_screenWidth - MAX_LINE_LEN) - 3), "...", NULL));
  }

  if (g_Statement[statemNum].type == p_ || g_Statement[statemNum].type == a_) {

    print2("%s\n", outputStr);
  }

  if (g_Statement[statemNum].type != p_) {
    let(&outputStr, "");
    return;
  }

  if (endIndent) {

    if (endIndent < recursDepth + 2) {
      let(&outputStr, "");
      return;
    }
  }


  if (parseProof(statemNum) > 1) {

    nmbrLet(&proof, nmbrAddElement(NULL_NMBRSTRING, -(long)'?'));
  } else {
    nmbrLet(&proof, g_WrkProof.proofString);
  }

  plen = nmbrLen(proof);

  if (essentialFlag) {
    nmbrLet(&essentialFlags, nmbrGetEssential(proof));
  }
  nmbrLet(&localFoundList, NULL_NMBRSTRING);
  nmbrLet(&localPrintedList, NULL_NMBRSTRING);
  for (step = 0; step < plen; step++) {
    if (essentialFlag) {
      if (!essentialFlags[step]) continue;

    }
    stmt = proof[step];
    if (stmt < 0) {
      if (stmt > -1000) {

        unprovedFlag = 1;
      }
      continue;
    }
    if (!nmbrElementIn(1, localFoundList, stmt)) {
      nmbrLet(&localFoundList, nmbrAddElement(localFoundList, stmt));
    }
    if (!nmbrElementIn(1, stmtFoundList, stmt)) {
      traceProofTreeRec(stmt, essentialFlag, endIndent, recursDepth + 1);
      nmbrLet(&localPrintedList, nmbrAddElement(localPrintedList, stmt));
      nmbrLet(&stmtFoundList, nmbrAddElement(stmtFoundList, stmt));
    }
  }


  slen = nmbrLen(localFoundList);
  let(&outputStr, "");
  for (pos = 0; pos < slen; pos++) {
    stmt = localFoundList[pos];
    if (!nmbrElementIn(1, localPrintedList, stmt)) {

      if (g_Statement[stmt].type == p_ || g_Statement[stmt].type == a_) {
        let(&outputStr, cat(outputStr, " ",
            g_Statement[stmt].labelName, NULL));
      }
    }
  }

  if (len(outputStr)) {
    printLongLine(cat(space(INDENT_INCR * (recursDepth + 1) - 1 - indentShift),
      outputStr, " (shown above)", NULL),
      space(INDENT_INCR * (recursDepth + 2) - indentShift), " ");
  }

  if (unprovedFlag) {
    printLongLine(cat(space(INDENT_INCR * (recursDepth + 1) - indentShift),
      "*** Statement ", g_Statement[statemNum].labelName, " has not been proved."
      , NULL),
      space(INDENT_INCR * (recursDepth + 2)), " ");
  }

  let(&outputStr, "");
  nmbrLet(&localFoundList, NULL_NMBRSTRING);
  nmbrLet(&localPrintedList, NULL_NMBRSTRING);
  nmbrLet(&proof, NULL_NMBRSTRING);
  nmbrLet(&essentialFlags, NULL_NMBRSTRING);

}






double countSteps(long statemNum, flag essentialFlag)
{
  static double *stmtCount;
  static double *stmtNodeCount;
  static long *stmtDist;
  static long *stmtMaxPath;
  static double *stmtAveDist;
  static long *stmtProofLen;
  static long *stmtUsage;
  static long level = 0;
  static flag unprovedFlag;

  long stmt, plen, step, i, j, k;
  long essentialplen;
  nmbrString *proof = NULL_NMBRSTRING;
  double stepCount;


  static vstring *stmtBigCount;
  vstring stepBigCount = "";
  vstring tmpBig1 = "";

  double stepNodeCount;
  double stepDistSum;
  nmbrString *essentialFlags = NULL_NMBRSTRING;
  vstring tmpStr = "";
  long actualSteps, actualSubTheorems;
  long actualSteps2, actualSubTheorems2;

  essentialplen = 0;


  if (!level) {
    stmtCount = malloc((sizeof(double) * ((size_t)g_statements + 1)));
    stmtBigCount = malloc((sizeof(vstring) * ((size_t)g_statements + 1)));

    stmtNodeCount = malloc(sizeof(double) * ((size_t)g_statements + 1));
    stmtDist = malloc(sizeof(long) * ((size_t)g_statements + 1));
    stmtMaxPath = malloc(sizeof(long) * ((size_t)g_statements + 1));
    stmtAveDist = malloc(sizeof(double) * ((size_t)g_statements + 1));
    stmtProofLen = malloc(sizeof(long) * ((size_t)g_statements + 1));
    stmtUsage = malloc(sizeof(long) * ((size_t)g_statements + 1));
    if (!stmtCount || !stmtNodeCount || !stmtDist || !stmtMaxPath ||
        !stmtAveDist || !stmtProofLen || !stmtUsage) {
      print2("?Memory overflow.  Step count will be wrong.\n");
      if (stmtCount) free(stmtCount);
      if (stmtBigCount) free(stmtBigCount);
      if (stmtNodeCount) free(stmtNodeCount);
      if (stmtDist) free(stmtDist);
      if (stmtMaxPath) free(stmtMaxPath);
      if (stmtAveDist) free(stmtAveDist);
      if (stmtProofLen) free(stmtProofLen);
      if (stmtUsage) free(stmtUsage);
      return (0);
    }
    for (stmt = 1; stmt < g_statements + 1; stmt++) {
      stmtCount[stmt] = 0;
      stmtBigCount[stmt] = "";
      stmtUsage[stmt] = 0;
      stmtDist[stmt] = 0;
    }
    unprovedFlag = 0;
  }
  level++;
  stepCount = 0;
  let(&stepBigCount, "0");
  stepNodeCount = 0;
  stepDistSum = 0;
  stmtDist[statemNum] = -2;

  if (g_Statement[statemNum].type != (char)p_) {

    stepCount = 1;
    let(&stepBigCount, "1");
    stepNodeCount = 0;
    stmtDist[statemNum] = 0;
    goto returnPoint;
  }


  if (parseProof(statemNum) > 1) {

    nmbrLet(&proof, nmbrAddElement(NULL_NMBRSTRING, -(long)'?'));
  } else {

    nmbrLet(&proof, g_WrkProof.proofString);
  }

  plen = nmbrLen(proof);

  if (essentialFlag) {
    nmbrLet(&essentialFlags, nmbrGetEssential(proof));
  }
  essentialplen = 0;
  for (step = 0; step < plen; step++) {
    if (essentialFlag) {
      if (!essentialFlags[step]) continue;
    }
    essentialplen++;
    stmt = proof[step];
    if (stmt < 0) {
      if (stmt <= -1000) {




        continue;
      } else {

        unprovedFlag = 1;
        stepCount = stepCount + 1;


        let(&tmpBig1, "");
        tmpBig1 = bigAdd(stepBigCount, "1");
        let(&stepBigCount, tmpBig1);

        stepNodeCount = stepNodeCount + 1;
        stepDistSum = stepDistSum + 1;
      }
    } else {
      if (stmtCount[stmt] == 0) {

        stepCount = stepCount + countSteps(stmt, essentialFlag);
      } else {

        stepCount = stepCount + stmtCount[stmt];
      }



      let(&tmpBig1, "");
      tmpBig1 = bigAdd(stepBigCount, stmtBigCount[stmt]);
      let(&stepBigCount, tmpBig1);

      if (g_Statement[stmt].type == (char)p_) {

        for (j = 0; j < g_Statement[stmt].numReqHyp; j++) {
          k = g_Statement[stmt].reqHypList[j];
          if (!essentialFlag || g_Statement[k].type == (char)e_) {
            stepCount--;



            let(&tmpBig1, "");
            tmpBig1 = bigSub(stepBigCount, "1");
            let(&stepBigCount, tmpBig1);

          }
        }
      }
      stmtUsage[stmt]++;
      if (stmtDist[statemNum] < stmtDist[stmt] + 1) {
        stmtDist[statemNum] = stmtDist[stmt] + 1;
        stmtMaxPath[statemNum] = stmt;
      }
      stepNodeCount = stepNodeCount + stmtNodeCount[stmt];
      stepDistSum = stepDistSum + stmtAveDist[stmt] + 1;
    }

  }

 returnPoint:


  stmtCount[statemNum] = stepCount;


  if ((stmtBigCount[statemNum])[0] != 0) bug(264);
  let(&stmtBigCount[statemNum], stepBigCount);

  stmtNodeCount[statemNum] = stepNodeCount + 1;
  stmtAveDist[statemNum] = (double)stepDistSum / (double)essentialplen;
  stmtProofLen[statemNum] = essentialplen;

  nmbrLet(&proof, NULL_NMBRSTRING);
  nmbrLet(&essentialFlags, NULL_NMBRSTRING);

  level--;

  if (!level) {
    if (unprovedFlag) stepCount = 0;


    actualSteps = stmtProofLen[statemNum];
    actualSubTheorems = 0;
    actualSteps2 = actualSteps;
    actualSubTheorems2 = 0;
    for (i = 1; i < statemNum; i++) {
      if (g_Statement[i].type == (char)p_ && stmtCount[i] != 0) {
        actualSteps = actualSteps + stmtProofLen[i];
        actualSubTheorems++;
        if (stmtUsage[i] > 1) {
          actualSubTheorems2++;
          actualSteps2 = actualSteps2 + stmtProofLen[i];
        } else {
          actualSteps2 = actualSteps2 + stmtProofLen[i] - 1;
          for (j = 0; j < g_Statement[i].numReqHyp; j++) {

            k = g_Statement[i].reqHypList[j];
            if (!essentialFlag || g_Statement[k].type == (char)e_) {
              actualSteps2--;
            }
          }
        }
      }
    }

    j = statemNum;
    for (i = stmtDist[statemNum]; i >= 0; i--) {
      if (stmtDist[j] != i) bug(214);
      let(&tmpStr, cat(tmpStr, " <- ", g_Statement[j].labelName,
          NULL));
      j = stmtMaxPath[j];
    }
    printLongLine(cat(
       "The statement's actual proof has ",
           str((double)(stmtProofLen[statemNum])), " steps.  ",
       "Backtracking, a total of ", str((double)actualSubTheorems),
           " different subtheorems are used.  ",
       "The statement and subtheorems have a total of ",
           str((double)actualSteps), " actual steps.  ",
       "If subtheorems used only once were eliminated,",
           " there would be a total of ",
           str((double)actualSubTheorems2), " subtheorems, and ",
       "the statement and subtheorems would have a total of ",
           str((double)actualSteps2), " steps.  ",
       "The proof would have ",




       stepBigCount,
       strlen(stepBigCount) < 6 ? ""
           : cat(" =~ ",
                 left(
                    str((double)
                        ((5.0 + val(left(stepBigCount, 3))) / 100.0)),
                    3),
                 " x 10^",
                 str((double)strlen(stepBigCount) - 1), NULL),


       " steps if fully expanded back to axiom references.  ",
       "The maximum path length is ",
       str((double)(stmtDist[statemNum])),
       ".  A longest path is:  ", right(tmpStr, 5), " .", NULL),
       "", " ");
    let(&tmpStr, "");

    free(stmtCount);
    free(stmtNodeCount);
    free(stmtDist);
    free(stmtMaxPath);
    free(stmtAveDist);
    free(stmtProofLen);
    free(stmtUsage);



    for (stmt = 1; stmt < g_statements + 1; stmt++) {
      let(&stmtBigCount[stmt], "");
    }
    free(stmtBigCount);

  }


  let(&tmpBig1, "");
  let(&stepBigCount, "");

  return(stepCount);

}



vstring bigAdd(vstring bignum1, vstring bignum2) {
  long len1, len2, maxlen, p, p1, p2, p3;
  char d1, d2, carry, dsum;
  vstring bignum3 = "";
  len1 = (long)strlen(bignum1);
  len2 = (long)strlen(bignum2);
  maxlen = (len1 < len2 ? len2 : len1);
  let(&bignum3, space(maxlen + 1));
  carry = 0;
  for (p = 1; p <= maxlen; p++) {
    p1 = len1 - p;
    p2 = len2 - p;
    d1 = (char)(p1 >= 0 ? bignum1[p1] - '0' : 0);
    d2 = (char)(p2 >= 0 ? bignum2[p2] - '0' : 0);
    dsum = (char)(d1 + d2 + carry);
    if (dsum > 9) {
      dsum = (char)(dsum - 10);
      carry = 1;
    } else {
      carry = 0;
    }
    p3 = maxlen + 1 - p;
    bignum3[p3] = (char)(dsum + '0');
  }
  bignum3[0] = (char)(carry + '0');
  while (bignum3[0] == '0') {

    let(&bignum3, right(bignum3, 2));
  }
  return bignum3;
}





vstring bigSub(vstring bignum1, vstring bignum2) {
  long len1, len3, p;
  vstring bignum3 = "";
  vstring bignum1cmpl = "";
  len1 = (long)strlen(bignum1);
  let(&bignum1cmpl, space(len1));
  for (p = 0; p <= len1 - 1; p++) {

    bignum1cmpl[p] = (char)(9 - (bignum1[p] - '0') + '0');
  }
  bignum3 = bigAdd(bignum1cmpl, bignum2);
  len3 = (long)strlen(bignum3);
  if (len3 < len1) {

    let(&bignum3, cat(string(len1 - len3, '0'), bignum3, NULL));
    len3 = len1;
  }
  for (p = 0; p <= len3 - 1; p++) {

    bignum3[p] = (char)(9 - (bignum3[p] - '0') + '0');
  }
  while (bignum3[0] == '0') {

    let(&bignum3, right(bignum3, 2));
  }
  let(&bignum1cmpl, "");
  return bignum3;
}







vstring traceUsage(long statemNum,
  flag recursiveFlag,
  long cutoffStmt ) {

  long lastPos, stmt, slen, pos;
  flag tmpFlag;
  vstring statementUsedFlags = "";

  nmbrString *statementList = NULL_NMBRSTRING;
  nmbrString *proof = NULL_NMBRSTRING;


  char *fbPtr;
  char *fbPtr2;
  char zapSave;
  flag notEFRec;

  if (g_Statement[statemNum].type == e_ || g_Statement[statemNum].type == f_
      || recursiveFlag) {
    notEFRec = 0;
  } else {
    notEFRec = 1;
  }

  nmbrLet(&statementList, nmbrAddElement(statementList, statemNum));
  lastPos = 1;


  if (cutoffStmt == 0) cutoffStmt = g_statements;


  for (stmt = statemNum + 1; stmt <= cutoffStmt; stmt++) {
    if (g_Statement[stmt].type != p_) continue;

    if (notEFRec) {
      fbPtr = g_Statement[stmt].proofSectionPtr;
      if (fbPtr[0] == 0) {
        continue;
      }
      fbPtr = fbPtr + whiteSpaceLen(fbPtr);
      if (fbPtr[0] == '(') {
        fbPtr2 = fbPtr;
        while (fbPtr2[0] != ')') {
          fbPtr2++;
          if (fbPtr2[0] == 0) bug(217);
        }
      } else {

        fbPtr2 = g_Statement[stmt].proofSectionPtr +
            g_Statement[stmt].proofSectionLen;
      }
      zapSave = fbPtr2[0];
      fbPtr2[0] = 0;
      if (!instr(1, fbPtr, g_Statement[statemNum].labelName)) {
        fbPtr2[0] = zapSave;
        continue;
      } else {

        fbPtr2[0] = zapSave;
      }
    }


    if (parseProof(stmt) > 1) {

      nmbrLet(&proof, nmbrAddElement(NULL_NMBRSTRING, -(long)'?'));
    } else {
      nmbrLet(&proof, g_WrkProof.proofString);
    }

    tmpFlag = 0;
    for (pos = 0; pos < lastPos; pos++) {
      if (nmbrElementIn(1, proof, statementList[pos])) {
        tmpFlag = 1;
        break;
      }
    }
    if (!tmpFlag) continue;


    nmbrLet(&statementList, nmbrAddElement(statementList, stmt));
    if (recursiveFlag) lastPos++;
  }

  slen = nmbrLen(statementList);


  let(&statementUsedFlags, string(g_statements + 1, 'N'));
  if (slen > 1) statementUsedFlags[0] = 'Y';

  for (pos = 1; pos < slen; pos++) {
    stmt = statementList[pos];
    if (stmt <= statemNum || g_Statement[stmt].type != p_ || stmt > g_statements)
        bug(212);
    statementUsedFlags[stmt] = 'Y';
  }
  return (statementUsedFlags);




}



void readInput(void)
{
  vstring fullInput_fn = "";

  let(&fullInput_fn, cat(g_rootDirectory, g_input_fn, NULL));




  g_sourcePtr = readSourceAndIncludes(g_input_fn, &g_sourceLen);
  if (g_sourcePtr == NULL) {
    print2(
"?Source was not read due to error(s).  Please correct and try again.\n");
    goto RETURN_POINT;
  }

  g_sourcePtr = readRawSource(
      g_sourcePtr, &g_sourceLen);
  parseKeywords();
  parseLabels();
  parseMathDecl();
  parseStatements();
  g_sourceHasBeenRead = 1;

 RETURN_POINT:
  let(&fullInput_fn, "");

}


void writeSource(
                flag reformatFlag ,

                flag splitFlag,
                flag noVersioningFlag,
                flag keepSplitsFlag,
                vstring extractLabelList
                )
{


  long i;

  vstring buffer = "";
  vstring fullOutput_fn = "";

  File fp;

  let(&fullOutput_fn, cat(g_rootDirectory, g_output_fn, NULL));

  if (splitFlag == 0
      && extractLabelList[0] == 0) {
    print2("Writing \"%s\"...\n", fullOutput_fn);
  }


  if (extractLabelList[0] != 0) {
    writeExtractedSource(
       extractLabelList,
       fullOutput_fn,
       noVersioningFlag
       );

    goto RETURN_POINT;
  }

  if (reformatFlag > 0) {



    for (i = 1; i <= g_statements + 1; i++) {


      let(&buffer,"");

      buffer = outputStatement(i,  reformatFlag);


    }
  }



  let(&buffer, "");
  buffer = writeSourceToBuffer();
  if (splitFlag == 1) {


    for (i = 1; i <= g_includeCalls; i++) {
      if (g_IncludeCall[i].pushOrPop == 0
          && !strcmp(g_output_fn, g_IncludeCall[i].included_fn)) {
        print2(
"?The output was not written because the main output file name is\n");
        print2(
"  the same as an included file.  Use a different name.\n");
        goto RETURN_POINT;
      }
    }

    writeSplitSource(&buffer, g_output_fn, noVersioningFlag, keepSplitsFlag);
  } else {
    fp = fSafeOpen(fullOutput_fn, "w", noVersioningFlag);
    if (fp == NULL) {
      print2("?Error trying to write \"%s\".\n", fp);
    } else {
      fprintf(fp, "%s", buffer);
      fclose(fp);
      if (keepSplitsFlag == 0) {
        deleteSplits(&buffer, noVersioningFlag);
      }
    }
  }

  print2("%ld source statement(s) were written.\n", g_statements);



 RETURN_POINT:
  let(&buffer,"");
  let(&fullOutput_fn,"");
  return;

}




void writeExtractedSource(
    vstring extractLabelList,
    vstring fullOutput_fn,
    flag noVersioningFlag)
{
  vstring statementUsedFlags = "";
  long stmt, stmtj, scpStmt, strtScpStmt, endScpStmt, j, p1, p2, p3, p4;
  vstring extractNeeded = "";
  nmbrString *unprovedList = NULL_NMBRSTRING;
  nmbrString *mstring = NULL_NMBRSTRING;
  long maxStmt;
  long hyp, hyps, mtkn, mtkns, dv, dvs;
  long dollarTStmt;
  vstring dollarTCmt = "";
  char zapChar;
  char *tmpPtr;
  vstring hugeHdrNeeded = "";
  vstring bigHdrNeeded = "";
  vstring smallHdrNeeded = "";
  vstring tinyHdrNeeded = "";
  char hdrNeeded;

  vstring hugeHdr = "";
  vstring bigHdr = "";
  vstring smallHdr = "";
  vstring tinyHdr = "";
  vstring hugeHdrComment = "";
  vstring bigHdrComment = "";
  vstring smallHdrComment = "";
  vstring tinyHdrComment = "";

  vstring mathTokenDeclared = "";
  vstring undeclaredC = "";
  vstring undeclaredV = "";
  long extractedStmts;
  vstring hdrSuffix = "";
  File fp;
  vstring buf = "";


  let(&extractNeeded, string(g_statements + 2, 'N'));


  print2("Tracing back through proofs for $a and $p statements needed...\n");
  if (!strcmp(extractLabelList, "*")) {

    print2(
       "   This may take up to 10 minutes.  (For audio alert when done,\n");
    print2("   type ahead \"b\" then \"<enter>\".)\n");

  }

  for (stmt = g_statements; stmt >= 1; stmt--) {
    if (extractNeeded[stmt] == 'Y') {

      continue;
    }

    if (!matchesList(g_Statement[stmt].labelName, extractLabelList, '*', '?'))
      continue;
    if (g_Statement[stmt].type == (char)a_) {
      extractNeeded[stmt] = 'Y';
      continue;
    }
    if (g_Statement[stmt].type != (char)p_)
      continue;
    traceProofWork(stmt,
        0,
        "",
        &statementUsedFlags,
        &unprovedList);
    if ((signed)(strlen(statementUsedFlags)) != g_statements + 1) bug(268);

    for (stmtj = 1; stmtj <= stmt; stmtj++) {
      if (statementUsedFlags[stmtj] == 'Y')
        extractNeeded[stmtj] = 'Y';
    }
  }


  print2("Determining which ${ and $} scoping statements are needed...\n");
  for (stmt = 1; stmt <= g_statements; stmt++) {
    if (extractNeeded[stmt] == 'Y'

        && (g_Statement[stmt].type == a_ || g_Statement[stmt].type == p_)) {
      scpStmt = stmt;
      while (g_Statement[scpStmt].beginScopeStatementNum != 0) {

        strtScpStmt = g_Statement[scpStmt].beginScopeStatementNum;
        if (g_Statement[strtScpStmt].type != lb_) bug(269);
        if (extractNeeded[strtScpStmt] == 'Y')

          break;
        endScpStmt = g_Statement[strtScpStmt].endScopeStatementNum;
        if (g_Statement[endScpStmt].type != rb_) bug(270);
        extractNeeded[strtScpStmt] = 'Y';
        extractNeeded[endScpStmt] = 'Y';
        scpStmt = strtScpStmt;
      }
    }
  }


  print2("Adding in $e and $f hypotheses and $d provisos...\n");
  for (stmt = 1; stmt <= g_statements; stmt++) {
    if (extractNeeded[stmt] == 'Y'

        && (g_Statement[stmt].type == a_ || g_Statement[stmt].type == p_)) {
      hyps = g_Statement[stmt].numReqHyp;
      for (hyp = 0; hyp < hyps; hyp++) {
        extractNeeded[g_Statement[stmt].reqHypList[hyp]] = 'Y';
      }
      hyps = nmbrLen(g_Statement[stmt].optHypList);
      for (hyp = 0; hyp < hyps; hyp++) {
        extractNeeded[g_Statement[stmt].optHypList[hyp]] = 'Y';
      }
      mtkns = nmbrLen(g_Statement[stmt].reqVarList);
      for (mtkn = 0; mtkn < mtkns; mtkn++) {


        extractNeeded[g_MathToken[
            (g_Statement[stmt].reqVarList)[mtkn]].statement] = 'Y';
      }
      mtkns = nmbrLen(g_Statement[stmt].optVarList);
      for (mtkn = 0; mtkn < mtkns; mtkn++) {

        extractNeeded[g_MathToken[
            (g_Statement[stmt].optVarList)[mtkn]].statement] = 'Y';
      }
      dvs = nmbrLen(g_Statement[stmt].reqDisjVarsStmt);
      for (dv = 0; dv < dvs; dv++) {

        extractNeeded[(g_Statement[stmt].reqDisjVarsStmt)[dv]] = 'Y';
      }
      dvs = nmbrLen(g_Statement[stmt].optDisjVarsStmt);
      for (dv = 0; dv < dvs; dv++) {

        extractNeeded[(g_Statement[stmt].optDisjVarsStmt)[dv]] = 'Y';
      }
    }
  }


  print2("Determining which $c and $v statements are needed...\n");
  for (stmt = 1; stmt <= g_statements; stmt++) {
    if (extractNeeded[stmt] == 'Y'

        && (g_Statement[stmt].type == a_
             || g_Statement[stmt].type == p_
             || g_Statement[stmt].type == e_
             || g_Statement[stmt].type == f_
           )) {
      nmbrLet(&mstring, g_Statement[stmt].mathString);
      mtkns = g_Statement[stmt].mathStringLen;
      for (mtkn = 0; mtkn < mtkns; mtkn++) {

        extractNeeded[g_MathToken[mstring[mtkn]].statement] = 'Y';
      }
    }
  }


  maxStmt = 0;
  for (stmt = g_statements; stmt >= 1; stmt--) {
    if (extractNeeded[stmt] == 'Y') {
      maxStmt = stmt;
      break;
    }
  }


  print2("Locating the $t statement if any...\n");
  dollarTStmt = 0;
  let(&dollarTCmt, "");
  for (stmt = 1; stmt <= g_statements + 1; stmt++) {

    tmpPtr = g_Statement[stmt].labelSectionPtr;
    j = g_Statement[stmt].labelSectionLen;
    zapChar = tmpPtr[j];
    tmpPtr[j] = 0;
    p1 = instr(1, tmpPtr, "$t");
    if (p1 != 0) {
      dollarTStmt = stmt;

      p2 = instr(p1, tmpPtr, "$)");
      let(&dollarTCmt, left(tmpPtr, p2 + 1));

      p1 = rinstr(dollarTCmt, "$(");

      p1--;
      while (p1 != 0) {
        if (dollarTCmt[p1 - 1] != ' ') break;
        p1--;
      }
      let(&dollarTCmt, cat("\n", seg(dollarTCmt, p1 + 1, p2 + 1), NULL));
    }
    tmpPtr[j] = zapChar;
    if (dollarTStmt != 0) {
      break;
    }
  }



  print2("Analyzing scopes of section headings...\n");
  let(&hugeHdrNeeded, string(g_statements + 2, 'N'));
  let(&bigHdrNeeded, string(g_statements + 2, 'N'));
  let(&smallHdrNeeded, string(g_statements + 2, 'N'));
  let(&tinyHdrNeeded, string(g_statements + 2, 'N'));

  for (stmt = 1; stmt <= maxStmt; stmt++) {
    getSectionHeadings(stmt, &hugeHdr, &bigHdr, &smallHdr,
        &tinyHdr,
        &hugeHdrComment, &bigHdrComment, &smallHdrComment,
        &tinyHdrComment,
        1,
        1 );
    if (hugeHdr[0] != 0) hugeHdrNeeded[stmt] = '?';
    if (bigHdr[0] != 0) bigHdrNeeded[stmt] = '?';
    if (smallHdr[0] != 0) smallHdrNeeded[stmt] = '?';
    if (tinyHdr[0] != 0) tinyHdrNeeded[stmt] = '?';
  }

  for (stmt = 1; stmt <= maxStmt; stmt++) {
    if (tinyHdrNeeded[stmt] == '?') {
      hdrNeeded = 0;
      for (stmtj = stmt; stmtj <= maxStmt; stmtj++) {
        if (hugeHdrNeeded[stmtj] != 'N' || bigHdrNeeded[stmtj] != 'N'
            || smallHdrNeeded[stmtj] != 'N' || tinyHdrNeeded[stmtj] != 'N') {
          if (stmtj > stmt) break;
        }
        if (extractNeeded[stmtj] == 'Y') {
          hdrNeeded = 1;

          break;
        }
      }
      if (hdrNeeded == 1) {
        tinyHdrNeeded[stmt] = 'Y';
      } else {
        tinyHdrNeeded[stmt] = 'N';
      }
    }
    if (smallHdrNeeded[stmt] == '?') {
      hdrNeeded = 0;
      for (stmtj = stmt; stmtj <= maxStmt; stmtj++) {
        if (hugeHdrNeeded[stmtj] != 'N' || bigHdrNeeded[stmtj] != 'N'
            || smallHdrNeeded[stmtj] != 'N') {
          if (stmtj > stmt) break;
        }
        if (extractNeeded[stmtj] == 'Y') {
          hdrNeeded = 1;

          break;
        }
      }
      if (hdrNeeded == 1) {
        smallHdrNeeded[stmt] = 'Y';
      } else {
        smallHdrNeeded[stmt] = 'N';
      }
    }
    if (bigHdrNeeded[stmt] == '?') {
      hdrNeeded = 0;

      for (stmtj = stmt; stmtj <= maxStmt; stmtj++) {
        if (hugeHdrNeeded[stmtj] != 'N' || bigHdrNeeded[stmtj] != 'N') {
          if (stmtj > stmt) break;
        }
        if (extractNeeded[stmtj] == 'Y') {
          hdrNeeded = 1;

          break;
        }
      }
      if (hdrNeeded == 1) {
        bigHdrNeeded[stmt] = 'Y';
      } else {
        bigHdrNeeded[stmt] = 'N';
      }
    }
    if (hugeHdrNeeded[stmt] == '?') {
      hdrNeeded = 0;
      for (stmtj = stmt; stmtj <= maxStmt; stmtj++) {
        if (hugeHdrNeeded[stmtj] != 'N') {
          if (stmtj > stmt) break;
        }
        if (extractNeeded[stmtj] == 'Y') {
          hdrNeeded = 1;

          break;
        }
      }
      if (hdrNeeded == 1) {
        hugeHdrNeeded[stmt] = 'Y';
      } else {
        hugeHdrNeeded[stmt] = 'N';
      }
    }
  }

  print2("Building $c and $v statements for unused math tokens...\n");
  let(&mathTokenDeclared, string(g_mathTokens, 'N'));
  for (stmt = 1; stmt <= g_statements; stmt++) {
    if (extractNeeded[stmt] == 'Y'
       && (g_Statement[stmt].type == c_
           || g_Statement[stmt].type == v_)) {
      mtkns = g_Statement[stmt].mathStringLen;
      for (mtkn = 0; mtkn < mtkns; mtkn++) {

        mathTokenDeclared[(g_Statement[stmt].mathString)[mtkn]] = 'Y';
      }
    }
  }
  let(&undeclaredC, "");
  let(&undeclaredV, "");
  for (mtkn = 0; mtkn < g_mathTokens; mtkn++) {
    if (mathTokenDeclared[mtkn] == 'N') {
      if (g_MathToken[mtkn].tokenType == con_) {
        let(&undeclaredC, cat(undeclaredC, " ", g_MathToken[mtkn].tokenName,
            NULL));
      } else {
        if (g_MathToken[mtkn].tokenType != var_) bug(271);
        p1 = 0;
        for (j = 0; j < g_mathTokens; j++) {
          if (j == mtkn) continue;
          if (!strcmp(g_MathToken[mtkn].tokenName, g_MathToken[j].tokenName)) {

            if (mathTokenDeclared[j] == 'Y') {
              p1 = 1;
              break;
            }
          }
        }
        if (p1 == 0) {
          let(&undeclaredV, cat(undeclaredV, " ", g_MathToken[mtkn].tokenName,
            NULL));
        }

        mathTokenDeclared[mtkn] = 'Y';
      }
    }
  }



  print2("Creating the final output file \"%s\"...\n", fullOutput_fn);

  fp = fSafeOpen(fullOutput_fn, "w", noVersioningFlag);
  if (fp == NULL) {
    print2("?Error trying to write \"%s\".\n", fp);
    goto EXTRACT_RETURN;
  }


  let(&buf, space(g_Statement[1].labelSectionLen));
  memcpy(buf, g_Statement[1].labelSectionPtr,
      (size_t)(g_Statement[1].labelSectionLen));
  let(&buf, left(buf, instr(1, buf, "\n") - 1));
  let(&buf, right(edit(buf, 8), 3));
  j = (long)strlen(" Extracted from: ");
  if (!strcmp(left(buf, j), " Extracted from: ")) {

    let(&buf, right(buf, j + 1));
  }
  fprintf(fp, "$( Extracted from: %s", buf);
  if (instr(1, buf, "$)") == 0) fprintf(fp, " $)");
  fprintf(fp,
      "\n$( Created %s %s using \"READ '%s'\" then\n",
      date(), time_(), g_input_fn);
  fprintf(fp,
      "   \"WRITE SOURCE '%s' /EXTRACT %s\" $)",
      fullOutput_fn, extractLabelList);

  extractedStmts = 0;
  for (stmt = 1; stmt <= g_statements + 1; stmt++) {
    if (extractNeeded[stmt] == 'Y') {
      let(&hdrSuffix, "");
    } else {
      let(&hdrSuffix, "\n");
    }

    if (hugeHdrNeeded[stmt] == 'Y'
        || bigHdrNeeded[stmt] == 'Y'
        || smallHdrNeeded[stmt] == 'Y'
        || tinyHdrNeeded[stmt] == 'Y') {
      getSectionHeadings(stmt, &hugeHdr, &bigHdr, &smallHdr, &tinyHdr,
          &hugeHdrComment, &bigHdrComment, &smallHdrComment,
          &tinyHdrComment,
          1,
          1
          );
      let(&buf, "");
      if (hugeHdrNeeded[stmt] == 'Y') {
        fixUndefinedLabels(extractNeeded, &hugeHdrComment);

        fprintf(fp, "%s", cat(hugeHdr, hugeHdrComment, hdrSuffix, NULL));
      }
      if (bigHdrNeeded[stmt] == 'Y') {
        fixUndefinedLabels(extractNeeded, &bigHdrComment);

        fprintf(fp, "%s", cat(bigHdr, bigHdrComment, hdrSuffix, NULL));
      }
      if (smallHdrNeeded[stmt] == 'Y') {
        fixUndefinedLabels(extractNeeded, &smallHdrComment);

        fprintf(fp, "%s", cat(smallHdr, smallHdrComment, hdrSuffix, NULL));
      }
      if (tinyHdrNeeded[stmt] == 'Y') {
        fixUndefinedLabels(extractNeeded, &tinyHdrComment);

        fprintf(fp, "%s", cat(tinyHdr, tinyHdrComment, hdrSuffix, NULL));
      }
    }


    if (dollarTStmt == stmt) {
      fprintf(fp, "\n%s", dollarTCmt);
    }


    if (extractNeeded[stmt] == 'Y') {
      let(&buf, "");
      buf = getDescriptionAndLabel(stmt);

      fixUndefinedLabels(extractNeeded, &buf);

      fprintf(fp, "%s", buf);
      if (stmt == g_statements + 1) bug(272);
      if (stmt != g_statements + 1) {
        extractedStmts++;
        fprintf(fp, "$%c", g_Statement[stmt].type);
        if (g_Statement[stmt].type != lb_ && g_Statement[stmt].type != rb_) {

          let(&buf, space(g_Statement[stmt].mathSectionLen));
          memcpy(buf, g_Statement[stmt].mathSectionPtr,
              (size_t)(g_Statement[stmt].mathSectionLen));
          fprintf(fp, "%s", buf);
          if (g_Statement[stmt].type != p_) {
            fprintf(fp, "$.");

          } else {

            fprintf(fp, "$=");
            let(&buf, space(g_Statement[stmt].proofSectionLen));
            memcpy(buf, g_Statement[stmt].proofSectionPtr,
                (size_t)(g_Statement[stmt].proofSectionLen));
            fprintf(fp, "%s$.", buf);
          }
        }
        if (extractNeeded[stmt + 1] == 'N') {

          fprintf(fp, "\n");
        }
      }

    }
  }


  if (g_outputToString == 1) bug(273);
  if (g_printString[0] != 0) bug(274);
  g_outputToString = 1;
  if (undeclaredC[0] != 0) {
    print2("\n");
    print2(
"  $( Unused constants to satisfy the htmldef's in the $ t comment. $)\n");
    printLongLine(cat("  $c", undeclaredC, " $.", NULL), "    ", " ");
  }
  if (undeclaredV[0] != 0) {
    print2("\n");
    print2(
"  $( Unused variables to satisfy the htmldef's in the $ t comment. $)\n");
    printLongLine(cat("  $v", undeclaredV, " $.", NULL), "    ", " ");
  }
  g_outputToString = 0;
  if (g_printString[0] != 0) {
    fprintf(fp, "%s", g_printString);
    let(&g_printString, "");
  }


  fclose(fp);


  j = 0; p1 = 0; p2 = 0; p3 = 0; p4 = 0;
  for (stmt = 1; stmt <= g_statements; stmt++) {
    if (extractNeeded[stmt] == 'Y') {
      j++;
      if (g_Statement[stmt].type == a_) {
        p1++;
        if (!strcmp("ax-", left(g_Statement[stmt].labelName, 3))) p3++;
        if (!strcmp("df-", left(g_Statement[stmt].labelName, 3))) p4++;
        let(&buf, "");
      }
      if (g_Statement[stmt].type == p_) p2++;
    }
  }
  print2(
"Extracted %ld statements incl. %ld $a (%ld \"ax-\", %ld \"df-\"), %ld $p.\n",
      j, p1, p3, p4, p2);

 EXTRACT_RETURN:

  let(&extractNeeded, "");
  let(&statementUsedFlags, "");
  nmbrLet(&unprovedList, NULL_NMBRSTRING);
  nmbrLet(&mstring, NULL_NMBRSTRING);
  let(&dollarTCmt, "");
  let(&hugeHdrNeeded, "");
  let(&bigHdrNeeded, "");
  let(&smallHdrNeeded, "");
  let(&tinyHdrNeeded, "");
  let(&hugeHdr, "");
  let(&bigHdr, "");
  let(&smallHdr, "");
  let(&tinyHdr, "");
  let(&hugeHdrComment, "");
  let(&bigHdrComment, "");
  let(&smallHdrComment, "");
  let(&tinyHdrComment, "");
  let(&mathTokenDeclared, "");
  let(&undeclaredC, "");
  let(&undeclaredV, "");
  let(&buf, "");
  return;
}



void fixUndefinedLabels(vstring extractNeeded,
    vstring *buf) {
  long p1, p2, p3;
  vstring label = "";
  vstring newLabelWithTilde = "";
  vstring restOfComment = "";
  int mathMode;
  static final long   ASCII_4 =D.ASCII_4;


  p1 = (long)strlen(*buf);
  mathMode = 0;
  for (p2 = 0; p2 < p1; p2++) {
    if ((*buf)[p2] == '`') {
      mathMode = 1 - mathMode;
      continue;
    }
    if ((*buf)[p2] == '~' && mathMode == 1) {
      (*buf)[p2] = ASCII_4;
    }
  }

  p1 = 0;
  let(&(*buf), cat(*buf, " \n", NULL));
  while (1) {
    p1 = instr(p1 + 1, *buf, "~");
    if (p1 == 0) break;
    if (p1 - 2 >= 0) {
      if ((*buf)[p1 - 2] == '~') {
        continue;
      }
    }
    while (1) {

      if ((*buf)[p1 + 1] == 0) break;
      if ((*buf)[p1 + 1] != ' ' && (*buf)[p1 + 1] != '\n') {

        break;
      }
      p1++;
    }
    if ((*buf)[p1 + 1] == 0) break;
    p2 = instr(p1 + 2, *buf, " ");
    p3 = instr(p1 + 2, *buf, "\n");
    if (p3 < p2) p2 = p3;
    let(&label, seg(*buf, p1 + 2, p2 - 1));
    let(&restOfComment, right(*buf, p2));
    p3 = lookupLabel(label);
    if (p3 == -1) continue;
    if (extractNeeded[p3] == 'Y') continue;

    let(&newLabelWithTilde, cat(label,
        "\n ~ http://us.metamath.org/mpeuni/",
        label, ".html", NULL));
    let(&(*buf), cat(left(*buf, p1 - 1), newLabelWithTilde, NULL));

    p1 = p1 + (long)strlen(newLabelWithTilde)
          - ((long)strlen(label) + 2);


    if (restOfComment[0] == '\n') {
      let(&(*buf), cat(*buf, restOfComment, NULL));
    } else {
      let(&(*buf), cat(*buf, "\n", restOfComment, NULL));
    }
  }
  let(&(*buf), left(*buf, (long)strlen(*buf) - 2));




  p1 = (long)strlen(*buf);
  for (p2 = 0; p2 < p1; p2++) {
    if ((*buf)[p2] == ASCII_4) (*buf)[p2] = '~';
  }

  let(&label, "");
  let(&newLabelWithTilde, "");
  let(&restOfComment, "");
  return;
}


void writeDict(void)
{
  print2("This function has not been implemented yet.\n");
  return;
}


void eraseSource(void)
{
  long i;
  vstring tmpStr = "";




  if (g_wrkProofMaxSize) {
    free(g_WrkProof.tokenSrcPtrNmbr);
    free(g_WrkProof.tokenSrcPtrPntr);
    free(g_WrkProof.stepSrcPtrNmbr);
    free(g_WrkProof.stepSrcPtrPntr);
    free(g_WrkProof.localLabelFlag);
    free(g_WrkProof.hypAndLocLabel);
    free(g_WrkProof.localLabelPool);
    poolFree(g_WrkProof.proofString);
    free(g_WrkProof.mathStringPtrs);
    free(g_WrkProof.RPNStack);
    free(g_WrkProof.compressedPfLabelMap);
    g_wrkProofMaxSize = 0;
  }

  if (g_statements == 0) {

    memFreePoolPurge(0);
    return;
  }

  for (i = 0; i <= g_includeCalls; i++) {
    let(&g_IncludeCall[i].source_fn, "");
    let(&g_IncludeCall[i].included_fn, "");
    let(&g_IncludeCall[i].current_includeSource, "");
  }
  g_includeCalls = -1;


  for (i = 1; i <= g_statements + 1; i++) {

    if (g_Statement[i].labelName[0]) free(g_Statement[i].labelName);

    if (g_Statement[i].mathString != NULL_NMBRSTRING)
        poolFree(g_Statement[i].mathString);

    if (g_Statement[i].proofString != NULL_NMBRSTRING)
        poolFree(g_Statement[i].proofString);
    if (g_Statement[i].reqHypList != NULL_NMBRSTRING)
        poolFree(g_Statement[i].reqHypList);

    if (g_Statement[i].optHypList != NULL_NMBRSTRING)
        poolFree(g_Statement[i].optHypList);
    if (g_Statement[i].reqVarList != NULL_NMBRSTRING)
        poolFree(g_Statement[i].reqVarList);

    if (g_Statement[i].optVarList != NULL_NMBRSTRING)
        poolFree(g_Statement[i].optVarList);
    if (g_Statement[i].reqDisjVarsA != NULL_NMBRSTRING)
        poolFree(g_Statement[i].reqDisjVarsA);
    if (g_Statement[i].reqDisjVarsB != NULL_NMBRSTRING)
        poolFree(g_Statement[i].reqDisjVarsB);
    if (g_Statement[i].reqDisjVarsStmt != NULL_NMBRSTRING)
        poolFree(g_Statement[i].reqDisjVarsStmt);
    if (g_Statement[i].optDisjVarsA != NULL_NMBRSTRING)
        poolFree(g_Statement[i].optDisjVarsA);
    if (g_Statement[i].optDisjVarsB != NULL_NMBRSTRING)
        poolFree(g_Statement[i].optDisjVarsB);
    if (g_Statement[i].optDisjVarsStmt != NULL_NMBRSTRING)
        poolFree(g_Statement[i].optDisjVarsStmt);



    if (g_Statement[i].labelSectionChanged == 1) {

      let(&(g_Statement[i].labelSectionPtr), "");
    }
    if (g_Statement[i].mathSectionChanged == 1) {

      let(&(g_Statement[i].mathSectionPtr), "");
    }
    if (g_Statement[i].proofSectionChanged == 1) {

      let(&(g_Statement[i].proofSectionPtr), "");
    }

  }

  for (i = 0; i <= g_mathTokens + g_dummyVars; i++) {
    let(&(g_MathToken[i].tokenName), "");
  }

  memFreePoolPurge(0);

  g_errorCount = 0;

  free(g_Statement);
  free(g_IncludeCall);
  free(g_MathToken);
  g_dummyVars = 0;
  free(g_sourcePtr);
  free(g_labelKey);
  free(g_mathKey);
  free(g_allLabelKeyBase);





  eraseTexDefs();

  g_extHtmlStmt = 0;



  g_mathboxStmt = 0;
  nmbrLet(&g_mathboxStart, NULL_NMBRSTRING);
  nmbrLet(&g_mathboxEnd, NULL_NMBRSTRING);
  for (i = 1; i <= g_mathboxes; i++) {
    let((vstring *)(&g_mathboxUser[i - 1]), "");
  }
  pntrLet(&g_mathboxUser, NULL_PNTRSTRING);
  g_mathboxes = 0;


  initBigArrays();

  g_bracketMatchInit = 0;
  g_minSubstLen = 1;

  nmbrLet(&g_firstConst, NULL_NMBRSTRING);
  nmbrLet(&g_lastConst, NULL_NMBRSTRING);
  nmbrLet(&g_oneConst, NULL_NMBRSTRING);


  getMarkupFlag(0, RESET);




  let(&tmpStr, "");
  tmpStr = getContrib(0 , GC_RESET);



  g_statements = 0;

}


void verifyProofs(vstring labelMatch, flag verifyFlag) {
  vstring emptyProofList = "";
  long i, k;
  long lineLen = 0;
  vstring header = "";
  flag errorFound;
#ifdef CLOCKS_PER_SEC
  clock_t clockStart;
#endif





#ifdef CLOCKS_PER_SEC
  clockStart = clock();
#endif
  if (!strcmp("*", labelMatch) && verifyFlag) {

    let(&header, "0 10%  20%  30%  40%  50%  60%  70%  80%  90% 100%");
#ifdef XXX


    for (i = 1; i <= (long)strlen(header); i++) {
      let(&tmpStr, mid(header, i, 1));
      if (tmpStr[0] == '%') let(&tmpStr, "%%");
      print2("%s", tmpStr);
    }
    print2("\n");
#else
#ifdef XXX


    for (i = 1; i <= (long)strlen(header); i++) {
      let(&tmpStr, mid(header, i, 1));
      if (tmpStr[0] == '%') let(&tmpStr, "%%");
      print2("%s", tmpStr);
    }
    print2("\n");
#else
    print2("%s\n", header);
    let(&header, "");
#endif
#endif
  }

  errorFound = 0;
  for (i = 1; i <= g_statements; i++) {
    if (!strcmp("*", labelMatch) && verifyFlag) {
      while (lineLen < (50 * i) / g_statements) {
        print2(".");
        lineLen++;
      }
    }

    if (g_Statement[i].type != p_) continue;

    if (!matchesList(g_Statement[i].labelName, labelMatch, '*', '?')) continue;
    if (strcmp("*",labelMatch) && verifyFlag) {

      lineLen = lineLen + (long)strlen(g_Statement[i].labelName) + 1;
      if (lineLen > 72) {
        lineLen = (long)strlen(g_Statement[i].labelName) + 1;
        print2("\n");
      }
      print2("%s ",g_Statement[i].labelName);
    }

    k = parseProof(i);
    if (k >= 2) errorFound = 1;
    if (k < 2) {
      if (verifyFlag) {
        if (verifyProof(i) >= 2) errorFound = 1;
        cleanWrkProof();
      }
    }
    if (k == 1) {
      let(&emptyProofList, cat(emptyProofList, ", ", g_Statement[i].labelName,
          NULL));
    }
  }
  if (verifyFlag) {
    print2("\n");
  }

  if (emptyProofList[0]) {
    printLongLine(cat(
        "Warning: The following $p statement(s) were not proved:  ",
        right(emptyProofList,3), NULL)," ","  ");
  }
  if (!emptyProofList[0] && !errorFound && !strcmp("*", labelMatch)) {
    if (verifyFlag) {
#ifdef CLOCKS_PER_SEC
      print2("All proofs in the database were verified in %1.2f s.\n",
           (double)((1.0 * (double)(clock() - clockStart)) / CLOCKS_PER_SEC));
#else
      print2("All proofs in the database were verified.\n");
#endif

    } else {
      print2("All proofs in the database passed the syntax-only check.\n");
    }
  }
  let(&emptyProofList, "");

}







void verifyMarkup(vstring labelMatch,
    flag dateSkip,
    flag topDateSkip,
    flag fileSkip,
    flag underscoreSkip,
    flag mathboxSkip,
    flag verboseMode)  {
  flag f;
  flag saveHtmlFlag, saveAltHtmlFlag;
  flag errFound = 0;
  long stmtNum, p1, p2, p3;
  long flen, lnum, lstart;


  vstring mmVersionDate = "";
  vstring mostRecentDate = "";
  long mostRecentStmt = 0;

  vstring hugeHdr = "";
  vstring bigHdr = "";
  vstring smallHdr = "";
  vstring tinyHdr = "";
  vstring hugeHdrComment = "";
  vstring bigHdrComment = "";
  vstring smallHdrComment = "";
  vstring tinyHdrComment = "";

  vstring descr = "";
  vstring str1 = ""; vstring str2 = "";

  long mbox, pmbox, stmt, pstmt, plen, step;
  nmbrString *proof = NULL_NMBRSTRING;
  vstring dupCheck = "";

  saveHtmlFlag = g_htmlFlag;  saveAltHtmlFlag = g_altHtmlFlag;

  print2("Checking statement label conventions...\n");




  let(&str1, cat(
     ",CON,PRN,AUX,NUL,COM1,COM2,COM3,COM4,COM5,COM6,COM7,",
     "COM8,COM9,LPT1,LPT2,LPT3,LPT4,LPT5,LPT6,LPT7,LPT8,LPT9,", NULL));
  for (stmtNum = 1; stmtNum <= g_statements; stmtNum++) {
    if (!matchesList(g_Statement[stmtNum].labelName, labelMatch, '*', '?')) {
      continue;
    }




    if (underscoreSkip == 0
        && instr(1, g_Statement[stmtNum].labelName, "_") != 0) {
      assignStmtFileAndLineNum(stmtNum);
      printLongLine(cat("?Warning: In statement \"",
          g_Statement[stmtNum].labelName, "\" at line ",
          str((double)(g_Statement[stmtNum].lineNum)),
          " in file \"", g_Statement[stmtNum].fileName,
          "\".  Underscores in labels are not recommended per our conventions.  ",
          "Use the / UNDERSCORE_SKIP ",
          "qualifier to skip this check.",
          NULL),
          "    ", " ");

      errFound = 1;
    }


    if (g_Statement[stmtNum].type != a_ && g_Statement[stmtNum].type != p_) {
      continue;
    }
    let(&str2, cat(",", edit(g_Statement[stmtNum].labelName, 32),
        ",", NULL));
    if (instr(1, str1, str2) ||

        !strcmp(",MM", left(str2, 3))) {

      assignStmtFileAndLineNum(stmtNum);
      printLongLine(cat("?Warning: In statement \"",
          g_Statement[stmtNum].labelName, "\" at line ",
          str((double)(g_Statement[stmtNum].lineNum)),
          " in file \"", g_Statement[stmtNum].fileName,
          "\".  To workaround a Microsoft operating system limitation, the",
          " the following reserved words cannot be used for label names:",
          " CON, PRN, AUX, CLOCK$, NUL, COM1, COM2, COM3, COM4, COM5,",
          " COM6, COM7, COM8, COM9, LPT1, LPT2, LPT3, LPT4, LPT5, LPT6,",
          " LPT7, LPT8, and LPT9.  Also, \"mm*.html\" is reserved for",
          " Metamath file names.  Use another name for this label.", NULL),
          "    ", " ");

      errFound = 1;
    }


    if (g_Statement[stmtNum].type == (char)a_) {
      if (!strcmp("|-", g_MathToken[
          (g_Statement[stmtNum].mathString)[0]].tokenName)) {
        let(&str1, left(g_Statement[stmtNum].labelName, 3));
        if (strcmp("ax-", str1) && strcmp("df-", str1)) {
          assignStmtFileAndLineNum(stmtNum);
          printLongLine(cat("?Warning: In the $a statement \"",
              g_Statement[stmtNum].labelName, "\" at line ",
              str((double)(g_Statement[stmtNum].lineNum)),
              " in file \"", g_Statement[stmtNum].fileName,
              "\", the label does not start with \"ax-\" or \"df-\"",
              " per our convention for axiomatic assertions (\"$a |- ...\").",
              NULL), "    ", " ");
          errFound = 1;
        }
      }
    }


  }








  for (p1 = 0; p1 < g_mathTokens; p1++) {
    if (strchr(g_MathToken[p1].tokenName, '@') != NULL) {
      stmtNum = g_MathToken[p1].statement;
      assignStmtFileAndLineNum(stmtNum);
      printLongLine(cat("?Warning: The token \"",
          g_MathToken[p1].tokenName,
          "\" declared at line ",
          str((double)(g_Statement[stmtNum].lineNum)),
          " in file \"", g_Statement[stmtNum].fileName,
          "\" has an \"@\" character, which is discouraged because ",
          "\"@\" is traditionally used to replace \"$\" in commented-out ",
          "database source code.",
          NULL),
          "    ", " ");

      errFound = 1;
    }
    if (strchr(g_MathToken[p1].tokenName, '?') != NULL) {
      stmtNum = g_MathToken[p1].statement;
      assignStmtFileAndLineNum(stmtNum);
      printLongLine(cat("?Warning: The token \"",
          g_MathToken[p1].tokenName,
          "\" declared at line ",
          str((double)(g_Statement[stmtNum].lineNum)),
          " in file \"", g_Statement[stmtNum].fileName,
          "\" has a \"?\" character, which is discouraged because ",
          "\"?\" is sometimes used as a math token search wildcard.",
          NULL),
          "    ", " ");

      errFound = 1;
    }
  }






  for (stmtNum = 1; stmtNum <= g_statements; stmtNum++) {
    if (g_Statement[stmtNum].type != a_) {
      continue;
    }
    if (!matchesList(g_Statement[stmtNum].labelName, labelMatch, '*', '?')) {
      continue;
    }

    let(&str1, "");

    if (strcmp("ax-", left(g_Statement[stmtNum].labelName, 3))) {
      continue;
    }

    let(&str1, g_Statement[stmtNum].labelName);

    let(&str2, cat(left(str1, 2), right(str1, 4), NULL));

    p1 = lookupLabel(str2);
    if (p1 == -1) continue;


    if (verboseMode == 1) {
      print2("Comparing \"%s\" to \"%s\"...\n", str2, str1);
    }



    if (nmbrEq(g_Statement[stmtNum].mathString,
        g_Statement[p1].mathString) != 1) {
      printLongLine(cat("?Warning: The assertions for statements \"",
          g_Statement[stmtNum].labelName, "\" and \"",
          g_Statement[p1].labelName, "\" are different.",
          NULL), "  ", " ");
      errFound = 1;
      continue;
    }


    if (g_Statement[stmtNum].numReqHyp != g_Statement[p1].numReqHyp) {
      printLongLine(cat("?Warning: Statement \"",
          g_Statement[stmtNum].labelName, "\" has ",
          str((double)(g_Statement[stmtNum].numReqHyp)),
          " mandatory hypotheses but \"",
          g_Statement[p1].labelName, "\" has ",
          str((double)(g_Statement[p1].numReqHyp)), ".",
          NULL), "  ", " ");
      errFound = 1;
      continue;
    }


    if (nmbrEq(g_Statement[stmtNum].reqDisjVarsA,
        g_Statement[p1].reqDisjVarsA) != 1) {
      printLongLine(cat(
          "?Warning: The mandatory distinct variable pairs for statements \"",
          g_Statement[stmtNum].labelName, "\" and \"",
          g_Statement[p1].labelName,
          "\" are different or have a different order.",
          NULL), "  ", " ");
      errFound = 1;
      continue;
    } else if (nmbrEq(g_Statement[stmtNum].reqDisjVarsB,
        g_Statement[p1].reqDisjVarsB) != 1) {
      printLongLine(cat(
          "?Warning: The mandatory distinct variable pairs for statements \"",
          g_Statement[stmtNum].labelName, "\" and \"",
          g_Statement[p1].labelName,
          "\" are different or have a different order.",
          NULL), "  ", " ");
      errFound = 1;
      continue;
    }


    for (p2 = 0; p2 < g_Statement[stmtNum].numReqHyp; p2++) {
      if (nmbrEq(g_Statement[(g_Statement[stmtNum].reqHypList)[p2]].mathString,
          g_Statement[(g_Statement[p1].reqHypList)[p2]].mathString) != 1) {
        printLongLine(cat("?Warning: The mandatory hypotheses of statements \"",
            g_Statement[stmtNum].labelName, "\" and \"",
            g_Statement[p1].labelName, "\" are different.",
            NULL), "  ", " ");
        errFound = 1;
        break;
      }
    }

    for (p2 = 0; p2 < nmbrLen(g_Statement[stmtNum].reqDisjVarsA); p2++) {
      if (nmbrEq(g_Statement[stmtNum].reqDisjVarsA,
          g_Statement[p1].reqDisjVarsA) != 1) {
        printLongLine(cat("?Warning: The mandatory hypotheses of statements \"",
            g_Statement[stmtNum].labelName, "\" and \"",
            g_Statement[p1].labelName, "\" are different.",
            NULL), "  ", " ");
        errFound = 1;
        break;
      }
    }
  }






  let(&str1, "");
  let(&str2, "");
  if (g_statements >= 0

      ) {
    str1 = g_Statement[1].labelSectionPtr;
    str2 = g_Statement[g_statements + 1].labelSectionPtr
       + g_Statement[g_statements + 1].labelSectionLen;

    if (str2[0] != 0) bug(258);
    if (str2[-1] != '\n') bug(259);
    flen = str2 - str1;
    str2 = "";
    lnum = 0;
    lstart = 0;
    for (p1 = 0; p1 < flen; p1++) {
      if (str1[p1] == 0) {

        bug(260);
      }
      if (str1[p1] == '\n') {

        lnum++;


        if (p1 > 0) {
          if (str1[p1 - 1] == ' ') {
            printLongLine(cat("?Warning: Line number ",
                str((double)lnum),
                " ends with a space character, which is discouraged.",
                NULL), "    ", " ");
            errFound = 1;
          }
        }

        if (p1 - lstart > g_screenWidth) {

          let(&str2, space(p1 - lstart));
          memcpy(str2, str1 + lstart,
            (size_t)(p1 - lstart));
          printLongLine(cat("?Warning: Line number ",
              str((double)lnum),
              " has ",
              str((double)(p1 - lstart)),
              " characters (should have ",
              str((double)g_screenWidth),
              " or less):",
              NULL), "    ", " ");
          print2("    %s...\n", left(str2, g_screenWidth - 7));
          errFound = 1;
          let(&str2, "");
        }
        lstart = p1 + 1;
      }



      if (str1[p1] == '\t') {
        printLongLine(cat("?Warning: Line number ",
            str((double)lnum + 1),
            " contains a tab, which is discouraged.",
            NULL), "    ", " ");
        errFound = 1;
      }

    }
    str1 = "";
  }




  eraseTexDefs();

  print2("Checking latexdef, htmldef, althtmldef...\n");
  g_htmlFlag = 0;
  g_altHtmlFlag = 0;
  f = readTexDefs(1,
          fileSkip   );
  if (f != 0) errFound = 1;
  if (f != 2) {


    g_htmlFlag = 1;
    g_altHtmlFlag = 0;
    f = readTexDefs(1,
            fileSkip   );
  }
  if (f != 0) errFound = 1;
  if (f != 2) {


    g_htmlFlag = 1;
    g_altHtmlFlag = 1;
    f = readTexDefs(1,
            fileSkip   );
  }
  if (f != 0) errFound = 1;



  print2("Checking statement comments...\n");
  let(&mostRecentDate, "");
  for (stmtNum = 1; stmtNum <= g_statements; stmtNum++) {
    if (g_Statement[stmtNum].type != a_ && g_Statement[stmtNum].type != p_) {
      continue;
    }
    if (!matchesList(g_Statement[stmtNum].labelName, labelMatch, '*', '?')) {
      continue;
    }



    let(&str1, "");
    str1 = getContrib(stmtNum, CONTRIBUTOR);
    if (!strcmp(str1, DEFAULT_CONTRIBUTOR)) {
      printLongLine(cat(
          "?Warning: Contributor \"", DEFAULT_CONTRIBUTOR,
          "\" should be updated in statement \"",
          g_Statement[stmtNum].labelName, "\".", NULL), "    ", " ");
      errFound = 1;
    }

    let(&str1, "");
    str1 = getContrib(stmtNum, REVISER);
    if (!strcmp(str1, DEFAULT_CONTRIBUTOR)) {
      printLongLine(cat(
          "?Warning: Reviser \"", DEFAULT_CONTRIBUTOR,
          "\" should be updated in statement \"",
          g_Statement[stmtNum].labelName, "\".", NULL), "    ", " ");
      errFound = 1;
    }

    if (dateSkip == 0) {




      let(&str1, "");
      str1 = getContrib(stmtNum, GC_ERROR_CHECK_PRINT);
      if (str1[0] == 'F') errFound = 1;
      let(&str1, "");
      str1 = getContrib(stmtNum, MOST_RECENT_DATE);



      if (compareDates(mostRecentDate, str1) == -1) {
        let(&mostRecentDate, str1);
        mostRecentStmt = stmtNum;
      }

    }

    let(&descr, "");
    descr = getDescription(stmtNum);



    g_showStatement  = stmtNum;
    g_texFilePtr   = NULL;

    f = printTexComment(descr,
        0,
        PROCESS_EVERYTHING + ERRORS_ONLY,
        fileSkip );
    if (f == 1) errFound = 1;



    if (g_Statement[stmtNum].type == a_) {
      if (getMarkupFlag(stmtNum, PROOF_DISCOURAGED) == 1) {
        printLongLine(cat(
            "?Warning: Statement \"", g_Statement[stmtNum].labelName,
            "\" is a $a but has a \"(Proof modification is discouraged.)\" tag.",
            NULL), "    ", " ");
        errFound = 1;
      }
    }

    p1 = (long)strlen(g_Statement[stmtNum].labelName);
    let(&str1, right(g_Statement[stmtNum].labelName, p1 - 2));
    if (!strcmp(str1, "OLD") || !strcmp(str1, "ALT")) {
      if (getMarkupFlag(stmtNum, PROOF_DISCOURAGED) != 1
          && g_Statement[stmtNum].type == p_
          ) {
        printLongLine(cat(
            "?Warning: Statement \"", g_Statement[stmtNum].labelName,
            "\" has suffix \"", str1,
            "\" but has no \"(Proof modification is discouraged.)\" tag.",
            NULL), "    ", " ");
        errFound = 1;
      }
      if (getMarkupFlag(stmtNum, USAGE_DISCOURAGED) != 1) {
        printLongLine(cat(
            "?Warning: Statement \"", g_Statement[stmtNum].labelName,
            "\" has suffix \"", str1,
            "\" but has no \"(New usage is discouraged.)\" tag.",
            NULL), "    ", " ");
        errFound = 1;
      }
    }
  }



  if (dateSkip == 0  && topDateSkip == 0) {

    let(&str1, space(g_Statement[1].labelSectionLen));
    memcpy(str1, g_Statement[1].labelSectionPtr,
      (size_t)(g_Statement[1].labelSectionLen));

    p1 = instr(1, str1, "Version of ");
    if (p1 == 0) {
      printLongLine(cat(
          "?Warning: There is no \"Version of \" comment at the top of the",
          " file \"", g_input_fn, "\".", NULL), "    ", " ");
      errFound = 1;
    } else {
      p2 = instr(p1 + 11, str1, " ");
      let(&str2, seg(str1, p1 + 11, p2 - 1));
      f = parseDate(str2, &p1, &p2, &p3);
      if (f == 1) {
        printLongLine(cat(
            "?Warning: The Version date \"", str2, "\" at the top of file \"",
            g_input_fn, "\" is not a valid date.", NULL), "    ", " ");
        errFound = 1;
      } else {
        if (compareDates(mostRecentDate, str2) == 1) {
          printLongLine(cat(
              "?Warning: The \"Version of\" date ", str2,
              " at the top of file \"",
              g_input_fn,
              "\" is less recent than the date ", mostRecentDate,
              " in the description of statement \"",
              g_Statement[mostRecentStmt].labelName, "\".", NULL), "    ", " ");
          errFound = 1;
        }
      }
    }
  }



  print2("Checking section header comments...\n");
  for (stmtNum = 1; stmtNum <= g_statements; stmtNum++) {
    if (g_Statement[stmtNum].type != a_ && g_Statement[stmtNum].type != p_) {
      continue;
    }
    if (!matchesList(g_Statement[stmtNum].labelName, labelMatch, '*', '?')) {
      continue;
    }

    let(&hugeHdr, "");
    let(&bigHdr, "");
    let(&smallHdr, "");
    let(&tinyHdr, "");
    let(&hugeHdrComment, "");
    let(&bigHdrComment, "");
    let(&smallHdrComment, "");
    let(&tinyHdrComment, "");
    f = getSectionHeadings(stmtNum, &hugeHdr, &bigHdr, &smallHdr,
        &tinyHdr,

        &hugeHdrComment, &bigHdrComment, &smallHdrComment,
        &tinyHdrComment,
        0,
        0 );
    if (f != 0) errFound = 1;

    g_showStatement  = stmtNum;
    g_texFilePtr   = NULL;

    f = 0;
    if (hugeHdrComment[0] != 0)
      f = (char)(f + printTexComment(hugeHdrComment,
          0,
          PROCESS_EVERYTHING + ERRORS_ONLY,
          fileSkip ));
    if (bigHdrComment[0] != 0)
      f = (char)(f + printTexComment(bigHdrComment,
          0,
          PROCESS_EVERYTHING + ERRORS_ONLY,
          fileSkip ));
    if (smallHdrComment[0] != 0)
      f = (char)(f + printTexComment(smallHdrComment,
          0,
          PROCESS_EVERYTHING + ERRORS_ONLY,
          fileSkip ));


    if (tinyHdrComment[0] != 0)
      f = (char)(f + printTexComment(tinyHdrComment,
          0,
          PROCESS_EVERYTHING + ERRORS_ONLY,
          fileSkip ));


    if (f != 0) printf(
"    (The warning above refers to a header above the referenced statement.)\n");
    if (f != 0) errFound = 1;
  }



  print2("Checking bibliographic references...\n");
  f = writeBibliography("mmbiblio.html",
          labelMatch,
          1,
          fileSkip);
  if (f != 0) errFound = 1;



  if (mathboxSkip == 0) {
    print2("Checking mathbox independence...\n");


    assignMathboxInfo();

    for (pmbox = 2; pmbox <= g_mathboxes; pmbox++) {

      for (pstmt = g_mathboxStart[pmbox - 1]; pstmt <= g_mathboxEnd[pmbox - 1];
          pstmt++) {
        if (g_Statement[pstmt].type != (char)p_)
          continue;

        if (parseProof(pstmt) > 1) {

          nmbrLet(&proof, nmbrAddElement(NULL_NMBRSTRING, -(long)'?'));
        } else {
          nmbrLet(&proof, g_WrkProof.proofString);
        }
        plen = nmbrLen(proof);

        for (step = 0; step < plen; step++) {
          stmt = proof[step];
          if (stmt < 0) continue;
          if (stmt == 0) bug(266);
          if (stmt > g_mathboxStmt && stmt < g_mathboxStart[pmbox - 1]) {



            let(&str1, cat(str((double)pstmt), "-", str((double)stmt), NULL));
            if (lookup(str1, dupCheck) != 0) {
              continue;
            } else {
              let(&dupCheck, cat(dupCheck,
                  (dupCheck[0] == 0) ? "" : ",", str1, NULL));
            }

            mbox = getMathboxNum(stmt);
            if (mbox == 0) bug(267);
            if (verboseMode == 0) {
              printLongLine(cat("?Warning: The proof of \"",
                  g_Statement[pstmt].labelName,
                  "\" in the mathbox for ", (vstring *)(g_mathboxUser[pmbox - 1]),
                  " references \"", g_Statement[stmt].labelName,
                  "\" in the mathbox for ", (vstring *)(g_mathboxUser[mbox - 1]),
                  ".",
                  NULL),
                  "    ", " ");
            } else {

              assignStmtFileAndLineNum(stmt);
              assignStmtFileAndLineNum(pstmt);
              printLongLine(cat("?Warning: The proof of statement \"",
                  g_Statement[pstmt].labelName,
                  "\" in the mathbox for \"", (vstring *)(g_mathboxUser[pmbox - 1]),
                  "\" at line ", str((double)(g_Statement[pstmt].lineNum)),
                  " in file \"", g_Statement[pstmt].fileName,
                  "\" references statement \"", g_Statement[stmt].labelName,
                  "\" in the mathbox for \"", (vstring *)(g_mathboxUser[mbox - 1]),
                  "\" at line ", str((double)(g_Statement[stmt].lineNum)),
                  " in file \"", g_Statement[stmt].fileName,
                  "\".  ",
                  "(Use the / MATHBOX_SKIP qualifier to skip this check.)",
                  NULL),
                  "    ", " ");
            }

            errFound = 1;
          }
        }
      }
    }

    let(&dupCheck, "");
    nmbrLet(&proof, NULL_NMBRSTRING);
  }

  if (errFound == 0) {
    print2("No errors were found.\n");
  }

  g_htmlFlag = saveHtmlFlag;  g_altHtmlFlag = saveAltHtmlFlag;

  eraseTexDefs();


  let(&mostRecentDate, "");
  let(&mmVersionDate, "");
  let(&descr, "");
  let(&str1, "");
  let(&str2, "");


  let(&hugeHdr, "");
  let(&bigHdr, "");
  let(&smallHdr, "");
  let(&tinyHdr, "");
  let(&hugeHdrComment, "");
  let(&bigHdrComment, "");
  let(&smallHdrComment, "");
  let(&tinyHdrComment, "");


  return;

}




void processMarkup(vstring inputFileName, vstring outputFileName,
    flag processCss, long actionBits) {
  File outputFilePtr;
  vstring inputFileContent = "";
  long size;
  long p;


  if (g_outputToString == 1 || g_printString[0] != 0) {
    bug(265);
  }


  if (2 == readTexDefs(0 ,
      1   )) {
    goto PROCESS_MARKUP_RETURN;
  }

  print2("Reading \"%s\"...\n", inputFileName);

  let(&inputFileContent, "");
  inputFileContent = readFileToString(inputFileName, 1, &size);
  if (inputFileContent == NULL) {

    inputFileContent = "";
    goto PROCESS_MARKUP_RETURN;
  }

  print2("Creating \"%s\"...\n", outputFileName);


  if (processCss != 0 && instr(1, inputFileContent, g_htmlCSS) == 0) {
    p = instr(1, edit(inputFileContent, 32), "</HEAD>");
    if (p != 0) {
      let(&inputFileContent, cat(left(inputFileContent, p - 1),
          g_htmlCSS, "\n", right(inputFileContent, p), NULL));
    }
  }

  outputFilePtr = fSafeOpen(outputFileName, "w", 0);
  if (outputFilePtr == NULL) {


    goto PROCESS_MARKUP_RETURN;
  }

  g_outputToString = 0;
  let(&g_printString, "");
  g_showStatement = 0;
  g_texFilePtr = outputFilePtr;
  printTexComment(
      inputFileContent,
      0,
      actionBits,
      0 );
  fclose(g_texFilePtr);
  g_texFilePtr = NULL;

 PROCESS_MARKUP_RETURN:

  let(&inputFileContent, "");
  let(&g_printString, "");
  return;
}



void showDiscouraged(void) {
  long stmt, s, usageCount;
  long lowStmt = 0, highStmt = 0;
  flag notQuitPrint = 1;
  vstring str1 = "";
  for (stmt = 1; stmt <= g_statements; stmt++) {

    if (notQuitPrint == 0) break;

    if (g_Statement[stmt].type != p_ && g_Statement[stmt].type != a_) continue;
    if (getMarkupFlag(stmt, PROOF_DISCOURAGED) == 1
        && g_Statement[stmt].type == p_
        ) {


      parseProof(stmt);
      notQuitPrint = print2(
"SHOW DISCOURAGED:  Proof modification of \"%s\" is discouraged (%ld steps).\n",
          g_Statement[stmt].labelName,
          nmbrLen(g_WrkProof.proofString));
    }
    if (getMarkupFlag(stmt, USAGE_DISCOURAGED) == 1) {

      usageCount = 0;
      let(&str1, "");
      str1 = traceUsage(stmt,
          0,
          0 );
      if (str1[0] == 'Y') {

        lowStmt = g_statements;
        highStmt = 0;

        for (s = stmt + 1; s <= g_statements; s++) {

          if (str1[s] != 'Y') continue;
          usageCount++;
          if (lowStmt > s) lowStmt = s;
          if (highStmt < s) highStmt = s;
        }
      }
      notQuitPrint = print2(
"SHOW DISCOURAGED:  New usage of \"%s\" is discouraged (%ld uses).\n",
          g_Statement[stmt].labelName,
          usageCount);
      if (str1[0] == 'Y') {


        for (s = lowStmt; s <= highStmt; s++) {

          if (str1[s] != 'Y') continue;
          notQuitPrint = print2(
              "SHOW DISCOURAGED:  \"%s\" is used by \"%s\".\n",
              g_Statement[stmt].labelName,
              g_Statement[s].labelName);
        }
      }
    }
  }
  let(&str1, "");
}


long getStepNum(vstring relStep,
   nmbrString *pfInProgress,
   flag allFlag )
{
  long pfLen, i, j, relStepVal, actualStepVal;
  flag negFlag = 0;
  nmbrString *essentialFlags = NULL_NMBRSTRING;
  vstring relStepCaps = "";

  let(&relStepCaps, edit(relStep, 32));
  pfLen = nmbrLen(pfInProgress);
  relStepVal = (long)(val(relStepCaps));

  if (relStepVal >= 0 && !strcmp(relStepCaps, str((double)relStepVal))) {

    actualStepVal = relStepVal;
    if (actualStepVal > pfLen || actualStepVal < 1) {
      print2("?The step must be in the range from 1 to %ld.\n", pfLen);
      actualStepVal = -1;
    }
    goto RETURN_POINT;
  } else if (!strcmp(relStepCaps, left("FIRST", (long)(strlen(relStepCaps))))) {
    negFlag = 0;
    relStepVal = 0;
  } else if (!strcmp(relStepCaps, left("LAST", (long)(strlen(relStepCaps))))) {
    negFlag = 1;
    relStepVal = 0;
  } else if (relStepCaps[0] == '+') {
    negFlag = 0;
    if (strcmp(right(relStepCaps, 2), str((double)relStepVal))) {
      print2("?The characters after '+' are not a number.\n");
      actualStepVal = -1;
      goto RETURN_POINT;
    }
  } else if (relStepCaps[0] == '-') {
    negFlag = 1;
    if (strcmp(right(relStepCaps, 2), str((double)(- relStepVal)))) {
      print2("?The characters after '-' are not a number.\n");
      actualStepVal = -1;
      goto RETURN_POINT;
    }
    relStepVal = - relStepVal;
  } else if (!strcmp(relStepCaps, left("ALL", (long)(strlen(relStepCaps))))) {
    if (!allFlag) {

      print2("?You must specify FIRST, LAST, nn, +nn, or -nn.\n");
      actualStepVal = -1;
      goto RETURN_POINT;
    }
    actualStepVal = 0;
    goto RETURN_POINT;
  } else {
    if (allFlag) {
      print2("?You must specify FIRST, LAST, nn, +nn, -nn, or ALL.\n");
    } else {
      print2("?You must specify FIRST, LAST, nn, +nn, or -nn.\n");
    }
    actualStepVal = -1;
    goto RETURN_POINT;
  }

  nmbrLet(&essentialFlags, nmbrGetEssential(pfInProgress));


  actualStepVal = 0;
  if (negFlag) {


    j = relStepVal + 1;
    for (i = pfLen; i >= 1; i--) {
      if (essentialFlags[i - 1]
          && pfInProgress[i - 1] == -(long)'?') {
        j--;
        if (j == 0) {

          actualStepVal = i;
          break;
        }
      }
    }
  } else {


    j = relStepVal + 1;
    for (i = 1; i <= pfLen; i++) {
      if (essentialFlags[i - 1]
          && pfInProgress[i - 1] == -(long)'?') {
        j--;
        if (j == 0) {

          actualStepVal = i;
          break;
        }
      }
    }
  }
  if (actualStepVal == 0) {
    if (relStepVal == 0) {
      print2("?There are no unknown essential steps.\n");
    } else {
      print2("?There are not at least %ld unknown essential steps.\n",
        relStepVal + 1);
    }
    actualStepVal = -1;
    goto RETURN_POINT;
  }

 RETURN_POINT:

  let(&relStepCaps, "");
  nmbrLet(&essentialFlags, NULL_NMBRSTRING);

  return actualStepVal;
}




nmbrString *getRelStepNums(nmbrString *pfInProgress) {
  nmbrString *essentialFlags = NULL_NMBRSTRING;
  nmbrString *relSteps = NULL_NMBRSTRING;
  long i, j, pfLen;

  pfLen = nmbrLen(pfInProgress);
  nmbrLet(&relSteps, nmbrSpace(pfLen));
  nmbrLet(&essentialFlags, nmbrGetEssential(pfInProgress));
  j = 0;
  for (i = pfLen; i >= 1; i--) {
    if (essentialFlags[i - 1]
        && pfInProgress[i - 1] == -(long)'?') {
      relSteps[i - 1] = j;
      j--;
    } else {
      relSteps[i - 1] = i;
    }
  }


  nmbrLet(&essentialFlags, NULL_NMBRSTRING);

  return relSteps;
}



long getStatementNum(vstring stmtName,
    long startStmt,
    long maxStmt,
    flag aAllowed,
    flag pAllowed,
    flag eAllowed,
    flag fAllowed,
    flag efOnlyForMaxStmt,
    flag uniqueFlag)
{
  flag hasWildcard;
  long matchesFound, matchStmt, matchStmt2, stmt;
  char typ;
  flag laterMatchFound = 0;

  hasWildcard = 0;

  if (strpbrk(stmtName, "*?=~%#@,") != NULL) {
    hasWildcard = 1;
  }
  matchesFound = 0;
  matchStmt = 1;
  matchStmt2 = 1;


  for (stmt = startStmt; stmt <= g_statements; stmt++) {


    if (stmt >= maxStmt) {
      if (matchesFound > 0) break;
      if (!uniqueFlag) break;
    }

    if (!g_Statement[stmt].labelName[0]) continue;
    typ = g_Statement[stmt].type;

    if ((!aAllowed && typ == (char)a_)
        ||(!pAllowed && typ == (char)p_)
        ||(!eAllowed && typ == (char)e_)
        ||(!fAllowed && typ == (char)f_)) {
      continue;
    }

    if (hasWildcard) {
      if (!matchesList(g_Statement[stmt].labelName, stmtName, '*', '?')) {
        continue;
      }
    } else {
      if (strcmp(stmtName, g_Statement[stmt].labelName)) {
        continue;
      }
    }

    if (efOnlyForMaxStmt) {
      if (maxStmt > g_statements) bug(247);
      if (typ == (char)e_ || typ == (char)f_){
        if (!nmbrElementIn(1, g_Statement[maxStmt].reqHypList, stmt) &&
            !nmbrElementIn(1, g_Statement[maxStmt].optHypList, stmt))
            continue;
      }
    }


    if (stmt >= maxStmt) {
      laterMatchFound = 1;
      break;
    }

    if (matchesFound == 0) {

      matchStmt = stmt;
    }
    if (matchesFound == 1) {

      matchStmt2 = stmt;
    }
    matchesFound++;
    if (!uniqueFlag) break;
    if (!hasWildcard) break;
  }

  if (matchesFound == 0) {
    if (!uniqueFlag) {
      if (startStmt == 1) {

        print2("?No statement label matches \"%s\".\n", stmtName);
      }
    } else if (aAllowed && pAllowed && eAllowed && fAllowed
               && !efOnlyForMaxStmt) {
      print2("?No statement label matches \"%s\".\n", stmtName);
    } else if (!aAllowed && pAllowed && !eAllowed && !fAllowed) {

      print2("?No $p statement label matches \"%s\".\n", stmtName);
    } else if (!eAllowed && !fAllowed) {

      if (!laterMatchFound) {
        print2("?No $a or $p statement label matches \"%s\".\n",
          stmtName);
      } else {

        print2(
   "?You must specify a statement that occurs earlier the one being proved.\n");
      }
    } else {

      if (!laterMatchFound) {
        printLongLine(cat("?A statement label matching \"",
            stmtName,
            "\" was not found or is not a hypothesis of the statement ",
            "being proved.", NULL), "", " ");
      } else {

        print2(
   "?You must specify a statement that occurs earlier the one being proved.\n");
      }
    }
  } else if (matchesFound == 2) {
    printLongLine(cat("?This command requires a unique label, but there are ",
        " 2 matches for \"",
        stmtName, "\":  \"", g_Statement[matchStmt].labelName,
        "\" and \"", g_Statement[matchStmt2].labelName, "\".",
        NULL), "", " ");
  } else if (matchesFound > 2) {
    printLongLine(cat("?This command requires a unique label, but there are ",
        str((double)matchesFound), " (allowed) matches for \"",
        stmtName, "\".  The first 2 are \"", g_Statement[matchStmt].labelName,
        "\" and \"", g_Statement[matchStmt2].labelName, "\".",
        "  Use SHOW LABELS \"", stmtName, "\" to see all non-$e matches.",
        NULL), "", " ");
  }
  if (!uniqueFlag && matchesFound > 1) bug(248);
  if (matchesFound != 1) matchStmt = -1;
  return matchStmt;
}






void H(vstring helpLine)
{
  if (g_printHelp) {
    print2("%s\n", helpLine);
  }
}






void outputMidi(long plen, nmbrString *indentationLevels,
  nmbrString *logicalFlags, vstring g_midiParameter, vstring statementLabel) {






  public static final long TEMPO =D.TEMPO;


  public static final long MIN_NOTE =D.MIN_NOTE;
  public static final long MIN_NOTE =D.;


  long step;
  long midiKey;
  long midiNote;
  long midiTime;
  long midiPreviousFormulaStep;
  long midiPreviousLogicalStep;
  vstring midiFileName = "";
  File midiFilePtr;
  long midiBaseline;
  long midiMaxIndent;
  long midiMinKey;
  long midiMaxKey;
  long midiKeyInc;
  flag midiSyncopate;
  flag midiHesitate;
  long midiTempo;
  vstring midiLocalParam = "";
  vstring tmpStr = "";
  static final long ALLKEYSFLAG =D.ALLKEYSFLAG;
  static final long WHITEKEYSFLAG =D.WHITEKEYSFLAG;
  static final long BLACKKEYSFLAG =D.BLACKKEYSFLAG;
  flag keyboardType;
  long absMinKey;
  long absMaxKey;
  long key2MidiMap[128];
  long keyboardOctave;
  long i;


  static final long ALLKEYS =D.ALLKEYS;
  static final long WHITEKEYS =D.WHITEKEYS;
  static final long BLACKKEYS =D.BLACKKEYS;
  long allKeys[ALLKEYS] =
      {  0,   1,   2,   3,   4,   5,   6,   7,   8,   9,  10,  11,
        12,  13,  14,  15,  16,  17,  18,  19,  20,  21,  22,  23,
        24,  25,  26,  27,  28,  29,  30,  31,  32,  33,  34,  35,
        36,  37,  38,  39,  40,  41,  42,  43,  44,  45,  46,  47,
        48,  49,  50,  51,  52,  53,  54,  55,  56,  57,  58,  59,
        60,  61,  62,  63,  64,  65,  66,  67,  68,  69,  70,  71,
        72,  73,  74,  75,  76,  77,  78,  79,  80,  81,  82,  83,
        84,  85,  86,  87,  88,  89,  90,  91,  92,  93,  94,  95,
        96,  97,  98,  99, 100, 101, 102, 103, 104, 105, 106, 107,
       108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119,
       120, 121, 122, 123, 124, 125, 126, 127};
  long whiteKeys[WHITEKEYS] =
      {  0,   2,   4,   5,   7,   9,  11,
        12,  14,  16,  17,  19,  21,  23,
        24,  26,  28,  29,  31,  33,  35,
        36,  38,  40,  41,  43,  45,  47,
        48,  50,  52,  53,  55,  57,  59,
        60,  62,  64,  65,  67,  69,  71,
        72,  74,  76,  77,  79,  81,  83,
        84,  86,  88,  89,  91,  93,  95,
        96,  98, 100, 101, 103, 105, 107,
       108, 110, 112, 113, 115, 117, 119,
       120, 122, 124, 125, 127};
  long blackKeys[BLACKKEYS] =
      {  1,   3,   6,   8,  10,
        13,  15,  18,  20,  22,
        25,  27,  30,  32,  34,
        37,  39,  42,  44,  46,
        49,  51,  54,  56,  58,
        61,  63,  66,  68,  70,
        73,  75,  78,  80,  82,
        85,  87,  90,  92,  94,
        97,  99, 102, 104, 106,
       109, 111, 114, 116, 118,
       121, 123, 126};



  midiTime = 0;
  midiPreviousFormulaStep = 0;
  midiPreviousLogicalStep = 0;
  midiFilePtr = NULL;


  let(&midiLocalParam, edit(g_midiParameter, 32));


  if (strchr(midiLocalParam, 'S') != NULL) {
    midiSyncopate = 1;
  } else {
    midiSyncopate = 0;
  }
  if (strchr(midiLocalParam, 'H') != NULL) {
    midiHesitate = 1;
  } else {
    midiHesitate = 0;
  }

  if (strchr(midiLocalParam, 'F') != NULL) {
    midiTempo = 2 * TEMPO;
  } else {
    if (strchr(midiLocalParam, 'M') != NULL) {
      midiTempo = 3 * TEMPO / 2;
    } else {
      midiTempo = TEMPO;
    }
  }

  if (strchr(midiLocalParam, 'W') != NULL) {
    keyboardType = WHITEKEYSFLAG;
  } else {
    if (strchr(midiLocalParam, 'B') != NULL) {
      keyboardType = BLACKKEYSFLAG;
    } else {
      keyboardType = ALLKEYSFLAG;
    }
  }

  if (strchr(midiLocalParam, 'I') != NULL) {

    midiKeyInc = 1 + 1;
  } else {

    midiKeyInc = 4 + 1;
  }





  absMinKey = MIN_NOTE;
  absMaxKey = MAX_NOTE;
  keyboardOctave = 12;
  switch (keyboardType) {
    case ALLKEYSFLAG:
      for (i = 0; i < 128; i++) key2MidiMap[i] = allKeys[i];
     break;
    case WHITEKEYSFLAG:
      for (i = 0; i < WHITEKEYS; i++) key2MidiMap[i] = whiteKeys[i];
      keyboardOctave = 7;

      for (i = 0; i < WHITEKEYS; i++) {
        if (key2MidiMap[i] >= absMinKey) {
          absMinKey = i;
          break;
        }
      }

      for (i = WHITEKEYS - 1; i >= 0; i--) {
        if (key2MidiMap[i] <= absMinKey) {
          absMinKey = i;
          break;
        }
      }

      if (absMaxKey >= WHITEKEYS) absMaxKey = WHITEKEYS - 1;
      if (absMinKey >= WHITEKEYS) absMinKey = WHITEKEYS - 1;
      break;
    case BLACKKEYSFLAG:
      for (i = 0; i < BLACKKEYS; i++) key2MidiMap[i] = blackKeys[i];
      keyboardOctave = 5;

      for (i = 0; i < BLACKKEYS; i++) {
        if (key2MidiMap[i] >= absMinKey) {
          absMinKey = i;
          break;
        }
      }

      for (i = BLACKKEYS - 1; i >= 0; i--) {
        if (key2MidiMap[i] <= absMinKey) {
          absMinKey = i;
          break;
        }
      }

      if (absMaxKey >= BLACKKEYS) absMaxKey = BLACKKEYS - 1;
      if (absMinKey >= BLACKKEYS) absMinKey = BLACKKEYS - 1;
      break;
  }

  midiMaxIndent = 0;
  for (step = 0; step < plen; step++) {
    if (indentationLevels[step] > midiMaxIndent)
      midiMaxIndent = indentationLevels[step];
  }




  do {
    midiKeyInc--;
    midiBaseline = ((absMaxKey + absMinKey) / 2) -
      (((midiMaxIndent * midiKeyInc) - keyboardOctave) / 2);
    midiMinKey = midiBaseline - keyboardOctave;
    midiMaxKey = midiBaseline + (midiMaxIndent * midiKeyInc);
  } while ((midiMinKey < absMinKey || midiMaxKey > absMaxKey) &&
      midiKeyInc > 0);


  let(&midiFileName, cat(g_Statement[g_showStatement].labelName,
      ".txt", NULL));
  print2("Creating MIDI source file \"%s\"...", midiFileName);

  midiFilePtr = fSafeOpen(midiFileName, "w", 0);
  if (midiFilePtr == NULL) {
    print2("?Couldn't open %s\n", midiFileName);
    goto midi_return;
  }


  fprintf(midiFilePtr, "MFile 0 1 %ld\n", midiTempo);
  fprintf(midiFilePtr, "MTrk\n");

  let(&tmpStr, cat("Theorem ", statementLabel, " ", g_midiParameter,
      space(30), NULL));
  let(&tmpStr, left(tmpStr, 38));
  fprintf(midiFilePtr, "0 Meta Text \"%s\"\n", tmpStr);
  fprintf(midiFilePtr,
      "0 Meta Copyright \"Released to Public Domain by N. Megill\"\n");



  for (step = 0; step < plen; step++) {

    if (!logicalFlags[step]) {



      midiKey = (midiKeyInc * indentationLevels[step]) + midiBaseline;

      if (midiKey < 0) midiKey = 0;
      if (midiKey > absMaxKey) midiKey = absMaxKey;

      midiNote = key2MidiMap[midiKey];
      if (midiPreviousFormulaStep != midiNote || !midiSyncopate) {

        fprintf(midiFilePtr, "%ld On ch=2 n=%ld v=75\n", midiTime, midiNote);

        fprintf(midiFilePtr, "%ld On ch=2 n=%ld v=0\n", midiTime + 18,
            midiNote);
        midiPreviousFormulaStep = midiNote;
      } else {

        if (!midiHesitate) {
          midiPreviousFormulaStep = 0;
        }
      }
      midiTime += 24;

    } else {



      midiKey = (midiKeyInc * indentationLevels[step]) + midiBaseline;

      if (midiKey < 0) midiKey = 0;
      if (midiKey > absMaxKey) midiKey = absMaxKey;

      midiNote = key2MidiMap[midiKey];
      midiNote = midiNote - 12;
      if (midiNote < 0) midiNote = 0;
      if (midiPreviousLogicalStep) {

        fprintf(midiFilePtr, "%ld On ch=1 n=%ld v=0\n", midiTime,
            midiPreviousLogicalStep);
      }

      fprintf(midiFilePtr, "%ld On ch=1 n=%ld v=100\n", midiTime,
          midiNote);
      midiTime += 24;
      midiPreviousLogicalStep = midiNote;

    }
  }





  midiTime += 72;
  fprintf(midiFilePtr, "%ld On ch=1 n=%ld v=0\n", midiTime,
      midiPreviousLogicalStep);

  fprintf(midiFilePtr, "%ld Meta TrkEnd\n", midiTime);
  fprintf(midiFilePtr, "TrkEnd\n");
  fclose(midiFilePtr);

  print2(" length = %ld sec\n", (long)(midiTime / (2 * midiTempo)));

 midi_return:
  let(&midiFileName, "");
  let(&tmpStr, "");
  let(&midiLocalParam, "");

}
}