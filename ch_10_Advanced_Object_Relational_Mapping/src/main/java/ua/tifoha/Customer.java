package ua.tifoha;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;

@Entity
@IdClass(CustomerId.class)
public class Customer {

    @Id
    private int id;
    @Id
    private String countryCode;
    private String name;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "address.zip", column = @Column(name = "postalCode")))
    @AssociationOverrides({
            @AssociationOverride(name = "primaryPhone",
                    joinColumns = @JoinColumn(name = "mainPhone")),
            @AssociationOverride(name = "phones",
                    joinTable = @JoinTable(name = "customer_phones",
                            joinColumns = {@JoinColumn(name = "contry_code"), @JoinColumn(name = "id")},
                            inverseJoinColumns = @JoinColumn(name = "phone_id"))
 )
 })
    private ContactInfo contactInfo = new ContactInfo();

    @ManyToOne
    private Employee manager;

    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Employee getManager() {
        return manager;
    }
    
    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public String toString() {
        return "Employee " + getId() + 
               ": name: " + getName()
                ;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setId(CustomerId id) {
        this.id = id.getId();
        this.countryCode = id.getCountryCode();
    }
}
