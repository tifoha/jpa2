package ua.tifoha;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Vitaliy Sereda on 06.05.17.
 */
@Entity
public class Department extends AbstractEntity {
	public String getLastName() {
		return lastName;
	}

	public Set<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	@Embeddable
	public static class EmbaddedName {

		@Column (insertable = false,updatable = false)
		public String firstName;
		@Column (insertable = false,updatable = false)
		public String lastName1;

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("[")
			.append(firstName).append(':')
			.append(lastName1).append(']');
			return sb.toString();
		}

	}
	//	@OneToMany (mappedBy = "department")
//	@MapKeyColumn (name = "cub_id", nullable = true)
//	private Map<String, Employee> employeesByCubicle = new LinkedHashMap<>();
	private String lastName;

//	@ManyToMany
//	@MapKeyColumn (name = "cub_id")
//	@JoinTable (name = "emp_dep", joinColumns = @JoinColumn (name = "dep_id"), inverseJoinColumns = @JoinColumn (name = "emp_id"))
//	private Map<String, Employee> employeesByCubicle = new LinkedHashMap<>();
//
//	public Map<String, Employee> getEmployeesByCubicle() {
//		return employeesByCubicle;
//	}

	@OneToMany (mappedBy = "department")
	private Set<Employee> employees = new LinkedHashSet<>();

	@ManyToOne
	private Firm firm;

	public Firm getFirm() {
		return firm;
	}

	public void setFirm(Firm firm) {
		this.firm = firm;
	}

	//	public void setEmployeesByCubicle(Map<String, Employee> employeesByCubicle) {

//	}
//		this.employeesByCubicle = employeesByCubicle;
//	private Set<Employee> employees = new LinkedHashSet<>();
//	@OrderBy("info.value desc ")
//	@OneToMany
public void setLastName(String lastName) {
	this.lastName = lastName;
}
}
