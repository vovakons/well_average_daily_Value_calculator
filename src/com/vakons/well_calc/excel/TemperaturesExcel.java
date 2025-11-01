package com.vakons.well_calc.excel;

import com.bergen.exel_orm.annotations.ExelORMCell;
import com.bergen.exel_orm.annotations.ExelORMFile;
import com.bergen.exel_orm.annotations.ExelORMInfinitiveList;
import com.bergen.exel_orm.annotations.ExelORMSheet;
import com.vakons.well_calc.utils.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExelORMFile
public class TemperaturesExcel {
    @ExelORMSheet(name = "Лист1")
    public Sheet inputSheet;
    private Map<Long, Line> dataMap = new HashMap<>();
    private Line defaultData = new Line();

    public static class Sheet {
        @ExelORMInfinitiveList(from = "A2", dx = 0, dy = 1)
        public List<Line> lines;
    }

    public static class Line {
        @ExelORMCell(pos = "A1") public String date;
        @ExelORMCell(pos = "B1") public String temperature;
        @ExelORMCell(pos = "C1") public String showHeight;
    }

    public void prepareMap() {
        dataMap = new HashMap<>();
        for(var line : inputSheet.lines) {
            var date = DateUtils.parseUnixTimeFromExcelDate(line.date);
            dataMap.put(date, line);
        }
        defaultData = new Line();
        defaultData.date = "01.10.2022";
        defaultData.temperature = "0";
        defaultData.showHeight = "0";
    }

    public Line get(long date) {
        return dataMap.getOrDefault(date, defaultData);
    }
}
