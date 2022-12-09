package org.phonebook.reqAndRes;

public class PhoneNumberUpdates {

	private PhoneNumber currentPhoneNumber;
	private PhoneNumber newPhoneNumber;

	public PhoneNumberUpdates() {
		super();
	}

	
	public PhoneNumberUpdates(PhoneNumber currentPhoneNumber, PhoneNumber newPhoneNumber) {
		super();
		this.currentPhoneNumber = currentPhoneNumber;
		this.newPhoneNumber = newPhoneNumber;
	}

	public PhoneNumber getCurrentPhoneNumber() {
		return currentPhoneNumber;
	}

	public void setCurrentPhoneNumber(PhoneNumber currentPhoneNumber) {
		this.currentPhoneNumber = currentPhoneNumber;
	}

	public PhoneNumber getNewPhoneNumber() {
		return newPhoneNumber;
	}

	public void setNewPhoneNumber(PhoneNumber newPhoneNumber) {
		this.newPhoneNumber = newPhoneNumber;
	}

}
