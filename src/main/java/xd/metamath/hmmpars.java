package xd.metamath;

public class hmmpars {






































struct sortHypAndLoc {
  long labelTokenNum;
  void *labelName;
};
struct wrkProof_struct {
  long numTokens;
  long numSteps;
  long numLocalLabels;
  long numHypAndLoc;
  char *localLabelPoolPtr;
  long RPNStackPtr;
  long errorCount;
  flag errorSeverity;

  nmbrString *tokenSrcPtrNmbr;
  pntrString *tokenSrcPtrPntr;
  nmbrString *stepSrcPtrNmbr;
  pntrString *stepSrcPtrPntr;
  flag *localLabelFlag;
  struct sortHypAndLoc *hypAndLocLabel;

  char *localLabelPool;
  nmbrString *proofString;
  pntrString *mathStringPtrs;

  nmbrString *RPNStack;


  nmbrString *compressedPfLabelMap;
  long compressedPfNumLabels;

};

































}