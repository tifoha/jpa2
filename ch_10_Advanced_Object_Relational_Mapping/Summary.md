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
С hibernate это не работает

