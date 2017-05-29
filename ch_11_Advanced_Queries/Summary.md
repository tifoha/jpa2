#Advanced Queries 
We recommend avoiding SQL initially if possible and then introducing it only when necessary. This will enable your queries to be more portable across databases and more maintainable as your mappings change. 
One of the major benefits of SQL query support is that it uses the same Query interface used for JP QL queries. 
The main reason to consider using SQL queries in JPA, instead of just issuing JDBC queries, is when the result of the query will be converted back into entities.
Now there is no backward compatible way to return a TypedQuery instead of a Query (createNativeQuery(ORG_QUERY, Employee.class) will return Query)
The first and simplest form of dynamically defining a SQL query that returns an entity result is to use the createNativeQuery() method of the EntityManager interface, passing in the query string and the entity type that will be returned.
Если в запросе нехватает полей для сущьность будет брошена ошибка. Если поля лишние все ок.
A further benefit is that createNamedQuery() can return a TypedQuery whereas the createNativeQuery() method returns an untyped Query. 
A query originally specified in JP QL can be overridden with a SQL version, and vice versa. 
Resulting entity instances become managed by the persistence context, just like the results of a JP QL query.
####There are two benefits to getting managed entities back from a SQL query. 
	The first is that a SQL query can replace an existing JP QL query and that application code should still work without changes. 
	The second benefit is that it allows the developer to use SQL queries as a method of constructing new entity instances from tables that may not have any object-relational mapping. 
SQL data-manipulation statements (INSERT, UPDATE, and DELETE) are also supported as a convenience so that JDBC calls do not have to be introduced in an application otherwise limited to JPA. 
Executing SQL statements that make changes to data in tables mapped by entities is generally discouraged.

JPA provides SQL result set mappings to handle different mapping scenarios. 
`@SqlResultSetMapping(name="EmployeeWithAddress",
	entities={
		@EntityResult(entityClass=Employee.class,
			fields=@FieldResult(name="id",column="EMP_ID")),
		@EntityResult(entityClass=Address.class)} ) `
Non-entity result types, called scalar result types, are mapped using the @ColumnResult annotation.
The only special element in the @EntityResult annotation for use with inheritance is the discriminatorColumn element. 
New with JPA 2.1 is the ability to construct non-entity types from native queries via constructor expressions. 
`@SqlResultSetMapping(name="EmployeeDetailMapping",`
`					classes={`
`							@ConstructorResult(targetClass=example.EmployeeDetails.class,`
`							columns={`
`									@ColumnResult(name="name"),`
`									@ColumnResult(name="salary", type=Long.class),`
`									@ColumnResult(name="deptName")`
`									})`
`							})`
The column results are applied to the constructor of the user-specified type in the order in which the column result mappings are defined. 
In cases where there are multiple constructors that may be ambiguous based on position only, the column result type may also be specified in order to ensure the correct match. 
JPA guarantees only the use of positional parameter binding for SQL queries

JPA stored procedure definitions support the main parameter types defined for JDBC stored procedures: IN, OUT, INOUT, and REF_CURSOR. REF_CURSOR parameter types are used to return result sets to the caller. 

###Entity Graph
Entity graphs are used to override at runtime the fetch settings of attribute mappings. 
For example, if an attribute is mapped to be eagerly fetched (set to FetchType.EAGER) it can set to be lazily fetched for a single execution of a query.

####The structure of an entity graph 
	* _Entity graph nodes:_ There is exactly one entity graph node for every entity graph. It is the root of the entity graph and represents the root entity type. It contains all of the attribute nodes for that type, plus all of the subgraph nodes for types associated with the root entity type. 
	* _Attribute nodes:_ Each attribute node represents an attribute in an entity or embeddable type. If it is for a basic attribute, then there will be no subgraph associated with it, but if it is for a relationship attribute, embeddable attribute, or element collection of embeddables, then it may refer to a named subgraph. 
	* _Subgraph nodes:_ A subgraph node is the equivalent of an entity graph node in that it contains attribute nodes and subgraph nodes, but represents a graph of attributes for an entity or embeddable type that is not the root type. 2
	
Entity graphs can be defined statically in annotation form or dynamically through an API. 
The transitive closure part means that the rule is recursively applied, so the default fetch graph of an entity includes not only all of the eager attributes defined on it, but also all of the eager attributes of the entities that are related to it, and so on until the relationship graph has been exhausted.
Entity graphs and the ability to dynamically override the fetch type of an entity or embeddable attribute during a query or find() method was introduced in Jpa 2.1.  
You can define any number of entity graphs for the same entity, each depicting a different attribute fetch plan. 
Shortcut for named entity graph @NamedEntityGraph(includeAllAttributes=true) (name = entity name,  include all attributes)
Annotating the class without listing any attributes is a shorthand for defining a named entity graph that is composed of the default fetch graph for that entity.