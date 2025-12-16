package org.example.repository;

import dbConnection.DBConnection;
import dto.TaskDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AppRepository {

    ObservableList<TaskDTO> completeTask = FXCollections.observableArrayList();

    public int getLastId() throws SQLException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT id FROM task ORDER BY id DESC LIMIT 1");
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("id")+1;
        }
        return 0;
    }

    public void addTask(TaskDTO taskDTO) throws SQLException {
        PreparedStatement preparedStatement = DBConnection.getInstance()
                .getConnection()
                .prepareStatement("INSERT INTO task (title, date, description) VALUES ( ? , ? , ? )");

        preparedStatement.setString(1, taskDTO.getTitle());
        preparedStatement.setString(2, taskDTO.getDate());
        preparedStatement.setString(3, taskDTO.getDescription());
        preparedStatement.executeUpdate();
    }

    public ObservableList<TaskDTO> getAllComplete() throws SQLException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM completeTask");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            completeTask.add(new TaskDTO(
                            resultSet.getInt("id"),
                            resultSet.getString("title"),
                            resultSet.getString("date"),
                            resultSet.getString("description")
                    )
            );
        }
        return completeTask;
    }

    public void getCompleteTask(List<Integer> intList) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        for (int i = 0; i < intList.size(); i++) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM task  where id = ?");
            System.out.println(intList.get(i));
            preparedStatement.setInt(1, intList.get(i));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                TaskDTO taskDTO = new TaskDTO(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("date"),
                        resultSet.getString("description")
                );
                addCompleteTask(taskDTO);
            }
        }
    }

    private void addCompleteTask(TaskDTO taskDTO) throws SQLException {
        PreparedStatement preparedStatement = DBConnection.getInstance()
                .getConnection()
                .prepareStatement("INSERT INTO completetask (title, date, description) VALUES ( ? , ? , ? )");

        preparedStatement.setString(1, taskDTO.getTitle());
        preparedStatement.setString(2, taskDTO.getDate());
        preparedStatement.setString(3, taskDTO.getDescription());
        preparedStatement.executeUpdate();
    }

    public void deleteHistory(TaskDTO selectedItem) throws SQLException {
        PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM completeTask WHERE id = ?");
        preparedStatement.setInt(1,selectedItem.getId());
        preparedStatement.executeUpdate();
    }
}
