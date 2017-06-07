package ua.tifoha;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.PessimisticLockScope;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Unit test for simple App.
 */
@SuppressWarnings ("unchecked")
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = RootConfig.class)
//@Transactional
//@Rollback
public class AppTest {
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private PlatformTransactionManager tm;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void lifeCycleCollbacks() throws Exception {
		Phone phone = new Phone();
		em.persist(phone);
		em.flush();
		phone.setNumber("sadfasd");
		em.merge(phone);
		em.flush();
		em.clear();
		phone = em.find(Phone.class, phone.getId());
		em.remove(phone);
		em.flush();
	}

	@Test
	public void concurrency() throws Exception {
		Phone phone = new Phone();
		em.persist(phone);
		phone.setNumber("sadfasd");
		em.flush();
		em.clear();
		Phone phone1 = em.find(Phone.class, phone.getId());
		phone1.setNumber("123456");
		em.merge(phone1);
		em.flush();
		em.refresh(phone);
		System.out.println("phone1.getNumber() = " + phone1.getNumber());
	}

	@Test
	public void optimisticReadLockTest() throws Exception {
		TransactionStatus ts = tm.getTransaction(new DefaultTransactionDefinition());

		Department department = em.find(Department.class, 1);
		em.lock(department, LockModeType.OPTIMISTIC);
		CompletableFuture.runAsync(this::changeDepartment1);
//		CompletableFuture.runAsync(this::changeDepartment2);
		TimeUnit.SECONDS.sleep(1);
//		department.setName("Engineering");
//		department.setVersion(100);
		System.out.println(department);
		tm.commit(ts);


//		TransactionStatus ts = tm.getTransaction(new DefaultTransactionDefinition());
//
//		Department department = em.find(Department.class, 1, LockModeType.OPTIMISTIC);
////		Department department = em.find(Department.class, 1);
//		em.lock(department, LockModeType.OPTIMISTIC);
//		CompletableFuture.runAsync(this::changeDepartment);
//		Thread.yield();
//		TimeUnit.SECONDS.sleep(1);
//		System.out.println(department);
//		tm.commit(ts);

//		List<Department> departments = em.createQuery("select d from Department d").setLockMode(LockModeType.OPTIMISTIC).getResultList();
//		CompletableFuture.runAsync(this::addNewEmployee);
//		TimeUnit.SECONDS.sleep(1);
//		Map<Department, Integer> depSizes1 = departments.stream().collect(Collectors.toMap(Function.identity(), d -> d.getEmployees().size()));
//		System.out.println(depSizes1);
//
//		em.createQuery("select d from Department d", Department.class)
//										 .setLockMode(LockModeType.OPTIMISTIC)
//										 .getResultList()
//										 .stream()
//										 .collect(Collectors.toMap((Department d) -> d, d -> d.getEmployees().size()))
//		  .forEach((department, integer) -> System.out.println(department + " = " + integer));
//		  .foreEach((Map<Department, Integer> e)->

	}

	@Test
	public void pesimisticLockTest() throws Exception {
		TransactionStatus ts = tm.getTransaction(new DefaultTransactionDefinition());
		Employee emp = em.find(Employee.class, 1);
		long salary = emp.getSalary();

		CompletableFuture.runAsync(() -> {
			TransactionStatus its = tm.getTransaction(new DefaultTransactionDefinition());
//			Employee e = em.find(Employee.class, 1);
			Employee e = em.find(Employee.class, 1, LockModeType.PESSIMISTIC_WRITE);
			emp.getPhones().size();
			e.setSalary(60000);
			try {
				TimeUnit.MILLISECONDS.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			tm.commit(its);
		});

		TimeUnit.MILLISECONDS.sleep(500);

		if (salary < 57000) {
			em.refresh(emp, LockModeType.PESSIMISTIC_WRITE, Collections.singletonMap("javax.persistence.lock.timeout", 6000));
			if (emp.getSalary() < 57000) {
				emp.setSalary(58000);
			}
		}
		System.out.println(emp.getSalary());
		tm.commit(ts);
		// Find amt according to union rules and emp status
//		EmployeeStatus status = emp.getStatus();
//		double accruedDays = calculateAccrual(status);
//		if (accruedDays > 0) {
//			em.refresh(emp, LockModeType.PESSIMISTIC_WRITE);
//			if (status != emp.getStatus()) accruedDays = calculateAccrual(emp.getStatus());
//			if (accruedDays > 0) emp.setVacationDays(emp.getVacationDays() + accruedDays);
//		}
	}

	private void changeDepartment1() {
		TransactionStatus ts = tm.getTransaction(new DefaultTransactionDefinition());
		Department department = em.find(Department.class, 1);
//		Department department = em.find(Department.class, 1, LockModeType.OPTIMISTIC);
//		Department department = em.find(Department.class, 1, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
//		em.lock(department, LockModeType.OPTIMISTIC);
//		em.lock(department, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
		department.setName(department.getName() + "1");
		em.merge(department);
//		em.flush();
		tm.commit(ts);
	}

	private void changeDepartment2() {
		TransactionStatus ts = tm.getTransaction(new DefaultTransactionDefinition());
		Department department = em.find(Department.class, 1, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
		em.lock(department, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
		department.setName(department.getName() + "2");
		em.merge(department);
//		em.flush();
		tm.commit(ts);
	}

	private void addNewEmployee() {
		TransactionStatus ts = tm.getTransaction(new DefaultTransactionDefinition());
		Employee employee = new Employee();
		employee.setName("Name");
		employee.setDepartment(em.find(Department.class, 1));
		em.persist(employee);
		tm.commit(ts);
	}
}
