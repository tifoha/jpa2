package ua.tifoha;

import static java.util.Collections.singletonMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.RandomStringUtils;

public class DataPreparer implements Runnable, AutoCloseable {

	private final EntityManagerFactory emf;

	public DataPreparer() {
		this(Persistence.createEntityManagerFactory("MainUnit",
				singletonMap("hibernate.hbm2ddl.auto", "create")));
	}

	public DataPreparer(EntityManagerFactory emf) {
		this.emf = emf;
	}

	@Override
	public void run() {
		Random rnd = new Random();
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		List<Project> projectList = IntStream
				.range(1, 4).mapToObj(i -> new Project("Project_" + i))
				.peek(em::persist)
				.collect(Collectors.toList());

		List<Department> departmentList = IntStream
				.range(1, 3).mapToObj(i -> new Department("Department_" + i))
				.peek(em::persist)
				.collect(Collectors.toList());

		List<Employee> employeeList = IntStream
				.range(1, 10)
				.mapToObj(i -> {
					final Employee employee = new Employee("Employee_" + i);
					employee.setSalary(rnd.nextInt(4000));
					final LocalDate birthDay = LocalDate.of(1970, 1, 1).plusDays(rnd.nextInt(365 * 50));
					employee.setBirthDay(birthDay);

					final Address address = new Address("Zip_" + i, "State_" + i, "City_" + i, "Street_" + i);
					employee.setAddress(address);

					IntStream
							.range(1, rnd.nextInt(4))
							.mapToObj(j -> RandomStringUtils.random(10, false, true))
							.forEach(employee::addPhone);

					departmentList.get(i % 2).addEmploee(employee);

					return employee;
				})
				.peek(em::persist)
				.collect(Collectors.toList());

		em.getTransaction().commit();
		em.close();
	}

	@Override
	public void close() throws Exception {
		emf.close();
	}
}
