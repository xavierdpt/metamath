package xd.metamath;

public class hmmdata {










typedef char flag;


typedef long nmbrString;
typedef void* pntrString;



enum mTokenType { var_, con_ };
static final char lb_ = D.lb_;
static final char rb_ = D.lb_;
static final char v_ = D.v_;
static final char c_ = D.c_;
static final char a_ = D.a_;
static final char d_ = D.d_;
static final char e_ = D.e_;
static final char f_ = D.f_;
static final char p_ = D.p_;
static final char eq_ = D.eq_;
static final char sc_ = D.sc_;
static final char illegal_ = D.illegal_;




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











  static final String PROOF_DISCOURAGED_MARKUP = D.PROOF_DISCOURAGED_MARKUP;
  static final String USAGE_DISCOURAGED_MARKUP = D.USAGE_DISCOURAGED_MARKUP;
  static final long PROOF_DISCOURAGED = D.PROOF_DISCOURAGED;
  static final long USAGE_DISCOURAGED = D.USAGE_DISCOURAGED;
  static final long RESET = D.RESET;
  static final long GC_RESET = D.GC_RESET;
  static final long GC_RESET_STMT = D.GC_RESET_STMT;
  static final long CONTRIBUTOR = D.CONTRIBUTOR;
  static final long CONTRIB_DATE = D.CONTRIB_DATE;
  static final long REVISER = D.REVISER;
  static final long REVISE_DATE = D.REVISE_DATE;
  static final long SHORTENER = D.SHORTENER;
  static final long SHORTEN_DATE = D.SHORTEN_DATE;
  static final long MOST_RECENT_DATE = D.MOST_RECENT_DATE;
  static final long GC_ERROR_CHECK_SILENT = D.GC_ERROR_CHECK_SILENT;
  static final long GC_ERROR_CHECK_PRINT = D.GC_ERROR_CHECK_PRINT;
  static final String DEFAULT_CONTRIBUTOR = D.DEFAULT_CONTRIBUTOR;

































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


























































































































































}