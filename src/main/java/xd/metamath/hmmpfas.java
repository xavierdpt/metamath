package xd.metamath;

public class hmmpfas {







#ifndef METAMATH_MMPFAS_H_
#define METAMATH_MMPFAS_H_

#include "mmvstr.h"
#include "mmdata.h"

extern long g_proveStatement;
extern flag g_proofChangedFlag;

extern long g_userMaxProveFloat;

extern long g_dummyVars;
extern long g_pipDummyVars;



struct pip_struct {
  nmbrString *proof;
  pntrString *target;
  pntrString *source;
  pntrString *user;
};
extern struct pip_struct g_ProofInProgress;


void interactiveMatch(long step, long maxEssential);


void assignStatement(long statemNum, long step);


nmbrString *proveByReplacement(long prfStmt,
    long prfStep,
    flag noDistinct,
    flag dummyVarFlag,
    flag searchMethod,
    long improveDepth,
    flag overrideFlag,
    flag mathboxFlag
    );

nmbrString *replaceStatement(long replStatemNum,
    long prfStep,
    long provStmtNum,
    flag subProofFlag,
    flag noDistinct,
    flag searchMethod,
    long improveDepth,
    flag overrideFlag,
    flag mathboxFlag
    );


vstring getIndepKnownSteps(long proofStmt, long refStep);


vstring getKnownSubProofs(void);

void addSubProof(nmbrString *subProof, long step);


nmbrString *expandProof(nmbrString *targetProof,
    long sourceStmtNum );

void deleteSubProof(long step);

char checkStmtMatch(long statemNum, long step);

char checkMStringMatch(nmbrString *mString, long step);


nmbrString *proveFloating(nmbrString *mString, long statemNum, long maxEDepth,
    long step, flag noDistinct,

    flag overrideFlag,
    flag mathboxFlag
);


char quickMatchFilter(long trialStmt, nmbrString *mString,
    long dummyVarFlag );


void minimizeProof(long repStatemNum, long prvStatemNum, flag
    allowGrowthFlag);

void initStep(long step);

void assignKnownSubProofs(void);

void assignKnownSteps(long startStep, long sbProofLen);


void interactiveUnifyStep(long step, char messageFlag);


char interactiveUnify(nmbrString *schemeA, nmbrString *schemeB,
    pntrString **stateVector);


void autoUnify(flag congrats);

void makeSubstAll(pntrString *stateVector);


void replaceDummyVar(long dummyVar, nmbrString *mString);


long subproofLen(nmbrString *proof, long endStep);


char checkDummyVarIsolation(long testStep);




long getParentStep(long startStep);



void declareDummyVars(long numNewVars);


void copyProofStruct(struct pip_struct *outProofStruct,
    struct pip_struct inProofStruct);


void initProofStruct(struct pip_struct *proofStruct, nmbrString *proof,
    long proveStatement);


void deallocProofStruct(struct pip_struct *proofStruct);


#define PUS_INIT 1
#define PUS_PUSH 2
#define PUS_UNDO 3
#define PUS_REDO 4
#define PUS_NEW_SIZE 5
#define PUS_GET_SIZE 6
#define PUS_GET_STATUS 7

long processUndoStack(struct pip_struct *proofStruct,
    char action,
    vstring info,
    long newStackSize);

#endif
}