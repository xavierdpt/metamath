package xd.metamath;

public class hmmveri {







#ifndef METAMATH_MMVERI_H_
#define METAMATH_MMVERI_H_

#include "mmdata.h"

char verifyProof(long statemNum);

nmbrString *assignVar(nmbrString *bigSubstSchemeAss,
  nmbrString *bigSubstInstAss, long substScheme,

  long statementNum, long step, flag unkHypFlag);

void cleanWrkProof(void);




struct getStep_struct {
  long stepNum;
  long sourceStmt;
  long targetStmt;
  long targetParentStep;
  long targetParentStmt;
  nmbrString *sourceHyps;
  nmbrString *sourceSubstsNmbr;
  pntrString *sourceSubstsPntr;
  nmbrString *targetSubstsNmbr;
  pntrString *targetSubstsPntr;
};
extern struct getStep_struct getStep;

#endif
}