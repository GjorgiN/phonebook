package org.phonebook.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "mobile_phonebook")
public class MobilePhonebook extends Phonebook {

	@EmbeddedId
	Mobile phoneNumber;

	public Mobile getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Mobile phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public MobilePhonebook(Person person, Mobile phoneNumber) {
		super(person);
		this.phoneNumber = phoneNumber;
	}

	public MobilePhonebook() {
		super();
	}

	public MobilePhonebook(Person person) {
		super(person);
	}

}
