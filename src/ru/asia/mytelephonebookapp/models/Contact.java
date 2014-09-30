package ru.asia.mytelephonebookapp.models;


public class Contact {
	
	private long id;
	private String name;
	private String surname;
	private String dateOfBirth;
	private String gender;
	private String address;
	
	public Contact() {
		// TODO Auto-generated constructor stub
	}
	
	public Contact(String surname, String name, String gender) {
		this.surname = surname;
		this.name = name;
		this.gender = gender;
	}
	
	/*
	 * Set Methods
	 */
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	/*
	 * Get Methods
	 */
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	
	public String getGender() {
		return gender;
	}
	
	public String getAddress() {
		return address;
	}
	
	

}
