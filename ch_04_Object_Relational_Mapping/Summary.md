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
		9. Serializable objects: Any system or user-defined serializable type

* An optional @Basic annotation can be placed on a field or property to explicitly mark it as being persistent.
* Без транзакции persiste, update или delete не работает
* Lazy Fetching - это скорее рекомендация для провайдера о способе доставки полей (любых) особенно если указано Lazy. Если указано Eager то это более строгая инструкция. Но в любом случае все зависит от провайдера.
* Second, on the surface it might appear that this is a good idea for certain attributes of an entity, but in practice it is almost never a good idea to lazily fetch simple types. 
* The @Lob annotation acts as the marker annotation to fulfill this purpose and might appear in conjunction with the @Basic annotation, or it might appear when @Basic is absent and implicitly assumed to be on the mapping. The Java types mapped to BLOB columns are byte[], Byte[], and Serializable types, while char[], Character[], and String objects are mapped to CLOB columns.  
* Enumerated Types могут сохранятся в БД как число или строка при помощи аннотации @Enumerated 