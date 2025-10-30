import com.bergen.exel_orm.annotations.ExelORMCell;
import com.bergen.exel_orm.annotations.ExelORMFile;
import com.bergen.exel_orm.annotations.ExelORMInfinitiveList;
import com.bergen.exel_orm.annotations.ExelORMSheetsMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ExelORMFile
public class ExcelModelXls {
    @ExelORMSheetsMap public Map<String, DataSheet> sheets;
    public DataSheet sheet;

    public void prepareRaw() {
        if(!sheets.isEmpty()) {
            for(var shName : sheets.keySet()) {
                if(Objects.equals(shName, "average")) {
                    continue;
                }
                sheet = sheets.get(shName);
                var errors = 0;
                var num = 1;
                for(var line : sheet.lines) {
                    line.lineNum = num++;
                    line.parseRaw();
                    errors += line.parseErrors;
                }
                System.out.println("Parse sheet errors=" + errors);
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
        @ExelORMCell(pos = "A1") public String date;
        @ExelORMCell(pos = "B1") public String time;
        @ExelORMCell(pos = "C1") public String target;
        @ExelORMCell(pos = "D1") public String endKosaNumber; //wtf?
        @ExelORMCell(pos = "E1") public String depthMinus1mRaw;
        @ExelORMCell(pos = "F1") public String depth0mRaw;
        @ExelORMCell(pos = "G1") public String depth1mRaw;
        @ExelORMCell(pos = "H1") public String depth2mRaw;
        @ExelORMCell(pos = "I1") public String depth3mRaw;
        @ExelORMCell(pos = "J1") public String depth5mRaw;
        @ExelORMCell(pos = "K1") public String depth7mRaw;
        @ExelORMCell(pos = "L1") public String depth10mRaw;
        @ExelORMCell(pos = "M1") public String zeroIsotermDepth;
        @ExelORMCell(pos = "N1") public String integralTemperatureRaw;
        public float depth0m;
        public float depth1m;
        public float depth2m;
        public float depth3m;
        public float depth5m;
        public float depth7m;
        public float depth10m;
        public float integralTemperature;

        public int parseErrors = 0;

        public void parseRaw() {
            depth0m = parseFloat(depth0mRaw);
            depth1m = parseFloat(depth1mRaw);
            depth2m = parseFloat(depth2mRaw);
            depth3m = parseFloat(depth3mRaw);
            depth5m = parseFloat(depth5mRaw);
            depth7m = parseFloat(depth7mRaw);
            depth10m = parseFloat(depth10mRaw);
            integralTemperature = parseFloat(integralTemperatureRaw);
        }

        private float parseFloat(String text) {
            try {
                return Float.parseFloat(text);
            } catch (NumberFormatException err) {
                parseErrors++;
                return 0;
            }
        }

        public void add(DataLine other) {
            depth0m += other.depth0m;
            depth1m += other.depth1m;
            depth2m += other.depth2m;
            depth3m += other.depth3m;
            depth5m += other.depth5m;
            depth7m += other.depth7m;
            depth10m += other.depth10m;
            integralTemperature += other.integralTemperature;
        }

        public void divide(float value) {
            depth0m /= value;
            depth1m /= value;
            depth2m /= value;
            depth3m /= value;
            depth5m /= value;
            depth7m /= value;
            depth10m /= value;
            integralTemperature /= value;
        }
    }
}
