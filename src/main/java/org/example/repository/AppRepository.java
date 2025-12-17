package org.example.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.dbConnection.DBConnection;
import org.example.dto.TaskDTO;

import java.sql.*;
import java.util.List;

public class AppRepository {

    public List<TaskDTO> getAllPendingTasks() throws SQLException {
        ObservableList<TaskDTO> list = FXCollections.observableArrayList();
        Connection connection = DBConnection.getInstance().getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM task");
        while (resultSet.next()) {
            list.add(new TaskDTO(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getString("date"), resultSet.getString("description")));
        }
        return list;
    }

    public ObservableList<TaskDTO> getAllComplete() throws SQLException {
        ObservableList<TaskDTO> completeList = FXCollections.observableArrayList();
        Connection connection = DBConnection.getInstance().getConnection();
        ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM completetask");
        while (resultSet.next()) {
            completeList.add(new TaskDTO(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getString("date"), resultSet.getString("description")));
        }
        return completeList;
    }

    public void addTask(TaskDTO taskDTO) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO task (title, date, description) VALUES (?, ?, ?)");
        pstm.setString(1, taskDTO.getTitle());
        pstm.setString(2, taskDTO.getDate());
        pstm.setString(3, taskDTO.getDescription());
        pstm.executeUpdate();
    }

    public void getCompleteTask(List<Integer> intList) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        for (Integer id : intList) {
            PreparedStatement selectPstm = connection.prepareStatement("SELECT * FROM task WHERE id = ?");
            selectPstm.setInt(1, id);
            ResultSet resultSet = selectPstm.executeQuery();
            if (resultSet.next()) {
                TaskDTO task = new TaskDTO(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getString("date"), resultSet.getString("description"));

                PreparedStatement insertPstm = connection.prepareStatement("INSERT INTO completetask (title, date, description) VALUES (?, ?, ?)");
                insertPstm.setString(1, task.getTitle());
                insertPstm.setString(2, task.getDate());
                insertPstm.setString(3, task.getDescription());
                insertPstm.executeUpdate();

                PreparedStatement deletePstm = connection.prepareStatement("DELETE FROM task WHERE id = ?");
                deletePstm.setInt(1, id);
                deletePstm.executeUpdate();
            }
        }
    }

    public void deleteHistory(TaskDTO selectedItem) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM completetask WHERE id = ?");
        pstm.setInt(1, selectedItem.getId());
        pstm.executeUpdate();
    }

    public int getLastId() throws SQLException {
        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT id FROM task ORDER BY id DESC LIMIT 1");
        return resultSet.next() ? resultSet.getInt("id") + 1 : 1;
    }
}