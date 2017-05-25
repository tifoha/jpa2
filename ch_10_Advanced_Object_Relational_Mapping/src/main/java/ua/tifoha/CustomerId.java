package ua.tifoha;

import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Vitaliy Sereda on 25.05.17.
 */
public class CustomerId implements Serializable{
	private String countryCode;
	private int id;

	public CustomerId() {
	}

	public CustomerId(String countryCode, int id) {
		this.countryCode = countryCode;
		this.id = id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CustomerId)) return false;

		CustomerId that = (CustomerId) o;

		if (id != that.id) return false;
		return countryCode != null ? countryCode.equals(that.countryCode) : that.countryCode == null;
	}

	@Override
	public int hashCode() {
		int result = countryCode != null ? countryCode.hashCode() : 0;
		result = 31 * result + id;
		return result;
	}
}
