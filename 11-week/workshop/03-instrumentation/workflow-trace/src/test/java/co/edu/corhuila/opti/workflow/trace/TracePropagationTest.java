package co.edu.corhuila.opti.workflow.trace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

class TracePropagationTest {

    private static final String INCOMING = "00-7d3e1c0a9b5f4e2a8c6d1f0b3a9e4c72-1111aaaa2222bbbb-01";
    private static final String PLACE_ORDER = "/api/v1/sagas/place-order";

    private final ByteArrayOutputStream logs = new ByteArrayOutputStream();
    private final AtomicReference<String> seenTraceparent = new AtomicReference<>();
    private final AtomicReference<String> seenCorrelation = new AtomicReference<>();
    private final Metrics metrics = new Metrics();
    private HttpServer products;
    private HttpServer workflow;

    @BeforeEach
    void start() throws Exception {
        products = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        products.createContext("/api/v1/frames/f1/reservations", exchange -> {
            seenTraceparent.set(exchange.getRequestHeaders().getFirst("traceparent"));
            seenCorrelation.set(exchange.getRequestHeaders().getFirst("X-Correlation-Id"));
            reply(exchange, 201, "{}");
        });
        products.start();

        Clock clock = Clock.fixed(Instant.parse("2026-10-20T19:41:07.102Z"), ZoneOffset.UTC);
        StructuredLogger log = new StructuredLogger(new PrintStream(logs, true), clock, "opti-workflow", "1.1.0", "qa");
        TracedHttpClient client = new TracedHttpClient(Duration.ofSeconds(2));
        String reserveUrl = "http://127.0.0.1:" + products.getAddress().getPort() + "/api/v1/frames/f1/reservations";
        workflow = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        workflow.createContext(PLACE_ORDER, exchange -> {
            try {
                log.info("saga.step.started", "reserve-stock", Map.of("saga_step", "reserve-stock"));
                reply(exchange, client.post(reserveUrl, "saga-1:reserve-stock").statusCode(), "{}");
            } catch (Exception e) {
                reply(exchange, 502, "{}");
            }
        }).getFilters().add(new TraceContextFilter(log, metrics));
        workflow.createContext("/metrics", metrics.handler());
        workflow.start();
    }

    @AfterEach
    void stop() {
        workflow.stop(0);
        products.stop(0);
    }

    @Test
    void downstreamCallCarriesTheTraceIdReceived() throws Exception {
        HttpResponse<String> response = call(INCOMING);

        String sentDownstream = seenTraceparent.get();
        assertEquals("7d3e1c0a9b5f4e2a8c6d1f0b3a9e4c72", TraceContext.parse(sentDownstream).orElseThrow().traceId());
        assertEquals(TraceContext.parse(response.headers().firstValue("traceparent").orElseThrow()).orElseThrow().traceId(),
                TraceContext.parse(sentDownstream).orElseThrow().traceId());
        assertNotEquals("1111aaaa2222bbbb", TraceContext.parse(sentDownstream).orElseThrow().spanId());
        assertEquals(response.headers().firstValue("X-Correlation-Id").orElseThrow(), seenCorrelation.get());
    }

    @Test
    void startsANewTraceWhenNoneArrivesAndStillPropagatesIt() throws Exception {
        HttpResponse<String> response = call(null);

        TraceContext returned = TraceContext.parse(response.headers().firstValue("traceparent").orElseThrow()).orElseThrow();
        assertEquals(returned.traceId(), TraceContext.parse(seenTraceparent.get()).orElseThrow().traceId());
    }

    @Test
    void logLinesAndMetricsFollowTheContract() throws Exception {
        call(INCOMING);

        String output = logs.toString(StandardCharsets.UTF_8);
        for (String line : output.strip().split("\n")) {
            assertTrue(line.contains("\"trace_id\":\"7d3e1c0a9b5f4e2a8c6d1f0b3a9e4c72\""), line);
            assertTrue(line.contains("\"service\":\"opti-workflow\""), line);
        }
        assertTrue(output.contains("\"event\":\"http.request.completed\""));
        HttpResponse<String> scrape = HttpClient.newHttpClient().send(HttpRequest.newBuilder(
                URI.create("http://127.0.0.1:" + workflow.getAddress().getPort() + "/metrics")).build(),
                HttpResponse.BodyHandlers.ofString());
        assertEquals(200, scrape.statusCode());
        String exposition = scrape.body();
        assertTrue(exposition.contains("workflow_requests_total{route=\"" + PLACE_ORDER + "\",outcome=\"ok\"} 1"), exposition);
        assertTrue(exposition.contains("workflow_request_duration_seconds_count{route=\"" + PLACE_ORDER + "\"} 1"), exposition);
    }

    private HttpResponse<String> call(String traceparent) throws Exception {
        HttpRequest.Builder request = HttpRequest.newBuilder(
                URI.create("http://127.0.0.1:" + workflow.getAddress().getPort() + PLACE_ORDER))
                .POST(HttpRequest.BodyPublishers.noBody());
        if (traceparent != null) {
            request.header("traceparent", traceparent);
        }
        return HttpClient.newHttpClient().send(request.build(), HttpResponse.BodyHandlers.ofString());
    }

    private static void reply(HttpExchange exchange, int status, String body) throws java.io.IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }
}
