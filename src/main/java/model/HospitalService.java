package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SERVICES")
public class HospitalService {
    private long id;

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
    @JoinTable(name = "SERVICE_LOCATION",
            joinColumns = @JoinColumn(name = "SERVICE_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "NODE_ID", referencedColumnName = "ID"))
    public List<Node> getLocations() {
        return locations;
    }

    public void setLocations(List<Node> locations) {
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
