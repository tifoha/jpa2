package ua.tifoha;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.metamodel.ManagedType;

/**
 * Created by Vitaliy Sereda on 21.04.17.
 */
public class DaoFactory implements AutoCloseable{
	private final EntityManagerFactory entityManagerFactory;

	public DaoFactory(String persistanceUnit) {
		entityManagerFactory = Persistence.createEntityManagerFactory(persistanceUnit);
	}

	public<T> GenericDao<T> getDao(Class<T> entityClass) {
		ManagedType<T> managedType = entityManagerFactory.getMetamodel().managedType(entityClass);
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		return new GenericDao<T>(entityManager, entityClass) {};
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	@Override
	public void close() {
		entityManagerFactory.close();
	}
}
