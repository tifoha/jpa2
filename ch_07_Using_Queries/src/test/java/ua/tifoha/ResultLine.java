package ua.tifoha;

/**
 * Created by Vitaliy Sereda on 17.05.17.
 */
public class ResultLine {
	private final String empName;
	private final String deptName;

	public ResultLine(String empName, String deptName) {
		this.empName = empName;
		this.deptName = deptName;
	}

	public String getEmpName() {
		return empName;
	}

	public String getDeptName() {
		return deptName;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("(")
				.append(empName)
				.append(':')
				.append(deptName)
				.append(')');
		return sb.toString();
	}
}
