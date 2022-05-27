import java.io.*;
import java.net.Socket;

public class ServerClient {

    private Socket socket;
    Person person;
    private DataOutputStream output;
    private String barcode = "";

    public ServerClient(Socket socket) {
        this.socket = socket;
        new Thread(this::handleRequest).start();
    }

    public void handleRequest() {
        while (this.socket.isConnected()){
            try {
                DataInputStream input = new DataInputStream(this.socket.getInputStream());

                //inputStream.readObject();

                String request = input.readUTF();
                String[] chunks = request.split(" ");
                System.out.println(chunks[0]);

                switch (chunks[0]){
                    case "login":
                        System.out.println("login request");
                        ObjectInputStream inputStream = new ObjectInputStream(this.socket.getInputStream());
                        try {
                            handleLogin((Person) inputStream.readObject());
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "barcode":
                        this.barcode = chunks[1];
                        output = new DataOutputStream(this.socket.getOutputStream());
                        output.writeBoolean(handleBarcode(this.barcode));
                        output.flush();
                        break;
                }
                request = "";
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean handleLogin(Person person){
        this.person = person;
        System.out.println(this.person.getUserName() + " added");
        return true;
    }

    public boolean handleBarcode(String userBarcode){
        for (String barcode : Server.barcodes) {
            if(barcode.equals(userBarcode)){
                return true;
            }
        }
        return false;
    }

    public Person getPerson(){
        return this.person;
    }

}
