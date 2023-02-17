package han.awt.tank2;

public class Boom {
    public int x,y;
    int life = 9;
    boolean isLive=true;

    public Boom(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void lifeDown() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (life>0){
            life--;
        }else {
            isLive = false;
        }
    }

}
