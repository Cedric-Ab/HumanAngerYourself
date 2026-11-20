package integration.scenario;

import integration.RiggedController;
import integration.scenario.data.Scenario;
import integration.scenario.data.ScenarioStep;
import integration.scenario.data.StepInput;

import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.api.action.Action;
import ch.zhaw.it.pm.humanangeryourself.api.action.EndTurn;
import ch.zhaw.it.pm.humanangeryourself.api.action.Gamble;

public class ScenarioRunner {

    private final RiggedController controller;

    public ScenarioRunner(RiggedController controller) {
        this.controller = controller;
    }

    public ScenarioRunner() {
        this(new RiggedController());
    }

    private PlayerColor getDecider() {
        return controller.getDecision().decider();
    }

    public void run(Scenario scenario) {
        for (ScenarioStep step : scenario.steps()) {
            executeStep(step.input());
            step.expect().ifPresent(expect -> expect.verify(controller.getSnapshot()));
        }
    }

    private void executeStep(StepInput input) {
        if (handleMacros(input)) return;

        input.rollDieResult().ifPresent(controller::setRollDieResult);
        input.gambleResult().ifPresent(controller::setGambleResult);

        Action action = input.action().toAction();

        validateDecider(input);
        validateAction(action);

        controller.submit(input.submittedBy(), action);
    }

    private boolean handleMacros(StepInput input) {
        Action action = input.action().toAction();

        switch (action) {
            case StepInput.SkipAction(boolean onlyPrimary) -> {
                validateDecider(input);

                if (onlyPrimary) {
                    skipPrimaryAction(input.submittedBy());
                } else {
                    skipTurn(input.submittedBy());
                }
            }
            case StepInput.AdvanceToTurn ignored -> {
                while (getDecider() != input.submittedBy()) {
                    skipTurn(getDecider());
                }
            }
            default -> { return false; }
        }
        return true;
    }

    private void validateDecider(StepInput input) {
        if (input.submittedBy() != getDecider()) {
            throw new IllegalStateException(
                    "Scenario is invalid. Expected decider: " + getDecider() +
                            ", but actual decider is: " + input.submittedBy());
        }
    }

    private void validateAction(Action action) {
        if (!controller.getDecision().availableActions().contains(action)) {
            String availableActions = controller.getDecision().availableActions().stream()
                    .map(Action::toString)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("none");
            throw new IllegalStateException("Scenario is invalid. Action not available: " + action +
                    ". Available actions: " + availableActions);
        }
    }

    private void skipPrimaryAction(PlayerColor player) {
        controller.setGambleResult(0);
        controller.submit(player, new Gamble());
    }

    private void skipTurn(PlayerColor player) {
        skipPrimaryAction(player);
        controller.submit(player, new EndTurn());
    }
}
