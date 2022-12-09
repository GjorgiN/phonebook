package org.phonebook.reqAndRes;

public class CityMobileCodesReq {

	private Integer cityCode;
	private Integer cityLocalCode;
	private Integer mobilePrefix;

	public Integer getMobilePrefix() {
		return mobilePrefix;
	}

	public void setMobilePrefix(Integer mobilePrefix) {
		this.mobilePrefix = mobilePrefix;
	}

	public Integer getCityCode() {
		return cityCode;
	}

	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}

	public Integer getCityLocalCode() {
		return cityLocalCode;
	}

	public void setCityLocalCode(Integer cityLocalCode) {
		this.cityLocalCode = cityLocalCode;
	}

	public CityMobileCodesReq(Integer cityCode, Integer cityLocalCode, Integer mobilePrefix) {
		super();
		this.cityCode = cityCode;
		this.cityLocalCode = cityLocalCode;
		this.mobilePrefix = mobilePrefix;
	}

	public CityMobileCodesReq() {
		super();
	}

}
