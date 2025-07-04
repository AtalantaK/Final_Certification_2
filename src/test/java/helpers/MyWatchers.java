package helpers;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;

public abstract class MyWatchers implements TestWatcher, BeforeAllCallback, AfterAllCallback {
    @Override
    public void testSuccessful(ExtensionContext context) {
        System.out.println("Тест \"" + context.getDisplayName() + "\" прошел успешно");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        System.out.println(cause.getMessage());
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        System.out.println(reason.get());
    }
}
