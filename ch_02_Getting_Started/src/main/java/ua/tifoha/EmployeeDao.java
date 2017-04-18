package ua.tifoha;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Created by Vitaliy Sereda on 07.04.17.
 */
public class EmployeeDao {
	private final EntityManager em;

	public EmployeeDao(EntityManager em) {
		this.em = em;
	}

	public Optional<Employee> findOne(long id) {
		return Optional.ofNullable(em.find(Employee.class, id));
	}

	public List<Employee> findAll() {
		return em
				.createQuery("select emp from Employee emp", Employee.class)
				.getResultList();
	}

	public Employee createEmployee(long id, String name, BigDecimal salary) {
		Employee employee = new Employee(id);
		employee.setName(name);
		employee.setSalary(salary);
		em.persist(employee);

		return employee;
	}

	public Optional<Employee> raiseSalary(long id, BigDecimal delta) {
		Optional<Employee> employee = findOne(id);

		if (employee.isPresent()) {
			Employee e = employee.get();
			e.setSalary(e.getSalary().add(delta));
			em.persist(e);
		}
		return employee;
	}

	public void remove(long id) {
		findOne(id)
				.ifPresent(em::remove);
	}
}
