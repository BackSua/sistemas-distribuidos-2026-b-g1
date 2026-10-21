package co.edu.corhuila.opti.workflow.trace;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Clock;
import java.time.Duration;
import java.util.Map;

import com.sun.net.httpserver.HttpServer;

/** Starts a fake opti-products-api and the instrumented workflow, sends one request and prints the logs and /metrics. */
public final class DemoRun {

    private DemoRun() {
    }

    public static void main(String[] args) throws Exception {
        HttpServer products = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        products.createContext("/api/v1/frames/f1/reservations", exchange -> {
            exchange.sendResponseHeaders(201, -1);
            exchange.close();
        });
        products.start();
        Metrics metrics = new Metrics();
        StructuredLogger log = new StructuredLogger(System.out, Clock.systemUTC(), "opti-workflow", "1.1.0", "dev");
        TracedHttpClient client = new TracedHttpClient(Duration.ofSeconds(2));
        String url = "http://127.0.0.1:" + products.getAddress().getPort() + "/api/v1/frames/f1/reservations";
        HttpServer workflow = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        workflow.createContext("/api/v1/sagas/place-order", exchange -> {
            try {
                log.info("saga.step.started", "reserve-stock", Map.of("saga_step", "reserve-stock"));
                int status = client.post(url, "saga-1:reserve-stock").statusCode();
                log.info("saga.step.completed", "reserve-stock answered " + status, Map.of("saga_step", "reserve-stock"));
                exchange.sendResponseHeaders(200, -1);
            } catch (Exception e) {
                exchange.sendResponseHeaders(502, -1);
            }
            exchange.close();
        }).getFilters().add(new TraceContextFilter(log, metrics));
        workflow.start();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + workflow.getAddress().getPort()
                + "/api/v1/sagas/place-order"))
                .header("traceparent", "00-7d3e1c0a9b5f4e2a8c6d1f0b3a9e4c72-1111aaaa2222bbbb-01")
                .POST(HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        Thread.sleep(300);
        System.out.println("response traceparent: " + response.headers().firstValue("traceparent").orElse("-"));
        System.out.println(metrics.render());
        workflow.stop(0);
        products.stop(0);
        System.out.println("done");
    }
}
