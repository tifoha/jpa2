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
`<persistence-unit name="EmployeeService">`
`	<jta-data-source>java:app/jdbc/EmployeeDS</jta-data-source>`
`	<mapping-file>META-INF/employee_service_queries.xml</mapping-file>`
`	<mapping-file>META-INF/employee_service_entities.xml</mapping-file> `
`</persistence-unit>`
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

The properties element gives a deployer the chance to supply standard and provider-specific settings for the persistence unit. To guarantee runtime compatibility, a provider must ignore properties it does not understand. 

###Java SE Deployment
 
##Building and Deploying
 
 
##Outside the Server
 
 
##Schema Generation

