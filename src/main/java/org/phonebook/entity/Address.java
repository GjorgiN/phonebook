package org.phonebook.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "address", uniqueConstraints = @UniqueConstraint(columnNames = { "city", "street",
		"number" }, name = "unique_combination"))
public class Address {

	@Column(name = "address_id", nullable = false)
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID addressId;

	@Column(name = "city", length = 50, nullable = false)
	private String city;

	@Column(name = "street", length = 100, nullable = false)
	private String street;

	@Column(name = "number", length = 15, nullable = false)
	private String number;

	public Address() {
		super();
	}

	public Address(String city, String street, String number) {
		super();
		this.city = city;
		this.street = street;
		this.number = number;
	}

	public UUID getUniqueId() {
		return addressId;
	}

	public void setUniqueId(UUID uniqueId) {
		this.addressId = uniqueId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}
