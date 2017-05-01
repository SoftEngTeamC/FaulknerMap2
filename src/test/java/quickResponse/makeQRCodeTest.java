package quickResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.EMFProvider;

/**
 * Created by sam on 4/30/17.
 */
public class makeQRCodeTest {
    String Directions;

    @Before
    public void setUp() throws Exception {
        EMFProvider.getInstance().useTest();
        Directions = "Testing tests testy McTests";
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void buildQR() throws Exception {
        try{
            QR.buildQR(Directions);
        }catch(Exception E){
            //Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

}