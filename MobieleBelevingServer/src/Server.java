import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {
    //Namen kunnen niet met spaties nu!!
    private static ArrayList<ServerClient> clients = new ArrayList<>();
    public static ArrayList<String> barcodes = new ArrayList<>(Arrays.asList("012345", "543210"));

    public static void main(String[] args){
        new Thread(Server::server).start();
    }

    public static void server() {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                Socket newSocket = serverSocket.accept();
                clients.add(new ServerClient(newSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
