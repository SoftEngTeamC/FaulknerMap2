package db;


import java.util.UUID;

public class HospitalService {

  private String name;
  private String location;
  private UUID id;

  public HospitalService(String name, String location){
    this.name = name;
    this.location = location;
    id = UUID.randomUUID(); //set id to random
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

}
