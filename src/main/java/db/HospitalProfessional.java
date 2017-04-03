package db;

import java.util.UUID;

/**
 * Created by wangyaofeng on 3/31/17.
 */
public class HospitalProfessional {

    private String name;
    private String title;
    private String location;
    private UUID id;

    public HospitalProfessional(String name, String title, String location){
        this.name = name;
        this.title = title;
        this.location = location;
        id = UUID.randomUUID(); //set id to random
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public UUID getId() { return id; }
}
