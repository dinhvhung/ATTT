package com.example.demoattt;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model cho một bản ghi điểm. Dùng JavaFX property để TableView tự cập nhật.
 */
public class Score {
    private final StringProperty maSV = new SimpleStringProperty();
    private final StringProperty hoTen = new SimpleStringProperty();
    private final StringProperty monHoc = new SimpleStringProperty();
    private final DoubleProperty diem = new SimpleDoubleProperty();

    public Score(String maSV, String hoTen, String monHoc, double diem) {
        this.maSV.set(maSV);
        this.hoTen.set(hoTen);
        this.monHoc.set(monHoc);
        this.diem.set(diem);
    }

    public String getMaSV() { return maSV.get(); }
    public void setMaSV(String value) { maSV.set(value); }
    public StringProperty maSVProperty() { return maSV; }

    public String getHoTen() { return hoTen.get(); }
    public void setHoTen(String value) { hoTen.set(value); }
    public StringProperty hoTenProperty() { return hoTen; }

    public String getMonHoc() { return monHoc.get(); }
    public void setMonHoc(String value) { monHoc.set(value); }
    public StringProperty monHocProperty() { return monHoc; }

    public double getDiem() { return diem.get(); }
    public void setDiem(double value) { diem.set(value); }
    public DoubleProperty diemProperty() { return diem; }

    /** Xếp loại được tính tự động từ điểm. */
    public String getXepLoai() {
        double d = diem.get();
        if (d >= 8.0) return "Giỏi";
        if (d >= 6.5) return "Khá";
        if (d >= 5.0) return "Trung bình";
        return "Yếu";
    }
}
