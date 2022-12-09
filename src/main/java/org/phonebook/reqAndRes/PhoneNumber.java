package org.phonebook.reqAndRes;

import org.phonebook.entity.CityAreaCode;
import org.phonebook.entity.CountryCode;
import org.phonebook.entity.MobileCode;

public class PhoneNumber {

	private CountryCode countryCode;
	private CityAreaCode cityAreaCode;
	private MobileCode mobileCode;
	private Integer phoneNumber;

	public MobileCode getMobileCode() {
		return mobileCode;
	}

	public void setMobileCode(MobileCode mobileCode) {
		this.mobileCode = mobileCode;
	}

	public CountryCode getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(CountryCode countryCode) {
		this.countryCode = countryCode;
	}

	public Integer getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(Integer phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public CityAreaCode getCityAreaCode() {
		return cityAreaCode;
	}

	public void setCityAreaCode(CityAreaCode cityAreaCode) {
		this.cityAreaCode = cityAreaCode;
	}

	public PhoneNumber(CountryCode countryCode, CityAreaCode cityAreaCode, MobileCode mobileCode, Integer phoneNumber) {
		super();
		this.countryCode = countryCode;
		this.cityAreaCode = cityAreaCode;
		this.mobileCode = mobileCode;
		this.phoneNumber = phoneNumber;
	}

	public PhoneNumber() {
		super();
	}

}
