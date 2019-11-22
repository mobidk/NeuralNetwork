/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetmachine;

import java.util.*;
import java.net.*;
import java.io.*;

public class Input2 {
    
    public static void main(String[] args){
        String order1="send";
        String order2="send";
        String order3="send";        
        int epochs = 0;
        int iterate = 0;
        int[][] data=new int[4][2];
        int[] toSend=new int[2];
        int port=(int)Math.random();
        String config;
        File f = new File(System.getProperty("user.dir").toString()+"\\src\\neuralnetmachine\\config\\userconf.txt");
        try{
            BufferedReader br = new BufferedReader(new FileReader(f));
                while((config=br.readLine())!=null){
                    String[] conf=config.split(" ");
                    
                        port=Integer.parseInt(conf[0]);
                    
                }
            Socket s=new Socket("localhost",port);

            ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
            data=(int[][])ois.readObject();
            for(int i=0;i<data.length;i++){
                for(int j=0;j<data[0].length;j++){
                    System.out.print(data[i][j]+"| ");
                }
                System.out.println();
            }
            f = new File(System.getProperty("user.dir").toString()+"\\src\\neuralnetmachine\\config\\hidden.txt");
            br = new BufferedReader(new FileReader(f));
            Socket s1 = null;
            Socket s2 = null;
            Socket s3 = null;
            while(( config=br.readLine())!=null){
                String[] conf=config.split(" ");
                if((Integer.parseInt(conf[0]))==1){
                    s1=new Socket(conf[1],Integer.parseInt(conf[2]));   
                }else if(Integer.parseInt(conf[0])==2){
                    s2=new Socket(conf[1],Integer.parseInt(conf[2]));
                }else{
                    s3=new Socket(conf[1],Integer.parseInt(conf[2]));
                }
            } 
            
            
            DataInputStream dis1 =new DataInputStream(s1.getInputStream());
            DataOutputStream dos1 = new DataOutputStream(s1.getOutputStream());
            ObjectOutputStream oos1 = new ObjectOutputStream(s1.getOutputStream());           
            DataInputStream dis2 =new DataInputStream(s2.getInputStream());
            DataOutputStream dos2 = new DataOutputStream(s2.getOutputStream());
            ObjectOutputStream oos2 = new ObjectOutputStream(s2.getOutputStream());
            DataInputStream dis3 =new DataInputStream(s3.getInputStream());
            DataOutputStream dos3 = new DataOutputStream(s3.getOutputStream());
            ObjectOutputStream oos3 = new ObjectOutputStream(s3.getOutputStream());
            System.out.println("entering while"); 
            while(!order1.equals("exit") && !order2.equals("exit") && !order3.equals("exit")){
            System.out.println("Inside while");   
            toSend[0]=data[iterate][0];
            toSend[1]=data[iterate][1];
            oos1.writeObject(toSend);
            oos2.writeObject(toSend);
            oos3.writeObject(toSend);
            order1=dis1.readUTF();
            System.out.println(order1);  
            order2=dis2.readUTF();
            System.out.println(order2);
            order3=dis3.readUTF();
            System.out.println(order3);
            iterate++;
            if(iterate==4){
                System.out.println(epochs);
                System.out.println("-----------------------------------------");
                iterate=0;
                epochs++;
            }
            }
        }catch(Exception e){
            
        }  
        
        return;
    }    
    
}
