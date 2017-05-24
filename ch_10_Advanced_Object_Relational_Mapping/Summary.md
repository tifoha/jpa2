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