import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class GameServer {
    private Socket socket;
    private DataOutputStream DOS;
    private DataInputStream DIS;

    public GameServer(Socket socket) {
        this.socket = socket;
        new Thread(this::startGameserver).start();
    }

    /**
     * initialises the game server
     */
    private void startGameserver() {

        while (socket.isConnected()) {
            try {
                DOS = new DataOutputStream(socket.getOutputStream());
                DIS = new DataInputStream(socket.getInputStream());


                while (true) {
                    if (DIS.available() > 0) {
                        int length = DIS.available();
                        byte[] myMessage = new byte[length];
                        DIS.readFully(myMessage);
                        String clientMessage = translate(myMessage);
                        if (clientMessage.equals("game end")) {
//                                Server.killserver();
                        }
                        System.out.println(clientMessage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        if (!socket.isConnected()) {
            Server.killserver();

        }
    }

    /**
     * starts the physical maze game
     */
    public void startGame() {
        try {
            System.out.println("the game is starting up");
            DOS.writeUTF("A");
            DOS.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * converts bytes to a string
     * @param word the bytes to convert
     * @return the obtained string
     */
    private static String translate(byte[] word) {
        StringBuilder translatedMessage = new StringBuilder();
        for (byte b : word) {
            translatedMessage.append((char) b);
        }

        return translatedMessage.toString();
    }
}
