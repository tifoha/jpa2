package ua.tifoha;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.LinkedHashMap;
import java.util.Map;

import ua.tifoha.Department.EmbaddedName;

/**
 * Created by Vitaliy Sereda on 08.05.17.
 */
@Entity
public class Firm extends AbstractEntity {
	@OneToMany (mappedBy = "firm")
	@AttributeOverrides({
			@AttributeOverride(name = "key.firstName",  column = @Column(name = "name")),
			@AttributeOverride(name = "key.lastName1",  column = @Column(name = "lastName")
			)
	})
//	@MapKey(name = "id")
	private Map<EmbaddedName, Department> departments = new LinkedHashMap<>();

	public Map<EmbaddedName, Department> getDepartments() {
		return departments;
	}

	public void setDepartments(Map<EmbaddedName, Department> departments) {
		this.departments = departments;
	}
}
