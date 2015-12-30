import java.awt.Graphics;
import java.util.Stack;

public class CombiningTree {
	private Node[] leaf;

	public CombiningTree(int width) {
		System.out.println("Width:" + width);
		Node[] nodes = new Node[width - 1];
		nodes[0] = new Node();
		for (int i = 1; i < nodes.length; i++) {
			nodes[i] = new Node(nodes[(i - 1) / 2]);
		}
		leaf = new Node[(width + 1) / 2];
		for (int i = 0; i < leaf.length; i++) {
			leaf[i] = nodes[nodes.length - i - 1];
		}
	}

	/**
	 * set name of thread as int for thread ID
	 * 
	 * @return
	 * @throws InterruptedException
	 * @throws PanicException
	 */
	public int getAndIncrement() throws InterruptedException, PanicException {
		Stack<Node> stack = new Stack<Node>();
		int threadId=Integer.parseInt((Thread.currentThread()
				.getName()));
		Node myLeaf = leaf[threadId/2];
		Node node = myLeaf;
		// precombining phase
		GraphicalMain.setStatus(threadId, "Pre-Combining");
		while (node.precombine()) {
			node = node.parent;
		}
		Node stop = node;
		stop.stopId=threadId;
		waitOnButton(threadId,"Pre-Combine Completed");
		
		// combining phase
		GraphicalMain.setStatus(threadId,"Combining");
		node = myLeaf;
		int combined = 1;
		while (node != stop) {
			combined = node.combine(combined);
			stack.push(node);
			node = node.parent;
		}
		waitOnButton(threadId,"Combining Completed");
		
		// operation phase
		GraphicalMain.setStatus(threadId, "Operation");
		int prior = stop.op(combined);
		waitOnButton(threadId,"Operation Completed");
		
		// distribution phase
		GraphicalMain.setStatus(threadId, "Distribute");
		while (!stack.empty()) {
			node = stack.pop();
			node.distribute(prior);
		}
		GraphicalMain.setStatus(threadId, "Distribute completed");
		
		return prior;
	}

	private void waitOnButton(int threadId,String threadStatus) throws InterruptedException {
		GraphicalMain.enableButton(threadId,threadStatus);
		GraphicalMain.threadControl[threadId].acquire();
	}

	public void refreshTree(CombiningTree counter, Graphics g) {
		int height = (int) (Math.log10(leaf.length) / Math.log10(2));
		int leftOff = (int) ((GraphicalMain.X - (int) ((leaf.length - 1) * 2
				* Node.widthOfNode + Node.widthOfNode)) * 0.5);
		int topOff = 50;
		NodeAndPosition tmp[] = new NodeAndPosition[(leaf.length + 1) / 2];
		for (int i = 0; i < leaf.length; i++) {
			leaf[i].drawNode(g, (int) (leftOff + i * 1.8 * Node.widthOfNode), topOff + height * (Node.heightOfNode + Node.widthOfNode));
			
			if (i % 2 == 0) {
				tmp[i / 2] = new NodeAndPosition();
				tmp[i / 2].n = leaf[i].parent;
				tmp[i / 2].leftOff = (int) ((leftOff + i * 1.8 * Node.widthOfNode) + 0.9*Node.widthOfNode);
				tmp[i / 2].topOff = topOff + (height - 1) * (Node.heightOfNode + Node.widthOfNode);
			}
			g.drawLine((int) (leftOff + i * 1.8 * Node.widthOfNode)+Node.widthOfNode/2, topOff + height * (Node.heightOfNode + Node.widthOfNode)+Node.heightOfLine/2, tmp[i/2].leftOff+Node.widthOfNode/2, tmp[i/2].topOff+Node.heightOfNode);
			
		}
		int nodesAtThisLevel = (leaf.length + 1) / 2;
		while (nodesAtThisLevel > 0) {
			for (int i = 0; i < nodesAtThisLevel; i++) {

				tmp[i].drawNode(g);
				if(tmp[i].n.cStatus!=Node.CStatus.ROOT)
				if (i % 2 == 0) {
					g.drawLine(tmp[i].leftOff+Node.widthOfNode/2, tmp[i].topOff +Node.heightOfLine/2, (int) ((tmp[i].leftOff + tmp[i+1].leftOff)/2)+Node.widthOfNode/2, tmp[i].topOff - (Node.heightOfNode + Node.widthOfNode)+Node.heightOfNode);
					tmp[i / 2].n = tmp[i].n.parent;
					tmp[i / 2].leftOff = (int) ((tmp[i].leftOff + tmp[i+1].leftOff)/2);
					tmp[i / 2].topOff = tmp[i].topOff - (Node.heightOfNode + Node.widthOfNode);
				}else{
					g.drawLine(tmp[i].leftOff+Node.widthOfNode/2, tmp[i].topOff +Node.heightOfLine/2, tmp[i/2].leftOff+Node.widthOfNode/2, tmp[i/2].topOff+Node.heightOfNode);
				}
				
			}
			
			nodesAtThisLevel /= 2;
		}
	}

}

class NodeAndPosition {
	Node n;
	int leftOff, topOff;

	public void drawNode(Graphics g) {
		n.drawNode(g, leftOff, topOff);
	}
}
