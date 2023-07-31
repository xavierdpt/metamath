package xd.metamath;

public class mmwtex {











flag g_oldTexFlag = 0;

flag g_htmlFlag = 0;
flag g_altHtmlFlag = 0;
flag g_briefHtmlFlag = 0;
long g_extHtmlStmt = 0;


long g_mathboxStmt = 0;
long g_mathboxes = 0;
nmbrString *g_mathboxStart = NULL_NMBRSTRING;
nmbrString *g_mathboxEnd = NULL_NMBRSTRING;
pntrString *g_mathboxUser = NULL_PNTRSTRING;

  static final String OPENING_PUNCTUATION=D.OPENING_PUNCTUATION;
  static final String CLOSING_PUNCTUATION=D.CLOSING_PUNCTUATION;


File g_texFilePtr = null;
flag g_texFileOpenFlag = 0;



flag g_texDefsRead = 0;
struct texDef_struct *g_TexDefs;


long numSymbs;
  static final long  DOLLAR_SUBST =D.DOLLAR_SUBST;




vstring g_htmlCSS = "";
vstring g_htmlFont = "";
vstring g_htmlVarColor = "";
vstring htmlExtUrl = "";
vstring htmlTitle = "";

  vstring htmlTitleAbbr = "";
vstring g_htmlHome = "";


  vstring g_htmlHomeHREF = "";
  vstring g_htmlHomeIMG = "";
vstring g_htmlBibliography = "";
vstring extHtmlLabel = "";
vstring g_extHtmlTitle = "";
  vstring g_extHtmlTitleAbbr = "";
vstring extHtmlHome = "";


  vstring extHtmlHomeHREF = "";
  vstring extHtmlHomeIMG = "";
vstring extHtmlBibliography = "";
vstring htmlDir = "";
vstring altHtmlDir = "";


vstring sandboxHome = "";
  vstring sandboxHomeHREF = "";
  vstring sandboxHomeIMG = "";
vstring sandboxTitle = "";

  vstring sandboxTitleAbbr = "";


vstring g_htmlBibliographyTags = "";
vstring extHtmlBibliographyTags = "";



void eraseTexDefs() {

  long i;
  if (g_texDefsRead) {
    g_texDefsRead = 0;

    for (i = 0; i < numSymbs; i++) {
      let(&(g_TexDefs[i].tokenName), "");
      let(&(g_TexDefs[i].texEquiv), "");
    }
    free(g_TexDefs);
  }
  return;
}


flag readTexDefs(
  flag errorsOnly,
  flag noGifCheck   )
{

  char *fileBuf;
  char *startPtr;
  long lineNumOffset = 0;
  char *fbPtr;
  char *tmpPtr;
  char *tmpPtr2;
  long charCount;
  long i, j, k, p;
  long lineNum;
  long tokenLength;
  char zapChar;
  long cmd;
  long parsePass;
  vstring token = "";
  vstring partialToken = "";
  File mpFp;
  static flag saveHtmlFlag = -1;
  static flag saveAltHtmlFlag = 1;
  flag warningFound = 0;
  char *dollarTStmtPtr = null;


  void *g_mathKeyPtr;
  void *texDefsPtr;



  if (saveHtmlFlag != g_htmlFlag || saveAltHtmlFlag != g_altHtmlFlag
      || !g_texDefsRead) {

    eraseTexDefs();
    saveHtmlFlag = g_htmlFlag;
    saveAltHtmlFlag = g_altHtmlFlag;
    if (g_htmlFlag == 0  && g_altHtmlFlag == 1) {
      bug(2301);
    }
  } else {

    return 0;
  }


  let(&htmlTitle, "Metamath Test Page");


  let(&g_htmlHome, cat("<A HREF=\"mmset.html\"><FONT SIZE=-2 FACE=sans-serif>",
    "<IMG SRC=\"mm.gif\" BORDER=0 ALT=",
    "\"Home\" HEIGHT=32 WIDTH=32 ALIGN=MIDDLE STYLE=\"margin-bottom:0px\">",
    "Home</FONT></A>", null));


  if (errorsOnly == 0) {
    mminou.print2("Reading definitions from $t statement of %s...\n", g_input_fn);
  }


  fileBuf = "";
  let(&fileBuf, "");
  for (i = 1; i <= g_statements + 1; i++) {

    tmpPtr = g_Statement[i].labelSectionPtr;
    j = g_Statement[i].labelSectionLen;
    if (!fileBuf[0]) lineNumOffset = g_Statement[i].lineNum;

    zapChar = tmpPtr[j];
    tmpPtr[j] = 0;
    if (instr(1, tmpPtr, "$t")) {


      if (fileBuf[0]) {
        mminou.print2(
  "?Error: There are two comments containing a $t keyword in \"%s\".\n",
            g_input_fn);
        let(&fileBuf, "");
        return 2;
      }
      let(&fileBuf, tmpPtr);
      tmpPtr[j] = zapChar;
      dollarTStmtPtr = g_Statement[i].labelSectionPtr;

    }
    tmpPtr[j] = zapChar;
  }

  j = (long)C.strlen(fileBuf);
  for (i = 0; i < j; i++) {
    if (fileBuf[i] == '\n') lineNumOffset--;
  }



  static final long LATEXDEF =D.LATEXDEF;
  static final long HTMLDEF =D.HTMLDEF;
  static final long HTMLVARCOLOR =D.HTMLVARCOLOR;
  static final long HTMLTITLE =D.HTMLTITLE;
  static final long HTMLHOME =D.HTMLHOME;

  static final long ALTHTMLDEF =D.ALTHTMLDEF;
  static final long EXTHTMLTITLE =D.EXTHTMLTITLE;
  static final long EXTHTMLHOME =D.EXTHTMLHOME;
  static final long EXTHTMLLABEL =D.EXTHTMLLABEL;
  static final long HTMLDIR =D.HTMLDIR;
  static final long ALTHTMLDIR =D.ALTHTMLDIR;

  static final long HTMLBIBLIOGRAPHY =D.HTMLBIBLIOGRAPHY;
  static final long EXTHTMLBIBLIOGRAPHY =D.EXTHTMLBIBLIOGRAPHY;

  static final long HTMLCSS =D.HTMLCSS;
  static final long HTMLFONT =D.HTMLFONT;

  static final long HTMLEXTURL =D.HTMLEXTURL;

  startPtr = fileBuf;


  while (true) {
    if (startPtr[0] == '$') {
      if (startPtr[1] == 't') {
        startPtr++;
        break;
      }
    }
    if (startPtr[0] == 0) break;
    startPtr++;
  }
  if (startPtr[0] == 0) {
    mminou.print2("?Error: There is no $t command in the file \"%s\".\n", g_input_fn);
    mminou.print2(
"The file should have exactly one comment of the form $(...$t...$) with\n");
    mminou.print2("the LaTeX and HTML definitions between $t and $).\n");
    let(&fileBuf, "");
    return 2;
  }
  startPtr++;


  tmpPtr = startPtr;
  while (true) {
    if (tmpPtr[0] == '$') {
      if (tmpPtr[1] == ')') {
        break;
      }
    }
    if (tmpPtr[0] == 0) break;
    tmpPtr++;
  }
  if (tmpPtr[0] == 0) {
    mminou.print2(
  "?Error: There is no $) comment closure after the $t keyword in \"%s\".\n",
        g_input_fn);
    let(&fileBuf, "");
    return 2;
  }


  tmpPtr2 = tmpPtr;
  while (true) {
    if (tmpPtr2[0] == '$') {
      if (tmpPtr2[1] == 't') {
        mminou.print2(
  "?Error: There are two comments containing a $t keyword in \"%s\".\n",
            g_input_fn);
        let(&fileBuf, "");
        return 2;
      }
    }
    if (tmpPtr2[0] == 0) break;
    tmpPtr2++;
  }


  tmpPtr[0] = '\n';
  tmpPtr[1] = 0;

  charCount = tmpPtr + 1 - fileBuf;

  for (parsePass = 1; parsePass <= 2; parsePass++) {


    numSymbs = 0;
    fbPtr = startPtr;

    while (true) {


      fbPtr = fbPtr + texDefWhiteSpaceLen(fbPtr);
      tokenLength = texDefTokenLen(fbPtr);


      if (!tokenLength) break;
      zapChar = fbPtr[tokenLength];
      fbPtr[tokenLength] = 0;
      cmd = lookup(fbPtr,
          "latexdef,htmldef,htmlvarcolor,htmltitle,htmlhome"
          ",althtmldef,exthtmltitle,exthtmlhome,exthtmllabel,htmldir"
          ",althtmldir,htmlbibliography,exthtmlbibliography"
          ",htmlcss,htmlfont,htmlexturl"
          );
      fbPtr[tokenLength] = zapChar;
      if (cmd == 0) {
        lineNum = lineNumOffset;
        for (i = 0; i < (fbPtr - fileBuf); i++) {
          if (fileBuf[i] == '\n') lineNum++;
        }
        rawSourceError(g_sourcePtr,
            dollarTStmtPtr + (fbPtr - fileBuf), tokenLength,
            cat("Expected \"latexdef\", \"htmldef\", \"htmlvarcolor\",",
            " \"htmltitle\", \"htmlhome\", \"althtmldef\",",
            " \"exthtmltitle\", \"exthtmlhome\", \"exthtmllabel\",",
            " \"htmldir\", \"althtmldir\",",
            " \"htmlbibliography\", \"exthtmlbibliography\",",
            " \"htmlcss\", \"htmlfont\",",
            " or \"htmlexturl\" here.",
            null));
        let(&fileBuf, "");
        return 2;
      }
      fbPtr = fbPtr + tokenLength;

      if (cmd != HTMLVARCOLOR && cmd != HTMLTITLE && cmd != HTMLHOME
          && cmd != EXTHTMLTITLE && cmd != EXTHTMLHOME && cmd != EXTHTMLLABEL
          && cmd != HTMLDIR && cmd != ALTHTMLDIR
          && cmd != HTMLBIBLIOGRAPHY && cmd != EXTHTMLBIBLIOGRAPHY
          && cmd != HTMLCSS
          && cmd != HTMLFONT
          && cmd != HTMLEXTURL
          ) {

        fbPtr = fbPtr + texDefWhiteSpaceLen(fbPtr);
        tokenLength = texDefTokenLen(fbPtr);


        if (fbPtr[0] != '\"' && fbPtr[0] != '\'') {
          if (!tokenLength) {
            fbPtr--;
            tokenLength++;
          }
          lineNum = lineNumOffset;
          for (i = 0; i < (fbPtr - fileBuf); i++) {
            if (fileBuf[i] == '\n') lineNum++;
          }
          rawSourceError(g_sourcePtr,
            dollarTStmtPtr + (fbPtr - fileBuf),
              tokenLength,
              "Expected a quoted string here.");
          let(&fileBuf, "");
          return 2;
        }
        if (parsePass == 2) {
          zapChar = fbPtr[tokenLength - 1];
          fbPtr[tokenLength - 1] = 0;
          let(&token, fbPtr + 1);
          fbPtr[tokenLength - 1] = zapChar;


          if (fbPtr[0] != '\"' && fbPtr[0] != '\'') bug(2329);
          j = (long)C.strlen(token);
          for (i = 0; i < j - 1; i++) {
            if (token[i] == fbPtr[0] &&
                token[i + 1] == fbPtr[0]) {
              let(&token, cat(left(token,
                  i + 1), right(token, i + 3), null));
              j--;
            }
          }

          if ((cmd == LATEXDEF && !g_htmlFlag)
              || (cmd == HTMLDEF && g_htmlFlag && !g_altHtmlFlag)
              || (cmd == ALTHTMLDEF && g_htmlFlag && g_altHtmlFlag)) {
            g_TexDefs[numSymbs].tokenName = "";
            let(&(g_TexDefs[numSymbs].tokenName), token);
          }
        }

        fbPtr = fbPtr + tokenLength;
      }

      if (cmd != HTMLVARCOLOR && cmd != HTMLTITLE && cmd != HTMLHOME
          && cmd != EXTHTMLTITLE && cmd != EXTHTMLHOME && cmd != EXTHTMLLABEL
          && cmd != HTMLDIR && cmd != ALTHTMLDIR
          && cmd != HTMLBIBLIOGRAPHY && cmd != EXTHTMLBIBLIOGRAPHY
          && cmd != HTMLCSS
          && cmd != HTMLFONT
          && cmd != HTMLEXTURL
          ) {

        fbPtr = fbPtr + texDefWhiteSpaceLen(fbPtr);
        tokenLength = texDefTokenLen(fbPtr);
        zapChar = fbPtr[tokenLength];
        fbPtr[tokenLength] = 0;
        if (strcmp(fbPtr, "as")) {
          if (!tokenLength) {
            fbPtr--;
            tokenLength++;
          }
          lineNum = lineNumOffset;
          for (i = 0; i < (fbPtr - fileBuf); i++) {
            if (fileBuf[i] == '\n') lineNum++;
          }
          rawSourceError(g_sourcePtr,
            dollarTStmtPtr + (fbPtr - fileBuf),
              tokenLength,
              "Expected the keyword \"as\" here.");
          let(&fileBuf, "");
          return 2;
        }
        fbPtr[tokenLength] = zapChar;
        fbPtr = fbPtr + tokenLength;
      }

      if (parsePass == 2) {

        let(&token, "");
      }


      while (true) {


        fbPtr = fbPtr + texDefWhiteSpaceLen(fbPtr);
        tokenLength = texDefTokenLen(fbPtr);
        if (fbPtr[0] != '\"' && fbPtr[0] != '\'') {
          if (!tokenLength) {
            fbPtr--;
            tokenLength++;
          }
          lineNum = lineNumOffset;
          for (i = 0; i < (fbPtr - fileBuf); i++) {
            if (fileBuf[i] == '\n') lineNum++;
          }
          rawSourceError(g_sourcePtr,
            dollarTStmtPtr + (fbPtr - fileBuf),
              tokenLength,
              "Expected a quoted string here.");
          let(&fileBuf, "");
          return 2;
        }
        if (parsePass == 2) {
          zapChar = fbPtr[tokenLength - 1];
          fbPtr[tokenLength - 1] = 0;


          let(&partialToken, fbPtr + 1);
          fbPtr[tokenLength - 1] = zapChar;



          if (fbPtr[0] != '\"' && fbPtr[0] != '\'') bug(2330);
          j = (long)C.strlen(partialToken);
          for (i = 0; i < j - 1; i++) {
            if (token[i] == fbPtr[0] &&
                token[i + 1] == fbPtr[0]) {
              let(&partialToken, cat(left(partialToken,
                  i + 1), right(token, i + 3), null));
              j--;
            }
          }



          tmpPtr2 = strchr(partialToken, '\n');
          if (tmpPtr2 != null) {


            lineNum = lineNumOffset;
            for (i = 0; i < (fbPtr - fileBuf); i++) {
              if (fileBuf[i] == '\n') lineNum++;
            }

            rawSourceError(g_sourcePtr,
                dollarTStmtPtr + (fbPtr - fileBuf),
                tmpPtr2 - partialToken + 1 ,

                "String should be on a single line.");
          }



          let(&token, cat(token, partialToken, null));

        }

        fbPtr = fbPtr + tokenLength;



        fbPtr = fbPtr + texDefWhiteSpaceLen(fbPtr);
        tokenLength = texDefTokenLen(fbPtr);
        if ((fbPtr[0] != '+' && fbPtr[0] != ';') || tokenLength != 1) {
          if (!tokenLength) {
            fbPtr--;
            tokenLength++;
          }
          lineNum = lineNumOffset;
          for (i = 0; i < (fbPtr - fileBuf); i++) {
            if (fileBuf[i] == '\n') {
              lineNum++;
            }
          }
          rawSourceError(g_sourcePtr,
            dollarTStmtPtr + (fbPtr - fileBuf),
              tokenLength,
              "Expected \"+\" or \";\" here.");
          let(&fileBuf, "");
         return 2;
        }
        fbPtr = fbPtr + tokenLength;

        if (fbPtr[-1] == ';') break;

      }


      if (parsePass == 2) {





        if ((cmd == LATEXDEF && !g_htmlFlag)
            || (cmd == HTMLDEF && g_htmlFlag && !g_altHtmlFlag)
            || (cmd == ALTHTMLDEF && g_htmlFlag && g_altHtmlFlag)) {
          g_TexDefs[numSymbs].texEquiv = "";
          let(&(g_TexDefs[numSymbs].texEquiv), token);
        }
        if (cmd == HTMLVARCOLOR) {
          let(&g_htmlVarColor, cat(g_htmlVarColor, " ", token, null));
        }
        if (cmd == HTMLTITLE) {
          let(&htmlTitle, token);
        }
        if (cmd == HTMLHOME) {
          let(&g_htmlHome, token);
        }
        if (cmd == EXTHTMLTITLE) {
          let(&g_extHtmlTitle, token);
        }
        if (cmd == EXTHTMLHOME) {
          let(&extHtmlHome, token);
        }
        if (cmd == EXTHTMLLABEL) {
          let(&extHtmlLabel, token);
        }
        if (cmd == HTMLDIR) {
          let(&htmlDir, token);
        }
        if (cmd == ALTHTMLDIR) {
          let(&altHtmlDir, token);
        }
        if (cmd == HTMLBIBLIOGRAPHY) {
          let(&g_htmlBibliography, token);
        }
        if (cmd == EXTHTMLBIBLIOGRAPHY) {
          let(&extHtmlBibliography, token);
        }
        if (cmd == HTMLCSS) {
          let(&g_htmlCSS, token);


          do {
            p = instr(1, g_htmlCSS, "\\n");
            if (p != 0) {
              let(&g_htmlCSS, cat(left(g_htmlCSS, p - 1), "\n",
                  right(g_htmlCSS, p + 2), null));
            }
          } while (p != 0);
        }
        if (cmd == HTMLFONT) {
          let(&g_htmlFont, token);
        }
        if (cmd == HTMLEXTURL) {
          let(&htmlExtUrl, token);
        }
      }

      if ((cmd == LATEXDEF && !g_htmlFlag)
          || (cmd == HTMLDEF && g_htmlFlag && !g_altHtmlFlag)
          || (cmd == ALTHTMLDEF && g_htmlFlag && g_altHtmlFlag)) {
        numSymbs++;
      }

    }

    if (fbPtr != fileBuf + charCount) bug(2305);

    if (parsePass == 1 ) {
      if (errorsOnly == 0) {
        mminou.print2("%ld typesetting statements were read from \"%s\".\n",
            numSymbs, g_input_fn);
      }
      g_TexDefs = malloc((size_t)numSymbs * sizeof(struct texDef_struct));
      if (!g_TexDefs) outOfMemory("#99 (TeX symbols)");
    }

  }



  qsort(g_TexDefs, (size_t)numSymbs, sizeof(struct texDef_struct), texSortCmp);


  for (i = 1; i < numSymbs; i++) {
    if (!strcmp(g_TexDefs[i].tokenName, g_TexDefs[i - 1].tokenName)) {
      printLongLine(cat("?Warning: Token ", g_TexDefs[i].tokenName,
          " is defined more than once in ",
          g_htmlFlag
            ? (g_altHtmlFlag ? "an althtmldef" : "an htmldef")
            : "a latexdef",
          " statement.", null),
          "", " ");
      warningFound = 1;
    }
  }


  for (i = 0; i < numSymbs; i++) {
    g_mathKeyPtr = (void *)bsearch(g_TexDefs[i].tokenName, g_mathKey,
        (size_t)g_mathTokens, sizeof(long), mathSrchCmp);
    if (!g_mathKeyPtr) {
      printLongLine(cat("?Warning: The token \"", g_TexDefs[i].tokenName,
          "\", which was defined in ",
          g_htmlFlag
            ? (g_altHtmlFlag ? "an althtmldef" : "an htmldef")
            : "a latexdef",
          " statement, was not declared in any $v or $c statement.", null),
          "", " ");
      warningFound = 1;
    }
  }


  for (i = 0; i < g_mathTokens; i++) {
    texDefsPtr = (void *)bsearch(g_MathToken[i].tokenName, g_TexDefs,
        (size_t)numSymbs, sizeof(struct texDef_struct), texSrchCmp);
    if (!texDefsPtr) {
      printLongLine(cat("?Warning: The token \"", g_MathToken[i].tokenName,
       "\", which was defined in a $v or $c statement, was not declared in ",
          g_htmlFlag
            ? (g_altHtmlFlag ? "an althtmldef" : "an htmldef")
            : "a latexdef",
          " statement.", null),
          "", " ");
      warningFound = 1;
    }
  }



  if (g_htmlFlag) {
    for (i = 0; i < numSymbs; i++) {
      tmpPtr = g_TexDefs[i].texEquiv;
      k = 0;
      while (true) {
        j = instr(k + 1, tmpPtr, "IMG SRC=");
        if (j == 0) break;
        k = instr(j + 9, g_TexDefs[i].texEquiv, mid(tmpPtr, j + 8, 1));

        let(&token, seg(tmpPtr, j + 9, k - 1));
        if (k == 0) break;
        if (noGifCheck == 0) {
          tmpFp = fopen(token, "r");
          if (!tmpFp) {
            printLongLine(cat("?Warning: The file \"", token,
                "\", which is referenced in an htmldef",
                " statement, was not found.", null),
                "", " ");
            warningFound = 1;
          } else {
            C.fclose(tmpFp);
          }
        }
      }
    }
  }



  if (extHtmlLabel[0]) {
    for (i = 1; i <= g_statements; i++) {
      if (!strcmp(extHtmlLabel, g_Statement[i].labelName)) break;
    }
    if (i > g_statements) {
      printLongLine(cat("?Warning: There is no statement with label \"",
          extHtmlLabel,
          "\" (specified by exthtmllabel in the database source $t comment).  ",
          "Use SHOW LABELS for a list of valid labels.", null), "", " ");
      warningFound = 1;
    }
    g_extHtmlStmt = i;
  } else {

    g_extHtmlStmt = g_statements + 1;
  }




  assignMathboxInfo();
  if (g_extHtmlStmt == g_statements + 1) g_extHtmlStmt = g_mathboxStmt;
  let(&sandboxHome, cat("<A HREF=\"mmtheorems.html#sandbox:bighdr\">",
    "<FONT SIZE=-2 FACE=sans-serif>",
    "<IMG SRC=\"_sandbox.gif\" BORDER=0 ALT=",
    "\"Table of Contents\" HEIGHT=32 WIDTH=32 ALIGN=MIDDLE>",
    "Table of Contents</FONT></A>", null));
  let(&sandboxHomeHREF, "mmtheorems.html#sandbox:bighdr");
  let(&sandboxHomeIMG, "_sandbox.gif");
  let(&sandboxTitleAbbr, "Users' Mathboxes");


  let(&sandboxTitle, "Users' Mathboxes");






  i = instr(1, g_htmlHome, "HREF=\"") + 5;
  if (i == 5) {
    printLongLine(
        "?Warning: In the $t comment, htmlhome has no 'HREF=\"'.", "", " ");
    warningFound = 1;
  }
  j = instr(i + 1, g_htmlHome, "\"");
  let(&g_htmlHomeHREF, seg(g_htmlHome, i + 1, j - 1));
  i = instr(1, g_htmlHome, "IMG SRC=\"") + 8;
  if (i == 8) {
    printLongLine(
        "?Warning: In the $t comment, htmlhome has no 'IMG SRC=\"'.", "", " ");
    warningFound = 1;
  }
  j = instr(i + 1, g_htmlHome, "\"");
  let(&g_htmlHomeIMG, seg(g_htmlHome, i + 1, j - 1));




  j = (long)C.strlen(htmlTitle);
  let(&htmlTitleAbbr, "");
  for (i = 1; i <= j; i++) {
    if (htmlTitle[i - 1] >= 'A' && htmlTitle[i -1] <= 'Z') {
      let(&htmlTitleAbbr, cat(htmlTitleAbbr, chr(htmlTitle[i - 1]), null));
    }
  }
  let(&htmlTitleAbbr, cat(htmlTitleAbbr, " Home", null));


  if (g_extHtmlStmt < g_statements + 1

      && g_extHtmlStmt != g_mathboxStmt) {
    i = instr(1, extHtmlHome, "HREF=\"") + 5;
    if (i == 5) {
      printLongLine(
          "?Warning: In the $t comment, exthtmlhome has no 'HREF=\"'.", "", " ");
      warningFound = 1;
    }
    j = instr(i + 1, extHtmlHome, "\"");
    let(&extHtmlHomeHREF, seg(extHtmlHome, i + 1, j - 1));
    i = instr(1, extHtmlHome, "IMG SRC=\"") + 8;
    if (i == 8) {
      printLongLine(
          "?Warning: In the $t comment, exthtmlhome has no 'IMG SRC=\"'.", "", " ");
      warningFound = 1;
    }
    j = instr(i + 1, extHtmlHome, "\"");
    let(&extHtmlHomeIMG, seg(extHtmlHome, i + 1, j - 1));

    j = (long)C.strlen(g_extHtmlTitle);
    let(&g_extHtmlTitleAbbr, "");
    for (i = 1; i <= j; i++) {
      if (g_extHtmlTitle[i - 1] >= 'A' && g_extHtmlTitle[i -1] <= 'Z') {
        let(&g_extHtmlTitleAbbr, cat(g_extHtmlTitleAbbr,
            chr(g_extHtmlTitle[i - 1]), null));
      }
    }
    let(&g_extHtmlTitleAbbr, cat(g_extHtmlTitleAbbr, " Home", null));
  }





  let(&token, "");
  let(&partialToken, "");
  let(&fileBuf, "");
  g_texDefsRead = 1;
  return warningFound;

}

long texDefWhiteSpaceLen(char *ptr)
{
  long i = 0;
  char tmpchr;
  char *ptr1;
  while (true) {
    tmpchr = ptr[i];
    if (!tmpchr) return (i);
    if (isalnum((unsigned char)(tmpchr))) return (i);



    if (tmpchr == '/') {
      if (ptr[i + 1] == '*') {
        while (true) {
          ptr1 = strchr(ptr + i + 2, '*');
          if (!ptr1) {
            return(i + (long)C.strlen(&ptr[i]));
          }
          if (ptr1[1] == '/') break;
          i = ptr1 - ptr;
        }
        i = ptr1 - ptr + 2;
        continue;
      } else {
        return(i);
      }
    }
    if (isgraph((unsigned char)tmpchr)) return (i);
    i++;
  }
  bug(2307);
  return(0);
}


long texDefTokenLen(char *ptr)
{
  long i = 0;
  char tmpchr;
  char *ptr1;
  tmpchr = ptr[i];
  if (tmpchr == '\"') {
    while (true) {
      ptr1 = strchr(ptr + i + 1, '\"');
      if (!ptr1) {
        return(i + (long)C.strlen(&ptr[i]));
      }
      if (ptr1[1] != '\"') return(ptr1 - ptr + 1);
      i = ptr1 - ptr + 1;
    }
  }
  if (tmpchr == '\'') {
    while (true) {
      ptr1 = strchr(ptr + i + 1, '\'');
      if (!ptr1) {
        return(i + (long)C.strlen(&ptr[i]));
      }
      if (ptr1[1] != '\'') return(ptr1 - ptr + 1);
      i = ptr1 - ptr + 1;
    }
  }
  if (ispunct((unsigned char)tmpchr)) return (1);
  while (true) {
    tmpchr = ptr[i];
    if (!isalnum((unsigned char)tmpchr)) return (i);
    i++;
  }
  bug(2308);
  return(0);
}


int texSortCmp(const void *key1, const void *key2)
{

  return (strcmp( ((struct texDef_struct *)key1)->tokenName,
      ((struct texDef_struct *)key2)->tokenName));
}



int texSrchCmp(const void *key, const void *data)
{

  return (strcmp(key,
      ((struct texDef_struct *)data)->tokenName));
}




vstring asciiToTt(vstring s)
{

  vstring ttstr = "";
  vstring tmp = "";
  long i, j, k;

  let(&ttstr, s);
  j = (long)C.strlen(ttstr);


  for (i = 0; i < j; i++) {
    k = 1;
    if (!g_htmlFlag) {
      switch (ttstr[i]) {

        case ' ':
        case '$':
        case '%':
        case '#':
        case '{':
        case '}':
        case '&':
          let(&ttstr,cat(left(ttstr,i),"\\",right(ttstr,i+1),null));
          k = 2;
          break;
        case '^':
          let(&ttstr,cat(left(ttstr,i),"\\^{ }",right(ttstr,i+2),null));
          k = 5;
          break;
        case '\\':
        case '|':
        case '<':
        case '>':
        case '"':
        case '~':
        case '_':
          let(&ttstr,cat(left(ttstr,i),"\\char`\\",right(ttstr,i+1),null));
          k = 8;
          break;
      }
    } else {
      switch (ttstr[i]) {

        case '<':



          if (!strcmp(mid(ttstr, i + 1, 6), "<HTML>")) {
            let(&ttstr, ttstr);
            i = i + 6;
            break;
          }
          if (!strcmp(mid(ttstr, i + 1, 7), "</HTML>")) {
            let(&ttstr, ttstr);
            i = i + 7;
            break;
          }
          let(&ttstr,cat(left(ttstr,i),"&lt;",right(ttstr,i+2),null));
          k = 4;
          break;
        case '>':
          let(&ttstr,cat(left(ttstr,i),"&gt;",right(ttstr,i+2),null));
          k = 4;
          break;
        case '"':
          let(&ttstr,cat(left(ttstr,i),"&quot;",right(ttstr,i+2),null));
          k = 6;
          break;
      }
    }

    if (k > 1) {
      i = i + k - 1;
      j = j + k - 1;
    }
  }

  let(&tmp, "");
  return(ttstr);
}





vstring tokenToTex(vstring mtoken, long statemNum )
{
  vstring tex = "";
  vstring tmpStr;
  long i, j, k;
  void *texDefsPtr;
  flag saveOutputToString;

  if (!g_texDefsRead) {
    bug(2320);
  }

  texDefsPtr = (void *)bsearch(mtoken, g_TexDefs, (size_t)numSymbs,
      sizeof(struct texDef_struct), texSrchCmp);
  if (texDefsPtr) {
    let(&tex, ((struct texDef_struct *)texDefsPtr)->texEquiv);
  } else {

    saveOutputToString = g_outputToString;
    g_outputToString = 0;

    if (statemNum < 0 || statemNum > g_statements) bug(2331);
    if (statemNum > 0) {
      printLongLine(cat("?Warning: In the comment for statement \"",
          g_Statement[statemNum].labelName,
          "\", math symbol token \"", mtoken,
          "\" does not have a LaTeX and/or an HTML definition.", null),
          "", " ");
    } else {
      printLongLine(cat("?Warning: Math symbol token \"", mtoken,
          "\" does not have a LaTeX and/or an HTML definition.", null),
          "", " ");
    }
    g_outputToString = saveOutputToString;



    let(&tex, mtoken);




    if (tex[0] == '~') {
      if (isalpha((unsigned char)(tex[1]))) {
        let(&tex, right(tex, 2));
      }
    }


    j = (long)C.strlen(tex);
    for (i = 0; i < j; i++) {
      if (ispunct((unsigned char)(tex[i]))) {
        tmpStr = asciiToTt(chr(tex[i]));
        if (!g_htmlFlag)
          let(&tmpStr, cat("{\\tt ", tmpStr, "}", null));
        k = (long)C.strlen(tmpStr);
        let(&tex,
            cat(left(tex, i), tmpStr, right(tex, i + 2), null));
        i = i + k - 1;
        j = j + k - 1;
        let(&tmpStr, "");
      }
    }


    if (!g_htmlFlag)
      let(&tex, cat("\\mbox{\\rm ", tex, "}", null));

  }

  return (tex);
}


vstring asciiMathToTex(vstring mathComment, long statemNum)
{

  vstring tex;
  vstring texLine = "";
  vstring lastTex = "";
  vstring token = "";
  flag alphnew, alphold, unknownnew, unknownold;

  long i;
  vstring srcptr;

  srcptr = mathComment;

  let(&texLine, "");
  let(&lastTex, "");

  while(true) {
    i = whiteSpaceLen(srcptr);
    srcptr = srcptr + i;
    i = tokenLen(srcptr);
    if (!i) break;
    let(&token, space(i));
    memcpy(token, srcptr, (size_t)i);
    srcptr = srcptr + i;
    tex = tokenToTex(token, statemNum);


    if (!g_htmlFlag) {

      alphnew = !!isalpha((unsigned char)(tex[0]));
      unknownnew = 0;
      if (!strcmp(left(tex, 10), "\\mbox{\\rm ")) {
        unknownnew = 1;
      }
      alphold = !!isalpha((unsigned char)(lastTex[0]));
      unknownold = 0;
      if (!strcmp(left(lastTex, 10), "\\mbox{\\rm ")) {
        unknownold = 1;
      }


      if ((alphold || unknownold) && (alphnew || unknownnew)) {

        let(&texLine, cat(texLine, "\\,", tex, " ", null));
      } else {
        let(&texLine, cat(texLine, tex, " ", null));
      }
    } else {
      let(&texLine, cat(texLine, tex, null));
    }
    let(&lastTex, "");
    lastTex = tex;



  }
  let(&lastTex, "");
  let(&token, "");

  return (texLine);

}


vstring getCommentModeSection(vstring *srcptr, char *mode)
{
  vstring modeSection = "";
  vstring ptr;
  flag addMode = 0;
  if (!g_outputToString) bug(2319);

  if ((*srcptr)[0] != DOLLAR_SUBST ) {
    if ((*srcptr)[0] == 0) {
      *mode = 0;
      return ("");
    } else {
      *mode = 'n';
      addMode = 1;
    }
  } else {
    switch ((*srcptr)[1]) {
      case 'l':
      case 'm':
      case 'n':
        *mode = (*srcptr)[1];
        break;
      case ')':
        bug(2317);

        *mode = 0;
        return ("");
        break;
      default:
        *mode = 'n';
        break;
    }
  }

  ptr = (*srcptr) + 1;
  while (true) {
    if (ptr[0] == DOLLAR_SUBST ) {
      switch (ptr[1]) {
        case 'l':
        case 'm':
        case 'n':
        case ')':
          if (ptr[1] == ')') bug(2318);
          let(&modeSection, space(ptr - (*srcptr)));
          memcpy(modeSection, *srcptr, (size_t)(ptr - (*srcptr)));
          if (addMode) {
            let(&modeSection, cat(chr(DOLLAR_SUBST), "n",  modeSection,
                null));
          }
          *srcptr = ptr;
          return (modeSection);
          break;
      }
    } else {
      if (ptr[0] == 0) {
          let(&modeSection, space(ptr - (*srcptr)));
          memcpy(modeSection, *srcptr, (size_t)(ptr - (*srcptr)));
          if (addMode) {
            let(&modeSection, cat(chr(DOLLAR_SUBST), "n",  modeSection,
                null));
          }
          *srcptr = ptr;
          return (modeSection);
      }
    }
    ptr++;
  }
  return(null);
}


void printTexHeader(flag texHeaderFlag)
{

  long i, j, k;
  vstring tmpStr = "";


  vstring localSandboxTitle = "";
  vstring hugeHdr = "";
  vstring bigHdr = "";
  vstring smallHdr = "";
  vstring tinyHdr = "";
  vstring hugeHdrComment = "";
  vstring bigHdrComment = "";
  vstring smallHdrComment = "";
  vstring tinyHdrComment = "";


  if (2 == readTexDefs(0, 0 )) {
    mminou.print2(
       "?There was an error in the $t comment's LaTeX/HTML definitions.\n");
    return;
  }


  g_outputToString = 1;

  if (!g_htmlFlag) {
    mminou.print2("%s This LaTeX file was created by Metamath on %s %s.\n",
       "%", date(), time_());


    if (texHeaderFlag && !g_oldTexFlag) {
      mminou.print2("\\documentclass{article}\n");
      mminou.print2("\\usepackage{graphicx} %% For rotated iota\n");
      mminou.print2("\\usepackage{amssymb}\n");
      mminou.print2("\\usepackage{amsmath} %% For \\begin{align}...\n");
      mminou.print2("\\usepackage{amsthm}\n");
      mminou.print2("\\theoremstyle{plain}\n");
      mminou.print2("\\newtheorem{theorem}{Theorem}[section]\n");
      mminou.print2("\\newtheorem{definition}[theorem]{Definition}\n");
      mminou.print2("\\newtheorem{lemma}[theorem]{Lemma}\n");
      mminou.print2("\\newtheorem{axiom}{Axiom}\n");
      mminou.print2("\\allowdisplaybreaks[1] %% Allow page breaks in {align}\n");
      mminou.print2("\\usepackage[plainpages=false,pdfpagelabels]{hyperref}\n");
      mminou.print2("\\hypersetup{colorlinks} %% Get rid of boxes around links\n");

      mminou.print2("\\begin{document}\n");
      mminou.print2("\n");
    }

    if (texHeaderFlag && g_oldTexFlag) {

      mminou.print2(
    "\\documentclass[leqno]{article}\n");

      mminou.print2("\\usepackage{graphicx}\n");
      mminou.print2(
    "\\usepackage{amssymb}\n");
      mminou.print2(
"\\raggedbottom\n");
      mminou.print2(
"\\raggedright\n");
      mminou.print2(
"%%\\title{Your title here}\n");
      mminou.print2(
"%%\\author{Your name here}\n");
      mminou.print2(
"\\begin{document}\n");
      mminou.print2(
"%%\\maketitle\n");
      mminou.print2(
"\\newbox\\mlinebox\n");
      mminou.print2(
"\\newbox\\mtrialbox\n");
      mminou.print2(
"\\newbox\\startprefix  %% Prefix for first line of a formula\n");
      mminou.print2(
"\\newbox\\contprefix  %% Prefix for continuation line of a formula\n");
      mminou.print2(
"\\def\\startm{  %% Initialize formula line\n");
      mminou.print2(
"  \\setbox\\mlinebox=\\hbox{\\unhcopy\\startprefix}\n");
      mminou.print2(
"}\n");
      mminou.print2(
"\\def\\m#1{  %% Add a symbol to the formula\n");
      mminou.print2(
"  \\setbox\\mtrialbox=\\hbox{\\unhcopy\\mlinebox $\\,#1$}\n");
      mminou.print2(
"  \\ifdim\\wd\\mtrialbox>\\hsize\n");
      mminou.print2(
"    \\box\\mlinebox\n");
      mminou.print2(
"    \\setbox\\mlinebox=\\hbox{\\unhcopy\\contprefix $\\,#1$}\n");
      mminou.print2(
"  \\else\n");
      mminou.print2(
"    \\setbox\\mlinebox=\\hbox{\\unhbox\\mtrialbox}\n");
      mminou.print2(
"  \\fi\n");
      mminou.print2(
"}\n");
      mminou.print2(
"\\def\\endm{  %% Output the last line of a formula\n");
      mminou.print2(
"  \\box\\mlinebox\n");
      mminou.print2(
"}\n");
    }
  } else {

    mminou.print2(
        "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n");
    mminou.print2(     "    \"http://www.w3.org/TR/html4/loose.dtd\">\n");
    mminou.print2("<HTML LANG=\"EN-US\">\n");
    mminou.print2("<HEAD>\n");
    mminou.print2("%s%s\n", "<META HTTP-EQUIV=\"Content-Type\" ",
        "CONTENT=\"text/html; charset=iso-8859-1\">");


    mminou.print2(
"<META NAME=\"viewport\" CONTENT=\"width=device-width, initial-scale=1.0\">\n"
        );

    mminou.print2("<STYLE TYPE=\"text/css\">\n");
    mminou.print2("<!--\n");



    mminou.print2("img { margin-bottom: -4px }\n");
#ifndef RAINBOW_OPTION

    mminou.print2(".p { font-family: \"Arial Narrow\";\n");
    mminou.print2("     font-size: x-small;\n");

    printLongLine(cat("     color: ", seg(PINK_NUMBER_COLOR, 2,
        (long)C.strlen(PINK_NUMBER_COLOR) - 1), ";", null), "", "&");
    mminou.print2("   }\n");
#else
    mminou.print2(".r { font-family: \"Arial Narrow\";\n");
    mminou.print2("     font-size: x-small;\n");

    mminou.print2("   }\n");
#endif

#ifdef INDENT_HTML_PROOFS



    mminou.print2(".i { font-family: \"Arial Narrow\";\n");
    mminou.print2("     font-size: x-small;\n");
    mminou.print2("     color: gray;\n");
    mminou.print2("   }\n");
#endif
    mminou.print2("-->\n");
    mminou.print2("</STYLE>\n");
    printLongLine(g_htmlCSS, "", " ");



    if (g_showStatement < g_extHtmlStmt) {
      mminou.print2("%s\n", cat("<TITLE>",

          left(g_texFileName, (long)C.strlen(g_texFileName) - 5),

          " - ", htmlTitle,
          "</TITLE>", null));

    } else if (g_showStatement < g_mathboxStmt) {
      mminou.print2("%s\n", cat("<TITLE>",

          left(g_texFileName, (long)C.strlen(g_texFileName) - 5),

          " - ", g_extHtmlTitle,
          "</TITLE>", null));


    } else {



      for (i = g_showStatement; i > g_mathboxStmt; i--) {
        if (g_Statement[i].type == a_ || g_Statement[i].type == p_) {

          getSectionHeadings(i, &hugeHdr, &bigHdr, &smallHdr,
              &tinyHdr,

              &hugeHdrComment, &bigHdrComment, &smallHdrComment,
              &tinyHdrComment,
              0,
              0  );
          if (bigHdr[0] != 0) break;
        }
      }
      if (bigHdr[0]) {

        let(&localSandboxTitle, bigHdr);
      } else {
        let(&localSandboxTitle, sandboxTitle);
      }
      let(&hugeHdr, "");
      let(&bigHdr, "");
      let(&smallHdr, "");
      let(&tinyHdr, "");
      let(&hugeHdrComment, "");
      let(&bigHdrComment, "");
      let(&smallHdrComment, "");
      let(&tinyHdrComment, "");


      printLongLine(cat("<TITLE>",

          left(g_texFileName, (long)C.strlen(g_texFileName) - 5),



          " - ", localSandboxTitle,
          "</TITLE>", null), "", "\"");

    }

    mminou.print2("%s%s\n", "<LINK REL=\"shortcut icon\" HREF=\"favicon.ico\" ",
        "TYPE=\"image/x-icon\">");

    mminou.print2("</HEAD>\n");


    mminou.print2("<BODY BGCOLOR=\"#FFFFFF\">\n");


    mminou.print2("<TABLE BORDER=0 CELLSPACING=0 CELLPADDING=0 WIDTH=\"100%s\">\n",
         "%");
    mminou.print2("  <TR>\n");
    mminou.print2("    <TD ALIGN=LEFT VALIGN=TOP WIDTH=\"25%s\"><A HREF=\n", "%");
    mminou.print2("    \"%s\"><IMG SRC=\"%s\"\n",
        (g_showStatement < g_extHtmlStmt ? g_htmlHomeHREF :
             (g_showStatement < g_mathboxStmt ? extHtmlHomeHREF :
             sandboxHomeHREF)),

        (g_showStatement < g_extHtmlStmt ? g_htmlHomeIMG :
             (g_showStatement < g_mathboxStmt ? extHtmlHomeIMG :
             sandboxHomeIMG)));
    mminou.print2("      BORDER=0\n");
    mminou.print2("      ALT=\"%s\"\n",
        (g_showStatement < g_extHtmlStmt ? htmlTitleAbbr :
             (g_showStatement < g_mathboxStmt ? g_extHtmlTitleAbbr :
             sandboxTitleAbbr)));
    mminou.print2("      TITLE=\"%s\"\n",
        (g_showStatement < g_extHtmlStmt ? htmlTitleAbbr :
             (g_showStatement < g_mathboxStmt ? g_extHtmlTitleAbbr :
             sandboxTitleAbbr)));
    mminou.print2(
      "      HEIGHT=32 WIDTH=32 ALIGN=TOP STYLE=\"margin-bottom:0px\"></A>\n");
    mminou.print2("    </TD>\n");
    mminou.print2(
"    <TD ALIGN=CENTER COLSPAN=2 VALIGN=TOP><FONT SIZE=\"+3\" COLOR=%s><B>\n",

      GREEN_TITLE_COLOR);
    mminou.print2("%s\n",
        (g_showStatement < g_extHtmlStmt ? htmlTitle :
             (g_showStatement < g_mathboxStmt ? g_extHtmlTitle :
             localSandboxTitle)));
    mminou.print2("      </B></FONT></TD>\n");






    if (texHeaderFlag) {

      mminou.print2("    <TD ALIGN=RIGHT VALIGN=TOP WIDTH=\"25%s\">\n", "%");
      mminou.print2("      <FONT SIZE=-1 FACE=sans-serif>\n");

      j = 0;
      k = 0;
      for (i = g_showStatement - 1; i >= 1; i--) {
        if ((g_Statement[i].type == (char)p_ ||
            g_Statement[i].type == (char)a_ )

            ) {
          j = i;
          break;
        }
      }
      if (j == 0) {
        k = 1;

        for (i = g_statements; i >= 1; i--) {
          if ((g_Statement[i].type == (char)p_ ||
              g_Statement[i].type == (char)a_ )

              ) {
            j = i;
            break;
          }
        }
      }
      if (j == 0) bug(2314);
      mminou.print2("      <A HREF=\"%s.html\">\n",
          g_Statement[j].labelName);
      if (!k) {
        mminou.print2("      &lt; Previous</A>&nbsp;&nbsp;\n");
      } else {
        mminou.print2("      &lt; Wrap</A>&nbsp;&nbsp;\n");
      }

      j = 0;
      k = 0;
      for (i = g_showStatement + 1; i <= g_statements; i++) {
        if ((g_Statement[i].type == (char)p_ ||
            g_Statement[i].type == (char)a_ )

            ) {
          j = i;
          break;
        }
      }
      if (j == 0) {
        k = 1;

        for (i = 1; i <= g_statements; i++) {
          if ((g_Statement[i].type == (char)p_ ||
              g_Statement[i].type == (char)a_ )

              ) {
            j = i;
            break;
          }
        }
      }
      if (j == 0) bug(2315);

      if (!k) {
        mminou.print2("      <A HREF=\"%s.html\">Next &gt;</A>\n",
            g_Statement[j].labelName);
      } else {
        mminou.print2("      <A HREF=\"%s.html\">Wrap &gt;</A>\n",
            g_Statement[j].labelName);
      }

      mminou.print2("      </FONT><FONT FACE=sans-serif SIZE=-2>\n");


      i = ((g_Statement[g_showStatement].pinkNumber - 1) / 100) + 1;

      let(&tmpStr, cat("mmtheorems", str((double)i), ".html#",
          g_Statement[g_showStatement].labelName, null));

      printLongLine(cat("      <BR><A HREF=\"", tmpStr,
            "\">Nearby theorems</A>", null), " ", " ");


      mminou.print2("      </FONT>\n");
      mminou.print2("    </TD>\n");
      mminou.print2("  </TR>\n");
      mminou.print2("  <TR>\n");
      mminou.print2("    <TD COLSPAN=2 ALIGN=LEFT VALIGN=TOP><FONT SIZE=-2\n");
      mminou.print2("      FACE=sans-serif>\n");
      mminou.print2("      <A HREF=\"../mm.html\">Mirrors</A>&nbsp; &gt;\n");
      mminou.print2("      &nbsp;<A HREF=\"../index.html\">Home</A>&nbsp; &gt;\n");
      mminou.print2("      &nbsp;<A HREF=\"%s\">%s</A>&nbsp; &gt;\n",
          (g_showStatement < g_extHtmlStmt ? g_htmlHomeHREF :
               (g_showStatement < g_mathboxStmt ? extHtmlHomeHREF :
               g_htmlHomeHREF)),
          (g_showStatement < g_extHtmlStmt ? htmlTitleAbbr :
               (g_showStatement < g_mathboxStmt ? g_extHtmlTitleAbbr :
               htmlTitleAbbr)));
      mminou.print2("      &nbsp;<A HREF=\"mmtheorems.html\">Th. List</A>&nbsp; &gt;\n");
      if (g_showStatement >= g_mathboxStmt) {
        mminou.print2("      &nbsp;<A HREF=\"mmtheorems.html#sandbox:bighdr\">\n");
        mminou.print2("      Mathboxes</A>&nbsp; &gt;\n");
      }
      mminou.print2("      &nbsp;%s\n",

          left(g_texFileName, (long)C.strlen(g_texFileName) - 5));
      mminou.print2("      </FONT>\n");
      mminou.print2("    </TD>\n");
      mminou.print2("    <TD COLSPAN=2 ALIGN=RIGHT VALIGN=TOP>\n");

      mminou.print2("      <FONT SIZE=-2 FACE=sans-serif>\n");




      let(&tmpStr, htmlExtUrl);
      i = 1;
      while (true) {
        i = instr(i, tmpStr, "*");
        if (i == 0) break;
        let(&tmpStr, cat(left(tmpStr, i - 1),
            g_Statement[g_showStatement].labelName,
            right(tmpStr, i + 1), null));
      }
      printLongLine(tmpStr, "", " ");



      if (htmlDir[0]) {

        if (g_altHtmlFlag) {
          mminou.print2("      <A HREF=\"%s%s\">GIF version</A>\n",
                htmlDir, g_texFileName);

        } else {
          mminou.print2("      <A HREF=\"%s%s\">Unicode version</A>\n",
                altHtmlDir, g_texFileName);

        }
      }

    } else {

      mminou.print2("      <TD ALIGN=RIGHT VALIGN=TOP\n");
      mminou.print2("       ><FONT FACE=sans-serif SIZE=-2>\n", "%");


      if (htmlDir[0]) {
        mminou.print2("\n");
        if (g_altHtmlFlag) {
          mminou.print2("This is the Unicode version.<BR>\n");
          mminou.print2("<A HREF=\"%s%s\">Change to GIF version</A>\n",
              htmlDir, g_texFileName);
        } else {
          mminou.print2("This is the GIF version.<BR>\n");
          mminou.print2("<A HREF=\"%s%s\">Change to Unicode version</A>\n",
              altHtmlDir, g_texFileName);
        }
      }
      else {
        mminou.print2("&nbsp;\n");
      }

    }

    mminou.print2("      </FONT>\n");
    mminou.print2("    </TD>\n");
    mminou.print2("  </TR>\n");
    mminou.print2("</TABLE>\n");



    mminou.print2("<HR NOSHADE SIZE=1>\n");

  }
  fprintf(g_texFilePtr, "%s", g_printString);
  g_outputToString = 0;
  let(&g_printString, "");


  let(&tmpStr, "");

}



flag printTexComment(vstring commentPtr, flag htmlCenterFlag,

    long actionBits,


    flag noFileCheck)
{
  vstring cmtptr;
  vstring srcptr;
  vstring lineStart;
  vstring tmpStr = "";
  vstring modeSection;
  vstring sourceLine = "";
  vstring outputLine = "";
  vstring tmp = "";
  flag textMode, mode, lastLineFlag, displayMode;
  vstring tmpComment = "";


  flag preformattedMode = 0;


  vstring bibTag = "";
  vstring bibFileName = "";
  vstring bibFileContents = "";
  vstring bibFileContentsUpper = "";
  vstring bibTags = "";
  long pos1, pos2, htmlpos1, htmlpos2, saveScreenWidth;
  flag tmpMathMode;


  vstring cmt = "";
  vstring cmtMasked = "";

  vstring tmpMasked = "";
  vstring tmpStrMasked = "";
  long i, clen;
  flag returnVal = 0;



  flag errorsOnly;
  flag processSymbols;
  flag processLabels;
  flag addColoredLabelNumber;
  flag processBibrefs;
  flag processUnderscores;
  flag convertToHtml;
  flag metamathComment;


  errorsOnly = (actionBits & ERRORS_ONLY ) != 0;
  processSymbols = (actionBits & PROCESS_SYMBOLS ) != 0;
  processLabels = (actionBits & PROCESS_LABELS ) != 0;
  addColoredLabelNumber = (actionBits & ADD_COLORED_LABEL_NUMBER ) != 0;
  processBibrefs = (actionBits & PROCESS_BIBREFS ) != 0;
  processUnderscores = (actionBits & PROCESS_UNDERSCORES ) != 0;
  convertToHtml = (actionBits & CONVERT_TO_HTML ) != 0;
  metamathComment = (actionBits & METAMATH_COMMENT ) != 0;


  if (g_outputToString) bug(2309);

  if (errorsOnly == 0) {
    if (!g_texFilePtr) bug(2321);
  }

  cmtptr = commentPtr;

  if (!g_texDefsRead) {
    return returnVal;
  }

  if (metamathComment != 0) {
    i = instr(1, cmtptr, "$)");
    if (!i) i = (long)C.strlen(cmtptr) + 1;
  } else {
    i = (long)C.strlen(cmtptr) + 1;
  }
  let(&cmt, left(cmtptr, i - 1));

  let(&cmtMasked, cmt);



  if (g_htmlFlag) {


    if (convertToHtml != 0) {
      if (instr(1, cmt, "<HTML>") != 0) preformattedMode = 1;
    } else {
      preformattedMode = 1;
    }
    mode = 1;
    let(&tmp, "");
    let(&tmpMasked, "");
    while (true) {
      pos1 = 0;
      while (true) {
        pos1 = instr(pos1 + 1, cmt, "`");
        if (!pos1) break;
        if (cmt[pos1] == '`') {
          pos1++;
          continue;
        }
        break;
      }
      if (!pos1) pos1 = (long)C.strlen(cmt) + 1;
      if (mode == 1 && preformattedMode == 0) {
        let(&tmpStr, "");

        tmpStr = asciiToTt(left(cmt, pos1));
        let(&tmpStrMasked, tmpStr);
      } else {
        let(&tmpStr, left(cmt, pos1));

        if (mode == -1) {
          let(&tmpStrMasked, cat(space(pos1 - 1),
              mid(cmtMasked, pos1, 1), null));
        } else {
          let(&tmpStrMasked, left(cmtMasked, pos1));
        }
      }
      let(&tmp, cat(tmp, tmpStr, null));
      let(&tmpMasked, cat(tmpMasked, tmpStrMasked, null));
      let(&cmt, right(cmt, pos1 + 1));
      let(&cmtMasked, right(cmtMasked, pos1 + 1));
      if (!cmt[0]) break;
      mode = (char)(-mode);
    }
    let(&cmt, tmp);
    let(&cmtMasked, tmpMasked);
    let(&tmpStr, "");
    let(&tmpStrMasked, "");
  }


  if (g_htmlFlag) {

    if (htmlCenterFlag) {
      let(&cmt, cat("<CENTER><TABLE><TR><TD ALIGN=LEFT><B>Description: </B>",
          cmt, "</TD></TR></TABLE></CENTER>", null));
      let(&cmtMasked,
          cat("<CENTER><TABLE><TR><TD ALIGN=LEFT><B>Description: </B>",
          cmtMasked, "</TD></TR></TABLE></CENTER>", null));
    }
  }




  if (g_htmlFlag != 0


      ) {
    pos1 = 0;
    while (true) {
      pos1 = instr(pos1 + 1, cmtMasked, "~");
      if (!pos1) break;
      if (cmtMasked[pos1] == '~') {
        pos1++;
        continue;
      }

      while (true) {
        if (cmtMasked[pos1] == 0) break;
        if (isspace((unsigned char)(cmtMasked[pos1]))) {
          pos1++;
          continue;
        } else {
          break;
        }
      }

      while (true) {
        if (cmtMasked[pos1] == 0) break;
        if (!(isspace((unsigned char)(cmtMasked[pos1])))) {
          if (cmtMasked[pos1] == '_') {

            cmtMasked[pos1] = '?';
          }
          pos1++;
          continue;
        } else {
          break;
        }
      }
    }
  }






  if (!g_htmlFlag) {




    pos1 = 0;
    while (true) {
      pos1 = instr(pos1 + 1, cmt, "$");
      if (!pos1) break;

      if (pos1 > instr(1, cmt, "<HTML>") && pos1 < instr(1, cmt, "</HTML>"))
        continue;
      let(&cmt, cat(left(cmt, pos1 - 1), "\\$",
          right(cmt, pos1 + 1), null));
      let(&cmtMasked, cat(left(cmtMasked, pos1 - 1), "\\$",
          right(cmtMasked, pos1 + 1), null));
      pos1 = pos1 + 1;
    }
  }




  if (!g_htmlFlag) {

    pos1 = instr(1, cmt, "} ");
    if (pos1) {
      pos1++;
    } else {
      pos1 = 1;
    }
    pos2 = (long)C.strlen(cmt);
    tmpMathMode = 0;
    for (pos1 = pos1 + 0; pos1 <= pos2; pos1++) {
      if (cmt[pos1 - 1] == '`') tmpMathMode = (flag)(1 - tmpMathMode);
      if (tmpMathMode) continue;
      if (pos1 > 1) {
        if (cmt[pos1 - 1] == '_' && cmt[pos1 - 2] == '$') {

          continue;
        }
      }




      switch(cmt[pos1 - 1]) {
        case '|': cmt[pos1 - 1] = '/'; break;
        case '<': cmt[pos1 - 1] = '{'; break;
        case '>': cmt[pos1 - 1] = '}'; break;
        case '_': cmt[pos1 - 1] = '-'; break;
      }
      if (strchr("%#{}&^|<>_", cmt[pos1 - 1]) != null) {
        let(&tmpStr, "");
        tmpStr = asciiToTt(chr(cmt[pos1 - 1]));
        let(&cmt, cat(left(cmt, pos1 - 1), tmpStr,
            right(cmt, pos1 + 1), null));
        let(&cmtMasked, cat(left(cmtMasked, pos1 - 1), tmpStr,
            right(cmtMasked, pos1 + 1), null));
        pos1 += (long)C.strlen(tmpStr) - 1;
        pos2 += (long)C.strlen(tmpStr) - 1;
      }
    }
  }



  if (g_htmlFlag != 0
      && processUnderscores != 0) {
    pos1 = 0;
    while (true) {


      pos1 = instr(pos1 + 1, cmtMasked, "_");
      if (!pos1) break;

      if (pos1 > instr(1, cmt, "<HTML>") && pos1 < instr(1, cmt, "</HTML>"))
        continue;



      pos2 = pos1 - 1;
      while (true) {
        if (pos2 == 0 || isspace((unsigned char)(cmt[pos2]))) break;
        pos2--;
      }
      if (!strcmp(mid(cmt, pos2 + 2, 7), "http://")) {
        continue;
      }

      if (!strcmp(mid(cmt, pos2 + 2, 8), "https://")) {
        continue;
      }

      if (!strcmp(mid(cmt, pos2 + 2, 2), "mm")) {
        continue;
      }


      if (pos1 > 1) {

        if (!isspace((unsigned char)(cmt[pos1 - 2]))
            && strchr(OPENING_PUNCTUATION, cmt[pos1 - 2]) == null) {

          if (!isspace((unsigned char)(cmt[pos1]))
            && strchr(CLOSING_PUNCTUATION, cmt[pos1]) == null) {




            pos2 = pos1 + 1;
            while (true) {
              if (!cmt[pos2]) break;

              if (isspace((unsigned char)(cmt[pos2]))
                  || strchr(OPENING_PUNCTUATION, cmt[pos2]) != null
                  || strchr(CLOSING_PUNCTUATION, cmt[pos2]) != null) break;
              pos2++;
            }
            pos2++;
            if (g_htmlFlag) {

              let(&cmt, cat(left(cmt, pos1 - 1),
                  "<SUB><FONT SIZE=\"-1\">",
                  seg(cmt, pos1 + 1, pos2 - 1),
                  "</FONT></SUB>", right(cmt, pos2), null));
              let(&cmtMasked, cat(left(cmtMasked, pos1 - 1),
                  "<SUB><FONT SIZE=\"-1\">",
                  seg(cmtMasked, pos1 + 1, pos2 - 1),
                  "</FONT></SUB>", right(cmtMasked, pos2), null));
              pos1 = pos2 + 33;
            } else {

              let(&cmt, cat(left(cmt, pos1 - 1), "$_{",
                  seg(cmt, pos1 + 1, pos2 - 1),
                  "}$", right(cmt, pos2), null));
              let(&cmtMasked, cat(left(cmtMasked, pos1 - 1), "$_{",
                  seg(cmtMasked, pos1 + 1, pos2 - 1),
                  "}$", right(cmtMasked, pos2), null));
              pos1 = pos2 + 4;
            }
            continue;


          } else {


            continue;
          }
        }
      }
      if (!isalnum((unsigned char)(cmt[pos1]))) continue;


      pos2 = instr(pos1 + 1, cmtMasked, "_");
      if (!pos2) break;

      if (!isalnum((unsigned char)(cmt[pos2 - 2]))) continue;
      if (isalnum((unsigned char)(cmt[pos2]))) continue;
      if (g_htmlFlag) {
        let(&cmt, cat(left(cmt, pos1 - 1), "<I>",
            seg(cmt, pos1 + 1, pos2 - 1),
            "</I>", right(cmt, pos2 + 1), null));
        let(&cmtMasked, cat(left(cmtMasked, pos1 - 1), "<I>",
            seg(cmtMasked, pos1 + 1, pos2 - 1),
            "</I>", right(cmtMasked, pos2 + 1), null));
        pos1 = pos2 + 5;
      } else {
        let(&cmt, cat(left(cmt, pos1 - 1), "{\\em ",
            seg(cmt, pos1 + 1, pos2 - 1),
            "}", right(cmt, pos2 + 1), null));
        let(&cmtMasked, cat(left(cmtMasked, pos1 - 1), "{\\em ",
            seg(cmtMasked, pos1 + 1, pos2 - 1),
            "}", right(cmtMasked, pos2 + 1), null));
        pos1 = pos2 + 4;
      }
    }
  }




  if (!g_htmlFlag) {
    i = 1;
    pos1 = 0;
    while (true) {

      pos1 = instr(pos1 + 1, cmtMasked, "\"");
      if (pos1 == 0) break;
      if (i == 1) {
        let(&cmt, cat(left(cmt, pos1 - 1), "````",
            right(cmt, pos1 + 1), null));
        let(&cmtMasked, cat(left(cmtMasked, pos1 - 1), "````",
            right(cmtMasked, pos1 + 1), null));
      }
      i = 1 - i;
    }
  }


  if (g_htmlFlag
      && processBibrefs != 0
      ) {

    if (g_showStatement < g_extHtmlStmt) {
      let(&bibTags, g_htmlBibliographyTags);
      let(&bibFileName, g_htmlBibliography);

    } else if (g_showStatement < g_mathboxStmt) {
      let(&bibTags, extHtmlBibliographyTags);
      let(&bibFileName, extHtmlBibliography);


    } else {
      let(&bibTags, g_htmlBibliographyTags);
      let(&bibFileName, g_htmlBibliography);

    }
    if (bibFileName[0]) {
      pos1 = 0;
      while (true) {




        pos1 = instr(pos1 + 1, cmtMasked, "[");
        if (!pos1) break;



        if (cmtMasked[pos1] == '[') {

          let(&cmt, cat(left(cmt, pos1 - 1),
              right(cmt, pos1 + 1), null));
          let(&cmtMasked, cat(left(cmtMasked, pos1 - 1),
              right(cmtMasked, pos1 + 1), null));

          continue;
        }



        pos2 = instr(pos1 + 1, cmtMasked, "]");
        if (!pos2) break;








        let(&bibTag, seg(cmtMasked, pos1, pos2));

        if ((signed)(strcspn(bibTag, " \n\r\t\f")) < pos2 - pos1 + 1) continue;


        if (noFileCheck == 0) {
          if (!bibTags[0]) {

            let(&bibFileContents, "");
            if (errorsOnly == 0) {
              mminou.print2("Reading HTML bibliographic tags from file \"%s\"...\n",
                  bibFileName);
            }
            bibFileContents = readFileToString(bibFileName, 0,
                &i );
            if (!bibFileContents) {
              printLongLine(cat("?Warning: Couldn't open or read the file \"",
                  bibFileName,
                  "\".  The bibliographic hyperlinks will not be checked for",
                  " correctness.  The first one is \"", bibTag,
                  "\" in the comment for statement \"",
                  g_Statement[g_showStatement].labelName, "\".",
                  null), "", " ");
              returnVal = 1;
              bibFileContents = "";
              let(&bibTags, "?");
            } else {

              let(&bibFileContents, edit(bibFileContents, 2));

              let(&bibFileContentsUpper, edit(bibFileContents, 32));
              htmlpos1 = 0;
              while (true) {
                htmlpos1 = instr(htmlpos1 + 1, bibFileContentsUpper, "<ANAME=");
                if (!htmlpos1) break;
                htmlpos1 = htmlpos1 + 7;

                if (bibFileContents[htmlpos1 - 1] == '\''
                    || bibFileContents[htmlpos1 - 1] == '"') htmlpos1++;
                htmlpos2 = instr(htmlpos1, bibFileContents, ">");
                if (!htmlpos2) break;
                htmlpos2--;
                if (bibFileContents[htmlpos2 - 1] == '\''
                    || bibFileContents[htmlpos2 - 1] == '"') htmlpos2--;
                if (htmlpos2 <= htmlpos1) continue;
                let(&tmp, cat("[",
                    seg(bibFileContents, htmlpos1, htmlpos2), "]", null));

                if (instr(1, bibTags, tmp)) {
                  printLongLine(cat("?Error: There two occurrences of",
                      " bibliographic reference \"",
                      seg(bibFileContents, htmlpos1, htmlpos2),
                      "\" in the file \"", bibFileName, "\".", null), "", " ");
                  returnVal = 1;
                }

                let(&bibTags, cat(bibTags, tmp, null));
              }
              if (!bibTags[0]) {

                let(&bibTags, "[");
              }
            }
          }

          if (g_showStatement < g_extHtmlStmt) {
            let(&g_htmlBibliographyTags, bibTags);

          } else if (g_showStatement < g_mathboxStmt) {

            let(&extHtmlBibliographyTags, bibTags);


          } else {
            let(&g_htmlBibliographyTags, bibTags);

          }

        }

        if (bibTags[0] == '[') {

          if (!instr(1, bibTags, bibTag)) {
            printLongLine(cat("?Error: The bibliographic reference \"", bibTag,
                "\" in statement \"", g_Statement[g_showStatement].labelName,
                "\" was not found as an <A NAME=\"",
                seg(bibTag, 2, pos2 - pos1),
                "\"></A> anchor in the file \"", bibFileName, "\".", null),
                "", " ");
            returnVal = 1;
          }
        }



        let(&tmp, cat("[<A HREF=\"",
            bibFileName, "#", seg(bibTag, 2, pos2 - pos1), "\">",
            seg(bibTag, 2, pos2 - pos1), "</A>]", null));
        let(&cmt, cat(left(cmt, pos1 - 1), tmp, right(cmt,
            pos2 + 1), null));
        let(&cmtMasked, cat(left(cmtMasked, pos1 - 1), tmp, right(cmtMasked,
            pos2 + 1), null));
        pos1 = pos1 + (long)C.strlen(tmp) - (long)C.strlen(bibTag);
      }
    }
  }


  if (C.strlen(cmt) != C.strlen(cmtMasked)) bug(2334);


  clen = (long)C.strlen(cmt);
  mode = 'n';
  for (i = 0; i < clen; i++) {
    if (cmt[i] == '`') {
      if (cmt[i + 1] == '`') {
        if (processSymbols != 0) {

          let(&cmt, cat(left(cmt, i + 1), right(cmt, i + 3), null));
          clen--;
        }
      } else {

        if (mode != 'm') {
          mode = 'm';
        } else {
          mode = 'n';
        }

        if (processSymbols != 0) {
          let(&cmt, cat(left(cmt, i), chr(DOLLAR_SUBST) , chr(mode),
              right(cmt, i+2), null));
          clen++;
          i++;
        }


        if (mode == 'm'
            && processSymbols != 0
            ) {
          let(&tmp, mid(cmt, i - 2, 2));
          if (!strcmp("( ", tmp)) {
            let(&cmt, cat(left(cmt, i - 2), right(cmt, i), null));
            clen = clen - 1;
          }

          let(&tmp, mid(cmt, i - 8, 8));
          if (!strcmp("&quot; ", right(tmp, 2))
              && strchr("( ", tmp[0]) != null) {
            let(&cmt, cat(left(cmt, i - 2), right(cmt, i), null));
            clen = clen - 1;
          }
          let(&tmp, "");
        }
        if (mode == 'n'
            && processSymbols != 0
            ) {
          let(&tmp, mid(cmt, i + 2, 2));
          if (tmp[0] == ' ' && strchr(CLOSING_PUNCTUATION, tmp[1]) != null) {
            let(&cmt, cat(left(cmt, i + 1), right(cmt, i + 3), null));
            clen = clen - 1;
          }

          let(&tmp, mid(cmt, i + 2, 8));
          if (C.strlen(tmp) < 8)
              let(&tmp, cat(tmp, space(8 - (long)C.strlen(tmp)), null));
          if (!strcmp(" &quot;", left(tmp, 7))
              && strchr(CLOSING_PUNCTUATION, tmp[7]) != null) {
            let(&cmt, cat(left(cmt, i + 1), right(cmt, i + 3), null));
            clen = clen - 1;
          }
          let(&tmp, "");
        }

      }
    }
    if (cmt[i] == '~' && mode != 'm') {
      if (cmt[i + 1] == '~'
          || processLabels == 0
          ) {
        if (processLabels != 0) {

          let(&cmt, cat(left(cmt, i + 1), right(cmt, i + 3), null));
          clen--;
        }
      } else {

        if (mode != 'l') {
          mode = 'l';
          while (isspace((unsigned char)(cmt[i + 1])) && clen > i + 1) {
            let(&cmt, cat(left(cmt, i + 1), right(cmt, i + 3), null));
            clen--;
          }
        } else {



          g_outputToString = 0;
          printLongLine(cat("?Warning: There is a \"~\" inside of a label",
              " in the comment of statement \"",
              g_Statement[g_showStatement].labelName,
              "\".  Use \"~~\" to escape \"~\" in an http reference.",
              null), "", " ");
          returnVal = 1;
          g_outputToString = 1;
          mode = 'n';
        }
        let(&cmt, cat(left(cmt, i), chr(DOLLAR_SUBST) , chr(mode),
            right(cmt, i+2), null));
        clen++;
        i++;


        let(&tmp, mid(cmt, i - 2, 2));

        if (!strcmp("( ", tmp) || !strcmp("[ ", tmp)) {
          let(&cmt, cat(left(cmt, i - 2), right(cmt, i), null));
          clen = clen - 1;
        }
        let(&tmp, "");

      }
    }

    if (processLabels == 0 && mode == 'l') {

      bug(2344);
    }

    if ((isspace((unsigned char)(cmt[i]))
            || cmt[i] == '<')
        && mode == 'l') {

      mode = 'n';
      let(&cmt, cat(left(cmt, i), chr(DOLLAR_SUBST) , chr(mode),
          right(cmt, i+1), null));
      clen = clen + 2;
      i = i + 2;


      let(&tmp, mid(cmt, i + 1, 2));
      if (tmp[0] == ' ' && strchr(CLOSING_PUNCTUATION, tmp[1]) != null) {
        let(&cmt, cat(left(cmt, i), right(cmt, i + 2), null));
        clen = clen - 1;
      }
      let(&tmp, "");

    }

    if ((signed)(C.strlen(cmt)) != clen) {
      bug(2311);
    }
  }




  pos1 = -1;
  while (true) {
    pos1 = instr(pos1 + 2, cmt, "<HTML>");
    if (pos1 == 0
      || convertToHtml == 0

      ) break;

    let(&tmpStr, edit(left(cmt, pos1 - 1), 2));
    i = (long)C.strlen(tmpStr);
    if (i == 0) continue;
    if (tmpStr[i - 1] == '\n') continue;

    let(&cmt, cat(left(cmt, pos1 - 1), "\n", right(cmt, pos1), null));
  }
  pos1 = -1;
  while (true) {
    pos1 = instr(pos1 + 2, cmt, "</HTML>");
    if (pos1 == 0
      || convertToHtml == 0

      ) break;

    let(&tmpStr, edit(left(cmt, pos1 - 1), 2));
    i = (long)C.strlen(tmpStr);
    if (i == 0) continue;
    if (tmpStr[i - 1] == '\n') continue;

    let(&cmt, cat(left(cmt, pos1 - 1), "\n", right(cmt, pos1), null));
  }


  cmtptr = cmt;

  g_outputToString = 1;


  while (true) {
    lineStart = cmtptr;
    textMode = 1;
    lastLineFlag = 0;
    while (true) {
      if (cmtptr[0] == 0) {
        lastLineFlag = 1;
        break;
      }
      if (cmtptr[0] == '\n' && textMode) break;

      if (cmtptr[0] == DOLLAR_SUBST) {
        if (cmtptr[1] == ')') {
          bug(2312);
          lastLineFlag = 1;
          break;
        }
      }
      if (cmtptr[0] == DOLLAR_SUBST ) {
        cmtptr++;
        if (cmtptr[0] == 'm') textMode = 0;
        if (cmtptr[0] == 'l') textMode = 0;
        if (cmtptr[0] == 'n') textMode = 1;
      }
      cmtptr++;
    }
    let(&sourceLine, space(cmtptr - lineStart));
    memcpy(sourceLine, lineStart, (size_t)(cmtptr - lineStart));
    cmtptr++;


    displayMode = 0;
    let(&tmpStr, edit(sourceLine, 8 + 128));
    if (!strcmp(right(tmpStr, (long)C.strlen(tmpStr) - 1), cat(chr(DOLLAR_SUBST), "n",
        null))) let(&tmpStr, left(tmpStr, (long)C.strlen(tmpStr) - 2));
    srcptr = tmpStr;
    modeSection = getCommentModeSection(&srcptr, &mode);
    let(&modeSection, "");
    if (mode == 'm') {
      modeSection = getCommentModeSection(&srcptr, &mode);
      let(&modeSection, "");

    }
    let(&tmpStr, "");



    let(&outputLine, "");
    srcptr = sourceLine;
    while (true) {
      modeSection = getCommentModeSection(&srcptr, &mode);
      if (!mode) break;
      let(&modeSection, right(modeSection, 3));
      switch (mode) {
        case 'n':
          let(&outputLine, cat(outputLine, modeSection, null));
          break;
        case 'l':

          if (processLabels == 0) {

            bug(2345);
          }

          let(&modeSection, edit(modeSection, 8 + 128 + 16));
          let(&tmpStr, "");
          tmpStr = asciiToTt(modeSection);
          if (!tmpStr[0]) {




            g_outputToString = 0;
            printLongLine(cat("?Error: There is a \"~\" with no label",
                " in the comment of statement \"",
                g_Statement[g_showStatement].labelName,
                "\".  Check that \"`\" inside of a math symbol is",
                " escaped with \"``\".",
                null), "", " ");
            returnVal = 1;
            g_outputToString = 1;

          }



          if (!strcmp("http://", left(tmpStr, 7))
              || !strcmp("https://", left(tmpStr, 8))
              || !strcmp("mm", left(tmpStr, 2))
              ) {

            if (g_htmlFlag) {
              let(&outputLine, cat(outputLine, "<A HREF=\"", tmpStr,
                  "\">", tmpStr, "</A>", tmp, null));
            } else {



              i = instr(1, tmpStr, "\\char`\\~");

              if (i != 0) {
                let(&tmpStr, cat(left(tmpStr, i - 1), right(tmpStr, i + 7),
                    null));
              }
              let(&outputLine, cat(outputLine, "\\url{", tmpStr,
                  "}", tmp, null));

            }
          } else {
            i = lookupLabel(tmpStr);
            if (i < 0) {
              g_outputToString = 0;
              printLongLine(cat("?Warning: The label token \"", tmpStr,
                  "\" (referenced in comment of statement \"",
                  g_Statement[g_showStatement].labelName,
                  "\") is not a $a or $p statement label.", null), "", " ");
              g_outputToString = 1;
              returnVal = 1;
            }

            if (!g_htmlFlag) {
              let(&outputLine, cat(outputLine, "{\\tt ", tmpStr,
                 "}", null));
            } else {
              let(&tmp, "");
              if (addColoredLabelNumber != 0) {
                tmp = pinkHTML(i);
              }
              if (i < 0) {

                let(&outputLine, cat(outputLine, "<FONT COLOR=blue ",
                    ">", tmpStr, "</FONT>", tmp, null));
              } else {

                let(&outputLine, cat(outputLine, "<A HREF=\"", tmpStr,
                    ".html\">", tmpStr, "</A>", tmp, null));
              }
            }
          }
          let(&tmpStr, "");
          break;
        case 'm':

          if (processSymbols == 0) {

            bug(2346);
          }

          let(&tmpStr, "");
          tmpStr = asciiMathToTex(modeSection, g_showStatement);
          if (!g_htmlFlag) {
            if (displayMode) {
              let(&outputLine, cat(outputLine,  edit(tmpStr, 128),
                 null));
            } else {
              let(&outputLine, cat(outputLine, "$", edit(tmpStr, 128),
                "$", null));
            }
          } else {
            let(&tmpStr, edit(tmpStr, 8 + 128));


            let(&tmpStr, cat(
                (g_altHtmlFlag ? cat("<SPAN ", g_htmlFont, ">", null) : ""),

                tmpStr,
                (g_altHtmlFlag ? "</SPAN>" : ""),
                null));
            let(&outputLine, cat(outputLine, tmpStr, null));
          }
          let(&tmpStr, "");
          break;
      }
      let(&modeSection, "");
    }
    let(&outputLine, edit(outputLine, 128));

    if (g_htmlFlag) {

      if (!outputLine[0]) {
        if (preformattedMode == 0
            && convertToHtml == 1
            ) {
          let(&outputLine,


              "<P STYLE=\"margin-bottom:0em\">");
        }
      }


      pos1 = instr(1, outputLine, "<HTML>");
      if (pos1 != 0
          && convertToHtml == 1
          ) {
        preformattedMode = 1;

        let(&outputLine, cat(left(outputLine, pos1 - 1),
            right(outputLine, pos1 + 6), null));
      }

      pos1 = instr(1, outputLine, "</HTML>");
      if (pos1 != 0
          && convertToHtml == 1
          ) {
        preformattedMode = 0;

        let(&outputLine, cat(left(outputLine, pos1 - 1),
            right(outputLine, pos1 + 7), null));
      }
    }

    if (!g_htmlFlag) {


      while (true) {
        pos1 = instr(1, outputLine, "<PRE>");
        if (pos1) {
          let(&outputLine, cat(left(outputLine, pos1 - 1), "\\begin{verbatim} ",
              right(outputLine, pos1 + 5), null));
        } else {
          break;
        }
      }
      while (true) {
        pos1 = instr(1, outputLine, "</PRE>");
        if (pos1) {
          let(&outputLine, cat(left(outputLine, pos1 - 1), "\\end{verbatim} ",
              right(outputLine, pos1 + 6), null));
        } else {
          break;
        }
      }

      while (true) {
        pos1 = instr(1, outputLine, "<HTML>");
        if (pos1) {
          let(&outputLine, cat(left(outputLine, pos1 - 1),
              right(outputLine, pos1 + 6), null));
        } else {
          break;
        }
      }
      while (true) {
        pos1 = instr(1, outputLine, "</HTML>");
        if (pos1) {
          let(&outputLine, cat(left(outputLine, pos1 - 1),
              right(outputLine, pos1 + 7), null));
        } else {
          break;
        }
      }
    }

    saveScreenWidth = g_screenWidth;

    if (preformattedMode) g_screenWidth = 50000;
    if (errorsOnly == 0) {
      printLongLine(outputLine, "", g_htmlFlag ? "\"" : "\\");
    }
    g_screenWidth = saveScreenWidth;

    let(&tmp, "");

    if (lastLineFlag) break;
  }

  if (g_htmlFlag) {
    if (convertToHtml != 0) {
      mminou.print2("\n");
    } else {


      if (g_printString[0] != 0) {
        i = (long)C.strlen(g_printString);
        if (g_printString[i - 1] != '\n')  {
          mminou.print2("\n");
        } else {

          if (i > 1) {
            if (g_printString[i - 2] == '\n') {
              let(&g_printString, left(g_printString, i - 1));
            }
          }
        }
      }
    }
  } else {
    if (!g_oldTexFlag) {


    } else {
      mminou.print2("\n");
    }
  }

  g_outputToString = 0;
  if (errorsOnly == 0) {
    fprintf(g_texFilePtr, "%s", g_printString);
  }

  let(&g_printString, "");
  let(&sourceLine, "");
  let(&outputLine, "");
  let(&cmt, "");
  let(&cmtMasked, "");
  let(&tmpComment, "");
  let(&tmp, "");
  let(&tmpMasked, "");
  let(&tmpStr, "");
  let(&tmpStrMasked, "");
  let(&bibTag, "");
  let(&bibFileName, "");
  let(&bibFileContents, "");
  let(&bibFileContentsUpper, "");
  let(&bibTags, "");

  return returnVal;

}



void printTexLongMath(nmbrString *mathString,
    vstring startPrefix,
    vstring contPrefix,
    long hypStmt,
    long indentationLevel)
{
  static final long INDENTATION_OFFSET =D.INDENTATION_OFFSET;
  long i;
  long pos;
  vstring tex = "";
  vstring texLine = "";
  vstring sPrefix = "";
  vstring htmStep = "";
  vstring htmStepTag = "";
  vstring htmHyp = "";
  vstring htmRef = "";
  vstring htmLocLab = "";
  vstring tmp = "";
  vstring descr = "";
  char refType = '?';

  let(&sPrefix, startPrefix);

  if (!g_texDefsRead) return;
  g_outputToString = 1;



  let(&tex, "");
  tex = asciiToTt(sPrefix);



  let(&texLine, "");


  i = instr(1, sPrefix, "$");
  if (i) refType = sPrefix[i];


  if (g_htmlFlag || !g_oldTexFlag) {


    if (C.strlen(sPrefix)) {







      let(&tex, edit(sPrefix, 8
           + 16
           + 128));

      i = 0;
      pos = 1;
      while (pos) {
        pos = instr(1, tex, " ");
        if (pos) {
          if (i > 3) {
            bug(2316);
          }
          if (i == 0) let(&htmStep, left(tex, pos - 1));
          if (i == 1) let(&htmHyp, left(tex, pos - 1));
          if (i == 2) let(&htmRef, left(tex, pos - 1));

          if (i == 3) let(&htmLocLab, left(tex, pos - 1));

          let(&tex, right(tex, pos + 1));
          i++;
        }
      }


      if (i == 3 && htmRef[0] == '@') {
        let(&htmLocLab, htmRef);
        let(&htmRef, htmHyp);
        let(&htmHyp, "");
      }

      if (i < 3) {
        let(&htmRef, htmHyp);
        let(&htmHyp, "");



        if (!g_htmlFlag) {
          pos = instr(1, htmRef, "=");
          if (pos) bug(2342);
        }


      }
    }
  }

  if (!g_htmlFlag) {


    if (!g_oldTexFlag) {






    } else {
      static final long  TRIMTHRESHOLD =D.TRIMTHRESHOLD;
      i = (long)C.strlen(tex);
      while (i > TRIMTHRESHOLD) {
        if (tex[i] == '\\') {

          let(&texLine, cat("\\m{\\mbox{\\tt", right(tex, i + 1), "}}",
              texLine, null));

          let(&tex, left(tex, i));
        }
        i--;
      }

      printLongLine(cat(
          "\\setbox\\startprefix=\\hbox{\\tt ", tex, "}", null), "", "\\");
      let(&tex, "");
      tex = asciiToTt(contPrefix);
      printLongLine(cat(
          "\\setbox\\contprefix=\\hbox{\\tt ", tex, "}", null), "", "\\");
      mminou.print2("\\startm\n");
    }
  } else {
    if (C.strlen(sPrefix)) {

      if (htmHyp[0] == 0)
        let(&htmHyp, "&nbsp;");




      let(&htmStepTag, cat("<A NAME=\"", htmStep, "\">","</A>", null));
      i = 1;
      pos = 1;
      while (pos && strcmp(htmHyp, "&nbsp;")) {
        pos = instr(i,htmHyp, ",");
        if (!pos) pos = len(htmHyp) + 1;
        let(&htmHyp, cat(left(htmHyp, i - 1),
            "<A HREF=\"#",
            seg(htmHyp, i, pos - 1),
            "\">",
            seg(htmHyp, i, pos - 1),
            "</A>",
            right(htmHyp, pos),
            null));

        pos += 16 + len(seg(htmHyp, i, pos - 1)) + 1;
        if (!instr(i, htmHyp, ",")) break;
        i = pos;
      }


      pos = instr(1, htmHyp, ",");
      while (pos) {
        let(&htmHyp, cat(left(htmHyp, pos), " ", right(htmHyp, pos + 1), null));
        pos = instr(pos + 1, htmHyp, ",");
      }


      if (refType == 'e' || refType == 'f') {

        printLongLine(cat("<TR ALIGN=LEFT><TD>", htmStep, "</TD><TD>",
            htmHyp, "</TD><TD>", htmRef,
            "</TD><TD>",
            htmStepTag,

            null), "", "\"");
      } else {
        if (hypStmt <= 0) {
          printLongLine(cat("<TR ALIGN=LEFT><TD>", htmStep, "</TD><TD>",
              htmHyp, "</TD><TD><A HREF=\"", htmRef, ".html\">", htmRef,
              "</A></TD><TD>",
              htmStepTag,

              null), "", "\"");
        } else {
          let(&tmp, "");
          tmp = pinkHTML(hypStmt);


#define TOOLTIP
#ifdef TOOLTIP

          let(&descr, "");
          descr = getDescription(hypStmt);
          let(&descr, edit(descr, 4 + 16));
          static final long MAX_DESCR_LEN =D.MAX_DESCR_LEN;
          if (C.strlen(descr) > MAX_DESCR_LEN) {
            i = MAX_DESCR_LEN - 3;
            while (i >= 0) {
              if (descr[i] == ' ') break;
              i--;
            }
            let(&descr, cat(left(descr, i), "...", null));
          }
          i = 0;
          while (descr[i] != 0) {
            descr[i] = (char)(descr[i] == '"' ? '\'' : descr[i]);
            i++;
          }
#endif

          printLongLine(cat("<TR ALIGN=LEFT><TD>", htmStep, "</TD><TD>",
              htmHyp, "</TD><TD><A HREF=\"", htmRef, ".html\"",

#ifdef TOOLTIP
              " TITLE=\"", descr, "\"",
#endif

              ">", htmRef,
              "</A>", tmp,
              "</TD><TD>",
              htmStepTag,

              null), "", "\"");
        }
      }
#ifdef INDENT_HTML_PROOFS

      let(&tmp, "");
      for (i = 1; i <= indentationLevel; i++) {
        let(&tmp, cat(tmp, ". ", null));
      }
      let(&tmp, cat("<SPAN CLASS=i>",
          tmp,
          str((double)(indentationLevel + INDENTATION_OFFSET)), "</SPAN>",
          null));
      printLongLine(tmp, "", "\"");
      let(&tmp, "");
#endif
    }
  }
  let(&tex, "");
  let(&sPrefix, "");

  let(&tex, "");
  tex = getTexLongMath(mathString, hypStmt);
  let(&texLine, cat(texLine, tex, null));

  if (!g_htmlFlag) {

    if (!g_oldTexFlag) {

      if (refType == 'e' || refType == 'f') {

        printLongLine(cat("  ",

            !strcmp(htmStep, "1") ? "" : "\\\\ ",
            htmStep,
            " && ",
            " & ",
            texLine,

            "&\\text{Hyp~",
            right(htmRef, instr(1, htmRef, ".") + 1),
            "}\\notag%",
            htmRef, null),
            "    \\notag \\\\ && & \\qquad ",
            " ");
      } else {
        printLongLine(cat("  ",

            !strcmp(htmStep, "1") ? "" : "\\\\ ",
            htmStep,
            " && ",


            (htmLocLab[0] != 0) ? cat(htmLocLab, "\\ ", null) : "",

            " & ",
            texLine,


            "&",
            "(",


            (htmRef[0] != '@') ?
                cat("\\mbox{\\ref{eq:", htmRef, "}}", null)
                : htmRef,

            htmHyp[0] ? "," : "",
            htmHyp,
            ")\\notag", null),

            "    \\notag \\\\ && & \\qquad ",
            " ");
      }





    } else {
      printLongLine(texLine, "", "\\");
      mminou.print2("\\endm\n");
    }
  } else {
    printLongLine(cat(texLine, "</TD></TR>", null), "", "\"");
  }

  g_outputToString = 0;
  fprintf(g_texFilePtr, "%s", g_printString);
  let(&g_printString, "");

  let(&descr, "");
  let(&htmStep, "");
  let(&htmStepTag, "");
  let(&htmHyp, "");
  let(&htmRef, "");
  let(&htmLocLab, "");
  let(&tmp, "");
  let(&texLine, "");
  let(&tex, "");
}

void printTexTrailer(flag texTrailerFlag) {

  if (texTrailerFlag) {
    g_outputToString = 1;
    if (!g_htmlFlag) let(&g_printString, "");

    if (!g_htmlFlag) {
      mminou.print2("\\end{document}\n");
    } else {
      mminou.print2("</TABLE></CENTER>\n");
      mminou.print2("<TABLE BORDER=0 WIDTH=\"100%s\">\n", "%");
      mminou.print2("<TR><TD WIDTH=\"25%s\">&nbsp;</TD>\n", "%");
      mminou.print2("<TD ALIGN=CENTER VALIGN=BOTTOM>\n");
      mminou.print2("<FONT SIZE=-2 FACE=sans-serif>\n");
      mminou.print2("Copyright terms:\n");
      mminou.print2("<A HREF=\"../copyright.html#pd\">Public domain</A>\n");
      mminou.print2("</FONT></TD><TD ALIGN=RIGHT VALIGN=BOTTOM WIDTH=\"25%s\">\n",
          "%");
      mminou.print2("<FONT SIZE=-2 FACE=sans-serif>\n");
      mminou.print2("<A HREF=\"http://validator.w3.org/check?uri=referer\">\n");
      mminou.print2("W3C validator</A>\n");
      mminou.print2("</FONT></TD></TR></TABLE>\n");



      mminou.print2("</BODY></HTML>\n");
    }
    g_outputToString = 0;
    fprintf(g_texFilePtr, "%s", g_printString);
    let(&g_printString, "");
  }

}


void writeTheoremList(long theoremsPerPage, flag showLemmas, flag noVersioning)
{
  nmbrString *nmbrStmtNmbr = NULL_NMBRSTRING;
  long pages, page, assertion, assertions, lastAssertion;
  long s, p, i1, i2;
  vstring str1 = "";
  vstring str3 = "";
  vstring str4 = "";
  vstring prevNextLinks = "";
  long partCntr;
  long sectionCntr;
  long subsectionCntr;
  long subsubsectionCntr;
  vstring outputFileName = "";
  File outputFilePtr;
  long passNumber;


  vstring hugeHdr = "";
  vstring bigHdr = "";
  vstring smallHdr = "";
  vstring tinyHdr = "";
  vstring hugeHdrComment = "";
  vstring bigHdrComment = "";
  vstring smallHdrComment = "";
  vstring tinyHdrComment = "";
  long stmt, i;
  pntrString *pntrHugeHdr = NULL_PNTRSTRING;
  pntrString *pntrBigHdr = NULL_PNTRSTRING;
  pntrString *pntrSmallHdr = NULL_PNTRSTRING;
  pntrString *pntrTinyHdr = NULL_PNTRSTRING;
  pntrString *pntrHugeHdrComment = NULL_PNTRSTRING;
  pntrString *pntrBigHdrComment = NULL_PNTRSTRING;
  pntrString *pntrSmallHdrComment = NULL_PNTRSTRING;
  pntrString *pntrTinyHdrComment = NULL_PNTRSTRING;
  vstring hdrCommentMarker = "";
  vstring hdrCommentAnchor = "";
  flag hdrCommentAnchorDone = 0;



  nmbrLet(&nmbrStmtNmbr, nmbrSpace(g_statements + 1));
  assertions = 0;
  for (s = 1; s <= g_statements; s++) {
    if (g_Statement[s].type == a_ || g_Statement[s].type == p_) {
      assertions++;
      nmbrStmtNmbr[assertions] = s;
    }
  }
  if (assertions != g_Statement[g_statements].pinkNumber) bug(2328);



  pntrLet(&pntrHugeHdr, pntrSpace(g_statements + 1));
  pntrLet(&pntrBigHdr, pntrSpace(g_statements + 1));
  pntrLet(&pntrSmallHdr, pntrSpace(g_statements + 1));
  pntrLet(&pntrTinyHdr, pntrSpace(g_statements + 1));
  pntrLet(&pntrHugeHdrComment, pntrSpace(g_statements + 1));
  pntrLet(&pntrBigHdrComment, pntrSpace(g_statements + 1));
  pntrLet(&pntrSmallHdrComment, pntrSpace(g_statements + 1));
  pntrLet(&pntrTinyHdrComment, pntrSpace(g_statements + 1));

  pages = ((assertions - 1) / theoremsPerPage) + 1;


  for (page = 0; page <= pages; page++) {

    let(&outputFileName,


        cat("mmtheorems", (page > 0) ? str((double)page) : "", ".html", null));
    mminou.print2("Creating %s\n", outputFileName);
    outputFilePtr = fSafeOpen(outputFileName, "w", noVersioning);
    if (!outputFilePtr) C.go2("TL_ABORT");




    g_outputToString = 1;
    mminou.print2(
        "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n");
    mminou.print2(     "    \"http://www.w3.org/TR/html4/loose.dtd\">\n");
    mminou.print2("<HTML LANG=\"EN-US\">\n");
    mminou.print2("<HEAD>\n");
    mminou.print2("%s%s\n", "<META HTTP-EQUIV=\"Content-Type\" ",
        "CONTENT=\"text/html; charset=iso-8859-1\">");


    mminou.print2(
"<META NAME=\"viewport\" CONTENT=\"width=device-width, initial-scale=1.0\">\n"
        );

    mminou.print2("<STYLE TYPE=\"text/css\">\n");
    mminou.print2("<!--\n");

    mminou.print2("img { margin-bottom: -4px }\n");
#ifndef RAINBOW_OPTION

    mminou.print2(".p { font-family: \"Arial Narrow\";\n");
    mminou.print2("     font-size: x-small;\n");

    printLongLine(cat("     color: ", seg(PINK_NUMBER_COLOR, 2,
        (long)C.strlen(PINK_NUMBER_COLOR) - 1), ";", null), "", "&");
    mminou.print2("   }\n");
#else

    mminou.print2(".r { font-family: \"Arial Narrow\";\n");
    mminou.print2("     font-size: x-small;\n");
    mminou.print2("   }\n");
#endif
    mminou.print2("-->\n");
    mminou.print2("</STYLE>\n");
    printLongLine(g_htmlCSS, "", " ");



    printLongLine(cat("<TITLE>",




        ((page == 0)
            ? "TOC of Theorem List"
            : cat("P. ", str((double)page), " of Theorem List", null)),

        " - ",
        htmlTitle,
        "</TITLE>",
        null), "", "\"");

    mminou.print2("%s%s\n", "<LINK REL=\"shortcut icon\" HREF=\"favicon.ico\" ",
        "TYPE=\"image/x-icon\">");


    mminou.print2(
        "<STYLE TYPE=\"text/css\">\n");
    mminou.print2(
        "<!--\n");

    mminou.print2(
        "img { margin-bottom: -4px }\n");
    mminou.print2(
        "-->\n");
    mminou.print2(
        "</STYLE>\n");

    mminou.print2("</HEAD>\n");
    mminou.print2("<BODY BGCOLOR=\"#FFFFFF\">\n");
    mminou.print2("<TABLE BORDER=0 WIDTH=\"100%s\"><TR>\n", "%");
    mminou.print2("<TD ALIGN=LEFT VALIGN=TOP WIDTH=\"25%s\"\n", "%");
    printLongLine(cat("ROWSPAN=2>", g_htmlHome, "</TD>", null), "", "\"");
    printLongLine(cat(
        "<TD NOWRAP ALIGN=CENTER ROWSPAN=2><FONT SIZE=\"+3\" COLOR=",
        GREEN_TITLE_COLOR, "><B>", htmlTitle, "</B></FONT>",
        "<BR><FONT SIZE=\"+2\" COLOR=",
        GREEN_TITLE_COLOR,


        "><B>Theorem List (",
        ((page == 0)
            ? "Table of Contents"


            : cat("p. ", str((double)page), " of ", str((double)pages), null)),
        ")</B></FONT>",
        null), "", "\"");



    mminou.print2("</TD><TD NOWRAP ALIGN=RIGHT VALIGN=TOP WIDTH=\"25%c\"><FONT\n", '%');
    mminou.print2(" SIZE=-1 FACE=sans-serif>\n");






    let(&prevNextLinks, cat("<A HREF=\"mmtheorems",

        (page > 0)
            ? ((page - 1 > 0) ? str((double)page - 1) : "")
            : ((pages > 0) ? str((double)pages) : ""),
        ".html\">", null));


    if (page > 0) {
      let(&prevNextLinks, cat(prevNextLinks,
          "&lt; Previous</A>&nbsp;&nbsp;", null));
    } else {
      let(&prevNextLinks, cat(prevNextLinks, "&lt; Wrap</A>&nbsp;&nbsp;", null));
    }
    let(&prevNextLinks, cat(prevNextLinks, "<A HREF=\"mmtheorems",
        (page < pages)
            ? str((double)page + 1)
            : "",
        ".html\">", null));
    if (page < pages) {
      let(&prevNextLinks, cat(prevNextLinks, "Next &gt;</A>", null));
    } else {
      let(&prevNextLinks, cat(prevNextLinks, "Wrap &gt;</A>", null));
    }

    printLongLine(prevNextLinks,
        " ",
        "\"");




    if (htmlDir[0]) {
      if (g_altHtmlFlag) {

        mminou.print2("</FONT></TD></TR><TR><TD ALIGN=RIGHT><FONT FACE=sans-serif\n");
        mminou.print2("SIZE=-2>Bad symbols? Try the\n");
        mminou.print2("<BR><A HREF=\"%s%s\">GIF\n",
            htmlDir, outputFileName);
        mminou.print2("version</A>.</FONT></TD>\n");
      } else {
        mminou.print2("</FONT></TD></TR><TR><TD ALIGN=RIGHT><FONT FACE=sans-serif\n");
        mminou.print2("SIZE=-2>Browser slow? Try the\n");
        mminou.print2("<BR><A HREF=\"%s%s\">Unicode\n",
            altHtmlDir, outputFileName);
        mminou.print2("version</A>.</FONT></TD>\n");
      }
    }





    mminou.print2("<TR>\n");
    mminou.print2(
      "<TD COLSPAN=3 ALIGN=LEFT VALIGN=TOP><FONT SIZE=-2 FACE=sans-serif>\n");
    mminou.print2("<BR>\n");



    mminou.print2("<A HREF=\"../mm.html\">Mirrors</A>\n");
    mminou.print2("&nbsp;&gt;&nbsp;<A HREF=\"../index.html\">\n");
    mminou.print2("Metamath Home Page</A>\n");



    mminou.print2("&nbsp;&gt;&nbsp;<A HREF=\"%s\">\n", g_htmlBibliography);



    let(&str1, "");
    s = (long)C.strlen(htmlTitle);
    for (i = 0; i < s; i++) {
      if (htmlTitle[i] >= 'A' && htmlTitle[i] <= 'Z') {
        let(&str1, cat(str1, chr(htmlTitle[i]), null));
      }
    }
    mminou.print2("%s Home Page</A>\n", str1);



    if (page != 0) {
      mminou.print2("&nbsp;&gt;&nbsp;<A HREF=\"mmtheorems.html\">\n");


      mminou.print2("Theorem List Contents</A>\n");
    } else {
      mminou.print2("&nbsp;&gt;&nbsp;\n");


      mminou.print2("Theorem List Contents\n");
    }


    if (g_mathboxStmt < g_statements + 1) {
      mminou.print2("&nbsp;&gt;&nbsp;<A HREF=\"mmrecent.html\">\n");
      mminou.print2("Recent Proofs</A>\n");
    }


    mminou.print2("&nbsp; &nbsp; &nbsp; <B><FONT COLOR=%s>\n", GREEN_TITLE_COLOR);
    mminou.print2("This page:</FONT></B> \n");



    if (page == 0) {
      mminou.print2(
          "&nbsp;<A HREF=\"#mmdtoc\">Detailed Table of Contents</A>&nbsp;\n");
    }

    mminou.print2("<A HREF=\"#mmpglst\">Page List</A>\n");



    mminou.print2("</FONT>\n");
    mminou.print2("</TD>\n");
    mminou.print2("</TR></TABLE>\n");


    mminou.print2("<HR NOSHADE SIZE=1>\n");


    fprintf(outputFilePtr, "%s", g_printString);
    g_outputToString = 0;
    let(&g_printString, "");





    if (page == 0) {


      for (passNumber = 1; passNumber <= 2; passNumber++) {

        g_outputToString = 1;


        if (passNumber == 1) {





          mminou.print2(
              "<P><CENTER><B>Table of Contents Summary</B></CENTER>\n");
        } else {
          mminou.print2(
  "<P><CENTER><A NAME=\"mmdtoc\"></A><B>Detailed Table of Contents</B><BR>\n");


          mminou.print2(
           "<B>(* means the section header has a description)</B></CENTER>\n");

        }

        fprintf(outputFilePtr, "%s", g_printString);

        g_outputToString = 0;
        let(&g_printString, "");

        let(&hugeHdr, "");
        let(&bigHdr, "");
        let(&smallHdr, "");
        let(&tinyHdr, "");
        let(&hugeHdrComment, "");
        let(&bigHdrComment, "");
        let(&smallHdrComment, "");
        let(&tinyHdrComment, "");
        partCntr = 0;
        sectionCntr = 0;
        subsectionCntr = 0;
        subsubsectionCntr = 0;
        for (stmt = 1; stmt <= g_statements; stmt++) {



          if (g_Statement[stmt].type == p_ || g_Statement[stmt].type == a_) {
            hdrCommentAnchorDone = 0;
            getSectionHeadings(stmt, &hugeHdr, &bigHdr, &smallHdr,
                &tinyHdr,

                &hugeHdrComment, &bigHdrComment, &smallHdrComment,
                &tinyHdrComment,
                0,
                0  );
            if (hugeHdr[0] || bigHdr[0] || smallHdr[0] || tinyHdr[0]) {

              g_outputToString = 1;
              i = ((g_Statement[stmt].pinkNumber - 1) / theoremsPerPage)
                  + 1;



              let(&str3, cat("mmtheorems", str((double)i), ".html#",

                  "mm", str((double)(g_Statement[stmt].pinkNumber)), null));

              let(&str4, "");
              str4 = pinkHTML(stmt);


              if (hugeHdr[0]) {


                partCntr++;
                sectionCntr = 0;
                subsectionCntr = 0;
                subsubsectionCntr = 0;
                let(&hugeHdr, cat("PART ", str((double)partCntr), "&nbsp;&nbsp;",
                    hugeHdr, null));


                if (hugeHdrComment[0] != 0 && passNumber == 2) {
                  let(&hdrCommentMarker, "*");

                  if (hdrCommentAnchorDone == 0) {
                    let(&hdrCommentAnchor, cat(
                        "<A NAME=\"",
                        g_Statement[stmt].labelName, "\"></A>",

                        "&#8203;",

                        null));
                    hdrCommentAnchorDone = 1;
                  } else {
                    let(&hdrCommentAnchor, "");
                  }
                } else {
                  let(&hdrCommentMarker, "");
                  let(&hdrCommentAnchor, "");
                }

                printLongLine(cat(


                    (passNumber == 2) ?
                        cat("<A NAME=\"dtl:", str((double)partCntr),
                            "\"></A>",

                            "&#8203;",

                            null) : "",

                    (stmt == g_mathboxStmt && bigHdr[0] == 0

                          && passNumber == 1
                          ) ?

                        "<A NAME=\"sandbox:bighdr\"></A>&#8203;" : "",

                    hdrCommentAnchor,
                    "<A HREF=\"",


                    (passNumber == 1) ?
                        cat("#dtl:", str((double)partCntr), null)
                        : cat(str3, "h", null),

                    "\"><B>",
                    hdrCommentMarker,
                    hugeHdr, "</B></A>",
                    "<BR>", null),
                    " ",
                    "\"");
                if (passNumber == 2) {

                  let((vstring *)(&pntrHugeHdr[stmt]), hugeHdr);
                  let((vstring *)(&pntrHugeHdrComment[stmt]), hugeHdrComment);
                }
                let(&hugeHdr, "");
                let(&hugeHdrComment, "");
              }
              if (bigHdr[0]) {


                sectionCntr++;
                subsectionCntr = 0;
                subsubsectionCntr = 0;
                let(&bigHdr, cat(str((double)partCntr), ".", str((double)sectionCntr),
                    "&nbsp;&nbsp;",
                    bigHdr, null));


                if (bigHdrComment[0] != 0 && passNumber == 2) {
                  let(&hdrCommentMarker, "*");

                  if (hdrCommentAnchorDone == 0) {
                    let(&hdrCommentAnchor, cat(
                        "<A NAME=\"",
                        g_Statement[stmt].labelName, "\"></A>",

                        "&#8203;",

                        null));
                    hdrCommentAnchorDone = 1;
                  } else {
                    let(&hdrCommentAnchor, "");
                  }
                } else {
                  let(&hdrCommentMarker, "");
                  let(&hdrCommentAnchor, "");
                }

                printLongLine(cat(
                    "&nbsp; &nbsp; &nbsp; ",


                    (passNumber == 2) ?
                        cat("<A NAME=\"dtl:", str((double)partCntr), ".",
                            str((double)sectionCntr), "\"></A>",

                            "&#8203;",

                            null)
                        : "",

                    (stmt == g_mathboxStmt

                          && passNumber == 1
                          ) ?

                        "<A NAME=\"sandbox:bighdr\"></A>&#8203;" : "",
                    hdrCommentAnchor,
                    "<A HREF=\"",


                    (passNumber == 1) ?
                         cat("#dtl:", str((double)partCntr), ".",
                             str((double)sectionCntr),
                             null)
                        : cat(str3, "b", null),

                    "\"><B>",
                    hdrCommentMarker,
                    bigHdr, "</B></A>",
                    "<BR>", null),
                    " ",
                    "\"");
                if (passNumber == 2) {

                  let((vstring *)(&pntrBigHdr[stmt]), bigHdr);
                  let((vstring *)(&pntrBigHdrComment[stmt]), bigHdrComment);
                }
                let(&bigHdr, "");
                let(&bigHdrComment, "");
              }
              if (smallHdr[0]
                  && passNumber == 2) {


                subsectionCntr++;
                subsubsectionCntr = 0;
                let(&smallHdr, cat(str((double)partCntr), ".",
                    str((double)sectionCntr),
                    ".", str((double)subsectionCntr), "&nbsp;&nbsp;",
                    smallHdr, null));


                if (smallHdrComment[0] != 0 && passNumber == 2) {
                  let(&hdrCommentMarker, "*");

                  if (hdrCommentAnchorDone == 0) {
                    let(&hdrCommentAnchor, cat("<A NAME=\"",
                        g_Statement[stmt].labelName, "\"></A>",

                        "&#8203;",

                        null));
                    hdrCommentAnchorDone = 1;
                  } else {
                    let(&hdrCommentAnchor, "");
                  }
                } else {
                  let(&hdrCommentMarker, "");
                  let(&hdrCommentAnchor, "");
                }

                printLongLine(cat("&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ",


                    hdrCommentAnchor,
                    "<A HREF=\"", str3, "s\">",
                    hdrCommentMarker,
                    smallHdr, "</A>",
                    " &nbsp; <A HREF=\"",
                    g_Statement[stmt].labelName, ".html\">",
                    g_Statement[stmt].labelName, "</A>",
                    str4,
                    "<BR>", null),
                    " ",
                    "\"");

                let((vstring *)(&pntrSmallHdr[stmt]), smallHdr);
                let(&smallHdr, "");
                let((vstring *)(&pntrSmallHdrComment[stmt]), smallHdrComment);
                let(&smallHdrComment, "");
              }


              if (tinyHdr[0]
                  && passNumber == 2) {


                subsubsectionCntr++;
                let(&tinyHdr, cat(str((double)partCntr), ".",
                    str((double)sectionCntr),
                    ".", str((double)subsectionCntr),
                    ".", str((double)subsubsectionCntr), "&nbsp;&nbsp;",
                    tinyHdr, null));


                if (tinyHdrComment[0] != 0 && passNumber == 2) {
                  let(&hdrCommentMarker, "*");

                  if (hdrCommentAnchorDone == 0) {
                    let(&hdrCommentAnchor, cat("<A NAME=\"",
                        g_Statement[stmt].labelName, "\"></A> ",

                        "&#8203;",

                        null));
                    hdrCommentAnchorDone = 1;
                  } else {
                    let(&hdrCommentAnchor, "");
                  }
                } else {
                  let(&hdrCommentMarker, "");
                  let(&hdrCommentAnchor, "");
                }

                printLongLine(cat(
         "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ",

                    hdrCommentAnchor,
                    "<A HREF=\"", str3, "s\">",
                    hdrCommentMarker,
                    tinyHdr, "</A>",
                    " &nbsp; <A HREF=\"",
                    g_Statement[stmt].labelName, ".html\">",
                    g_Statement[stmt].labelName, "</A>",
                    str4,
                    "<BR>", null),
                    " ",
                    "\"");

                let((vstring *)(&pntrTinyHdr[stmt]), tinyHdr);
                let(&tinyHdr, "");
                let((vstring *)(&pntrTinyHdrComment[stmt]), tinyHdrComment);
                let(&tinyHdrComment, "");
              }


              fprintf(outputFilePtr, "%s", g_printString);
              g_outputToString = 0;
              let(&g_printString, "");
            }
          }
        }

        fprintf(outputFilePtr, "<HR NOSHADE SIZE=1>\n");

      }

    }



    if (page == 0) C.go2("SKIP_LIST");



    g_outputToString = 1;
    mminou.print2("<A NAME=\"mmstmtlst\"></A>\n");


    if (g_extHtmlStmt < g_mathboxStmt) {

      mminou.print2("<P>\n");
      mminou.print2("<CENTER><TABLE CELLSPACING=0 CELLPADDING=5\n");
      mminou.print2("SUMMARY=\"Color key\"><TR>\n");
      mminou.print2("\n");
      mminou.print2("<TD>Color key:&nbsp;&nbsp;&nbsp;</TD>\n");
      mminou.print2("<TD BGCOLOR=%s NOWRAP><A\n", MINT_BACKGROUND_COLOR);
      mminou.print2("HREF=\"mmset.html\"><IMG SRC=\"mm.gif\" BORDER=0\n");
      mminou.print2("ALT=\"Metamath Proof Explorer\" HEIGHT=32 WIDTH=32\n");
      mminou.print2("ALIGN=MIDDLE> &nbsp;Metamath Proof Explorer</A>\n");

      let(&str3, "");
      if (g_Statement[g_extHtmlStmt].pinkNumber <= 0) bug(2332);
      str3 = pinkRangeHTML(nmbrStmtNmbr[1],
          nmbrStmtNmbr[g_Statement[g_extHtmlStmt].pinkNumber - 1]);
      printLongLine(cat("<BR>(", str3, ")", null),
        " ",
        "\"");

      mminou.print2("</TD>\n");
      mminou.print2("\n");
      mminou.print2("<TD WIDTH=10>&nbsp;</TD>\n");
      mminou.print2("\n");


      mminou.print2("<TD BGCOLOR=%s NOWRAP><A\n", PURPLISH_BIBLIO_COLOR);
      mminou.print2(" HREF=\"mmhil.html\"><IMG SRC=\"atomic.gif\"\n");
      mminou.print2(
 "BORDER=0 ALT=\"Hilbert Space Explorer\" HEIGHT=32 WIDTH=32 ALIGN=MIDDLE>\n");
      mminou.print2("&nbsp;Hilbert Space Explorer</A>\n");

      let(&str3, "");

      if (g_Statement[g_mathboxStmt].pinkNumber <= 0) bug(2333);
      str3 = pinkRangeHTML(g_extHtmlStmt,
         nmbrStmtNmbr[g_Statement[g_mathboxStmt].pinkNumber - 1]);
      printLongLine(cat("<BR>(", str3, ")", null),
        " ",
        "\"");

      mminou.print2("</TD>\n");
      mminou.print2("\n");
      mminou.print2("<TD WIDTH=10>&nbsp;</TD>\n");
      mminou.print2("\n");



      mminou.print2("<TD BGCOLOR=%s NOWRAP><A\n", SANDBOX_COLOR);
      mminou.print2(


       " HREF=\"mathbox.html\"><IMG SRC=\"_sandbox.gif\"\n");
      mminou.print2(


         "BORDER=0 ALT=\"Users' Mathboxes\" HEIGHT=32 WIDTH=32 ALIGN=MIDDLE>\n");


      mminou.print2("&nbsp;Users' Mathboxes</A>\n");

      let(&str3, "");
      str3 = pinkRangeHTML(g_mathboxStmt, nmbrStmtNmbr[assertions]);
      printLongLine(cat("<BR>(", str3, ")", null),
        " ",
        "\"");

      mminou.print2("</TD>\n");
      mminou.print2("\n");
      mminou.print2("<TD WIDTH=10>&nbsp;</TD>\n");
      mminou.print2("\n");


      mminou.print2("</TR></TABLE></CENTER>\n");
    }


    fprintf(outputFilePtr, "%s", g_printString);
    g_outputToString = 0;
    let(&g_printString, "");



    g_outputToString = 1;
    mminou.print2("\n");
    mminou.print2("<P><CENTER>\n");
    mminou.print2("<TABLE BORDER CELLSPACING=0 CELLPADDING=3 BGCOLOR=%s\n",
        MINT_BACKGROUND_COLOR);


    mminou.print2("SUMMARY=\"Theorem List for %s\">\n", htmlTitle);
    let(&str3, "");
    if (page < 1) bug(2335);
    str3 = pinkHTML(nmbrStmtNmbr[(page - 1) * theoremsPerPage + 1]);
    let(&str3, right(str3, (long)C.strlen(PINK_NBSP) + 1));
    let(&str4, "");
    str4 = pinkHTML((page < pages) ?
        nmbrStmtNmbr[page * theoremsPerPage] :
        nmbrStmtNmbr[assertions]);
    let(&str4, right(str4, (long)C.strlen(PINK_NBSP) + 1));


    printLongLine(cat("<CAPTION><B>Theorem List for ", htmlTitle,
        " - </B>", str3, "<B>-</B>", str4,
        " &nbsp; *Has distinct variable group(s)"
        "</CAPTION>",null),
        " ",
        "\"");
    mminou.print2("\n");
    mminou.print2("<TR><TH>Type</TH><TH>Label</TH><TH>Description</TH></TR>\n");
    mminou.print2("<TR><TH COLSPAN=3>Statement</TH></TR>\n");
    mminou.print2("\n");
    mminou.print2("<TR BGCOLOR=white><TD COLSPAN=3><FONT SIZE=-3>&nbsp;</FONT></TD></TR>\n");
    mminou.print2("\n");
    fprintf(outputFilePtr, "%s", g_printString);
    g_outputToString = 0;
    let(&g_printString, "");

    lastAssertion = 0;
    for (assertion = (page - 1) * theoremsPerPage + 1;
        assertion <= page * theoremsPerPage; assertion++) {
      if (assertion > assertions) break;




      lastAssertion = assertion;
    }


    for (assertion = (page - 1) * theoremsPerPage + 1;
        assertion <= page * theoremsPerPage; assertion++) {
      if (assertion > assertions) break;

      s = nmbrStmtNmbr[assertion];





      if (g_Statement[s].type == p_) {
        let(&str1, "Theorem");
      } else if (!strcmp("ax-", left(g_Statement[s].labelName, 3))) {
        let(&str1, "<B><FONT COLOR=red>Axiom</FONT></B>");
      } else if (!strcmp("df-", left(g_Statement[s].labelName, 3))) {
        let(&str1, "<B><FONT COLOR=blue>Definition</FONT></B>");
      } else {
        let(&str1, "<B><FONT COLOR=\"#00CC00\">Syntax</FONT></B>");
      }

      if (s == s + 0) C.go2("skip_date");


      let(&str1, space(g_Statement[s + 1].labelSectionLen));
      memcpy(str1, g_Statement[s + 1].labelSectionPtr,
          (size_t)(g_Statement[s + 1].labelSectionLen));
      let(&str1, edit(str1, 2));
      i1 = instr(1, str1, "$([");
      i2 = instr(i1, str1, "]$)");
      if (i1 && i2) {
        let(&str1, seg(str1, i1 + 3, i2 - 1));
      } else {
        let(&str1, "");
      }
      C.label("skip_date");

      let(&str3, "");
      str3 = getDescription(s);
      let(&str4, "");
      str4 = pinkHTML(s);


      let(&g_printString, "");
      g_outputToString = 1;
      mminou.print2("\n");


      if (((vstring)(pntrHugeHdr[s]))[0]) {
        printLongLine(cat(

                 "<TR BGCOLOR=\"#FFFFF2\"><TD COLSPAN=3",


                 "><CENTER><FONT SIZE=\"+1\"><B>",
                 "<A NAME=\"mm", str((double)(g_Statement[s].pinkNumber)),
                     "h\"></A>",
                 (vstring)(pntrHugeHdr[s]),


                 "</B></FONT></CENTER>",
                 null),
            " ",
            "\"");



        if (((vstring)(pntrHugeHdrComment[s]))[0]) {




          mminou.print2("%s\n", "<P STYLE=\"margin-bottom:0em\">");


          g_outputToString = 0;
          fprintf(outputFilePtr, "%s", g_printString);
          let(&g_printString, "");
          g_showStatement = s;
          g_texFilePtr = outputFilePtr;



          printTexComment(
              (vstring)(pntrHugeHdrComment[s]),
              0,
              PROCESS_EVERYTHING,
              0 );
          g_texFilePtr = null;
          g_outputToString = 1;



        }



        mminou.print2("%s\n", "</TD></TR>");

        printLongLine(cat(

                 "<TR BGCOLOR=white><TD COLSPAN=3>",
                 "<FONT SIZE=-3>&nbsp;</FONT></TD></TR>",
                 null),
            " ",
            "\"");
      }
      if (((vstring)(pntrBigHdr[s]))[0]) {
        printLongLine(cat(

                 "<TR BGCOLOR=\"#FFFFF2\"><TD COLSPAN=3",


                 "><CENTER><FONT SIZE=\"+1\"><B>",
                 "<A NAME=\"mm", str((double)(g_Statement[s].pinkNumber)),
                     "b\"></A>",
                 (vstring)(pntrBigHdr[s]),


                 "</B></FONT></CENTER>",
                 null),
            " ",
            "\"");



        if (((vstring)(pntrBigHdrComment[s]))[0]) {




          mminou.print2("%s\n", "<P STYLE=\"margin-bottom:0em\">");


          g_outputToString = 0;
          fprintf(outputFilePtr, "%s", g_printString);
          let(&g_printString, "");
          g_showStatement = s;
          g_texFilePtr = outputFilePtr;



          printTexComment(
              (vstring)(pntrBigHdrComment[s]),
              0,
              PROCESS_EVERYTHING,
              0  );
          g_texFilePtr = null;
          g_outputToString = 1;



        }



        mminou.print2("%s\n", "</TD></TR>");

        printLongLine(cat(

                 "<TR BGCOLOR=white><TD COLSPAN=3>",
                 "<FONT SIZE=-3>&nbsp;</FONT></TD></TR>",
                 null),
            " ",
            "\"");
      }
      if (((vstring)(pntrSmallHdr[s]))[0]) {
        printLongLine(cat(

                 "<TR BGCOLOR=\"#FFFFF2\"><TD COLSPAN=3",


                 "><CENTER><B>",
                 "<A NAME=\"mm", str((double)(g_Statement[s].pinkNumber)),
                     "s\"></A>",
                 (vstring)(pntrSmallHdr[s]),


                 "</B></CENTER>",
                 null),
            " ",
            "\"");



        if (((vstring)(pntrSmallHdrComment[s]))[0]) {




          mminou.print2("%s\n", "<P STYLE=\"margin-bottom:0em\">");


          g_outputToString = 0;
          fprintf(outputFilePtr, "%s", g_printString);
          let(&g_printString, "");
          g_showStatement = s;
          g_texFilePtr = outputFilePtr;



          printTexComment(
              (vstring)(pntrSmallHdrComment[s]),
              0,
              PROCESS_EVERYTHING,
              0  );
          g_texFilePtr = null;
          g_outputToString = 1;



        }



        mminou.print2("%s\n", "</TD></TR>");

        printLongLine(cat(

                 "<TR BGCOLOR=white><TD COLSPAN=3>",
                 "<FONT SIZE=-3>&nbsp;</FONT></TD></TR>",
                 null),
            " ",
            "\"");
      }


      if (((vstring)(pntrTinyHdr[s]))[0]) {
        printLongLine(cat(

                 "<TR BGCOLOR=\"#FFFFF2\"><TD COLSPAN=3",


                 "><CENTER><B>",
                 "<A NAME=\"mm", str((double)(g_Statement[s].pinkNumber)),
                     "s\"></A>",
                 (vstring)(pntrTinyHdr[s]),


                 "</B></CENTER>",
                 null),
            " ",
            "\"");



        if (((vstring)(pntrTinyHdrComment[s]))[0]) {


          \

          mminou.print2("%s\n", "<P STYLE=\"margin-bottom:0em\">");


          g_outputToString = 0;
          fprintf(outputFilePtr, "%s", g_printString);
          let(&g_printString, "");
          g_showStatement = s;
          g_texFilePtr = outputFilePtr;



          printTexComment(
              (vstring)(pntrTinyHdrComment[s]),
              0,
              PROCESS_EVERYTHING,
              0  );
          g_texFilePtr = null;
          g_outputToString = 1;



        }



        mminou.print2("%s\n", "</TD></TR>");

        printLongLine(cat(

                 "<TR BGCOLOR=white><TD COLSPAN=3>",
                 "<FONT SIZE=-3>&nbsp;</FONT></TD></TR>",
                 null),
            " ",
            "\"");
      }


      printLongLine(cat(
            (s < g_extHtmlStmt)
               ? "<TR>"
               : (s < g_mathboxStmt)
                   ? cat("<TR BGCOLOR=", PURPLISH_BIBLIO_COLOR, ">", null)

                   : cat("<TR BGCOLOR=", SANDBOX_COLOR, ">", null),
            "<TD NOWRAP>",
            str1,
            "</TD><TD ALIGN=CENTER><A HREF=\"",
            g_Statement[s].labelName, ".html\">",
            g_Statement[s].labelName, "</A>",
            str4,



            (nmbrLen(g_Statement[s].reqDisjVarsA) > 0) ? "*" : "",

            "</TD><TD ALIGN=LEFT>",

            "<A NAME=\"", g_Statement[s].labelName, "\"></A>",

            null),
          " ",
          "\"");

      g_showStatement = s;
      g_outputToString = 0;
      g_texFilePtr = outputFilePtr;



      printTexComment(
          str3,
          0,
          PROCESS_EVERYTHING,
          0  );
      g_texFilePtr = null;
      g_outputToString = 1;


      let(&str4, "");
      str4 = getTexOrHtmlHypAndAssertion(s);

      if (!strcmp(left(str3, 10), "Lemma for ")
          && !showLemmas) {

        mminou.print2(" <I>[Auxiliary lemma - not displayed.]</I></TD></TR>\n");
      } else {

        printLongLine(cat("</TD></TR><TR",



              (s < g_extHtmlStmt)
                 ? ">"
                 : (s < g_mathboxStmt)
                     ? cat(" BGCOLOR=", PURPLISH_BIBLIO_COLOR, ">", null)
                     : cat(" BGCOLOR=", SANDBOX_COLOR, ">", null),



            "<TD COLSPAN=3 ALIGN=CENTER>",
            str4, "</TD></TR>", null),

            " ",
            "\"");
      }

      g_outputToString = 0;
      fprintf(outputFilePtr, "%s", g_printString);
      let(&g_printString, "");

      if (assertion != lastAssertion) {

        g_outputToString = 1;
        printLongLine(cat("<TR BGCOLOR=white><TD COLSPAN=3>",
            "<FONT SIZE=-3>&nbsp;</FONT></TD></TR>", null),
            " ",
            "\"");
        g_outputToString = 0;
        fprintf(outputFilePtr, "%s", g_printString);
        let(&g_printString, "");
      }
    }


    g_outputToString = 1;
    mminou.print2("</TABLE></CENTER>\n");
    mminou.print2("\n");


    C.label("SKIP_LIST");
    g_outputToString = 1;



    mminou.print2("<TABLE BORDER=0 WIDTH=\"100%c\">\n", '%');
    mminou.print2("  <TR>\n");
    mminou.print2("    <TD ALIGN=LEFT VALIGN=TOP WIDTH=\"25%c\">\n", '%');
    mminou.print2("      &nbsp;\n");
    mminou.print2("    </TD>\n");
    mminou.print2("    <TD NOWRAP ALIGN=CENTER>&nbsp;</TD>\n");
    mminou.print2("    <TD NOWRAP ALIGN=RIGHT VALIGN=TOP WIDTH=\"25%c\"><FONT\n", '%');
    mminou.print2("      SIZE=-1 FACE=sans-serif>\n");
    printLongLine(cat("      ", prevNextLinks, null),
        " ",
        "\"");
    mminou.print2("      </FONT></TD>\n");
    mminou.print2("  </TR>\n");
    mminou.print2("</TABLE>\n");


    mminou.print2("<HR NOSHADE SIZE=1>\n");


    g_outputToString = 0;
    fprintf(outputFilePtr, "%s", g_printString);
    let(&g_printString, "");


    fprintf(outputFilePtr, "<A NAME=\"mmpglst\"></A>\n");
    fprintf(outputFilePtr, "<CENTER>\n");
    fprintf(outputFilePtr, "<B>Page List</B>\n");
    fprintf(outputFilePtr, "</CENTER>\n");




    fprintf(outputFilePtr, "Jump to page: \n");


    for (p = 0; p <= pages; p++) {


      let(&str3, "");
      if (p > 0) {
        str3 = pinkRangeHTML(
            nmbrStmtNmbr[(p - 1) * theoremsPerPage + 1],
            (p < pages) ?
              nmbrStmtNmbr[p * theoremsPerPage] :
              nmbrStmtNmbr[assertions]);
      }


      if (p == page) {
        let(&str1,


            (p == 0) ? "Contents" : str((double)p)
            );
      } else {
        let(&str1, cat("<A HREF=\"mmtheorems",


            (p == 0) ? "" : str((double)p),

            ".html\">",

            (p == 0) ? "Contents" : str((double)p)
            , "</A>", null));
      }
      let(&str1, cat(str1, PINK_NBSP, str3, null));
      fprintf(outputFilePtr, "%s\n", str1);
    }



    g_outputToString = 1;
    mminou.print2("<HR NOSHADE SIZE=1>\n");



    mminou.print2("<TABLE BORDER=0 WIDTH=\"100%c\">\n", '%');
    mminou.print2("  <TR>\n");


    mminou.print2("    <TD ALIGN=LEFT VALIGN=TOP WIDTH=\"25%c\">\n", '%');
    mminou.print2("      &nbsp;\n");
    mminou.print2("    </TD>\n");
    mminou.print2("    <TD NOWRAP ALIGN=CENTER><FONT SIZE=-2\n");
    mminou.print2("      FACE=ARIAL>\n");
    mminou.print2("Copyright terms:\n");
    mminou.print2("<A HREF=\"../copyright.html#pd\">Public domain</A>\n");
    mminou.print2("      </FONT></TD>\n");
    mminou.print2("    <TD NOWRAP ALIGN=RIGHT VALIGN=TOP WIDTH=\"25%c\"><FONT\n", '%');
    mminou.print2("      SIZE=-1 FACE=sans-serif>\n");

    printLongLine(cat("      ", prevNextLinks, null),
        " ",
        "\"");
    mminou.print2("      </FONT></TD>\n");
    mminou.print2("  </TR>\n");
    mminou.print2("</TABLE>\n");



    mminou.print2("</BODY></HTML>\n");
    g_outputToString = 0;
    fprintf(outputFilePtr, "%s", g_printString);
    let(&g_printString, "");


    C.fclose(outputFilePtr);
  }

  C.label("TL_ABORT");

  let(&str1, "");
  let(&str3, "");
  let(&str4, "");
  let(&prevNextLinks, "");
  let(&outputFileName, "");
  let(&hugeHdr, "");
  let(&bigHdr, "");
  let(&smallHdr, "");
  let(&tinyHdr, "");
  let(&hdrCommentMarker, "");
  for (i = 0; i <= g_statements; i++) let((vstring *)(&pntrHugeHdr[i]), "");
  pntrLet(&pntrHugeHdr, NULL_PNTRSTRING);
  for (i = 0; i <= g_statements; i++) let((vstring *)(&pntrBigHdr[i]), "");
  pntrLet(&pntrBigHdr, NULL_PNTRSTRING);
  for (i = 0; i <= g_statements; i++) let((vstring *)(&pntrSmallHdr[i]), "");
  pntrLet(&pntrSmallHdr, NULL_PNTRSTRING);

  for (i = 0; i <= g_statements; i++) let((vstring *)(&pntrTinyHdr[i]), "");
  pntrLet(&pntrTinyHdr, NULL_PNTRSTRING);

}









flag getSectionHeadings(long stmt,
    vstring *hugeHdrTitle,
    vstring *bigHdrTitle,
    vstring *smallHdrTitle,
    vstring *tinyHdrTitle,

    vstring *hugeHdrComment,
    vstring *bigHdrComment,
    vstring *smallHdrComment,
    vstring *tinyHdrComment,
    flag fineResolution,
    flag fullComment
    )
{


  vstring labelStr = "";
  long pos, pos1, pos2, pos3, pos4;
  flag errorFound = 0;
  flag saveOutputToString;



  saveOutputToString = g_outputToString;
  g_outputToString = 0;


  let(&(*hugeHdrTitle), "");
  let(&(*bigHdrTitle), "");
  let(&(*smallHdrTitle), "");
  let(&(*tinyHdrTitle), "");
  let(&(*hugeHdrComment), "");
  let(&(*bigHdrComment), "");
  let(&(*smallHdrComment), "");
  let(&(*tinyHdrComment), "");



  if (fineResolution == 0) {
    if (g_Statement[stmt].type != a_ && g_Statement[stmt].type != p_) bug(2340);
  }



  if (fineResolution == 0) {
    pos3 = g_Statement[stmt].headerStartStmt;
  } else {

    pos3 = stmt;
  }
  if (pos3 == 0 || pos3 > stmt) bug(2241);
  pos4 = (g_Statement[stmt].labelSectionPtr
        - g_Statement[pos3].labelSectionPtr)
        + g_Statement[stmt].labelSectionLen;
  let(&labelStr, space(pos4));
  memcpy(labelStr, g_Statement[pos3].labelSectionPtr,
      (size_t)(pos4));


  pos = 0;
  pos2 = 0;
  while (true) {
    pos1 = pos;
    pos = instr(pos + 1, labelStr, "$(\n" HUGE_DECORATION);

    pos1 = instr(pos1 + 1, labelStr, "$( \n" HUGE_DECORATION);
    if (pos1 > pos) pos = pos1;

    if (!pos) break;
    if (pos) pos2 = pos;
  }
  if (pos2) {
    pos1 = pos2;
    pos = instr(pos2 + 4, labelStr, "\n");
    pos2 = instr(pos + 1, labelStr, "\n");


    if (strcmp(mid(labelStr, pos2 + 1, 4), HUGE_DECORATION)) {
      mminou.print2(
       "?Warning: missing closing \"%s\" decoration above statement \"%s\".\n",
          HUGE_DECORATION, g_Statement[stmt].labelName);
      errorFound = 1;
    }

    pos3 = instr(pos2 + 1, labelStr, "\n");
    while (labelStr[(pos3 - 1) + 1] == '\n') pos3++;
    pos4 = instr(pos3, labelStr, "$)");
    if (fullComment == 0) {
      let(&(*hugeHdrTitle), seg(labelStr, pos + 1, pos2 - 1));
      let(&(*hugeHdrTitle), edit((*hugeHdrTitle), 8 + 128));

      let(&(*hugeHdrComment), seg(labelStr, pos3 + 1, pos4 - 2));
      let(&(*hugeHdrComment), edit((*hugeHdrComment), 8 + 16384));

    } else {



      pos = pos1;
      pos--;
      while (pos > 0) {
        if (labelStr[pos - 1] != ' '
              && labelStr[pos - 1] != '\n') break;
        pos--;
      }



      let(&(*hugeHdrTitle), seg(labelStr, pos + 1, pos3));
      let(&(*hugeHdrComment), seg(labelStr, pos3 + 1, pos4 + 1));
    }
  }
  pos2 = 0;
  while (true) {
    pos1 = pos;
    pos = instr(pos + 1, labelStr, "$(\n" BIG_DECORATION);

    pos1 = instr(pos1 + 1, labelStr, "$( \n" BIG_DECORATION);
    if (pos1 > pos) pos = pos1;

    if (!pos) break;
    if (pos) pos2 = pos;
  }
  if (pos2) {
    pos1 = pos2;
    pos = instr(pos2 + 4, labelStr, "\n");
    pos2 = instr(pos + 1, labelStr, "\n");


    if (strcmp(mid(labelStr, pos2 + 1, 4), BIG_DECORATION)) {
      mminou.print2(
       "?Warning: missing closing \"%s\" decoration above statement \"%s\".\n",
          BIG_DECORATION, g_Statement[stmt].labelName);
      errorFound = 1;
    }

    pos3 = instr(pos2 + 1, labelStr, "\n");
    while (labelStr[(pos3 - 1) + 1] == '\n') pos3++;
    pos4 = instr(pos3, labelStr, "$)");
    if (fullComment == 0) {
      let(&(*bigHdrTitle), seg(labelStr, pos + 1, pos2 - 1));
      let(&(*bigHdrTitle), edit((*bigHdrTitle), 8 + 128));

      let(&(*bigHdrComment), seg(labelStr, pos3 + 1, pos4 - 2));
      let(&(*bigHdrComment), edit((*bigHdrComment), 8 + 16384));

    } else {



      pos = pos1;
      pos--;
      while (pos > 0) {
        if (labelStr[pos - 1] != ' '
              && labelStr[pos - 1] != '\n') break;
        pos--;
      }



      let(&(*bigHdrTitle), seg(labelStr, pos + 1, pos3));
      let(&(*bigHdrComment), seg(labelStr, pos3 + 1, pos4 + 1));
    }
  }
  pos2 = 0;
  while (true) {
    pos1 = pos;
    pos = instr(pos + 1, labelStr, "$(\n" SMALL_DECORATION);

    pos1 = instr(pos1 + 1, labelStr, "$( \n" SMALL_DECORATION);
    if (pos1 > pos) pos = pos1;

    if (!pos) break;
    if (pos) pos2 = pos;
  }
  if (pos2) {
    pos1 = pos2;
    pos = instr(pos2 + 4, labelStr, "\n");
    pos2 = instr(pos + 1, labelStr, "\n");


    if (strcmp(mid(labelStr, pos2 + 1, 4), SMALL_DECORATION)) {
      mminou.print2(
       "?Warning: missing closing \"%s\" decoration above statement \"%s\".\n",
          SMALL_DECORATION, g_Statement[stmt].labelName);
      errorFound = 1;
    }

    pos3 = instr(pos2 + 1, labelStr, "\n");
    while (labelStr[(pos3 - 1) + 1] == '\n') pos3++;
    pos4 = instr(pos3, labelStr, "$)");
    if (fullComment == 0) {
      let(&(*smallHdrTitle), seg(labelStr, pos + 1, pos2 - 1));
      let(&(*smallHdrTitle), edit((*smallHdrTitle), 8 + 128));

      let(&(*smallHdrComment), seg(labelStr, pos3 + 1, pos4 - 2));
      let(&(*smallHdrComment), edit((*smallHdrComment), 8 + 16384));

    } else {



      pos = pos1;
      pos--;
      while (pos > 0) {
        if (labelStr[pos - 1] != ' '
              && labelStr[pos - 1] != '\n') break;
        pos--;
      }



      let(&(*smallHdrTitle), seg(labelStr, pos + 1, pos3));
      let(&(*smallHdrComment), seg(labelStr, pos3 + 1, pos4 + 1));
    }
  }


  pos2 = 0;
  while (true) {
    pos1 = pos;
    pos = instr(pos + 1, labelStr, "$(\n" TINY_DECORATION);

    pos1 = instr(pos1 + 1, labelStr, "$( \n" TINY_DECORATION);
    if (pos1 > pos) pos = pos1;

    if (!pos) break;
    if (pos) pos2 = pos;
  }
  if (pos2) {
    pos1 = pos2;
    pos = instr(pos2 + 4, labelStr, "\n");
    pos2 = instr(pos + 1, labelStr, "\n");


    if (strcmp(mid(labelStr, pos2 + 1, 4), TINY_DECORATION)) {
      mminou.print2(
       "?Warning: missing closing \"%s\" decoration above statement \"%s\".\n",
          TINY_DECORATION, g_Statement[stmt].labelName);
      errorFound = 1;
    }

    pos3 = instr(pos2 + 1, labelStr, "\n");
    while (labelStr[(pos3 - 1) + 1] == '\n') pos3++;
    pos4 = instr(pos3, labelStr, "$)");
    if (fullComment == 0) {
      let(&(*tinyHdrTitle), seg(labelStr, pos + 1, pos2 - 1));
      let(&(*tinyHdrTitle), edit((*tinyHdrTitle), 8 + 128));

      let(&(*tinyHdrComment), seg(labelStr, pos3 + 1, pos4 - 2));
      let(&(*tinyHdrComment), edit((*tinyHdrComment), 8 + 16384));

    } else {



      pos = pos1;
      pos--;
      while (pos > 0) {
        if (labelStr[pos - 1] != ' '
              && labelStr[pos - 1] != '\n') break;
        pos--;
      }



      let(&(*tinyHdrTitle), seg(labelStr, pos + 1, pos3));
      let(&(*tinyHdrComment), seg(labelStr, pos3 + 1, pos4 + 1));
    }
  }



  if (errorFound == 1) {
    mminou.print2("  (Note that section titles may not be longer than one line.)\n");
  }

  g_outputToString = saveOutputToString;

  let(&labelStr, "");
  return errorFound;
}




#ifdef DUMMY
long pinkNumber(long statemNum)
{
  long statemMap = 0;
  long i;
  statemMap = 0;
  for (i = 1; i <= statemNum; i++) {
    if (g_Statement[i].type == a_ || g_Statement[i].type == p_)
      statemMap++;
  }
  return statemMap;
}
#endif


vstring pinkHTML(long statemNum)
{
  long statemMap;
  vstring htmlCode = "";
  vstring hexValue = "";


  if (statemNum > 0) {
    statemMap = g_Statement[statemNum].pinkNumber;
  } else {

    statemMap = -1;
  }




#ifndef RAINBOW_OPTION

  let(&htmlCode, cat(PINK_NBSP,
      "<SPAN CLASS=p>",
      (statemMap != -1) ? str((double)statemMap) : "(future)", "</SPAN>", null));
#endif

#ifdef RAINBOW_OPTION

  let(&hexValue, "");
  hexValue = spectrumToRGB(statemMap, g_Statement[g_statements].pinkNumber);
  let(&htmlCode, cat(PINK_NBSP,
      "<SPAN CLASS=r STYLE=\"color:#", hexValue, "\">",
      (statemMap != -1) ? str((double)statemMap) : "(future)", "</SPAN>", null));
#endif
  let(&hexValue, "");

  return htmlCode;
}




vstring pinkRangeHTML(long statemNum1, long statemNum2)
{
  vstring htmlCode = "";
  vstring str3 = "";
  vstring str4 = "";


  let(&str3, "");
  str3 = pinkHTML(statemNum1);
  let(&str3, right(str3, (long)C.strlen(PINK_NBSP) + 1));
  let(&str4, "");
  str4 = pinkHTML(statemNum2);
  let(&str4, right(str4, (long)C.strlen(PINK_NBSP) + 1));
  let(&htmlCode, cat(str3, "-", str4, null));
  let(&str3, "");
  let(&str4, "");
  return htmlCode;
}


#ifdef RAINBOW_OPTION


vstring spectrumToRGB(long color, long maxColor) {
  vstring str1 = "";
  double fraction, fractionInPartition;
  long j, red, green, blue, partition;

  static final long PARTITIONS =D.PARTITIONS;
  static double redRef[PARTITIONS +  1];
  static double greenRef[PARTITIONS +  1];
  static double blueRef[PARTITIONS +  1];
  static long i = -1;

  if (i > -1) C.go2("SKIP_INIT");
  i = -1;

#define L53empirical
#ifdef L53empirical
  i++; redRef[i] = 251; greenRef[i] = 0; blueRef[i] = 0;
  i++; redRef[i] = 247; greenRef[i] = 12; blueRef[i] = 0;
  i++; redRef[i] = 238; greenRef[i] = 44; blueRef[i] = 0;
  i++; redRef[i] = 222; greenRef[i] = 71; blueRef[i] = 0;
  i++; redRef[i] = 203; greenRef[i] = 89; blueRef[i] = 0;
  i++; redRef[i] = 178; greenRef[i] = 108; blueRef[i] = 0;
  i++; redRef[i] = 154; greenRef[i] = 122; blueRef[i] = 0;
  i++; redRef[i] = 127; greenRef[i] = 131; blueRef[i] = 0;
  i++; redRef[i] = 110; greenRef[i] = 136; blueRef[i] = 0;
  i++; redRef[i] = 86; greenRef[i] = 141; blueRef[i] = 0;
  i++; redRef[i] = 60; greenRef[i] = 144; blueRef[i] = 0;
  i++; redRef[i] = 30; greenRef[i] = 147; blueRef[i] = 0;
  i++; redRef[i] = 0; greenRef[i] = 148; blueRef[i] = 22;
  i++; redRef[i] = 0; greenRef[i] = 145; blueRef[i] = 61;
  i++; redRef[i] = 0; greenRef[i] = 145; blueRef[i] = 94;
  i++; redRef[i] = 0; greenRef[i] = 143; blueRef[i] = 127;
  i++; redRef[i] = 0; greenRef[i] = 140; blueRef[i] = 164;
  i++; redRef[i] = 0; greenRef[i] = 133; blueRef[i] = 218;
  i++; redRef[i] = 3; greenRef[i] = 127; blueRef[i] = 255;
  i++; redRef[i] = 71; greenRef[i] = 119; blueRef[i] = 255;
  i++; redRef[i] = 110; greenRef[i] = 109; blueRef[i] = 255;
  i++; redRef[i] = 137; greenRef[i] = 99; blueRef[i] = 255;
  i++; redRef[i] = 169; greenRef[i] = 78; blueRef[i] = 255;
  i++; redRef[i] = 186; greenRef[i] = 57; blueRef[i] = 255;
  i++; redRef[i] = 204; greenRef[i] = 33; blueRef[i] = 249;
  i++; redRef[i] = 213; greenRef[i] = 16; blueRef[i] = 235;
  i++; redRef[i] = 221; greenRef[i] = 0; blueRef[i] = 222;
  i++; redRef[i] = 233; greenRef[i] = 0; blueRef[i] = 172;
  i++; redRef[i] = 239; greenRef[i] = 0; blueRef[i] = 132;


#endif

#ifdef L53obsolete

  i++; redRef[i] = 251; greenRef[i] = 0; blueRef[i] = 0;
  i++; redRef[i] = 234; greenRef[i] = 59; blueRef[i] = 0;
  i++; redRef[i] = 196; greenRef[i] = 98; blueRef[i] = 0;
  i++; redRef[i] = 160; greenRef[i] = 120; blueRef[i] = 0;

  i++; redRef[i] = 104; greenRef[i] = 138; blueRef[i] = 0;


  i++; redRef[i] = 0; greenRef[i] = 148; blueRef[i] = 0;


  i++; redRef[i] = 0; greenRef[i] = 145; blueRef[i] = 109;


  i++; redRef[i] = 0; greenRef[i] = 128; blueRef[i] = 255;

  i++; redRef[i] = 110; greenRef[i] = 110; blueRef[i] = 255;
  i++; redRef[i] = 138; greenRef[i] = 99; blueRef[i] = 255;
  i++; redRef[i] = 168; greenRef[i] = 81; blueRef[i] = 255;
  i++; redRef[i] = 201; greenRef[i] = 40; blueRef[i] = 255;
  i++; redRef[i] = 222; greenRef[i] = 0; blueRef[i] = 222;
  i++; redRef[i] = 233; greenRef[i] = 0; blueRef[i] = 175;
  i++; redRef[i] = 241; greenRef[i] = 0; blueRef[i] = 120;

#endif

#ifdef L68obsolete

  i++; redRef[i] = 255; greenRef[i] = 122; blueRef[i] = 122;
  i++; redRef[i] = 255; greenRef[i] = 127; blueRef[i] = 0;
  i++; redRef[i] = 207; greenRef[i] = 155; blueRef[i] = 0;
  i++; redRef[i] = 170; greenRef[i] = 170; blueRef[i] = 0;
  i++; redRef[i] = 93; greenRef[i] = 186; blueRef[i] = 0;
  i++; redRef[i] = 0; greenRef[i] = 196; blueRef[i] = 0;
  i++; redRef[i] = 0; greenRef[i] = 190; blueRef[i] = 94;
  i++; redRef[i] = 0; greenRef[i] = 185; blueRef[i] = 185;
  i++; redRef[i] = 87; greenRef[i] = 171; blueRef[i] = 255;
  i++; redRef[i] = 156; greenRef[i] = 156; blueRef[i] = 255;
  i++; redRef[i] = 197; greenRef[i] = 140; blueRef[i] = 255;

  i++; redRef[i] = 255; greenRef[i] = 100; blueRef[i] = 255;
  i++; redRef[i] = 255; greenRef[i] = 115; blueRef[i] = 185;
#endif

#ifdef L53S57obsolete

  i++; redRef[i] = 206; greenRef[i] = 89; blueRef[i] = 89;
  i++; redRef[i] = 184; greenRef[i] = 105; blueRef[i] = 79;
  i++; redRef[i] = 164; greenRef[i] = 117; blueRef[i] = 71;
  i++; redRef[i] = 145; greenRef[i] = 124; blueRef[i] = 62;


  i++; redRef[i] = 98; greenRef[i] = 137; blueRef[i] = 59;

  i++; redRef[i] = 62; greenRef[i] = 144; blueRef[i] = 62;

  i++; redRef[i] = 61; greenRef[i] = 142; blueRef[i] = 102;



  i++; redRef[i] = 80; greenRef[i] = 132; blueRef[i] = 185;

  i++; redRef[i] = 110; greenRef[i] = 110; blueRef[i] = 255;
  i++; redRef[i] = 139; greenRef[i] = 104; blueRef[i] = 242;
  i++; redRef[i] = 159; greenRef[i] = 96; blueRef[i] = 223;
  i++; redRef[i] = 178; greenRef[i] = 89; blueRef[i] = 207;
  i++; redRef[i] = 191; greenRef[i] = 82; blueRef[i] = 191;
  i++; redRef[i] = 194; greenRef[i] = 83; blueRef[i] = 166;
  i++; redRef[i] = 199; greenRef[i] = 86; blueRef[i] = 142;

#endif

  if (i != PARTITIONS) {
    mminou.print2("? %ld partitions but PARTITIONS = %ld\n", i, (long)PARTITIONS);
    bug(2326);
  }

  C.label("SKIP_INIT");
  if (color == -1) {
    let(&str1, "000000");
    return str1;
  }

  if (color < 1 || color > maxColor) {
    bug(2327);
  }
  fraction = (1.0 * ((double)color - 1)) / (double)maxColor;

  partition = (long)(PARTITIONS * fraction);
  if (partition >= PARTITIONS) bug(2325);
  fractionInPartition = 1.0 * (fraction - (1.0 * (double)partition) / PARTITIONS)
      * PARTITIONS;
  red = (long)(1.0 * (redRef[partition] +
          fractionInPartition *
              (redRef[partition + 1] - redRef[partition])));
  green = (long)(1.0 * (greenRef[partition] +
          fractionInPartition *
              (greenRef[partition + 1] - greenRef[partition])));
  blue = (long)(1.0 * (blueRef[partition] +
          fractionInPartition *
              (blueRef[partition + 1] - blueRef[partition])));





  if (red < 0 || green < 0 || blue < 0
      || red > 255 || green > 255 || blue > 255) {
    mminou.print2("%ld %ld %ld\n", red, green, blue);
    bug(2323);
  }
  let(&str1, "      ");
  j = sprintf(str1, "%02X%02X%02X", (unsigned int)red, (unsigned int)green,
      (unsigned int)blue);
  if (j != 6) bug(2324);


  return str1;
}
#endif



vstring getTexLongMath(nmbrString *mathString, long statemNum)
{
  long pos;
  vstring tex = "";
  vstring texLine = "";
  vstring lastTex = "";
  flag alphnew, alphold, unknownnew, unknownold;

  if (!g_texDefsRead) bug(2322);
  let(&texLine, "");

  let(&lastTex, "");
  for (pos = 0; pos < nmbrLen(mathString); pos++) {
    let(&tex, "");
    tex = tokenToTex(g_MathToken[mathString[pos]].tokenName, statemNum);

    if (!g_htmlFlag) {

      alphnew = !!isalpha((unsigned char)(tex[0]));
      unknownnew = 0;
      if (!strcmp(left(tex, 10), "\\mbox{\\rm ")) {
        unknownnew = 1;
      }
      alphold = !!isalpha((unsigned char)(lastTex[0]));
      unknownold = 0;
      if (!strcmp(left(lastTex, 10), "\\mbox{\\rm ")) {
        unknownold = 1;
      }


      if ((alphold || unknownold) && (alphnew || unknownnew)) {


        if (!g_oldTexFlag) {
          let(&texLine, cat(texLine, "\\,", tex, " ", null));
        } else {
          let(&texLine, cat(texLine, "\\m{\\,", tex, "}", null));
        }
      } else {

        if (!g_oldTexFlag) {
          let(&texLine, cat(texLine, "", tex, " ", null));
        } else {
          let(&texLine, cat(texLine, "\\m{", tex, "}", null));
        }
      }
    } else {


      if (pos >=4) {
        if (!strcmp(g_MathToken[mathString[pos - 2]].tokenName, "e.")
            && (!strcmp(g_MathToken[mathString[pos - 4]].tokenName, "E.")
              || !strcmp(g_MathToken[mathString[pos - 4]].tokenName, "A.")


              || !strcmp(g_MathToken[mathString[pos - 4]].tokenName, "prod_")
              || !strcmp(g_MathToken[mathString[pos - 4]].tokenName, "E*")
              || !strcmp(g_MathToken[mathString[pos - 4]].tokenName, "iota_")
              || !strcmp(g_MathToken[mathString[pos - 4]].tokenName, "Disj_")


              || !strcmp(g_MathToken[mathString[pos - 4]].tokenName, "E!")

              || !strcmp(g_MathToken[mathString[pos - 4]].tokenName, "sum_")

              || !strcmp(g_MathToken[mathString[pos - 4]].tokenName, "X_")

              || !strcmp(g_MathToken[mathString[pos - 4]].tokenName, "U_")
              || !strcmp(g_MathToken[mathString[pos - 4]].tokenName, "|^|_"))


            && strcmp(g_MathToken[mathString[pos]].tokenName, ")")

            && strcmp(g_MathToken[mathString[pos - 1]].tokenName, "(")

            && strcmp(g_MathToken[mathString[pos - 1]].tokenName, "U.")
            && strcmp(g_MathToken[mathString[pos - 1]].tokenName, "|^|")

            && strcmp(g_MathToken[mathString[pos - 1]].tokenName, "{")) {
          let(&texLine, cat(texLine, " ", null));
        }
      }
      if (pos >=2) {

        if (isalpha((unsigned char)(g_MathToken[mathString[pos]].tokenName[0]))) {

          if (!(g_MathToken[mathString[pos]].tokenName[1])) {



            if (isalpha((unsigned char)(g_MathToken[mathString[pos - 1]].tokenName[0]))) {

              if (!(g_MathToken[mathString[pos - 1]].tokenName[1])) {


                if (!strcmp(g_MathToken[mathString[pos - 2]].tokenName, "E.")
                    || !strcmp(g_MathToken[mathString[pos - 2]].tokenName, "A.")

                    || !strcmp(g_MathToken[mathString[pos - 2]].tokenName, "F/")

                    || !strcmp(g_MathToken[mathString[pos - 2]].tokenName, "E!")

                    || !strcmp(g_MathToken[mathString[pos - 2]].tokenName, "ran")
                    || !strcmp(g_MathToken[mathString[pos - 2]].tokenName, "dom")
                    || !strcmp(g_MathToken[mathString[pos - 2]].tokenName, "E*")) {
                  let(&texLine, cat(texLine, " ", null));
                }

              }
            }

          }
        }
      }
      if (pos >= 1) {

        if (!strcmp(g_MathToken[mathString[pos]].tokenName, "suc")) {

          if (isalpha(
              (unsigned char)(g_MathToken[mathString[pos - 1]].tokenName[0]))) {

            if (!(g_MathToken[mathString[pos - 1]].tokenName[1])) {
              let(&texLine, cat(texLine, " ", null));
            }
          }
        }
      }
      if (pos >=1) {

        if (strcmp(g_MathToken[mathString[pos - 1]].tokenName, "(")
            && !strcmp(g_MathToken[mathString[pos]].tokenName, "-.")) {
          let(&texLine, cat(texLine, " ", null));
        }
      }

      if (pos >=4) {
        if (!strcmp(g_MathToken[mathString[pos - 4]].tokenName, "Isom")
            && !strcmp(g_MathToken[mathString[pos - 2]].tokenName, ",")
            && !strcmp(g_MathToken[mathString[pos]].tokenName, "(")) {
          let(&texLine, cat(texLine, " ", null));
        }
      }
      if (pos >=1) {

        if (!strcmp(g_MathToken[mathString[pos - 1]].tokenName, "}")
            && !strcmp(g_MathToken[mathString[pos]].tokenName, "(")) {
          let(&texLine, cat(texLine, " ", null));
        }
      }
      if (pos >=1) {

        if (!strcmp(g_MathToken[mathString[pos - 1]].tokenName, "}")
            && !strcmp(g_MathToken[mathString[pos]].tokenName, "{")) {
          let(&texLine, cat(texLine, " ", null));
        }
      }


      let(&texLine, cat(texLine, tex, null));
    }
    let(&lastTex, tex);
  }


  let(&texLine, edit(texLine, 8 + 16 + 128));



  let(&texLine, cat(
      (g_altHtmlFlag ? cat("<SPAN ", g_htmlFont, ">", null) : ""),
      texLine,
      (g_altHtmlFlag ? "</SPAN>" : ""), null));

  let(&tex, "");
  let(&lastTex, "");
  return texLine;
}



vstring getTexOrHtmlHypAndAssertion(long statemNum)
{
  long reqHyps, essHyps, n;
  nmbrString *nmbrTmpPtr;
  vstring texOrHtmlCode = "";
  vstring str2 = "";

  essHyps = 0;
  reqHyps = nmbrLen(g_Statement[statemNum].reqHypList);
  let(&texOrHtmlCode, "");
  for (n = 0; n < reqHyps; n++) {
    if (g_Statement[g_Statement[statemNum].reqHypList[n]].type
        == (char)e_) {
      essHyps++;
      if (texOrHtmlCode[0]) {
        if (!g_htmlFlag) {

          let(&texOrHtmlCode, cat(texOrHtmlCode,
                 "\\quad\\&\\quad "
              ,null));
        } else {
          if (g_altHtmlFlag) {

            let(&texOrHtmlCode, cat(texOrHtmlCode,


                "<SPAN ", g_htmlFont, ">",
                "&nbsp;&nbsp;&nbsp; &amp;&nbsp;&nbsp;&nbsp;",
                "</SPAN>",
                null));
          } else {

            let(&texOrHtmlCode, cat(texOrHtmlCode,
          "&nbsp;&nbsp;&nbsp;<IMG SRC='amp.gif' WIDTH=12 HEIGHT=19 ALT='&amp;'"
                ," ALIGN=TOP>&nbsp;&nbsp;&nbsp;"
                ,null));
          }
        }
      }

      nmbrTmpPtr = g_Statement[g_Statement[statemNum].reqHypList[n]].mathString;
      let(&str2, "");
      str2 = getTexLongMath(nmbrTmpPtr, statemNum);
      let(&texOrHtmlCode, cat(texOrHtmlCode, str2, null));
    }
  }
  if (essHyps) {
    if (!g_htmlFlag) {

      let(&texOrHtmlCode, cat(texOrHtmlCode,
                 "\\quad\\Rightarrow\\quad "
          ,null));
    } else {
      if (g_altHtmlFlag) {

        let(&texOrHtmlCode, cat(texOrHtmlCode,



      "&nbsp;&nbsp;&nbsp; <FONT FACE=sans-serif>&#8658;</FONT>&nbsp;&nbsp;&nbsp;"
            ,null));
      } else {

        let(&texOrHtmlCode, cat(texOrHtmlCode,
          "&nbsp;&nbsp;&nbsp;<IMG SRC='bigto.gif' WIDTH=15 HEIGHT=19 ALT='=&gt;'"
            ," ALIGN=TOP>&nbsp;&nbsp;&nbsp;"
            ,null));
      }
    }
  }

  nmbrTmpPtr = g_Statement[statemNum].mathString;
  let(&str2, "");
  str2 = getTexLongMath(nmbrTmpPtr, statemNum);
  let(&texOrHtmlCode, cat(texOrHtmlCode, str2, null));


  let(&str2, "");
  return texOrHtmlCode;
}






flag writeBibliography(vstring bibFile,
    vstring labelMatch,
    flag errorsOnly,
    flag noFileCheck)
{
  flag errFlag;
  File list1_fp = null;
  File list2_fp = null;
  long lines, p2, i, j, jend, k, l, m, n, p, q, s, pass1refs;
  vstring str1 = "", str2 = "", str3 = "", str4 = "", newstr = "", oldstr = "";
  pntrString *pntrTmp = NULL_PNTRSTRING;
  flag warnFlag;

  n = 0;
  pass1refs = 0;
  if (noFileCheck == 1 && errorsOnly == 0) {
    bug(2336);
  }

  warnFlag = 0;
  errFlag = 0;
  if (noFileCheck == 0) {
    list1_fp = fSafeOpen(bibFile, "r", 0);
    if (list1_fp == null) {

      return 1;
    }
    if (errorsOnly == 0) {
      C.fclose(list1_fp);

      list2_fp = fSafeOpen(bibFile, "w", 0);
      if (list2_fp == null) {

        return 1;
      }
      list1_fp = fSafeOpen(cat(bibFile, "~1", null), "r", 0);
      if (list1_fp == null) bug(2337);
    }
  }
  if (!g_texDefsRead) {
    g_htmlFlag = 1;
    if (2 == readTexDefs(errorsOnly, noFileCheck)) {
      errFlag = 2;
      C.go2("BIB_ERROR");
    }
  }


  if (noFileCheck == 0) {
    while (true) {
      if (!linput(list1_fp, null, &str1)) {
        mminou.print2(
  "?Error: Could not find \"<!-- #START# -->\" line in input file \"%s\".\n",
            bibFile);
        errFlag = 2;
        break;
      }
      if (errorsOnly == 0) {
        fprintf(list2_fp, "%s\n", str1);
      }
      if (!strcmp(str1, "<!-- #START# -->")) break;
    }
    if (errFlag) C.go2("BIB_ERROR");
  }

  p2 = 1;
  lines = 0;
  while (true) {

    if (p2 == 2) {

      pntrLet(&pntrTmp, pntrSpace(lines));
      lines = 0;
    }


    for (i = 1; i <= g_statements; i++) {
      if (g_Statement[i].type != (char)p_ &&
        g_Statement[i].type != (char)a_) continue;


      if (!matchesList(g_Statement[i].labelName, labelMatch, '*', '?')) {
        continue;
      }


      if (instr(1, g_Statement[i].labelName, "NEW")) continue;
      if (instr(1, g_Statement[i].labelName, "OLD")) continue;
      let(&str1, "");
      str1 = getDescription(i);
      if (!instr(1, str1, "[")) continue;
      l = (signed)(C.strlen(str1));
      for (j = 0; j < l; j++) {
        if (str1[j] == '\n') str1[j] = ' ';
        if (str1[j] == '\r') bug(2338);
      }
      let(&str1, edit(str1, 8 + 128 + 16));


      k = 0;
      l = (signed)(C.strlen(str1));
      for (j = 0; j < l - 1; j++) {
        if (k == 0) {
          if (str1[j] == '`') {
            k = 1;
          }
        } else {
          if (str1[j] == '`') {
            if (str1[j + 1] == '`') {

              str1[j] = ' ';
              str1[j + 1] = ' ';
            } else {
              k = 0;
            }
          } else {
            str1[j] = ' ';
          }
        }
      }


      j = 0;
      while (true) {
        j = instr(j + 1, str1, " p. ");
        if (!j) break;
        if (j) {
          for (k = j + 4; k <= (signed)(C.strlen(str1)) + 1; k++) {
            if (!isdigit((unsigned char)(str1[k - 1]))) {
              let(&str1, cat(left(str1, j + 2),
                  space(4 - (k - (j + 4))), right(str1, j + 3), null));

              let(&str1, cat(left(str1, j + 7), "###", right(str1, j + 8),
                  null));
              break;
            }
          }
        }
      }

      j = 0;
      n = 0;
      while (true) {
        j = instr(j + 1, str1, "[");
        if (!j) break;


        jend = instr(j, str1, "]");
        if (!jend) break;

        if (instr(1, seg(str1, j, jend), " ")) continue;


        if (str1[j] == '[') {
          j++;
          continue;
        }
        if (!isalnum((unsigned char)(str1[j]))) continue;
        n++;

        m = 0;
        let(&str2, edit(str1, 32));

        for (k = j - 1; k >= 1; k--) {
          if (0




              || !strcmp(mid(str2, k, (long)C.strlen("THEOREM")), "THEOREM")
              || !strcmp(mid(str2, k, (long)C.strlen("EQUATION")), "EQUATION")
              || !strcmp(mid(str2, k, (long)C.strlen("DEFINITION")), "DEFINITION")
              || !strcmp(mid(str2, k, (long)C.strlen("LEMMA")), "LEMMA")
              || !strcmp(mid(str2, k, (long)C.strlen("EXERCISE")), "EXERCISE")
              || !strcmp(mid(str2, k, (long)C.strlen("AXIOM")), "AXIOM")
              || !strcmp(mid(str2, k, (long)C.strlen("CLAIM")), "CLAIM")
              || !strcmp(mid(str2, k, (long)C.strlen("CHAPTER")), "CHAPTER")
              || !strcmp(mid(str2, k, (long)C.strlen("COMPARE")), "COMPARE")
              || !strcmp(mid(str2, k, (long)C.strlen("CONDITION")), "CONDITION")
              || !strcmp(mid(str2, k, (long)C.strlen("CONJECTURE")), "CONJECTURE")
              || !strcmp(mid(str2, k, (long)C.strlen("COROLLARY")), "COROLLARY")
              || !strcmp(mid(str2, k, (long)C.strlen("EXAMPLE")), "EXAMPLE")
              || !strcmp(mid(str2, k, (long)C.strlen("FIGURE")), "FIGURE")
              || !strcmp(mid(str2, k, (long)C.strlen("ITEM")), "ITEM")
              || !strcmp(mid(str2, k, (long)C.strlen("LEMMAS")), "LEMMAS")
              || !strcmp(mid(str2, k, (long)C.strlen("LINE")), "LINE")
              || !strcmp(mid(str2, k, (long)C.strlen("LINES")), "LINES")
              || !strcmp(mid(str2, k, (long)C.strlen("NOTATION")), "NOTATION")
              || !strcmp(mid(str2, k, (long)C.strlen("NOTE")), "NOTE")
              || !strcmp(mid(str2, k, (long)C.strlen("OBSERVATION")), "OBSERVATION")
              || !strcmp(mid(str2, k, (long)C.strlen("PART")), "PART")
              || !strcmp(mid(str2, k, (long)C.strlen("POSTULATE")), "POSTULATE")
              || !strcmp(mid(str2, k, (long)C.strlen("PROBLEM")), "PROBLEM")
              || !strcmp(mid(str2, k, (long)C.strlen("PROPERTY")), "PROPERTY")
              || !strcmp(mid(str2, k, (long)C.strlen("PROPOSITION")), "PROPOSITION")
              || !strcmp(mid(str2, k, (long)C.strlen("REMARK")), "REMARK")
              || !strcmp(mid(str2, k, (long)C.strlen("RESULT")), "RESULT")
              || !strcmp(mid(str2, k, (long)C.strlen("RULE")), "RULE")
              || !strcmp(mid(str2, k, (long)C.strlen("SCHEME")), "SCHEME")
              || !strcmp(mid(str2, k, (long)C.strlen("SECTION")), "SECTION")
              || !strcmp(mid(str2, k, (long)C.strlen("PROOF")), "PROOF")
              || !strcmp(mid(str2, k, (long)C.strlen("STATEMENT")), "STATEMENT")
              || !strcmp(mid(str2, k, (long)C.strlen("CONCLUSION")), "CONCLUSION")
              || !strcmp(mid(str2, k, (long)C.strlen("FACT")), "FACT")
              || !strcmp(mid(str2, k, (long)C.strlen("INTRODUCTION")), "INTRODUCTION")
              || !strcmp(mid(str2, k, (long)C.strlen("PARAGRAPH")), "PARAGRAPH")
              || !strcmp(mid(str2, k, (long)C.strlen("SCOLIA")), "SCOLIA")
              || !strcmp(mid(str2, k, (long)C.strlen("SCOLION")), "SCOLION")
              || !strcmp(mid(str2, k, (long)C.strlen("SUBSECTION")), "SUBSECTION")
              || !strcmp(mid(str2, k, (long)C.strlen("TABLE")), "TABLE")
              ) {
            m = k;
            break;
          }
          let(&str3, "");
        }
        if (!m) {
          if (p2 == 1) {
            mminou.print2(
             "?Warning: Bibliography keyword missing in comment for \"%s\".\n",
                g_Statement[i].labelName);
            mminou.print2(
                "    (See HELP WRITE BIBLIOGRAPHY for list of keywords.)\n");
            warnFlag = 1;
          }
          continue;
        }

        p = instr(m, str1, "[");
        q = instr(p, str1, "]");
        if (q == 0) {
          if (p2 == 1) {
            mminou.print2(
        "?Warning: Bibliography reference not found in HTML file in \"%s\".\n",
              g_Statement[i].labelName);
            warnFlag = 1;
          }
          continue;
        }
        s = instr(q, str1, "###");
        if (!s) {
          if (p2 == 1) {
            mminou.print2(
          "?Warning: No page number after [<author>] bib ref in \"%s\".\n",
              g_Statement[i].labelName);
            warnFlag = 1;
          }
          continue;
        }

        lines++;
        if (p2 == 1) continue;

        let(&str2, seg(str1, m, p - 1));
        let(&str3, seg(str1, p + 1, q - 1));
        let(&str4, seg(str1, q + 1, s - 1));
        str2[0] = (char)(toupper((unsigned char)(str2[0])));

        for (k = (long)C.strlen(str2); k >=1; k--) {
          if (0
              || !strcmp(mid(str2, k, (long)C.strlen(" of ")), " of ")
              || !strcmp(mid(str2, k, (long)C.strlen(" in ")), " in ")
              || !strcmp(mid(str2, k, (long)C.strlen(" from ")), " from ")
              || !strcmp(mid(str2, k, (long)C.strlen(" on ")), " on ")
              ) {
            let(&str2, left(str2, k - 1));
            break;
          }
          let(&str2, str2);
        }

        let(&newstr, "");
        newstr = pinkHTML(i);
        let(&oldstr, cat(


            str3, " ", str4, space(20 - (long)C.strlen(str2)), str2,
            "|||",

            "<A HREF=\"", g_Statement[i].labelName,
            ".html\">", g_Statement[i].labelName, "</A>",
            newstr,
            "&&&",



            (i < g_extHtmlStmt)
               ? "<TR>"
               : (i < g_mathboxStmt)
                   ? cat("<TR BGCOLOR=", PURPLISH_BIBLIO_COLOR, ">", null)
                   : cat("<TR BGCOLOR=", SANDBOX_COLOR, ">", null),

            "<TD NOWRAP>[<A HREF=\"",



            (i < g_extHtmlStmt)
               ? g_htmlBibliography
               : (i < g_mathboxStmt)
                   ? extHtmlBibliography
                   : g_htmlBibliography,

            "#",
            str3,
            "\">", str3, "</A>]", str4,
            "</TD><TD>", str2, "</TD><TD><A HREF=\"",
            g_Statement[i].labelName,
            ".html\">", g_Statement[i].labelName, "</A>",
            newstr, null));

        let((vstring *)(&pntrTmp[lines - 1]), oldstr);
      }
    }


    if (p2 == 1) {
      pass1refs = lines;
    } else {
      if (pass1refs != lines) bug(2339);
    }

    if (errorsOnly == 0 && p2 == 2) {
      mminou.print2("%ld references were processed.\n", lines);
    }
    if (p2 == 2) break;
    p2++;
  }


  g_qsortKey = "";
  qsort(pntrTmp, (size_t)lines, sizeof(void *), qsortStringCmp);


  let(&str1, "");
  for (i = 0; i < lines; i++) {
    j = instr(1, (vstring)(pntrTmp[i]), "|||");
    let(&str2, left((vstring)(pntrTmp[i]), j - 1));
    if (!strcmp(str1, str2)) {


      k = instr(j, (vstring)(pntrTmp[i]), "&&&");

      let(&str3, seg((vstring)(pntrTmp[i]), j + 3, k -1));
      let((vstring *)(&pntrTmp[i]),
          cat((vstring)(pntrTmp[i - 1]), " &nbsp;", str3, null));
      let((vstring *)(&pntrTmp[i - 1]), "");
    }
    let(&str1, str2);
  }


  if (noFileCheck == 0 && errorsOnly == 0) {
    n = 0;
    for (i = 0; i < lines; i++) {
      j = instr(1, (vstring)(pntrTmp[i]), "&&&");
      if (j) {
        n++;

        let(&str1, edit(right((vstring)(pntrTmp[i]), j + 3), 16));
        j = 1;

        let(&g_printString, "");
        g_outputToString = 1;
        printLongLine(cat(str1, "</TD></TR>", null),
            " ",
            "\"");
        g_outputToString = 0;
        fprintf(list2_fp, "%s", g_printString);
        let(&g_printString, "");
      }
    }
  }



  if (noFileCheck == 0) {
    while (true) {
      if (!linput(list1_fp, null, &str1)) {
        mminou.print2(
  "?Error: Could not find \"<!-- #END# -->\" line in input file \"%s\".\n",
            bibFile);
        errFlag = 2;
        break;
      }
      if (!strcmp(str1, "<!-- #END# -->")) {
        if (errorsOnly == 0) {
          fprintf(list2_fp, "%s\n", str1);
        }
        break;
      }
    }
    if (errFlag) C.go2("BIB_ERROR");
  }

  if (noFileCheck == 0 && errorsOnly == 0) {

    while (true) {
      if (!linput(list1_fp, null, &str1)) {
        break;
      }



      if (!strcmp("This page was last updated on ", left(str1, 30))) {
        let(&str1, cat(left(str1, 30), date(), ".", null));
      }

      fprintf(list2_fp, "%s\n", str1);
    }

    mminou.print2("%ld table rows were written.\n", n);

    for (i = 0; i < lines; i++) let((vstring *)(&pntrTmp[i]), "");
    pntrLet(&pntrTmp,NULL_PNTRSTRING);
  }


  C.label("BIB_ERROR");
  if (noFileCheck == 0) {
    C.fclose(list1_fp);
    if (errorsOnly == 0) {
      C.fclose(list2_fp);
    }
    if (errorsOnly == 0) {
      if (errFlag) {

        remove(bibFile);
        rename(cat(bibFile, "~1", null), g_fullArg[2]);

        mminou.print2("?The file \"%s\" was not modified.\n", g_fullArg[2]);
      }
    }
  }
  if (errFlag == 2) warnFlag = 2;
  return warnFlag;
}



flag inDiffMathboxes(long stmt1, long stmt2) {
  long mbox1, mbox2;
  mbox1 = getMathboxNum(stmt1);
  mbox2 = getMathboxNum(stmt2);
  if (mbox1 == 0 || mbox2 == 0) return 0;
  if (mbox1 != mbox2) return 1;
  return 0;
}


vstring getMathboxUser(long stmt) {
  long mbox;
  mbox = getMathboxNum(stmt);
  if (mbox == 0) return "";
  return g_mathboxUser[mbox - 1];
}


long getMathboxNum(long stmt) {
  long mbox;
  assignMathboxInfo();
  for (mbox = 0; mbox < g_mathboxes; mbox++) {
    if (stmt < g_mathboxStart[mbox]) break;
  }
  return mbox;
}




  static final String MB_LABEL =D.MB_LABEL;
void assignMathboxInfo() {
  if (g_mathboxStmt == 0) {
    g_mathboxStmt = lookupLabel(MB_LABEL);
    if (g_mathboxStmt == -1) {
      g_mathboxStmt = g_statements + 1;
      g_mathboxes = 0;
    } else {

      g_mathboxes = getMathboxLoc(&g_mathboxStart, &g_mathboxEnd,
          &g_mathboxUser);
    }
  }
  return;
}



  static final String MB_TAG=D.MB_TAG;
long getMathboxLoc(nmbrString **mathboxStart, nmbrString **mathboxEnd,
    pntrString **mathboxUser) {
  long m, p, q, tagLen, stmt;
  long mathboxes = 0;
  vstring comment = "";
  vstring user = "";
  assignMathboxInfo();
  tagLen = (long)C.strlen(MB_TAG);

  if (pntrLen((pntrString *)(*mathboxUser)) != 0) bug(2347);
  if (nmbrLen((nmbrString *)(*mathboxStart)) != 0) bug(2348);
  if (nmbrLen((nmbrString *)(*mathboxEnd)) != 0) bug(2349);
  for (stmt = g_mathboxStmt + 1; stmt <= g_statements; stmt++) {

    let(&comment, left(g_Statement[stmt].labelSectionPtr,
        g_Statement[stmt].labelSectionLen));
    p = 0;
    while (true) {
      q = instr(p + 1, comment, MB_TAG);
      if (q == 0) break;
      p = q;
    }
    if (p == 0) continue;


    mathboxes++;
    q = instr(p, comment, "\n");
    if (q == 0) bug(2350);
    let(&user, seg(comment, p + tagLen, q - 1));
    pntrLet(&(*mathboxUser), pntrAddElement(*mathboxUser));
    (*mathboxUser)[mathboxes - 1] = "";
    let((vstring *)(&((*mathboxUser)[mathboxes - 1])), user);
    nmbrLet(&(*mathboxStart), nmbrAddElement(*mathboxStart, stmt));
  }
  if (mathboxes == 0) C.go2("RETURN_POINT");

  nmbrLet(&(*mathboxEnd), nmbrSpace(mathboxes));
  for (m = 0; m < mathboxes - 1; m++) {
    (*mathboxEnd)[m] = (*mathboxStart)[m + 1] - 1;
  }
  (*mathboxEnd)[mathboxes - 1] = g_statements;
  C.label("RETURN_POINT");
  let(&comment, "");
  let(&user, "");
  return mathboxes;
}
}