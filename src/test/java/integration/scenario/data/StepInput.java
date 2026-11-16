package integration.scenario.data;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.EndTurn;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;
import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;

import java.util.Map;
import java.util.Optional;

public record StepInput(
        Optional<Integer> rollDieResult,
        Optional<Integer> gambleResult,
        PlayerColor submittedBy,
        ActionDefinition action
) {
    public StepInput {
        if ("Roll".equals(action.type) && rollDieResult.isEmpty()) {
            throw new IllegalArgumentException("Roll action requires a rollDieResult");
        }
        if ("Gamble".equals(action.type) && gambleResult.isEmpty()) {
            throw new IllegalArgumentException("Gamble action requires a gambleResult");
        }
    }

    public record ActionDefinition(
            String type,
            Map<String, Object> parameters
    ) {
        public Action toAction() {
            return switch (type()) {
                case "Roll" -> new Roll();
                case "Gamble" -> new Gamble();
                case "EndTurn" -> new EndTurn();
                case "MovePiece" -> new MovePiece(
                        (Integer) parameters().get("startTile"),
                        (Integer) parameters().get("targetTile")
                );
                default -> throw new IllegalArgumentException(
                        "Unknown action type: " + type()
                );
            };
        }
    }
}
