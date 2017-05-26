package ua.tifoha;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ua.tifoha.inheritance.*;
import ua.tifoha.inheritance.Employee;

/**
 * Created by Vitaliy Sereda on 26.05.17.
 */
@SuppressWarnings ("unchecked")
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = RootConfig.class)
@Transactional
@Rollback
public class InheritanceTest {
	@PersistenceContext
	private EntityManager em;
	@Test
	public void findFromHierarchy() throws Exception {
		final Employee fullTimeEmployee = em.find(FullTimeEmployee.class, 1);
		final Employee contactEmployee = em.find(Employee.class, 1);

		assertNull(fullTimeEmployee);
		assertNotNull(contactEmployee);
	}
}
