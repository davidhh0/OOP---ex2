package gameClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class EnteringFrame extends JFrame {
    private int _win_h = 600;
    private int _win_w = 450;
    private static BufferedImage image;
    public boolean isOkay = false;
    EnteringFrame(String a){
        super(a);
        init();
    }
    public void init(){
        this.setSize(_win_h, _win_w);
//        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Ex2 - OOP");
        try {
            image  = ImageIO.read(new File("Ex2/Pokemon icons", "Wallpaper1.jpg"));
        }catch (Exception e){
            e.printStackTrace();
        }
        JPanel panel = new JPanel();
//        panel.setBackground(Color.BLACK);
        panel.setBounds(100,100,200,100);
        JTextField idField = new JTextField();
        panel.add(idField);
        JButton button = new JButton();
        button.setText("Okay");
        button.setBounds(0,0,50,50);
        panel.add(button);
        this.setContentPane(panel);


        this.setLayout(null);
        this.setResizable(false);
        this.setVisible(true);

    }

    public void paint(Graphics g){
//        g.drawImage(image,0,0,this.getWidth(),this.getHeight(),this);
//        try {
//            BufferedImage logo  = ImageIO.read(new File("Ex2/Pokemon icons", "Logo.png"));
//            g.drawImage(logo,0,0,this.getWidth(),this.getHeight()/3,this);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }



    }
}
