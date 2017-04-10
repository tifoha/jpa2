package ua.tifoha;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * Created by Vitaliy Sereda on 07.04.17.
 */
@Entity
public class Employee {
	@Id
	private Long id;

	private String name;

	private BigDecimal salary = BigDecimal.ZERO;

	public Employee() {
	}

	public Employee(long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Employee{");
		sb.append("id=").append(id);
		sb.append(", name='").append(name).append('\'');
		sb.append(", salary=").append(salary);
		sb.append('}');
		return sb.toString();
	}
}
