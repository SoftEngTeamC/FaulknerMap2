package model;


public interface Navigable {
    // Use @Transient to ignore getNode() for JPA
    Node getNode();
    String toString();
    String getInfo();
}
