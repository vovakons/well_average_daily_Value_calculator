package com.vakons.well_calc;

import com.bergen.exel_orm.exel.ExcelSheetWriter;
import com.bergen.exel_orm.exel.ExcelWriter;
import com.bergen.exel_orm.orm.ExelORM;
import com.vakons.well_calc.excel.ExcelModel2023;
import com.vakons.well_calc.excel.ExcelModelXls2024;
import com.vakons.well_calc.excel.TemperaturesExcel;
import com.vakons.well_calc.model.WellMeasurement;
import com.vakons.well_calc.model.WellModel;
import com.vakons.well_calc.model.WellsModel;
import com.vakons.well_calc.utils.DateUtils;

import java.util.*;

public class TablesMerger {
    public static final int[] YEARS = { 2021, 2022, 2023, 2024, 2025 };
    public static final int[] DEPTHS = { 0, 1, 2, 3, 5, 7, 10 };
    private WellsModel wells = new WellsModel();

    public void parseWellDataFormat2024(String id, String filename) {
        System.out.println("===== " + filename + " =====");
        var orm = new ExelORM();
        var excel = orm.read(ExcelModelXls2024.class, filename);
        excel.prepareRaw();
        var model = wells.get(id);
        for(var line : excel.sheet.lines) {
            model.objectName = line.target;
            model.braidId = line.endKosaNumber;
            var measurement = new WellMeasurement();
            measurement.date = DateUtils.parseUnixTimeFromExcelDate(line.date);
            measurement.year = DateUtils.parseYearFromUnix(measurement.date);
            measurement.setDepthData(0, line.depth0m);
            measurement.setDepthData(1, line.depth1m);
            measurement.setDepthData(2, line.depth2m);
            measurement.setDepthData(3, line.depth3m);
            measurement.setDepthData(5, line.depth5m);
            measurement.setDepthData(7, line.depth7m);
            measurement.setDepthData(10, line.depth10m);
            measurement.zeroIsotermLevel = parseFloat(line.zeroIsotermDepth);
            measurement.integralTemperature = line.integralTemperature;
            model.measurements.add(measurement);
        }
    }

    public void parseWellDataFormat2023(String id, String filename) {
        System.out.println("===== " + filename + " =====");
        var orm = new ExelORM();
        var excel = orm.read(ExcelModel2023.class, filename);
        excel.prepareRaw();
        var model = wells.get(id);
        for(var line : excel.sheet.lines) {
            model.objectName = line.target;
            model.braidId = line.endKosaNumber;
            var measurement = new WellMeasurement();
            measurement.date = DateUtils.parseUnixTimeFromExcelDate(line.date);
            measurement.year = DateUtils.parseYearFromUnix(measurement.date);
            measurement.setDepthData(0, line.depth0m);
            measurement.setDepthData(1, line.depth1m);
            measurement.setDepthData(2, line.depth2m);
            measurement.setDepthData(3, line.depth3m);
            measurement.setDepthData(5, line.depth5m);
            measurement.setDepthData(7, line.depth7m);
            measurement.setDepthData(10, line.depth10m);
            measurement.zeroIsotermLevel = 0;
            measurement.integralTemperature = 0;
            model.measurements.add(measurement);
        }
    }

    public void printDebug() {
        System.out.println("===== Debug =====");
        for(var model : wells.getModels()) {
            System.out.println(model.objectId + " (" + model.objectName + "): " + model.measurements.size() + " measurements");
        }
    }

    public void calculateAverages() {
        System.out.println("===== Calculate averages =====");
        for(var model : wells.getModels()) {
            calculateAverageByDay(model);
        }
    }

    private void calculateAverageByDay(WellModel model) {
        Map<Long, List<WellMeasurement>> dayMeasurements = new HashMap<>();
        for(var measurement : model.measurements) {
            if (!dayMeasurements.containsKey(measurement.date)) {
                dayMeasurements.put(measurement.date, new LinkedList<>());
            }
            dayMeasurements.get(measurement.date).add(measurement);
        }
        model.measurements.clear();
        for(var date : dayMeasurements.keySet()) {
            var measurements = dayMeasurements.get(date);
            var measurement = measurements.get(0);
            for(var i = 0; i < measurements.size(); i++) {
                measurement.add(measurements.get(i));
            }
            measurement.divide(measurements.size());
            model.measurements.add(measurement);
        }
        model.measurements.sort(Comparator.comparingLong(a -> a.date));
    }

    public void parseTemperatures(String filename) {
        System.out.println("===== " + filename + " =====");
        var orm = new ExelORM();
        var temperatures = orm.read(TemperaturesExcel.class, filename);
        temperatures.prepareMap();
        for(var model : wells.getModels()) {
            for(var measurement : model.measurements) {
                var tempData = temperatures.get(measurement.date);
                measurement.temperature = parseFloat(tempData.temperature);
                measurement.snowLevelHeight = parseFloat(tempData.showHeight);
            }
        }
    }

    public void printExcels(String outputDir) {
        System.out.println("===== Print output excels =====");
        System.out.println("Output dir: " + outputDir);
        for(var model : wells.getModels()) {
            printExcelForModel(model, outputDir);
        }
    }

    private void printExcelForModel(WellModel model, String outputDir) {
        System.out.print("Model " + model.objectId + "...");
        try {
            var writer = new ExcelWriter(outputDir + model.objectId + ".xlsx");
            for(int year : YEARS) {
                var yearMeasurements = model.collectByYear(year);
                if(yearMeasurements.isEmpty()) {
                    continue;
                }
                var sheet = writer.sheet(Integer.toString(year));
                writeYearMeasurementsToSheet(model, yearMeasurements, sheet);
            }
            writer.save();
            System.out.println("SUCCESS");
        } catch (Throwable error) {
            System.out.println("ERROR");
            error.printStackTrace(System.out);
        }
    }

    private void writeYearMeasurementsToSheet(WellModel model, List<WellMeasurement> measurements, ExcelSheetWriter sheet) {
        //print header
        sheet.set("A1", "Дата замера");
        sheet.set("B1", "№ Объекта");
        sheet.set("C1", "№ Зав.№ косы");
        sheet.set("D1", "0,0м");
        sheet.set("E1", "1,0м");
        sheet.set("F1", "2,0м");
        sheet.set("G1", "3,0м");
        sheet.set("H1", "5,0м");
        sheet.set("I1", "7,0м");
        sheet.set("J1", "10,0м");
        sheet.set("K1", "Глубина нулевой изотермы, м");
        sheet.set("L1", "Интегральная температура");
        sheet.set("M1", "Температура воздуха");
        sheet.set("N1", "Уровень снежного покрова, м");
        // print lines
        int line = 2;
        for(var measurement : measurements) {
            sheet.set("A" + line, DateUtils.formatDDMMYYYY(measurement.date));
            sheet.set("B" + line, model.objectName);
            sheet.set("C" + line, model.braidId.replace(".0", ""));
            sheet.set("D" + line, floatToString(measurement.getDepthValue(0)));
            sheet.set("E" + line, floatToString(measurement.getDepthValue(1)));
            sheet.set("F" + line, floatToString(measurement.getDepthValue(2)));
            sheet.set("G" + line, floatToString(measurement.getDepthValue(3)));
            sheet.set("H" + line, floatToString(measurement.getDepthValue(5)));
            sheet.set("I" + line, floatToString(measurement.getDepthValue(7)));
            sheet.set("J" + line, floatToString(measurement.getDepthValue(10)));
            sheet.set("K" + line, floatToString(measurement.zeroIsotermLevel));
            sheet.set("L" + line, floatToString(measurement.integralTemperature));
            sheet.set("M" + line, floatToString(measurement.temperature));
            sheet.set("N" + line, floatToString(measurement.snowLevelHeight));
            line++;
        }
    }

    public static void main(String[] args) {
        var merger = new TablesMerger();
        merger.parseWellDataFormat2024("well_5" , "data/well_5_2024.xlsx");
        merger.parseWellDataFormat2024("well_6" , "data/well_6_2024.xlsx");
        merger.parseWellDataFormat2023("well_5" , "data/well_5_2023.xlsx");
        merger.parseWellDataFormat2023("well_6" , "data/well_6_2023.xlsx");
        merger.printDebug();
        merger.calculateAverages();
        merger.printDebug();
        merger.parseTemperatures("data/temperatures.xlsx");
        merger.printExcels("data/out/");
    }

    private static float parseFloat(String text) {
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
    private static String floatToString(float value) {
        return Float.toString(value).replace(".", ",");
    }
}
