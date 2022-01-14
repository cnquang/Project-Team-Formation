package application;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class GUI extends Application{

	private GUIview view = new GUIview();
	
	@Override // Override the start method from the superclass
	public void start(Stage primaryStage) {
		try {			
			BorderPane pane = new BorderPane();
			
			pane.setTop(view.fiveTeams());
			pane.setLeft(view.stuID());
			pane.setCenter(view.display());
			pane.setRight(view.listButtons());
			pane.setBottom(view.barchart());
			
			Scene scene = new Scene(pane);
			
			primaryStage.setTitle("Forming Project Teams");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch( Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
