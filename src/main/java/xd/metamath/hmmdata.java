package xd.metamath;

public class hmmdata {










typedef char flag;


typedef long nmbrString;
typedef void* pntrString;



enum mTokenType { var_, con_ };
#define lb_ '{'
#define rb_ '}'
#define v_  'v'
#define c_  'c'
#define a_  'a'
#define d_  'd'
#define e_  'e'
#define f_  'f'
#define p_  'p'
#define eq_ '='
#define sc_ '.'
#define illegal_ '?'



struct statement_struct {
  long lineNum;
  vstring fileName;
  vstring labelName;
  flag uniqueLabel;
  char type;
  int scope;
  long beginScopeStatementNum;
  long endScopeStatementNum;
  vstring statementPtr;
  vstring labelSectionPtr;
  long labelSectionLen;

  char labelSectionChanged;
  vstring mathSectionPtr;
  long mathSectionLen;

  char mathSectionChanged;
  vstring proofSectionPtr;
  long proofSectionLen;

  char proofSectionChanged;
  nmbrString *mathString;
  long mathStringLen;
  nmbrString *proofString;
  nmbrString *reqHypList;
  nmbrString *optHypList;
  long numReqHyp;
  nmbrString *reqVarList;
  nmbrString *optVarList;
  nmbrString *reqDisjVarsA;
  nmbrString *reqDisjVarsB;
  nmbrString *reqDisjVarsStmt;
  nmbrString *optDisjVarsA;
  nmbrString *optDisjVarsB;
  nmbrString *optDisjVarsStmt;
  long pinkNumber;

  long headerStartStmt;
  };



struct includeCall_struct {
  vstring source_fn;
  vstring included_fn;
  long current_offset;
  long current_line;
  flag pushOrPop;
  vstring current_includeSource;
  long current_includeLength;
  };

struct mathToken_struct {
  vstring tokenName;
  long length;
  char tokenType;
  flag active;
  int scope;
  long tmp;
  long statement;
  long endStatement;
  };












#define PROOF_DISCOURAGED_MARKUP "(Proof modification is discouraged.)"
#define USAGE_DISCOURAGED_MARKUP "(New usage is discouraged.)"

#define PROOF_DISCOURAGED 1
#define USAGE_DISCOURAGED 2
#define RESET 0

#define GC_RESET 0
#define GC_RESET_STMT 10
#define CONTRIBUTOR 1
#define CONTRIB_DATE 2
#define REVISER 3
#define REVISE_DATE 4
#define SHORTENER 5
#define SHORTEN_DATE 6
#define MOST_RECENT_DATE 7
#define GC_ERROR_CHECK_SILENT 8
#define GC_ERROR_CHECK_PRINT 9





#define DEFAULT_CONTRIBUTOR "?who?"



void *poolFixedMalloc(long size );
void *poolMalloc(long size );
void poolFree(void *ptr);
void addToUsedPool(void *ptr);

void memFreePoolPurge(flag untilOK);

void getPoolStats(long *freeAlloc, long *usedAlloc, long *usedActual);


void initBigArrays(void);


long getFreeSpace(long max);


void outOfMemory(vstring msg);


void bug(int bugNum);



struct nullNmbrStruct {
    long poolLoc;
    long allocSize;
    long actualSize;
    nmbrString nullElement; };
#define NULL_NMBRSTRING &(g_NmbrNull.nullElement)


struct nullPntrStruct {
    long poolLoc;
    long allocSize;
    long actualSize;
    pntrString nullElement; };
#define NULL_PNTRSTRING &(g_PntrNull.nullElement)



flag matchesList(vstring testString, vstring pattern, char wildCard,
    char oneCharWildCard);


flag matches(vstring testString, vstring pattern, char wildCard,
    char oneCharWildCard);










void nmbrMakeTempAlloc(nmbrString *s);





void nmbrLet(nmbrString **target,nmbrString *source);


nmbrString *nmbrCat(nmbrString *string1,...);


nmbrString *nmbrSeg(nmbrString *sin, long p1, long p2);
nmbrString *nmbrMid(nmbrString *sin, long p, long l);
nmbrString *nmbrLeft(nmbrString *sin, long n);
nmbrString *nmbrRight(nmbrString *sin, long n);


nmbrString *nmbrSpace(long n);

long nmbrLen(nmbrString *s);
long nmbrAllocLen(nmbrString *s);
void nmbrZapLen(nmbrString *s, long length);


long nmbrInstr(long start, nmbrString *sin, nmbrString *s);



long nmbrRevInstr(long start_position,nmbrString *string1,
    nmbrString *string2);


int nmbrEq(nmbrString *sout,nmbrString *sin);


vstring nmbrCvtMToVString(nmbrString *s);



vstring nmbrCvtRToVString(nmbrString *s,
    flag explicitTargets,
    long statemNum);


nmbrString *nmbrGetProofStepNumbs(nmbrString *reason);

vstring nmbrCvtAnyToVString(nmbrString *s);


nmbrString *nmbrExtractVars(nmbrString *m);


long nmbrElementIn(long start, nmbrString *g, long element);


nmbrString *nmbrAddElement(nmbrString *g, long element);

nmbrString *nmbrUnion(nmbrString *m1,nmbrString *m2);

nmbrString *nmbrIntersection(nmbrString *m1,nmbrString *m2);

nmbrString *nmbrSetMinus(nmbrString *m1,nmbrString *m2);



long nmbrGetSubproofLen(nmbrString *proof, long step);

nmbrString *nmbrSquishProof(nmbrString *proof);

nmbrString *nmbrUnsquishProof(nmbrString *proof);

nmbrString *nmbrGetIndentation(nmbrString *proof,
  long startingLevel);

nmbrString *nmbrGetEssential(nmbrString *proof);

nmbrString *nmbrGetTargetHyp(nmbrString *proof, long statemNum);

vstring compressProof(nmbrString *proof, long statemNum,
    flag oldCompressionAlgorithm);


long compressedProofSize(nmbrString *proof, long statemNum);









void pntrMakeTempAlloc(pntrString *s);





void pntrLet(pntrString **target,pntrString *source);


pntrString *pntrCat(pntrString *string1,...);


pntrString *pntrSeg(pntrString *sin, long p1, long p2);
pntrString *pntrMid(pntrString *sin, long p, long length);
pntrString *pntrLeft(pntrString *sin, long n);
pntrString *pntrRight(pntrString *sin, long n);


pntrString *pntrSpace(long n);

pntrString *pntrNSpace(long n);

pntrString *pntrPSpace(long n);

long pntrLen(pntrString *s);
long pntrAllocLen(pntrString *s);
void pntrZapLen(pntrString *s, long length);


long pntrInstr(long start, pntrString *sin, pntrString *s);



long pntrRevInstr(long start_position,pntrString *string1,
    pntrString *string2);


int pntrEq(pntrString *sout,pntrString *sin);


pntrString *pntrAddElement(pntrString *g);


pntrString *pntrAddGElement(pntrString *g);




long knapsack01(long items, long *size, long *worth, long maxSize,
       char *itemIncluded );


long **alloc2DMatrix(size_t xsize, size_t ysize);
void free2DMatrix(long **matrix, size_t xsize );

long getSourceIndentation(long statemNum);


vstring getDescription(long statemNum);

vstring getDescriptionAndLabel(long statemNum);



flag getMarkupFlag(long statemNum, char mode);


vstring getContrib(long stmtNum, char mode);




void getProofDate(long stmtNum, vstring *date1, vstring *date2);


flag parseDate(vstring dateStr, long *dd, long *mmm, long *yyyy);


void buildDate(long dd, long mmm, long yyyy, vstring *dateStr);

flag compareDates(vstring date1, vstring date2);




int qsortStringCmp(const void *p1, const void *p2);



void freeData(void);

}