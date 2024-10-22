package tr.gov.ptt.LogQualityDashboard.entry;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import lombok.Data;
import java.util.List;

@Data
@Document(indexName = "sosyalyardimelektrik")
public class LogEntry {

    @Id
    private String id;

    //private String inputType;
    //private Agent agent;
    //private Host host;
    //private Log log;
    //private List<String> tags;
    //private Fields fields;
    //private Ecs ecs;

    private String threadName;
    private String loggerName;
    private String version;
    private String timestamp;
    private String level;
    private long levelValue;
    private String message;

    @Data
    public static class Agent {
        private String ephemeralId;
        private String id;
        private String type;
        private String hostname;
        private String version;
    }

    @Data
    public static class Host {
        private String name;
    }

    @Data
    public static class Log {
        private File file;
        private long offset;

        @Data
        public static class File {
            private String path;
        }
    }

    @Data
    public static class Fields {
        private String app;
    }

    @Data
    public static class Ecs {
        private String version;
    }
}
