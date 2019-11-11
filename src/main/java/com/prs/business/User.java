package com.prs.business;

import javax.persistence.*;


@Entity
public class User {
	@Id
	@GeneratedValue (strategy= GenerationType.IDENTITY)
	
	private int id;
	private String userName;
	private String passWord;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String email;
	private boolean isReviewer;
	private boolean isAdmin;

	public User() {
		super();
	}

	public User(int id, String userName, String passWord, String firstName, String lastName, String phoneNumber,
			String email, boolean isReviewer, boolean isAdmin) {
		super();
		this.id = id;
		this.userName = userName;
		this.passWord = passWord;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.isReviewer = isReviewer;
		this.isAdmin = isAdmin;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isReviewer() {
		return isReviewer;
	}

	public void setReviewer(boolean isReviewer) {
		this.isReviewer = isReviewer;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public String toString() {
		return "UserID: " + id + ", Username: " + userName + ", Password: " + passWord + ", First Name:" + firstName
				+ ", Last Name: " + lastName + ", Phone number: " + phoneNumber + ", Email: " + email
				+ ", Is Reviewer: " + isReviewer + ", Is Admin: " + isAdmin;
	}

}
