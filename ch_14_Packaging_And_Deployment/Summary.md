#Packaging and Deployment
Once the persistence unit has been configured, we will package a persistence unit with a few of the more common deployment units, such as EJB archives, web archives, and the application archives in a Java EE server. 

##Configuring Persistence Units
There may be one or more persistence.xml files in an application, and each persistence.xml file may define multiple persistence units. 
All the information required for the persistence unit should be specified in the persistence.xml file. Once a packaging strategy has been chosen, the persistence.xml file should be placed in the META-INF directory of the chosen archive. 
Вся информация о persistanceUnit должна находится в элементе <persistence-unit>
###Java EE Deployment
if a persistence unit is defined within a Java EE module, there must not be any other persistence unit of the same name in that module(jar). В других джарниках могут присутствовать Юниты с таким же именем.
Минимальное определение юнита: `<persistence-unit name="UnitName"/>`
###Java SE Deployment
 
##Building and Deploying
 
 
##Outside the Server
 
 
##Schema Generation

