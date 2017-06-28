package ua.tifoha;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Unit test for simple App.
 */
@SuppressWarnings ("unchecked")
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = RootConfig.class)
@Transactional
@Rollback
public class AppTest {
	@PersistenceContext
	private EntityManager em;

	@Test
	public void joinSelectByRelationship() throws Exception {
		execute(em.createQuery("select e.name, d.name from Employee e join e.department d where e.directs is not empty "));
//		execute(em.createQuery("select e.salary, p.number from Employee e join Phone p where p.employee = e and e.salary > 50000"));
	}

	@Test
	public void leftOuterJoin() throws Exception {
		execute(em.createQuery("SELECT e, d FROM Employee e LEFT JOIN e.department d"));
	}

	@Test
	public void implicitFetchJoin() throws Exception {
		em
				.createQuery("SELECT d, e FROM Department d LEFT JOIN d.employees e", Department.class)
				.getResultList()
				.forEach(System.out::println);
	}

	@Test
	public void subquery() throws Exception {
		execute(em.createQuery("SELECT e.name, e.salary FROM Employee e WHERE e.salary = (SELECT MAX(emp.salary)FROM Employee emp)"));
	}

	@Test
	public void inTest() throws Exception {
		execute(em.createQuery("SELECT distinct p.id, p.name FROM Employee e, in(e.projects) p"));
	}

	public void execute(Query query) {
		System.out.println("======================================================");
		final List<Object[]> resultList = query
				.getResultList();
		resultList
				.stream()
				.map(Arrays::toString)
				.forEach(System.out::println);
	}
}
