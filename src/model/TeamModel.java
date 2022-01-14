package model;

import java.io.*;
import java.util.*;
import java.util.stream.*;

import data.Data;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.text.*;

//Singleton - Pattern (Creational)
//Facade
public class TeamModel {
	private Data data = new Data();
	private Utility u = Utility.getInstance();
	private AlertError Aerror = AlertError.getInstance();
	
	private static TeamModel uniqueInstance;
	private HashMap<String,Student> students = new HashMap<String,Student>();
	private HashMap<String,Student> availableStus = new HashMap<String,Student>();
	private HashMap<String,Project> projects = new HashMap<String,Project>();
	private HashMap<String,Company> companies = new HashMap<String, Company>();
	private HashMap<String,Owner> owners = new HashMap<String, Owner>();
	private HashMap<Integer, String> StepsOfSwap = new HashMap<Integer, String>();
	private List<String> Leaders = new ArrayList<String>();
	private List<String> listTeams = new ArrayList<String>();
	private List<String> listProjects = new ArrayList<String>();
	private List<String> listProjectID = new ArrayList<String>();
	private List<String> normalHisSwap = new LinkedList<String>(); //Normal history when swap 2 students
	private List<String> reverseHisSwap = new LinkedList<String>();//form String will be Student ID1 Student ID2 (e.g. "S1 S2")
	private List<Team> FormTeam = new LinkedList<Team>();
	
	final String FTPATH = "formTeam.dat";
	final String DBNAME = "FORMTEAM.db";
	final String TBCOMPANY = "COMPANY";
	final String TBOWNER = "OWNER";
	final String TBSTUDENT = "STUDENT";
	final String TBPROJECT = "PROJECT";
	final String TBFORMTEAM = "FORMTEAM";
	final String TBSUGGESTION = "SUGGESTION";
	
	//Check file formteam.dat exists or not
	File checkFile = new File(FTPATH);
	boolean exist = checkFile.exists();
	
	//Constructor with initial data
	private TeamModel() {
		try {
			//Read students and projects from .dat files, owners project and companies from .txt files
			students = data.serialReadStu("StusProjs.dat");
			projects = data.serialReadPro("StusProjs.dat");
			owners = data.readOwner("owners.txt");
			companies = data.readCompany("companies.txt");
			
			//Create list of project ID
			listProjectID = projects.keySet().stream().collect(Collectors.toList());
			
			//Create 4 tables (company + owner + student + project) in FORMTEAM.db database
			data.createTables(DBNAME, TBCOMPANY, TBOWNER, TBSTUDENT, TBPROJECT);
			//Insert data for 2 tables (student + project)
			data.insertData(DBNAME, TBCOMPANY, TBOWNER, TBSTUDENT, TBPROJECT, companies, owners, students, projects);
			
			//if formteam.dat exists, we will fill all the team
			if(exist) {
				FormTeam = data.readFromTeam(FTPATH);
				//Create and fill data in form team table in FORMTEAM.db database
				data.formTeamdata(DBNAME, TBFORMTEAM, FormTeam);
				//Print all tables in database
				data.printAllTables(DBNAME, TBCOMPANY, TBOWNER, TBSTUDENT, TBPROJECT, TBFORMTEAM);
				//Print join Project table and FormTeam table
				data.joinFTeamProject(DBNAME, TBPROJECT, TBFORMTEAM);
				//Print members in each team with details of each student
				data.printFormTeamMember(DBNAME, TBSTUDENT, TBFORMTEAM);
			} else {
				//Fist-time available students list always full
				availableStus = data.serialReadStu("StusProjs.dat");
				//Create list of students with leader type
				Leaders = (availableStus.keySet().stream()
						.filter(x -> availableStus.get(x).getperType().equals("A")))
						.collect(Collectors.toList());	
				//Create number of teams = number of projects
				for (int i = 0; i < listProjectID.size(); i++) {
					int k = i + 1;
					FormTeam.add(i, new Team(k, projects.get(listProjectID.get(i))));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//Singleton - Pattern (Creational)
	public static TeamModel getInstance() {
		if (uniqueInstance == null)
		{ uniqueInstance = new TeamModel();
		}
		return uniqueInstance;
	}
	
	////////////////////////////////////////////////Add Students///////////////////////////////////////////
	public void addStudent(String stuID, HashMap<Integer, TextField> textField, HashMap<Integer, CheckBox> checkBox) {
		int numtick = 0;
		// Count how many check boxes are chosen
		for(CheckBox cb : checkBox.values()) {
			if(cb.isSelected())
				numtick++;
		}
		//Show Alert when tick more than 1 checkBox or do not tick any checkBox
		if (numtick != 1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error add student");
			alert.setHeaderText("Add only one student at a time");
			alert.setContentText("Tick only one");
			alert.showAndWait();
		}
		else if (stuID.contains("S")){
			for(int i : checkBox.keySet()) {
				// check empty text field and check box is selected -> add student
				if (checkBox.get(i).isSelected() && textField.get(i).getLength() == 0) {
					if(addStu(i, stuID))
						textField.get(i).setText(stuID);
				}
				//Show Alert when TextField is already has student
				else if (checkBox.get(i).isSelected() && textField.get(i).getLength() != 0) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error add student");
					alert.setHeaderText("Exist a student in this place!");
					alert.setContentText("Tick another checkBox");
					alert.showAndWait();
				}
			}
		}
	}
	
	//Add student when click button "add" or click Student ID in listView
	public boolean addStu(Integer i, String stuID) {
		// If a student ID valid all conditions, it can add to the team 
		boolean valid = validAddStuID(u.teamNum(i), stuID);
		if (valid) {
			u.put(u.teamNum(i), stuID, FormTeam, students); //put student in hash map team
			availableStus.remove(stuID);
			Leaders.remove(new String(stuID));
		}
		return valid;
	}
	
	////////////////////////////////////////////////Delete Students///////////////////////////////////////////
	public void deleteStudent(HashMap<Integer, TextField> textField, HashMap<Integer, CheckBox> checkBox) {
		for(int i : checkBox.keySet()) {
			if (checkBox.get(i).isSelected() && textField.get(i).getLength() != 0) {
				deleteStu(i, textField.get(i).getText());
				textField.get(i).setText("");
			}	
		}
	}
	//Delete student ID when click button "delete"
	public void deleteStu(Integer i, String stuID) {
		u.remove(u.teamNum(i), stuID, FormTeam); //remove student from the team
		// Add available students to the list view
		availableStus.put(stuID, students.get(stuID));
		if (students.get(stuID).getperType().equals("A"))
			Leaders.add(stuID);
	}
	
	////////////////////////////////////////////////Get Student details///////////////////////////////////////////
	public void getStudent(String stuID, Text txtStudent) {
		//Check stuID is entered
		if (stuID.contains("S")) {
			txtStudent.setText(getStudents().get(stuID).toString() + " - Personality type: " +getStudents().get(stuID).getperType()
					+ "\n" + getStudents().get(stuID).getPreferences()+ "\n" + getStudents().get(stuID).getGrades()
					+ "\n" + getStudents().get(stuID).getConflicts());
		}	
	}
	
	////////////////////////////////////////////////Swap Students///////////////////////////////////////////
	
	//Swap students when click button "swap" updated on GUI
	public void swap(HashMap<Integer, TextField> textField, HashMap<Integer, CheckBox> checkBox) {
		// save positions of ticked boxes in listBox ArrayList
		ArrayList<Integer> listBox = new ArrayList<Integer>();
		checkBox.keySet().forEach(k -> {
			if(checkBox.get(k).isSelected())
				listBox.add(k);
		});
				
		//Swap when two students are chosen / two checkBox are chosen
		if (listBox.size() == 2) {
			if(swapStudent(listBox, textField.get(listBox.get(0)).getText(), textField.get(listBox.get(1)).getText())) {
				//swap 2 textFiled boxes
				String tempt = textField.get(listBox.get(0)).getText();
				textField.get(listBox.get(0)).setText(textField.get(listBox.get(1)).getText());
				textField.get(listBox.get(1)).setText(tempt);
				
				System.out.println("swap: " + textField.get(listBox.get(0)).getText() 
						+ " and " + textField.get(listBox.get(1)).getText());
			}
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Swap Student");
			alert.setHeaderText("Tick two check boxes before swap");
			alert.setContentText("Tick two check boxes");
			alert.showAndWait();
		}
	}
		
	//Swap students when click button "swap" updated on data
	public boolean swapStudent(ArrayList<Integer> listBox, String stu1, String stu2) {
		//Check all hard conditions before swap
		boolean valid1, valid2;
		
		//Check student 1 and student 2 IDs not null before swap
		if (stu1.equals("") == false && stu2.equals("") == false) {
			u.remove(u.teamNum(listBox.get(0)), stu1, FormTeam);
			u.remove(u.teamNum(listBox.get(1)), stu2, FormTeam);
			valid1 = Aerror.validSwapStuID(u.teamNum(listBox.get(1)), stu1, FormTeam, students, "GUI");
			valid2 = Aerror.validSwapStuID(u.teamNum(listBox.get(0)), stu2, FormTeam, students, "GUI");
			
			// Swap if valid all conditions
			if(valid1 && valid2) {
				u.put(u.teamNum(listBox.get(1)), stu1, FormTeam, students);
				u.put(u.teamNum(listBox.get(0)), stu2, FormTeam, students);
				
				//Just save history of swap when form team done.
				if (availableStus.isEmpty())
					//Save all history swap 2 students.
					u.push(normalHisSwap, stu1 + " " + stu2);
				return true;
			}
			else {
				u.put(u.teamNum(listBox.get(0)), stu1, FormTeam, students);
				u.put(u.teamNum(listBox.get(1)), stu2, FormTeam, students);
				return false;
			}
		}
		else {
			if (stu1.equals("")) valid1 = true;
			else valid1 = Aerror.validSwapStuID(u.teamNum(listBox.get(1)), stu1, FormTeam, students, "GUI");
			if (stu2.equals("")) valid2 = true;
			else valid2 = Aerror.validSwapStuID(u.teamNum(listBox.get(0)), stu2, FormTeam, students, "GUI");
			// Swap if valid all conditions
			if(valid1 && valid2) {
				if (stu1.equals("") == false) {
					u.remove(u.teamNum(listBox.get(0)), stu1, FormTeam);
					u.put(u.teamNum(listBox.get(1)), stu1, FormTeam, students);
				}
				if (stu2.equals("") == false) {
					u.remove(u.teamNum(listBox.get(1)), stu2, FormTeam);
					u.put(u.teamNum(listBox.get(0)), stu2, FormTeam, students);
				}
				return true;
			}
			else return false;
		}
	}

	//////////////////////////////////////////////// Data for Bar chart ///////////////////////////////////////////
	//Update bar chart immediately
	public void updateBarchart(BarChart<String, Number> bcAverage, BarChart<String, Number> bcPercentage,
			BarChart<String, Number> bcShortfall, Label lbSDAve, Label lbSDPer, Label lbSDGap) {
		// Just update when all students are formed in teams
		if(getAvailableStus().size() == 0) {
			bcAverage.getData().clear();
			bcPercentage.getData().clear();
			bcShortfall.getData().clear();
			
			XYChart.Series<String, Number> dataSeriesAvg = new XYChart.Series<String, Number>();
			XYChart.Series<String, Number> dataSeriesPer = new XYChart.Series<String, Number>();	
			XYChart.Series<String, Number> dataSeriesGap = new XYChart.Series<String, Number>();
			
			u.getSeriesAvg(getFormTeam()).keySet().forEach(x 
					-> dataSeriesAvg.getData().add(new XYChart.Data<String, Number>(x, u.getSeriesAvg(FormTeam).get(x))));
			u.getSeriesPer(FormTeam).keySet().forEach(x 
					-> dataSeriesPer.getData().add(new XYChart.Data<String, Number>(x, u.getSeriesPer(FormTeam).get(x))));
			u.getSeriesGap(FormTeam).keySet().forEach(x 
					-> dataSeriesGap.getData().add(new XYChart.Data<String, Number>(x, u.getSeriesGap(FormTeam).get(x))));
			
			bcAverage.getData().add(dataSeriesAvg);
			bcPercentage.getData().add(dataSeriesPer);
			bcShortfall.getData().add(dataSeriesGap);
			
			lbSDAve.setText("SD = " + u.computeSD(u.getSeriesAvg(FormTeam)));
			lbSDPer.setText("SD = " + u.computeSD(u.getSeriesPer(FormTeam)));
			lbSDGap.setText("SD = " + u.computeSD(u.getSeriesGap(FormTeam)));
			
			//Save form team to .dat files and database
			save();
		}
	}

	//////////////////////////////////////////////// Undo - Redo ///////////////////////////////////////////
	public void undo(HashMap<Integer, TextField> textField) {
		//String of the recently history swap. ("S1 S2")
		String s;
		String[] stuIDs = null;
		
		if (normalHisSwap.size() > 0) {
			s = normalHisSwap.get(normalHisSwap.size() - 1);
			System.out.println("undo: " + s);
			stuIDs = s.split(" ");
			//Delete top history on normalHisSwap
			u.pop(normalHisSwap);
			//add to from deleted normalHisSwap to reverseHisSwap
			u.push(reverseHisSwap, s);
			//Swap 2 students ID in GUI
			u.swapTextFieldStu(textField, stuIDs[0], stuIDs[1]);
			//Swap 2 students in FormTeam
			swapdataStu(stuIDs[0], stuIDs[1]);
		}
	}
	
	public void redo(HashMap<Integer, TextField> textField) {
		//String of the recently history swap. ("S1 S2")
		String s;
		String[] stuIDs = null;
		
		if (reverseHisSwap.size() > 0) {
			s = reverseHisSwap.get(reverseHisSwap.size() - 1);
			System.out.println("redo: " + s);
			stuIDs = s.split(" ");
			//Delete top history on reverseHisSwap
			u.pop(reverseHisSwap);
			//add to from deleted reverseHisSwap to normalHisSwap
			u.push(normalHisSwap, s);
			//Swap 2 students ID in GUI
			u.swapTextFieldStu(textField, stuIDs[0], stuIDs[1]);
			//Swap 2 students in FormTeam
			swapdataStu(stuIDs[0], stuIDs[1]);
		}
	}
	
	//////////////////////////////////////////////// Auto Swap ///////////////////////////////////////////
	public void autoSwap(HashMap<Integer, TextField> textField) {
		StepsOfSwap = data.readStepsOfSwap(DBNAME, TBSUGGESTION);
		normalHisSwap.clear();
		String pair;
		String[] stu;
		for (int i = 1; i <= StepsOfSwap.size(); i++) {
			pair = StepsOfSwap.get(i);
			stu = pair.split(" ");
			swapAutoStu(textField, stu[0], stu[1]);
		}
		//Clear all stepsOfSwap when we done in database
		StepsOfSwap.clear();
		List<String> s = null;
		data.Suggestionsdata(DBNAME, TBSUGGESTION, s);
	}
	
	//Swap students when click button "Auto Swap" updated on GUI
	public void swapAutoStu(HashMap<Integer, TextField> textField, String stu1, String stu2) {
		//Swap 2 students in FormTeam
		swapdataStu(stu1, stu2);
		//Just save history of swap when form team done. Using for undo and redo buttons
		if (availableStus.isEmpty())
			//Save all history swap 2 students.
			u.push(normalHisSwap, stu1 + " " + stu2);
		//Swap 2 students ID in GUI
		u.swapTextFieldStu(textField, stu1, stu2);
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	//Swap in data
	private void swapdataStu(String stu1, String stu2) {
		int team1 = u.teamNumber(stu1, FormTeam);
		int team2 = u.teamNumber(stu2, FormTeam);
		
		u.remove(team1, stu1, FormTeam);
		u.remove(team2, stu2, FormTeam);
		
		u.put(team2, stu1, FormTeam, students);
		u.put(team1, stu2, FormTeam, students);
	}
	
	//Check Valid student before add to the team
	private boolean validAddStuID(Integer i, String stuID) {
		return check(FormTeam.get(i - 1).getTeamMembers(), stuID);
	}
	
	// Check student ID with all conditions below
	private boolean check(HashMap<String,Student> members, String stuID) {
		if (Aerror.checkStuID(students, stuID)) return false;
		else if (Aerror.checkRepeatedMember(members, stuID)) return false;
		else if (Aerror.checkInvalidMember(availableStus, stuID)) return false;
		else if (Aerror.checkPerImbalance(members, availableStus.get(stuID), "GUI")) return false;
		
		for (String ID : members.keySet()) {
			if (Aerror.checkConflictStudent(members.get(ID), availableStus.get(stuID), "GUI")) return false;
			if (Aerror.checkOverLeader(members.get(ID), availableStus.get(stuID), "GUI")) return false;
		}
		
		if(members.size() >= 3)
			if (Aerror.checkNoLeader(members, availableStus.get(stuID), "GUI")) return false;
		
		return true;
	}
	
	// Update list of team from 1 to 5 including project ID and members in this team
	public List<String> getListTeams(){
		listTeams.clear();
		for (int i = 0; i < FormTeam.size(); i++) {
			int k = i + 1;
			listTeams.add("Team " + k + " - " + FormTeam.get(i).getProject().getprID() + ": "+ FormTeam.get(i).getTeamMembers());
		}
		return listTeams;
	}
	
	public void save() {
		//Save form team to .dat file
		data.saveFormTeam(FormTeam, FTPATH);
		//Save form team to database
		data.formTeamdata(DBNAME, TBFORMTEAM, FormTeam);
	}
	
	public void print() {
		//Print join Project table and FormTeam table
		data.joinFTeamProject(DBNAME, TBPROJECT, TBFORMTEAM);
		//Print members in each team with details of each student
		data.printFormTeamMember(DBNAME, TBSTUDENT, TBFORMTEAM);
		//User cannot undo - redo when click print button
		normalHisSwap.clear();
		reverseHisSwap.clear();
	}
	
	public HashMap<String,Student> getAvailableStus(){
		return availableStus;
	}
	public HashMap<String,Student> getStudents(){
		return students;
	}
	public List<String> getLeaders(){
		return Leaders;
	}
	public List<Team> getFormTeam() {
		return FormTeam;
	}
	public List<String> getListProjects() {
		projects.keySet().forEach(x -> listProjects.add(projects.get(x).toString()));
		return listProjects;
	}
	public HashMap<String,Project> getProjects(){
		return projects;
	}
}
