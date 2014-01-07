package scripts.rs07.magicbuddy.utls;

import java.awt.Point;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Skills;

public class Tools {

	 public static String timeToLevel(Skills.SKILLS skill, int expGain, long runTime) {
         if(expGain > 0 && skill.getActualLevel() < 99) {
                 return Timing.msToString((runTime * (Skills.getXPToNextLevel(skill) + 1) / expGain));
         }
         
         return "0:00:00";
	 }
 
	 
	 public static Point randomizePoint(Point p, int range) {
         return new Point(p.x + General.random(-range, range), 
        		 			p.y + General.random(-range, range));
	 }
	
}
