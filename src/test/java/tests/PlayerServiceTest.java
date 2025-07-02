package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.MyWatchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerServiceTest {

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
    @DisplayName("Добавить игрока в пустой список")
    @Tag("Positive_TC")
    public void createPlayerInEmptyListTest() {
        PlayerService service = new PlayerServiceImpl();

        String expectedPlayerNick = "Nick";
        int expectedPlayerId = 1;
        int actualPlayerId = service.createPlayer(expectedPlayerNick);

        String expectedPlayer = "Player{id=" + expectedPlayerId + ", nick='" + expectedPlayerNick + "', points=0, isOnline=true}";

        assertAll("Несколько проверок",
                () -> assertTrue(Files.exists(Path.of("data.json"))),
                () -> assertEquals(expectedPlayer, service.getPlayerById(actualPlayerId).toString()));
    }

    //todo: есть ли смысл проверять в какой список мы добавляем игрока? - возможно есть
    @Test
    @DisplayName("Добавить игрока в не пустой список")
    @Tag("Positive_TC")
    public void createPlayerInNotEmptyListTest() {
        PlayerService service = new PlayerServiceImpl();

        String expectedPlayerNick = "Nick2";
        int expectedPlayerId = 2;

        service.createPlayer("Nick1");
        int actualPlayerId = service.createPlayer(expectedPlayerNick);

        String expectedPlayer = "Player{id=" + expectedPlayerId + ", nick='" + expectedPlayerNick + "', points=0, isOnline=true}";

        assertEquals(expectedPlayer, service.getPlayerById(actualPlayerId).toString());
    }

    @Test
    @DisplayName("Удалить не последнего игрока")
    @Tag("Positive_TC")
    public void deleteNotLastPlayerTest() {
        PlayerService service = new PlayerServiceImpl();

        String expectedDeletedPlayerNick = "Nick1";
        int expectedDeletedPlayerId = 1;
        service.createPlayer(expectedDeletedPlayerNick);
        service.createPlayer("Nick2");
        Player deletedPlayer = service.deletePlayer(expectedDeletedPlayerId);

        String expectedDeletedPlayer = "Player{id=" + expectedDeletedPlayerId + ", nick='" + expectedDeletedPlayerNick + "', points=0, isOnline=true}";

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            service.getPlayerById(expectedDeletedPlayerId);
        });

        assertAll("Несколько проверок",
                () -> assertEquals(expectedDeletedPlayer, deletedPlayer.toString()),
                () -> assertEquals("No such user: " + expectedDeletedPlayerId, exception.getMessage()),
                () -> assertFalse(service.getPlayers().isEmpty()));
    }

    //todo: есть ли смысл проверять какого игрока мы удаляем? - возможно есть
    @Test
    @DisplayName("Удалить последнего игрока")
    @Tag("Positive_TC")
    public void deleteLastPlayerTest() {
        PlayerService service = new PlayerServiceImpl();

        String expectedDeletedPlayerNick = "Nick1";
        int expectedDeletedPlayerId = 1;
        service.createPlayer(expectedDeletedPlayerNick);
        Player deletedPlayer = service.deletePlayer(expectedDeletedPlayerId);

        String expectedDeletedPlayer = "Player{id=" + expectedDeletedPlayerId + ", nick='" + expectedDeletedPlayerNick + "', points=0, isOnline=true}";

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            service.getPlayerById(expectedDeletedPlayerId);
        });

        assertAll("Несколько проверок",
                () -> assertEquals(expectedDeletedPlayer, deletedPlayer.toString()),
                () -> assertEquals("No such user: " + expectedDeletedPlayerId, exception.getMessage()),
                () -> assertTrue(service.getPlayers().isEmpty()));
    }

    @Test
    @DisplayName("Создать игрока при условии что JSON файл не существует")
    @Tag("Positive_TC")
    public void createPlayerWithoutJSONTest() throws IOException {
        Files.deleteIfExists(Path.of("data.json"));

        // Создаем буфер для захвата вывода
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalErr = System.err;

        // Перенаправляем System.err в наш буфер
        System.setErr(new PrintStream(baos));

        // Вызываем ошибочный вывод
        new PlayerServiceImpl();

        // Возвращаем System.err в исходное состояние
        System.setErr(originalErr);

        // Получаем строку из буфера
        String errorOutput = baos.toString();

        // Теперь можно использовать значение
        assertTrue(errorOutput.contains("File loading error 1. java.io.FileNotFoundException: data.json (The system cannot find the file specified)"));
    }

    @Test
    @DisplayName("Начислить баллы существующему игроку")
    @Tag("Positive_TC")
    public void addPointsForExistingPlayerTest() {
        PlayerService service = new PlayerServiceImpl();

        String expectedPlayerNick = "Nick";
        int actualPlayerId = service.createPlayer(expectedPlayerNick);
        int expectedPoints = 100;
        int actualPoints = service.addPoints(actualPlayerId, expectedPoints);

        assertAll("Несколько проверок",
                () -> assertEquals(expectedPoints, actualPoints),
                () -> assertEquals(expectedPoints, service.getPlayerById(actualPlayerId).getPoints()));

    }

    @Test
    @DisplayName("Начислить баллы поверх существующих существующему игроку")
    @Tag("Positive_TC")
    public void addAdditionalPointsForExistingPlayerTest() {
        PlayerService service = new PlayerServiceImpl();

        String expectedPlayerNick = "Nick";
        int points1 = 100;
        int points2 = 250;
        int actualPlayerId = service.createPlayer(expectedPlayerNick);
        service.addPoints(actualPlayerId, points1);
        int expectedPoints = points1 + points2;
        int actualPoints = service.addPoints(actualPlayerId, points2);

        assertAll("Несколько проверок",
                () -> assertEquals(expectedPoints, actualPoints),
                () -> assertEquals(expectedPoints, service.getPlayerById(actualPlayerId).getPoints()));

    }
}
