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
 * This class represents a GUI class to present a game on a graph.
 */
public class MyFrame extends JFrame {
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
    private static BufferedImage imageWallpaper;

    /**
     * Constructor for JFrame and then calls the main function init().
     *
     * @param a
     */
    MyFrame(String a) {
        super(a);
        init();
    }

    /**
     * The main function that initializes the frame.
     * The method gives the user a pleasant experience game.
     * It sets a pokemon background photo, handles resize frame and makes sure when the user wishes to close the window.
     * Furthermore, it allows the user to save a photo of the game whenever she/he wants to.
     */
    public void init() {
        this.setSize(_win_h, _win_w);
        this.setBackground(Color.BLACK);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Ex2 - OOP");
        frame = this;
        try {
            imageWallpaper = ImageIO.read(new File("Pokemon icons", "Wallpaper3.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        wasDrawn = new HashMap<>();
        MenuBar menu_bar = new MenuBar();
        Menu menu = new Menu("File");
        menu_bar.add(menu);
        this.setMenuBar(menu_bar);
        MenuItem file_save_photo = new MenuItem("Save photo");
        MenuItem file_close = new MenuItem("Close");
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

        menu.add(file_save_photo);
        menu.add(file_close);

    }

    /**
     * This function draws the graph details on the frame including:
     * time to end, number of pokemons, number of agents, logged in ID, total value of the game.
     * for every agent:
     * id of the agent, speed , value gained by the agent.
     *
     * @param g
     */
    private void drawGraphDetails(Graphics g) {
        _ar.get_info();
        int width = this.getWidth();
        String agentString = "";
        int i = 0;
        int sum = 0;
        g.setFont(new Font("Times New Roman", Font.PLAIN, width / 100));

        ArrayList<String> agentStrings = new ArrayList<>();
        if (_ar.get_info().get(0).split(",").length > 1) {
            ArrayList<CL_Agent> agentArray = (ArrayList<CL_Agent>) Arena.getAgents(_ar.get_info().get(0), _ar.getGraph());
            for (CL_Agent run : agentArray) {
                agentString = "id: " + run.getID() + ", speed: " + run.getSpeed() + ", value: " + run.getValue();
                sum += run.getValue();
                agentStrings.add(agentString);
                i++;

            }
        }
        g.setColor(new Color(97, 198, 207));
        g.fillRoundRect(5, 185, width / 8, i * 20, 20, 20);
        g.setColor(new Color(46, 103, 161));
        g.drawRoundRect(5, 185, width / 8, i * 20, 20, 20);

        for (int j = 0; j < i; j++) {
            g.drawString(agentStrings.get(j), 12, 200 + j * 15);
        }
        //For Details
        g.setColor(new Color(97, 198, 207));
        g.fillRoundRect(5, 70, width / 8, 80, 20, 20);
        g.setColor(new Color(46, 103, 161));
        g.drawRoundRect(5, 70, width / 8, 80, 20, 20);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Times New Roman", Font.BOLD, (width / 90) + 8));
        g.drawString("Arena Details", 12, 65);
        g.setFont(new Font("Times New Roman", Font.BOLD, (width / 90) + 5));
        g.drawString("Agents Details", 12, 180);
        g.setColor(new Color(46, 103, 161));
        g.setFont(new Font("Times New Roman", Font.PLAIN, (width / 90)));
        g.drawString("Time to end: " + (Ex2_Client.timeToEnd / 1000) + "s", 12, 70 + 15);
        g.drawString("Number of Agents: " + Ex2_Client._numberOfAgents, 12, 70 + 30);
        g.drawString("Number of Pokemons: " + _ar.getPokemons().size(), 12, 70 + 45);
        g.drawString("Logged in id: " + (Ex2_Client.isLogged ? Ex2_Client.TzNumber : "-1"), 12, 70 + 60);
        g.drawString("Total value: " + sum, 12, 70 + 75);

    }

    /**
     * @param component
     * @return Captured photo of the current moment.
     * this method helps to the save_photo function.
     * @throws AWTException
     */
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

    /**
     * Main function paint for JFrame class. It calls all the drawing function of this class.
     * This function draws the entire frame on a buffered graphics and
     * then let the user see the changes only when full buffered. This method allows smooth game experience.
     *
     * @param g
     */
    public void paint(Graphics g) {
        _buffer_img = createImage(this.getWidth(), this.getHeight());
        _buffer_graphics = _buffer_img.getGraphics();

        _buffer_graphics.setFont(new Font("Times New Roman", Font.BOLD, (this.getWidth() * this.getHeight()) / 50000));


        _buffer_graphics.drawImage(imageWallpaper, 0, 0, this.getWidth(), this.getHeight(), 0, 0, (int) (imageWallpaper.getWidth() * 0.85), imageWallpaper.getHeight(), this);

        int w = this.getWidth();
        int h = this.getHeight();

        //if (isResized) {
        try {
            white_buffer_img = createImage((int) (w * 0.85), this.getHeight());
            white_buffer_graphics = white_buffer_img.getGraphics();
            white_buffer_graphics.drawImage(imageWallpaper, 0, 0, (int) (imageWallpaper.getWidth() * 0.15), this.getHeight(), (int) (imageWallpaper.getWidth() * 0.85), 0, imageWallpaper.getWidth(), imageWallpaper.getHeight(), null);
            drawGraphDetails(white_buffer_graphics);
            g.drawImage(white_buffer_img, (int) (w * 0.85), 0, this);
            isResized = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        drawGraph(_buffer_graphics);

        drawAgants(_buffer_graphics);
        drawPokemons(_buffer_graphics);


        g.drawImage(_buffer_img, 0, 0, (int) (w * 0.85), h, this);


    }

    /**
     * This method draws the graph nodes and edges with auxiliary functions: drawEdge and drawNode.
     *
     * @param g
     */
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

    /**
     * This function draws the pokemons on the graph by considering the value of each one and decide the pokemon icon
     * for each value.
     *
     * @param g
     */
    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> fs = _ar.getPokemons();
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
                        BufferedImage image = ImageIO.read(new File("Pokemon icons", toRead));
                        g.drawImage(image, (int) fp.x() - r, (int) fp.y() - r, null);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        }
    }

    /**
     * This function draws the agents on the graph by considering the speed of each one and decide the agent icon
     * for each speed value.
     *
     * @param g
     */
    private void drawAgants(Graphics g) {
        List<CL_Agent> rs = _ar.getAgents();
        g.setColor(Color.red);
        int i = 0;
        while (rs != null && i < rs.size()) {
            geo_location c = rs.get(i).getLocation();
            int id = rs.get(i).getID();
            int speed = (int) rs.get(i).getSpeed();
            String toRead = "p" + speed + ".png";
            int r = 8;
            i++;
            if (c != null) {

                try {
                    BufferedImage image = ImageIO.read(new File("Pokemon icons", toRead));
                    geo_location fp = this._w2f.world2frame(c);

                    g.drawImage(image, (int) fp.x() - r, (int) fp.y() - r, null);
                    g.drawString("" + id, (int) fp.x() - r, (int) fp.y() - r);
                    g.setColor(Color.RED);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This function draws a basic nodes on the graph.
     *
     * @param n
     * @param r
     * @param g
     */
    private void drawNode(node_data n, int r, Graphics g) {
        g.setFont(null);
        g.setColor(Color.WHITE);
        geo_location pos = n.getLocation();
        geo_location fp = realLocation(pos);
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 2 * r);
    }

    /**
     * @param pos
     * @return the real location to thge given geo_location pos based on this world2frame.
     */
    public static geo_location realLocation(geo_location pos) {
        return worldtoframe.world2frame(pos);
    }

    /**
     * This function draws an edge represented by a basic line from node to node.
     *
     * @param e
     * @param g
     */
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
    }


}