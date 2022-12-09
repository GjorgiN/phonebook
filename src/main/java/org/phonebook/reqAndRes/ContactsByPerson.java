package org.phonebook.reqAndRes;

import java.util.List;

import org.phonebook.entity.Person;
import org.phonebook.entity.PhoneNumberId;

public class ContactsByPerson {

	private Person person;

	private List<PhoneNumberId> phoneNumbers;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person p) {
		this.person = p;
	}

	public List<PhoneNumberId> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<PhoneNumberId> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public ContactsByPerson(Person person, List<PhoneNumberId> phoneNumbers) {
		super();
		this.person = person;
		this.phoneNumbers = phoneNumbers;
	}

	public ContactsByPerson() {
		super();
	}

}
