package com.ryhma6.maven.steambeater.model;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="stat")
public class Stat {
	
	@Setter @Getter
	@Id
	@Column(name="tunnus")
	private String tunnus;
	
	@Setter @Getter
	@Column(name="nimi")
	private String nimi;
	
	public Stat(String id, String name) {
		super();
		this.tunnus = id;
		this.nimi = name;
	}
	
	public Stat() {
		super();
	}
}
