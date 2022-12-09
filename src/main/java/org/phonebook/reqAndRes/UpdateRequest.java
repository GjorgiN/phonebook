package org.phonebook.reqAndRes;

public class UpdateRequest {

	private PersonUpdates personUpdates;
	private PhoneNumberUpdates phoneNumberUpdates;

	public UpdateRequest(PersonUpdates personUpdates, PhoneNumberUpdates phoneNumberUpdates) {
		super();
		this.personUpdates = personUpdates;
		this.phoneNumberUpdates = phoneNumberUpdates;
	}

	public PersonUpdates getPersonUpdates() {
		return personUpdates;
	}

	public void setPersonUpdates(PersonUpdates personUpdates) {
		this.personUpdates = personUpdates;
	}

	public PhoneNumberUpdates getPhoneNumberUpdates() {
		return phoneNumberUpdates;
	}

	public void setPhoneNumberUpdates(PhoneNumberUpdates phoneNumberUpdates) {
		this.phoneNumberUpdates = phoneNumberUpdates;
	}

	public UpdateRequest() {
		super();
	}

}
