package xd.metamath;

public class hmminou {















static final long MAX_COMMAND_FILE_NESTING = D.MAX_COMMAND_FILE_NESTING;







flag print2(char* fmt,...);

     static final long MAX_LEN = D.MAX_LEN;
     static final long SCREEN_HEIGHT = D.SCREEN_HEIGHT;


void printLongLine(vstring line, vstring startNextLine, vstring breakMatch);
vstring cmdInput(File stream,vstring ask);
vstring cmdInput1(vstring ask);

enum severity {notice_,warning_,error_,fatal_};
void errorMessage(vstring line, long lineNum, long column, long tokenLength,
  vstring error, vstring fileName, long statementNum, flag warnFlag);

File fSafeOpen(vstring fileName, vstring mode, flag noVersioningFlag);

int fSafeRename(vstring oldFileName, vstring newFileName);

vstring fGetTmpName(vstring filePrefix);


vstring readFileToString(vstring fileName, char verbose, long *charCount);


double getRunTime(double *timeSinceLastCall);


void freeInOu(void);

}