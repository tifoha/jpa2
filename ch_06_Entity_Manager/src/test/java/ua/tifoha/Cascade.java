package ua.tifoha;

import static org.hamcrest.Matchers.hasSize;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.RollbackException;

import java.rmi.server.ExportException;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import ua.tifoha.config.RootConfig;

/**
 * Created by Vitaliy Sereda on 11.05.17.
 */
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = RootConfig.class)
@Transactional
public class Cascade {
	@PersistenceUnit
	private EntityManagerFactory emf;

	private EntityManager em;

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Before
	public void setUp() throws Exception {
		em = emf.createEntityManager();
	}

	@Test
	public void ownerCascade() throws Exception {
		em.getTransaction().begin();
		School school = new School("S1");
		Student student = new Student("Vitaly");
		student.school = school;
		em.persist(student);
		em.getTransaction().commit();

		em.clear();
		Assert.assertNotNull(em.find(Student.class, student.getId()));
		Assert.assertNotNull(em.find(School.class, student.school.getId()));
	}

	@Test
	public void slaveCascade() throws Exception {
		em.getTransaction().begin();
		School school = new School("S1");
		Student student1 = new Student("Vitaly");
		Student student2 = new Student("Dasha");
		school.students.add(student1);
		student1.school = school;
		school.students.add(student2);
		student2.school = school;
		em.persist(school);
		em.getTransaction().commit();

		em.clear();
		Assert.assertNotNull(em.find(Student.class, student1.getId()));
		Assert.assertNotNull(em.find(Student.class, student2.getId()));
		school = em.find(School.class, student1.school.getId());
		Assert.assertNotNull(school);
		Assert.assertThat(school.students, hasSize(2));
		school = em.find(School.class, student2.school.getId());
		Assert.assertNotNull(school);
	}

	@Test
	public void persistCascadeFailOnDetachedrefferenceToEntity() throws Exception {
		exception.expect(RollbackException.class);
		em.getTransaction().begin();
		final School school = new School();
		final Director director = new Director();
		school.director = director;
		em.persist(school);
		em.getTransaction().commit();
	}

	@Test
	public void persistCascadeWillIgnoreownedEntityWhenSlavePersist() throws Exception {
		em.getTransaction().begin();
		final School school = new School();
		final Director director = new Director();
		school.director = director;
		em.persist(director);
		em.getTransaction().commit();
	}
}
