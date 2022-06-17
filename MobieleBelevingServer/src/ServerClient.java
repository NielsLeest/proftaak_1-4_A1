import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class ServerClient {

    private Socket socket;
    private Person person;

    Queue<ServerClient> buddyQueue;
    ServerClient pendingRequest;
    Team team;
    private boolean otherAccepted = false;

    private DataOutputStream output;

    private String barcode = "";
    private DataInputStream input;
    private InetAddress clientIP;
    private PrintWriter writer;
    public boolean startgame = false;


    public void setGame(GameServer game) {
        this.game = game;
    }

    private GameServer game;

    public ServerClient(Socket socket, MatchQueue que) {
        this.socket = socket;
        try {
            this.input = new DataInputStream(this.socket.getInputStream());
//            this.output = new DataOutputStream(this.socket.getOutputStream());
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new Thread(this::handleRequest).start();
        this.buddyQueue = new LinkedList<>();
        this.clientIP = socket.getInetAddress();

    }

    public void handleRequest() {
        while (this.socket.isConnected()) {
            try {

                while (this.input.available() > 0) {
                    String request = input.readUTF();
                    System.out.println(request);
                    String[] chunks = request.split("/");

                    switch (chunks[0]) {

                        case "startgame":
                            this.startgame = true;
                            System.out.println(this.getPerson() + " wants to start");
                            if (team.game()) {
                                System.out.println("let the game begin!");
                                Server.mazeGame.startGame();
                            }
                            break;

                        case "pending":
                            if (pendingRequest != null) {
                                writer.println(pendingRequest.getPerson().getUserName());
                                writer.flush();
                                writer.println(pendingRequest.getPerson().getAge());
                                writer.flush();
                            } else {
                                writer.println("Midas");
                                writer.println("55");
                                writer.flush();
                            }

                            break;

                        case "barcode":
                            System.out.println(chunks[1]);
                            if (Server.barcodes.contains(chunks[1])) {
                                this.writer.println("true");

                            } else {
                                this.writer.println("false");
                            }
                            this.writer.flush();
                            break;
                        case "login":
                            for (String s : chunks
                            ) {
                                System.out.println(s);

                            }
                            System.out.println(chunks[3]);
                            this.person = new Person(chunks[1] + " " + chunks[2], chunks[3], "0");
//                            boolean isLoggedIn = handleLogin(person);
//                            this.writer.write(isLoggedIn+"");
//                            this.writer.flush();

                            break;
                        case "get":
                            ObjectOutputStream ous = new ObjectOutputStream(this.output);
                            if (chunks[1].equals("name"))
//                                ous.writeObject(this.person.getUserName());
                                this.writer.write(this.person.getUserName());
                            if (chunks[1].equals("Age"))
//                                ous.writeObject(this.person.getAge());
                                this.output.writeUTF(this.person.getAge());
                            break;

                        case "join":
                            if (this.person == null) {
                                break;
                            } else {
                                Server.queue.join(this);
                            }
                            break;

                        case "leave":
                            if (this.person == null)
                                break;

                            if (Server.queue.contains(this))
                                Server.queue.leave(this);
                            else if (this.team != null)
                                this.leaveTeam();

                            break;

                        case "decline":

                            pendingRequest.revokeRequest(this);
                            pendingRequest.uploadRequest();

                            this.revokeRequest(pendingRequest);
                            this.uploadRequest();

                            break;

                        case "accept":

                            pendingRequest.otherAccepted = true;
                            if (this.otherAccepted) {
                                if (pendingRequest.team == null) {
                                    team = new Team();
                                } else {
                                    team = pendingRequest.team;
                                }
                                team.join(this);
//                                pendingRequest.joinTeam(team);

                            }
                            break;

                    }

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean handleLogin(Person person) {
        if (Server.barcodes.contains(person.getBarcode())) {
            this.person = person;
            System.out.println(this.person.getUserName() + " added");
            return true;
        } else {
            return false;
        }
    }

    public Person getPerson() {
        return this.person;
    }

    public void uploadRequest() {
        this.otherAccepted = false;
        try {
            if (pollRequest())
                output.writeUTF("request/" + pendingRequest.getPerson().getUserName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void revokeRequest(ServerClient person) {
        try {
            buddyQueue.remove(person);
            if (pendingRequest.equals(person)) {
                output.writeUTF("revoke");
                pendingRequest = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean pollRequest() {
        if (pendingRequest == null && !buddyQueue.isEmpty()) {
            pendingRequest = buddyQueue.poll();
            return true;
        }
        return false;
    }

    public void joinTeam(Team team) {
        this.team = team;
        team.join(this);
        Server.queue.leave(this);
    }

    public void leaveTeam() {
        this.team.leave(this);
        this.team = null;
    }

    @Override
    public String toString() {
        return "ServerClient{" +
                "person=" + person +
                '}';
    }


    public InetAddress getClientIP() {
        return this.clientIP;
    }


    public void send(String s) {
        System.out.println("send " + s);
        writer.println(s);
        writer.flush();

    }
}
