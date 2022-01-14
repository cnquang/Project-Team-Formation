package model;
import java.util.*;

public class Owner {
	private String firstname;
	private String surname;
	private String ownID;
	private String role;
	private String email;
	private String comID;
	
	private HashMap<String,Project> pros = new HashMap<String, Project>();
	
	public Owner(String ownID, String firstname, String surname, String role, String email, String comID) {
		this.ownID = ownID;
		this.firstname = firstname;
		this.surname = surname;
		this.role = role;
		this.email = email;
		this.comID = comID;
	}
	
	public void addProject(Project p) {
		pros.put(ownID, p);
	}
	
	@Override
	public String toString() {
		return new String(ownID + " " + firstname + " " + surname + " " + role + " " + email + " " + comID);
	}
	
	public String getOwnfirstname() {
		return firstname;
	}
	public String getOwnsurname() {
		return surname;
	}
	public String getOwnID() {
		return ownID;
	}
	public String getOwnRole() {
		return role;
	}
	public String getOwnEmail() {
		return email;
	}
	public String getcomID() {
		return comID;
	}
	public HashMap<String,Project> getProjects(){
		return pros;
	}
}
