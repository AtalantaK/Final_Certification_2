package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.MyWatchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.io.IOException;
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

    @AfterEach
    public void clearAfter() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Path FILEPATH = Path.of("data.json");

        Collection<Player> currentList = Collections.EMPTY_LIST;

        mapper.writerWithDefaultPrettyPrinter().writeValue(FILEPATH.toFile(), currentList);
    }

    @Test
    @DisplayName("Добавить игрока в пустой список")
    @Tag("Positive_TC")
    public void createPlayerTestInEmptyList() {
        PlayerService service = new PlayerServiceImpl();
        String expectedPlayerNick = "Nick";
        int expectedPlayerId = 1;
        int actualPlayerId = service.createPlayer(expectedPlayerNick);

        String expectedPlayer = "Player{id=" + expectedPlayerId + ", nick='" + expectedPlayerNick + "', points=0, isOnline=true}";

        assertEquals(expectedPlayer, service.getPlayerById(actualPlayerId).toString());
    }

    //todo: есть ли смысл проверять в какой список мы добавляем игрока? - возможно есть
    @Test
    @DisplayName("Добавить игрока в не пустой список")
    @Tag("Positive_TC")
    public void createPlayerTestInNotEmptyList() {
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
    public void deleteNotLastPlayer() {
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
                () -> assertEquals("No such user: " + expectedDeletedPlayerId, exception.getMessage()));
    }

    //todo: есть ли смысл проверять какого игрока мы удаляем? - возможно есть
    @Test
    @DisplayName("Удалить последнего игрока")
    @Tag("Positive_TC")
    public void deleteLastPlayer() {
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
}
