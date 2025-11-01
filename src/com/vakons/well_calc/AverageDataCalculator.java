package com.vakons.well_calc;

import com.bergen.exel_orm.exel.ExcelSheetWriter;
import com.bergen.exel_orm.exel.ExcelWriter;
import com.vakons.well_calc.excel.ExcelModel2023;
import com.vakons.well_calc.excel.ExcelModelXls2024;

import java.io.IOException;
import java.util.*;

public class AverageDataCalculator {
    private static final String AVG_DATA_SHEET = "Среднесуточные данные";
    private static final String INPUT_DATA_SHEET = "Входные данные";
    private ExcelWriter writer;

    public void calculateAverageDayData(ExcelModel2023 model, String outputFilename) throws IOException {
        writer = new ExcelWriter(outputFilename);
        var linesByDate = groupByDates(model);
        System.out.println("Found " + linesByDate.size() + " days data");
        var averageLines = calculateAverage(linesByDate);
        averageLines.sort(Comparator.comparingInt(a -> a.lineNum));
        printInputDataToXlsx(model.sheet.lines, writer.sheet(INPUT_DATA_SHEET));
        printInputDataToXlsx(averageLines, writer.sheet(AVG_DATA_SHEET));
        writer.save();
    }

    public void calculateAverageDayData(ExcelModelXls2024 model, String outputFilename) throws IOException {
        writer = new ExcelWriter(outputFilename);
        var linesByDate = groupByDates(model);
        System.out.println("Found " + linesByDate.size() + " days data");
        var averageLines = calculateAverageXls(linesByDate);
        averageLines.sort(Comparator.comparingInt(a -> a.lineNum));
        printInputDataToXls(model.sheet.lines, writer.sheet(INPUT_DATA_SHEET));
        printInputDataToXls(averageLines, writer.sheet(AVG_DATA_SHEET));
        writer.save();
    }

    private Map<String, List<ExcelModel2023.DataLine>> groupByDates(ExcelModel2023 model) {
        Map<String, List<ExcelModel2023.DataLine>> linesByDate = new HashMap<>();
        for(var line : model.sheet.lines) {
            if(!linesByDate.containsKey(line.date)) {
                linesByDate.put(line.date, new LinkedList<>());
            }
            linesByDate.get(line.date).add(line);
        }
        return linesByDate;
    }

    private Map<String, List<ExcelModelXls2024.DataLine>> groupByDates(ExcelModelXls2024 model) {
        Map<String, List<ExcelModelXls2024.DataLine>> linesByDate = new HashMap<>();
        for(var line : model.sheet.lines) {
            if(!linesByDate.containsKey(line.date)) {
                linesByDate.put(line.date, new LinkedList<>());
            }
            linesByDate.get(line.date).add(line);
        }
        return linesByDate;
    }

    private List<ExcelModel2023.DataLine> calculateAverage(Map<String, List<ExcelModel2023.DataLine>> linesByDate) {
        List<ExcelModel2023.DataLine> result = new LinkedList<>();
        for(var date : linesByDate.keySet()) {
            var linesInDay = linesByDate.get(date);
            var dateLine = linesInDay.get(0);
            result.add(dateLine);
            for(var i = 1; i < linesInDay.size(); i++) {
                var additionData = linesInDay.get(i);
                dateLine.add(additionData);
            }
            dateLine.divide(linesInDay.size());
        }
        return result;
    }

    private List<ExcelModelXls2024.DataLine> calculateAverageXls(Map<String, List<ExcelModelXls2024.DataLine>> linesByDate) {
        List<ExcelModelXls2024.DataLine> result = new LinkedList<>();
        for(var date : linesByDate.keySet()) {
            var linesInDay = linesByDate.get(date);
            var dateLine = linesInDay.get(0);
            result.add(dateLine);
            for(var i = 1; i < linesInDay.size(); i++) {
                var additionData = linesInDay.get(i);
                dateLine.add(additionData);
            }
            dateLine.divide(linesInDay.size());
        }
        return result;
    }

    private void printInputDataToXlsx(List<ExcelModel2023.DataLine> lines, ExcelSheetWriter sheet) {
        sheet.set("A1", "№");
        sheet.set("B1", "Дата замера");
        sheet.set("C1", "Время замера");
        sheet.set("D1", "№ Объекта");
        sheet.set("E1", "Зав.№ косы");
        sheet.set("F1", "0,00м");
        sheet.set("G1", "1,00м");
        sheet.set("H1", "2,00м");
        sheet.set("I1", "3,00м");
        sheet.set("J1", "5,00м");
        sheet.set("K1", "7,00м");
        sheet.set("L1", "10,00м");
        var lineNum = 2;
        for(var line : lines) {
            sheet.set("A" + lineNum, line.number.replace(".", ","));
            sheet.set("B" + lineNum, line.date.replace(".", ","));
            sheet.set("C" + lineNum, line.time.replace(".", ","));
            sheet.set("D" + lineNum, line.target);
            sheet.set("E" + lineNum, line.endKosaNumber.replace(".", ","));
            sheet.set("F" + lineNum, Float.toString(line.depth0m).replace(".", ","));
            sheet.set("G" + lineNum, Float.toString(line.depth1m).replace(".", ","));
            sheet.set("H" + lineNum, Float.toString(line.depth2m).replace(".", ","));
            sheet.set("I" + lineNum, Float.toString(line.depth3m).replace(".", ","));
            sheet.set("J" + lineNum, Float.toString(line.depth5m).replace(".", ","));
            sheet.set("K" + lineNum, Float.toString(line.depth7m).replace(".", ","));
            sheet.set("L" + lineNum, Float.toString(line.depth10m).replace(".", ","));
            lineNum++;
        }
    }

    private void printInputDataToXls(List<ExcelModelXls2024.DataLine> lines, ExcelSheetWriter sheet) {
        sheet.set("A1", "Дата замера");
        sheet.set("B1", "Время замера");
        sheet.set("C1", "№ Объекта");
        sheet.set("D1", "Зав.№ косы");
        sheet.set("E1", "-1,0м");
        sheet.set("F1", "0,00м");
        sheet.set("G1", "1,00м");
        sheet.set("H1", "2,00м");
        sheet.set("I1", "3,00м");
        sheet.set("J1", "5,00м");
        sheet.set("K1", "7,00м");
        sheet.set("L1", "10,00м");
        sheet.set("M1", "Глубина нулевой изотермы, м");
        sheet.set("N1", "Интегральная температура");
        var lineNum = 2;
        for(var line : lines) {
            sheet.set("A" + lineNum, line.date);
            sheet.set("B" + lineNum, line.time.replace(".", ","));
            sheet.set("C" + lineNum, line.target);
            sheet.set("D" + lineNum, line.endKosaNumber.replace(".", ","));
            sheet.set("E" + lineNum, line.depthMinus1mRaw.replace(".", ","));
            sheet.set("F" + lineNum, Float.toString(line.depth0m).replace(".", ","));
            sheet.set("G" + lineNum, Float.toString(line.depth1m).replace(".", ","));
            sheet.set("H" + lineNum, Float.toString(line.depth2m).replace(".", ","));
            sheet.set("I" + lineNum, Float.toString(line.depth3m).replace(".", ","));
            sheet.set("J" + lineNum, Float.toString(line.depth5m).replace(".", ","));
            sheet.set("K" + lineNum, Float.toString(line.depth7m).replace(".", ","));
            sheet.set("L" + lineNum, Float.toString(line.depth10m).replace(".", ","));
            sheet.set("M" + lineNum, line.zeroIsotermDepth.replace(".", ","));
            sheet.set("N" + lineNum, Float.toString(line.integralTemperature).replace(".", ","));
            lineNum++;
        }
    }
}
