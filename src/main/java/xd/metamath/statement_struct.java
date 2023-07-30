package xd.metamath;

public class statement_struct {
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
    P<nmbrString> mathString;
    long mathStringLen;
    P<nmbrString> proofString;
    P<nmbrString> reqHypList;
    P<nmbrString> optHypList;
    long numReqHyp;
    P<nmbrString> reqVarList;
    P<nmbrString> optVarList;
    P<nmbrString> reqDisjVarsA;
    P<nmbrString> reqDisjVarsB;
    P<nmbrString> reqDisjVarsStmt;
    P<nmbrString> optDisjVarsA;
    P<nmbrString> optDisjVarsB;
    P<nmbrString> optDisjVarsStmt;
    long pinkNumber;
    long headerStartStmt;
}
