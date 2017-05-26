package ua.tifoha.inheritance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Vitaliy Sereda on 26.05.17.
 */
@Entity
public class EntityFromTransientSuperclass extends TransientSuperClass {
	@Id
	@GeneratedValue
	private int id;

	public EntityFromTransientSuperclass() {
		super(null);
	}

	public int getId() {
		return id;
	}
}
