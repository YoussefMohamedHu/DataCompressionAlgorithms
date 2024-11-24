package org.example;

public class Node{
    public String chr;
    public Node[]child;
    public double percentage;
    public Node(String c,double percentage){
        chr=c;
        child=new Node[2];
        this.percentage=percentage;
    }
    public Node(){
        child=new Node[2];
        this.percentage=percentage;
    }
}