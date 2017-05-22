package ua.tifoha;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by Vitaliy Sereda on 05.05.17.
 */
@Embeddable
public class Address {
	private String street;

	private String city;

	private String state;

	@Column (name = "ZIP_CODE")
	private String zip;

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}
}
