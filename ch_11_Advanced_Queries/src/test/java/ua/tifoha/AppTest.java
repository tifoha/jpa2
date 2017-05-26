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
