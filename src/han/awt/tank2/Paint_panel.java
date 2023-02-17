package han.awt.tank2;

import han.awt.tank.resources.BoomResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

public class Paint_panel extends JPanel implements KeyListener,ActionListener {
    Timer timer = new Timer(100, this);
    //初始化己方坦克
    private MyTank myTank = null;
   EnemyTanks enemyTanks=null;
    //敌方坦克
    Vector<EnemyTanks> enemyTanksVector = null;
    Vector<EnemyTanks> boomTanksVector = null;
    Vector<Shot> shotVector = new Vector();
    int enemyTank_Size=3;
    //创建一个集合装载爆炸对象
    Vector<Boom> bombVector = new Vector();
    
    //爆炸图片
    Image imageSmall = null;
    Image imageMid = null;
    Image imageBig = null;

    //装载坦克移动的线程集合
    Vector<Thread> enemyTankThreadVector = new Vector<>();
    public Paint_panel() {
        enemyTanksVector = new Vector<>();
        myTank = new MyTank(0,300,300,4/*,enemyTanksVector*/);

       this.setFocusable(true);
       this.addKeyListener(this);
       //添加敌方坦克到集合中
       // EnemyTanks enemyTanks;
        for (int i = 0; i < enemyTank_Size; i++) {
            //创建敌方坦克

             enemyTanks = new EnemyTanks(2,100*i,100,i);

           // System.out.println(enemyTanks);
            //坦克对象添加到集合中
            enemyTanksVector.add(enemyTanks);
            //开启子弹的任务 并存储到集合中
            shotVector.add(enemyTanks.tank_Shot());
            //爆炸坦克
            //boomTanksVector.add(enemyTanks);
        }
        //开启敌方坦克射击
        //enemyTank_Shot();
        //把坦克对象添加到vector集合中
        for (int i = 0; i < enemyTanksVector.size(); i++) {
            enemyTankThreadVector.add(new Thread(enemyTanksVector.get(i)));
        }

        imageSmall = Toolkit.getDefaultToolkit().getImage(BoomResources.small_url);
        imageMid = Toolkit.getDefaultToolkit().getImage(BoomResources.mid_url);
        imageBig = Toolkit.getDefaultToolkit().getImage(BoomResources.big_url);

    }

    private void enemyTank_Shot() {
        for (int i = 0; i < enemyTanksVector.size(); i++) {
            EnemyTanks tank = enemyTanksVector.get(i);
            if (tank.shot!=null && shotVector.size()<3 && tank.shot.isLive==false) {
                shotVector.add(tank.tank_Shot());
            }
            System.out.println(shotVector.size());
        }
    }


    @Override
    public void paint(Graphics g) {

        super.paint(g);
        this.setBackground(Color.black);

        //System.out.println(myTank.getX()+"  "+myTank.getY());
        // 0 ↑ 1↓ 2← 3→
        //System.out.println(myTank.getDirection());
        //画我方坦克
        draw_MyTank(g);

        //画敌方坦克
        draw_enemyTank(g);
        //先调用方法加载boom集合
        /*if (boomTanksVector==null) {
           System.out.println("boom_Mod方法运行");
            boom_Mod();
        }*/
        //画爆炸效果 判断子弹爆炸状态(碰到敌方坦克爆炸) 
        //先从集合取出爆炸坐标
        if (bombVector!=null){

            for (int i = 0; i<bombVector.size();i++) {
                Boom boom = bombVector.get(i);
                //开始画爆炸
                if (boom.life>6) {
                    g.drawImage(imageBig, boom.x, boom.y, this);
                }
                else if(boom.life>3) {
                    g.drawImage(imageMid, boom.x, boom.y, this);
                }
                else {
                    g.drawImage(imageSmall, boom.x, boom.y, this);
                }
                //让子弹生命减少
                boom.lifeDown();
                //爆炸之后bomb对象消失
                if (boom.life==0) {
                    System.out.println("爆炸结束");
                    boolean remove = bombVector.remove(boom);
                    System.out.println(bombVector.size());

                }
            }
        }
        
        //画爆炸效果 判断子弹爆炸状态(碰到敌方坦克爆炸) 每次播放前都要延迟100毫秒
        /*ArrayList arrayLis = BoomResources.getArrayLis();
        if(myTank.shot!=null && !myTank.shot.isLive){
       // System.out.println(BoomResources.arrayList.size());
            for (int i = 0; i < arrayLis.size(); i++) {
                Icon icon = (Icon) arrayLis.get(i);
                icon.paintIcon(this,g,myTank.boomX,myTank.boomY);
            }
        }*/
        //System.out.println(myTank.shot.isLive);


        //画出敌方坦克的子弹
        for (int i1 = 0; i1 < shotVector.size(); i1++) {
            //开启子弹的任务 并存储到集合中
            Shot shot = shotVector.get(i1);
            if (shot.isLive) {
                g.setColor(Color.yellow);
                g.drawOval(shot.x, shot.y, 1, 1);
            }else {
                //System.out.println("remove");
                shotVector.remove(shot);
            }
        }
        //

            enemyTank_Shot();


        //画玩家子弹
        //System.out.println(bombVector.size());
        if (myTank.shot!=null && myTank.shot.getLive()==true) {
            //System.out.println("子弹绘制了");
           // System.out.println(myTank.shot.getX()+"   "+myTank.shot.getY());
            g.setColor(Color.yellow);
            g.drawOval(myTank.shot.getX(), myTank.shot.getY(),1,1);
            //攻击到敌方坦克
            hitTank();
        }
        repaint();
        timer.start();
    }

    private void draw_MyTank(Graphics g) {
        if ( myTank.getY()<0  ){
            drawTank(myTank.getX(), myTank.y=0, g, Color.YELLOW, myTank.getDirection());
        }else if(myTank.getY()>800){
            drawTank(myTank.getX(), myTank.y=800, g, Color.YELLOW, myTank.getDirection());
        }else if(myTank.getX()>1000){
            drawTank(myTank.x=1000, myTank.y, g, Color.YELLOW, myTank.getDirection());
        }else if(myTank.getX()<0){
            drawTank(myTank.x=0, myTank.y, g, Color.YELLOW, myTank.getDirection());
        }
        else {
            drawTank(myTank.getX(),myTank.getY(), g,Color.YELLOW,myTank.getDirection());

        }
    }

    private void draw_enemyTank(Graphics g) {
        for (int i = 0; i < enemyTanksVector.size(); i++) {
            EnemyTanks enemyTanks = enemyTanksVector.get(i);
            //画出敌方坦克 -先判断坦克是否存活
            //System.out.println("坦克数量:"+enemyTanksVector.size());
            //if (enemyTanks.tank_live) {
                //System.out.println("敌方坦克存在");
            //边界判断
            if ( enemyTanks.getY()<0  ){
                drawTank(enemyTanks.getX(), enemyTanks.y=0, g, Color.CYAN, enemyTanks.getDirection());
            }else if(enemyTanks.getY()>800){
                drawTank(enemyTanks.getX(), enemyTanks.y=800, g, Color.CYAN, enemyTanks.getDirection());
            }else if(enemyTanks.getX()>1000){
                drawTank(enemyTanks.x=1000, enemyTanks.y, g, Color.CYAN, enemyTanks.getDirection());
            }else if(enemyTanks.getX()<0){
                drawTank(enemyTanks.x=0, enemyTanks.y, g, Color.CYAN, enemyTanks.getDirection());
            }
            else {

                drawTank(enemyTanks.getX(), enemyTanks.getY(), g, Color.CYAN, enemyTanks.getDirection());
            }

            //}

        }
    }

    public void drawTank(int x,int y,Graphics g,Color color,int direction){
        g.setColor(color);
        switch (direction) {
            case 0:
            g.fill3DRect(x, y, 10, 60, false);
            g.fill3DRect(x + 30, y, 10, 60, false);
            //画矩形
            g.fill3DRect(x + 10, y + 10, 20, 40, false);
            //画圆
            g.fillOval(x + 10, y + 20, 20, 20);
            //画炮杆--起点终点
            g.drawLine(x + 20, y + 30, x + 20, y);
            break;

            case 1:
                g.fill3DRect(x, y, 10, 60, false);
                g.fill3DRect(x + 30, y, 10, 60, false);
                //画矩形
                g.fill3DRect(x + 10, y + 10, 20, 40, false);
                //画圆
                g.fillOval(x + 10, y + 20, 20, 20);
                //画炮杆--起点终点
                g.drawLine(x+20, y+60, x + 20, y+30);
                break;

            case 2:
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y+30, 60, 10, false);
                //画矩形
                g.fill3DRect(x +10, y + 10, 40, 20, false);
                //画圆
                g.fillOval(x + 20, y + 10, 20, 20);
                //画炮杆--起点终点
                g.drawLine(x , y + 20, x +20, y+20);
                break;

            case 3:
                g.fill3DRect(x, y, 60, 10, false);
                g.fill3DRect(x, y+30, 60, 10, false);
                //画矩形
                g.fill3DRect(x +10, y + 10, 40, 20, false);
                //画圆
                g.fillOval(x + 20, y + 10, 20, 20);
                //画炮杆--起点终点
                g.drawLine(x +30, y + 20, x +60, y+20);
                break;
        }

    }
    //攻击坦克
    public void hitTank(){
        for (int i = 0 ;i<enemyTanksVector.size();i++) {
            //拿到敌方坦克对象
            EnemyTanks tank = enemyTanksVector.get(i);
            //System.out.println(tank.x+"   "+tank.y);
            if ((tank.getDirection()==0 || tank.getDirection()==1) && (myTank.shot.y > tank.y && myTank.shot.y < tank.y+60  && myTank.shot.x >= tank.x && myTank.shot.x<tank.x+40) ) {
                //记录爆炸坦克x y坐标
                bombVector.add(new Boom(tank.x,tank.y));
                System.out.println("坦克被击中");
                //坦克状态置为死
                tank.tank_live = false;
                //清空对应坦克对象
                enemyTanksVector.remove(tank);
                //把子弹清空
                myTank.shot.isLive = false;
               // break w;
            }else if ((tank.getDirection()==2 ||tank.getDirection()==3) && (myTank.shot.y > tank.y && myTank.shot.y < tank.y+40  && myTank.shot.x > tank.x && myTank.shot.x<tank.x+60)){
                //记录爆炸坦克坐标
                bombVector.add(new Boom(tank.x,tank.y));
                //坦克状态置为死
                tank.tank_live = false;
                //清空对应坦克对象
                enemyTanksVector.remove(tank);
                //把子弹清空
                myTank.shot.isLive = false;
                //break w;
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        int keyCode = e.getKeyCode();
        //System.out.println(keyCode);
        //判断方向
        if (keyCode==KeyEvent.VK_UP){
            myTank.move(0);
        }else if (keyCode==KeyEvent.VK_DOWN){
            myTank.move(1);
        }else if (keyCode==KeyEvent.VK_LEFT){
            myTank.move(2);
        }else if (keyCode==KeyEvent.VK_RIGHT){
            myTank.move(3);
        }
        //子弹发射
        if (keyCode==KeyEvent.VK_J){
            myTank.tank_Shot();
            //new Thread(myTank).start();
        }

        this.repaint();
        //timer.start();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //判断方向
        //新建敌方坦克移动线程
        for (int i = 0; i < enemyTankThreadVector.size(); i++) {
            Thread t = enemyTankThreadVector.get(i);
            //如果线程结束才创建
            if (t.getState()== Thread.State.NEW){
                t.start();
            }
        }
        /*int fx = 0;
        System.out.println("敌方坦克移动");
        for (int i = 0; i < enemyTanksVector.size(); i++) {
            EnemyTanks tank = enemyTanksVector.get(i);
            System.out.println("敌方坦克移动");
            if (fx==0){
                tank.move(tank.getY(),0);
            }else if (fx==1){
                tank.move(tank.getY(),1);
            }else if (fx==2){
                tank.move(tank.getX(),2);
            }else if (fx==3){
                tank.move(tank.getX(),3);
            }
        }*/
        //timer.start();
    }
}
