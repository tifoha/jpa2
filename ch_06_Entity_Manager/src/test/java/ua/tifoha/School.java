package ua.tifoha;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Vitaliy Sereda on 11.05.17.
 */
@Entity
public class School extends AbstractEntity {
	public School() {
	}

	public School(String name) {
		super(name);
	}

	@OneToMany (mappedBy = "school", cascade = {PERSIST, MERGE})
	public Set<Student> students = new LinkedHashSet<>();

	@OneToOne
	public Director director;
}
