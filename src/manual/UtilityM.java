package manual;

import model.*;
import java.util.*;
import java.util.stream.*;

import data.Data;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UtilityM {
	private Data data = new Data();
	private Error error = new Error(); //Check own Exceptions
	
	private HashMap<String,Company> companies = new HashMap<String, Company>();
	private HashMap<String,Owner> owners = new HashMap<String, Owner>();
	private HashMap<String,Project> projects = new HashMap<String, Project>();
	private HashMap<String,Student> students = new HashMap<String, Student>();

	int numCom = 1;
	int numOwn = 1;
	int numPro = 1;
	
	//////////////////////////////////Milestone 1///////////////////////////////////////////
	//A. write data "Add Company" to companies.txt *****
	public void AddCompany(File path) throws Exception {
		
		boolean valid = false;
		String ID;
			
		FileWriter out = new FileWriter(path, true); //Set true for append mode
		PrintWriter output = new PrintWriter(out);
			
		//Enter input from keyboard
		Scanner input;
		Scanner sc;
			
		while(!valid) {
			//Enter input from keyboard
			sc = new Scanner(System.in);
			input = new Scanner(System.in);	
			try {
				System.out.println("Enter a unique company ID (e.g. C" + numCom +", ...): ");
				ID = input.nextLine();
				error.checkComID(ID);
				
				//check Company ID exist or not yet
				while (companies.get(ID) != null) {
					System.out.println("Company " + companies.get(ID) + " is exist. " + "Enter another unique company ID (e.g. C" + numCom +", ...): ");
					ID = input.nextLine();
					error.checkComID(ID);
				}
				
				System.out.println("Enter company name: ");
				String Name = input.nextLine();
								
				System.out.println("Enter company ABN number: ");
				long ABN = sc.nextLong();
				error.checkComABN(ABN);
								
				System.out.println("Enter company URL: ");
				String URL = input.nextLine();
				error.checkComURL(URL);
							
				System.out.println("Enter company address: ");
				String Address = input.nextLine();

				valid = true;
				numCom++; //count number of company added
				
				companies.put(ID,new Company(ID, Name, Long.toString(ABN), URL, Address));
				
				output.print(ID + ";");
				output.print(Name + ";");
				output.print(ABN + ";");
				output.print(URL + ";");
				output.println(Address);
				output.close();
			}
			catch(CompanyIDException e) {
				System.out.println(e.getMessage());
			}
			catch(CompanyABNException e) {
				System.out.println(e.getMessage());
			}
			catch(CompanyURLException e) {
				System.out.println(e.getMessage());
			}
			catch(Exception e) {
				System.out.println("Invalid input! Try agian!");
			}
		}
	}

	//B. write data "Add Project Owner" to owners.txt ******
	public void AddPrOwner(File path) throws Exception {
		boolean valid = false;
			
		FileWriter out = new FileWriter(path, true); //Set true for append mode
		PrintWriter output = new PrintWriter(out);
				
		//Enter input from keyboard
		Scanner input; 
			
		while(!valid) {
			try {
				input = new Scanner(System.in);
				System.out.println("Enter a unique project owner ID  (e.g. Own" + numOwn + ", ...): ");
				String ownID = input.nextLine();
				error.checkOwnID(ownID);
				
				//check Project Owner ID exist or not yet
				while (owners.get(ownID) != null) {
					System.out.println("Company " + companies.get(ownID) + " is exist. " + "Enter another unique project owner ID (e.g. C" + numOwn +", ...): ");
					ownID = input.nextLine();
					error.checkComID(ownID);
				}
				
				System.out.println("Enter first name: ");
				String firstname = input.nextLine();
									
				System.out.println("Enter surname: ");
				String surname = input.nextLine();
						
				System.out.println("Enter  role (such as software engineer): ");
				String role = input.nextLine();
					
				System.out.println("Enter email: ");
				String email = input.nextLine();
				error.checkEmail(email);
									
				System.out.println("Enter the ID of the company the project owner represents (of the form C1, ...): ");
				String comID = input.nextLine();
				error.checkComID(comID);
				
				while (companies.get(comID) == null) {
					System.out.println("Company ID is not exist! Please enter the another ID of the company the project owner represents (of the form C1, ...): ");
					comID = input.nextLine();
					error.checkComID(comID);
				}
					
				valid = true;
				numOwn++;
				
				owners.put(ownID,new Owner(ownID, firstname, surname, role, email, comID));
				
				output.print(ownID + ";");
				output.print(firstname + ";");
				output.print(surname + ";");
				output.print(role + ";");
				output.print(email + ";");
				output.println(comID);
				output.close();
			}
			catch(OwnerIDException e) {
				System.out.println(e.getMessage());
			}
			catch(EmailException e) {
				System.out.println(e.getMessage());
			}
			catch(CompanyIDException e) {
				System.out.println(e.getMessage());
			}
			catch(Exception e) {
				System.out.println("Invalid input! Try agian!");
			}
		}
	}
	
	//C. write data "Add Project" to projects.txt ******
	public void AddProject(File path) throws Exception {
		
		owners = data.readOwner("owners.txt");
		
		boolean valid = false;
		
		FileWriter out = new FileWriter(path, false); //Set false for append mode
		PrintWriter output = new PrintWriter(out);
					
		//Enter input from keyboard
		Scanner input;  
		Scanner sc;
			
		while(!valid) {
			
			HashMap<Integer, String> rank = new HashMap<Integer, String>(); // Ranking of the skills (only one with rank 4,3,2,and 1)
			
			try {
				input = new Scanner(System.in);
				sc = new Scanner(System.in);
				System.out.println("Enter title of project " + numPro + ": ");
				String title = input.nextLine();
				
				System.out.println("Enter unique project ID (of the form Pr" + numPro + ", ...): ");
				String prID = input.nextLine();
				error.checkProjectID(prID);
				
				//check Project ID exist or not yet
				while (projects.get(prID) != null) {
					System.out.println("Project ID: " + prID + " is exist. " + "Enter another unique project ID (e.g. C" + numPro +", ...): ");
					prID = input.nextLine();
					error.checkProjectID(prID);
				}
										
				System.out.println("Enter brief description (one line): ");
				String description = input.nextLine();
										
				System.out.println("Enter ID of the project owner (of the form Own" + numPro + ", ...): ");
				String ownID = input.nextLine();
				error.checkOwnID(ownID);
				
				//Check user enter Owner ID exist or not
				while(owners.get(ownID) == null) {
					System.out.println("The ownID is not exist! Please enter another ID of the project owner (of the form Own1, ...): ");
					ownID = input.nextLine();
					error.checkOwnID(ownID);
				}
									
				System.out.println("Enter Ranking Technical Skill Categories (4 being the highest and 1 the lowest)");
				System.out.println("Enter Ranking (P) Programming & Software Engineering (from 4 to 1): ");
				int Prank = sc.nextInt();
				error.checkRank(Prank);
				rank.put(Prank, "P");
					
				System.out.println("Enter Ranking (N) Networking and Security (from 4 to 1): ");
				int Nrank = sc.nextInt();
				error.checkRank(Nrank);
				
				while (rank.get(Nrank) != null) {
					System.out.println("Rank " + Nrank + " is exist. Please enter another Ranking (N) Networking and Security (from 4 to 1): ");
					Nrank = sc.nextInt();
					error.checkRank(Nrank);
				}
				rank.put(Nrank, "N");
					
				System.out.println("Enter Ranking (A) Analytics and Big Data (from 4 to 1): ");
				int Arank = sc.nextInt();
				error.checkRank(Arank);
				
				while (rank.get(Arank) != null) {
					System.out.println("Rank " + Arank + " is exist. Please enter another Ranking (N) Networking and Security (from 4 to 1): ");
					Arank = sc.nextInt();
					error.checkRank(Arank);
				}
				rank.put(Arank, "A");
				
				System.out.println("Enter Ranking (W) Web & Mobile applications (from 4 to 1): ");
				int Wrank = sc.nextInt();
				error.checkRank(Wrank);
				
				while (rank.get(Wrank) != null) {
					System.out.println("Rank " + Wrank + " is exist. Please enter another Ranking (N) Networking and Security (from 4 to 1): ");
					Wrank = sc.nextInt();
					error.checkRank(Wrank);
				}
				rank.put(Wrank, "W");
					
				valid = true;
				numPro++;
				
				projects.put(prID, new Project(title, prID, description, ownID));
				
				output.println(title);
				output.println(prID);
				output.println(description);
				output.println(ownID);
				output.print("P " + Prank + " ");
				output.print("N " + Nrank + " ");
				output.print("A " + Arank + " ");
				output.println("W " + Wrank);
			}
			catch(OwnerIDException e) {
				System.out.println(e.getMessage());
			}
			catch(ProjectIDException e) {
				System.out.println(e.getMessage());
			}
			catch(RankException e) {
				System.out.println(e.getMessage());
			}
			catch(Exception e) {
				System.out.println("Invalid input! Try agian!");
			}
		}
		output.close();
	}
	
	//D. Capture Student Personalities & Conflicts ***** input students.txt, output studentinfo.txt
	public void interview(String inpath,File outpath) throws Exception{
		//Read students.txt file and save in students HashMap
		students = data.readStudent(inpath);
		
		int countA = 0, countB = 0, countC = 0, countD = 0;
		String type = null;
		String A = "A", B = "B", C = "C", D = "D";
		int numConflict = 0, i;
		boolean valid = false;
		
		Scanner read = new Scanner(new File(inpath));
	
		FileWriter out = new FileWriter(outpath, false); //Set false for append mode
		PrintWriter output = new PrintWriter(out);
			
		while(read.hasNext()) {
			String eachline = read.nextLine();
			output.print(eachline);
			
			String e[] = eachline.split(" ");
			String ID = e[0];
			String firstname = e[1];
			int num = 1;
				
			Scanner input;
				
			valid = false;
			while(!valid) {
				try {
					input = new Scanner(System.in);
					//Assign students personality types
					System.out.println("What is *" + ID + "-" + firstname +"* personality type? (" + A + B + C + D + ")");
					type = input.nextLine();
					error.checkPersonalType(type);
						
					if(type.charAt(0) == 'A' && countA < 5 ) {
						countA += 1;
						output.print(type + " ");
						students.get(ID).setPerType(type);
					}
					else if(type.charAt(0) == 'B' && countB < 5) {
						countB += 1;
						output.print(type + " ");
						students.get(ID).setPerType(type);
					}
					else if(type.charAt(0) == 'C' && countC < 5) {
						countC += 1;
						output.print(type + " ");
						students.get(ID).setPerType(type);
					}
					else if(type.charAt(0) == 'D' && countD < 5) {
						countD += 1;
						output.print(type + " ");
						students.get(ID).setPerType(type);
					}
					else if(countA == 5 || countB == 5 || countC == 5 || countD == 5) {
						System.out.println("Number of students with personality types " + type + " is full");
						System.out.println("Please assign again!");
						System.out.println("What is *" + ID + "-" + firstname +"* personality type? (" + A + B + C + D + ")");
						type = input.nextLine();
						error.checkPersonalType(type);
						output.print(type + " ");
						students.get(ID).setPerType(type);
					}
						
					if (countA == 5) A ="";
					if (countB == 5) B ="";
					if (countC == 5) C ="";
					if (countD == 5) D ="";
						
					valid = true;
				}
				catch(PersonalTypeException ep) {
					System.out.println(ep.getMessage());
				}
			}
			
			Scanner sc;
			boolean check = false;
			valid = false;
			while(!valid) {
				try {
					input = new Scanner(System.in);
					while(!check) {
						System.out.println("How many students that you can not work with in a team? (up to 2)");
						//Enter the number of conflicts student
						sc = new Scanner(System.in);
						// Check option input have to a integer number
						while (!sc.hasNextInt()) {
							System.out.println("You have to put an integer option number from 0 to 2: ");
							sc.next();
						}
						
						numConflict = sc.nextInt();
						
						//check available option
						if (numConflict < 0 || numConflict > 2) {
							System.out.println("Invalid input. You have to put an integer option number from 0 to 2.");
							check = false;
						}
						else {
							check = true;
						}
					}
					
					//Add conflict students
					String conflict;
					for ( i = num; i <= numConflict; i++) {
						
						System.out.println("Student *" + ID + "-" + firstname +"* conflicts with student ID, " + i + " (e.g. S12,...): ");
						conflict = input.nextLine();
						error.checkStuID(conflict);
						error.sameID(ID, conflict);//have to different ID
						
						//Check conflicts students ID have in the list
						while (students.get(conflict) == null) {
							System.out.println("Student *" + conflict + " ID is not exist");
							System.out.println("Try agian! Student *" + ID + "-" + firstname +"* conflicts with student ID, " + i + ": ");
							error.checkStuID(conflict);
							error.sameID(ID, conflict);//have to different ID
						}
						
						num++;
						output.print(conflict + " ");
						students.get(ID).setConflicts(conflict);
					}
					
					valid = true;
					output.println("");	
				}
				catch(StudentIDException ex) {
					System.out.println(ex.getMessage());
				}
				catch(Exception ex) {
					System.out.println("Invalid input! Try agian!");
				}
			}
		}
		output.close();
	}
	
	//E. Capture Student Preferences ***** input students.txt, output preferences.txt
	public void stuPreference(String inpath,File outpath) throws Exception{
		//Read students.txt file and save in students HashMap
		students = data.readStudent(inpath);
		
		Scanner read = new Scanner(new File(inpath));
				
		FileWriter out = new FileWriter(outpath, false); //Set false for append mode
		PrintWriter output = new PrintWriter(out);
				
		String pre;
		int score;
		boolean check, valid;
				
		while(read.hasNext()){
			//only the final preference should be counted
			HashMap<String, Integer> HMproject = new HashMap<String, Integer>();
			String eachline = read.nextLine();
			String e[] = eachline.split(" ");
			String ID = e[0];
			String firstname = e[1];
			String surname = e[2];

			//ask each student for their preferred
			Scanner input;
			Scanner sc;
			System.out.println(ID + " " + firstname + "  assigns preferences.");
			
			valid = false;
			while(!valid) {
				try {
					do {
						sc = new Scanner(System.in);
						input = new Scanner(System.in);
						System.out.println("Enter the project from Pr1 to Pr10:");
						pre = input.nextLine();
						error.checkProjectIDLimit(pre);
						
						do { //check available input
							System.out.println("Enter the score (4 for most preferred and 1 for the least) of " + pre + ":");
							// Check option input have to a integer number
							while (!sc.hasNextInt()) {
								System.out.println("You have to put an integer option number from 1 to 4: ");
								sc.next();
							}
						
							score = sc.nextInt();

							if (score < 1 || score > 4) {
								System.out.println("Invalid input. You have to put an integer score number from 1 to 4.");
								check = false;
							}
							else {
								check = true;
							}
						}while(!check);
								
						HMproject.put(pre,score);
								
						System.out.println("Do you want to continue? (Y/N)");
						String con = input.nextLine();
						if(con.charAt(0) == 'Y' || con.charAt(0) == 'y') {
							check = true;
						}
						else check = false;
					}while (check);

					valid = true;				
				}
				catch(ProjectIDLimitException ex) {
					System.out.println(ex.getMessage());
				}
				catch(Exception ex) {
					ex.printStackTrace();
				}
			}

			//write final preference in the text file
			output.println(ID + " " + firstname + " " + surname);
			
			//TreeMap to follow the project alphabet order
			TreeMap<String, Integer>  treeMap = new TreeMap<String, Integer> (HMproject);
			treeMap.keySet().forEach(x -> {
				output.print(x + " " + treeMap.get(x) + " ");
				students.get(ID).setPreferences(x, treeMap.get(x));
			});
			output.println("");
		}
		output.close();
	}

	//F1. Summed for all the projects from student references *****
	public void sumProject(String inpath,File outpath) throws Exception{
		Scanner read = new Scanner(new File(inpath));
					
		FileWriter out = new FileWriter(outpath, false); //Set false for append mode
		PrintWriter output = new PrintWriter(out);
			
		//save projects and total scores of each project 
		HashMap<String, Integer> HM = new HashMap<String, Integer>();
			
		while(read.hasNext()) {
				
			read.nextLine(); //Line Student ID, first name, and surname
				
			String PROline = read.nextLine(); //Line project scores
			String e[] = PROline.split(" ");
			
			for( int i = 0; i < e.length; i += 2) {
				String proID = e[i];
				int score = Integer.parseInt(e[i+1]);
					
				//check proID exist or not yet
				if(HM.get(proID) != null) {
					int total = ((Integer)HM.get(proID)).intValue(); 
					total += score;
					HM.put(proID, total);
				}
				else HM.put(proID, score);
			}
		}
			
		//TreeMap to follow the project order
		TreeMap<String, Integer> treeMap = new TreeMap<String, Integer>(HM);
		treeMap.keySet().forEach(x -> {
			output.println(x + " " + treeMap.get(x));
		});
		
		output.close();
	}
		
	//F2. Short_list Projects based on Preferences *****
	public void fiveProjects(String inpath,File outpath) throws Exception{

		projects = data.readProject("projects.txt");
		
		
		FileWriter out = new FileWriter(outpath, false); //Set false for append mode
		PrintWriter output = new PrintWriter(out);
		
		Stream<String> rows = Files.lines(Paths.get(inpath));
		Map<String, Double> hashmap = new HashMap<String, Double>(); //HashMap for data in preferences.txt 
		Map<Double, String> oppositeHm = new HashMap<Double, String>(); //HashMap for data in preferences.txt 

		hashmap = rows.map(x -> x.split(" "))
				.collect(Collectors.toMap(x -> x[0], x -> Double.parseDouble(x[1])));
		rows.close();
		
		double add = 0.0; //Solve the problem when many projects have same scores
		for(String k : hashmap.keySet()) {
			double score = hashmap.get(k);
			if (oppositeHm.get(score) != null) {
				add += 0.0001;
				oppositeHm.put(score + add, k);
			}else oppositeHm.put(score, k);
		}
			
		//TreeMap to follow the scores order
		TreeMap<Double, String> treeMap = new TreeMap<Double, String>(oppositeHm); 
		
		//treeMap.keySet().stream().skip(5);
		
		Iterator<Double> iterator = treeMap.keySet().iterator();               
		
		//Remove 5 least popular projects
		int remove = (oppositeHm.size() - 5);
		for (int i = 0; i < remove; i++) {    
			double key = (double)iterator.next();  
			oppositeHm.remove(key);
		}   
			
		//export final five projects
		oppositeHm.keySet().forEach(x -> {
			output.println(projects.get(oppositeHm.get(x)).toString());
		});
			
		output.close();
	}
	
	////////////////////////////////////////////////Standard deviation ///////////////////////////////////////////
	public double computeSD(ArrayList<Double> x) {
		double total = x.stream().mapToDouble(y -> y).sum();
		double mean = total/x.size();
		double sum = x.stream().mapToDouble(y -> Math.pow(y-mean, 2)).sum();

		return Math.sqrt(sum/x.size());
	}
	
	////////////////////////////////////// MENU ////////////////////////////////////////////
	public void printMenu() {
		String[] menu = {"********* MENU *********",
						"A. * Add Company *",
						"B. * Add Project Owner *",
						"C. * Add Project *",
						"D. * Capture Student Personalities & Conflicts *",
						"E. * Add Student Preferences *",
						"F. * Shortlist Project *",
						"G. * Form Teams *",
						"H. * Display Team Fitness Metrics *",
						"I. * GUI *",
						"Q. * Quit *"};
		for(int i = 0; i < menu.length; i++){
			System.out.println(menu[i]);
		}
	}
	
	public void printSubMenu() {
		String[] menu = {"********* SUB-MENU *********",
						"H1. * Average student skill competency for each project team *",
						"H2. * Percentage of students who got their first and second preferences in each of the teams. *",
						"H3. * Skills shortfall for each project *",
						"D. * Done! Display Team Fitness Metrics *"};
		for(int i = 0; i < menu.length; i++){
			System.out.println(menu[i]);
		}
	}
	
	//Check valid Option in Menu
	public boolean validOptionMenu(String opt) {
		if (opt.length() != 1) {
			//System.out.println("\"You have to put a character option from A to Q: \"");
			return false;
		} 
		else if (opt.charAt(0) != 'A' && opt.charAt(0) != 'a'
				&&	opt.charAt(0) != 'B' && opt.charAt(0) != 'b'
				&&	opt.charAt(0) != 'C' && opt.charAt(0) != 'c'
				&&	opt.charAt(0) != 'D' && opt.charAt(0) != 'd'
				&&	opt.charAt(0) != 'E' && opt.charAt(0) != 'e'
				&&	opt.charAt(0) != 'F' && opt.charAt(0) != 'f'
				&&	opt.charAt(0) != 'G' && opt.charAt(0) != 'g'
				&&	opt.charAt(0) != 'H' && opt.charAt(0) != 'h'
				&&	opt.charAt(0) != 'I' && opt.charAt(0) != 'i'
				&&	opt.charAt(0) != 'Q' && opt.charAt(0) != 'q') {
			System.out.println("\"You have to put a character option from A to Q: \"");
			return false;
		}
		else {
			return true;
		} 
	}
	//Check valid Option in Menu
	public boolean validOptionSubMenu(String opt) {
		if (opt.length() > 2) {
			//System.out.println("\"You have to put a character option from A to Q: \"");
			return false;
		} 
		else if (opt.equals("H1") == false && opt.equals("h1") == false
				&&	opt.equals("H2") == false && opt.equals("h2") == false
				&&	opt.equals("H3") == false && opt.equals("h3") == false
				&&	opt.charAt(0) != 'D' && opt.charAt(0) != 'd') {
			System.out.println("\"You have to put a character option from A to Q: \"");
			return false;
		}
		else {
			return true;
		} 
	}
		
	//print Characteristics & Personality Type	
	public void ListPersonalityType() {
		String[] perType = {"**** Personality Type. * Characteristics ****",
							"A. * Likes to be a Leader (Director) *",
							"B. * Outgoing and maintains good relationships (Socializer) *",
							"C. * Detail oriented (Thinker) *",
							"D. * Less assertive (Supporter) *"};
		//print List Personality Type
		for(int i = 0; i < perType.length; i++){
				System.out.println(perType[i]);
		}
	}
}



