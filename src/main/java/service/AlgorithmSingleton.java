package service;

import pathfinding.Map;

public class AlgorithmSingleton {

    private static AlgorithmSingleton ourInstance = new AlgorithmSingleton();

    public static AlgorithmSingleton getInstance() {
        return ourInstance;
    }

    // initially we are using A*
    private Map.algorithm currentAlgorithm = Map.algorithm.ASTAR;

    private AlgorithmSingleton() {

    }

    public Map.algorithm getCurrentAlgorithm(){
        return this.currentAlgorithm;
    }

    public void setCurrentAlgorithm(Map.algorithm newAlgorithm){
        this.currentAlgorithm = newAlgorithm;
    }

}
