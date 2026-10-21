package co.edu.corhuila.opti.workflow.trace;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

/**
 * Middleware of the workflow: reads the incoming {@code traceparent} (or starts a new trace),
 * keeps the {@code X-Correlation-Id} the platform already uses, returns both in the response and
 * writes one access line per request in the log contract.
 */
public final class TraceContextFilter extends Filter {

    public static final String CORRELATION_HEADER = "X-Correlation-Id";
    private static final Pattern SAFE_ID = Pattern.compile("^[A-Za-z0-9._-]{1,64}$");

    private final StructuredLogger log;

    public TraceContextFilter(StructuredLogger log) {
        this.log = log;
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        TraceContext context = TraceContext.parse(exchange.getRequestHeaders().getFirst(TraceContext.HEADER))
                .map(TraceContext::child)
                .orElseGet(TraceContext::newRoot);
        String incoming = exchange.getRequestHeaders().getFirst(CORRELATION_HEADER);
        String correlationId = incoming != null && SAFE_ID.matcher(incoming).matches()
                ? incoming : UUID.randomUUID().toString();
        TraceScope.enter(context, correlationId);
        exchange.getResponseHeaders().set(TraceContext.HEADER, context.format());
        exchange.getResponseHeaders().set(CORRELATION_HEADER, correlationId);
        long started = System.nanoTime();
        try {
            chain.doFilter(exchange);
        } finally {
            Map<String, Object> fields = new LinkedHashMap<>();
            fields.put("duration_ms", (System.nanoTime() - started) / 1_000_000);
            fields.put("status", exchange.getResponseCode());
            log.info("http.request.completed", exchange.getRequestMethod() + " " + exchange.getHttpContext().getPath(),
                    fields);
            TraceScope.exit();
        }
    }

    @Override
    public String description() {
        return "trace context and structured access log";
    }
}
