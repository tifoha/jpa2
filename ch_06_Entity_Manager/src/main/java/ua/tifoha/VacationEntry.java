package ua.tifoha;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import java.time.LocalDate;

/**
 * Created by Vitaliy Sereda on 05.05.17.
 */
@Embeddable
public class VacationEntry {
	private LocalDate startDate;

	@Column (unique = false)
	private long dayCount;

	public VacationEntry() {
	}

	public VacationEntry(LocalDate startDate, long dayCount) {
		this.startDate = startDate;
		this.dayCount = dayCount;
	}

	@Temporal (TemporalType.DATE)
	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public long getDayCount() {
		return dayCount;
	}

	public void setDayCount(long dayCount) {
		this.dayCount = dayCount;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder()
				.append(startDate)
				.append("+")
				.append(dayCount);
		return sb.toString();
	}
}
