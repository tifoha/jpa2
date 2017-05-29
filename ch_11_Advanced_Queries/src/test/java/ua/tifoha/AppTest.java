package ua.tifoha;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
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
	public void nativeQueryTest() throws Exception {
		TypedQuery<Employee> query = (TypedQuery<Employee>) em.createNativeQuery("SELECT ID, NAME, ADDRESS_ID, DEPARTMENT_ID, MANAGER_ID, SALARY, STARTDATE,  1 AS nn FROM employee e", Employee.class);
		printResults(query);

	}

	@Test
	public void nativeQueryNotEntityMappingTest() throws Exception {
		Query query = em.createNativeQuery("SELECT ID, NAME, ADDRESS_ID, DEPARTMENT_ID, MANAGER_ID, SALARY, STARTDATE,  1 AS nn FROM employee e", "test");
		List<?> result = query.getResultList();
		printResults(query);

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

	private void printSQL(CriteriaQuery<?> q) {
		System.out.println("----------------------------------------------------------------------");
		System.out.println(em.createQuery(q).unwrap(org.hibernate.query.Query.class).getQueryString());
		System.out.println("----------------------------------------------------------------------");
	}


	public void printResults(Query query) {
		List<?> result = query
				.getResultList();

		System.out.println("----------------------------------------------------------------------");
		result.forEach(System.out::println);
	}

	public void printTupleResults(Query query) {
		List<Tuple> result = query
				.getResultList();

		System.out.println("----------------------------------------------------------------------");
		result
				.stream()
				.map(Tuple::toArray)
				.map(Arrays::toString)
				.forEach(System.out::println);
	}

	public void printAnyResults(Query query) {
		List<?> result = query
				.getResultList();

		System.out.println("----------------------------------------------------------------------");
		result
				.stream()
				.map(o -> (Object[]) o)
				.map(Arrays::toString)
				.forEach(System.out::println);
	}
}
