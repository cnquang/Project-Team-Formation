package data;

import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
import java.util.stream.*;

import model.Company;
import model.Owner;
import model.Project;
import model.Student;
import model.Team;

public class Data {

	////////////////////////////////////////////////Database SQLite///////////////////////////////////////////
	////////////////////////////////////////////////   Write      ///////////////////////////////////////////
	//create 2 tables (student + project)
	public void createTables(String dbName, String tbCompany, String tbOwner, String tbStuName, String tbProName) {
		//use try-with-resources Statement
		try (Connection con = getConnection(dbName); Statement st = con.createStatement();) {
			//Deleting tables with these names as below if exist
			st.executeUpdate("DROP TABLE IF EXISTS " + tbCompany);
			st.executeUpdate("DROP TABLE IF EXISTS " + tbOwner);
			st.executeUpdate("DROP TABLE IF EXISTS " + tbStuName);
			st.executeUpdate("DROP TABLE IF EXISTS " + tbProName);
			
			//create table Company
			st.executeUpdate("create table " + tbCompany + " ("
					+ "COMPANY_ID char(10) primary key NOT NULL, COMPANY_NAME char(100), ABN_NUMBER char(30), " 
					+ "COMPANY_URL char(500), COMPANY_ADDRESS char(500))");
			//create table Project Owner
			st.executeUpdate("create table " + tbOwner + " ("
					+ "OWNER_ID char(10) primary key NOT NULL, FIRST_NAME char(30), LAST_NAME char(30), " 
					+ "ROLE char(30), EMAIL char(10), COMPANY_ID char(10))");
			//create table student
			st.executeUpdate("create table " + tbStuName + " ("
					+ "STUDENT_ID char(10) primary key NOT NULL, FIRST_NAME char(30), LAST_NAME char(30), " 
					+ "GRADE char(30), PERSONALITY_TYPE char(10), CONFLICT char(10), PREFERENCES char(30))");
			//create table project
			st.executeUpdate("create table " + tbProName + " ("
					+ "PROJECT_ID char(10) primary key, TITLE char(500), DESCRIPTION char(500), " 
					+ "PROJECT_OWNER_ID char(10), RANKING_SKILLS char(30))");
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private Connection getConnection(String dbName) throws SQLException {
		String url = "jdbc:sqlite:/Users/quangnhat/Desktop/Advanced/ProjectTeamFormation/database/"+dbName;
		
		//Database files will be created in the "database" folder
		Connection con = DriverManager.getConnection(url);
				
		return con;
	}
	
	public void insertData(String dbName, String tbCompany, String tbOwner, String tbStuName, String tbProName,
			HashMap<String,Company> companies, HashMap<String,Owner> owners,
			HashMap<String,Student> students, HashMap<String,Project> projects) {
		//use try-with-resources Statement
		try (Connection con = getConnection(dbName); Statement st = con.createStatement();) {
			String insertTbCom = "INSERT INTO " + tbCompany;
			String insertTbOwn = "INSERT INTO " + tbOwner;
			String insertTbStu = "INSERT INTO " + tbStuName;
			String insertTbPro = "INSERT INTO " + tbProName;
			
			//insert multiple rows of company table
			companies.values().forEach(x -> {
				try {
					st.addBatch(insertTbCom + " VALUES ('" + x.getcID() + "', " + "'" + x.getcName() + "'," 
							+ "'" + x.getcABN() + "'," + "'" + x.getcURL() + "'," + "'" + x.getcAddress() + "')");
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			});
			
			//insert multiple rows of project owner table
			owners.values().forEach(x -> {
				try {
					st.addBatch(insertTbOwn + " VALUES ('" + x.getOwnID() + "', " + "'" + x.getOwnfirstname() + "'," 
							+ "'" + x.getOwnsurname() + "'," + "'" + x.getOwnRole() + "'," + "'" + x.getOwnEmail() + "'," 
							+ "'" + x.getcomID() + "')");
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			});
			
			//insert multiple rows of student table
			students.values().forEach(x -> {
				try {
					st.addBatch(insertTbStu + " VALUES ('" + x.getstuID() + "', " + "'" + x.getStuFirstname() + "'," 
							+ "'" + x.getStuSurname() + "'," + "'" + x.toStringGrades() + "'," + "'" + x.getperType() + "'," 
							+ "'" + x.toStringConflicts() + "'," + "'" + x.toStringPreferences() + "'"+ ")");
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			});
			
			//insert multiple rows of project table
			projects.values().forEach(x -> {
				try {
					st.addBatch(insertTbPro + " VALUES ('" + x.getprID() + "', " + "'" + x.gettitle() + "'," 
							+ "'" + x.getDescription() + "'," + "'" + x.getOwnID() + "'," 
							+ "'" + x.toStringRankings() + "'" + ")");
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			});
			//start the execution of all the statements grouped together
			st.executeBatch();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//Create table FormTeam
	public void formTeamdata(String dbName, String tbFormTeam, List<Team> FormTeam) {
		//use try-with-resources Statement
		try (Connection con = getConnection(dbName); Statement st = con.createStatement();) {
			//Deleting tables with these names as below if exist
			st.executeUpdate("DROP TABLE IF EXISTS " + tbFormTeam);
			//create table form_team reference with projectID, if any remove projectID, Form_team row with this project also remove
			st.executeUpdate("create table " + tbFormTeam + " ("
					+ "TEAM_ID char(10) primary key, "
					+ "PROJECT_ID char(10) references tbProName(PROJECT_ID) on update cascade on delete cascade," 
					+ "MEMBERS char(250))");
			
			String insertTbPro = "INSERT INTO " + tbFormTeam;
			//insert multiple rows of project table
			FormTeam.forEach(x -> {
				try {
					st.addBatch(insertTbPro + " VALUES ('" + "T" + x.getTeamNum() + "', " + "'" + x.getProject().getprID() + "'," 
							+ "'" + x.toStringMembers() + "')");
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			});
			//start the execution of all the statements grouped together
			st.executeBatch();
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//Create table Suggestion
	public void Suggestionsdata(String dbName, String tbSuggestion, List<String> StepsOfSwap) {
		//use try-with-resources Statement
		try (Connection con = getConnection(dbName); Statement st = con.createStatement();) {
			//Deleting tables with these names as below if exist
			st.executeUpdate("DROP TABLE IF EXISTS " + tbSuggestion);
			//create table form_team reference with projectID, if any remove projectID, Form_team row with this project also remove
			st.executeUpdate("create table " + tbSuggestion + " ("
					+ "STEP_ID char(10) primary key, "
					+ "SWAP char(10))");
			
			String insertTbPro = "INSERT INTO " + tbSuggestion;
			int index = 0;
			//insert multiple rows of project table
			for(String k : StepsOfSwap) {
				try {
					index++;
					st.addBatch(insertTbPro + " VALUES ('" + index + "', " + "'" + k + "')");
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
			}
			//start the execution of all the statements grouped together
			st.executeBatch();
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	////////////////////////////////////////////////Database SQLite///////////////////////////////////////////
	//////////////////////////////////////////////// Read & Print ///////////////////////////////////////////
	
	//Print all tables in our database
	public void printAllTables(String dbName, String tbCompany, String tbOwner, 
			String tbStuName, String tbProName, String tbFormTeam) {
		//use try-with-resources Statement
		try (Connection con = getConnection(dbName); Statement st = con.createStatement();) {
			printTable(tbCompany,st);
			printTable(tbOwner,st);
			printTable(tbStuName,st);
			printTable(tbProName,st);
			printTable(tbFormTeam,st);
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//Print each table in database 
	private void printTable(String tbName, Statement st) {
		System.out.println("******************* Displaying " + tbName + " table ************************");
		
		try {
			String query = "SELECT * FROM " + tbName;
			
			ResultSet rs = st.executeQuery(query);
			ResultSetMetaData rsMD = rs.getMetaData();
			
			if(tbName.contains("PROJECT")) {
				for (int i=1; i<= rsMD.getColumnCount(); i++)
					System.out.printf("%-80s",rsMD.getColumnName(i)); //Minimum length, left-justified
				System.out.println();
				
				//Read each row of the table
				while (rs.next()) {
					for (int i = 1; i <= rsMD.getColumnCount(); i++)
						System.out.printf("%-80s",rs.getString(i));
					System.out.println();
				}
				System.out.println();
			}
			else {
				for (int i = 1; i <= rsMD.getColumnCount(); i++)
					System.out.printf("%-20s",rsMD.getColumnName(i)); //Minimum length, left-justified
				System.out.println();
				
				//Read each row of the table
				while (rs.next()) {
					for (int i=1; i<= rsMD.getColumnCount(); i++)
						System.out.printf("%-20s",rs.getString(i));
					System.out.println();
				}
				System.out.println();
			}
		} 
		catch( SQLException e ) {
			System.out.println(e.getMessage());
		}
	}
	
	//Print join Project table and FormTeam table
	public void joinFTeamProject(String dbName, String tbProName, String tbFormTeam) {
		
		System.out.println("******************* Joining " + tbProName + " and " + tbFormTeam  + " tables ************************");
		
		//use try-with-resources Statement
		try (Connection con = getConnection(dbName); Statement st = con.createStatement();) {

			String query = "select " + tbProName + ".PROJECT_ID," + tbFormTeam + ".TEAM_ID,"
					+ tbFormTeam + ".MEMBERS, " + tbProName + ".TITLE"
					+ " from " + tbProName + " left outer join " + tbFormTeam 
					+ " ON " + tbProName + ".PROJECT_ID = " + tbFormTeam + ".PROJECT_ID";
			
			try (ResultSet rs = st.executeQuery(query)) {
				while(rs.next()) {
					String proID = rs.getString(1);
					String teamID = rs.getString(2);
					String members = rs.getString(3);
					String proTitle = rs.getString(4);
					
					System.out.printf("project_ID: %s  |  team_ID: %s  |  members: %s  |  project_Title: %s\n",
							proID, teamID, members, proTitle);
				}
				System.out.println();
			} 
			catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//Print members in each team with details of each student
	public void printFormTeamMember(String dbName, String tbStuName, String tbFormTeam) {
		HashMap<String, String> listmembers = new HashMap<String, String>();
		listmembers = stringMembers(dbName, tbFormTeam);
		
		try (Connection con = getConnection(dbName); Statement st = con.createStatement();) {
			
			//loop for each team
			for (String x : listmembers.keySet()) {
				System.out.println("*** Members in team: " + x + " ****");
				
				String[] stuID = listmembers.get(x).split(" ");
				
				//loop for each student in members of team
				for (int i = 0; i < stuID.length; i++) {
					String query = "select * from " + tbStuName + " where STUDENT_ID = '" + stuID[i] + "'";
					try {
						ResultSet rs = st.executeQuery(query);
						while(rs.next()) {
							System.out.printf("Student_ID: %s  |  First_name: %s  |  Last_name: %s \n",
									rs.getString("STUDENT_ID"), rs.getString("FIRST_NAME"), rs.getString("LAST_NAME"));
						}
					} 
					catch (SQLException e1) {
						System.out.println(e1.getMessage());
					}
				}
				System.out.println();
			}
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	//Read data from FormTeam table and save members in HashMap
	public HashMap<String, String> stringMembers(String dbName, String tbFormTeam) {
		HashMap<String, String> m = new HashMap<String, String>();
		//use try-with-resources Statement
		try (Connection con = getConnection(dbName); Statement st = con.createStatement();) {
			String query = "select * from " + tbFormTeam;
			
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				String teamID = rs.getString(1);
				String members = rs.getString(3);
				m.put(teamID, members);
			}
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return m;
	}
	
	//Read data from SUGGESTION table and save step in HashMap
	public HashMap<Integer, String> readStepsOfSwap(String dbName, String tbName) {
		HashMap<Integer, String> m = new HashMap<Integer, String>();
		//use try-with-resources Statement
		try (Connection con = getConnection(dbName); Statement st = con.createStatement();) {
			String query = "select * from " + tbName;
			
			ResultSet rs = st.executeQuery(query);
			while(rs.next()) {
				Integer stepID = Integer.parseInt(rs.getString(1));
				String members = rs.getString(2);
				m.put(stepID, members);
			}
		} 
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return m;
	}
	
	////////////////////////////////////////////////Serialization ///////////////////////////////////////////
	////////////////////////////////////////////////Save ///////////////////////////////////////////
	//Save all teams and projects after finish
	public void saveFormTeam(List<Team> FormTeam, String path) {
		try (ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream(path))){
			out.writeObject(FormTeam);
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	//Read files from .txt files and save .dat file
	public void serializationSave (String stuIfoPath, String stuPrePath, String proPath) throws Exception {
		HashMap<String,Project> pros = readProject(proPath);
		HashMap<String,Student> stus = readStuFiles(stuIfoPath, stuPrePath);

		//try-with, the file will close automatically
		try (ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream("StusProjs.dat"))){
			out.writeObject(stus);
			out.writeObject(pros);
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	////////////////////////////////////////////////Serialization ///////////////////////////////////////////
	////////////////////////////////////////////////Read///////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public List<Team> readFromTeam(String path) throws ClassNotFoundException{
		List<Team> FormTeam = new LinkedList<Team>();
		try (ObjectInputStream in = new ObjectInputStream (new FileInputStream(path))){
			FormTeam = (LinkedList<Team>) in.readObject();
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
		return FormTeam;
	}
	
	//Read from files .dat
	@SuppressWarnings({ "unused", "unchecked" })
	public HashMap<String, Project> serialReadPro (String inPath) throws Exception {
		HashMap<String, Project> projects = new HashMap<String, Project>();
		HashMap<String, Student> students = new HashMap<String, Student>();
		
		try (ObjectInputStream in = new ObjectInputStream (new FileInputStream(inPath))){
			students = (HashMap<String, Student>) in.readObject();
			projects = (HashMap<String, Project>) in.readObject();
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
		return projects;
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Student> serialReadStu (String inPath) throws Exception {
		HashMap<String, Student> hm = new HashMap<String, Student>();
		try (ObjectInputStream in = new ObjectInputStream (new FileInputStream(inPath))){
			hm = (HashMap<String, Student>) in.readObject();
		}
		catch(IOException e)
		{
			System.out.println(e.getMessage());
		}
		return hm;
	}

	///////////////////////////////////////// READ TXT FILES ////////////////////////////////////////////////////////
	//Read file studentinfo.txt and preferences.txt
	public HashMap<String,Student> readStuFiles(String stuInfPath, String stuPrePath) throws FileNotFoundException{
		Scanner read = null;
		Map<String,Student> stu = new HashMap<String, Student>();
		try{

			Stream <String> rows = Files.lines(Paths.get(stuInfPath));
			stu = rows.map(x -> x.split(" "))
					.collect(Collectors.toMap(x -> x[0], x ->{
						Student s = new Student(x[0], x[1], x[2]);
						s.setGrades(x[3], x[4]);
						s.setGrades(x[5], x[6]);
						s.setGrades(x[7], x[8]);
						s.setGrades(x[9], x[10]);
						s.setPerType(x[11]);
						for(int i = 12; i < x.length; i++ ) {
							s.setConflicts(x[i]);
						}
						return s;
					}));
			rows.close();

			read = new Scanner(new File(stuPrePath));
			//read all lines in preferences.txt file
			while(read.hasNext()) {

				String lineID = read.nextLine(); //Line Student ID, first name, and surname
				String line = read.nextLine();
				String[] eID = lineID.split(" ");
				String[] pro = line.split(" ");
				for (int i = 0; i < pro.length; i += 2) {
					stu.get(eID[0]).setPreferences(pro[i], Integer.parseInt(pro[i+1]));
				}
			}
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			read.close();
		}
		return (HashMap<String, Student>) stu;
	}

	//Read file students.txt
	public HashMap<String,Student> readStudent(String inpath) throws IOException{
		Map<String,Student> stu = new HashMap<String, Student>();

		try{
			Stream <String> rows = Files.lines(Paths.get(inpath));
			stu = rows.map(x -> x.split(" "))
					.collect(Collectors.toMap(x -> x[0], x -> {
						Student s = new Student(x[0], x[1], x[2]);
						s.setGrades(x[3], x[4]);
						s.setGrades(x[5], x[6]);
						s.setGrades(x[7], x[8]);
						s.setGrades(x[9], x[10]);
						return s;
					}));
			rows.close();			

		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return (HashMap<String, Student>) stu;	
	}

	//Read file owners.txt
	public HashMap<String,Owner> readOwner(String inpath) throws IOException{
		Map<String,Owner> own = new HashMap<String, Owner>();
		try{
			Stream <String> rows = Files.lines(Paths.get(inpath));

			own = rows.map(x -> x.split(";"))
					.collect(Collectors.toMap(x -> x[0], x -> new Owner(x[0], x[1], x[2],x[3],x[4],x[5])));
			rows.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return (HashMap<String, Owner>) own;
	}
	
	//Read file companiess.txt
	public HashMap<String,Company> readCompany(String inpath) throws IOException{
		Map<String,Company> com = new HashMap<String, Company>();
		try{
			Stream <String> rows = Files.lines(Paths.get(inpath));

			com = rows.map(x -> x.split(";"))
					.collect(Collectors.toMap(x -> x[0], x -> new Company(x[0], x[1], x[2],x[3],x[4])));
			rows.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return (HashMap<String, Company>) com;
	}

	//Read file projects.txt
	public HashMap<String,Project>readProject(String inpath) throws FileNotFoundException{
		HashMap<String,Project> pro = new HashMap<String, Project>();
		Scanner read = null;
		try{
			read = new Scanner(new File(inpath));

			//read all lines in students.txt file
			while(read.hasNext()) {
				String titleLine = read.nextLine();
				String prIDLine = read.nextLine(); 
				String desLine = read.nextLine(); 
				String ownIDLine = read.nextLine(); 
				String rankLine = read.nextLine(); 
				String e[] = rankLine.split(" ");

				Project p = new Project(titleLine, prIDLine, desLine,ownIDLine);
				p.setRanking(e[0], e[1]);
				p.setRanking(e[2], e[3]);
				p.setRanking(e[4], e[5]);
				p.setRanking(e[6], e[7]);
				pro.put(prIDLine, p);
			}
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			read.close();
		}
		return pro;
	}
	
	//Check whether file is exist or not
	public void checkFile(String path) throws Exception{
		Scanner read = null;
		try {
			read = new Scanner(new File(path));
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			read.close();
		}
	}
}
