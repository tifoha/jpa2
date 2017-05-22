package ua.tifoha;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Vitaliy Sereda on 07.04.17.
 */
@Entity
@Table (name = "jpa2.firm")
public class Firm extends AbstractEntity{
//	@Id
//	public Long id;

//	@Lob
//	@Basic (fetch = FetchType.LAZY)
//	public String name;

//	@OneToMany(fetch = FetchType.EAGER)
////	@OneToMany
//	public List<Employee> employees;

//	public Firm() {
//	}
//
//	public Firm(long id) {
//		this.id = id;
//	}
//
//	//	@Id
////	@Access(PROPERTY)
//	public Long getId() {
//		return id;
//	}
//
//	private void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}

//	public List<Employee> getEmployees() {
//		return employees;
//	}
//
//	public void setEmployees(List<Employee> employees) {
//		this.employees = employees;
//	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Firm{");
		sb.append("id=").append(id);
		sb.append(", name='").append(name).append('\'');
//		sb.append(", employees=").append(employees);
		sb.append('}');
		return sb.toString();
	}
}
