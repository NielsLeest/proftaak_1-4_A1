import java.io.*;
import java.net.Socket;

public class ServerClient {

    private Socket socket;
    Person person;
    private DataOutputStream output;
    private ObjectInputStream inputStream;
    private String barcode = "";
    private DataInputStream input;

    public ServerClient(Socket socket) {
        this.socket = socket;
        new Thread(this::handleRequest).start();
    }

    public void handleRequest() {
        while (this.socket.isConnected()){
            try {
                this.input = new DataInputStream(this.socket.getInputStream());
                this.output = new DataOutputStream(this.socket.getOutputStream());

                String request = "";
                request = this.input.readUTF();
                String[] chunks = request.split(" ");

                switch (chunks[0]){
                    case "login":
                        System.out.println(chunks[2]);
                        Person person = new Person(chunks[1], chunks[2]);
                        boolean isLoggedIn = handleLogin(person);
                        this.output.writeBoolean(isLoggedIn);
                        this.output.flush();

                        break;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean handleLogin(Person person){
        if(Server.barcodes.contains(person.getBarcode())){
            this.person = person;
            System.out.println(this.person.getUsername() + " added");
            return true;
        }
        else {
            return false;
        }
    }

    public Person getPerson(){
        return this.person;
    }

}
