package com.example.demoattt;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.*;

public class LoginController {
    String dbURL = "jdbc:mysql://localhost:3306/attt";
    String user = "root";
    String pass = "1234";

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text notificationText;

    @FXML
    private Button loginBtn;

    @FXML
    void onLogin (ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbURL, user, pass);
            // 1.1) Kịch bản bị In-band SQLi (dùng admin' or '1=1 và admin' -- )
            // 1.2) Inferential SQLi: Boolean-based SQLi
            // ' OR (EXISTS(select 1 from userinfo where email='admin' and substring(password,1,1)='a')) --
            String sql = "select * from userinfo where email = '" + emailField.getText() + "' and password = '" + passwordField.getText() + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
//
//            // 1.3) Time-based SQLi:
//            // ' OR IF(EXISTS(select 1 from userinfo where email='admin' and substring(password,1,1)='a'), SLEEP(5), 0) --
//            String sql = "select * from userinfo where email = '" + emailField.getText() + "' and password = '" + passwordField.getText() + "'";
//            Statement statement = connection.createStatement();
//            long start = System.currentTimeMillis();
//            ResultSet resultSet = statement.executeQuery(sql);
//            long end = System.currentTimeMillis();
//            System.out.println("Response time: " + (end - start) + " ms");

//            // 2) Kịch bản chống SQLi
//            // 2.1) Validate & Sanitize input
//            if (email.contains("'") || password.contains("'") || email.contains(",") || password.contains(",") ||
//                    email.contains("--") || password.contains("--")) {
//                notificationText.setText("Email hoặc mật khẩu không hợp lệ!");
//                return;
//            }

//            // 2.2) PreparedStatement
//            String sql = "select * from userinfo where email = ? and password = ?";
//            PreparedStatement preparedStatement = connection.prepareStatement(sql);
//            preparedStatement.setString(1, emailField.getText());
//            preparedStatement.setString(2, passwordField.getText());
//            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                notificationText.setText("Thành công");
            }
            else {
                notificationText.setText("Thất bại");
            }
            resultSet.close();
            connection.close();
        } catch (SQLException e) {
//            // In lỗi trên màn hình để tạo lỗi SQLi
//            notificationText.setText("Error: " + e.getMessage());

            // Ẩn lỗi
            notificationText.setText("Sai tài khoản hoặc mật khẩu");
        }


    }
}
