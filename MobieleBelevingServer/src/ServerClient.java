import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class ServerClient {

    private Socket socket;
    Person person;

    Queue<ServerClient> buddyQueue;
    ServerClient pendingRequest;
    Team team;
    private boolean otherAccepted = false;

    private DataOutputStream output;
    private ObjectInputStream inputStream;
    private String barcode = "";
    private DataInputStream input;

    public ServerClient(Socket socket) {
        this.socket = socket;
        new Thread(this::handleRequest).start();

        this.buddyQueue = new LinkedList<>();
    }

    public void handleRequest() {
        while (this.socket.isConnected()) {
            try {
                this.input = new DataInputStream(this.socket.getInputStream());
                this.output = new DataOutputStream(this.socket.getOutputStream());

                String request = input.readUTF();
                String[] chunks = request.split("/");

                switch (chunks[0]) {
                    case "login":
                        System.out.println(chunks[2]);
                        Person person = new Person(chunks[1], chunks[2]);
                        boolean isLoggedIn = handleLogin(person);
                        this.output.writeBoolean(isLoggedIn);
                        this.output.flush();

                        break;

                    case "join":
                        if (this.person == null)
                            break;

                        Server.queue.join(this);

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
                            Team team = new Team();
                            this.joinTeam(team);
                            pendingRequest.joinTeam(team);
                            Server.queue.leave(this);
                            Server.queue.leave(pendingRequest);
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
}
