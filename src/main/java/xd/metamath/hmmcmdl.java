package xd.metamath;

public class hmmcmdl {









flag processCommandLine(void);
flag getFullArg(long arg, vstring cmdList);
void parseCommandLine(vstring line);
flag lastArgMatches(vstring argString);
flag cmdMatches(vstring cmdString);
long switchPos(vstring swString);
void printCommandError(vstring line, long arg, vstring errorMsg);
void freeCommandLine(void);

    static final long DEFAULT_COLUMN= D.DEFAULT_COLUMN;






}