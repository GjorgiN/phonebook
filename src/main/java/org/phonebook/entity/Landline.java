package org.phonebook.entity;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class Landline extends PhoneNumberId {

	@ManyToOne(targetEntity = org.phonebook.entity.CityAreaCode.class, fetch = FetchType.EAGER, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "cityAreaCode")
	private CityAreaCode cityAreaCode;

	public CityAreaCode getCityAreaCode() {
		return cityAreaCode;
	}

	public void setCityAreaCode(CityAreaCode cityAreaCode) {
		this.cityAreaCode = cityAreaCode;
	}

	public Landline(CountryCode countryCode, Integer phoneNumber, CityAreaCode cityAreaCode) {
		super(countryCode, phoneNumber);
		this.cityAreaCode = cityAreaCode;
	}

	public Landline() {
		super();
	}

	public Landline(CountryCode countryCode, Integer phoneNumber) {
		super(countryCode, phoneNumber);
	}

}
