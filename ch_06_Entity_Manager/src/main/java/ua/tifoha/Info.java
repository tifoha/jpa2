package ua.tifoha;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

/**
 * Created by Vitaliy Sereda on 06.05.17.
 */
@Embeddable
public class Info {
	private String value;

	public Info() {
	}

	public Info(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
