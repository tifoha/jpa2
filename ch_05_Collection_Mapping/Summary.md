Collections of embeddable and basic types are not relationships; they are simply collections of elements that are thus called element collections.
Коллекции могут содержать базовые типы embaddad или Serializable объекты
В коллекции объектов все поля должны быть уникальные, но это можно переопоеделить с помощью @AttributeOverrides
AttributeOverride используется для перезаписи колонок в  Embadable элементах
Depending upon the needs of the application, any of Collection, Set, List, and Map might be appropriate.
Once the entity becomes managed or has been persisted, the interface must always be used when operating on the collection This is because the moment the entity becomes managed, the persistence provider can replace the initial concrete instance with an alternate instance of a Collection implementation class of its own.
The most prevalent approach to ordering entities or elements in a List is to specify an ordering rule based on the comparison of a particular attribute of the entity or element.
We indicate the attribute to order by in the @OrderBy annotation. @OrderBy("attr1 asc,attr2 desc) by default ordered by Id. ASC можно и не писать но это плохой тон. Можно также сортировать по вложенным объектам к примеру "info.value desc"
@OrderBy annotation имеет смысл применять только на List хотя на остальных оно тоже работает но это зависит от провайдера
@OrderBy применяется только тогда когда сущьность подгружается из БД
As a rule of thumb, the List order should always be maintained in memory to be consistent with the @OrderBy ordering rules. 
@OrderColumn можно использовать вместо @OrderBy. Эта аннотация добавляет дополнительную колонку в таблицу для упорядочивания строк. эта колонка не видна пользователю
При изменении колекции с @OrderColumn во время записи провайдер обновит порядок всех элементов что приведет к дополнительной нагрузке на БД
@OrderColumn работает только с List
MAPS
Although basic types, embeddable types, or entity types can be Map keys or values, remember that if they are playing the role of key, they must follow the basic rules for keys. 
Basic keys are stored in the tables referred to by the mapping.
@MapKeyColumn, which is used to indicate the column in the collection table that stores the basic key. When the annotation is not specified, the key is stored in a column named after the mapped collection attribute, appended with the “_KEY” suffix. 
MapKeyEnumerated & MapKeyTemporal применяются для указания специфических ключей и применяются только в мапах
However, if we were to put @Enumerated on our Map attribute, it would apply to the values of the element collection, not the keys. 
Если попытаться сохранить мапу на mappedBy стороне тогда сохранится только ключ, Значение сохраняется только на ведущей стороне.
Ключ по умолчанию not null
@MapKey позволяет сделать мапу из связи One to Many но в качестве ключа поставить одно из полей таргета
Также можно создать мапу типа Map<Embdded, Entity> где Embedded будет частью полей entity
Для переопределения Embedded свойств используется конструкция 
@AttributeOverrides({
						@AttributeOverride(name="key.first_Name",column=@Column(name="EMP_FNAME")),
						@AttributeOverride(name="value.last_Name",column=@Column(name="EMP_LNAME")) 
					})
Для того чтоб делать ключем Entity и переопредилить его ключевое поле существует аннотация @MapKeyJoinColumn
В мапах связь OneToMany или ManyToOne Используется тогда когда Entity является Значением мапы					 

RULES FOR MAPS:

	* @MapKeyClass and targetEntity/targetClass elements of the relationship and element collection mappings to specify the classes when an untyped Map is used. 
	* Use @MapKey with one-to-many or many-to-many relationship Map that is keyed on an attribute of the target entity. 
	* Use @MapKeyJoinColumn to override the join column of the entity key. 
	* Use @Column to override the column storing the values of an element collection of basic types. 
	* Use @MapKeyColumn to override the column storing the keys when keyed by a basic type. 
	* Use @MapKeyTemporal and @MapKeyEnumerated if you need to further qualify a basic key that is a temporal or enumerated type. 
	* Use @AttributeOverride with a “key.” or “value.” prefix to override the column of an embeddable attribute type that is a Map key or a value, respectively. 

*Table 5-1. Summary of Mapping a Map*

Map	|	Mapping	|	Key Annotation	|	Value Annotation |
---	|------------	|------------------	|-------------------| 
Map\<Basic,Basic>|@ElementCollection |@MapKeyColumn, @MayKeyEnumerated, @MapKeyTemporal| @Column |
Map\<Basic,Embeddable> |@ElementCollection |@MapKeyColumn, @MayKeyEnumerated, @MapKeyTemporal |Mapped by embeddable, @AttributeOverride, @AssociationOverride 
Map\<Basic,Entity>|@OneToMany,  @ManyToMany |@MapKey, @MapKeyColumn, @MayKeyEnumerated, @MapKeyTemporal |Mapped by entity 
Map\<Embeddable,Basic> |@ElementCollection |Mapped by embeddable, @AttributeOverride |@Column 
Map\<Embeddable,Embeddable> |@ElementCollection |Mapped by embeddable, @AttributeOverride |Mapped by embeddable, @AttributeOverride, @AssociationOverride 
Map\<Embeddable,Entity> |@OneToMany,  @ManyToMany |Mapped by embeddable @AttributeOverride |Mapped by entity 
Map\<Entity,Basic>|@ElementCollection |@MapKeyJoinColumn |@Column 
Map\<Entity,Embeddable> |@ElementCollection |@MapKeyJoinColumn |Mapped by embeddable, @AttributeOverride, @AssociationOverride 
Map\<Entity,Entity>|@OneToMany,  @ManyToMany |@MapKeyJoinColumn |Mapped by entity 

They simply delegate the decision to the implementation, so an implementation class might choose to support inserting null values or throw an exception. JPA does no better; it ends up falling to the particular vendor’s proxy implementation to allow null or throw a NullPointerException when n
only relationships and element collections that use a join table or collection table can have null values in the collection. 

BEST PRACTICES:

	* List, do not assume that it is ordered automatically if you have not actually specified any ordering. The List order might be affected by the database results, which are only partially deterministic with respect to how they are ordered. There are no guarantees that such an ordering will be the same across multiple executions. 
	* It will generally be possible to order the objects by one of their own attributes. Using the  @OrderBy annotation will always be the best approach when compared to a persistent List that must maintain the order of the items within it by updating a specific order column. Use the order column only when it is impossible to do otherwise. 
	* Map types are very helpful, but they can be relatively complicated to properly configure. Once you reach that stage, however, the modeling capabilities that they offer and the loose association support that can be leveraged makes them ideal candidates for various kinds of relationships and element collections. 
	* As with the List, the preferred and most efficient use of a Map is to use an attribute of the target object as a key, making a Map of entities keyed by a basic attribute type the most common and useful. It will often solve most of the problems you encounter. A Map of basic keys and values can be a useful configuration for associating one basic object with another. 
	* Avoid using embedded objects in a Map, particularly as keys, because their identity is typically not defined. Embeddables in general should be treated with care and used only when absolutely necessary. 
	* Support for duplicate or null values in collections is not guaranteed, and is not recommended even when possible. They will cause certain types of operations on the collection type to be slower and more database-intensive, sometimes amounting to a combination of record deletion and insertion instead of simple updates. 