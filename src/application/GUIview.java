package application;

import java.util.*;

import controller.GUIcontroller;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;

public class GUIview {
	private GUIcontroller control = new GUIcontroller();
	
	//TextField and checkBox will count from 1 to 20 (e.g from 1 to 4: team 1)
	private HashMap<Integer, TextField> textField = new HashMap<Integer, TextField>();
	private HashMap<Integer, CheckBox> checkBox = new HashMap<Integer, CheckBox>();
	private int index = 1;
	//Data for bar chart
	private ObservableList<String> dataLeader, dataStu, dataTeam, dataProject;
	private BarChart<String, Number> bcAverage, bcPercentage, bcShortfall;
	private Label lbSDAve = new Label();
	private Label lbSDPer = new Label();
	private Label lbSDGap = new Label();
	private Text txtStudent = new Text();
	private TextField txtID;
	
	// Create each team in a box with 4 textFields and 4 checkBoxes, index from 1 to 20
	private GridPane teamBox(int teamNum, String color) {
		// Create a pane and set its properties
		GridPane pane = new GridPane();
		
		Text t = new Text(20, 20, "Team " + teamNum);
		t.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 15));
		GridPane.setHalignment(t, HPos.CENTER);
				
		TextField tf1 = new TextField();
		TextField tf2 = new TextField();
		TextField tf3 = new TextField();
		TextField tf4 = new TextField();
		
		CheckBox cb1 = new CheckBox();
		CheckBox cb2 = new CheckBox();
		CheckBox cb3 = new CheckBox();
		CheckBox cb4 = new CheckBox();
		
		textField.put(index, tf1); 
		checkBox.put(index, cb1);
		index++;
		textField.put(index, tf2); 
		checkBox.put(index, cb2);
		index++;
		textField.put(index, tf3); 
		checkBox.put(index, cb3);
		index++;
		textField.put(index, tf4);
		checkBox.put(index, cb4);
		index++;
		
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(15, 15, 15, 15)); //top, right, bottom, left
		pane.setHgap(20);
		pane.setVgap(10);
		pane.add(t, 0, 0);
		pane.add(tf1, 0, 1);
		pane.add(cb1, 1, 1);
		pane.add(tf2, 0, 2);
		pane.add(cb2, 1, 2);
		pane.add(tf3, 0, 3);
		pane.add(cb3, 1, 3);
		pane.add(tf4, 0, 4);
		pane.add(cb4, 1, 4);
				
		pane.setStyle("-fx-border-color: blue; -fx-background-color: " + color + ";");
		
		return pane;
	}
	
	// Create list of five teams' box
	public HBox fiveTeams() {
		HBox pane = new HBox(45);
		pane.setFillHeight(true);
		pane.setAlignment(Pos.CENTER);
		pane.setFillHeight(true);
		pane.setPadding(new Insets(15, 15, 15, 15)); //top, right, bottom, left
		// Add all 5 team boxes
		pane.getChildren().addAll(teamBox(1, "gold"), teamBox(2, "violet"), 
				teamBox(3, "aqua"), teamBox(4, "coral"), teamBox(5, "lime"));
		pane.alignmentProperty();
		
		//if file formTeam.dat exist we will fill all the textFields with studentID
		if (control.getAvailableStus().size() == 0) {
			int k = 1;
			for (int i = 0; i < control.getFormTeam().size(); i++) {
				for (String key : control.getFormTeam().get(i).getTeamMembers().keySet()) {
					textField.get(k).setText(key);
					k++;
				}
			}
		}
		return pane;
	}
	
	// Create Student ID + textFields + buttons (Add, Delete, Get, Done)
	public GridPane stuID() {
		
		GridPane pane = new GridPane();
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(5, 5, 5, 5));
		pane.setHgap(5);
		pane.setVgap(5);
		
		Text t = new Text(20, 20, "Student ID ");
		t.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 15));
		
		txtID = new TextField();
		
		//Add student to a team
		Button Add = new Button("Add");
		Add.setOnAction(e -> {
			control.addStudent(txtID.getText(), textField, checkBox);
			control.updateAvailableList(dataStu, dataLeader, dataTeam);
			control.updateBarchart(bcAverage, bcPercentage, bcShortfall, lbSDAve, lbSDPer, lbSDGap);
		});
		
		//Remove students from teams
		Button Delete = new Button("Delete");
		Delete.setOnAction(e -> {
			control.deleteStudent(textField, checkBox);
			control.updateAvailableList(dataStu, dataLeader, dataTeam);
		});
		
		//Get student details from textField via student ID
		Button Get = new Button("Get");
		Get.setOnAction(e -> {
			control.getStudent(txtID.getText(), txtStudent);
			txtStudent.setFont(Font.font("verdana", FontWeight.LIGHT, FontPosture.ITALIC, 10));
			txtStudent.setFill(Color.BLUEVIOLET);
		});
		
		//Save and print data
		Button done = new Button("Done");
		done.setOnAction(e -> {
			control.print();
		});

		pane.add(t, 0, 0);
		pane.add(txtID, 1, 0);
		pane.add(autoSizeButton(Add), 1, 1);
		pane.add(autoSizeButton(Delete), 0, 1);
		pane.add(autoSizeButton(Get), 0, 2);
		pane.add(txtStudent, 1, 2);
		pane.add(autoSizeButton(done), 0, 3);
		return pane;
	}
	
	//Swap + Suggest + Auto Swap + Redo + Undo + Print buttons
	public VBox listButtons() {
		VBox vb = new VBox(0);
		
		//Swap 2 students are choose
		Button swap = new Button("Swap");
		swap.setOnAction(e -> {
			control.swapStudent(textField, checkBox);
			control.updateAvailableList(dataStu, dataLeader, dataTeam);
			control.updateBarchart(bcAverage, bcPercentage, bcShortfall, lbSDAve, lbSDPer, lbSDGap);
		});
		
		//suggest manager for swap students to reduce the gap between different teams for all metrics
		Button suggest = new Button("Suggest");
		suggest.setOnAction(e -> {
			control.suggest(textField);
		});
		
		Button autoSwap = new Button("Auto Swap");
			autoSwap.setOnAction(e -> {
			control.autoSwap(textField);
			control.updateAvailableList(dataStu, dataLeader, dataTeam);
			control.updateBarchart(bcAverage, bcPercentage, bcShortfall, lbSDAve, lbSDPer, lbSDGap);
		});
		//Undo after swaps
		Button undo = new Button("Undo");
		undo.setOnAction(e -> {
			control.undo(textField);
			control.updateAvailableList(dataStu, dataLeader, dataTeam);
			control.updateBarchart(bcAverage, bcPercentage, bcShortfall, lbSDAve, lbSDPer, lbSDGap);
		});
		
		//Redo after undoes
		Button redo = new Button("Redo");
		redo.setOnAction(e -> {
			control.redo(textField);
			control.updateAvailableList(dataStu, dataLeader, dataTeam);
			control.updateBarchart(bcAverage, bcPercentage, bcShortfall, lbSDAve, lbSDPer, lbSDGap);
		});
		
		vb.getChildren().addAll(autoSizeButton(swap), autoSizeButton(suggest), autoSizeButton(autoSwap), 
				autoSizeButton(undo), autoSizeButton(redo));
		return vb;
	}

	// button change size depend on size of node
	public AnchorPane autoSizeButton(Button b) {
		AnchorPane pane = new AnchorPane();
		AnchorPane.setTopAnchor(b, 2.0);
		AnchorPane.setBottomAnchor(b, 2.0);
		AnchorPane.setLeftAnchor(b, 2.0);
		AnchorPane.setRightAnchor(b, 2.0);
		pane.getChildren().add(b);
		return pane;
	}

	// Display both List Project details + Available Students + List Available Leaders + List Team members
	public HBox display() {
		HBox hb = new HBox(15);
		hb.setAlignment(Pos.CENTER);
		hb.setPadding(new Insets(2, 2, 2, 2));
		hb.getChildren().addAll(displayListProject(),displayListTeam(), displayAvailableStu(), displayLeaderStu());
		return hb;
	}
	
	//List Available Students update immediately 
	public VBox displayAvailableStu(){
		VBox vbox = new VBox();
		
		ListView<String> listStu = new ListView<String>();
	    dataStu = FXCollections.observableArrayList(control.getAvailableStus().keySet());
	    listStu.setItems(dataStu);
	    //Add student to a team
	    listStu.setOnMouseClicked(x -> {
	    	if (listStu.getSelectionModel().getSelectedItem() != null) {
	    		control.addStudent(listStu.getSelectionModel().getSelectedItem(), textField, checkBox);
		    	control.updateAvailableList(dataStu, dataLeader, dataTeam);
		    	control.updateBarchart(bcAverage, bcPercentage, bcShortfall, lbSDAve, lbSDPer, lbSDGap);
	    	}
	    });
	    
	    Label l = new Label("List Available Students");
	    l.setTextFill(Color.RED);
	    l.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 15));
	    
		vbox.getChildren().addAll(l, listStu);
		return vbox;
	}
	
	//List Leader Students type update immediately 
	public VBox displayLeaderStu(){
		VBox vbox = new VBox();

	    ListView<String> listLeader = new ListView<String>();
	    dataLeader = FXCollections.observableArrayList(control.getLeaders());
	    listLeader.setItems(dataLeader);
	    //Add student with leader type to a team
	    listLeader.setOnMouseClicked(x -> {
	    	if (listLeader.getSelectionModel().getSelectedItem()!= null) {
	    		control.addStudent(listLeader.getSelectionModel().getSelectedItem(), textField, checkBox);
			    control.updateAvailableList(dataStu, dataLeader, dataTeam);
			    control.updateBarchart(bcAverage, bcPercentage, bcShortfall, lbSDAve, lbSDPer, lbSDGap);
	    	}
	    });
	    
	    Label l = new Label("List Available Leaders");
	    l.setTextFill(Color.RED);
	    l.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 15));
	    
		vbox.getChildren().addAll(l, listLeader);
		
		return vbox;
	}
	
	//List Form Team update immediately when add or delete student
	public VBox displayListTeam(){
		VBox vbox = new VBox();

	    ListView<String> listTeam = new ListView<String>();
	    dataTeam = FXCollections.observableArrayList(control.getListTeams());
	    listTeam.setItems(dataTeam);
	    
	    Label l = new Label("List Student in each Team");
	    l.setTextFill(Color.RED);
	    l.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 15));
	    
		vbox.getChildren().addAll(l, listTeam);
		return vbox;
	}
	
	//List Projects with main details
	public VBox displayListProject(){
		VBox vbox = new VBox();
		
		ListView<String> listProject = new ListView<String>();
		dataProject = FXCollections.observableArrayList(control.getListProjects());
		listProject.setItems(dataProject);
		
		Label l = new Label("List Projects");
		l.setTextFill(Color.RED);
		l.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 15));
		
		vbox.getChildren().addAll(l, listProject);
		return vbox;
	}
	
	// Three bar charts + 3 label SD
	public HBox barchart() {
		
		//Bar chart Average Competency Level
		CategoryAxis Xavg = new CategoryAxis();
		Axis<Number> Yavg = new NumberAxis();
		Xavg.setLabel("Team");
		Yavg.setLabel("Value");
		
		//Bar chart % Getting 1st and 2nd Preferences
		CategoryAxis Xper = new CategoryAxis();
		NumberAxis Yper = new NumberAxis();
		Xper.setLabel("Team");
		Yper.setLabel("%");
		
		//Bar chart Skills Gap
		CategoryAxis Xsho = new CategoryAxis();
		NumberAxis Ysho = new NumberAxis();
		Xsho.setLabel("Team");
		Ysho.setLabel("Value");
		
		bcAverage = new BarChart<String, Number>(Xavg, Yavg);
		bcPercentage = new BarChart<String, Number>(Xper, Yper);
		bcShortfall = new BarChart<String, Number>(Xsho, Ysho);
		bcAverage.setLegendVisible(false);
		bcPercentage.setLegendVisible(false);
		bcShortfall.setLegendVisible(false);
		
		//Create title for bar chart Average Competency Level
		Text titleAvg = new Text();
		titleAvg.setText("Average Competency Level");
		titleAvg.setFont(Font.font ("Times", FontWeight.BOLD, 20));
		titleAvg.setFill(Color.BLUE);
		
		//Create title for bar chart % Getting 1st and 2nd Preferences
		Text titlePer = new Text();
		titlePer.setText("% Getting 1st and 2nd Preferences");
		titlePer.setFont(Font.font ("Times", FontWeight.BOLD, 20));
		titlePer.setFill(Color.BLUE);
		
		//Create title for bar chart Skills Gap
		Text titleGap = new Text();
		titleGap.setText("Skills Gap");
		titleGap.setFont(Font.font ("Times", FontWeight.BOLD, 20));
		titleGap.setFill(Color.BLUE);
		
		//Bar chart for Average Competence Level and SD
		StackPane pAve = new StackPane();
		lbSDAve.setText("SD = ...");
		lbSDAve.setTextFill(Color.BLACK);
		lbSDAve.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 20));
	    pAve.getChildren().addAll(bcAverage, lbSDAve);
	    pAve.setAlignment(Pos.TOP_CENTER);
	    
	    //VBox to add all related values
	    VBox vbAvg = new VBox();
	    vbAvg.getChildren().addAll(titleAvg, pAve);
	    vbAvg.setAlignment(Pos.CENTER);
	    
	    //Bar chart for %1st and 2nd preferences and SD
	    StackPane pPer = new StackPane();
		lbSDPer.setText("SD = ...");
		lbSDPer.setTextFill(Color.BLACK);
		lbSDPer.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 20));
		pPer.getChildren().addAll(bcPercentage, lbSDPer);
		pPer.setAlignment(Pos.TOP_CENTER);
		
		//VBox to add all related values
	    VBox vbPer = new VBox();
	    vbPer.getChildren().addAll(titlePer, pPer);
	    vbPer.setAlignment(Pos.CENTER);
		
	    
		//Bar chart for skill Gaps and SD
	    StackPane pGap = new StackPane();
		lbSDGap.setText("SD = ...");
		lbSDGap.setTextFill(Color.BLACK);
		lbSDGap.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 20));
		pGap.getChildren().addAll(bcShortfall, lbSDGap);
		pGap.setAlignment(Pos.TOP_CENTER);
		
		//VBox to add all related values
	    VBox vbGap = new VBox();
	    vbGap.getChildren().addAll(titleGap, pGap);
	    vbGap.setAlignment(Pos.CENTER);
		
		//Data for 3 bar charts
		XYChart.Series<String, Number> dataSeriesAvg = new XYChart.Series<String, Number>();
		XYChart.Series<String, Number> dataSeriesPer = new XYChart.Series<String, Number>();	
		XYChart.Series<String, Number> dataSeriesGap = new XYChart.Series<String, Number>();
		
		control.getSeriesAvg().keySet().forEach(x -> dataSeriesAvg.getData().add(
				new XYChart.Data<String, Number>(x, control.getSeriesAvg().get(x))));
		control.getSeriesPer().keySet().forEach(x -> dataSeriesPer.getData().add(
				new XYChart.Data<String, Number>(x, control.getSeriesPer().get(x))));
		control.getSeriesGap().keySet().forEach(x -> dataSeriesGap.getData().add(
				new XYChart.Data<String, Number>(x, control.getSeriesGap().get(x))));
		
		bcAverage.getData().add(dataSeriesAvg);
		bcPercentage.getData().add(dataSeriesPer);
		bcShortfall.getData().add(dataSeriesGap);
		
		lbSDAve.setText("SD = " + control.resultSD(control.getSeriesAvg()));
		lbSDPer.setText("SD = " + control.resultSD(control.getSeriesPer()));
		lbSDGap.setText("SD = " + control.resultSD(control.getSeriesGap()));
		
		// HBox contains 3 bar charts
		HBox hb = new HBox(5);
		hb.setAlignment(Pos.CENTER);
		hb.setPadding(new Insets(5, 5, 5, 5));
		hb.getChildren().addAll(vbAvg, vbPer, vbGap);
		hb.setAlignment(Pos.CENTER);
		
		return hb;
	}
}
