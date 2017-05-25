package ua.tifoha;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by Vitaliy Sereda on 25.05.17.
 */
@Entity
@IdClass(CompositeIdRelations.IdClass.class)
public class CompositeIdRelations implements Serializable{
	@Id
	@GeneratedValue
	private int id1;
	@Id
	@GeneratedValue
	private int id2;

	private String name;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "p_id1", referencedColumnName = "ID1"),
			@JoinColumn(name = "p_id2", referencedColumnName = "ID2"),
	})
	private CompositeIdRelations parent;


	public static class IdClass implements Serializable{
		private int id1;
		private int id2;

		public IdClass() {
		}

		public IdClass(int id1, int id2) {
			this.id1 = id1;
			this.id2 = id2;
		}
	}

	public int getId1() {
		return id1;
	}

	public void setId1(int id1) {
		this.id1 = id1;
	}

	public int getId2() {
		return id2;
	}

	public void setId2(int id2) {
		this.id2 = id2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CompositeIdRelations getParent() {
		return parent;
	}

	public void setParent(CompositeIdRelations parent) {
		this.parent = parent;
	}
}
