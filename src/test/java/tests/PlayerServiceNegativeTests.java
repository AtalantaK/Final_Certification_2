package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.MyWatchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import ru.inno.course.player.data.DataProviderJSON;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerServiceNegativeTests {

    @BeforeAll
    public static void beforeAll() {
        System.out.println("Запускаю тесты");
    }

    @BeforeEach
    public void clearBefore() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Path FILEPATH = Path.of("data.json");

        Collection<Player> currentList = Collections.EMPTY_LIST;

        mapper.writerWithDefaultPrettyPrinter().writeValue(FILEPATH.toFile(), currentList);
    }

//    @AfterEach
//    public void clearAfter() {
//        final ObjectMapper mapper = new ObjectMapper();
//        final Path FILEPATH = Path.of("data.json");
//
//        Collection<Player> currentList = Collections.EMPTY_LIST;
//
//        mapper.writerWithDefaultPrettyPrinter().writeValue(FILEPATH.toFile(), currentList);
//    }

    @Test
    @DisplayName("1. Удалить игрока которого нет")
    @Tag("Negative_TC")
    //@ExtendWith(MyWatchers.class)
    public void createPlayerInEmptyListTest() {
        PlayerService service = new PlayerServiceImpl();

        String expectedPlayerNick = "Nick";
        int expectedPlayerId = 10;

        for (int i = 0; i < 8; i++) {
            service.createPlayer(expectedPlayerNick + (i + 1));
        }

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.deletePlayer(expectedPlayerId));

        assertEquals("No such user: " + expectedPlayerId, exception.getMessage());
    }

    @Test
    @DisplayName("2. Создать дубликат")
    @Tag("Negative_TC")
    //@ExtendWith(MyWatchers.class)
    public void createDuplicateTest() {
        PlayerService service = new PlayerServiceImpl();

        String expectedPlayerNick = "Nick";

        service.createPlayer(expectedPlayerNick);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.createPlayer(expectedPlayerNick));

        assertEquals("Nickname is already in use: " + expectedPlayerNick, exception.getMessage());
    }

}

//todo: добавить комментарии к коду
