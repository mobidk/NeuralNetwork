/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetmachine;

import java.net.*;
import java.util.*;
import java.io.*;
/**
 *
 * @author mobar
 */
public class Output2 {

    public static void main(String[] args){
        Socket h1;
        Socket h2;
        Socket h3;
        int iterate=0;
        int epochs=0;
        String config;
        int port=(int)Math.random();
        Map<Socket,Float> Hidden=new HashMap<Socket,Float>();
        float[] infos = new float[4];
        OutputNode oNode = new OutputNode();
        File file = new File(System.getProperty("user.dir").toString()+"\\src\\neuralnetmachine\\config\\output.txt");
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            
            while((config=br.readLine())!=null){
                String[] conf=config.split(" ");
                if((Integer.parseInt(conf[0]))==2){
                    port=Integer.parseInt(conf[2]);
                }
            }

            ServerSocket ss= new ServerSocket(port);
                h1 = ss.accept();
                Hidden.put(h1,(float)Math.random());
                InitialHiddenHandler hh1 = new InitialHiddenHandler(h1,oNode,Hidden.get(h1));
                hh1.start();
                h2 = ss.accept();
                Hidden.put(h2,(float)Math.random());
                InitialHiddenHandler hh2 = new InitialHiddenHandler(h2,oNode,Hidden.get(h2));
                hh2.start();
                h3 = ss.accept();
                Hidden.put(h3,(float)Math.random());
                InitialHiddenHandler hh3 = new InitialHiddenHandler(h3,oNode,Hidden.get(h3));
                hh3.start();
                hh1.join();
                hh2.join();
                hh3.join();
                oNode.sigmoid();
                System.out.println("suretée = " + oNode.af);
                if(oNode.target==1){
                    oNode.error=(oNode.af*oNode.af)/2;
                }else{
                    oNode.error=(1-2*oNode.af+oNode.af*oNode.af)/2;
                }
                while(true){
                iterate++;
                if(iterate==4){
                    iterate=0;
                    epochs++;
                    System.out.println(epochs);
                }
                if(epochs==5){
                    System.out.println("epochs ran out");
                    break;
                }
                infos[0]=oNode.af;
                infos[1]=oNode.target;
                infos[2]=oNode.z;
                
                for (Map.Entry<Socket, Float> entry : Hidden.entrySet()) {
		ObjectOutputStream oos=new ObjectOutputStream(entry.getKey().getOutputStream());
                infos[3]=Hidden.get(entry.getKey());
                oos.writeObject(infos);
                }
                for (Map.Entry<Socket, Float> entry : Hidden.entrySet()) {
		Hidden.put(entry.getKey(),entry.getValue()+oNode.deGrad(entry.getValue()));
                }
                hh1 = new InitialHiddenHandler(h1,oNode,Hidden.get(h1));
                hh1.start();
                hh2 = new InitialHiddenHandler(h2,oNode,Hidden.get(h2));
                hh2.start();
                hh3 = new InitialHiddenHandler(h3,oNode,Hidden.get(h3));
                hh3.start();
                hh1.join();
                hh2.join();
                hh3.join();
                System.out.println("joined all");
                oNode.sigmoid();
                System.out.println("suretée = " + oNode.af);
                if(oNode.target==0){
                    oNode.error=(oNode.af*oNode.af)/2;
                }else{
                    oNode.error=(1-2*oNode.af+oNode.af*oNode.af)/2;
                }
                }
        }catch(Exception e){
            
        }
        
    }
}