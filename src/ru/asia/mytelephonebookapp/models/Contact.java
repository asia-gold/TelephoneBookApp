package ru.asia.mytelephonebookapp.models;


public class Contact {
	
	private long id;
	private byte[] photo;
	private String name;
	private String dateOfBirth;
	private String gender;
	private String address;
	
	public Contact() {
		// TODO Auto-generated constructor stub
	}
	
	public Contact(String name, String gender) {
		this.name = name;
		this.gender = gender;
	}
	
	/*
	 * Set Methods
	 */
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}
	
	public void setName(String name) {
		this.name = name;
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
	
	public byte[] getPhoto() {
		return photo;
	}
	
	public String getName() {
		return name;
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
