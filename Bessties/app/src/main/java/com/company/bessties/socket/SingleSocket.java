package com.company.bessties.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SingleSocket {
    private  static volatile SingleSocket sSocket;

     public Client client;
    private SingleSocket(){
        this.client = new Client();


    }
     public static SingleSocket getInstance() {
        if(sSocket == null){
            synchronized (SingleSocket.class){
                if(sSocket == null){
                    sSocket = new SingleSocket();
                }

            }

        }
        return sSocket;
    }

}
