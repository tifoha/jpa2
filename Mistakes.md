An inner join may be implicitly specified by the use of a cartesian product in the FROM clause and a join condition in the WHERE clause. In the absence of a join condition, this reduces to the cartesian product. The main use case for this generalized style of join is when a join condition does not involve a foreign key relationship that is mapped to an entity relationship.

OrderColumn is used to preserve order of elements in which they were added in a collection. There is no additional persistent field required in the collection entity, however, an additional database column is created. 

All instances of PersistenceException except for instances of NoResultException, NonUniqueResultException, LockTimeoutException, and QueryTimeoutException will cause not the current transaction, if one is active, to be marked for rollback.(section 3.9 of JPA 2.0)

While formulating a Criteria API query, it is best to build a regular sql query on paper and then think about converting it to criteria API query. 

Since it is not given what rollback value is specified for this exception (i.e. @ApplicationException(rollback=true|false), we cannot be sure about this option. If no rollback is specified, false is assumed and in that case, the transaction will not be rolled back.

The rows for element collection are not locked. They are locked only if lock.scope is set to PessimisticLockScope.EXTENDED in the query. For example: `hints={@QueryHint(name="javax.persistence.lock.scope",value="EXTENDED")}` также лочатся и ключи которые находятся в строке

Cache.evict(class) applies to all the entities of the given class and its subclasses.

Все джоины в запросе по умалчанию являются внутреними поэтому если нет совпадения пара не выводится

BMT может использовать любое количество транзакционных ресурсов A transactional resource means a resource that "understands" transactions. For example, a database or a JMS message server. Any bean can use a transactional resource. 

Any type of entity manager can be enlisted in a transaction started by a session bean with bean managed transactions.

It is implementation-dependent as to whether PreUpdate and PostUpdate callbacks occur when an entity is persisted and subsequently modified in a single transaction or when an entity is modified and subsequently removed within a single transaction. Portable applications should not rely on such behavior.

root.fetch(""students", JoinType.LEFT) будет невиден для запроса

####Valid LockModeTypes are:  
	READ : Same as OPTIMISTIC 
	WRITE: Same as OPTIMISTIC_FORCE_INCREMENT 
	OPTIMISTIC : Prevents dirty read and non-repeatable read 
	OPTIMISTIC_FORCE_INCREMENT: Prevents dirty read and non-repeatable read as well as increments the version of the object.
	
	PESSIMISTIC_READ : Acquires the lock on the row in the database + Prevents dirty read and non-repeatable read 
	PESSIMISTIC_WRITE : Acquires the lock on the row in the database + Prevents dirty read and non-repeatable read.

	An OptimisticLockException always causes the transaction to be marked for rollback. Refreshing objects or reloading objects in a new transaction context and then retrying the transaction is a potential response to an OptimisticLockException.
	PESSIMISTIC_FORCE_INCREMENT:  Acquires the lock on the row in the database + Prevents dirty read and non-repeatable read + Increments the version of the object. Support for this lock mode for non-versioned objects is not required.  
	NONE:  No Lock.

The use of map keys that contain embeddables that reference entities is not permitted.

If the client calls without a transaction context, the container throws the javax.ejb.EJBTransactionRequiredException. (If the business interface is a remote business interface that extends java.rmi.Remote, the javax.transaction.TransactionRequiredException is thrown to the client instead.)

EJBException, it is a system exception. Note that in general, any system exception thrown by an EJB would cause the respective instance to be discarded. However, that does not apply to singleton beans, because they must remain active until the shutdown of the application. Therefore, any system exception thrown on a business method or on a callback does not cause the instance to to be destroyed.

REQUIRED - если транзакция есть заюзать ее если нет тогда создать новую Indicates that business method has to be executed within transaction otherwise a new transaction will be started for that method.

REQUIRES_NEW - остановить старую и начать новую Indicates that a new transaction is to be started for the business method.

SUPPORTS - может выполнятся как в транзакции так и без Indicates that business method will execute as part of transaction.

NOT_SUPPORTED - Приостанавливает существующую транзакцию Indicates that business method should not be executed as part of transaction.

MANDATORY - Indicates that business method will execute as part of transaction otherwise exception will be thrown.

NEVER - Indicates if business method executes as part of transaction then an exception will be thrown.

Транзакция автоматически стартует перед началом запуска метода в бине и заканчивается по завершении этого метода. Container-managed транзакции не поддерживают вложенные и мульти-транзакции.

Container-managed транзакция не должна использовать методы управления транзакциями, т. к. эти операции возложены на контейнер EJB.

Subqueries may be used in WHERE or HAVING clause. NOT in the from

The query may have more than one Root.

The main issue to consider when using bulk updates is that the persistence context is not updated to reflect the results of the operation. Therefore, updating the entities using bulk update will not change the current values for any entities managed in memory as part of a persistence context.  This results in stale entity states in the persistence context. Only the entities retrieved after the bulk update contain the correct data. Лучше запускать эти методы в новой транзакции

####Remember the following for bulk operations: 
1. A bulk operation applies to entities of the specified class and its subclasses. It does not cascade to related entities.
2. Bulk update maps directly to a database update operation, bypassing optimistic locking checks. Portable applications must manually update the value of the version column, if desired, and/or manually validate the value of the version column.
3. The persistence context is not synchronized with the result of the bulk update or delete.
4. Caution should be used when executing bulk update or delete operations because they may result in inconsistencies between the database and the entities in the active persistence context. In general, bulk update and delete operations should only be performed within a separate transaction or at the beginning of a transaction (before entities have been accessed whose state might be affected by such operations).

Note that javax.persistence.Query is not a generified interface, so you cannot do Query<T>. TypedQuery is generified, so you can do TypedQuery<T>.

IN можно юзать в from `from Student s, IN(s.courses) c`

@PersistenceContext(**type**=PersistenceContextType.TRANSACTION)

An application managed persistence context (which is always an extended persistence context) exists until the entity manager is closed by means of EntityManager.close. 
A container managed extended persistence context can only exist for a stateful session bean and it exists until the bean is removed or otherwise destroyed.

* 1 - **Application Managed persistence context** - It is always an extended persistence context. In other words, the scope of the persistence context of an application-managed entity manager is always extended. It is the responsibility of the application to manage the lifecycle of the persistence context.  
* 2 - **Container Managed persistence context** - It can be transaction scoped or extended. In other words, the lifetime of a container-managed persistence context can either be scoped to a transaction (transaction-scoped persistence context), or have a lifetime scope that extends beyond that of a single transaction (extended persistence context). The enum PersistenceContextType is used to define the persistence context lifetime scope for container-managed entity managers. The persistence context lifetime scope is defined when the EntityManager instance is created (whether explicitly, or in conjunction with injection or JNDI lookup). By default, the lifetime of the persistence context of a container-managed entity manager corresponds to the scope of a transaction (i.e., it is of type PersistenceContextType.TRANSACTION).  
* A new **transaction scoped persistence context** begins when the container-managed entity manager is invoked in the scope of an active JTA transaction, and there is no current persistence context already associated with the JTA transaction. The persistence context is created and then associated with the JTA transaction. The persistence context ends when the associated JTA transaction commits or rolls back, and all entities that were managed by the EntityManager become detached.  
* A **container-managed extended persistence context** can only be initiated within the scope of a stateful session bean. It exists from the point at which the stateful session bean that declares a dependency on an entity manager of type PersistenceContextType.EXTENDED is created, and is said to be bound to the stateful session bean. The dependency on the extended persistence context is declared by means of the PersistenceContext annotation or persistence-context-ref deployment descriptor element. The persistence context is closed by the container when the @Remove method of the stateful session bean completes (or the stateful session bean instance is otherwise destroyed).  
* An **application managed extended persistence context** exists from the point at which the entity manager has been created using EntityManagerFactory.createEntityManager until the entity manager is closed by means of EntityManager.close. An extended persistence context obtained from the application-managed entity manager is a stand-alone persistence context—it is not propagated with the transaction.

The container throws the TransactionRequiredException if a transaction-scoped persistence context is used, and the EntityManager persist, remove, merge, or refresh method is invoked when no transaction is active.

The rule of thumb for persistence context propagation is that the persistence context propagates as the JTA transaction propagates. Когда стартует новая транзакция может создаться новый персистанс контекст

When a stateful session bean with an extended persistence context creates another stateful session bean that also uses an extended persistence context, the child will inherit the parent’s persistence context. 

#2

If the bean uses Container Managed Transactions, then the exception would be thrown because of sessionContext.getUserTransaction()

The MapKeyColumn annotation is used to specify the mapping for the key column of a map whose map key is a basic type. 

If it is a new entity, it is ignored by the remove operation. но каскады срабатывают. If it is a removed entity, it is ignored by the remove operation. If X is a detached entity, an IllegalArgumentException will be thrown by the remove operation (or the transaction commit will fail).

В отношинии @OneToOne обе стороны могут иметь ссылки друг на друга, хотя это и не приветствуется

(Like Cascade.Romove)If orphanRemoval is true and an entity that is the target of the relationship is removed from the relationship (either by removal from the collection or by setting the relationship to null), the remove operation will be applied to the entity being orphaned.

Если в рамках одной транзакции удалить а потом замерджить ентити тогда вызовется метод PreRemove а после этого будет брошено исключение

```
public static volatile SingularAttribute<X, Y> y;  
public static volatile CollectionAttribute<X, Z> z;      
public static volatile SetAttribute<X, Z> z;      
public static volatile ListAttribute<X, Z> z;      
public static volatile MapAttribute<X, K, Z> z;
```

Two NULL values are not considered to be equal, the comparison yields an unknown value.

Since isolation levels of transactions are implemented differently in various databases, the EJB specification has not standardized its usage.

####Thus, to ensure that the results of past queries in the persistence context are visible to the query   
1. either the flush mode on EntityManager should be set to AUTO and flush mode on Query should not be set.   
2. or, the flush mode on Query should be set to AUTO.

A JoinTable annotation is specified on the owning side of the association. A join table is typically used in the mapping of many-to-many and unidirectional one-to-many associations. It may also be used to map bidirectional many-to-one/one-to-many associations, unidirectional many-to-one relationships, and one-to-one associations (both bidirectional and unidirectional).

An entity is considered to be loaded if all attributes with FetchType.EAGER—whether explicitly specified or by default—(including relationship and other collection-valued attributes) have been loaded from the database or assigned by the application. Attributes with FetchType.LAZY may or may not have been loaded.

The ordering of NULL values is not specified.

Support for the table per concrete class inheritance mapping strategy is optional in this release. 

#3
Every entity class must have a public or protected no-args constructor. даже абстрактные ентити

cache.evict(class, id) удалит из кеша только ентити конкретного класса и не тронет его потомков

Only a stateful session bean is permitted to continue a transaction beyond a method. A stateless session bean must either committ or rollback a transaction before it returns.

```
 @PersistenceContext(unitName="myPU", name="myapp/myPU") 
 public class MyServlet extends HttpServlet{
```

Since the person entity has been removed at //3, a call to merge the same entity will throw an IllegalArgumentException or the call to commit will fail later.

If the result set of the query contains entities that have been updated in the persistence context in the same transaction, those entities may not contain the updates.

FlushModeType.COMMIT  не гарантирует что изменения не будут слиты в базу перед выполнением запроса, а рекомендуют провайдеру такая же фигня как с Lazy

Если бин ловит EJBException тогда бин будет унечтожен

Не забывать про функции для работы с коллекциями

