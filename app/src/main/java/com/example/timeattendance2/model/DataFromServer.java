package com.example.timeattendance2.model;

import java.util.ArrayList;

public class DataFromServer {
    String units;
    float totals;

    public DataFromServer(String units, float totals) {
        this.units = units;
        this.totals = totals;
    }

    public float getTotals(){
        return totals;
    }

    public void setTotals(float totals){
        this.totals = totals;
    }

    public String getUnits(){
        return units;
    }

    public void setUnits(String units){
        this.units = units;
    }

    public static ArrayList<DataFromServer> getSampleUnitData(int size){
        ArrayList<DataFromServer> units = new ArrayList<>();
        for (int i = 0 ; i<size; i++){
            units.add(new DataFromServer("Unit "+(i+1),(float)Math.random()*100));
        }
        return units;
    }
}
