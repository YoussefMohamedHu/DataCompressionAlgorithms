package org.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Data implements Serializable {
    public ArrayList<Byte> MatrixByte;
    public HashMap<Integer,double [][]>idxpixiel;
    public int blockArraywidth;
    public int blockArrayHeight;
    public int codeBlockwidth;
    public int codeBlockHeight;
    public int codeBookSize;
    Data(
            HashMap<Integer,double [][]>idxpixiel,
            ArrayList<Byte> MatrixOfByte,
            int blockArraywidth,
            int blockArrayHeight,
            int codeBlockwidth,
            int codeBlockHeight,
            int codeBookSize
    ){
        this.idxpixiel=idxpixiel;
        MatrixByte=new ArrayList<>();MatrixByte.add((byte) 0);
        this.blockArraywidth=blockArraywidth;
        this.blockArrayHeight=blockArrayHeight;
        this.codeBlockwidth=codeBlockwidth;
        this.codeBlockHeight=codeBlockHeight;
        this.codeBookSize=codeBookSize;
        int itr=0;
        int idx=0;
        int mask=(int)(Math.log(codeBookSize)/Math.log(2));
        for(int i=0;i<MatrixOfByte.size();i++){
            for(int j=0;j<mask;j++){
                MatrixByte.set(idx,(byte)(MatrixByte.get(idx)|(((1<<j)&(MatrixOfByte.get(i)))!=0?(1<<itr):0)));
                itr++;
                if(itr==8){
                    itr=0;
                    idx++;
                    MatrixByte.add((byte)0);
                }

            }
        }
    }
}
