package ua.tifoha;

import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
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
public class SharedContext {
	@PersistenceUnit
	private EntityManagerFactory emf;

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private PlatformTransactionManager tm;

	@Before
	public void setUp() throws Exception {
//		em = emf.createEntityManager();
	}

	@Test
	public void sharedEMNotFlushChangesFromOtherThread() throws Exception {
		TransactionStatus ts = tm.getTransaction(new DefaultTransactionDefinition());
		EntityManager sharedEntityManager = em;
		School school1 = sharedEntityManager.find(School.class, 32L);
		school1.setName("Name1");
		CompletableFuture.runAsync(() -> {
			School school2 = em.find(School.class, 42L);
			school2.setName("Name1");
			System.out.println("Slave thread end." + Thread.currentThread());
		}).join();

		tm.commit(ts);
		System.out.println("Main thread end." + Thread.currentThread());
		assertThat(sharedEntityManager.find(School.class, 32L).getName(), equalToIgnoringCase("Name1"));
		assertThat(sharedEntityManager.find(School.class, 42L).getName(), not(equalToIgnoringCase("Name1")));
	}

	@Test
	public void sharedEmSideEffectOutOfTransaction() throws Exception {
		School school1 = em.find(School.class, 32L);
		school1.setName("Name1");
		TransactionStatus ts = tm.getTransaction(new DefaultTransactionDefinition());
		tm.commit(ts);
		em.flush();
		em.clear();

//		Данные в базу запишутся потому что перед транзакцией PC небыл очищен
		assertThat(em.find(School.class, 32L).getName(), equalToIgnoringCase("Name1"));
	}

	@Test
	public void detachedEntityBeforeTransaction() throws Exception {
		School school1 = em.find(School.class, 32L);
		school1.setName("Name1");
		em.detach(school1);
		TransactionStatus ts = tm.getTransaction(new DefaultTransactionDefinition());
		tm.commit(ts);
		em.flush();
		em.clear();

//		Данные в базу не запишутся потому что ентити детачено
		assertThat(em.find(School.class, 32L).getName(), not(equalToIgnoringCase("Name1")));
	}
}
