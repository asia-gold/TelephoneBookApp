package ru.asia.mytelephonebookapp.models;

import java.util.Date;


public class Contact {
	
	private long id;
	private byte[] photo;
	private String name;
	private Date dateOfBirth;
	private boolean isMale;
	private String address;
	
	public Contact() {
		// TODO Auto-generated constructor stub
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
		
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
	public void setIsMale(boolean isMale) {
		this.isMale = isMale;
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
	
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	
	public boolean getIsMale() {
		return isMale;
	}
	
	public String getAddress() {
		return address;
	}

}
