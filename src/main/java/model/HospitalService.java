package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SERVICES")
public class HospitalService {
    @Id
    @Column(name="ID")
    private long id;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
            name="SERVICES_NODES",
            joinColumns=@JoinColumn(name="SERVICES_ID", referencedColumnName="ID"),
            inverseJoinColumns=@JoinColumn(name="NODES_ID", referencedColumnName="ID"))
    private List<Node> locations;

    private String name;

    public HospitalService() {
        // This is left empty for hibernate
    }

    public HospitalService(List<Node> locations, String name) {
        this.locations = locations;
        this.name = name;
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToMany
    @JoinColumn(name = "SERVICE_NODES")
    public List<Node> getLocation() {
        return locations;
    }

    public void setLocation(List<Node> location) {
        this.locations = locations;
    }

    @Column(name = "SERVICE_NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
