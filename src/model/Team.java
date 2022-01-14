package model;
import java.io.Serializable;
import java.util.*;

public class Team implements Serializable{

	private static final long serialVersionUID = 1L;
	private HashMap<String,Student> students = new HashMap<String, Student>();
	private HashMap<String, Double> avgStuSkill = new HashMap<String, Double>();
	private Project p;
	private int teamNum;
	private double sPercentage, sShortfall = 0.0, avgSkill;
	
	public Team(int teamNum, Project p) {
		this.teamNum = teamNum;
		this.p = p;
	}
	
	public Team(int teamNum, HashMap<String,Student> students, Project p) {
		this.teamNum = teamNum;
		this.students = students;
		this.p = p;
	}
	
	public void AddStudent(String stuID, Student s) {
		students.put(stuID, s);
	}
	
	public void RemoveStudent(String stuID) {
		students.remove(stuID);
	}
	
	@Override
	public String toString() {
		return new String("Team" + teamNum + " - " + p.getprID() + " " + students);
		//return new String(stuID + " " + firstname + " " + surname + " " + perType + " " + conflicts);
	}
	
	//H1. Average student skill competence for each project team.
	public double AvgStuSkill() {
		double scoreP = 0.0;
		double scoreN = 0.0;
		double scoreA = 0.0;
		double scoreW = 0.0;
		double total = 0.0;
		String[] skills = {"P", "N", "A", "W"};
			
		// Total score of each skill/number of students in each team, after that putting in avgStuSkill
		for( String k1 : students.keySet()) {
			//System.out.println(students.keySet());
				
			if (avgStuSkill.get("P") != null) {
				scoreP += Double.parseDouble(students.get(k1).getGrades().get("P"));
				avgStuSkill.put("P",scoreP);
			}
			else {
				scoreP = Double.parseDouble(students.get(k1).getGrades().get("P"));
				avgStuSkill.put("P",scoreP);
			}
			
			if (avgStuSkill.get("N") != null) {
				scoreN += Double.parseDouble(students.get(k1).getGrades().get("N"));
				avgStuSkill.put("N",scoreN);
			}
			else {
				scoreN = Double.parseDouble(students.get(k1).getGrades().get("N"));
				avgStuSkill.put("N",scoreN);
			}
				
			if (avgStuSkill.get("A") != null) {
				scoreA += Double.parseDouble(students.get(k1).getGrades().get("A"));
				avgStuSkill.put("A",scoreA);
			}
			else {
				scoreA = Double.parseDouble(students.get(k1).getGrades().get("A"));
				avgStuSkill.put("A",scoreA);
			}
				
			if (avgStuSkill.get("W") != null) {
				scoreW += Double.parseDouble(students.get(k1).getGrades().get("W"));
				avgStuSkill.put("W",scoreW);
			}
			else {
				scoreW = Double.parseDouble(students.get(k1).getGrades().get("W"));
				avgStuSkill.put("W",scoreW);
			}
		}
			
		// Compute average student skill
		for (String s : skills) {
			avgStuSkill.put(s, avgStuSkill.get(s)/students.size());
			total += avgStuSkill.get(s);
		}
		avgSkill = total/skills.length;
		return avgSkill;
	}
	
	//H2. Percentage of students who got their first and second preferences in each of the teams.
	public double PerOfStu(){
		int satisfied = 0;
		for( String k : students.keySet()) {
			if (students.get(k).getPreferences().get(p.getprID()) != null) {
				if (students.get(k).getPreferences().get(p.getprID()) == 4 ||  students.get(k).getPreferences().get(p.getprID()) == 3) 
					satisfied++;
			}
		}
		sPercentage = satisfied*100/students.size();
		return sPercentage;
	}
	
	//H3. Skills shortfall for each project
	public double skillShortfall(){
		sShortfall = 0.0;
		String[] skills = {"P", "N", "A", "W"};
		for (String k : skills) {
			if(Double.parseDouble(p.getRankings().get(k)) > avgStuSkill.get(k))
				sShortfall += (Double.parseDouble(p.getRankings().get(k)) - avgStuSkill.get(k));
		}
		return sShortfall;
	}
	
	//List skills short-fall for each student (skills Gap)
	public HashMap<String, Integer> getSkillGapStu(){
		HashMap<String, Integer> shortSkill = new HashMap<String, Integer>();
		String[] skills = {"P", "N", "A", "W"};
		int score = 0, proScore, stuScore;
		
		for (String key : students.keySet()) {
			for (String k : skills) {
				proScore = Integer.parseInt(p.getRankings().get(k));
				stuScore = Integer.parseInt(students.get(key).getGrades().get(k));
				if(proScore > stuScore)
					score += (proScore - stuScore);
			}
			//add skills gap score for each students in the team
			shortSkill.put(key, score);
			score = 0;
		}
		return shortSkill;
	}
	
	//List Average score of each student in this team
	public HashMap<String, Double> getAvgStu(){
		HashMap<String, Double> avgScore = new HashMap<String, Double>();
		String[] skills = {"P", "N", "A", "W"};
		double score = 0.0;
		
		for (String key : students.keySet()) {
			for (String k : skills) {
				score += Integer.parseInt(students.get(key).getGrades().get(k));
			}
			//add skills gap score for each students in the team
			avgScore.put(key, score/skills.length);
			score = 0.0;
		}
		return avgScore;
	}
	
	public Project getProject() {
		return p;
	}
	
	public String getProID() {
		return p.getprID();
	}
	
	public int getTeamNum() {
		return teamNum;
	}
	
	public double getsPercentage() {
		return sPercentage;
	}
	
	public double getsShortfall() {
		return sShortfall;
	}
	
	public double getAveStuSkill() {
		return avgSkill;
	}
	
	public HashMap<String, Double> getAvgSkill(){
		return avgStuSkill;
	}

	public HashMap<String,Student> getTeamMembers() {
		return students;
	}
	
	public String toStringMembers() {
		String s = new String();
		for (String key : students.keySet()) {
			s += key + " ";
		}		
		return s;
	}
}


