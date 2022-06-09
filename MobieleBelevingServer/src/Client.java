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
    private TextField nameText;
    private Button sendButton;
    private Button button;
    private TextField barcodeText;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.socket = new Socket("localhost", 8080);

        this.nameText = new TextField();
        this.barcodeText = new TextField();
        this.sendButton = new Button("send");

        this.sendButton.setOnAction(e ->{
            sendLogin(this.nameText.getText());
        });

        GridPane pane = new GridPane();
        pane.add(this.nameText, 0, 0);
        pane.add(this.sendButton, 0, 1);
        pane.add(this.barcodeText, 1, 0);

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
                DataInputStream input = new DataInputStream(this.socket.getInputStream());

                while (this.socket.isConnected()) {
                    boolean message = input.readBoolean();

                    this.barcodeText.setText("" + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendLogin(String username){
        try {
            DataOutputStream ouput = new DataOutputStream(this.socket.getOutputStream());
            ouput.writeUTF("login " + this.nameText.getText() + " " + this.barcodeText.getText());
            ouput.flush();
            System.out.println("client sent request");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
