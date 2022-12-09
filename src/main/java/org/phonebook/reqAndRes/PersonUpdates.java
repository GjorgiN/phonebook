package org.phonebook.reqAndRes;

import org.phonebook.entity.Address;

public class PersonUpdates {

	private String currentEmbg;
	private String newEmbg;
	private String newFirstName;
	private String newLastName;
	private Address newAddress;

	public String getCurrentEmbg() {
		return currentEmbg;
	}

	public void setCurrentEmbg(String currentEmbg) {
		this.currentEmbg = currentEmbg;
	}

	public String getNewEmbg() {
		return newEmbg;
	}

	public void setNewEmbg(String newEmbg) {
		this.newEmbg = newEmbg;
	}

	public String getNewFirstName() {
		return newFirstName;
	}

	public void setNewFirstName(String newFirstName) {
		this.newFirstName = newFirstName;
	}

	public String getNewLastName() {
		return newLastName;
	}

	public void setNewLastName(String newLastName) {
		this.newLastName = newLastName;
	}

	public Address getNewAddress() {
		return newAddress;
	}

	public void setNewAddress(Address newAddress) {
		this.newAddress = newAddress;
	}

	public PersonUpdates(String currentEmbg, String newEmbg, String newFirstName, String newLastName,
			Address newAddress) {
		super();
		this.currentEmbg = currentEmbg;
		this.newEmbg = newEmbg;
		this.newFirstName = newFirstName;
		this.newLastName = newLastName;
		this.newAddress = newAddress;
	}

	public PersonUpdates() {
		super();
	}

}
