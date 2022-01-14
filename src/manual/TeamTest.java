package manual;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.*;

import data.Data;
import model.*;

public class TeamTest {
	
	private Team t, t2;
	private Project p, p2;
	private UtilityM um = new UtilityM();
	private Data data = new Data();
	private Error error = new Error();
	private Student s1, s2, s3, s4, s5, s6, s7, s8;
	private HashMap<String,Student> stus = new HashMap<String, Student>();
	private HashMap<String,Student> AvailableStus = new HashMap<String, Student>();
	private HashMap<String,Student> members = new HashMap<String, Student>();

	@Before
	public void setUp() throws Exception {
		AvailableStus = data.readStuFiles("studentinfo.txt", "preferences.txt");
		
		p = new Project("Title", "Pr1", "all members work on pr1", "Own1");
		
		s1 = new Student("S1", "Istiaq", "Ahmed");
		s2 = new Student("S2", "Amit", "Uprety");
		s3 = new Student("S3", "Tsitso", "Mocumi");
		s4 = new Student("S4", "Sri", "Riwu");
		
		s1.setConflicts("S5"); s1.setConflicts("S11"); 
		s2.setConflicts("S6"); s2.setConflicts("S12");
		s3.setConflicts("S7"); s3.setConflicts("S13");
		s4.setConflicts("S8"); s4.setConflicts("S14");
		
		stus.put("S1", s1);
		stus.put("S2", s2);
		stus.put("S3", s3);
		stus.put("S4", s4);
		
		AvailableStus.remove("S1"); AvailableStus.remove("S2"); AvailableStus.remove("S3"); AvailableStus.remove("S4");
		
		t = new Team(1,stus, p);
		
		//Spend for some negative test cases
		p2 = new Project("Title2", "Pr2", "all members work on pr2", "Own2");
		p2.setRanking("P", "10"); p2.setRanking("N", "10"); p2.setRanking("W", "10"); p2.setRanking("A", "10");
		
		s5 = new Student("S5", "Is", "Ah");
		s6 = new Student("S6", "Am", "Up");
		s7 = new Student("S7", "Ts", "Mo");
		s8 = new Student("S8", "S", "Ri");
		
		members.put("S5", s5); 
		members.put("S6", s6); 
		members.put("S7", s7); 
		members.put("S8", s8);

		t2 = new Team(2,members, p2);
	}

	@After
	public void tearDown() throws Exception {
	}

	//*** a. Average skill competency for a team is computed correctly
	@Test
	public void testAvgStuSkillPos1() throws OverAverageSkillException {
		s1.setGrades("P", "4"); s1.setGrades("N", "3"); s1.setGrades("W", "2"); s1.setGrades("A", "1");
		s2.setGrades("P", "1"); s2.setGrades("N", "2"); s2.setGrades("W", "3"); s2.setGrades("A", "4");
		s3.setGrades("P", "1"); s3.setGrades("N", "3"); s3.setGrades("W", "4"); s3.setGrades("A", "2");
		s4.setGrades("P", "4"); s4.setGrades("N", "1"); s4.setGrades("W", "2"); s4.setGrades("A", "3");
		
		t.AvgStuSkill();
		assertEquals("Test average skill (positive)", 2.5, t.getAveStuSkill(), 0.001);
	}
	
	@Test
	public void testAvgStuSkillPos2() throws OverAverageSkillException {
		s1.setGrades("P", "2"); s1.setGrades("N", "2"); s1.setGrades("W", "2"); s1.setGrades("A", "2");
		s2.setGrades("P", "2"); s2.setGrades("N", "2"); s2.setGrades("W", "2"); s2.setGrades("A", "2");
		s3.setGrades("P", "2"); s3.setGrades("N", "2"); s3.setGrades("W", "2"); s3.setGrades("A", "2");
		s4.setGrades("P", "2"); s4.setGrades("N", "2"); s4.setGrades("W", "2"); s4.setGrades("A", "2");
		
		t.AvgStuSkill();
		assertEquals("Test average skill (positive)", 2, t.getAveStuSkill(), 0.001);
	}
	
	//*** b. Percentage of project members getting first and second project preference is computed correctly
	@Test
	public void testPerOfStuPos1() throws OverPercentageLimitException {
		s1.setPreferences("Pr1", 4);
		s2.setPreferences("Pr1", 3);
		s3.setPreferences("Pr1", 3);
		s4.setPreferences("Pr1", 4);
		
		t.PerOfStu();
		assertEquals("Test percentage of project members (positive)", 100.0, t.getsPercentage(), 0.001);
	}
	
	@Test
	public void testPerOfStuPos2() throws OverPercentageLimitException {
		s1.setPreferences("Pr2", 4);
		s2.setPreferences("Pr2", 3);
		s3.setPreferences("Pr2", 3);
		s4.setPreferences("Pr2", 4);
		
		t.PerOfStu();
		assertEquals("Test percentage of project members (positive)", 0.0, t.getsPercentage(), 0.001);
	}
	
	//*** c. Skill shortfall for any team is computed correctly.
	@Test
	public void testskillShortfallPos1() throws OverAverageSkillException, OverSkillShortfallException {
		p.setRanking("P", "4"); p.setRanking("N", "3"); p.setRanking("W", "2"); p.setRanking("A", "1");
		
		s1.setGrades("P", "4"); s1.setGrades("N", "3"); s1.setGrades("W", "2"); s1.setGrades("A", "1");
		s2.setGrades("P", "1"); s2.setGrades("N", "2"); s2.setGrades("W", "3"); s2.setGrades("A", "4");
		s3.setGrades("P", "1"); s3.setGrades("N", "3"); s3.setGrades("W", "4"); s3.setGrades("A", "2");
		s4.setGrades("P", "4"); s4.setGrades("N", "1"); s4.setGrades("W", "2"); s4.setGrades("A", "3");
		
		t.AvgStuSkill();
		assertEquals("Test skill shortfall (positive)", 2.25, t.skillShortfall(), 0.001);
	}
	
	@Test
	public void testskillShortfallPos2() throws OverAverageSkillException, OverSkillShortfallException {
		p.setRanking("P", "4"); p.setRanking("N", "3"); p.setRanking("W", "2"); p.setRanking("A", "1");
		
		s1.setGrades("P", "2"); s1.setGrades("N", "2"); s1.setGrades("W", "2"); s1.setGrades("A", "2");
		s2.setGrades("P", "2"); s2.setGrades("N", "2"); s2.setGrades("W", "2"); s2.setGrades("A", "2");
		s3.setGrades("P", "2"); s3.setGrades("N", "2"); s3.setGrades("W", "2"); s3.setGrades("A", "2");
		s4.setGrades("P", "2"); s4.setGrades("N", "2"); s4.setGrades("W", "2"); s4.setGrades("A", "2");
		
		t.AvgStuSkill();
		assertEquals("Test skill shortfall (positive)", 3, t.skillShortfall(), 0.001);
	}
	
	//*** d. InvalidMember exception thrown when an attempt is made to add a student already assigned to another project team
	@Test
	public void testInvalidMemberPos1()  throws InvalidMemberException{
		error.checkInvalidMember(AvailableStus, "S20");
	}
	
	@Test
	public void testInvalidMemberPos2()  throws InvalidMemberException{
		error.checkInvalidMember(AvailableStus, "S19");
	}
	
	@Test
	public void testInvalidMemberPos3()  throws InvalidMemberException{
		error.checkInvalidMember(AvailableStus, "S11");
	}
	
	@Test
	public void testInvalidMemberPos4()  throws InvalidMemberException{
		error.checkInvalidMember(AvailableStus, "S9");
	}
	
	@Test(expected = InvalidMemberException.class)
	public void testInvalidMemberNeg1()  throws InvalidMemberException{
		error.checkInvalidMember(AvailableStus, "S1");
		fail("Invalid Member");
	}
	
	@Test(expected = InvalidMemberException.class)
	public void testInvalidMemberNeg2()  throws InvalidMemberException{
		error.checkInvalidMember(AvailableStus, "S2");
		fail("Invalid Member");
	}
	
	@Test(expected = InvalidMemberException.class)
	public void testInvalidMemberNeg3()  throws InvalidMemberException{
		error.checkInvalidMember(AvailableStus, "S3");
		fail("Invalid Member");
	}
	
	@Test(expected = InvalidMemberException.class)
	public void testInvalidMemberNeg4()  throws InvalidMemberException{
		error.checkInvalidMember(AvailableStus, "S4");
		fail("Invalid Member");
	}
	
	//*** e. StudentConflict exception thrown when students members who have indicated prior conflicts are assigned to the same team
	@Test
	public void testStuConflictExceptionPos1()  throws StudentConflictException{
		for(String ID : t.getTeamMembers().keySet()) {
			error.checkConflictStudent(AvailableStus.get("S19"),t.getTeamMembers().get(ID));
		}
	}
	
	@Test
	public void testStuConflictExceptionPos2()  throws StudentConflictException{
		for(String ID : t.getTeamMembers().keySet()) {
			error.checkConflictStudent(AvailableStus.get("S20"),t.getTeamMembers().get(ID));
		}
	}
	
	@Test(expected = StudentConflictException.class)
	public void testStuConflictExceptionNeg1()  throws StudentConflictException{
		for(String ID : t.getTeamMembers().keySet()) {
			error.checkConflictStudent(AvailableStus.get("S6"),t.getTeamMembers().get(ID));
		}
		fail("Conflict Student");
	}
	
	@Test(expected = StudentConflictException.class)
	public void testStuConflictExceptionNeg2()  throws StudentConflictException{
		for(String ID : t.getTeamMembers().keySet()) {
			error.checkConflictStudent(AvailableStus.get("S11"),t.getTeamMembers().get(ID));
		}
		fail("Conflict Student");
	}
	
	//*** f. PersonalityImbalance exception whenever a team has less than three different personality types.
	@Test
	public void testPerImbalanceExceptionPos1()  throws PersonalityImbalanceException{
		s1.setPerType("A");
		s2.setPerType("B");
		s3.setPerType("C");
		s4.setPerType("B");
		
		error.checkPerImbalance(t.getTeamMembers());
	}
	
	@Test
	public void testPerImbalanceExceptionPos2()  throws PersonalityImbalanceException{
		s1.setPerType("A");
		s2.setPerType("B");
		s3.setPerType("B");
		s4.setPerType("D");
		
		error.checkPerImbalance(t.getTeamMembers());
	}
	
	@Test(expected = PersonalityImbalanceException.class)
	public void testPerImbalanceExceptionNeg1()  throws PersonalityImbalanceException{
		s5.setPerType("A");
		s6.setPerType("B");
		s7.setPerType("B");
		s8.setPerType("B");
		
		error.checkPerImbalance(t2.getTeamMembers());
		fail("Personality Imbalance in this team");
	}
	
	@Test(expected = PersonalityImbalanceException.class)
	public void testPerImbalanceExceptionNeg2()  throws PersonalityImbalanceException{
		s5.setPerType("A");
		s6.setPerType("B");
		s7.setPerType("B");
		s8.setPerType("A");
		
		error.checkPerImbalance(t2.getTeamMembers());
		fail("Personality Imbalance in this team");
	}
	
	//*** g. RepeatedMember exception thrown when an attempt is made to add a student twice to the same team
	@Test
	public void testReMemberExceptionPos1()  throws RepeatedMemberException{
		error.checkRepeatedMember(t.getTeamMembers(),"S20");
	}
	
	@Test
	public void testReMemberExceptionPos2()  throws RepeatedMemberException{
		error.checkRepeatedMember(t.getTeamMembers(),"S10");
	}
	
	@Test(expected = RepeatedMemberException.class)
	public void testReMemberExceptionNeg1()  throws RepeatedMemberException{
		error.checkRepeatedMember(t.getTeamMembers(),"S2");
		fail("Repeat s student twice in this team");
	}
	
	@Test(expected = RepeatedMemberException.class)
	public void testReMemberExceptionNeg2()  throws RepeatedMemberException{
		error.checkRepeatedMember(t.getTeamMembers(),"S1");
		fail("Repeat s student twice in this team");
	}
	
	//*** h. NoLeader exception thrown when a team does not have a leader personality type 
	@Test
	public void testNoLeaderExceptionPos1()  throws NoLeaderException{
		s1.setPerType("A");
		s2.setPerType("B");
		s3.setPerType("B");
		s4.setPerType("D");
		
		error.checkNoLeader(t.getTeamMembers());
	}
	
	@Test
	public void testNoLeaderExceptionPos2()  throws NoLeaderException{
		s1.setPerType("B");
		s2.setPerType("B");
		s3.setPerType("A");
		s4.setPerType("D");
		
		error.checkNoLeader(t.getTeamMembers());
	}
	
	@Test(expected = NoLeaderException.class)
	public void testNoLeaderExceptionNeg1()  throws NoLeaderException{
		s5.setPerType("B");
		s6.setPerType("B");
		s7.setPerType("D");
		s8.setPerType("D");
		
		error.checkNoLeader(t2.getTeamMembers());
		fail("No leader in this team");
	}
	
	@Test(expected = NoLeaderException.class)
	public void testNoLeaderExceptionNeg2()  throws NoLeaderException{
		s5.setPerType("B");
		s6.setPerType("B");
		s7.setPerType("C");
		s8.setPerType("D");
		
		error.checkNoLeader(t2.getTeamMembers());
		fail("No leader in this team");
	}
	
	//*** i. Standard deviation in skill competency across projects computed correctly.
	@Test
	public void testSDAdvSkillPos1() throws OverAverageSkillException {
		ArrayList<Double> xAvgSkill = new ArrayList<Double>();
		xAvgSkill.add(2.0); //assume adv student competence for team 1
		xAvgSkill.add(3.0); //assume adv student competence for team 2
		xAvgSkill.add(1.0); //assume adv student competence for team 3
		xAvgSkill.add(2.5); //assume adv student competence for team 4
		xAvgSkill.add(3.0); //assume adv student competence for team 5
	
		assertEquals("Test SD in skill competency (positive)", 0.74833147735, um.computeSD(xAvgSkill), 0.00001);
	}
	
	@Test
	public void testSDAdvSkillPos2() throws OverAverageSkillException {
		ArrayList<Double> xAvgSkill = new ArrayList<Double>();
		xAvgSkill.add(2.0); //assume adv student competence for team 1
		xAvgSkill.add(2.0); //assume adv student competence for team 2
		xAvgSkill.add(2.0); //assume adv student competence for team 3
		xAvgSkill.add(2.0); //assume adv student competence for team 4
		xAvgSkill.add(2.0); //assume adv student competence for team 5
	
		assertEquals("Test SD in skill competency (positive)", 0.0, um.computeSD(xAvgSkill), 0.00001);
	}
	
	//*** j. Standard deviation for percentage of project members getting first and second project preferences across projects
	@Test
	public void testSDPerPrePos1() throws OverPercentageLimitException {
		ArrayList<Double> xPerPre = new ArrayList<Double>();
		xPerPre.add(50.0); //assume percentage of project members getting first and second project preferences for team 1
		xPerPre.add(50.0); //assume percentage of project members getting first and second projects preferences for team 2
		xPerPre.add(25.0); //assume percentage of project members getting first and second projects preferences for team 3
		xPerPre.add(0.0); //assume percentage of project members getting first and second projects preferences for team 4
		xPerPre.add(50.0); //assume percentage of project members getting first and second projects preferences for team 5
		
		assertEquals("Test SD for percentage of project members getting first and second project preferences "
				+ "(positive)", 20.0, um.computeSD(xPerPre), 0.00001);
	}
	
	@Test
	public void testSDPerPrePos2() throws OverPercentageLimitException {
		ArrayList<Double> xPerPre = new ArrayList<Double>();
		xPerPre.add(50.0); //assume percentage of project members getting first and second project preferences for team 1
		xPerPre.add(50.0); //assume percentage of project members getting first and second projects preferences for team 2
		xPerPre.add(50.0); //assume percentage of project members getting first and second projects preferences for team 3
		xPerPre.add(50.0); //assume percentage of project members getting first and second projects preferences for team 4
		xPerPre.add(50.0); //assume percentage of project members getting first and second projects preferences for team 5
		
		assertEquals("Test SD for percentage of project members getting first and second project preferences "
				+ "(positive)", 0.0, um.computeSD(xPerPre), 0.00001);
	}
	
	//k. Standard deviation of shortfall across teams is computed correctly.
	@Test
	public void testSDShortfallPos1() throws OverSkillShortfallException {
		ArrayList<Double> xShortfall = new ArrayList<Double>();
		xShortfall.add(2.75); //assume percentage of project members getting first and second project preferences for team 1
		xShortfall.add(2.5); //assume percentage of project members getting first and second projects preferences for team 2
		xShortfall.add(3.75); //assume percentage of project members getting first and second projects preferences for team 3
		xShortfall.add(3.5); //assume percentage of project members getting first and second projects preferences for team 4
		xShortfall.add(2.75); //assume percentage of project members getting first and second projects preferences for team 5
		
		assertEquals("Test SD for percentage of project members getting first and second project preferences "
				+ "(positive)", 0.48476798574163293, um.computeSD(xShortfall), 0.00001);
	}
	
	@Test
	public void testSDShortfallPos2() throws OverSkillShortfallException {
		ArrayList<Double> xShortfall = new ArrayList<Double>();
		xShortfall.add(2.0); //assume percentage of project members getting first and second project preferences for team 1
		xShortfall.add(2.0); //assume percentage of project members getting first and second projects preferences for team 2
		xShortfall.add(2.0); //assume percentage of project members getting first and second projects preferences for team 3
		xShortfall.add(2.0); //assume percentage of project members getting first and second projects preferences for team 4
		xShortfall.add(2.0); //assume percentage of project members getting first and second projects preferences for team 5
		
		assertEquals("Test SD for percentage of project members getting first and second project preferences "
				+ "(positive)", 0.0, um.computeSD(xShortfall), 0.00001);
	}
}
