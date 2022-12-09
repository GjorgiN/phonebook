package org.phonebook.entity;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class Mobile extends PhoneNumberId {

	@ManyToOne(targetEntity = org.phonebook.entity.MobileCode.class, fetch = FetchType.EAGER, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "mobileCode")
	private MobileCode mobileCode;

	public MobileCode getMobileCode() {
		return mobileCode;
	}

	public void setMobileCode(MobileCode mobileCode) {
		this.mobileCode = mobileCode;
	}

	public Mobile(CountryCode countryCode, Integer phoneNumber, MobileCode mobileCode) {
		super(countryCode, phoneNumber);
		this.mobileCode = mobileCode;
	}

	public Mobile() {
		super();
	}

	public Mobile(CountryCode countryCode, Integer phoneNumber) {
		super(countryCode, phoneNumber);
	}

}