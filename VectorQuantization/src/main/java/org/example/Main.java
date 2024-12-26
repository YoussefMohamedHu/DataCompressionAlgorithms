package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    public static void generateCodeBook(
            int blockArray[][][][],
            int blockArraywidth,
            int blockArrayHeight,
            int codeBlockwidth,
            int codeBlockHeight,
            int codeBookSize
    ) throws IOException {

        double [][]baseVector=new double[codeBlockwidth][codeBlockHeight];
        for(int i=0;i<blockArraywidth;i++){
            for(int j=0;j<blockArrayHeight;j++){
                for(int k=0;k<codeBlockwidth;k++){
                    for(int l=0;l<codeBlockHeight;l++){
                        baseVector[k][l]+=blockArray[i][j][k][l];
                    }
                }
            }
        }
        for(int k=0;k<codeBlockwidth;k++){
            for(int l=0;l<codeBlockHeight;l++){
                baseVector[k][l]/=blockArrayHeight*blockArraywidth;
            }
        }
        ArrayList<double[][]>arrayOfVectors=new ArrayList<>();
        arrayOfVectors.add(baseVector);
        ArrayList<ArrayList<int[][]>>loader=new ArrayList<>();
        HashMap<int [][],Integer>pixielToIdx =new HashMap<>();
        HashMap<Integer,double [][]>idxTopixiel = new HashMap<>();
        while(arrayOfVectors.size()<=codeBookSize){
            if(arrayOfVectors.size()!=codeBookSize) {
                loader = new ArrayList<>();
                for (int i = 0; i < arrayOfVectors.size() * (arrayOfVectors.size() != codeBookSize ? 2 : 1); i++) {
                    loader.add(new ArrayList<>());
                }
            }
            ArrayList<double[][]> base = new ArrayList<>();
            if(arrayOfVectors.size()<codeBookSize) {
                for (int i = 0; i < arrayOfVectors.size(); i++) {
                    double one[][] = new double[codeBlockwidth][codeBlockHeight];
                    double two[][] = new double[codeBlockwidth][codeBlockHeight];
                    for (int k = 0; k < codeBlockwidth; k++) {
                        for (int l = 0; l < codeBlockHeight; l++) {
                            one[k][l] = arrayOfVectors.get(i)[k][l] - 1;
                            two[k][l] = arrayOfVectors.get(i)[k][l] + 1;
                        }
                    }
                    base.add(one);
                    base.add(two);
                }
            }else{
                for(int i=0;i<arrayOfVectors.size();i++){
                    baseVector=new double[codeBlockwidth][codeBlockHeight];
                    for(int o=0;o<loader.get(i).size();o++) {
                        for (int j = 0; j < codeBlockwidth; j++) {
                            for (int k = 0; k < codeBlockHeight; k++) {
                                baseVector[j][k]+=loader.get(i).get(o)[j][k];
                            }
                        }
                    }
                    for (int j = 0; j < codeBlockwidth; j++) {
                        for (int k = 0; k < codeBlockHeight; k++) {
                            baseVector[j][k]/=Math.max(loader.get(i).size(),1);
                        }
                    }
                    base.add(i,baseVector);
                }
                loader = new ArrayList<>();
                for (int i = 0; i < arrayOfVectors.size() * (arrayOfVectors.size() != codeBookSize ? 2 : 1); i++) {
                    loader.add(new ArrayList<>());
                }
            }

            int nearest=0;
            double diff;
            for(int i=0;i<blockArraywidth;i++){
                for(int j=0;j<blockArrayHeight;j++){
                    diff=Double.MAX_VALUE;
                    nearest=0;
                    for(int k=0;k<base.size();k++){
                        double cnt=0;
                        for(int o=0;o<codeBlockwidth;o++){
                            for(int p=0;p<codeBlockHeight;p++){
                                cnt+=Math.abs(base.get(k)[o][p]-blockArray[i][j][o][p]);
                            }
                        }
                        if(cnt<diff){nearest=k;diff=cnt;}

                    }
                    loader.get(nearest).add(blockArray[i][j]);
                }
            }

            for(int i=0;i<base.size();i++){
                baseVector=new double[codeBlockwidth][codeBlockHeight];
                for(int o=0;o<loader.get(i).size();o++) {
                    for (int j = 0; j < codeBlockwidth; j++) {
                        for (int k = 0; k < codeBlockHeight; k++) {
                            baseVector[j][k]+=loader.get(i).get(o)[j][k];

                        }
                    }
                    if(arrayOfVectors.size()<=codeBookSize){
                        pixielToIdx.put(loader.get(i).get(o),i);

                    }
                }
                for (int j = 0; j < codeBlockwidth; j++) {
                    for (int k = 0; k < codeBlockHeight; k++) {
                        baseVector[j][k]/=Math.max(loader.get(i).size(),1);
                    }
                }
                base.set(i,baseVector);
                idxTopixiel.put(i,baseVector);
                loader.get(i).clear();

            }
            nearest=0;
             diff=0;
            for(int i=0;i<blockArraywidth;i++){
                for(int j=0;j<blockArrayHeight;j++){
                    diff=Double.MAX_VALUE;
                    nearest=0;
                    for(int k=0;k<base.size();k++){

                        double cnt=0;
                        for(int o=0;o<codeBlockwidth;o++){
                            for(int p=0;p<codeBlockHeight;p++){
                                cnt+=Math.abs(base.get(k)[o][p]-blockArray[i][j][o][p]);
                            }
                        }
                        if(cnt<diff){nearest=k;diff=cnt;}

                    }
                    loader.get(nearest).add(blockArray[i][j]);
                }
            }
            if(arrayOfVectors.size()==base.size()){
                arrayOfVectors=base;
                break;
            }
            arrayOfVectors=base;

        }
        int nearest=0;
        double diff;
        //if(arrayOfVectors.size()==codeBookSize)
        for(int i=blockArraywidth-1;i>=0;i--){
            for(int j=blockArrayHeight-1;j>=0;j--){
                diff=Double.MAX_VALUE;
                nearest=0;
                for(int k=0;k<arrayOfVectors.size();k++){
                    double cnt=0;
                    for(int o=0;o<codeBlockwidth;o++){
                        for(int p=0;p<codeBlockHeight;p++){
                            cnt+=Math.abs(arrayOfVectors.get(k)[o][p]-blockArray[i][j][o][p]);
                        }
                    }
                    if(cnt<diff){nearest=k;diff=cnt;}

                }
                pixielToIdx.put(blockArray[i][j],nearest);
            }
        }
        ArrayList<Byte>MatrixOfBytes=new ArrayList<>();
        for(int i=0;i<blockArraywidth;i++){
            for(int j=0;j<blockArrayHeight;j++){
                MatrixOfBytes.add(pixielToIdx.get(blockArray[i][j]).byteValue());
            }
        }
        Data obj=new Data(
                idxTopixiel,
                MatrixOfBytes,
         blockArraywidth,
         blockArrayHeight,
         codeBlockwidth,
         codeBlockHeight,
         codeBookSize
        );

        FileOutputStream fos = new FileOutputStream("decompressed.bin");
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(obj);
        oos.close();
        fos.close();


    }
    public static void compress(String inputPath,int codeBlockwidth,int codeBlockHeight,int codeBookSize) throws IOException {
        BufferedImage original= ImageIO.read(new File(inputPath));

        int power2CodeBookSize=(int)Math.pow(2,Math.ceil(Math.log(codeBookSize)/Math.log(2)));
        codeBookSize=Math.min(power2CodeBookSize,256);
        int x1=codeBlockwidth,x2=x1;
        int y1=codeBlockHeight,y2=y1;

        while(original.getWidth()%x1!=0)x1++;
        while(original.getHeight()%y1!=0)y1++;
        while(original.getWidth()%x2!=0)x2--;
        while(original.getHeight()%y2!=0)y2--;

        if(Math.abs(codeBlockwidth-x1)>Math.abs(codeBlockwidth-x2))codeBlockwidth=Math.max(x2,1);
        else codeBlockwidth=x1;

        if(Math.abs(codeBlockHeight-y1)>Math.abs(codeBlockHeight-y2))codeBlockHeight=Math.max(y2,1);
        else codeBlockHeight=y1;

        int lenW= original.getWidth();
        int lenH= original.getHeight();
        int [][]bitMatrixImage=new int[lenW][lenH];
        for(int i=0;i<lenW;i++){
            for(int j=0;j<lenH;j++){
                bitMatrixImage[i][j]=original.getRGB(i,j)&0xFF;

            }
        }
        int blockArraywidth=lenW/codeBlockwidth;
        int blockArrayHeight=lenH/codeBlockHeight;
        int blockArray[][][][]=new int[blockArraywidth][blockArrayHeight][codeBlockwidth][codeBlockHeight];
        for(int i=0;i<lenW;i+=codeBlockwidth){
            for(int j=0;j<lenH;j+=codeBlockHeight){
                for(int k=0;k<codeBlockwidth;k++){
                    for(int l=0;l<codeBlockHeight;l++){
                        blockArray[i/codeBlockwidth][j/codeBlockHeight][k][l]=bitMatrixImage[i+k][j+l];
                    }
                }
            }
        }
        generateCodeBook(blockArray,blockArraywidth,blockArrayHeight,codeBlockwidth,codeBlockHeight,codeBookSize);



    }
    public static void decompress() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream("decompressed.bin");
        ObjectInputStream ois = new ObjectInputStream(fis);

        Data obj = (Data) ois.readObject();

        ois.close();
        fis.close();
        int width=obj.blockArraywidth*obj.codeBlockwidth;
        int height=obj.blockArrayHeight*obj.codeBlockHeight;
        BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_BYTE_GRAY);
        ArrayList<double[][]>ArrayOfBlocks=new ArrayList<>();
        int itr=0;
        int idx=0;
        int w=0,h=0;
        int mask=(int)(Math.log(obj.codeBookSize)/Math.log(2));
        int tot=mask* obj.blockArrayHeight*obj.blockArraywidth;
        for(int i=0;idx<tot;){
            int ref=0;
            for(int j=0;j<mask;j++){
                ref|=((1<<itr)&(obj.MatrixByte.get(i)))!=0?(1<<j):0;
                itr++;
                if(itr==8){
                    itr=0;
                    i++;
                }
                idx++;
            }
            double [][]base=obj.idxpixiel.get(ref);
            //ArrayOfBlocks.add();
            int ptri=0;
            int ptrj=0;
            for(int k=(w* obj.codeBlockwidth);k<obj.codeBlockwidth+(w* obj.codeBlockwidth);k++){
                for(int o=(h*obj.codeBlockHeight);o<obj.codeBlockHeight+(h*obj.codeBlockHeight);o++){
                    Color color=new Color((int)base[ptri][ptrj],(int)base[ptri][ptrj],(int)base[ptri][ptrj]);
                    //if(k<width/2 &&o<height/2)
                    image.setRGB(k,o,color.getRGB());
                    ptrj++;
                }
                ptri++;
                ptrj=0;
            }
            h++;
            if(h==obj.blockArrayHeight){
                h=0;
                w++;
            }
        }
        ImageIO.write(image,"jpg", new File("out.jpg"));

    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        while(true){
            System.out.println("Type (c)ompress - (d)ecompress , any key else to exit\n");
            Scanner input=new Scanner(System.in);
            char chr=input.next().charAt(0);
            if(chr=='c'){
                System.out.print("Image Path : ");
                String image=input.next();
                System.out.print("\nCodeBlockwidth : ");
                int codeBlockwidth=input.nextInt();
                System.out.print("\nCodeBlockHeight : ");
                int codeBlockHeight=input.nextInt();
                System.out.print("\nCodeBookSize : ");
                int codeBookSize=input.nextInt();
                System.out.println();

                compress(image,codeBlockwidth,codeBlockHeight,codeBookSize);
            }
            else if(chr=='d'){
                decompress();
            }
            else{
                break;
            }
        }
    }
}