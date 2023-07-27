package xd.metamath;

public class hmmunif {


















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

}