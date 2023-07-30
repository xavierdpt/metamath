package xd.metamath;

public class getStep_struct {
    long stepNum;
    long sourceStmt;
    long targetStmt;
    long targetParentStep;
    long targetParentStmt;
    P<nmbrString> sourceHyps;
    P<nmbrString> sourceSubstsNmbr;
    P<pntrString> sourceSubstsPntr;
    P<nmbrString> targetSubstsNmbr;
    P<pntrString> targetSubstsPntr;
}
