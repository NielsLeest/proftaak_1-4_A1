import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Server {
    //Namen kunnen niet met spaties nu!!
    private static ArrayList<ServerClient> clients = new ArrayList<>();
    public static ArrayList<String> barcodes = new ArrayList<>(Arrays.asList("1234", "543210", "49302"));
    public static GameServer mazeGame;
    public static MatchQueue queue = new MatchQueue();

    public static void main(String[] args) {
        new Thread(Server::server).start();
        new Thread(Server::commander).start();
        new Thread(Server::match).start();
    }


    private static void commander() {
        Scanner reader = new Scanner(System.in);
        while (true) {
            String l = reader.nextLine();
            String[] chunk = l.split(" ");
            switch (chunk[0]) {


                case "qsize":

                    System.out.println(queue.getQueue().size());
                    break;

                case "send":
                    clients.get(Integer.parseInt(chunk[1])).send(chunk[2] + "\n");
                    System.out.println("send" + chunk[2] + "to" + clients.get(Integer.parseInt(chunk[1])));
                    break;
                case "":
                    System.out.println("empty string");
                    break;

                case "getque":
                    for (ServerClient s : queue.getQueue()) {
                        System.out.println(s);
                    }

                    break;
                case "start":
                    if (mazeGame != null) {
                        System.out.println("start game");
                        mazeGame.startGame();

                    }
                    break;
                case "kill":
                    killserver();
                    break;
                case "clients":
                    for (ServerClient s : clients
                    ) {
                        System.out.println(s);

                    }
                case "clear_empty":
                    for (ServerClient s : clients
                    ) {
                        if (s.getPerson() == null) {
                            clients.remove(s);
                        }

                    }
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
                if (newSocket.getInetAddress().toString().equals("/192.168.137.22") && mazeGame == null) {
                    mazeGame = new GameServer(newSocket);
                    System.out.println("dit is een esp!");

                } else if (!newSocket.getInetAddress().toString().equals("/192.168.137.22")) {
                    System.out.println("phone connect");
                    clients.add(new ServerClient(newSocket, queue));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void killserver() {
        mazeGame = null;
    }


    public static void match() {

        while (true) {
            if (queue.getQueue().size() > 1) {
                ServerClient s1 = queue.getQueue().get(0);
                ServerClient s2 = queue.getQueue().get(1);
                s1.pendingRequest = s2;
                s2.pendingRequest = s1;
                s1.send("found");
                s2.send("found");
                System.out.println("werkt?");


                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
