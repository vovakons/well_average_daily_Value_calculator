import com.bergen.exel_orm.orm.ExelORM;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        var filename = "data/Улахан Ан 2 скв 5 поле.xlsx";
        var orm = new ExelORM();
        System.out.println("Begin parse file '" + filename + "'...");
        var excel = orm.read(ExcelModel.class, filename);
        System.out.println("Success: Parsed " + excel.sheet.lines.size() + " lines");
        System.out.println("Prepare input data...");
        for(var line : excel.sheet.lines) {
            line.parseRaw();
        }
        System.out.println("Calculate average in day...");
        var calculator = new AverageDataCalculator();
        calculator.calculateAverageDayData(excel, "data/Улахан Ан скв 5 поле calculate.xlsx");
        System.out.println("Complete!");
    }
} //82 + 70 + 21 = 152 + 21 = 173