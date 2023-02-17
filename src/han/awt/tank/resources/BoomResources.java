package han.awt.tank.resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class BoomResources {

    public static ArrayList<Icon> arrayList;

    public static URL small_url  = BoomResources.class.getResource("boom/small.png");
    public static Icon small_icon = new ImageIcon(small_url);



    public static URL big_url  = BoomResources.class.getResource("boom/big.png");
    public static Icon big_icon = new ImageIcon(big_url);
    public static URL mid_url  = BoomResources.class.getResource("boom/mid.png");
    public static Icon mid_icon = new ImageIcon(mid_url);

    public  static ArrayList getArrayLis(){
        arrayList = new ArrayList<>();
        arrayList.add(small_icon);
        arrayList.add(mid_icon);
        arrayList.add(big_icon);
        return arrayList;
    }


}
