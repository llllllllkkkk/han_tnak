package han.awt.tank3;







import java.util.Random;

public class EnemyTanks extends Tank implements Runnable{


    public EnemyTanks(int direction, int x, int y, int style) {
        super(direction, x, y, style);
    }



    public void move() {
        Random random = new Random();
      //  int tankDirection =1;
        int tankDirection = random.nextInt(4)+1;

        int tankMove = random.nextInt(30) + 31;
        for (int j = 0; j < tankMove; j++) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (tankDirection % 4 == 0) {
                this.setDirection(0);
                this.setY(this.getY() - 2);
            } else if (tankDirection % 4 == 1) {
                this.setDirection(1);
                this.setY(this.getY() + 2);
            } else if (tankDirection % 4 == 2) {
                this.setDirection(2);
                this.setX(this.getX() - 2);
            } else if (tankDirection % 4 == 3) {
                this.setDirection(3);
                this.setX(this.getX() + 2);

            }
        }

    }

    @Override
    public void run() {
        //当坦克存活的时候才进行移动
        while (this.tank_live) {

            move();

        }
    }


}
