package sample;

import javafx.application.Application;
import javafx.beans.binding.IntegerBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Компилятор");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();
        int s = -5;
        System.out.println(Integer.toBinaryString(-5));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
