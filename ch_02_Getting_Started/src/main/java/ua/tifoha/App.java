package ua.tifoha;

import static java.math.BigDecimal.valueOf;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;
        try {
            emf = Persistence.createEntityManagerFactory("MainUnit");
            em = emf.createEntityManager();
            EmployeeDao dao = new EmployeeDao(em);

            em.getTransaction().begin();
            Employee employee = dao.createEmployee(1, "Vitalii Seteda", valueOf(3000));
            em.getTransaction().commit();
            System.out.println("Persisted " + employee);

            employee = dao.findOne(1).orElse(null);
            System.out.println("Find " + employee);

            // find all employees
            List<Employee> emps = dao.findAll();
            for (Employee e : emps)
				System.out.println("Found employee: " + e);
            // update the employee
            em.getTransaction().begin();
            employee = dao
					.raiseSalary(1, valueOf(1000))
					.orElse(null);
            em.getTransaction().commit();
            System.out.println("Updated " + employee);

            // remove an employee
            em.getTransaction().begin();
            dao.remove(1);
            em.getTransaction().commit();
            System.out.println("Removed Employee 1");
        } finally {
            // close the EM and EMF when done
            em.close();
            emf.close();
        }
    }
}
