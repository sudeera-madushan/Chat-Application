package Client.controller;

import Client.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

import static Client.controller.LoginFormController.userName;
import static Client.controller.LoginFormController.host;

public class ChatFormController {
    public TextField txtClientMessage;
    public Label lblUserName;
    public VBox chatListContext;
    public ScrollPane scrollPane;
    private Client client;

    public void initialize() throws IOException {
        lblUserName.setText(userName);
        txtClientMessage.requestFocus();
        client = new Client(new Socket(host,5000),userName);
        client.receiveMessageFromServer(chatListContext,scrollPane);
    }

    public void sendMassage(){
        String messageToSend = txtClientMessage.getText();
        if (!messageToSend.isEmpty()){
            HBox hBox =  new HBox();

            VBox vBox1=new VBox();
            Text user = new Text("you ");
            TextFlow userFlow = new TextFlow(user);
            vBox1.getChildren().add(userFlow);
            user.setFill(Color.WHITE);
            user.setStyle("-fx-font-size: 10");
            userFlow.setTextAlignment(TextAlignment.RIGHT);

            hBox.setAlignment(Pos.CENTER_RIGHT );
            hBox.setPadding(new Insets(5,10,5,10));
            Text text = new Text(messageToSend);
            TextFlow textFlow = new TextFlow(text);
//            textFlow.setStyle("-fx-background-color: #3a86ff;"+"-fx-background-radius: 10px");
            vBox1.setStyle("-fx-background-color: #3a86ff;"+"-fx-background-radius: 10px 0px 10px 10px");
            textFlow.setPadding(new Insets(5,10,5,10));
            text.setFill(Color.color(1,1,1));
            vBox1.getChildren().add(textFlow);
            hBox.getChildren().add(vBox1);
            chatListContext.getChildren().add(hBox);
            client.sendMessageToServer(userName+" : "+messageToSend);
            txtClientMessage.clear();
        }
        scrollPane.setVvalue(1.0);
    }

    public void sendOnAction(MouseEvent actionEvent) {
        sendMassage();
    }

    public void closeOnAction(MouseEvent mouseEvent) {
        System.exit(0);
    }

    public void uploadImageOnAction(MouseEvent mouseEvent) {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {

            HBox hBox =  new HBox();

            VBox vBox1=new VBox();
            Text user = new Text("you ");
            TextFlow userFlow = new TextFlow(user);
            vBox1.getChildren().add(userFlow);
            user.setFill(Color.WHITE);
            user.setStyle("-fx-font-size: 10");

            userFlow.setTextAlignment(TextAlignment.RIGHT);
            hBox.setAlignment(Pos.CENTER_RIGHT );
            vBox1.setPadding(new Insets(5,10,5,10));
            Image image = new Image("file:"+file.getAbsolutePath());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);

            vBox1.setStyle("-fx-background-color: #3a86ff;"+"-fx-background-radius: 10px 0px 10px 10px");
            vBox1.getChildren().add(imageView);
            hBox.getChildren().add(vBox1);
            chatListContext.getChildren().add(hBox);
            client.sendFileToServer(file, userName);
            System.out.println("send "+file.getName());
        }
    }

    public static void  addLabel(String messageFromServer,VBox vBox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        VBox vBox1=new VBox();
        hBox.setPadding(new Insets(5,10,5,10));

        Text text;
        Text user;
        try {
            user = new Text(" "+messageFromServer.split(":")[0]);
            text = new Text(messageFromServer.split(":")[1]);
        }catch (ArrayIndexOutOfBoundsException e){
            text = new Text(messageFromServer);
            user=new Text("server");
        }
        TextFlow userFlow = new TextFlow(user);
        vBox1.getChildren().add(userFlow);
        user.setFill(Color.BLUE);
        user.setStyle("-fx-font-size: 10");

        TextFlow textFlow = new TextFlow(text);
        vBox1.setStyle("-fx-background-color: #edf6f9;"+"-fx-background-radius: 0 10px 10px 10px");
        textFlow.setPadding(new Insets(5,10,5,10));
//        hBox.getChildren().add(textFlow);
        vBox1.getChildren().add(textFlow);
        hBox.getChildren().add(vBox1);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }

    public static void  addImage(File file,VBox vBox, String name){
        HBox hBox = new HBox();
        VBox vBox1 = new VBox();
        vBox1.setStyle("-fx-background-color: #edf6f9;"+"-fx-background-radius: 0 10px 10px 10px");
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(5,10,5,10));
        Text text = new Text(" "+name);
        text.setStyle("-fx-font-size: 10");
        text.setFill(Color.BLUE);
        vBox1.getChildren().add(text);
        Image image = new Image("file:"+file.getAbsolutePath());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        vBox1.setPadding(new Insets(5,10,5,10));
        vBox1.getChildren().add(imageView);
        hBox.getChildren().add(vBox1);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vBox.getChildren().add(hBox);
            }
        });
    }

    public void textFieldEnterOnAction(ActionEvent actionEvent) {
        sendMassage();
    }
}

