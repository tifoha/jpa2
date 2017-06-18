####Container-Managed Entity Managers are created by resolve container annotation @PersistenceContext 
	* TRANSACTION-SCOPED Every time an operation is invoked on the entity manager, the container proxy for that entity manager checks to see whether a persistence context is associated with the container JTA transaction. If PC is associatet with transaction then it uses if not then ne PC created. When Tx is end then PC go away. This process is repeated every time one or more entity manager operations are invoked within a transaction. when PC go away entity becomes detached. any changes to its state will be ignored 
	* EXTENDED (PersistenceContextType.EXTENDED) The lifecycle of an extended persistence context is tied to the stateful session bean to which it is bound. При старте транзакции она привязывается к ужу существующему контексту. Могут быть проблемы если stateful bean будет вызван из stateless bean. (Коллизия контекстов) 
	
Application-Managed Entity Managers The entity manager in that example, and any entity manager that is created from the createEntityManager() call of an EntityManagerFactory instance

the entity manager is explicitly closed with the close() call when it is no longer needed in application-managed entity manager, he EntityManagerFactory instance must also be closed,

####TRANSACTION MANAGEMENT 

Transactions define when new, changed, or removed entities are synchronized to the database. 

Transaction synchronization is the process by which a persistence context is registered with a transaction so that the persistence context can be notified when a transaction commits.(to flush)

Transaction association is the act of binding a persistence context to a transaction. 

Transaction propagation is the process of sharing a persistence context between multiple container-managed entity managers in a single transaction. There can be only one persistence context associated with and propagated across a JTA transaction.  

####TRANSACTION-SCOPED PERSISTENCE CONTEXTS 
An entity manager will create a persistence context only when a method is invoked on the entity manager and when there is no persistence context available. 

The rule of thumb for persistence context propagation is that the persistence context propagates as the JTA transaction propagates.

####EXTENDED PERSISTENCE CONTEXTS

The stateful session bean is associated with a single extended persistence context that is created when the bean instance is created and closed when the bean instance is removed. 

Situations in which the two persistence contexts collide with each other. 

If the stateful session bean is largely self-contained; that is, it does not call other session beans and does not need its persistence context propagated, a default transaction attribute type of NOT_SUPPORTED can be worth considering. 
 
####PERSISTENCE CONTEXT INHERITANCE 
When a stateful session bean with an extended persistence context creates another stateful session bean that also uses an extended persistence context, the child will inherit the parent’s persistence context. 

####APPLICATION-MANAGED PERSISTENCE CONTEXTS 
There is no limit to the number of application-managed persistence contexts that can be synchronized with a transaction, but only one container-managed persistence context will ever be associated. 
If the persistence context is created inside the transaction, the persistence provider will automatically synchronize the persistence context with the transaction. 
If the persistence context was created earlier (outside of a transaction or in a transaction that has since ended), the persistence context can be manually synchronized with the transaction by calling joinTransaction() on the EntityManager interface. (automatically be flushed) 
EntityManagerFactory instances are thread-safe,  EntityManager instances are not. 
Also, application code must not call joinTransaction() on the same entity manager in multiple concurrent transactions. 

Entity попадает в PS после вызова метода persist или marge и будет сохранено в БД автоматически после того как будет закоммичена транзакция к которой он подключен
An entity manager can be explicitly specified to have an unsynchronized persistence context, requiring it to manually join any JTA transaction it wants to participate in. 
@PersistenceContext(unitName="EmployeeService",synchronization=UNSYNCHRONIZED) Creates EM that is not sinchronized with context
When an unsynchronized persistence context has not been joined to a transaction, no writes to the database, such as those resulting from a user-initiated flush() call, may occur. attempts to do so will cause an exception to be thrown.

####RESOURCE-LOCAL TRANSACTIONS
Resource-local transactions are controlled explicitly by the application. 

The EntityTransaction interface is designed to imitate the UserTransaction interface defined by JTA, and the two behave very similarly. 

If the transaction is marked for rollback, and a commit is attempted, a RollbackException will be thrown. (Call getRollbackOnly() before commit())
 
####TRANSACTION ROLLBACK AND ENTITY STATE
In your application to control whether data is committed to the database, you can’t truly apply the same techniques to the in-memory persistence context that manages your entity instances. 

#####If transaction rolls back, two things happen:
	* The database transaction will be rolled back.  
	* The persistence context is cleared, detaching all our managed entity instances. 
	* If the persistence context was transaction-scoped, it is removed. 

#####Faced with a rolled-back transaction and detached entities, you might be tempted to start a new transaction, merge the entities into the new persistence context, and start over. The following issues need to be considered in this case: 
	* (Primary key collision) If there is a new entity that uses automatic primary key generation, there can be a primary key value assigned to the detached entity. If this primary key was generated from a database sequence or table, the operation to generate the number might have been rolled back with the transaction. This means that the same sequence number could be given out again to a different object. Clear the primary key before attempting to persist the entity again, and do not rely on the primary key value in the detached entity. 
	* (version collision) If your entity uses a version field for locking purposes that is automatically maintained by the persistence provider, it might be set to an incorrect value. The value in the entity will not match the correct value stored in the database. We will cover locking and versioning in Chapter 12. 
If you need to reapply some of the changes that failed and are currently sitting in the detached entities, consider selectively copying the changed data into new managed entities.

The changes made to unsynchronized persistence contexts are not transactional and are therefore mostly immune to transaction failures. But if a persistence unit is configured to access a transactional data source (e.g. by setting the jta-data-source element in persistence.xml) then that data source will be used to read entities from the database, even by entity managers with unsynchronized persistence contexts. You can define a non-transactional data source in the server and reference it in the persistence unit (by setting the non-jta-data-source element in persistence.xml to the non-transactional data source) 

####ENTITY MANAGER OPERATIONS

**persist()** new entity instance become managed. If the entity to be persisted is already managed by the persistence context, it is ignored. TransactionRequiredException outside of the transaction. Application-managed and extended entity managers will persist, causing the entity to become managed, but not flush to DB until a new transaction begins and the persistence context becomes synchronized with the transaction. 

**contains()** operation can be used to check whether an entity is already managed, but it is very rare that this should be required.  

For an entity to be managed does not mean that it is persisted to the database right away.

При связывании объектов обезательно засетить значение на owner стороне иниче ничего не запишется в БД

The find() operation returns a managed entity instance in all cases except when invoked outside of a transaction on a transaction-scoped entity manager. (Провайдеры могут использовать кеш)

**getReference()** только возвращает ссылку(провайдер может вернуть прокси без запроса в БД) на ентити(для создания связей) Ты должен быть уверен что ентити существует

remove() удаляет сущьность перед тем как удалить сущьность нужно обнулить все ее ссылки

An entity can be removed only if it is managed by a persistence context.

A removed entity instance can be persisted again with the persist() operation, but the same issues with generated state that we discussed in the “Transaction Rollback and Entity State” section apply here as well. 

**clear()** method of the EntityManager interface can be used to clear the persistence context. 

**detach()** удаляет сущьность из памяти PC. When detach new or removed entity does not detach either new or removed entities, but it will still attempt, when configured to cascade, to cascade

**flush()** сливает измененные сущьности в БД, если транзакция откатится БД вернется в предидущее состояние. Не вызывается пока все транзакции в иерархии не завершаться. A provider might also flush the persistence context often if it uses an eager-write approach to entity updates.
	* OneToOne & OneToMany могут сохранятся если цели не сохранены

**merge()** добавляет сущьность в PC 
	* сначала читает из БД 
	* перезаписывает все поля старой сущьности новой
	* возвращает managed entity!!! (Если изменить старую сущьность изменения не запишутся) emp = em.merge(emp);


When merge() is invoked on a new entity, it behaves similarly to the persist() operation.

In the presence of relationships, the merge() operation will attempt to update the managed entity to point to managed versions of the entities referenced by the detached entity. If the entity has a relationship to an object that has no persistent identity, the outcome of the merge operation is undefined. 

При мердже сущьности с связями будут терджится только Id сущьностей, если не указан каскад

####CASCADING OPERATIONS 

Каскады работают на обеих сторонах

There are really only two cases in which cascading the remove() operation makes sense: one-to-one  and one-to-many relationships, in which there is a clear parent-child relationship.

if an owning relationship is safe to use REMOVE, it is also safe to use PERSIST. 

####DETACHMENT AND MERGING

Detached entity is one that is no longer associated with a persistence context.

A detached entity cannot be used with any entity manager operation that requires a managed instance.

Any changes to entity state that were made on the detached entity overwrite the current values in the persistence context. 

####Situations will lead to detached entities:
 	* When the transaction that a transaction-scoped persistence context is associated with commits, all the entities managed by the persistence context become detached. 
 	* If an application-managed persistence context is closed, all its managed entities become detached. 
 	* If a stateful session bean with an extended persistence context is removed, all its managed entities become detached. 
 	* If the clear() method of an entity manager is used, it detaches all the entities in the persistence context managed by that entity manager. 
 	* If the detach() method of an entity manager is used, it detaches a single entity instance from the persistence context managed by that entity manager. 
 	* When transaction rollback occurs, it causes all entities in all persistence contexts associated with the transaction to become detached. 
 	* When an entity is serialized, the serialized form of the entity is detached from its persistence • context. 
 	
The behavior of accessing an unloaded attribute when the entity is detached is not defined. Some vendors might attempt to resolve the relationship, while others might simply throw an exception or leave the attribute uninitialized. (EclipseLink allow!!!)

If the Department entity changes as well, it is better to cascade the merge from the Department to its associated Employee instances and then merge only a single Department instance instead of multiple Employee instances. 


####WORKING WITH DETACHED ENTITIES
	* Triggering Lazy Loading Для этого нужно дернуть метод объекта (getName) чтоб инициировать прокси
	* Configuring Eager Loading
	* Transaction View (Открыть транзакцию и закрыть ее после выполнения всех действий)(filter for transaction)
	* Entity Manager per Request (создать EM во время реквеста и закрыть его в конце, либо создать stateful bean with extended EM)
	
Чтоб не потерять связи и незатронутые изменения во время мерджа нужно хранить где-то прочитаную из базы сущьность (можно в сессии либо перед изменениями прочитать ее)

#####The pattern for using stateful session beans and extended entity managers in the web tier is as follows:
	1. For each application use case that modifies entity data, we create a stateful session bean with an extended persistence context. This bean will hold onto all entity instances necessary to make the desired changes.
	2. The HTTP request that initiates the editing use case creates an instance of the stateful session bean and binds it to the HTTP session. The entities are retrieved at this point and used to populate the web form for editing.
	3. The HTTP request that completes the editing use case obtains the previously bound stateful session bean instance and writes the changed data from the web form into the entities stored on the bean. A method is then invoked on the bean to commit the changes to the database. 

Подход с stateful session bean хорошо вписывается в JSF page flow. После окончания редактирования/отмена бины должны быть удалины, можно заюзать HttpSessionBindingListener

The HTTP session is not thread-safe