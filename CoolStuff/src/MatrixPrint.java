public class MatrixPrint {
	int[][] matrix;
	int x, y;
	int xpos = 0, ypos = 0;
	int xmin = 0, ymin = 0, xmax, ymax;

	public MatrixPrint(int px, int py) {
		x = px;
		y = py;
		xmax = x - 1;
		ymax = y - 1;
	}

	private void createMatrix() {
		matrix = new int[x][y];
		int value = 0;
		// each row
		for (int i = 0; i < x; i++) {
			// each column
			for (int j = 0; j < y; j++) {
				matrix[i][j] = value++;
			}
		}
	}

	private void normalMatrixPrint() {
		System.out
				.println("*************************************************************************");
		// each row
		for (int i = 0; i < x; i++) {
			// each column
			for (int j = 0; j < y; j++) {
				System.out.print("\t" + matrix[i][j]);
			}
			System.out.print("\n");
		}
		System.out
				.println("*************************************************************************");
	}

	private void spiralMatrixPrint() {
		System.out.print("\t" + matrix[xpos][ypos]);
		while (true) {
			if (ypos < ymax)
				moveRight();
			if (xpos < xmax)
				moveDown();
			if (ypos > ymin)
				moveLeft();
			if (xpos > xmin)
				moveUp();
			if (xmin >= xmax && ymin >= ymax)
				break;
		}
	}

	private boolean moveRight() {
		while (true) {
			System.out.print("\t" + matrix[xpos][++ypos]);
			if (xmin == xmax)
				ymin++;
			if (ypos == (ymax)) {
				break;
			}
		}
		if (xmin < xmax)
			xmin++;
		return false;
	}

	private boolean moveLeft() {
		while (true) {
			System.out.print("\t" + matrix[xpos][--ypos]);
			if (ypos == (ymin)) {
				break;
			}
		}
		if (xmin < xmax)
			xmax--;
		return false;
	}

	private boolean moveDown() {
		while (true) {
			System.out.print("\t" + matrix[++xpos][ypos]);
			if (ymin == ymax)
				xmin++;
			if (xpos == (xmax)) {
				break;
			}
		}
		if (ymin < ymax)
			ymax--;
		return false;
	}

	private boolean moveUp() {
		while (true) {
			System.out.print("\t" + matrix[--xpos][ypos]);
			if (xpos == xmin) {
				break;
			}
		}
		if (ymin < ymax)
			ymin++;
		return false;
	}

	public static void main(String[] str) {
		MatrixPrint mp = new MatrixPrint(5, 3);
		mp.createMatrix();
		mp.normalMatrixPrint();
		mp.spiralMatrixPrint();
	}
}
