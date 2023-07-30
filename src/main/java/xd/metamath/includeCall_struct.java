package xd.metamath;

public class includeCall_struct {
    vstring source_fn;
    vstring included_fn;
    long current_offset;
    long current_line;
    flag pushOrPop;
    vstring current_includeSource;
    long current_includeLength;
}
