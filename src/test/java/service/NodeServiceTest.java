package service;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NodeServiceTest {
    @Before
    public void setUp() throws Exception {
        EMFProvider.getInstance().useTest();
        NodeService nodeService = new NodeService();
        // TODO: Fill this bitch with sum fake ass data (the nodeService
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void find() throws Exception {
    }
}
