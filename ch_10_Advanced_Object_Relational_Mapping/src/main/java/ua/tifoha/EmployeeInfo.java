package ua.tifoha;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.io.Serializable;

/**
 * Created by Vitaliy Sereda on 25.05.17.
 */
@Entity
public class EmployeeInfo implements Serializable{
	@Id
	private Integer id;

	@MapsId
	@OneToOne
	private Employee employee;

	private String info = "";

	public Employee getEmployee() {
		return employee;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
}
