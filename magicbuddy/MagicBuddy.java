package scripts.rs07.magicbuddy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Login;
import org.tribot.api2007.Player;
import org.tribot.api2007.Skills;
import org.tribot.api2007.WebWalking;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.MessageListening07;
import org.tribot.script.interfaces.Painting;

import scripts.rs07.magicbuddy.config.Data;
import scripts.rs07.magicbuddy.config.GUI;
import scripts.rs07.magicbuddy.types.State;
import scripts.rs07.magicbuddy.utls.Antiban;
import scripts.rs07.magicbuddy.utls.Tools;

@ScriptManifest(
		authors = { "Vancouver" },
		category = "Magic",
		name = "MagicBuddy")

public class MagicBuddy extends Script implements MessageListening07, Painting {

	@Override
	public void run() {
		onStart();
		
		State.setState(State.findCurrentState());
		while(!State.getState().equals(State.STOP)) {
			Mouse.setSpeed(Data.mouseSpeed + General.random(-10, 10));
			
			switch(State.getState()) {
			case WAIT : 
				sleep(75, 100);
				Antiban.pushMouse(3, 25);
				break;
				
			case NEED_TO_SELECT_SPELL : 
				if(!Data.spell.select()) {
					sleep(75, 100);
				}
				
				break;
				
			case NEED_TO_REPOSITION : 
				if(WebWalking.walkTo(Data.startTile)) {
					sleep(250, 350);
				}
				
				break;
				
			case CAN_CAST :
				Data.spell.cast();
				break;
				
			default : break;
			}
			
			if(!State.getState().equals(State.STOP)) {
				State.setState(State.findCurrentState());
				sleep(75, 100);
			}
		}
	}
	
	private void onStart() {
		final GUI gui = new GUI();
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					gui.setVisible(true);
					gui.setLocationRelativeTo(null);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		} 

		while(!GUI.started || !Login.getLoginState().equals(Login.STATE.INGAME)) {
			General.sleep(50);
		}

		Data.startExp = Skills.getXP(Skills.SKILLS.MAGIC);
        Data.startLevel = Skills.SKILLS.MAGIC.getCurrentLevel();
        Data.startTime = System.currentTimeMillis();
        
        Data.startTile = Player.getPosition();
	}

	@Override
	public void clanMessageReceived(String arg0, String arg1) {
	}

	@Override
	public void personalMessageReceived(String arg0, String arg1) {
	}

	@Override
	public void playerMessageReceived(String arg0, String arg1) {
	}

	@Override
	public void serverMessageReceived(String s) {
		if(s.contains("to cast this spell.")) {
			println("No more runes to cast " + Data.spell.name() + ".");
			State.setState(State.STOP);
		}
	}

	@Override
	public void tradeRequestReceived(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPaint(Graphics g) {
		if(Login.getLoginState().equals(Login.STATE.INGAME)) {
			Data.runTime = System.currentTimeMillis();
			long timeRan = Data.runTime - Data.startTime;

			int expGained = Skills.getXP(Skills.SKILLS.MAGIC) - Data.startExp;
			int expPerHour = (int) (expGained * 3600000D / timeRan);
			int levelsGained = Skills.getCurrentLevel(Skills.SKILLS.MAGIC) - Data.startLevel;

			g.setFont(new Font("Sans Serif", 0, 12));

			Color lightBlue = new Color(153, 204, 255, 150);
			g.setColor(lightBlue);
			g.fillRect(383, 4, 132, 100);

			// Draws the back panel border
			g.setColor(Color.BLACK);
			g.drawRect(383, 4, 132, 100);

			// Draws a black line under the name branding
			g.drawLine(383, 25, 515, 25);

			// Draws the text shadowing
			g.drawString("MagicBuddy", 418, 21);
			g.drawString("Time: " + Timing.msToString(timeRan), 396, 41);
			g.drawString("Exp: " + expGained + " (" + expPerHour + "/h)", 396, 59);
			g.drawString("Level: " + Skills.getActualLevel(Skills.SKILLS.MAGIC) + " (" + levelsGained + ")", 396, 79);
			g.drawString("TTL: " + Tools.timeToLevel(Skills.SKILLS.MAGIC, expGained, timeRan), 396, 97);

			// Draws the text
			g.setColor(Color.WHITE);
			g.drawString("MagicBuddy", 417, 20);
			g.drawString("Time: " + Timing.msToString(timeRan), 395, 40);
			g.drawString("Exp: " + expGained + " (" + expPerHour + "/h)", 395, 58);
			g.drawString("Level: " + Skills.getActualLevel(Skills.SKILLS.MAGIC) + " (" + levelsGained + ")", 395, 78);
			g.drawString("TTL: " + Tools.timeToLevel(Skills.SKILLS.MAGIC, expGained, timeRan), 395, 96);
		}
	}

}
