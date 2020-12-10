package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.swing.JOptionPane;


/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 */
public class MyFrame extends JFrame implements MouseListener, MouseMotionListener, ActionListener {
    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    public static gameClient.util.Range2Range worldtoframe;
    private Image _buffer_img;
    private Graphics _buffer_graphics;
    private Image white_buffer_img;
    private Graphics white_buffer_graphics;
    private static boolean isResized = true;
    private int _win_h = 1000;
    private int _win_w = 600;
    private JFrame frame;
    private HashMap<edge_data, Boolean> wasDrawn;
    private int nRadius = 6;
    private int _numberOfAgents = 1;


    MyFrame(String a) {
        super(a);
        init();
    }

    public void init() {
        this.setSize(_win_h, _win_w);
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Ex2 - OOP");
        frame = this;


        wasDrawn = new HashMap<>();
        MenuBar menu_bar = new MenuBar();
        Menu menu = new Menu("File");
        Menu data = new Menu("Data");
        menu_bar.add(menu);
        menu_bar.add(data);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setMenuBar(menu_bar);
        MenuItem file_save_photo = new MenuItem("Save photo");
        MenuItem file_close = new MenuItem("Close");
        MenuItem file_restart = new MenuItem("Restart(not working)");
        MenuItem data_nodes = new MenuItem("Nodes");
        Toolkit.getDefaultToolkit().setDynamicLayout(false);
        file_save_photo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Save photo clicked");
                try { //saves the image
                    // retrieve image
                    String nameOfPhoto = JOptionPane.showInputDialog("Enter a photo name: ");
                    //nameOfPhoto = nameOfPhoto;
                    boolean isNull = nameOfPhoto == null;
                    if (!isNull) {
                        nameOfPhoto = nameOfPhoto + ".png";
                        BufferedImage bi = getScreenShot(frame.getContentPane());
                        File outputfile = new File(nameOfPhoto);
                        ImageIO.write(bi, "png", outputfile);
                    }
                } catch (AWTException | IOException e1) {
                    e1.printStackTrace();
                }

            }
        });
        file_close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = JOptionPane.showConfirmDialog(frame, "Are you sure?", "Closing..", JOptionPane.YES_NO_OPTION);
                if (i == 0) {
                    frame.dispose();
                    System.exit(0);
                }
            }
        });

        file_restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int i = JOptionPane.showConfirmDialog(frame, "Are you sure?", "Restarting..", JOptionPane.YES_NO_OPTION);
                if (i == 0) {
                    Thread client = new Thread(new Ex2_Client());
                    client.start();
                    //Ex2_Client.stop();
                    //init();
                }
            }
        });
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int i = JOptionPane.showConfirmDialog(frame, "Are you sure?", "Closing..", JOptionPane.YES_NO_OPTION);
                if (i == 0) {
                    frame.dispose();
                    System.exit(0);
                }
            }

        });


        this.getRootPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // This is only called when the user releases the mouse button.

                int newHeight = e.getComponent().getHeight();
                int newWidth = e.getComponent().getWidth();
                if (frame.isActive()) {
                    try {
                        isResized = true;
                        frame.getContentPane().setPreferredSize(new Dimension(newWidth, newHeight));
                        pack();
                        updateFrame();
                    } catch (Exception b) {
                        b.printStackTrace();
                    }
                }

            }
        });


        data_nodes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        menu.add(file_save_photo);
        menu.add(file_restart);
        menu.add(file_close);
        data.add(data_nodes);


    }

    private void drawGraphDetails(Graphics g) {
        _ar.get_info();
        int width = this.getWidth();
        int height = this.getHeight();
        String agentString = "";
        int i = 0;
        int sum=0;
        g.setFont(new Font("Times New Roman", Font.PLAIN, width / 100));
        g.setColor(Color.white);

        if (_ar.get_info().size() > 0) {
            ArrayList<CL_Agent> agentArray = (ArrayList<CL_Agent>) Arena.getAgents(_ar.get_info().get(0), _ar.getGraph());
            for (CL_Agent run : agentArray) {
                agentString = "id: " + run.getID() + ", speed: " + run.getSpeed() + ", value: " + run.getValue();
                sum+=run.getValue();
                g.drawString(agentString, 12, 200 + i * 15);
                i++;

            }
        }
        //g.setColor(Color.black);


        g.drawRoundRect(5, 70, width / 8, (int) (height / 7.5), 20, 20);

        // g.drawRoundRect((int) (width *0.85), height / 5, width / 8, height / 8, 20, 20);
        g.setFont(new Font("Times New Roman", Font.PLAIN, width / 90));


        g.drawString("Arena Details", 12, 65);
        g.drawString("Time to end: " + (Ex2_Client.timeToEnd / 10) + "ms", 12, 70 + 15);
        g.drawString("Number of Agents: " + Ex2_Client._numberOfAgents, 12, 70 + 30);
        g.drawString("Number of Pokemons: "+_ar.getPokemons().size(),12,70+45);
        g.drawString("Logged in id: "+(Ex2_Client.isLogged?"number":"null"),12,70+60);
        g.drawString("Total value: "+sum,12,70+75);
        // g.drawString("Time to end: " + (Ex2_Client.timeToEnd / 10) + "ms", (int) (width * 0.85) + 12, height / 4 + 15);

    }

    private static BufferedImage getScreenShot(Component component) throws AWTException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        Robot robot = new Robot(gd);
        Rectangle bounds = new Rectangle(component.getLocationOnScreen(), component.getSize());
        return robot.createScreenCapture(bounds);
    }

    public void update(Arena ar) {
        this._ar = ar;
        updateFrame();
    }

    private void updateFrame() {
        Range rx = new Range(20, this.getWidth() - 20);
        Range ry = new Range(this.getHeight() - 10, 150);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _ar.getGraph();
        worldtoframe = Arena.w2f(g, frame);
        _w2f = Arena.w2f(g, frame);

    }

    public void paint(Graphics g) {
        _buffer_img = createImage(this.getWidth(), this.getHeight());
        _buffer_graphics = _buffer_img.getGraphics();

        _buffer_graphics.setFont(new Font("Times New Roman", Font.BOLD, (this.getWidth() * this.getHeight()) / 50000));

//        try {
//            BufferedImage image = ImageIO.read(new File("Ex2/Pokemon icons", "true (2).png"));
//            _buffer_graphics.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        int w = this.getWidth();
        int h = this.getHeight();
        //drawGraphDetails(_buffer_graphics);

        //if (isResized) {
        try {
            BufferedImage image = ImageIO.read(new File("Ex2/Pokemon icons", "brick.jpg"));
            white_buffer_img = createImage(this.getWidth(), this.getHeight());
            white_buffer_graphics = white_buffer_img.getGraphics();
            white_buffer_graphics.drawImage(image, 0, 0, (int) (this.getWidth() * 0.2), (int) (this.getHeight() * 1), null);
            //drawGraphDetails(white_buffer_graphics);
            g.drawImage(white_buffer_img, (int) (w * 0.85), 0, this);
            isResized = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //     }

        //g.clearRect(0, 0, w, h);
        //	updateFrame();
        //  drawInfo(_buffer_graphics);
        drawGraph(_buffer_graphics);

        drawAgants(_buffer_graphics);
        drawPokemons(_buffer_graphics);


        g.drawImage(_buffer_img, 0, 0, (int) (w * 0.85), h - 10, this);


    }

    private void drawInfo(Graphics g) {
        List<String> str = _ar.get_info();
        String dt = "none";
        for (int i = 0; i < str.size(); i++) {
            g.drawString(str.get(i) + " dt: " + dt, 100, 60 + i * 20);
        }

    }

    private void drawGraph(Graphics g) {

        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while (iter.hasNext()) {
            node_data n = iter.next();
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while (itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.darkGray);
                drawEdge(e, g);
                wasDrawn.put(e, true);
            }
            g.setColor(Color.blue);
            drawNode(n, nRadius, g);
        }
    }

    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> fs = _ar.getPokemons();
        //int totalPokemons = fs.size();

        int count = 0;
        if (fs != null) {
            Iterator<CL_Pokemon> itr = fs.iterator();

            while (itr.hasNext()) {

                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r = 10;
                g.setColor(Color.green);
                if (f.getType() < 0) {
                    g.setColor(Color.orange);
                }
                if (c != null) {

                    geo_location fp = this._w2f.world2frame(c);
                    //g.fillOval(, 2 * r, 2 * r);
                    try {
                        String toRead;
                        if (f.getValue() < 6) {
                            toRead = "2.png";
                        } else if (f.getValue() < 9) {
                            toRead = "3.png";
                        } else if (f.getValue() < 10) {
                            toRead = "4.png";
                        } else if (f.getValue() < 13) {
                            toRead = "5.png";
                        } else
                            toRead = "1.png";
                        BufferedImage image = ImageIO.read(new File("Ex2/Pokemon icons", toRead));
                        g.drawImage(image, (int) fp.x() - r, (int) fp.y() - r, null);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                count++;
            }

        }
    }

    private void drawAgants(Graphics g) {
        List<CL_Agent> rs = _ar.getAgents();
        //	Iterator<OOP_Point3D> itr = rs.iterator();
        g.setColor(Color.red);
        int i = 0;
        while (rs != null && i < rs.size()) {
            geo_location c = rs.get(i).getLocation();
            //edge_data p = rs.get(i).get_curr_edge();
            int id = rs.get(i).getID();
            int speed = (int) rs.get(i).getSpeed();
            String toRead = "p" + speed + ".png";
            int r = 8;
            i++;
            if (c != null) {

                try {
                    BufferedImage image = ImageIO.read(new File("Ex2/Pokemon icons", toRead));
                    geo_location fp = this._w2f.world2frame(c);

                    g.drawImage(image, (int) fp.x() - r, (int) fp.y() - r, null);
                    g.drawString("" + id, (int) fp.x() - r, (int) fp.y() - r);
                    g.setColor(Color.RED);
                    //g.drawString("Src: " + Ex2_Client.AgentToPok.get(id).get_edge().getDest() + " Dest: " + Ex2_Client.AgentToPok.get(id).get_edge().getSrc(), (int) fp.x() - 2 * r, (int) fp.y() - 2 * r);
                    //g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private void drawNode(node_data n, int r, Graphics g) {
        g.setFont(null);
        g.setColor(Color.WHITE);
        geo_location pos = n.getLocation();
        geo_location fp = realLocation(pos);
        //geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 2 * r);
    }

    public static geo_location realLocation(geo_location pos) {
        return worldtoframe.world2frame(pos);
    }

    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.setColor(Color.WHITE);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(1.5F));
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());

        //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {


    }


    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

}