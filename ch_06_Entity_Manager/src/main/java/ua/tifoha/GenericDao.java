package ua.tifoha;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

/**
 * Created by Vitaliy Sereda on 21.04.17.
 */
public abstract class GenericDao<T> {
	private final EntityManager em;
	private final Class<T> entityClass;

	protected GenericDao(EntityManager em, Class<T> entityClass) {
		this.em = em;
		this.entityClass = entityClass;
	}

	public Optional<T> findOne(long id) {
		return Optional.ofNullable(em.find(entityClass, id));
	}

	public List<T> findAll() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> cq = cb.createQuery(entityClass);
		Root<T> rootEntry = cq.from(entityClass);
		CriteriaQuery<T> all = cq.select(rootEntry);
		TypedQuery<T> allQuery = em.createQuery(all);

		return allQuery.getResultList();
	}

	public T save(T entity) {
		em.getTransaction().begin();
		em.persist(entity);
		em.getTransaction().commit();
		return entity;
	}
//	public Employee createEmployee(long id, String name, BigDecimal salary) {
//		Employee employee = new Employee(id);
//		employee.setName(name);
//		employee.setSalary(salary);
//		employee.setEmbeddedField(new EmbeddedField("Value"));
//		em.persist(employee);
//
//		return employee;
//	}

//	public Optional<T> raiseSalary(long id, BigDecimal delta) {
//		Optional<T> employee = findOne(id);
//
//		if (employee.isPresent()) {
//			Employee e = employee.get();
//			e.setSalary(e.getSalary().add(delta));
//			em.persist(e);
//		}
//		return employee;
//	}

	public void remove(long id) {
		em.getTransaction().begin();
		findOne(id)
				.ifPresent(em::remove);
		em.getTransaction().commit();
	}

	public EntityManager getEntityManager() {
		return em;
	}

	public void flush() {
		em.flush();
	}
}
