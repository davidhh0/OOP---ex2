package gameClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class EnteringFrame extends JFrame {
    private int _win_h = 600;
    private int _win_w = 450;
    private static BufferedImage image;
    public boolean isOkay = false;
    private JFrame frame;
    public int _id = -1;
    public int _sce = -1;
    EnteringFrame(String a){
        super(a);
        init();
    }
    public void init(){
        frame=this;
        this.setSize(_win_h, _win_w);
        BufferedImage imageWallpaper;
        JPanel contentPane = new JPanel() {
             public void paintComponent(Graphics g) {
                 Image img = Toolkit.getDefaultToolkit().getImage(
                         String.valueOf(new File("Pokemon icons", "Wallpaper1.jpg")));
                 g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
                 g.setColor(new Color(97,198,207));
                 g.fillRoundRect(_win_w/2 - 30,_win_h/2 -90,250,  160,20,20);
                 g.setColor(new Color(46,103,161));
                 g.drawRoundRect(_win_w/2 - 30, _win_h/2 -90 , 250, 160, 20, 20);
             }
            };
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JButton okButton=new JButton("OK");
        okButton.setBounds(this._win_w/2 + 70,this._win_h/2 + 20 ,140, 40);
        //enter name label
        JLabel idLabel = new JLabel();
        idLabel.setText("Enter ID :");
        idLabel.setBounds(this._win_w/2-20, this._win_h/2 -110, 100, 100);

        //textfield to enter id
        JTextField idField= new JTextField();
        idField.setBounds(this._win_w/2 + 80, this._win_h/2 - 70, 130, 30);


        JLabel scenarioLabel = new JLabel();
        scenarioLabel.setText("Enter scenario :");
        scenarioLabel.setBounds(this._win_w/2-20, this._win_h/2 - 60, 100, 100);

        //textfield to enter scenario
        JTextField scenarioField= new JTextField();
        scenarioField.setBounds(this._win_w/2 + 80, this._win_h/2 - 20, 130, 30);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                String sce = scenarioField.getText();
                //PROBLEM
                if(id.length()!=9 || Integer.parseInt(sce)<0 || Integer.parseInt(sce)>23){

                }
                else{
                    int finalID = Integer.parseInt(id);
                    int finalSce = Integer.parseInt(sce);
                    _id = finalID;
                    _sce = finalSce;
                    frame.setVisible(false);
                }
            }
        });



        this.add(idField);
        this.add(idLabel);
        this.add(scenarioField);
        this.add(scenarioLabel);
        this.add(okButton);
//        this.setSize(300,300);
        this.setLayout(null);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

//    public void paint(Graphics g){
//        g.drawImage(image,0,0,this.getWidth(),this.getHeight(),this);
//        try {
//            BufferedImage logo  = ImageIO.read(new File("Ex2/Pokemon icons", "Logo.png"));
//            g.drawImage(logo,0,0,this.getWidth(),this.getHeight()/3,this);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
////
////
////
//    }

    public static void main(String[]args){
        EnteringFrame frame = new EnteringFrame("yuval");
        //frame.repaint();
    }
}