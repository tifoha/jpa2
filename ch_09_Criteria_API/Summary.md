#Criteria API 
The CriteriaBuilder interface provides three methods for creating a new select query definition, depending on the desired result type of the query.
	* The first and most common method is the createQuery(Class<T>) method, passing in the class corresponding to the result of the query. 
	* The second method is createQuery(), without any parameters, and corresponds to a query with a result type of Object. 
	* The third method, createTupleQuery(), is used for projection or report queries where the SELECT clause of the query contains more than one expression and you wish to work with the result in a more strongly typed manner.
	
a CriteriaQuery instance is not a Query object that may be invoked to get results from the database, it's a query definition
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
`CriteriaQuery<String> c = cb.createQuery(String.class); 
Root<Employee> emp = c.from(Employee.class); 
c.select(emp.<String>get("name"));`
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

	`Join<Employee,Project> project = emp.join("projects", JoinType.LEFT);`
	`Join<Employee,Employee> directs = emp.join("directs");` 
	`Join<Employee,Project> projects = directs.join("projects");` 
	`Join<Employee,Department> dept = directs.join("dept");`
The JoinType.INNER is default value
Joins across collection relationships that use Map are a special case. Use .joinMap() to create and then use key() end value() methods of join

The Criteria API builds fetch joins through the use of the fetch() method on the FetchParent interface. It is used instead of join() in cases where fetch semantics are required and accepts the same argument types. 
`Root<Employee> emp = c.from(Employee.class); `
`emp.fetch("address"); `
`c.select(emp);`
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
> | greaterThan(), gt() 
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
SUMs | um(), sumAsLong(), sumAsDouble() 
MIN | min(), least() 
MAX | max(), greatest() 
COUNT | count() 
COUNT DISTINCT | countDistinct() 

Passing multiple arguments to where() implicitly combines the expressions using AND operator semantics. 
Для того чтоб все условия склонить в OR или в AND достаточно передать массив условий в соответствующий метод 
Для создания условия по умолчиню можно заюзать методы .conjunction()[true] and .disjunction()[false] 
Predicate criteria = cb.conjunction(); 
`if (name != null) {
	ParameterExpression<String> p = cb.parameter(String.class, "name");
	criteria = cb.and(criteria, cb.equal(emp.get("name"), p)); 
}
......`
Once obtained, these primitive predicates can then be combined with other predicates to build up nested conditional expressions in a tree-like fashion. 
 There is greaterThan() and gt(), two-letter forms are specific to numeric values and are strongly typed to work with number types. 
 
 If you encounter this situation then, to use these expressions with Java literals, the literals must be wrapped using the literal() method. NULL literals are created from the nullLiteral() method, which accepts a class parameter and produces a typed version of NULL to match the passed-in class. 
 
 To work with parameters we must explicitly create a ParameterExpression of the correct type that can be used in conditional expressions. This is achieved through the parameter() method of the CriteriaBuilder interface. 