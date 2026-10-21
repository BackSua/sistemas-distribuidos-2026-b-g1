package co.edu.corhuila.opti.workflow.trace;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Outbound call of a saga step (the way ParticipantClient reaches opti-products-api): copies the
 * current trace to the request with a new span id and keeps the correlation id.
 */
public final class TracedHttpClient {

    private final HttpClient http = HttpClient.newHttpClient();
    private final Duration timeout;

    public TracedHttpClient(Duration timeout) {
        this.timeout = timeout;
    }

    public HttpResponse<String> post(String url, String idempotencyKey) throws IOException, InterruptedException {
        TraceContext current = TraceScope.context();
        HttpRequest.Builder request = HttpRequest.newBuilder(URI.create(url)).timeout(timeout)
                .header("Idempotency-Key", idempotencyKey)
                .POST(HttpRequest.BodyPublishers.noBody());
        if (current != null) {
            request.header(TraceContext.HEADER, current.child().format());
            request.header(TraceContextFilter.CORRELATION_HEADER, TraceScope.correlationId());
        }
        return http.send(request.build(), HttpResponse.BodyHandlers.ofString());
    }
}
