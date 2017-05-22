package ua.tifoha;

import java.io.Serializable;

/**
 * Created by Vitaliy Sereda on 05.05.17.
 */
public class SerializableObject implements Serializable {
	private String value;

	public SerializableObject() {
		this("");
	}

	public SerializableObject(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SerializableObject)) return false;

		SerializableObject that = (SerializableObject) o;

		return value != null ? value.equals(that.value) : that.value == null;
	}

	@Override
	public int hashCode() {
		return value != null ? value.hashCode() : 0;
	}
}
