package ua.tifoha;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Phone {
    @Id
    private long id;
    private String number;
    private String type;
    
    @ManyToMany(mappedBy = "contactInfo.phones")
    private List<Employee> employee;

//    @ManyToMany(mappedBy = "contactInfo.phones")
//    private List<Customer> customers;

    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getNumber() {
        return number;
    }
    
    public void setNumber(String phoneNo) {
        this.number = phoneNo;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String phoneType) {
        this.type = phoneType;
    }
    
//    public List<Employee> getEmployee() {
//        return employee;
//    }
//
//    public void setEmployee(List<Employee> employee) {
//        this.employee = employee;
//    }

//    public List<Customer> getCustomers() {
//        return customers;
//    }
//
//    public void setCustomers(List<Customer> customers) {
//        this.customers = customers;
//    }

    public String toString() {
        return "Phone id: " + getId() + 
               ", no: " + getNumber() +
               ", type: " + getType();
    }
}
