#Advanced Object-Relational Mapping
По умолчанию БД не чуствительна к регистру, хотя можно заставить ее быть чуствительной если поместить название идентификаторов в кавычки @Table(name="\"Employee\"") <column name="&quot;Id&quot;"/> или добавить пустой тег delimited-identifiers в XML
Для записи нестандартных данных в БД используются конвертеры интерфейс AttributeConverter<X,Y> и аннотирован аннотацией @Converter 
There is currently only support to convert an entity attribute to a single column in the database. the ability to store it across multiple columns may be standardized in a future release. 
Converters are managed classes, hence when running in java Se each converter class should be included in a class element in the persistence.xml descriptor. 
можно явно задать конвертер для поля использовав аннотацию @Convert
Для конвертации Embedded полей мы должны использовать @Convert(converter=BooleanToIntegerConverter.class, attributeName="bonded") на этом объекте но также нужно указать имя аттрибута
Можно конвертировать все значения в коллекции если на нее повесить конвертер элементов
If an element collection is a Map with values that are of a basic type, then the values of the Map will be converted. To perform conversion on the keys, the attributeName element should be used with a special value of “key” to indicate that the keys of the Map are to be converted instead of the values. 

`@ManyToMany`
`@Convert(converter=UpperCaseConverter.class, attributeName="key.lastName")`
`private Map<EmployeeName, Employee> employees;`
For instance, converters cannot be used on identifier attributes, version attributes, or relationship attributes
Converters also cannot be used on attributes annotated with @Enumerated or @Temporal, but that doesn’t mean you can’t convert enumerated types or temporal  types. 
Для создания автоматического конвертера используется @Converter(autoApply=true), чтоб переопределить автоматический конвертер достаточно переопределить аннотацию чтоб отключить конвертацию: @Convert(disableConversion=true) 
при выполнении JPQL запросов значения будут сконвертированы с таким запросом не будет проблем так как оба значения сконвертятся в интежер а вот с этим запросом будет ексепшн 'SELECT e FROM Employee e WHERE NOT e.bonded' потому что NOT нельзя
Процессор запросов может не сработать на литералах которые используются в встроеных функциях UPPER() or MOD() или даже LIKE. Лучше на юзать конвертируемые поля в запросах, либо обращить на них особое внимание

Embedded objects can embed other objects, have element collections of basic or embeddable types, and have relationships to entities. 
If Embedded using in collection then the embedded object in the collection can only include mappings where the foreign key is stored in the source table.
To override how a relationship is mapped in Embedded, we need to use  @AssociationOverride, which provides us with the ability to override relationship join columns and join tables. 
only embeddables with relationships that are owned by the source entity, A, and that are unidirectional, can be reused in other entities. 
There is currently no way to override the collection table for an element collection in an embeddable. 

###Compound Primary Keys 
Primary key classes must include method definitions for equals() and hashCode() in order to be able to be stored and keyed on by the persistence provider, and their fields or properties must be in the set of valid identifier types 
They must also be public, implement Serializable, and have a no-arg constructor. 
Для создания композитного ключа нужно проаннатировать ключевый поля аннотацией @Id и класс аннотацией @IdClass(CustomIdClass.class) в которой указать клас который будет включать в себа теже поля этот класс будет использоваться в поиске .find()
Второй подход это использовать для ключа Embedded объект для этого поле сущьность ключа нужно проанатировать аннотацией @EmbeddedId
Для использования Embedded ключа в запросе нужно явно использовать его поля `SELECT e " +"FROM Employee e " +"WHERE e.id.country = ?1 AND e.id.id = ?2`

When an identifier in one entity includes a foreign key to another entity, it’s called a derived identifier. 
a new dependent entity cannot be persisted without the relationship to the parent entity being established. 

####Basic Rules for Derived Identifiers 
A dependent entity might have multiple parent entities (i.e., a derived identifier might include multiple foreign keys). 
A dependent entity must have all its relationships to parent entities set before it can be persisted.  
If an entity class has multiple id attributes, then not only must it use an id class, but there must also be a corresponding attribute of the same name in the id class as each of the id attributes in the entity. 
Id attributes in an entity might be of a simple type, or of an entity type that is the target of a many-to-one or one-to-one relationship. 
If an id attribute in an entity is of a simple type, then the type of the matching attribute in the id class must be of the same simple type. 
If an id attribute in an entity is a relationship, then the type of the matching attribute in the id class is of the same type as the primary key type of the target entity in the relationship (whether the primary key type is a simple type, an id class, or an embedded id class). 
If the derived identifier of a dependent entity is in the form of an embedded id class, then each attribute of that id class that represents a relationship should be referred to by a @MapsId annotation on the corresponding relationship attribute. 

`@Entity `
`public class EmployeeHistory {`
`// ...`
`@Id`
`@OneToOne`
`@JoinColumn(name="EMP_ID")`
`private Employee employee;`
`// ... `
`}`

Для указания отдельного атрибута с ИД можно на соответственной связи уаказать аннотацию @MapsId
The identifier attribute will get filled in automatically by the provider when an entity instance is read from the database, or flushed/committed. 
@JoinColumns используется для того чтоб переопределить ИД в связи которая участвует в ИД
The compound derived identifier means that we must also specify the primary key class using the @IdClass annotation.
В случае с композитным ключем в котором есть связь класс ключа должен содержать не Ентити связя а поле с ее типом ключа

@Entity 
public class Project {

@EmbeddedId private ProjectId id;

@MapsId("dept")
@ManyToOne
@JoinColumns({
	@JoinColumn(name="DEPT_NUM", referencedColumnName="NUM"),
	@JoinColumn(name="DEPT_CTRY", referencedColumnName="CTRY")})
private Department department;
// ... 
}

в этом примере аннотация @MapsId("dept") используется для того чтоб замапить департамент в поле id
the @MapsId annotation and the ability to apply @Id to relationship attributes was introduced in jpa 2.0 

If the data in the mapped table already exists and we want to ensure that it will not be modified at runtime, then the insertable and updatable elements can be set to false, effectively preventing the provider from doing anything other than reading the entity from the database. 
Если указать insertable=false, updatable=false тогда провайдер не будет писать эти свойства, если свойства будут изменены он их просто проигнорит
@ManyToOne можно повесить даже на связь которая может быть владельцем
`@ManyToOne`
`@JoinTable(name = "ioe_p")`
`private Phone phone;`

Для того чтоб избежать создания третьей таблицы в односторонней связи @OneToMany достаточно указать @JoinColumn(name="DEPT_ID") в исходном объекте это повляет на таблицу целевого объекта но не на сам объект

`@OneToMany`referencedColumnName
`@JoinColumn(name="DEPT_ID")`
`private Collection<Employee> employees;`

Для переопределения полей в композитной связи можно использовать @JoinColumns где name = новое имя колонки, referencedColumnName = старое имя аттрибута

Only relationships with single cardinality on the source side can enable orphan removal, which is why the orphanRemoval option is defined on the @OneToOne and @OneToMany relationship annotations, but on neither of the  @ManyToOne or @ManyToMany annotations. 
When specified, the orphanRemoval element causes the child entity to be removed when the relationship between the parent and the child is broken.(This can be done either by setting to null the attribute that holds the related entity, or additionally in the one-to-many case by removing the child entity from the collection. )
С hibernate orphanRemoval не работает

Для того чтоб связь имели некие атрибуты можно использовать две связи @OneToMany(mappedBy=) и один промежуточный объект в котором ключ будет состоять из двух ключей сущьностей в эту связь можно добавить дополнительные поля

Можно разбить аттрибуты сущьности по разным таблицам для этого нужно указать для сущьность список дополнинельных таблиц в аннотации @SecondaryTables и указать имена таблиц и ключи связи которые указываются в аннотоции @SecondaryTable(name = "user_address", pkJoinColumns = @PrimaryKeyJoinColumn(name = "user_id")) далее нужно для кождого аттрибута указать его таблицу @Column(table = "user_address")
eferencedColumnName elements in those join columns refer to the primary key columns COUNTRY and EMP_ID in the entity’s own primary table EMPLOYEE. 

###Inheritance
An entity hierarchy is rooted at the first entity class in the hierarchy. 
A mapped superclass provides a convenient class in which to store shared state and behavior that entities can inherit from, but it is itself not a persistent class and cannot act in the capacity of an entity. It cannot be queried over and cannot be the target of a relationship. Annotations such as @Table are not permitted on mapped superclasses because the state defined in them applies only to its entity subclasses. 
Mapped superclass can’t be instantiated as persistent entities. They do not play a role in an entity inheritance hierarchy other than contributing that state and behavior to the entities that inherit from them.
Mapped superclasses may or may not be defined as abstract in their class definitions, but it is good practice to make them actual abstract Java classes.
A class is indicated as being a mapped superclass by annotating it with the @MappedSuperclass annotation.
Classes in an entity hierarchy, that are not entities or mapped superclasses, are called transient classes.
Поля унаследованые от транзитных классов не сохраняются в БД
В иерархии сущьностей классы могут быть как абстрактными так и конкретными
Сушьность может быть абстрактным классом. Его можно использовать в запросах но нельзя инстанциировать с помощью джавы. We would not want users to accidentally instantiate this class and then try to persist a partially defined employee.

####JPA provides support for three different data representations. 
	1. Single-Table Strategy 
	2. Joined Strategy
	3. Table-per-Concrete-Class Strategy 
	
When an entity hierarchy exists, it is always rooted at an entity class.
The root entity class must signify the inheritance hierarchy by being annotated with the @Inheritance annotation. В этой аннотации указывается стратегия наследования
Every entity in the hierarchy must either define or inherit its identifier, which means that the identifier must be defined either in the root entity or in a mapped superclass above it.
In general, the single-table approach tends to be more wasteful of database tablespace, but it does offer peak performance for both polymorphic queries and write operations.
Для переопределения колонки разделителя используется аннотация @DiscriminatorColumn
Every row in the table will have a value in the discriminator column called a discriminator value, or a class indicator, to indicate the type of entity that is stored in that row.
If no @DiscriminatorValue annotation is specified, then the provider will use a provider-specific way of obtaining the value. 
If the discriminatorType is INTEGER, then we would either have to specify the discriminator values for every entity class or none of them.
Желательно определять либо все @DiscriminatorValue либо ни одного чтоб можно было гарантировать уникальность
discriminator values should not be specified for abstract entity classes, mapped superclasses, transient classes, or any abstract classes for that matter

Mapping a table per entity provides the data reuse that a normalized 2data schema offers and is the most efficient way to store data that is shared by multiple subclasses in a hierarchy. The problem is that, when it comes time to reassemble an instance of any of the subclasses, the tables of the subclasses must be joined together with the superclass tables. 
In a joined approach, we will have the same type of primary key in each of the tables, and the primary key of a subclass  table also acts as a foreign key that joins to its superclass table.
joined approach может работать и  без @DiscriminatorColumn. Some vendors offer implementations of joined inheritance without the use of a discriminator column.  discriminator columns should be used if provider portability is required. 

Table-per-Concrete-Class Strategy goes in the reverse direction of non-normalization of entity data and maps each concrete entity class and all its inherited state to a separate table.
This strategy is not required to be supported by providers but is included because it is anticipated that it will be required in a future release of the API.
The negative side of using this strategy is that it makes polymorphic querying across a class hierarchy more expensive than the other strategies. (UNION)

Для переопределения полей в иерархии используется аннотация @AttributeOverride а для переопределения связей используется @AssociationOverride

Mixed Inheritance warning that it might not be portable to rely on such behavior, even if your vendor supports it. 
Furthermore, it really makes sense to mix only single-table and joined inheritance types. We will show an example of mixing these two, bearing in mind that support for them is vendor-specific. 




