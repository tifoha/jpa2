package ua.tifoha;

/**
 * Created by Vitaliy Sereda on 12.05.17.
 */

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ua.tifoha.config.RootConfig;

/**
 * Created by Vitaliy Sereda on 11.05.17.
 */
@RunWith (SpringJUnit4ClassRunner.class)
@ContextConfiguration (classes = RootConfig.class)
@Transactional
public class AttachDetach {
	@PersistenceUnit
	private EntityManagerFactory emf;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private PlatformTransactionManager tm;

	@Test
	public void detachSerializedEntity() throws Exception {
		TransactionStatus ts = tm.getTransaction(new DefaultTransactionDefinition());
		School school = new School("Test");
		em.persist(school);
		tm.commit(ts);

		assertTrue(em.contains(school));

		byte[] bytes = toByteArray(school);
		school = toObject(bytes);

		assertFalse(em.contains(school));
	}

	@Test
	public void mergeDetachedEntityWithRelations() throws Exception {
		TransactionStatus ts = tm.getTransaction(new DefaultTransactionDefinition());
		School school = new School("Test");
		Student student = new Student("Vitaly");
		school.students.add(student);
		em.persist(school);
		tm.commit(ts);
		em.flush();
		em.clear();

		student.setName("Dasha");
		em.merge(school);

		assertThat(em.find(Student.class, student.id).getName(), Matchers.equalToIgnoringCase("Dasha"));

		em.clear();
		school.students.add(new Student("Vitaly"));
		em.merge(school);
		System.out.println(school.students);
	}

	private byte[] toByteArray(Object object) {
			try(ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutput out = new ObjectOutputStream(bos)) {
				out.writeObject(object);
				out.flush();
				return bos.toByteArray();
		} catch (IOException e) {
				throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T toObject(byte[] bytes) {
		try(ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream in = new ObjectInputStream(bis);) {
			return (T) in.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
}
