package ua.tifoha;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Vitaliy Sereda on 26.05.17.
 */
@Entity
public class Wife {
	@Id
	@GeneratedValue
	private int id;

	private String name;

	@OneToMany(mappedBy = "wife")
	private Set<Marriage> marriages = new LinkedHashSet<>();

	public Wife() {
	}

	public Wife(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
