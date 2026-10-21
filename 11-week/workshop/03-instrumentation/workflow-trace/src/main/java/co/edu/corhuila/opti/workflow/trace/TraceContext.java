package co.edu.corhuila.opti.workflow.trace;

import java.security.SecureRandom;
import java.util.HexFormat;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** W3C trace context of one hop: the trace id is shared by the whole request, the span id is per hop. */
public record TraceContext(String traceId, String spanId, boolean sampled) {

    public static final String HEADER = "traceparent";
    private static final Pattern FORMAT = Pattern.compile("^00-([0-9a-f]{32})-([0-9a-f]{16})-([0-9a-f]{2})$");
    private static final SecureRandom RANDOM = new SecureRandom();

    /** Reads an incoming header; empty when it is missing or malformed (the caller then starts a new trace). */
    public static Optional<TraceContext> parse(String header) {
        if (header == null) {
            return Optional.empty();
        }
        Matcher m = FORMAT.matcher(header.trim());
        if (!m.matches() || m.group(1).equals("0".repeat(32)) || m.group(2).equals("0".repeat(16))) {
            return Optional.empty();
        }
        return Optional.of(new TraceContext(m.group(1), m.group(2), (Integer.parseInt(m.group(3), 16) & 1) == 1));
    }

    public static TraceContext newRoot() {
        return new TraceContext(randomHex(16), randomHex(8), true);
    }

    /** Same trace, new span: what an outbound call carries. */
    public TraceContext child() {
        return new TraceContext(traceId, randomHex(8), sampled);
    }

    public String format() {
        return "00-" + traceId + "-" + spanId + "-" + (sampled ? "01" : "00");
    }

    private static String randomHex(int bytes) {
        byte[] data = new byte[bytes];
        do {
            RANDOM.nextBytes(data);
        } while (isZero(data));
        return HexFormat.of().formatHex(data);
    }

    private static boolean isZero(byte[] data) {
        for (byte b : data) {
            if (b != 0) {
                return false;
            }
        }
        return true;
    }
}
