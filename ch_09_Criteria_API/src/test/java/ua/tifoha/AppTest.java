package ua.tifoha;

import static javax.persistence.criteria.JoinType.LEFT;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Unit test for simple App.
 */
@SuppressWarnings ("unchecked")
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = RootConfig.class)
@Transactional
@Rollback
public class AppTest {
	@PersistenceUnit
	private EntityManagerFactory emf;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	PlatformTransactionManager tm;

	private CriteriaBuilder cb;


	@Before
	public void setUp() throws Exception {
		cb = em.getCriteriaBuilder();
		System.out.println("======================================================================");
	}

	@Test
	public void joinSelectByRelationship() throws Exception {
		CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
		Root<Employee> emp = cq.from(Employee.class);
		cq.select(emp)
		  .distinct(true)
		  .where(cb.equal(emp.get("name"), "John"));

		printResults(cq);
	}

	@Test
	public void formPair() throws Exception {
		CriteriaQuery<Department> q = cb.createQuery(Department.class);
		Root<Employee> emp = q.from(Employee.class);
		Root<Department> dpt = q.from(Department.class);
		q.select(dpt)
		 .where(cb.equal(dpt, emp.get("department")));
		printResults(q);
	}

	@Test
	public void selectFieldOfEntity() throws Exception {
		CriteriaQuery<String> q = cb.createQuery(String.class);
		Root<Employee> emp = q.from(Employee.class);

		q.select(emp.get("name"));
		printResults(q);
	}

	@Test
	public void tupleQuery() throws Exception {
		CriteriaQuery<Tuple> q = cb.createTupleQuery();
		Root<Employee> emp = q.from(Employee.class);

		q.select(cb.tuple(emp.get("name"), emp.get("salary")));
		printTupleResults(q);
	}

	@Test
	public void multiselectQuery() throws Exception {
		CriteriaQuery<Object[]> q = cb.createQuery(Object[].class);
		Root<Employee> emp = q.from(Employee.class);

		q.multiselect(emp.get("name"), emp.get("department").get("name"), emp.get("salary"));
		printAnyResults(q);
	}

	@Test
	public void joinTables() throws Exception {
		CriteriaQuery<Tuple> q = cb.createTupleQuery();

		Root<Employee> emp = q.from(Employee.class);
		Join<Employee, Phone> phone = emp.join("phones", LEFT);

		q.multiselect(emp.get("name"), emp.get("department").get("name"), phone.get("type"), phone.get("number"));
		printTupleResults(q);
	}

	@Test
	@Transactional (propagation = Propagation.NOT_SUPPORTED)
	public void fetchTest() throws Exception {
		TransactionStatus ts = tm.getTransaction(new DefaultTransactionDefinition());
		CriteriaQuery<Employee> q = cb.createQuery(Employee.class);

		Root<Employee> emp = q.from(Employee.class);
		emp.fetch("phones", LEFT);
//		emp.join("phones", LEFT);

		q.select(emp).distinct(true);
		List<Employee> result = em.createQuery(q).getResultList();
		tm.commit(ts);
		assertThat(result, hasSize(13));
		result.get(0).getPhones().size();
	}

	@Test
	public void collectionOfPredicates() throws Exception {
		CriteriaQuery<Employee> q = cb.createQuery(Employee.class);
		Root<Employee> emp = q.from(Employee.class);

		List<Predicate> ands = new ArrayList<>();
		ands.add(cb.conjunction());
		ands.add(cb.equal(emp.get("name"), "John"));

		Predicate condition = cb.and(ands.toArray(new Predicate[] {}));
		condition = cb.or(condition, cb.equal(emp.get("department").get("name"), "QA"));

		q
				.select(emp)
				.where(condition);
		printResults(q);
	}

	@Test
	public void criteriaWithParameter() throws Exception {
		CriteriaQuery<Employee> q = cb.createQuery(Employee.class);
		Root<Employee> emp = q.from(Employee.class);

		ParameterExpression<String> nameParam = cb.parameter(String.class, "name");

		List<Predicate> ands = new ArrayList<>();
		ands.add(cb.conjunction());
		ands.add(cb.equal(emp.get("name"), nameParam));

		Predicate condition = cb.and(ands.toArray(new Predicate[] {}));
//		condition = cb.or(condition, cb.equal(emp.get("department").get("name"), "QA"));

		q
				.select(emp)
				.where(condition);

		em.createQuery(q)
		  .setParameter("name", "John")
		  .getResultList()
		  .forEach(System.out::println);
	}

	public void printResults(CriteriaQuery<?> cq) {
		List<?> result = em.createQuery(cq)
						   .getResultList();

		System.out.println("----------------------------------------------------------------------");
		result.forEach(System.out::println);
	}

	public void printTupleResults(CriteriaQuery<Tuple> cq) {
		List<Tuple> result = em.createQuery(cq)
							   .getResultList();

		System.out.println("----------------------------------------------------------------------");
		result
				.stream()
				.map(Tuple::toArray)
				.map(Arrays::toString)
				.forEach(System.out::println);
	}

	public void printAnyResults(CriteriaQuery<?> cq) {
		List<?> result = em.createQuery(cq)
						   .getResultList();

		System.out.println("----------------------------------------------------------------------");
		result
				.stream()
				.map(o -> (Object[]) o)
				.map(Arrays::toString)
				.forEach(System.out::println);
	}
}
