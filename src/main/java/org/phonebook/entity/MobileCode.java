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
@Table(name = "mobileCode", uniqueConstraints = @UniqueConstraint(columnNames = { "prefix",
		"operator" }, name = "unique_combination"))
public class MobileCode {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	private UUID operator_id;

	private Integer prefix;

	private String operator;

	public Integer getPrefix() {
		return prefix;
	}

	public void setPrefix(Integer prefix) {
		this.prefix = prefix;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public UUID getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(UUID operator_id) {
		this.operator_id = operator_id;
	}

	public MobileCode(Integer prefix, String operator) {
		super();
		this.prefix = prefix;
		this.operator = operator;
	}

	public MobileCode() {
		super();
	}

}
