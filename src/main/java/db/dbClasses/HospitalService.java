package db.dbClasses;


import java.util.UUID;

public class HospitalService {

  private String name;
  private String location;
  private UUID id;
  private UUID nodeId;

  public HospitalService(String name, String location){
    this.name = name;
    this.location = location;
    id = UUID.randomUUID(); //set id to random
    nodeId = UUID.randomUUID(); //TODO: change once populated Nodes
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public UUID getId() { return id; }

  public void setId(UUID id){
    this.id = id;
  }

  public UUID getNodeId() {
    return nodeId; }

  public void setNodeId(UUID nodeId) { this.nodeId = nodeId; }

}
