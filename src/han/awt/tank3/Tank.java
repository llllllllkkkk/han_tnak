package han.awt.tank3;

public class Tank {
    public boolean tank_live =true;
    public Shot shot=null;
    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    //坦克的方向
    private int direction;
    //坦克的初始坐标
    //坦克移动
   public int x, y,style;

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Tank(int direction, int x, int y,int style) {
        this.direction = direction;
        this.style = style;
        this.x = x;
        this.y = y;
    }
    //坦克发射子弹线程方法
    public Shot tank_Shot(){
        //System.out.println("x:"+x+"  y:"+y);
        shot = new Shot(this);
        new Thread(shot).start();
        return shot;
    }

    public int getX(){
        return this.x;
    }

    public int getY() {
        return y;
    }


    public void  move(int direction){
        if (direction==0){
            this.setY(this.getY()-2);

        }else if (direction==1){
            this.setY(this.getY()+2);

        }else if (direction==2){
            this.setX(this.getX()-2);

        }else if (direction==3){
            this.setX(this.getX()+2);

        }
        this.setDirection(direction);
    }


}
