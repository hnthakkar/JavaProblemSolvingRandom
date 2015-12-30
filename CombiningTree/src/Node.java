import java.awt.Font;
import java.awt.Graphics;

public class Node {
	enum CStatus {
		IDLE, FIRST, SECOND, RESULT, ROOT
	};
	static final int widthOfNode=60;
	static final int heightOfLine=15;
	static final int heightOfNode=heightOfLine*4;
	int stopId=-1;

	boolean locked;
	CStatus cStatus;
	int firstValue, secondValue;
	int result;
	Node parent;

	public Node() {
		cStatus = CStatus.ROOT;
		locked = false;
	}

	public Node(Node myParent) {
		parent = myParent;
		cStatus = CStatus.IDLE;
		locked = false;
	}

	synchronized boolean precombine() throws InterruptedException,
			PanicException {
		int threadId=Integer.parseInt((Thread.currentThread().getName()));
		while (locked){
			GraphicalMain.setStatus(threadId, "Pre-Combining:waiting");
			wait();
			GraphicalMain.setStatus(threadId, "Pre-Combining");
		}
		switch (cStatus) {
		case IDLE:
			cStatus = CStatus.FIRST;
			return true;
		case FIRST:
			locked = true;
			cStatus = CStatus.SECOND;
			return false;
		case ROOT:
			return false;
		default:
			throw new PanicException("unexpected Node state" + cStatus);
		}
	}

	synchronized int combine(int combined) throws PanicException,
			InterruptedException {
		int threadId=Integer.parseInt((Thread.currentThread().getName()));
		while (locked){
			GraphicalMain.setStatus(threadId, "Combining:waiting");
			wait();
			GraphicalMain.setStatus(threadId, "Combining");
		}
		locked = true;
		firstValue = combined;
		switch (cStatus) {
		case FIRST:
			return firstValue;
		case SECOND:
			return firstValue + secondValue;
		default:
			throw new PanicException("unexpected Node state " + cStatus);
		}
	}

	synchronized int op(int combined) throws PanicException,
			InterruptedException {
		int threadId=Integer.parseInt((Thread.currentThread().getName()));
		switch (cStatus) {
		case ROOT:
			int prior = result;
			result += combined;
			return prior;
		case SECOND:
			secondValue = combined;
			locked = false;
			notifyAll(); // wake up waiting threads
			while (cStatus != CStatus.RESULT){
				GraphicalMain.setStatus(threadId, "Operation:waiting");
				wait();
				GraphicalMain.setStatus(threadId, "Operation");
			}
			locked = false;
			notifyAll();
			cStatus = CStatus.IDLE;
			return result;
		default:
			throw new PanicException("unexpected Node state");
		}
	}

	synchronized void distribute(int prior) throws PanicException {
		switch (cStatus) {
		case FIRST:
			cStatus = CStatus.IDLE;
			locked = false;
			break;
		case SECOND:
			result = prior + firstValue;
			cStatus = CStatus.RESULT;
			break;
		default:
			throw new PanicException("unexpected Node state");
		}
		notifyAll();
	}

	public void drawNode(Graphics g, int leftOffset, int topOffset){
    	g.setFont(new Font(Font.SERIF, 0, 12));
    	g.drawRect(leftOffset, topOffset, widthOfNode, heightOfLine);
    	g.drawRect(leftOffset, topOffset+heightOfLine, widthOfNode/2, heightOfLine);
        g.drawRect(leftOffset, topOffset+2*heightOfLine, widthOfNode/2, heightOfLine);
        g.drawRect(leftOffset+widthOfNode/2, topOffset+heightOfLine, widthOfNode/2, heightOfLine);
        g.drawRect(leftOffset+widthOfNode/2, topOffset+2*heightOfLine, widthOfNode/2, heightOfLine);
        g.drawRect(leftOffset, topOffset+3*heightOfLine, widthOfNode, heightOfLine);
        g.drawString((""+cStatus).substring(0, 1), leftOffset+widthOfNode/4-5, topOffset+2*heightOfLine-2);
        g.drawString(""+result, leftOffset+3*widthOfNode/4-6, topOffset+2*heightOfLine-2);
        g.drawString(""+firstValue, leftOffset+widthOfNode/4-5, topOffset+3*heightOfLine-2);
        g.drawString(""+secondValue, leftOffset+3*widthOfNode/4-6, topOffset+3*heightOfLine-2);
        g.drawString(""+(locked==true?1:0), leftOffset+widthOfNode/2-2, topOffset+4*heightOfLine-2);
        if(stopId!=-1){
        	g.drawString(GraphicalMain.ABCD.charAt(stopId)+"stops", leftOffset-widthOfNode/2, topOffset-20);
		}
    }
}
