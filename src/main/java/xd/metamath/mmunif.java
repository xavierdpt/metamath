package xd.metamath;

public class mmunif {











long g_minSubstLen = 1;
long g_userMaxUnifTrials = 100000;

long g_unifTrialCount = 0;

long g_unifTimeouts = 0;
flag g_hentyFilter = 1;
flag g_bracketMatchInit = 0;


void hentyNormalize(nmbrString **hentyVars, nmbrString **hentyVarStart,
    nmbrString **hentyVarLen, nmbrString **hentySubstList,
    pntrString **stateVector);
flag hentyMatch(nmbrString *hentyVars, nmbrString *hentyVarStart,
     nmbrString *hentySubstList,
    pntrString **stateVector);
void hentyAdd(nmbrString *hentyVars, nmbrString *hentyVarStart,
    nmbrString *hentyVarLen, nmbrString *hentySubstList,
    pntrString **stateVector);



int maxNestingLevel = -1;
int nestingLevel = 0;




nmbrString *g_firstConst = NULL_NMBRSTRING;
nmbrString *g_lastConst = NULL_NMBRSTRING;
nmbrString *g_oneConst = NULL_NMBRSTRING;



nmbrString *makeSubstUnif(flag *newVarFlag,
    nmbrString *trialScheme, pntrString *stateVector)
{
  long p,q,i,j,k,m,tokenNum;
  long schemeLen;
  nmbrString *result = NULL_NMBRSTRING;
  nmbrString *stackUnkVar = NULL_NMBRSTRING;
  nmbrString *unifiedScheme;
  nmbrString *stackUnkVarLen;
  nmbrString *stackUnkVarStart;
  long stackTop;
long d;
vstring tmpStr = "";
let(&tmpStr,tmpStr);

  stackTop = ((nmbrString *)(stateVector[11]))[1];
  nmbrLet(&stackUnkVar,nmbrLeft((nmbrString *)(stateVector[1]), stackTop + 1));
  stackUnkVarStart = (nmbrString *)(stateVector[2]);
  stackUnkVarLen = (nmbrString *)(stateVector[3]);
  unifiedScheme = (nmbrString *)(stateVector[8]);

if(db7)print2("Entered makeSubstUnif.\n");
if(db7)printLongLine(cat("unifiedScheme is ",
    nmbrCvtMToVString(unifiedScheme), NULL), "", " ");
if(db7)printLongLine(cat("trialScheme is ",
    nmbrCvtMToVString(trialScheme), NULL), "", " ");
if(db7)print2("stackTop is %ld.\n",stackTop);
for (d = 0; d <= stackTop; d++) {
  if(db7)print2("Unknown var %ld is %s.\n",d,
      g_MathToken[stackUnkVar[d]].tokenName);
  if(db7)print2("  Its start is %ld; its length is %ld.\n",
      stackUnkVarStart[d],stackUnkVarLen[d]);
}
  schemeLen = nmbrLen(trialScheme);


  q = 0;
  *newVarFlag = 0;
if(db7)print2("schemeLen is %ld.\n",schemeLen);
  for (p = 0; p < schemeLen; p++) {
if(db7)print2("p is %ld.\n",p);
    tokenNum = trialScheme[p];
if(db7)print2("token is %s, tokenType is %ld\n",g_MathToken[tokenNum].tokenName,
  (long)g_MathToken[tokenNum].tokenType);
    if (g_MathToken[tokenNum].tokenType == (char)con_) {
      q++;
    } else {
      if (tokenNum > g_mathTokens) {

        m = nmbrElementIn(1,stackUnkVar,tokenNum);
if(db7)print2("token is %s, m is %ld\n",g_MathToken[tokenNum].tokenName,m);
        if (m) {

          q = q + stackUnkVarLen[m - 1];

          g_MathToken[tokenNum].tmp = m - 1;
        } else {

          *newVarFlag = 1;
          q++;

          g_MathToken[tokenNum].tmp = -1;
        }
      } else {

        q++;
      }
    }
  }

  nmbrLet(&result, nmbrSpace(q));

  q = 0;
  for (p = 0; p < schemeLen; p++) {
    tokenNum = trialScheme[p];
    if (g_MathToken[tokenNum].tokenType == (char)con_) {
      result[q] = tokenNum;
      q++;
    } else {
      if (tokenNum > g_mathTokens) {

        k = g_MathToken[tokenNum].tmp;
        if (k != -1) {

          m = stackUnkVarStart[k];
          j = stackUnkVarLen[k];
          for (i = 0; i < j; i++) {
            result[q + i] = unifiedScheme[m + i];
          }
          q = q + j;
        } else {

          result[q] = tokenNum;
          q++;
        }
      } else {

        result[q] = tokenNum;
        q++;
      }
    }
  }
if(db7)print2("after newVarFlag %d\n",(int)*newVarFlag);
if(db7)print2("final len is %ld\n",q);
if(db7)printLongLine(cat("result ",nmbrCvtMToVString(result),NULL),""," ");
  nmbrLet(&stackUnkVar, NULL_NMBRSTRING);
  return (result);
}





char unify(
    nmbrString *schemeA,
    nmbrString *schemeB,

    pntrString **stateVector,
    long reEntryFlag)
{



  long stackTop;
  nmbrString *unkVars;
  long unkVarsLen;
  long schemeAUnkVarsLen;
  nmbrString *stackUnkVar;
  nmbrString *stackUnkVarStart;
  nmbrString *stackUnkVarLen;
  pntrString *stackSaveUnkVarStart;
  pntrString *stackSaveUnkVarLen;
  pntrString *stackSaveSchemeA;
  pntrString *stackSaveSchemeB;
  nmbrString *unifiedScheme;
  long p;
  long substToken;
  nmbrString *substitution = NULL_NMBRSTRING;

  nmbrString *nmbrTmpPtr;
  pntrString *pntrTmpPtr;
  nmbrString *schA = NULL_NMBRSTRING;
  nmbrString *schB = NULL_NMBRSTRING;
  long i,j,k,m, pairingMismatches;
  flag breakFlag;
  flag schemeAFlag;
  flag timeoutAbortFlag = 0;
  vstring mToken;


  flag impossible;
  long stmt;


  static char bracketMatchOn;

  long bracketScanStart, bracketScanStop;
  flag bracketMismatchFound;


long d;
vstring tmpStr = "";
let(&tmpStr,tmpStr);
if(db5)print2("Entering unify() with reEntryFlag = %ld.\n",
  (long)reEntryFlag);
if(db5)printLongLine(cat("schemeA is ",
    nmbrCvtMToVString(schemeA),".",NULL),"    ","  ");
if(db5)printLongLine(cat("schemeB is ",
    nmbrCvtMToVString(schemeB),".",NULL),"    ","  ");

  p = 0;
  bracketMismatchFound = 0;


  if (g_MathToken[schemeA[0]].tokenType == (char)con_) {
    if (g_MathToken[schemeB[0]].tokenType == (char)con_) {
      if (schemeA[0] != schemeB[0]) {
        return (0);
      }
    }
  }

  j = nmbrLen(schemeA);
  k = nmbrLen(schemeB);
  if (!j || !k) bug(1901);
  if (g_MathToken[schemeA[j-1]].tokenType == (char)con_) {
    if (g_MathToken[schemeB[k-1]].tokenType == (char)con_) {
      if (schemeA[j-1] != schemeB[k-1]) {
        return (0);
      }
    }
  }


  nmbrLet(&schA, nmbrAddElement(schemeA, g_mathTokens));
  nmbrLet(&schB, nmbrAddElement(schemeB, g_mathTokens));

  if (!nmbrLen(g_firstConst)) {

    nmbrLet(&g_firstConst, nmbrSpace(g_mathTokens));
    nmbrLet(&g_lastConst, nmbrSpace(g_mathTokens));
    nmbrLet(&g_oneConst, nmbrSpace(g_mathTokens));


    for (stmt = 1; stmt <= g_statements; stmt++) {
      if (g_Statement[stmt].type != (char)a_)
        continue;
      if (g_Statement[stmt].mathStringLen < 2) continue;

      if (g_MathToken[(g_Statement[stmt].mathString)[1]].tokenType == (char)con_) {
        g_firstConst[(g_Statement[stmt].mathString)[1]] = 1;
        if (g_Statement[stmt].mathStringLen == 2) {
          g_oneConst[(g_Statement[stmt].mathString)[1]] = 1;
        }
      }

      if (g_MathToken[(g_Statement[stmt].mathString)[
          g_Statement[stmt].mathStringLen - 1]].tokenType == (char)con_) {
        g_lastConst[(g_Statement[stmt].mathString)[
          g_Statement[stmt].mathStringLen - 1]] = 1;
      }
    }
  }


  if (!reEntryFlag) {

    p = 0;




    unkVars = NULL_NMBRSTRING;
    nmbrLet(&unkVars, nmbrSpace(j + k));
    unkVarsLen = 0;
    for (i = 0; i < j; i++) {
      if (schemeA[i] > g_mathTokens) {

        breakFlag = 0;
        for (m = 0; m < unkVarsLen; m++) {
          if (unkVars[m] == schemeA[i]) {

            breakFlag = 1;
          }
        }
        if (!breakFlag) {

          unkVars[unkVarsLen++] = schemeA[i];
        }
      }
    }

    schemeAUnkVarsLen = unkVarsLen;
    for (i = 0; i < k; i++) {
      if (schemeB[i] > g_mathTokens) {

        breakFlag = 0;
        for (m = 0; m < unkVarsLen; m++) {
          if (unkVars[m] == schemeB[i]) {

            breakFlag = 1;
          }
        }
        if (!breakFlag) {

          unkVars[unkVarsLen++] = schemeB[i];
        }
      }
    }


    if (pntrLen(*stateVector)) {


      for (i = 4; i <= 7; i++) {
        pntrTmpPtr = (pntrString *)((*stateVector)[i]);
        for (j = 0; j < ((nmbrString *)((*stateVector)[11]))[0]; j++) {
          nmbrLet((nmbrString **)(&pntrTmpPtr[j]),
              NULL_NMBRSTRING);
        }
        pntrLet((pntrString **)(&(*stateVector)[i]),
            NULL_PNTRSTRING);
      }
      for (i = 0; i <= 3; i++) {
        nmbrLet((nmbrString **)(&(*stateVector)[i]),
            NULL_NMBRSTRING);
      }
      for (i = 8; i <= 10; i++) {
        nmbrLet((nmbrString **)(&(*stateVector)[i]),
            NULL_NMBRSTRING);
      }
      k = pntrLen((pntrString *)((*stateVector)[12]));
      for (i = 12; i < 16; i++) {
        pntrTmpPtr = (pntrString *)((*stateVector)[i]);
        for (j = 0; j < k; j++) {
          nmbrLet((nmbrString **)(&pntrTmpPtr[j]),
              NULL_NMBRSTRING);
        }
        pntrLet((pntrString **)(&(*stateVector)[i]),
            NULL_PNTRSTRING);
      }

    } else {


      pntrLet(&(*stateVector), pntrPSpace(16));
      nmbrLet((nmbrString **)(&(*stateVector)[11]), nmbrSpace(4));
    }


    stackTop = -1;
    stackUnkVar = NULL_NMBRSTRING;
    stackUnkVarStart = NULL_NMBRSTRING;
    stackUnkVarLen = NULL_NMBRSTRING;
    stackSaveUnkVarStart = NULL_PNTRSTRING;
    stackSaveUnkVarLen = NULL_PNTRSTRING;
    stackSaveSchemeA = NULL_PNTRSTRING;
    stackSaveSchemeB = NULL_PNTRSTRING;
    unifiedScheme = NULL_NMBRSTRING;
    nmbrLet(&stackUnkVar, nmbrSpace(unkVarsLen));
    nmbrLet(&stackUnkVarStart, stackUnkVar);
    nmbrLet(&stackUnkVarLen, stackUnkVar);


    pntrLet(&stackSaveUnkVarStart, pntrNSpace(unkVarsLen));
    pntrLet(&stackSaveUnkVarLen, stackSaveUnkVarStart);
    pntrLet(&stackSaveSchemeA, stackSaveUnkVarStart);
    pntrLet(&stackSaveSchemeB, stackSaveUnkVarStart);
    for (i = 0; i < unkVarsLen; i++) {

      nmbrLet((nmbrString **)(&stackSaveUnkVarStart[i]),
          stackUnkVar);
      nmbrLet((nmbrString **)(&stackSaveUnkVarLen[i]),
          stackUnkVar);
    }



    for (i = 0; i < unkVarsLen; i++) {
      g_MathToken[unkVars[i]].tmp = -1;
    }

  } else {




    unkVarsLen = ((nmbrString *)((*stateVector)[11]))[0];
    unkVars = (nmbrString *)((*stateVector)[0]);
    stackTop = ((nmbrString *)((*stateVector)[11]))[1];
    stackUnkVar = (nmbrString *)((*stateVector)[1]);
    stackUnkVarStart = (nmbrString *)((*stateVector)[2]);
    stackUnkVarLen = (nmbrString *)((*stateVector)[3]);
    stackSaveUnkVarStart = (pntrString *)((*stateVector)[4]);
    stackSaveUnkVarLen = (pntrString *)((*stateVector)[5]);
    stackSaveSchemeA = (pntrString *)((*stateVector)[6]);
    stackSaveSchemeB = (pntrString *)((*stateVector)[7]);
    unifiedScheme = (nmbrString *)((*stateVector)[8]);
    schemeAUnkVarsLen = ((nmbrString *)((*stateVector)[11]))[2];




    for (i = 0; i < unkVarsLen; i++) {
      g_MathToken[unkVars[i]].tmp = -1;
    }
    for (i = 0; i <= stackTop; i++) {
      g_MathToken[stackUnkVar[i]].tmp = i;
    }


    goto backtrack;
   reEntry1:
    reEntryFlag = 0;


  }



 scan:
if(db6)print2("Entered scan: p=%ld\n",p);
if(db6)print2("Enter scn sbA %s\n",nmbrCvtMToVString(schA));
if(db6)print2("Enter scn sbB %s\n",nmbrCvtMToVString(schB));
if(db6)let(&tmpStr,tmpStr);
  while (schA[p] == schB[p] &&
      schA[p + 1] != -1) {
    p++;
  }
if(db6)print2("First mismatch: p=%ld\n",p);

  if (schA[p] == g_mathTokens
      || schB[p] == g_mathTokens) {

    if (schA[p] != schB[p]) {

      if (schA[p] <= g_mathTokens &&
          schB[p] <= g_mathTokens) {
if(db6)print2("Backtracked because end-of-string\n");
        goto backtrack;
      }
    } else {
      if (schA[p + 1] == -1) {

        goto done;
      }
    }
  }

  if (schB[p] > g_mathTokens && schA[p] > g_mathTokens) {




    if (g_MathToken[schB[p]].tmp == -1) {

      goto schAUnk;
    } else {
      if (g_MathToken[schA[p]].tmp != -1) bug(1902);
      goto schBUnk;
    }
  }

 schBUnk:
  if (schB[p] > g_mathTokens) {
if(db6)print2("schB has unknown variable\n");

    schemeAFlag = 0;
    substToken = schB[p];

    if (g_MathToken[substToken].tmp == -1) {

      stackTop++;
      stackUnkVar[stackTop] = substToken;
      g_MathToken[substToken].tmp = stackTop;
      stackUnkVarStart[stackTop] = p;

      stackUnkVarLen[stackTop] = g_minSubstLen;

      nmbrTmpPtr = (nmbrString *)(stackSaveUnkVarStart[stackTop]);
      for (i = 0; i <= stackTop; i++) {
        nmbrTmpPtr[i] = stackUnkVarStart[i];
      }
      nmbrTmpPtr = (nmbrString *)(stackSaveUnkVarLen[stackTop]);
      for (i = 0; i <= stackTop; i++) {
        nmbrTmpPtr[i] = stackUnkVarLen[i];
      }
      nmbrLet((nmbrString **)(&stackSaveSchemeA[stackTop]),
          schA);
      nmbrLet((nmbrString **)(&stackSaveSchemeB[stackTop]),
          schB);
    }

    if (substToken != stackUnkVar[stackTop]) {
      print2("PROGRAM BUG #1903\n");
      print2("substToken is %s\n", g_MathToken[substToken].tokenName);
      print2("stackTop %ld\n", stackTop);
      print2("p %ld stackUnkVar[stackTop] %s\n", p,
        g_MathToken[stackUnkVar[stackTop]].tokenName);
      print2("schA %s\nschB %s\n", nmbrCvtMToVString(schA),
        nmbrCvtMToVString(schB));
      bug(1903);
    }
    nmbrLet(&substitution, nmbrMid(schA, p + 1,
        stackUnkVarLen[stackTop]));
    goto substitute;
  }

 schAUnk:
  if (schA[p] > g_mathTokens) {
if(db6)print2("schA has unknown variable\n");

    schemeAFlag = 1;
    substToken = schA[p];
    if (g_MathToken[substToken].tmp == -1) {

      stackTop++;
      stackUnkVar[stackTop] = substToken;
      g_MathToken[substToken].tmp = stackTop;
      stackUnkVarStart[stackTop] = p;

      stackUnkVarLen[stackTop] = g_minSubstLen;

      nmbrTmpPtr = (nmbrString *)(stackSaveUnkVarStart[stackTop]);
      for (i = 0; i <= stackTop; i++) {
        nmbrTmpPtr[i] = stackUnkVarStart[i];
      }
      nmbrTmpPtr = (nmbrString *)(stackSaveUnkVarLen[stackTop]);
      for (i = 0; i <= stackTop; i++) {
        nmbrTmpPtr[i] = stackUnkVarLen[i];
      }
      nmbrLet((nmbrString **)(&stackSaveSchemeA[stackTop]),
          schA);
      nmbrLet((nmbrString **)(&stackSaveSchemeB[stackTop]),
          schB);
    }

    if (substToken != stackUnkVar[stackTop]) {
print2("PROGRAM BUG #1904\n");
print2("\nsubstToken is %s\n",g_MathToken[substToken].tokenName);
print2("stack top %ld\n",stackTop);
print2("p %ld stUnV[stakTop] %s\n",p,
g_MathToken[stackUnkVar[stackTop]].tokenName);
print2("schA %s\nschB %s\n",nmbrCvtMToVString(schA),nmbrCvtMToVString(schB));
      bug(1904);
    }
    nmbrLet(&substitution, nmbrMid(schB, p + 1,
        stackUnkVarLen[stackTop]));
    goto substitute;
  }

if(db6)print2("Neither scheme has unknown variable\n");
  goto backtrack;


 substitute:
if(db6)print2("Entering substitute...\n");
for (d = 0; d <= stackTop; d++) {
  if(db6)print2("Unknown var %ld is %s.\n",d,
      g_MathToken[stackUnkVar[d]].tokenName);
  if(db6)print2("  Its start is %ld; its length is %ld.\n",
      stackUnkVarStart[d],stackUnkVarLen[d]);
}


  if (nmbrElementIn(1, substitution, substToken)) {
if(db6)print2("Substituted token occurs in substitution string\n");
    goto backtrack;
  }

  if (substitution[0] == g_mathTokens) {
if(db6)print2("End of string token occurs in substitution string\n");

    g_MathToken[stackUnkVar[stackTop]].tmp = -1;
    stackTop--;
    goto backtrack;
  }


  for (i = g_bracketMatchInit; i <= 1; i++) {
    bracketMismatchFound = 0;
    if (g_bracketMatchInit == 0) {
      if (i != 0) bug(1908);

      bracketScanStart = 1;
      bracketScanStop = g_statements;
    } else {
      if (i != 1) bug(1909);
      if (!bracketMatchOn) break;

      bracketScanStart = 0;
      bracketScanStop = 0;
    }
    for (m = bracketScanStart; m <= bracketScanStop; m++) {
      if (g_bracketMatchInit == 0) {
        if (g_Statement[m].type != a_) continue;
        nmbrTmpPtr = g_Statement[m].mathString;
      } else {
        nmbrTmpPtr = substitution;
      }
      j = nmbrLen(nmbrTmpPtr);


      pairingMismatches = 0;
      for (k = 0; k < j; k++) {
        mToken = g_MathToken[nmbrTmpPtr[k]].tokenName;
        if (mToken[0] == '(' && mToken[1] == 0 ) {
          pairingMismatches++;
        } else if (mToken[0] == ')' && mToken[1] == 0 ) {
          pairingMismatches--;
          if (pairingMismatches < 0) break;
        }
      }
      if (pairingMismatches != 0) {
        bracketMismatchFound = 1;
        break;
      }


      pairingMismatches = 0;
      for (k = 0; k < j; k++) {
        mToken = g_MathToken[nmbrTmpPtr[k]].tokenName;
        if (mToken[0] == '{' && mToken[1] == 0 ) pairingMismatches++;
        else
          if (mToken[0] == '}' && mToken[1] == 0 ) {
            pairingMismatches--;
            if (pairingMismatches < 0) break;
          }
      }
      if (pairingMismatches != 0) {
        bracketMismatchFound = 1;
        break;
      }


      pairingMismatches = 0;
      for (k = 0; k < j; k++) {
        mToken = g_MathToken[nmbrTmpPtr[k]].tokenName;
        if (mToken[0] == '[' && mToken[1] == 0 )
          pairingMismatches++;
        else
          if (mToken[0] == ']' && mToken[1] == 0 ) {
            pairingMismatches--;
            if (pairingMismatches < 0) break;
          }
      }
      if (pairingMismatches != 0) {
        bracketMismatchFound = 1;
        break;
      }


      pairingMismatches = 0;
      for (k = 0; k < j; k++) {
        mToken = g_MathToken[nmbrTmpPtr[k]].tokenName;
        if (mToken[1] == 0) continue;
        if (mToken[0] == '<' && mToken[1] == '.' && mToken[2] == 0 )
            pairingMismatches++;
        else
          if (mToken[0] == '>' && mToken[1] == '.' && mToken[2] == 0 ) {
            pairingMismatches--;
            if (pairingMismatches < 0) break;
          }
      }
      if (pairingMismatches != 0) {
        bracketMismatchFound = 1;
        break;
      }


      pairingMismatches = 0;
      for (k = 0; k < j; k++) {
        mToken = g_MathToken[nmbrTmpPtr[k]].tokenName;
        if (mToken[1] == 0) continue;
        if (mToken[0] == '[' && mToken[1] == '_' && mToken[2] == 0 )
            pairingMismatches++;
        else
          if (mToken[0] == ']' && mToken[1] == '_' && mToken[2] == 0 ) {
            pairingMismatches--;
            if (pairingMismatches < 0) break;
          }
      }
      if (pairingMismatches != 0) {
        bracketMismatchFound = 1;
        break;
      }
    }

    if (g_bracketMatchInit == 0) {

      if (bracketMismatchFound) {
        if (m < 1 || m > g_statements) bug(1910);
        printLongLine(cat("The bracket matching unification heuristic was",
           " turned off for this database because of a bracket mismatch in",
           " statement \"",

           g_Statement[m].labelName,
           "\".", NULL),
           "    ", " ");
        bracketMatchOn = 0;
      } else {
        bracketMatchOn = 1;
      }
if(db6)print2("bracketMatchOn = %ld\n", (long)bracketMatchOn);
      g_bracketMatchInit = 1;
    }
  }

  if (bracketMismatchFound) goto backtrack;


  j = nmbrLen(substitution);

  if (j > 0 && g_minSubstLen > 0) {
    impossible = 0;
    if (g_MathToken[substitution[0]].tokenType == (char)con_) {
      if (!g_firstConst[substitution[0]]
         || (j == 1 && !g_oneConst[substitution[0]])) {
        impossible = 1;
      }
    }
    if (g_MathToken[substitution[j - 1]].tokenType == (char)con_) {
      if (!g_lastConst[substitution[j - 1]]) {
        impossible = 1;
      }
    }
    if (impossible) {
if(db6)print2("Impossible subst: %s\n", nmbrCvtMToVString(substitution));
      goto backtrack;
    }
  }


if(db6)print2("Substitution is '%s'\n",nmbrCvtMToVString(substitution));
  k = 1;
  while (1) {

    k = nmbrElementIn(k, schA, substToken);
    if (!k) break;

    if (schemeAFlag) {


      if (k - 1 <= p) {
        if (k <= p) {

          for (i = 0; i <= stackTop; i++) {
            if (k - 1 < stackUnkVarStart[i]) {
              stackUnkVarStart[i] = stackUnkVarStart[i] + j-1;
            } else {
              if (k <= stackUnkVarStart[i] +
                  stackUnkVarLen[i]) {
                stackUnkVarLen[i] = stackUnkVarLen[i] + j - 1;
              }
            }
          }
        }
        p = p + j - 1;
if(db6)print2("Scheme A adjusted p=%ld\n",p);
      }
    }

    nmbrLet(&schA, nmbrCat(
        nmbrLeft(schA, k - 1), substitution, nmbrRight(schA, k + 1), NULL));
    k = k + j - 1;
  }
  k = 1;
  while (1) {

    k = nmbrElementIn(k, schB, substToken);
    if (!k) break;

    if (!schemeAFlag) {


      if (k - 1 <= p) {
        if (k <= p) {

          for (i = 0; i <= stackTop; i++) {
            if (k - 1 < stackUnkVarStart[i]) {
              stackUnkVarStart[i] = stackUnkVarStart[i] + j-1;
            } else {
              if (k <= stackUnkVarStart[i] +
                  stackUnkVarLen[i]) {
                stackUnkVarLen[i] = stackUnkVarLen[i] + j - 1;
              }
            }
          }
        }
        p = p + j - 1;
      }
if(db6)print2("Scheme B adjusted p=%ld\n",p);
    }

    nmbrLet(&schB, nmbrCat(
        nmbrLeft(schB, k - 1), substitution, nmbrRight(schB, k + 1), NULL));
    k = k + j - 1;
  }
  p++;
if(db6)print2("Scheme A or B final p=%ld\n",p);
if(db6)print2("after sub sbA %s\n",nmbrCvtMToVString(schA));
if(db6)print2("after sub sbB %s\n",nmbrCvtMToVString(schB));
for (d = 0; d <= stackTop; d++) {
  if(db6)print2("Unknown var %ld is %s.\n",d,
      g_MathToken[stackUnkVar[d]].tokenName);
  if(db6)print2("  Its start is %ld; its length is %ld.\n",
      stackUnkVarStart[d],stackUnkVarLen[d]);
}
  goto scan;

 backtrack:
if(db6)print2("Entered backtrack with p=%ld stackTop=%ld\n",p,stackTop);
  if (stackTop < 0) {
    goto abort;
  }
  if (g_unifTrialCount > 0) {
    g_unifTrialCount++;
    if (g_unifTrialCount > g_userMaxUnifTrials) {
      g_unifTimeouts++;
if(db5)print2("Aborted due to timeout: %ld > %ld\n",
    g_unifTrialCount, g_userMaxUnifTrials);
      timeoutAbortFlag = 1;
      goto abort;
    }
  }

  nmbrTmpPtr = (nmbrString *)(stackSaveUnkVarLen[stackTop]);
  nmbrTmpPtr[stackTop]++;

  nmbrLet(&stackUnkVarStart, (nmbrString *)(stackSaveUnkVarStart[stackTop]));
  nmbrLet(&schA, (nmbrString *)(stackSaveSchemeA[stackTop]));
  nmbrLet(&schB, (nmbrString *)(stackSaveSchemeB[stackTop]));

  p = stackUnkVarStart[stackTop];
 switchVarToB:

  nmbrLet(&stackUnkVarLen, (nmbrString *)(stackSaveUnkVarLen[stackTop]));

if(db6)print2("Backtracked to token %s.\n",
  g_MathToken[stackUnkVar[stackTop]].tokenName);
  if (stackUnkVar[stackTop] == schA[p]) {

    if (schB[p - 1 + stackUnkVarLen[stackTop]]
        == g_mathTokens) {
if(db6)print2("It was in scheme A; overflowed scheme B: p=%ld, len=%ld.\n",
  p,stackUnkVarLen[stackTop]);

      g_MathToken[stackUnkVar[stackTop]].tmp = -1;


      if (schB[p] > g_mathTokens) {
if(db6)print2("Switched var-var match to scheme B token %s\n",
     g_MathToken[stackUnkVar[stackTop]].tokenName);

        if (g_MathToken[schB[p]].tmp != -1) bug(1905);

        stackUnkVar[stackTop] = schB[p];
        g_MathToken[schB[p]].tmp = stackTop;

        stackUnkVarLen[stackTop] = g_minSubstLen;

        nmbrTmpPtr = (nmbrString *)(stackSaveUnkVarLen[stackTop]);
        nmbrTmpPtr[stackTop] = g_minSubstLen;

        goto switchVarToB;
      }


      stackTop--;
      goto backtrack;
    }
  } else {

    if (schA[p - 1 + stackUnkVarLen[stackTop]]
        == g_mathTokens) {
if(db6)print2("It was in scheme B; overflowed scheme A: p=%ld, len=%ld.\n",
  p,stackUnkVarLen[stackTop]);

      g_MathToken[stackUnkVar[stackTop]].tmp = -1;
      stackTop--;
      goto backtrack;
    }
  }
if(db6)print2("Exited backtrack with p=%ld stackTop=%ld\n",p,stackTop);
  if (reEntryFlag) goto reEntry1;
  goto scan;

 done:


  nmbrLet(&unifiedScheme, nmbrLeft(schA, nmbrLen(schA) - 1));
if(db5)print2("Backtrack count was %ld\n",g_unifTrialCount);
if(db5)printLongLine(cat("Unified scheme is ",
    nmbrCvtMToVString(unifiedScheme),".",NULL),"    ","  ");

  ((nmbrString *)((*stateVector)[11]))[0] = unkVarsLen;
  (*stateVector)[0] = unkVars;

  ((nmbrString *)((*stateVector)[11]))[1] = stackTop;
  (*stateVector)[1] = stackUnkVar;
  (*stateVector)[2] = stackUnkVarStart;
  (*stateVector)[3] = stackUnkVarLen;
  (*stateVector)[4] = stackSaveUnkVarStart;
  (*stateVector)[5] = stackSaveUnkVarLen;
  (*stateVector)[6] = stackSaveSchemeA;
  (*stateVector)[7] = stackSaveSchemeB;

  (*stateVector)[8] = unifiedScheme;
  ((nmbrString *)((*stateVector)[11]))[2] = schemeAUnkVarsLen;

if(db5)printSubst(*stateVector);

  nmbrLet(&schA, NULL_NMBRSTRING);
  nmbrLet(&schB, NULL_NMBRSTRING);
  nmbrLet(&substitution, NULL_NMBRSTRING);
  return (1);

 abort:
if(db5)print2("Backtrack count was %ld\n",g_unifTrialCount);

  nmbrLet(&unkVars,NULL_NMBRSTRING);
  nmbrLet(&stackUnkVar,NULL_NMBRSTRING);
  nmbrLet(&stackUnkVarStart,NULL_NMBRSTRING);
  nmbrLet(&stackUnkVarLen,NULL_NMBRSTRING);
  for (i = 0; i < unkVarsLen; i++) {

    nmbrLet((nmbrString **)(&stackSaveUnkVarStart[i]),
        NULL_NMBRSTRING);
    nmbrLet((nmbrString **)(&stackSaveUnkVarLen[i]),
        NULL_NMBRSTRING);
    nmbrLet((nmbrString **)(&stackSaveSchemeA[i]),
        NULL_NMBRSTRING);
    nmbrLet((nmbrString **)(&stackSaveSchemeB[i]),
        NULL_NMBRSTRING);
  }
  pntrLet(&stackSaveUnkVarStart,NULL_PNTRSTRING);
  pntrLet(&stackSaveUnkVarLen,NULL_PNTRSTRING);
  pntrLet(&stackSaveSchemeA,NULL_PNTRSTRING);
  pntrLet(&stackSaveSchemeB,NULL_PNTRSTRING);
  nmbrLet(&unifiedScheme,NULL_NMBRSTRING);

  nmbrLet((nmbrString **)(&(*stateVector)[9]),NULL_NMBRSTRING);
  nmbrLet((nmbrString **)(&(*stateVector)[10]),NULL_NMBRSTRING);

  k = pntrLen((pntrString *)((*stateVector)[12]));
  for (i = 12; i < 16; i++) {
    pntrTmpPtr = (pntrString *)((*stateVector)[i]);
    for (j = 0; j < k; j++) {
      nmbrLet((nmbrString **)(&pntrTmpPtr[j]),
          NULL_NMBRSTRING);
    }
    pntrLet((pntrString **)(&(*stateVector)[i]),
        NULL_PNTRSTRING);
  }

  ((nmbrString *)((*stateVector)[11]))[1] = 0;

  nmbrLet((nmbrString **)(&(*stateVector)[11]),NULL_NMBRSTRING);
  pntrLet(&(*stateVector),NULL_PNTRSTRING);


  nmbrLet(&schA,NULL_NMBRSTRING);
  nmbrLet(&schB,NULL_NMBRSTRING);
  nmbrLet(&substitution,NULL_NMBRSTRING);

  if (timeoutAbortFlag) {
    return (2);
  }
  return (0);
}


flag oneDirUnif(
    nmbrString *schemeA,
    nmbrString *schemeB,
    pntrString **stateVector,
    long reEntryFlag)
{
long i;
flag tmpFlag;
long schemeAUnkVarsLen;
nmbrString *stackUnkVarStart;
nmbrString *stackUnkVarLen;
nmbrString *oldStackUnkVarStart;
nmbrString *oldStackUnkVarLen;

  if (!reEntryFlag) {
    tmpFlag = unify(schemeA, schemeB, stateVector, 0);
    if (tmpFlag) {

      nmbrLet((nmbrString **)(&(*stateVector)[9]),
          (nmbrString *)((*stateVector)[2]));
      nmbrLet((nmbrString **)(&(*stateVector)[10]),
          (nmbrString *)((*stateVector)[3]));
    }
    return (tmpFlag);
  } else {
    while (1) {
      tmpFlag = unify(schemeA, schemeB, stateVector, 1);
      if (!tmpFlag) return (0);

      schemeAUnkVarsLen = ((nmbrString *)((*stateVector)[11]))[2];
      stackUnkVarStart = (nmbrString *)((*stateVector)[2]);
      stackUnkVarLen = (nmbrString *)((*stateVector)[3]);
      oldStackUnkVarStart = (nmbrString *)((*stateVector)[9]);
      oldStackUnkVarLen = (nmbrString *)((*stateVector)[10]);
      for (i = 0; i < schemeAUnkVarsLen; i++) {
        if (stackUnkVarStart[i] != oldStackUnkVarStart[i]) {


          nmbrLet(&oldStackUnkVarStart, stackUnkVarStart);
          nmbrLet(&oldStackUnkVarLen, stackUnkVarLen);
          return (1);
        }
        if (stackUnkVarLen[i] != oldStackUnkVarLen[i]) {


          nmbrLet(&oldStackUnkVarStart, stackUnkVarStart);
          nmbrLet(&oldStackUnkVarLen, stackUnkVarLen);
          return (1);
        }
      }
    }
  }
  return(0);
}



char uniqueUnif(
    nmbrString *schemeA,
    nmbrString *schemeB,
    pntrString **stateVector)
{
  pntrString *saveStateVector = NULL_PNTRSTRING;
  pntrString *pntrTmpPtr1;
  pntrString *pntrTmpPtr2;
  long i, j, k;
  char tmpFlag;

  tmpFlag = unifyH(schemeA, schemeB, stateVector, 0);
  if (!tmpFlag) {
    return (0);
  }
  if (tmpFlag == 2) {
    return (2);
  }


  pntrLet(&saveStateVector,*stateVector);
  if (pntrLen(*stateVector) != 16) bug(1906);
  for (i = 0; i < 4; i++) {

    saveStateVector[i] = NULL_NMBRSTRING;
    nmbrLet((nmbrString **)(&saveStateVector[i]),
        (nmbrString *)((*stateVector)[i]));
  }
  for (i = 4; i <= 7; i++) {

    saveStateVector[i] = NULL_PNTRSTRING;
    pntrLet((pntrString **)(&saveStateVector[i]),
        (pntrString *)((*stateVector)[i]));
  }
  for (i = 8; i < 12; i++) {

    saveStateVector[i] = NULL_NMBRSTRING;
    nmbrLet((nmbrString **)(&saveStateVector[i]),
        (nmbrString *)((*stateVector)[i]));
  }
  for (i = 12; i < 16; i++) {

    saveStateVector[i] = NULL_PNTRSTRING;
    pntrLet((pntrString **)(&saveStateVector[i]),
        (pntrString *)((*stateVector)[i]));
  }
  k = ((nmbrString *)((*stateVector)[11]))[0];
  for (i = 4; i <= 7; i++) {
    pntrTmpPtr1 = (pntrString *)(saveStateVector[i]);
    pntrTmpPtr2 = (pntrString *)((*stateVector)[i]);
    for (j = 0; j < k; j++) {

      pntrTmpPtr1[j] = NULL_NMBRSTRING;
      nmbrLet((nmbrString **)(&pntrTmpPtr1[j]),
          (nmbrString *)(pntrTmpPtr2[j]));
    }
  }
  k = pntrLen((pntrString *)((*stateVector)[12]));
  for (i = 12; i < 16; i++) {
    pntrTmpPtr1 = (pntrString *)(saveStateVector[i]);
    pntrTmpPtr2 = (pntrString *)((*stateVector)[i]);
    for (j = 0; j < k; j++) {

      pntrTmpPtr1[j] = NULL_NMBRSTRING;
      nmbrLet((nmbrString **)(&pntrTmpPtr1[j]),
          (nmbrString *)(pntrTmpPtr2[j]));
    }
  }


  tmpFlag = unifyH(schemeA, schemeB, stateVector, 1);

  if (!tmpFlag) {
    *stateVector = saveStateVector;
    return (1);
  }


  ((nmbrString *)(saveStateVector[11]))[1] = 0;
  for (i = 4; i <= 7; i++) {
    pntrTmpPtr1 = (pntrString *)(saveStateVector[i]);
    for (j = 0; j < ((nmbrString *)(saveStateVector[11]))[0]; j++) {

      nmbrLet((nmbrString **)(&pntrTmpPtr1[j]), NULL_NMBRSTRING);
    }
  }
  for (i = 0; i <= 3; i++) {
    nmbrLet((nmbrString **)(&saveStateVector[i]),
        NULL_NMBRSTRING);
  }
  for (i = 4; i <= 7; i++) {
    pntrLet((pntrString **)(&saveStateVector[i]),
        NULL_PNTRSTRING);
  }
  for (i = 8; i <= 11; i++) {
    nmbrLet((nmbrString **)(&saveStateVector[i]),
        NULL_NMBRSTRING);
  }
  k = pntrLen((pntrString *)(saveStateVector[12]));
  for (i = 12; i < 16; i++) {
    pntrTmpPtr1 = (pntrString *)(saveStateVector[i]);
    for (j = 0; j < k; j++) {
      nmbrLet((nmbrString **)(&pntrTmpPtr1[j]),
          NULL_NMBRSTRING);
    }
    pntrLet((pntrString **)(&saveStateVector[i]),
        NULL_PNTRSTRING);
  }
  pntrLet(&saveStateVector, NULL_PNTRSTRING);

  if (tmpFlag == 2) {
    return (2);
  }

  return (3);

}



void purgeStateVector(pntrString **stateVector) {

  long i, j, k;
  pntrString *pntrTmpPtr1;

  if (!pntrLen(*stateVector)) return;


  ((nmbrString *)((*stateVector)[11]))[1] = 0;
  for (i = 4; i <= 7; i++) {
    pntrTmpPtr1 = (pntrString *)((*stateVector)[i]);
    for (j = 0; j < ((nmbrString *)((*stateVector)[11]))[0]; j++) {

      nmbrLet((nmbrString **)(&pntrTmpPtr1[j]), NULL_NMBRSTRING);
    }
  }
  for (i = 0; i <= 3; i++) {
    nmbrLet((nmbrString **)(&(*stateVector)[i]),
        NULL_NMBRSTRING);
  }
  for (i = 4; i <= 7; i++) {
    pntrLet((pntrString **)(&(*stateVector)[i]),
        NULL_PNTRSTRING);
  }
  for (i = 8; i <= 11; i++) {
    nmbrLet((nmbrString **)(&(*stateVector)[i]),
        NULL_NMBRSTRING);
  }
  k = pntrLen((pntrString *)((*stateVector)[12]));
  for (i = 12; i < 16; i++) {
    pntrTmpPtr1 = (pntrString *)((*stateVector)[i]);
    for (j = 0; j < k; j++) {
      nmbrLet((nmbrString **)(&pntrTmpPtr1[j]),
          NULL_NMBRSTRING);
    }
    pntrLet((pntrString **)(&(*stateVector)[i]),
        NULL_PNTRSTRING);
  }
  pntrLet(&(*stateVector), NULL_PNTRSTRING);

  return;

}



void printSubst(pntrString *stateVector)
{
  long d;
  nmbrString *stackUnkVar;
  nmbrString *unifiedScheme;
  nmbrString *stackUnkVarLen;
  nmbrString *stackUnkVarStart;
  long stackTop;
  vstring tmpStr = "";
  nmbrString *nmbrTmp = NULL_NMBRSTRING;

  stackTop = ((nmbrString *)(stateVector[11]))[1];
  stackUnkVar = (nmbrString *)(stateVector[1]);
  stackUnkVarStart = (nmbrString *)(stateVector[2]);
  stackUnkVarLen = (nmbrString *)(stateVector[3]);
  unifiedScheme = (nmbrString *)(stateVector[8]);

  for (d = 0; d <= stackTop; d++) {
    printLongLine(cat(" Variable '",
        g_MathToken[stackUnkVar[d]].tokenName,"' was replaced with '",
        nmbrCvtMToVString(
            nmbrMid(unifiedScheme,stackUnkVarStart[d] + 1,
            stackUnkVarLen[d])),"'.",NULL),"    "," ");

    let(&tmpStr,"");
    nmbrLet(&nmbrTmp,NULL_NMBRSTRING);
  }
}


char unifyH(
    nmbrString *schemeA,
    nmbrString *schemeB,
    pntrString **stateVector,
    long reEntryFlag)
{
  char tmpFlag;
  nmbrString *hentyVars = NULL_NMBRSTRING;
  nmbrString *hentyVarStart = NULL_NMBRSTRING;
  nmbrString *hentyVarLen = NULL_NMBRSTRING;
  nmbrString *hentySubstList = NULL_NMBRSTRING;


  if (!g_hentyFilter) return unify(schemeA, schemeB, stateVector, reEntryFlag);

  if (!reEntryFlag) {

    tmpFlag = unify(schemeA, schemeB, stateVector, 0);
    if (tmpFlag == 1) {


      hentyNormalize(&hentyVars, &hentyVarStart, &hentyVarLen,
          &hentySubstList, stateVector);


      hentyAdd(hentyVars, hentyVarStart, hentyVarLen,
          hentySubstList, stateVector);

    }
    return (tmpFlag);

  } else {

    while (1) {
      tmpFlag = unify(schemeA, schemeB, stateVector, 1);
      if (tmpFlag == 1) {


        hentyNormalize(&hentyVars, &hentyVarStart, &hentyVarLen,
            &hentySubstList, stateVector);


        if (!hentyMatch(hentyVars, hentyVarStart,
            hentySubstList, stateVector)) {

          hentyAdd(hentyVars, hentyVarStart, hentyVarLen,
              hentySubstList, stateVector);
          return (1);
        }

      } else {

        break;
      }


    }

    nmbrLet(&hentyVars, NULL_NMBRSTRING);
    nmbrLet(&hentyVarStart, NULL_NMBRSTRING);
    nmbrLet(&hentyVarLen, NULL_NMBRSTRING);
    nmbrLet(&hentySubstList, NULL_NMBRSTRING);
    return (tmpFlag);

  }
}


void hentyNormalize(nmbrString **hentyVars, nmbrString **hentyVarStart,
    nmbrString **hentyVarLen, nmbrString **hentySubstList,
    pntrString **stateVector)
{
  long vars, var1, var2, schLen;
  long n, el, rra, rrb, rrc, ir, i, j;
  long totalSubstLen, pos;
  nmbrString *substList = NULL_NMBRSTRING;


  vars = ((nmbrString *)((*stateVector)[11]))[1] + 1;
  nmbrLet((nmbrString **)(&(*hentyVars)), nmbrLeft(
      (nmbrString *)((*stateVector)[1]), vars));
  nmbrLet((nmbrString **)(&(*hentyVarStart)), nmbrLeft(
      (nmbrString *)((*stateVector)[2]), vars));
  nmbrLet((nmbrString **)(&(*hentyVarLen)), nmbrLeft(
      (nmbrString *)((*stateVector)[3]), vars));
  nmbrLet((nmbrString **)(&(*hentySubstList)),
      (nmbrString *)((*stateVector)[8]));

  for (i = 0; i < vars; i++) {
    if ((*hentyVarLen)[i] == 1) {
      var2 = (*hentySubstList)[(*hentyVarStart)[i]];
      if (var2 > g_mathTokens) {

        var1 = (*hentyVars)[i];
        if (var1 > var2) {

          (*hentyVars)[i] = var2;
          schLen = nmbrLen(*hentySubstList);
          for (j = 0; j < schLen; j++) {
            if ((*hentySubstList)[(*hentyVarStart)[i]] == var2) {
              (*hentySubstList)[(*hentyVarStart)[i]] = var1;
            }
          }
        }
      }
    }
  }




  n = vars;
  if (n < 2) goto heapExit;
  el = n / 2 + 1;
  ir = n;
 label10:
  if (el > 1) {
    el = el - 1;
    rra = (*hentyVars)[el - 1];
    rrb = (*hentyVarStart)[el - 1];
    rrc = (*hentyVarLen)[el - 1];
  } else {
    rra = (*hentyVars)[ir - 1];
    rrb = (*hentyVarStart)[ir - 1];
    rrc = (*hentyVarLen)[ir - 1];
    (*hentyVars)[ir - 1] = (*hentyVars)[0];
    (*hentyVarStart)[ir - 1] = (*hentyVarStart)[0];
    (*hentyVarLen)[ir - 1] = (*hentyVarLen)[0];
    ir = ir - 1;
    if (ir == 1) {
      (*hentyVars)[0] = rra;
      (*hentyVarStart)[0] = rrb;
      (*hentyVarLen)[0] = rrc;
      goto heapExit;
    }
  }
  i = el;
  j = el + el;
 label20:
  if (j <= ir) {
    if (j < ir) {
      if ((*hentyVars)[j - 1] < (*hentyVars)[j]) j = j + 1;
    }
    if (rra < (*hentyVars)[j - 1]) {
      (*hentyVars)[i - 1] = (*hentyVars)[j - 1];
      (*hentyVarStart)[i - 1] = (*hentyVarStart)[j - 1];
      (*hentyVarLen)[i - 1] = (*hentyVarLen)[j - 1];
      i = j;
      j = j + j;
    } else {
      j = ir + 1;
    }
    goto label20;
  }
  (*hentyVars)[i - 1] = rra;
  (*hentyVarStart)[i - 1] = rrb;
  (*hentyVarLen)[i - 1] = rrc;
  goto label10;

 heapExit:

  totalSubstLen = 0;
  for (i = 0; i < vars; i++) {
    totalSubstLen = totalSubstLen + (*hentyVarLen)[i];
  }

  nmbrLet(&substList, nmbrSpace(totalSubstLen));

  pos = 0;
  for (i = 0; i < vars; i++) {
    for (j = 0; j < (*hentyVarLen)[i]; j++) {
      substList[pos + j] = (*hentySubstList)[(*hentyVarStart)[i] + j];
    }
    (*hentyVarStart)[i] = pos;
    pos = pos + (*hentyVarLen)[i];
  }
  if (pos != totalSubstLen) bug(1907);
  nmbrLet((nmbrString **)(&(*hentySubstList)), substList);


  nmbrLet(&substList, NULL_NMBRSTRING);

  return;

}


flag hentyMatch(nmbrString *hentyVars, nmbrString *hentyVarStart,
     nmbrString *hentySubstList,
    pntrString **stateVector)
{
  long i, size;

  size = pntrLen((pntrString *)((*stateVector)[12]));

  for (i = 0; i < size; i++) {
    if (nmbrEq(hentyVars,
        (nmbrString *)(((pntrString *)((*stateVector)[12]))[i]))) {
      if (nmbrEq(hentyVarStart,
          (nmbrString *)(((pntrString *)((*stateVector)[13]))[i]))) {

        if (nmbrEq(hentySubstList,
            (nmbrString *)(((pntrString *)((*stateVector)[15]))[i]))) {
          return(1);
        }
      }
    }
  }

  return (0);
}


void hentyAdd(nmbrString *hentyVars, nmbrString *hentyVarStart,
    nmbrString *hentyVarLen, nmbrString *hentySubstList,
    pntrString **stateVector)
{
  long size;
  size = pntrLen((pntrString *)((*stateVector)[12]));

  pntrLet((pntrString **)(&(*stateVector)[12]), pntrAddGElement(
      (pntrString *)((*stateVector)[12])));
  ((pntrString *)((*stateVector)[12]))[size] = hentyVars;
  pntrLet((pntrString **)(&(*stateVector)[13]), pntrAddGElement(
      (pntrString *)((*stateVector)[13])));
  ((pntrString *)((*stateVector)[13]))[size] = hentyVarStart;
  pntrLet((pntrString **)(&(*stateVector)[14]), pntrAddGElement(
      (pntrString *)((*stateVector)[14])));
  ((pntrString *)((*stateVector)[14]))[size] = hentyVarLen;
  pntrLet((pntrString **)(&(*stateVector)[15]), pntrAddGElement(
      (pntrString *)((*stateVector)[15])));
  ((pntrString *)((*stateVector)[15]))[size] =
      hentySubstList;
} 
}