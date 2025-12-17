package org.example.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.example.dto.TaskDTO;
import org.example.repository.AppRepository;

import java.sql.SQLException;
import java.util.List;

public class AppService {
    AppRepository taskRepository = new AppRepository();

    public List<TaskDTO> loadPending() {
        try { return taskRepository.getAllPendingTasks(); }
        catch (SQLException e) { return null; }
    }

    public ObservableList<TaskDTO> getAllComplete() {
        try { return taskRepository.getAllComplete(); }
        catch (SQLException e) { return FXCollections.observableArrayList(); }
    }

    public void addTask(TaskDTO taskDTO) {
        try { taskRepository.addTask(taskDTO); }
        catch (SQLException e) { new Alert(Alert.AlertType.ERROR, e.getMessage()).show(); }
    }

    public void getCompleteTask(List<Integer> intList) {
        try { taskRepository.getCompleteTask(intList); }
        catch (SQLException e) { new Alert(Alert.AlertType.ERROR, e.getMessage()).show(); }
    }

    public ObservableList<TaskDTO> deleteHistory(TaskDTO selectedItem) {
        try {
            taskRepository.deleteHistory(selectedItem);
            return getAllComplete();
        } catch (SQLException e) { return getAllComplete(); }
    }
}