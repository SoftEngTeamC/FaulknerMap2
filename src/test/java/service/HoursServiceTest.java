package service;

import model.Hours;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

/**
 * Created by sam on 4/28/17.
 */
public class HoursServiceTest {
    HoursService hoursService = new HoursService();
    Hours testHours;
    String testName;
    Date testMS, testME, testES, testEE;

    @Before
    public void setUp() throws Exception {
        EMFProvider.getInstance().useTest();
    }

    @Test
    public void find() throws Exception {
        try{
            // System.out.print();
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

}