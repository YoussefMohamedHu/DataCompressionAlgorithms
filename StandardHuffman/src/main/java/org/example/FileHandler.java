package org.example;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    public static String readTxt(String input) throws IOException {
        String ret= Files.readString(Paths.get(input));
        return ret;
    }
    public static List<Byte> readBin(String input)throws IOException {
         byte[] temp= Files.readAllBytes(Paths.get(input));
         List<Byte>ret = new ArrayList<>();
         for(var i:temp){
             ret.add(i);
         }
        return ret;
    }
    public static void writeBin(List<Byte>input,String dir) throws IOException {
        FileOutputStream fos=new FileOutputStream(dir);
        for(byte i:input){
            fos.write(i);
        }

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
        Files.writeString(Paths.get("outputDecompression.txt"),ans);
    }
}
