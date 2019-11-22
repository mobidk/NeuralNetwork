/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnetmachine;

import java.net.*;
import java.io.*;
import java.util.*;
/**
 *
 * @author mobar
 */
public class Initialise {


    public static void main(String[] args){
        File file = new File("C:\\Users\\IMRANE\\Documents\\NetBeansProjects\\NeuralNetMachine\\src\\neuralnetmachine\\config\\xor.txt");
        int[][] data = new int[4][3];
        String line;
        String[] values = new String[3];
        int counter =0;
        InitialInputHandler[] iih = new InitialInputHandler[2];
        int port=(int)Math.random();
        String config;
        File f = new File(System.getProperty("user.dir").toString()+"\\src\\neuralnetmachine\\config\\userconf.txt");
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));
            for(int i=0; i< data.length;i++){
                line=br.readLine();
                values=line.split(" ");
                for(int j=0;j<data[0].length;j++){
                    data[i][j]=Integer.parseInt(values[j]);
                }
            }
                br = new BufferedReader(new FileReader(f));
                while((config=br.readLine())!=null){
                    String[] conf=config.split(" ");
                    
                        port=Integer.parseInt(conf[0]);
                    
                }
                ServerSocket ss = new ServerSocket(port);              
                System.out.println("started Server");
                Socket s = ss.accept();
                InitialInputHandler iih1 = new InitialInputHandler(s,counter,data);
                iih1.start();
                System.out.println(counter);
                counter++;
                System.out.println("waiting input 2");
                Socket s2 = ss.accept();
                InitialInputHandler iih2 = new InitialInputHandler(s2,counter,data);
                iih2.start();
                System.out.println("started initializing input 2");
                System.out.println(counter);
                counter++;
                iih1.join();
                iih2.join();
                Thread.sleep(3000);
                }
                
        catch(Exception e){
            
        }
        System.out.println("out");
    }
}
class InitialInputHandler extends Thread {
    Socket s;
    int counter;
    int[][] data = new int[4][3];
    int[][] toSend = new int[4][2];
    ObjectOutputStream oos;
    InitialInputHandler(Socket s,int counter,int[][]data){
        try{
            this.oos=new ObjectOutputStream(s.getOutputStream());
            this.s=s;
            this.counter=counter;
            this.data=data;
        }catch(Exception e){
            
        }
        
        
    }
    public void run(){
                    try{

                        for(int j=0;j<toSend.length;j++){
                            toSend[j][0]=data[j][counter];
                            toSend[j][1]=data[j][2];
                        }                                              
                       sendInput(oos);                                            
                                                                                               
            }catch(Exception e){
                    
                }                   
    }
    public void sendInput(ObjectOutputStream oos){
        try{
            oos.writeObject(toSend);                                              
            System.out.println("Sent informatins"); 
        }catch(Exception e){
            
        }
       
    }
                }

