
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.lang.reflect.Array;
import java.util.*;
import java.util.Vector;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.function.BooleanSupplier;


public class ShipGUI extends JFrame implements  Runnable{
    private static final int NONE = 0, RUNNING = 1, PAUSED = 2, ENDGAME = 3, EXIT = 4;
    private int imageIndex = 0;
    private ActionPanel drawPanel;
    private JPanel menuButtons;
    private JPanel end;
    private JButton start;
    private JButton pause;
    private JButton endGame;
    private JButton exit;
    private JButton leaders;
    private JTextField state;
    private int level;
    private int counter;
    private int ballCounter;
    private int mode;
    private Ship ship;
    private ArrayList<Integer> scores;
    private ArrayList<Asteroid> asteroids;
    private ArrayList<String> names;
    private String asteroidPic = new String("Asteroid.png");
    private BufferedImage meteor;
    private Boolean collisonCheck;
    private Boolean gameEnded = false;
    public int scoreRecord = 0;
    public int ifEntry = 0;
    public String nameholder;
    public Integer score;
    public  TreeMap<Integer, String> highScores;


    public ShipGUI()
    {
        super("SpaceShooter V1");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        highScores = new TreeMap<Integer, String>(Collections.reverseOrder());

        drawPanel = new ActionPanel();
        menuButtons = new JPanel(new GridLayout(2, 3));
        end = new JPanel();
        start = new JButton("Start Game");
        pause = new JButton("Pause");
        endGame = new JButton("End Current Game");
        exit = new JButton("Exit client");
        leaders = new JButton("View Leaderboards");
        state = new JTextField("");
        asteroids = new ArrayList<Asteroid>();
        scores = new ArrayList<Integer>();
        names = new ArrayList<String>();
        level = 0;
        counter = 0;
        ballCounter = 1;

        ButtonHandler hand = new ButtonHandler();

        start.addActionListener(hand);
        pause.addActionListener(hand);
        endGame.addActionListener(hand);
        exit.addActionListener(hand);
        leaders.addActionListener(hand);

        state.setEditable(false);
        menuButtons.add(start);
        menuButtons.add(pause);
        menuButtons.add(endGame);
        menuButtons.add(leaders);
        menuButtons.add(exit);
        menuButtons.add(state);

        mode = NONE;

        this.drawPanel.setBackground(Color.BLACK);
        this.end.setBackground(Color.YELLOW);
        this.add(drawPanel,BorderLayout.CENTER);
        this.add(menuButtons, BorderLayout.SOUTH);
        this.add(end, BorderLayout.EAST);

        Thread refresher = new Thread(this);
        refresher.start();

        setSize(screenSize.width, screenSize.height);
        setVisible(true);
    }

    public void run()
    {
        while (true)
        {

            if (ifEntry == 1) {
                if (scoreRecord > 0) {
                    String nullName = new String("Player");
                    nameholder = new String();
                    nameholder = JOptionPane.showInputDialog("Sorry you have been hit! - You scored: " + scores.get(scoreRecord - 1) + ", Please enter your name for the leaderboards!");
                    if (nameholder == null) {
                        names.add(nullName);
                    }
                    else
                    {
                        names.add(nameholder);
                    }
                }
                ifEntry = 0;
                for (int i = 0; i < names.size(); i++) {
                    highScores.put(scores.get(i), names.get(i));
                }
            }

            drawPanel.repaint();
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e) {}
        }
    }

    private class Asteroid extends Ellipse2D.Double implements Runnable {

        private int size;
        private int speed;
        private boolean astrt;
        private int deltaX;
        private int deltaY;
        private int astX, astY;
        public int i;

        public Asteroid(int id){
            astrt = true;
            size = 10 + (int)(Math.random()* 130);
            speed = 10 + (int)(Math.random() * 60);
            astX = 1000;
            astY = 500;
            deltaX = -10 + (int)(Math.random() * 21);
            deltaY = -10 + (int)(Math.random() * 21);
            setFrame(astX, astY, size, size);
            i = id;
            try
            {
                meteor = ImageIO.read(new File(asteroidPic));
            }
            catch (IOException ex)
            {
                System.out.println("File not found");
            }

        }
        public void stopAst()
        {
            astrt = false;
        }

        public void run(){
            astrt = true;
            while(astrt)
            {
                try{
                    Thread.sleep(speed);
                }
                catch (InterruptedException e){}

                int oldx = (int) getX();
                int oldy = (int) getY();

                int newx = oldx + deltaX;
                if (newx + size > drawPanel.getWidth() || newx < 0)
                {
                    deltaX = -deltaX;
                }
                int newy = oldy + deltaY;
                if (newy + size > drawPanel.getHeight() || newy < 0)
                {
                    deltaY = -deltaY;
                }
                setFrame(newx, newy, size, size);

            }

        }

        public void draw(Graphics2D g2d)
        {
            g2d.drawImage(meteor, (int) getX(), (int) getY(), null);
        }

    }



    private class ButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (e.getSource() == start)
            {
                state.setText("GOODLUCK!");
                drawPanel.setMode(ShipGUI.RUNNING);

                for (int i = 0; i < 3; i++)
                {
                    Asteroid newAsteroid = new Asteroid(i);
                    asteroids.add(newAsteroid);
                    (new Thread(newAsteroid)).start();
                }
                ship = new Ship(100,100);
                ship.fillImageArray();
                (new Thread(ship)).start();

                pause.setEnabled(true);

                start.setEnabled(false);
                leaders.setEnabled(false);

            }
            else if (e.getSource() == pause)
            {
                drawPanel.setMode(ShipGUI.PAUSED);
                if ((pause.getText()).equals("Pause"))
                {
                    for (int i = 0; i < asteroids.size(); i++)
                        ( asteroids.get(i)).stopAst();
                    ship.shipStop();
                    pause.setText("Resume");
                    state.setText("GAME PAUSED");
                }
                else if ((pause.getText()).equals("Resume"))
                {
                    for (int i = 0; i < asteroids.size(); i++)
                        (new Thread(asteroids.get(i))).start();
                    (new Thread(ship)).start();
                   pause.setText("Pause");
                    drawPanel.setMode(ShipGUI.RUNNING);
                    state.setText("GAME RESUMED");

                }
                repaint();
            }
            else if (e.getSource() == endGame)
            {
                drawPanel.setMode(ShipGUI.ENDGAME);
                for (int i = 0; i < asteroids.size(); i++)
                    ((Asteroid) asteroids.get(i)).stopAst();
                asteroids.clear();
                ship.shipStop();
                repaint();
                drawPanel.setMode(ShipGUI.NONE);
                state.setText("GAME ENDED");
                start.setEnabled(true);
                leaders.setEnabled(true);
            }
            else if (e.getSource() == leaders)
            {
                StringBuilder scoreList = new StringBuilder();
                System.out.print(scores.toString());
                for (Integer treeKey : highScores.keySet()) {
                    scoreList.append(highScores.get(treeKey)).append(" --- Score: ").append(treeKey).append("\n");
                }
                JOptionPane.showMessageDialog(null, scoreList, "Leaderboards", JOptionPane.INFORMATION_MESSAGE );
            }
            else if (e.getSource() == exit)
            {
                ship.shipStop();
                for (int i = 0; i < asteroids.size(); i++)
                    ((Asteroid) asteroids.get(i)).stopAst();

                drawPanel.setMode(ShipGUI.EXIT);
                System.exit(0);
            }
        }
    }

    private class MyKeyListener extends KeyAdapter
    {
        public void keyPressed(KeyEvent e)
        {
            if (e.getKeyCode() == KeyEvent.VK_UP)
            {
                ship.increaseSpeed(1);
            }
            else if (e.getKeyCode() == KeyEvent.VK_DOWN)
            {
                ship.decreaseSpeed(1);
            }
            else if (e.getKeyCode() == KeyEvent.VK_LEFT)
            {
                imageIndex--;
                if (imageIndex == -1)
                {
                    imageIndex = 15;
                }

                ship.getDirection(imageIndex);
            }
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            {
                imageIndex++;
                if (imageIndex >= 15)
                {
                    imageIndex = 0;
                }
                ship.getDirection(imageIndex);
            }
        }
    }

    private class ActionPanel extends JPanel  {

        public ActionPanel()
        {
            addKeyListener(new MyKeyListener());
                    addMouseListener(
                    new MouseAdapter() {
                        public void mouseEntered(MouseEvent e)
                        {
                            requestFocus();
                        }
                    }
            );
        }

        public boolean isFocusable()
        {
            return true;
        }

        public void setMode(int newMode)
        {
            mode = newMode;
        }

        public Boolean checkCollision(){
            Boolean check = false;

            for (int i = 0; i < asteroids.size(); i++)
            {
                Rectangle bounds;
                bounds = asteroids.get(i).getBounds();
                if (bounds.contains(ship.getShipX() + 5, ship.getShipY() + 9))
                {
                    check = true;
                    setMode(ShipGUI.ENDGAME);
                }
            }
            return check;

        }

        public void paintComponent (Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            if (mode == ShipGUI.RUNNING) {
                collisonCheck = checkCollision();
                if (collisonCheck == false) {
                    score = (level * 100) + 100;
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("TimesRoman", Font.PLAIN, 17));
                    g2d.drawString("Space Avoider - Get to the yellow exit!", (drawPanel.getWidth() / 4) - 200, 25);
                    g2d.drawString("Level : " + (level + 1) + "", drawPanel.getWidth() / 2, 25);
                    g2d.drawString("Speed: " + ship.speed, (drawPanel.getWidth() - (drawPanel.getWidth() / 4) - 100), 25);
                    g2d.drawString("Score: " + score, (drawPanel.getWidth() - (drawPanel.getWidth() / 4)), 25);
                    if (ship.getShipX() > drawPanel.getWidth()) {
                        level++;
                        counter = 0;
                        ballCounter++;
                    }

                    if (ship.getShipX() < 0) {
                        ship.deltaX = -(ship.deltaX);

                        if (imageIndex == 12) {
                            imageIndex = 4;
                        }
                        if (imageIndex == 13) {
                            imageIndex = 3;
                        }
                        if (imageIndex == 14) {
                            imageIndex = 2;
                        }
                        if (imageIndex == 15) {
                            imageIndex = 1;
                        }
                        if (imageIndex == 11) {
                            imageIndex = 5;
                        }
                        if (imageIndex == 9) {
                            imageIndex = 7;
                        }
                        if (imageIndex == 10) {
                            imageIndex = 6;
                        }


                    }
                    if (ship.getShipY() < 0) {
                        ship.deltaY = -(ship.deltaY);

                        if (imageIndex == 1) {
                            imageIndex = 7;
                        }
                        if (imageIndex == 2) {
                            imageIndex = 6;
                        }
                        if (imageIndex == 3) {
                            imageIndex = 5;
                        }
                        if (imageIndex == 15) {
                            imageIndex = 9;
                        }
                        if (imageIndex == 14) {
                            imageIndex = 10;
                        }
                        if (imageIndex == 13) {
                            imageIndex = 11;
                        }
                        if (imageIndex == 0) {
                            imageIndex = 8;
                        }

                    }
                    if (ship.getShipY() > drawPanel.getHeight() - 50) {
                        ship.deltaY = -(ship.deltaY);

                        if (imageIndex == 8) {
                            imageIndex = 0;
                        }
                        if (imageIndex == 5) {
                            imageIndex = 3;
                        }
                        if (imageIndex == 6) {
                            imageIndex = 2;
                        }
                        if (imageIndex == 7) {
                            imageIndex = 1;
                        }
                        if (imageIndex == 9) {
                            imageIndex = 15;
                        }
                        if (imageIndex == 10) {
                            imageIndex = 14;
                        }
                        if (imageIndex == 11) {
                            imageIndex = 13;
                        }
                    }


                    g2d.drawImage(ship.getImage(imageIndex), ship.getShipX(), ship.getShipY(), this);

                    for (int i = 0; i < asteroids.size(); i++) {
                        asteroids.get(i).draw(g2d);
                    }

                    if (level >= 1) {
                        if (counter == 0) {
                            ship.setShipX(100);
                            ship.setShipY(500);
                            ship.decreaseSpeed(1);
                            counter++;
                            for (int i = 0; i < 3; i++) {
                                Asteroid newAsteroid = new Asteroid(i);
                                asteroids.add(newAsteroid);
                                (new Thread(newAsteroid)).start();
                            }
                        }

                        for (int i = 0; i < asteroids.size(); i++) {
                            asteroids.get(i).draw(g2d);
                        }
                    }
                }
            }

                else if (mode == ShipGUI.PAUSED) {
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("TimesRoman", Font.PLAIN, 17));
                    g2d.drawString("Space Avoider - Get to the Exit!", (drawPanel.getWidth() / 4) - 100, 25);
                    g2d.drawString("Level : " + (level + 1) + "", drawPanel.getWidth() / 2, 25);
                    g2d.drawString("Speed: " + ship.speed, (drawPanel.getWidth() - (drawPanel.getWidth() / 4)), 25);
                    g2d.setFont(new Font("TimesRoman", Font.PLAIN, 40));
                    g2d.drawString("PAUSED", (drawPanel.getWidth() / 2) - 100, drawPanel.getHeight() / 2);

                    g2d.drawImage(ship.getImage(imageIndex), ship.getShipX(), ship.getShipY(), this);
                    for (int i = 0; i < asteroids.size(); i++) {
                        asteroids.get(i).draw(g2d);
                    }
                }

            else if (mode == ShipGUI.ENDGAME){
                asteroids.clear();
                score = (level * 100) + 100;
                System.out.print(score);
                scores.add(score);
                scoreRecord++;
                level = 0;
                ifEntry = 1;

                start.setEnabled(true);
                leaders.setEnabled(true);
                setMode(ShipGUI.NONE);
                gameEnded = true;

            }
            else if (mode == ShipGUI.NONE)
            {
                pause.setEnabled(false);
            }

        }

    }

}
