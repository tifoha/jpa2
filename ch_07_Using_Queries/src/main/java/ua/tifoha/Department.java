package ua.tifoha;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vitaliy Sereda on 07.04.17.
 */
@Entity
public class Department extends AbstractEntity {

	@OneToMany(mappedBy = "department")
	public List<Employee> employees = new ArrayList<>();

	public Department() {
	}

	public Department(String name) {
		super(name);
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public void addEmploee(Employee employee) {
		employee.setDepartment(this);
		employees.add(employee);
	}
}
