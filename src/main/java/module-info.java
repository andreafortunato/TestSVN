module BikersLand {
    requires transitive javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires org.controlsfx.controls;
	requires javafx.base;
	requires transitive java.sql;
	requires javafx.swing;
	requires java.desktop;
	requires commons.validator;
	requires jakarta.servlet;

    opens com.bikersland to javafx.fxml;
    opens com.bikersland.controller.graphics to javafx.fxml;
    opens com.bikersland.utility to javafx.fxml;
    
    exports com.bikersland;
    exports com.bikersland.bean;
}