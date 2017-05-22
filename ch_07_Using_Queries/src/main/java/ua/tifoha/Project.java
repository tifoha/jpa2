package ua.tifoha;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vitaliy Sereda on 04.05.17.
 */
@Entity
public class Project extends AbstractEntity {
	public Project() {
		super();
	}

	public Project(String name) {
		super(name);
	}

	@ManyToMany(mappedBy = "projects")
	private List<Employee> employees = new ArrayList<>();

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}
}
