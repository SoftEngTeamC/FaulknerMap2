package model;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

public class HospitalProfessional {
    private Long id;

    private String name;
    private Node office;

    public HospitalProfessional() {
        // Left empty for hibernate
    }

    public HospitalProfessional(String name, Node office) {
        this.name = name;
        this.office = office;
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

    @Column(name = "PROF_NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name = "PROF_OFFICES")
    public Node getOffice() {
        return office;
    }

    public void setOffice(Node office) {
        this.office = office;
    }
}
