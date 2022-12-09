package org.phonebook.reqAndRes;

import org.phonebook.entity.Person;

public class RecordRequest {

	private Person person;
	private PhoneNumber phoneNumber;

	public RecordRequest(Person person, PhoneNumber phoneNumber) {
		super();
		this.person = person;
		this.phoneNumber = phoneNumber;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public PhoneNumber getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(PhoneNumber phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public RecordRequest() {
		super();
	}

}
