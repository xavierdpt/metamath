package xd.metamath;

public class hmmpfas {














struct pip_struct {
  nmbrString *proof;
  pntrString *target;
  pntrString *source;
  pntrString *user;
};


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



     static final long PUS_INIT = D.PUS_INIT;
     static final long PUS_PUSH = D.PUS_PUSH;
     static final long PUS_UNDO = D.PUS_UNDO;
     static final long PUS_REDO = D.PUS_REDO;
     static final long PUS_NEW_SIZE = D.PUS_NEW_SIZE;
     static final long PUS_GET_SIZE = D.PUS_GET_SIZE;
     static final long PUS_GET_STATUS = D.PUS_GET_STATUS;

long processUndoStack(struct pip_struct *proofStruct,
    char action,
    vstring info,
    long newStackSize);

}