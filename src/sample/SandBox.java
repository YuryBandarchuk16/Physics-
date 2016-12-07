package sample;

import sample.matchParser.MatchParser;
import sample.matchParser.ParserRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Created by YuryBandarchuk on 06.12.16.
 */
public class SandBox extends JFrame {

    private MatchParser matchParser;

    private final double X_RANGE = 50.0;
    private final double Y_RANGE = 100.0;
    private final double RANGE_STEP = 0.001;

    public boolean graphIsReadyToBeDrawn = false;

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
    }

    double convertX(double x) {
        return x / X_RANGE * 400.0 + 400.0;
    }

    double convertY(double y) {
        return 800 - (y / Y_RANGE * 400.0 + 400.0);
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


    public void paint(Graphics g) {
        super.paint(g);
        this.graphics = (Graphics2D) g;
        drawAxes();
        if (graphIsReadyToBeDrawn) {
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
                drawLittleSegment(previousX, previousY, convertX(currentX), convertY(currentY), Color.GREEN, true);
                previousX = convertX(currentX);
                previousY = convertY(currentY);
            }
        }
    }

    public void start() throws InterruptedException {
        getContentPane().add(jPanel);
        setVisible(true);
    }

    public void close()  {
        setVisible(false);
        dispose();
    }

}
