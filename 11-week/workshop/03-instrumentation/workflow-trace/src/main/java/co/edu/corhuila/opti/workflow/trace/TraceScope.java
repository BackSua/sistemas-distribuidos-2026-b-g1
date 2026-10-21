package co.edu.corhuila.opti.workflow.trace;

/** The trace context and correlation id of the request running on the current thread. */
public final class TraceScope {

    private record Current(TraceContext context, String correlationId) {
    }

    private static final ThreadLocal<Current> CURRENT = new ThreadLocal<>();

    private TraceScope() {
    }

    public static void enter(TraceContext context, String correlationId) {
        CURRENT.set(new Current(context, correlationId));
    }

    public static void exit() {
        CURRENT.remove();
    }

    public static TraceContext context() {
        Current current = CURRENT.get();
        return current == null ? null : current.context();
    }

    public static String correlationId() {
        Current current = CURRENT.get();
        return current == null ? null : current.correlationId();
    }
}
