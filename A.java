package robot37;
import robocode.*;
import java.awt.geom.Point2D; 
import java.awt.Color;
public class A extends AdvancedRobot
{ 
double moveDirection;  
enemyStat enemy = new enemyStat();   
public void run() 
    {   
     setBodyColor(Color.yellow);
     setGunColor(Color.YELLOW);
     setRadarColor(Color.YELLOW);
     setScanColor(Color.GREEN);
     setBulletColor(Color.YELLOW);    
     setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setTurnRadarRightRadians(2*Math.PI);  //让雷达一直转
         while(true) 
      {  
      doFire();
      doScannedRobot();
      doMovement() ;
      execute(); 
   }
 }
public  void  onScannedRobot(ScannedRobotEvent e)
      {   
         enemy.updateStat(e, this);      
      }    
 public void  doMovement()//随机移动
        { //程序首先判断运动是否完成，若完成，随机选出一个座标点，移动到该点  
        if( Math.abs( getDistanceRemaining() ) < 1 ) 
    {
            double myX = getX(); 
            double myY = getY(); 
            double nextX, nextY;        // the next point move to
            nextX = Math.random() * ( getBattleFieldWidth() - 100 ) + 50; 
            nextY = Math.random() * ( getBattleFieldHeight() - 100 ) + 50;  
            double turnAngle =getAngle(myX,myY,nextX,nextY ); 
            turnAngle = normalizeBearing( turnAngle - getHeadingRadians() ); 
            double moveDistance = Point2D.distance( myX, myY, nextX, nextY ); 
            double moveDirection = 1;  
       if ( Math.abs( turnAngle ) >Math.PI/2) 
            {         
                turnAngle  =  normalizeBearing( turnAngle  + Math.PI);     
                moveDirection *= -1; 
            } 
            setTurnRightRadians( turnAngle ); 
            setAhead( moveDirection * moveDistance ); }
      } 
 public static double getAngle(double x1, double y1, double x2, double y2) 
    { 
        return Math.atan2( x2 - x1, y2 - y1 ); 
    }  
 public  void doScannedRobot()
      {
            if (getTime()-enemy.time>1)  
            {
                 setTurnRadarRightRadians(3*Math.PI);
             }
            else
            {
              double absolation_bearing=(getHeadingRadians()+enemy.relative_bearing)%(2*Math.PI);
              double relative_radar_bearing=getRadarHeadingRadians()-absolation_bearing;
                 double a=normalizeBearing(relative_radar_bearing);
                 setTurnRadarLeftRadians(a);
             }
      }
public  double normalizeBearing( double angle ) 
    { 
        if ( angle < -Math.PI ) 
            angle += 2*Math.PI; 
        if ( angle > Math.PI ) 
            angle -= 2*Math.PI; 
        return angle; 
    }   
 public void  doFire()
       {
              double  heading_offset=enemy.en_heading-enemy.pre_heading+0.000001;
              double  distance=enemy.distance;
              double  bullet_velocity=20-3*3;              
              double  r=enemy.velocity/heading_offset;
              double heading=0.0;             
              for(int i=0;i<4;i++)//迭代   使预测更加准确 
          {
             double  b_travel_ti=distance/bullet_velocity;
             double predict_heading_r=enemy.en_heading+heading_offset*b_travel_ti;
                   double predict_x=enemy.xCoordinate-r*Math.cos(predict_heading_r)+r*Math.cos(enemy.en_heading);
                   double predict_y=enemy.yCoordinate+r*Math.sin(predict_heading_r)-r*Math.sin(enemy.en_heading);
                   heading=Math.atan2(predict_x-getX(),predict_y-getY());
                   double diatance=Point2D.distance( getX(), getY(), predict_x, predict_y );
             }
              double a=normalizeBearing(heading-getGunHeadingRadians());              
              setTurnGunRightRadians(a);
              setFire(3);
       }
 public   void   onHitByBullet(HitByBulletEvent e)
     {
      if(getX()>150&&getY()>150&&enemy.battle_w-getX()>150&&enemy.battle_h-getY()>150)
       {
               double dist=150;
               double  a=normalizeBearing(90 - (getHeading() - e.getHeading()));
               if(Math.abs(a)>Math.PI/2) 
                    {
                         a=normalizeBearing(a+Math.PI);
                       } 
                  setTurnRight( a);  
                  setAhead(dist); 
                  dist *= -1; 
             }
      }    
public void onWin(WinEvent e)
   {
  for (int i = 0; i < 50; i++) 
    {
   turnGunRightRadians(Math.PI*3/4);
   turnGunLeftRadians(Math.PI*3/4);
  }
 }
}
class enemyStat //方法

          {    public  double  pre_heading;
            public  double  en_heading;
                    double xCoordinate;
                    double yCoordinate;
                       double direction;
                       double battle_h;
                       double battle_w;
                       double relative_bearing;
                       double velocity;
                       double time;
                    double distance;
               public  void  updateStat(ScannedRobotEvent e,AdvancedRobot ar)
                    {
                          pre_heading=en_heading;  
                          en_heading=e.getHeadingRadians();  
                          battle_h=ar.getBattleFieldHeight();
                       battle_w=ar.getBattleFieldWidth();
                          relative_bearing=e.getBearingRadians();
                          direction = relative_bearing + ar.getHeadingRadians(); 
                          xCoordinate= ar.getX() + Math.sin( direction ) * distance; 
                          yCoordinate = ar.getY() + Math.cos( direction ) *distance; 
                          velocity=e.getVelocity(); 
                          time=e.getTime();     
                          distance=e.getDistance();            
                    }
          }