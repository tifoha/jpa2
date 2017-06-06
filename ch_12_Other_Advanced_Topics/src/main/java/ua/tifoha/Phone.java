package ua.tifoha;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

@Entity
@EntityListeners({PhoneEntityListener.class})
public class Phone {
    @Id
    private long id;
    private String number;
    private String type;
    
    @ManyToOne
    Employee employee;
    
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
    
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String toString() {
        return "Phone id: " + getId() + 
               ", no: " + getNumber() +
               ", type: " + getType();
    }

    @PrePersist
    private void prePersist() throws Exception{
        System.out.println("Phone.prePersist");
    }

    @PostPersist
    public void postPersist(){
        System.out.println("Phone.postPersist");
    }

    @PreUpdate
    public void preUpdate(){
        System.out.println("Phone.preUpdate");
    }

    @PostUpdate
    public void postUpdate(){
        System.out.println("Phone.postUpdate");
    }

    @PreRemove
    public void preRemove(){
        System.out.println("Phone.preRemove");
    }

    @PostRemove
    public void postRemove(){
        System.out.println("Phone.postRemove");
    }

    @PostLoad
    public void postLoad(){
        System.out.println("Phone.postLoad");
    }


}
