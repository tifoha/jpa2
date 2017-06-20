package ua.tifoha;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Collections;

/**
 * Hello world!
 */
public class App {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("MainUnit", Collections.singletonMap("javax.persistence.schema-generation.database.action", "drop-and-create"));
		EntityManager em = emf.createEntityManager();
//        em.find(Pupil.class, 1L);
//		em.find(School.class, 1L);
		em.close();
		emf.close();
		System.out.println("Hello World!");
	}
}
