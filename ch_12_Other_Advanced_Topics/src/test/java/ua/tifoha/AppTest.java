package ua.tifoha;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.persistence.CacheRetrieveMode;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.PersistenceUtil;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
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
	@PersistenceUnit
	private EntityManagerFactory emf;

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
	}

	@Test
//	@Transactional
	public void sharedCacheTest() throws Exception {
		TransactionStatus ts = tm.getTransaction(new DefaultTransactionDefinition());

//		Map<String, Object> props = new LinkedHashMap<>();
////		props.put("javax.persistence.sharedCache.mode", "ALL");
//		props.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.USE);
//		props.put("javax.persistence.cache.storeMode", CacheStoreMode.USE);
//		EntityManagerFactory emf1 = Persistence.createEntityManagerFactory("MainUnit", props);
//		EntityManager em1 = emf1.createEntityManager();
////		TransactionStatus ts = tm.getTransaction(new DefaultTransactionDefinition());
//		Employee emp = em1.find(Employee.class, 1, props);
//		assertTrue(em1.contains(emp));
//		em1.clear();
//		assertFalse(em1.contains(emp));
//		assertTrue(emf1.getCache().contains(Employee.class, emp.getId()));
		TypedQuery<Employee> q = em.createQuery("SELECT s FROM Employee s", Employee.class);
		q.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
		q.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH);
//		q.setParameter("amount", threshold);
		List<Employee> r = q.getResultList();
		tm.commit(ts);

	}

	@Test
//	@Transactional
//	@Rollback
	public void utilitiesTest() throws Exception {
		PersistenceUtil jpaUtils = Persistence.getPersistenceUtil();
		Employee emp = em.getReference(Employee.class, 1);

		//Можно определить что сущьность на самом деле ссылка
		assertFalse(jpaUtils.isLoaded(emp));

		TransactionStatus ts = tm.getTransaction(new DefaultTransactionDefinition());
//		emp = em.find(Employee.class, 1);
//		emp = em.find(Employee.class, 1, loadGraph("graph.Employee.all"));
		emp = em.find(Employee.class, 1, loadGraph("graph.Employee.directs"));
		tm.commit(ts);
		assertTrue(jpaUtils.isLoaded(emp));
		assertTrue(jpaUtils.isLoaded(emp, "directs"));
		assertTrue(jpaUtils.isLoaded(emp, "phones"));
		assertFalse(jpaUtils.isLoaded(emp, "projects"));
		PersistenceUnitUtil persistanceUnitUtils = emf.getPersistenceUnitUtil();
		Object key = persistanceUnitUtils.getIdentifier(emp);
		assertEquals(key, 1);

	}

	@Test
	public void name() throws Exception {
//		System.out.println(Math.floor(.4));
//		System.out.println(Math.floor(.6));
//		System.out.println(Math.floor(1.1));
//
//		long startTimeMillis = LocalDateTime.of(2011, 1, 1, 1, 1, 1).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
//		long startTimeMillis1 = LocalDateTime.of(2011, 1, 1, 1, 1, 1).atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
//		long startTimeMillis2 = LocalDateTime.of(2011, 1, 1, 1, 1, 1).atZone(ZoneId.of("America/Belize")).toInstant().toEpochMilli();
//		System.out.println(startTimeMillis);
//		System.out.println(startTimeMillis1);
//		System.out.println(startTimeMillis2);
//		System.out.println("asdfasdfasdf");
		TimeZone.setDefault(TimeZone.getTimeZone("America/Belize"));
		System.out.println(System.currentTimeMillis());
		final TimeZone timeZone = TimeZone.getTimeZone("Europe/Kiev");
		timeZone.setRawOffset(100_000);
		TimeZone.setDefault(timeZone);
//		System.out.println(LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant().toEpochMilli());
		System.out.println(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
		System.out.println(LocalDateTime.now().atZone(ZoneId.of("America/Belize")).toInstant().toEpochMilli());
		System.out.println(LocalDateTime.now().atZone(ZoneId.of("Europe/Moscow")).toInstant().toEpochMilli());
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

	private Map<String, Object> graph(EntityGraph<?> entityGraph, String property) {
		return Collections.singletonMap(property, entityGraph);
	}

	private Map<String, Object> loadGraph(String graphName) {
		return graph(em.getEntityGraph(graphName), "javax.persistence.loadgraph");
	}

	private Map<String, Object> loadGraph(EntityGraph<?> eg) {
		return graph(eg, "javax.persistence.loadgraph");
	}

	private Map<String, Object> fetchGraph(String graphName) {
		return graph(em.getEntityGraph(graphName), "javax.persistence.fetchgraph");
	}

	private Map<String, Object> fetchGraph(EntityGraph<?> eg) {
		return graph(eg, "javax.persistence.fetchgraph");
	}

}
