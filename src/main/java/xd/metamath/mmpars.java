package xd.metamath;

public class mmpars {









long potentialStatements;
flag illegalLabelChar[256];
long *g_labelKeyBase;
long g_numLabelKeys;

long *g_allLabelKeyBase;
long g_numAllLabelKeys;




long g_wrkProofMaxSize = 0;
long wrkMathPoolMaxSize = 0;
struct wrkProof_struct g_WrkProof;



char *readRawSource(
         vstring fileBuf,

         long *size )
{
  long charCount = 0;

  char *fbPtr;

  flag insideComment;

  char mode;
  char tmpch;


  charCount = *size;



  fbPtr = fileBuf;

  mode = 0;
  insideComment = 0;

  while (true) {


    tmpch = fbPtr[0];
    if (!tmpch) {
      if (insideComment) {
        rawSourceError(fileBuf, fbPtr - 1, 2,
         "The last comment in the file is incomplete.  \"$)\" was expected.");
      } else {
        if (mode != 0) {
          rawSourceError(fileBuf, fbPtr - 1, 2,
   "The last include statement in the file is incomplete.  \"$]\" was expected."
           );
        }
      }
      break;
    }
    if (tmpch != '$') {
      if (tmpch == '\n') {


      } else {

          if (!isgraph((unsigned char)tmpch) && !isspace((unsigned char)tmpch)) {
            rawSourceError(fileBuf, fbPtr, 1,
                cat("Illegal character (ASCII code ",
                str((double)((unsigned char)tmpch)),
                " decimal).",null));

        }
      }
      fbPtr++;
      continue;
    }


    if (fbPtr > fileBuf) {
      if (isgraph((unsigned char)(fbPtr[-1]))) {

        if (!insideComment || fbPtr[1] == ')') {

          rawSourceError(fileBuf, fbPtr, 2,
              "A keyword must be preceded by white space.");
        }
      }
    }
    fbPtr++;
    if (fbPtr[0]) {
      if (isgraph((unsigned char)(fbPtr[1]))) {
        if (!insideComment || fbPtr[0] == ')') {

          rawSourceError(fileBuf, fbPtr + 1, 1,
              "A keyword must be followed by white space.");
          }
      }
    }


    switch (fbPtr[0]) {
      case '(':
        if (insideComment) {
          rawSourceError(fileBuf, fbPtr - 1, 2,
          "Nested comments are not allowed.");
        }
        insideComment = 1;
        continue;
      case ')':
        if (!insideComment) {
          rawSourceError(fileBuf, fbPtr - 1, 2,
              "A comment terminator was found outside of a comment.");
        }
        insideComment = 0;
        continue;

    }
    if (insideComment) continue;
    switch (fbPtr[0]) {
      case '[':
        if (mode != 0) {
          rawSourceError(fileBuf, fbPtr - 1, 2,
              "Nested include statements are not allowed.");
        } else {


          rawSourceError(fileBuf, fbPtr - 1, 2,
              "\"$[\" is unterminated or has ill-formed \"$]\".");

        }
        continue;
      case ']':
        if (mode == 0) {
          rawSourceError(fileBuf, fbPtr - 1, 2,
              "A \"$[\" is required before \"$]\".");
          continue;
        }



        bug(1759);

        continue;

      case '{':
      case '}':
      case '.':
        potentialStatements++;
        break;
    }
  }

  if (fbPtr != fileBuf + charCount) {

    printf("fbPtr=%ld fileBuf=%ld charCount=%ld diff=%ld\n",
        (long)fbPtr, (long)fileBuf, charCount, fbPtr - fileBuf - charCount);
    bug(1704);
  }


  mminou.print2("%ld bytes were read into the source buffer.\n", charCount);

  if (*size != charCount) bug(1761);
  return (fileBuf);

}

void parseKeywords()
{
  long i, j, k;
  char *fbPtr;
  flag insideComment;
  char mode, type;
  char *startSection;
  char tmpch;
  long dollarPCount = 0;
  long dollarACount = 0;


  type = 0;

  potentialStatements = potentialStatements + 3;
if(db5)mminou.print2("There are up to %ld potential statements.\n",
   potentialStatements);


  g_Statement = realloc(g_Statement, (size_t)potentialStatements
      * sizeof(struct statement_struct));
  if (!g_Statement) outOfMemory("#4 (statement)");


  i = 0;
  g_Statement[i].lineNum = 0;
  g_Statement[i].fileName = "";
  g_Statement[i].labelName = "";
  g_Statement[i].uniqueLabel = 0;
  g_Statement[i].type = illegal_;
  g_Statement[i].scope = 0;
  g_Statement[i].beginScopeStatementNum = 0;
  g_Statement[i].endScopeStatementNum = 0;
  g_Statement[i].labelSectionPtr = "";
  g_Statement[i].labelSectionLen = 0;
  g_Statement[i].labelSectionChanged = 0;
  g_Statement[i].statementPtr = "";
  g_Statement[i].mathSectionPtr = "";
  g_Statement[i].mathSectionLen = 0;
  g_Statement[i].mathSectionChanged = 0;
  g_Statement[i].proofSectionPtr = "";
  g_Statement[i].proofSectionLen = 0;
  g_Statement[i].proofSectionChanged = 0;
  g_Statement[i].mathString = NULL_NMBRSTRING;
  g_Statement[i].mathStringLen = 0;
  g_Statement[i].proofString = NULL_NMBRSTRING;
  g_Statement[i].reqHypList = NULL_NMBRSTRING;
  g_Statement[i].optHypList = NULL_NMBRSTRING;
  g_Statement[i].numReqHyp = 0;
  g_Statement[i].reqVarList = NULL_NMBRSTRING;
  g_Statement[i].optVarList = NULL_NMBRSTRING;
  g_Statement[i].reqDisjVarsA = NULL_NMBRSTRING;
  g_Statement[i].reqDisjVarsB = NULL_NMBRSTRING;
  g_Statement[i].reqDisjVarsStmt = NULL_NMBRSTRING;
  g_Statement[i].optDisjVarsA = NULL_NMBRSTRING;
  g_Statement[i].optDisjVarsB = NULL_NMBRSTRING;
  g_Statement[i].optDisjVarsStmt = NULL_NMBRSTRING;
  g_Statement[i].pinkNumber = 0;
  g_Statement[i].headerStartStmt = 0;
  for (i = 1; i < potentialStatements; i++) {
    g_Statement[i] = g_Statement[0];
  }


  g_Statement[0].labelName = "(N/A)";

if(db5)mminou.print2("Finished initializing statement array.\n");


  fbPtr = g_sourcePtr;
  mode = 0;
  insideComment = 0;
  startSection = fbPtr;

  while (true) {


    tmpch = fbPtr[0];
    if (!tmpch) {
      if (mode != 0) {
        sourceError(fbPtr - 1, 2, g_statements,
            "Expected \"$.\" here (last line of file).");
        if (g_statements) {
          startSection = g_Statement[g_statements].labelSectionPtr;
          g_statements--;
        }
      }
      break;
    }

    if (tmpch != '$') {


      fbPtr++;
      continue;
    }
    fbPtr++;
    switch (fbPtr[0]) {
      case '$':
        fbPtr++;
        continue;
      case '(':



        insideComment = 1;
        continue;
      case ')':


        insideComment = 0;
        continue;
      case '!':
        if (insideComment) continue;
    }
    if (insideComment) continue;
    switch (fbPtr[0]) {
      case 'c':  type = c_; break;
      case 'v':  type = v_; break;
      case 'e':  type = e_; break;
      case 'f':  type = f_; break;
      case 'd':  type = d_; break;
      case 'a':  type = a_; dollarACount++; break;
      case 'p':  type = p_; dollarPCount++; break;
      case '{':  type = lb_; break;
      case '}':  type = rb_; break;
    }
    switch (fbPtr[0]) {
      case 'c':
      case 'v':
      case 'e':
      case 'f':
      case 'd':
      case 'a':
      case 'p':
      case '{':
      case '}':
        if (mode != 0) {
          if (mode == 2 || type != p_) {
            sourceError(fbPtr - 1, 2, g_statements,
                "Expected \"$.\" here.");
          } else {
            sourceError(fbPtr - 1, 2, g_statements,
                "Expected \"$=\" here.");
          }
          continue;
        }

        g_statements++;
        g_Statement[g_statements].type = type;
        g_Statement[g_statements].labelSectionPtr = startSection;
        g_Statement[g_statements].labelSectionLen = fbPtr - startSection - 1;
        g_Statement[g_statements].statementPtr = startSection
            + g_Statement[g_statements].labelSectionLen;
        startSection = fbPtr + 1;
        if (type != lb_ && type != rb_) mode = 1;
        continue;
      default:
        if (mode == 0) {
          sourceError(fbPtr - 1, 2, g_statements, cat(
              "Expected \"$c\", \"$v\", \"$e\", \"$f\", \"$d\",",
              " \"$a\", \"$p\", \"${\", or \"$}\" here.",null));
          continue;
        }
        if (mode == 1) {
          if (type == p_ && fbPtr[0] != '=') {
            sourceError(fbPtr - 1, 2, g_statements,
                "Expected \"$=\" here.");
            if (fbPtr[0] == '.') {
              mode = 2;
            }
          }
          if (type != p_ && fbPtr[0] != '.') {
            sourceError(fbPtr - 1, 2, g_statements,
                "Expected \"$.\" here.");
            continue;
          }

          g_Statement[g_statements].mathSectionPtr = startSection;
          g_Statement[g_statements].mathSectionLen = fbPtr - startSection - 1;
          startSection = fbPtr + 1;
          if (type == p_ && mode != 2 ) {
            mode = 2;
          } else {
            mode = 0;
          }
          continue;
        }
        if (mode == 2) {
          if (fbPtr[0] != '.') {
            sourceError(fbPtr - 1, 2, g_statements,
                "Expected \"$.\" here.");
            continue;
          }

          g_Statement[g_statements].proofSectionPtr = startSection;
          g_Statement[g_statements].proofSectionLen = fbPtr - startSection - 1;
          startSection = fbPtr + 1;
          mode = 0;
          continue;
        }
    }
  }

  if (fbPtr != g_sourcePtr + g_sourceLen) bug(1706);

  mminou.print2("The source has %ld statements; %ld are $a and %ld are $p.\n",
       g_statements, dollarACount, dollarPCount);





  g_Statement[g_statements + 1].type = illegal_;
  g_Statement[g_statements + 1].labelSectionPtr = startSection;
  g_Statement[g_statements + 1].labelSectionLen = fbPtr - startSection;

  g_Statement[g_statements + 1].statementPtr = fbPtr - 1;

  j = 0;
  k = 0;
  for (i = 1; i <= g_statements; i++) {
    if (g_Statement[i].type == a_ || g_Statement[i].type == p_) {


      g_Statement[i].headerStartStmt = k + 1;
      k = i;

      j++;
      g_Statement[i].pinkNumber = j;
    }
  }
  g_Statement[g_statements].pinkNumber = j;


if(db5){for (i=1; i<=g_statements; i++){
  if (i == 5) { mminou.print2("(etc.)\n");} else { if (i<5) {
  assignStmtFileAndLineNum(i);
    mminou.print2("Statement %ld: line %ld file %s.\n",i,g_Statement[i].lineNum,
      g_Statement[i].fileName);
}}}}

}

void parseLabels()
{
  long i, j, k;
  char *fbPtr;
  char type;
  long stmt;
  flag dupFlag;


  for (i = 0; i < 256; i++) {
    illegalLabelChar[i] = !isalnum(i);
  }
  illegalLabelChar['-'] = 0;
  illegalLabelChar['_'] = 0;
  illegalLabelChar['.'] = 0;



  for (stmt = 1; stmt <= g_statements; stmt++) {
    type = g_Statement[stmt].type;
    fbPtr = g_Statement[stmt].labelSectionPtr;
    fbPtr = fbPtr + whiteSpaceLen(fbPtr);
    j = tokenLen(fbPtr);
    if (j) {
      for (k = 0; k < j; k++) {
        if (illegalLabelChar[(unsigned char)fbPtr[k]]) {
          sourceError(fbPtr + k, 1, stmt,
        "Only letters, digits, \"_\", \"-\", and \".\" are allowed in labels.");
          break;
        }
      }
      switch (type) {
        case d_:
        case rb_:
        case lb_:
        case v_:
        case c_:
          sourceError(fbPtr, j, stmt,
                "A label isn't allowed for this statement type.");
      }
      g_Statement[stmt].labelName = malloc((size_t)j + 1);
      if (!g_Statement[stmt].labelName) outOfMemory("#5 (label)");
      g_Statement[stmt].labelName[j] = 0;
      memcpy(g_Statement[stmt].labelName, fbPtr, (size_t)j);
      fbPtr = fbPtr + j;
      fbPtr = fbPtr + whiteSpaceLen(fbPtr);
      j = tokenLen(fbPtr);
      if (j) {
        sourceError(fbPtr, j, stmt,
            "A statement may have only one label.");
      }
    } else {
      switch (type) {
        case e_:
        case f_:
        case a_:
        case p_:
          sourceError(fbPtr, 2, stmt,
                "A label is required for this statement type.");
      }
    }
  }


  fbPtr = g_Statement[g_statements + 1].labelSectionPtr;
  i = whiteSpaceLen(fbPtr);
  j = tokenLen(fbPtr + i);
  if (j) {
    sourceError(fbPtr + i, j, 0,
        "There should be no tokens after the last statement.");
  }


  g_labelKey = malloc(((size_t)g_statements + 1) * sizeof(long));
  if (!g_labelKey) outOfMemory("#6 (g_labelKey)");
  for (i = 1; i <= g_statements; i++) {
    g_labelKey[i] = i;
  }
  g_labelKeyBase = &g_labelKey[1];
  g_numLabelKeys = g_statements;
  qsort(g_labelKeyBase, (size_t)g_numLabelKeys, sizeof(long), labelSortCmp);


  for (i = 1; i <= g_statements; i++) {
    if (g_Statement[g_labelKey[i]].labelName[0]) break;
  }
  g_labelKeyBase = &g_labelKey[i];
  g_numLabelKeys = g_statements - i + 1;
if(db5)mminou.print2("There are %ld non-empty labels.\n", g_numLabelKeys);
if(db5){mminou.print2("The first (up to 5) sorted labels are:\n");
  for (i=0; i<5; i++) {
    if (i >= g_numLabelKeys) break;
    mminou.print2("%s ",g_Statement[g_labelKeyBase[i]].labelName);
  } mminou.print2("\n");}



  g_allLabelKeyBase = malloc((size_t)g_numLabelKeys * sizeof(long));
  if (!g_allLabelKeyBase) outOfMemory("#60 (g_allLabelKeyBase)");
  memcpy(g_allLabelKeyBase, g_labelKeyBase, (size_t)g_numLabelKeys * sizeof(long));
  g_numAllLabelKeys = g_numLabelKeys;




  dupFlag = 0;
  for (i = 0; i < g_numLabelKeys; i++) {
    if (dupFlag) {
      dupFlag = 0;
      if (!strcmp(g_Statement[g_labelKeyBase[i]].labelName,
          g_Statement[g_labelKeyBase[i - 1]].labelName)) dupFlag = 1;
    }
    if (i < g_numLabelKeys - 1) {
      if (!strcmp(g_Statement[g_labelKeyBase[i]].labelName,
          g_Statement[g_labelKeyBase[i + 1]].labelName)) dupFlag = 1;
    }
    if (dupFlag) {
      fbPtr = g_Statement[g_labelKeyBase[i]].labelSectionPtr;
      k = whiteSpaceLen(fbPtr);
      j = tokenLen(fbPtr + k);
      sourceError(fbPtr + k, j, g_labelKeyBase[i],
         "This label is declared more than once.  All labels must be unique.");
    }
  }

}

void parseMathDecl()
{
  long potentialSymbols;
  long stmt;
  char *fbPtr;
  long i, j, k;
  char *tmpPtr;
  nmbrString *nmbrTmpPtr;
  long oldG_mathTokens;
  void *voidPtr;

  potentialSymbols = 0;
  for (stmt = 1; stmt <= g_statements; stmt++) {
    switch (g_Statement[stmt].type) {
      case c_:
      case v_:
        potentialSymbols = potentialSymbols + g_Statement[stmt].mathSectionLen;
    }
  }
  potentialSymbols = (potentialSymbols / 2) + 2;
if(db5)mminou.print2("%ld potential symbols were computed.\n",potentialSymbols);
  g_MathToken = realloc(g_MathToken, (size_t)potentialSymbols *
      sizeof(struct mathToken_struct));
  if (!g_MathToken) outOfMemory("#7 (g_MathToken)");


  g_mathTokens = 0;
  for (stmt = 1; stmt <= g_statements; stmt++) {
    switch (g_Statement[stmt].type) {
      case c_:
      case v_:
        oldG_mathTokens = g_mathTokens;
        fbPtr = g_Statement[stmt].mathSectionPtr;
        while (true) {
          i = whiteSpaceLen(fbPtr);
          j = tokenLen(fbPtr + i);
          if (!j) break;
          tmpPtr = malloc((size_t)j + 1);
          if (!tmpPtr) outOfMemory("#8 (symbol name)");
          tmpPtr[j] = 0;
          memcpy(tmpPtr, fbPtr + i, (size_t)j);
          fbPtr = fbPtr + i + j;

          g_MathToken[g_mathTokens].tokenName = tmpPtr;
          g_MathToken[g_mathTokens].length = j;
          if (g_Statement[stmt].type == c_) {
            g_MathToken[g_mathTokens].tokenType = (char)con_;
          } else {
            g_MathToken[g_mathTokens].tokenType = (char)var_;
          }
          g_MathToken[g_mathTokens].active = 0;
          g_MathToken[g_mathTokens].scope = 0;
          g_MathToken[g_mathTokens].tmp = 0;
          g_MathToken[g_mathTokens].statement = stmt;
          g_MathToken[g_mathTokens].endStatement = g_statements;

          g_mathTokens++;

        }


        j = g_mathTokens - oldG_mathTokens;
        nmbrTmpPtr = poolFixedMalloc((j + 1) * (long)(sizeof(nmbrString)));

        nmbrTmpPtr[j] = -1;
        for (i = 0; i < j; i++) {
          nmbrTmpPtr[i] = oldG_mathTokens + i;
        }
        g_Statement[stmt].mathString = nmbrTmpPtr;
        g_Statement[stmt].mathStringLen = j;
        if (!j) {
          sourceError(fbPtr, 2, stmt,
           "At least one math symbol should be declared.");
        }
    }
  }

if(db5)mminou.print2("%ld math symbols were declared.\n",g_mathTokens);

  g_MAX_MATHTOKENS = g_mathTokens + 100;
  g_MathToken = realloc(g_MathToken, (size_t)g_MAX_MATHTOKENS *
      sizeof(struct mathToken_struct));
  if (!g_MathToken) outOfMemory("#10 (g_MathToken)");


  g_MathToken[g_mathTokens].tokenName = "";
  let(&g_MathToken[g_mathTokens].tokenName, "$|$");
  g_MathToken[g_mathTokens].length = 2;
  g_MathToken[g_mathTokens].tokenType = (char)con_;
  g_MathToken[g_mathTokens].active = 0;
  g_MathToken[g_mathTokens].scope = 0;
  g_MathToken[g_mathTokens].tmp = 0;
  g_MathToken[g_mathTokens].statement = 0;
  g_MathToken[g_mathTokens].endStatement = g_statements;



  g_mathKey = malloc((size_t)g_mathTokens * sizeof(long));
  if (!g_mathKey) outOfMemory("#11 (g_mathKey)");
  for (i = 0; i < g_mathTokens; i++) {
    g_mathKey[i] = i;
  }
  qsort(g_mathKey, (size_t)g_mathTokens, sizeof(long), mathSortCmp);
if(db5){mminou.print2("The first (up to 5) sorted math tokens are:\n");
  for (i=0; i<5; i++) {
    if (i >= g_mathTokens) break;
    mminou.print2("%s ",g_MathToken[g_mathKey[i]].tokenName);
  } mminou.print2("\n");}



  for (i = 0; i < g_mathTokens; i++) {

    voidPtr = (void *)bsearch(g_MathToken[i].tokenName, g_labelKeyBase,
        (size_t)g_numLabelKeys, sizeof(long), labelSrchCmp);
    if (voidPtr) {
      stmt = (*(long *)voidPtr);
      fbPtr = g_Statement[stmt].labelSectionPtr;
      k = whiteSpaceLen(fbPtr);
      j = tokenLen(fbPtr + k);
      assignStmtFileAndLineNum(stmt);
      assignStmtFileAndLineNum(g_MathToken[i].statement);
      sourceError(fbPtr + k, j, stmt, cat(
         "This label has the same name as the math token declared on line ",
         str((double)(g_Statement[g_MathToken[i].statement].lineNum)),
         " of file \"",
         g_Statement[g_MathToken[i].statement].fileName,
         "\".", null));
    }
  }



}



void parseStatements()
{
  long stmt;
  char type;
  long i, j, k, m, n, p;
  char *fbPtr;
  long mathStringLen;
  long tokenNum;
  long lowerKey, upperKey;
  long symbolLen, origSymbolLen, mathSectionLen, g_mathKeyNum;
  void *g_mathKeyPtr;
  int maxScope;
  long reqHyps, optHyps, reqVars, optVars;
  flag reqFlag;
  int undeclErrorCount = 0;
  vstring tmpStr = "";

  nmbrString *nmbrTmpPtr;

  long *mathTokenSameAs;
  long *reverseMathKey;

  long *labelTokenSameAs;
  long *reverseLabelKey;
  flag *labelActiveFlag;

  struct activeConstStack_struct {
    long tokenNum;
    int scope;
  };
  struct activeConstStack_struct *activeConstStack;
  long activeConstStackPtr = 0;

  struct activeVarStack_struct {
    long tokenNum;
    int scope;
    char tmpFlag;
  };
  struct activeVarStack_struct *activeVarStack;
  nmbrString *wrkVarPtr1;
  nmbrString *wrkVarPtr2;
  long activeVarStackPtr = 0;

  struct activeEHypStack_struct {
    long statemNum;
    nmbrString *varList;
    int scope;
  };
  struct activeEHypStack_struct *activeEHypStack;
  long activeEHypStackPtr = 0;
  struct activeFHypStack_struct {
    long statemNum;
    nmbrString *varList;
    int scope;
  };
  struct activeFHypStack_struct *activeFHypStack;
  long activeFHypStackPtr = 0;
  nmbrString *wrkHypPtr1;
  nmbrString *wrkHypPtr2;
  nmbrString *wrkHypPtr3;
  long activeHypStackSize = 30;


  struct activeDisjHypStack_struct {
    long tokenNumA;
    long tokenNumB;
    long statemNum;
    int scope;
  };
  struct activeDisjHypStack_struct *activeDisjHypStack;
  nmbrString *wrkDisjHPtr1A;
  nmbrString *wrkDisjHPtr1B;
  nmbrString *wrkDisjHPtr1Stmt;
  nmbrString *wrkDisjHPtr2A;
  nmbrString *wrkDisjHPtr2B;
  nmbrString *wrkDisjHPtr2Stmt;
  long activeDisjHypStackPtr = 0;
  long activeDisjHypStackSize = 30;


  long wrkLen;
  nmbrString *wrkNmbrPtr;
  char *wrkStrPtr;

  long maxSymbolLen;
  flag *symbolLenExists;

  long beginScopeStmtNum = 0;

  mathStringLen = 0;
  tokenNum = 0;

  mathTokenSameAs = malloc((size_t)g_mathTokens * sizeof(long));
  if (!mathTokenSameAs) outOfMemory("#12 (mathTokenSameAs)");
  reverseMathKey = malloc((size_t)g_mathTokens * sizeof(long));
  if (!reverseMathKey) outOfMemory("#13 (reverseMathKey)");
  for (i = 0; i < g_mathTokens; i++) {
    mathTokenSameAs[i] = 0;
    reverseMathKey[g_mathKey[i]] = i;
  }
  for (i = 1; i < g_mathTokens; i++) {
    if (!strcmp(g_MathToken[g_mathKey[i]].tokenName,
        g_MathToken[g_mathKey[i - 1]].tokenName)) {
      if (!mathTokenSameAs[i - 1]) mathTokenSameAs[i - 1] = i;
      mathTokenSameAs[i] = mathTokenSameAs[i - 1];
    }
  }

  labelTokenSameAs = malloc(((size_t)g_statements + 1) * sizeof(long));
  if (!labelTokenSameAs) outOfMemory("#112 (labelTokenSameAs)");
  reverseLabelKey = malloc(((size_t)g_statements + 1) * sizeof(long));
  if (!reverseLabelKey) outOfMemory("#113 (reverseLabelKey)");
  labelActiveFlag = malloc(((size_t)g_statements + 1) * sizeof(flag));
  if (!labelActiveFlag) outOfMemory("#114 (labelActiveFlag)");
  for (i = 1; i <= g_statements; i++) {
    labelTokenSameAs[i] = 0;
    reverseLabelKey[g_labelKey[i]] = i;
    labelActiveFlag[i] = 0;
  }
  for (i = 2; i <= g_statements; i++) {
    if (!strcmp(g_Statement[g_labelKey[i]].labelName,
        g_Statement[g_labelKey[i - 1]].labelName)) {
      if (!labelTokenSameAs[i - 1]) labelTokenSameAs[i - 1] = i;
      labelTokenSameAs[i] = labelTokenSameAs[i - 1];
    }
  }



  activeConstStack = malloc((size_t)g_MAX_MATHTOKENS
      * sizeof(struct activeConstStack_struct));
  activeVarStack = malloc((size_t)g_MAX_MATHTOKENS
      * sizeof(struct activeVarStack_struct));
  wrkVarPtr1 = malloc((size_t)g_MAX_MATHTOKENS * sizeof(nmbrString));
  wrkVarPtr2 = malloc((size_t)g_MAX_MATHTOKENS * sizeof(nmbrString));
  if (!activeConstStack || !activeVarStack || !wrkVarPtr1 || !wrkVarPtr2)
      outOfMemory("#14 (activeVarStack)");

  activeEHypStack = malloc((size_t)activeHypStackSize
      * sizeof(struct activeEHypStack_struct));
  activeFHypStack = malloc((size_t)activeHypStackSize
      * sizeof(struct activeFHypStack_struct));
  wrkHypPtr1 = malloc((size_t)activeHypStackSize * sizeof(nmbrString));
  wrkHypPtr2 = malloc((size_t)activeHypStackSize * sizeof(nmbrString));
  wrkHypPtr3 = malloc((size_t)activeHypStackSize * sizeof(nmbrString));
  if (!activeEHypStack || !activeFHypStack || !wrkHypPtr1 || !wrkHypPtr2 ||
      !wrkHypPtr3)
      outOfMemory("#15 (activeHypStack)");

  activeDisjHypStack = malloc((size_t)activeDisjHypStackSize *
      sizeof(struct activeDisjHypStack_struct));
  wrkDisjHPtr1A = malloc((size_t)activeDisjHypStackSize * sizeof(nmbrString));
  wrkDisjHPtr1B = malloc((size_t)activeDisjHypStackSize * sizeof(nmbrString));
  wrkDisjHPtr1Stmt = malloc((size_t)activeDisjHypStackSize
      * sizeof(nmbrString));
  wrkDisjHPtr2A = malloc((size_t)activeDisjHypStackSize * sizeof(nmbrString));
  wrkDisjHPtr2B = malloc((size_t)activeDisjHypStackSize * sizeof(nmbrString));
  wrkDisjHPtr2Stmt = malloc((size_t)activeDisjHypStackSize
      * sizeof(nmbrString));
  if (!activeDisjHypStack
      || !wrkDisjHPtr1A || !wrkDisjHPtr1B || !wrkDisjHPtr1Stmt
      || !wrkDisjHPtr2A || !wrkDisjHPtr2B || !wrkDisjHPtr2Stmt)
      outOfMemory("#27 (activeDisjHypStack)");


  wrkLen = 1;
  wrkNmbrPtr = malloc((size_t)wrkLen * sizeof(nmbrString));
  if (!wrkNmbrPtr) outOfMemory("#22 (wrkNmbrPtr)");
  wrkStrPtr = malloc((size_t)wrkLen + 1);
  if (!wrkStrPtr) outOfMemory("#23 (wrkStrPtr)");


  maxSymbolLen = 0;
  for (i = 0; i < g_mathTokens; i++) {
    if (g_MathToken[i].length > maxSymbolLen) {
      maxSymbolLen = g_MathToken[i].length;
    }
  }
  symbolLenExists = malloc(((size_t)maxSymbolLen + 1) * sizeof(flag));
  if (!symbolLenExists) outOfMemory("#25 (symbolLenExists)");
  for (i = 0; i <= maxSymbolLen; i++) {
    symbolLenExists[i] = 0;
  }
  for (i = 0; i < g_mathTokens; i++) {
    symbolLenExists[g_MathToken[i].length] = 1;
  }


  g_currentScope = 0;
  beginScopeStmtNum = 0;


  for (stmt = 1; stmt <= g_statements; stmt++) {



    g_Statement[stmt].beginScopeStatementNum = beginScopeStmtNum;

    g_Statement[stmt].endScopeStatementNum = 0;
    g_Statement[stmt].scope = g_currentScope;
    type = g_Statement[stmt].type;


    switch (type) {
      case lb_:
        g_currentScope++;
        if (g_currentScope > 32000) outOfMemory("#16 (more than 32000 \"${\"s)");

        beginScopeStmtNum = stmt;
        break;
      case rb_:


        while (activeConstStackPtr) {
          if (activeConstStack[activeConstStackPtr - 1].scope < g_currentScope)
              break;
          activeConstStackPtr--;
          g_MathToken[activeConstStack[activeConstStackPtr].tokenNum].active = 0;
          g_MathToken[activeConstStack[activeConstStackPtr].tokenNum
              ].endStatement = stmt;
        }

        while (activeVarStackPtr) {
          if (activeVarStack[activeVarStackPtr - 1].scope < g_currentScope) break;
          activeVarStackPtr--;
          g_MathToken[activeVarStack[activeVarStackPtr].tokenNum].active = 0;
          g_MathToken[activeVarStack[activeVarStackPtr].tokenNum].endStatement
              = stmt;
        }

        while (activeEHypStackPtr) {
          if (activeEHypStack[activeEHypStackPtr - 1].scope < g_currentScope)
              break;
          activeEHypStackPtr--;
          labelActiveFlag[activeEHypStack[activeEHypStackPtr].statemNum] = 0;

          free(activeEHypStack[activeEHypStackPtr].varList);
        }
        while (activeFHypStackPtr) {
          if (activeFHypStack[activeFHypStackPtr - 1].scope < g_currentScope)
              break;
          activeFHypStackPtr--;
          labelActiveFlag[activeFHypStack[activeFHypStackPtr].statemNum] = 0;

          free(activeFHypStack[activeFHypStackPtr].varList);
        }
        while (activeDisjHypStackPtr) {
          if (activeDisjHypStack[activeDisjHypStackPtr - 1].scope
              < g_currentScope) break;
          activeDisjHypStackPtr--;
        }
        g_currentScope--;
        if (g_currentScope < 0) {
          sourceError(g_Statement[stmt].labelSectionPtr +
              g_Statement[stmt].labelSectionLen, 2, stmt,
              "Too many \"$}\"s at this point.");
        }


        if (beginScopeStmtNum > 0) {
          if (g_Statement[beginScopeStmtNum].type != lb_) bug(1773);

          g_Statement[beginScopeStmtNum].endScopeStatementNum = stmt;

          beginScopeStmtNum
              = g_Statement[beginScopeStmtNum].beginScopeStatementNum;
        }

        break;
      case c_:
      case v_:

        if (type == c_) {
          if (g_currentScope > 0) {
            sourceError(g_Statement[stmt].labelSectionPtr +
                g_Statement[stmt].labelSectionLen, 2, stmt,
        "A \"$c\" constant declaration may occur in the outermost scope only.");
          }
        }


        i = 0;
        nmbrTmpPtr = g_Statement[stmt].mathString;
        while (true) {
          tokenNum = nmbrTmpPtr[i];
          if (tokenNum == -1) break;
          if (mathTokenSameAs[reverseMathKey[tokenNum]]) {
            lowerKey = reverseMathKey[tokenNum];
            upperKey = lowerKey;
            j = mathTokenSameAs[lowerKey];
            while (lowerKey) {
              if (j != mathTokenSameAs[lowerKey - 1]) break;
              lowerKey--;
            }
            while (upperKey < g_mathTokens - 1) {
              if (j != mathTokenSameAs[upperKey + 1]) break;
              upperKey++;
            }
            for (j = lowerKey; j <= upperKey; j++) {
              if (g_MathToken[g_mathKey[j]].active) {
                if (g_MathToken[g_mathKey[j]].scope <= g_currentScope) {

                  mathTokenError(i, nmbrTmpPtr, stmt,
                      "This symbol has already been declared in this scope.");
                }
              }
            }



            k = 0;
            m = 0;
            for (j = lowerKey; j <= upperKey; j++) {
              if (g_MathToken[g_mathKey[j]].tokenType == (char)con_) k = 1;
              if (g_MathToken[g_mathKey[j]].tokenType == (char)var_) m = 1;
            }
            if ((k == 1 && g_MathToken[tokenNum].tokenType == (char)var_) ||
                (m == 1 && g_MathToken[tokenNum].tokenType == (char)con_)) {
               mathTokenError(i, nmbrTmpPtr, stmt,
                   "A symbol may not be both a constant and a variable.");
            }


          }


          g_MathToken[tokenNum].active = 1;
          g_MathToken[tokenNum].scope = g_currentScope;

          if (type == v_) {

            g_MathToken[tokenNum].tmp = activeVarStackPtr;


            activeVarStack[activeVarStackPtr].tokenNum = tokenNum;
            activeVarStack[activeVarStackPtr].scope = g_currentScope;
            activeVarStack[activeVarStackPtr].tmpFlag = 0;
            activeVarStackPtr++;
          } else {


            activeConstStack[activeConstStackPtr].tokenNum = tokenNum;
            activeConstStack[activeConstStackPtr].scope = g_currentScope;
            activeConstStackPtr++;

          }

          i++;
        }
        break;
      case d_:
      case f_:
      case e_:
      case a_:
      case p_:

        mathSectionLen = g_Statement[stmt].mathSectionLen;
        if (wrkLen < mathSectionLen) {
          free(wrkNmbrPtr);
          free(wrkStrPtr);
          wrkLen = mathSectionLen + 100;
          wrkNmbrPtr = malloc((size_t)wrkLen * sizeof(nmbrString));
          if (!wrkNmbrPtr) outOfMemory("#20 (wrkNmbrPtr)");
          wrkStrPtr = malloc((size_t)wrkLen + 1);
          if (!wrkStrPtr) outOfMemory("#21 (wrkStrPtr)");
        }


        mathStringLen = 0;
        fbPtr = g_Statement[stmt].mathSectionPtr;
        while (true) {
          fbPtr = fbPtr + whiteSpaceLen(fbPtr);
          origSymbolLen = tokenLen(fbPtr);
          if (!origSymbolLen) break;


          C.label("nextAdjToken");



          symbolLen = origSymbolLen;

          memcpy(wrkStrPtr, fbPtr, (size_t)symbolLen);



          for (; symbolLen > 0; symbolLen = 0) {


            if (!symbolLenExists[symbolLen]) continue;
            wrkStrPtr[symbolLen] = 0;
            g_mathKeyPtr = (void *)bsearch(wrkStrPtr, g_mathKey, (size_t)g_mathTokens,
                sizeof(long), mathSrchCmp);
            if (!g_mathKeyPtr) continue;
            g_mathKeyNum = (long *)g_mathKeyPtr - g_mathKey;
            if (mathTokenSameAs[g_mathKeyNum]) {
              lowerKey = g_mathKeyNum;
              upperKey = lowerKey;
              j = mathTokenSameAs[lowerKey];
              while (lowerKey) {
                if (j != mathTokenSameAs[lowerKey - 1]) break;
                lowerKey--;
              }
              while (upperKey < g_mathTokens - 1) {
                if (j != mathTokenSameAs[upperKey + 1]) break;
                upperKey++;
              }

              maxScope = -1;
              for (i = lowerKey; i <= upperKey; i++) {
                j = g_mathKey[i];
                if (g_MathToken[j].active) {
                  if (g_MathToken[j].scope > maxScope) {
                    tokenNum = j;
                    maxScope = g_MathToken[j].scope;
                    if (maxScope == g_currentScope) break;
                  }
                }
              }
              if (maxScope == -1) {
                tokenNum = g_mathKey[g_mathKeyNum];
                sourceError(fbPtr, symbolLen, stmt,
       "This math symbol is not active (i.e. was not declared in this scope).");


                g_MathToken[tokenNum].tmp = 0;
                if (!activeVarStackPtr) {
                  activeVarStack[activeVarStackPtr].tokenNum = tokenNum;
                  activeVarStack[activeVarStackPtr].scope = g_currentScope;
                  activeVarStack[activeVarStackPtr].tmpFlag = 0;
                  activeVarStackPtr++;
                }
              }
            } else {
              tokenNum = *((long *)g_mathKeyPtr);

              if (!g_MathToken[tokenNum].active) {
                sourceError(fbPtr, symbolLen, stmt,
       "This math symbol is not active (i.e. was not declared in this scope).");

                g_MathToken[tokenNum].tmp = 0;
                if (!activeVarStackPtr) {
                  activeVarStack[activeVarStackPtr].tokenNum = tokenNum;
                  activeVarStack[activeVarStackPtr].scope = g_currentScope;
                  activeVarStack[activeVarStackPtr].tmpFlag = 0;
                  activeVarStackPtr++;
                }
              }
            }
            break;
          }
          if (symbolLen == 0) {
            symbolLen = tokenLen(fbPtr);
            sourceError(fbPtr, symbolLen, stmt,
      "This math symbol was not declared (with a \"$c\" or \"$v\" statement).");
            undeclErrorCount++;
            tokenNum = g_mathTokens + undeclErrorCount;
            if (tokenNum >= g_MAX_MATHTOKENS) {


              mminou.print2(
"?Error: The temporary space for holding bad tokens has run out, because\n");
              mminou.print2(
"there are too many errors.  Therefore we will force an \"out of memory\"\n");
              mminou.print2("program abort:\n");
              outOfMemory("#33 (too many errors)");
            }
            g_MathToken[tokenNum].tokenName = "";
            let(&g_MathToken[tokenNum].tokenName, left(fbPtr,symbolLen));
            g_MathToken[tokenNum].length = symbolLen;
            g_MathToken[tokenNum].tokenType = (char)var_;

            g_MathToken[tokenNum].tmp = 0;
            if (!activeVarStackPtr) {
              activeVarStack[activeVarStackPtr].tokenNum = tokenNum;
              activeVarStack[activeVarStackPtr].scope = g_currentScope;
              activeVarStack[activeVarStackPtr].tmpFlag = 0;
              activeVarStackPtr++;
            }
          }

          if (type == d_) {
            if (g_MathToken[tokenNum].tokenType == (char)con_) {
              sourceError(fbPtr, symbolLen, stmt,
                  "Constant symbols are not allowed in a \"$d\" statement.");
            }
          } else {
            if (mathStringLen == 0) {
              if (g_MathToken[tokenNum].tokenType != (char)con_) {
                sourceError(fbPtr, symbolLen, stmt, cat(
                    "The first symbol must be a constant in a \"$",
                    chr(type), "\" statement.", null));
              }
            } else {
              if (type == f_) {
                if (mathStringLen == 1) {
                  if (g_MathToken[tokenNum].tokenType == (char)con_) {
                    sourceError(fbPtr, symbolLen, stmt,
                "The second symbol must be a variable in a \"$f\" statement.");
                  }
                } else {
                  if (mathStringLen == 2) {
                    sourceError(fbPtr, symbolLen, stmt,
               "There cannot be more than two symbols in a \"$f\" statement.");
                  }
                }
              }
            }
          }


          wrkNmbrPtr[mathStringLen] = tokenNum;
          mathStringLen++;
          fbPtr = fbPtr + symbolLen;

          if (symbolLen < origSymbolLen) {


            origSymbolLen = origSymbolLen - symbolLen;
            C.go2("nextAdjToken");
          }
        }

        if (type == d_) {
          if (mathStringLen < 2) {
            sourceError(fbPtr, 2, stmt,
                "A \"$d\" statement requires at least two variable symbols.");
          }
        } else {
          if (!mathStringLen) {
            sourceError(fbPtr, 2, stmt,
                "This statement type requires at least one math symbol.");
          } else {
            if (type == f_ && mathStringLen < 2) {
              sourceError(fbPtr, 2, stmt,
                  "A \"$f\" statement requires two math symbols.");
            }
          }
        }



        nmbrTmpPtr = poolFixedMalloc(
            (mathStringLen + 1) * (long)(sizeof(nmbrString)));

        for (i = 0; i < mathStringLen; i++) {
          nmbrTmpPtr[i] = wrkNmbrPtr[i];
        }
        nmbrTmpPtr[mathStringLen] = -1;
        g_Statement[stmt].mathString = nmbrTmpPtr;
        g_Statement[stmt].mathStringLen = mathStringLen;
if(db5){if(stmt<5)mminou.print2("Statement %ld mathString: %s.\n",stmt,
  nmbrCvtMToVString(nmbrTmpPtr)); if(stmt==5)mminou.print2("(etc.)\n");}

        break;
      default:
        bug(1707);

    }


    switch (type) {
      case f_:
      case e_:
      case a_:
      case p_:



        labelActiveFlag[stmt] = 1;

    }


    switch (type) {
      case d_:

        nmbrTmpPtr = g_Statement[stmt].mathString;

        for (i = 0; i < mathStringLen - 1; i++) {
          p = nmbrTmpPtr[i];
          for (j = i + 1; j < mathStringLen; j++) {
            n = nmbrTmpPtr[j];

            if (p < n) {
              m = p;
            } else {
              if (p > n) {

                m = n;
                n = p;
              } else {
                mathTokenError(j, nmbrTmpPtr, stmt,
                    "All variables in a \"$d\" statement must be unique.");
                break;
              }
            }
            for (k = 0; k < activeDisjHypStackPtr; k++) {
              if (m == activeDisjHypStack[k].tokenNumA)
                if (n == activeDisjHypStack[k].tokenNumB)
                  break;
            }
            if (k == activeDisjHypStackPtr) {


              if (activeDisjHypStackPtr >= activeDisjHypStackSize) {
                free(wrkDisjHPtr1A);
                free(wrkDisjHPtr1B);
                free(wrkDisjHPtr1Stmt);
                free(wrkDisjHPtr2A);
                free(wrkDisjHPtr2B);
                free(wrkDisjHPtr2Stmt);
                activeDisjHypStackSize = activeDisjHypStackSize + 100;
                activeDisjHypStack = realloc(activeDisjHypStack,
                    (size_t)activeDisjHypStackSize
                    * sizeof(struct activeDisjHypStack_struct));
                wrkDisjHPtr1A = malloc((size_t)activeDisjHypStackSize
                    * sizeof(nmbrString));
                wrkDisjHPtr1B = malloc((size_t)activeDisjHypStackSize
                    * sizeof(nmbrString));
                wrkDisjHPtr1Stmt = malloc((size_t)activeDisjHypStackSize
                    * sizeof(nmbrString));
                wrkDisjHPtr2A = malloc((size_t)activeDisjHypStackSize
                    * sizeof(nmbrString));
                wrkDisjHPtr2B = malloc((size_t)activeDisjHypStackSize
                    * sizeof(nmbrString));
                wrkDisjHPtr2Stmt = malloc((size_t)activeDisjHypStackSize
                    * sizeof(nmbrString));
                if (!activeDisjHypStack
                    || !wrkDisjHPtr1A || !wrkDisjHPtr1B || !wrkDisjHPtr1Stmt
                    || !wrkDisjHPtr2A || !wrkDisjHPtr2B || !wrkDisjHPtr2Stmt)
                    outOfMemory("#28 (activeDisjHypStack)");
              }
              activeDisjHypStack[activeDisjHypStackPtr].tokenNumA = m;
              activeDisjHypStack[activeDisjHypStackPtr].tokenNumB = n;
              activeDisjHypStack[activeDisjHypStackPtr].scope = g_currentScope;
              activeDisjHypStack[activeDisjHypStackPtr].statemNum = stmt;

              activeDisjHypStackPtr++;
            }

          }
        }

        break;

      case f_:
      case e_:


        if (activeEHypStackPtr + activeFHypStackPtr >= activeHypStackSize) {
          free(wrkHypPtr1);
          free(wrkHypPtr2);
          free(wrkHypPtr3);
          activeHypStackSize = activeHypStackSize + 100;
          activeEHypStack = realloc(activeEHypStack, (size_t)activeHypStackSize
              * sizeof(struct activeEHypStack_struct));
          activeFHypStack = realloc(activeFHypStack, (size_t)activeHypStackSize
              * sizeof(struct activeFHypStack_struct));
          wrkHypPtr1 = malloc((size_t)activeHypStackSize * sizeof(nmbrString));
          wrkHypPtr2 = malloc((size_t)activeHypStackSize * sizeof(nmbrString));
          wrkHypPtr3 = malloc((size_t)activeHypStackSize * sizeof(nmbrString));
          if (!activeEHypStack || !activeFHypStack || !wrkHypPtr1 ||
              !wrkHypPtr2 || !wrkHypPtr3) outOfMemory("#32 (activeHypStack)");
        }


        if (type == e_) {
          activeEHypStack[activeEHypStackPtr].statemNum = stmt;
          activeEHypStack[activeEHypStackPtr].scope = g_currentScope;
        } else {
          activeFHypStack[activeFHypStackPtr].statemNum = stmt;
          activeFHypStack[activeFHypStackPtr].scope = g_currentScope;
        }


        reqVars = 0;
        j = 0;
        nmbrTmpPtr = g_Statement[stmt].mathString;
        k = nmbrTmpPtr[j];
        while (k != -1) {
          if (g_MathToken[k].tokenType == (char)var_) {
            if (!activeVarStack[g_MathToken[k].tmp].tmpFlag) {

              wrkVarPtr1[reqVars] = k;
              reqVars++;
              activeVarStack[g_MathToken[k].tmp].tmpFlag = 1;
            }
          }
          j++;
          k = nmbrTmpPtr[j];
        }
        nmbrTmpPtr = malloc(((size_t)reqVars + 1) * sizeof(nmbrString));
        if (!nmbrTmpPtr) outOfMemory("#32 (hypothesis variables)");
        memcpy(nmbrTmpPtr, wrkVarPtr1, (size_t)reqVars * sizeof(nmbrString));
        nmbrTmpPtr[reqVars] = -1;

        for (i = 0; i < reqVars; i++) {
          activeVarStack[g_MathToken[nmbrTmpPtr[i]].tmp].tmpFlag = 0;
        }

        if (type == e_) {
          activeEHypStack[activeEHypStackPtr].varList = nmbrTmpPtr;
          activeEHypStackPtr++;
        } else {
          activeFHypStack[activeFHypStackPtr].varList = nmbrTmpPtr;
          activeFHypStackPtr++;
        }

        break;

      case a_:
      case p_:


        reqVars = 0;
        j = 0;
        nmbrTmpPtr = g_Statement[stmt].mathString;
        k = nmbrTmpPtr[j];
        while (k != -1) {
          if (g_MathToken[k].tokenType == (char)var_) {
            if (!activeVarStack[g_MathToken[k].tmp].tmpFlag) {

              wrkVarPtr1[reqVars] = k;
              reqVars++;
              activeVarStack[g_MathToken[k].tmp].tmpFlag = 2;


            }
          }
          j++;
          k = nmbrTmpPtr[j];
        }


        for (i = 0; i < activeEHypStackPtr; i++) {


          wrkHypPtr1[i] = activeEHypStack[i].statemNum;


          nmbrTmpPtr = activeEHypStack[i].varList;
          j = 0;
          k = nmbrTmpPtr[j];
          while (k != -1) {
            if (!activeVarStack[g_MathToken[k].tmp].tmpFlag) {

              wrkVarPtr1[reqVars] = k;
              reqVars++;
            }
            activeVarStack[g_MathToken[k].tmp].tmpFlag = 1;

            j++;
            k = nmbrTmpPtr[j];
          }
        }

        reqHyps = activeEHypStackPtr;

        nmbrTmpPtr = poolFixedMalloc((reqVars + 1)
            * (long)(sizeof(nmbrString)));


        memcpy(nmbrTmpPtr, wrkVarPtr1, (size_t)reqVars * sizeof(nmbrString));
        nmbrTmpPtr[reqVars] = -1;
        g_Statement[stmt].reqVarList = nmbrTmpPtr;


        optHyps = 0;
        for (i = 0; i < activeFHypStackPtr; i++) {
          nmbrTmpPtr = activeFHypStack[i].varList;
          tokenNum = nmbrTmpPtr[0];
          if (tokenNum == -1) {


            wrkHypPtr1[reqHyps] = activeFHypStack[i].statemNum;
            reqHyps++;
            continue;
          } else {
            reqFlag = activeVarStack[g_MathToken[tokenNum].tmp].tmpFlag;
          }
          if (reqFlag) {

            wrkHypPtr1[reqHyps] = activeFHypStack[i].statemNum;
            reqHyps++;
            reqFlag = 1;
            activeVarStack[g_MathToken[tokenNum].tmp].tmpFlag = 1;

          } else {

            wrkHypPtr2[optHyps] = activeFHypStack[i].statemNum;
            optHyps++;
          }


          j = 1;
          tokenNum = nmbrTmpPtr[1];
          while (tokenNum != -1) {
            if (activeVarStack[g_MathToken[tokenNum].tmp].tmpFlag == 2) {
              activeVarStack[g_MathToken[tokenNum].tmp].tmpFlag = 1;

            }
            if (reqFlag != activeVarStack[g_MathToken[tokenNum].tmp].tmpFlag) {
              k = activeFHypStack[i].statemNum;
              m = nmbrElementIn(1, g_Statement[k].mathString, tokenNum);
              n = nmbrTmpPtr[0];
              if (reqFlag) {
                mathTokenError(m - 1, g_Statement[k].mathString, k,
                    cat("This variable does not occur in statement ",
                    str((double)stmt)," (label \"",g_Statement[stmt].labelName,
                    "\") or statement ", str((double)stmt),
                    "'s \"$e\" hypotheses, whereas variable \"",
                    g_MathToken[n].tokenName,
                   "\" DOES occur.  A \"$f\" hypothesis may not contain such a",
                    " mixture of variables.",null));
              } else {
                mathTokenError(m - 1, g_Statement[k].mathString, k,
                    cat("This variable occurs in statement ",
                    str((double)stmt)," (label \"",g_Statement[stmt].labelName,
                    "\") or statement ", str((double)stmt),
                    "'s \"$e\" hypotheses, whereas variable \"",
                    g_MathToken[n].tokenName,
               "\" does NOT occur.  A \"$f\" hypothesis may not contain such a",
                    " mixture of variables.",null));
              }
              break;
            }
            j++;
            tokenNum = nmbrTmpPtr[j];
          }

        }


        j = 0;
        nmbrTmpPtr = g_Statement[stmt].mathString;
        k = nmbrTmpPtr[j];
        while (k != -1) {
          if (g_MathToken[k].tokenType == (char)var_) {
            if (activeVarStack[g_MathToken[k].tmp].tmpFlag == 2) {

              mathTokenError(j, g_Statement[stmt].mathString, stmt,
                    cat("This variable does not occur in any active ",
                    "\"$e\" or \"$f\" hypothesis.  All variables in \"$a\" and",
                    " \"$p\" statements must appear in at least one such",
                    " hypothesis.",null));
              activeVarStack[g_MathToken[k].tmp].tmpFlag = 1;
            }
          }
          j++;
          k = nmbrTmpPtr[j];
        }


        i = 0;
        j = activeEHypStackPtr;
        for (k = 0; k < reqHyps; k++) {
          if (i >= activeEHypStackPtr) {
            wrkHypPtr3[k] = wrkHypPtr1[j];
            j++;
            continue;
          }
          if (j >= reqHyps) {
            wrkHypPtr3[k] = wrkHypPtr1[i];
            i++;
            continue;
          }
          if (wrkHypPtr1[i] > wrkHypPtr1[j]) {
            wrkHypPtr3[k] = wrkHypPtr1[j];
            j++;
          } else {
            wrkHypPtr3[k] = wrkHypPtr1[i];
            i++;
          }
        }


        nmbrTmpPtr = poolFixedMalloc((reqHyps + 1)
            * (long)(sizeof(nmbrString)));


        memcpy(nmbrTmpPtr, wrkHypPtr3, (size_t)reqHyps * sizeof(nmbrString));
        nmbrTmpPtr[reqHyps] = -1;
        g_Statement[stmt].reqHypList = nmbrTmpPtr;
        g_Statement[stmt].numReqHyp = reqHyps;

        if (type == p_) {
          nmbrTmpPtr = poolFixedMalloc((optHyps + 1)
              * (long)(sizeof(nmbrString)));

          memcpy(nmbrTmpPtr, wrkHypPtr2, (size_t)optHyps * sizeof(nmbrString));
          nmbrTmpPtr[optHyps] = -1;
          g_Statement[stmt].optHypList = nmbrTmpPtr;
        }


        optHyps = 0;
        reqHyps = 0;
        for (i = 0; i < activeDisjHypStackPtr; i++) {
          m = activeDisjHypStack[i].tokenNumA;
          n = activeDisjHypStack[i].tokenNumB;
          if (activeVarStack[g_MathToken[m].tmp].tmpFlag &&
              activeVarStack[g_MathToken[n].tmp].tmpFlag) {
            wrkDisjHPtr1A[reqHyps] = m;
            wrkDisjHPtr1B[reqHyps] = n;
            wrkDisjHPtr1Stmt[reqHyps] =
                activeDisjHypStack[i].statemNum;
            reqHyps++;
          } else {
            wrkDisjHPtr2A[optHyps] = m;
            wrkDisjHPtr2B[optHyps] = n;
            wrkDisjHPtr2Stmt[optHyps] =
                activeDisjHypStack[i].statemNum;
            optHyps++;
          }
        }


        nmbrTmpPtr = poolFixedMalloc((reqHyps + 1)
            * (long)(sizeof(nmbrString)));

        memcpy(nmbrTmpPtr, wrkDisjHPtr1A, (size_t)reqHyps
            * sizeof(nmbrString));
        nmbrTmpPtr[reqHyps] = -1;
        g_Statement[stmt].reqDisjVarsA = nmbrTmpPtr;

        nmbrTmpPtr = poolFixedMalloc((reqHyps + 1)
            * (long)(sizeof(nmbrString)));

        memcpy(nmbrTmpPtr, wrkDisjHPtr1B, (size_t)reqHyps
            * sizeof(nmbrString));
        nmbrTmpPtr[reqHyps] = -1;
        g_Statement[stmt].reqDisjVarsB = nmbrTmpPtr;

        nmbrTmpPtr = poolFixedMalloc((reqHyps + 1)
            * (long)(sizeof(nmbrString)));

        memcpy(nmbrTmpPtr, wrkDisjHPtr1Stmt, (size_t)reqHyps
            * sizeof(nmbrString));
        nmbrTmpPtr[reqHyps] = -1;
        g_Statement[stmt].reqDisjVarsStmt = nmbrTmpPtr;


        if (type == p_) {

          nmbrTmpPtr = poolFixedMalloc((optHyps + 1)
              * (long)(sizeof(nmbrString)));

          memcpy(nmbrTmpPtr, wrkDisjHPtr2A, (size_t)optHyps
              * sizeof(nmbrString));
          nmbrTmpPtr[optHyps] = -1;
          g_Statement[stmt].optDisjVarsA = nmbrTmpPtr;

          nmbrTmpPtr = poolFixedMalloc((optHyps + 1)
              * (long)(sizeof(nmbrString)));

          memcpy(nmbrTmpPtr, wrkDisjHPtr2B, (size_t)optHyps
              * sizeof(nmbrString));
          nmbrTmpPtr[optHyps] = -1;
          g_Statement[stmt].optDisjVarsB = nmbrTmpPtr;

          nmbrTmpPtr = poolFixedMalloc((optHyps + 1)
              * (long)(sizeof(nmbrString)));

          memcpy(nmbrTmpPtr, wrkDisjHPtr2Stmt, (size_t)optHyps
              * sizeof(nmbrString));
          nmbrTmpPtr[optHyps] = -1;
          g_Statement[stmt].optDisjVarsStmt = nmbrTmpPtr;

        }



        optVars = 0;
        for (i = 0; i < activeVarStackPtr; i++) {
          if (activeVarStack[i].tmpFlag) {
            activeVarStack[i].tmpFlag = 0;
          } else {
            wrkVarPtr2[optVars] = activeVarStack[i].tokenNum;
            optVars++;
          }
        }
        if (type == p_) {
          nmbrTmpPtr = poolFixedMalloc((optVars + 1)
              * (long)(sizeof(nmbrString)));

          memcpy(nmbrTmpPtr, wrkVarPtr2, (size_t)optVars * sizeof(nmbrString));
          nmbrTmpPtr[optVars] = -1;
          g_Statement[stmt].optVarList = nmbrTmpPtr;
        }

        if (optVars + reqVars != activeVarStackPtr) bug(1708);


        break;
    }


    type = g_Statement[stmt].type;
    if (type == a_) {
      if (g_minSubstLen) {
        if (g_Statement[stmt].mathStringLen == 1) {
          g_minSubstLen = 0;
          printLongLine(cat("SET EMPTY_SUBSTITUTION was",
             " turned ON (allowed) for this database.", null),
             "    ", " ");
        }
      }
    }



    if (type == a_ || type == p_) {

      reqHyps = nmbrLen(g_Statement[stmt].reqHypList);
      for (i = 0; i <= reqHyps; i++) {
        if (i < reqHyps) {
          m = (g_Statement[stmt].reqHypList)[i];
        } else {
          m = stmt;
        }
        if (g_Statement[m].type != f_) {
          nmbrTmpPtr = g_Statement[m].mathString;

          for (j = 0; j < g_Statement[m].mathStringLen; j++) {
            tokenNum = nmbrTmpPtr[j];
            if (g_MathToken[tokenNum].tokenType == (char)con_) continue;

            p = 0;

            for (k = 0; k < i; k++) {
              n = (g_Statement[stmt].reqHypList)[k];
              if (g_Statement[n].type != f_) continue;
              if (g_Statement[n].mathStringLen != 2) continue;
              if ((g_Statement[n].mathString)[1] == tokenNum) {
                p = 1;
                break;
              }
            }
            if (!p) {
              sourceError(g_Statement[m].mathSectionPtr,
                  0,
                  m, cat(
                  "The variable \"", g_MathToken[tokenNum].tokenName,
                  "\" does not appear in an active \"$f\" statement.", null));
            }
          }
        } else {

          if (g_Statement[m].mathStringLen != 2) continue;
          tokenNum = (g_Statement[m].mathString)[1];

          for (k = 0; k < i; k++) {
            n = (g_Statement[stmt].reqHypList)[k];
            if (g_Statement[n].type != f_) continue;
            if (g_Statement[n].mathStringLen != 2) continue;
            if ((g_Statement[n].mathString)[1] == tokenNum) {

              assignStmtFileAndLineNum(n);
              sourceError(g_Statement[m].mathSectionPtr,
                  0,
                  m, cat(
                  "The variable \"", g_MathToken[tokenNum].tokenName,
                "\" already appears in the earlier active \"$f\" statement \"",
                  g_Statement[n].labelName, "\" on line ",
                  str((double)(g_Statement[n].lineNum)),
                  " in file \"",
                  g_Statement[n].fileName,
                  "\".", null));
              break;
            }
          }
        }
      }
    }


  }

  if (g_currentScope > 0) {
    if (g_currentScope == 1) {
      let(&tmpStr,"A \"$}\" is");
    } else {
      let(&tmpStr,cat(str((double)g_currentScope)," \"$}\"s are",null));
    }
    sourceError(g_Statement[g_statements].labelSectionPtr +
        g_Statement[g_statements].labelSectionLen, 2, 0,
        cat(tmpStr," missing at the end of the file.",null));
  }


  j = 0;
if(db5)mminou.print2("Number of label keys before filter: %ld",g_numLabelKeys);
  for (i = 0; i < g_numLabelKeys; i++) {
    type = g_Statement[g_labelKeyBase[i]].type;
    if (type == e_ || type == f_) {
      j++;
    } else {
      g_labelKeyBase[i - j] = g_labelKeyBase[i];
    }
  }
  g_numLabelKeys = g_numLabelKeys - j;
if(db5)mminou.print2(".  After: %ld\n",g_numLabelKeys);



  free(mathTokenSameAs);
  free(reverseMathKey);
  free(labelTokenSameAs);
  free(reverseLabelKey);
  free(labelActiveFlag);
  free(activeConstStack);
  free(activeVarStack);
  free(wrkVarPtr1);
  free(wrkVarPtr2);
  for (i = 0; i < activeEHypStackPtr; i++) {
    free(activeEHypStack[i].varList);
  }
  free(activeEHypStack);
  for (i = 0; i < activeFHypStackPtr; i++) {
    free(activeFHypStack[i].varList);
  }
  free(activeFHypStack);
  free(wrkHypPtr1);
  free(wrkHypPtr2);
  free(wrkHypPtr3);
  free(activeDisjHypStack);
  free(wrkDisjHPtr1A);
  free(wrkDisjHPtr1B);
  free(wrkDisjHPtr1Stmt);
  free(wrkDisjHPtr2A);
  free(wrkDisjHPtr2B);
  free(wrkDisjHPtr2Stmt);
  free(wrkNmbrPtr);
  free(wrkStrPtr);
  free(symbolLenExists);
  let(&tmpStr, "");



}



char parseProof(long statemNum)
{

  long i, j, k, m, tok, step;
  char *fbPtr;
  long tokLength;
  long numReqHyp;
  long numOptHyp;
  long numActiveHyp;
  char zapSave;
  flag labelFlag;
  char returnFlag = 0;
  nmbrString *nmbrTmpPtr;
  void *voidPtr;
  vstring tmpStrPtr;


  flag explicitTargets = 0;

  pntrString *targetPntr = NULL_PNTRSTRING;
  nmbrString *targetNmbr = NULL_NMBRSTRING;

  nmbrString *wrkProofString = NULL_NMBRSTRING;
  long hypStepNum, hypSubProofLen, conclSubProofLen;
  long matchingHyp;
  nmbrString *oldStepNums = NULL_NMBRSTRING;
  pntrString *reqHypSubProof = NULL_PNTRSTRING;
  pntrString *reqHypOldStepNums = NULL_PNTRSTRING;
  nmbrString *rearrangedSubProofs = NULL_NMBRSTRING;
  nmbrString *rearrangedOldStepNums = NULL_NMBRSTRING;
  flag subProofMoved;


  if (g_Statement[statemNum].type != p_) {
    bug(1723);
    g_WrkProof.errorSeverity = 4;
    return (4);
  }
  fbPtr = g_Statement[statemNum].proofSectionPtr;
  if (fbPtr[0] == 0) {
    g_WrkProof.errorSeverity = 4;
    return (4);
  }
  fbPtr = fbPtr + whiteSpaceLen(fbPtr);
  if (fbPtr[0] == '(') {
    g_WrkProof.errorSeverity = parseCompressedProof(statemNum);
    return (g_WrkProof.errorSeverity);
  }



  numOptHyp = nmbrLen(g_Statement[statemNum].optHypList);
  if (g_Statement[statemNum].proofSectionLen + g_Statement[statemNum].numReqHyp
      + numOptHyp > g_wrkProofMaxSize) {
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
    }
    g_wrkProofMaxSize = g_Statement[statemNum].proofSectionLen
        + g_Statement[statemNum].numReqHyp + numOptHyp
        + 2;
    g_WrkProof.tokenSrcPtrNmbr = malloc((size_t)g_wrkProofMaxSize
        * sizeof(nmbrString));
    g_WrkProof.tokenSrcPtrPntr = malloc((size_t)g_wrkProofMaxSize
        * sizeof(pntrString));
    g_WrkProof.stepSrcPtrNmbr = malloc((size_t)g_wrkProofMaxSize
        * sizeof(nmbrString));
    g_WrkProof.stepSrcPtrPntr = malloc((size_t)g_wrkProofMaxSize
        * sizeof(pntrString));
    g_WrkProof.localLabelFlag = malloc((size_t)g_wrkProofMaxSize
        * sizeof(flag));
    g_WrkProof.hypAndLocLabel =
        malloc((size_t)g_wrkProofMaxSize * sizeof(struct sortHypAndLoc));
    g_WrkProof.localLabelPool = malloc((size_t)g_wrkProofMaxSize);
    g_WrkProof.proofString =
        poolFixedMalloc(g_wrkProofMaxSize * (long)(sizeof(nmbrString)));
    g_WrkProof.mathStringPtrs =
        malloc((size_t)g_wrkProofMaxSize * sizeof(nmbrString));
    g_WrkProof.RPNStack = malloc((size_t)g_wrkProofMaxSize * sizeof(nmbrString));
    g_WrkProof.compressedPfLabelMap =
         malloc((size_t)g_wrkProofMaxSize * sizeof(nmbrString));
    if (!g_WrkProof.tokenSrcPtrNmbr ||
        !g_WrkProof.tokenSrcPtrPntr ||
        !g_WrkProof.stepSrcPtrNmbr ||
        !g_WrkProof.stepSrcPtrPntr ||
        !g_WrkProof.localLabelFlag ||
        !g_WrkProof.hypAndLocLabel ||
        !g_WrkProof.localLabelPool ||

        !g_WrkProof.mathStringPtrs ||
        !g_WrkProof.RPNStack
        ) outOfMemory("#99 (g_WrkProof)");
  }


  g_WrkProof.errorCount = 0;
  g_WrkProof.numSteps = 0;
  g_WrkProof.numTokens = 0;
  g_WrkProof.numHypAndLoc = 0;
  g_WrkProof.numLocalLabels = 0;
  g_WrkProof.RPNStackPtr = 0;
  g_WrkProof.localLabelPoolPtr = g_WrkProof.localLabelPool;




  while (true) {
    tokLength = proofTokenLen(fbPtr);
    if (!tokLength) break;
    g_WrkProof.tokenSrcPtrPntr[g_WrkProof.numTokens] = fbPtr;
    g_WrkProof.tokenSrcPtrNmbr[g_WrkProof.numTokens] = tokLength;
    g_WrkProof.numTokens++;
    fbPtr = fbPtr + tokLength;
    fbPtr = fbPtr + whiteSpaceLen(fbPtr);
  }


  if (!g_WrkProof.numTokens) {


    if (!g_WrkProof.errorCount) {
      sourceError(fbPtr, 2, statemNum,
          "The proof is empty.  If you don't know the proof, make it a \"?\".");
    }
    g_WrkProof.errorCount++;
    if (returnFlag < 1) returnFlag = 1;


    g_WrkProof.numTokens = 1;
    g_WrkProof.tokenSrcPtrPntr[0] = "?";
    g_WrkProof.tokenSrcPtrNmbr[0] = 1;
  }


  nmbrTmpPtr = g_Statement[statemNum].optHypList;

  while (true) {
    i = nmbrTmpPtr[g_WrkProof.numHypAndLoc];
    if (i == -1) break;
    g_WrkProof.hypAndLocLabel[g_WrkProof.numHypAndLoc].labelTokenNum = i;
    g_WrkProof.hypAndLocLabel[g_WrkProof.numHypAndLoc].labelName =
        g_Statement[i].labelName;
    g_WrkProof.numHypAndLoc++;
  }

  j = g_Statement[statemNum].numReqHyp;
  nmbrTmpPtr = g_Statement[statemNum].reqHypList;
  for (i = 0; i < j; i++) {
    k = nmbrTmpPtr[i];
    g_WrkProof.hypAndLocLabel[g_WrkProof.numHypAndLoc].labelTokenNum = k;
    g_WrkProof.hypAndLocLabel[g_WrkProof.numHypAndLoc].labelName =
        g_Statement[k].labelName;
    g_WrkProof.numHypAndLoc++;
  }


  numActiveHyp = g_WrkProof.numHypAndLoc;
  qsort(g_WrkProof.hypAndLocLabel, (size_t)(g_WrkProof.numHypAndLoc),
      sizeof(struct sortHypAndLoc), hypAndLocSortCmp);



  fbPtr = g_WrkProof.tokenSrcPtrPntr[0];
  if (fbPtr[0] == ':') {
    if (!g_WrkProof.errorCount) {
      sourceError(fbPtr, 1, statemNum,
          "The colon at proof step 1 must be preceded by a local label.");
    }
    if (returnFlag < 2) returnFlag = 2;
    g_WrkProof.tokenSrcPtrPntr[0] = "?";
    g_WrkProof.tokenSrcPtrNmbr[0] = 1;
    g_WrkProof.errorCount++;
  }
  fbPtr = g_WrkProof.tokenSrcPtrPntr[g_WrkProof.numTokens - 1];
  if (fbPtr[0] == ':') {
    if (!g_WrkProof.errorCount) {
      sourceError(fbPtr, 1, statemNum,
          "The colon in the last proof step must be followed by a label.");
    }
    if (returnFlag < 2) returnFlag = 2;
    g_WrkProof.errorCount++;
    g_WrkProof.numTokens--;
  }
  labelFlag = 0;
  for (tok = 0; tok < g_WrkProof.numTokens; tok++) {
    fbPtr = g_WrkProof.tokenSrcPtrPntr[tok];


    if (tok < g_WrkProof.numTokens - 2) {
      if (((char *)((g_WrkProof.tokenSrcPtrPntr)[tok + 1]))[0] == '=') {
        explicitTargets = 1;
        continue;
      }
    }
    if (fbPtr[0] == '=') continue;


    g_WrkProof.stepSrcPtrNmbr[g_WrkProof.numSteps] =
        g_WrkProof.tokenSrcPtrNmbr[tok];
    g_WrkProof.stepSrcPtrPntr[g_WrkProof.numSteps] = fbPtr;


    g_WrkProof.localLabelFlag[g_WrkProof.numSteps] = labelFlag;
    labelFlag = 0;

    g_WrkProof.numSteps++;
    if (fbPtr[0] != ':') continue;


    labelFlag = 1;

    g_WrkProof.numSteps = g_WrkProof.numSteps - 2;
    fbPtr = g_WrkProof.tokenSrcPtrPntr[tok - 1];
    tokLength = g_WrkProof.tokenSrcPtrNmbr[tok - 1];


    for (j = 0; j < tokLength; j++) {
      if (illegalLabelChar[(unsigned char)fbPtr[j]]) {
        if (!g_WrkProof.errorCount) {
          sourceError(fbPtr + j, 1, statemNum,cat(
              "The local label at proof step ",
              str((double)(g_WrkProof.numSteps + 1)),
              " is incorrect.  Only letters,",
              " digits, \"_\", \"-\", and \".\" are allowed in local labels.",
              null));
        }
        if (returnFlag < 2) returnFlag = 2;
        g_WrkProof.errorCount++;
      }
    }


    memcpy(g_WrkProof.localLabelPoolPtr, fbPtr, (size_t)tokLength);
    g_WrkProof.localLabelPoolPtr[tokLength] = 0;
    g_WrkProof.hypAndLocLabel[g_WrkProof.numHypAndLoc].labelTokenNum =
       -g_WrkProof.numSteps - 1000;
    g_WrkProof.hypAndLocLabel[g_WrkProof.numHypAndLoc].labelName
        = g_WrkProof.localLabelPoolPtr;


    voidPtr = (void *)bsearch(g_WrkProof.localLabelPoolPtr, g_labelKeyBase,
        (size_t)g_numLabelKeys, sizeof(long), labelSrchCmp);
    if (voidPtr) {
      j = *(long *)voidPtr;
      if (j <= statemNum) {
        if (!g_WrkProof.errorCount) {
          assignStmtFileAndLineNum(j);
          sourceError(fbPtr, tokLength, statemNum,cat(
              "The local label at proof step ",
              str((double)(g_WrkProof.numSteps + 1)),
              " is the same as the label of statement ",
              str((double)j),
              " at line ",
              str((double)(g_Statement[j].lineNum)),
              " in file \"",
              g_Statement[j].fileName,
              "\".  Local labels must be different from active statement labels.",
              null));
        }
        g_WrkProof.errorCount++;
        if (returnFlag < 2) returnFlag = 2;
      }
    }


    voidPtr = (void *)bsearch(g_WrkProof.localLabelPoolPtr,
        g_WrkProof.hypAndLocLabel,
        (size_t)numActiveHyp, sizeof(struct sortHypAndLoc), hypAndLocSrchCmp);
    if (voidPtr) {
      j = ( (struct sortHypAndLoc *)voidPtr)->labelTokenNum;
      if (!g_WrkProof.errorCount) {
        assignStmtFileAndLineNum(j);
        sourceError(fbPtr, tokLength, statemNum,cat(
            "The local label at proof step ",
            str((double)(g_WrkProof.numSteps + 1)),
            " is the same as the label of statement ",
            str((double)j),
            " at line ",
            str((double)(g_Statement[j].lineNum)),
            " in file \"",
            g_Statement[j].fileName,
            "\".  Local labels must be different from active statement labels.",
            null));
      }
      g_WrkProof.errorCount++;
      if (returnFlag < 2) returnFlag = 2;
      g_WrkProof.numHypAndLoc--;
    }

    g_WrkProof.numHypAndLoc++;
    g_WrkProof.localLabelPoolPtr = &g_WrkProof.localLabelPoolPtr[tokLength + 1];

  }



  if (explicitTargets == 1) {
    pntrLet(&targetPntr, pntrSpace(g_WrkProof.numSteps));
    nmbrLet(&targetNmbr, nmbrSpace(g_WrkProof.numSteps));
    step = 0;
    for (tok = 0; tok < g_WrkProof.numTokens - 2; tok++) {
      if (((char *)((g_WrkProof.tokenSrcPtrPntr)[tok + 1]))[0] == '=') {
        fbPtr = g_WrkProof.tokenSrcPtrPntr[tok];
        if (step >= g_WrkProof.numSteps) {
          if (!g_WrkProof.errorCount) {
            sourceError(fbPtr, g_WrkProof.tokenSrcPtrNmbr[tok], statemNum, cat(
                "There are more target labels than proof steps.", null));
          }
          g_WrkProof.errorCount++;
          if (returnFlag < 2) returnFlag = 2;
          break;
        }
        targetPntr[step] = fbPtr;
        targetNmbr[step] = g_WrkProof.tokenSrcPtrNmbr[tok];
        if (g_WrkProof.tokenSrcPtrPntr[tok + 2]
            != g_WrkProof.stepSrcPtrPntr[step]) {
          if (!g_WrkProof.errorCount) {
            sourceError(fbPtr, g_WrkProof.tokenSrcPtrNmbr[tok], statemNum, cat(
                "The target label for step ", str((double)step + 1),
                " is not assigned to that step.  ",
                "(Check for missing or extra \"=\".)", null));
          }
          g_WrkProof.errorCount++;
          if (returnFlag < 2) returnFlag = 2;
        }
        step++;
      }
    }
    if (step != g_WrkProof.numSteps) {
      if (!g_WrkProof.errorCount) {
        sourceError(
            (char *)((g_WrkProof.tokenSrcPtrPntr)[g_WrkProof.numTokens - 1]),
            g_WrkProof.tokenSrcPtrNmbr[g_WrkProof.numTokens - 1],
            statemNum, cat(
                "There are ", str((double)(g_WrkProof.numSteps)), " proof steps but only ",
                str((double)step), " target labels.", null));
      }
      g_WrkProof.errorCount++;
      if (returnFlag < 2) returnFlag = 2;
    }
  }

  if (g_WrkProof.numHypAndLoc > numActiveHyp) {


    qsort(g_WrkProof.hypAndLocLabel, (size_t)(g_WrkProof.numHypAndLoc),
        sizeof(struct sortHypAndLoc), hypAndLocSortCmp);


    for (i = 1; i < g_WrkProof.numHypAndLoc; i++) {
      if (!strcmp(g_WrkProof.hypAndLocLabel[i - 1].labelName,
          g_WrkProof.hypAndLocLabel[i].labelName)) {

        j = -(g_WrkProof.hypAndLocLabel[i - 1].labelTokenNum + 1000);
        k = -(g_WrkProof.hypAndLocLabel[i].labelTokenNum + 1000);
        if (j > k) {
          m = j;
          j = k;
          k = m;
        }

        fbPtr = g_WrkProof.stepSrcPtrPntr[k - 1];
        fbPtr = fbPtr + g_WrkProof.stepSrcPtrNmbr[k - 1];
        fbPtr = fbPtr + whiteSpaceLen(fbPtr);
        if (!g_WrkProof.errorCount) {
          sourceError(fbPtr,
              proofTokenLen(fbPtr), statemNum,
              cat("The local label at proof step ", str((double)k + 1),
              " is the same as the one declared at step ",
              str((double)j + 1), ".", null));
        }
        g_WrkProof.errorCount++;
        if (returnFlag < 2) returnFlag = 2;
      }
    }

  }


  g_WrkProof.proofString[g_WrkProof.numSteps] = -1;
  nmbrZapLen(g_WrkProof.proofString, g_WrkProof.numSteps);



  if (explicitTargets == 1) {

    nmbrLet(&oldStepNums, nmbrSpace(g_WrkProof.numSteps));
    for (i = 0; i < g_WrkProof.numSteps; i++) {
      oldStepNums[i] = i;
    }
  }

  for (step = 0; step < g_WrkProof.numSteps; step++) {
    tokLength = g_WrkProof.stepSrcPtrNmbr[step];
    fbPtr = g_WrkProof.stepSrcPtrPntr[step];


    if (fbPtr[0] == '?') {
      if (returnFlag < 1) returnFlag = 1;

      g_WrkProof.proofString[step] = -(long)'?';

      g_WrkProof.RPNStack[g_WrkProof.RPNStackPtr] = step;
      g_WrkProof.RPNStackPtr++;
      continue;
    }


    zapSave = fbPtr[tokLength];
    fbPtr[tokLength] = 0;


    voidPtr = (void *)bsearch(fbPtr, g_WrkProof.hypAndLocLabel,
        (size_t)(g_WrkProof.numHypAndLoc), sizeof(struct sortHypAndLoc),
        hypAndLocSrchCmp);
    if (voidPtr) {
      fbPtr[tokLength] = zapSave;
      j = ((struct sortHypAndLoc *)voidPtr)->labelTokenNum;
      g_WrkProof.proofString[step] = j;


      g_WrkProof.RPNStack[g_WrkProof.RPNStackPtr] = step;
      g_WrkProof.RPNStackPtr++;

      if (j < 0) {
        i = -1000 - j;
        if (i < 0) bug(1734);


        if (i > step) {
          if (!g_WrkProof.errorCount) {
            sourceError(fbPtr, tokLength, statemNum,cat("Proof step ",
                str((double)step + 1),
                " references a local label before it is declared.",
                null));
          }
          g_WrkProof.proofString[step] = -(long)'?';
          g_WrkProof.errorCount++;
          if (returnFlag < 2) returnFlag = 2;
        }

        if (g_WrkProof.localLabelFlag[step]) {
          if (!g_WrkProof.errorCount) {
            sourceError(fbPtr, tokLength, statemNum, cat(
                "The local label reference at proof step ",
                str((double)step + 1),
                " declares a local label.  Only \"$a\" and \"$p\" statement",
                " labels may have local label declarations.",null));
          }
          g_WrkProof.errorCount++;
          if (returnFlag < 2) returnFlag = 2;
        }
      } else {
        if (g_WrkProof.localLabelFlag[step]) {
          if (!g_WrkProof.errorCount) {
            sourceError(fbPtr, tokLength, statemNum, cat(
                "The hypothesis reference at proof step ",
                str((double)step + 1),
                " declares a local label.  Only \"$a\" and \"$p\" statement",
                " labels may have local label declarations.",null));
          }
          g_WrkProof.errorCount++;
          if (returnFlag < 2) returnFlag = 2;
        }
        if (j <= 0) bug(1709);
      }
      continue;
    }


    voidPtr = (void *)bsearch(fbPtr, g_labelKeyBase, (size_t)g_numLabelKeys,
        sizeof(long), labelSrchCmp);
    fbPtr[tokLength] = zapSave;
    if (!voidPtr) {
      if (!g_WrkProof.errorCount) {
        sourceError(fbPtr, tokLength, statemNum, cat(
            "The token at proof step ",
            str((double)step + 1),
            " is not an active statement label or a local label.",null));
      }
      g_WrkProof.errorCount++;
      g_WrkProof.proofString[step] = -(long)'?';

      g_WrkProof.RPNStack[g_WrkProof.RPNStackPtr] = step;
      g_WrkProof.RPNStackPtr++;
      if (returnFlag < 2) returnFlag = 2;
      continue;
    }


    j = *(long *)voidPtr;
    if (g_Statement[j].type != a_ && g_Statement[j].type != p_) bug(1710);
    g_WrkProof.proofString[step] = j;

    if (j >= statemNum) {
      if (!g_WrkProof.errorCount) {
        if (j == statemNum) {
          sourceError(fbPtr, tokLength, statemNum, cat(
              "The label at proof step ",
              str((double)step + 1),
              " is the label of this statement.  A statement may not be used to",
              " prove itself.",null));
        } else {
          assignStmtFileAndLineNum(j);
          sourceError(fbPtr, tokLength, statemNum, cat(
              "The label \"", g_Statement[j].labelName, "\" at proof step ",
              str((double)step + 1),
              " is the label of a future statement (at line ",
              str((double)(g_Statement[j].lineNum)),
              " in file ",g_Statement[j].fileName,
      ").  Only local labels or previous, active statements may be referenced.",
              null));
        }
      }
      g_WrkProof.errorCount++;
      if (returnFlag < 2) returnFlag = 2;
    }


    numReqHyp = g_Statement[j].numReqHyp;


    if (g_WrkProof.RPNStackPtr < numReqHyp) {
      if (!g_WrkProof.errorCount) {
        tmpStrPtr = shortDumpRPNStack();
        if (strcmp(left(tmpStrPtr,18),"RPN stack is empty")){
          i = instr(1,tmpStrPtr,"contains ");
          let(&tmpStrPtr,cat(left(tmpStrPtr,i + 7)," only",
            right(tmpStrPtr,i + 8),
            null));
        }
        if (numReqHyp == 1) {
          let(&tmpStrPtr,cat("a hypothesis but the ",tmpStrPtr,null));
        } else {
          let(&tmpStrPtr,cat(str((double)numReqHyp)," hypotheses but the ",tmpStrPtr,
              null));
        }
        sourceError(fbPtr, tokLength, statemNum, cat(
            "At proof step ",
            str((double)step + 1),", statement \"",
            g_Statement[j].labelName,"\" requires ",
            tmpStrPtr,".",null));
        let(&tmpStrPtr, "");
      }

      g_WrkProof.errorCount++;
      g_WrkProof.proofString[step] = -(long)'?';

      g_WrkProof.RPNStack[g_WrkProof.RPNStackPtr] = step;
      g_WrkProof.RPNStackPtr++;
      if (returnFlag < 3) returnFlag = 3;
      continue;
    }


    if (explicitTargets == 1) {
      nmbrLet(&wrkProofString, g_WrkProof.proofString);
      nmbrTmpPtr = g_Statement[j].reqHypList;
      numReqHyp = g_Statement[j].numReqHyp;
      conclSubProofLen = subproofLen(wrkProofString, step);
      pntrLet(&reqHypSubProof, pntrNSpace(numReqHyp));

      pntrLet(&reqHypOldStepNums, pntrNSpace(numReqHyp));

      k = 0;
      for (i = 0; i < numReqHyp; i++) {
        m = g_WrkProof.RPNStackPtr - numReqHyp + i;
        hypStepNum = g_WrkProof.RPNStack[m];
        hypSubProofLen = subproofLen(wrkProofString, hypStepNum);
        k += hypSubProofLen;
        nmbrLet((nmbrString **)(&(reqHypSubProof[i])),

            nmbrSeg(wrkProofString,
                hypStepNum - hypSubProofLen + 2, hypStepNum + 1));
        nmbrLet((nmbrString **)(&(reqHypOldStepNums[i])),

            nmbrSeg(oldStepNums,
                hypStepNum - hypSubProofLen + 2, hypStepNum + 1));
      }
      if (k != conclSubProofLen - 1 ) {

        bug(1731);
      }
      nmbrLet(&rearrangedSubProofs, NULL_NMBRSTRING);
      matchingHyp = -1;
      for (i = 0; i < numReqHyp; i++) {
        matchingHyp = -1;
        for (k = 0; k < numReqHyp; k++) {
          m = g_WrkProof.RPNStackPtr - numReqHyp + k;
          hypStepNum = g_WrkProof.RPNStack[m];



          fbPtr = targetPntr[hypStepNum];
          zapSave = fbPtr[targetNmbr[hypStepNum]];
          fbPtr[targetNmbr[hypStepNum]] = 0;

          if (!strcmp(g_Statement[nmbrTmpPtr[i]].labelName, fbPtr)) {
            matchingHyp = k;
          }
          fbPtr[targetNmbr[hypStepNum]] = zapSave;
          if (matchingHyp != -1) break;
        }
        if (matchingHyp == -1) {
          if (!g_WrkProof.errorCount) {
            sourceError(fbPtr, 1, statemNum, cat(
                "The target labels for the hypotheses for step ", str((double)step + 1),
                " do not match hypothesis \"",
                g_Statement[nmbrTmpPtr[i]].labelName,
                "\" of the assertion \"",
                g_Statement[j].labelName,
                "\" in step ",  str((double)step + 1), ".",
                null));
          }
          g_WrkProof.errorCount++;
          if (returnFlag < 2) returnFlag = 2;
          break;
        }

        nmbrLet(&rearrangedSubProofs, nmbrCat(rearrangedSubProofs,
            reqHypSubProof[matchingHyp], null));
        nmbrLet(&rearrangedOldStepNums, nmbrCat(rearrangedOldStepNums,
            reqHypOldStepNums[matchingHyp], null));
      }

      if (matchingHyp != -1) {
        if (nmbrLen(rearrangedSubProofs) != conclSubProofLen - 1
             ) {

          bug(1732);
        }
        nmbrLet(&(wrkProofString), nmbrCat(
            nmbrLeft(wrkProofString, step - conclSubProofLen + 1),
            rearrangedSubProofs,
            nmbrRight(wrkProofString, step + 1), null));
        nmbrLet(&oldStepNums, nmbrCat(
            nmbrLeft(oldStepNums, step - conclSubProofLen + 1),
            rearrangedOldStepNums,
            nmbrRight(oldStepNums, step + 1), null));
      }


      for (i = 0; i < step; i++) {

        (g_WrkProof.proofString)[i] = wrkProofString[i];
      }
      if ((g_WrkProof.proofString)[step] != wrkProofString[step]) bug(1735);


      for (i = 0; i < numReqHyp; i++) {
        nmbrLet((nmbrString **)(&(reqHypSubProof[i])), NULL_NMBRSTRING);
        nmbrLet((nmbrString **)(&(reqHypOldStepNums[i])), NULL_NMBRSTRING);
      }
      pntrLet(&reqHypSubProof, NULL_PNTRSTRING);
      pntrLet(&reqHypOldStepNums, NULL_PNTRSTRING);
      nmbrLet(&rearrangedSubProofs, NULL_NMBRSTRING);
      nmbrLet(&rearrangedOldStepNums, NULL_NMBRSTRING);
      nmbrLet(&wrkProofString, NULL_NMBRSTRING);
    }


    numReqHyp = g_Statement[j].numReqHyp;




    g_WrkProof.RPNStackPtr = g_WrkProof.RPNStackPtr - numReqHyp;
    g_WrkProof.RPNStack[g_WrkProof.RPNStackPtr] = step;
    g_WrkProof.RPNStackPtr++;

  }


  if (g_WrkProof.RPNStackPtr != 1) {
    tmpStrPtr = shortDumpRPNStack();
    fbPtr = g_WrkProof.stepSrcPtrPntr[g_WrkProof.numSteps - 1];
    if (!g_WrkProof.errorCount) {
      sourceError(fbPtr, proofTokenLen(fbPtr), statemNum, cat("After proof step ",
          str((double)(g_WrkProof.numSteps))," (the last step), the ",
          tmpStrPtr,".  It should contain exactly one entry.",null));
    }
    g_WrkProof.errorCount++;
    if (returnFlag < 3) returnFlag = 3;
  }


  if (explicitTargets) {

    for (step = 0; step < g_WrkProof.numSteps; step++) {
      k = (g_WrkProof.proofString)[step];
      if (k <= -1000) {
        k = -1000 - k;
        if (k < 0 || k >= g_WrkProof.numSteps) bug(1733);

        if (oldStepNums[k] == k) {

          continue;
        }
        i = 0;

        for (m = 0; m < g_WrkProof.numSteps; m++) {
          if (oldStepNums[m] == k) {
            (g_WrkProof.proofString)[step] = -1000 - m;
            i = 1;
            break;
          }
        }
        if (i == 0) bug(1740);
      }
    }




    do {
      subProofMoved = 0;
      for (step = 0; step < g_WrkProof.numSteps; step++) {
        k = (g_WrkProof.proofString)[step];
        if (k <= -1000) {
          k = -1000 - k;
          if (k > step) {
            m = subproofLen(g_WrkProof.proofString, k);





            nmbrLet(&wrkProofString, nmbrCat(

                nmbrLeft(g_WrkProof.proofString, step),

                nmbrMid(g_WrkProof.proofString, k - m + 2, m),

                nmbrSeg(g_WrkProof.proofString, step + 2, k - m + 1),
                nmbrMid(g_WrkProof.proofString, step + 1, 1),

                nmbrRight(g_WrkProof.proofString, k + 2),
                null));
            if (nmbrLen(wrkProofString) != g_WrkProof.numSteps) {
              bug(1736);
            }
            if (wrkProofString[k] != (g_WrkProof.proofString)[step]) {
              bug(1737);
            }



            for (i = 0; i < g_WrkProof.numSteps; i++) {
              j = (wrkProofString)[i];
              if (j > -1000) continue;
              j = -1000 - j;
              if (j >= 0 && j < step) {


                j = j + 0;
              } else if (j == step) {
                bug(1738);
              } else if (j > step && j <= k - m) {

                j = j + m - 1;
              } else if (j > k - m && j <= k) {

                j = j + step + m - 1 - k;
              } else if (j > k && j <= g_WrkProof.numSteps - 1) {



                j = j + 0;
              } else {
                bug(1739);
              }
              (wrkProofString)[i] = -j - 1000;
            }


            for (i = 0; i < g_WrkProof.numSteps; i++) {
              (g_WrkProof.proofString)[i] = wrkProofString[i];
            }


            subProofMoved = 1;

            for (i = 0; i < g_WrkProof.numSteps; i++) {
              (g_WrkProof.proofString)[i] = wrkProofString[i];
            }
            break;

          }
        }
      }
    } while (subProofMoved);


    pntrLet(&targetPntr, NULL_PNTRSTRING);
    nmbrLet(&targetNmbr, NULL_NMBRSTRING);
    nmbrLet(&oldStepNums, NULL_NMBRSTRING);
    nmbrLet(&wrkProofString, NULL_NMBRSTRING);
  }


  g_WrkProof.errorSeverity = returnFlag;
  return (returnFlag);

}





char parseCompressedProof(long statemNum)
{

  long i, j, k, step, stmt;

  char *fbPtr;
  char *fbStartProof;
  char *labelStart;
  long tokLength;
  long numReqHyp;
  long numOptHyp;
  char zapSave;
  flag breakFlag;
  char returnFlag = 0;
  nmbrString *nmbrTmpPtr;
  void *voidPtr;
  vstring tmpStrPtr;
  flag hypLocUnkFlag;
  long labelMapIndex;

  static unsigned char chrWeight[256];
  static unsigned char chrType[256];
  static flag chrTablesInited = 0;
  static char *digits = "0123456789";
  static char *letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  static char labelChar = ':';
  static long lettersLen;
  static long digitsLen;


  long bggyProofLen;
  char bggyZapSave;
  flag bggyAlgo;

  labelStart = "";


  if (!chrTablesInited) {
    chrTablesInited = 1;


    letters = "ABCDEFGHIJKLMNOPQRST";
    digits = "UVWXY";
    labelChar = 'Z';

    lettersLen = (long)strlen(letters);
    digitsLen = (long)strlen(digits);



    for (i = 0; i < 256; i++) {
      chrWeight[i] = 0;
      chrType[i] = 6;
    }
    j = lettersLen;
    for (i = 0; i < j; i++) {
      chrWeight[(long)(letters[i])] = (unsigned char)i;
      chrType[(long)(letters[i])] = 0;
    }
    j = digitsLen;
    for (i = 0; i < j; i++) {
      chrWeight[(long)(digits[i])] = (unsigned char)i;
      chrType[(long)(digits[i])] = 1;
    }
    for (i = 0; i < 256; i++) {
      if (isspace(i)) chrType[i] = 3;
    }
    chrType[(long)(labelChar)] = 2;
    chrType['$'] = 4;
    chrType['?'] = 5;
  }


  if (g_Statement[statemNum].type != p_) {
    bug(1724);
    return (4);
  }
  fbPtr = g_Statement[statemNum].proofSectionPtr;
  if (fbPtr[0] == 0) {
    bug(1711);
  }
  fbPtr = fbPtr + whiteSpaceLen(fbPtr);
  if (fbPtr[0] != '(') {
    bug(1712);
  }



  numOptHyp = nmbrLen(g_Statement[statemNum].optHypList);
  if (g_Statement[statemNum].proofSectionLen + g_Statement[statemNum].numReqHyp
      + numOptHyp > g_wrkProofMaxSize) {
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
    }
    g_wrkProofMaxSize = g_Statement[statemNum].proofSectionLen
        + g_Statement[statemNum].numReqHyp + numOptHyp;
    g_WrkProof.tokenSrcPtrNmbr = malloc((size_t)g_wrkProofMaxSize
        * sizeof(nmbrString));
    g_WrkProof.tokenSrcPtrPntr = malloc((size_t)g_wrkProofMaxSize
        * sizeof(pntrString));
    g_WrkProof.stepSrcPtrNmbr = malloc((size_t)g_wrkProofMaxSize
        * sizeof(nmbrString));
    g_WrkProof.stepSrcPtrPntr = malloc((size_t)g_wrkProofMaxSize
        * sizeof(pntrString));
    g_WrkProof.localLabelFlag = malloc((size_t)g_wrkProofMaxSize
        * sizeof(flag));
    g_WrkProof.hypAndLocLabel =
        malloc((size_t)g_wrkProofMaxSize * sizeof(struct sortHypAndLoc));
    g_WrkProof.localLabelPool = malloc((size_t)g_wrkProofMaxSize);
    g_WrkProof.proofString =
        poolFixedMalloc(g_wrkProofMaxSize * (long)(sizeof(nmbrString)));
    g_WrkProof.mathStringPtrs =
        malloc((size_t)g_wrkProofMaxSize * sizeof(nmbrString));
    g_WrkProof.RPNStack = malloc((size_t)g_wrkProofMaxSize * sizeof(nmbrString));
    g_WrkProof.compressedPfLabelMap =
         malloc((size_t)g_wrkProofMaxSize * sizeof(nmbrString));
    if (!g_WrkProof.tokenSrcPtrNmbr ||
        !g_WrkProof.tokenSrcPtrPntr ||
        !g_WrkProof.stepSrcPtrNmbr ||
        !g_WrkProof.stepSrcPtrPntr ||
        !g_WrkProof.localLabelFlag ||
        !g_WrkProof.hypAndLocLabel ||
        !g_WrkProof.localLabelPool ||

        !g_WrkProof.mathStringPtrs ||
        !g_WrkProof.RPNStack
        ) outOfMemory("#99 (g_WrkProof)");
  }


  g_WrkProof.errorCount = 0;
  g_WrkProof.numSteps = 0;
  g_WrkProof.numTokens = 0;
  g_WrkProof.numHypAndLoc = 0;
  g_WrkProof.numLocalLabels = 0;
  g_WrkProof.RPNStackPtr = 0;
  g_WrkProof.localLabelPoolPtr = g_WrkProof.localLabelPool;

  fbPtr++;





  while (true) {
    fbPtr = fbPtr + whiteSpaceLen(fbPtr);
    tokLength = proofTokenLen(fbPtr);
    if (!tokLength) {
      if (!g_WrkProof.errorCount) {
        sourceError(fbPtr, 2, statemNum,
            "A \")\" which ends the label list is not present.");
      }
      g_WrkProof.errorCount++;
      if (returnFlag < 3) returnFlag = 3;
      break;
    }
    if (fbPtr[0] == ')') {
      fbPtr++;
      break;
    }
    g_WrkProof.stepSrcPtrPntr[g_WrkProof.numSteps] = fbPtr;
    g_WrkProof.stepSrcPtrNmbr[g_WrkProof.numSteps] = tokLength;
    g_WrkProof.numSteps++;
    fbPtr = fbPtr + tokLength;
  }

  fbStartProof = fbPtr;


  nmbrTmpPtr = g_Statement[statemNum].optHypList;

  while (true) {
    i = nmbrTmpPtr[g_WrkProof.numHypAndLoc];
    if (i == -1) break;
    g_WrkProof.hypAndLocLabel[g_WrkProof.numHypAndLoc].labelTokenNum = i;
    g_WrkProof.hypAndLocLabel[g_WrkProof.numHypAndLoc].labelName =
        g_Statement[i].labelName;
    g_WrkProof.numHypAndLoc++;
  }

  j = g_Statement[statemNum].numReqHyp;
  nmbrTmpPtr = g_Statement[statemNum].reqHypList;
  for (i = 0; i < j; i++) {
    k = nmbrTmpPtr[i];
    g_WrkProof.hypAndLocLabel[g_WrkProof.numHypAndLoc].labelTokenNum = -1000 - k;
    g_WrkProof.hypAndLocLabel[g_WrkProof.numHypAndLoc].labelName =
        g_Statement[k].labelName;
    g_WrkProof.numHypAndLoc++;
  }


  qsort(g_WrkProof.hypAndLocLabel, (size_t)(g_WrkProof.numHypAndLoc),
      sizeof(struct sortHypAndLoc), hypAndLocSortCmp);


  g_WrkProof.proofString[g_WrkProof.numSteps] = -1;
  nmbrZapLen(g_WrkProof.proofString, g_WrkProof.numSteps);


  for (step = 0; step < g_WrkProof.numSteps; step++) {
    tokLength = g_WrkProof.stepSrcPtrNmbr[step];
    fbPtr = g_WrkProof.stepSrcPtrPntr[step];


    zapSave = fbPtr[tokLength];
    fbPtr[tokLength] = 0;


    voidPtr = (void *)bsearch(fbPtr, g_WrkProof.hypAndLocLabel,
        (size_t)(g_WrkProof.numHypAndLoc), sizeof(struct sortHypAndLoc),
        hypAndLocSrchCmp);
    if (voidPtr) {

      fbPtr[tokLength] = zapSave;
      j = ((struct sortHypAndLoc *)voidPtr)->labelTokenNum;


      if (j < 0) {
        j = -1000 - j;
        if (!g_WrkProof.errorCount) {
          sourceError(fbPtr, tokLength, statemNum,
              "Required hypotheses may not be explicitly declared.");
        }
        g_WrkProof.errorCount++;

        if (returnFlag < 1) returnFlag = 1;
      }

      g_WrkProof.proofString[step] = j;
      if (j <= 0) bug(1713);
      continue;
    }


    voidPtr = (void *)bsearch(fbPtr, g_labelKeyBase, (size_t)g_numLabelKeys,
        sizeof(long), labelSrchCmp);
    fbPtr[tokLength] = zapSave;
    if (!voidPtr) {
      if (!g_WrkProof.errorCount) {
        sourceError(fbPtr, tokLength, statemNum,
         "This token is not the label of an assertion or optional hypothesis.");
      }
      g_WrkProof.errorCount++;
      g_WrkProof.proofString[step] = -(long)'?';
      if (returnFlag < 2) returnFlag = 2;
      continue;
    }


    j = *(long *)voidPtr;
    if (g_Statement[j].type != a_ && g_Statement[j].type != p_) bug(1714);
    g_WrkProof.proofString[step] = j;

    if (j >= statemNum) {
      if (!g_WrkProof.errorCount) {
        if (j == statemNum) {
          sourceError(fbPtr, tokLength, statemNum, cat(
              "The label at proof step ",
              str((double)step + 1),
             " is the label of this statement.  A statement may not be used to",
              " prove itself.",null));
        } else {
          assignStmtFileAndLineNum(j);
          sourceError(fbPtr, tokLength, statemNum, cat(
              "The label \"", g_Statement[j].labelName, "\" at proof step ",
              str((double)step + 1),
              " is the label of a future statement (at line ",
              str((double)(g_Statement[j].lineNum)),
              " in file ",g_Statement[j].fileName,
              ").  Only previous statements may be referenced.",
              null));
        }
      }
      g_WrkProof.errorCount++;
      if (returnFlag < 2) returnFlag = 2;
    }

  }

  g_WrkProof.compressedPfNumLabels = g_Statement[statemNum].numReqHyp;
  nmbrTmpPtr = g_Statement[statemNum].reqHypList;
  for (i = 0; i < g_WrkProof.compressedPfNumLabels; i++) {
    g_WrkProof.compressedPfLabelMap[i] = nmbrTmpPtr[i];
  }
  for (i = 0; i < g_WrkProof.numSteps; i++) {
    g_WrkProof.compressedPfLabelMap[i + g_WrkProof.compressedPfNumLabels] =
        g_WrkProof.proofString[i];
  }
  g_WrkProof.compressedPfNumLabels = g_WrkProof.compressedPfNumLabels +
      g_WrkProof.numSteps;


  g_WrkProof.numSteps = 0;
  g_WrkProof.RPNStackPtr = 0;



  bggyProofLen = g_Statement[statemNum].proofSectionLen -
             (fbPtr - g_Statement[statemNum].proofSectionPtr);

  bggyZapSave = fbPtr[bggyProofLen];
  fbPtr[bggyProofLen] = 0;
  bggyAlgo = 0;
  if (strstr(fbPtr, "UV") != null) {
    if (strstr(fbPtr, "UU") == null) {
      bggyAlgo = 1;
      mminou.print2("?Warning: the proof of \"%s\" uses obsolete compression.\n",
          g_Statement[statemNum].labelName);
      mminou.print2(" Please SAVE PROOF * / COMPRESSED to reformat your proofs.\n");
    }
  }
  fbPtr[bggyProofLen] = bggyZapSave;


  fbPtr = fbStartProof;
  breakFlag = 0;
  labelMapIndex = 0;
  while (true) {
    switch (chrType[(long)(fbPtr[0])]) {
      case 0:
        if (!labelMapIndex) labelStart = fbPtr;


        tokLength = fbPtr - labelStart + 1;
        g_WrkProof.stepSrcPtrNmbr[g_WrkProof.numSteps] = tokLength;
        g_WrkProof.stepSrcPtrPntr[g_WrkProof.numSteps] = labelStart;



        labelMapIndex = labelMapIndex * lettersLen +
            chrWeight[(long)(fbPtr[0])];
        if (labelMapIndex >= g_WrkProof.compressedPfNumLabels) {
          if (!g_WrkProof.errorCount) {
            sourceError(labelStart, tokLength, statemNum, cat(
     "This compressed label reference is outside the range of the label list.",
                "  The compressed label value is ", str((double)labelMapIndex),
                " but the largest label defined is ",
                str((double)(g_WrkProof.compressedPfNumLabels - 1)), ".", null));
          }
          g_WrkProof.errorCount++;
          if (returnFlag < 2) returnFlag = 2;
          labelMapIndex = 0;
        }

        stmt = g_WrkProof.compressedPfLabelMap[labelMapIndex];
        g_WrkProof.proofString[g_WrkProof.numSteps] = stmt;


        hypLocUnkFlag = 0;
        if (stmt < 0) {
          hypLocUnkFlag = 1;
        } else {
          if (g_Statement[stmt].type != (char)a_ &&
              g_Statement[stmt].type != (char)p_) hypLocUnkFlag = 1;

        }
        if (hypLocUnkFlag) {
          g_WrkProof.RPNStack[g_WrkProof.RPNStackPtr] = g_WrkProof.numSteps;
          g_WrkProof.RPNStackPtr++;
        } else {


          numReqHyp = g_Statement[stmt].numReqHyp;


          if (g_WrkProof.RPNStackPtr < numReqHyp) {
            if (!g_WrkProof.errorCount) {
              tmpStrPtr = shortDumpRPNStack();
              if (strcmp(left(tmpStrPtr,18),"RPN stack is empty")){
                i = instr(1,tmpStrPtr,"contains ");
                let(&tmpStrPtr,cat(left(tmpStrPtr,i + 7)," only",
                  right(tmpStrPtr,i + 8),
                  null));
              }
              if (numReqHyp == 1) {
                let(&tmpStrPtr,cat("a hypothesis but the ",tmpStrPtr,null));
              } else {
                let(&tmpStrPtr,cat(str((double)numReqHyp)," hypotheses but the ",tmpStrPtr,
                    null));
              }
              sourceError(fbPtr, tokLength, statemNum, cat(
                  "At proof step ",
                  str((double)(g_WrkProof.numSteps + 1)),", statement \"",
                  g_Statement[stmt].labelName,"\" requires ",
                  tmpStrPtr,".",null));
              let(&tmpStrPtr, "");
            }

            g_WrkProof.errorCount++;
            g_WrkProof.proofString[g_WrkProof.numSteps] = -(long)'?';

            g_WrkProof.RPNStack[g_WrkProof.RPNStackPtr] = g_WrkProof.numSteps;
            g_WrkProof.RPNStackPtr++;
            if (returnFlag < 3) returnFlag = 3;
            continue;
          }

          numReqHyp = g_Statement[stmt].numReqHyp;



          g_WrkProof.RPNStackPtr = g_WrkProof.RPNStackPtr - numReqHyp;
          g_WrkProof.RPNStack[g_WrkProof.RPNStackPtr] = g_WrkProof.numSteps;
          g_WrkProof.RPNStackPtr++;

        }

        g_WrkProof.numSteps++;
        labelMapIndex = 0;
        break;

      case 1:


        if (!labelMapIndex) {
          labelMapIndex = chrWeight[(long)(fbPtr[0])] + 1;
          labelStart = fbPtr;
        } else {
          labelMapIndex = labelMapIndex * digitsLen +
              chrWeight[(long)(fbPtr[0])] + 1;
          if (bggyAlgo) labelMapIndex--;
        }
        break;

      case 2:
        if (labelMapIndex) {
          if (!g_WrkProof.errorCount) {
            sourceError(fbPtr, 1, statemNum,
             "A compressed label character was expected here.");
          }
          g_WrkProof.errorCount++;
          if (returnFlag < 2) returnFlag = 2;
          labelMapIndex = 0;
        }


        g_WrkProof.compressedPfLabelMap[g_WrkProof.compressedPfNumLabels] =
          -1000 - (g_WrkProof.numSteps - 1);
        g_WrkProof.compressedPfNumLabels++;

        hypLocUnkFlag = 0;


        if (g_WrkProof.numSteps == 0) {
          if (!g_WrkProof.errorCount) {
            sourceError(fbPtr, 1, statemNum, cat(
              "A local label character must occur after a proof step.",null));
          }
          g_WrkProof.errorCount++;
          if (returnFlag < 2) returnFlag = 2;
          break;
        }

        stmt = g_WrkProof.proofString[g_WrkProof.numSteps - 1];
        if (stmt < 0) {
          hypLocUnkFlag = 1;
        } else {
          if (g_Statement[stmt].type != (char)a_ &&
              g_Statement[stmt].type != (char)p_) hypLocUnkFlag = 1;

        }
        if (hypLocUnkFlag) {
          if (!g_WrkProof.errorCount) {
            sourceError(fbPtr, 1, statemNum, cat(
                "The hypothesis or local label reference at proof step ",
                str((double)(g_WrkProof.numSteps)),
                " declares a local label.  Only \"$a\" and \"$p\" statement",
                " labels may have local label declarations.",null));
          }
          g_WrkProof.errorCount++;
          if (returnFlag < 2) returnFlag = 2;
        }

        break;

      case 3:
        break;

      case 4:

        if (fbPtr[1] == '.') {
          breakFlag = 1;
          break;
        }

        if (fbPtr[1] != '(' && fbPtr[1] != '!') {
          if (!g_WrkProof.errorCount) {
            sourceError(fbPtr + 1, 1, statemNum,
             "Expected \".\", \"(\", or \"!\" here.");
          }
          g_WrkProof.errorCount++;
          if (returnFlag < 2) returnFlag = 2;
        } else {
          fbPtr = fbPtr + whiteSpaceLen(fbPtr) - 1;
        }
        break;

      case 5:
        if (labelMapIndex) {
          if (!g_WrkProof.errorCount) {
            sourceError(fbPtr, 1, statemNum,
             "A compressed label character was expected here.");
          }
          g_WrkProof.errorCount++;
          if (returnFlag < 2) returnFlag = 2;
          labelMapIndex = 0;
        }


        g_WrkProof.stepSrcPtrNmbr[g_WrkProof.numSteps] = 1;
        g_WrkProof.stepSrcPtrPntr[g_WrkProof.numSteps] = fbPtr;

        g_WrkProof.proofString[g_WrkProof.numSteps] = -(long)'?';


        if (returnFlag < 1) returnFlag = 1;


        g_WrkProof.RPNStack[g_WrkProof.RPNStackPtr] = g_WrkProof.numSteps;
        g_WrkProof.RPNStackPtr++;

        g_WrkProof.numSteps++;
        break;

      case 6:
        if (!g_WrkProof.errorCount) {
          sourceError(fbPtr, 1, statemNum,
           "This character is not legal in a compressed proof.");
        }
        g_WrkProof.errorCount++;
        if (returnFlag < 2) returnFlag = 2;
        break;
      default:
        bug(1715);
        break;
    }

    if (breakFlag) break;
    fbPtr++;

  }

  if (labelMapIndex) {
    if (!g_WrkProof.errorCount) {
      sourceError(fbPtr, 1, statemNum,
       "A compressed label character was expected here.");
    }
    g_WrkProof.errorCount++;
    if (returnFlag < 2) returnFlag = 2;

  }


  if (g_WrkProof.numSteps == 0) {


    if (!g_WrkProof.errorCount) {
      sourceError(fbPtr, 2, statemNum,
          "The proof is empty.  If you don't know the proof, make it a \"?\".");
    }
    g_WrkProof.errorCount++;


    g_WrkProof.stepSrcPtrNmbr[g_WrkProof.numSteps] = 1;
    g_WrkProof.stepSrcPtrPntr[g_WrkProof.numSteps] = fbPtr;

    g_WrkProof.proofString[g_WrkProof.numSteps] = -(long)'?';


    if (returnFlag < 1) returnFlag = 1;


    g_WrkProof.RPNStack[g_WrkProof.RPNStackPtr] = g_WrkProof.numSteps;
    g_WrkProof.RPNStackPtr++;

    g_WrkProof.numSteps++;


  }

  g_WrkProof.proofString[g_WrkProof.numSteps] = -1;
  nmbrZapLen(g_WrkProof.proofString, g_WrkProof.numSteps);



  if (g_WrkProof.RPNStackPtr != 1) {
    tmpStrPtr = shortDumpRPNStack();
    fbPtr = g_WrkProof.stepSrcPtrPntr[g_WrkProof.numSteps - 1];
    if (!g_WrkProof.errorCount) {
      sourceError(fbPtr, proofTokenLen(fbPtr), statemNum,
          cat("After proof step ",
          str((double)(g_WrkProof.numSteps))," (the last step), the ",
          tmpStrPtr,".  It should contain exactly one entry.",null));
    }
    g_WrkProof.errorCount++;
   if (returnFlag < 3) returnFlag = 3;
  }

  g_WrkProof.errorSeverity = returnFlag;
  return (returnFlag);

}




nmbrString *getProof(long statemNum, flag printFlag) {
  nmbrString *proof = NULL_NMBRSTRING;
  parseProof(statemNum);
  if (g_WrkProof.errorSeverity > 1) {
    if (printFlag) mminou.print2(
         "The starting proof has a severe error.  It will not be used.\n");
    nmbrLet(&proof, nmbrAddElement(NULL_NMBRSTRING, -(long)'?'));
  } else {
    nmbrLet(&proof, g_WrkProof.proofString);
  }

  return proof;
}



void rawSourceError(char *startFile, char *ptr, long tokLen,
     vstring errMsg)
{
  char *startLine;
  char *endLine;
  vstring errLine = "";
  vstring errorMsg = "";


  vstring fileName = "";
  long lineNum;

  let(&errorMsg, errMsg);


  fileName = getFileAndLineNum(startFile, ptr, &lineNum);


  startLine = ptr;
  while (startLine[0] != '\n' && startLine > startFile) {
    startLine--;
  }
  if (startLine[0] == '\n'
      && startLine != ptr)
    startLine++;
  endLine = ptr;
  while (endLine[0] != '\n' && endLine[0] != 0) {
    endLine++;
  }
  endLine--;
  let(&errLine, space(endLine - startLine + 1));
  if (endLine - startLine + 1 < 0) bug(1721);
  memcpy(errLine, startLine, (size_t)(endLine - startLine) + 1);
  errorMessage(errLine, lineNum, ptr - startLine + 1, tokLen, errorMsg,
      fileName, 0, (char)error_);
  mminou.print2("\n");
  let(&errLine, "");
  let(&errorMsg, "");
  let(&fileName, "");
}

void sourceError(char *ptr, long tokLen, long stmtNum, vstring errMsg)
{
  char *startLine;
  char *endLine;
  vstring errLine = "";
  long lineNum;
  vstring fileName = "";
  vstring errorMsg = "";



  char *locSourcePtr;




  locSourcePtr = g_sourcePtr;


  let(&errorMsg, errMsg);

  if (!stmtNum) {
    lineNum = 0;
    C.go2("SKIP_LINE_NUM");
  }
  if (ptr < g_sourcePtr || ptr > g_sourcePtr + g_sourceLen) {

    if (g_Statement[stmtNum].labelSectionChanged == 1
         && ptr >= g_Statement[stmtNum].labelSectionPtr
         && ptr <= g_Statement[stmtNum].labelSectionPtr
             + g_Statement[stmtNum].labelSectionLen) {
      locSourcePtr = g_Statement[stmtNum].labelSectionPtr;

    } else if (g_Statement[stmtNum].mathSectionChanged == 1
         && ptr >= g_Statement[stmtNum].mathSectionPtr
         && ptr <= g_Statement[stmtNum].mathSectionPtr
             + g_Statement[stmtNum].mathSectionLen) {
      locSourcePtr = g_Statement[stmtNum].mathSectionPtr;

    } else if (g_Statement[stmtNum].proofSectionChanged == 1
         && ptr >= g_Statement[stmtNum].proofSectionPtr
         && ptr <= g_Statement[stmtNum].proofSectionPtr
             + g_Statement[stmtNum].proofSectionLen) {
      locSourcePtr = g_Statement[stmtNum].proofSectionPtr;

    } else {

      bug(1741);
    }

    lineNum = 0;
    C.go2("SKIP_LINE_NUM");
  }



  fileName = getFileAndLineNum(locSourcePtr, ptr, &lineNum);

  C.label("SKIP_LINE_NUM");

  if (lineNum != 0 && ptr > locSourcePtr) {
    startLine = ptr - 1;
  } else {

    startLine = ptr;
  }





  while (startLine[0] != '\n' && startLine > locSourcePtr) {

    startLine--;
  }


  if (startLine[0] == '\n') startLine++;


  endLine = ptr;
  while (endLine[0] != '\n' && endLine[0] != 0) {
    endLine++;
  }
  endLine--;


  let(&errLine, space(endLine - startLine + 1));
  memcpy(errLine, startLine, (size_t)(endLine - startLine) + 1);

  if (!lineNum) {

    errorMessage(errLine, lineNum, ptr - startLine + 1, tokLen, errorMsg,
        null, stmtNum, (char)error_);
  } else {
    errorMessage(errLine, lineNum,
        ptr - startLine + 1, tokLen,
        errorMsg,

        fileName,
        stmtNum,
        (char)error_ );
  }
  let(&errLine, "");
  let(&errorMsg, "");
  let(&fileName, "");
}


void mathTokenError(long tokenNum ,
    nmbrString *tokenList, long stmtNum, vstring errMsg)
{
  long i;
  char *fbPtr;
  fbPtr = g_Statement[stmtNum].mathSectionPtr;
  fbPtr = fbPtr + whiteSpaceLen(fbPtr);

  for (i = 0; i < tokenNum; i++) {
    fbPtr = fbPtr + g_MathToken[tokenList[i]].length;
    fbPtr = fbPtr + whiteSpaceLen(fbPtr);
  }
  sourceError(fbPtr, g_MathToken[tokenList[tokenNum]].length,
      stmtNum, errMsg);
}

vstring shortDumpRPNStack() {

  vstring tmpStr = "";
  vstring tmpStr2 = "";
  long i, k, m;

  for (i = 0; i < g_WrkProof.RPNStackPtr; i++) {
     k = g_WrkProof.RPNStack[i];
     let(&tmpStr,space(g_WrkProof.stepSrcPtrNmbr[k]));
     memcpy(tmpStr,g_WrkProof.stepSrcPtrPntr[k],
         (size_t)(g_WrkProof.stepSrcPtrNmbr[k]));
     let(&tmpStr2,cat(
         tmpStr2,", ","\"",tmpStr,"\" (step ", str((double)k + 1),")",null));
  }
  let(&tmpStr2,right(tmpStr2,3));
  if (g_WrkProof.RPNStackPtr == 2) {
    m = instr(1, tmpStr2, ",");
    let(&tmpStr2,cat(left(tmpStr2,m - 1)," and ",
        right(tmpStr2,m + 1),null));
  }
  if (g_WrkProof.RPNStackPtr > 2) {
    for (m = (long)strlen(tmpStr2); m > 0; m--) {
      if (tmpStr2[m - 1] == ',') break;
    }
    let(&tmpStr2,cat(left(tmpStr2,m - 1),", and ",
        right(tmpStr2,m + 1),null));
  }
  if (g_WrkProof.RPNStackPtr == 1) {
    let(&tmpStr2,cat("one entry, ",tmpStr2,null));
  } else {
    let(&tmpStr2,cat(str((double)(g_WrkProof.RPNStackPtr))," entries: ",tmpStr2,null));
  }
  let(&tmpStr2,cat("RPN stack contains ",tmpStr2,null));
  if (g_WrkProof.RPNStackPtr == 0) let(&tmpStr2,"RPN stack is empty");
  let(&tmpStr, "");
  return(tmpStr2);
}



long lookupLabel(vstring label)
{
  void *voidPtr;
  long statemNum;

  voidPtr = (void *)bsearch(label, g_labelKeyBase, (size_t)g_numLabelKeys,
      sizeof(long), labelSrchCmp);
  if (!voidPtr) {
    return (-1);
  }
  statemNum = (*(long *)voidPtr);
  if (g_Statement[statemNum].type != a_ && g_Statement[statemNum].type != p_)
      bug(1718);
  return (statemNum);
}



int labelSortCmp(const void *key1, const void *key2)
{

  return (strcmp(g_Statement[ *((long *)key1) ].labelName,
      g_Statement[ *((long *)key2) ].labelName));
}



int labelSrchCmp(const void *key, const void *data)
{

  return (strcmp(key,
      g_Statement[ *((long *)data) ].labelName));
}



int mathSortCmp(const void *key1, const void *key2)
{

  return (strcmp(g_MathToken[ *((long *)key1) ].tokenName,
      g_MathToken[ *((long *)key2) ].tokenName));
}




int mathSrchCmp(const void *key, const void *data)
{

  return (strcmp(key, g_MathToken[ *((long *)data) ].tokenName));
}



int hypAndLocSortCmp(const void *key1, const void *key2)
{

  return (strcmp( ((struct sortHypAndLoc *)key1)->labelName,
      ((struct sortHypAndLoc *)key2)->labelName));
}




int hypAndLocSrchCmp(const void *key, const void *data)
{

  return (strcmp(key, ((struct sortHypAndLoc *)data)->labelName));
}


long whiteSpaceLen(char *ptr)
{
  long i = 0;
  char tmpchr;
  char *ptr1;
  while (true) {
    tmpchr = ptr[i];
    if (!tmpchr) return (i);
    if (tmpchr == '$') {
      if (ptr[i + 1] == '(') {
        while (true) {


          for (ptr1 = ptr + i + 2; ptr1[0] != '$'; ptr1++) {
            if (ptr1[0] == 0) {
              if ('$' != 0)
                ptr1 = null;
              break;
            }
          }

          if (!ptr1) {
            return(i + (long)strlen(&ptr[i]));
          }
          if (ptr1[1] == ')') break;
          i = ptr1 - ptr;
        }
        i = ptr1 - ptr + 2;
        continue;
      } else {
        if (ptr[i + 1] == '!') {
          ptr1 = strchr(ptr + i + 2, '\n');
          if (!ptr1) bug(1716);
          i = ptr1 - ptr + 1;
          continue;
        }
        return(i);
      }
    }
    if (isgraph((unsigned char)tmpchr)) return (i);
    i++;
  }
  return(0);
}



long rawWhiteSpaceLen(char *ptr)
{
  long i = 0;
  char tmpchr;
  while (true) {
    tmpchr = ptr[i];
    if (!tmpchr) return (i);
    if (isgraph((unsigned char)tmpchr)) return (i);
    i++;
  }
  return(0);
}


long tokenLen(char *ptr)
{
  long i = 0;
  char tmpchr;
  while (true) {
    tmpchr = ptr[i];
    if (tmpchr == '$') {
      if (ptr[i + 1] == '$') {
        i = i + 2;
        continue;
      } else {

        if (ptr[i + 1] >= '0' && ptr[i + 1] <= '9') {
          i = i + 2;
          continue;
        } else {
          return(i);
        }
      }
    }
    if (!isgraph((unsigned char)tmpchr)) return (i);
    i++;
  }
  return(0);
}


long rawTokenLen(char *ptr)
{
  long i = 0;
  char tmpchr;
  while (true) {
    tmpchr = ptr[i];
    if (!isgraph((unsigned char)tmpchr)) return (i);
    i++;
  }
  return(0);
}


long proofTokenLen(char *ptr)
{
  long i = 0;
  char tmpchr;
  if (ptr[0] == ':') return (1);
  if (ptr[0] == '?') return (1);
  if (ptr[0] == '(') return (1);
  if (ptr[0] == ')') return (1);

  if (ptr[0] == '=') return (1);
  while (true) {
    tmpchr = ptr[i];
    if (tmpchr == '$') {
      if (ptr[i + 1] == '$') {
        i = i + 2;
        continue;
      } else {
        return(i);
      }
    }
    if (!isgraph((unsigned char)tmpchr)) return (i);
    if (tmpchr == ':') return(i);
    if (tmpchr == '?') return(i);
    if (tmpchr == '(') return(i);
    if (tmpchr == ')') return(i);
    if (tmpchr == '=') return(i);
    i++;
  }
  return(0);
}



long countLines(vstring start, long length) {
  long lines, i;
  lines = 0;
  if (length == -1) {
    i = 0;
    while (true) {
      if (start[i] == '\n') lines++;
      if (start[i] == 0) break;
      i++;
    }
  } else {
    for (i = 0; i < length; i++) {
      if (start[i] == '\n') lines++;
      if (start[i] == 0) break;
    }
  }
  return lines;
}




vstring outputStatement(long stmt,
    flag reformatFlag)
{
  vstring labelSection = "";
  vstring mathSection = "";
  vstring proofSection = "";
  vstring labelSectionSave = "";
  vstring mathSectionSave = "";
  vstring proofSectionSave = "";
  vstring output = "";


  long slen;
  long pos;
  long indent;
  static long dollarDpos = 0;
  static char previousType = illegal_;
  long commentStart;
  long commentEnd;
  vstring comment = "";
  vstring str1 = "";
  long length;
  flag nowrapHtml;




  if (stmt == 1) {
    previousType = illegal_;
    dollarDpos = 0;
  }

  let(&labelSection, space(g_Statement[stmt].labelSectionLen));
  memcpy(labelSection, g_Statement[stmt].labelSectionPtr,
      (size_t)(g_Statement[stmt].labelSectionLen));

  if (stmt == g_statements + 1) return labelSection;


  let(&mathSection, space(g_Statement[stmt].mathSectionLen));
  memcpy(mathSection, g_Statement[stmt].mathSectionPtr,
      (size_t)(g_Statement[stmt].mathSectionLen));

  let(&proofSection, space(g_Statement[stmt].proofSectionLen));
  memcpy(proofSection, g_Statement[stmt].proofSectionPtr,
      (size_t)(g_Statement[stmt].proofSectionLen));


  let(&labelSectionSave, labelSection);
  let(&mathSectionSave, mathSection);
  let(&proofSectionSave, proofSection);


  if (reformatFlag > 0) {

    static final long INDENT_FIRST =D.INDENT_FIRST;
    static final long  INDENT_INCR =D.INDENT_INCR2;
    indent = INDENT_FIRST + (INDENT_INCR * g_Statement[stmt].scope);

    if (g_Statement[stmt].type == rb_) indent = indent - INDENT_INCR;



    if (strchr(labelSection, '\t') != null) {
      let(&labelSection, edit(labelSection, 2048));
    }

    if (strchr(mathSection, '\t') != null) {
      let(&mathSection, edit(mathSection, 2048));
    }




    while (true) {
      pos = instr(1, labelSection, " \n");
      if (pos == 0) break;
      let(&labelSection, cat(left(labelSection, pos - 1),
          right(labelSection, pos + 1), null));
    }


    while (true) {

      pos = instr(1, labelSection, "\n\n\n\n");

      if (pos == 0) break;
      let(&labelSection, cat(left(labelSection, pos - 1),
          right(labelSection, pos + 1), null));
    }

    switch (g_Statement[stmt].type) {
      case lb_:
      case rb_:
      case v_:
      case c_:
      case d_:



        let(&labelSection, edit(labelSection, 128 ));
        slen = (long)strlen(labelSection);


        if (slen != 0 && labelSection[slen - 1] != '\n') {
          let(&labelSection, cat(labelSection, "\n", null));
          slen++;
        }

        if (g_Statement[stmt].type == lb_
            && previousType == rb_) {
          if (slen == 0) {
            let(&labelSection, "\n\n");
            slen = 2;
          } else {

            if (instr(1, labelSection, "\n\n") == 0) {
              let(&labelSection, cat(labelSection, "\n", null));
              slen++;
            }
          }
        }
        if (slen == 0) {


          let(&labelSection, cat(labelSection, "  ", null));
          slen = 2;
        } else {

          let(&labelSection, cat(labelSection, space(indent), null));
          slen = slen + indent;
        }
        if (g_Statement[stmt].type == d_) {
          let(&mathSection, edit(mathSection,
               + 16));
          if (strlen(edit(labelSection, 4 + 2))
              == 0)
              {
            if (previousType == d_) {

              if (dollarDpos + 2 + (signed)(strlen(mathSection)) + 4
                  <= g_screenWidth) {
                let(&labelSection, "  ");
                dollarDpos = dollarDpos + 2 + (long)strlen(mathSection) + 4;
              } else {


                dollarDpos = indent + (long)strlen(mathSection) + 4;

                let(&labelSection, cat("\n", space(indent), null));
              }
            } else {
              dollarDpos = indent + (long)strlen(mathSection) + 4;
            }
          } else {
            dollarDpos = indent + (long)strlen(mathSection) + 4;
          }
        }


        break;
      case a_:
      case p_:

        commentStart = rinstr(labelSection,  "$(");

        commentEnd = rinstr(labelSection, "$)") + 1;
        if (commentEnd < commentStart) {
          mminou.print2("?Make sure syntax passes before running / REWRAP.\n");
          mminou.print2("(Forcing a bug check since output may be corrupted.)\n");
          bug(1725);
        }
        if (commentStart != 0) {
          let(&comment, seg(labelSection, commentStart, commentEnd));
        } else {

          let(&comment, "$( PLEASE PUT DESCRIPTION HERE. $)");
        }



        let(&labelSection, left(labelSection, commentStart - 1));

        pos = rinstr(labelSection, "\n");



        if (previousType == e_ && pos == 2 && labelSection[0] == '\n') {
          let(&labelSection, right(labelSection, 2));
          pos = 1;
        }


        if (pos == 0 && stmt > 1) {
          let(&labelSection, cat(edit(labelSection, 128 ),
              "\n", null));
          pos = (long)strlen(labelSection) + 1;
        }



        if (instr(1, comment, "<HTML>") != 0) {
          nowrapHtml = 1;
        } else {
          nowrapHtml = 0;
        }


        if (nowrapHtml == 0) {


          let(&labelSection, left(labelSection, pos));

          if (reformatFlag == 2) {

            let(&str1, "");
            str1 = rewrapComment(comment);
            let(&comment, str1);
          }

          pos = 0;
          while (true) {
            pos = instr(pos + 1, comment, "\n");
            if (pos == 0) break;

            length = 0;
            while (true) {
              if (comment[pos + length] != ' ') break;
              length++;
            }

            let(&comment, cat(left(comment, pos),
                (comment[pos + length] != '\n')
                    ? space(indent + 3)
                    : "",
                right(comment, pos + length + 1), null));
          }


          if (g_outputToString == 1) bug(1726);
          g_outputToString = 1;
          let(&g_printString, "");
          printLongLine(cat(space(indent), comment, null),
              space(indent + 3), " ");
          let(&comment, g_printString);
          let(&g_printString, "");
          g_outputToString = 0;
          static final long ASCII_4 =D.ASCII_4;

          length = (long)strlen(comment);
          for (pos = 2; pos < length - 2; pos++) {


             if (comment[pos] == ASCII_4) comment[pos] = ' ';
          }
        }  else {


          let(&comment, cat(comment, "\n", null));
        }


        pos = 2;
        while(true) {
          pos = instr(pos + 1, comment, " \n");
          if (!pos) break;
          let(&comment, cat(left(comment, pos - 1), right(comment, pos + 1),
              null));
          pos = pos - 2;
        }


        let(&labelSection, cat(labelSection, comment,
            space(indent), g_Statement[stmt].labelName, " ", null));
        break;
      case e_:
      case f_:
        pos = rinstr(labelSection, g_Statement[stmt].labelName);
        let(&labelSection, left(labelSection, pos - 1));
        pos = rinstr(labelSection, "\n");

        if (pos == 0 && stmt > 1) {
          let(&labelSection, cat(edit(labelSection, 128 ),
              "\n", null));
          pos = (long)strlen(labelSection) + 1;
        }
        let(&labelSection, left(labelSection, pos));
        if ((previousType == d_
              || previousType == e_)
            && instr(1, labelSection, "$(") == 0) {
          let(&labelSection, "\n");
        }

        let(&labelSection, cat(labelSection,
            space(indent), g_Statement[stmt].labelName, " ", null));
        break;
      default: bug(1727);
    }


    switch (g_Statement[stmt].type) {
      case lb_:
      case rb_:
      case v_:
      case c_:
      case d_:
      case a_:
      case p_:
      case e_:
      case f_:

        while (true) {
          pos = instr(1, mathSection, "\n\n");
          if (pos == 0) break;
          let(&mathSection, cat(left(mathSection, pos),
              right(mathSection, pos + 2), null));
        }



        pos = 0;
        while(true) {
          pos = instr(pos + 1, mathSection, "  ");
          if (pos == 0) break;
          if (pos > 1) {
            if (mathSection[pos - 2] != '\n' && mathSection[pos - 2] != ' ') {

              let(&mathSection, cat(left(mathSection, pos),
                  right(mathSection, pos + 2), null));
              pos--;
            }
          }
        }


        break;
      default: bug(1729);
    }


    if (g_Statement[stmt].type == d_) {

    } else {
      dollarDpos = 0;
    }
    previousType = g_Statement[stmt].type;

  }

  let(&output, labelSection);


  let(&output, cat(output, "$", chr(g_Statement[stmt].type), null));


  if (g_Statement[stmt].mathSectionLen != 0) {
    let(&output, cat(output, mathSection, null));


    if (g_Statement[stmt].type == (char)p_) {
      let(&output, cat(output, "$=", proofSection, null));



    }
    let(&output, cat(output, "$.", null));

  }



  if (strchr(output, '\r') != null) {



    bug(1758);

    let(&output, edit(output, 8192));
  }


  if (strcmp(labelSection, labelSectionSave)) {
    g_Statement[stmt].labelSectionLen = (long)strlen(labelSection);
    if (g_Statement[stmt].labelSectionChanged == 1) {
      let(&(g_Statement[stmt].labelSectionPtr), labelSection);
    } else {

      g_Statement[stmt].labelSectionChanged = 1;
      g_Statement[stmt].labelSectionPtr = labelSection;
      labelSection = "";
    }
  }
  if (strcmp(mathSection, mathSectionSave)) {
    g_Statement[stmt].mathSectionLen = (long)strlen(mathSection);
    if (g_Statement[stmt].mathSectionChanged == 1) {
      let(&(g_Statement[stmt].mathSectionPtr), mathSection);
    } else {

      g_Statement[stmt].mathSectionChanged = 1;
      g_Statement[stmt].mathSectionPtr = mathSection;
      mathSection = "";
    }
  }
  if (strcmp(proofSection, proofSectionSave)) {
    bug(1757);
    g_Statement[stmt].proofSectionLen = (long)strlen(proofSection);
    if (g_Statement[stmt].proofSectionChanged == 1) {
      let(&(g_Statement[stmt].proofSectionPtr), proofSection);
    } else {

      g_Statement[stmt].proofSectionChanged = 1;
      g_Statement[stmt].proofSectionPtr = proofSection;
      proofSection = "";
    }
  }

  let(&labelSection, "");
  let(&mathSection, "");
  let(&proofSection, "");
  let(&labelSectionSave, "");
  let(&mathSectionSave, "");
  let(&proofSectionSave, "");
  let(&comment, "");
  let(&str1, "");
  return output;
}


vstring rewrapComment(vstring comment1)
{

  static final String OPENING_PUNCTUATION=D.OPENING_PUNCTUATION2;

  static final String CLOSING_PUNCTUATION=D.CLOSING_PUNCTUATION2;
  static final String SENTENCE_END_PUNCTUATION=D.SENTENCE_END_PUNCTUATION;
  vstring comment = "";
  vstring commentTemplate = "";
  long length, pos, i, j;
  vstring ch;
  flag mathmode = 0;

  let(&comment, comment1);






  pos = 2;
  mathmode = 0;
  while (true) {
    pos = instr(pos + 1, comment, "`");
    if (pos == 0) break;
    mathmode = (flag)(1 - mathmode);
    if (comment[pos - 2] == '`' || comment[pos] == '`') continue;

    if (comment[pos] != ' ' && comment[pos] != '\n') {
      if (mathmode == 1 || (comment[pos] != '_' && comment[pos] != '-')) {

        let(&comment, cat(left(comment, pos), " ",
            right(comment, pos + 1), null));
      }
    }
    if (comment[pos - 2] != ' ') {

      let(&comment, cat(left(comment, pos - 1), " ",
          right(comment, pos), null));
      pos++;
    }
  }


  if (instr(2, comment, "`") == 0) {
    pos = 2;
    while (true) {
      pos = instr(pos + 1, comment, "~");
      if (pos == 0) break;
      if (comment[pos - 2] == '~' || comment[pos] == '~') continue;

      if (comment[pos] != ' ') {

        let(&comment, cat(left(comment, pos), " ",
            right(comment, pos + 1), null));
      }
      if (comment[pos - 2] != ' ') {

        let(&comment, cat(left(comment, pos - 1), " ",
            right(comment, pos), null));
        pos++;
      }
    }
  }


  length = (long)strlen(comment);
  for (pos = 2; pos < length - 2; pos++) {
    if (comment[pos] == '\n' && comment[pos - 1] != '\n'
        && comment[pos + 1] != '\n')
      comment[pos] = ' ';
  }
  let(&comment, edit(comment, 16 ));


  while (true) {
    length = (long)strlen(comment);
    if (comment[length - 3] != ' ') bug(1730);

    if (comment[length - 4] != ' ' && comment[length - 4] != '\n') break;
    let(&comment, cat(left(comment, length - 4),
        right(comment, length - 2), null));
  }


  length = (long)strlen(comment);
  if (islower((unsigned char)(comment[length - 4]))) {
    let(&comment, cat(left(comment, length - 3), ". $)", null));
  }

  mathmode = 0;
  for (pos = 3; pos < length - 2; pos++) {
    if (comment[pos] == '`') {
      mathmode = (char)(1 - mathmode);
    }
    if ( mathmode == 1 && comment[pos] == ' ')
      comment[pos] = ASCII_4;
  }


  if (g_proofDiscouragedMarkup[0] == 0) {
    let(&g_proofDiscouragedMarkup, PROOF_DISCOURAGED_MARKUP);
    let(&g_usageDiscouragedMarkup, USAGE_DISCOURAGED_MARKUP);
  }
  pos = instr(1, comment, g_proofDiscouragedMarkup);
  if (pos != 0) {
    i = (long)strlen(g_proofDiscouragedMarkup);
    for (j = pos; j < pos + i - 1; j++) {
      if (comment[j] == ' ') {
        comment[j] = ASCII_4;
      }
    }
  }
  pos = instr(1, comment, g_usageDiscouragedMarkup);
  if (pos != 0) {
    i = (long)strlen(g_usageDiscouragedMarkup);
    for (j = pos; j < pos + i - 1; j++) {
      if (comment[j] == ' ') {
        comment[j] = ASCII_4;
      }
    }
  }



  ch = "";
  for (i = 0; i < 4; i++) {
    switch (i) {
      case 0: ch = "."; break;
      case 1: ch = "?"; break;
      case 2: ch = "!"; break;
      case 3: ch = ":";
    }
    pos = 2;
    while (true) {
      pos = instr(pos + 1, comment, ch);
      if (pos == 0) break;
      if (ch[0] == '.' && comment[pos - 2] >= 'A' && comment[pos - 2] <= 'Z')
        continue;
      if (strchr(SENTENCE_END_PUNCTUATION, comment[pos]) != null)
        pos++;
      if (comment[pos] != ' ') continue;
      if ((comment[pos + 1] >= 'A' && comment[pos + 1] <= 'Z')
          || strchr(OPENING_PUNCTUATION, comment[pos + 1]) != null) {
        let(&comment, cat(left(comment, pos + 1), " ",
            right(comment, pos + 2), null));
      }
    }
  }

  length = (long)strlen(comment);
  let(&commentTemplate, space(length));
  for (pos = 3; pos < length - 2; pos++) {
    if (comment[pos] == ' ') {
      if (comment[pos - 1] == '~' && comment[pos - 2] != '~') {

        commentTemplate[pos] = ASCII_4;
      } else if ((comment[pos - 2] == ' '
            || strchr(OPENING_PUNCTUATION, comment[pos - 2]) != null)
          && strchr(OPENING_PUNCTUATION, comment[pos - 1]) != null) {

        commentTemplate[pos] = ASCII_4;
      } else if ((comment[pos + 2] == ' '
            || comment[pos + 2] == '\n'
            || comment[pos + 2] == ASCII_4
            || strchr(CLOSING_PUNCTUATION, comment[pos + 2]) != null)
          && strchr(CLOSING_PUNCTUATION, comment[pos + 1]) != null) {

        commentTemplate[pos] = ASCII_4;
      } else if (comment[pos - 3] == ' ' && comment[pos - 2] == 'p'
          && comment[pos - 1] == '.') {

        commentTemplate[pos] = ASCII_4;
      }
    }
  }
  commentTemplate[length - 3] = ASCII_4;

  for (pos = 3; pos < length - 2; pos++) {

    if (commentTemplate[pos] == ASCII_4) comment[pos] = ASCII_4;
  }

  let(&commentTemplate, "");

  return(comment);
}

nmbrString *parseMathTokens(vstring userText, long statemNum)
{
  long i, j;
  char *fbPtr;
  long mathStringLen;
  long tokenNum;
  long lowerKey, upperKey;
  long symbolLen, origSymbolLen, g_mathKeyNum;
  void *g_mathKeyPtr;
  int maxScope;
  flag errorFlag = 0;
  int errCount = 0;
  vstring tmpStr = "";
  vstring nlUserText = "";


  long *mathTokenSameAs;
  long *reverseMathKey;



  long wrkLen;
  nmbrString *wrkNmbrPtr;
  char *wrkStrPtr;


  nmbrString *mathString = NULL_NMBRSTRING;

  long maxSymbolLen;
  flag *symbolLenExists;

  long nmbrSaveTempAllocStack;
  long saveTempAllocStack;
  nmbrSaveTempAllocStack = g_nmbrStartTempAllocStack;
  g_nmbrStartTempAllocStack = g_nmbrTempAllocStackTop;
  saveTempAllocStack = g_startTempAllocStack;
  g_startTempAllocStack = g_tempAllocStackTop;

  tokenNum = 0;


  let(&nlUserText, cat("\n", userText, null));


  if (!g_mathTokens) bug(1717);

  for (i = 0; i < g_mathTokens; i++) {
    if (g_MathToken[i].statement <= statemNum && g_MathToken[i].endStatement >=
        statemNum) {
      g_MathToken[i].active = 1;
    } else {
      g_MathToken[i].active = 0;
    }
  }

  mathTokenSameAs = malloc((size_t)g_mathTokens * sizeof(long));
  if (!mathTokenSameAs) outOfMemory("#12 (mathTokenSameAs)");
  reverseMathKey = malloc((size_t)g_mathTokens * sizeof(long));
  if (!reverseMathKey) outOfMemory("#13 (reverseMathKey)");
  for (i = 0; i < g_mathTokens; i++) {
    mathTokenSameAs[i] = 0;
    reverseMathKey[g_mathKey[i]] = i;
  }
  for (i = 1; i < g_mathTokens; i++) {
    if (!strcmp(g_MathToken[g_mathKey[i]].tokenName,
        g_MathToken[g_mathKey[i - 1]].tokenName)) {
      if (!mathTokenSameAs[i - 1]) mathTokenSameAs[i - 1] = i;
      mathTokenSameAs[i] = mathTokenSameAs[i - 1];
    }
  }



  wrkLen = (long)strlen(userText);
  wrkNmbrPtr = malloc((size_t)wrkLen * sizeof(nmbrString));
  if (!wrkNmbrPtr) outOfMemory("#22 (wrkNmbrPtr)");
  wrkStrPtr = malloc((size_t)wrkLen + 1);
  if (!wrkStrPtr) outOfMemory("#23 (wrkStrPtr)");


  maxSymbolLen = 0;
  for (i = 0; i < g_mathTokens; i++) {
    if (g_MathToken[i].length > maxSymbolLen) {
      maxSymbolLen = g_MathToken[i].length;
    }
  }
  symbolLenExists = malloc(((size_t)maxSymbolLen + 1) * sizeof(flag));
  if (!symbolLenExists) outOfMemory("#25 (symbolLenExists)");
  for (i = 0; i <= maxSymbolLen; i++) {
    symbolLenExists[i] = 0;
  }
  for (i = 0; i < g_mathTokens; i++) {
    symbolLenExists[g_MathToken[i].length] = 1;
  }


  g_currentScope = g_Statement[statemNum].scope;




        mathStringLen = 0;
        fbPtr = nlUserText;
        while (true) {
          fbPtr = fbPtr + whiteSpaceLen(fbPtr);
          origSymbolLen = tokenLen(fbPtr);
          if (!origSymbolLen) break;


          C.label("nextAdjToken");

          if (origSymbolLen > maxSymbolLen) {
            symbolLen = maxSymbolLen;
          } else {
            symbolLen = origSymbolLen;
          }
          memcpy(wrkStrPtr, fbPtr, (size_t)symbolLen);
          for (; symbolLen > 0; symbolLen--) {

            if (!symbolLenExists[symbolLen]) continue;
            wrkStrPtr[symbolLen] = 0;
            g_mathKeyPtr = (void *)bsearch(wrkStrPtr, g_mathKey,
                (size_t)g_mathTokens, sizeof(long), mathSrchCmp);
            if (!g_mathKeyPtr) continue;
            g_mathKeyNum = (long *)g_mathKeyPtr - g_mathKey;
            if (mathTokenSameAs[g_mathKeyNum]) {
              lowerKey = g_mathKeyNum;
              upperKey = lowerKey;
              j = mathTokenSameAs[lowerKey];
              while (lowerKey) {
                if (j != mathTokenSameAs[lowerKey - 1]) break;
                lowerKey--;
              }
              while (upperKey < g_mathTokens - 1) {
                if (j != mathTokenSameAs[upperKey + 1]) break;
                upperKey++;
              }

              maxScope = -1;
              for (i = lowerKey; i <= upperKey; i++) {
                j = g_mathKey[i];
                if (g_MathToken[j].active) {
                  if (g_MathToken[j].scope > maxScope) {
                    tokenNum = j;
                    maxScope = g_MathToken[j].scope;
                    if (maxScope == g_currentScope) break;
                  }
                }
              }
              if (maxScope == -1) {
                tokenNum = g_mathKey[g_mathKeyNum];
                errCount++;
                if (errCount <= 1) {
                  sourceError(fbPtr, symbolLen,  0,
       "This math symbol is not active (i.e. was not declared in this scope).");
                }
                errorFlag = 1;
              }
            } else {
              tokenNum = *((long *)g_mathKeyPtr);

              if (!g_MathToken[tokenNum].active) {
                errCount++;
                if (errCount <= 1) {
                  sourceError(fbPtr, symbolLen,  0,
       "This math symbol is not active (i.e. was not declared in this scope).");
                }
                errorFlag = 1;
              }
            }
            break;
          }

          if (symbolLen == 0) {

            if (fbPtr[0] == '$') {
              symbolLen = tokenLen(fbPtr);
              for (i = 1; i < symbolLen; i++) {
                if (fbPtr[i] < '0' || fbPtr[i] > '9') break;
              }
              symbolLen = i;
              if (symbolLen == 1) {
                symbolLen = 0;
              } else {
                memcpy(wrkStrPtr, fbPtr + 1, (size_t)i - 1);
                wrkStrPtr[i - 1] = 0;
                tokenNum = (long)(val(wrkStrPtr)) + g_mathTokens;

                if (tokenNum > g_pipDummyVars + g_mathTokens) {
                  declareDummyVars(tokenNum - g_pipDummyVars - g_mathTokens);
                }
              }
            }
         }


          if (symbolLen == 0) {
            symbolLen = tokenLen(fbPtr);
            errCount++;
            if (errCount <= 1) {
              sourceError(fbPtr, symbolLen,  0,
      "This math symbol was not declared (with a \"$c\" or \"$v\" statement).");
            }
            errorFlag = 1;
          }


          if (!errorFlag) {
            wrkNmbrPtr[mathStringLen] = tokenNum;
            mathStringLen++;
          } else {
            errorFlag = 0;
          }
          fbPtr = fbPtr + symbolLen;

          if (symbolLen < origSymbolLen) {


            origSymbolLen = origSymbolLen - symbolLen;
            C.go2("nextAdjToken");
          }
        }



        nmbrLet(&mathString, nmbrSpace(mathStringLen));
        for (i = 0; i < mathStringLen; i++) {
          mathString[i] = wrkNmbrPtr[i];
        }



  g_startTempAllocStack = saveTempAllocStack;
  g_nmbrStartTempAllocStack = nmbrSaveTempAllocStack;
  if (mathStringLen) nmbrMakeTempAlloc(mathString);


  free(mathTokenSameAs);
  free(reverseMathKey);
  free(wrkNmbrPtr);
  free(wrkStrPtr);
  free(symbolLenExists);
  let(&tmpStr, "");
  let(&nlUserText, "");

  return (mathString);

}




void getNextInclusion(char *fileBuf, long startOffset,

    long *cmdPos1, long *cmdPos2,
    long *endPos1, long *endPos2,
    char *cmdType,
    vstring *fileName
    )
{

  char *fbPtr;
  char *tmpPtr;
  flag lookForEndMode = 0;
  long i, j;

  fbPtr = fileBuf + startOffset;

  while (true) {
    fbPtr = fbPtr + rawWhiteSpaceLen(fbPtr);
    j = rawTokenLen(fbPtr);
    if (j == 0) {
      *cmdType = 'N';
      break;
    }
    if (fbPtr[0] != '$') {
      fbPtr = fbPtr + j;
      continue;
    }


    if (fbPtr[1] == '[') {
      if (lookForEndMode == 0) {
        *cmdPos1 = fbPtr - fileBuf + 1;
        fbPtr = fbPtr + j;
        fbPtr = fbPtr + whiteSpaceLen(fbPtr);
        j = rawTokenLen(fbPtr);
        let(&(*fileName), left(fbPtr, j));
        fbPtr = fbPtr + j;
        fbPtr = fbPtr + whiteSpaceLen(fbPtr);
        j = rawTokenLen(fbPtr);
        if (j == 2 && !strncmp("$]", fbPtr, (size_t)j)) {
          *cmdPos2 = fbPtr - fileBuf + j + 1;
          *endPos1 = 0;
          *endPos2 = 0;
          *cmdType = 'I';
          return;
        }

        mminou.print2("?Missing \"$]\" after \"$[ %s\"\n", *fileName);
        fbPtr = fbPtr + j;
        continue;
      }
      fbPtr = fbPtr + j;
      continue;

    } else if (fbPtr[1] == '(') {

      if (lookForEndMode == 0) {
        *cmdPos1 = fbPtr - fileBuf + 1;
      } else {
        *endPos1 = fbPtr - fileBuf + 1;
      }
      fbPtr = fbPtr + j;
      fbPtr = fbPtr + rawWhiteSpaceLen(fbPtr);
      j = rawTokenLen(fbPtr);
      *cmdType = '?';
      if (j == 5 && !strncmp("Begin", fbPtr, (size_t)j)) {

        if (lookForEndMode == 0) {
          *cmdType = 'B';
        }
      } else if (j == 4 && !strncmp("Skip", fbPtr, (size_t)j)) {

        if (lookForEndMode == 0) {
          *cmdType = 'S';
        }
      } else if (j == 3 && !strncmp("End", fbPtr, (size_t)j)) {

        if (lookForEndMode == 1) {
          *cmdType = 'E';
        }
      }
      if (*cmdType == '?') {

        C.go2("GET_PASSED_END_OF_COMMENT");
      } else {

        fbPtr = fbPtr + j;
        fbPtr = fbPtr + rawWhiteSpaceLen(fbPtr);
        j = rawTokenLen(fbPtr);
        if (j != 2 || strncmp("$[", fbPtr, (size_t)j)) {

          C.go2("GET_PASSED_END_OF_COMMENT");
        }
        fbPtr = fbPtr + j;
        fbPtr = fbPtr + rawWhiteSpaceLen(fbPtr);
        j = rawTokenLen(fbPtr);
        if (lookForEndMode == 0) {

          let(&(*fileName), left(fbPtr, j));
        } else {

          if (strncmp(*fileName, fbPtr, (size_t)j)) {


            C.go2("GET_PASSED_END_OF_COMMENT");
          }
        }
        fbPtr = fbPtr + j;
        fbPtr = fbPtr + rawWhiteSpaceLen(fbPtr);
        j = rawTokenLen(fbPtr);
        if (j != 2 || strncmp("$]", fbPtr, (size_t)j)) {


          C.go2("GET_PASSED_END_OF_COMMENT");
        }
        fbPtr = fbPtr + j;
        fbPtr = fbPtr + rawWhiteSpaceLen(fbPtr);
        j = rawTokenLen(fbPtr);
        if (j != 2 || strncmp("$)", fbPtr, (size_t)j)) {


          C.go2("GET_PASSED_END_OF_COMMENT");
        }

        fbPtr = fbPtr + j;
        if (lookForEndMode == 0) {
          *cmdPos2 = fbPtr - fileBuf + 1
            + ((*cmdType == 'B') ? 1 : 0);
          if (*cmdType == 'S') {
            *endPos1 = 0;
            *endPos2 = 0;
            return;
          }
          if (*cmdType != 'B') bug(1742);
          lookForEndMode = 1;
        } else {
          if (*cmdType != 'E') bug(1743);

          *cmdType = 'B';
          *endPos2 = fbPtr - fileBuf + 1;
          return;
        }
        continue;
      }
    } else if (i != i + 1) {

      fbPtr = fbPtr + j;
      continue;
    }
    bug(1746);
    C.label("GET_PASSED_END_OF_COMMENT");



    tmpPtr = fbPtr;
    i = 0;
    while (true) {



      while (tmpPtr[0] != '$') {
        if (tmpPtr[0] == 0) break;
        tmpPtr++;
      }
      if (tmpPtr[0] == 0) {
        i = 0;
        break;
      }
      if (tmpPtr[1] == ')') {
        i = tmpPtr - fbPtr + 1;
        break;
      }
      tmpPtr++;

    }

    if (i == 0) {

      printf("?End of comment not found\n");
      i = (long)strlen(fileBuf);
      fbPtr = fileBuf + i;
    } else {
      fbPtr = fbPtr + i + 2 - 1;
    }

  }
  if (j != 0) bug(1744);
  if (lookForEndMode == 1) {

    *cmdType = 'E';
    *endPos1 = 0; *endPos2 = 0;
  } else {
    *cmdType = 'N';
    *cmdPos1 = 0; *cmdPos2 = 0; *endPos1 = 0; *endPos2 = 0;
    let(&(*fileName), "");
  }
  return;

}




vstring writeSourceToBuffer()
{
  long stmt, size;
  vstring buf = "";
  char *ptr;


  size = 0;
  for (stmt = 1; stmt <= g_statements + 1; stmt++) {
    size += g_Statement[stmt].labelSectionLen
        + g_Statement[stmt].mathSectionLen
        + g_Statement[stmt].proofSectionLen;
    switch (g_Statement[stmt].type) {
      case lb_:
      case rb_:
        size += 2;
        break;
      case v_:
      case c_:
      case d_:
      case e_:
      case f_:
      case a_:
        size += 4;
        break;
      case p_:
        size += 6;
        break;
      case illegal_:
        if (stmt != g_statements + 1) bug(1747);

        size += 0;
        break;
      default: bug(1748);
    }
  }


  buf = malloc((size_t)(size + 1) * sizeof(char));

  ptr = buf;

  for (stmt = 1; stmt <= g_statements + 1; stmt++) {

    memcpy(ptr, g_Statement[stmt].labelSectionPtr,
        (size_t)(g_Statement[stmt].labelSectionLen));
    ptr += g_Statement[stmt].labelSectionLen;
    switch (g_Statement[stmt].type) {
      case illegal_:
        if (stmt != g_statements + 1) bug(1749);
        break;
      case lb_:
      case rb_:
        ptr[0] = '$';
        ptr[1] = g_Statement[stmt].type;
        ptr += 2;
        break;
      case v_:
      case c_:
      case d_:
      case e_:
      case f_:
      case a_:
        ptr[0] = '$';
        ptr[1] = g_Statement[stmt].type;
        ptr += 2;
        memcpy(ptr, g_Statement[stmt].mathSectionPtr,
            (size_t)(g_Statement[stmt].mathSectionLen));
        ptr += g_Statement[stmt].mathSectionLen;
        ptr[0] = '$';
        ptr[1] = '.';
        ptr += 2;
        break;
      case p_:
        ptr[0] = '$';
        ptr[1] = g_Statement[stmt].type;
        ptr += 2;
        memcpy(ptr, g_Statement[stmt].mathSectionPtr,
            (size_t)(g_Statement[stmt].mathSectionLen));
        ptr += g_Statement[stmt].mathSectionLen;
        ptr[0] = '$';
        ptr[1] = '=';
        ptr += 2;
        memcpy(ptr, g_Statement[stmt].proofSectionPtr,
            (size_t)(g_Statement[stmt].proofSectionLen));
        ptr += g_Statement[stmt].proofSectionLen;
        ptr[0] = '$';
        ptr[1] = '.';
        ptr += 2;
        break;
      default: bug(1750);
    }
  }
  if (ptr - buf != size) bug(1751);
  buf[size] = 0;
  return buf;
}





 void writeSplitSource(vstring *fileBuf, vstring fileName,
    flag noVersioningFlag, flag noDeleteFlag) {
  File fp;
  vstring tmpStr1 = "";
  vstring tmpFileName = "";
  vstring includeBuf = "";
  vstring includeFn = "";
  vstring fileNameWithPath = "";
  long size;
  flag writeFlag;
  long startOffset;
  long cmdPos1;
  long cmdPos2;
  long endPos1;
  long endPos2;
  char cmdType;
  startOffset = 0;
  let(&fileNameWithPath, cat(g_rootDirectory, fileName, null));
  while (true) {
    getNextInclusion(*fileBuf, startOffset,

        &cmdPos1, &cmdPos2,
        &endPos1, &endPos2,
        &cmdType,
        &includeFn  );
    if (cmdType == 'N') {
      writeFlag = 0;

      if (!strcmp(fileName, g_output_fn)) {

        writeFlag = 1;
      } else {


        let(&tmpStr1, "");
        tmpStr1 = readFileToString(fileNameWithPath, 0, &size);
        if (tmpStr1 == null) {
          tmpStr1 = "";

          let(&tmpFileName, cat(fileNameWithPath, "~1", null));
          tmpStr1 = readFileToString(tmpFileName, 0, &size);
          if (tmpStr1 == null) {
            tmpStr1 = "";

            writeFlag = 1;
          } else {

            if (strcmp(tmpStr1, *fileBuf)) {

              writeFlag = 1;
            } else {

              mminou.print2("Recovering \"%s\" from \"%s\"...\n",
                  fileNameWithPath, tmpFileName);
              rename(tmpFileName, fileNameWithPath);
            }
          }
        } else {

          if (strcmp(tmpStr1, *fileBuf)) {

            writeFlag = 1;
          } else {

            mminou.print2("Content of \"%s\" did not change.\n",
                fileNameWithPath);
            rename(tmpFileName, fileNameWithPath);
          }
        }
      }
      if (writeFlag == 1) {
        fp = fSafeOpen(fileNameWithPath, "w", 0);
        if (fp == null) {

          mminou.print2("?Error: couldn't create the file \"%s\"\n", fileNameWithPath);
          mminou.print2("  Make sure any directories needed have been created.\n");
          mminou.print2("  Try WRITE SOURCE without / SPLIT to recover your work.\n");
          break;
        } else {
          mminou.print2("Writing \"%s\"...\n", fileNameWithPath);
          fprintf(fp, "%s", *fileBuf);
          C.fclose(fp);
          break;
        }
      }
      break;
    } else if (cmdType == 'S') {

      let(&tmpStr1, cat("$[ ", includeFn, " $]", null));
      startOffset = cmdPos1 - 1 + (long)strlen(tmpStr1);
      let(&(*fileBuf), cat(left(*fileBuf, cmdPos1 - 1), tmpStr1,
          right(*fileBuf, cmdPos2), null));
      continue;
    } else if (cmdType == 'B') {

      let(&tmpStr1, cat("$[ ", includeFn, " $]", null));
      startOffset = cmdPos1 - 1 + (long)strlen(tmpStr1);
      let(&includeBuf, seg(*fileBuf, cmdPos2, endPos1 - 1));
      let(&(*fileBuf), cat(left(*fileBuf, cmdPos1 - 1), tmpStr1,
          right(*fileBuf, endPos2), null));

      writeSplitSource(&includeBuf, includeFn, noVersioningFlag, noDeleteFlag);
      continue;
    } else if (cmdType == 'I') {
      bug(1752);
      startOffset = cmdPos2 - 1;
      continue;
    } else if (cmdType == 'E') {

      mminou.print2("?Unterminated \"$( Begin $[...\" inclusion markup in \"%s\".",
          fileNameWithPath);
      startOffset = cmdPos2 - 1;
      continue;
    } else {

      bug(1753);
    }
  }


  let(&tmpStr1, "");
  let(&tmpFileName, "");
  let(&includeFn, "");
  let(&includeBuf, "");
  let(&fileNameWithPath, "");
}



 void deleteSplits(vstring *fileBuf, flag noVersioningFlag) {
  File fp;
  vstring includeFn = "";
  vstring fileNameWithPath = "";
  long startOffset;
  long cmdPos1;
  long cmdPos2;
  long endPos1;
  long endPos2;
  char cmdType;
  startOffset = 0;
  while (true) {
    getNextInclusion(*fileBuf, startOffset,

        &cmdPos1, &cmdPos2,
        &endPos1, &endPos2,
        &cmdType,
        &includeFn  );

    if (cmdType == 'B') {
      let(&fileNameWithPath, cat(g_rootDirectory, includeFn, null));

      fp = fopen(fileNameWithPath, "r");
      if (fp != null) {
        C.fclose(fp);
        if (noVersioningFlag == 1) {
          mminou.print2("Deleting \"%s\"...\n", fileNameWithPath);
        } else {
          mminou.print2("Renaming \"%s\" to \"%s~1\"...\n", fileNameWithPath,
              fileNameWithPath);
        }
        fp = fSafeOpen(fileNameWithPath, "d", noVersioningFlag);
      }

      startOffset = cmdPos2 - 1;
    } else if (cmdType == 'N') {

      break;
    } else if (cmdType == 'S') {

      startOffset = cmdPos2 - 1;
    } else if (cmdType == 'E') {


      startOffset = cmdPos2 - 1;
    } else if (cmdType == 'I') {
      bug(1755);
    } else {
      bug(1756);
    }
    continue;
  }


  let(&includeFn, "");
  let(&fileNameWithPath, "");
  return;
}






vstring getFileAndLineNum(vstring buffPtr,
    vstring currentPtr,
    long *lineNum) {
  long i, smallestOffset, smallestNdx;
  vstring fileName = "";


  if (currentPtr < buffPtr
      || currentPtr >= buffPtr + g_IncludeCall[1].current_offset) {
    bug(1769);
  }

  smallestOffset = currentPtr - buffPtr;
  if (smallestOffset < 0) bug(1767);
  smallestNdx = 0;
  for (i = 0; i <= g_includeCalls; i++) {
    if (g_IncludeCall[i].current_offset <= currentPtr - buffPtr) {
      if ((currentPtr - buffPtr) - g_IncludeCall[i].current_offset
          <= smallestOffset) {
        smallestOffset = (currentPtr - buffPtr) - g_IncludeCall[i].current_offset;
        smallestNdx = i;
      }
    }
  }
  if (smallestOffset < 0) bug(1768);
  *lineNum = g_IncludeCall[smallestNdx].current_line
      + countLines(buffPtr + g_IncludeCall[smallestNdx].current_offset,
          smallestOffset);

  let(&fileName, g_IncludeCall[smallestNdx].source_fn);

  return fileName;
}




void assignStmtFileAndLineNum(long stmtNum) {
  if (g_Statement[stmtNum].lineNum > 0) return;
  if (g_Statement[stmtNum].lineNum < 0) bug(1766);
  if (g_Statement[stmtNum].fileName[0] != 0) bug(1770);

  g_Statement[stmtNum].fileName = getFileAndLineNum(g_sourcePtr,
      g_Statement[stmtNum].statementPtr, &(g_Statement[stmtNum].lineNum));
  return;
}






char *readInclude(vstring fileBuf, long fileBufOffset,
     vstring sourceFileName,
    long *size, long parentLineNum, flag *errorFlag)
{
  long i;

  long inclSize;

  vstring newFileBuf = "";
  vstring inclPrefix = "";
  vstring tmpSource = "";
  vstring inclSource = "";
  vstring oldSource = "";
  vstring inclSuffix = "";

  long startOffset;
  long cmdPos1;
  long cmdPos2;
  long endPos1;
  long endPos2;
  char cmdType;
  long oldInclSize = 0;
  long newInclSize = 0;
  long befInclLineNum;
  long aftInclLineNum;

  vstring includeFn = "";
  vstring fullInputFn = "";
  vstring fullIncludeFn = "";
  long alreadyInclBy;
  long saveInclCalls;

  let(&newFileBuf, fileBuf);

  startOffset = 0;

  while (true) {
    getNextInclusion(newFileBuf, startOffset,

        &cmdPos1, &cmdPos2,
        &endPos1, &endPos2,
        &cmdType,
        &includeFn  );

    if (cmdType == 'N') break;
    if (cmdType == 'E') {

      mminou.print2("?Error: \"$( Begin $[...\" without matching \"$( End $[...\"\n");
      startOffset = cmdPos2;
      *errorFlag = 1;
      continue;
    }


    if (g_IncludeCall[g_includeCalls].pushOrPop != 1) bug(1764);





    g_includeCalls++;
    if (g_includeCalls >= g_MAX_INCLUDECALLS - 3) {
      g_MAX_INCLUDECALLS = g_MAX_INCLUDECALLS + 20;
if(db5)mminou.print2("'Include' call table was increased to %ld entries.\n",
    g_MAX_INCLUDECALLS);
      g_IncludeCall = realloc(g_IncludeCall, (size_t)g_MAX_INCLUDECALLS *
          sizeof(struct includeCall_struct));
      if (g_IncludeCall == null) outOfMemory("#2 (g_IncludeCall)");
    }
    g_IncludeCall[g_includeCalls].pushOrPop = 0;


    g_includeCalls++;
    g_IncludeCall[g_includeCalls].pushOrPop = 1;
    saveInclCalls = g_includeCalls;

    g_IncludeCall[saveInclCalls - 1].included_fn = "";
    let(&(g_IncludeCall[saveInclCalls - 1].included_fn), includeFn);
    g_IncludeCall[saveInclCalls].included_fn = "";
    let(&g_IncludeCall[saveInclCalls].included_fn,
        sourceFileName);



    alreadyInclBy = -1;
    for (i = 0; i <= saveInclCalls - 2; i++) {
      if (g_IncludeCall[i].pushOrPop == 0
         && !strcmp(g_IncludeCall[i].included_fn, includeFn)) {
        alreadyInclBy = i;
        break;
      }
    }
    if (alreadyInclBy == -1) {

      switch (cmdType) {
        case 'B':
          let(&inclPrefix, seg(newFileBuf, cmdPos1, cmdPos2 - 1));
          let(&inclSuffix, seg(newFileBuf, endPos1, endPos2 - 1));
          let(&tmpSource, seg(newFileBuf, cmdPos2, endPos1 - 1));
          inclSize = endPos1 - cmdPos2;


          befInclLineNum = parentLineNum + countLines(
              newFileBuf + startOffset + 1,
              cmdPos2 - 1 - startOffset);
          g_IncludeCall[saveInclCalls - 1].current_line = befInclLineNum - 1;
          aftInclLineNum = befInclLineNum + countLines(newFileBuf
              + cmdPos2,
              endPos2 - cmdPos2 - 1) + 1;
          g_IncludeCall[saveInclCalls].current_line = aftInclLineNum - 1;
          parentLineNum = aftInclLineNum;



          let(&inclSource, "");
          inclSource = readInclude(tmpSource,
              fileBufOffset + cmdPos1 - 1 + (long)strlen(inclPrefix),
               sourceFileName,
              &inclSize , befInclLineNum, &(*errorFlag));

          oldInclSize = endPos2 - cmdPos1;

          newInclSize = (long)strlen(inclPrefix) + inclSize +
                (long)strlen(inclSuffix);



          startOffset = cmdPos1 + newInclSize - 1;
          break;
        case 'I':

          let(&fullIncludeFn, cat(g_rootDirectory, includeFn, null));
          let(&tmpSource, "");
          tmpSource = readFileToString(fullIncludeFn, 0, &inclSize);
          if (tmpSource == null) {

            mminou.print2(

                "?Error: file \"%s%s\" (included in \"%s\") was not found\n",
                fullIncludeFn, g_rootDirectory, sourceFileName);
            tmpSource = "";
            inclSize = 0;
            *errorFlag = 1;
          } else {
            mminou.print2("Reading included file \"%s\"... %ld bytes\n",
                fullIncludeFn, inclSize);
          }


          let(&inclPrefix, cat("$( Begin $[ ", includeFn, " $] $)\n", null));
          let(&inclSuffix, cat("$( End $[ ", includeFn, " $] $)", null));



          befInclLineNum = parentLineNum + countLines(
              newFileBuf + startOffset + 1,
              cmdPos1 - 1 - startOffset);
          g_IncludeCall[saveInclCalls - 1].current_line = 0;
          aftInclLineNum = befInclLineNum + countLines(newFileBuf
              + cmdPos1,
              cmdPos2 - cmdPos1 - 1);
          g_IncludeCall[saveInclCalls].current_line = aftInclLineNum;
          parentLineNum = aftInclLineNum;



          let(&inclSource, "");
          inclSource = readInclude(tmpSource,
              fileBufOffset + cmdPos1 - 1 + (long)strlen(inclPrefix),
               includeFn,
              &inclSize , 1, &(*errorFlag));

          oldInclSize = cmdPos2 - cmdPos1;
          let(&newFileBuf, cat(left(newFileBuf, cmdPos1 - 1),
              inclPrefix, inclSource, inclSuffix,
              right(newFileBuf, cmdPos2), null));
          *size = *size - (cmdPos2 - cmdPos1) + (long)strlen(inclPrefix)
              + inclSize + (long)strlen(inclSuffix);
          newInclSize = (long)strlen(inclPrefix) + inclSize +
                (long)strlen(inclSuffix);
          startOffset = cmdPos1 + newInclSize - 1;
              ;

          break;
        case 'S':

          let(&fullIncludeFn, cat(g_rootDirectory, includeFn, null));
          let(&tmpSource, "");
          tmpSource = readFileToString(fullIncludeFn, 1, &inclSize);
          if (tmpSource == null) {

            mminou.print2(

                "?Error: file \"%s%s\" (included in \"%s\") was not found\n",
                fullIncludeFn, g_rootDirectory, sourceFileName);
            *errorFlag = 1;
            tmpSource = "";
            inclSize = 0;
          }


          let(&inclPrefix, cat("$( Begin $[ ", includeFn, " $] $)\n", null));
          let(&inclSuffix, cat("$( End $[ ", includeFn, " $] $)", null));


          befInclLineNum = parentLineNum + countLines(
              newFileBuf + startOffset + 1,

              cmdPos1 - 1 - startOffset);
          g_IncludeCall[saveInclCalls - 1].current_line = 0;
          aftInclLineNum = befInclLineNum + countLines(newFileBuf
              + cmdPos1,
              cmdPos2 - cmdPos1 - 1);
          g_IncludeCall[saveInclCalls].current_line = aftInclLineNum;
          parentLineNum = aftInclLineNum;



          let(&inclSource, "");
          inclSource = readInclude(tmpSource,
              fileBufOffset + cmdPos1 - 1 + (long)strlen(inclPrefix),
               includeFn,
              &inclSize , 1, &(*errorFlag));

          oldInclSize = cmdPos2 - cmdPos1;
          let(&newFileBuf, cat(left(newFileBuf, cmdPos1 - 1),
              inclPrefix, inclSource, inclSuffix,
              right(newFileBuf, cmdPos2), null));
          newInclSize = (long)strlen(inclPrefix) + inclSize +
                (long)strlen(inclSuffix);
          *size = *size - (cmdPos2 - cmdPos1) + (long)strlen(inclPrefix)
              + inclSize + (long)strlen(inclSuffix);

          startOffset = cmdPos1 + newInclSize - 1;

          break;
        default:
          bug(1745);
      }
    } else {
      if (!(alreadyInclBy > 0)) bug(1765);
      switch (cmdType) {
        case 'B':

          let(&inclSource,
              seg(newFileBuf, cmdPos2, endPos1 - 1));

          let(&oldSource, "");
          oldSource = g_IncludeCall[
                  alreadyInclBy
                  ].current_includeSource;
          if (strcmp(inclSource, oldSource)) {

            mminou.print2(
        "?Warning: \"$( Begin $[...\" source, with %ld characters, mismatches\n",
                (long)strlen(inclSource));
            mminou.print2(
                "  earlier inclusion, with %ld characters.\n",
                (long)strlen(oldSource));
          }
          oldSource = "";

          let(&inclPrefix, cat("$( Skip $[ ", includeFn, " $] $)", null));
          let(&inclSuffix, "");


          befInclLineNum = parentLineNum + countLines(
              newFileBuf + startOffset + 1,
              cmdPos2 - 1 - startOffset);
          g_IncludeCall[saveInclCalls - 1].current_line = befInclLineNum;
          aftInclLineNum = befInclLineNum + countLines(newFileBuf
              + cmdPos2,
              endPos2 - cmdPos2 );
          g_IncludeCall[saveInclCalls].current_line = aftInclLineNum;
          parentLineNum = aftInclLineNum;

          let(&inclSource, "");
          inclSize = 0;
          oldInclSize = endPos2 - cmdPos1;
          let(&newFileBuf, cat(left(newFileBuf, cmdPos1 - 1),
              inclPrefix,
              right(newFileBuf, endPos2), null));
          newInclSize = (long)strlen(inclPrefix);
          *size = *size - (endPos2 - cmdPos1) + newInclSize;
          startOffset = cmdPos1 + newInclSize - 1;
          break;
        case 'I':

          let(&inclPrefix, cat("$( Skip $[ ", includeFn, " $] $)", null));
          let(&inclSuffix, "");


          befInclLineNum = parentLineNum + countLines(
              newFileBuf + startOffset + 1,
              cmdPos1 - 1 - startOffset);
          g_IncludeCall[saveInclCalls - 1].current_line = befInclLineNum;
          aftInclLineNum = befInclLineNum + countLines(newFileBuf
              + cmdPos1,
              cmdPos2 - cmdPos1 );
          g_IncludeCall[saveInclCalls].current_line = aftInclLineNum;
          parentLineNum = aftInclLineNum;

          let(&inclSource, "");
          inclSize = 0;
          oldInclSize = cmdPos2 - cmdPos1;
          let(&newFileBuf, cat(left(newFileBuf, cmdPos1 - 1),
              inclPrefix,
              right(newFileBuf, cmdPos2), null));
          newInclSize = (long)strlen(inclPrefix);
          *size = *size - (cmdPos2 - cmdPos1) + (long)strlen(inclPrefix);

          startOffset = cmdPos1 + newInclSize - 1;
          break;
        case 'S':



          let(&inclPrefix, seg(newFileBuf, cmdPos1, cmdPos2 - 1));
          let(&inclSuffix, "");


          befInclLineNum = parentLineNum + countLines(
              newFileBuf + startOffset + 1,
              cmdPos1 - 1 - startOffset);
          g_IncludeCall[saveInclCalls - 1].current_line = befInclLineNum;
          aftInclLineNum = befInclLineNum + countLines(newFileBuf
              + cmdPos1,
              cmdPos2 - cmdPos1 );
          g_IncludeCall[saveInclCalls].current_line = aftInclLineNum - 1;
          parentLineNum = aftInclLineNum;

          let(&inclSource, "");
          inclSize = 0;
          oldInclSize = cmdPos2 - cmdPos1;
          newInclSize = oldInclSize;
          startOffset = cmdPos1 + newInclSize - 1;
          if (startOffset != cmdPos2 - 1) bug(1772);
          break;
        default:
          bug(1745);
      }
    }

    g_IncludeCall[saveInclCalls - 1].source_fn = "";
    g_IncludeCall[saveInclCalls - 1].current_offset = fileBufOffset + cmdPos1 - 1
        + (long)strlen(inclPrefix) - 1;
    if (alreadyInclBy >= 0 || cmdType == 'B') {
      let(&g_IncludeCall[saveInclCalls - 1].source_fn, sourceFileName);
    } else {
      let(&g_IncludeCall[saveInclCalls - 1].source_fn, includeFn);
    }
    g_IncludeCall[saveInclCalls - 1].current_includeSource = inclSource;
    inclSource = "";
    g_IncludeCall[saveInclCalls - 1].current_includeSource = "";
    g_IncludeCall[saveInclCalls - 1].current_includeLength = inclSize;




    g_IncludeCall[saveInclCalls].source_fn = "";
    let(&g_IncludeCall[saveInclCalls].source_fn,
        sourceFileName);
    g_IncludeCall[saveInclCalls].current_offset = fileBufOffset + cmdPos1
        + newInclSize - 1;

    g_IncludeCall[saveInclCalls].current_includeSource = "";
    g_IncludeCall[saveInclCalls].current_includeLength = 0;

  }


  let(&inclSource, "");
  let(&tmpSource, "");
  let(&oldSource, "");
  let(&inclPrefix, "");
  let(&inclSuffix, "");
  let(&includeFn, "");
  let(&fullInputFn, "");
  let(&fullIncludeFn, "");

  return newFileBuf;
}



char *readSourceAndIncludes(vstring inputFn , long *size )
{
  long i;


  vstring fileBuf = "";
  vstring newFileBuf = "";

  vstring fullInputFn = "";
  flag errorFlag = 0;


  let(&fullInputFn, cat(g_rootDirectory, inputFn, null));
  fileBuf = readFileToString(fullInputFn, 1, &(*size));
  if (fileBuf == null) {
    mminou.print2(
        "?Error: file \"%s\" was not found\n", fullInputFn);
    fileBuf = "";
    *size = 0;
    errorFlag = 1;
  }
  mminou.print2("Reading source file \"%s\"... %ld bytes\n", fullInputFn, *size);

  g_includeCalls = 0;
  g_IncludeCall[g_includeCalls].pushOrPop = 0;
  g_IncludeCall[g_includeCalls].source_fn = "";
  let(&g_IncludeCall[g_includeCalls].source_fn, inputFn);
  g_IncludeCall[g_includeCalls].included_fn = "";
  let(&g_IncludeCall[g_includeCalls].included_fn, inputFn);
  g_IncludeCall[g_includeCalls].current_offset = 0;
  g_IncludeCall[g_includeCalls].current_line = 1;
  g_IncludeCall[g_includeCalls].current_includeSource = "";
  g_IncludeCall[g_includeCalls].current_includeLength = *size;

  g_includeCalls++;
  g_IncludeCall[g_includeCalls].pushOrPop = 1;
  g_IncludeCall[g_includeCalls].source_fn = "";
  g_IncludeCall[g_includeCalls].included_fn = "";
  g_IncludeCall[g_includeCalls].current_line = -1;
  g_IncludeCall[g_includeCalls].current_includeSource = "";
  g_IncludeCall[g_includeCalls].current_includeLength = 0;


  newFileBuf = "";
  newFileBuf = readInclude(fileBuf, 0,  inputFn, &(*size),
      1, &errorFlag);
  g_IncludeCall[1].current_offset = *size;
  let(&fileBuf, "");





  if (errorFlag == 1) {


    for (i = 0; i <= g_includeCalls; i++) {
      let(&g_IncludeCall[i].source_fn, "");
      let(&g_IncludeCall[i].included_fn, "");
      let(&g_IncludeCall[i].current_includeSource, "");
      g_includeCalls = -1;
    }
    return null;
  } else {





    return newFileBuf;
  }

}

}