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
По идее если подграф не определен то будут загружены все по умолчанию поля этого объекта и его жадных аттрибутов, для того чтоб такого не произошло нужно определить минимальный подграф

`@NamedEntityGraph(name="Employee.graph3",attributeNodes={`
`	@NamedAttributeNode("name"),`
`	@NamedAttributeNode(value="projects", subgraph="project")},`
`	subgraphs={`
`		@NamedSubgraph(name="project", type=Project.class,attributeNodes={@NamedAttributeNode("name")}),`
`		@NamedSubgraph(name="project", type=QualityProject.class,attributeNodes={@NamedAttributeNode("qaRating")})}) `
В данном примере используется два подграфа с одним названием в котором указывается тип объекта(в случае наследования) и дополниельные аттрибуты относящиеся к этому объекту (у DesignProject нет доп полей поэтому его граф не объявлен)
Если рутовая сущьность является суперклассом тогда можно использовать аттрибут `subclassSubgraphs` для указания подграфов ее потомков

`@NamedEntityGraph(name="Employee.graph4",attributeNodes={
@NamedAttributeNode("name"),
@NamedAttributeNode("address"),
@NamedAttributeNode(value="department", subgraph="dept")},subgraphs={@NamedSubgraph(name="dept",
 					attributeNodes={@NamedAttributeNode("name")})},
 					subclassSubgraphs={@NamedSubgraph(name="notUsed", type=ContractEmployee.class,attributeNodes={@NamedAttributeNode("hourlyRate")})})`

When a relationship attribute is of type Map, there is the issue of the additional key part of the Map(when key is an embeddable or an entity type). To handle these cases there is a keySubgraph element in NamedAttributeNode.
###Entity Graph API 
EntityGraph<Address> graph = em.createEntityGraph(Address.class);
There is unfortunately no method equivalent to the includeAllAttributes element in the @NamedEntityGraph annotation.
_**Dynamic Entity Graph with Subgraphs**_ 
`EntityGraph<Employee> graph = em.createEntityGraph(Employee.class); `
`graph.addAttributeNodes("name","salary", "address"); `
`Subgraph<Phone> phone = graph.addSubgraph("phones"); `
`phone.addAttributeNodes("number", "type"); `
`Subgraph<Department> dept = graph.addSubgraph("department"); `
`dept.addAttributeNodes("name");`
API doesn’t allow sharing of a subgraph within the same entity graph. Since there is no API to pass in an existing Subgraph instance, we need to construct two identical named employee subgraphs. 
Для добавления графа с наследованием можно воспользоваться такой конструкцией
`Subgraph<Project> project = graph.addSubgraph("projects", Project.class); `
`project.addAttributeNodes("name"); `
`Subgraph<QualityProject> qaProject = graph.addSubgraph("projects", QualityProject.class); `
`qaProject.addAttributeNodes("qaRating");`
Для иерархии главного графа используетс этот подход
`graph.addSubclassSubgraph(ContractEmployee.class).addAttributeNodes("hourlyRate");`
Map graph
`graph.addKeySubgraph("employees").addAttributeNodes("firstName", "lastName");`

When you have defined a named entity graph, though, you must access it through the entity manager before it can be used.
If there are many entity graphs defined on a single class and we have a reason to sequence through them, we can do so using the class-based accessor method. 
`List<EntityGraph<? super Employee>> egList =em.getEntityGraphs(Employee.class); `
You can add new named entity graph
`em.getEntityManagerFactory().addNamedEntityGraph("Employee.graphX", graph);`

If a named entity graph with the same name already existed in the named entity graph namespace, it will be overridden by the one we are supplying in the addNamedEntityGraph() call, since there can only be one entity graph of a given name in a persistence unit. 
Мы можем создать новый граф на основании существующего
`EntityGraph<?> graph = em.createEntityGraph("Employee.graph2");`
Документация не уточняет можно ли удалять атрибуты из графа непонятно что возвращает метод getAttributeNodes() реальный лист или копию
The hardest part of entity graphs is creating them to be correct and to produce the result that you are expecting
The hint with graphs can either be passed into a find() method or set as a query hint on any named or dynamic query. 
Depending upon which property is used, the entity graph will take on the role of either a fetch graph or a load graph. 
When an entity graph is passed into a find() method or a query as the value to the “javax.persistence.fetchgraph” property, the entity graph will be treated as a fetch graph. The semantics of a fetch graph are that all attributes included in the graph are to be treated as having a fetch type of EAGER, as if the mapping had been specified as fetch=FetchType.EAGER, regardless of what the static mapping actually specifies. Attributes that are not included in the graph are to be treated as LAZY. As described earlier, all identifier or version attributes will be treated as EAGER and loaded, regardless of whether they are included in the graph or not.
The usefulness of a fetch graph is primarily to enable attributes to be fetched lazily when they were configured or defaulted to be eagerly fetched in the mapping. (но у меня так не получилось)
The main difference between a fetch graph and a load graph is how the missing attributes are treated. fetch граф считает отсутствующие аттрибуты как lazy а load граф берет значения из маппинга. 
Все зависит от того как провайдер работает с ленивой загрузкой 
You can test the loading behavior of your provider by using the PersistenceUnitUtil.isLoaded() method on a lazy attribute before it is accessed.
Remember that the default fetch graph will be used when no subgraph is specified. This is relevant for all bidirectional relationships (remember to set a subgraph for the relationship back to the original object type), but especially those that navigate back to the root entity type. Your intuition may tell you that the root entity graph specification will be used, but it is the default fetch graph that will be used instead. 
