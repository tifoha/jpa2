#Packaging and Deployment
Once the persistence unit has been configured, we will package a persistence unit with a few of the more common deployment units, such as EJB archives, web archives, and the application archives in a Java EE server. 

##Configuring Persistence Units
There may be one or more persistence.xml files in an application, and each persistence.xml file may define multiple persistence units. 

All the information required for the persistence unit should be specified in the persistence.xml file. Once a packaging strategy has been chosen, the persistence.xml file should be placed in the META-INF directory of the chosen archive. 

Вся информация о persistanceUnit должна находится в элементе <persistence-unit>
###Java EE Deployment
if a persistence unit is defined within a Java EE module, there must not be any other persistence unit of the same name in that module(jar). В других джарниках могут присутствовать Юниты с таким же именем.

Минимальное определение юнита: `<persistence-unit name="UnitName"/>`

In situations such as those described in Chapter 6, when you want to use resource-local transactions instead of JTA, the transaction-type attribute of the persistence-unit element is used to explicitly declare the transaction type of RESOURCE_LOCAL or JTA

`<persistence-unit name="EmployeeService"transaction-type="RESOURCE_LOCAL"/>`

The typical case is that JTA transactions are used, so it is in the jta-data-source element that the name of the JTA data source should be specified. Similarly, if the transaction type of the persistence unit is resource-local,  the non-jta-data-source element should be used. 

If a mapping file named “orm.xml” exists in a META-INF directory on the classpath, for example beside the persistence.xml file, it does not need to be explicitly listed.

Пример ипользования нескольких маппинг файлов:
```
<persistence-unit name="EmployeeService">
	<jta-data-source>java:app/jdbc/EmployeeDS</jta-data-source>
	<mapping-file>META-INF/employee_service_queries.xml</mapping-file>
	<mapping-file>META-INF/employee_service_entities.xml</mapping-file> 
</persistence-unit>
```
Managed classes are all the classes that must be processed and considered in a persistence unit, including entities, mapped superclasses, embeddables, and converter classes. 
####A managed class will be included if it is among the following: 
	* Local classes: The annotated classes in the deployment unit in which its persistence.xml file was packaged. 
	* Classes in mapping files: The classes that have mapping entries in an XML mapping file. 
	* Explicitly listed classes: The classes that are listed as class elements in the persistence.xml file. 
	* Additional JARs of managed classes: The annotated classes in a named JAR listed in a jar-file element in the persistence.xml file.
When a JAR is deployed with a persistence.xml file in the META-INF directory, that JAR will be searched for all the classes that are annotated with  @Entity, @MappedSuperclass, @Embeddable, or @Converter.

Нужно явно задавать класс сущьность в персистент юните в элементе class когда:

The first is when there are additional classes that are not local to the deployment unit JAR. 

When **exclude-unlisted-classes** is used, none of the classes in the local classes category described earlier will be included.  

The jar-file element is used to indicate to the  provider a JAR that may contain annotated classes. (джарник должен быть в класспасе) Again, this may be done by either putting the JAR in the lib directory of the EAR (or WAR if we are deploying a WAR), adding the JAR to the manifest classpath of the deployment unit, or by some other vendor-specific means. 

The provider will then treat the named JAR as if it were a deployment JAR, and it will look for any annotated classes and add them to the persistence unit. It will even search for an orm.xml file in the META-INF directory in the JAR and process it just as if it were an additionally listed mapping file. 
####Sared cache modes:
	* **UNSPECIFIED** The provider chooses whatever option is most appropriate for that provider. 
	* **ALL** Cache all the entities in the persistence unit. 
	* **NONE** Do not cache any of the entities in the persistence unit. 
	* **DISABLE_SELECTED** Cache all entities except those annotated with @Cacheable(false). 
	* **ENABLE_SELECTED** Cache no entities except those annotated with @Cacheable(true). 

##Building and Deploying
The properties element gives a deployer the chance to supply standard and provider-specific settings for the persistence unit. To guarantee runtime compatibility, a provider must ignore properties it does not understand. 

If we want to allow a persistence unit to be shared or accessible by multiple components, either in different Java EE modules or in a single WAR, we should use a persistence archive. 

The persistence archive is simple to create and easy to deploy. It is simply a JAR that contains a persistence.xml in its META-INF directory and the managed classes for the persistence unit defined by the persistence.xml file.

If multiple persistence units are defined in the same persistence.xml file, and exclude-unlisted-classes is not used on either one, the same classes will be added to  all the defined persistence units. 

there may be only one persistence unit of a given name in the same WAR, as well as only one persistence unit of the same name in all the persistence archives at the EAR level.
##Outside the Server
The transaction type does not normally need to be specified when deploying to Java SE. It will just default to being RESOURCE_LOCAL, but may be specified explicitly to make the programming contract more clear.  
####DataSource
```
<properties>
	<property name="javax.persistence.jdbc.driver"value="org.apache.derby.jdbc.ClientDriver"/>
	<property name="javax.persistence.jdbc.url"value="jdbc:derby://localhost:1527/EmpServDB;create=true"/>
	<property name="javax.persistence.jdbc.user"value="APP"/>
	<property name="javax.persistence.jdbc.password"value="APP"/>
</properties> 
```
When the createEntityManagerFactory() method is invoked, the Persistence class will begin a built-in pluggability protocol that goes out and finds the provider that is specified in the persistence unit configuration. Если не указан конкретныйе провайдер будет использован провайдер по умолчинию

To prevent runtime and deployment errors, the provider element should be used if the application has a code dependency on a specific provider.

The properties passed to **createEntityManagerFactory()** method are combined with those already specified, normally in the persistence.xml file. They may be additional properties or they may override the value of a property that was already specified. 
 
##Schema Generation
Schema generation properties and scripts can now be used to create and/or drop the existing tables,  and even cause data to be preloaded into them before running an application.

Генератор схемы принимает несколько вводных и геренирует исходящие скрипты https://web.kamihq.com/web/viewer.html?state=%7B%22ids%22:%5B%220B6eob1xI2ysudXdOR1NMZ09lNzA%22%5D,%22action%22:%22open%22,%22userId%22:%22109076766539106375120%22%7D#9781430249269_Ch14.indd%3AFig1

The inputs can be either the application domain objects with accompanying mapping metadata (in either annotation or XML form), or pre-existing DDL scripts accessible to the processor (either packaged within the application or referenceable from it).

The outputs will be DDL that is either executed in the database by the processor or written out to script files. 

The schema generation process will typically happen either when the application is deployed (for example, when in a container), or when the entity manager factory gets created (on a Persistence.createEntityManagerFactory() or Persistence.generateSchema()  invocation in Java SE). 

Properties passed in at runtime will override properties defined in the persistence.xml file. 
####The properties all fall into one of two categories: 
	generation output 
	generation input. 
The properties are not exclusive, meaning that more than one property can be included, causing the schema generation process to generate to multiple targets.
####Свойство которое отвечает за генерацию схемы **javax.persistence.schema-generation.database.action**
	• none (default): Do no schema generation in the database. 
	• create: Generate the schema in the database.   
	• drop: Drop the schema from the database. 
	• drop-and-create: Drop the schema from the database and then generate a new schema in  the database. 
####Свойство которое отвечает за генерацию скриптов **javax.persistence.schema-generation.scripts.action**
	• none (default): Generate no scripts. 
	• create: Generate a script to create the schema. If the create option is specified, then the javax.persistence.schema-generation.scripts.create-target property must also be supplied and have a value. 
	• drop: Generate a script to drop the schema. If the drop option is specified, then the javax.persistence.schema-generation.scripts.drop-target property must be supplied and have a value.
	• drop-and-create: Generate a script to drop the schema and a script to create the schema. If drop-and-create is specified, then both properties must be supplied with values.  
Example:

`<property name="javax.persistence.schema-generation.scripts.action"value="create"/>` 

`<property name="javax.persistence.schema-generation.scripts.create-targetvalue="file:///c:/scripts/create.ddl"/>`

If the sources are combined, there is the additional option of ordering which happens first, the schema creation from the mapping metadata or the scripts. 

The second pair of properties, **create-script-source** and **drop-script-source**, can be used to specify the exact scripts to use as the inputs, and the last one, **sql-load-script-source**, can be used to preload data into the schema.
####Свойство которое отвечает за выбор источника схемы **javax.persistence.schema-generation.create-source**
	• metadata: Generate schema from mapping metadata. 
	• script: Generate schema from existing script. 
	• metadata-then-script: Generate schema from mapping metadata then from an  existing script. 
	• script-then-metadata: Generate schema from an existing script then from  mapping metadata. 
####Свойство **javax.persistence.schema-generation.drop-source** отвечает за выбор источника для дропа схемы
 	• metadata: Generate DDL to drop schema from mapping metadata. 
 	• script: Use existing script to get DDL to drop schema script. 
 	• metadata-then-script: Generate DDL from mapping metadata, then use an existing script. 
 	• script-then-metadata: Use an existing script, then generate DDL from mapping metadata. 
####Свойство **javax.persistence.schema-generation.create-script-sourc** отвечает за выбор источника скрипта для дропа схемы
####Свойство **javax.persistence.schema-generation.drop-script-source** отвечает за 
Пример:

`<property name="javax.persistence.schema-generation.scripts.drop-script-source"value="META-INF/dropSchema.ddl"/> `
####Свойство **javax.persistence. sql-load-script-source** отвечает за предварительную загрузку схемы
Пример:
`<property name="javax.persistence.sql-load-script-source"value="META-INF/loadData.ddl"/>`
####Table 14-2. Runtime Schema Generation Property Differences 

Property Name | Difference 
--------------|------------
javax.persistence.schema-generation.create-script-source | java.io.Reader instead of String file path 
javax.persistence.schema-generation.drop-script-source |
javax.persistence.sql-load-script-source |
javax.persistence.schema-generation.scripts.create-target | java.io.Writer instead of String file path 
javax.persistence.schema-generation.scripts.drop-target |

A couple of comments are in order before we start into them, though. First, the elements that contain the schema-dependent properties are, with few exceptions, in the physical annotations. This is to try to keep them separate from the logical non-schema-related metadata. Second, these annotations are ignored, for the most part 2 , if the schema is not being generated. This is one reason why using them is a little out of place in the usual case, since schema information about the database is of little use once the schema has been created and is being used. 

###Unique Constraints 

A unique constraint can be created on a generated column or join column by using the unique element in the @Column, @JoinColumn, @MapKeyColumn, or @MapKeyJoinColumn annotations.  

Note that the unique element is unnecessary on the identifier column because a primary key constraint will always be generated for the primary key. 

A second way of adding a unique constraint is to embed one or more @UniqueConstraint annotations in a uniqueConstraints element in the @Table, @SecondaryTable , @JoinTable, @CollectionTable, or @TableGenerator annotations.

`@Table(name="EMP",uniqueConstraints=@UniqueConstraint(columnNames={"NAME"}))` 
###Null Constraints 
A null constraint just indicates that the column may or may not be null. Null constraints are defined on a column by using the nullable element in the @Column, @JoinColumn,  @MapKeyColumn, @MapKeyJoinColumn, or @OrderColumn annotations.
###Indexes
Additional indexes may be generated on a column or sequence of columns in a table by using the indexes element and specifying one or more @Index annotations, each of which specifies an index to add to the table.

The indexes element can be specified on the @Table, @SecondaryTable, @JoinTable, @CollectionTable, and @TableGenerator annotations. 

@Id является индексом по умолчанию

Note that multiple comma-separated column names can be specified in the columnNames string in the case of a  multi-column index.

An optional ASC or DESC can also be added to the column using the same syntax as in the  @OrderBy annotation (see Chapter 5).

We can even put an additional uniqueness constraint on the index using the unique element. 

`@Table(name="EMP",indexes={@Index(name="NAME_IDX", columnNames="EMPNAME", unique=true)})`
###Foreign Key Constraints
По умолчанию провайдер должен строить внешние ключи на связи, но некоторые провайдеры этого не делают, поэтому ты можешь позаботится об этом сам

You can control whether a foreign key constraint gets generated for a join column by using the foreignKey element in the @JoinColumn,  @PrimaryKeyJoinColumn, or @MapKeyJoinColumn annotations. 
```
@ManyToOne
@JoinColumn(name="MGR",foreignKey=@ForeignKey(value=ConstraintMode.CONSTRAINT,foreignKeyDefinition="FOREIGN KEY (Mgr) REFERENCES Emp(Id)"))
```
###String-Based Columns
When a column is generated for a basic mapping of a field or property of type String, char[], or Character[], its length should be explicitly listed in the length element of the @Column annotation if 255 is not the desired maximum 
###Floating Point Columns 

Columns containing floating point types have a precision and scale associated with them.

* **precision** общее количество цифр
* **scale** количество знаков после запятой

Like other schema generation elements, they have no effect on the entity at runtime. 

`@Column(precision=8, scale=2)`
###Defining the Column
Иногда вас может неустраивать тип колонки который генерирует провайдер, для этого можно определить его вручную

By hand-rolling the DDL for the column, we can include it as the column definition and let the provider use it to define the column. 

The columnDefinition element is available in all the column-oriented annotation types, including @Column,  @JoinColumn, @PrimaryKeyJoinColumn, @MapKeyColumn, @MapKeyJoinColumn, @OrderColumn, and @DiscriminatorColumn.
```
@Column(name="START_DATE",columnDefinition="DATE DEFAULT SYSDATE")
private java.sql.Date startDate;
```
When a column definition is included, other accompanying column-specific generation metadata is ignored. Specifying the precision, scale, or length in the same annotation as a column definition would be both unnecessary and confusing.  




