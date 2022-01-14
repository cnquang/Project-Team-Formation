package main;

import model.*;
import java.util.*;
import application.*;
import data.Data;
import javafx.application.*;
import manual.*;
import java.io.*;

public class ProjectTeamMain {

	public static void main(String[] args) throws Exception {
		
		UtilityM utility = new UtilityM();
		Data data = new Data();
		FormTeamM formTeamM = new FormTeamM();
		HashMap<Project,Team> FT = null;
		
		Scanner input; //Enter input from keyboard
		String option = null; //input option from menu
		boolean check; //check valid option
		String con = "Y"; //continue work
		final String STUDENT_FILE_NAME = "students.txt";
		final String STUDENT_INFO_FILE_NAME = "studentinfo.txt";
		final String STUDENT_PREFERENCE_FILE_NAME = "preferences.txt";
		final String COMPANY_FILE_NAME = "companies.txt";
		final String OWNER_FILE_NAME = "owners.txt";
		final String PROJECT_FILE_NAME = "projects.txt";
		
		File comfile = new File(COMPANY_FILE_NAME); //Create companies.txt 
		File ownfile = new File(OWNER_FILE_NAME); //Create companies.txt 
		File profile = new File(PROJECT_FILE_NAME); //Create projects.txt 
		File stuInfo = new File(STUDENT_INFO_FILE_NAME); //Create projects.txt 
		File preference = new File(STUDENT_PREFERENCE_FILE_NAME); //Create projects.txt
		File sumpro = new File("sumprojects.txt"); //Create sumprojects.txt summed for all the projects 
		
		while(true) {
			
			//Enter until option is valid from menu
			check = false;
			while (!check) {
				input = new Scanner(System.in);
				utility.printMenu();
				
				System.out.println("Enter your option from A to Q: ");
				//enter option from keyboard
				option = input.nextLine();
				
				if (utility.validOptionMenu(option))
		            check = true;
				else {
					System.out.println();
		            System.out.println("Warning! Not valid option, enter your option again");
		            check = false;
		        }
			}
			
			//*** A. Add Company ***
			if (option.charAt(0) == 'A' || option.charAt(0) == 'a') {
				input = new Scanner(System.in);
				while (con.charAt(0) == 'Y'|| con.charAt(0) == 'y') {
					System.out.println("-----Start to Add Company details-----");
					
					utility.AddCompany(comfile);
					
					System.out.println("Do you want to continue to Add a new Company? (Y/N)");
					con = input.nextLine();
				}			
				con = "Y";
			}
			
			//*** B. Add Project Owner ***
			if (option.charAt(0) == 'B' || option.charAt(0) == 'b') {
				input = new Scanner(System.in);
				while (con.charAt(0) == 'Y'|| con.charAt(0) == 'y') {
					System.out.println("-----Start to Add Project Owner details-----");
					utility.AddPrOwner(ownfile);
					
					System.out.println("Do you want to continue to Add Project Owner? (Y/N)");
					con = input.nextLine();
				}
				con = "Y";
			}
			
			//*** C. Add Project ***
			if (option.charAt(0) == 'C' || option.charAt(0) == 'c') {
				input = new Scanner(System.in);
				while (con.charAt(0) == 'Y'|| con.charAt(0) == 'y') {
					System.out.println("-----Start to Add Project details-----");
					utility.AddProject(profile);
					
					System.out.println("Do you want to continue to Add Project? (Y/N)");
					con = input.nextLine();
				}
				con = "Y";
			}
			
			//*** D. Capture Student Personalities & Conflicts ***
			if (option.charAt(0) == 'D' || option.charAt(0) == 'd') {
				//check file student.txt is exist or not
				data.checkFile(STUDENT_FILE_NAME);
				//print Characteristics & Personality Type	
				utility.ListPersonalityType();
				System.out.println("-----Start interviews each of the students in the list-----");
				utility.interview(STUDENT_FILE_NAME, stuInfo);
			}
			
			//*** E. Capture Student Preferences ***
			if (option.charAt(0) == 'E' || option.charAt(0) == 'e') {
				//check file student.txt is exist or not
				data.checkFile(STUDENT_FILE_NAME);
				System.out.println("----- Start -----");
				utility.stuPreference(STUDENT_FILE_NAME, preference);
			}
			
			//*** F. Short_list Projects based on Preferences ***
			if (option.charAt(0) == 'F' || option.charAt(0) == 'f') {
				
				data.checkFile(STUDENT_PREFERENCE_FILE_NAME);
				data.checkFile("sumprojects.txt");
				
				//Sum all project
				utility.sumProject(STUDENT_PREFERENCE_FILE_NAME, sumpro);
				
				//short list projects
				utility.fiveProjects("sumprojects.txt", profile);
			}
			
			//*** G. Form Teams ***
			if (option.charAt(0) == 'G' || option.charAt(0) == 'g') {
				
				data.checkFile(STUDENT_INFO_FILE_NAME);
				data.checkFile(STUDENT_PREFERENCE_FILE_NAME);
				data.checkFile(PROJECT_FILE_NAME);
				
				FT = formTeamM.formTeam(STUDENT_INFO_FILE_NAME, STUDENT_PREFERENCE_FILE_NAME, PROJECT_FILE_NAME);
				
				System.out.println("Form Teams done!");
				for(Project p : FT.keySet()) {
					System.out.println(p.getprID() + ": Team " + FT.get(p).getTeamNum() + ": " + FT.get(p).getTeamMembers().values());
				}
			}
			
			//*** H. Display Team Fitness Metrics ***
			if (option.charAt(0) == 'H' || option.charAt(0) == 'h') {
				try {
					if (FT.size() == 5){
						boolean loop = false;
						while(!loop) {
							//Enter until option is valid from menu
							check = false;
							while (!check) {
								input = new Scanner(System.in);
								utility.printSubMenu();
								
								System.out.println("Enter your option from H1 to H3 (D for done): ");
								//enter option from keyboard
								option = input.nextLine();
								
								if (utility.validOptionSubMenu(option))
						            check = true;
								else {
									System.out.println();
						            System.out.println("Warning! Not valid option, enter your option again.");
						            check = false;
						        }
							}
							
							//*** H1. Average student skill competence for each project team  ***
							if (option.equals("H1") || option.equals("h1")) {
								System.out.println("Result average student skill competence for each project team: ");
								formTeamM.displayAvgStuSkill(FT);
								
								System.out.println("Standard deviation for average student skill competence across projects: ");
								System.out.println("SD = " + utility.computeSD(formTeamM.getXAvgSkill()));
								System.out.println();
							}
							
							//*** H2. Percentage of students who got their first and second preferences in each of the teams.  ***
							if (option.equals("H2") || option.equals("h2")) {
								System.out.println("Result percentage of students who got their first and second preferences in each of the teams: ");
								formTeamM.percentageOfStu(FT);
								
								System.out.println("Standard deviation for percentage of project members getting first and second project "
										+ "preferences across projects : ");
								System.out.println("SD = " + utility.computeSD(formTeamM.getXPerOfPre()));
								System.out.println();
							}
							
							//*** H3. Skills shortfall for each project based on categories.  ***
							if (option.equals("H3") || option.equals("h3")) {
								System.out.println("Result skills shortfall for each project based on categories: ");
								formTeamM.skillShortfall(FT);
								
								System.out.println("Standard deviation of shortfall across teams: ");
								System.out.println("SD = " + utility.computeSD(formTeamM.getXShortfall()));
								System.out.println();
							}
						
							//*** D. Done ***
							if (option.charAt(0) == 'D' || option.charAt(0) == 'd') {
								loop = true;
							}
						}
					}
					else System.out.println("All teams are not formed. Please *Form Team* first! e");
				}
				catch (NullPointerException e) {
					System.out.println("All teams are not formed. Please *Form Team* first!");
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			//*** I. GUI ***
			if (option.charAt(0) == 'I' || option.charAt(0) == 'i') {
				Application.launch(GUI.class, args);
			}
			
			//*** Q. Quit ***
			if (option.charAt(0) == 'Q' || option.charAt(0) == 'q') {
				break;
			}
		}
	}
}



