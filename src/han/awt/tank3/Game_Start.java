package han.awt.tank3;

import javax.swing.*;

public class Game_Start {
    public static void main(String[] args) {
        Paint_panel paint_panel = new Paint_panel();
        JFrame jFrame = new JFrame("儿时回忆-经典坦克大战");
        jFrame.setBounds(300,300,1000,800);
        jFrame.add(paint_panel);
        //jFrame.addKeyListener(paint_panel);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
    }

}
