#Using Queries
##Joins Between Entitie
The result type of a select query cannot be a collection; it must be a single valued object such as an entity instance or persistent field type. Expressions such as e.phones are illegal in the SELECT clause because they would result in Collection instances
SELECT 
	p.number 
FROM 
	Employee e, Phone p 
WHERE 
	e = p.employee 
	AND e.department.name = 'NA42' 
	AND p.type = 'Cell'

SELECT 
	p.number 
FROM 
	Employee e 
	JOIN e.phones p 
WHERE 
	e.department.name = 'NA42' 
	AND p.type = 'Cell'

AGREGATING

select d, count(e), max(e.salary), avg(e.salary) from Department d left join d.employees e group by d having count (e) >=5

Query Parameters:
	1. Positional binding, where parameters are indicated in the query string by a question mark followed by the parameter number (?1)
	2. Named parameters may also be used and are indicated in the query string by a colon followed by the parameter name. (:name)
	
The **Query** interface is used in cases when the result type is Object or in dynamic queries when the result type may not be known ahead of time. 
The **TypedQuery** interface is the preferred one and can be used whenever the result type is known.
Named queries, on the other hand, are static and unchangeable, but are more efficient to execute because the persistence provider can translate the JP QL string to SQL once when the application starts as opposed to every time the query is executed. 
Dynamically defining a query and then naming it allows a dynamic query to be reused multiple times throughout the life of the application but incur the dynamic processing cost only once. 
An issue to consider with string dynamic queries, however, is the cost of translating the JP QL string to SQL for execution.
Провайдеры кешируют запросы, если использовать параметры а не конкатенацию(конкатенация опасна SQL injection)
The name of the query is scoped to the entire persistence unit and must be unique within that scope. 
A hybrid approach is to dynamically create a query and then save it as a named query in the entity manager factory.
As mentioned earlier, named queries are scoped to the entire persistence unit so it makes sense that they be added at the level of the entity manager factory.
При добавлении нового именного запроса можноп ререзаписать старый

The cases where it might be advantageous to make a named query out of a dynamic query are the following: 
	* The application gets access to some criteria at runtime that contributes to a query that is predetermined to be commonly executed. 
	* A named query is already defined but because of some aspect of the runtime environment you want to override the named query with a different one without using an additional  XML descriptor. 
	* There is a preference to define all of the queries in code at startup time.

PARAMETER TYPES
 
"SELECT FROM Employee e WHERE e.department = :dept AND e.salary > :sal" - JPQL автоматически конвертит ентити в ID
Автоматом конвертятся примитивы
Date & Calendar должны быть дополнительно дополнены энумом TemporalType
If multiple results are available after executing the query instead of the single expected result, getSingleResult() will throw a NonUniqueResultException exception.(not rollback transaction)
For transaction-scoped entity managers, this limits the lifetime of the Query or TypedQuery object to the life of the transaction.

types that may result from JP QL queries: 
	* Basic types, such as String, the primitive types, and JDBC types 
	* Entity types
	* An array of Object 
	* User-defined types created from a constructor expression
	
Whenever an entity instance is returned, it becomes managed by the active persistence context.
Выполнение запросов вне транзакции не вызывает создание снепшотов ентитей для добавления их в PC. Для application-managet EM нужно пометить метод как NOT_SUPPORTED (Tx)
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)public List<Department> findAllDepartmentsDetached() {return em.createQuery("SELECT d FROM Department d",Department.class).getResultList();}
	This optimization is completely provider-specific. some providers may instead opt to create a temporary  persistence context for the query and just throw it away after extracting the results from it, making this suggested  optimization rather extraneous. Check your provider before making a coding decision. 
select new ua.tifoha.ResultLine(e.name, e.department.name) позволяет создавать параметризированый запрос
the setFirstResult() and setMaxResults() methods should not be used with queries that join across collection relationships (one-to-many and many-to-many) because these queries may return duplicate values. the  duplicate values in the result set make it impossible to use a logical result position. 
Если сделать запрос на одной сущьности то новые сущьности из запроса будут взяты из контекста, если они в нем есть, если выбирать отдельные поля тогда берется из базы
Если сделать запрос на неопределенное количество сущьностей тогда провайдер может зафлашить контекст перед выполнением запроса. для того чтоб он это не делал нужно установить setFlushMode(FlushModeType.COMMIT)
Generally speaking, if you are going to execute queries in transactions where data is being changed, AUTO is the right answer. 
If you are concerned about the performance implications of ensuring query integrity, consider changing the flush mode to COMMIT on a per-query basis. 
Unfortunately, setting a query timeout is not portable behavior. It may not be supported by all database platforms nor is it a requirement to be supported by all persistence providers. 
The second is that the property is enabled and any select, update, or delete operation that runs longer than the specified timeout value is aborted, and a QueryTimeoutException is thrown. (Not cause transaction rollback)
Bulk DELETE & UPDATE Again, the syntax is the same as the SQL version, except that the target in the FROM clause is an entity instead of a table, and the WHERE clause is composed of entity expressions instead of column expressions.
Bulk operations are issued as SQL against the database, bypassing the in-memory structures of the persistence context. 
Running the bulk operation in its own transaction is the preferred approach because it minimizes the chance of accidentally fetching data before the bulk change occurs. 
A typical strategy for persistence providers dealing with bulk operations is to invalidate any in-memory cache of data related to the target entity. 
Native sQL update and delete operations should not be executed on tables mapped by an entity. the Jp QL operations tell the provider what cached entity state must be invalidated in order to remain consistent with the database. native sQL operations bypass such checks and can quickly lead to situations where the in-memory cache is out of date with respect to the database. 
DELETE statements are applied to a set of entities in the database, unlike remove(), which applies to a single entity in the persistence context. 
DELETE/UPDATE statements not invoke cascades

Query hints are the JPA extension point for query features. (Хинты зависят от конкретного провайдера. Если провайдер незнает хинт он его просто игнорит)
Persistence providers will often take steps to precompile JP QL named queries to SQL as part of the deployment or initialization phase of an application. 
query parameters also help to avoid security issues caused by concatenating values into query strings. For applications exposed to the Web, security has to be a concern at every level of an application.
If you are executing queries that return entities for reporting purposes and have no intention of modifying the results, consider executing queries using a transaction-scoped entity manager but outside of a transaction.(это позволит упростить добавление сущьностей в контекст или вообще не добавлять)
The ideal location for query hints is in an XML mapping file or at the very least as part of a named query definition. 
Keep hints decoupled from your code if at all possible. 
Entity versioning and locking requires special consideration when bulk update operations are used.
Take time to become familiar with the SQL that your persistence provider generates for different JP QL queries. 
Finally, understanding the provider strategy for when and how often it flushes the persistence context is necessary before looking at optimizations such as changing the flush mode. 