package scripts.rs07.magicbuddy.types;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.Game;
import org.tribot.api2007.Magic;
import org.tribot.api2007.NPCs;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSNPC;

import scripts.rs07.magicbuddy.config.Data;
import scripts.rs07.magicbuddy.utls.Tools;

public enum Spell {

	CONFUSE("Confuse"),
	WEAKEN("Weaken"),
	CURSE("Curse");
	
	private String name;
	
	private Spell(String name) {
		this.name = name;
	}
	
	public boolean select() {
		if(!Game.getUptext().contains("->") && Magic.selectSpell(name)) {
			return Timing.waitUptext("->", General.random(800, 1000));
		}
		
		return Game.getUptext().contains("->");
	}
	
	private RSNPC getNPC() {
		RSNPC[] npcs = NPCs.findNearest(Data.targetName);
		if(npcs.length > 0) {
			Mouse.move(Tools.randomizePoint(npcs[0].getModel().getCentrePoint(), 5));
			if(Timing.waitUptext("-> " + Data.targetName, General.random(800, 1000))) {
				return npcs[0];
			}
		}
		
		return null;
	}
	
	public void cast() {
		if(Game.getUptext().contains("->")) {
			RSNPC npc = getNPC();
			if(npc != null ) {
				Mouse.click(1); // We already know we have the right uptext 
				if(!Timing.waitCondition(new Condition() {

					@Override
					public boolean active() {
						return Player.getAnimation() != -1;
					}
					
				}, General.random(1250, 1500))) {
					General.println("Timed-out waiting to cast the " + name + " spell,"
							+ " NPC may be weakened already.");
				}	
			}
		}
	}
	
}
