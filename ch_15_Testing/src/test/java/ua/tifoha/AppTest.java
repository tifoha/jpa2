package ua.tifoha;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest{
    @Test
    public void test() throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MainUnit", Collections.singletonMap("javax.persistence.schema-generation.database.action", "drop-and-create"));
        EntityManager em = emf.createEntityManager();
//        em.find(Pupil.class, 1L);
//        em.find(School.class, 1L);
        em.close();
        emf.close();
    }
}
