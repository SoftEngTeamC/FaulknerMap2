package db.dbHelpers;

import db.Driver;
import db.dbClasses.HospitalService;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Jack Charbonneau on 4/2/17.
 * Edited by Sam Coache on 4/3/17 - Formatting
 */
public class HospitalServicesHelperTest {
    HospitalServicesHelper hsh = Driver.getHospitalServiceHelper();
    HospitalService hs1 = new HospitalService("My Name", "My location");
    HospitalService hs2 = new HospitalService("My Name", "My location");
    HospitalService hs3 = new HospitalService("My Name", "My location");
    HospitalService hs4 = new HospitalService("My Name", "My location");
    HospitalService hs5 = new HospitalService("My Name", "My location");
    HospitalService hs999 = new HospitalService("My Name", "My location");

    public HospitalServicesHelperTest(){
        Driver.registerDriver(false);
        Driver.runDatabase(false);
        hsh = Driver.getHospitalServiceHelper();
        hsh.addHospitalService(hs1);
        hsh.addHospitalService(hs2);
        hsh.addHospitalService(hs3);
        hsh.addHospitalService(hs4);
        hsh.addHospitalService(hs5);
    }

    @Test
    //test that the number of elements in the table increases
    //by one when an element is added to the table
    public void addTest(){
        ArrayList<HospitalService> initialList = hsh.getHospitalServices(null);
        int initialSize = initialList.size();
        HospitalService temp = new HospitalService("My Name", "My location");
        hsh.addHospitalService(temp);
        ArrayList<HospitalService> newList = hsh.getHospitalServices(null);
        int newSize = newList.size();
        assertEquals((initialSize + 1), newSize);
    }

    @Test
    //test that the number of elements in the table decreases
    //by one when an element is removed from the table
    public void deleteTest(){
        ArrayList<HospitalService> initialList = hsh.getHospitalServices(null);
        int initialSize = initialList.size();
        hsh.deleteHospitalService(hs3);
        ArrayList<HospitalService> newList = hsh.getHospitalServices(null);
        int newSize = newList.size();
        assertEquals((initialSize - 1), newSize);
    }

    @Test
    //test that the element that was added first can be
    //accessed from the table
    public void accessFirst(){
        assertEquals(hs1.getId(),hsh.getHospitalService(hs1.getId()).getId());
    }

    @Test
    //test that the element that was added most recently
    //can be accessed from the table
    public void accessLast(){
        assertEquals(hs1.getId(),hsh.getHospitalService(hs1.getId()).getId());
    }

    @Test
    //test that an elements in the middle of the table
    //can be accessed from the table
    public void accessMiddle(){
        assertEquals(hs1.getId(),hsh.getHospitalService(hs1.getId()).getId());
    }

    @Test
    //test that if an element is search for but does not exist
    //in the database null is returned
    public void accessNonExistant(){
        assertNull(hsh.getHospitalService(hs999.getId()));
    }

    @Test
    //test that a hospital service can have its name edited
    public void changeName(){
        hs2.setName("Johnny Boy");
        assertEquals("Johnny Boy", hs2.getName());
    }

    @Test
    //test that a hospital service can have its location edited
    public void changeLocation(){
        hs2.setLocation("100Institute");
        assertEquals("100Institute", hs2.getLocation());
    }

    @Test
    //test that an element with an edited name can be
    //properly updated in the database
    public void makeNameEdit(){
        hs5.setName("newname");
        hsh.updateHospitalService(hs5);
        assertEquals("newname", hsh.getHospitalService(hs5.getId()).getName());
    }

    @Test
    //test that an element with an edited location can be
    //properly updated in the database
    public void makeLocationEdit(){
        hs5.setLocation("newlocation");
        hsh.updateHospitalService(hs5);
        assertEquals("newlocation", hsh.getHospitalService(hs5.getId()).getLocation());
    }

    @Test
    //test that two elements are not equal even if they
    //have the same name and location
    public void pseudoSame(){
        assertNotEquals(new HospitalService("Jon","Boston"),
                new HospitalService("Jon","Boston"));
    }
}