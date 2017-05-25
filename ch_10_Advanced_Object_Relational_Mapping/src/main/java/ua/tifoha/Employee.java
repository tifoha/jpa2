package ua.tifoha;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
public class Employee {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private long salary;
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Embedded
    private ContactInfo contactInfo = new ContactInfo();
//    @OneToOne
//    private Address address;
    
//    @OneToMany (mappedBy = "employee")
//    private Collection<Phone> phones = new ArrayList<Phone>();

//    public void setPhones(Collection<Phone> phones) {
//        this.phones = phones;
//    }

    @ManyToOne(cascade = CascadeType.ALL)
    private Department department;
    
    @ManyToOne
    private Employee manager;
    
    @OneToMany(mappedBy="manager")
    private Collection<Employee> directs = new ArrayList<Employee>();
    
//    @ManyToMany(mappedBy="employees")
//    private Collection<Project> projects = new ArrayList<Project>();

    public int getId() {
        return id;
    }
    
    public void setId(int empNo) {
        this.id = empNo;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
//    public Collection<Phone> getPhones() {
//        return phones;
//    }
//
//    public void addPhone(String type, Phone phone) {
//        if (!getPhones().contains(phone)) {
//            getPhones().add(phone);
//            if (phone.getEmployee() != null) {
//                phone.getEmployee().getPhones().remove(phone);
//            }
//            phone.setEmployee(this);
//        }
//    }
    
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
//        if (this.department != null) {
//            this.department.getEmployees().remove(this);
//        }
        this.department = department;
//        this.department.getEmployees().add(this);
    }
    
    public Collection<Employee> getDirects() {
        return directs;
    }
    
    public void addDirect(Employee employee) {
        if (!getDirects().contains(employee)) {
            getDirects().add(employee);
            if (employee.getManager() != null) {
                employee.getManager().getDirects().remove(employee);
            }
            employee.setManager(this);
        }
    }
    
    public Employee getManager() {
        return manager;
    }
    
    public void setManager(Employee manager) {
        this.manager = manager;
    }

//    public Collection<Project> getProjects() {
//        return projects;
//    }
//
//    public void addProject(Project project) {
//        if (!getProjects().contains(project)) {
//            getProjects().add(project);
//        }
//        if (!project.getEmployees().contains(this)) {
//            project.getEmployees().add(this);
//        }
//    }
    
//    public Address getAddress() {
//        return address;
//    }
//
//    public void setAddress(Address address) {
//        this.address = address;
//    }
    
    public String toString() {
        return "Employee " + getId() + 
               ": name: " + getName()
//               ", salary: " + getSalary() +
//               ", phones: " + getPhones() +
//               ", managerNo: " + ((getManager() == null) ? null : getManager().getId()) +
//               ", deptNo: " + ((getDepartment() == null) ? null : getDepartment().getId())
                ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;

        Employee employee = (Employee) o;

        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
