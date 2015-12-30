/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.util.ArrayList;

/**
 *
 * @author Linh
 */

public class jGraphPanel extends JPanel{

    public ArrayList<Vertical> listVerticals;
    public ArrayList<Edge> listEdges;
    public boolean showResult = false;
    public static final int radius = 25;
    private static final int delta = 7;
    private static final Color Normal = Color.GREEN;
    private static final Color Mark = Color.RED;
    private static final Color Scan = Color.BLUE;
    private static final Color Text = Color.BLACK;

    public jGraphPanel() {
        super();
        this.listVerticals = new ArrayList<Vertical>();
        this.listEdges = new ArrayList<Edge>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Edge edge : listEdges)
            drawEdge(g, edge);
        for (Vertical Vertical : listVerticals)
            drawVertical(g, Vertical);
    }

    public void drawVertical(Graphics g, Vertical vertical) {
        Color c = g.getColor();
        if (vertical.state == State.LABELED)
            g.setColor(jGraphPanel.Mark);
        else if (!this.showResult && vertical.state == State.SCANNED)
            g.setColor(jGraphPanel.Scan);
        else
            g.setColor(jGraphPanel.Normal);
        
        g.fillOval(vertical.getX_cor(), vertical.getY_cor(), radius, radius);
        g.setColor(Text);
        g.drawString(Integer.toString(vertical.getData()), vertical.getX_cor() + 2*radius/5, vertical.getY_cor() + 3*radius/4);
        //if (!this.showResult && Vertical.getKey() > 0)
        //    g.drawString(Integer.toString(Vertical.getKey()), Vertical.getX_cor() + radius, Vertical.getY_cor());
        g.setColor(c);
    }

    public void drawEdge(Graphics g, Edge edge) {
        Color c = g.getColor();
        if (edge.edgeState == State.LABELED)
            g.setColor(jGraphPanel.Mark);
        else if (edge.edgeState == State.SCANNED)
            g.setColor(jGraphPanel.Scan);
        else
            g.setColor(Normal);

        //for edge
        double x1 = edge.getHead().getX_cor() + radius/2;
        double x2 = edge.getTail().getX_cor() + radius/2;
        double y1 = edge.getHead().getY_cor() + radius/2;
        double y2 = edge.getTail().getY_cor() + radius/2;
        double sin = this.sinEgde(x1, y1, x2, y2);
        double cos = this.cosEdge(x1, y1, x2, y2);
        boolean mark = false;

        if (sin * cos >= 0)
            mark = true;
        sin = Math.abs(sin);
        cos = Math.abs(cos);

        if (edge.edgeType == EdgeType.FIRST) {
            if (mark) {
                x1 += delta * sin;
                x2 += delta * sin;
            } else {
                x1 -= delta * sin;
                x2 -= delta * sin;
            }
            y1 -= delta * cos;
            y2 -= delta * cos;
        } else if (edge.edgeType == EdgeType.SECOND) {
            if (mark) {
                x1 -= delta * sin;
                x2 -= delta * sin;
            } else {
                x1 += delta * sin;
                x2 += delta * sin;
            }
            y1 += delta * cos;
            y2 += delta * cos;
        }
        g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);

        //for arrow
        double x3 = x2 + 0.5 * delta * sin;
        double x4 = x2 - 0.5 * delta * sin;
        if (!mark) {
            double x = x4;
            x4 = x3;
            x3 = x;
        }
        double x5 = x2;
        double y3 = y2 - 0.5 * delta * cos;
        double y4 = y2 + 0.5 * delta * cos;
        double y5 = y2;
        double xtb = Math.abs(x2 - (x1 + x2)/2);
        double ytb = Math.abs(y2 - (y1 + y2)/2);

        if (x2 > x1) {
            x3 -= ((delta) * cos + xtb);
            x4 -= ((delta) * cos + xtb);
            x5 -= xtb;
        } else {
            x3 += ((delta) * cos + xtb);
            x4 += ((delta) * cos + xtb);
            x5 += xtb;
        }
        if (y2 > y1) {
            y3 -= ((delta) * sin + ytb);
            y4 -= ((delta) * sin + ytb);
            y5 -= ytb;
        } else {
            y3 += ((delta) * sin + ytb);
            y4 += ((delta) * sin + ytb);
            y5 += ytb;
        }

        g.drawLine((int) x5, (int) y5, (int) x3, (int) y3);
        g.drawLine((int) x5, (int) y5, (int) x4, (int) y4);
        g.setColor(Text);
        g.drawString(Integer.toString(edge.getLength()), (int) (x1+x2)/2, (int) (y1+y2)/2);
        g.setColor(c);
    }

    public Vertical checkInVertical(int x, int y) {
        for (Vertical vertical : listVerticals) {
            int xn = vertical.getX_cor() - jGraphPanel.radius/2;
            int yn = vertical.getY_cor() - jGraphPanel.radius/2;
            if (xn < x && yn < y && xn+radius > x && yn+radius >y)
                return vertical;
        }
        return null;
    }

    EdgeType checkEdge(Vertical head, Vertical tail) {
        for (Edge edge : listEdges) {
            Vertical eHead = edge.getHead();
            Vertical eTail = edge.getTail();
            if (eHead.equals(tail) && eTail.equals(head)) {
                edge.edgeType = EdgeType.FIRST;
                return EdgeType.SECOND;
            } else if (eHead.equals(head) && eTail.equals(tail)) {
                EdgeType temp = edge.edgeType;
                eHead.outgoingEdges.remove(edge);
                eTail.incomingEdges.remove(edge);
                this.listEdges.remove(edge);
                return temp;
            }
        }
        return EdgeType.SINGLE;
    }

    public double sinEgde(double x1, double y1, double x2, double y2) {
        return ((y2-y1)/Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)));
    }

    public double cosEdge(double x1, double y1, double x2, double y2) {
        return ((x2-x1)/Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1)));
    }

}

