package ua.tifoha;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vitaliy Sereda on 07.04.17.
 */
@Entity
@NamedQueries ( {
		@NamedQuery (name = "Emploee.findByName", query = "select e from Employee e where e.name like concat('%', :name, '%')")
})
public class Employee extends AbstractEntity {
	@ManyToOne
	private Department department;

	@ElementCollection
	private List<String> phones = new ArrayList<>();

	private Address address = new Address();

	private Integer salary = 0;

	private LocalDate birthDay;

	@ManyToMany
	private List<Project> projects = new ArrayList<>();

	public Employee() {
		super();
	}

	public Employee(String name) {
		super(name);
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	public void addPhone(String phone) {
		phones.add(phone);
	}

	public void setSalary(Integer salary) {
		this.salary = salary;
	}

	public Integer getSalary() {
		return salary;
	}

	public LocalDate getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(LocalDate birthDay) {
		this.birthDay = birthDay;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public void addProject(Project project) {
		projects.add(project);
	}

}
