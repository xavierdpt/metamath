package xd.metamath;

public class hmminou {










extern int g_errorCount;


extern flag g_logFileOpenFlag;
extern FILE *g_logFilePtr;
extern FILE *g_listFile_fp;

extern flag g_outputToString;
extern vstring g_printString;

#define MAX_COMMAND_FILE_NESTING 10
extern long g_commandFileNestingLevel;
extern FILE *g_commandFilePtr[MAX_COMMAND_FILE_NESTING + 1];
extern vstring g_commandFileName[MAX_COMMAND_FILE_NESTING + 1];
extern flag g_commandFileSilent[MAX_COMMAND_FILE_NESTING + 1];
extern flag g_commandFileSilentFlag;


extern FILE  *g_input_fp ;

extern vstring  g_input_fn, g_output_fn;




flag print2(char* fmt,...);
extern long g_screenHeight;
extern long g_screenWidth;
#define MAX_LEN 79
#define SCREEN_HEIGHT 23
extern flag g_scrollMode;
extern flag g_quitPrint;


void printLongLine(vstring line, vstring startNextLine, vstring breakMatch);
vstring cmdInput(FILE *stream,vstring ask);
vstring cmdInput1(vstring ask);

enum severity {notice_,warning_,error_,fatal_};
void errorMessage(vstring line, long lineNum, long column, long tokenLength,
  vstring error, vstring fileName, long statementNum, flag warnFlag);

FILE *fSafeOpen(vstring fileName, vstring mode, flag noVersioningFlag);

int fSafeRename(vstring oldFileName, vstring newFileName);

vstring fGetTmpName(vstring filePrefix);


vstring readFileToString(vstring fileName, char verbose, long *charCount);


double getRunTime(double *timeSinceLastCall);


void freeInOu(void);

}