package com.vakons.well_calc.model;

import java.util.LinkedList;
import java.util.List;

public class WellModel {
    public String objectName;
    public String objectId;
    public String braidId;
    public List<WellMeasurement> measurements = new LinkedList<>();

    public List<WellMeasurement> collectByYear(int year) {
        List<WellMeasurement> results = new LinkedList<>();
        for(var measurement : measurements) {
            if(measurement.year == year) {
                results.add(measurement);
            }
        }
        return results;
    }
}
