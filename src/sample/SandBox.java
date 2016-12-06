package sample;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

/**
 * Created by YuryBandarchuk on 06.12.16.
 */
public class SandBox extends JFrame {

    private Dimension dimension;
    private JPanel jPanel;
    private Graphics2D graphics;

    public SandBox(Dimension dimension) {
        this.dimension = dimension;
        setTitle("SandBox");
        setSize(dimension);
        setBounds(0, 0, dimension.width, dimension.height);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        jPanel = new JPanel();
        jPanel.setLayout(null);
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



    public void drawAxes() {
        drawLine(0, 400, 800, 400, Color.BLACK, true);
        drawLine(400, 0, 400, 800, Color.BLACK, true);
    }


    public void paint(Graphics g) {
        super.paint(g);
        this.graphics = (Graphics2D) g;
        drawAxes();
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
