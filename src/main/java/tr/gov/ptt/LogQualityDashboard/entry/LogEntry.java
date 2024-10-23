package tr.gov.ptt.LogQualityDashboard.entry;

import lombok.Data;
import org.thymeleaf.spring6.expression.Fields;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class LogEntry {
    private String loggerName;
    private String logTime;
    private String message;
    private String level;
    private String exception;
    private String threadName;
    private boolean processed;
    private Fields fields;
    private String logId;
    private String version;
    private String timestamp;

    public Date getLogTimeAsDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        try {
            return format.parse(this.logTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
