package com.iqmsoft.cassandra;

import java.util.UUID;

import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;


@Table(value = "vendor")
public class Vendor {

	@PrimaryKey
	private UUID id;

	@Column(value = "firsname")
	private String firstName;

	@Column(value = "lastname")
	private String lastName;

	public Vendor() {
	}

	public Vendor(UUID id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return String.format("Vendor[id=%s, firstName='%s', lastName='%s']", this.id,
				this.firstName, this.lastName);
	}

}


