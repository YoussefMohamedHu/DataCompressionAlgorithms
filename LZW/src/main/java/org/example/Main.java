package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    public static List<Integer> compress(String input){
        if(input.isEmpty())return new ArrayList<>();
        ArrayList<Integer> lst=new ArrayList<>();
        HashMap<String,Integer>map=new HashMap<>();
        for(int i=0;i<=255;i++){
            map.put(Character.toString((char)i),i);
        }
        int currentVal=256;
        String temp="";
        int len=input.length();
        for(int i=0;i<len;i++){
            temp+=input.charAt(i);
            if(i<len-1){
                int res=map.getOrDefault(temp+input.charAt(i+1),-1);
                if(res==-1){
                    lst.add(map.get(temp));
                    map.put(temp+input.charAt(i+1),currentVal++);
                    temp="";
                }
            }
            else if(i==len-1){
                lst.add(map.get(temp));
            }
        }
        return lst;
    }
    public static String decompress(List<Integer> input){
        if(input.isEmpty())return new String();
        String output="";
        HashMap<Integer,String>map=new HashMap<>();
        for(int i=0;i<=255;i++){
            map.put(i,Character.toString((char)i));
        }
        int currentVal=256;
        output=map.get(input.get(0));
        int prev=input.get(0);
        for(int i=1;i<input.size();i++){
            String temp=map.getOrDefault(input.get(i),"");
            if(temp.isEmpty()){
                map.put(currentVal++,map.get(prev)+map.get(prev).charAt(0));
                output+=map.get(input.get(i));
                prev=input.get(i);
            }
            else{
                output+=map.get(input.get(i));
                map.put(currentVal++,map.get(prev)+map.get(input.get(i)).charAt(0));
                prev=input.get(i);
            }
        }
        return output;
    }
    public static void main(String[] args) throws IOException {
        System.out.println("(c)ompress - (d)ecompress");
            Scanner read=new Scanner(System.in);
            String cmd=read.nextLine().toString().trim().toLowerCase();
            if(cmd.equals("c")){
                String input= Files.readString(Paths.get("in.txt"));
                List<Integer> lst=compress(input);
                String ans="";
                for(var i:lst){
                    ans+=i.toString()+'\n';
                }
                ans=ans.substring(0,Math.max(ans.length()-1,0));

               Files.write(Paths.get("out.txt"), Arrays.asList(ans));
            }
            else if(cmd.equals("d")){
                String input= Files.readString(Paths.get("in.txt"));
                List<String> tempList=Arrays.asList(input.split("\r\n"));
                List<Integer> lst=new ArrayList<>();
                for(var i:tempList){
                    lst.add(Integer.parseInt(i));
                }
                String ans=decompress(lst);
                Files.write(Paths.get("out.txt"), Arrays.asList(ans));
            }
            else {
                System.out.println("Invalid option!");
            }

    }
}