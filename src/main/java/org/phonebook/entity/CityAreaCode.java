package org.phonebook.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "cityAreaCode")
public class CityAreaCode {

	@Column(name = "city_id")
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID cityId;

	@Column(name = "cityAreaCode", nullable = false)
	private Integer cityAreaCode;

	@Column(name = "cityLocalAreaCode")
	private Integer cityLocalAreaCode;

	@Column(name = "cityName")
	private String cityName;

	public Integer getCityAreaCode() {
		return cityAreaCode;
	}

	public void setCityAreaCode(Integer cityAreaCode) {
		this.cityAreaCode = cityAreaCode;
	}

	public Integer getCityLocalAreaCode() {
		return cityLocalAreaCode;
	}

	public void setCityLocalAreaCode(Integer cityLocalAreaCode) {
		this.cityLocalAreaCode = cityLocalAreaCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public UUID getCityId() {
		return cityId;
	}

	public CityAreaCode(Integer cityAreaCode, Integer cityLocalAreaCode, String cityName) {
		super();
		this.cityAreaCode = cityAreaCode;
		this.cityLocalAreaCode = cityLocalAreaCode;
		this.cityName = cityName;
	}

	public CityAreaCode() {
		super();
	}

}
