package ua.tifoha;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.AccessType.PROPERTY;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Vitaliy Sereda on 07.04.17.
 */
@Entity
//@Access(FIELD)
//@Table(name = "jpa2_2.emp")
@Table(name = "emp")
public class Employee {
	@Id
	public Long id;

	public String name;


	private BigDecimal wage = BigDecimal.ZERO;

	//	@ManyToOne(fetch = FetchType.LAZY)
//	private Firm firm;
	@Embedded
	@AttributeOverrides(@AttributeOverride(name = "state", column = @Column(name = "oblast", length = 50, unique = true)))
	private Address address;

	@Basic(fetch = FetchType.LAZY)
	private EmbeddedField embeddedField;

	@ManyToMany
	@JoinTable(name = "emp_proj", joinColumns = {@JoinColumn(name = "emp")}, inverseJoinColumns = @JoinColumn(name = "proj"))
	private Set<Project> projects = new LinkedHashSet<>();

	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	public static class EmbeddedField implements Serializable {

		private final String value;

		public EmbeddedField(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("EF{");
			sb.append(value);
			sb.append('}');
			return sb.toString();
		}

	}
	public Employee() {
	}

	public Employee(long id) {
		this.id = id;
	}

//	@Id
//	@Access(PROPERTY)
	public Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	@Access(PROPERTY)
	public BigDecimal getSalary() {
		return wage;
	}

	public void setSalary(BigDecimal salary) {
		this.wage = salary;
	}

	public EmbeddedField getEmbeddedField() {
		return embeddedField;
	}

	public void setEmbeddedField(EmbeddedField embeddedField) {
		this.embeddedField = embeddedField;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Employee{");
		sb.append("id=").append(id);
		sb.append(", name='").append(name).append('\'');
		sb.append(", salary=").append(wage);
		sb.append(", EF=").append(embeddedField);
//		sb.append(", Firm=").append(firm);
		sb.append('}');
		return sb.toString();
	}

//	public Firm getFirm() {
//		return firm;
//	}
//
//	public void setFirm(Firm firm) {
//		this.firm = firm;
//	}
}
