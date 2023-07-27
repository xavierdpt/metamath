package xd.metamath;

public class hmmword {







#ifndef METAMATH_MMWORD_H_
#define METAMATH_MMWORD_H_



void revise(FILE *f1_fp, FILE *f2_fp, FILE *f3_fp, vstring addTag, long m);





long highestRevision(vstring fileName);




long getRevision(vstring line);


void gosub_7320(void);
void gosub_7330(void);

#endif
}