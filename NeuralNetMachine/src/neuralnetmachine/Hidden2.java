/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetmachine;

import java.util.*;
import java.net.*;
import java.io.*;
import static neuralnetmachine.Hidden.sendInput;
import static neuralnetmachine.Hidden.sendOutput;


/**
 *
 * @author IMRANE
 */
public class Hidden2 {

    public static void main(String[] args){
        Socket i1;
        Socket i2;
        Socket o1;
        Socket o2;
        float lr = 0.5f;
        float[] first=new float[4];
        float[] second=new float[4];
        Map<Socket,Float> inputs=new HashMap<Socket,Float>();
        Map<Socket,float[]> outputs=new HashMap<Socket,float[]>();
        HiddenNode hNode = new HiddenNode();
        String config;
        int port=(int)Math.random();
        File file = new File(System.getProperty("user.dir").toString()+"\\src\\neuralnetmachine\\config\\output.txt");
        try{
        BufferedReader br = new BufferedReader(new FileReader(file));
            o1=o2=null;
            while(( config=br.readLine())!=null){
                String[] conf=config.split(" ");
                if((Integer.parseInt(conf[0]))==1){
                    o1=new Socket(conf[1],Integer.parseInt(conf[2]));   
                }else{
                    o2=new Socket(conf[1],Integer.parseInt(conf[2]));
                }
            }    
        file = new File(System.getProperty("user.dir").toString()+"\\src\\neuralnetmachine\\config\\hidden.txt");
            
            br = new BufferedReader(new FileReader(file));
            
            while(( config=br.readLine())!=null){
                String[] conf=config.split(" ");
                if((Integer.parseInt(conf[0]))==2){
                    port=Integer.parseInt(conf[2]);   
                }
            }              
        ServerSocket ss= new ServerSocket(port);
        i1=ss.accept();
        inputs.put(i1,(float)Math.random());
        InputHandler ih1=new InputHandler(i1,hNode,inputs.get(i1));
        ih1.start();
        i2=ss.accept();
        inputs.put(i2,(float)Math.random());
        InputHandler ih2=new InputHandler(i2,hNode,inputs.get(i2));
        ih2.start();
        ih1.join();
        ih2.join();
        hNode.sigmoid();
        sendOutput(o1,hNode.af,hNode.target);
        sendOutput(o2,hNode.af,hNode.target);
        while(true){
            ObjectInputStream ois1=new ObjectInputStream(o1.getInputStream());
            first=(float[])ois1.readObject();
            outputs.put(o1, first);
            ObjectInputStream ois2=new ObjectInputStream(o2.getInputStream());
            second=(float[])ois2.readObject();
            outputs.put(o2, second);
            for (Map.Entry<Socket, Float> entry : inputs.entrySet()) {
		inputs.put(entry.getKey(),entry.getValue()-
                        hNode.afDeriv(entry.getValue(),first[0],first[1],first[2],first[3])*lr-
                        hNode.afDeriv(entry.getValue(),second[0],second[1],second[2],second[3])*lr);
                }
            for (Map.Entry<Socket, Float> entry : inputs.entrySet()) {
		sendInput(entry.getKey());
                }
            ih1=new InputHandler(i1,hNode,inputs.get(i1));
            ih1.start();
            ih2=new InputHandler(i2,hNode,inputs.get(i2));
            ih2.start();
            ih1.join();
            ih2.join();
            hNode.sigmoid();
            sendOutput(o1,hNode.af,hNode.target);
            sendOutput(o2,hNode.af,hNode.target);
        }
        }catch(Exception e){
            
        }
    }
    public static void sendOutput(Socket s,float data,int target){
        Socket socket =s;
        ObjectOutputStream oos;
        float[] outdata = new float[2];
        try{
            oos=new ObjectOutputStream(socket.getOutputStream());
            outdata[0]=data;
            outdata[1]=target;
            oos.writeObject(outdata);
        }catch(Exception e){
            
        }
        
    }
    public static void wait(Socket s){
        try{
          DataInputStream dis = new DataInputStream(s.getInputStream());
          dis.available();
        }catch(Exception e){
            
        }
        
    }
    
}

