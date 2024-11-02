package org.example;
import java.util.*;
public class LZ77 {
    String file;
    int searchSize=10;
    int lookUpSize=10;
    LZ77(){
    }
    public ArrayList<tags>compress(String file)
    {
        this.file=file;
        ArrayList<tags>lst=new ArrayList<tags>();
        int llook=0,rlook=Math.min(lookUpSize,file.length());

        while(llook<file.length()){

            tags tag=new tags();
            tag.len=0;tag.pos=0;
            String temp="";
            for(int i=llook;i<rlook;i++){
                temp+=file.charAt(i);
                int ok=exist(temp,llook);
                if(ok==-1){

                    tag.len=temp.length()-1;
                    tag.lastChar=temp.charAt(temp.length()-1);
                    break;
                }
                else{
                    tag.pos=ok;
                    tag.len=temp.length();
                        if(i==file.length()-1){
                            tag.len--;
                            tag.lastChar=file.charAt(i);
                            if(tag.len==0)tag.pos=0;
                        }
                }
            }
            lst.add(tag);
            rlook=Math.min(rlook+tag.len+1,file.length());
            llook=Math.min(llook+tag.len+1,file.length());

        }

        return lst;
    }
    public int exist(String str,int l){
        if(l==0)return -1;
       int ret= file.indexOf(str,Math.max(l-searchSize,0),l);
       return ret==-1?-1:l-ret;
    }
    public String decompress(ArrayList<tags>lst)
    {
        String temp="";
        for(var i : lst){
            if(i.len==0)temp+=i.lastChar;
            else{
                int startPoint=temp.length()-i.pos;
                for(int j=startPoint;j<i.len+startPoint;j++)temp+=temp.charAt(j);
                temp+=i.lastChar;
            }
        }
        return temp;
    }
}
