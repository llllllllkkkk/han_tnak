package han.awt.tank;

import java.util.ArrayList;
import java.util.Vector;

public class Shot implements Runnable{
    //子弹速度
    private  int speed=2;
    public int x,y;
    int direction;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
    private Tank tank;

    public Shot(Tank tank) {

        this.x = tank.getX();
        this.y = tank.getY();
        this.tank = tank;
        this.direction = tank.getDirection();
    }

    //判断子弹是否存活
    public boolean isLive = true;

    public boolean getLive() {
        return isLive;
    }

    //设置是否需要结束循环
    private boolean isFlag =true;
    public void setFlag(boolean b){
        isFlag = b;
    }

    public boolean getFlag() {
        return isFlag;
    }

    @Override
    public void run() {
        //设置子弹坐标初始值
        initial_Value();
        //System.out.println("shotX:"+getX() +"  shotY"+getY());
        while (isFlag){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //边界判断
            //System.out.println(x+"  "+y);
            if ((x>1000 || y>800 || x<0 || y<0)){
                setFlag(false);
                isLive=false;
                break;
            }
            //根据方向发射子弹
            if (direction == 0) {
                this.setY(y-speed);

            } else if (direction == 1) {
                this.setY(y+speed);

            } else if (direction == 2) {
                this.setX(x-speed);

            } else if (direction == 3) {
                this.setX(x+speed);

            }


        }

    }
    public void initial_Value(){

        if (direction == 0) {
            this.setX(x + 20);
            this.setY(y);

        } else if (direction == 1) {
            this.setX(x + 20);
            this.setY(y + 60);

        } else if (direction == 2) {
            this.setX(x);
            this.setY(y + 20);

        } else if (direction == 3) {
            this.setX(x + 60);
            this.setY(y + 20);

        }


    }


}
