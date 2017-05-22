package ua.tifoha;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vitaliy Sereda on 07.04.17.
 */
@Entity
@Table (name = "jpa2.departament")
public class Department extends AbstractEntity {

	@OneToMany (fetch = FetchType.EAGER)
//	@OneToMany
	public List<Employee> employees = new ArrayList<>();

	public Department() {
	}

	public Department(long id) {
		this.id = id;
	}

	public List<Employee> getEmployees() {
		return employees;
	}
//
//	public void setEmployees(List<Employee> employees) {
//		this.employees = employees;
//	}

}
