package service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pathfinding.Map;

public class AlgorithmSingletonTest {
    //AlgorithmSingleton testInstance = new AlgorithmSingleton();
    Map.algorithm currentTestAlgorithm;
    private static AlgorithmSingleton testInstance;
    //Map.algorithm.BFS or Map.algorithm.DFS or Map.algorithm.ASTAR are valid

    @Before
    public void setUp() throws Exception {
        EMFProvider.getInstance().useTest();
        currentTestAlgorithm = Map.algorithm.ASTAR;
        //testInstance  = new AlgorithmSingleton();
    }

    @After
    public void tearDown() throws Exception {
    }

    // Tests - - - - - -
    @Test
    public void getInstance() throws Exception {
        try{
            AlgorithmSingleton.getInstance();
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void getCurrentAlgorithm() throws Exception {
        try{
            AlgorithmSingleton.getInstance().setCurrentAlgorithm(Map.algorithm.ASTAR);
            Map.algorithm retAlg = AlgorithmSingleton.getInstance().getCurrentAlgorithm();
            Map.algorithm compAlg = Map.algorithm.ASTAR;
            Assert.assertEquals(retAlg, compAlg);
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }

    @Test
    public void setCurrentAlgorithm() throws Exception {
        try{
            testInstance.getInstance().setCurrentAlgorithm(Map.algorithm.BFS);
            Map.algorithm retAlg2 = testInstance.getInstance().getCurrentAlgorithm();
            Map.algorithm compAlg2 = Map.algorithm.BFS;
            Assert.assertEquals(retAlg2, compAlg2);
        }catch(Exception E){
            Assert.fail("Exception " + E);
            E.printStackTrace();
        }
    }
}