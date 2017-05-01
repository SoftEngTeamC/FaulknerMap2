package model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "EDGES")
public class Edge {
    private Long id;

    private Node start, end;
    private double length;
    private boolean disabled = false;

    public Edge() {
        // This is left empty for hibernate
    }

    public Edge(Node start, Node end, double length) {
        this.start = start;
        this.end = end;
        this.length = length;
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
    @JoinColumn(name = "EDGE_STARTS")
    public Node getStart() {
        return start;
    }

    public void setStart(Node start) {
        this.start = start;
    }

    @ManyToOne
    @JoinColumn(name = "EDGE_ENDS")
    public Node getEnd() {
        return end;
    }

    public void setEnd(Node end) {
        this.end = end;
    }

    @Column(name = "EDGE_LENGTH")
    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    @Column(name = "EDGE_ISDISABLED")
    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    @Override
    @Transient
    public String toString() {
        return getStart().toString() + " <-> " + getEnd().toString();
    }
}
