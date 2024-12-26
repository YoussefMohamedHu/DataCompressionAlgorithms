package org.example;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    public static String readTxt(String input) throws IOException {
        input="toCompressInput.txt";
        String ret= Files.readString(Paths.get(input));
        return ret;
    }
    public static decompressedData readBin()throws IOException {
        decompressedData ret=new decompressedData();
        RandomAccessFile wr=new RandomAccessFile("Decompressed.bin","r");
        ret.floating =wr.readDouble();
        for(int i=0;i<256;i++){
            ret.arr[i]=wr.readDouble();
        }
        ret.len=wr.readInt();
        return ret;
    }
    public static void writeBin(double []arr,double res,int len) throws IOException {
        RandomAccessFile wr=new RandomAccessFile("Decompressed.bin","rw");
        wr.writeDouble(res);
        for(int i=0;i<256;i++){
            wr.writeDouble(arr[i]);
        }
        wr.writeInt(len);

    }

    public static void fillCharsPercentage(double[] percentage) throws IOException {
        String temp="";
        for(int i=0;i<256;i++){
            if(percentage[i]!=0){
                temp+=String.valueOf(String.valueOf((char)i)+" "+String.valueOf(percentage[i]))+'\n';

            }
        }
        Files.writeString(Paths.get("CharsPercentage.txt"),temp);
    }

    public static void writeTxt(String ans) throws IOException {
        Files.writeString(Paths.get("toDecompressOutput.txt"),ans);
    }
}
