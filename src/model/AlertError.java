package model;

import java.util.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertError {
	private Alert alert = new Alert(AlertType.ERROR);
	private static AlertError uniqueInstance;
	
	private AlertError() {}
	
	//Singleton - Pattern (Creational)
	public static AlertError getInstance() {
		if (uniqueInstance == null)
		{ uniqueInstance = new AlertError();
		}
		return uniqueInstance;
	}
	
	// Check Student ID it has to S + number and it contains in the list 
	public boolean checkStuID(HashMap<String,Student> listStus, String ID) {
		if (!ID.startsWith("S")) {
			alert.setTitle("Invalid Student ID");
			alert.setHeaderText("Invalid Student ID input!");
			alert.setContentText("It must start with capital S");
			alert.showAndWait();
			return true;
		}	
		else if(ID.length() < 2) {
			alert.setTitle("Invalid Student ID");
			alert.setHeaderText("Invalid Student ID input!");
			alert.setContentText("It must start with capital S follow by a number");
			alert.showAndWait();
			return true;
		}
		
		try {
			Integer.parseInt(ID.substring(1));
		}
		catch (NumberFormatException e){
			alert.setTitle("Invalid Student ID");
			alert.setHeaderText("Invalid Student ID input!");
			alert.setContentText("Student ID must start with capital S + number");
			alert.showAndWait();
			return true;
		}
		
		if (listStus.get(ID) == null) {
			alert.setTitle("Invalid Student ID");
			alert.setHeaderText("Invalid Student ID input!");
			alert.setContentText("Student ID is not exist");
			alert.showAndWait();
			return true;
		}
		return false;
	}
	
	//students members who have indicated prior conflicts are assigned to the same team
	public boolean checkConflictStudent(Student oldStu, Student newStu, String type) {
		if(oldStu.getConflicts().contains(newStu.getstuID()) || newStu.getConflicts().contains(oldStu.getstuID())) {
			if (type.contains("GUI")) {
				alert.setTitle("Conflict Student");
				alert.setHeaderText("Conflict with other team members");
				alert.setContentText("Choose another student ID");
				alert.showAndWait();
			}
			return true;
		}
		return false;
	}

	//an attempt is made to add a student twice to the same team
	public boolean checkRepeatedMember(HashMap<String,Student> m, String sID) {
		if (m.get(sID) != null) {
			alert.setTitle("Invalid Student");
			alert.setHeaderText("Invalid Student ID input!");
			alert.setContentText("The student ID exist in this team");
			alert.showAndWait();
			return true;
		}
		return false;
	}
	
	//add a student already assigned to another project team
	public boolean checkInvalidMember(HashMap<String,Student> m, String sID) {
		if (m.get(sID) == null) {
			alert.setTitle("Invalid Student");
			alert.setHeaderText("Invalid Student ID input!");
			alert.setContentText("The student ID exist in another team");
			alert.showAndWait();
			return true;
		}
		return false;
	}

	//a team have more than 1 leader personality type
	public boolean checkOverLeader(Student sOld, Student sNew, String type) {
		
		if(sNew.getperType().equals("A") && sOld.getperType().equals("A")) {
			if (type.contains("GUI")) {
				alert.setTitle("Invalid Leader");
				alert.setHeaderText("Just one leader type in this team");
				alert.setContentText("Choose another student ID");
				alert.showAndWait();
			}
			return true;
		}
		
		return false;
	}

	//a team has less than three different personality types
	public boolean checkPerImbalance(HashMap<String, Student> m, Student newStu, String type) {
		int tA = 0;
		int tB = 0;
		int tC = 0;
		int tD = 0;
		
		if(newStu.getperType().equals("A")) tA++;
		else if(newStu.getperType().equals("B")) tB++;
		else if(newStu.getperType().equals("C")) tC++;
		else if(newStu.getperType().equals("D")) tD++;
		
		for(String k : m.keySet()) {
			if (m.get(k).getperType().equals("A")) {
				tA++;
			}
			if (m.get(k).getperType().equals("B")) {
				tB++;
			}
			if (m.get(k).getperType().equals("C")) {
				tC++;
			}
			if (m.get(k).getperType().equals("D")) {
				tD++;
			}
		}
		if (tA > 2 || tB > 2 || tC > 2 || tD > 2 || (tA + tB) == 4 || (tA + tC) == 4 || (tA + tD) == 4 
				|| (tB + tC) == 4 || (tB + tD) == 4 || (tC + tD) == 4) {
			if (type.contains("GUI")) {
				alert.setTitle("Invalid Personality Types");
				alert.setHeaderText("This team has less than three personality types!");
				alert.setContentText("Choose another student ID");
				alert.showAndWait();
			}
			return true;
		}	
		return false;
	}

	//a team does not have a leader personality type
	public boolean checkNoLeader(HashMap<String, Student> m, Student newStu, String type) {
		int i = 0;
		for(String k : m.keySet()) {
			if (m.get(k).getperType().equals("A") != true)
				i++;	
		}
		if (newStu.getperType().equals("A") != true) i++;
		if (i == m.size() + 1) {
			if (type.contains("GUI")) {
				alert.setTitle("No Leader");
				alert.setHeaderText("This team has to one leader!");
				alert.setContentText("Choose another student ID");
				alert.showAndWait();
			}
			return true;
		}
		return false;
	}
	
	///////////////////////////////////////////Check valid all hard conditions////////////////////////////////////////
	//Check Valid student before swap
	public boolean validSwapStuID(Integer i, String stuID, List<Team> FormTeam, HashMap<String,Student> students, String s) {
		if (s.contains("Thread"))
			return checkswapThread(FormTeam.get(i - 1).getTeamMembers(), stuID, students);
		if (s.contains("GUI"))
			return checkswapGUI(FormTeam.get(i - 1).getTeamMembers(), stuID, students);
		return false;
	}

	// Check student ID with all conditions below in Thread
	public boolean checkswapThread(HashMap<String,Student> members, String stuID, HashMap<String,Student> students) {
		if (checkPerImbalance(members, students.get(stuID), "Thread")) return false;

		for (String ID : members.keySet()) {
			if (checkConflictStudent(members.get(ID), students.get(stuID), "Thread")) return false;
			if (checkOverLeader(members.get(ID), students.get(stuID), "Thread")) return false;
		}
		if(members.size() >= 3)
			if (checkNoLeader(members, students.get(stuID), "Thread")) return false;
		return true;
	}
		
	// Check student ID with all conditions below in GUI
	private boolean checkswapGUI(HashMap<String,Student> members, String stuID, HashMap<String,Student> students) {
		if (checkPerImbalance(members, students.get(stuID), "GUI")) return false;
		
		for (String ID : members.keySet()) {
			if (checkConflictStudent(members.get(ID), students.get(stuID), "GUI")) return false;
			if (checkOverLeader(members.get(ID), students.get(stuID), "GUI")) return false;
		}
		if(members.size() >= 3)
			if (checkNoLeader(members, students.get(stuID), "GUI")) return false;
		return true;
	}
}
