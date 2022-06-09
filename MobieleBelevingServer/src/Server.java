import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Server {
    //Namen kunnen niet met spaties nu!!
    private static ArrayList<ServerClient> clients = new ArrayList<>();
    public static ArrayList<String> barcodes = new ArrayList<>(Arrays.asList("012345", "543210"));
    public static GameServer mazeGame;
    public static MatchQueue queue = new MatchQueue();

    public static void main(String[] args) {
        new Thread(Server::server).start();
        new Thread(Server::commander).start();
    }



    private static void commander() {
        Scanner reader = new Scanner(System.in);
        while (true) {
            String command = reader.nextLine();

            switch (command) {
                case "":
                    System.out.println("empty string");
                    break;
                case "start":
                    if(mazeGame != null) {
                        System.out.println("start game");
                        mazeGame.startGame();

                    }
                    break;
                case "kill":
                    killserver();
                    break;
                case "clients":

                    break;
            }
        }
    }

    public static void server() {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {


                Socket newSocket = serverSocket.accept();
                System.out.println(newSocket.getInetAddress());
                if (newSocket.getInetAddress().toString().equals("/192.168.137.22")&& mazeGame == null) {
                    mazeGame = new GameServer(newSocket);
                    System.out.println("dit is een esp!");

                } else if (!newSocket.getInetAddress().toString().equals("/192.168.137.22")) {
                    System.out.println("phone connect");
                    clients.add(new ServerClient(newSocket));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void killserver() {
        mazeGame = null;
    }
}
