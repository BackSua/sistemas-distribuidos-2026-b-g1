package co.edu.corhuila.opti.workflow.trace;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

import java.io.PrintWriter;

import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

/** Runs the tests without Maven: {@code java -cp <classes>:<junit jars> ...RunTests}. */
public final class RunTests {

    private RunTests() {
    }

    public static void main(String[] args) {
        var listener = new SummaryGeneratingListener();
        var request = LauncherDiscoveryRequestBuilder.request().selectors(selectClass(TracePropagationTest.class)).build();
        LauncherFactory.create().execute(request, listener);
        listener.getSummary().printTo(new PrintWriter(System.out));
        listener.getSummary().printFailuresTo(new PrintWriter(System.out), 15);
        System.exit(listener.getSummary().getTotalFailureCount() == 0 ? 0 : 1);
    }
}
