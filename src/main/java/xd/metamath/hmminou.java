package xd.metamath;

public class hmminou {














#define MAX_COMMAND_FILE_NESTING 10







flag print2(char* fmt,...);
#define MAX_LEN 79
#define SCREEN_HEIGHT 23


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