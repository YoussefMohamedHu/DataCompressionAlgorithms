package org.example;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    static double []arr=new double[257];
    public static double compress(String in,double low,double high,int itr){
        if(itr==in.length()){
            return (low+high)/2;
        }
        double l=0,r=0;
        int c=(int)in.charAt(itr);
        l=(c>0?arr[c-1]:0);
        r=arr[c];
         double temp=low+(high-low)*l;
         high=low+(high-low)*r;
         low=temp;
         return compress(in,low,high,itr+1);
    }
    public static String decompressHelper(double floating,double low,double high,int itr,int len,String res){
        if(len==itr)return res;
        double l=0,r=0;
        for(int i=0;i<256;i++){
            r=arr[i];
            if((floating-low)/(high-low)<r)
                return decompressHelper(floating,low+(l*(high-low)),low+(r*(high-low)),itr+1,len,res+(char)i);
            l=r;
        }
        return "";
    }
    public static String decompress() throws IOException {
        decompressedData data=FileHandler.readBin();
        int len=data.len;
        double floating=data.floating;
        for(int i=0;i<256;i++){
            arr[i]=data.arr[i];
        }
        return decompressHelper(floating,0,1,0,len,"");
    }
    public static void main(String[] args) throws IOException {

        System.out.println("(c)ompress / (d)ecompress");
        Scanner input=new Scanner(System.in);
        char c=input.next().charAt(0);
        switch(c){
            case 'c':
                String in=FileHandler.readTxt(" ");
                for(int i=0;i<in.length();i++){
                    arr[(int)in.charAt(i)]++;
                }
                for(int i=0;i<256;i++){
                    arr[i]/=in.length();
                    if(i>0)arr[i]+=arr[i-1];
                }
                arr[256]=1;
                double floating=compress(in,0,1,0);
                FileHandler.writeBin(arr,floating,in.length());

            break;
            case 'd':
                FileHandler.writeTxt(decompress());
            break;
            default:
                System.out.println("Error");
        }
    }
}