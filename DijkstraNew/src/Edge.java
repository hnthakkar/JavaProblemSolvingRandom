/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Linh
 */
enum EdgeType {
    SINGLE, FIRST, SECOND
}

public class Edge {
    //For draw
    EdgeType edgeType;

    //Data structure
    private Vertical tail;
    private Vertical head;
    private int length;
    State edgeState;

    public Edge(Vertical head, Vertical tail, int length) {
        this.tail = tail;
        this.head = head;
        this.length = length;
        this.edgeType = EdgeType.SINGLE;
        this.edgeState = State.UNLABELED;
    }

    @Override
    public String toString() {
        return "Edge{" + "edgeType=" + edgeType + "length=" + length + '}';
    }

    public Vertical getHead() {
        return head;
    }

    public void setHead(Vertical head) {
        this.head = head;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Vertical getTail() {
        return tail;
    }

    public void setTail(Vertical tail) {
        this.tail = tail;
    }
    
}