package xd.metamath;

public class mmpfas {












#ifndef INLINE
#define INLINE
#endif

long g_proveStatement = 0;
flag g_proofChangedFlag;



long g_userMaxProveFloat =  50000;

long g_dummyVars = 0;
long g_pipDummyVars = 0;



struct pip_struct g_ProofInProgress = {
    NULL_NMBRSTRING, NULL_PNTRSTRING, NULL_PNTRSTRING, NULL_PNTRSTRING };


void interactiveMatch(long step, long maxEssential)
{
  long matchCount = 0;
  long timeoutCount = 0;
  long essHypCount, hyp;
  vstring matchFlags = "";
  vstring timeoutFlags = "";
  char unifFlag;
  vstring tmpStr1 = "";
  vstring tmpStr4 = "";
  vstring tmpStr2 = "";
  vstring tmpStr3 = "";
  nmbrString *matchList = NULL_NMBRSTRING;
  nmbrString *timeoutList = NULL_NMBRSTRING;
  long stmt, matchListPos, timeoutListPos;

  printLongLine(cat("Step ", str((double)step + 1), ":  ", nmbrCvtMToVString(
      (g_ProofInProgress.target)[step]), null), "  ", " ");
  if (nmbrLen((g_ProofInProgress.user)[step])) {
    printLongLine(cat("Step ", str((double)step + 1), "(user):  ", nmbrCvtMToVString(
        (g_ProofInProgress.user)[step]), null), "  ", " ");
  }


  let(&matchFlags, string(g_proveStatement, 1));

  let(&timeoutFlags, string(g_proveStatement, 1));
  for (stmt = 1; stmt < g_proveStatement; stmt++) {
    if (g_Statement[stmt].type != (char)e_ &&
        g_Statement[stmt].type != (char)f_ &&
        g_Statement[stmt].type != (char)a_ &&
        g_Statement[stmt].type != (char)p_) continue;

    if (maxEssential != -1) {
      essHypCount = 0;
      for (hyp = 0; hyp < g_Statement[stmt].numReqHyp; hyp++) {
        if (g_Statement[g_Statement[stmt].reqHypList[hyp]].type == (char)e_) {
          essHypCount++;
          if (essHypCount > maxEssential) break;
        }
      }
      if (essHypCount > maxEssential) continue;
    }

    unifFlag = checkStmtMatch(stmt, step);
    if (unifFlag) {
      if (unifFlag == 1) {
        matchFlags[stmt] = 2;
        matchCount++;
      } else {
        timeoutFlags[stmt] = 2;
        timeoutCount++;
      }
    }
  }

  if (matchCount == 0 && timeoutCount == 0) {
    mminou.print2("No statements match step %ld.  The proof has an error.\n",
        (long)(step + 1));
    let(&matchFlags, "");
    let(&timeoutFlags, "");
    return;
  }

  static final long MATCH_LIMIT=D.MATCH_LIMIT;
  if (matchCount > MATCH_LIMIT) {
    let(&tmpStr1, cat("There are ", str((double)matchCount), " matches for step ",
      str((double)step + 1), ".  View them (Y, N) <N>? ", null));
    tmpStr2 = cmdInput1(tmpStr1);
    let(&tmpStr1, "");

    if (tmpStr2[0] != 'Y' && tmpStr2[0] != 'y') {
      let(&tmpStr2, "");
      let(&matchFlags, "");
      let(&timeoutFlags, "");
      return;
    }

  }

  nmbrLet(&matchList, nmbrSpace(matchCount));
  matchListPos = 0;
  for (stmt = 1; stmt < g_proveStatement; stmt++) {
    if (matchFlags[stmt] == 2) {
      matchList[matchListPos] = stmt;
      matchListPos++;
    }
  }

  nmbrLet(&timeoutList, nmbrSpace(timeoutCount));
  timeoutListPos = 0;
  for (stmt = 1; stmt < g_proveStatement; stmt++) {
    if (timeoutFlags[stmt] == 2) {
      timeoutList[timeoutListPos] = stmt;
      timeoutListPos++;
    }
  }

  let(&tmpStr1, nmbrCvtRToVString(matchList,

                0,
                0 ));
  let(&tmpStr4, nmbrCvtRToVString(timeoutList,

                0,
                0 ));

  printLongLine(cat("Step ", str((double)step + 1), " matches statements:  ", tmpStr1,
      null), "  ", " ");
  if (timeoutCount) {
    printLongLine(cat("In addition, there were unification timeouts with the",
        " following steps, which may or may not match:  ", tmpStr4, null),
        "  ", " ");
  }

  if (matchCount == 1 && timeoutCount == 0 && maxEssential == -1) {

    matchListPos = 0;
    stmt = matchList[matchListPos];
    mminou.print2("Step %ld was assigned statement %s since it is the only match.\n",
        (long)(step + 1),
        g_Statement[stmt].labelName);
  } else {

    while (true) {
      let(&tmpStr3, cat("What statement to select for step ", str((double)step + 1),
          " (<return> to bypass)? ", null));
      tmpStr2 = cmdInput1(tmpStr3);
      let(&tmpStr3, "");

      if (tmpStr2[0] == 0) {
        let(&tmpStr1, "");
        let(&tmpStr4, "");
        let(&tmpStr2, "");
        let(&matchFlags, "");
        let(&timeoutFlags, "");
        nmbrLet(&matchList, NULL_NMBRSTRING);
        nmbrLet(&timeoutList, NULL_NMBRSTRING);
        return;
      }
      if (!instr(1, cat(" ", tmpStr1, " ", tmpStr4, " ", null),
           cat(" ", tmpStr2, " ", null))) {
        mminou.print2("\"%s\" is not one of the choices.  Try again.\n", tmpStr2);
      } else {
        break;
      }
    }

    for (matchListPos = 0; matchListPos < matchCount; matchListPos++) {
      if (!strcmp(tmpStr2, g_Statement[matchList[matchListPos]].labelName)) break;
    }
    if (matchListPos < matchCount) {
      stmt = matchList[matchListPos];
    } else {
      for (timeoutListPos = 0; timeoutListPos < timeoutCount;
          timeoutListPos++) {
      if (!strcmp(tmpStr2, g_Statement[timeoutList[timeoutListPos]].labelName))
          break;
      }
      if (timeoutListPos == timeoutCount) bug(1801);
      stmt = timeoutList[timeoutListPos];
    }
    mminou.print2("Step %ld was assigned statement %s.\n",
        (long)(step + 1),
        g_Statement[stmt].labelName);

  }


  assignStatement(matchList[matchListPos], step);
  g_proofChangedFlag = 1;

  let(&tmpStr1, "");
  let(&tmpStr4, "");
  let(&tmpStr2, "");
  let(&matchFlags, "");
  let(&timeoutFlags, "");
  nmbrLet(&matchList, NULL_NMBRSTRING);
  nmbrLet(&timeoutList, NULL_NMBRSTRING);
  return;

}




void assignStatement(long statemNum, long step)
{
  long hyp;
  nmbrString *hypList = NULL_NMBRSTRING;

  if ((g_ProofInProgress.proof)[step] != -(long)'?') bug(1802);


  nmbrLet(&hypList, nmbrSpace(g_Statement[statemNum].numReqHyp + 1));
  for (hyp = 0; hyp < g_Statement[statemNum].numReqHyp; hyp++) {

    hypList[hyp] = -(long)'?';
  }
  hypList[g_Statement[statemNum].numReqHyp] = statemNum;
  addSubProof(hypList, step);
  initStep(step + g_Statement[statemNum].numReqHyp);
  nmbrLet(&hypList, NULL_NMBRSTRING);
  return;
}




nmbrString *proveByReplacement(long prfStmt,
    long prfStep,
    flag noDistinct,
    flag dummyVarFlag,
    flag searchMethod,
    long improveDepth,

    flag overrideFlag,

    flag mathboxFlag
    )
{

  long trialStmt;
  nmbrString *prfMath;
  nmbrString *trialPrf = NULL_NMBRSTRING;
  long prfMbox;

  prfMath = (g_ProofInProgress.target)[prfStep];
  prfMbox = getMathboxNum(prfStmt);
  for (trialStmt = 1; trialStmt < prfStmt; trialStmt++) {

    if (quickMatchFilter(trialStmt, prfMath, dummyVarFlag) == 0) continue;



    if (overrideFlag == 0 && getMarkupFlag(trialStmt, USAGE_DISCOURAGED)) {
      continue;
    }


    if (mathboxFlag == 0 && prfMbox >= 2) {

      if (trialStmt > g_mathboxStmt && trialStmt < g_mathboxStart[prfMbox - 1]) {
        continue;
      }
    }


    if (noDistinct) {
      if (nmbrLen(g_Statement[trialStmt].reqDisjVarsA)) {
        continue;
      }
    }

    trialPrf = replaceStatement(trialStmt, prfStep,
        prfStmt, 0,
        noDistinct,
        searchMethod,
        improveDepth,
        overrideFlag,
        mathboxFlag
        );
    if (nmbrLen(trialPrf) > 0) {




      if (overrideFlag == 1 && getMarkupFlag(trialStmt, USAGE_DISCOURAGED)) {

        mminou.print2(
          ">>> ?Warning: Overriding discouraged usage of statement \"%s\".\n",
            g_Statement[trialStmt].labelName);

      }

      return trialPrf;
    }


  }
  return trialPrf;
}


nmbrString *replaceStatement(long replStatemNum, long prfStep,
    long provStmtNum,
    flag subProofFlag,
    flag noDistinct,
    flag searchMethod,
    long improveDepth,

    flag overrideFlag,
    flag mathboxFlag
    ) {
  nmbrString *prfMath;
  long reqHyps;
  long hyp, sym, var, i, j, k, trialStep;
  nmbrString *proof = NULL_NMBRSTRING;
  nmbrString *scheme = NULL_NMBRSTRING;
  pntrString *hypList = NULL_PNTRSTRING;
  nmbrString *hypSortMap = NULL_NMBRSTRING;
  pntrString *hypProofList = NULL_PNTRSTRING;
  pntrString *stateVector = NULL_PNTRSTRING;
  nmbrString *replStmtSchemePtr;
  nmbrString *hypSchemePtr;
  nmbrString *hypProofPtr;
  nmbrString *makeSubstPtr;
  pntrString *hypMakeSubstList = NULL_PNTRSTRING;
  pntrString *hypStateVectorList = NULL_PNTRSTRING;
  vstring hypReEntryFlagList = "";
  nmbrString *hypStepList = NULL_NMBRSTRING;
  flag reEntryFlag;
  flag tmpFlag;
  flag noHypMatch;
  flag foundTrialStepMatch;
  long replStmtSchemeLen, schemeVars, schReqHyps, hypLen, reqVars,
      schEHyps, subPfLen;
  long saveUnifTrialCount;
  flag reenterFFlag;
  flag dummyVarFlag;
  nmbrString *hypTestPtr;
  flag hypOrSubproofFlag;
  vstring indepKnownSteps = "";

  long pfLen;
  long scanLen;
  long scanUpperBound;
  long scanLowerBound;
  vstring hasFloatingProof = "";

  vstring tryFloatingProofLater = "";
  flag hasDummyVar;


  if (overrideFlag == 0 && getMarkupFlag(replStatemNum, USAGE_DISCOURAGED)) {
    bug(1868);
  }

  trialStep = 0;

  prfMath = (g_ProofInProgress.target)[prfStep];
  if (subProofFlag) {
    subPfLen = subproofLen(g_ProofInProgress.proof, prfStep);
    scanLen = subPfLen;
    scanUpperBound = prfStep;
    scanLowerBound = scanUpperBound - scanLen + 1;
  } else {
    pfLen = nmbrLen(g_ProofInProgress.proof);

    scanUpperBound = pfLen - 1;
    scanLowerBound = 0;
  }

  if (g_Statement[replStatemNum].type != (char)a_ &&
      g_Statement[replStatemNum].type != (char)p_)
    bug(1822);

  schReqHyps = g_Statement[replStatemNum].numReqHyp;
  reqVars = nmbrLen(g_Statement[replStatemNum].reqVarList);


  let(&hasFloatingProof, string(schReqHyps, ' '));
  let(&tryFloatingProofLater, string(schReqHyps, ' '));
  replStmtSchemePtr = g_Statement[replStatemNum].mathString;
  replStmtSchemeLen = nmbrLen(replStmtSchemePtr);


  nmbrLet(&scheme, replStmtSchemePtr);
  schemeVars = reqVars;
  if (schemeVars + g_pipDummyVars > g_dummyVars) {

    declareDummyVars(schemeVars + g_pipDummyVars - g_dummyVars);
  }
  for (var = 0; var < schemeVars; var++) {

    g_MathToken[g_Statement[replStatemNum].reqVarList[var]].tmp
        = g_mathTokens + 1 + g_pipDummyVars + var;
  }
  for (sym = 0; sym < replStmtSchemeLen; sym++) {
    if (g_MathToken[replStmtSchemePtr[sym]].tokenType != (char)var_) continue;

    scheme[sym] = g_MathToken[replStmtSchemePtr[sym]].tmp;
  }


  pntrLet(&hypList, pntrNSpace(schReqHyps));
  nmbrLet(&hypSortMap, nmbrSpace(schReqHyps));
  pntrLet(&hypProofList, pntrNSpace(schReqHyps));

  for (hyp = 0; hyp < schReqHyps; hyp++) {
    hypSchemePtr = NULL_NMBRSTRING;
    nmbrLet(&hypSchemePtr,
      g_Statement[g_Statement[replStatemNum].reqHypList[hyp]].mathString);
    hypLen = nmbrLen(hypSchemePtr);
    for (sym = 0; sym < hypLen; sym++) {
      if (g_MathToken[hypSchemePtr[sym]].tokenType
          != (char)var_) continue;

      hypSchemePtr[sym] = g_MathToken[hypSchemePtr[sym]].tmp;
    }
    hypList[hyp] = hypSchemePtr;
    hypSortMap[hyp] = hyp;
  }


  schEHyps = 0;
  for (hyp = 0; hyp < schReqHyps; hyp++) {
    if (g_Statement[g_Statement[replStatemNum].reqHypList[hypSortMap[hyp]]].type
         == (char)e_) {
      j = hypSortMap[hyp];
      hypSortMap[hyp] = hypSortMap[schEHyps];
      hypSortMap[schEHyps] = j;
      schEHyps++;
    }
  }

  for (hyp = 0; hyp < schEHyps; hyp++) {

    for (i = hyp + 1; i < schEHyps; i++) {
      if (nmbrLen(hypList[hypSortMap[i]]) > nmbrLen(hypList[hypSortMap[hyp]])) {
        j = hypSortMap[hyp];
        hypSortMap[hyp] = hypSortMap[i];
        hypSortMap[i] = j;
      }
    }
  }

  if (!subProofFlag) {
    let(&indepKnownSteps, "");
    indepKnownSteps = getIndepKnownSteps(provStmtNum, prfStep);
  }


  pntrLet(&hypStateVectorList, pntrPSpace(schReqHyps));



  let(&hypReEntryFlagList, string(schReqHyps, 1));

  nmbrLet(&hypStepList, nmbrSpace(schReqHyps));

  pntrLet(&hypMakeSubstList, pntrNSpace(schReqHyps));


  g_unifTrialCount = 1;
  reEntryFlag = 0;


  reqHyps = g_Statement[provStmtNum].numReqHyp;

  while (true) {
    tmpFlag = unifyH(scheme, prfMath, &stateVector, reEntryFlag);
    if (!tmpFlag) break;
    if (tmpFlag == 2) {
      mminou.print2(
"Unification timed out.  Larger SET UNIFICATION_TIMEOUT may improve results.\n");
      g_unifTrialCount = 1;
      break;
    }

    reEntryFlag = 1;

    nmbrLet(&proof, NULL_NMBRSTRING);
    noHypMatch = 0;
    for (hyp = 0; hyp < schReqHyps; hyp++) {


      nmbrLet((nmbrString **)(&(hypMakeSubstList[hypSortMap[hyp]])),
          NULL_NMBRSTRING);
      hypMakeSubstList[hypSortMap[hyp]] =
          makeSubstUnif(&dummyVarFlag, hypList[hypSortMap[hyp]],
          stateVector);


      hasFloatingProof[hyp] = 'N';
      tryFloatingProofLater[hyp] = 'N';


      for (i = 0; i < hyp; i++) {
        if (i >= schEHyps) break;

        if (hasFloatingProof[i] == 'Y') continue;

        makeSubstPtr = makeSubstUnif(&dummyVarFlag,
            hypMakeSubstList[hypSortMap[hyp]],
            hypStateVectorList[hypSortMap[i]]);
        nmbrLet((nmbrString **)(&(hypMakeSubstList[hypSortMap[hyp]])),
            NULL_NMBRSTRING);
        hypMakeSubstList[hypSortMap[hyp]] = makeSubstPtr;
      }

      if (hyp < schEHyps) {

        if (g_Statement[g_Statement[replStatemNum].reqHypList[hypSortMap[hyp]]
            ].type != (char)e_) bug (1823);
      } else {

        if (g_Statement[g_Statement[replStatemNum].reqHypList[hypSortMap[hyp]]
             ].type != (char)f_) bug(1824);
      }


      foundTrialStepMatch = 0;
      reenterFFlag = 0;
      if (hypReEntryFlagList[hypSortMap[hyp]] == 2) {
        trialStep = hypStepList[hypSortMap[hyp]];

        if (hyp >= schEHyps
            || hasFloatingProof[hyp] == 'Y'
            ) {
          reenterFFlag = 1;
        }
      } else {
        if (hypReEntryFlagList[hypSortMap[hyp]] == 1) {


          trialStep = scanLowerBound;

          trialStep = trialStep - reqHyps;
        } else {
          if (hypReEntryFlagList[hypSortMap[hyp]] == 3) {
            hypReEntryFlagList[hypSortMap[hyp]] = 1;
            reenterFFlag = 1;

            trialStep = scanUpperBound;
          } else {
            bug(1826);
          }
        }
      }


      for (trialStep = trialStep + 0; trialStep < scanUpperBound;
          trialStep++) {


        if (trialStep < scanLowerBound) {

          hypOrSubproofFlag = 0;


          hypTestPtr =
              g_Statement[g_Statement[provStmtNum].reqHypList[

              trialStep - (scanLowerBound - reqHyps)]].mathString;

        } else {

          hypOrSubproofFlag = 1;

          hypTestPtr = (g_ProofInProgress.target)[trialStep];




          if (!subProofFlag) {
            if (indepKnownSteps[trialStep] != 'Y') {
              if (indepKnownSteps[trialStep] != 'N') bug(1836);
              continue;
            }
          }
        }


        if (!dummyVarFlag) {
          if (!nmbrEq(hypTestPtr, hypMakeSubstList[hypSortMap[hyp]])) {
            continue;
          }
        }



        i = hypTestPtr[0];
        j = ((nmbrString *)(hypMakeSubstList[hypSortMap[hyp]]))[0];
        if (g_MathToken[i].tokenType == (char)con_) {
          if (g_MathToken[j].tokenType == (char)con_) {
            if (i != j) {
              continue;
            }
          }
        }

        g_unifTrialCount = 1;
        if (hypReEntryFlagList[hypSortMap[hyp]] < 1
            || hypReEntryFlagList[hypSortMap[hyp]] > 2)
          bug(1851);
        tmpFlag = unifyH(hypMakeSubstList[hypSortMap[hyp]],
            hypTestPtr,
            (pntrString **)(&(hypStateVectorList[hypSortMap[hyp]])),

            hypReEntryFlagList[hypSortMap[hyp]] - 1);
        if (!tmpFlag || tmpFlag == 2) {

          if (tmpFlag == 2) {
            mminou.print2(
"Unification timed out.  SET UNIFICATION_TIMEOUT larger for better results.\n");
            g_unifTrialCount = 1;

            purgeStateVector(
                (pntrString **)(&(hypStateVectorList[hypSortMap[hyp]])));
          }

          if (!dummyVarFlag) {
            if (hypReEntryFlagList[hypSortMap[hyp]] - 1 == 1) {

              trialStep = scanUpperBound - 1;

            }
          }

          hypReEntryFlagList[hypSortMap[hyp]] = 1;
          continue;
        } else {


          if (tmpFlag != 1) bug(1828);


          if (hypReEntryFlagList[hypSortMap[hyp]] == 1

              ) {
            j = 0;

            for (i = scanLowerBound - reqHyps; i < trialStep; i++) {


              if (i < scanLowerBound) {

                if (nmbrEq(hypTestPtr,
                    g_Statement[g_Statement[provStmtNum].reqHypList[

                    i - (scanLowerBound - reqHyps)]].mathString)) {

                  j = 1;
                  break;
                }
              } else {

                if (nmbrEq(hypTestPtr,
                    (g_ProofInProgress.target)[i])) {
                  j = 1;
                  break;
                }
              }
            }
            if (j) {

              purgeStateVector(
                  (pntrString **)(&(hypStateVectorList[hypSortMap[hyp]])));
              continue;
            }
          }



          hypReEntryFlagList[hypSortMap[hyp]] = 2;
          hypStepList[hypSortMap[hyp]] = trialStep;

          if (!hypOrSubproofFlag) {

            nmbrLet((nmbrString **)(&hypProofList[hypSortMap[hyp]]),
                nmbrAddElement(NULL_NMBRSTRING,
                g_Statement[provStmtNum].reqHypList[

                trialStep - (scanLowerBound - reqHyps)]));
          } else {

            i = subproofLen(g_ProofInProgress.proof, trialStep);
            nmbrLet((nmbrString **)(&hypProofList[hypSortMap[hyp]]),
                nmbrSeg(g_ProofInProgress.proof, trialStep - i + 2,
                trialStep + 1));
          }

          foundTrialStepMatch = 1;
          break;
        }
      }

      if (!foundTrialStepMatch) {
        hasDummyVar = 0;
        hypLen = nmbrLen(hypMakeSubstList[hypSortMap[hyp]]);
        for (sym = 0; sym < hypLen; sym++) {
          k = ((nmbrString *)(hypMakeSubstList[hypSortMap[hyp]]))[sym];
          if (k > g_mathTokens) {
            hasDummyVar = 1;
            break;
          }
        }

        if ((hyp >= schEHyps || searchMethod == 1)
             && !reenterFFlag
             ) {

          if (hasDummyVar) {
            if (hyp < schEHyps) {
              tryFloatingProofLater[hyp] = 'Y';
              tmpFlag = unifyH(hypMakeSubstList[hypSortMap[hyp]],
                  hypMakeSubstList[hypSortMap[hyp]],
                  (pntrString **)(&(hypStateVectorList[hypSortMap[hyp]])),

                  hypReEntryFlagList[hypSortMap[hyp]] - 1);
              if (tmpFlag != 1) bug (1849);
              foundTrialStepMatch = 1;
            }
          } else {
            saveUnifTrialCount = g_unifTrialCount;
            hypProofPtr =
                proveFloating(hypMakeSubstList[hypSortMap[hyp]],
                    provStmtNum, improveDepth, prfStep, noDistinct,
                    overrideFlag, mathboxFlag
                    );
            g_unifTrialCount = saveUnifTrialCount;
            if (nmbrLen(hypProofPtr)) {
              nmbrLet((nmbrString **)(&hypProofList[hypSortMap[hyp]]),
                  NULL_NMBRSTRING);
              hypProofList[hypSortMap[hyp]] = hypProofPtr;
              foundTrialStepMatch = 1;
              hypReEntryFlagList[hypSortMap[hyp]] = 3;

              hasFloatingProof[hyp] = 'Y';
            }
          }
        }
      }

      if (hyp == schEHyps - 1 && foundTrialStepMatch) {

        for (i = 0; i < schEHyps; i++) {
          if (tryFloatingProofLater[i] == 'Y') {

            for (j = i + 1; j < schEHyps; j++) {
              if (hasFloatingProof[j] == 'Y') continue;
              makeSubstPtr = makeSubstUnif(&dummyVarFlag,
                  hypMakeSubstList[hypSortMap[i]],
                  hypStateVectorList[hypSortMap[j]]);
              nmbrLet((nmbrString **)(&(hypMakeSubstList[hypSortMap[i]])),
                  NULL_NMBRSTRING);
              hypMakeSubstList[hypSortMap[i]] = makeSubstPtr;
            }

            hasDummyVar = 0;
            hypLen = nmbrLen(hypMakeSubstList[hypSortMap[i]]);
            for (sym = 0; sym < hypLen; sym++) {
              k = ((nmbrString *)(hypMakeSubstList[hypSortMap[i]]))[sym];
              if (k > g_mathTokens) {
                hasDummyVar = 1;
                break;
              }
            }
            if (hasDummyVar) {
              foundTrialStepMatch = 0;
              hyp = 0;
              break;
            }
            saveUnifTrialCount = g_unifTrialCount;
            hypProofPtr =
                proveFloating(hypMakeSubstList[hypSortMap[i]],
                    provStmtNum, improveDepth, prfStep, noDistinct,
                    overrideFlag,
                    mathboxFlag
                    );
            g_unifTrialCount = saveUnifTrialCount;
            if (nmbrLen(hypProofPtr)) {
              nmbrLet((nmbrString **)(&hypProofList[hypSortMap[i]]),
                  NULL_NMBRSTRING);
              hypProofList[hypSortMap[i]] = hypProofPtr;
              foundTrialStepMatch = 1;
              hypReEntryFlagList[hypSortMap[i]] = 3;

              hasFloatingProof[i] = 'Y';
            } else {
              foundTrialStepMatch = 0;
              hyp = 0;
              break;
            }
          }
        }
      }

      if (!foundTrialStepMatch) {



        for (i = hyp - 1; i >=0; i--) {
          if (tryFloatingProofLater[i] == 'N') break;
          if (tryFloatingProofLater[i] != 'Y') bug(1853);
          hyp--;
        }

        if (hyp == 0) {

          noHypMatch = 1;
          break;
        }
        hyp = hyp - 2;
      }

    }

    if (noHypMatch) {

      continue;
    }




    for (hyp = 0; hyp < schReqHyps; hyp++) {
      if (nmbrLen(hypProofList[hyp]) == 0) bug(1852);
      nmbrLet(&proof, nmbrCat(proof, hypProofList[hyp], null));
    }
    nmbrLet(&proof, nmbrAddElement(proof, replStatemNum));



    C.go2("returnPoint");

  }

  nmbrLet(&proof, NULL_NMBRSTRING);

    C.label("returnPoint");


  for (hyp = 0; hyp < schReqHyps; hyp++) {
    nmbrLet((nmbrString **)(&(hypList[hyp])), NULL_NMBRSTRING);
    nmbrLet((nmbrString **)(&(hypProofList[hyp])), NULL_NMBRSTRING);
    nmbrLet((nmbrString **)(&(hypMakeSubstList[hyp])), NULL_NMBRSTRING);
    purgeStateVector((pntrString **)(&(hypStateVectorList[hyp])));
  }


  purgeStateVector(&stateVector);

  nmbrLet(&scheme, NULL_NMBRSTRING);
  pntrLet(&hypList, NULL_PNTRSTRING);
  nmbrLet(&hypSortMap, NULL_NMBRSTRING);
  pntrLet(&hypProofList, NULL_PNTRSTRING);
  pntrLet(&hypMakeSubstList, NULL_PNTRSTRING);
  pntrLet(&hypStateVectorList, NULL_PNTRSTRING);
  let(&hypReEntryFlagList, "");
  nmbrLet(&hypStepList, NULL_NMBRSTRING);
  let(&indepKnownSteps, "");
  let(&hasFloatingProof, "");
  let(&tryFloatingProofLater, "");


if(db8)mminou.print2("%s\n", cat("Returned: ",
   nmbrCvtRToVString(proof,

                0,
                0 ), null));
  return (proof);
}




vstring getIndepKnownSteps(long proofStmt, long refStep)
{
  long proofLen, prfStep, step2;
  long wrkSubPfLen, mathLen;
  nmbrString *proofStepContent;
  vstring indepSteps = "";
  vstring unkSubPrfSteps = "";


  proofLen = nmbrLen(g_ProofInProgress.proof);

  let(&indepSteps, string(proofLen, 'N'));


  for (prfStep = proofLen - 2 ; prfStep >= 0;
      prfStep--) {
    wrkSubPfLen = subproofLen(g_ProofInProgress.proof, prfStep);
    if (prfStep >= refStep && prfStep - wrkSubPfLen + 1 <= refStep) {

      continue;
    }

    for (step2 = prfStep - wrkSubPfLen + 1; step2 <= prfStep; step2++) {
      if (indepSteps[step2] == 'Y') bug(1832);
      indepSteps[step2] = 'Y';
    }

    prfStep = prfStep - wrkSubPfLen + 1;

  }

  for (prfStep = 0; prfStep < proofLen; prfStep++) {
    if (indepSteps[prfStep] == 'N') continue;


    proofStmt = (g_ProofInProgress.proof)[prfStep];
    if (proofStmt < 0) {
      if (proofStmt == -(long)'?') {

      } else {
        bug(1833);
      }
    } else {
      if (g_Statement[proofStmt].type == (char)e_
          || g_Statement[proofStmt].type == (char)f_) {

        indepSteps[prfStep] = 'N';
      } else if (g_Statement[proofStmt].type != (char)p_
            && g_Statement[proofStmt].type != (char)a_) {
        bug(1834);
      }
    }

    if (indepSteps[prfStep] == 'N') continue;




    proofStepContent = (g_ProofInProgress.target)[prfStep];

    mathLen = nmbrLen(proofStepContent);
    if (mathLen == 0) bug(1835);
    for (mathLen = mathLen - 1; mathLen >= 0; mathLen--) {
      if (((nmbrString *)proofStepContent)[mathLen] >
          g_mathTokens) {

        indepSteps[prfStep] = 'N';
        break;
      }
    }
  }


  unkSubPrfSteps = getKnownSubProofs();

  for (prfStep = 0; prfStep < proofLen; prfStep++) {
    if (unkSubPrfSteps[prfStep] == 'U') indepSteps[prfStep] = 'N';
  }

  let(&unkSubPrfSteps, "");

  return indepSteps;

}




vstring getKnownSubProofs()
{
  long proofLen, hyp;
  vstring unkSubPrfSteps = "";
  vstring unkSubPrfStack = "";
  long stackPtr, prfStep, stmt;


  proofLen = nmbrLen(g_ProofInProgress.proof);


  let(&unkSubPrfSteps, space(proofLen));
  let(&unkSubPrfStack, space(proofLen));
  stackPtr = -1;
  for (prfStep = 0; prfStep < proofLen; prfStep++) {
    stmt = (g_ProofInProgress.proof)[prfStep];
    if (stmt < 0) {
      if (stmt != -(long)'?') bug(1837);
      unkSubPrfSteps[prfStep] = 'U';
      stackPtr++;
      unkSubPrfStack[stackPtr] = 'U';
      continue;
    }
    if (g_Statement[stmt].type == (char)e_ ||
        g_Statement[stmt].type == (char)f_) {
      unkSubPrfSteps[prfStep] = 'K';
      stackPtr++;
      unkSubPrfStack[stackPtr] = 'K';
      continue;
    }
    if (g_Statement[stmt].type != (char)a_ &&
        g_Statement[stmt].type != (char)p_) bug(1838);
    unkSubPrfSteps[prfStep] = 'K';
    for (hyp = 1; hyp <= g_Statement[stmt].numReqHyp; hyp++) {
      if (stackPtr < 0) bug(1839);
      if (unkSubPrfStack[stackPtr] == 'U') {

        unkSubPrfSteps[prfStep] = 'U';
      }
      stackPtr--;
    }
    stackPtr++;
    if (stackPtr < 0) bug(1840);
    unkSubPrfStack[stackPtr] = unkSubPrfSteps[prfStep];
  }
  if (stackPtr != 0) bug(1841);
  let(&unkSubPrfStack, "");
  return unkSubPrfSteps;

}



void addSubProof(nmbrString *subProof, long step) {
  long sbPfLen;

  if ((g_ProofInProgress.proof)[step] != -(long)'?') bug(1803);

  sbPfLen = nmbrLen(subProof);
  nmbrLet(&g_ProofInProgress.proof, nmbrCat(nmbrLeft(g_ProofInProgress.proof, step),
      subProof, nmbrRight(g_ProofInProgress.proof, step + 2), null));
  pntrLet(&g_ProofInProgress.target, pntrCat(pntrLeft(g_ProofInProgress.target,
      step), pntrNSpace(sbPfLen - 1), pntrRight(g_ProofInProgress.target,
      step + 1), null));

  if (nmbrLen((g_ProofInProgress.source)[step])) bug(1804);

  pntrLet(&g_ProofInProgress.source, pntrCat(pntrLeft(g_ProofInProgress.source,
      step), pntrNSpace(sbPfLen - 1), pntrRight(g_ProofInProgress.source,
      step + 1), null));
  pntrLet(&g_ProofInProgress.user, pntrCat(pntrLeft(g_ProofInProgress.user,
      step), pntrNSpace(sbPfLen - 1), pntrRight(g_ProofInProgress.user,
      step + 1), null));
}



nmbrString *expandProof(
    nmbrString *rawTargetProof,
    long sourceStmtNum
    ) {
  nmbrString *origTargetProof = NULL_NMBRSTRING;
  nmbrString *targetProof = NULL_NMBRSTRING;
  nmbrString *sourceProof = NULL_NMBRSTRING;
  nmbrString *expandedTargetProof = NULL_NMBRSTRING;
  pntrString *hypSubproofs = NULL_PNTRSTRING;
  nmbrString *expandedSubproof = NULL_NMBRSTRING;
  long targetStep, srcHyp, hypStep, totalSubpLen, subpLen, srcStep;
  long sourcePLen, sourceHyps, targetPLen, targetSubpLen;
  flag hasDummyVar = 0;
  flag hasUnknownStep = 0;
  char srcStepType;
  long srcHypNum;
  flag foundMatch;

  sourceProof = getProof(sourceStmtNum, 0);
  nmbrLet(&sourceProof, nmbrUnsquishProof(sourceProof));
  nmbrLet(&origTargetProof, nmbrUnsquishProof(rawTargetProof));
  nmbrLet(&expandedTargetProof, origTargetProof);
  sourcePLen = nmbrLen(sourceProof);
  sourceHyps = nmbrLen(g_Statement[sourceStmtNum].reqHypList);
  pntrLet(&hypSubproofs, pntrNSpace(sourceHyps));
  if (g_Statement[sourceStmtNum].type != (char)p_) {

    bug(1871);
    nmbrLet(&expandedTargetProof, targetProof);
    C.go2("RETURN_POINT");
  }

  while (true) {
    nmbrLet(&targetProof, expandedTargetProof);
    targetPLen = nmbrLen(targetProof);
    foundMatch = 0;
    for (targetStep = targetPLen - 1; targetStep >= 0; targetStep--) {
      if (targetProof[targetStep] != sourceStmtNum) continue;
      foundMatch = 1;

      targetSubpLen = subproofLen(targetProof, targetStep);


      hypStep = targetStep - 1;
      totalSubpLen = 0;
      for (srcHyp = sourceHyps - 1; srcHyp >= 0; srcHyp--) {
        subpLen = subproofLen(targetProof, hypStep);

        nmbrLet((nmbrString **)(&(hypSubproofs[srcHyp])),
          nmbrMid(targetProof, (hypStep + 1) - (subpLen - 1), subpLen));

        hypStep = hypStep - subpLen;
        totalSubpLen = totalSubpLen + subpLen;
      }
      if (totalSubpLen != targetSubpLen - 1) {
        bug(1872);
        nmbrLet(&expandedTargetProof, targetProof);
        C.go2("RETURN_POINT");
      }


      nmbrLet(&expandedSubproof, NULL_NMBRSTRING);

      for (srcStep = 0; srcStep < sourcePLen; srcStep++) {

        if (sourceProof[srcStep] < 0) {
          if (sourceProof[srcStep] == -(long)'?') {
            hasUnknownStep = 1;
          } else {
            bug(1873);
          }

          nmbrLet(&expandedSubproof, nmbrAddElement(expandedSubproof,
              -(long)'?'));
          continue;
        }
        srcStepType = g_Statement[sourceProof[srcStep]].type;
        if (srcStepType == (char)e_ || srcStepType == (char)f_) {
          srcHypNum = -1;
          for (srcHyp = 0; srcHyp < sourceHyps; srcHyp++) {

            if ((g_Statement[sourceStmtNum].reqHypList)[srcHyp]
                == sourceProof[srcStep]) {
              srcHypNum = srcHyp;
              break;
            }
          }
          if (srcHypNum > -1) {

            nmbrLet(&expandedSubproof, nmbrCat(expandedSubproof,
                hypSubproofs[srcHypNum], null));
          } else if (srcStepType == (char)e_) {

            bug(1874);
          } else if (srcStepType == (char)f_) {
            hasDummyVar = 1;
            nmbrLet(&expandedSubproof, nmbrAddElement(expandedSubproof,
                -(long)'?'));
          }
        } else if (srcStepType != (char)a_ && srcStepType != (char)p_) {
          bug(1875);
        } else {

          nmbrLet(&expandedSubproof, nmbrAddElement(expandedSubproof,
              sourceProof[srcStep]));
        }
      }

      nmbrLet(&expandedTargetProof, nmbrCat(
          nmbrLeft(expandedTargetProof, (targetStep + 1) - targetSubpLen),
          expandedSubproof,
          nmbrRight(expandedTargetProof, (targetStep + 1) + 1), null));
      break;
    }
    if (!foundMatch) break;
  }


    C.label("RETURN_POINT");
  if (nmbrEq(origTargetProof, expandedTargetProof)) {
  } else {
    if (hasDummyVar == 1) {
      printLongLine(cat(
      "******* Note: The expansion of \"",
      g_Statement[sourceStmtNum].labelName,
      "\" has dummy variable(s) that need to be assigned.", null), " ", " ");
    }
    if (hasUnknownStep == 1) {
      printLongLine(cat(
      "******* Note: The expansion of \"",
      g_Statement[sourceStmtNum].labelName,
      "\" has unknown step(s) that need to be assigned.", null), " ", " ");
    }
  }

  nmbrLet(&sourceProof, NULL_NMBRSTRING);
  nmbrLet(&origTargetProof, NULL_NMBRSTRING);
  nmbrLet(&targetProof, NULL_NMBRSTRING);
  nmbrLet(&expandedSubproof, NULL_NMBRSTRING);

  for (srcHyp = 0; srcHyp < sourceHyps; srcHyp++) {
    nmbrLet((nmbrString **)(&(hypSubproofs[srcHyp])), NULL_NMBRSTRING);
  }

  pntrLet(&hypSubproofs, NULL_PNTRSTRING);
  return expandedTargetProof;
}


void deleteSubProof(long step) {
  long sbPfLen, pos;


  if ((g_ProofInProgress.proof)[step] == -(long)'?') return;


  sbPfLen = subproofLen(g_ProofInProgress.proof, step);
  nmbrLet(&g_ProofInProgress.proof, nmbrCat(nmbrAddElement(
      nmbrLeft(g_ProofInProgress.proof, step - sbPfLen + 1), -(long)'?'),
      nmbrRight(g_ProofInProgress.proof, step + 2), null));
  for (pos = step - sbPfLen + 1; pos <= step; pos++) {
    if (pos < step) {

      nmbrLet((nmbrString **)(&((g_ProofInProgress.target)[pos])), NULL_NMBRSTRING);
      nmbrLet((nmbrString **)(&((g_ProofInProgress.user)[pos])), NULL_NMBRSTRING);
    }

    nmbrLet((nmbrString **)(&((g_ProofInProgress.source)[pos])), NULL_NMBRSTRING);
  }
  pntrLet(&g_ProofInProgress.target, pntrCat(pntrLeft(g_ProofInProgress.target,
      step - sbPfLen + 1), pntrRight(g_ProofInProgress.target,
      step + 1), null));
  pntrLet(&g_ProofInProgress.source, pntrCat(pntrLeft(g_ProofInProgress.source,
      step - sbPfLen + 1), pntrRight(g_ProofInProgress.source,
      step + 1), null));
  pntrLet(&g_ProofInProgress.user, pntrCat(pntrLeft(g_ProofInProgress.user,
      step - sbPfLen + 1), pntrRight(g_ProofInProgress.user,
      step + 1), null));
}


char checkStmtMatch(long statemNum, long step)
{
  char targetFlag;
  char userFlag = 1;
  pntrString *stateVector = NULL_PNTRSTRING;
  nmbrString *mString;
  nmbrString *scheme = NULL_NMBRSTRING;
  long targetLen, mStringLen, reqVars, stsym, tasym, sym, var, hyp, numHyps;
  flag breakFlag;
  flag firstSymbsAreConstsFlag;


  targetLen = nmbrLen((g_ProofInProgress.target)[step]);
  if (!targetLen) bug(1807);


  if (g_Statement[statemNum].type == (char)e_ || g_Statement[statemNum].type ==
      (char)f_) {


    breakFlag = 0;
    numHyps = g_Statement[g_proveStatement].numReqHyp;
    for (hyp = 0; hyp < numHyps; hyp++) {
      if (g_Statement[g_proveStatement].reqHypList[hyp] == statemNum) {
        breakFlag = 1;
        break;
      }
    }
    if (!breakFlag) {
      numHyps = nmbrLen(g_Statement[g_proveStatement].optHypList);
      for (hyp = 0; hyp < numHyps; hyp++) {
        if (g_Statement[g_proveStatement].optHypList[hyp] == statemNum) {
          breakFlag = 1;
          break;
        }
      }
      if (!breakFlag) {
        targetFlag = 0;
        C.go2("returnPoint");
      }
    }

    g_unifTrialCount = 1;
    targetFlag = unifyH((g_ProofInProgress.target)[step],
        g_Statement[statemNum].mathString, &stateVector, 0);
   if (nmbrLen((g_ProofInProgress.user)[step])) {
      g_unifTrialCount = 1;
      userFlag = unifyH((g_ProofInProgress.user)[step],
        g_Statement[statemNum].mathString, &stateVector, 0);
    }
    C.go2("returnPoint");
  }

  mString = g_Statement[statemNum].mathString;
  mStringLen = g_Statement[statemNum].mathStringLen;



  firstSymbsAreConstsFlag = 0;
  stsym = mString[0];
  tasym = ((nmbrString *)((g_ProofInProgress.target)[step]))[0];
  if (g_MathToken[stsym].tokenType == (char)con_) {
    if (g_MathToken[tasym].tokenType == (char)con_) {
      firstSymbsAreConstsFlag = 1;
      if (stsym != tasym) {
        targetFlag = 0;
        C.go2("returnPoint");
      }
    }
  }

  stsym = mString[mStringLen - 1];
  tasym = ((nmbrString *)((g_ProofInProgress.target)[step]))[targetLen - 1];
  if (stsym != tasym) {
    if (g_MathToken[stsym].tokenType == (char)con_) {
      if (g_MathToken[tasym].tokenType == (char)con_) {
        targetFlag = 0;
        C.go2("returnPoint");
      }
    }
  }

  if (targetLen > 1 && mStringLen > 1 && firstSymbsAreConstsFlag) {
    stsym = mString[1];
    tasym = ((nmbrString *)((g_ProofInProgress.target)[step]))[1];
    if (stsym != tasym) {
      if (g_MathToken[stsym].tokenType == (char)con_) {
        if (g_MathToken[tasym].tokenType == (char)con_) {
          targetFlag = 0;
          C.go2("returnPoint");
        }
      }
    }
  }


  nmbrLet(&scheme, mString);
  reqVars = nmbrLen(g_Statement[statemNum].reqVarList);
  if (reqVars + g_pipDummyVars > g_dummyVars) {

    declareDummyVars(reqVars + g_pipDummyVars - g_dummyVars);
  }
  for (var = 0; var < reqVars; var++) {

    g_MathToken[g_Statement[statemNum].reqVarList[var]].tmp = g_mathTokens + 1 +
        g_pipDummyVars + var;
  }
  for (sym = 0; sym < mStringLen; sym++) {
    if (g_MathToken[scheme[sym]].tokenType != (char)var_)
        continue;

    scheme[sym] = g_MathToken[scheme[sym]].tmp;
  }


  g_unifTrialCount = 1;
  targetFlag = unifyH((g_ProofInProgress.target)[step],
      scheme, &stateVector, 0);
  if (nmbrLen((g_ProofInProgress.user)[step])) {
    g_unifTrialCount = 1;
    userFlag = unifyH((g_ProofInProgress.user)[step],
      scheme, &stateVector, 0);
  }

  C.label("returnPoint");
  nmbrLet(&scheme, NULL_NMBRSTRING);
  purgeStateVector(&stateVector);

  if (!targetFlag || !userFlag) return (0);
  if (targetFlag == 1 && userFlag == 1) return (1);
  return (2);

}

char checkMStringMatch(nmbrString *mString, long step)
{
  pntrString *stateVector = NULL_PNTRSTRING;
  char targetFlag;
  char sourceFlag = 1;

  g_unifTrialCount = 1;
  targetFlag = unifyH(mString, (g_ProofInProgress.target)[step],
      &stateVector, 0);
  if (nmbrLen((g_ProofInProgress.source)[step])) {
    g_unifTrialCount = 1;
    sourceFlag = unifyH(mString, (g_ProofInProgress.source)[step],
        &stateVector, 0);
  }

  purgeStateVector(&stateVector);

  if (!targetFlag || !sourceFlag) return (0);
  if (targetFlag == 1 && sourceFlag == 1) return (1);
  return (2);

}




nmbrString *proveFloating(nmbrString *mString, long statemNum, long maxEDepth,
    long step,
    flag noDistinct,

    flag overrideFlag,
    flag mathboxFlag
    )
{

  long reqHyps, optHyps;
  long hyp, stmt, sym, var, i, j;
  nmbrString *proof = NULL_NMBRSTRING;
  nmbrString *scheme = NULL_NMBRSTRING;
  pntrString *hypList = NULL_PNTRSTRING;
  nmbrString *hypOrdMap = NULL_NMBRSTRING;
  pntrString *hypProofList = NULL_PNTRSTRING;
  pntrString *stateVector = NULL_PNTRSTRING;

  nmbrString *stmtMathPtr;
  nmbrString *hypSchemePtr;
  nmbrString *hypProofPtr;
  nmbrString *makeSubstPtr;
  flag reEntryFlag;
  flag tmpFlag;
  flag breakFlag;
  flag firstEHypFlag;
  long schemeLen,  schemeVars, schReqHyps, hypLen, reqVars;
  long saveUnifTrialCount;
  static long depth = 0;
  static long trials;
  static flag maxDepthExceeded;
  long selfScanSteps;
  long selfScanStep;
  long prfMbox;

  long unNum;
if (db8)mminou.print2("%s\n", cat(space(depth+2), "Entered: ",
   nmbrCvtMToVString(mString), null));

  prfMbox = getMathboxNum(statemNum);

  if (depth == 0) {
    trials = 0;
    maxDepthExceeded = 0;
  } else {
    trials++;
  }
  depth++;
  if (trials > g_userMaxProveFloat) {
    nmbrLet(&proof, NULL_NMBRSTRING);
    mminou.print2(
"Exceeded trial limit at step %ld.  You may increase with SET SEARCH_LIMIT.\n",
        (long)(step + 1));
    C.go2("returnPoint");
  }


  if (maxDepthExceeded) {

    nmbrLet(&proof, NULL_NMBRSTRING);
    C.go2("returnPoint");
  }

  static final long  MAX_DEPTH=D.MAX_DEPTH;
  if (depth > MAX_DEPTH) {
    nmbrLet(&proof, NULL_NMBRSTRING);

    printLongLine(cat(
       "?Warning: A possible infinite loop was found in $f hypothesis ",
       "backtracking (i.e., depth > ", str((double)MAX_DEPTH),
       ").  The last proof attempt was for math string \"",
       nmbrCvtMToVString(mString),
       "\".  Your axiom system may have an error ",
       "or you may have to SET EMPTY_SUBSTITUTION ON.", null), " ", " ");
    maxDepthExceeded = 1;
    C.go2("returnPoint");
  }


  reqHyps = g_Statement[statemNum].numReqHyp;
  for (hyp = 0; hyp < reqHyps; hyp++) {
    if (nmbrEq(mString,
        g_Statement[g_Statement[statemNum].reqHypList[hyp]].mathString)) {
      nmbrLet(&proof, nmbrAddElement(NULL_NMBRSTRING,
          g_Statement[statemNum].reqHypList[hyp]));
      C.go2("returnPoint");
    }
  }
  optHyps = nmbrLen(g_Statement[statemNum].optHypList);
  for (hyp = 0; hyp < optHyps; hyp++) {
    if (nmbrEq(mString,
        g_Statement[g_Statement[statemNum].optHypList[hyp]].mathString)) {
      nmbrLet(&proof, nmbrAddElement(NULL_NMBRSTRING,
          g_Statement[statemNum].optHypList[hyp]));
      C.go2("returnPoint");
    }
  }

  selfScanSteps = nmbrLen(g_ProofInProgress.proof);
  for (selfScanStep = selfScanSteps - 1; selfScanStep >= 0; selfScanStep--) {
    if (nmbrEq(mString, (g_ProofInProgress.target)[selfScanStep])) {



      nmbrLet(&proof, nmbrSeg(g_ProofInProgress.proof, selfScanStep -
          subproofLen(g_ProofInProgress.proof, selfScanStep) + 2,
          selfScanStep + 1));


      if (nmbrElementIn(1, proof, -(long)'?')) {

        nmbrLet(&proof, NULL_NMBRSTRING);

        continue;
      }

      C.go2("returnPoint");
    }
  }






  for (stmt = statemNum - 1; stmt >= 1; stmt--) {


    if (quickMatchFilter(stmt, mString, 0) == 0) continue;


    if (!overrideFlag && getMarkupFlag(stmt, USAGE_DISCOURAGED)) {

      continue;
    }


    if (mathboxFlag == 0 && prfMbox >= 2) {

      if (stmt > g_mathboxStmt && stmt < g_mathboxStart[prfMbox - 1]) {
        continue;
      }
    }



    if (noDistinct) {
      if (nmbrLen(g_Statement[stmt].reqDisjVarsA)) {
        continue;
      }
    }

    stmtMathPtr = g_Statement[stmt].mathString;


    schemeLen = nmbrLen(stmtMathPtr);




    schReqHyps = g_Statement[stmt].numReqHyp;
    reqVars = nmbrLen(g_Statement[stmt].reqVarList);



    breakFlag = 0;
    firstEHypFlag = 1;
    for (hyp = 0; hyp < schReqHyps; hyp++) {
      if (g_Statement[g_Statement[stmt].reqHypList[hyp]].type == (char)e_) {
        if (depth > maxEDepth) {
          breakFlag = 1;
          break;
        } else {
          if (firstEHypFlag) {

            for (var = 0; var < reqVars; var++) {
              g_MathToken[g_Statement[stmt].reqVarList[var]].tmp = 1;
            }

            for (sym = 0; sym < schemeLen; sym++) {
              g_MathToken[stmtMathPtr[sym]].tmp = 0;
            }

            for (var = 0; var < reqVars; var++) {
              if (g_MathToken[g_Statement[stmt].reqVarList[var]].tmp) {
                breakFlag = 1;
                break;
              }
            }
            if (breakFlag) break;
            firstEHypFlag = 0;
          }
        }
      }
    }
    if (breakFlag) continue;




    nmbrLet(&scheme, stmtMathPtr);
    schemeVars = reqVars;
    if (schemeVars + g_pipDummyVars > g_dummyVars) {

      declareDummyVars(schemeVars + g_pipDummyVars - g_dummyVars);
    }
    for (var = 0; var < schemeVars; var++) {

      g_MathToken[g_Statement[stmt].reqVarList[var]].tmp = g_mathTokens + 1 +
          g_pipDummyVars + var;
    }
    for (sym = 0; sym < schemeLen; sym++) {
      if (g_MathToken[stmtMathPtr[sym]].tokenType != (char)var_) continue;

      scheme[sym] = g_MathToken[stmtMathPtr[sym]].tmp;
    }


    pntrLet(&hypList, pntrNSpace(schReqHyps));
    nmbrLet(&hypOrdMap, nmbrSpace(schReqHyps));
    pntrLet(&hypProofList, pntrNSpace(schReqHyps));
    for (hyp = 0; hyp < schReqHyps; hyp++) {
      hypSchemePtr = NULL_NMBRSTRING;
      nmbrLet(&hypSchemePtr,
        g_Statement[g_Statement[stmt].reqHypList[hyp]].mathString);
      hypLen = nmbrLen(hypSchemePtr);
      for (sym = 0; sym < hypLen; sym++) {
        if (g_MathToken[hypSchemePtr[sym]].tokenType
            != (char)var_) continue;

        hypSchemePtr[sym] = g_MathToken[hypSchemePtr[sym]].tmp;
      }
      hypList[hyp] = hypSchemePtr;
      hypOrdMap[hyp] = hyp;
    }

    g_unifTrialCount = 1;
    reEntryFlag = 0;

unNum = 0;
    while (true) {
      tmpFlag = unifyH(scheme, mString, &stateVector, reEntryFlag);
      if (!tmpFlag) break;
      if (tmpFlag == 2) {
        mminou.print2(
"Unification timed out.  SET UNIFICATION_TIMEOUT larger for better results.\n");
        g_unifTrialCount = 1;
        break;
      }

unNum++;
if (db8)mminou.print2("%s\n", cat(space(depth+2), "Testing unification ",
   str((double)unNum), " statement ", g_Statement[stmt].labelName,
   ": ", nmbrCvtMToVString(scheme), null));
      reEntryFlag = 1;

      nmbrLet(&proof, NULL_NMBRSTRING);
      breakFlag = 0;
      for (hyp = 0; hyp < schReqHyps; hyp++) {
if (db8)mminou.print2("%s\n", cat(space(depth+2), "Proving hyp. ",
   str((double)(hypOrdMap[hyp])), "(#", str((double)hyp), "):  ",
   nmbrCvtMToVString(hypList[hypOrdMap[hyp]]), null));
        makeSubstPtr = makeSubstUnif(&tmpFlag, hypList[hypOrdMap[hyp]],
            stateVector);
        if (tmpFlag) bug(1808);


        saveUnifTrialCount = g_unifTrialCount;
        hypProofPtr = proveFloating(makeSubstPtr, statemNum, maxEDepth, step,
            noDistinct,
            overrideFlag,
            mathboxFlag
            );
        g_unifTrialCount = saveUnifTrialCount;

        nmbrLet(&makeSubstPtr, NULL_NMBRSTRING);
        if (!nmbrLen(hypProofPtr)) {

          breakFlag = 1;
          break;
        }

        nmbrLet((nmbrString **)(&hypProofList[hypOrdMap[hyp]]),
            NULL_NMBRSTRING);

        hypProofList[hypOrdMap[hyp]] = hypProofPtr;
      }
      if (breakFlag) {



       if (trials > g_userMaxProveFloat) {

         for (hyp = 0; hyp < schReqHyps; hyp++) {
           nmbrLet((nmbrString **)(&hypList[hyp]), NULL_NMBRSTRING);
           nmbrLet((nmbrString **)(&hypProofList[hyp]), NULL_NMBRSTRING);
         }

         nmbrLet(&proof, NULL_NMBRSTRING);
         C.go2("returnPoint");
       }


       j = hypOrdMap[hyp];
       for (i = hyp; i >= 1; i--) {
         hypOrdMap[i] = hypOrdMap[i - 1];
       }
       hypOrdMap[0] = j;

       continue;

      }




      for (hyp = 0; hyp < schReqHyps; hyp++) {
        nmbrLet(&proof, nmbrCat(proof, hypProofList[hyp], null));
      }

      if (getMarkupFlag(stmt, USAGE_DISCOURAGED)) {
        switch (overrideFlag) {
          case 0: bug(1869); break;
          case 2: break;
          case 1:

            mminou.print2(
          ">>> ?Warning: Overriding discouraged usage of statement \"%s\".\n",
                g_Statement[stmt].labelName);

            break;
          default: bug(1870);
        }
      }




      if (mathboxFlag != 0) {

        assignMathboxInfo();
        if (stmt > g_mathboxStmt && g_proveStatement > g_mathboxStmt) {
          if (stmt < g_mathboxStart[getMathboxNum(g_proveStatement) - 1]) {
            printLongLine(cat("Used \"", g_Statement[stmt].labelName,
                  "\" from the mathbox for ",
                  g_mathboxUser[getMathboxNum(stmt) - 1], ".",
                  null),
                "  ", " ");
          }
        }
      }

      nmbrLet(&proof, nmbrAddElement(proof, stmt));


      for (hyp = 0; hyp < schReqHyps; hyp++) {
        nmbrLet((nmbrString **)(&hypList[hyp]), NULL_NMBRSTRING);
        nmbrLet((nmbrString **)(&hypProofList[hyp]), NULL_NMBRSTRING);
      }
      C.go2("returnPoint");

    }


    for (hyp = 0; hyp < schReqHyps; hyp++) {
      nmbrLet((nmbrString **)(&hypList[hyp]), NULL_NMBRSTRING);
      nmbrLet((nmbrString **)(&hypProofList[hyp]), NULL_NMBRSTRING);
    }

  }

  nmbrLet(&proof, NULL_NMBRSTRING);

  C.label("returnPoint");

  purgeStateVector(&stateVector);

  nmbrLet(&scheme, NULL_NMBRSTRING);
  pntrLet(&hypList, NULL_PNTRSTRING);
  nmbrLet(&hypOrdMap, NULL_NMBRSTRING);
  pntrLet(&hypProofList, NULL_PNTRSTRING);
  depth--;
if(db8)mminou.print2("%s\n", cat(space(depth+2), "Returned: ",
   nmbrCvtRToVString(proof,

                0,
                0 ), null));
if(db8){if(!depth)mminou.print2("Trials: %ld\n", trials);}
  return (proof);
}



INLINE flag quickMatchFilter(long trialStmt, nmbrString *mString,
    long dummyVarFlag ) {
  long sym;
  long firstSymbol, secondSymbol, lastSymbol;
  nmbrString *stmtMathPtr;
  flag breakFlag;
  long schemeLen, mStringLen;

  if (g_Statement[trialStmt].type != (char)p_ &&
      g_Statement[trialStmt].type != (char)a_) return 0;

  mStringLen = nmbrLen(mString);
  firstSymbol = mString[0];
  if (g_MathToken[firstSymbol].tokenType != (char)con_) firstSymbol = 0;
  if (mStringLen > 1) {
    secondSymbol = mString[1];
    if (g_MathToken[secondSymbol].tokenType != (char)con_) secondSymbol = 0;

    if (!firstSymbol) secondSymbol = 0;
  } else {
    secondSymbol = 0;
  }
  lastSymbol = mString[mStringLen - 1];
  if (g_MathToken[lastSymbol].tokenType != (char)con_) lastSymbol = 0;



  stmtMathPtr = g_Statement[trialStmt].mathString;

  if (firstSymbol) {
    if (firstSymbol != stmtMathPtr[0]) {
      if (g_MathToken[stmtMathPtr[0]].tokenType == (char)con_) return 0;
    }
  }

  schemeLen = nmbrLen(stmtMathPtr);


  if (secondSymbol) {
    if (schemeLen > 1) {
      if (secondSymbol != stmtMathPtr[1]) {

        if (g_MathToken[stmtMathPtr[0]].tokenType == (char)con_) {
          if (g_MathToken[stmtMathPtr[1]].tokenType == (char)con_)
              return 0;
        }
      }
    }
  }
  if (lastSymbol) {
    if (lastSymbol != stmtMathPtr[schemeLen - 1]) {
      if (g_MathToken[stmtMathPtr[schemeLen - 1]].tokenType ==
         (char)con_) return 0;
    }
  }


  for (sym = 0; sym < schemeLen; sym++) {
    g_MathToken[stmtMathPtr[sym]].tmp = 1;
  }

  for (sym = 0; sym < mStringLen; sym++) {
    g_MathToken[mString[sym]].tmp = 0;
  }

  if (dummyVarFlag == 0) {
    breakFlag = 0;
    for (sym = 0; sym < schemeLen; sym++) {
      if (g_MathToken[stmtMathPtr[sym]].tokenType == (char)con_) {
        if (g_MathToken[stmtMathPtr[sym]].tmp) {
          breakFlag = 1;
          break;
        }
      }
    }
    if (breakFlag) return 0;
  }

  return 1;

}



void minimizeProof(long repStatemNum, long prvStatemNum,
    flag allowGrowthFlag)
{


  long plen, step, mlen, sym, sublen;
  long startingPlen = 0;
  flag foundFlag, breakFlag;
  nmbrString *mString;
  nmbrString *newSubProofPtr = NULL_NMBRSTRING;

  if (allowGrowthFlag) startingPlen = nmbrLen(g_ProofInProgress.proof);

  while (true) {
    plen = nmbrLen(g_ProofInProgress.proof);
    foundFlag = 0;
    for (step = plen - 1; step >= 0; step--) {

      mString = (g_ProofInProgress.target)[step];
      mlen = nmbrLen(mString);
      breakFlag = 0;
      for (sym = 0; sym < mlen; sym++) {
        if (mString[sym] > g_mathTokens) {

          breakFlag = 1;
          break;
        }
      }
      if (breakFlag) continue;


      if (!checkStmtMatch(repStatemNum, step)) {
        continue;
      }


      if ((g_ProofInProgress.proof)[step] != repStatemNum


          || !allowGrowthFlag) {
        newSubProofPtr = replaceStatement(repStatemNum,
            step,
            prvStatemNum,
            1,
            0,
            0,
            0,
            2,
            1
            );
      }
      if (!nmbrLen(newSubProofPtr)) continue;


      if (nmbrElementIn(1, newSubProofPtr, -(long)'?')) {
        nmbrLet(&newSubProofPtr, NULL_NMBRSTRING);
        continue;
      }


      sublen = subproofLen(g_ProofInProgress.proof, step);
      if (sublen > nmbrLen(newSubProofPtr) || allowGrowthFlag) {

        if ((g_ProofInProgress.proof)[step] == -(long)'?') {

          if (!allowGrowthFlag) bug(1831);
        } else {
          deleteSubProof(step);
        }
        addSubProof(newSubProofPtr, step - sublen + 1);
        assignKnownSteps(step - sublen + 1, nmbrLen(newSubProofPtr));
        foundFlag = 1;
        nmbrLet(&newSubProofPtr, NULL_NMBRSTRING);
        break;
      }

      nmbrLet(&newSubProofPtr, NULL_NMBRSTRING);
    }

    if (!foundFlag) break;

    static final long MAX_GROWTH_FACTOR =D.MAX_GROWTH_FACTOR;
    if (allowGrowthFlag && plen > MAX_GROWTH_FACTOR * startingPlen) {

      mminou.print2("Suppressed excessive ALLOW_GROWTH growth.\n");
      break;
    }
  }

}




void initStep(long step)
{
  long stmt, reqHyps, pos, hyp, sym, reqVars, var, mlen;
  nmbrString *reqHypPos = NULL_NMBRSTRING;
  nmbrString *nmbrTmpPtr;

  stmt = (g_ProofInProgress.proof)[step];
  if (stmt < 0) {
    if (stmt == -(long)'?') {

      nmbrLet((nmbrString **)(&((g_ProofInProgress.source)[step])),
          NULL_NMBRSTRING);
    } else {
      mminou.print2("step %ld stmt %ld\n",step,stmt);
      bug(1809);
    }
    return;
  }
  if (g_Statement[stmt].type == (char)e_ || g_Statement[stmt].type == (char)f_) {

    nmbrLet((nmbrString **)(&((g_ProofInProgress.source)[step])),
        g_Statement[stmt].mathString);
    return;
  }




  nmbrLet((nmbrString **)(&((g_ProofInProgress.source)[step])),
      g_Statement[stmt].mathString);

  reqHyps = g_Statement[stmt].numReqHyp;
  nmbrLet(&reqHypPos, nmbrSpace(reqHyps));
  pos = step - 1;
  for (hyp = reqHyps - 1; hyp >= 0; hyp--) {
    reqHypPos[hyp] = pos;
    nmbrLet((nmbrString **)(&((g_ProofInProgress.target)[pos])),
        g_Statement[g_Statement[stmt].reqHypList[hyp]].mathString);

    if (hyp > 0) {
      pos = pos - subproofLen(g_ProofInProgress.proof, pos);

    }
  }


  reqVars = nmbrLen(g_Statement[stmt].reqVarList);
  if (g_pipDummyVars + reqVars > g_dummyVars) {

    declareDummyVars(g_pipDummyVars + reqVars - g_dummyVars);
  }
  for (var = 0; var < reqVars; var++) {

    g_MathToken[g_Statement[stmt].reqVarList[var]].tmp = g_mathTokens + 1 +
      g_pipDummyVars + var;
  }

  nmbrTmpPtr = (g_ProofInProgress.source)[step];
  mlen = nmbrLen(nmbrTmpPtr);
  for (sym = 0; sym < mlen; sym++) {
    if (g_MathToken[nmbrTmpPtr[sym]].tokenType == (char)var_) {

      nmbrTmpPtr[sym] = g_MathToken[nmbrTmpPtr[sym]].tmp;
    }
  }

  for (hyp = 0; hyp < reqHyps; hyp++) {
    nmbrTmpPtr = (g_ProofInProgress.target)[reqHypPos[hyp]];
    mlen = nmbrLen(nmbrTmpPtr);
    for (sym = 0; sym < mlen; sym++) {
      if (g_MathToken[nmbrTmpPtr[sym]].tokenType == (char)var_) {

        nmbrTmpPtr[sym] = g_MathToken[nmbrTmpPtr[sym]].tmp;
      }
    }
  }


  g_pipDummyVars = g_pipDummyVars + reqVars;

  nmbrLet(&reqHypPos, NULL_NMBRSTRING);

  return;
}




void assignKnownSubProofs()
{
  long plen, pos, subplen, q;
  flag breakFlag;

  plen = nmbrLen(g_ProofInProgress.proof);

  for (pos = plen - 1; pos >= 0; pos--) {
    subplen = subproofLen(g_ProofInProgress.proof, pos);
    breakFlag = 0;
    for (q = pos - subplen + 1; q <= pos; q++) {
      if ((g_ProofInProgress.proof)[q] == -(long)'?') {
        breakFlag = 1;
        break;
      }
    }
    if (breakFlag) continue;




    assignKnownSteps(pos - subplen + 1, subplen);


    pos = pos - subplen + 1;

  }
  return;
}


void assignKnownSteps(long startStep, long sbProofLen)
{

  long stackPtr, st;
  nmbrString *stack = NULL_NMBRSTRING;
  nmbrString *instance = NULL_NMBRSTRING;
  nmbrString *scheme = NULL_NMBRSTRING;
  nmbrString *assertion = NULL_NMBRSTRING;
  long pos, stmt, reqHyps, instLen, instPos, schemeLen, schemePos, hypLen,
      hypPos, hyp, reqVars, var, assLen, assPos;
  flag tmpFlag;
  pntrString *stateVector = NULL_PNTRSTRING;

  nmbrLet(&stack, nmbrSpace(sbProofLen));
  stackPtr = 0;
  for (pos = startStep; pos < startStep + sbProofLen; pos++) {
    stmt = (g_ProofInProgress.proof)[pos];

    if (stmt <= 0) {
      if (stmt != -(long)'?') bug(1810);

      if (stmt == -(long)'?') bug(1830);

    }

    if (g_Statement[stmt].type == (char)e_ || g_Statement[stmt].type == (char)f_){

      nmbrLet((nmbrString **)(&((g_ProofInProgress.source)[pos])),
          g_Statement[stmt].mathString);
      stack[stackPtr] = pos;
      stackPtr++;
    } else {



      reqHyps = g_Statement[stmt].numReqHyp;

      instLen = 1;
      for (st = stackPtr - reqHyps; st < stackPtr; st++) {
        if (st < 0) bug(1850);

        instLen = instLen + nmbrLen((g_ProofInProgress.source)[stack[st]]) + 1;
      }

      nmbrLet(&instance, nmbrSpace(instLen));

      instance[0] = g_mathTokens;
      instPos = 1;
      for (st = stackPtr - reqHyps; st < stackPtr; st++) {
        hypLen = nmbrLen((g_ProofInProgress.source)[stack[st]]);
        for (hypPos = 0; hypPos < hypLen; hypPos++) {
          instance[instPos] =
              ((nmbrString *)((g_ProofInProgress.source)[stack[st]]))[hypPos];
          instPos++;
        }
        instance[instPos] = g_mathTokens;
        instPos++;
      }
      if (instLen != instPos) bug(1811);

      schemeLen = 1;
      for (hyp = 0; hyp < reqHyps; hyp++) {

        schemeLen = schemeLen +
            g_Statement[g_Statement[stmt].reqHypList[hyp]].mathStringLen + 1;
      }

      nmbrLet(&scheme, nmbrSpace(schemeLen));

      scheme[0] = g_mathTokens;
      schemePos = 1;
      for (hyp = 0; hyp < reqHyps; hyp++) {
        hypLen = g_Statement[g_Statement[stmt].reqHypList[hyp]].mathStringLen;
        for (hypPos = 0; hypPos < hypLen; hypPos++) {
          scheme[schemePos] =
              g_Statement[g_Statement[stmt].reqHypList[hyp]].mathString[hypPos];
          schemePos++;
        }
        scheme[schemePos] = g_mathTokens;
        schemePos++;
      }
      if (schemeLen != schemePos) bug(1812);


      reqVars = nmbrLen(g_Statement[stmt].reqVarList);
      if (reqVars + g_pipDummyVars > g_dummyVars) {

        declareDummyVars(reqVars + g_pipDummyVars - g_dummyVars);
      }
      for (var = 0; var < reqVars; var++) {

        g_MathToken[g_Statement[stmt].reqVarList[var]].tmp = g_mathTokens + 1 +
          g_pipDummyVars + var;
      }
      for (schemePos = 0; schemePos < schemeLen; schemePos++) {
        if (g_MathToken[scheme[schemePos]].tokenType
            != (char)var_) continue;

        scheme[schemePos] = g_MathToken[scheme[schemePos]].tmp;
      }


      nmbrLet(&assertion, g_Statement[stmt].mathString);
      assLen = nmbrLen(assertion);
      for (assPos = 0; assPos < assLen; assPos++) {
        if (g_MathToken[assertion[assPos]].tokenType
            != (char)var_) continue;

        assertion[assPos] = g_MathToken[assertion[assPos]].tmp;
      }


      g_unifTrialCount = 0;
      tmpFlag = unifyH(scheme, instance, &stateVector, 0);
      if (!tmpFlag) {

        printLongLine(cat("?Error in step ", str((double)pos + 1),
            ":  Could not simultaneously unify the hypotheses of \"",
            g_Statement[stmt].labelName, "\":\n    ",
            nmbrCvtMToVString(scheme),
            "\nwith the following statement list:\n    ",
            nmbrCvtMToVString(instance),
            "\n(The $|$ tokens are internal statement separation markers)",
            "\nZapping targets so we can proceed (but you should exit the ",
            "Proof Assistant and fix this problem)",
            "\n(This may take a while; please wait...)",
            null), "", " ");
        purgeStateVector(&stateVector);
        C.go2("returnPoint");
      }

      nmbrLet((nmbrString **)(&((g_ProofInProgress.source)[pos])), NULL_NMBRSTRING);
      (g_ProofInProgress.source)[pos] = makeSubstUnif(&tmpFlag, assertion,
          stateVector);
      if (tmpFlag) bug(1814);


      if (unifyH(scheme, instance, &stateVector, 1)) bug(1815);


      stackPtr = stackPtr - reqHyps;
      stack[stackPtr] = pos;
      stackPtr++;

    }
  }

  if (stackPtr != 1) bug(1816);

  C.label("returnPoint");

  for (pos = startStep; pos < startStep + sbProofLen - 1; pos++) {
    nmbrLet((nmbrString **)(&((g_ProofInProgress.target)[pos])),
        (g_ProofInProgress.source)[pos]);
  }


  nmbrLet(&stack, NULL_NMBRSTRING);
  nmbrLet(&instance, NULL_NMBRSTRING);
  nmbrLet(&scheme, NULL_NMBRSTRING);
  nmbrLet(&assertion, NULL_NMBRSTRING);
  return;
}



void interactiveUnifyStep(long step, char messageFlag)
{
  pntrString *stateVector = NULL_PNTRSTRING;
  char unifFlag;


  if (!nmbrLen((g_ProofInProgress.target)[step])) bug (1817);




  if (nmbrLen((g_ProofInProgress.user)[step])) {
    if (!nmbrEq((g_ProofInProgress.target)[step], (g_ProofInProgress.user)[step])) {
      if (messageFlag == 0) mminou.print2("Step %ld:\n", step + 1);
      unifFlag = interactiveUnify((g_ProofInProgress.target)[step],
        (g_ProofInProgress.user)[step], &stateVector);
      C.go2("subAndReturn");
    }
    if (nmbrLen((g_ProofInProgress.source)[step])) {
      if (!nmbrEq((g_ProofInProgress.source)[step], (g_ProofInProgress.user)[step])) {
        if (messageFlag == 0) mminou.print2("Step %ld:\n", step + 1);
        unifFlag = interactiveUnify((g_ProofInProgress.source)[step],
          (g_ProofInProgress.user)[step], &stateVector);
        C.go2("subAndReturn");
      }
    }
  } else {
    if (nmbrLen((g_ProofInProgress.source)[step])) {
      if (!nmbrEq((g_ProofInProgress.target)[step], (g_ProofInProgress.source)[step])) {
        if (messageFlag == 0) mminou.print2("Step %ld:\n", step + 1);
        unifFlag = interactiveUnify((g_ProofInProgress.target)[step],
          (g_ProofInProgress.source)[step], &stateVector);
        C.go2("subAndReturn");
      }
    }
  }


  if (messageFlag == 1) {
    mminou.print2("?Step %ld is already unified.\n", step + 1);
  }
  unifFlag = 0;

  C.label("subAndReturn");
  if (unifFlag == 1) {

    g_proofChangedFlag = 1;

    makeSubstAll(stateVector);

  }

  purgeStateVector(&stateVector);

  return;

}



char interactiveUnify(nmbrString *schemeA, nmbrString *schemeB,
    pntrString **stateVector)
{

  long var, i;
  long unifCount, unifNum;
  char unifFlag;
  flag reEntryFlag;
  nmbrString *stackUnkVar;
  nmbrString *unifiedScheme;
  nmbrString *stackUnkVarLen;
  nmbrString *stackUnkVarStart;
  long stackTop;
  vstring tmpStr = "";
  nmbrString *nmbrTmp = NULL_NMBRSTRING;
  char returnValue;

  nmbrString *unifWeight = NULL_NMBRSTRING;
  long unifTrialWeight;
  long maxUnifWeight;
  long minUnifWeight;
  long unifTrials;
  long thisUnifWeight;
  long onesCount;
  nmbrString *substResult = NULL_NMBRSTRING;
  long unkCount;

  if (nmbrEq(schemeA, schemeB)) bug(1818);


  g_unifTrialCount = 1;
  unifCount = 0;
  reEntryFlag = 0;
  minUnifWeight = -1;
  maxUnifWeight = 0;
  while (true) {
    unifFlag = unifyH(schemeA, schemeB, &(*stateVector), reEntryFlag);
    if (unifFlag == 2) {
      printLongLine(
          cat("Unify:  ", nmbrCvtMToVString(schemeA), null), "    ", " ");
      printLongLine(
          cat(" with:  ", nmbrCvtMToVString(schemeB), null), "    ", " ");
      mminou.print2(
"The unification timed out.  Increase timeout (SET UNIFICATION_TIMEOUT) or\n");
      mminou.print2(
"assign some variables (LET VARIABLE) or the step (LET STEP) manually.\n");

      returnValue = 2;
      C.go2("returnPoint");
    }
    if (!unifFlag) break;
    reEntryFlag = 1;




    stackTop = ((nmbrString *)((*stateVector)[11]))[1];
    stackUnkVarStart = (nmbrString *)((*stateVector)[2]);
    stackUnkVarLen = (nmbrString *)((*stateVector)[3]);
    unifiedScheme = (nmbrString *)((*stateVector)[8]);


    thisUnifWeight = stackTop * 2;
    onesCount = 0;
    unkCount = 0;
    for (var = 0; var <= stackTop; var++) {

      thisUnifWeight = thisUnifWeight + stackUnkVarLen[var];

      if (stackUnkVarLen[var] == 1) onesCount++;


      nmbrLet(&substResult, nmbrMid(unifiedScheme, stackUnkVarStart[var] + 1,
              stackUnkVarLen[var]));
      for (i = 0; i < nmbrLen(substResult); i++) {
        if (substResult[i] > g_mathTokens) unkCount++;
      }

    }
    thisUnifWeight = thisUnifWeight - onesCount;
    thisUnifWeight = thisUnifWeight + 7 * unkCount;



    if (thisUnifWeight > maxUnifWeight) maxUnifWeight = thisUnifWeight;
    if (thisUnifWeight < minUnifWeight || minUnifWeight == -1)
      minUnifWeight = thisUnifWeight;

    nmbrLet(&unifWeight, nmbrAddElement(unifWeight, 0));

    unifWeight[unifCount] = thisUnifWeight;
    unifCount++;
    if (nmbrLen(unifWeight) != unifCount) bug(1827);
  }

  if (!unifCount) {
    printf("The unification is not possible.  The proof has an error.\n");

    returnValue = 0;
    C.go2("returnPoint");
  }
  if (unifCount > 1) {
    printLongLine(cat("There are ", str((double)unifCount),
      " possible unifications.  Please select the correct one or QUIT if",
      " you want to UNIFY later.", null),
        "    ", " ");
    printLongLine(cat("Unify:  ", nmbrCvtMToVString(schemeA), null),
        "    ", " ");
    printLongLine(cat(" with:  ", nmbrCvtMToVString(schemeB), null),
        "    ", " ");
  }

  unifTrials = 0;
  for (unifTrialWeight = minUnifWeight; unifTrialWeight <= maxUnifWeight;
      unifTrialWeight++) {

    if (!nmbrElementIn(1, unifWeight, unifTrialWeight)) continue;

    g_unifTrialCount = 1;
    reEntryFlag = 0;

    for (unifNum = 1; unifNum <= unifCount; unifNum++) {
      unifFlag = unifyH(schemeA, schemeB, &(*stateVector), reEntryFlag);
      if (unifFlag != 1) bug(1819);

      reEntryFlag = 1;
      if (unifWeight[unifNum - 1] != unifTrialWeight) continue;

      if (unifCount == 1) {
        mminou.print2("Step was successfully unified.\n");

        returnValue = 1;
        C.go2("returnPoint");
      }

      unifTrials++;
      mminou.print2("Unification #%ld of %ld (weight = %ld):\n",
          unifTrials, unifCount, unifTrialWeight);

      stackTop = ((nmbrString *)((*stateVector)[11]))[1];
      stackUnkVar = (nmbrString *)((*stateVector)[1]);
      stackUnkVarStart = (nmbrString *)((*stateVector)[2]);
      stackUnkVarLen = (nmbrString *)((*stateVector)[3]);
      unifiedScheme = (nmbrString *)((*stateVector)[8]);
      for (var = 0; var <= stackTop; var++) {
        printLongLine(cat("  Replace \"",
          g_MathToken[stackUnkVar[var]].tokenName,"\" with \"",
            nmbrCvtMToVString(
                nmbrMid(unifiedScheme,stackUnkVarStart[var] + 1,
                stackUnkVarLen[var])), "\"", null),"    "," ");

        let(&tmpStr,"");
        nmbrLet(&nmbrTmp,NULL_NMBRSTRING);
      }

      while(true) {
        tmpStr = cmdInput1("  Accept (A), reject (R), or quit (Q) <A>? ");
        if (!tmpStr[0]) {

          returnValue = 1;
          C.go2("returnPoint");
        }
        if (tmpStr[0] == 'R' || tmpStr[0] == 'r') {
          if (!tmpStr[1]) {
            let(&tmpStr, "");
            break;
          }
        }
        if (tmpStr[0] == 'Q' || tmpStr[0] == 'q') {
          if (!tmpStr[1]) {

            returnValue = 3;
            C.go2("returnPoint");
          }
        }
        if (tmpStr[0] == 'A' || tmpStr[0] == 'a') {
          if (!tmpStr[1]) {

            returnValue = 1;
            C.go2("returnPoint");
          }
        }
        let(&tmpStr, "");
      }

    }

  }


  if (unifTrials != unifCount) bug(1829);


  returnValue = 3;
  C.go2("returnPoint");

  C.label("returnPoint");
  let(&tmpStr, "");
  nmbrLet(&unifWeight, NULL_NMBRSTRING);
  nmbrLet(&substResult, NULL_NMBRSTRING);
  return returnValue;

}





void autoUnify(flag congrats)
{
  long step, plen;
  char unifFlag;

  flag somethingChanged = 1;
  int pass;
  nmbrString *schemeAPtr;
  nmbrString *schemeBPtr;
  pntrString *stateVector = NULL_PNTRSTRING;
  flag somethingNotUnified = 0;

  schemeAPtr = NULL_NMBRSTRING;
  schemeBPtr = NULL_NMBRSTRING;

  plen = nmbrLen(g_ProofInProgress.proof);

  while (somethingChanged) {
    somethingChanged = 0;
    for (step = 0; step < plen; step++) {


      for (pass = 0; pass < 3; pass++) {

        switch (pass) {
          case 0:

            schemeAPtr = (g_ProofInProgress.target)[step];
            if (!nmbrLen(schemeAPtr)) mminou.print2(
 "?Bad unification selected:  A proof step should never be completely empty\n");

            schemeBPtr = (g_ProofInProgress.user)[step];
            break;
          case 1:

            schemeAPtr = (g_ProofInProgress.source)[step];
            schemeBPtr = (g_ProofInProgress.user)[step];
            break;
          case 2:

            schemeAPtr = (g_ProofInProgress.target)[step];
            schemeBPtr = (g_ProofInProgress.source)[step];
            break;
        }
        if (nmbrLen(schemeAPtr) && nmbrLen(schemeBPtr)) {
          if (!nmbrEq(schemeAPtr, schemeBPtr)) {
            g_unifTrialCount = 1;
            unifFlag = uniqueUnif(schemeAPtr, schemeBPtr, &stateVector);
            if (unifFlag != 1) somethingNotUnified = 1;
            if (unifFlag == 2) {
              mminou.print2("A unification timeout occurred at step %ld.\n", step + 1);
            }
            if (!unifFlag) {
              mminou.print2(
              "Step %ld cannot be unified.  THERE IS AN ERROR IN THE PROOF.\n",
                  (long)(step + 1));
              continue;
            }
            if (unifFlag == 1) {

              makeSubstAll(stateVector);
              somethingChanged = 1;
              g_proofChangedFlag = 1;

            }
          }
        }
      }
    }
  }

  purgeStateVector(&stateVector);


  if (congrats) {
    if (!somethingNotUnified) {
      if (!nmbrElementIn(1, g_ProofInProgress.proof, -(long)'?')) {
        mminou.print2(
  "CONGRATULATIONS!  The proof is complete.  Use SAVE NEW_PROOF to save it.\n");
        mminou.print2(
  "Note:  The Proof Assistant does not detect $d violations.  After saving\n");
        mminou.print2(
  "the proof, you should verify it with VERIFY PROOF.\n");
      }
    }
  }

  return;

}


void makeSubstAll(pntrString *stateVector) {

  nmbrString *nmbrTmpPtr;
  long plen, step;
  flag tmpFlag;

  plen = nmbrLen(g_ProofInProgress.proof);
  for (step = 0; step < plen; step++) {

    nmbrTmpPtr = (g_ProofInProgress.target)[step];
    (g_ProofInProgress.target)[step] = makeSubstUnif(&tmpFlag, nmbrTmpPtr,
      stateVector);
    nmbrLet(&nmbrTmpPtr, NULL_NMBRSTRING);

    nmbrTmpPtr = (g_ProofInProgress.source)[step];
    if (nmbrLen(nmbrTmpPtr)) {
      (g_ProofInProgress.source)[step] = makeSubstUnif(&tmpFlag, nmbrTmpPtr,
        stateVector);
      nmbrLet(&nmbrTmpPtr, NULL_NMBRSTRING);
    }

    nmbrTmpPtr = (g_ProofInProgress.user)[step];
    if (nmbrLen(nmbrTmpPtr)) {
      (g_ProofInProgress.user)[step] = makeSubstUnif(&tmpFlag, nmbrTmpPtr,
        stateVector);
      nmbrLet(&nmbrTmpPtr, NULL_NMBRSTRING);
    }

  }
  return;
}


void replaceDummyVar(long dummyVar, nmbrString *mString)
{
  long numSubs = 0;
  long numSteps = 0;
  long plen, step, sym, slen;
  flag stepChanged;
  nmbrString *nmbrTmpPtr;

  plen = nmbrLen(g_ProofInProgress.proof);
  for (step = 0; step < plen; step++) {

    stepChanged = 0;

    nmbrTmpPtr = (g_ProofInProgress.target)[step];
    slen = nmbrLen(nmbrTmpPtr);
    for (sym = slen - 1; sym >= 0; sym--) {
      if (nmbrTmpPtr[sym] == dummyVar + g_mathTokens) {
        nmbrLet((nmbrString **)(&((g_ProofInProgress.target)[step])),
            nmbrCat(nmbrLeft(nmbrTmpPtr, sym), mString,
            nmbrRight(nmbrTmpPtr, sym + 2), null));
        nmbrTmpPtr = (g_ProofInProgress.target)[step];
        stepChanged = 1;
        numSubs++;
      }
    }

    nmbrTmpPtr = (g_ProofInProgress.source)[step];
    slen = nmbrLen(nmbrTmpPtr);
    for (sym = slen - 1; sym >= 0; sym--) {
      if (nmbrTmpPtr[sym] == dummyVar + g_mathTokens) {
        nmbrLet((nmbrString **)(&((g_ProofInProgress.source)[step])),
            nmbrCat(nmbrLeft(nmbrTmpPtr, sym), mString,
            nmbrRight(nmbrTmpPtr, sym + 2), null));
        nmbrTmpPtr = (g_ProofInProgress.source)[step];
        stepChanged = 1;
        numSubs++;
      }
    }

    nmbrTmpPtr = (g_ProofInProgress.user)[step];
    slen = nmbrLen(nmbrTmpPtr);
    for (sym = slen - 1; sym >= 0; sym--) {
      if (nmbrTmpPtr[sym] == dummyVar + g_mathTokens) {
        nmbrLet((nmbrString **)(&((g_ProofInProgress.user)[step])),
            nmbrCat(nmbrLeft(nmbrTmpPtr, sym), mString,
            nmbrRight(nmbrTmpPtr, sym + 2), null));
        nmbrTmpPtr = (g_ProofInProgress.user)[step];
        stepChanged = 1;
        numSubs++;
      }
    }

    if (stepChanged) numSteps++;
  }

  if (numSubs) {
    g_proofChangedFlag = 1;
    mminou.print2("%ld substitutions were made in %ld steps.\n", numSubs, numSteps);
  } else {
    mminou.print2("?The dummy variable $%ld is nowhere in the proof.\n", dummyVar);
  }

  return;
}

long subproofLen(nmbrString *proof, long endStep)
{
  long stmt, p, lvl;
  lvl = 1;
  p = endStep + 1;
  while (lvl) {
    p--;
    lvl--;
    if (p < 0) bug(1821);
    stmt = proof[p];
    if (stmt < 0) {
      continue;
    }
    if (g_Statement[stmt].type == (char)e_ ||
        g_Statement[stmt].type == (char)f_) {
      continue;
    }
    lvl = lvl + g_Statement[stmt].numReqHyp;
  }
  return (endStep - p + 1);
}



char checkDummyVarIsolation(long testStep)
{
  long proofLen, hyp, parentStep, tokpos, token;
  char dummyVarIndicator;
  long prfStep, parentStmt;
  nmbrString *dummyVarList = NULL_NMBRSTRING;
  flag bugCheckFlag;
  char hypType;


  for (tokpos = 0; tokpos < nmbrLen((g_ProofInProgress.target)[testStep]);
      tokpos++) {
    token = ((nmbrString *)((g_ProofInProgress.target)[testStep]))[tokpos];
    if (token > g_mathTokens) {
      if (!nmbrElementIn(1, dummyVarList, token)) {
        nmbrLet(&dummyVarList, nmbrAddElement(dummyVarList, token));
      }
    }
  }
  if (nmbrLen(dummyVarList) == 0) {
    dummyVarIndicator = 0;
    C.go2("RETURN_POINT");
  }

  proofLen = nmbrLen(g_ProofInProgress.proof);
  if (testStep == proofLen - 1) {
    dummyVarIndicator = 1;
    C.go2("RETURN_POINT");
  }

  parentStep = getParentStep(testStep);

  for (tokpos = 0; tokpos < nmbrLen((g_ProofInProgress.target)[parentStep]);
      tokpos++) {
    token = ((nmbrString *)((g_ProofInProgress.target)[parentStep]))[tokpos];
    if (token > g_mathTokens) {
      if (nmbrElementIn(1, dummyVarList, token)) {
        dummyVarIndicator = 2;
        C.go2("RETURN_POINT");
      }
    }
  }
  parentStmt = (g_ProofInProgress.proof)[parentStep];
  if (parentStmt < 0) bug(1845);
  if (g_Statement[parentStmt].type != (char)a_ &&
      g_Statement[parentStmt].type != (char)p_) bug(1846);
  bugCheckFlag = 0;
  prfStep = parentStep - 1;
  for (hyp = g_Statement[parentStmt].numReqHyp - 1; hyp >= 0; hyp--) {
    if (hyp < g_Statement[parentStmt].numReqHyp - 1) {
      prfStep = prfStep - subproofLen(g_ProofInProgress.proof, prfStep);
    }
    if (prfStep == testStep) {
      bugCheckFlag = 1;
      continue;
    }
    hypType = g_Statement[g_Statement[parentStmt].reqHypList[hyp]].type;
    if (hypType == (char)e_) {
      for (tokpos = 0; tokpos < nmbrLen((g_ProofInProgress.target)[prfStep]);
          tokpos++) {
        token = ((nmbrString *)((g_ProofInProgress.target)[prfStep]))[tokpos];
        if (token > g_mathTokens) {
          if (nmbrElementIn(1, dummyVarList, token)) {
            dummyVarIndicator = 2;
            C.go2("RETURN_POINT");
          }
        }
      }
    } else if (hypType != (char)f_) {
      bug(1848);
    }
  }
  if (bugCheckFlag == 0) bug(1847);

  dummyVarIndicator = 1;

  C.label("RETURN_POINT");
  nmbrLet(&dummyVarList, NULL_NMBRSTRING);
  return dummyVarIndicator;
}





long getParentStep(long startStep)
{
  long proofLen;
  long stackPtr, prfStep, stmt;


  proofLen = nmbrLen(g_ProofInProgress.proof);

  stackPtr = 0;
  for (prfStep = startStep + 1; prfStep < proofLen; prfStep++) {
    stmt = (g_ProofInProgress.proof)[prfStep];
    if (stmt < 0) {
      if (stmt != -(long)'?') bug(1842);
      stackPtr++;
    } else if (g_Statement[stmt].type == (char)e_ ||
          g_Statement[stmt].type == (char)f_) {
      stackPtr++;
    } else {
      if (g_Statement[stmt].type != (char)a_ &&
          g_Statement[stmt].type != (char)p_) bug(1843);
      stackPtr = stackPtr - g_Statement[stmt].numReqHyp + 1;
      if (stackPtr <= 0) return prfStep;
    }
  }
  if (startStep != proofLen - 1) bug(1844);
  return startStep;
}



void declareDummyVars(long numNewVars)
{

  long i;

  long saveTempAllocStack;
  saveTempAllocStack = g_startTempAllocStack;
  g_startTempAllocStack = g_tempAllocStackTop;

  for (i = 0; i < numNewVars; i++) {

    g_dummyVars++;

    if (g_mathTokens + 1 + g_dummyVars >= g_MAX_MATHTOKENS) {



      g_MAX_MATHTOKENS = g_MAX_MATHTOKENS + 1000;
      g_MathToken = realloc(g_MathToken, (size_t)g_MAX_MATHTOKENS *
        sizeof(struct mathToken_struct));
      if (!g_MathToken) outOfMemory("#10 (mathToken)");
    }

    g_MathToken[g_mathTokens + g_dummyVars].tokenName = "";

    let(&g_MathToken[g_mathTokens + g_dummyVars].tokenName,
        cat("$", str((double)g_dummyVars), null));
    g_MathToken[g_mathTokens + g_dummyVars].length =
        (long)strlen(g_MathToken[g_mathTokens + g_dummyVars].tokenName);
    g_MathToken[g_mathTokens + g_dummyVars].scope = g_currentScope;
    g_MathToken[g_mathTokens + g_dummyVars].active = 1;
    g_MathToken[g_mathTokens + g_dummyVars].tokenType = (char)var_;
    g_MathToken[g_mathTokens + g_dummyVars].tmp = 0;

  }

  g_startTempAllocStack = saveTempAllocStack;

  return;

}




void copyProofStruct(struct pip_struct *outProofStruct,
    struct pip_struct inProofStruct)
{
  long proofLen, j;
  deallocProofStruct(&(*outProofStruct));


  proofLen = nmbrLen(inProofStruct.proof);
  if (proofLen == 0) bug(1854);
  if (proofLen == 0) return;
  nmbrLet(&((*outProofStruct).proof), inProofStruct.proof);

  pntrLet(&((*outProofStruct).target), pntrNSpace(proofLen));
  pntrLet(&((*outProofStruct).source), pntrNSpace(proofLen));
  pntrLet(&((*outProofStruct).user), pntrNSpace(proofLen));

  if (proofLen != pntrLen(inProofStruct.target)) bug(1855);
  if (proofLen != pntrLen(inProofStruct.source)) bug(1856);
  if (proofLen != pntrLen(inProofStruct.user)) bug(1857);

  for (j = 0; j < proofLen; j++) {
    nmbrLet((nmbrString **)(&(((*outProofStruct).target)[j])),
        (inProofStruct.target)[j]);
    nmbrLet((nmbrString **)(&(((*outProofStruct).source)[j])),
        (inProofStruct.source)[j]);
    nmbrLet((nmbrString **)(&(((*outProofStruct).user)[j])),
        (inProofStruct.user)[j]);
  }
  return;
}



void initProofStruct(struct pip_struct *proofStruct, nmbrString *proof,
    long proveStmt)
{
  nmbrString *tmpProof = NULL_NMBRSTRING;
  long plen, step;

  nmbrLet(&tmpProof, nmbrUnsquishProof(proof));


  if (nmbrLen((*proofStruct).proof)) bug(1876);
  nmbrLet(&((*proofStruct).proof), tmpProof);
  plen = nmbrLen((*proofStruct).proof);
  pntrLet(&((*proofStruct).target), pntrNSpace(plen));
  pntrLet(&((*proofStruct).source), pntrNSpace(plen));
  pntrLet(&((*proofStruct).user), pntrNSpace(plen));
  nmbrLet((nmbrString **)(&(((*proofStruct).target)[plen - 1])),
      g_Statement[proveStmt].mathString);
  g_pipDummyVars = 0;

  assignKnownSubProofs();

  for (step = 0; step < plen; step++) {
    if (!nmbrLen(((*proofStruct).source)[step])) {
      initStep(step);
    }
  }

  autoUnify(0);


  nmbrLet(&tmpProof, NULL_NMBRSTRING);
  return;
}



void deallocProofStruct(struct pip_struct *proofStruct)
{
  long proofLen, j;

  proofLen = nmbrLen((*proofStruct).proof);
  if (proofLen == 0) return;
  nmbrLet(&((*proofStruct).proof), NULL_NMBRSTRING);
  for (j = 0; j < proofLen; j++) {
    nmbrLet((nmbrString **)(&(((*proofStruct).target)[j])), NULL_NMBRSTRING);
    nmbrLet((nmbrString **)(&(((*proofStruct).source)[j])), NULL_NMBRSTRING);
    nmbrLet((nmbrString **)(&(((*proofStruct).user)[j])), NULL_NMBRSTRING);
  }
  pntrLet(&((*proofStruct).target), NULL_PNTRSTRING);
  pntrLet(&((*proofStruct).source), NULL_PNTRSTRING);
  pntrLet(&((*proofStruct).user), NULL_PNTRSTRING);
  return;
}



  static final long  DEFAULT_UNDO_STACK_SIZE =D.DEFAULT_UNDO_STACK_SIZE;
long processUndoStack(struct pip_struct *proofStruct,
    char action,
    vstring info,
    long newSize)
{

  static struct pip_struct *proofStack = null;
  static pntrString *infoStack = NULL_PNTRSTRING;
  static long stackSize = DEFAULT_UNDO_STACK_SIZE;
  static long stackEnd = -1;
  static long stackPtr = -1;
  static flag firstTime = 1;
  static flag stackOverflowed = 0;
  static flag stackAborted = 0;
  long i;

  if (stackPtr < -1 || stackPtr > stackEnd || stackPtr > stackSize - 1
      || stackEnd < -1 || stackEnd > stackSize -1 ) {
    bug(1858);
  }

  if (firstTime == 1) {
    firstTime = 0;
    proofStack = malloc((size_t)(stackSize) * sizeof(struct pip_struct));
    if (!proofStack) bug(1859);
    for (i = 0; i < stackSize; i++) {
      proofStack[i].proof = NULL_NMBRSTRING;
      proofStack[i].target = NULL_PNTRSTRING;
      proofStack[i].source = NULL_PNTRSTRING;
      proofStack[i].user = NULL_PNTRSTRING;
    }
    pntrLet(&infoStack, pntrSpace(stackSize));
  }

  if (!proofStack) bug(1860);

  switch (action) {
    case PUS_GET_SIZE:
    case PUS_GET_STATUS:
      break;

    case PUS_INIT:
    case PUS_NEW_SIZE:

      for (i = 0; i <= stackEnd; i++) {
        deallocProofStruct(&(proofStack[i]));
        let((vstring *)(&(infoStack[i])), "");
      }

      if (action == PUS_NEW_SIZE) {
        if (stackPtr > 0) {
          mminou.print2("The previous UNDOs are no longer available.\n");
          stackAborted = 1;
        }
        if (stackOverflowed) stackAborted = 1;
      }

      stackEnd = -1;
      stackPtr = -1;
      stackOverflowed = 0;

      if (action == PUS_INIT) {
        stackAborted = 0;
        break;
      }



      free(proofStack);

      stackSize = newSize + 1;
      if (stackSize < 1) bug(1867);
      proofStack = malloc((size_t)(stackSize) * sizeof(struct pip_struct));
      if (!proofStack) bug(1861);
      for (i = 0; i < stackSize; i++) {
        proofStack[i].proof = NULL_NMBRSTRING;
        proofStack[i].target = NULL_PNTRSTRING;
        proofStack[i].source = NULL_PNTRSTRING;
        proofStack[i].user = NULL_PNTRSTRING;
      }
      pntrLet(&infoStack, pntrSpace(stackSize));
      break;

    case PUS_PUSH:


      if (stackPtr < stackEnd) {
        for (i = stackPtr + 1; i <= stackEnd; i++) {
          deallocProofStruct(&(proofStack[i]));
          let((vstring *)(&(infoStack[i])), "");
        }
        stackEnd = stackPtr;
      }

      if (stackPtr == stackSize - 1) {
        stackOverflowed = 1;
        deallocProofStruct(&(proofStack[0]));
        let((vstring *)(&(infoStack[0])), "");
        for (i = 0; i < stackSize - 1; i++) {
          proofStack[i].proof = proofStack[i + 1].proof;
          proofStack[i].target = proofStack[i + 1].target;
          proofStack[i].source = proofStack[i + 1].source;
          proofStack[i].user = proofStack[i + 1].user;
          infoStack[i] = infoStack[i + 1];
        }
        proofStack[stackPtr].proof = NULL_NMBRSTRING;
        proofStack[stackPtr].target = NULL_PNTRSTRING;
        proofStack[stackPtr].source = NULL_PNTRSTRING;
        proofStack[stackPtr].user = NULL_PNTRSTRING;
        infoStack[stackPtr] = "";
        stackPtr--;
        stackEnd--;
        if (stackPtr != stackSize - 2 || stackPtr != stackEnd) bug(1862);
      }


      stackPtr++;
      stackEnd++;
      if (stackPtr != stackEnd) bug(1863);
      copyProofStruct(&(proofStack[stackPtr]), *proofStruct);
      let((vstring *)(&(infoStack[stackPtr])), info);
      break;

    case PUS_UNDO:
      if (stackPtr < 0) bug(1864);
      if (stackPtr == 0) {
        if (stackOverflowed == 0) {
          mminou.print2("There is nothing to undo.\n");
        } else {
          printLongLine(cat("Exceeded maximum of ", str((double)stackSize - 1),
              " UNDOs.  To increase the number, see HELP SET UNDO.",
              null), "", " ");
        }
        break;
      }


      printLongLine(cat("Undid:  ", infoStack[stackPtr],
              null), "", " ");
      stackPtr--;

      copyProofStruct(&(*proofStruct), proofStack[stackPtr]);
      break;

    case PUS_REDO:
      if (stackPtr == stackEnd) {
        mminou.print2("There is nothing more to redo.\n");
        break;
      }


      stackPtr++;

      copyProofStruct(&(*proofStruct), proofStack[stackPtr]);
      printLongLine(cat("Redid:  ", infoStack[stackPtr],
              null), "", " ");
      break;

    default:
      bug(1865);
  }

  if (stackPtr < -1 || stackPtr > stackEnd || stackPtr > stackSize - 1
      || stackEnd < -1 || stackEnd > stackSize -1 ) {
    bug(1866);
  }

  if (action == PUS_GET_STATUS) {
    return (stackOverflowed || stackAborted || stackPtr != 0);
  } else {
    return stackSize - 1;
  }
}
}