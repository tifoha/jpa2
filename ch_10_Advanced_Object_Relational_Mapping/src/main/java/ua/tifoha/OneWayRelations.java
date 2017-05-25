package ua.tifoha;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Vitaliy Sereda on 25.05.17.
 */
@Entity
public class OneWayRelations {
	@Id
	@GeneratedValue
	private int id;

	@OneToMany
	@JoinColumn(name = "owr_id")
	private Set<Phone> phones = new LinkedHashSet<>();

	public OneWayRelations() {
	}

	public void addPhone(Phone phone) {
		phones.add(phone);
	}
}
