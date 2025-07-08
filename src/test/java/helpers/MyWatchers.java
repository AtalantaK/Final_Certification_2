package helpers;

import org.jsoup.Jsoup;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MyWatchers implements TestWatcher, BeforeAllCallback, AfterAllCallback {

    String information = "";
    static int PassedTC;
    static int FailedTC;
    static int DisabledTC;

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        System.out.println("Запускаю тесты...");
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        information += "<p>Тест \"" + context.getDisplayName()
                + "\" <b><span style=\"color: green;\">прошел успешно</span></b></p>";
        System.out.println("Тест \"" + context.getDisplayName() + "\" прошел успешно");
        PassedTC++;
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        information += "<p>Тест \"" + context.getDisplayName()
                + "\" <b><span style=\"color: red;\">упал</span></b></p>";
        System.out.println("Тест \"" + context.getDisplayName() + "\" упал");
        System.out.println("\tПричина падения: " + cause.getMessage());
        FailedTC++;
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        information += "<p>Тест \"" + context.getDisplayName()
                + "\" <b><span style=\"color: gray;\">отключен</span></b>. Причина: "
                + reason.get() + "</p>";
        System.out.println("Тест \"" + context.getDisplayName() + "\" отключен."
                + "\n\tПричина: " + reason.get());
        DisabledTC++;
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        String head = """
                <!DOCTYPE html>
                <html>
                    <head lang="en">
                        <meta charset="UTF-8">
                        <title>Отчёт</title>
                    </head>
                    <body>""";
        String tail = """
                    </body>
                </html>
                """;

        String report = head + "<h1>Кол-во пройденных тестов: " + (PassedTC + FailedTC + DisabledTC) + " </h1>"
                + "<h2>Основная информация:</h2>"
                + information
                + tail
                + "<h2>Итог:</h2><p>"
                + "<ul><li>" + PassedTC + " шт. - Passed</li>"
                + "<li>" + FailedTC + " шт. - Failed</li>"
                + "<li>" + DisabledTC + " шт. - Disabled</li></ul></p>";

        Document document = Jsoup.parse(report);
        document.outputSettings().indentAmount(4).prettyPrint(true);

        Files.writeString(Path.of("report.html"), document.outerHtml());
        System.out.println("Тесты завершены");
    }

}
