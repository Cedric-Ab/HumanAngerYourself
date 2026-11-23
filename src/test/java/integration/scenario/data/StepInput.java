package integration.scenario.data;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.PropertySize;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.BuyProperty;
import ch.zhaw.it.pm.humanangeryourself.api.action.DiscardCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.EndTurn;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;
import ch.zhaw.it.pm.humanangeryourself.api.action.MovePiece;
import ch.zhaw.it.pm.humanangeryourself.api.action.PlayCard;
import ch.zhaw.it.pm.humanangeryourself.api.action.Roll;
import ch.zhaw.it.pm.humanangeryourself.api.action.TargetPiece;

import java.util.Map;
import java.util.Optional;

public record StepInput(
        Optional<Integer> rollDieResult,
        Optional<GambleResultDefinition> gambleResult,
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

    public record GambleResultDefinition(
            Optional<Integer> amount,
            Optional<CardType> card
    ) {
        public GambleResultDefinition {
            if (amount.isEmpty() && card.isEmpty()) {
                throw new IllegalArgumentException("Gamble result must have either an amount or a card");
            } else if (amount.isPresent() && card.isPresent()) {
                throw new IllegalArgumentException("Gamble result cannot have both an amount and a card");
            }
        }
    }

    public record ActionDefinition(String type, Map<String, Object> parameters) {
        public ActionDefinition {
            parameters = parameters == null ? Map.of() : parameters;
        }

        public Action toAction() {
            return switch (type()) {
                case "SkipPrimary" -> new SkipAction(true);
                case "SkipTurn" -> new SkipAction(false);
                case "AdvanceToTurn" -> new AdvanceToTurn();

                case "Roll" -> new Roll();
                case "Gamble" -> new Gamble();
                case "EndTurn" -> new EndTurn();
                case "MovePiece" -> new MovePiece(intParam("startTile"), intParam("targetTile"));
                case "TargetPiece" -> new TargetPiece(
                        enumParam("targetPlayer", PlayerColor.class),
                        intParam("targetTile"));
                case "PlayCard" -> new PlayCard(enumParam("card", CardType.class));
                case "DiscardCard" -> new DiscardCard(enumParam("card", CardType.class));
                case "BuyCard" -> new BuyCard(enumParam("card", CardType.class));
                case "BuyProperty" -> new BuyProperty(
                        intParam("tile"),
                        enumParam("size", PropertySize.class));

                default -> throw new IllegalArgumentException("Unknown action type: " + type());
            };
        }

        private int intParam(String name) {
            if (parameters.get(name) == null)
                throw new IllegalArgumentException("Missing parameter '%s'".formatted(name));

            if (!(parameters.get(name) instanceof Number number))
                throw new IllegalArgumentException("Parameter '%s' must be a number".formatted(name));

            return number.intValue();
        }

        private <T extends Enum<T>> T enumParam(String name, Class<T> enumType) {
            Object value = parameters.get(name);

            if (value == null)
                throw new IllegalArgumentException("Missing parameter '%s'".formatted(name));

            try {
                return Enum.valueOf(enumType, value.toString());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Parameter '%s' must be a valid %s but was '%s'"
                        .formatted(name, enumType.getSimpleName(), value), e);
            }
        }
    }

    public record SkipAction(boolean onlyPrimary) implements Action { }

    public record AdvanceToTurn() implements Action { }
}
