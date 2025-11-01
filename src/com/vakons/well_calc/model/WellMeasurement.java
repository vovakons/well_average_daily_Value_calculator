package com.vakons.well_calc.model;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class WellMeasurement {
    /** Unix time for day start */
    public long date;
    /** Year of measurement */
    public int year;
    /** Milliseconds after day start */
    public long daytime;
    /** Depth measurement data */
    public List<WellDepthData> depths = new LinkedList<>();
    /** Zero isoterm level in meters */
    public float zeroIsotermLevel;
    /** Integral temperature */
    public float integralTemperature;
    /** Temperacture in celcius */
    public float temperature;
    /** Snow level height in centimeters */
    public float snowLevelHeight;

    public static class WellDepthData {
        public int depth;
        public float value;
    }

    public void setDepthData(int depth, float value) {
        for(var depthData : depths) {
            if(depthData.depth == depth) {
                depthData.value = value;
                return;
            }
        }
        //not found depth, create one
        var depthData = new WellDepthData();
        depthData.depth = depth;
        depthData.value = value;
        depths.add(depthData);
    }

    public float getDepthValue(int depth) {
        for(var depthData : depths) {
            if(depthData.depth == depth) {
                return depthData.value;
            }
        }
        return 0;
    }

    public void sortDepths() {
        depths.sort(Comparator.comparingInt(a -> a.depth));
    }

    public void add(WellMeasurement other) {
        for(var depth : depths) {
            setDepthData(depth.depth, depth.value + other.getDepthValue(depth.depth));
        }
        //add not exists in this depth data
        for(var depth : other.depths) {
            if (getDepthValue(depth.depth) == 0) {
                setDepthData(depth.depth, depth.value);
            }
        }
        integralTemperature += other.integralTemperature;
    }

    public void divide(float divider) { //need for calculate average value
        for(var depth : depths) {
            depth.value = depth.value / divider;
        }
        integralTemperature = integralTemperature / divider;
    }
}
