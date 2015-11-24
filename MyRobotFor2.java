package robot2;
import robocode.*;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * MyRobotFor2 - a robot by (your name here)
 */
public class MyRobotFor2 extends Robot
{
 boolean peek;
 double moveAmount;
	/**
	 * run: MyRobotFor2's default behavior
	 */
	public void run() {
	
	 moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
     peek = false;
     turnLeft(getHeading() % 90);
      ahead(moveAmount);
      peek = true;
       turnGunRight(90);
        turnRight(90);
         while (true) {
				peek = true;

                  ahead(moveAmount);

                  peek = false;

                  turnRight(90);
		
		}
	}


	public void onScannedRobot(ScannedRobotEvent e) {

		fire(2);
		if (peek) {
                  scan();
				  }
		
	}

		public void onHitRobot(HitRobotEvent e) {

	public void onBulletMissed(BulletMissedEvent e) {
}
	public void onBulletHitBullet(BulletHitBulletEvent e) {
}
	public void onBulletHit(BulletHitEvent e) {
}

	 
}
