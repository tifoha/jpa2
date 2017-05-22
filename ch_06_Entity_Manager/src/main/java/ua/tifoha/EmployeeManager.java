package ua.tifoha;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Vitaliy Sereda on 11.05.17.
 */
@Service
@Transactional
public class EmployeeManager {
	@PersistenceContext
	private EntityManager em;

	@Transactional()
	public Employee getNew(String name) {
		Employee emp = new Employee();
		emp.setName(name);
//		em.joinTransaction();
		em.persist(emp);
		System.out.println(em.contains(emp));

		return emp;
	}
}
