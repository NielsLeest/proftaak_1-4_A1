package com.company.bessties.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public  class Client {
    private  Socket socket;


    private String firstName;
    private String lastName;
    private int age;
    private String barcode;
    private  DataOutputStream dos;
    private  DataInputStream dis;

    private boolean validation = false;

    public  void startConnection(){
        try {
            this.socket = new Socket("192.168.137.1", 8080);
            System.out.println("socketed");
             this.dos = new DataOutputStream(socket.getOutputStream());
            this.dis = new DataInputStream(socket.getInputStream());
            dos.writeUTF("kutzooi");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge(){
        return this.age;
    }

    public String getBarcode() {
        return barcode;
    }

    public  boolean sendBarcode(String barcode){
        try {
            DataOutputStream ouput = new DataOutputStream(this.socket.getOutputStream());
            ouput.writeUTF("barcode" +  "/" + barcode);
            ouput.flush();
            DataInputStream input = new DataInputStream(this.socket.getInputStream());
            return input.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
return false;
    }




    //    @Override
//    public void start(Stage primaryStage) throws Exception{
//        this.socket = new Socket("localhost", 8080);
//
//        this.nameText = new TextField();
//        this.barcodeText = new TextField();
//        this.sendButton = new Button("send");
//
//        this.sendButton.setOnAction(e ->{
//            sendLogin(this.nameText.getText());
//        });
//
//        GridPane pane = new GridPane();
//        pane.add(this.nameText, 0, 0);
//        pane.add(this.sendButton, 0, 1);
//        pane.add(this.barcodeText, 1, 0);
//
//        primaryStage.setScene(new Scene(pane));
//        primaryStage.show();
//
//        new Thread(this::handleConnection).start();
//    }

//    public static void main(String[] args) {
//        launch(args);
//    }

    public void handleConnection(){
        while (true){
            try {
                DataInputStream input = new DataInputStream(this.socket.getInputStream());

                while (this.socket.isConnected()) {
                    String message = input.readUTF();
                    if(message.equals("true")){
                        this.validation = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean sendLogin(String barcode){
        try {
            DataOutputStream ouput = new DataOutputStream(this.socket.getOutputStream());
            ouput.writeUTF("login " +  " " + barcode);
            ouput.flush();
            DataInputStream input = new DataInputStream(this.socket.getInputStream());
            return input.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean getvalidation(){
        return this.validation;
    }


//    public void sendBarcode(String barcode){
//        try {
//            DataOutputStream output = new DataOutputStream(this.socket.getOutputStream());
//            output.writeUTF("barcode" + " " + barcode);
//            output.flush();
//            System.out.println("client sent barcode");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

}
