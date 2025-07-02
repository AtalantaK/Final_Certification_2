package helpers;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class MyWatchers implements TestWatcher {
    @Override
    public void testSuccessful(ExtensionContext context) {
        //System.out.println(context.get);
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {

    }
}
