package thread;

import java.util.*;

import data.Data;
import model.AlertError;
import model.Student;
import model.Team;
import model.Utility;

public class SuggestionThread extends Thread{
	
	private Data data = new Data();
	private Utility u = Utility.getInstance();
	private AlertError Aerror = AlertError.getInstance();
	
	//Copy all data from TeamModel, from that, we find write a algorithm to reduce all gap of three metrics 
	private HashMap<String,Student> students;
	private Map<String,Double> seriesAvg;//Data for bar chart - Average Competence Level
	private Map<String,Double> seriesPer;//Data for bar chart - % Getting 1st and 2nd preferences
	private Map<String,Double> seriesGap;////Data for bar chart - Skills Gap
	private List<Team> FormTeam;
	private List<String> PossibleSwap = new ArrayList<String>();
	private List<String> StepsOfSwap = new ArrayList<String>();
	private TreeMap<Double, String> listSD = new TreeMap<Double, String>(); //Using TreeMap to short SDs follow the order
	
	final String DBNAME = "FORMTEAM.db";
	final String TBSUGGESTION = "SUGGESTION";
	
	//Constructor creating initial data
	public SuggestionThread() {
		try {
			students = data.serialReadStu("StusProjs.dat");
			FormTeam = data.readFromTeam("formTeam.dat");
		} catch (Exception e) {
			e.printStackTrace();
		}
		seriesAvg = u.getSeriesAvg(FormTeam);
		seriesPer = u.getSeriesPer(FormTeam);
		seriesGap = u.getSeriesGap(FormTeam);
		//List SDs of 3 team-metrics
		listSD.put(u.computeSD(u.getSeriesAvg(FormTeam)), "Avg");
		listSD.put(u.computeSD(u.getSeriesPer(FormTeam))/100, "Pre");
		listSD.put(u.computeSD(u.getSeriesGap(FormTeam)), "Gap");	
	}
	
	@Override
	public void run()
	{
		boolean success = false;
		double sdOld, sdNew;
		Map<String,Double> temptValues = new HashMap<String,Double>();
		//update data from GUI.
		update();
		
		while (!success) {
			
			temptValues.clear();
			
			sdOld = u.totalSDs(listSD);
			//List of possible swap to improve each or team-fitness metrics below:
			swapPre();
			swapGap();
			swapAvg();
			
			for(String x : PossibleSwap) {
				String[] stu = x.split(" ");
				int team1 = u.teamNumber(stu[0], FormTeam);
				int team2 = u.teamNumber(stu[1], FormTeam);
				
				u.remove(team1, stu[0], FormTeam);
				u.remove(team2, stu[1], FormTeam);
				
				u.put(team1, stu[1], FormTeam, students);
				u.put(team2, stu[0], FormTeam, students);
				
				listSD.clear();
				listSD.put(u.computeSD(u.getSeriesAvg(FormTeam)), "Avg");
				listSD.put(u.computeSD(u.getSeriesPer(FormTeam))/100, "Per");
				listSD.put(u.computeSD(u.getSeriesGap(FormTeam)), "Gap");	
				
				temptValues.put(x, u.totalSDs(listSD));
				
				u.remove(team1, stu[1], FormTeam);
				u.remove(team2, stu[0], FormTeam);
				
				u.put(team1, stu[0], FormTeam, students);
				u.put(team2, stu[1], FormTeam, students);
			}
			PossibleSwap.clear();
			//The pair of students with lowest SD score. 
			String s = u.lowestSDMap(temptValues);
			
			String[] stu = s.split(" ");
			int team1 = u.teamNumber(stu[0], FormTeam);
			int team2 = u.teamNumber(stu[1], FormTeam);
			
			u.remove(team1, stu[0], FormTeam);
			u.remove(team2, stu[1], FormTeam);
			
			u.put(team1, stu[1], FormTeam, students);
			u.put(team2, stu[0], FormTeam, students);
			
			listSD.clear();
			listSD.put(u.computeSD(u.getSeriesAvg(FormTeam)), "Avg");
			listSD.put(u.computeSD(u.getSeriesPer(FormTeam))/100, "Per");
			listSD.put(u.computeSD(u.getSeriesGap(FormTeam)), "Gap");		
			
			sdNew = u.totalSDs(listSD);

			if (sdNew <= sdOld) {
				StepsOfSwap.add(u.lowestSDMap(temptValues));
			}
			else {
				System.out.println("******************************* Suggestions ***************");
				System.out.println("Follow the list of pair of students below to swap students:");
				System.out.println(StepsOfSwap);
				System.out.println("***********************************************************");
				data.Suggestionsdata(DBNAME, TBSUGGESTION, StepsOfSwap);
				success = true;
			}
		}
	}
	
	////////////////////////////////////////////////Algorithms for 1st and 2nd preferences/////////////////////////////////
	//Swap students to improve SD in 1st and 2nd preferences
	private void swapPre() {
		int worstTeam;
		worstTeam = Integer.parseInt(u.lowestSDMap(u.getSeriesPer(FormTeam)));

		//Try to finds possible swaps between list of preference students to the worstTeam in other teams 
		//and list of unpreference students in the worstTeam
		swapForType(PreStu(worstTeam), notPreStu(worstTeam), worstTeam, "Preferences");
	}

	////////////////////////////////////////////////Algorithms for Skills Gap/////////////////////////////////
	//Swap students to improve SD in Skills Gap
	private void swapGap() {
		int worstTeam;
		worstTeam = Integer.parseInt(u.highestSDMap(u.getSeriesGap(FormTeam)));

		//Try to finds possible swaps between top 5 students suitable with the worstTeam in other teams 
		//and list of unsuitable students in the worstTeam
		swapForType(topGapStu(worstTeam), lowGapStu(worstTeam), worstTeam, "Skills Gap");
	}

	////////////////////////////////////////////////Algorithms for Skills Gap/////////////////////////////////
	//Swap students to improve SD in Skills Gap
	private void swapAvg() {
		int worstTeam;
		worstTeam = Integer.parseInt(u.lowestSDMap(u.getSeriesAvg(FormTeam)));

		//Try to finds possible swaps between top 5 students suitable with the worstTeam in other teams 
		//and list of unsuitable students in the worstTeam
		swapForType(topAvgStu(worstTeam), lowAvgStu(worstTeam), worstTeam, "Average  Competency Level");
	}

	///////////////////////////////////////////////Possible swaps/////////////////////////////////
	//We will find students in other teams to swap with worst team if possible
	private void swapForType(List<String> preStu, List<String> notpreStu, int worstTeam, String type) {
		boolean valid1, valid2;
		int teamNum = 0;
		String wID;
		Collections.shuffle(preStu);
		Collections.shuffle(notpreStu);

		for (String ID : preStu) {
			for (int i = 0; i < notpreStu.size(); i++) {
				wID = notpreStu.get(i);

				if (u.teamNumber(ID, FormTeam) != null)
					teamNum =  u.teamNumber(ID, FormTeam);

				u.remove(worstTeam, wID, FormTeam);
				u.remove(teamNum, ID, FormTeam);
				//Check all hard conditions before swap
				valid1 = Aerror.validSwapStuID(teamNum, wID, FormTeam, students, "Thread");
				valid2 = Aerror.validSwapStuID(worstTeam, ID, FormTeam, students, "Thread");

				//Swap if valid all conditions
				if(valid1 && valid2) {
					//Swap to improve % getting 1st and 2nd Preferences
					if (type.contains("Pre")) {
						//List of possible pair to swap
						PossibleSwap.add(wID + " " + ID);
					}
					//Swap to improve Skills Gap
					if (type.contains("Gap")) {
						//List of possible pair to swap
						PossibleSwap.add(wID + " " + ID);
					}
					//Swap to improve average competence level
					if (type.contains("Average")) { 
						//List of possible pair to swap
						PossibleSwap.add(wID + " " + ID);
					}
				}
				
				u.put(worstTeam, wID, FormTeam, students);
				if(FormTeam.get(teamNum - 1).getTeamMembers().size() < students.size()/FormTeam.size())
					u.put(teamNum, ID, FormTeam, students);
			}
		}
	}	

	////////////////////////List students can swap to improve SD in 1st and 2nd preferences//////////////////////////////
	//List of students who are preferences to the Team 
	private List<String> PreStu(Integer worstTeamNum){
		List<String> preStu = new ArrayList<String>();
		//Project ID in this team
		String worstPrID;
		worstPrID = FormTeam.get(worstTeamNum - 1).getProID();

		FormTeam.forEach(team -> {
			if(team.getTeamNum() != worstTeamNum) {
				team.getTeamMembers().keySet().forEach(k -> {
					if (students.get(k).getPreferences().get(worstPrID) != null) {
						if (students.get(k).getPreferences().get(worstPrID) == 4 
								|| students.get(k).getPreferences().get(worstPrID) == 3) 
							preStu.add(k);
					}
				});
			}
		});
		return preStu;
	}

	//List of students who are not preferences in the Team 
	private List<String> notPreStu(Integer worstTeamNum){
		List<String> notpreStu = new ArrayList<String>();
		//Project ID in this team
		String worstPrID;
		worstPrID = FormTeam.get(worstTeamNum - 1).getProID();

		FormTeam.forEach(team -> {
			if(team.getTeamNum() == worstTeamNum) {
				team.getTeamMembers().keySet().forEach(k -> {
					if (students.get(k).getPreferences().get(worstPrID) != null) {
						if (students.get(k).getPreferences().get(worstPrID) != 4 
								&& students.get(k).getPreferences().get(worstPrID) != 3) 
							notpreStu.add(k);
					}
					else
						notpreStu.add(k);
				});
			}
		});
		return notpreStu;
	}

	////////////////////////List students can swap to improve SD in Skills Gap//////////////////////////////
	//List of students who have top low skills gap scores (suitable students) other teams for the worst Team 
	private List<String> topGapStu(Integer worstTeamNum){
		List<String> top = new ArrayList<String>();
		HashMap<String, Integer> listStu = listStuBestGap(worstTeamNum);
		int index = 0;
		//save 5 students who are the most suitable for the team (low skill gap score)
		for (String key : listStu.keySet()) {
			index++;
			if (index < 5) {
				top.add(key);
			}
		}
		return top;
	}

	//List of half number of students who have high skills gap scores (unsuitable students) in the worst Team 
	private List<String> lowGapStu(Integer worstTeamNum){
		List<String> low = new ArrayList<String>();
		HashMap<String, Integer> listStu = listStuWorstGap(worstTeamNum);
		int index = 0;
		//save two students who are unsuitable for the team (high skill gap score)
		for (String key : listStu.keySet()) {
			index++;
			if (index > listStu.size()/2 - 1) {
				low.add(key);
			}
		}
		return low;
	}

	//Sort list of other students in other teams compute for skills short-fall (skills gap) in the Team, except members in the team
	private HashMap<String, Integer> listStuBestGap(Integer teamNum){
		HashMap<String, Integer> listStu = new HashMap<String, Integer>();

		FormTeam.forEach(team -> {
			if(team.getTeamNum() != teamNum) {
				listStu.putAll(team.getSkillGapStu());
			}
		});
		return u.intSorted(listStu);
	}

	//Sort list of students follow the order for skills short-fall (skills gap) in the Team
	private HashMap<String, Integer> listStuWorstGap(Integer teamNum){
		HashMap<String, Integer> listStu = new HashMap<String, Integer>();

		FormTeam.forEach(team -> {
			if(team.getTeamNum() == teamNum) {
				listStu.putAll(team.getSkillGapStu());
			}
		});
		return u.intSorted(listStu);
	}

	////////////////////////List students can swap to improve SD in Average  Competence Level//////////////////////////////

	//List of students who have top high scores (suitable students) other teams for the worst Team 
	private List<String> topAvgStu(Integer worstTeamNum){
		List<String> top = new ArrayList<String>();
		HashMap<String, Double> listStu = listStuBestAvg(worstTeamNum);
		int index = 0;
		//save 5 students who are the top scores in other teams
		for (String key : listStu.keySet()) {
			index++;
			if (index > listStu.size() - 5) {
				top.add(key);
			}
		}
		return top;
	}

	//List of half number of students who have low scores (unsuitable students) in the worst Team
	private List<String> lowAvgStu(Integer worstTeamNum){
		List<String> low = new ArrayList<String>();
		HashMap<String, Double> listStu = listStuWorstAvg(worstTeamNum);
		int index = 0;
		//save half of students who are the lowest score in the team
		for (String key : listStu.keySet()) {
			index++;
			if (index < listStu.size()/2 + 1) {
				low.add(key);
			}
		}
		return low;
	}

	//Sorted list of other students in other teams
	private HashMap<String, Double> listStuBestAvg(Integer teamNum){
		HashMap<String, Double> listStu = new HashMap<String, Double>();

		FormTeam.forEach(team -> {
			if(team.getTeamNum() != teamNum) {
				listStu.putAll(team.getAvgStu());
			}
		});
		return u.doubleSorted(listStu);
	}

	//Sorted list of other students in other teams
	private HashMap<String, Double> listStuWorstAvg(Integer teamNum){
		HashMap<String, Double> listStu = new HashMap<String, Double>();

		FormTeam.forEach(team -> {
			if(team.getTeamNum() == teamNum) {
				listStu.putAll(team.getAvgStu());
			}
		});
		return u.doubleSorted(listStu);
	}

	////////////////////////////////////////////////updated///////////////////////////////////////////

	private void update() {
		clear();
		try {
			FormTeam = data.readFromTeam("formTeam.dat");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		seriesAvg = u.getSeriesAvg(FormTeam);
		seriesPer = u.getSeriesPer(FormTeam);
		seriesGap = u.getSeriesGap(FormTeam);
		//List SDs of 3 team-metrics
		listSD.put(u.computeSD(u.getSeriesAvg(FormTeam)), "Avg");
		listSD.put(u.computeSD(u.getSeriesPer(FormTeam))/100, "Per");
		listSD.put(u.computeSD(u.getSeriesGap(FormTeam)), "Gap");	
	}

	private void clear() {
		FormTeam.clear();
		seriesAvg.clear();
		seriesPer.clear();
		seriesGap.clear();
		listSD.clear();
		PossibleSwap.clear();
	}
}
