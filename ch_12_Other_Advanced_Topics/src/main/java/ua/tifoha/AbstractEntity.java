package ua.tifoha;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;
import java.io.Serializable;

/**
 * Created by Vitaliy Sereda on 27.04.17.
 */
@MappedSuperclass
public class AbstractEntity implements Serializable{
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@TableGenerator(name = "mainGenerator",
			pkColumnName = "entity", valueColumnName = "lastIndex", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "mainGenerator")
	protected Long id;
	//	@Lob
//	@Basic (fetch = FetchType.LAZY)
	protected String name;

	public AbstractEntity() {
	}

	public AbstractEntity(String name) {
		this.name = name;
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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
		sb.append("{");
		sb.append("id=").append(id);
		sb.append(", name='").append(name).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
