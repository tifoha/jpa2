package ua.tifoha;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by Vitaliy Sereda on 26.05.17.
 */
@Entity
@IdClass(Marriage.MarriageId.class)
public class Marriage {
	public static class MarriageId implements Serializable{
		private Husband husband;

		private Wife wife;

		public MarriageId() {
		}

		public MarriageId(Husband husband, Wife wife) {
			this.husband = husband;
			this.wife = wife;
		}

		public Husband getHusband() {
			return husband;
		}

		public Wife getWife() {
			return wife;
		}
	}

	@Id
	@ManyToOne
	private Husband husband;

	@Id
	@ManyToOne
	private Wife wife;

	private LocalDate date = LocalDate.now();

	public Marriage() {
	}

	public Marriage(Husband husband, Wife wife) {
		this.husband = husband;
		this.wife = wife;
	}
}
