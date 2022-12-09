package org.phonebook.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "person")
public class Person {

	@Id
	@Column(name = "embg", length = 50)
	private String embg;

	@Column(name = "firstName", length = 50)
	private String firstName;

	@Column(name = "lastName", length = 150)
	private String lastName;

	@ManyToOne(targetEntity = org.phonebook.entity.Address.class, fetch = FetchType.EAGER, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "address_id")
	private Address address;

	public String getEmbg() {
		return embg;
	}

	public void setEmbg(String embg) {
		this.embg = embg;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Person(String embg, String firstName, String lastName) {
		super();
		this.embg = embg;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public Person(String embg, String firstName, String lastName, Address address) {
		super();
		this.embg = embg;
		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
	}

	public Person() {
		super();
	}

}
