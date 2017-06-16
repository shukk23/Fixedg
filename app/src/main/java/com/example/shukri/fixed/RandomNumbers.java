package com.example.shukri.fixed;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by shukri on 23-May-17.
 */

public class RandomNumbers
{
    int rndOne,rndTwo,rndThree;

    public RandomNumbers(){

    }

    public void setRndOne(int rndOne) {
        this.rndOne = rndOne;
    }

    public int getRndOne() {
        return rndOne;
    }

    public void setRndTwo(int rndTwo) {
        this.rndTwo = rndTwo;
    }

    public int getRndTwo() {
        return rndTwo;
    }

    public int getRndThree() {
        return rndThree;
    }

    public void setRndThree(int rndThree) {
        this.rndThree = rndThree;
    }
    public void generateNumbers(){
        ArrayList<Integer> nr=new ArrayList<>();
        nr.add(0);
        nr.add(1);
        nr.add(2);
        nr.add(3);
        nr.add(4);
        nr.add(5);
        nr.add(6);
        nr.add(7);
        nr.add(8);
        nr.add(9);

        Random r = new Random();

        int rndPos1 = r.nextInt(nr.size());
        setRndOne(nr.remove(rndPos1));

        int rndPos2 = r.nextInt(nr.size());
        setRndTwo(nr.remove(rndPos2));

        int rndPos3 = r.nextInt(nr.size());
        setRndThree(nr.remove(rndPos3));
    }
}
