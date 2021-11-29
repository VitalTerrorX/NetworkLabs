package ru.nsu.spirin.snake.utils;

import lombok.experimental.UtilityClass;
import me.ippolitov.fit.snakes.SnakesProto;
import me.ippolitov.fit.snakes.SnakesProto.GamePlayer;
import org.apache.log4j.Logger;
import ru.nsu.spirin.snake.datatransfer.NetNode;
import ru.nsu.spirin.snake.game.Player;
import ru.nsu.spirin.snake.game.Snake;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
public final class PlayerUtils {
    private static final Logger logger = Logger.getLogger(PlayerUtils.class);

    public static GamePlayer createPlayerForMessage(Player player) {
        var builder = GamePlayer.newBuilder();
        builder.setName(player.getName());
        builder.setId(player.getId());
        builder.setIpAddress(player.getNetNode().getAddress().getHostAddress());
        builder.setPort(player.getNetNode().getPort());
        builder.setRole(player.getRole());
        builder.setType(SnakesProto.PlayerType.HUMAN);
        builder.setScore(player.getScore());
        return builder.build();
    }

    public static Player findPlayerBySnake(Snake snake, List<Player> playerList) {
        for (var player : playerList) {
            if (player.getId() == snake.getPlayerID()) {
                return player;
            }
        }
        return null;
    }

    public static List<Player> getPlayerList(List<GamePlayer> gamePlayers) {
        return gamePlayers.stream().map(
                gamePlayer -> {
                    Player player = null;
                    try {
                        player = new Player(gamePlayer.getName(), gamePlayer.getId(), new NetNode(gamePlayer.getIpAddress(), gamePlayer.getPort()));
                        player.setRole(gamePlayer.getRole());
                        player.setScore(gamePlayer.getScore());
                    }
                    catch (UnknownHostException e) {
                        logger.error(e.getLocalizedMessage());
                    }
                    return player;
                }
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
