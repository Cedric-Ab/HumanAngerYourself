package ch.zhaw.it.pm.humanangeryourself.api.snapshot;

import java.util.List;

/**
 * Information about an NPC.
 *
 * @param tile   where the NPC is located
 * @param offers cards and their prices offered by this NPC
 */
public record NpcInfo(
        int tile,
        List<CardOffer> offers
) {
}
