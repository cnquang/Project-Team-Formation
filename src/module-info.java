module ProjectTeamFormation {
	requires javafx.controls;
	requires junit;
	requires javafx.graphics;
	requires javafx.base;
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml;
}
