#Other Advanced Topics 
##Lifecycle Callbacks
The event types that make up the lifecycle fall into four categories: persisting, updating, removing, and loading.
In the load category there is only a PostLoad event because it would not make any sense for there to be PreLoad on an entity that was not yet built. 
####Lifecycle Events 
	* PrePersist
	* PostPersist
	* PreUpdate
	* PostUpdate
	* PreRemove
	* PostRemove
	* PostLoad

**PrePersist** вызывается на сущьности после того когда был вызван метод EntityManager.persist() или merge() (если сущьность новая) If multiple entities are cascaded to during the same operation, the order in which the PrePersist callbacks occur cannot be relied upon. 
**PostPersist** events occur when an entity is inserted, which normally occurs during the transaction completion phase. Firing of a PostPersist event does not indicate that the entity has committed successfully to the database because the transaction in which it was persisted may be rolled back after the PostPersist event but before the transaction successfully commits. 
When an EntityManager.remove() call is invoked on an entity, the **PreRemove** callback is triggered. 
When the SQL for deletion of an entity finally does get sent to the database, the **PostRemove** event will get fired. 
The **PostLoad** callback occurs after the data for an entity is read from the database and the entity instance is constructed. This can get triggered by any operation that causes an entity to be loaded, normally by either a query or traversal of a lazy relationship. It can also happen as a result of a refresh() call on the entity manager. (очередность не определена)
Other lifecycle methods may be defined by specific providers, such as when an entity is merged or copied/cloned. 
Final or static methods are not valid callback methods, however. 
Checked exceptions may not be thrown from callback methods because the method definition of a callback method is not permitted to include a throws clause.
A method may be annotated with multiple lifecycle event annotations, but only one lifecycle annotation of a given type may be present in an entity class.
In lifecycle methods Looking up resources in JNDI or using JDBC and JMS resources are allowed, so looking up and invoking EJB session beans would be allowed. 
Callback methods are executed in whatever contexts are active at the time they are invoked. 
Multiple event listeners may be applied to an entity, though. 
the signature required of callback methods on entity listeners is slightly different from the one required on entities. On an entity listener, a callback method must have a similar signature as an entity with the exception that it must also have a single defined parameter of a type that is compatible with the entity type, as the entity class, a superclass (including Object), or an interface implemented by the entity. 
Entity listener classes must be stateless, every entity listener class must have a public no-argument constructor. 
Можно делать CDI в EntityListner но нужно будет писать интеграцию с спрингом самостоятельно в случае с hibernate это ListenerFactory 
для аттача листнера к сушьности нужно использовать @EntityListeners
If any of the listeners throws an exception, it will abort the callback process, causing the remaining listeners and the callback method on the entity to not be invoked. 
There is currently no standard annotation target for persistence unit scoped metadata, so this kind of metadata can be declared only in an XML mapping file
Default entity listeners will always get invoked before any of the entity listeners listed in the @EntityListeners annotation for a given entity. 
When an entity is annotated with @ExcludeDefaultListeners annotation, none of the declared default listeners will get invoked for the lifecycle events for instances of that entity type. 	
@EntityListeners annotation on an entity is additive in that it only adds listeners; it does not redefine them or their order of invocation.

Entity listeners order:
	1. Check whether any default entity listeners exist (see Chapter 13). If they do, iterate through them in the order they are defined and look for methods that are annotated with the lifecycle event X annotation. Invoke the lifecycle method on the listener if a method was found.
	2. Check the highest mapped superclass or entity in the hierarchy for classes that have an @EntityListeners annotation. Iterate through the entity listener classes that are listed in the annotation and look for methods that are annotated with the lifecycle event X annotation. Invoke the lifecycle method on the listener if a method was found.
	3. Repeat step 2 going down the hierarchy on entities and mapped superclasses until entity A is reached, and then repeat it for entity A.
    4. Check the highest mapped superclass or entity in the hierarchy for methods that are annotated with the lifecycle event X annotation. Invoke the callback method on the entity if a method was found and the method is not also defined in entity A with the lifecycle event X annotation on it.
    5. Repeat step 2 going down the hierarchy on entities and mapped superclasses until entity A is reached.
    6. Invoke any methods that are defined on A and annotated with the lifecycle event  X annotation. 
	

##Validation
The main API class for invoking validation on an object is the javax.validation.Validator class. Once a Validator instance is obtained, the validate() 6method can be invoked on it, passing in the object to be validated. 
It may be that the same object needs to be validated at different times for multiple different constraint sets. To achieve this, we would create separate validation groups and specify which group or groups the constraint belongs to.
Fore lazy loading the process of validation could unwittingly cause the entire object graph to be loaded into memory! Or case an exception when lazy field validated on detached entity
поля объектов @Embedded или связи будут провалидированы только тогда когда они помечены аннотацией @Valid
####When no overriding settings are present at the JPA configuration level, validation is on by default when a validation provider is on the classpath. To explicitly control whether validation should be enabled or disabled, there are two possible settings. 
	* validation-mode element in the persistence.xml file. This element may be set to one of three possible values. 
	* AUTO: Turn on validation when a validation provider is present on the classpath (default). 
	* CALLBACK: Turn on validation and throw an error if no validation provider is available. 
	* NONE: Turn off validation. • javax.persistence.validation.mode persistence property. This property may be specified in the Map passed to the createEntityManagerFactory() method and overrides the validation-mode setting if present. Possible values are the string equivalents of the validation-mode values (AUTO, CALLBACK, and NONE) and have exactly the same meanings as their validation-mode counterparts. 
By default, each of the PrePersist and PreUpdate lifecycle events will trigger validation on the affected entity, immediately following the event callback, using the Default validation group.
No group will be validated, by default, during the PreRemove phase. 

####Properties to specify validation group for lifecycle callbacks
	* javax.persistence.validation.group.pre-persist: Set the groups to validate  at PrePersist time. 
	* javax.persistence.validation.group.pre-update: Set the groups to validate  at PreUpdate time. 
	* javax.persistence.validation.group.pre-remove: Set the groups to validate  at PreRemove time. 
example: `<property name="javax.persistence.validation.group.pre-remove"value="Remove"/>`
It is not currently portable in Jpa to set the groups on a per-entity basis, although some providers may provide such capabilities. providers that do support it would typically allow the entity name to be an additional suffix on the  property name (e.g., javax.persistence.validation.group.pre-remove.Employee). 

##Concurrency
A managed entity belongs to a single persistence context and should not be managed by more than one persistence context at any given time. This is an application responsibility, however, and may not necessarily be enforced by the persistence provider. Merging the same entity into two different open persistence contexts could produce undefined results.
Entity managers and the persistence contexts that they manage are not intended to be accessed by more than one concurrently executing thread. (поток должен сам побеспокоится о синхронности)
Applications may not access an entity directly from multiple threads while it is managed by a persistence context.  An application may choose, however, to allow entities to be accessed concurrently when they are detached. 
Concurrent entity state access is not recommended, however, because the entity model does not lend itself well to concurrent patterns. 
It would be preferable to simply copy the entity and pass the copied entity to other threads for access and then merge any changes back into a persistence context when they need to be persisted. 

##Refreshing Entity State 
Рефрешить можно только managed entity. The refresh() method of the EntityManager interface can be useful in situations when we know or suspect that there are changes in the database that we do not have in our managed entity.
One of the primary use cases is to “undo” or discard changes made in the current transaction, reverting them back to their original value.
Refresh operations may also be cascaded across relationships. This is done on the relationship annotation by setting the cascade element to include the REFRESH value. If the REFRESH value is not present in the cascade element, the refresh will stop at the source entity. 

##Locking
The optimistic locking model is based on the premise that there is a good chance that the transaction in which changes are made to an entity will be the only one that actually changes the entity during that interval.
При оптимистической блокировке транзакция не блокирует сущьность до тех пор пока она не попытается ее записат перед записью она проверит состояние сушьности из бд и если оно такое как было при чтении запишет новые изменения. если нет то бросит OptimisticLockException и откатит транзакцию
Version fields are not required, but we recommend that version fields be in every entity that has any chance of being concurrently modified by more than one process. 
A version column is an absolute necessity whenever an entity gets modified as a detached entity and merged back into a persistence context again afterward.
Version-locking fields defined on the entity can be of type int, short, long, the corresponding wrapper types, and java.sql.Timestamp. 
Приложение не должно редактировать поле @Version после того как сушьность была создана, читать можно
some providers do not require that the version field be defined and stored in the entity. variations of storing it in the entity are storing it in a vendor-specific cache, or not storing anything at all but instead using field comparison. For example, a popular option is to compare some application-specified combination of the entity state in the database with the entity state being written and then use the results as criteria to decide whether state has been changed. 
Vendor not guaranteed to be updated, either in the managed entities or the database, as part of a bulk update operation. (Для тех кто не гарантирует обновление версии во ввремя bulk update можно заложить обновление в меха)
version fields will be automatically updated only when either the non-relationship fields or the owning foreign key relationship fields (e.g., many-to-one and one-to-one source foreign key relationships) are modified. 
By default, JPA  assumes what is defined in the ANSI/ISO SQL specification and known in transaction isolation parlance as Read Committed isolation. 
####Locking options can be specified by means of a number of different calls: 
	* EntityManager.lock(): Explicit method for locking objects already in the persistence context. 
	* EntityManager.refresh(): Permits a lock mode to be passed in and applies to the object in the persistence context being refreshed. 
	* EntityManager.find(): Permits a lock mode to be passed in and applies to the object being returned. 
	* Query.setLockMode(): Sets the lock mode to be in effect during execution of the query. 
Each of the EntityManager methods must be invoked within a transaction. 
To optimistically read-lock an entity, a lock mode of LockModeType.OPTIMISTIC can be passed to one of the locking methods. 
Due to this, the common case for using OPTIMISTIC_FORCE_INCREMENT is to guarantee consistency across entity relationship changes (often they are one-to-many relationships with target foreign keys) when in the object model the entity relationship pointers change, but in the data model no columns in the entity table change. 
Pessimistic locking implies obtaining a lock on one or more objects immediately, instead of optimistically waiting until the commit phase and hoping that the data has not changed in the database since it was last read in.
Песимистические блокировки понижают маштабируемость пртложения и создают узкие места
The reality is that very few applications actually need pessimistic locking, and those that do only need it for a limited subset of queries.
The rule is that if you think you need pessimistic locking, think again.
####There are three supported pessimistic locking modes
	* PESSIMISTIC_WRITE This mode will be translated by most providers into a SQL "SELECT FOR UPDATE" statement in the database, obtaining a write lock on the entity so no other applications can modify it.
	* PESSIMISTIC_READ pessimistically achieve repeatable read semantics when no writes to the entity are expected. When an entity locked with a pessimistic read lock does end up getting modified, the lock will be upgraded to a pessimistic write lock. (во время flush) 
	* PESSIMISTIC_FORCE_INCREMENT this mode will also increment the version field of the locked entity regardless of whether changes were made to it.
	
An extra property **javax.persistence.lock.scope** exists to enable target entities lock in case someone needs to acquire the locks as part of a pessimistic query. The property can be set on the query as a property, with its value set to PessimisticLockScope.EXTENDED. 
**javax.persistence.lock.timeout** hint is likely supported by the major JPA providers; however, make sure that your provider supports it before coding to this hint. 
LockTimeoutException will be thrown, and the caller can catch it and simply retry the call if he desires to do so. 
if the failure is severe enough to cause a transaction failure, a PessimisticLockException will be thrown and the transaction will be marked for rollback. 

##Caching


##Utility Classes 