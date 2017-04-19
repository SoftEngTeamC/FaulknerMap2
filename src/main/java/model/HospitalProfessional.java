package model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PROFESSIONALS")
public class HospitalProfessional {

    private Long id;

    private String name;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "PROFESSIONAL_OFFICE",
            joinColumns = @JoinColumn(name = "PROFESSIONAL_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "OFFICE_ID", referencedColumnName = "ID"))
    public List<Node> getOffices() {
        return offices;
    }

    public void setOffices(List<Node> offices) {
        this.offices = offices;
    }
}
