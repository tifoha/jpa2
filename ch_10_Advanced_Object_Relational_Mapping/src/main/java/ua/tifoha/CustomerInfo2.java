package ua.tifoha;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity
@IdClass (CustomerInfoId.class)
public class CustomerInfo2 implements Serializable{

//    @Id
//    private Integer num;
//
    @Id
    private String name;

    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(referencedColumnName = "ID", name = "numb"),
            @JoinColumn(referencedColumnName = "COUNTRYCODE", name = "country")
    })
    private Customer customer;
    private String info;

//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public String getCountryCode() {
//        return countryCode;
//    }
//
//    public void setCountryCode(String countryCode) {
//        this.countryCode = countryCode;
//    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
