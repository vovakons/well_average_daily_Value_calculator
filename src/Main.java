import com.bergen.exel_orm.orm.ExelORM;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        parse("Улахан Ан 2 скв 5 поле.xlsx", "Улахан Ан 2 скв 5 поле average.xlsx");
//        parse("Улахан Ан 2 скв 6 лес.xlsx", "Улахан Ан 2 скв 6 лес average.xlsx");
        parseXls("Скв 5 2024.xlsx", "Скв 5 2024 average.xlsx");
        parseXls("Скв 6 2024.xlsx", "Скв 6 2024 average.xlsx");
    }

    public static void parse(String input, String output) throws IOException {
        var filename = "data/" + input;
        var orm = new ExelORM();
        System.out.println("Begin parse file '" + filename + "'...");
        var excel = orm.read(ExcelModel.class, filename);
        excel.prepareRaw();
        System.out.println("Success: Parsed " + excel.sheet.lines.size() + " lines");
        System.out.println("Calculate average in day...");
        var calculator = new AverageDataCalculator();
        calculator.calculateAverageDayData(excel, "data/" + output);
        System.out.println("Complete!");
    }

    public static void parseXls(String input, String output) throws IOException {
        var filename = "data/" + input;
        var orm = new ExelORM();
        System.out.println("Begin parse file '" + filename + "'...");
        var excel = orm.read(ExcelModelXls.class, filename);
        excel.prepareRaw();
        System.out.println("Success: Parsed " + excel.sheet.lines.size() + " lines");
        System.out.println("Calculate average in day...");
        var calculator = new AverageDataCalculator();
        calculator.calculateAverageDayData(excel, "data/" + output);
        System.out.println("Complete!");
    }
} //82 + 70 + 21 = 152 + 21 = 173