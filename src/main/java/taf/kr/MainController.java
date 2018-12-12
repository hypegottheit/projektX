package taf.kr;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private Button graphBtn;
    @FXML
    private Button tablesBtn;
    @FXML
    private TextArea code;
    @FXML
    private TextArea errorTextArea;

    Node node;
    Table tables;

    @FXML
    public void onClickMethod() {
        try {
            String read = code.getText().replaceAll("\n", " ").replaceAll("\t", " ");
            tables = new Table();
            LA m = new LA();
            m.analyze(read, tables);
            SA syntaxAnalyzer = new SA();
            node = syntaxAnalyzer.analyze(tables.res, tables.varnames);
            errorTextArea.setText("Успешно!");
            graphBtn.setDisable(false);
            tablesBtn.setDisable(false);
        } catch (Exception ex) {
            errorTextArea.setText(ex.getMessage());
            graphBtn.setDisable(true);
            tablesBtn.setDisable(true);
            ex.printStackTrace();
        }
    }

    @FXML
    public void viewTablesMethod() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../resources/tables.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
            TablesController tablesController = fxmlLoader.getController();
            tablesController.create(tables);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void viewGraphMethod() {
        node.out();
    }
}
