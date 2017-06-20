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
    public void localClassesTest() throws Exception {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MainUnit", Collections.singletonMap("javax.persistence.schema-generation.database.action", "drop-and-create"));
        EntityManager em = emf.createEntityManager();
//        em.find(Pupil.class, 1L);
//        em.find(School.class, 1L);
        em.close();
        emf.close();
    }

    @Test
    public void schemaAndScriptsGeneration() throws Exception {
        final Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("javax.persistence.schema-generation.database.action", "drop-and-create");
        properties.put("javax.persistence.schema-generation.scripts.action", "drop-and-create");
        properties.put("javax.persistence.schema-generation.scripts.create-target", Files.newBufferedWriter(Paths.get("/Users/user/IdeaProjects/jpa2/ch_14_Packaging_And_Deployment/src/main/resources/sql/create.ddl"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
        properties.put("javax.persistence.schema-generation.scripts.drop-target", "file:////Users/user/IdeaProjects/jpa2/ch_14_Packaging_And_Deployment/src/main/resources/sql/drop.ddl");
        properties.put("javax.persistence.schema-generation.scripts.drop-target", "file:////Users/user/IdeaProjects/jpa2/ch_14_Packaging_And_Deployment/src/main/resources/sql/drop1.ddl");

//        properties.put("", "");
//        properties.put("", "");
//        properties.put("", "");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MainUnit", properties);
        EntityManager em = emf.createEntityManager();
//        em.find(Pupil.class, 1L);
//        em.find(School.class, 1L);
        em.close();
        emf.close();
//        <property name="javax.persistence.schema-generation.scripts.action"value="create"/> <property name="javax.persistence.schema-generation.scripts.create-targetvalue="file:///c:/scripts/create.ddl"/>
    }
}
