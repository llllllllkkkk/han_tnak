package han.awt.tank;

import java.util.Vector;

public class MyTank extends Tank {
    //记录爆炸的坐标
    int boomX,boomY;
    private Vector<Tank> tankVector;
    public MyTank(int direction, int x, int y, int style /*,Vector enemyTankVector*/) {
        super(direction, x, y, style);
        /*this.tankVector = enemyTankVector;*/
    }
    public Shot tank_Shot(){

        //System.out.println("x:"+x+"  y:"+y);
        shot = new Shot(this);
        new Thread(shot).start();

        return shot;
    }

   /* public void run() {

        w:while (tankVector.size()!=0) {
            //System.out.println(this.shot.y);
            //先判断是玩家的子弹(先拿到玩家坦克的对象)，判断玩家的子弹坐标 如果子弹坐标大于坦克y坐标就输出坦克消失
            for (Tank tank : tankVector) {
                //拿到敌方坦克对象
                //System.out.println(tank.x+"   "+tank.y);
                if ((tank.getDirection()==0 || tank.getDirection()==1) && (this.shot.y > tank.y && this.shot.y < tank.y+60  && this.shot.x >= tank.x && this.shot.x<tank.x+40) ) {
                    //记录爆炸坦克x y坐标
                    boomX = tank.x;boomY = tank.y;
                    //坦克状态置为死
                    tank.tank_live = false;
                    //清空对应坦克对象
                    tankVector.remove(tank);
                    //把子弹清空
                    shot.isLive = false;
                    break w;
                }else if ((tank.getDirection()==2 ||tank.getDirection()==3) && (this.shot.y > tank.y && this.shot.y < tank.y+40  && this.shot.x > tank.x && this.shot.x<tank.x+60)){
                    //记录爆炸坦克坐标
                    boomX = tank.x;boomY = tank.y;
                    //坦克状态置为死
                    tank.tank_live = false;
                    //清空对应坦克对象
                    tankVector.remove(tank);
                    //把子弹清空
                    shot.isLive = false;
                    break w;
                }
            }

        }
    }*/
}
