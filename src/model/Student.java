package model;
import java.io.*;
import java.util.*;
import java.util.stream.*;

public class Student implements Serializable{

	private static final long serialVersionUID = 1384227068325191771L;
	private String stuID;
	private String firstname;
	private String surname;
	private String perType;
	private HashMap<String, String> grades = new HashMap<String, String>();
	private HashMap<String, Integer> preferences = new HashMap<String, Integer>();
	private ArrayList<String> conflicts = new ArrayList<String>();
	
	public Student(String stuID, String firstname, String surname) {
		this.stuID = stuID;
		this.firstname = firstname;
		this.surname = surname;
	}
	
	public void setPerType(String type) {
		this.perType = type;
	}

	public void setGrades(String skill, String grade){
		grades.put(skill, grade);
	}
	
	public void setConflicts(String cf) {
		conflicts.add(cf);
	}
	
	public void setPreferences(String projectID, int score) {
		preferences.put(projectID, score);
	}
	
	@Override
	public String toString() {
		return new String(stuID + " " + firstname + " " + surname);
		//return new String(stuID + " " + firstname + " " + surname + " " + perType + " " + conflicts);
	}
	
	public HashMap<String, String> getGrades(){
		return grades;
	}
	
	public String toStringGrades() {
		String s = new String();
		for (String key : grades.keySet()) {
			s += key + " " + grades.get(key) + " ";
		}		
		return s;
	}
	
	public HashMap<String, Integer> getPreferences(){
		// order follow by values
		return preferences.entrySet()
				.stream()
				.sorted(Map.Entry.<String, Integer>comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
	
	public String toStringPreferences() {
		String s = new String();
		for (String key : preferences.keySet()) {
			s += key + " " + preferences.get(key) + " ";
		}
		return s;
	}
	
	public ArrayList<String> getConflicts(){
		return conflicts;
	}
	
	public String toStringConflicts() {
		String s = new String();
		for (int i = 0; i < conflicts.size(); i++) {
			s += conflicts.get(i) + " ";
		}		
		return s;
	}
	
	public String getstuID() {
		return stuID;
	}
	
	public String getStuFirstname() {
		return firstname;
	}
	
	public String getStuSurname() {
		return surname;
	}
	
	public String getperType() {
		return perType;
	}
}
