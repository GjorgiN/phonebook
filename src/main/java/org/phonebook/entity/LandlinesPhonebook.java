package org.phonebook.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "landlines_phonebook")
public class LandlinesPhonebook extends Phonebook {

	@EmbeddedId
	Landline phoneNumber;

	public Landline getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Landline phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public LandlinesPhonebook(Person person, Landline phoneNumber) {
		super(person);
		this.phoneNumber = phoneNumber;
	}

	public LandlinesPhonebook() {
		super();
	}

	public LandlinesPhonebook(Person person) {
		super(person);
	}

}
