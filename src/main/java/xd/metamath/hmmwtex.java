package xd.metamath;

public class hmmwtex {










#define GREEN_TITLE_COLOR "\"#006633\""
#define MINT_BACKGROUND_COLOR "\"#EEFFFA\""
#define PINK_NUMBER_COLOR "\"#FA8072\""
#define PURPLISH_BIBLIO_COLOR "\"#FAEEFF\""



#define SANDBOX_COLOR "\"#FFFFD9\""



extern flag g_oldTexFlag;


extern flag g_htmlFlag;
extern flag g_altHtmlFlag;
extern flag g_briefHtmlFlag;
extern long g_extHtmlStmt;
extern vstring g_extHtmlTitle;
extern vstring g_htmlVarColor;

extern vstring g_htmlHome;

extern vstring g_htmlBibliography;
extern vstring g_extHtmlBibliography;
extern vstring g_htmlCSS;

extern vstring g_htmlFont;

void eraseTexDefs(void);


flag readTexDefs(
  flag errorsOnly,
  flag noGifCheck   );

extern flag g_texDefsRead;
struct texDef_struct {
  vstring tokenName;
  vstring texEquiv;
};
extern struct texDef_struct *g_TexDefs;


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

#define ERRORS_ONLY 1
#define PROCESS_SYMBOLS 2
#define PROCESS_LABELS 4
#define ADD_COLORED_LABEL_NUMBER 8
#define PROCESS_BIBREFS 16
#define PROCESS_UNDERSCORES 32

#define CONVERT_TO_HTML 64

#define METAMATH_COMMENT 128

#define PROCESS_EVERYTHING PROCESS_SYMBOLS + PROCESS_LABELS \
     + ADD_COLORED_LABEL_NUMBER + PROCESS_BIBREFS \
     + PROCESS_UNDERSCORES + CONVERT_TO_HTML + METAMATH_COMMENT

void printTexLongMath(nmbrString *proofStep, vstring startPrefix,
    vstring contPrefix, long hypStmt, long indentationLevel);
void printTexTrailer(flag texHeaderFlag);

void writeTheoremList(long theoremsPerPage, flag showLemmas,
    flag noVersioning);

#define HUGE_DECORATION "####"
#define BIG_DECORATION "#*#*"
#define SMALL_DECORATION "=-=-"
#define TINY_DECORATION "-.-."







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



extern flag g_texFileOpenFlag;
extern FILE *g_texFilePtr;






vstring pinkHTML(long statemNum);


vstring pinkRangeHTML(long statemNum1, long statemNum2);

#define PINK_NBSP "&nbsp;"
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


extern long g_mathboxStmt;
extern long g_mathboxes;
extern nmbrString *g_mathboxStart;
extern nmbrString *g_mathboxEnd;
extern pntrString *g_mathboxUser;



flag inDiffMathboxes(long stmt1, long stmt2);
vstring getMathboxUser(long stmt);
long getMathboxNum(long stmt);

void assignMathboxInfo(void);

long getMathboxLoc(nmbrString **mathboxStart, nmbrString **mathboxEnd,
    pntrString **mathboxUser);

}