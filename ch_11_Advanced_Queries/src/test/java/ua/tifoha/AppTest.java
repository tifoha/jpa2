package ua.tifoha;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

	@Test
	public void namedEntityGraph() throws Exception {
		Employee e;
		TransactionStatus ts;
//		ts = tm.getTransaction(new DefaultTransactionDefinition());
//		e = em.find(Employee.class, 1);
//		tm.commit(ts);
//		expectedException.expect(LazyInitializationException.class);
//		e.getPhones().size();
		ts = tm.getTransaction(new DefaultTransactionDefinition());
		e = em.find(Employee.class, 1, loadGraph("graph.Employee.phones"));
		tm.commit(ts);
		System.out.println(e.getPhones().iterator().next().getEmployee());
		System.out.println("=================================");
		ts = tm.getTransaction(new DefaultTransactionDefinition());
		e = em.find(Employee.class, 1, loadGraph("graph.Employee.name"));
		tm.commit(ts);
		System.out.println(e.getSalary());
		EntityGraph<Employee> eg = em.createEntityGraph(Employee.class);
	}

	@Test
	public void entityGraphTypes() throws Exception {
		Employee e;
		TransactionStatus ts;
		EntityGraph<Employee> eg = em.createEntityGraph(Employee.class);
		eg.addAttributeNodes("name", "manager");
		ts = tm.getTransaction(new DefaultTransactionDefinition());
		e = em.find(Employee.class, 1, fetchGraph(eg));
//		e = em.find(Employee.class, 1, loadGraph(eg));
		tm.commit(ts);
		System.out.println("=================================");
		System.out.println(e.getPhones().iterator().next().getEmployee());
//		ts = tm.getTransaction(new DefaultTransactionDefinition());
//		e = em.find(Employee.class, 1, loadGraph("graph.Employee.name"));
//		tm.commit(ts);
//		System.out.println(e.getSalary());
//		EntityGraph<Employee> eg = em.createEntityGraph(Employee.class);
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
