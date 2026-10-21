package co.edu.corhuila.opti.workflow.trace;

import java.io.PrintStream;
import java.time.Clock;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/** Writes one JSON line per event in the OptiView log contract (02-log-contract.md). */
public final class StructuredLogger {

    private static final DateTimeFormatter TIMESTAMP =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneOffset.UTC);

    private final PrintStream out;
    private final Clock clock;
    private final String service;
    private final String version;
    private final String environment;

    public StructuredLogger(PrintStream out, Clock clock, String service, String version, String environment) {
        this.out = out;
        this.clock = clock;
        this.service = service;
        this.version = version;
        this.environment = environment;
    }

    public void info(String event, String message, Map<String, Object> fields) {
        log("INFO", event, message, fields);
    }

    public void warn(String event, String message, Map<String, Object> fields) {
        log("WARN", event, message, fields);
    }

    public void error(String event, String message, Map<String, Object> fields) {
        log("ERROR", event, message, fields);
    }

    private void log(String level, String event, String message, Map<String, Object> fields) {
        TraceContext context = TraceScope.context();
        Map<String, Object> line = new LinkedHashMap<>();
        line.put("timestamp", TIMESTAMP.format(clock.instant()));
        line.put("level", level);
        line.put("service", service);
        line.put("version", version);
        line.put("environment", environment);
        line.put("trace_id", context == null ? null : context.traceId());
        line.put("span_id", context == null ? null : context.spanId());
        line.put("correlation_id", TraceScope.correlationId());
        line.put("event", event);
        line.put("message", message);
        line.putAll(fields);
        out.println(toJson(line));
    }

    static String toJson(Map<String, Object> line) {
        StringBuilder json = new StringBuilder("{");
        line.forEach((key, value) -> {
            if (value == null) {
                return;
            }
            if (json.length() > 1) {
                json.append(',');
            }
            json.append('"').append(key).append("\":");
            if (value instanceof Number || value instanceof Boolean) {
                json.append(value);
            } else {
                json.append('"').append(escape(value.toString())).append('"');
            }
        });
        return json.append('}').toString();
    }

    private static String escape(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\n").replace("\r", "\r");
    }
}
