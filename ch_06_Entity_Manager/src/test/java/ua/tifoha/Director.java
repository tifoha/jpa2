package ua.tifoha;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * Created by Vitaliy Sereda on 12.05.17.
 */
@Entity
public class Director extends AbstractEntity {
	@OneToOne(mappedBy = "director")
	public School school;
}
