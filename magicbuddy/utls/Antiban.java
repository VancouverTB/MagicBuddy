package scripts.rs07.magicbuddy.utls;

import java.awt.Point;

import org.tribot.api.General;
import org.tribot.api.input.Mouse;

public class Antiban {

	public static void pushMouse(int range, int chance) {
		int i = General.random(1, chance);
		if(i == 1) {
			Mouse.move(new Point(Mouse.getPos().x + General.random(-range, range),
								 Mouse.getPos().y + General.random(-range, range)));
		}
	}
	
}
