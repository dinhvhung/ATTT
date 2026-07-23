module com.example.demoattt {
    requires transitive javafx.base;
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.demoattt to javafx.fxml;
    exports com.example.demoattt;
}