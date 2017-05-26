package ua.tifoha.inheritance;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Created by Vitaliy Sereda on 26.05.17.
 */
@Entity
@Inheritance (strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "m_type")
public abstract class Mammal {
	@Id
	@GeneratedValue
	protected int id;

	protected double weight;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
}
