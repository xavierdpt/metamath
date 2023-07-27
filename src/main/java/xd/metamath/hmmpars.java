package xd.metamath;

public class hmmpars {









char *readRawSource(
    vstring inputBuf, long *size);
void parseKeywords(void);
void parseLabels(void);
void parseMathDecl(void);
void parseStatements(void);
char parseProof(long statemNum);
char parseCompressedProof(long statemNum);
nmbrString *getProof(long statemNum, flag printFlag);

void rawSourceError(char *startFile, char *ptr, long tokenLen,

     vstring errMsg);
void sourceError(char *ptr, long tokenLen, long stmtNum, vstring errMsg);
void mathTokenError(long tokenNum ,
    nmbrString *tokenList, long stmtNum, vstring errMsg);
vstring shortDumpRPNStack(void);


int labelSortCmp(const void *key1, const void *key2);


int labelSrchCmp(const void *key, const void *data);


int mathSortCmp(const void *key1, const void *key2);


int mathSrchCmp(const void *key, const void *data);


int hypAndLocSortCmp(const void *key1, const void *key2);


int hypAndLocSrchCmp(const void *key, const void *data);

long whiteSpaceLen(char *ptr);



long rawWhiteSpaceLen(char *ptr);

long tokenLen(char *ptr);

long rawTokenLen(char *ptr);

long proofTokenLen(char *ptr);


long countLines(vstring start, long length);




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

nmbrString *parseMathTokens(vstring userText, long statemNum);


vstring outputStatement(long stmt,  flag reformatFlag);


vstring rewrapComment(vstring comment);


long lookupLabel(vstring label);




void getNextInclusion(char *fileBuf, long startOffset,

    long *cmdPos1, long *cmdPos2,
    long *endPos1, long *endPos2,
    char *cmdType,
    vstring *fileName
    );

vstring writeSourceToBuffer(void);


void writeSplitSource(vstring *fileBuf, vstring fileName,
    flag noVersioningFlag, flag noDeleteFlag);

void deleteSplits(vstring *fileBuf, flag noVersioningFlag);





vstring getFileAndLineNum(vstring buffPtr,
    vstring currentPtr,
    long *lineNum);



void assignStmtFileAndLineNum(long stmtNum);


vstring readSourceAndIncludes(vstring inputFn, long *size);


char *readInclude(vstring fileBuf, long fileBufOffset,
     vstring sourceFileName,
    long *size, long parentLineNum, flag *errorFlag);

}