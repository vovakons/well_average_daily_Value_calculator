package com.vakons.well_calc.excel;

import com.bergen.exel_orm.annotations.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ExelORMFile
public class ExcelModel2023 {
    @ExelORMSheetsMap public Map<String, DataSheet> sheets;
    public DataSheet sheet;

    public void prepareRaw() {
        if(!sheets.isEmpty()) {
            for(var shName : sheets.keySet()) {
                if(Objects.equals(shName, "average")) {
                    continue;
                }
                sheet = sheets.get(shName);
                var num = 1;
                for(var line : sheet.lines) {
                    line.lineNum = num++;
                    line.parseRaw();
                }
                return;
            }
        }
    }

    public static class DataSheet {
        @ExelORMInfinitiveList(from = "A2", dx = 0, dy = 1)
        public List<DataLine> lines = new LinkedList<>();
    }

    public static class DataLine {
        public int lineNum;
        @ExelORMCell(pos = "A1") public String number;
        @ExelORMCell(pos = "B1") public String date;
        @ExelORMCell(pos = "C1") public String time;
        @ExelORMCell(pos = "D1") public String target;
        @ExelORMCell(pos = "E1") public String endKosaNumber; //wtf?
        @ExelORMCell(pos = "F1") public String depth0mRaw;
        @ExelORMCell(pos = "G1") public String depth1mRaw;
        @ExelORMCell(pos = "H1") public String depth2mRaw;
        @ExelORMCell(pos = "I1") public String depth3mRaw;
        @ExelORMCell(pos = "J1") public String depth5mRaw;
        @ExelORMCell(pos = "K1") public String depth7mRaw;
        @ExelORMCell(pos = "L1") public String depth10mRaw;
        public float depth0m;
        public float depth1m;
        public float depth2m;
        public float depth3m;
        public float depth5m;
        public float depth7m;
        public float depth10m;

        public void parseRaw() {
            depth0m = Float.parseFloat(depth0mRaw);
            depth1m = Float.parseFloat(depth1mRaw);
            depth2m = Float.parseFloat(depth2mRaw);
            depth3m = Float.parseFloat(depth3mRaw);
            depth5m = Float.parseFloat(depth5mRaw);
            depth7m = Float.parseFloat(depth7mRaw);
            depth10m = Float.parseFloat(depth10mRaw);
        }

        public void add(DataLine other) {
            depth0m += other.depth0m;
            depth1m += other.depth1m;
            depth2m += other.depth2m;
            depth3m += other.depth3m;
            depth5m += other.depth5m;
            depth7m += other.depth7m;
            depth10m += other.depth10m;
        }

        public void divide(float value) {
            depth0m /= value;
            depth1m /= value;
            depth2m /= value;
            depth3m /= value;
            depth5m /= value;
            depth7m /= value;
            depth10m /= value;
        }
    }
}
