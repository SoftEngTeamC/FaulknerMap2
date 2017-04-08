package model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PROFESSIONALS")
public class HospitalProfessional {
    private Long id;

    private String name;

    @ManyToMany(cascade=CascadeType.ALL)
    @JoinTable(
            name="SERVICES_NODES",
            joinColumns=@JoinColumn(name="PROFESSIONALS_ID", referencedColumnName="ID"),
            inverseJoinColumns=@JoinColumn(name="NODES_ID", referencedColumnName="ID"))
    private List<Node> offices;

    private String title;

    public HospitalProfessional() {
        // Left empty for hibernate
    }

    public HospitalProfessional(String name, String title, List<Node> offices) {
        this.name = name;
        this.offices = offices;
        this.title = title;
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

    @Column(name = "PROF_TITLE")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "PROF_NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany
    @JoinColumn(name = "PROF_OFFICES")
    public List<Node> getOffice() {
        return offices;
    }

    public void setOffice(List<Node> offices) {
        this.offices = offices;
    }
}
