package xd.metamath;

public class D {
    public static final char lb_ = '{';
    public static final char rb_ = '}';
    public static final char v_ = 'v';
    public static final char c_ = 'c';
    public static final char a_ = 'a';
    public static final char d_ = 'd';
    public static final char e_ = 'e';
    public static final char f_ = 'f';
    public static final char p_ = 'p';
    public static final char eq_ = '=';
    public static final char sc_ = '.';
    public static final char illegal_ = '?';

    public static final String PROOF_DISCOURAGED_MARKUP = "(Proof modification is discouraged.)";
    public static final String USAGE_DISCOURAGED_MARKUP = "(New usage is discouraged.)";
    public static final String DEFAULT_CONTRIBUTOR = "?who?";

    public static final long PROOF_DISCOURAGED = 1;
    public static final long USAGE_DISCOURAGED = 2;
    public static final long RESET = 0;
    public static final long GC_RESET = 0;
    public static final long GC_RESET_STMT = 10;
    public static final long CONTRIBUTOR = 1;
    public static final long CONTRIB_DATE = 2;
    public static final long REVISER = 3;
    public static final long REVISE_DATE = 4;
    public static final long SHORTENER = 5;
    public static final long SHORTEN_DATE = 6;
    public static final long MOST_RECENT_DATE = 7;
    public static final long GC_ERROR_CHECK_SILENT = 8;
    public static final long GC_ERROR_CHECK_PRINT = 9;
    public static final long MAX_COMMAND_FILE_NESTING = 10;
    public static final long MAX_LEN = 79;
    public static final long SCREEN_HEIGHT = 23;

    public static final long PUS_INIT = 1;
    public static final long PUS_PUSH = 2;
    public static final long PUS_UNDO = 3;
    public static final long PUS_REDO = 4;
    public static final long PUS_NEW_SIZE = 5;
    public static final long PUS_GET_SIZE = 6;
    public static final long PUS_GET_STATUS = 7;


    public static final long SYNTAX = 4;
    public static final long ADD_MODE = 1;
    public static final long DELETE_MODE = 2;
    public static final long CLEAN_MODE = 3;
    public static final long SUBSTITUTE_MODE = 4;
    public static final long SWAP_MODE = 5;
    public static final long INSERT_MODE = 6;
    public static final long BREAK_MODE = 7;
    public static final long BUILD_MODE = 8;
    public static final long MATCH_MODE = 9;
    public static final long RIGHT_MODE = 10;
    public static final long TAG_MODE = 11;
    public static final long SORT_MODE = 1;
    public static final long UNDUPLICATE_MODE = 2;
    public static final long DUPLICATE_MODE = 3;
    public static final long UNIQUE_MODE = 4;
    public static final long REVERSE_MODE = 5;
    public static final long THEOREMS_PER_PAGE = 100;
    public static final long RECENT_COUNT = 100;
    public static final long START_YEAR = 93;
    public static final long COL = 20;
    public static final long MIN_SPACE = 2;
    public static final String MVERSION = "0.198 7-Aug-2021";

    public static final long SYNTAX2 =1;
    public static final long DEFINITION =2;
    public static final long AXIOM =3;
    public static final long THEOREM =4;
    public static final long PF_INDENT_INC =2;
    public static final long INDENT_INCR =3;
    public static final long MAX_LINE_LEN =79;
    public static final long TEMPO =48;
    public static final long MIN_NOTE =28;
    public static final long MAX_NOTE =103;

    public static final long ALLKEYSFLAG =1;
    public static final long WHITEKEYSFLAG =2;
    public static final long BLACKKEYSFLAG =3;

    public static final long ALLKEYS =128;
    public static final long WHITEKEYS =75;
    public static final long BLACKKEYS =53;

    public static final long LATEXDEF =1;
    public static final long HTMLDEF =2;
    public static final long HTMLVARCOLOR =3;
    public static final long HTMLTITLE =4;
    public static final long HTMLHOME =5;

    public static final long ALTHTMLDEF =6;
    public static final long EXTHTMLTITLE =7;
    public static final long EXTHTMLHOME =8;
    public static final long EXTHTMLLABEL =9;
    public static final long HTMLDIR =10;
    public static final long ALTHTMLDIR =11;

    public static final long HTMLBIBLIOGRAPHY =12;
    public static final long EXTHTMLBIBLIOGRAPHY =13;

    public static final long HTMLCSS =14;
    public static final long HTMLFONT =15;

    public static final long HTMLEXTURL =16;
    public static final long DOLLAR_SUBST =2;
    public static final long PARTITIONS =28;

    public static final long ERRORS_ONLY = 1;
    public static final long PROCESS_SYMBOLS = 2;
    public static final long PROCESS_LABELS = 4;
    public static final long ADD_COLORED_LABEL_NUMBER = 8;
    public static final long PROCESS_BIBREFS = 16;
    public static final long PROCESS_UNDERSCORES = 32;
    public static final long CONVERT_TO_HTML = 64;
    public static final long METAMATH_COMMENT = 128;
    public static final long COMPR_INC =1000;

    public static final long  DEFAULT_COLUMN =16;

    public static final long  ASCII_4= 4;

    public static final long  MEM_POOL_GROW = 1000;

    public static final long  M_MAX_ALLOC_STACK =100;
    public static final long M_MAX_CAT_ARGS =30;
    public static final long CMD_BUFFER_SIZE =2000;
    public static final long VERSIONS =9;
    public static final long SEEK_END =2;
    public static final long QUOTED_SPACE =3;
    public static final long kBaseResID =128;
    public static final long  kHorizontalPixel =30;
    public static final long  kVerticalPixel =50;

    public static final long INDENT_FIRST =2;
    public static final long INDENT_INCR2 =2;
    public static final long  MATCH_LIMIT =100;
    public static final long MAX_DEPTH =40;
    public static final long MAX_GROWTH_FACTOR =2;
    public static final long DEFAULT_UNDO_STACK_SIZE =20;
    public static final long MAX_ALLOC_STACK =100;
    public static final long MAX_CAT_ARGS =50;
    public static final long LINE_LENGTH =80;
    public static final long MAX_LINES = 10000;
    public static final long MAX_BUF = 1000;

    public static final long MAX_DESCR_LEN =87;
    public static final long INDENTATION_OFFSET =1;
    public static final long TRIMTHRESHOLD =60;

    public static final String MB_TAG ="Mathbox for ";

    public static final String  MB_LABEL= "mathbox";

    public static final String OPENING_PUNCTUATION = "(['\"";
    public static final String CLOSING_PUNCTUATION = ".,;)?!:]'\"_-";

    public static final String OPENING_PUNCTUATION2 ="(['\"";

    public static final String CLOSING_PUNCTUATION2 =".,;)?!:]'\"";
    public static final String SENTENCE_END_PUNCTUATION= ")'\"";
    public static final String MONTHS = "JanFebMarAprMayJunJulAugSepOctNovDec";

    public static final String GREEN_TITLE_COLOR= "\"#006633\"";
    public static final String MINT_BACKGROUND_COLOR= "\"#EEFFFA\"";
    public static final String PINK_NUMBER_COLOR= "\"#FA8072\"";
    public static final String PURPLISH_BIBLIO_COLOR= "\"#FAEEFF\"";



    public static final String SANDBOX_COLOR= "\"#FFFFD9\"";

    public static final long PROCESS_EVERYTHING = PROCESS_SYMBOLS + PROCESS_LABELS
            + ADD_COLORED_LABEL_NUMBER + PROCESS_BIBREFS
            + PROCESS_UNDERSCORES + CONVERT_TO_HTML + METAMATH_COMMENT;

    public static final String  HUGE_DECORATION ="####";
    public static final String  BIG_DECORATION= "#*#*";
    public static final String  SMALL_DECORATION ="=-=-";
    public static final String  TINY_DECORATION= "-.-.";

    public static final String   PINK_NBSP= "&nbsp;";

    public static final String CONTRIB_MATCH = " (Contributed by ";
    public static final String REVISE_MATCH = " (Revised by ";
    public static final String SHORTEN_MATCH = " (Proof shortened by ";
    public static final String END_MATCH = ".) ";

    public static void main(String[] args) {

    }
}
