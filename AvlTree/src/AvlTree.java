import java.util.ArrayList;

public class AvlTree {

	private AvlNode root;
 
	/**
	 * Method to insert new Node in the AVL tree, taking int argument
	 * @param nodeValue, 
	 */
	public void insert(int nodeValue) {
		AvlNode newNode = new AvlNode(nodeValue);
		insertAVL(this.root, newNode);
	}
 
 	/**
 	 * Method to insert new Node in the AVL tree, taking AvlNode argument 
 	 * @param rootNode, root Node of the AVL tree
 	 * @param newNode, Node to be inserted
 	 */
 	private void insertAVL(AvlNode rootNode, AvlNode newNode) {
		/* Check if the root node is null, if so then the new Node is the root node */
		if (rootNode == null) {
			this.root = newNode;
		} else {
			/* If the root node is not null, check if the new node is smaller then the root node	 */
			if (newNode.key < rootNode.key) {
				/* if the new node is smaller then the root node plus the left child of the root node is null
				 * then the new node becomes the left child of the given root node */
				if (rootNode.left == null) {
					rootNode.left = newNode;
					newNode.parent = rootNode;
					
					/* Given new Node is inserted, now check for imbalance */
					checkForImbalance(rootNode);
				} else {
					/* if the new node is smaller then the root node, but the left child of the root node is not null
					 * then recursively call the same method with root node as the left child  */
					insertAVL(rootNode.left, newNode);
				}

			} 
			/* Check if the new node is greater then the root node	 */ 
			else if (newNode.key > rootNode.key) {
				/* if the new node is greater then the root node plus the right child of the root node is null
				 * then the new node becomes the right child of the given root node */
				if (rootNode.right == null) {
					rootNode.right = newNode;
					newNode.parent = rootNode;

					/* Given new Node is inserted, now check for imbalance */
					checkForImbalance(rootNode);
				} else {
					/* if the new node is greater then the root node, but the right child of the root node is not null
					 * then recursively call the same method with root node as the right child  */
					insertAVL(rootNode.right, newNode);
				}
			}
		}
	}
 
	/**
	 * check for imbalance for a given node
	 * @param cur
	 */
 	private void checkForImbalance(AvlNode cur) {
		setBalance(cur);
		/* right sub tree height - left sub tree height */
		int balance = cur.balance; 

		if (balance == -2) {
			/* height of left sub tree is +2 compared to right subtree */
			if (getHeight(cur.left.left) >= getHeight(cur.left.right)) {
				// LL - Rotation
				cur = rotateRight(cur);
			} else {
				//LR - Rotation
				cur = doubleRotateLeftRight(cur);
			}
		} else if (balance == 2) {
			/* height of right sub tree is +2 compared to left subtree */
			if (getHeight(cur.right.right) >= getHeight(cur.right.left)) {
				//RR - Rotation
				cur = rotateLeft(cur);
			} else {
				//RL - Rotation
				cur = doubleRotateRightLeft(cur);
			}
		}

		// check for imbalance for the parent node
		if (cur.parent != null) {
			checkForImbalance(cur.parent);
		} else {
			this.root = cur;
		}
	}

	/**
	 * Method to remove a given node from the AVL tree.
	 * @param nodeValue
	 */
 	public void remove(int nodeValue) {
		locateNDeleteNode(this.root, nodeValue);
	}

	/**
	 * Locates and deletes the Node with the given value 
	 * @param rootNode
	 * @param removeNodeOfValue
	 */
 	private void locateNDeleteNode(AvlNode rootNode, int removeNodeOfValue) {
		if (rootNode == null) {
			return;
		} else {
			if (rootNode.key > removeNodeOfValue) {
				locateNDeleteNode(rootNode.left, removeNodeOfValue);
			} else if (rootNode.key < removeNodeOfValue) {
				locateNDeleteNode(rootNode.right, removeNodeOfValue);
			} else if (rootNode.key == removeNodeOfValue) {
				deleteNode(rootNode);
			}
		}
	}

	/**
	 * Removes a node from a AVL-Tree, while balancing will be done if
	 * necessary.
	 * @param q, The node to be removed.
	 */
	public void deleteNode(AvlNode node) {
		AvlNode r;
		// at least one child of q, q will be removed directly
		if (node.left == null || node.right == null) {
			// the root is deleted
			if (node.parent == null) {
				this.root = null;
				node = null;
				return;
			}
			r = node;
		} else {
			// q has two children --> will be replaced by successor
			r = successor(node);
			node.key = r.key;
		}

		AvlNode p;
		if (r.left != null) {
			p = r.left;
		} else {
			p = r.right;
		}

		if (p != null) {
			p.parent = r.parent;
		}

		if (r.parent == null) {
			this.root = p;
		} else {
			if (r == r.parent.left) {
				r.parent.left = p;
			} else {
				r.parent.right = p;
			}
			// balancing must be done until the root is reached.
			checkForImbalance(r.parent);
		}
		r = null;
	}

	/**
	 * Left rotation using the given node.
	 * @param n, The node for the rotation.
	 * @return The root of the rotated tree.
	 */
	public AvlNode rotateLeft(AvlNode node) {
		AvlNode v = node.right;
		v.parent = node.parent;

		node.right = v.left;

		if (node.right != null) {
			node.right.parent = node;
		}

		v.left = node;
		node.parent = v;

		if (v.parent != null) {
			if (v.parent.right == node) {
				v.parent.right = v;
			} else if (v.parent.left == node) {
				v.parent.left = v;
			}
		}
		setBalance(node);
		setBalance(v);
		/* returns the root of the rotated tree */
		return v;
	}
 
	/**
	 * Right rotation using the given node.
	 * @param n, The node for the rotation
	 * @return The root of the new rotated tree.
	 */
	public AvlNode rotateRight(AvlNode n) {
		AvlNode v = n.left;
		v.parent = n.parent;

		n.left = v.right;

		if (n.left != null) {
			n.left.parent = n;
		}

		v.right = n;
		n.parent = v;

		if (v.parent != null) {
			if (v.parent.right == n) {
				v.parent.right = v;
			} else if (v.parent.left == n) {
				v.parent.left = v;
			}
		}
		setBalance(n);
		setBalance(v);
		/* returns the root of the rotated tree */
		return v;
	}

	/**
	 * LR - Rotation
	 * @param node
	 * @return
	 */
	public AvlNode doubleRotateLeftRight(AvlNode node) {
		node.left = rotateLeft(node.left);
		/* returns the root of the rotated tree */
		return rotateRight(node);
	}

	/**
	 * RL - Rotation
	 * @param node
	 * @return
	 */
	public AvlNode doubleRotateRightLeft(AvlNode node) {
		node.right = rotateRight(node.right);
		/* returns the root of the rotated tree */
		return rotateLeft(node);
	}
 
	/**
	 * @param node
	 * @return
	 */
	public AvlNode successor(AvlNode node) {
		if (node.right != null) {
			AvlNode right = node.right;
			while (right.left != null) {
				right = right.left;
			}
			return right;
		} else {
			AvlNode parent = node.parent;
			while (parent != null && node == parent.right) {
				node = parent;
				parent = node.parent;
			}
			return parent;
		}
	}
 
	/**
	 * Method to return height of a given Node
	 * @param cur, int 
	 * @return 
	 */
	private int getHeight(AvlNode cur) {
		if (cur == null) {
			// for a null node return -1
			return -1;
		}
		if (cur.left == null && cur.right == null) {
			// for leaf node return 0
			return 0;
		} else if (cur.left == null) {
			return 1 + getHeight(cur.right);
		} else if (cur.right == null) {
			return 1 + getHeight(cur.left);
		} else {
			return 1 + Math.max(getHeight(cur.left), getHeight(cur.right));
		}
	}
	
	/**
	 * default/exposed method to print the AVL tree 
	 */
	/*public void printDetails() {
		printDetails(root);
	}*/

	/**
	 * Prints the AVL tree from the root node
	 * @param 
	 */
	/*private void printDetails(AvlNode rootNode) {
		int left = 0;
		int right = 0;
		int parent = 0;
		if (rootNode.left != null) {
			left = rootNode.left.key;
		}
		if (rootNode.right != null) {
			right = rootNode.right.key;
		}
		if (rootNode.parent != null) {
			parent = rootNode.parent.key;
		}

		System.out.println("For " + rootNode.key + " : Left: " + left + " Key: " + rootNode + " Right: " + right + " Parent: " + parent + " Balance: " + rootNode.balance);

		if (rootNode.left != null) {
			printDetails(rootNode.left);
		}
		if (rootNode.right != null) {
			printDetails(rootNode.right);
		}
	}*/
 
	private void setBalance(AvlNode cur) {
		cur.balance = getHeight(cur.right) - getHeight(cur.left);
	}
 
	/**
	 * Method to get the in-order of the tree
	 * @return
	 */
	final protected ArrayList<AvlNode> getInOrder() {
		ArrayList<AvlNode> ret = new ArrayList<AvlNode>();
		getInOrder(root, ret);
		return ret;
	}
 
	/**
	 * Method to get the in-order of the tree, taking a given node
	 * @param rootNode
	 * @param list
	 */
	final protected void getInOrder(AvlNode rootNode, ArrayList<AvlNode> list) {
		if (rootNode == null) {
			return;
		}
		getInOrder(rootNode.left, list);
		list.add(rootNode);
		getInOrder(rootNode.right, list);
	}

	/**
	 * Method to get the pre-order of the tree
	 * @return
	 */
	final protected ArrayList<AvlNode> getPreOrder() {
		ArrayList<AvlNode> ret = new ArrayList<AvlNode>();
		getPreOrder(root, ret);
		return ret;
	}

	/**
	 * Method to get the in-order of the tree, taking a given node
	 * @param rootNode
	 * @param list
	 */
	final protected void getPreOrder(AvlNode rootNode, ArrayList<AvlNode> list) {
		if (rootNode == null) {
			return;
		}
		list.add(rootNode);
		getPreOrder(rootNode.left, list);
		getPreOrder(rootNode.right, list);
	}
}

class AvlNode {
	public AvlNode left;
	public AvlNode right;
	public AvlNode parent;
	public int key;
	public int balance;

	public AvlNode(int k) {
		left = right = parent = null;
		balance = 0;
		key = k;
	}

	public String toString() {
		return "" + key;
	}

}

