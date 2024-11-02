package org.example;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        LZ77 obj=new LZ77();
        ArrayList<tags>lst=obj.compress("yousef mohamed");
        for (tags temp : lst) {
            System.out.print(temp.pos + " ");
            System.out.print(temp.len + " ");
            System.out.println(temp.lastChar);
        }
        String ans=obj.decompress(lst);
        System.out.println(ans);
    }
}