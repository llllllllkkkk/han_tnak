package han.awt.tank3;

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
    //Vector<EnemyTanks> boomTanksVector = null;
    Vector<Tank> tankVector = new Vector<>();
    Vector<Shot> enemyShotVector = new Vector();
    int enemyTank_Size=3;
    //创建一个集合装载爆炸对象
    Vector<Boom> bombVector = new Vector();
    
    //爆炸图片
    Image imageSmall = null;
    Image imageMid = null;
    Image imageBig = null;

    //装载坦克移动的线程集合
    Vector<Thread> enemyTankThreadVector = new Vector<>();
    //装载我方坦克子弹线程集合
    Vector<Shot> myTankShotVector = new Vector<>();
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
            enemyShotVector.add(enemyTanks.tank_Shot());
            //爆炸坦克
            //boomTanksVector.add(enemyTanks);
            tankVector.add(enemyTanks);
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

        tankVector.add(myTank);
    }

    private void enemyTank_Shot() {
        for (int i = 0; i < enemyTanksVector.size(); i++) {
            EnemyTanks tank = enemyTanksVector.get(i);
            if (tank.shot!=null && enemyShotVector.size()<3 && tank.shot.isLive==false) {
                enemyShotVector.add(tank.tank_Shot());
            }
            //如果敌方子弹坐标在玩家坦克坐标中-让玩家坦克存活状态置为false 并且播放爆炸特效
            if ((myTank.getDirection()==0 || myTank.getDirection()==1) && (tank.shot.y > myTank.y && tank.shot.y < myTank.y+60  && tank.shot.x >= myTank.x && tank.shot.x<myTank.x+40) ) {
                System.out.println("我方坦克被击中");
                //记录爆炸坦克x y坐标
                bombVector.add(new Boom(myTank.x,myTank.y));
               // System.out.println("坦克被击中");
                //坦克状态置为死
                myTank.tank_live = false;
                //清空对应坦克对象
                myTank.x = 300;myTank.y=300;
                //把子弹清空
                tank.shot.isLive = false;
                // break w;
            }else if ((myTank.getDirection()==2 ||myTank.getDirection()==3) && (tank.shot.y > myTank.y && tank.shot.y < myTank.y+40  && tank.shot.x > myTank.x && tank.shot.x<myTank.x+60)){
                //记录爆炸坦克坐标
                bombVector.add(new Boom(myTank.x,myTank.y));
                //坦克状态置为死
                myTank.tank_live = false;
                //清空对应坦克对象
                myTank.x = 300;myTank.y=300;
                //把子弹清空
                tank.shot.isLive = false;
                //break w;
            }
        }
    }


    @Override
    public void paint(Graphics g) {

        super.paint(g);
        this.setBackground(Color.black);



       // System.out.println(myTank.getX()+"  "+myTank.getY());
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
                  //  System.out.println("爆炸结束");
                    boolean remove = bombVector.remove(boom);
                  //  System.out.println(bombVector.size());

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
        for (int i1 = 0; i1 < enemyShotVector.size(); i1++) {
            //开启子弹的任务 并存储到集合中
            Shot shot = enemyShotVector.get(i1);
            if (shot.isLive) {
                g.setColor(Color.yellow);
                g.drawOval(shot.x, shot.y, 1, 1);
            }else {
                //System.out.println("remove");
                enemyShotVector.remove(shot);
            }
        }
        //

            enemyTank_Shot();


        //画玩家子弹
        //System.out.println(bombVector.size());
        for (int i = 0; i < myTankShotVector.size(); i++) {
            Shot shot = myTankShotVector.get(i);
            if (shot!=null && shot.getLive()==true) {
                //System.out.println("子弹绘制了");
                // System.out.println(myTank.shot.getX()+"   "+myTank.shot.getY());
                g.setColor(Color.yellow);
                g.drawOval(shot.getX(), shot.getY(),1,1);
                //检测是否攻击到敌方坦克
                hitTank();
                //子弹消失
                //子弹碰撞之后从集合中删除不再绘画
                //System.out.println(myTank.shot.getX()+"---"+myTank.shot.getY());

            }
            //System.out.println(myTank.shot.isLive);

            //xxx
//            System.out.println(myTank.shot.isLive+"..."+myTankShotVector.size());
           // System.out.println(mytankShot==shot);
            if (mytankShot !=null && mytankShot.isLive==false){
                System.out.println("子弹碰到边界消失");
                myTankShotVector.remove(shot);
            }

        }


        repaint();
        timer.start();
    }

    private void draw_MyTank(Graphics g) {
        for (Tank tank : tankVector) {

            //当enemyTank不是该当前tank对象时候才进行体积碰撞
            if (tank!=myTank) {
                //System.out.println("enemTank:"+enemyTanks.x+"   "+enemyTanks.y);
                //System.out.println("tank:"+tank.x+"   "+tank.y);
                //先判断其他坦克的移动方向
                if (tank.getDirection()==0 || tank.getDirection()==1){
                    //判断体积碰撞
                        //aaa
                        if (myTank.getDirection()==0  ){
                            if (  (myTank.x >tank.x && myTank.x < tank.x+40) &&  (myTank.y>tank.y && myTank.y < tank.y+60) || (myTank.x+40 >tank.x && myTank.x+40 < tank.x+40) &&  (myTank.y>tank.y && myTank.y < tank.y+60)){
                               // System.out.println("碰撞1");
                                myTank.y = myTank.y+2;
                            }
                        }else if(myTank.getDirection()==1){
                            if (  (myTank.x >tank.x && myTank.x < tank.x+40) &&  (myTank.y+60>tank.y && myTank.y+60 < tank.y+60) || (myTank.x+40 >tank.x && myTank.x+40 < tank.x+40) &&  (myTank.y+60>tank.y && myTank.y+60 < tank.y+60)){
                               // System.out.println("碰撞2");
                                myTank.y = myTank.y-2;
                            }
                            //bbb
                        }else if(myTank.getDirection()==2){
                            if (  (myTank.x >tank.x && myTank.x < tank.x+40) &&  (myTank.y>tank.y && myTank.y < tank.y+60) || (myTank.x >tank.x && myTank.x < tank.x+40) &&  (myTank.y+40>tank.y && myTank.y+40 < tank.y+60)){
                               // System.out.println("碰撞3");
                                myTank.x= myTank.x+2;
                            }

                        }else if(myTank.getDirection()==3){
                            if (  (myTank.x+60 >tank.x && myTank.x+60 < tank.x+40) &&  (myTank.y>=tank.y && myTank.y <= tank.y+60) || (myTank.x+60 >tank.x && myTank.x+60 < tank.x+40) &&  (myTank.y+40>tank.y && myTank.y+40 < tank.y+60)){
                                //System.out.println("碰撞4");
                                myTank.x= myTank.x-2;
                            }

                        }
                    //System.out.println(myTank.x +"  "+myTank.y);

                }else if (tank.getDirection()==2 || tank.getDirection()==3){

                    if (myTank.getDirection()==0  ){
                        if (  (myTank.x >tank.x && myTank.x < tank.x+60) &&  (myTank.y>tank.y && myTank.y < tank.y+40) || (myTank.x+40 >tank.x && myTank.x+40 < tank.x+60) &&  (myTank.y>tank.y && myTank.y < tank.y+40)){
                            //System.out.println("碰撞1");
                            myTank.y = myTank.y+2;
                        }
                    }else if(myTank.getDirection()==1){
                        if (  (myTank.x >tank.x && myTank.x < tank.x+60) &&  (myTank.y+60>tank.y && myTank.y+60 < tank.y+40) || (myTank.x+40 >tank.x && myTank.x+40 < tank.x+60) &&  (myTank.y+60>tank.y && myTank.y+60 < tank.y+40)){
                            //System.out.println("碰撞2");
                            myTank.y = myTank.y-2;
                        }
                    }else if(myTank.getDirection()==2){
                        if (  (myTank.x >tank.x && myTank.x < tank.x+60) &&  (myTank.y>tank.y && myTank.y < tank.y+40) || (myTank.x >tank.x && myTank.x < tank.x+60) &&  (myTank.y+40>tank.y && myTank.y+40 < tank.y+40)){
                            //System.out.println("碰撞3");
                            myTank.x= myTank.x+2;
                        }

                    }else if(myTank.getDirection()==3){
                        if (  (myTank.x+60 >tank.x && myTank.x+60 < tank.x+60) &&  (myTank.y>=tank.y && myTank.y <= tank.y+40) || (myTank.x+60 >tank.x && myTank.x+60 < tank.x+60) &&  (myTank.y+40>tank.y && myTank.y+40 < tank.y+40)){
                           // System.out.println("碰撞4");
                            myTank.x= myTank.x-2;
                        }

                    }
                }


            }

        }

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
            //体积碰撞

            for (Tank tank : tankVector) {

                //当enemyTank不是该当前tank对象时候才进行体积碰撞
                if (tank != enemyTanks) {
                    //System.out.println("enemTank:"+enemyTanks.x+"   "+enemyTanks.y);
                    //System.out.println("tank:"+tank.x+"   "+tank.y);
                    //先判断其他坦克的移动方向
                    if (tank.getDirection() == 0 || tank.getDirection() == 1) {
                        //判断体积碰撞

                        if (enemyTanks.getDirection() == 0) {
                            if ((enemyTanks.x > tank.x && enemyTanks.x < tank.x + 40) && (enemyTanks.y > tank.y && enemyTanks.y < tank.y + 60) || (enemyTanks.x + 40 > tank.x && enemyTanks.x + 40 < tank.x + 40) && (enemyTanks.y > tank.y && enemyTanks.y < tank.y + 60)) {
                              //  System.out.println("碰撞1");
                                enemyTanks.y = enemyTanks.y + 2;
                            }
                        } else if (enemyTanks.getDirection() == 1) {
                            if ((enemyTanks.x > tank.x && enemyTanks.x < tank.x + 40) && (enemyTanks.y + 60 > tank.y && enemyTanks.y + 60 < tank.y + 60) || (enemyTanks.x + 40 > tank.x && enemyTanks.x + 40 < tank.x + 40) && (enemyTanks.y + 60 > tank.y && enemyTanks.y + 60 < tank.y + 60)) {
                               // System.out.println("碰撞2");
                                enemyTanks.y = enemyTanks.y - 2;
                            }
                        } else if (enemyTanks.getDirection() == 2) {
                            if ((enemyTanks.x > tank.x && enemyTanks.x < tank.x + 40) && (enemyTanks.y > tank.y && enemyTanks.y < tank.y + 60) || (enemyTanks.x > tank.x && enemyTanks.x < tank.x + 40) && (enemyTanks.y + 40 > tank.y && enemyTanks.y + 40 < tank.y + 60)) {
                               // System.out.println("碰撞3");
                                enemyTanks.x = enemyTanks.x + 2;
                            }

                        } else if (enemyTanks.getDirection() == 3) {
                            if ((enemyTanks.x + 60 > tank.x && enemyTanks.x + 60 < tank.x + 40) && (enemyTanks.y >= tank.y && enemyTanks.y <= tank.y + 60) || (enemyTanks.x + 60 > tank.x && enemyTanks.x + 60 < tank.x + 40) && (enemyTanks.y + 40 > tank.y && enemyTanks.y + 40 < tank.y + 60)) {
                              //  System.out.println("碰撞4");
                                enemyTanks.x = enemyTanks.x - 2;
                            }

                        }
                        //System.out.println(enemyTanks.x +"  "+enemyTanks.y);

                    } else if (tank.getDirection() == 2 || tank.getDirection() == 3) {

                        if (enemyTanks.getDirection() == 0) {
                            if ((enemyTanks.x > tank.x && enemyTanks.x < tank.x + 60) && (enemyTanks.y > tank.y && enemyTanks.y < tank.y + 40) || (enemyTanks.x + 40 > tank.x && enemyTanks.x + 40 < tank.x + 60) && (enemyTanks.y > tank.y && enemyTanks.y < tank.y + 40)) {
                               // System.out.println("碰撞1");
                                enemyTanks.y = enemyTanks.y + 2;
                            }
                            //zzz
                        } else if (enemyTanks.getDirection() == 1) {
                            if ((enemyTanks.x > tank.x && enemyTanks.x < tank.x + 60) && (enemyTanks.y + 60 > tank.y && enemyTanks.y + 60 < tank.y + 40) || (enemyTanks.x + 40 > tank.x && enemyTanks.x + 40 < tank.x + 60) && (enemyTanks.y + 60 > tank.y && enemyTanks.y + 60 < tank.y + 40)) {
                              //  System.out.println("碰撞2");
                                enemyTanks.y = enemyTanks.y - 2;
                            }
                        } else if (enemyTanks.getDirection() == 2) {
                            if ((enemyTanks.x + 60 > tank.x && enemyTanks.x + 60< tank.x + 60) && (enemyTanks.y > tank.y && enemyTanks.y < tank.y + 40) || (enemyTanks.x > tank.x && enemyTanks.x < tank.x + 60) && (enemyTanks.y + 40 > tank.y && enemyTanks.y + 40 < tank.y + 40)) {
                              //  System.out.println("碰撞3");
                                enemyTanks.x = enemyTanks.x + 2;
                            }

                        } else if (enemyTanks.getDirection() == 3) {
                            if ((enemyTanks.x + 60 > tank.x && enemyTanks.x + 60 < tank.x + 60) && (enemyTanks.y >= tank.y && enemyTanks.y <= tank.y + 40) || (enemyTanks.x + 60 > tank.x && enemyTanks.x + 60 < tank.x + 60) && (enemyTanks.y + 40 > tank.y && enemyTanks.y + 40 < tank.y + 40)) {
                              //  System.out.println("碰撞4");
                                enemyTanks.x = enemyTanks.x - 2;
                            }

                        }


                    }

                }
            }

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
               // System.out.println("坦克被击中");
                //坦克状态置为死
                tank.tank_live = false;
                //清空对应坦克对象
                enemyTanksVector.remove(tank);
                //把子弹清空
                myTank.shot.isLive = false;
                //把子弹从集合中删除
                myTankShotVector.remove(mytankShot);
                System.out.println("子弹碰到敌方坦克消失");

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
                //把子弹从集合中删除
                myTankShotVector.remove(mytankShot);
                System.out.println("子弹碰到敌方坦克消失");
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //我的坦克子弹shot
    Shot mytankShot;

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
            if (myTank.shot!= null && myTank.shot.isLive) {
                //我的坦克发射子弹
                MyTank shots = new MyTank(myTank.getDirection(),myTank.x,myTank.y,4);
                mytankShot = new Shot(shots);
                myTankShotVector.add(mytankShot);

                Thread t = new Thread(mytankShot);

                //=================开始发射线程
                t.start();

                //myTank.myTankShot(myTank,mytankShot,myTankShotVector,enemyShotVector);



                /*int i=0;
                while (i<10){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    i--;
                }*/

                /*System.out.println(mytankShot.isLive);
                mytankShot.isLive = false;
                System.out.println(mytankShot.isLive);*/
            }
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
