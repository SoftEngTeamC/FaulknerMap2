package model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.awt.*;
import java.util.List;

@Entity
@Indexed
@Table( name = "NODES" )
public class Node {
    private Long id;

    private Coordinate location;
    private String name;
    private List<Tag> tags;


    public Node() {
        // This is kept empty for hibernate
    }

    public Node(String name, Coordinate location) {
        this.location = location;
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

    @ManyToOne
    @JoinColumn(name = "NODE_LOCATIONS")
    public Coordinate getLocation() {
        return location;
    }

    public void setLocation(Coordinate location) {
        this.location = location;
    }

    @Field
    @Column(name = "NODE_NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany
    @JoinTable(name = "NODE_TAG",
            joinColumns = @JoinColumn(name = "NODE_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "TAG_ID", referencedColumnName = "ID"))
    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return name + " " + "(" + id.toString() + ")" + " " + location.toString();
    }

    @Transient
    public Point getPoint() {
        return location.getPoint();
    }
}
