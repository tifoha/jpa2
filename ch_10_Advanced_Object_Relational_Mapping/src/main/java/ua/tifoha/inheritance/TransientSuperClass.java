package ua.tifoha.inheritance;

/**
 * Created by Vitaliy Sereda on 26.05.17.
 */
public class TransientSuperClass {
	protected String transientField;

	public TransientSuperClass(String transientField) {
		this.transientField = transientField;
	}

	public String getTransientField() {
		return transientField;
	}

	public void setTransientField(String transientField) {
		this.transientField = transientField;
	}
}
