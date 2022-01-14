package manual;

import model.*;
import java.util.*;
import java.util.stream.*;

import data.Data;

public class FormTeamM {
	
	private HashMap<String,Student> students = new HashMap<String, Student>();
	private HashMap<String,Project> projects = new HashMap<String, Project>();
	private ArrayList<Double> xAvgSkill = new ArrayList<Double>();
	private ArrayList<Double> xPerPre = new ArrayList<Double>();
	private ArrayList<Double> xShortfall = new ArrayList<Double>();
	
	private Error error = new Error();
	private Data data = new Data();
	
	////////////////////////////////// Milestone 2 ///////////////////////////////////////////
	//G. Form team 
	// Data from studentinfo.txt; preperences.txt; and projects.txt
	public HashMap<Project,Team> formTeam(String stuIfoPath, String stuPrePath, String proPath) throws Exception {
		
		Team T = null;
		
		HashMap<Project,Team> FTeam = new HashMap<Project,Team>();
		HashMap<String,Student> AvailableStus = new HashMap<String, Student>();
		List<Student> Leaders;
		
		data.serializationSave (stuIfoPath, stuPrePath, proPath);
		projects = data.serialReadPro("StusProjs.dat");
		students = data.serialReadStu("StusProjs.dat");

		AvailableStus = students;

		String stuID;
		String prID;
		boolean valid, leader;
		int i = 0;
		int numMember = students.size()/projects.size();
		
		Scanner input;
		
		Iterator<String> itr = projects.keySet().iterator();
		while (itr.hasNext()) {
			
			HashMap<String,Student> members = new HashMap<String, Student>();
			prID = itr.next();
			input = new Scanner(System.in);
			i++;
			int j = 1;
			valid = false;
			leader = false;
			System.out.println("--------------------- Team " + i + " ---------------------");
			
			while(!valid) {
				try {
					// Add leader student first, at least one leader type (A) per team
					while(!leader) {
						
						System.out.print("List of leader students: ");
						Leaders = (AvailableStus.values().stream()
								.filter(x -> x.getperType().equals("A")))
								.collect(Collectors.toList());
						Leaders.forEach(x -> System.out.print("[ " + x.getstuID() + " ] "));
						System.out.println();
						
						System.out.println("Enter the *leader* student ID for project " + prID + " (Student " + j + ") :");
						stuID = input.nextLine();
						error.checkStuID(stuID);
						error.checkInvalidMember(AvailableStus, stuID);//

						for(String ID : members.keySet()) {
							error.sameID(ID, stuID);
							error.checkConflictStudent(AvailableStus.get(stuID), members.get(ID));
							error.checkOverLeader(members.get(ID), AvailableStus.get(stuID));
						}
						
						if (Leaders.toString().contains(stuID)) {
							j++;
							members.put(stuID, AvailableStus.get(stuID));
							AvailableStus.remove(stuID);
							leader = true;						
						}
						else System.out.println("*** Please choose a leader student fist! ***");
					}
					
					// Add other 3 students per team
					while(members.keySet().size() < numMember) {
						// Add the rest of student members
						System.out.println("Enter the student ID for project " + prID + " (Student " + j + ") :");
						stuID = input.nextLine();
						error.checkStuID(stuID);
						error.checkInvalidMember(AvailableStus, stuID);
						error.checkPerImbalance(members, AvailableStus.get(stuID));
						error.checkRepeatedMember(members, stuID);
						error.checkNoLeader(members);
						
						for(String ID : members.keySet()) {
							error.sameID(ID, stuID);
							error.checkConflictStudent(AvailableStus.get(stuID), members.get(ID));
							error.checkOverLeader(members.get(ID), AvailableStus.get(stuID));
						}
						j++;
						members.put(stuID, AvailableStus.get(stuID));
						AvailableStus.remove(stuID);
					}
					valid = true;
				}
				catch (StudentIDException e) {
					System.out.println(e.getMessage());
				}
				catch (StudentConflictException e) {
					System.out.println(e.getMessage());
				}
				catch (OverLeaderException e) {
					System.out.println(e.getMessage());
				}
				catch (NoLeaderException e) {
					System.out.println(e.getMessage());
				}
				catch (RepeatedMemberException e) {
					System.out.println(e.getMessage());
				}
				catch (InvalidMemberException e) {
					System.out.println(e.getMessage());
				}
				catch (PersonalityImbalanceException e) {
					System.out.println(e.getMessage());
				}
				catch(Exception e) {
					System.out.println("Invalid input! Try agian!");
				}
			}
			T = new Team(i, members, projects.get(prID));
			FTeam.put(projects.get(prID), T);
		}
		return FTeam;
	}
	
	/////////////////////////// H. Display Team Fitness Metrics ///////////////////////////////////////////////////
	
	//H1. * Average student skill competence for each project team *
	public void displayAvgStuSkill(HashMap<Project,Team> FT) throws OverAverageSkillException{
		List<Team> teams = FT.values().stream()
				.collect(Collectors.toList());
		teams.forEach(x -> {
			x.AvgStuSkill();
		});
		teams.forEach(x -> System.out.println("Team " + x.getTeamNum()+ ": " + x.getAveStuSkill() + " " + x.getAvgSkill()));
		System.out.println();
		
		for (Team t : teams) {
			xAvgSkill.add(t.getAveStuSkill());
		}
	}
	
	//H2. * Percentage of students who got their first and second preferences in each of the teams. *
	public void percentageOfStu (HashMap<Project,Team> FT) throws OverPercentageLimitException {
		List<Team> firstAndSecondPre = FT.values().stream()
				.collect(Collectors.toList());
		firstAndSecondPre.forEach(x -> {
			System.out.println("Team " + x.getTeamNum()+ ": " + x.PerOfStu() + "%");
		});
		System.out.println();
		
		for (Team t : firstAndSecondPre) {
			xPerPre.add(t.getsPercentage());
		}
	}
	
	//H3. * Skills shortfall for each project based on categories. *
	public void skillShortfall (HashMap<Project,Team> FT) throws OverSkillShortfallException {
		List<Team> skillS = FT.values().stream()
				.collect(Collectors.toList());
		skillS.forEach(x -> {
			System.out.println("Team " + x.getTeamNum()+ ": the skills shorfall = " + x.skillShortfall());
		});
		System.out.println();
		
		for (Team t : skillS) {
			xShortfall.add(t.getsShortfall());
		}
	}
	
	public ArrayList<Double> getXAvgSkill(){
		return xAvgSkill;
	}
	
	public ArrayList<Double> getXPerOfPre(){
		return xPerPre;
	}
	
	public ArrayList<Double> getXShortfall(){
		return xShortfall;
	}
}


