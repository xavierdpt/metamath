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

#define DEFAULT_COLUMN 16
extern pntrString *g_rawArgPntr;
extern nmbrString *g_rawArgNmbr;
extern long g_rawArgs;
extern pntrString *g_fullArg;
extern vstring g_fullArgString;
extern vstring g_commandPrompt;
extern vstring g_commandLine;
extern long g_showStatement;
extern vstring g_logFileName;
extern vstring g_texFileName;
extern flag g_PFASmode;

extern flag g_sourceChanged;
extern flag g_proofChanged;
extern flag g_commandEcho;
extern flag g_memoryStatus;


extern flag g_sourceHasBeenRead;

extern vstring g_rootDirectory;


}