package org.example.service;

import dto.TaskDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import repository.AppRepository;

import java.sql.SQLException;
import java.util.List;

public class AppService {

    AppRepository taskRepository = new AppRepository();
    ObservableList<TaskDTO> completeTask = FXCollections.observableArrayList();

    public int getNextIndex() {
        int index = 0;
        try {
            index = taskRepository.getLastId();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
        return index;
    }

    public ObservableList<TaskDTO> getAllComplete() {
        try {
            completeTask = taskRepository.getAllComplete();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
        return completeTask;
    }

    public void addTask(TaskDTO taskDTO) {
        try {
            taskRepository.addTask(taskDTO);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    public void getCompleteTask(List<Integer> intList) {
        try {
            taskRepository.getCompleteTask(intList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.WARNING, e.getMessage()).show();
        }
    }

    public ObservableList<TaskDTO> deleteHistory(TaskDTO selectedItem) {
        try {
            taskRepository.deleteHistory(selectedItem);
            completeTask.remove(selectedItem);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return completeTask;
    }
}
