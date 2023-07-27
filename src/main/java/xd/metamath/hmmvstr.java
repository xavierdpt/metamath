package xd.metamath;

public class hmmvstr {











typedef char* vstring;
#define vstringdef(x) vstring x = ""




void let(vstring *target, vstring source);




vstring cat(vstring string1,...);




int linput(File stream,const char* ask,vstring *target);



vstring seg(vstring sin, long p1, long p2);
vstring mid(vstring sin, long p, long l);
vstring left(vstring sin, long n);
vstring right(vstring sin, long n);
vstring edit(vstring sin, long control);
vstring space(long n);
vstring string(long n, char c);
vstring chr(long n);
vstring xlate(vstring sin, vstring control);
vstring date(void);
vstring time_(void);
vstring num(double x);
vstring num1(double x);
vstring str(double x);
long len(vstring s);
long instr(long start, vstring sin, vstring s);
long rinstr(vstring string1, vstring string2);
long ascii_(vstring c);
double val(vstring s);


vstring entry(long element, vstring list);
long lookup(vstring expression, vstring list);
long numEntries(vstring list);
long entryPosition(long element, vstring list);





void makeTempAlloc(vstring s);
}