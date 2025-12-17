package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.example.dto.TaskDTO;
import org.example.service.AppService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AppContrallor implements Initializable {

    AppService taskService = new AppService();
    int gridIndex = 0;

    @FXML private TableColumn<TaskDTO, String> colDate, colDescription, colTitle;
    @FXML private TableView<TaskDTO> tblTask;
    @FXML private GridPane gridTask;
    @FXML private DatePicker txtDate;
    @FXML private TextField txtDescription, txtTitle;
    @FXML private AnchorPane taskPane, completeTaskPane;
    @FXML private Button completeTaskBtn, taskBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        loadAllFromDB();
    }

    private void loadAllFromDB() {
        tblTask.setItems(taskService.getAllComplete());

        gridTask.getChildren().clear();
        gridIndex = 0;
        List<TaskDTO> pendingTasks = taskService.loadPending();
        if (pendingTasks != null) {
            for (TaskDTO task : pendingTasks) {
                addCheckBoxToGrid(task.getTitle(), String.valueOf(task.getId()));
            }
        }
    }

    private void addCheckBoxToGrid(String title, String id) {
        CheckBox checkBox = new CheckBox(title);
        checkBox.setId(id);
        checkBox.setStyle("-fx-font-size: 18px; -fx-padding: 5;");
        gridTask.add(checkBox, 0, gridIndex++);
    }

    @FXML
    void btnAddTask(ActionEvent event) {
        TaskDTO taskDTO = new TaskDTO(0, txtTitle.getText(), String.valueOf(txtDate.getValue()), txtDescription.getText());
        taskService.addTask(taskDTO);
        loadAllFromDB();
    }

    @FXML
    void btnCompleteTask(ActionEvent event) {
        List<Integer> selectedIds = new ArrayList<>();
        for (Node node : gridTask.getChildren()) {
            if (node instanceof CheckBox cb && cb.isSelected()) {
                selectedIds.add(Integer.parseInt(cb.getId()));
            }
        }
        taskService.getCompleteTask(selectedIds);
        loadAllFromDB();

        taskPane.setVisible(false);
        completeTaskPane.setVisible(true);
    }

    @FXML
    void btnDelete(ActionEvent event) {
        TaskDTO selected = tblTask.getSelectionModel().getSelectedItem();
        if (selected != null) {
            taskService.deleteHistory(selected);
            loadAllFromDB();
        }
    }

    @FXML
    void btnTaskAction(ActionEvent event) {
        taskPane.setVisible(true);
        completeTaskPane.setVisible(false);
    }
}