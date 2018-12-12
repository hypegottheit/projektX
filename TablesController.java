package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class TablesController {
    @FXML
    private TextArea res;
    @FXML
    private TableView functions;
    @FXML
    private TableColumn<Lexem, Integer> id;
    @FXML
    private TableColumn<Lexem, String> val;
    private ObservableList<Lexem> funData = FXCollections.observableArrayList();

    @FXML
    private TableView tablims;
    @FXML
    private TableColumn<Lexem, Integer> id1;
    @FXML
    private TableColumn<Lexem, String> val1;
    private ObservableList<Lexem> limData = FXCollections.observableArrayList();

    @FXML
    private TableView names;
    @FXML
    private TableColumn<Lexem, Integer> id2;
    @FXML
    private TableColumn<Lexem, String> val2;
    @FXML
    private TableColumn<Lexem, String> type2;
    private ObservableList<Lexem> nameData = FXCollections.observableArrayList();

    @FXML
    private TableView digits;
    @FXML
    private TableColumn<Digit, Integer> id3;
    @FXML
    private TableColumn<Digit, String> val3;
    @FXML
    private TableColumn<Digit, String> binval3;
    @FXML
    private TableColumn<Digit, String> type3;
    private ObservableList<Digit> digData = FXCollections.observableArrayList();

    public void create(Table table) {
        StringBuilder sb = new StringBuilder();
        List<Lexem> m = table.res;
        for (Lexem s : m) {
            sb.append(s.print() + "; ");
        }
        res.setText(sb.toString());
        viewTab(table.functionWords);
        viewTab1(table.limiters);
        viewTab2(table.varnames);
        viewTab3(table.digits);
    }

    public void viewTab(ArrayList<String> table) {
        id.setCellValueFactory(new PropertyValueFactory<Lexem, Integer>("num"));
        val.setCellValueFactory(new PropertyValueFactory<Lexem, String>("value"));
        functions.setItems(funData);
        for (int i = 0; i < table.size(); i++) {
            funData.add(new Lexem(i, table.get(i)));
        }
    }
    public void viewTab1(ArrayList<String> table){
        id1.setCellValueFactory(new PropertyValueFactory<Lexem, Integer>("num"));
        val1.setCellValueFactory(new PropertyValueFactory<Lexem, String>("value"));
        tablims.setItems(limData);
        for (int i = 0; i < table.size(); i++) {
            limData.add(new Lexem(i,table.get(i)));
        }
    }
    public void viewTab2(ArrayList<Lexem> table){
        id2.setCellValueFactory(new PropertyValueFactory<Lexem, Integer>("num"));
        val2.setCellValueFactory(new PropertyValueFactory<Lexem, String>("value"));
        type2.setCellValueFactory(new PropertyValueFactory<Lexem, String>("t"));
        names.setItems(nameData);
        for (int i = 0; i < table.size(); i++) {
            nameData.add(new Lexem(i, table.get(i).getValue(), table.get(i).getT()));
        }
    }
    public void viewTab3(ArrayList<Digit> table){
        id3.setCellValueFactory(new PropertyValueFactory<Digit, Integer>("num"));
        val3.setCellValueFactory(new PropertyValueFactory<Digit, String>("stack"));
        binval3.setCellValueFactory(new PropertyValueFactory<Digit, String>("value"));
        type3.setCellValueFactory(new PropertyValueFactory<Digit, String>("t"));
        digits.setItems(digData);
        for (int i = 0; i < table.size(); i++) {
            digData.add(new Digit(i, table.get(i).getValue(), table.get(i).getStack(), table.get(i).getT()));
        }
    }
}
