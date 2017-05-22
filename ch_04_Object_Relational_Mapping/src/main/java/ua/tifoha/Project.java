package ua.tifoha;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Vitaliy Sereda on 04.05.17.
 */
@Entity
public class Project extends AbstractEntity {
	@ManyToMany(mappedBy = "projects")
	private Set<Employee> employees = new LinkedHashSet<>();

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}
}
