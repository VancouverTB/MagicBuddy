package scripts.rs07.magicbuddy.types;

import org.tribot.api2007.Game;
import org.tribot.api2007.Login;
import org.tribot.api2007.Player;

import scripts.rs07.magicbuddy.config.Data;

public enum State {

	STOP, WAIT, NEED_TO_SELECT_SPELL, CAN_CAST, NEED_TO_REPOSITION;
	
	public static State state;
	
	public static State getState() {
		return state;
	}
	
	public static void setState(State s) {
		state = s;
	}
	
	public static State findCurrentState() {
		if(Player.isMoving() || !Login.getLoginState().equals(Login.STATE.INGAME)) {
			return WAIT;
		}
		
		if(!Player.getPosition().equals(Data.startTile)) {
			return NEED_TO_REPOSITION;
		}
		
		if(!Game.getUptext().contains("->")) {
			return NEED_TO_SELECT_SPELL;
		} else if(Player.getAnimation() == -1) {
			return CAN_CAST;
		}
		
		return WAIT;
	}
	
}
