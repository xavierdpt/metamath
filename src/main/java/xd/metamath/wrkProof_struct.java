package xd.metamath;

public class wrkProof_struct {
    long numTokens;
    long numSteps;
    long numLocalLabels;
    long numHypAndLoc;
    P<Byte> localLabelPoolPtr;
    long RPNStackPtr;
    long errorCount;
    flag errorSeverity;

    P<nmbrString>tokenSrcPtrNmbr;
    P<pntrString> tokenSrcPtrPntr;
    P<nmbrString> stepSrcPtrNmbr;
    P<pntrString> stepSrcPtrPntr;
    P<flag> localLabelFlag;
    P<sortHypAndLoc> hypAndLocLabel;

    P<Byte> localLabelPool;
    P<nmbrString> proofString;
    P<pntrString> mathStringPtrs;

    P<nmbrString> RPNStack;


    P<nmbrString> compressedPfLabelMap;
    long compressedPfNumLabels;
}
