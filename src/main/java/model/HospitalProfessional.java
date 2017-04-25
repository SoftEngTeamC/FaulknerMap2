package model;


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.List;

@Entity
@Indexed
@Table(name = "PROFESSIONALS")
public class HospitalProfessional implements Navigable {

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

    @Field
    @Column(name = "PROF_TITLE")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Field
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

    @Transient
    @Override
    public Node getNode() {
        // TODO: Make this robust
        return offices.get(0);
    }

    @Override
    public String toString() {
        return name + (title.equals("") ? "" : " (" + title + ")");
    }
}
