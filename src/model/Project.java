package model;
import java.io.*;
import java.util.*;

public class Project implements Serializable{

	private static final long serialVersionUID = -257581534290275357L;
	private String title;
	private String prID;
	private String description;
	private String ownID;
	private HashMap<String, String> rankings = new HashMap<String, String>();
	
	public Project(String title, String prID, String description, String ownID ) {
		this.title = title;
		this.prID = prID;
		this.description = description;
		this.ownID = ownID;
	}
	
	public void setRanking(String skill, String i) {
		rankings.put(skill, i);
	}
	
	public String toString() {
		String[] v = new String[4];
		String[] k = new String[4];
		int i = 0, j = 0;
		
		for ( String key : rankings.keySet()) {
			k[i] = key;
			i++;
		}
		
		for ( String value : rankings.values()) {
			v[j] = value;
			j++;
		}
		
		return new String(title + "\n" 
							+ prID + "\n" 
							+ description + "\n" 
							+ ownID + "\n"   
							+ k[0] + " " + v[0] + " " + k[1] + " " + v[1] + " " + k[2] + " " + v[2] + " " + k[3] + " " + v[3]); 
	}
	
	public String gettitle() {
		return title;
	}
	public String getprID() {
		return prID;
	}
	public String getOwnID() {
		return ownID;
	}
	public String getDescription() {
		return description;
	}
	public HashMap<String, String> getRankings(){
		return rankings;
	}
	public String toStringRankings(){
		String s = new String();
		for (String key : rankings.keySet()) {
			s += key + " " + rankings.get(key) + " ";
		}		
		return s;
	}
}
