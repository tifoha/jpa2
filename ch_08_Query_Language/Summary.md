#Query Language
##Terminology
####Queries fall into one of four categories: 
	* select
	* aggregate
	* update
	* delete

Queries operate on the set of entities and embeddables defined by a persistence unit. 

Simple persistent properties with no relationship mapping are called state fields.

Persistent properties that are also relationships are called association fields. 

##Select Queries

`SELECT <select_expression> FROM <from_clause> [WHERE <conditional_expression>] [ORDER BY <order_by_clause>]`

The first difference is that the domain of the query defined in the FROM clause is not a table but an entity; 

This aliased value is known as an **identification variable** and is the key by which the entity will be referred to in the rest of the select statement.

Unlike queries in SQL, where a table alias is optional, the use of identification variables is mandatory in JP QL. 

Even if the entity is cached in memory, the query engine will still typically read all required data to ensure that the cached version is up to date. 

e.name is a state field path expression resolving to the employee name

e.department is a single-valued association from the employee to the department 

e.directs is a collection-valued association that resolves to the collection of employees

A path cannot continue from a state field or collection-valued association.

`SELECT DISTINCT e.department FROM Employee e` Remove the duplicates

Changing any of the Embedded object results returned from a query that selected the Embedded directly, however, would have no persistent effect. 

`SELECT e.name, e.salary FROM Employee e` When this is executed, a collection Object[] will be returned.

`SELECT NEW example.EmployeeDetails(e.name, e.salary, e.department.name) FROM Employee e` The result type of this query is the example.EmployeeDetails Java class.(query engine will search for a constructor with those class types for arguments.)

####Joins occur whenever any of the following conditions are met in a select query.
	1. Two or more range variable declarations are listed in the FROM clause and appear in the select clause.
	2. The JOIN operator is used to extend an identification variable using a path expression.
	3. A path expression anywhere in the query navigates across an association field, to the same or a different entity.
	4. One or more WHERE conditions compare attributes of different identification variables. 


Path navigation from one entity to another is a form of inner join. `[INNER] JOIN <path_expression> [AS] <identifier>.`

`SELECT d, m FROM Department d, Employee m WHERE d = m.department AND m.directs IS NOT EMPTY` **IS NOT EMPTY**, to check that the collection of direct reports to the employee is not empty.

Для работы с мапами нужно искользовать ключевые слова **KEY** & **VALUE**

`SELECT e.name, KEY(p), VALUE(p) FROM Employee e JOIN e.phones p WHERE KEY(p) IN ('Work', 'Cell')`

we want both the key and the value returned together in the form of a  **java.util.Map.Entry** object, we can specify the **ENTRY** keyword in the same fashion. 

An outer join is specified using the following syntax: LEFT [OUTER] JOIN <path_expression> [AS] <identifier>. The following query demonstrates an outer join between two entities:

Для LEFT OUTER JOIN можно добавлять дополнительные условия с использованием ON (доступно только в OUTER JOIN)

The ability to add outer join conditions with On was added in Jpa 2.1 

Fetch joins are intended to help application designers optimize their database access and prepare query results for detachment. 

`SELECT e FROM Employee e JOIN FETCH e.address` подтянет из БД адресс

A fetch join is distinguished from a regular join by adding the FETCH keyword to the JOIN operator. 

`SELECT e, a FROM Employee e JOIN e.address a` - Это тоже FETCH JOIN только не явный, провайдер заинжектит отдел в сотрудника

??? Под вопросом Once again, as the results are processed, the Employee entity is constructed in memory but dropped from the result collection. Each Department entity now has a fully resolved employees collection, but the client receives one reference to each department per employee. 

Some persistence providers also offer batch reads as an alternative to fetch joins that issue multiple queries in a single batch and then correlate the results to eagerly load relationships. 

###Basic Expression Form
 Operator precedence is as follows.
	1. Navigation operator (.)
	2. Unary +/–
	3. Multiplication (*) and division (/)
	4. Addition (+) and subtraction (–)
	5. Comparison operators =, >, >=, <, <=, <>, [NOT] BETWEEN, [NOT] LIKE, [NOT] IN,  IS [NOT] NULL, IS [NOT] EMPTY, [NOT] MEMBER [OF]
	6. Logical operators (AND, OR, NOT) 
	
The **BETWEEN** operator can be used in conditional expressions to determine whether the result of an expression falls within an inclusive range of values. Numeric, string, and date expressions can be evaluated in this way.

`SELECT e FROM Employee e WHERE e.salary BETWEEN 40000 AND 45000`

JP QL supports the SQL **LIKE** condition to provide for a limited form of string pattern matching.

`SELECT d FROM Department d WHERE d.name LIKE '__QA\_%' ESCAPE '\'` (_ is the any single character)


Subqueries can be used in the WHERE and HAVING clauses of a query.

`SELECT e FROM Employee e WHERE e.salary = (SELECT MAX(emp.salary)FROM Employee emp)`

A subquery can be used in most conditional expressions and can appear on either the left or right side of an expression. 

The scope of an identifier variable name begins in the query where it is defined and extends down into any subqueries. 

If a subquery declares an identifier variable of the same name,  it overrides the parent declaration and prevents the subquery from referring to the parent variable. (Overriding an identification variable name in a subquery is not guaranteed to be supported by all providers. unique names should be used to ensure portability. ) 

The **EXISTS** expression in this example returns true if any results are returned by the subquery. 

`SELECT e FROM Employee e WHERE EXISTS (SELECT 1 FROM Phone p WHERE p.employee = e AND p.type = 'Cell')` This query returns all the employees who have a cell phone number. 

The **IN** expression can be used to check whether a single-valued path expression is a member of a collection.  

`SELECT e FROM Employee e WHERE e.department IN (SELECT DISTINCT d FROM Department d JOIN d.employees de JOIN de.projects p WHERE p.name LIKE 'QA%')`

The **IN** expression can also be negated using the **NOT** operator. 

`SELECT p FROM Phone p WHERE p.type NOT IN ('Office', 'Home')`


###Collection Expressions 
The **IS EMPTY** operator is the logical equivalent of **IS NULL**, but for collections.

`SELECT e FROM Employee e WHERE e.directs IS NOT EMPTY`

Note that **IS EMPTY** expressions are translated to SQL as subquery expressions.

The **MEMBER OF** operator and its negated form **NOT MEMBER OF** are a shorthand way of checking whether an entity is a member of a collection association path.

`SELECT e FROM Employee e WHERE :project MEMBER OF e.projects` the following query selects all employees who are assigned to a specified project:

The **ANY**, **ALL**, and **SOME** operators can be used to compare an expression to the results of a subquery.

`SELECT e FROM Employee e WHERE e.directs IS NOT EMPTY AND e.salary < ALL (SELECT d.salary FROM e.directs d)` eturns the managers who are paid less than all the employees who work for them.

The **SOME** operator is an alias for the **ANY** operator. 

###Inheritance and Polymorphism 
JPA supports inheritance between entities. As a result, the query language supports polymorphic results where multiple subclasses of an entity can be returned by the same query. 

`SELECT p FROM Project p WHERE p.employees IS NOT EMPTY` the query results will include a mixture of Project, QualityProject,  and DesignProject objects and the results can be cast to the subclasses by the caller as necessary. 

the keyword **TYPE** followed by an expression in parentheses that resolves to an entity.

`SELECT p FROM Project p WHERE TYPE(p) = DesignProject OR TYPE(p) = QualityProject` return only design and quality projects


**TREAT** can be used in the WHERE clause to filter the results based on subtype state of the instances.

`SELECT p FROM Project p WHERE TREAT(p AS QualityProject).qaRating > 4OR  TYPE(p) = DesignProject` returns all of the design projects plus all of the quality projects that have a quality rating greater than 4

The TREAT expression can be used in a similar way for other kinds of joins, too, such as outer joins and fetch joins. 

###Scalar Expressions

There are a number of different literal types that can be used in JP QL, including strings, numerics, booleans, enums, entity types, and temporal types. 

Queries can reference Java enum types by specifying the fully qualified name of the enum class.

`...WHERE KEY(p) = com.acme.PhoneType.Home`

####Time formats:
	* 'yyyy-mm-dd' 
	* 'hh-mm-ss' 
	* 'yyyy-mm-dd hh-mm-ss.f'
	
Table 8-1. Supported Function Expressions 

Function | Description
---------|------------
ABS(number) | The ABS function returns the unsigned version of the number argument. The result type is the same as the argument type (integer, float, or double). 
CONCAT(string1, string2) | The CONCAT function returns a new string that is the concatenation of its arguments, string1 and string2. 
CURRENT_DATE |  The CURRENT_DATE function returns the current date as defined by the database server. 
CURRENT_TIME |  The CURRENT_TIME function returns the current time as defined by the database server. 
CURRENT_TIMESTAMP |  The CURRENT_TIMESTAMP function returns the current timestamp as defined by the database server. 
INDEX(identification variable) |  The INDEX function returns the position of an entity within an ordered list. 
LENGTH(string) |  The LENGTH function returns the number of characters in the string argument. 
LOCATE(string1, string2 [, start]) |  The LOCATE function returns the position of string1 in string2, optionally starting at the position indicated by start. The result is zero if the string cannot be found. 
LOWER(string) |  The LOWER function returns the lowercase form of the string argument. 
MOD(number1, number2) |  The MOD function returns the modulus of numeric arguments number1 and number2 as an integer.
SIZE(collection) | The SIZE function returns the number of elements in the collection, or zero if the collection is empty. 
SQRT(number) | The SQRT function returns the square root of the number argument as a double. 
SUBSTRING | (string, start, end) The SUBSTRING function returns a portion of the input string, starting at the index indicated by start up to length characters. String indexes are measured starting from one. 
UPPER(string) |  The UPPER function returns the uppercase form of the string argument. 
TRIM([[LEADING&#124;TRAILING&#124;BOTH] [char] FROM] string) |  The TRIM function removes leading and/or trailing characters from a string. If the optional LEADING, TRAILING, or BOTH keyword is not used, both leading and trailing characters are removed. The default trim character is the space character. 

Database functions may be accessed in JP QL queries through the use of the **FUNCTION** expression.

`SELECT DISTINCT e FROM Employee e JOIN e.projects p WHERE FUNCTION('shouldGetBonus', e.department.id, p.id)`

`CASE {WHEN <cond_expr> THEN <scalar_expr>}+ ELSE <scalar_expr> END`
`CASE <value> {WHEN <scalar_expr1> THEN <scalar_expr2>}+ ELSE <scalar_expr> END` - Вариант когда много случаев

`COALESCE(<scalar_expr> {,<scalar_expr>}+)`

The scalar expressions in the **COALESCE** expression are resolved in order. The first one to return a non-null value becomes the result of the expression. 

`SELECT COALESCE(d.name, d.id) FROM Department d`

`NULLIF(<scalar_expr1>, <scalar_expr2>)`

It accepts two scalar expressions and resolves both of them. If the results of the two expressions are equal, the result of the expression is null. Otherwise  it returns the result of the first scalar expression. 

###ORDER BY Clause 

Можно сортировать по выражению если оно имеет алиас

`SELECT e.name, e.salary * 0.05 AS bonus, d.name AS deptName FROM Employee e JOIN e.department d ORDER BY deptName, bonus DESC`

If the SELECT clause of the query uses state field path expressions, the ORDER BY clause is limited to the same path expressions used in the SELECT clause.

`UPDATE <entity name> [[AS] <identification variable>] SET <update_statement> {, <update_statement>}* [WHERE <conditional_expression>]`

`DELETE FROM <entity name> [[AS] <identification variable>] [WHERE <condition>]`