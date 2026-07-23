package com.example.demoattt;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Màn hình phôi "Quản lý điểm" - chỉ dựng giao diện, dữ liệu để trong bộ nhớ,
 * không kết nối backend/CSDL.
 */
public class ScoreController implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private Label countLabel;
    @FXML private Label statusLabel;
    @FXML private TextField searchField;

    @FXML private TableView<Score> scoreTable;
    @FXML private TableColumn<Score, Void> sttCol;
    @FXML private TableColumn<Score, String> maSVCol;
    @FXML private TableColumn<Score, String> hoTenCol;
    @FXML private TableColumn<Score, String> monHocCol;
    @FXML private TableColumn<Score, Number> diemCol;
    @FXML private TableColumn<Score, String> xepLoaiCol;

    @FXML private TextField maSVField;
    @FXML private TextField hoTenField;
    @FXML private ComboBox<String> monHocCombo;
    @FXML private TextField diemField;

    /** Nguồn dữ liệu gốc (in-memory). */
    private final ObservableList<Score> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Cột STT đánh số theo dòng đang hiển thị.
        sttCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        // Dùng lambda thay cho PropertyValueFactory để tránh reflection (lỗi module).
        maSVCol.setCellValueFactory(c -> c.getValue().maSVProperty());
        hoTenCol.setCellValueFactory(c -> c.getValue().hoTenProperty());
        monHocCol.setCellValueFactory(c -> c.getValue().monHocProperty());
        diemCol.setCellValueFactory(c -> c.getValue().diemProperty());
        xepLoaiCol.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getXepLoai()));

        // Gợi ý môn học.
        monHocCombo.getItems().addAll(
                "An toàn thông tin", "Cơ sở dữ liệu", "Lập trình Java",
                "Mạng máy tính", "Hệ điều hành");

        // Dữ liệu mẫu cho màn hình phôi.
        data.addAll(
                new Score("SV001", "Nguyễn Văn An", "An toàn thông tin", 8.5),
                new Score("SV002", "Trần Thị Bình", "Cơ sở dữ liệu", 7.0),
                new Score("SV003", "Lê Hoàng Cường", "Lập trình Java", 9.0),
                new Score("SV004", "Phạm Thu Dung", "Mạng máy tính", 5.5),
                new Score("SV005", "Vũ Minh Đức", "An toàn thông tin", 4.0));

        // Tìm kiếm theo Mã SV / Họ tên / Môn học.
        FilteredList<Score> filtered = new FilteredList<>(data, s -> true);
        searchField.textProperty().addListener((obs, oldV, newV) -> {
            String q = newV == null ? "" : newV.trim().toLowerCase();
            filtered.setPredicate(s -> q.isEmpty()
                    || s.getMaSV().toLowerCase().contains(q)
                    || s.getHoTen().toLowerCase().contains(q)
                    || s.getMonHoc().toLowerCase().contains(q));
        });
        scoreTable.setItems(filtered);
        countLabel.textProperty().bind(Bindings.size(filtered).asString("Số bản ghi: %d"));

        // Chọn một dòng -> đổ dữ liệu lên form để xem/sửa.
        scoreTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, sel) -> {
            if (sel != null) {
                maSVField.setText(sel.getMaSV());
                hoTenField.setText(sel.getHoTen());
                monHocCombo.setValue(sel.getMonHoc());
                monHocCombo.getEditor().setText(sel.getMonHoc());
                diemField.setText(formatDiem(sel.getDiem()));
            }
        });
    }

    /** Hiển thị tài khoản đã đăng nhập (truyền sang từ màn hình Login). */
    public void setUser(String email) {
        if (email != null && !email.isEmpty()) {
            welcomeLabel.setText("Xin chào, " + email);
        }
    }

    @FXML
    private void onAdd(ActionEvent event) {
        String ma = text(maSVField);
        String ten = text(hoTenField);
        String mon = text(monHocCombo.getEditor());
        if (ma.isEmpty() || ten.isEmpty() || mon.isEmpty()) {
            warn("Vui lòng nhập đầy đủ Mã SV, Họ tên và Môn học.");
            return;
        }
        Double diem = parseDiem();
        if (diem == null) {
            warn("Điểm không hợp lệ. Nhập số trong khoảng 0 - 10.");
            return;
        }
        data.add(new Score(ma, ten, mon, diem));
        clearForm();
        info("Đã thêm điểm cho \"" + ten + "\".");
    }

    @FXML
    private void onUpdate(ActionEvent event) {
        Score sel = scoreTable.getSelectionModel().getSelectedItem();
        if (sel == null) {
            warn("Hãy chọn một dòng trong bảng để cập nhật.");
            return;
        }
        String ma = text(maSVField);
        String ten = text(hoTenField);
        String mon = text(monHocCombo.getEditor());
        if (ma.isEmpty() || ten.isEmpty() || mon.isEmpty()) {
            warn("Vui lòng nhập đầy đủ Mã SV, Họ tên và Môn học.");
            return;
        }
        Double diem = parseDiem();
        if (diem == null) {
            warn("Điểm không hợp lệ. Nhập số trong khoảng 0 - 10.");
            return;
        }
        sel.setMaSV(ma);
        sel.setHoTen(ten);
        sel.setMonHoc(mon);
        sel.setDiem(diem);
        scoreTable.refresh(); // Cập nhật lại cột "Xếp loại" (giá trị dẫn xuất).
        info("Đã cập nhật điểm cho \"" + ten + "\".");
    }

    @FXML
    private void onDelete(ActionEvent event) {
        Score sel = scoreTable.getSelectionModel().getSelectedItem();
        if (sel == null) {
            warn("Hãy chọn một dòng trong bảng để xóa.");
            return;
        }
        data.remove(sel);
        clearForm();
        info("Đã xóa bản ghi.");
    }

    @FXML
    private void onClear(ActionEvent event) {
        scoreTable.getSelectionModel().clearSelection();
        clearForm();
        statusLabel.setText("");
    }

    @FXML
    private void onLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(LoginApplication.class.getResource("login.fxml"));
            Scene scene = new Scene(loader.load(), 320, 240);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Đăng nhập");
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            warn("Không thể quay lại màn hình đăng nhập.");
        }
    }

    // ----------------- Helpers -----------------

    private void clearForm() {
        maSVField.clear();
        hoTenField.clear();
        monHocCombo.setValue(null);
        monHocCombo.getEditor().clear();
        diemField.clear();
    }

    private Double parseDiem() {
        try {
            double d = Double.parseDouble(diemField.getText().trim().replace(",", "."));
            return (d < 0 || d > 10) ? null : d;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String formatDiem(double d) {
        return d == Math.floor(d) ? String.valueOf((int) d) : String.valueOf(d);
    }

    private String text(TextField field) {
        return field.getText() == null ? "" : field.getText().trim();
    }

    private void warn(String msg) {
        statusLabel.setTextFill(Color.web("#d32f2f"));
        statusLabel.setText(msg);
    }

    private void info(String msg) {
        statusLabel.setTextFill(Color.web("#2e7d32"));
        statusLabel.setText(msg);
    }
}
