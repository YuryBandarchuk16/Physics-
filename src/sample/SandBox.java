package sample;

import sample.matchParser.MatchParser;
import sample.matchParser.ParserRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * Created by YuryBandarchuk on 06.12.16.
 */
public class SandBox extends JFrame implements Runnable {

    private MatchParser matchParser;

    private final double X_RANGE = 10.0;
    private final double Y_RANGE = 100.0;
    private final double RANGE_STEP = 0.06;

    public boolean hasParsed = false;

    public boolean graphIsReadyToBeDrawn = false;

    public ArrayList<Ball> balls;
    private ArrayList<Double> cx, cy;

    private Dimension dimension;
    private JPanel jPanel;
    private Graphics2D graphics;

    public SandBox(Dimension dimension, MatchParser matchParser) {
        this.dimension = dimension;
        this.matchParser = matchParser;
        setTitle("SandBox");
        setSize(dimension);
        setBounds(0, 0, dimension.width, dimension.height);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        jPanel = new JPanel();
        jPanel.setLayout(null);
        balls = new ArrayList<>();
        setFocusable(true);
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                balls.add(new Ball(e.getX(), e.getY()));
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
        });
    }

    private double convertXX(double x) {
        return x * X_RANGE / 400.0 - X_RANGE;
    }

    private double convertYY(double y) {
        return -(y - 800.0) * Y_RANGE / 400.0 - Y_RANGE;
    }


    private double convertX(double x) {
        return (x + X_RANGE) * 400.0 / X_RANGE;
    }

    private double convertY(double y) {
        return 800.0 - (y + Y_RANGE) * 400.0 / Y_RANGE;

    }

    public void drawLine(int x1, int y1, int x2, int y2, Color color, boolean needStroke) {
        Stroke stroke = new BasicStroke(3f);
        graphics.setColor(color);
        if (needStroke) {
            graphics.setStroke(stroke);
        }
        graphics.drawLine(x1, y1, x2, y2);
        if (needStroke) {
            graphics.setStroke(new BasicStroke(1f));
        }
    }

    public void drawLittleSegment(double x1, double y1, double x2, double y2, Color color, boolean needStroke) {
        Stroke stroke = new BasicStroke(3f);
        if (needStroke) {
            graphics.setStroke(stroke);
        }
        graphics.setColor(color);
        Line2D.Double lineToDraw = new Line2D.Double(x1, y1, x2, y2);
        graphics.draw(lineToDraw);
        if (needStroke) {
            graphics.setStroke(new BasicStroke(1f));
        }
    }

    public void drawAxes() {
        drawLine(0, 400, 800, 400, Color.BLACK, true);
        drawLine(400, 0, 400, 800, Color.BLACK, true);
    }


    public void reInit() {
        hasParsed = false;
        cx.clear();
        cy.clear();
    }

    public void paint(Graphics g) {
        super.paint(g);
        this.graphics = (Graphics2D) g;
        drawAxes();
        if (graphIsReadyToBeDrawn) {
            if (!hasParsed) {
                hasParsed = true;
                cx = new ArrayList<>();
                cy = new ArrayList<>();
                double previousX, previousY;
                previousX = previousY = -100000.0;
                for (double currentX = -X_RANGE; currentX <= X_RANGE; currentX += RANGE_STEP) {
                    matchParser.setVariable("X", currentX);
                    double currentY = 0.0;
                    try {
                        currentY = matchParser.Parse(ParserRunner.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cx.add(convertX(currentX));
                    cy.add(convertY(currentY));
                    //drawLittleSegment(previousX, previousY, convertX(currentX), convertY(currentY), Color.GREEN, true);
                    previousX = convertX(currentX);
                    previousY = convertY(currentY);
                }
            }
            for (int i = 1; i < cx.size(); i++) {
                drawLittleSegment(cx.get(i - 1), cy.get(i - 1), cx.get(i), cy.get(i), Color.CYAN, true);
            }
        }
        for (Ball ball : balls) {
            double x = convertXX(ball.getX());
            double y = ball.getY();
            matchParser.setVariable("X", x);
            double gy = 0;
            try {
                gy = matchParser.Parse(ParserRunner.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
            double minDistance = 1e10;
            double leftX = x - 0.1;
            double rightX = x + 0.1;
            double that_x = 0.0;
            for (double tempX = leftX; tempX <= rightX; tempX += 0.03) {
                gy = 0.0;
                matchParser.setVariable("X", tempX);
                try {
                    gy = matchParser.Parse(ParserRunner.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                double value = getDistance(ball.getCx(), ball.getCy(), convertX(tempX), convertY(gy));
                if (value < minDistance) {
                    minDistance = value;
                    that_x = tempX;
                }
            }
            if (minDistance <= ball.getR() + 0.7) {
                ball.setVx(0);
                ball.setVy(0);
                ball.G = 0.0;
                /*


                double x0 = ball.getVx();
                double y0 = ball.getVy();
                double lx = convertX(that_x - 0.0001);
                double rx = convertX(that_x + 0.0001);
                that_x = convertX(that_x);

                */
            }
            Color currentColor = this.graphics.getColor();
            Ellipse2D.Double ellipse2D = new Ellipse2D.Double(ball.getCx(), ball.getCy(), ball.getR(), ball.getR());
            this.graphics.setColor(ball.getColor());
            this.graphics.fill(ellipse2D);
            this.graphics.setColor(currentColor);
        }
    }

    private double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public void start() throws InterruptedException {
        getContentPane().add(jPanel);
        setVisible(true);
    }

    public void close()  {
        setVisible(false);
        dispose();
    }

    @Override
    public void run() {
        try {
            start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long currentTime = System.currentTimeMillis();
        while (true) {
            try {
                Thread.sleep(75L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long nowTime = System.currentTimeMillis();
            double delta = (double)(nowTime - currentTime) / 1000.0;
            for (Ball ball : balls) {
                ball.move(delta);
            }
            currentTime = nowTime;
            paint(this.getGraphics());
        }
    }
}
