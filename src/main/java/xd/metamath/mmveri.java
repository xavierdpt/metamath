package xd.metamath;

public class mmveri {










struct getStep_struct getStep = {0, 0, 0, 0, 0,
    NULL_NMBRSTRING, NULL_NMBRSTRING, NULL_PNTRSTRING, NULL_NMBRSTRING,
    NULL_PNTRSTRING};

char verifyProof(long statemNum)
{

  long stmt;
  long i, j, step;
  char type;
  char *fbPtr;
  long tokenLength;
  long numReqHyp;
  char returnFlag = 0;
  nmbrString *nmbrTmpPtr;
  nmbrString *nmbrHypPtr;
  nmbrString *bigSubstSchemeHyp = NULL_NMBRSTRING;
  nmbrString *bigSubstInstHyp = NULL_NMBRSTRING;
  flag unkHypFlag;
  nmbrString *nmbrTmp = NULL_NMBRSTRING;

  if (g_Statement[statemNum].type != p_) return (4);



  for (i = 0; i < g_WrkProof.numSteps; i++) {
    g_WrkProof.mathStringPtrs[i] = NULL_NMBRSTRING;
  }

  if (g_WrkProof.errorSeverity > 2) return (g_WrkProof.errorSeverity);

  g_WrkProof.RPNStackPtr = 0;

  if (g_WrkProof.numSteps == 0) return (2);


  for (step = 0; step < g_WrkProof.numSteps; step++) {

    stmt = g_WrkProof.proofString[step];


    if (stmt == -(long)'?') {
      if (returnFlag < 1) returnFlag = 1;


      g_WrkProof.RPNStack[g_WrkProof.RPNStackPtr] = step;

      g_WrkProof.RPNStackPtr++;

      continue;
    }


    if (stmt < 0) {

      if (stmt > -1000) bug(2101);
      i = -1000 - stmt;


      g_WrkProof.RPNStack[g_WrkProof.RPNStackPtr] = step;
      g_WrkProof.RPNStackPtr++;

      g_WrkProof.mathStringPtrs[step] =
          g_WrkProof.mathStringPtrs[i];

      continue;
    }

    type = g_Statement[stmt].type;


    if (type == e_ || type == f_) {



      g_WrkProof.RPNStack[g_WrkProof.RPNStackPtr] = step;
      g_WrkProof.RPNStackPtr++;

      g_WrkProof.mathStringPtrs[step] =
          g_Statement[stmt].mathString;

      continue;
    }


    if (type != a_ && type != p_) bug(2102);


    numReqHyp = g_Statement[stmt].numReqHyp;
    nmbrHypPtr = g_Statement[stmt].reqHypList;





    nmbrLet(&bigSubstSchemeHyp, nmbrAddElement(NULL_NMBRSTRING, g_mathTokens));
    nmbrLet(&bigSubstInstHyp, nmbrAddElement(NULL_NMBRSTRING, g_mathTokens));
    unkHypFlag = 0;
    j = 0;
    for (i = g_WrkProof.RPNStackPtr - numReqHyp; i < g_WrkProof.RPNStackPtr; i++) {
      nmbrTmpPtr = g_WrkProof.mathStringPtrs[
          g_WrkProof.RPNStack[i]];
      if (nmbrTmpPtr[0] == -1) {
        unkHypFlag = 1;

        nmbrLet(&bigSubstSchemeHyp,
            nmbrCat(bigSubstSchemeHyp,
            nmbrAddElement(nmbrTmpPtr, g_mathTokens), NULL));
      } else {
        nmbrLet(&bigSubstSchemeHyp,
            nmbrCat(bigSubstSchemeHyp,
            nmbrAddElement(g_Statement[nmbrHypPtr[j]].mathString,
            g_mathTokens), NULL));
      }
      nmbrLet(&bigSubstInstHyp,
          nmbrCat(bigSubstInstHyp,
          nmbrAddElement(nmbrTmpPtr, g_mathTokens), NULL));
      j++;


      if (getStep.stepNum) {
        if (g_WrkProof.RPNStack[i] == getStep.stepNum - 1) {

          getStep.targetParentStep = step + 1;
          getStep.targetParentStmt = stmt;
        }
        if (step == getStep.stepNum - 1) {

          nmbrLet(&getStep.sourceHyps, nmbrAddElement(getStep.sourceHyps,
              g_WrkProof.RPNStack[i]));
        }
      }

    }

if(db7)printLongLine(cat("step ", str((double)step+1), " sch ",
    nmbrCvtMToVString(bigSubstSchemeHyp), NULL), "", " ");
if(db7)printLongLine(cat("step ", str((double)step+1), " ins ",
    nmbrCvtMToVString(bigSubstInstHyp), NULL), "", " ");
    nmbrTmpPtr = assignVar(bigSubstSchemeHyp,
        bigSubstInstHyp, stmt, statemNum, step, unkHypFlag);
if(db7)printLongLine(cat("step ", str((double)step+1), " res ",
    nmbrCvtMToVString(nmbrTmpPtr), NULL), "", " ");


    nmbrLet(&nmbrTmp, NULL_NMBRSTRING);

    g_WrkProof.mathStringPtrs[step] = nmbrTmpPtr;
    if (nmbrTmpPtr[0] == -1) {
      if (!unkHypFlag) {
        returnFlag = 2;
      }
    }



    g_WrkProof.RPNStackPtr = g_WrkProof.RPNStackPtr - numReqHyp;
    g_WrkProof.RPNStack[g_WrkProof.RPNStackPtr] = step;
    g_WrkProof.RPNStackPtr++;

  }

  if (g_WrkProof.RPNStackPtr != 1) bug(2108);


  if (returnFlag == 0) {
    if (!nmbrEq(g_Statement[statemNum].mathString,
        g_WrkProof.mathStringPtrs[g_WrkProof.numSteps - 1])) {
      if (!g_WrkProof.errorCount) {
        fbPtr = g_WrkProof.stepSrcPtrPntr[g_WrkProof.numSteps - 1];
        tokenLength = g_WrkProof.stepSrcPtrNmbr[g_WrkProof.numSteps - 1];

        sourceError(fbPtr, tokenLength, statemNum, cat(
            "The result of the proof (step ", str((double)(g_WrkProof.numSteps)),
            ") does not match the statement being proved.  The result is \"",
            nmbrCvtMToVString(
            g_WrkProof.mathStringPtrs[g_WrkProof.numSteps - 1]),
            "\" but the statement is \"",
            nmbrCvtMToVString(g_Statement[statemNum].mathString),
            "\".  Type \"SHOW PROOF ",g_Statement[statemNum].labelName,
            "\" to see the proof attempt.",NULL));
      }
      g_WrkProof.errorCount++;
    }
  }

  nmbrLet(&bigSubstSchemeHyp, NULL_NMBRSTRING);
  nmbrLet(&bigSubstInstHyp, NULL_NMBRSTRING);

  return (returnFlag);

}




nmbrString *assignVar(nmbrString *bigSubstSchemeAss,
  nmbrString *bigSubstInstAss, long substScheme,

  long statementNum, long step, flag unkHypFlag)
{
  nmbrString *bigSubstSchemeVars = NULL_NMBRSTRING;
  nmbrString *substSchemeFrstVarOcc = NULL_NMBRSTRING;
  nmbrString *varAssLen = NULL_NMBRSTRING;
  nmbrString *substInstFrstVarOcc = NULL_NMBRSTRING;
  nmbrString *result = NULL_NMBRSTRING;
  long bigSubstSchemeLen,bigSubstInstLen,bigSubstSchemeVarLen,substSchemeLen,
      resultLen;
  long i,v,v1,p,q,tokenNum;
  flag breakFlag, contFlag;
  vstring tmpStr = "";
  vstring tmpStr2 = "";
  flag ambiguityCheckFlag = 0;
  nmbrString *saveResult = NULL_NMBRSTRING;


  nmbrString *nmbrTmpPtrAS;
  nmbrString *nmbrTmpPtrBS;
  nmbrString *nmbrTmpPtrAIR;
  nmbrString *nmbrTmpPtrBIR;
  nmbrString *nmbrTmpPtrAIO;
  nmbrString *nmbrTmpPtrBIO;
  long dLen, pos, substAPos, substALen, instAPos, substBPos, substBLen,
      instBPos, a, b, aToken, bToken, aToken2, bToken2, dILenR, dILenO,
      optStart, reqStart;
  flag foundFlag;


  long j,k,m;
  long numReqHyp;
  nmbrString *nmbrHypPtr;

  nmbrString *nmbrTmp = NULL_NMBRSTRING;

  long nmbrSaveTempAllocStack;

  dILenR = 0;
  dILenO = 0;
  optStart = 0;
  reqStart = 0;
  nmbrTmpPtrAIR = NULL_NMBRSTRING;
  nmbrTmpPtrBIR = NULL_NMBRSTRING;
  nmbrTmpPtrAIO = NULL_NMBRSTRING;
  nmbrTmpPtrBIO = NULL_NMBRSTRING;

  nmbrSaveTempAllocStack = g_nmbrStartTempAllocStack;
  g_nmbrStartTempAllocStack = g_nmbrTempAllocStackTop;

  bigSubstSchemeLen = nmbrLen(bigSubstSchemeAss);
  bigSubstInstLen = nmbrLen(bigSubstInstAss);
  nmbrLet(&bigSubstSchemeVars,nmbrExtractVars(bigSubstSchemeAss));
  bigSubstSchemeVarLen = nmbrLen(bigSubstSchemeVars);

  if (!bigSubstSchemeVarLen) {
    if (!nmbrLen(g_Statement[substScheme].reqVarList)) {
      nmbrLet(&result,g_Statement[substScheme].mathString);
    } else {

      if (!unkHypFlag) bug(2109);
    }
    goto returnPoint;
  }

  nmbrLet(&substSchemeFrstVarOcc,nmbrSpace(bigSubstSchemeVarLen));
  nmbrLet(&varAssLen,substSchemeFrstVarOcc);
  nmbrLet(&substInstFrstVarOcc,substSchemeFrstVarOcc);

  if (bigSubstSchemeVarLen != nmbrLen(g_Statement[substScheme].reqVarList)) {
    if (unkHypFlag) {
      goto returnPoint;
    } else {
      if (!g_WrkProof.errorCount) bug(2103);
      goto returnPoint;
    }
  }

  for (i = 0; i < bigSubstSchemeVarLen; i++) {
    substSchemeFrstVarOcc[i] = -1;
  }

  for (i = 0; i < bigSubstSchemeVarLen; i++) {
    g_MathToken[bigSubstSchemeVars[i]].tmp = i;
  }

  for (i = 0; i < bigSubstSchemeLen; i++) {
    if (g_MathToken[bigSubstSchemeAss[i]].tokenType ==
        (char)var_) {
      if (substSchemeFrstVarOcc[g_MathToken[bigSubstSchemeAss[
          i]].tmp] == -1) {
        substSchemeFrstVarOcc[g_MathToken[bigSubstSchemeAss[
            i]].tmp] = i;
      }
    }
  }


  v = -1;
  p = 0;
  q = 0;
 ambiguityCheck:
  while (p != bigSubstSchemeLen-1 || q != bigSubstInstLen-1) {
if(db7&&v>=0)printLongLine(cat("p ", str((double)p), " q ", str((double)q), " VAR ",str((double)v),
    " ASSIGNED ", nmbrCvtMToVString(
    nmbrMid(bigSubstInstAss,substInstFrstVarOcc[v]+1,
    varAssLen[v])), NULL), "", " ");
if(db7)nmbrLet(&bigSubstInstAss,bigSubstInstAss);
if(db7){print2("Enter scan: v=%ld,p=%ld,q=%ld\n",v,p,q); let(&tmpStr,"");}
    tokenNum = bigSubstSchemeAss[p];
    if (g_MathToken[tokenNum].tokenType == (char)con_) {

      if (tokenNum == bigSubstInstAss[q]) {
        p++;
        q++;
if(db7)print2(" Exit, c ok: v=%ld,p=%ld,q=%ld\n",v,p,q);
        continue;
      } else {

        breakFlag = 0;
        contFlag = 1;
        while (contFlag) {
          if (v < 0) {
            breakFlag = 1;
            break;
          }
          varAssLen[v]++;
          p = substSchemeFrstVarOcc[v] + 1;
          q = substInstFrstVarOcc[v] + varAssLen[v];
          contFlag = 0;
          if (bigSubstInstAss[q-1] == g_mathTokens) {
if(db7){print2("GOT TO DUMMY TOKEN1\n");}
            v--;
            contFlag = 1;
            continue;
          }
          if (q >= bigSubstInstLen) {

            v--;
            contFlag = 1;
            bug(2104);
          }
        }
        if (breakFlag) {
if(db7)print2(" Exit, c bktrk bad: v=%ld,p=%ld,q=%ld\n",v,p,q);
          break;
        }
if(db7)print2(" Exit, c bktrk ok: v=%ld,p=%ld,q=%ld\n",v,p,q);
      }
    } else {

      v1 = g_MathToken[tokenNum].tmp;
      if (v1 > v) {
        if (v1 != v + 1) bug(2105);
        v = v1;
        varAssLen[v] = 0;
        substInstFrstVarOcc[v] = q;
        p++;
if(db7)print2(" Exit, v new: v=%ld,p=%ld,q=%ld\n",v,p,q);
        continue;
      } else {
        breakFlag = 0;
        for (i = 0; i < varAssLen[v1]; i++) {
          if (q + i >= bigSubstInstLen) {

            breakFlag = 1;
            break;
          }
          if (bigSubstInstAss[substInstFrstVarOcc[v1] + i] !=
              bigSubstInstAss[q + i]) {

            breakFlag = 1;
            break;
          }
        }
        if (breakFlag) {

          breakFlag = 0;
          contFlag = 1;
          while (contFlag) {
            if (v < 0) {
              breakFlag = 1;
              break;
            }
            varAssLen[v]++;
            p = substSchemeFrstVarOcc[v] + 1;
            q = substInstFrstVarOcc[v] + varAssLen[v];
            contFlag = 0;
            if (bigSubstInstAss[q-1] == g_mathTokens) {
if(db7)print2("GOT TO DUMMY TOKEN\n");
              v--;
              contFlag = 1;
              continue;
            }
            if (q >= bigSubstInstLen) {

              v--;
              contFlag = 1;
              bug(2106);
            }
          }
          if (breakFlag) {
if(db7){print2(" Exit, vold bck bad: v=%ld,p=%ld,q=%ld\n",v,p,q);}
            break;
          }
if(db7)print2(" Exit, vold bck ok: v=%ld,p=%ld,q=%ld\n",v,p,q);
          continue;
        } else {
          p++;
          q = q + varAssLen[v1];
if(db7)print2(" Exit, vold ok: v=%ld,p=%ld,q=%ld\n",v,p,q);
          continue;
        }
      }
    }
  }

if(db7)printLongLine(cat("BIGVR ", nmbrCvtMToVString(bigSubstSchemeVars),
    NULL), "", " ");
if(db7)print2(
"p=%ld,bigSubstSchemeLen=%ld;q=%ld,bigSubstInstLen=%ld;v=%ld,bigSubstSchemeVarLen=%ld\n",
  p,bigSubstSchemeLen,q,bigSubstInstLen,v,bigSubstSchemeVarLen);

  if (v == -1) {
    if (ambiguityCheckFlag) {

      goto returnPoint;
    }
    if (!g_WrkProof.errorCount) {
      let(&tmpStr, "");
      j = g_Statement[substScheme].numReqHyp;
      for (i = 0; i < j; i++) {
        k = g_WrkProof.RPNStack[g_WrkProof.RPNStackPtr - j + i];
        let(&tmpStr2, nmbrCvtMToVString(g_WrkProof.mathStringPtrs[k]));
        if (tmpStr2[0] == 0) let(&tmpStr2,
            "? (Unknown step or previous error; unification ignored)");
        let(&tmpStr, cat(tmpStr, "\n  Hypothesis ", str((double)i + 1), ":  ",
            nmbrCvtMToVString(
                g_Statement[g_Statement[substScheme].reqHypList[i]].mathString),
            "\n  Step ", str((double)k + 1),
            ":  ", tmpStr2, NULL));
      }

      sourceError(g_WrkProof.stepSrcPtrPntr[step],
          g_WrkProof.stepSrcPtrNmbr[step],
          statementNum, cat(
          "The hypotheses of statement \"", g_Statement[substScheme].labelName,
          "\" at proof step ", str((double)step + 1),
          " cannot be unified.", tmpStr, NULL));

      let(&tmpStr, "");
      let(&tmpStr2, "");
    }
    g_WrkProof.errorCount++;
    goto returnPoint;
  }
  if (p != bigSubstSchemeLen - 1 || q != bigSubstInstLen - 1
      || v != bigSubstSchemeVarLen - 1) bug(2107);

  if (ambiguityCheckFlag) {
    if (unkHypFlag) {
      nmbrLet(&result,NULL_NMBRSTRING);
      goto returnPoint;
    }
    nmbrLet(&saveResult, result);
    nmbrLet(&result, NULL_NMBRSTRING);
  }



  if (!ambiguityCheckFlag) {
    if (getStep.stepNum) {

      if (step + 1 == getStep.stepNum) {
        nmbrLet(&getStep.sourceSubstsNmbr, nmbrExtractVars(
            g_Statement[substScheme].mathString));
        k = nmbrLen(getStep.sourceSubstsNmbr);
        pntrLet(&getStep.sourceSubstsPntr,
            pntrNSpace(k));
        for (m = 0; m < k; m++) {
          pos = g_MathToken[getStep.sourceSubstsNmbr[m]].tmp;
          nmbrLet((nmbrString **)(&getStep.sourceSubstsPntr[m]),
              nmbrMid(bigSubstInstAss,
              substInstFrstVarOcc[pos] + 1,
              varAssLen[pos])  );
        }
      }

      j = 0;
      numReqHyp = g_Statement[substScheme].numReqHyp;
      nmbrHypPtr = g_Statement[substScheme].reqHypList;
      for (i = g_WrkProof.RPNStackPtr - numReqHyp; i < g_WrkProof.RPNStackPtr; i++) {
        if (g_WrkProof.RPNStack[i] == getStep.stepNum - 1) {

          nmbrLet(&getStep.targetSubstsNmbr, nmbrExtractVars(
              g_Statement[nmbrHypPtr[j]].mathString));
          k = nmbrLen(getStep.targetSubstsNmbr);
          pntrLet(&getStep.targetSubstsPntr, pntrNSpace(k));
          for (m = 0; m < k; m++) {
            pos = g_MathToken[getStep.targetSubstsNmbr[m]].tmp;

            nmbrLet((nmbrString **)(&getStep.targetSubstsPntr[m]),
                nmbrMid(bigSubstInstAss,
                substInstFrstVarOcc[pos] + 1,
                varAssLen[pos])  );
          }
        }
        j++;
      }
    }
  }




  if (!ambiguityCheckFlag) {
    nmbrTmpPtrAS = g_Statement[substScheme].reqDisjVarsA;
    nmbrTmpPtrBS = g_Statement[substScheme].reqDisjVarsB;
    dLen = nmbrLen(nmbrTmpPtrAS);
    if (dLen) {

      nmbrTmpPtrAIR = g_Statement[statementNum].reqDisjVarsA;
      nmbrTmpPtrBIR = g_Statement[statementNum].reqDisjVarsB;
      dILenR = nmbrLen(nmbrTmpPtrAIR);
      nmbrTmpPtrAIO = g_Statement[statementNum].optDisjVarsA;
      nmbrTmpPtrBIO = g_Statement[statementNum].optDisjVarsB;
      dILenO = nmbrLen(nmbrTmpPtrAIO);
    }
    for (pos = 0; pos < dLen; pos++) {
      substAPos = g_MathToken[nmbrTmpPtrAS[pos]].tmp;
      substALen = varAssLen[substAPos];
      instAPos = substInstFrstVarOcc[substAPos];
      substBPos = g_MathToken[nmbrTmpPtrBS[pos]].tmp;
      substBLen = varAssLen[substBPos];
      instBPos = substInstFrstVarOcc[substBPos];
      for (a = 0; a < substALen; a++) {
        aToken = bigSubstInstAss[instAPos + a];
        if (g_MathToken[aToken].tokenType == (char)con_) continue;


        foundFlag = 0;
        for (i = 0; i < dILenR; i++) {
          if (nmbrTmpPtrAIR[i] == aToken
              || nmbrTmpPtrBIR[i] == aToken) {
            foundFlag = 1;
            reqStart = i;
            break;
          }
        }

        if (!foundFlag) {
          reqStart = dILenR;
          foundFlag = 0;
          for (i = 0; i < dILenO; i++) {
            if (nmbrTmpPtrAIO[i] == aToken
                || nmbrTmpPtrBIO[i] == aToken) {
              foundFlag = 1;
              optStart = i;
              break;
            }
          }
          if (!foundFlag) optStart = dILenO;
        } else {
          optStart = 0;
        }


        for (b = 0; b < substBLen; b++) {
          bToken = bigSubstInstAss[instBPos + b];
          if (g_MathToken[bToken].tokenType == (char)con_) continue;
          if (aToken == bToken) {
            if (!g_WrkProof.errorCount) {
              sourceError(g_WrkProof.stepSrcPtrPntr[step],
                  g_WrkProof.stepSrcPtrNmbr[step],
                  statementNum, cat(
                  "There is a disjoint variable ($d) violation at proof step ",
                  str((double)step + 1),".  Assertion \"",
                  g_Statement[substScheme].labelName,
                  "\" requires that variables \"",
                  g_MathToken[nmbrTmpPtrAS[pos]].tokenName,
                  "\" and \"",
                  g_MathToken[nmbrTmpPtrBS[pos]].tokenName,
                  "\" be disjoint.  But \"",
                  g_MathToken[nmbrTmpPtrAS[pos]].tokenName,
                  "\" was substituted with \"",
                  nmbrCvtMToVString(nmbrMid(bigSubstInstAss,instAPos + 1,
                      substALen)),
                  "\" and \"",
                  g_MathToken[nmbrTmpPtrBS[pos]].tokenName,
                  "\" was substituted with \"",
                  nmbrCvtMToVString(nmbrMid(bigSubstInstAss,instBPos + 1,
                      substBLen)),
                  "\".  These substitutions have variable \"",
                  g_MathToken[aToken].tokenName,
                  "\" in common.",
                  NULL));
              let(&tmpStr, "");
              nmbrLet(&nmbrTmp,NULL_NMBRSTRING);
            }
          } else {


            if (aToken > bToken) {
              aToken2 = bToken;
              bToken2 = aToken;
            } else {
              aToken2 = aToken;
              bToken2 = bToken;
            }

            foundFlag = 0;
            for (i = reqStart; i < dILenR; i++) {
              if (nmbrTmpPtrAIR[i] == aToken2) {
                if (nmbrTmpPtrBIR[i] == bToken2) {
                  foundFlag = 1;
                  break;
                }
              }
            }

            if (!foundFlag) {
              foundFlag = 0;
              for (i = optStart; i < dILenO; i++) {
                if (nmbrTmpPtrAIO[i] == aToken2) {
                  if (nmbrTmpPtrBIO[i] == bToken2) {
                    foundFlag = 1;
                    break;
                  }
                }
              }
            }

            if (!foundFlag) {
              if (!g_WrkProof.errorCount) {
                sourceError(g_WrkProof.stepSrcPtrPntr[step],
                    g_WrkProof.stepSrcPtrNmbr[step],
                    statementNum, cat(
                   "There is a disjoint variable ($d) violation at proof step ",
                    str((double)step + 1), ".  Assertion \"",
                    g_Statement[substScheme].labelName,
                    "\" requires that variables \"",
                    g_MathToken[nmbrTmpPtrAS[pos]].tokenName,
                    "\" and \"",
                    g_MathToken[nmbrTmpPtrBS[pos]].tokenName,
                    "\" be disjoint.  But \"",
                    g_MathToken[nmbrTmpPtrAS[pos]].tokenName,
                    "\" was substituted with \"",
                    nmbrCvtMToVString(nmbrMid(bigSubstInstAss, instAPos + 1,
                        substALen)),
                    "\" and \"",
                    g_MathToken[nmbrTmpPtrBS[pos]].tokenName,
                    "\" was substituted with \"",
                    nmbrCvtMToVString(nmbrMid(bigSubstInstAss, instBPos + 1,
                        substBLen)),
                    "\".", NULL));
                printLongLine(cat("Variables \"",

                    (strcmp(g_MathToken[aToken].tokenName,
                        g_MathToken[bToken].tokenName) < 0)
                      ? g_MathToken[aToken].tokenName
                      : g_MathToken[bToken].tokenName,
                    "\" and \"",
                    (strcmp(g_MathToken[aToken].tokenName,
                        g_MathToken[bToken].tokenName) < 0)
                      ? g_MathToken[bToken].tokenName
                      : g_MathToken[aToken].tokenName,
                    "\" do not have a disjoint variable requirement in the ",
                    "assertion being proved, \"",
                    g_Statement[statementNum].labelName,
                    "\".", NULL), "", " ");
                let(&tmpStr, "");
                nmbrLet(&nmbrTmp,NULL_NMBRSTRING);
              }
            }
          }
        }
      }
    }
  }



  substSchemeLen = nmbrLen(g_Statement[substScheme].mathString);

  q = 0;
  for (p = 0; p < substSchemeLen; p++) {
    tokenNum = g_Statement[substScheme].mathString[p];
    if (g_MathToken[tokenNum].tokenType == (char)con_) {
      q++;
    } else {
      q = q + varAssLen[g_MathToken[tokenNum].tmp];
    }
  }

  resultLen = q;
  nmbrLet(&result,nmbrSpace(resultLen));

  q = 0;
  for (p = 0; p < substSchemeLen; p++) {
    tokenNum = g_Statement[substScheme].mathString[p];
    if (g_MathToken[tokenNum].tokenType == (char)con_) {
      result[q] = tokenNum;
      q++;
    } else {
      for (i = 0; i < varAssLen[g_MathToken[tokenNum].tmp]; i++){
        result[q + i] = bigSubstInstAss[i +
            substInstFrstVarOcc[g_MathToken[tokenNum].tmp]];
      }
      q = q + i;
    }
  }
if(db7)printLongLine(cat("result ", nmbrCvtMToVString(result), NULL),""," ");

  if (ambiguityCheckFlag) {
    if (!g_WrkProof.errorCount) {

      sourceError(g_WrkProof.stepSrcPtrPntr[step],
          g_WrkProof.stepSrcPtrNmbr[step],
          statementNum, cat(
          "The unification with the hypotheses of the statement at proof step ",
          str((double)step + 1),
          " is not unique.  Two possible results at this step are \"",
          nmbrCvtMToVString(saveResult),
          "\" and \"",nmbrCvtMToVString(result),
          "\".  Type \"SHOW PROOF ",g_Statement[statementNum].labelName,
          "\" to see the proof attempt.",NULL));
    }
    g_WrkProof.errorCount++;
    goto returnPoint;
  } else {


    while (1) {
      v--;
      if (v < 0) {
        goto returnPoint;
      }
      varAssLen[v]++;
      p = substSchemeFrstVarOcc[v] + 1;
      q = substInstFrstVarOcc[v] + varAssLen[v];
      if (bigSubstInstAss[q - 1] != g_mathTokens) break;
      if (q >= bigSubstInstLen) bug(2110);
    }
    ambiguityCheckFlag = 1;
    goto ambiguityCheck;
  }


 returnPoint:


  for (i = 0; i < bigSubstSchemeVarLen; i++) {


    substSchemeFrstVarOcc[i] = 0;
    varAssLen[i] = 0;
    substInstFrstVarOcc[i] = 0;
  }
  nmbrLet(&bigSubstSchemeVars,NULL_NMBRSTRING);
  nmbrLet(&substSchemeFrstVarOcc,NULL_NMBRSTRING);
  nmbrLet(&varAssLen,NULL_NMBRSTRING);
  nmbrLet(&substInstFrstVarOcc,NULL_NMBRSTRING);
  nmbrLet(&saveResult,NULL_NMBRSTRING);

  g_nmbrStartTempAllocStack = nmbrSaveTempAllocStack;
  return(result);

}


void cleanWrkProof(void) {

  long step;
  char type;

  for (step = 0; step < g_WrkProof.numSteps; step++) {
    if (g_WrkProof.proofString[step] > 0) {
      type = g_Statement[g_WrkProof.proofString[step]].type;
      if (type == a_ || type == p_) {
        nmbrLet((nmbrString **)(&g_WrkProof.mathStringPtrs[step]),
            NULL_NMBRSTRING);
      }
    }
  }

}
}