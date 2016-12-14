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

    public Color ballColor, graphColor;

    private final double REM_ENERGY = 0.95;
    private final double ADD_ENERGY = 1.10;

    private MatchParser matchParser;

    private final double X_RANGE = 10.0;
    private final double Y_RANGE = 100.0;
    private final double RANGE_STEP = 0.07;

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
        if (cx != null) {
            cx.clear();
        }
        if (cy != null) {
            cy.clear();
        }
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
                drawLittleSegment(cx.get(i - 1), cy.get(i - 1), cx.get(i), cy.get(i), graphColor, true);
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
            double that_y = 0.0;
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

                ball.hitsCount += 1;

                if (ball.hitsCount == 10) {
                    ball.hitsCount = 0;
                    ball.setVy(ball.getVy() * ADD_ENERGY);
                    ball.setVx(ball.getVx() * ADD_ENERGY);
                }

                double x0 = -ball.getVx();
                double y0 = -ball.getVy();
                double lx = that_x - 0.0001;
                double rx = that_x + 0.0001;
                double bx = 0.0, by = 0.0;
                matchParser.setVariable("X", lx);
                try {
                    bx = matchParser.Parse(ParserRunner.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                matchParser.setVariable("X", rx);
                try {
                    by = matchParser.Parse(ParserRunner.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bx = convertY(bx);
                by = convertY(by);
                lx = convertX(lx);
                rx = convertX(rx);
                double oy = by - bx;
                double ox = rx - lx;

                double ax = x0;
                double ay = y0;

                double scalar = ox * ax + oy * ay;
                scalar /= (getLen(ax, ay) * getLen(ox, oy));
                double angle = Math.acos(scalar);

                // не забудь потратить энергию

                double prevLen = getLen(x0, y0);

                double new_vx = getRotatedX(x0, y0, angle * 2.0 + Math.PI);
                double new_vy = getRotatedY(x0, y0, angle * 2.0 + Math.PI);
                double cur_len = getLen(new_vx, new_vy);
                new_vx /= cur_len;
                new_vy /= cur_len;

                new_vx = (new_vx * cur_len * REM_ENERGY);
                new_vy = (new_vy * cur_len * REM_ENERGY);


                ball.setVx(new_vx);
                ball.setVy(new_vy);


            }
            Color currentColor = this.graphics.getColor();
            Ellipse2D.Double ellipse2D = new Ellipse2D.Double(ball.getCx(), ball.getCy(), ball.getR(), ball.getR());
            this.graphics.setColor(ballColor);
            this.graphics.fill(ellipse2D);
            this.graphics.setColor(currentColor);
        }
        for (int i = 0; i < balls.size(); i++) {
            double curR = balls.get(i).getR() + 0.00001;
            double fx = balls.get(i).getCx();
            double fy = balls.get(i).getCy();
            for (int j = i + 1; j < balls.size(); j++) {
                double sx = balls.get(j).getCx();
                double sy = balls.get(j).getCy();
                if (getDistance(fx, fy, sx, sy) <= curR) {
                    System.out.println("!");
                    double fvx = balls.get(i).getVx();
                    double fvy = balls.get(i).getVy();
                    double svx = balls.get(j).getVx();
                    double svy = balls.get(j).getVy();
                    double v1Minusv2X = fvx - svx;
                    double v1Minusv2Y = fvy - svy;
                    double frx = balls.get(i).getCx();
                    double fry = balls.get(i).getCy();
                    double srx = balls.get(j).getCx();
                    double sry = balls.get(j).getCy();
                    double deltaRx = frx - srx;
                    double deltaRy = fry - sry;
                    double value = getScalarProduct(v1Minusv2X, v1Minusv2Y, deltaRx, deltaRy);
                    value /= (0.7 * curR * curR);
                    deltaRx *= value;
                    deltaRy *= value;
                    balls.get(i).setVx(fvx - deltaRx);
                    balls.get(i).setVy(fvy - deltaRy);
                    balls.get(j).setVx(svx + deltaRx);
                    balls.get(j).setVy(svy + deltaRy);
                }
            }
        }
    }

    public double getScalarProduct(double ax, double ay, double bx, double by) {
        return (ax * bx + ay * by);
    }


    public double getLen(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }

    public double getRotatedX(double x, double y, double alpha) {
        return x * Math.cos(alpha) - y * Math.sin(alpha);
    }

    public double getRotatedY(double x, double y, double alpha) {
        return x * Math.sin(alpha) + y * Math.cos(alpha);
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
