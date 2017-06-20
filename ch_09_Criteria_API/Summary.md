#Criteria API 
####The CriteriaBuilder interface provides three methods for creating a new select query definition, depending on the desired result type of the query.
	* The first and most common method is the createQuery(Class<T>) method, passing in the class corresponding to the result of the query. 
	* The second method is createQuery(), without any parameters, and corresponds to a query with a result type of Object. 
	* The third method, createTupleQuery(), is used for projection or report queries where the SELECT clause of the query contains more than one expression and you wish to work with the result in a more strongly typed manner.
	

A CriteriaQuery instance is not a Query object that may be invoked to get results from the database, it's a query definition

The first issue we need to consider is one of mutability. The majority of objects created through the Criteria API are in fact immutable.

The CriteriaQuery and Subquery objects are intended to be modified many times by invoking methods such as select(), from(), and where(). 

####Criteria API Select Query Clause Methods 
JP QL Clause | Criteria API Interface | Method 
-------------|------------------------|-------
SELECT | CriteriaQuery, Subquery| select() 
FROM | AbstractQuery | from() 
WHERE | AbstractQuery | where() 
ORDER BY | CriteriaQuery | orderBy() 
GROUP BY | AbstractQuery | groupBy() 
HAVING | AbstractQuery | having() 


The from() method returns an instance of Root corresponding to the entity type. The Root interface is itself extended from the From interface, which exposes functionality for joins.

Можно создать несколько рутов(это фактически алиас как в запросе)

для получения доступа к филдам можно использовать метод get() `emp.get("address").get("city")`

the from() method of AbstractQuery should never be invoked more than once for each desired root. Invoking it multiple times will result in additional roots being created and a Cartesian product if not careful.

We could also supply a single-valued expression such as selecting an attribute from an entity or any compatible scalar expression.
```CriteriaQuery<String> c = cb.createQuery(String.class); 
Root<Employee> emp = c.from(Employee.class); 
c.select(emp.<String>get("name"));
```

The type of the expression provided to the select() method must be compatible with the result type used to create the CriteriaQuery object.

Для создания композитного селекта можно воспользоваться этими методами(tuple(), construct(), array()) класса CriteriaBuilder 

The multiselect() method will create the appropriate argument type given the result type of the query. 

Также можно исаользовать multiselect() для получение Кортежа тоже самое для конструктора 

Like JP QL, aliases may also be set on expressions in the SELECT clause, which will then be included in the resulting SQL statement. Это полезно когда используется сортировка или результат идет в Кортеж(там его можно использовать)

`c.multiselect(emp.get("id").alias("id"), emp.get("name").alias("fullName"));`

.alias() returns itself, method is an exception to the rule that only the query definition interfaces, CriteriaQuery and Subquery, contain mutating operations.


.join() can be chained, This means that any query root may join, and that joins may chain with one another. 

The JoinType.RIGHT enumerated value specifies that a right outer join should be applied. Support for this option is not required by the specification so applications that make use of it will not be portable.

When joining across a collection type, the join will have two parameterized types: the type of the source and the type of the target.
```
Join<Employee,Project> project = emp.join("projects", JoinType.LEFT);
Join<Employee,Employee> directs = emp.join("directs"); 
Join<Employee,Project> projects = directs.join("projects"); 
Join<Employee,Department> dept = directs.join("dept");
```
The JoinType.INNER is default value

Joins across collection relationships that use Map are a special case. Use .joinMap() to create and then use key() end value() methods of join

The Criteria API builds fetch joins through the use of the fetch() method on the FetchParent interface. It is used instead of join() in cases where fetch semantics are required and accepts the same argument types. 
```
Root<Employee> emp = c.from(Employee.class);
emp.fetch("address");
c.select(emp);
```

Note that when using the fetch() method the return type is Fetch, not Join. Fetch objects are not paths and may not be extended or referenced anywhere else in the query. 

Если просто заюзать join тогда lazy collections не будут загружены

Для того чтоб записи не дублировались можно использовать метод .distinct(true)


The where() method accepts either zero or more Predicate objects, or a single Expression<Boolean> argument.

Each call to where() will render any previously set WHERE expressions to be discarded and replaced with the newly passed-in ones. 

####JP QL to CriteriaBuilder Predicate Mapping 
JP QL Operator | CriteriaBuilder Method 
---------------|-----------------------
AND | and() 
OR | or() 
NOT | not() 
= | equal() 
<> | notEqual() 
\> | greaterThan(), gt() 
>= | greaterThanOrEqualTo(), ge() 
< | lessThan(), lt() 
<= | lessThanOrEqualTo(), le() 
BETWEEN | between() 
IS NULL | isNull() 
IS NOT NULL | isNotNull() 
EXISTS | exists() 
NOT EXISTS | not(exists()) 
IS EMPTY | isEmpty() 
IS NOT EMPTY | isNotEmpty() 
MEMBER OF | isMember() 
NOT MEMBER OF | isNotMember() 
LIKE | like() 
NOT LIKE | notLike() 
IN | in() 
NOT IN | not(in()) 

####JP QL to CriteriaBuilder Scalar Expression Mapping 
JP QL | ExpressionCriteriaBuilder Method 
------|---------------------------------
ALL | all() 
ANY | any() 
SOME | some() 
- | neg(), diff() 
+ | sum() 
* | prod() 
/ | quot() 
COALESCE | coalesce() 
NULLIF | nullif() 
CASE | selectCase() 

####JP QL to CriteriaBuilder Function Mapping 
JP QL | FunctionCriteriaBuilder Method 
------|-------------------------------
ABS | abs() 
CONCAT | concat() 
CURRENT_DATE | currentDate() 
CURRENT_TIME | currentTime() 
CURRENT_TIMESTAMP | currentTimestamp() 
LENGTH | length() 
LOCATE | locate() 
LOWER | lower() 
MOD | mod() 
SIZE | size() 
SQRT | sqrt() 
SUBSTRING | substring() 
UPPER | upper() 
TRIM | trim()

####JP QL to CriteriaBuilder Aggregate Function Mapping 
JP QL | Aggregate Function CriteriaBuilder Method 
------|-------------------------------
AVG | avg() 
SUMs | sum(), sumAsLong(), sumAsDouble() 
MIN | min(), least() 
MAX | max(), greatest() 
COUNT | count() 
COUNT DISTINCT | countDistinct() 


Passing multiple arguments to where() implicitly combines the expressions using AND operator semantics. 

Для того чтоб все условия склонить в OR или в AND достаточно передать массив условий в соответствующий метод 

Для создания условия по умолчиню можно заюзать методы .conjunction()[true] and .disjunction()[false] 

Predicate criteria = cb.conjunction(); 
```
if (name != null) {
	ParameterExpression<String> p = cb.parameter(String.class, "name");
	criteria = cb.and(criteria, cb.equal(emp.get("name"), p)); 
}
......
```
Once obtained, these primitive predicates can then be combined with other predicates to build up nested conditional expressions in a tree-like fashion. 

There is greaterThan() and gt(), two-letter forms are specific to numeric values and are strongly typed to work with number types. 

If you encounter this situation then, to use these expressions with Java literals, the literals must be wrapped using the literal() method. NULL literals are created from the nullLiteral() method, which accepts a class parameter and produces a typed version of NULL to match the passed-in class. 

To work with parameters we must explicitly create a ParameterExpression of the correct type that can be used in conditional expressions. This is achieved through the parameter() method of the CriteriaBuilder interface. 

The AbstractQuery interface provides the subquery() method for creation of subqueries.

Subquery instance is a complete query definition like CriteriaQuery that may be used to create both simple and complex queries. 
```
ParameterExpression<String> nameParam = cb.parameter(String.class, "name");
CriteriaQuery<String> q = cb.createQuery(String.class);
Subquery<Employee> sq = q.subquery(Employee.class);
final Root<Project> project = sq.from(Project.class);
Join<Project, Employee> sqEmp = project.join("employees");
sq
	.select(sqEmp)
	.where(cb.equal(project.get("name"), nameParam));
Root<Employee> emp = q.from(Employee.class);
q
	.select(emp.get("name"))
	.where(cb.in(emp).value(sq));
```
Еще один вариант:
```
SELECT p 
FROM Project p 
	JOIN p.employees e 
WHERE TYPE(p) = DesignProject 
	AND e.directs IS NOT EMPTY 
	AND(SELECT AVG(d.salary)FROM e.directs d) >= :value
```
в
```
CriteriaQuery<Project> c = cb.createQuery(Project.class); 
Root<Project> project = c.from(Project.class); 
Join<Project,Employee> emp = project.join("employees"); 
Subquery<Number> sq = c.subquery(Number.class); 
Join<Project,Employee> sqEmp = sq.correlate(emp); 
Join<Employee,Employee> directs = sqEmp.join("directs"); 
c.select(project)
	.where(
			cb.equal(project.type(), DesignProject.class),
			cb.isNotEmpty(emp.<Collection>get("directs")),
			cb.ge(sq.select(cb.avg(directs.get("salary"))),
			cb.parameter(Number.class, "value"))
		  ); 
```

The in() method of the CriteriaBuilder interface only accepts a single argument, the single-valued expression that will be tested against the values of the IN expression.

`SELECT e FROM Employee e WHERE e.address.state IN ('NY', 'CA')`

`.where(cb.in(emp.get("address").get("state")).value("NY").value("CA"));`

or

`.where(emp.get("address").get("state").in("NY","CA"));`

для того чтоб использовать in с подзапросом нужно юзать `cb.in(field).value(subquery)`


Пример использования CASE

`CASE TYPE(p)WHEN DesignProject THEN 'Development'WHEN QualityProject THEN 'QA'ELSE 'Non-Development'END`
```
cb.selectCase(project.type())
	.when(DesignProject.class, "Development")
	.when(QualityProject.class, "QA")
	.otherwise("Non-Development"))
```
 или  COALESCE

`COALESCE(d.name, d.id)` 

OR

`cb.coalesce(dept.get("name"),dept.get("id"))` 

OR 

`cb.coalesce().value(dept.get("name")).value(dept.get("id"))`
 
Function expressions are created with the function() method of the CriteriaBuilder interface.

`c.select(cb.function("initcap", String.class, dept.get("name")));`

TREAT(Downcasting)

`SELECT e FROM Employee e JOIN TREAT(e.projects AS QualityProject) qp`

`Join<Employee,QualityProject> project = cb.treat(emp.join(emp.get("projects"), QualityProject.class);`


Outer joins require additional support to establish filtering criteria that still preserves the optionality of the joined entities.

`SELECT e FROM Employee e JOIN e.projects p ON p.name = 'Zooby'`

`Join<Employee,Project> project = emp.join("projects", JoinType.LEFT).on(cb.equal(project.get("name"), "Zooby"));`

Support for the ON condition of outer join queries was added in Jpa 2.1. 


###ORDER BY EXAMPLE
`q.orderBy(cb.desc(dept.get("name")),cb.asc(emp.get("name")));`

###The GROUP BY and HAVING Clauses 
`SELECT e, COUNT(p) FROM Employee e JOIN e.projects p GROUP BY e HAVING COUNT(p) >= 2`

```
CriteriaQuery<Object[]> c = cb.createQuery(Object[].class); 
Root<Employee> emp = c.from(Employee.class); 
Join<Employee,Project> project = emp.join("projects"); 
c.multiselect(emp, cb.count(project))
	.groupBy(emp)
	.having(cb.ge(cb.count(project), 2));`
```
###Bulk Update and Delete
####UPDATE
`q.set(emp.get("salary"), cb.sum(emp.get("salary"), 5000))`

####DELETE
`q.where(cb.isNull(emp.get("dept"));`

##Strongly Typed Query Definitions
The metamodel of a persistence unit is a description of the persistent type, state, and relationships of entities, embeddables, and managed classes. 

The metamodel API is exposed through the getMetamodel() method of the EntityManager interface.

Метамодель создается во время создания EntityManagerFactory

при вызове метода mm.entity(Employee.class); не создается новый экземпляр а берется уже существующий

Пример жесткой типизации в критериях
```
Root<Employee> emp = q.from(Employee.class);
EntityType<Employee> empModel = emp.getModel();
CollectionJoin<Employee, Phone> phone = emp.join(empModel.getCollection("phones", Phone.class), LEFT);
```
getModel() exists on many of the Criteria API interfaces as a shortcut to the underlying metamodel object.
 
The potential for error in using the metamodel objects is actually the heart of what makes it strongly typed.  
 

To simplify usage of metamodel in CriteriaApi, JPA also provides a canonical metamodel for a persistence unit. 

Canonical Metamodel Class должен быть создан либо гененится специальными тулзами и иннотироваться  аннотацией @StaticMetamodel

поля метамодели могут быть таких типов SingularAttribute ListAttribute, SetAttribute, MapAttribute, or CollectionAttribute(зависит от типа коллекции)

также поля должны быть public static volatile

Пример использования метамодели

```
@StaticMetamodel(Employee.class) 
public class Employee_ {
	public static volatile SingularAttribute<Employee, Integer> id;
	public static volatile SingularAttribute<Employee, String> name;
	public static volatile SingularAttribute<Employee, String> salary;
	public static volatile SingularAttribute<Employee, Department> dept;
	public static volatile SingularAttribute<Employee, Address> address;
	public static volatile CollectionAttribute<Employee, Project> project;
	public static volatile MapAttribute<Employee, String, Phone> phones; 
}
```

```
CriteriaQuery<Object> c = cb.createQuery(); 
Root<Employee> emp = c.from(Employee.class); 
MapJoin<Employee,String,Phone> phone = emp.join(Employee_.phones); 
c.multiselect(emp.get(Employee_.name), phone.key(), phone.value());
```

Native SQL queries are an easy choice to make: either you need to access a vendor-specific feature or you don’t.

The programming API flows smoothly from the application. It is also ideal for cases where the query definition can’t be fully constructed with the input of a user.

JP QL, on the other hand, is more concise and familiar to anyone experienced with SQL. It can also be embedded with the application annotations or XML descriptors
