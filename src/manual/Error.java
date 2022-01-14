package manual;

import java.util.ArrayList;
import java.util.HashMap;

import model.*;

public class Error {
	//Personality Type exception
	public void checkPersonalType(String s) throws PersonalTypeException{		
		boolean unvalid = true;
		if (s.charAt(0) == 'A' || s.charAt(0) == 'B' || s.charAt(0) == 'C' || s.charAt(0) == 'D' )
			unvalid = false;
		if(unvalid)
			throw new PersonalTypeException("*** Invalid input! Personality Type includes A, B, C, or D *** \n Try agian!");	
	}
	//Student ID exception
	public void checkStuID(String ID) throws StudentIDException{
		if (!ID.startsWith("S"))
			throw new StudentIDException("*** Invalid Student ID input! It must start with capital S. *** \n Try agian!");
		else if(ID.length() < 2)
			throw new StudentIDException("*** Length of Student ID at least 2 *** \n Try agian!");
		
		try {
			Integer.parseInt(ID.substring(1));
		}
		catch (NumberFormatException e){
			throw new StudentIDException("*** Student ID must start with capital S + number *** \n Try agian!");
		}
	}
	
	public void sameID(String oldID, String newID) throws StudentIDException{
		if (oldID.equals(newID))
			throw new StudentIDException("*** Invalid Student ID input! The student ID exist. *** \n Try agian!");
	}
	
	//company ID exception
	public void checkComID(String ID) throws CompanyIDException{
		if (!ID.startsWith("C"))
			throw new CompanyIDException("*** Company ID must start with capital C *** \n Try agian!");
		try {
			Integer.parseInt(ID.substring(1));
		}
		catch (NumberFormatException e){
			throw new CompanyIDException("*** Company ID must strat with capital C + number *** \n Try agian!");
		}
	}
			
	//company ABN exception
	public void checkComABN(long ABN) throws CompanyABNException{
		if ( Long.toString(ABN).length() != 11 )
			throw new CompanyABNException("*** ABN number must have 11 numbers *** \n Try agian!");
	}
			
	//company URL exception
	public void checkComURL(String URL) throws CompanyURLException{
		if (!URL.contains("."))
			throw new CompanyURLException("*** Invalid URL input! *** \n Try agian!");
	}
			
	//Owner ID exception
	public void checkOwnID(String ID) throws OwnerIDException{
		if (!ID.startsWith("Own"))
			throw new OwnerIDException("*** Invalid Owner ID input! It must start with Own. *** \n Try agian!");
		try {
			Integer.parseInt(ID.substring(3));
		}
		catch (NumberFormatException e){
			throw new OwnerIDException("*** Owner ID must strat with Own + number *** \n Try agian!");
		}
	}

	//Email exception
	public void checkEmail(String email) throws EmailException{
		if (!email.contains("@"))
			throw new EmailException("*** Invalid email input! *** \n Try agian!");
	}
			
	//Project ID exception
	public void checkProjectID(String ID) throws ProjectIDException{
		if (!ID.startsWith("Pr"))
			throw new ProjectIDException("*** Invalid Project ID input! It must start with Pr. *** \n Try agian!");
		try {
			Integer.parseInt(ID.substring(2));
		}
		catch (NumberFormatException e){
			throw new ProjectIDException("*** Project ID must strat with Pr + number *** \n Try agian!");
		}
	}
		
	//Project ID exception
	public void checkProjectIDLimit(String ID) throws ProjectIDLimitException{
		if (!ID.startsWith("Pr"))
			throw new ProjectIDLimitException("*** Invalid Project ID input! It must start with Pr. *** \n Try agian!");
		try {
			Integer.parseInt(ID.substring(2));
		}
		catch (NumberFormatException e){
			throw new ProjectIDLimitException("*** Project ID must strat with Pr + number *** \n Try agian!");
		}
		if (Integer.parseInt(ID.substring(2)) < 1 || Integer.parseInt(ID.substring(2)) > 10){
			throw new ProjectIDLimitException("*** Project ID is just from Pr1 to Pr10 *** \n Try agian!");
		}

	}
			
	//Rank/Score exception
	public void checkRank(int r) throws RankException{
		if (r < 1 || r > 4)
			throw new RankException("*** Invalid input! Rank must from 1 to 4 (4 being the highest and 1 the lowest) *** \n Try agian!");
	}

	//number of conflict students exception
	public void checkConflictNumber(int i) throws ConflictNumberException{
		if(i < 0 || i > 2)
			throw new ConflictNumberException("*** Invalid input! Just up to 2 conflict students *** \n Try agian!");	
	}
	
	//////////////////////////////////Milestone 2 ///////////////////////////////////////////
	
	public void checkConflictStudent(Student oldStu, Student newStu) throws StudentConflictException{
		if(oldStu.getConflicts().contains(newStu.getstuID()) || newStu.getConflicts().contains(oldStu.getstuID())) {
			throw new StudentConflictException("*** Conflicts with Student in this team! *** \n Try agian!");
		}
	}
	
	public void checkOverLeader(Student sOld, Student sNew) throws OverLeaderException{
		if(sNew.getperType().equals("A") && sOld.getperType().equals("A")) {
			throw new OverLeaderException("*** Just one leader type in this team. *** \n Try agian!");
		}
	}
	
	public void checkNoLeader(HashMap<String,Student> m) throws NoLeaderException{
		int i = 0;
		for(String k : m.keySet()) {
			if (m.get(k).getperType().equals("A") != true)
				i++;	
		}
		if (i == m.size())
			throw new NoLeaderException("*** No leader type in this team. *** \n Try agian!");
	}
	
	public void checkInvalidMember(HashMap<String,Student> m, String sID) throws InvalidMemberException{
		if (m.get(sID) == null) {
			throw new InvalidMemberException("*** Student " + sID +   " repeated. Please choose another student. *** "
					+ "\n Try agian!"
					+ "\n List students available: " + m.keySet());
		}
	}
	
	public void checkPerImbalance(HashMap<String,Student> m, Student newStu) throws PersonalityImbalanceException{
		int tA = 0;
		int tB = 0;
		int tC = 0;
		int tD = 0;
		
		if(newStu.getperType().equals("A")) tA++;
		else if(newStu.getperType().equals("B")) tB++;
		else if(newStu.getperType().equals("C")) tC++;
		else if(newStu.getperType().equals("D")) tD++;
		
		for(String k : m.keySet()) {
			if (m.get(k).getperType().equals("A")) {
				tA++;
			}
			if (m.get(k).getperType().equals("B")) {
				tB++;
			}
			if (m.get(k).getperType().equals("C")) {
				tC++;
			}
			if (m.get(k).getperType().equals("D")) {
				tD++;
			}
		}
		if (tA > 2 || tB > 2 || tC > 2 || tD > 2 || (tA + tB) == 4 || (tA + tC) == 4 || (tA + tD) == 4 
				|| (tB + tC) == 4 || (tB + tD) == 4 || (tC + tD) == 4) {
			throw new PersonalityImbalanceException("*** This team has less than three personality types! ***" );
		}	
	}
	
	public void checkPerImbalance(HashMap<String,Student> m) throws PersonalityImbalanceException{
		int tA = 0;
		int tB = 0;
		int tC = 0;
		int tD = 0;
		
		for(String k : m.keySet()) {
			if (m.get(k).getperType().equals("A")) {
				tA++;
			}
			if (m.get(k).getperType().equals("B")) {
				tB++;
			}
			if (m.get(k).getperType().equals("C")) {
				tC++;
			}
			if (m.get(k).getperType().equals("D")) {
				tD++;
			}
		}
		if (tA > 2 || tB > 2 || tC > 2 || tD > 2 || (tA + tB) == 4 || (tA + tC) == 4 || (tA + tD) == 4 
				|| (tB + tC) == 4 || (tB + tD) == 4 || (tC + tD) == 4) {
			throw new PersonalityImbalanceException("*** This team has less than three personality types! ***" );
		}	
	}
	
	public void checkRepeatedMember(HashMap<String,Student> m, String sID) throws RepeatedMemberException{
		if (m.get(sID) != null) {
			throw new RepeatedMemberException("*** Student " + sID +   " repeated in this team. Please choose another student! *** ");
		}
	}
	
	public void checkSDAvgSkillException(ArrayList<Double> x) throws OverAverageSkillException{
		for( int i = 0; i < x.size(); i++) {
			if (x.get(i) < 0.0 ||  x.get(i) > 4.0) {
				throw new OverAverageSkillException("Wrong input! over limit skill score!");
			}
		}
	}
	
	public void checkSDPreferenceException(ArrayList<Double> x) throws OverPercentageLimitException{
		for( int i = 0; i < x.size(); i++) {
			if (x.get(i) < 0.0 ||  x.get(i) > 100.0) {
				throw new OverPercentageLimitException("Wrong input! over limit skill score!");
			}
		}
	}
	
	public void checkSDShortfallException(ArrayList<Double> x) throws OverSkillShortfallException{
		for( int i = 0; i < x.size(); i++) {
			if (x.get(i) < 1 ||  x.get(i) > 4) {
				throw new OverSkillShortfallException("Wrong input! over limit skill score!");
			}
		}
	}
}

//Exceptions
class CompanyIDException extends Exception{
	private static final long serialVersionUID = 1L;
	public CompanyIDException(String message) {
		super(message);
	}
}

class OwnerIDException extends Exception{
	private static final long serialVersionUID = 1L;
	public OwnerIDException(String message) {
		super(message);
	}
}

class StudentIDException extends Exception{
	private static final long serialVersionUID = 1L;
	public StudentIDException(String message) {
		super(message);
	}
}

class CompanyABNException extends Exception{
	private static final long serialVersionUID = 1L;
	public CompanyABNException(String message) {
		super(message);
	}
}

class CompanyURLException extends Exception{
	private static final long serialVersionUID = 1L;
	public CompanyURLException(String message) {
		super(message);
	}
}

class EmailException extends Exception{
	private static final long serialVersionUID = 1L;
	public EmailException(String message) {
		super(message);
	}
}

class ProjectIDException extends Exception{
	private static final long serialVersionUID = 1L;
	public ProjectIDException(String message) {
		super(message);
	}
}

class ProjectIDLimitException extends Exception{
	private static final long serialVersionUID = 1L;
	public ProjectIDLimitException(String message) {
		super(message);
	}
}

class RankException extends Exception{
	private static final long serialVersionUID = 1L;
	public RankException(String message) {
		super(message);
	}
}

class PersonalTypeException extends Exception{
	private static final long serialVersionUID = 1L;
	public PersonalTypeException(String message) {
		super(message);
	}
}

class ConflictNumberException extends Exception{
	private static final long serialVersionUID = 1L;
	public ConflictNumberException(String message) {
		super(message);
	}
}

class StudentConflictException extends Exception{
	private static final long serialVersionUID = 1L;
	public StudentConflictException(String message) {
		super(message);
	}
}

class OverLeaderException extends Exception{
	private static final long serialVersionUID = 1L;
	public OverLeaderException(String message) {
		super(message);
	}
}

class InvalidMemberException extends Exception{
	private static final long serialVersionUID = 1L;
	public InvalidMemberException(String message) {
		super(message);
	}
}
	
class PersonalityImbalanceException extends Exception{
	private static final long serialVersionUID = 1L;
	public PersonalityImbalanceException(String message) {
		super(message);
	}
}

class RepeatedMemberException extends Exception{
	private static final long serialVersionUID = 1L;
	public RepeatedMemberException(String message) {
		super(message);
	}
}

class NoLeaderException extends Exception{
	private static final long serialVersionUID = 1L;
	public NoLeaderException(String message) {
		super(message);
	}
}

class OverAverageSkillException extends Exception{
	private static final long serialVersionUID = 1L;
	public OverAverageSkillException(String message) {
		super(message);
	}
}

class OverPercentageLimitException extends Exception{
	private static final long serialVersionUID = 1L;
	public OverPercentageLimitException(String message) {
		super(message);
	}
}

class OverSkillShortfallException extends Exception{
	private static final long serialVersionUID = 1L;
	public OverSkillShortfallException(String message) {
		super(message);
	}
}


