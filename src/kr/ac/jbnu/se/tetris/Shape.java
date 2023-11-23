package kr.ac.jbnu.se.tetris;

import java.util.Random;

public class Shape {

	private Tetrominoes pieceShape;
	private int coords[][];
	private int[][][] coordsTable;

	private int heavyBlockCount;

	protected int getHeavyBlockCount() {
		return heavyBlockCount;
	}



	public Shape() {
		coords = new int[4][2];
		setShape(Tetrominoes.NoShape);

	}

	public void setShape(Tetrominoes shape) {

		coordsTable = new int[][][] {
				// 각 블록 모양의 좌표 정보
				// { {x0, y0}, {x1, y1}, {x2, y2}, {x3, y3} }
				{ { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } }, // NoShape
				{ { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 } }, // ZShape
				{ { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } }, // SShape
				{ { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } }, // LineShape
				{ { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } }, // TShape
				{ { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } }, // SquareShape
				{ { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } }, // LShape
				{ { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } },   // JShape
				{ { 0 , 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } }
		};

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; ++j) {
				coords[i][j] = coordsTable[shape.ordinal()][i][j];
			}
		}
		pieceShape = shape;

	}

	private void setX(int index, int x) {
		coords[index][0] = x;
	}

	private void setY(int index, int y) {
		coords[index][1] = y;
	}

	public int x(int index) {
		return coords[index][0];
	}

	public int y(int index) {
		return coords[index][1];
	}

	public Tetrominoes getShape() {
		return pieceShape;
	}

	public void setRandomShape() {
		Random r = new Random();
		int x = Math.abs(r.nextInt()) % 7 + 1;
		Tetrominoes[] values = Tetrominoes.values();
		setShape(values[x]);
	}

	public int minX() {
		int m = coords[0][0];
		for (int i = 0; i < 4; i++) {
			m = Math.min(m, coords[i][0]);
		}
		return m;
	}

	public int minY() {
		int m = coords[0][1];
		for (int i = 0; i < 4; i++) {
			m = Math.min(m, coords[i][1]);
		}
		return m;
	}

	public Shape rotateLeft() {
		if (pieceShape == Tetrominoes.SquareShape)
			return this;

		Shape result = new Shape();
		result.pieceShape = pieceShape;

		for (int i = 0; i < 4; ++i) {

			result.setX(i, y(i));
			result.setY(i, -x(i));
		}
		int minX = result.minX();
		int minY = result.minY();
		for (int i = 0; i < 4; ++i) {
			result.setX(i, result.x(i) - minX);
			result.setY(i, result.y(i) - minY);
		}
		return result;
	}

	public int maxX() {
		int m = coords[0][0];
		for (int i = 0; i < 4; i++) {
			m = Math.max(m, coords[i][0]);
		}
		return m;
	}

	public Shape rotateRight() {
		if (pieceShape == Tetrominoes.SquareShape)
			return this;

		Shape result = new Shape();
		result.pieceShape = pieceShape;

		for (int i = 0; i < 4; ++i) {
			result.setX(i, -y(i));
			result.setY(i, x(i));
		}
		return result;
	}
}