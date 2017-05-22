package ua.tifoha;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Basic;
import java.io.Serializable;

/**
 * Created by Vitaliy Sereda on 21.04.17.
 */
//@Entity
@Access(AccessType.FIELD)
public class EntityWithoutId implements Serializable {
	@Basic
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
