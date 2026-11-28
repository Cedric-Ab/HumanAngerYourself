package ch.zhaw.it.pm.humanangeryourself.domain.game.phase;

import ch.zhaw.it.pm.humanangeryourself.api.CardType;
import ch.zhaw.it.pm.humanangeryourself.api.PlayerColor;
import ch.zhaw.it.pm.humanangeryourself.domain.card.ApplicableCard;
import ch.zhaw.it.pm.humanangeryourself.domain.card.types.BlockCard;
import ch.zhaw.it.pm.humanangeryourself.domain.game.Game;
import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActionCardPhaseTest {

    private Game game;
    private GamePhase nextPhase;
    private Player attacker;
    private Player defender;
    private ApplicableCard attack;
    private ActionCardPhase phase;

    @BeforeEach
    void setUp() {
        game = mock(Game.class);
        nextPhase = mock(GamePhase.class);
        attacker = new Player("Attacker", PlayerColor.RED, 0, List.of());
        defender = new Player("Defender", PlayerColor.BLUE, 0, List.of());

        attack = mock(ApplicableCard.class);
        when(attack.type()).thenReturn(CardType.DOUBLE_IT);
        when(attack.getDefender(game, attacker)).thenReturn(Optional.of(defender));

        phase = new ActionCardPhase(game, nextPhase, attacker, attack);
    }

    private static void giveBlocks(Player player, int count) {
        for (int i = 0; i < count; i++) {
            player.addCard(new BlockCard());
        }
    }

    private void assertCardApplied() {
        assertSame(nextPhase, phase.process());
        verify(attack).apply(game, attacker);
    }

    private void assertCardBlocked() {
        assertSame(nextPhase, phase.process());
        verify(attack, never()).apply(any(), any());
    }

    @Test
    void cardWithoutDefenderIsApplied() {
        when(attack.getDefender(game, attacker)).thenReturn(Optional.empty());
        giveBlocks(defender, 1);

        assertCardApplied();
    }

    @Test
    void notCounterableCardIsAppliedAndBlockIsKept() {
        when(attack.type()).thenReturn(CardType.SPEED_BOOST);
        giveBlocks(defender, 1);

        assertCardApplied();
        assertEquals(1, defender.getCards().size());
    }

    @Test
    void defenderWithoutBlockLetsCardThrough() {
        assertCardApplied();
    }

    @Test
    void defenderBlocksAutomatically() {
        giveBlocks(defender, 1);

        assertCardBlocked();
        assertTrue(defender.getCards().isEmpty());
    }

    @Test
    void attackerBlocksTheBlockAutomatically() {
        giveBlocks(defender, 1);
        giveBlocks(attacker, 1);

        assertCardApplied();
        assertTrue(defender.getCards().isEmpty());
        assertTrue(attacker.getCards().isEmpty());
    }

    @Test
    void defenderBlocksAgainIfHeHasASecondBlock() {
        giveBlocks(defender, 2);
        giveBlocks(attacker, 1);

        assertCardBlocked();
        assertTrue(defender.getCards().isEmpty());
        assertTrue(attacker.getCards().isEmpty());
    }

    @Test
    void remainingBlocksAreKeptWhenChainEnds() {
        giveBlocks(defender, 1);
        giveBlocks(attacker, 2);

        assertCardApplied();
        assertEquals(1, attacker.getCards().size());
    }

    @Test
    void countersAreResolvedOnlyOnce() {
        giveBlocks(defender, 1);

        phase.process();
        phase.process();

        verify(attack, never()).apply(any(), any());
        assertTrue(defender.getCards().isEmpty());
    }
}
