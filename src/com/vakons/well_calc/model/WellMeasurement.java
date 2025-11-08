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
        public float pValue;
        public float averge;
        public float median;
    }

    public WellDepthData getDepth(int depth) {
        for(var depthData : depths) {
            if(depthData.depth == depth) {
                return depthData;
            }
        }
        var depthData = new WellDepthData();
        depthData.depth = depth;
        depths.add(depthData);
        return depthData;
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
}
