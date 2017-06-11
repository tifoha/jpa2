* All fields must be declared as either protected, package, or private. Public fields are disallowed because it would open up the state fields to access by any unprotected class in the JVM. 
* javax.persistence.schema-generation.database.action	none, create, drop-and-create, drop
* PROPERTY ACCESS When property access mode is used, the same contract as for JavaBeans applies, and there must be getter and setter methods for the persistent properties. The type of property is determined by the return type of the getter method and must be the same as the type of the single parameter passed into the setter method. Both methods must be either public or protected visibility.
* FIELD ACCESS The mapping annotations for a property must be on the getter method.
* MIXED ACCESS @Access анотация отвечает за подход к хранению данных ее можно вешить на FIELD, PROPERTY
* Corresponding field or property to the one being made persistent must be marked as transient so that the default accessing rules do not cause the same state to be persisted twice.
* Default names are not specified to be either uppercase or lowercase. Most databases are not case-sensitive,  so it won’t generally matter whether a vendor uses the case of the entity name or converts it to uppercase. in Chapter 10, we discuss how to delimit database identifiers when the database is set to be case-sensitive.
* Some vendors might allow the schema to be included in the name element of the table without having to specify the schema element, such as in @Table(name="HR.EMP").

* MAPPING SIMPLE TYPES:

		1. byte, int, short, long, boolean, char, float, double
		2. Wrapper classes of primitive Java types: Byte, Integer, Short, Long, Boolean, Character, Float, Double
		3. Byte and character array types: byte[], Byte[], char[], Character[]
		4. Large numeric types: java.math.BigInteger, java.math.BigDecimal
		5. Strings: java.lang.String
		6. Java temporal types: java.util.Date, java.util.Calendar
		7. JDBC temporal types: java.sql.Date, java.sql.Time, java.sql.Timestamp
		8. Enumerated types: Any system or user-defined enumerated type
		9. Serializable objects: Any system or user-defined serializable type;

* An optional @Basic annotation can be placed on a field or property to explicitly mark it as being persistent.
* Без транзакции persiste, update или delete не работает
* Lazy Fetching - это скорее рекомендация для провайдера о способе доставки полей (любых) особенно если указано Lazy. Если указано Eager то это более строгая инструкция. Но в любом случае все зависит от провайдера.
* Second, on the surface it might appear that this is a good idea for certain attributes of an entity, but in practice it is almost never a good idea to lazily fetch simple types. 
* The @Lob annotation acts as the marker annotation to fulfill this purpose and might appear in conjunction with the @Basic annotation, or it might appear when @Basic is absent and implicitly assumed to be on the mapping. The Java types mapped to BLOB columns are byte[], Byte[], and Serializable types, while char[], Character[], and String objects are mapped to CLOB columns.  
* Enumerated Types могут сохранятся в БД как число или строка при помощи аннотации @Enumerated 
* @Temporal - необходимо для указания каким образом сохранять дату в БД (Date, Time, Timestamp)
* Attributes that are part of a persistent entity but not intended to be persistent can either be modified with the transient modifier in Java or be annotated with the @Transient annotation.
* When an entity identifier is composed of only a single attribute, it's called a simple identifier. 

* PRIMARY KEY TYPES
 		1.  byte, int, short, long, char 
 		2.  Byte, Integer, Short, Long, Character 
 		3.  java.lang.String 
 		4. 	java.math.BigInteger 
 		5.	java.util.Date, java.sql.Date 
* @GeneratedValue аннотация для указанния доп параметров для генерации ИД
* Applications can choose one of four different id generation strategies by specifying a strategy in the strategy element. The value can be any one of AUTO, TABLE, SEQUENCE, or IDENTITY enumerated values of the GenerationType enumerated type. 
We recommend that long be used to accommodate the full extent of the generated identifier domain.
The most flexible and portable way to generate identifiers is to use a database table. Not only will it port to different databases but it also allows for storing multiple different identifier sequences for different entities within the same table. 
The provider might allocate identifiers within the same transaction as the entity being persisted or in a separate transaction. it is not specified, but you should check your provider documentation to see how it can avoid the risk of deadlock when concurrent threads are creating entities and locking resources. 
@TableGenerator(name="Address_Gen",table="ID_GEN",pkColumnName="GEN_NAME",valueColumnName="GEN_VAL",pkColumnValue="Addr_Gen",initialValue=10000,allocationSize=100) 
pkColumnValue  предназначено для явного указания имени сущьности в строке(используется редко)
Many databases support an internal mechanism for id generation called sequences.
Sequence: Note that the default allocation size is 50, just as it is with table generators. If schema generation is not being used, and the sequence is being manually created, the INCREMENT BY clause would need to be configured to match the allocationSize element or default allocation size of the corresponding @SequenceGenerator annotation. 
Some databases support a primary key identity column, sometimes referred to as an autonumber column. They are generally less efficient for object-relational identifier generation because they cannot be allocated in blocks and because the identifier is not available until after commit time. 
IDENTITY will indicate to the provider that it must reread the inserted row from the table after an insert has occurred. IDENTITY generation cannot be shared across multiple entity types. (недоступна до инсерта либо коммита в транзакции в отличии от остальных видов гинерации инициирует инсерт во время persist())

RELATIONSHIPS
Bidirectional relationship can be viewed as a pair of two unidirectional mappings
Cardinality - Это количественное отношение в отношениях
Все отношения строятся с точки зрения от source к target
Ordinality - указывает на то обязатильная ли это связь source не может существовать без target
Many-to-One Mapping - Поле всегда в единичном экземпляре обычно является владельцем связи так как таблица содержит foreign key
С помощью @JoinColumn можно настроить имя колонки в которой будет содержаться ключ
One-to-Many Mapping Поле всегда Collection<> не содержит  JoinColumn (иначе придется подключать третью таблицу) mappedBy = должно указывать на имя поля у владельца связи
Although we have described the owning side as being determined by the data schema, the object model must indicate the owning side through the use of the relationship mapping annotations. the absence of the mappedBy element in the mapping annotation implies ownership of the relationship, while the presence of the mappedBy element means the entity is on the inverse side of the relationship. the mappedBy element is described in subsequent sections.

There are two important points to remember when defining bidirectional one-to-many (or many-to-one) relationships: 
	1. The many-to-one side should be the owning side, so the join column should be defined on that side. 
	2.The one-to-many mapping should be the inverse side, so the mappedBy element should  be used. 

One-to-One Mapping семетричная связь но должен быть выбран владелец у владельца на колонку устанавливается constraint

The two rules, then, for bidirectional one-to-one associations are the following: 
	1. The @JoinColumn annotation goes on the mapping of the entity that is mapped to the table containing the join column, or the owner of the relationship. This might be on either side of the association. 
	2. The mappedBy element should be specified in the @OneToOne annotation in the entity that does not define a join column, or the inverse side of the relationship. 
	
When a many-to-many relationship is bidirectional, both sides of the relationship are many-to-many mappings.
Note that no matter which side is designated as the owner, the other side should include the mappedBy element; otherwise, the provider will think that both sides are the owner and that the mappings are separate unidirectional relationships. 
The @JoinTable annotation is used to configure the join table for the relationship. 
The When an entity has a one-to-many mapping to a target entity, but the @OneToMany annotation does not include the mappedBy element, it is assumed to be in a unidirectional relationship with the target entity. Эта связь будет использовать третью таблицу для связи. Эту таблицу можно настроить с помощью @JoinTab& 
At the relationship level, however, lazy loading can be a big boon to enhancing performance. It can reduce the amount of SQL that gets executed, and speed up queries and object loading considerably. 
When not specified on a  single-valued relationship, the related object is guaranteed to be loaded eagerly. Collection-valued relationships default to be lazily loaded, but because lazy loading is only a hint to the provider, they can be loaded eagerly if the provider decides to do so. 
A relationship that is specified or defaulted to be lazily loaded might or might not cause the related object to be loaded when the getter method is used to access the object. the object might be a proxy, so it might take actually invoking a method on it to cause it to be faulted in. 

EMBADDED OBJECTS
Although embedded objects are referenced by the entities that own them, they are not said to be in relationships with the entities. the term relationship can only be applied when both sides are entities. 
It is not portable to define embedded objects as part of inheritance hierarchies. Once they begin to extend one another, the complexity of embedding them increases, and the value for cost ratio decreases. 
We need a way for an entity to map the embedded object according to its own entity table needs, and we have one in the @AttributeOverride annotation
We use an @AttributeOverride annotation for each attribute of the embedded object that we want to override in the entity. 

 
 