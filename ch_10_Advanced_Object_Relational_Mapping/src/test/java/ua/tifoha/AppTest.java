package ua.tifoha;

import static java.util.Collections.singletonMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
//@SuppressWarnings ("unchecked")
//@RunWith (SpringJUnit4ClassRunner.class)
//@ContextConfiguration (classes = RootConfig.class)
//@Transactional
//@Rollback
public class AppTest {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("MainUnit",
            singletonMap("hibernate.hbm2ddl.auto", "create"));

    private EntityManager em = emf.createEntityManager();

    @Test
    public void simpleDerivedId() throws Exception {
        em.getTransaction().begin();
        Employee e = new Employee();
        em.persist(e);
        EmployeeInfo ei = new EmployeeInfo();
        ei.setEmployee(e);
        em.persist(ei);
        em.getTransaction().commit();

        em.clear();

        final EmployeeInfo employeeInfo = em.find(EmployeeInfo.class, 1);
        System.out.println(employeeInfo);
    }

    @Test
    public void readOnlyEntity() throws Exception {
        em.getTransaction().begin();
        InsertOnlyEntity e = new InsertOnlyEntity("asdfas");
        em.persist(e);
        em.getTransaction().commit();

        e.setValue("111111");

        em.getTransaction().begin();
        em.merge(e);
        em.getTransaction().commit();

    }

    @Test
    public void oneWayRelationShips() throws Exception {
        em.getTransaction().begin();

        OneWayRelations e = new OneWayRelations();
        Phone phone = new Phone();
        phone.setType("HOME");
        phone.setNumber("123456789");
        em.persist(phone);
        em.persist(e);
        em.persist(phone);
        em.getTransaction().commit();
    }

    @Test
    public void compositeRelationShips() throws Exception {
        em.getTransaction().begin();

        CompositeIdRelations parent = new CompositeIdRelations();
        em.getTransaction().commit();
    }

    @Test
    public void orphanRemoval() throws Exception {
        em.getTransaction().begin();
        Department d = new Department();
        em.persist(d);
        Employee e1 = new Employee();
        Employee e2 = new Employee();
        em.persist(e1);
        em.persist(e2);
        e1.setDepartment(d);
        e2.setDepartment(d);
        em.getTransaction().commit();

        em.getTransaction().begin();
        d = em.find(Department.class, d.getId());
        d.getEmployees().remove(e1);
        e1.setDepartment(null);
        em.merge(e1);
        em.persist(d);
        em.getTransaction().commit();
    }
}
