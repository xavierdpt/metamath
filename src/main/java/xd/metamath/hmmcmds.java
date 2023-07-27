package xd.metamath;

public class hmmcmds {







#ifndef METAMATH_MMCMDS_H_
#define METAMATH_MMCMDS_H_

#include "mmvstr.h"
#include "mmdata.h"


void typeStatement(long statemNum,
  flag briefFlag,
  flag commentOnlyFlag,
  flag texFlag,
  flag htmlFlg);

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
  long startColumn,
  flag skipRepeatedSteps,
  flag texFlag,
  flag htmlFlg);

void showDetailStep(long statemNum, long detailStep);

void proofStmtSumm(long statemNum, flag essentialFlag, flag texFlag);

flag traceProof(long statemNum,
  flag essentialFlag,
  flag axiomFlag,
  vstring matchList,
  vstring traceToList,
  flag testOnlyFlag );
void traceProofWork(long statemNum,
  flag essentialFlag,
  vstring traceToList,
  vstring *statementUsedFlagsP,
  nmbrString **unprovedListP);

void traceProofTree(long statemNum,
  flag essentialFlag, long endIndent);
void traceProofTreeRec(long statemNum,
  flag essentialFlag, long endIndent, long recursDepth);



double countSteps(long statemNum, flag essentialFlag);

vstring traceUsage(long statemNum,
  flag recursiveFlag,
  long cutoffStmt  );
vstring htmlDummyVars(long showStmt);
vstring htmlAllowedSubst(long showStmt);

void readInput(void);

void writeSource(
                flag reformatFlag ,

                flag splitFlag,
                flag noVersioningFlag,
                flag keepSplitsFlag,
                vstring extractLabels
                );



void writeExtractedSource(vstring extractLabels,
  vstring fullOutput_fn, flag noVersioningFlag);

void fixUndefinedLabels(vstring extractNeeded, vstring *buf);

void writeDict(void);
void eraseSource(void);
void verifyProofs(vstring labelMatch, flag verifyFlag);



void verifyMarkup(vstring labelMatch, flag dateSkip, flag topDateSkip,
    flag fileSkip,
    flag underscoreSkip,
    flag mathboxSkip,
    flag verboseMode);


void processMarkup(vstring inputFileName, vstring outputFileName,
    flag processCss, long actionBits);


void showDiscouraged(void);


long getStepNum(vstring relStep,
   nmbrString *pfInProgress,
   flag allFlag );



nmbrString *getRelStepNums(nmbrString *pfInProgress);


long getStatementNum(vstring stmtName,
    long startStmt,
    long maxStmt,
    flag aAllowed,
    flag pAllowed,
    flag eAllowed,
    flag fAllowed,
    flag efOnlyForMaxStmt,
    flag uniqueFlag);




extern flag g_printHelp;
void H(vstring helpLine);


extern flag g_midiFlag;
extern vstring g_midiParam;
void outputMidi(long plen, nmbrString *indentationLevels,
  nmbrString *logicalFlags, vstring g_midiParameter, vstring statementLabel);


#endif
}