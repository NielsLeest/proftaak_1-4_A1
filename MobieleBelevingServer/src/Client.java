import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Application {

    private Socket socket;
    private TextField text;
    private Button sendButton;
    private Button button;
    private TextField textField;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.socket = new Socket("localhost", 8080);

        GridPane pane = new GridPane();

        this.text = new TextField();
        pane.add(this.text, 0, 0);

        this.sendButton = new Button("send");
        this.sendButton.setOnAction(e ->{
            sendLogin(this.text.getText());
        });
        pane.add(this.sendButton, 0, 1);

        this.button = new Button();
        this.textField = new TextField();
        this.button.setOnAction(e -> {
            sendBarcode(this.textField.getText());
        });

        pane.add(this.textField, 1, 0);
        pane.add(this.button, 1, 1);

        primaryStage.setScene(new Scene(pane));
        primaryStage.show();

        new Thread(this::handleConnection).start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void handleConnection(){
        while (true){
            try {
                //Socket socket = new Socket("localhost", 8080);

                DataInputStream input = new DataInputStream(this.socket.getInputStream());

                System.out.println(this.socket.isConnected());
                while (this.socket.isConnected()) {
                    boolean message = input.readBoolean();
                    //String[] chunks = message.split(" ");

                    this.textField.setText("" + message);
                }
                System.out.println(this.socket.isConnected());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendLogin(String username){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
            DataOutputStream output = new DataOutputStream(this.socket.getOutputStream());
            output.writeUTF("login");
            output.flush();
            oos.writeObject(new Person(username));
            oos.flush();
            System.out.println("client sent request");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendBarcode(String barcode){
        try {
            DataOutputStream output = new DataOutputStream(this.socket.getOutputStream());
            output.writeUTF("barcode" + " " + barcode);
            output.flush();
            System.out.println("client sent barcode");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
