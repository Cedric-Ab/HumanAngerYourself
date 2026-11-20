package integration.scenario;

import integration.RiggedController;
import integration.scenario.data.Scenario;
import integration.scenario.data.ScenarioStep;
import integration.scenario.data.StepInput;

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

    public void run(Scenario scenario) {
        for (ScenarioStep step : scenario.steps()) {
            executeStep(step.input());
            step.expect().ifPresent(expect -> expect.verify(controller.getSnapshot()));
        }
    }

    private void executeStep(StepInput input) {
        input.rollDieResult().ifPresent(controller::setRollDieResult);
        input.gambleResult().ifPresent(controller::setGambleResult);

        Action action = input.action().toAction();

        if (controller.getDecision().decider() != input.submittedBy()) {
            throw new IllegalStateException(
                    "Scenario is invalid. Expected decider: " + input.submittedBy() +
                            ", but actual decider is: " + controller.getDecision().decider());
        }

        if (action instanceof StepInput.SkipAction(boolean onlyPrimary)) {
            controller.setGambleResult(0);
            controller.submit(input.submittedBy(), new Gamble());
            if (!onlyPrimary) {
                controller.submit(input.submittedBy(), new EndTurn());
            }
            return;
        }

        if (!controller.getDecision().availableActions().contains(action)) {
            throw new IllegalStateException(
                    "Scenario is invalid. Action not available: " + action);
        }

        controller.submit(input.submittedBy(), action);
    }
}
