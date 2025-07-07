package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.MyWatchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.opentest4j.AssertionFailedError;
import ru.inno.course.player.data.DataProviderJSON;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MyWatchers.class)
public class PlayerServiceNegativeTests {

    @BeforeEach
    public void clearBefore() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Path FILEPATH = Path.of("data.json");

        Collection<Player> currentList = Collections.EMPTY_LIST;

        mapper.writerWithDefaultPrettyPrinter().writeValue(FILEPATH.toFile(), currentList);
    }

    @AfterEach
    public void clearAfter() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Path FILEPATH = Path.of("data.json");

        Collection<Player> currentList = Collections.EMPTY_LIST;

        mapper.writerWithDefaultPrettyPrinter().writeValue(FILEPATH.toFile(), currentList);
    }

    @Test
    @DisplayName("1. Удалить игрока которого нет")
    @Tag("Negative_TC")
    public void createPlayerInEmptyListTest() {
        PlayerService service = new PlayerServiceImpl();

        String expectedPlayerNick = "Nick";
        int expectedPlayerId = 10;

        for (int i = 0; i < 8; i++) {
            service.createPlayer(expectedPlayerNick + (i + 1));
        }

        try {
            NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.deletePlayer(expectedPlayerId));
            assertEquals("No such user: " + expectedPlayerId, exception.getMessage());
        } catch (AssertionFailedError assertionFailedError) {
            fail("Нет исключения для невалидной ситуации!");
        }
    }

    @Test
    @DisplayName("2. Создать дубликат (имя уже занято)")
    @Tag("Negative_TC")
    public void createDuplicateTest() {
        PlayerService service = new PlayerServiceImpl();

        String expectedPlayerNick = "Nick";

        service.createPlayer(expectedPlayerNick);

        try {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.createPlayer(expectedPlayerNick));
            assertEquals("Nickname is already in use: " + expectedPlayerNick, exception.getMessage());
        } catch (AssertionFailedError assertionFailedError) {
            fail("Нет исключения для невалидной ситуации!");
        }
    }

    @Test
    @DisplayName("3. Получить игрока по id, которого нет")
    @Tag("Negative_TC")
    public void getPlayerForNonExistingIdTest() {
        PlayerService service = new PlayerServiceImpl();

        String expectedPlayerNick = "Nick";
        int expectedPlayerId = 1000;

        service.createPlayer(expectedPlayerNick);

        try {
            NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.getPlayerById(expectedPlayerId));
            assertEquals("No such user: " + expectedPlayerId, exception.getMessage());
        } catch (AssertionFailedError assertionFailedError) {
            fail("Нет исключения для невалидной ситуации!");
        }
    }

    @Test
    @DisplayName("4. Сохранить игрока с пустым ником")
    @Tag("Negative_TC")
    public void createPlayerWithEmptyNickTest() {
        PlayerService service = new PlayerServiceImpl();

        String expectedPlayerNick = "";

        try {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.createPlayer(expectedPlayerNick));
            assertEquals("Nickname is empty!", exception.getMessage());
        } catch (AssertionFailedError assertionFailedError) {
            fail("Нет исключения для невалидной ситуации!");
        }
    }

    @Test
    @DisplayName("5. Начислить отрицательное число очков")
    @Tag("Negative_TC")
    public void addNegativePointsTest() {
        PlayerService service = new PlayerServiceImpl();

        String expectedPlayerNick = "Nick";
        int expectedPoints = -200;

        try {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.addPoints(service.createPlayer(expectedPlayerNick), expectedPoints));
            assertEquals("You cannot add negative points!", exception.getMessage());
        } catch (AssertionFailedError assertionFailedError) {
            fail("Нет исключения для невалидной ситуации!");
        }
    }

    @Test
    @DisplayName("6. Начислить очки игроку которого нет")
    @Tag("Negative_TC")
    public void addPointsForNonExistingPlayerTest() {
        PlayerService service = new PlayerServiceImpl();

        String expectedPlayerNick = "Nick";
        int expectedPoints = 200;
        int expectedPLayerId = 1000;

        service.createPlayer(expectedPlayerNick);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> service.addPoints(expectedPLayerId, expectedPoints));
        assertEquals("No such user: " + expectedPLayerId, exception.getMessage());
    }

    @Test
    @DisplayName("11. Проверить корректность загрузки JSON файла. Есть дубликаты")
    @Tag("Negative_TC")
    public void loadJSONWithDuplicatesTest() throws IOException {
        Collection<Player> players = new ArrayList<>();
        players.add(new Player(1, "Nick", 100, true));
        players.add(new Player(2, "Nick", 200, false));
        players.add(new Player(1, "Nick1", 300, false));

        new DataProviderJSON().save(players);

        try {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new DataProviderJSON().load());
            assertEquals("JSON file contains duplicates!", exception.getMessage());
        } catch (AssertionFailedError assertionFailedError) {
            fail("Нет исключения для невалидной ситуации!");
        }
    }

    //todo: ожидаем что длина ника не более 15 символов?
    @Test
    @DisplayName("12. Проверить создание игрока с 16 символами")
    @Tag("Negative_TC")
    public void createPlayerWith16SymbolsTest() {
        PlayerService service = new PlayerServiceImpl();

        String expectedNick = "K".repeat(16);

        String actualNick = service.getPlayerById(service.createPlayer(expectedNick)).getNick();

        assertEquals(expectedNick, actualNick);
    }

}
