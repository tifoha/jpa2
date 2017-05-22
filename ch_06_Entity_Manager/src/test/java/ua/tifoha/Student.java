package ua.tifoha;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Created by Vitaliy Sereda on 11.05.17.
 */
@Entity
public class Student extends AbstractEntity {
	public Student() {
	}

	public Student(String name) {
		super(name);
	}

	@ManyToOne(cascade = CascadeType.PERSIST)
	public School school;
}
