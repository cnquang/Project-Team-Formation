package model;

import java.util.HashMap;

public class Company {
	private String comID;
	private String Name;
	private String ABN;
	private String URL;
	private String Address;
	
	private HashMap<String,Owner> owns = new HashMap<String, Owner>();
	
	public Company(String comID, String Name, String ABN, String URL, String Address) {
		this.comID = comID;
		this.Name = Name;
		this.ABN = ABN;
		this.URL = URL;
		this.ABN = ABN;
		this.Address = Address;
	}
	
	public void addOwner(Owner o) {
		owns.put(comID, o);
	}
	
	@Override
	public String toString() {
		return new String(comID + " " + Name + " " + ABN + " " + URL + " " + Address);
	}
	
	public String getcID() {
		return comID;
	}
	public String getcName() {
		return Name;
	}
	public String getcABN() {
		return ABN;
	}
	public String getcURL() {
		return URL;
	}
	public String getcAddress() {
		return Address;
	}
	public HashMap<String,Owner> getOwners(){
		return owns;
	}
}
