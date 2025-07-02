package tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.inno.course.player.model.Player;
import ru.inno.course.player.service.PlayerService;
import ru.inno.course.player.service.PlayerServiceImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerServiceTest {

    @BeforeEach
    public void clearBefore() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final Path FILEPATH = Path.of("data.json");

        Collection<Player> currentList = Collections.EMPTY_LIST;

        mapper.writerWithDefaultPrettyPrinter().writeValue(FILEPATH.toFile(), currentList);
    }

    @Test
    @DisplayName("Добавить игрока")
    @Tag("Positive_TC")
    public void createPlayerTest() {
        PlayerService service = new PlayerServiceImpl();
        String expectedPlayerNick = "Nick";
        int expectedPlayerId = 1;
        int actualPlayerId = service.createPlayer(expectedPlayerNick);

        String expectedPlayer = "Player{id=" + expectedPlayerId + ", nick='" + expectedPlayerNick + "', points=0, isOnline=true}";

        assertEquals(expectedPlayer, service.getPlayerById(actualPlayerId).toString());
    }
}
