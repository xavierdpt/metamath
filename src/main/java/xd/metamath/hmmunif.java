package xd.metamath;

public class hmmunif {







#ifndef METAMATH_MMUNIF_H_
#define METAMATH_MMUNIF_H_

#include "mmdata.h"

extern long g_minSubstLen;
extern long g_userMaxUnifTrials;

extern long g_unifTrialCount;

extern long g_unifTimeouts;
extern flag g_hentyFilter;


extern flag g_bracketMatchInit;


extern nmbrString *g_firstConst;

extern nmbrString *g_lastConst;
extern nmbrString *g_oneConst;


nmbrString *makeSubstUnif(flag *newVarFlag,
    nmbrString *trialScheme, pntrString *stateVector);


char unify(
    nmbrString *schemeA,
    nmbrString *schemeB,

    pntrString **stateVector,
    long reEntryFlag);


flag oneDirUnif(
    nmbrString *schemeA,
    nmbrString *schemeB,
    pntrString **stateVector,
    long reEntryFlag);


char uniqueUnif(
    nmbrString *schemeA,
    nmbrString *schemeB,
    pntrString **stateVector);

char unifyH(
    nmbrString *schemeA,
    nmbrString *schemeB,
    pntrString **stateVector,
    long reEntryFlag);


void purgeStateVector(pntrString **stateVector);


void printSubst(pntrString *stateVector);

#endif
}