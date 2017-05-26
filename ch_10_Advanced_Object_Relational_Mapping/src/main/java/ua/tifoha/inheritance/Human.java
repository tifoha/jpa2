package ua.tifoha.inheritance;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * Created by Vitaliy Sereda on 26.05.17.
 */
@Entity
@PrimaryKeyJoinColumn (name = "hid", referencedColumnName = "id")
public class Human extends Mammal {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
