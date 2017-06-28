package ua.tifoha;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ua.tifoha.config.RootConfig;

/**
 * Created by Vitaliy Sereda on 15.05.17.
 */
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = RootConfig.class)
@Transactional
@Rollback
public class TestCase {
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("MainUnit");
	private EntityManager em;

	@BeforeClass
	public static void setUpTest() throws Exception {
//		try(DataPreparer dataPreparer = new DataPreparer()) {
//			dataPreparer.run();
//		}
	}


	@Before
	public void setUp() throws Exception {
		em = emf.createEntityManager();
	}

	@After
	public void tearDown() throws Exception {
		em.close();
	}

	@Test
	public void dynamicQuery() throws Exception {
		TypedQuery<Employee> query = em.createQuery("select e from Employee e where e.name like :name", Employee.class);
		query.setParameter("name", "%1%");
		final List<Employee> resultList = query.getResultList();
		assertThat(resultList, hasSize(1));
	}

	@Test
	public void namedQuery() throws Exception {
		TypedQuery<Employee> query = em.createNamedQuery("Emploee.findByName", Employee.class);
		query.setParameter("name", "1");
		final List<Employee> resultList = query.getResultList();
		assertThat(resultList, hasSize(1));
	}

	@Test
	public void dynamicNamedQuary() throws Exception {
		TypedQuery<Employee> q = em.createQuery("select e from Employee e", Employee.class);
		emf.addNamedQuery("findAll", q);
		TypedQuery<Employee> query = em.createNamedQuery("findAll", Employee.class);
		final List<Employee> resultList = query.getResultList();
		assertThat(resultList, not(empty()));
	}

	@Test
	public void namedQuaryOverride() throws Exception {
		TypedQuery<Employee> query = em.createNamedQuery("Emploee.findByName", Employee.class);
		query.setParameter("name", "1");
		List<Employee> resultList = query.getResultList();
		assertThat(resultList, hasSize(1));

		query = em.createQuery("select e from Employee e", Employee.class);
		emf.addNamedQuery("Emploee.findByName", query);
		query = em.createNamedQuery("Emploee.findByName", Employee.class);
		resultList = query.getResultList();
		assertThat(resultList, hasSize(greaterThan(1)));
	}

	@Test
	public void dateParameters() throws Exception {
		TypedQuery<Employee> query = em.createQuery("select e from Employee e where e.birthDay between :start and :end", Employee.class);
		query.setParameter("start", LocalDate.ofYearDay(1970, 1));
		query.setParameter("end", LocalDate.ofYearDay(1980, 1));
		final List<Employee> resultList = query.getResultList();
		System.out.println(resultList);
//		assertThat(resultList, hasSize(1));
	}

	@Test
	public void customResultQuery() throws Exception {
		em.createQuery("select new ua.tifoha.ResultLine(e.name, e.department.name) from Employee e", ResultLine.class)
		  .getResultList()
		  .forEach(System.out::println);
	}

	@Test
	public void peginationTest() throws Exception {
		final TypedQuery<ResultLine> query = em.createQuery("select new ua.tifoha.ResultLine(e.name, e.department.name) from Employee e", ResultLine.class);

		query.setFirstResult(0);
		query.setMaxResults(3);
		query
				.getResultList()
				.forEach(System.out::println);
		System.out.println("==============================");

		query.setFirstResult(3);
		query.setMaxResults(3);
		query
				.getResultList()
				.forEach(System.out::println);
	}

	@Test
	public void entityInPCAndQuaryMismatchInTransaction() throws Exception {
		Employee first = em.find(Employee.class, 1L);
		first.setName("Name");
		System.out.println(first);

		Employee second = em.createQuery("select e from Employee e where e.id in (1,2)", Employee.class).getResultList().get(0);
		System.out.println(second);

		assertEquals(first, second);

		String name = em.createQuery("select e.name from Employee e where e.id = 1", String.class).getSingleResult();
		System.out.println("name = " + name);

		assertNotEquals(first.getName(), name);
	}

	@Test
	public void flushBeforeExecutingQuery() throws Exception {
		em.getTransaction().begin();
		Employee first = em.find(Employee.class, 1L);
		first.setName("Name");
		System.out.println(first);

		List<Employee> employeeList = em.createQuery("select e from Employee e", Employee.class).getResultList();
		Employee second = employeeList.get(0);
		System.out.println(second);
		em.getTransaction().commit();
	}

	@Test
	public void avoidFlushBeforeExecutingQuery() throws Exception {
		em.setFlushMode(FlushModeType.COMMIT);
		em.getTransaction().begin();
		Employee first = em.find(Employee.class, 1L);
		first.setName("Name");
		System.out.println(first);

		List<Employee> employeeList = em.createQuery("select e from Employee e", Employee.class).getResultList();
		Employee second = employeeList.get(0);
		System.out.println(second);
		em.getTransaction().commit();
	}

	@Test
	public void countOnOneToManyField() throws Exception {
		List<Object[]> employeeList = em.createQuery("select count (e.projects) from Employee e", Object[].class).getResultList();

	}

	@Test
	public void queryTimeoutLimit() throws Exception {
		em.getTransaction().begin();
		IntStream
				.range(10, 1000)
				.mapToObj(i -> new Employee("Employee_" + i))
				.peek(em::persist)
				.collect(Collectors.toList());
		em.getTransaction().commit();
		em.clear();
		System.out.println("===========================================================");
		List<Employee> employeeList = em.createQuery("select e from Employee e", Employee.class)
										.setHint("javax.persistence.query.timeout", 1)
										.getResultList();
		System.out.println(employeeList.size());
	}

	@Test
	@Rollback
	public void bulkUpdateTest() throws Exception {
		em.getTransaction().begin();
		List<Employee> newEmployees = IntStream
				.range(10, 10)
				.mapToObj(i -> new Employee("Employee_" + i))
				.peek(em::persist)
				.collect(Collectors.toList());
		System.out.println("===========================================================");
		em.getTransaction().commit();
		em.clear();

		em.getTransaction().begin();
		em
				.createQuery("delete from Employee e where e.id > 10")
//				.setParameter("entities", newEmployees)
				.executeUpdate();

		em.getTransaction().commit();

		System.out.println("===========================================================");
		List<Employee> employeeList = em.createQuery("select e from Employee e", Employee.class)
										.setHint("javax.persistence.query.timeout", 1)
										.getResultList();
		System.out.println(employeeList.size());
	}
}
