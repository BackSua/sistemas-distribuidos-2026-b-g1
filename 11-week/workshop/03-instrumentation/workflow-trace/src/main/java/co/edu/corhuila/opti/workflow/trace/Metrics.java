package co.edu.corhuila.opti.workflow.trace;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

import com.sun.net.httpserver.HttpHandler;

/** The two request metrics of the workflow, in Prometheus text format (04-metrics.md). */
public final class Metrics {

    /** Upper bounds in seconds; a saga step answers in well under 5 s (HTTP_TIMEOUT is PT5S). */
    static final double[] BUCKETS = {0.05, 0.1, 0.25, 0.5, 1, 2.5, 5, 10};

    private static final class Histogram {
        final LongAdder[] buckets = new LongAdder[BUCKETS.length];
        final LongAdder count = new LongAdder();
        final DoubleAdder sum = new DoubleAdder();

        Histogram() {
            for (int i = 0; i < buckets.length; i++) {
                buckets[i] = new LongAdder();
            }
        }
    }

    private final Map<String, LongAdder> requests = new ConcurrentHashMap<>();
    private final Map<String, Histogram> durations = new ConcurrentHashMap<>();

    /** Outcome is a closed list (ok, client_error, error); the route is the registered path, never the raw URL. */
    public void record(String route, int status, double seconds) {
        String outcome = status >= 500 ? "error" : status >= 400 ? "client_error" : "ok";
        requests.computeIfAbsent(route + "|" + outcome, k -> new LongAdder()).increment();
        Histogram h = durations.computeIfAbsent(route, k -> new Histogram());
        for (int i = 0; i < BUCKETS.length; i++) {
            if (seconds <= BUCKETS[i]) {
                h.buckets[i].increment();
            }
        }
        h.count.increment();
        h.sum.add(seconds);
    }

    /** The /metrics endpoint scraped by Prometheus. */
    public HttpHandler handler() {
        return exchange -> {
            byte[] body = render().getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/plain; version=0.0.4");
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        };
    }

    public String render() {
        StringBuilder text = new StringBuilder();
        text.append("# HELP workflow_requests_total Requests handled, by route and outcome.\n");
        text.append("# TYPE workflow_requests_total counter\n");
        new TreeMap<>(requests).forEach((key, value) -> {
            String[] part = key.split("[|]");
            text.append("workflow_requests_total{route=\"").append(part[0]).append("\",outcome=\"")
                    .append(part[1]).append("\"} ").append(value.sum()).append('\n');
        });
        text.append("# HELP workflow_request_duration_seconds Request latency in seconds.\n");
        text.append("# TYPE workflow_request_duration_seconds histogram\n");
        new TreeMap<>(durations).forEach((route, h) -> {
            String labels = "route=\"" + route + "\"";
            for (int i = 0; i < BUCKETS.length; i++) {
                text.append("workflow_request_duration_seconds_bucket{").append(labels).append(",le=\"")
                        .append(BUCKETS[i]).append("\"} ").append(h.buckets[i].sum()).append('\n');
            }
            text.append("workflow_request_duration_seconds_bucket{").append(labels).append(",le=\"+Inf\"} ")
                    .append(h.count.sum()).append('\n');
            text.append("workflow_request_duration_seconds_sum{").append(labels).append("} ").append(h.sum.sum()).append('\n');
            text.append("workflow_request_duration_seconds_count{").append(labels).append("} ").append(h.count.sum()).append('\n');
        });
        return text.toString();
    }
}
