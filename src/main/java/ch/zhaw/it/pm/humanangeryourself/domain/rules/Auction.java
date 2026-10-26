package ch.zhaw.it.pm.humanangeryourself.domain.rules;

import ch.zhaw.it.pm.humanangeryourself.domain.model.Player;

public class Auction {

    public Player getCurrentBidder(){return new Player();}
    public void bid(Player player, int amount){}
    public boolean isFinished(){return false;}
}
