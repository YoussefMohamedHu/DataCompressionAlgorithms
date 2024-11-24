package org.example;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.util.*;

public class Main {
    public static void setCodesInTree(Node root,String val,HashMap<String,String> ret,boolean decompress){

        if(root.child[0]==null){
            if(!decompress)
                ret.put(root.chr,val);
            else
                ret.put(val,root.chr);
        }
        else{
            setCodesInTree(root.child[0],val+"0",ret,decompress);
            setCodesInTree(root.child[1],val+"1",ret,decompress);
        }
        return;
    }

    public static HashMap<String,String> huffmanTree(double[] percentage,boolean decompress){
        HashMap<String,String>ret=new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                return Double.compare(n1.percentage, n2.percentage); // Reverse for descending order
            }
        });
        for(int i=0;i<256;i++){
            if(percentage[i]!=0){
                String chr=String.valueOf((char)i);
                Node temp=new Node(chr,percentage[i]);
                pq.add(temp);
            }
        }
        if(pq.size()==1){
            ret.put(pq.peek().chr,"0");
            return ret;
        }
        else{
            while(pq.size()>1){
                Node newTemp=new Node();
                Node temp1=pq.peek();
                newTemp.child[0]=temp1;
                pq.remove();
                Node temp2=pq.peek();
                newTemp.child[1]=temp2;
                pq.remove();
                newTemp.percentage=temp1.percentage+temp2.percentage;
                pq.add(newTemp);
            }
            setCodesInTree(pq.peek(),"",ret,decompress);
            return ret;
        }

    }
    public static List<Byte> compress(String input) throws IOException {
        List<Byte> ret=new ArrayList<>();
        double []percentage=new double[256];
        int cnt=input.length();
        for(int i=0;i<input.length();i++){
            int c=(int)input.charAt(i);
            percentage[c]++;
        }
        for(int i=0;i<256;i++){
            percentage[i]/=cnt;
        }
        FileHandler.fillCharsPercentage(percentage);
        HashMap<String,String>lookup=huffmanTree(percentage,false);
        int ptr=0;
        for(int i=0;i<input.length();i++){
            String val=lookup.get(String.valueOf(input.charAt(i)));
            for(int j=0;val!=null && j<val.length();j++){
                if((ptr)%8==0)ret.add((byte)0);
                int idx=ptr/8;
                int shift=ptr%8;
                if(val.charAt(j)=='1'){
                    ret.set(idx, (byte) (ret.get(idx)|(byte)(1<<(7-shift)))) ;
                }
                ptr++;
            }
        }

        if(ptr%8==0)ret.add((byte)0);
        else ret.add((byte)(8-(ptr%8)));
        List<Byte>overhead=new ArrayList<>();
        byte tot=0;
        for(int i=0;i<256;i++){
            if(percentage[i]!=0){
                overhead.add((byte)i);
               overhead.addAll(setDouble(percentage[i]));
               tot++;
            }
        }
        overhead.addFirst(tot);
        overhead.addAll(ret);
        ret=overhead;
        return ret;
    }

    public static List<Byte> setDouble(double num) {
        List<Byte>ret=new ArrayList<>(8);
        for(int i=0;i<8;i++)ret.add((byte) 0);
        ByteBuffer buffer = ByteBuffer.allocate(Double.BYTES); // Double.BYTES is 8
        buffer.putDouble(num);
        int ptr=0;
        for(var i:buffer.array()){
            ret.set(ptr,i);
            ptr++;
        }
        return ret;
    }
    public static double getDouble(List<Byte> num) {
        byte []number=new byte[8];
        for(int i=0;i<8;i++)number[i]=num.get(i);
        ByteBuffer buffnumber=ByteBuffer.wrap(number);
        return buffnumber.getDouble();
    }

    public static double []setPercentage(List<Byte>lines){

        double []percentage=new double[256];
        byte len=lines.getFirst();

        for(int i=1;i<=len*9;i+=9){
            byte chr=lines.get(i);
            double num=getDouble(lines.subList(i+1,i+9));
            percentage[chr]=num;
        }
        return percentage;
    }
    public static String decompress(List<Byte>content) throws IOException {
        String ret="";
        byte nonUsedBits=content.getLast();content.removeLast();
        double []percentage=setPercentage(content);
        byte overheadLen=content.get(0);
        content=content.subList(overheadLen*9+1,content.size());
        HashMap<String,String>lookup=huffmanTree(percentage,true);
        String temp="";
        int len=content.size()*8-nonUsedBits,ptr=0;
        while(ptr<len){
            boolean bit=false;
            int idx=ptr/8;
            int shift=ptr%8;
            bit=(content.get(idx)&(1<<(7-shift)))!=0?true:false;
            if(bit)temp+='1';
            else temp+='0';

            if(lookup.containsKey(temp)){
                ret+=lookup.get(temp);
                temp="";
            }
            ptr++;
        }
        return ret;
    }
    public static void main(String[] args) throws IOException {
       System.out.println("(C)ompress / (D)ecompress");
        Scanner in=new Scanner(System.in);
       String choice=in.next();
       if(Character.toUpperCase(choice.charAt(0))=='C'){
           String content=FileHandler.readTxt("inputCompression.txt");
           List<Byte> ans=compress(content);
           FileHandler.writeBin(ans,"Decompressed.bin");

       }
       else{
            List<Byte> content=FileHandler.readBin("Decompressed.bin");
            String ans=decompress(content);
           FileHandler.writeTxt(ans);
       }
    }
}