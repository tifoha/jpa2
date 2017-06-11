#XML Mapping Files
####Logic for obtaining the metadata for the persistence unit:
	1. Process the annotations. The set of entities, mapped superclasses, and embedded objects (we’ll call this set E) is discovered by looking for the @Entity, @MappedSuperclass, and  @Embeddable annotations. The class and method annotations in all the classes in set E  are processed, and the resulting metadata is stored in set C. Any missing metadata that was not explicitly specified in the annotations is left empty.
	2. Add the classes defined in XML. Look for all the entities, mapped superclasses, and embedded objects that are defined in the mapping files and add them to E. If we find that one of the classes already exists in E, we apply the overriding rules for class-level metadata that we found in the mapping file. Add or adjust the class-level metadata in C according to the overriding rules.
	3. Add the attribute mappings defined in XML. For each class in E, look at the fields or properties in the mapping file and try to add the method metadata to C. If the field or property already exists there, apply the overriding rules for attribute-level mapping metadata.
	4. Apply defaults. Determine all default values according to the scoping rules and where defaults might have been defined (see the following for description of default rules). The classes, attribute mappings, and other settings that have not yet been filled in are assigned values and put in C. 
	
The same defaults that you saw for annotations will be applied when using mapping files as well.
Any number of mapping files, or none, might be included in a persistence unit. 
####Mapping file
`<?xml version="1.0" encoding="UTF-8"?>` 
`<entity-mappings xmlns="http:// xmlns.jcp.org /xml/ns/persistence/orm"xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"xsi:schemaLocation="http:// xmlns.jcp.org/xml/ns/persistence/ormhttp:// xmlns.jcp.org/xml/ns/persistence/orm_2_1.xsd"version="2.1">`
`</entity-mappings>`

####The subelements of entity-mappings can be categorized into four main scoping and functional groups: 
	* Persistence unit defaults 
	* Mapping files defaults 
	* Queries and generators
	* Managed classes and mappings. 

##Disabling Annotations 
When the xml-mapping-metadata-complete element is specified, all annotations in the entire persistence unit will be ignored, and only the mapping files in the persistence unit will be considered as the total set of provided metadata.
`<entity-mappings>`
`	<persistence-unit-metadata>`
`		<xml-mapping-metadata-complete/>`
`	</persistence-unit-metadata>`
`</entity-mappings>`
If enabled, there is no way to portably override this setting. It is globaly.
The metadata-complete attribute is an attribute on the entity, mapped-superclass, and embeddable elements. Деактивирует все аннотации на данной сущьности
annotations defining queries, generators, or result set mappings are ignored if they are defined on a class that is marked as metadata-complete in an XML mapping file. 
В этом примере все метаданные будут перезаписаны значениями по умалчанию, а все аннотации будут отменены 
`<entity-mappings>`
`	<entity class="examples.model.Employee"`
`			metadata-complete="true">`
`		<attributes>`
`			<id name="id"/>`
`		</attributes>`
`	</entity>`
`</entity-mappingsЮ`

The element that encloses all the persistence unit level defaults is the aptly named persistence-unit-defaults element. It is the other subelement of the persistence-unit-metadata element (after xml-mapping-metadata-complete). 
If more than one mapping file exists in a persistence unit, only one of the files should contain these elements. 
####Default Sttings
* **schema** Устанавливает схему БД по умолчанию
	* _Возможности переопределения:_
		* schema element defined in the mapping file defaults (see the “Mapping File Defaults” section) 
		* schema attribute on any table, secondary-table, join-table, collection-table,  sequence-generator, or table-generator element in a mapping file 
		* schema defined within a @Table, @SecondaryTable, @JoinTable, @CollectionTable, @SequenceGenerator, or @TableGenerator annotation; or in a @TableGenerator annotation (unless xml-mapping-metadata-complete is set) 
* **catalog** The catalog element is exactly analogous to the schema element, but it is for databases that support catalogs. 
* **delimited-identifiers** (экранирование ключевых слов в именах таблиц и колонок) causes database table, schema, and column identifiers used in the persistence unit, defined in annotation form, XML, or defaulted, to be delimited when sent to the database (refer to Chapter 10 for more on delimited identifiers).It cannot be disabled locally, so it is important to have a full understanding of the consequences before enabling this option. If an annotation or XML element is locally delimited with quotes, they will be treated as part of the identifier name.    
* **access** used to set the access type for all the managed classes in the persistence unit that have XML entries but are not annotated. Its value can be either “FIELD” or “PROPERTY,” indicating how the provider should access the persistent state.
 	* _Возможности переопределения:_
		* access element defined in the mapping file defaults (see the “Mapping File Defaults” section)
		* access attribute on any entity, mapped-superclass, or embeddable element in a mapping file
		* access attribute on any basic, id, embedded-id, embedded, many-to-one, one-to-one,  one-to-many, many-to-many, element-collection, or version element in a mapping file
		* @Access annotation on any entity, mapped superclass, or embeddable class
		* @Access annotation on any field or property in an entity, mapped superclass, or embedded object An annotated field or property in an entity, mapped superclass, or embedded object
* **cascade-persist**  When the empty cascade-persist element is specified,  it is analogous to adding the PERSIST cascade option to all the relationships in the persistence unit. 
* **entity-listeners** This is the only place where a list of default entity listeners can be specified. They will be invoked in the order that they are listed in this element, before any other listener or callback method is invoked on the entity.
 	* _Возможности переопределения:_
		* exclude-default-listeners element in an entity or mapped-superclass mapping file element
		* @ExcludeDefaultListeners annotation on an entity or mapped superclass  (unless xml-mapping-metadata-complete is set) 

##Mapping File Defaults
In general,  if there is a persistence unit default defined for the same setting, this value will override the persistence unit default for the managed classes in the mapping file. 
Эти значения применяются только к классам определенным в этом маппинг файле. they follow the persistence-unit-metadata element.
* **package** The package element is intended to be used by developers who don’t want to have to repeat the fully qualified class name in all the mapping file metadata.  
* **schema**  
* **catalog** 
* **access**  
##Queries and Generators
these elements might appear in different contexts, but they are nevertheless still scoped to the persistence unit. 
There is no concept of overriding queries, generators, or result set mappings within the same or different mapping files. 
* **sequence-generator** The sequence-generator element is used to define a generator that uses a database sequence to generate identifiers.   
* **table-generator** The table-generator element defines a generator that uses a table to generate identifiers.  
* **named-query** A named-query element in the mapping file can also override an existing query of the same name that was defined as an annotation. Query strings can also be expressed as CDATA within the query element. Это удобно когда нужно заэкранировать специальные символы "</>"  
* **named-native-query**  
* **and sql-result-set-mapping** 
##Managed Classes and Mappings
The attributes element is a subelement of the entity, mapped-superclass, and embeddable elements. It is an enclosing element that groups all the mapping subelements for the fields or properties of the managed class. 
For identifiers, either multiple id subelements or a single embedded-id subelement can be included. 
An embeddable element is not permitted to contain  id, embedded-id, or version mapping subelements.
Each one has a name attribute (in the XML attribute sense)  that is required to indicate the name of the attribute (in this case, we mean field or property) that it is mapping. 
when overriding annotations, you should use the correct and compatible XML mapping.
The rule of thumb is that mappings should be overridden primarily to change the data-level mapping information. This would normally need to be done when, for example, an application is developed on one database but deployed to another or must deploy to multiple different databases in production.In these cases, the XML mappings would likely be xml-mapping-metadata-complete anyway, and the XML metadata would be used in its entirety rather than cobbling together bits of annotations and bits of XML and trying to keep it all straight across multiple database XML mapping configurations.  
**table** A table element can occur as a subelement of entity and describes the table that the entity is mapped to.
**secondary-table** Any number of secondary tables can be added to the entity by adding one or more secondary-table subelements to the entity element.
`<entity class="examples.model.Employee">`
`	<table name="EMP"/>`
`	<secondary-table name="EMP_INFO"/>`
`	<secondary-table name="EMP_HIST">`
`		<primary-key-join-column name="EMP_ID"/>`
`	</secondary-table>`
`</entity>` 
**id**  Overriding applies to the configuration information within a given identifier type, but the identifier type of a managed class should almost never be changed. 
`<entity class="examples.model.Employee">`
`	<attributes>`
`		<id name="id">`
`			<generated-value strategy="SEQUENCE" generator="empSeq"/>`
`			<sequence-generator name="empSeq" sequence-name="mySeq"/>`
`		</id>`
`	</attributes>` 
`</entity>`
**basic** this element is required when mapping persistent state to a specific column. Just as with annotations, when a field or property is not mapped, it will be assumed to be a basic mapping and will be defaulted as such.
`<attributes>`
`	<basic name="name"/>`
`	<basic name="comments">`
`		<column name="COMM"/>`
`		<lob/>`
`	</basic>`
`	<basic name="type">`
`		<column name="STR_TYPE"/>`
`		<enumerated>STRING</enumerated>`
`	</basic>`
`</attributes> `
**transient**  A transient element marks a field or property as being non-persistent. 
`<attributes>`
`	<transient name="cacheAge"/>`
`</attributes>`

`<attributes>`
`	<element-collection name="projectHours" target-class="java.lang.Integer">`
`		<map-key-class name="java.lang.String"/>`
`		<map-key-column name="PROJ_NAME"/>`
`		<column name="HOURS_WORKED"/>`
`		<collection-table name="PROJ_TIME"/>`
`	</element-collection>`
`</attributes> `

`<attributes>`
`	<embedded name`="address">
`		<attribute`-override name="state">
`			<colum`n name="PROV"/>
`		</attribut`e-override>
`		<attribute`-override name="zip">
`			<colum`n name="PCODE"/>
`		</attribut`e-override>
`	</embedded>`
`</attributes> `

The pre-persist, post-persist, pre-update,  post-update, pre-remove, post-remove, and post-load methods are all valid subelements of the entity or  mapped-superclass elements.
Each lifecycle event element will override any entity callback method of the same event type that might be annotated in the entity class. 
The entity-listeners element can be specified as a subelement of an entity or mapped-superclass element to accomplish exactly the same thing. It will also have the effect of overriding the entity listeners defined in an @EntityListeners annotation with the ones defined in the  entity-listeners element. 
<entity-listeners>
	<entity-listener class="examples.listeners.EmployeeAuditListener">
		<post-persist method-name="employeeCreated"/>
		<post-update method-name="employeeUpdated"/>
		<post-remove method-name="employeeRemoved"/>
	</entity-listener>
	<entity-listener class="examples.listeners.NameValidator">
		<pre-persist method-name="validateName"/>
	</entity-listener>
	<entity-listener class="examples.listeners.EmployeeExitListener">
		<post-remove method-name="notifyIT"/>
	</entity-listener>
</entity-listeners>
##Converters 
Converters are a way to programmatically transform the data in a basic mapped field or property into an alternate form before it gets saved to the database, and then reverse the transformation again when the data gets read from the database back into the entity. 
A converter is a managed class that can be declared either using the @Converter annotation or in the mapping file using the converter subelement of entity-mappings. 
The converter element can be specified at the same level as the entity, embeddable, or mapped-superclass elements. It has only two attributes: class and auto-apply. 
Converter should implements the javax.persistence.AttributeConverter<X,Y>
It is undefined to have more than one converter class declared using the @Converter annotation to be  auto-applied to the same target field or property type. 
However, an annotated converter can be overridden by using the converter element to declare a different (unannotated) converter class auto-applied to the same target type.  
`<convert converter="SecureURLConverter" attribute-name="homePage"/>` так можно заоверрайдить конвертер наследуемого поля