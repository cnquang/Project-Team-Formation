package controller;

import java.util.*;
import javafx.collections.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import model.*;
import thread.SuggestionThread;

public class GUIcontroller {	
	private TeamModel TM = TeamModel.getInstance();
	private Utility u = Utility.getInstance();
	
	//Add one student at a time with stuID to the textField ticked by checkBox
	public void addStudent(String stuID, HashMap<Integer, TextField> textField, HashMap<Integer, CheckBox> checkBox) {
		TM.addStudent(stuID, textField, checkBox);
	}
	
	//Delete students with stuID from the textField ticked by checkBox
	public void deleteStudent(HashMap<Integer, TextField> textField, HashMap<Integer, CheckBox> checkBox) {
		TM.deleteStudent(textField, checkBox);
	}
	
	//Get student details and print it out to Text place
	public void getStudent(String stuID, Text txtStudent) {
		TM.getStudent(stuID, txtStudent);	
	}
	
	//Swap 2 students
	public void swapStudent(HashMap<Integer, TextField> textField, HashMap<Integer, CheckBox> checkBox) {
		TM.swap(textField, checkBox);
	}
	
	//Undo after swap
	public void undo(HashMap<Integer, TextField> textField) {
		TM.undo(textField);
	}
	
	//Redo after undo
	public void redo(HashMap<Integer, TextField> textField) {
		TM.redo(textField);
	}
	
	//Suggest swaps so that team-fitness metrics can be improved
	public void suggest(HashMap<Integer, TextField> textField) {
		SuggestionThread thread = new SuggestionThread();
		thread.start();
	}
	
	//Auto swaps when we done suggestion steps
	public void autoSwap(HashMap<Integer, TextField> textField) {
		TM.autoSwap(textField);
	}
	
	//Redo after undo
	public void print() {
		TM.print();
	}

	//Update data immediately
	public void updateAvailableList(ObservableList<String> dataStu, 
			ObservableList<String> dataLeader, ObservableList<String> dataTeam) {
		dataLeader.clear();
		dataStu.clear();
		dataTeam.clear();
		dataStu.addAll( TM.getAvailableStus().keySet());
		dataLeader.addAll(TM.getLeaders());
		dataTeam.addAll(TM.getListTeams());
	}
	
	//Update bar chart immediately
	public void updateBarchart(BarChart<String, Number> bcAverage, BarChart<String, Number> bcPercentage,
			BarChart<String, Number> bcShortfall, Label lbSDAve, Label lbSDPer, Label lbSDGap) {
		TM.updateBarchart(bcAverage, bcPercentage, bcShortfall, lbSDAve, lbSDPer, lbSDGap);
	}
	

	public HashMap<String,Student> getAvailableStus(){
		return TM.getAvailableStus();
	}
	
	public List<Team> getFormTeam() {
		return TM.getFormTeam();
	}
	
	public List<String> getLeaders(){
		return TM.getLeaders();
	}
	
	public List<String> getListProjects() {
		return TM.getListProjects();
	}
	
	// Update list of team from 1 to 5 including project ID and members in this team
	public List<String> getListTeams(){
		return TM.getListTeams();
	}
	
	//Data for bar chart - Average Competence Level
	public Map<String,Double> getSeriesAvg(){
		return u.getSeriesAvg(TM.getFormTeam());
	}
	
	//Data for bar chart - % Getting 1st and 2nd preferences
	public Map<String,Double> getSeriesPer(){
		return u.getSeriesPer(TM.getFormTeam());
	}
	
	//Data for bar chart - Skills Gap
	public Map<String,Double> getSeriesGap(){
		return u.getSeriesGap(TM.getFormTeam());
	}
	//Result Standard deviation
	public double resultSD(Map<String, Double> m) {
		return u.computeSD(m);
	}
}
