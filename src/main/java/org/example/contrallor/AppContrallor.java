package org.example.contrallor;

import dto.TaskDTO;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import service.AppService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AppContrallor implements Initializable {

    AppService taskService = new AppService();
    ObservableList<TaskDTO> taskDTOObservableList = taskService.getAllComplete();
    int x = taskService.getNextIndex();
    int gridIndex = 1;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colTitle;

    @FXML
    private GridPane gridTask;

    @FXML
    private Label lblTask;

    @FXML
    private TableView<TaskDTO> tblTask;

    @FXML
    private DatePicker txtDate;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtTitle;

    @FXML
    private AnchorPane taskPane;

    @FXML
    private AnchorPane completeTaskPane;

    @FXML
    private Button completeTaskBtn;

    @FXML
    private Button taskBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tblTask.setItems(taskDTOObservableList);
    }

    @FXML
    void btnAddTask(ActionEvent event) {
        TaskDTO taskDTO = new TaskDTO(
                1,
                txtTitle.getText(),
                String.valueOf(txtDate.getValue()),
                txtDescription.getText()
        );

        taskService.addTask(taskDTO);
        System.out.println(taskDTO);
        lblTask.setText("");
        CheckBox checkBox = new CheckBox(txtTitle.getText());
        checkBox.setStyle(
                "-fx-font-size: 20px;" +
                        "-fx-padding: 5;" +
                        "-fx-background-color: transparent;"
        );
        checkBox.setId(x+"");
        gridTask.add(checkBox,0,gridIndex);
        gridIndex++;
        x++;
    }

    @FXML
    void btnCompleteTask(ActionEvent event) {
        taskPane.setVisible(false);
        completeTaskPane.setVisible(true);
        taskBtn.setStyle("-fx-background-color:  transparent;"+"-fx-text-fill: black");
        completeTaskBtn.setStyle("-fx-background-color:  #1e5a8f;"+"-fx-text-fill: white");
        List<Node> removeList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();

        for (Node node : gridTask.getChildren()) {
            if (node instanceof CheckBox checkBox) {
                if (checkBox.isSelected()) {
                    removeList.add(node);
                    intList.add(Integer.valueOf(checkBox.getId()));
                }
            }
        }
        taskService.getCompleteTask(intList);
        gridTask.getChildren().removeAll(removeList);
        taskDTOObservableList.clear();
        taskDTOObservableList = taskService.getAllComplete();
        tblTask.refresh();
        if (gridTask.getChildren().isEmpty()) {
            lblTask.setText("Not Select Task");
        }
    }

    @FXML
    void btnDelete(ActionEvent event) {
        TaskDTO selectedItem = tblTask.getSelectionModel().getSelectedItem();
        taskDTOObservableList = taskService.deleteHistory(selectedItem);
        tblTask.refresh();
    }

    @FXML
    void btnTaskAction(ActionEvent event) {
        taskPane.setVisible(true);
        completeTaskPane.setVisible(false);
        taskBtn.setStyle("-fx-background-color:  #1e5a8f;"+"-fx-text-fill: white");
        completeTaskBtn.setStyle("-fx-background-color:  transparent;"+"-fx-text-fill: black");
    }

}
