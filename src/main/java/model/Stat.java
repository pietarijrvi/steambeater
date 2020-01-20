package model;

import javax.persistence.*;

@Entity
@Table(name="stat")
public class Stat {
	
	@Id
	@Column(name="tunnus")
	private String tunnus;
	
	@Column(name="nimi")
	private String nimi;
	
	public Stat(String tunnus, String nimi) {
		super();
		this.tunnus = tunnus;
		this.nimi = nimi;
	}
	
	public Stat() {
		super();
	}
	
	public String getTunnus() {
		return tunnus;
	}

	public void setTunnus(String tunnus) {
		this.tunnus = tunnus;
	}

	public void setNimi(String nimi) {
		this.nimi = nimi;
	}

	public String getNimi() {
		return nimi;
	}
}
