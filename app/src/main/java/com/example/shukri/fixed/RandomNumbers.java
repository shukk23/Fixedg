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

        //krijojme nje varg i cili pranon vlera integer
        ArrayList<Integer> nr=new ArrayList<>();

        //vendosen numrat prej 0-9
        for(int i = 0;i<=9;i++){
            nr.add(i);

        }

        //perdorim metoden Random() per te bere zgjedhje te rastesishme
        Random r = new Random();

        //zgjidhet numri i pare i rastesishem nga vargu
        int rndPos1 = r.nextInt(nr.size());

        //numri i pare i zgjedhur hiqet nga vargu
        setRndOne(nr.remove(rndPos1));

        //zgjidhet numri i dyte i rastesishem nga vargu
        int rndPos2 = r.nextInt(nr.size());

        //numri i dyte i zgjedhur hiqet nga vargu
        setRndTwo(nr.remove(rndPos2));

        //zgjidhet numri i trete i rastesishem nga vargu
        int rndPos3 = r.nextInt(nr.size());

        setRndThree(nr.remove(rndPos3));
    }
}
