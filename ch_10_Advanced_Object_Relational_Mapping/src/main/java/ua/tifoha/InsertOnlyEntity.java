package ua.tifoha;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

/**
 * Created by Vitaliy Sereda on 25.05.17.
 */
@Entity
public class InsertOnlyEntity {
	@Id
	@GeneratedValue
	private int id;

	@ManyToOne
	@JoinTable(name = "ioe_p")
	private Phone phone;

	public InsertOnlyEntity() {
	}

	public InsertOnlyEntity(String value) {
		this.value = value;
	}

	@Column (updatable = false)
	private String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
