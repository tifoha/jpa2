package ua.tifoha;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by Vitaliy Sereda on 05.05.17.
 */
@Embeddable
public class Address {
	private String zip;

	private String state;

	private String city;

	private String street;

	public Address() {
	}

	public Address(String zip, String state, String city, String street) {
		this.zip = zip;
		this.state = state;
		this.city = city;
		this.street = street;
	}

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
