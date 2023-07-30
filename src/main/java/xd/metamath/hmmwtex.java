package xd.metamath;

public class hmmwtex {










    static final String GREEN_TITLE_COLOR= D.GREEN_TITLE_COLOR;
    static final String MINT_BACKGROUND_COLOR= D.MINT_BACKGROUND_COLOR;
    static final String PINK_NUMBER_COLOR= D.PINK_NUMBER_COLOR;
    static final String PURPLISH_BIBLIO_COLOR= D.PURPLISH_BIBLIO_COLOR;
    static final String SANDBOX_COLOR= D.SANDBOX_COLOR;









void eraseTexDefs(void);


flag readTexDefs(
  flag errorsOnly,
  flag noGifCheck   );

struct texDef_struct {
  vstring tokenName;
  vstring texEquiv;
};


long texDefWhiteSpaceLen(char *ptr);
long texDefTokenLen(char *ptr);

int texSortCmp(const void *key1, const void *key2);

int texSrchCmp(const void *key, const void *data);


vstring asciiToTt(vstring s);
vstring tokenToTex(vstring mtoken, long statemNum);
vstring asciiMathToTex(vstring mathComment, long statemNum);
vstring getCommentModeSection(vstring *srcptr, char *mode);
void printTexHeader(flag texHeaderFlag);



flag printTexComment(vstring commentPtr,
    flag htmlCenterFlag,
    long actionBits,
    flag noFileCheck );

    static final long ERRORS_ONLY =D.ERRORS_ONLY;
    static final long PROCESS_SYMBOLS  =D.PROCESS_SYMBOLS;
    static final long PROCESS_LABELS  =D.PROCESS_LABELS;
    static final long ADD_COLORED_LABEL_NUMBER  =D.ADD_COLORED_LABEL_NUMBER;
    static final long PROCESS_BIBREFS  =D.PROCESS_BIBREFS;
    static final long PROCESS_UNDERSCORES  =D.PROCESS_UNDERSCORES;

    static final long CONVERT_TO_HTML  =D.CONVERT_TO_HTML;

    static final long METAMATH_COMMENT  =D.METAMATH_COMMENT;

    static final long PROCESS_EVERYTHING =D.PROCESS_EVERYTHING;

void printTexLongMath(nmbrString *proofStep, vstring startPrefix,
    vstring contPrefix, long hypStmt, long indentationLevel);
void printTexTrailer(flag texHeaderFlag);

void writeTheoremList(long theoremsPerPage, flag showLemmas,
    flag noVersioning);

    static final String  HUGE_DECORATION= D.HUGE_DECORATION;
    static final String  BIG_DECORATION= D.BIG_DECORATION;
    static final String  SMALL_DECORATION= D.SMALL_DECORATION;
    static final String  TINY_DECORATION =D.TINY_DECORATION;







flag getSectionHeadings(long stmt, vstring *hugeHdrTitle,
    vstring *bigHdrTitle,
    vstring *smallHdrTitle,
    vstring *tinyHdrTitle,

    vstring *hugeHdrComment,
    vstring *bigHdrComment,
    vstring *smallHdrComment,
    vstring *tinyHdrComment,

    flag fineResolution,
    flag fullComment
    );









vstring pinkHTML(long statemNum);


vstring pinkRangeHTML(long statemNum1, long statemNum2);

    static final String PINK_NBSP = D.PINK_NBSP;
#define RAINBOW_OPTION

#ifdef RAINBOW_OPTION

vstring spectrumToRGB(long color, long maxColor);
#endif

#define INDENT_HTML_PROOFS


vstring getTexLongMath(nmbrString *mathString, long statemNum);




vstring getTexOrHtmlHypAndAssertion(long statemNum);




flag writeBibliography(vstring bibFile,
    vstring labelMatch,
    flag errorsOnly,
    flag noFileCheck);





flag inDiffMathboxes(long stmt1, long stmt2);
vstring getMathboxUser(long stmt);
long getMathboxNum(long stmt);

void assignMathboxInfo(void);

long getMathboxLoc(nmbrString **mathboxStart, nmbrString **mathboxEnd,
    pntrString **mathboxUser);

}