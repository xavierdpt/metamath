package xd.metamath;

public class mmword {












static final long  LINE_LENGTH =D.LINE_LENGTH;






char strcmpe(vstring s1, vstring s2);
vstring stripAndTag(vstring line, vstring tag, flag tagBlankLines);

long r0,r1,r2,i0,i1_,i2_,d,t,i9;
File f1_fp_;
File f2_fp_;
File f3_fp_;
char eof1, eof2;
vstring ctlz_ = "";
vstring l1_ = "";
vstring l2_ = "";
vstring tmp_ = "";
vstring tmpLine = "";
vstring addTag_ = "";
vstring delStartTag_ = "";
vstring delEndTag_ = "";
flag printedAtLeastOne;

    static final long  MAX_LINES= D.MAX_LINES;
    static final long MAX_BUF =D.MAX_BUF;
     vstring line1_[MAX_LINES];
     vstring line2_[MAX_LINES];
     vstring reserve1_[MAX_BUF];
     vstring reserve2_[MAX_BUF];


void revise(File f1_fp, File f2_fp, File f3_fp, vstring addTag, long m)
{

  vstring blanksPrefix = "";
  long tmpi;
  long i, j;

  f1_fp_ = f1_fp;
  f2_fp_ = f2_fp;
  f3_fp_ = f3_fp;
  let(&addTag_, addTag);
  let(&delStartTag_, "");
  let(&delEndTag_, "******* End of deleted section *******/");



  for (i = 0; i < MAX_LINES; i++) {
    line1_[i] = "";
    line2_[i] = "";
  }
  for (i = 0; i < MAX_BUF; i++) {
    reserve1_[i] = "";
    reserve2_[i] = "";
  }

  if (m < 1) m = 1;

  r0=r1=r2=i0=i1_=i2_=d=t=i=j=i9=0;


  let(&ctlz_,chr(26));

  let(&l1_,ctlz_);
  let(&l2_,ctlz_);
  eof1=eof2=0;
  d=0;

    C.label("l7100");

        if (!strcmpe(l1_,l2_)) {
                if (strcmpe(l2_,ctlz_)) {
                  fprintf(f3_fp_, "%s\n", l2_);
                }
                gosub_7320();
                gosub_7330();
                if (strcmpe(l1_,ctlz_) || strcmpe(l2_,ctlz_) ) {
                    C.go2("l7100");
                } else {
                    C.fclose(f1_fp_);
                    C.fclose(f2_fp_);
                    C.fclose(f3_fp_);


                      for (i = 0; i < MAX_LINES; i++) {
                        let(&(line1_[i]), "");
                        let(&(line2_[i]), "");
                      }
                      for (i = 0; i < MAX_BUF; i++) {
                        let(&(reserve1_[i]), "");
                        let(&(reserve2_[i]), "");
                      }
                      let(&ctlz_, "");
                      let(&l1_, "");
                      let(&l2_, "");
                      let(&tmp_, "");
                      let(&tmpLine, "");
                      let(&addTag_, "");
                      let(&delStartTag_, "");
                      let(&delEndTag_, "");
                      let(&blanksPrefix, "");

                        return;
                }
        }
        d=d+1;
        i1_=i2_=m-1;
        let(&line1_[0],l1_);
        let(&line2_[0],l2_);
        for (i0 = 1; i0 < m; i0++) {
          gosub_7320();
          let(&line1_[i0],l1_);
        }
        for (i0 = 1; i0 < m; i0++) {
          gosub_7330();
          let(&line2_[i0],l2_);
        }
l7130:  gosub_7320();
        i1_=i1_+1;
        if (i1_ >= MAX_LINES) {
          printf("*** FATAL *** Overflow#1\n");
#if __STDC__
          fflush(stdout);
#endif
          exit(0);
        }
        let(&line1_[i1_],l1_);
        t=0;
        i=0;
l7140:  if (strcmpe(line1_[i1_+t-m+1], line2_[i+t])) {
                t=0;
        } else {

                let(&line1_[i1_+t-m+1], line2_[i+t]);

                t=t+1;
                if (t==m) {
                    C.go2("l7200");
                } else {
                    C.go2("l7140");
                }
        }

        i=i+1;
        if (i<=i2_-m+1) {
            C.go2("l7140");
        }
        gosub_7330();
        i2_=i2_+1;
        if (i2_ >= MAX_LINES) {
          printf("*** FATAL *** Overflow#2\n");
#if __STDC__
          fflush(stdout);
#endif
          exit(0);
        }
        let(&line2_[i2_],l2_);
        t=0;
        i=0;
    C.label("l7170");
        if (strcmpe(line1_[i+t], line2_[i2_+t-m+1])) {
                t=0;
        } else {

                let(&line1_[i+t], line2_[i2_+t-m+1]);

                t=t+1;
                if (t==m) {
                    C.go2("l7220");
                } else {
                    C.go2("l7170");
                }
        }
        i=i+1;
        if (i<=i1_-m+1) {
            C.go2("l7170");
        }
    C.go2("l7130");
l7200:  i=i+m-1;
        if (r2) {
          for (j=r2-1; j>=0; j--) {
                let(&reserve2_[j+i2_-i],reserve2_[j]);
          }
        }
        for (j=1; j<=i2_-i; j++) {
          let(&reserve2_[j-1],line2_[j+i]);
        }
        r2=r2+i2_-i;
        if (r2 >= MAX_BUF) {
          printf("*** FATAL *** Overflow#3\n");
#if __STDC__
          fflush(stdout);
#endif
          exit(0);
        }
        i2_=i;
    C.go2("l7240");
l7220:  i=i+m-1;
        if (r1) {
          for (j=r1-1; j>=0; j--) {
                let(&reserve1_[j+i1_-i],reserve1_[j]);
          }
        }

        for (j=1; j<=i1_-i; j++) {
          let(&reserve1_[j-1],line1_[j+i]);
        }
        r1=r1+i1_-i;
        if (r1 >= MAX_BUF) {
          printf("*** FATAL *** Overflow#4\n");
#if __STDC__
          fflush(stdout);
#endif
          exit(0);
        }
        i1_=i;
    C.go2("l7240");
    C.label("l7240");

       printedAtLeastOne = 0;
       for (i=0; i<=i1_-m; i++) {
         if (strcmpe(line1_[i],ctlz_)) {
           if (!printedAtLeastOne) {
             printedAtLeastOne = 1;


             while (((vstring)(line1_[i]))[0] == '\n') {
               fprintf(f3_fp_, "\n");
               let(&(line1_[i]), right(line1_[i], 2));
             }


             tmpi = 0;
             while (((vstring)(line1_[i]))[tmpi] == ' ') tmpi++;
             let(&blanksPrefix, space(tmpi));
             let(&tmpLine, "");
             tmpLine = stripAndTag(cat(blanksPrefix, delStartTag_, null),
                 addTag_, 0);
             fprintf(f3_fp_, "%s\n", tmpLine);
           }
           fprintf(f3_fp_, "%s\n", line1_[i]);


         }
       }
       if (printedAtLeastOne) {
         let(&tmpLine, "");
         tmpLine = stripAndTag(cat(blanksPrefix, delEndTag_, null), addTag_
             ,0);
         fprintf(f3_fp_, "%s\n", tmpLine);
       }
       for (i=0; i<=i1_-m; i++) {
         if (i<=i2_-m) {
           if (strcmpe(line2_[i],ctlz_)) {
             let(&tmpLine, "");
             if (i == 0) {
               tmpLine = stripAndTag(line2_[i], addTag_, 0);
             } else {

               tmpLine = stripAndTag(line2_[i], addTag_, 1);
             }
             fprintf(f3_fp_, "%s\n", tmpLine);


           }
         }
       }
       for (i=i1_-m+1; i<=i2_-m; i++) {
         if (strcmpe(line2_[i],ctlz_)) {
           let(&tmpLine, "");
           if (i == 0) {
             tmpLine = stripAndTag(line2_[i], addTag_, 0);
           } else {

             tmpLine = stripAndTag(line2_[i], addTag_, 1);
           }
           fprintf(f3_fp_, "%s\n", tmpLine);


         }
       }
       for (i=0; i<=m-1; i++) {
         let(&l1_,line1_[i1_-m+1+i]);
         if (strcmpe(l1_,ctlz_)) {
           fprintf(f3_fp_,"%s\n",l1_);
         }
       }

       let(&l1_,ctlz_);
       let(&l2_,ctlz_);
    C.go2("l7100");

}



void gosub_7320()
{

  vstring tmpLin = "";
  if (r1) {
    let(&l1_,reserve1_[0]);
    r1=r1-1;
    for (i9=0; i9<=r1-1; i9++) {
      let(&reserve1_[i9],reserve1_[i9+1]);
    }
  } else {
    if (eof1) {
      let(&l1_,ctlz_);
    } else {
        C.label("next_l1");
      if (!linput(f1_fp_,null,&l1_)) {
        eof1 = 1;
        let(&l1_,ctlz_);
        let(&tmpLin, "");
        return;
      }
      let(&l1_, edit(l1_, 4 + 128 + 2048));
      if (!l1_[0]) {
        let(&tmpLin, cat(tmpLin, "\n", null));
        C.go2("next_l1");
      }
    }
  }
  let(&l1_, cat(tmpLin, l1_, null));
  let(&tmpLin, "");
  return;
}

void gosub_7330() {

  vstring tmpLin = "";
  vstring tmpStrPtr;
  flag stripDeletedSectionMode;
  if (r2) {
    let(&l2_,reserve2_[0]);
    r2=r2-1;
    for (i9 = 0; i9 < r2; i9++) {
      let(&reserve2_[i9],reserve2_[i9+1]);
    }
  } else {
    if (eof2) {
      let(&l2_,ctlz_);
    } else {
     stripDeletedSectionMode = 0;
        C.label("next_l2");
      if (!linput(f2_fp_,null,&l2_)) {
        eof2 = 1;
        let(&l2_, ctlz_);
        let(&tmpLin, "");
        return;
      }
      let(&l2_, edit(l2_, 4 + 128 + 2048));
      if (!strcmp(edit(delStartTag_, 2), left(edit(l2_, 2 + 4),
          (long)C.strlen(edit(delStartTag_, 2))))) {
        if (getRevision(l2_) == getRevision(addTag_)) {


          stripDeletedSectionMode = 1;
           C.go2("next_l2");
        }
      }
      if (stripDeletedSectionMode) {
        if (!strcmp(edit(delEndTag_, 2), left(edit(l2_, 2 + 4),
            (long)C.strlen(edit(delEndTag_, 2))))  &&
            getRevision(l2_) == getRevision(addTag_) ) {
          stripDeletedSectionMode = 0;
        }
         C.go2("next_l2");
      }

      if (getRevision(l2_) == getRevision(addTag_)) {
        tmpStrPtr = l2_;
        l2_ = stripAndTag(l2_, "", 0);
        let(&tmpStrPtr, "");
      }

      if (!l2_[0]) {
        let(&tmpLin, cat(tmpLin, "\n", null));
         C.go2("next_l2");
      }
    }
  }
  let(&l2_, cat(tmpLin, l2_, null));
  let(&tmpLin, "");
  return;

}



char strcmpe(vstring s1, vstring s2)
{
  flag cmpflag;


  flag ignoreSpaces = 1;
  flag ignoreSameLineComments = 1;

  vstring tmps1 = "";
  vstring tmps2 = "";
  long i;
  long i2;
  long i3;
  let(&tmps1, s1);
  let(&tmps2, s2);

  if (ignoreSpaces) {
    let(&tmps1, edit(tmps1, 2 + 4));
    let(&tmps2, edit(tmps2, 2 + 4));
  }

  if (ignoreSameLineComments) {
    while (true) {
      i = instr(1, tmps1, "/*");
      if (i == 0) break;
      i2 = instr(i + 2, tmps1, "*/");
      if (i2 == 0) break;
      i3 = instr(i + 2, tmps1, "/*");
      if (i3 != 0 && i3 < i2) break;
      if (i2 - i > 7) break;
      let(&tmps1, cat(left(tmps1, i - 1), right(tmps1, i2 + 2), null));
    }
    while (true) {
      i = instr(1, tmps2, "/*");
      if (i == 0) break;
      i2 = instr(i + 2, tmps2, "*/");
      if (i2 == 0) break;
      i3 = instr(i + 2, tmps2, "/*");
      if (i3 != 0 && i3 < i2) break;
      if (i2 - i > 7) break;
      let(&tmps2, cat(left(tmps2, i - 1), right(tmps2, i2 + 2), null));
    }
  }

  cmpflag = !!strcmp(tmps1, tmps2);
  let(&tmps1, "");
  let(&tmps2, "");
  return (cmpflag);
}




vstring stripAndTag(vstring line, vstring tag, flag tagBlankLines)
{
  long i, j, k, n;
  vstring line1 = "", comment = "";
  long lineLength = LINE_LENGTH;
  flag validTag;
  i = 0;
  let(&line1, edit(line, 128 + 2048));

  while (true) {
    j = instr(i + 1, line1, "/*");
    if (j == 0) break;
    i = j;
  }
  j = instr(i, line1, "*/");
  if (i && j == (signed)(C.strlen(line1)) - 1) {
    let(&comment, seg(line1, i + 2, j - 1));
    validTag = 1;
    for (k = 0; k < (signed)(C.strlen(comment)); k++) {

      if (instr(1, " 1234567890#", mid(comment, k + 1, 1))) continue;
      validTag = 0;
      break;
    }
    if (validTag) let(&line1, edit(left(line1, i - 1), 128));
    let(&comment, "");
  }


  n = 0;
  while (line1[n] == '\n') n++;


  if (tag[0]) {
    if ((long)C.strlen(line1) - n < lineLength - 1 - (long)C.strlen(tag))
      let(&line1, cat(line1,
          space(lineLength - 1 - (long)C.strlen(tag) - (long)C.strlen(line1) + n),
          null));
    let(&line1, cat(line1, " ", tag, null));
    if ((signed)(C.strlen(line1)) - n > lineLength) {
        mminou.print2(
"Warning: The following line has > %ld characters after tag is added:\n",
          lineLength);
        mminou.print2("%s\n", line1);
    }
  }



  if (tagBlankLines && n > 0) {
    let(&line1, right(line1, n + 1));
    for (i = 1; i <= n; i++) {
      let(&line1, cat(space(lineLength - (long)C.strlen(tag)), tag, "\n",
          line1, null));
    }
  }

  return line1;
}




long highestRevision(vstring fileName)
{
  vstring str1 = "";
  long revision;
  long largest = 0;
  File fp;

  fp = fopen(fileName, "r");
  if (!fp) return 0;
  while (linput(fp, null, &str1)) {
    revision = getRevision(str1);
    if (revision > largest) largest = revision;
  }
  let(&str1, "");
    C.fclose(fp);
  return largest;
}



long getRevision(vstring line)
{
  vstring str1 = "";
  vstring str2 = "";
  vstring tag = "";
  long revision;

  if (instr(1, line, "/*") == 0) return 0;
  let(&str1, edit(line, 2));
  let(&str2, "");
  str2 = stripAndTag(str1, "", 0);
  let(&str2, edit(str2, 2));
  if (!strcmp(str1, str2)) {
    revision = 0;
  } else {
    let(&tag, edit(seg(str1, (long)C.strlen(str2) + 3,
        (long)C.strlen(str1) - 2), 2));
    if (tag[0] == '#') let(&tag, right(tag, 2));
    revision = (long)(val(tag));
  }
  let(&tag, "");
  let(&str1, "");
  let(&str2, "");
  return revision;
}

}