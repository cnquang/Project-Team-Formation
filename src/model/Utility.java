package model;

import java.util.*;
import java.util.stream.*;
import javafx.scene.control.*;

public class Utility {
	private static Utility uniqueInstance;
	
	private Utility() {}
	
	//Singleton - Pattern (Creational)
	public static Utility getInstance() {
		if (uniqueInstance == null)
		{ uniqueInstance = new Utility();
		}
		return uniqueInstance;
	}

	////////////////////////////////////////////////Standard deviation ///////////////////////////////////////////
	//Compute Standard deviation
	public double computeSD(Map<String, Double> m) {
		ArrayList<Double> x = new ArrayList<Double>();
		for (Double k : m.values())
			x.add(k);
		double total = x.stream().mapToDouble(y -> y).sum();
		double mean = total/x.size();
		double sum = x.stream().mapToDouble(y -> Math.pow(y-mean, 2)).sum();

		return Math.sqrt(sum/x.size());
	}
	
	//Change the weight of each team-fitness metrics here
	public double totalSDs(TreeMap<Double, String> listSD) {
		double total = 0.0;
		for(double k : listSD.keySet()) {
			if(listSD.get(k).contains("Avg")) {
				total += k;
			}
			else {
				total += k;
			}
		}
		return total;
	}
	
	///////////////////////////////Find the best Team and worst Team in list of SD values//////////////////////////
	//Find the Team with highest SD score. 
	public String highestSDMap(Map<String,Double> value) {
		String best;
		TreeMap<Double, String> tempt = new TreeMap<Double, String>();
		value.keySet().forEach(key -> tempt.put(value.get(key), key));

		best = tempt.get(tempt.lastKey());
		return best;
	}

	//Find the Team with lowest SD score. 
	public String lowestSDMap(Map<String,Double> value) {
		String worst;
		TreeMap<Double, String> tempt = new TreeMap<Double, String>();
		value.keySet().forEach(key -> tempt.put(value.get(key), key));

		worst = tempt.get(tempt.firstKey());
		return worst;
	}
	
	////////////////////////////////////////////////Find Team number with Student ID /////////////////////////////////
	//Find team number depend on student ID
	public Integer teamNumber(String ID, List<Team> FormTeam) {
		for (Team a : FormTeam) {
			if (a.getTeamMembers().containsKey(ID)) {
				return a.getTeamNum();
			}
		}
		return null;
	}
	
	//Depend on position of checkBox or textField to find number of the team
	public int teamNum(Integer index) {
		return (int) Math.ceil((double)index/4);
	}
	
	///////////////////////////////////////////Add/Remove a student in a team////////////////////////////////////////
	// Remove a student from a team
	public void remove(Integer i, String stuID, List<Team> FormTeam) {
		FormTeam.get(i -1).RemoveStudent(stuID);
	}

	// Put a student to a team
	public void put(Integer i, String stuID, List<Team> FormTeam, HashMap<String,Student> students) {
		FormTeam.get(i - 1).AddStudent(stuID, students.get(stuID));
	}
	
	public void push(List<String> s, String twoStuIDs) {
		s.add(s.size(), twoStuIDs);
	}
	
	public void pop(List<String> s) {
		if(s.size() > 0)
			s.remove(s.size() - 1);
	}
	
	///////////////////////////////////////////Swap////////////////////////////////////////
	//Swap in GUI of 2 students
	public void swapTextFieldStu(HashMap<Integer, TextField> textField, String stu1, String stu2) {
		int p1 = 0, p2 = 0;
		//Find positions of two students and swap them
		for(Integer k :  textField.keySet()) {
			if(textField.get(k).getText().equals(stu1)) {
				p1 = k;
			}
			if(textField.get(k).getText().equals(stu2)) {
				p2 = k;
			}
		}
		textField.get(p1).setText(stu2);
		textField.get(p2).setText(stu1);
	}
	
	////////////////////////////////////////////////Sorted ///////////////////////////////////////////
	//Order follow by values
	public HashMap<String, Integer> intSorted(HashMap<String, Integer> m){
		return m.entrySet()
				.stream()
				.sorted(Map.Entry.<String, Integer>comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}

	//Order follow by values
	public HashMap<String, Double> doubleSorted(HashMap<String, Double> m){
		return m.entrySet()
				.stream()
				.sorted(Map.Entry.<String, Double>comparingByValue())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
	
	////////////////////////////////////////////////Data for bar charts///////////////////////////////////////////
	//Data for bar chart - Average Competence Level
	public Map<String,Double> getSeriesAvg(List<Team> FormTeam){
		Map<String,Double> seriesAvg = new TreeMap<String,Double>();
		if (FormTeam.get(0).getTeamMembers().isEmpty() != true) {
			//Calculate average students skill for each team
			for (int i = 0; i < FormTeam.size(); i++) {
				int k = i + 1;
				seriesAvg.put(Integer.toString(k), FormTeam.get(i).AvgStuSkill());
			}
		}
		return seriesAvg;
	}
	
	//Data for bar chart - % Getting 1st and 2nd preferences
	public Map<String,Double> getSeriesPer(List<Team> FormTeam){
		Map<String,Double> seriesPer = new TreeMap<String,Double>();
		if (FormTeam.get(0).getTeamMembers().isEmpty() != true) {
			//Calculate percentage of students who got their first and second preferences in each of the teams
			for (int i = 0; i < FormTeam.size(); i++) {
				int k = i + 1;
				seriesPer.put(Integer.toString(k), FormTeam.get(i).PerOfStu());
			}
		}
		return seriesPer;
	}
	
	//Data for bar chart - Skills Gap
	public Map<String,Double> getSeriesGap(List<Team> FormTeam){
		Map<String,Double> seriesGap = new TreeMap<String,Double>();
		if (FormTeam.get(0).getTeamMembers().isEmpty() != true) {
			//Calculate skills shortfall for each project based on categories 
			for (int i = 0; i < FormTeam.size(); i++) {
				int k = i + 1;
				seriesGap.put(Integer.toString(k), FormTeam.get(i).skillShortfall());
			}
		}
		return seriesGap;
	}
	
}
