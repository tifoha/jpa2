package ua.tifoha;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import java.time.LocalDate;

/**
 * Created by Vitaliy Sereda on 26.05.17.
 */
@Entity
@SecondaryTables({
		@SecondaryTable(name = "user_address", pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id")),
		@SecondaryTable(name = "user_profile", pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id")),
})
public class User {
	@Embeddable
	public static class Profile {
		private String firstName;
		private String lastName;
		private LocalDate birthday;

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

		public LocalDate getBirthday() {
			return birthday;
		}

		public void setBirthday(LocalDate birthday) {
			this.birthday = birthday;
		}
	}

	@Id
	@GeneratedValue
	private int id;
	private String username;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "firstName", column = @Column(table = "user_profile")),
			@AttributeOverride(name = "lastName", column = @Column(table = "user_profile")),
			@AttributeOverride(name = "birthday", column = @Column(table = "user_profile")),
	})
	private Profile profile = new Profile();

	@Column(table = "user_address")
	private String street;

	@Column(table = "user_address")
	private String city;
	@Column(table = "user_address")
	private String state;
	@Column(table = "user_address")
	private String zip;

	public User() {
	}

	public User(String username) {
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
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
