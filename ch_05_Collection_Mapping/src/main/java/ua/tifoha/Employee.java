package ua.tifoha;

import static java.util.Comparator.reverseOrder;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.MapKeyEnumerated;
import javax.persistence.OrderBy;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Vitaliy Sereda on 07.04.17.
 */
@Entity
//@Access(FIELD)
//@Table(name = "jpa2_2.emp")
@Table (name = "emp")
public class Employee extends AbstractEntity {
	@ElementCollection
	@OrderBy ("startDate desc, dayCount")
	@CollectionTable (uniqueConstraints = @UniqueConstraint (columnNames = {"Employee_id", "startDate"}))
//	@AttributeOverrides(@AttributeOverride(name = "dayCount", column = @Column(unique = false)))
	private Set<VacationEntry> vacation = new LinkedHashSet<>();

	@ElementCollection ()
	@CollectionTable (name = "nn", joinColumns = @JoinColumn (name = "emp"))
//	@OrderColumn (name = "nickNames")
	@OrderBy("nickNames desc ")
	private Set<String> nickNames = new TreeSet<>(reverseOrder());

	@ElementCollection
	@OrderColumn()
	private List<SerializableObject> objectSet = new LinkedList<>();

	@Embedded
	private Info info = new Info();

//	@ManyToOne
//	private Department department;

	@ElementCollection
	@CollectionTable(name = "emp_phones")
	@MapKeyColumn(name = "type")
	@MapKeyEnumerated(EnumType.STRING)
	@Column(name = "number")
	private Map<PhoneType, String> phoneNumbers = new TreeMap<>();

	public Set<VacationEntry> getVacation() {
		return vacation;
	}

	public void setVacation(Set<VacationEntry> vacation) {
		this.vacation = vacation;
	}

	public Set<String> getNickNames() {
		return nickNames;
	}

	public void setNickNames(Set<String> nickNames) {
		this.nickNames = nickNames;
	}

	public List<SerializableObject> getObjectSet() {
		return objectSet;
	}

	public void setObjectSet(List<SerializableObject> objectSet) {
		this.objectSet = objectSet;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public Map<PhoneType, String> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(Map<PhoneType, String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

//	public Department getDepartment() {
//		return department;
//	}
//
//	public void setDepartment(Department department) {
//		this.department = department;
//	}
}
