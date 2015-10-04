import java.awt.*;
import java.util.Random;

public class Grid {
	private int cols, rows;
	private int score;
	private Element[][] elements;
	private Random rnd;

	public Grid() {
		rnd = new Random();
		cols = 10;
		rows = 10;
		score = 0;
		elements = new Element[10][10];
		fillGrid();
	}

	public Grid(int r, int c) {
		rnd = new Random();
		cols = c;
		rows = r;
		score = 0;
		elements = new Element[r][c];
		fillGrid();
	}

	public void draw(Graphics2D g2) {
		for (Element[] r : elements)
			for (Element e : r) {
				e.draw(g2);
			}
	}

	public void fillGrid() {
		// filling the grid row by row
		for (int i = 0; i < rows; i++) {
			fillRow(i);
		}
		// removing all vertical lines of 3 or more elements in the grid
		for (int j = 0; j < cols; j++)
			for (int i = 2; i < rows; i++){
				while (elements[i][j].equals(elements[i - 1][j])
						&& elements[i][j].equals(elements[i - 2][j])) {
					int type=elements[i][j].getType();
					int typeWest=-1, typeEast=-1,typeCenter=-1;
					if(j>=2 && elements[i][j-2].equals(elements[i][j-1]))
						typeWest=elements[i][j-1].getType();
					if(j<cols-2 && elements[i][j+1].equals(elements[i][j+2]))
						typeEast=elements[i][j+1].getType();
					if(j>0 && j<cols-1 && elements[i][j-1].equals(elements[i][j+1]))
						typeCenter=elements[i][j+1].getType();
					elements[i][j]=generateElement(elements[i][j].getX(),elements[i][j].getY());
					while(elements[i][j].getType()==type || elements[i][j].getType()==typeWest ||
							elements[i][j].getType()==typeEast || elements[i][j].getType()==typeCenter)
						elements[i][j]=generateElement(elements[i][j].getX(),elements[i][j].getY());
						
				}
			}
	}

	// fill row r with random elements
	public void fillRow(int r) {
		int x = 400;
		for (int j = 0; j < cols; j++) {
			elements[r][j] = generateElement(x, 150 + r * 50);
			while (j >= 2 && elements[r][j].equals(elements[r][j - 1])
					&& elements[r][j].equals(elements[r][j - 2])){
				elements[r][j] = generateElement(x, 150 + r * 50);
			}
			x += 50;
		}
	}

	// filling row r with elements of type t
	public void fillRow(int r, int t) {
		int x = 400;
		int y = 150 + r * 50;
		for (int i = 0; i < cols; i++) {
			switch (t) {
			case Element.FIRE:
				elements[r][i] = new FireElement(x, y);
				break;
			case Element.WATER:
				elements[r][i] = new WaterElement(x, y);
				break;
			case Element.EARTH:
				elements[r][i] = new EarthElement(x, y);
				break;
			case Element.WIND:
				elements[r][i] = new WindElement(x, y);
				break;
			case Element.ICE:
				elements[r][i] = new IceElement(x, y);
				break;
			default:
				elements[r][i] = new ElectricityElement(x, y);
				break;
			}
			x += 50;
		}
	}

	// filling column c with random elements
	public void fillCol(int c) {
		int x = 400 + c * 50;
		int y = 150;
		for (int i = 0; i < rows; i++) {
			elements[i][c] = generateElement(x, y);
			while (i >= 2 && elements[i - 1][c].equals(elements[i][c])
					&& elements[i - 2][c].equals(elements[i][c]))
				elements[i][c] = generateElement(elements[i][c].getX(),
						elements[i][c].getY());
			y += 50;
		}
	}

	// filling column c with elements of type t
	public void fillCol(int c, int t) {
		int x = 400 + c * 50;
		int y = 150;
		for (int i = 0; i < rows; i++) {
			switch (t) {
			case Element.FIRE:
				elements[i][c] = new FireElement(x, y);
				break;
			case Element.WATER:
				elements[i][c] = new WaterElement(x, y);
				break;
			case Element.EARTH:
				elements[i][c] = new EarthElement(x, y);
				break;
			case Element.WIND:
				elements[i][c] = new WindElement(x, y);
				break;
			case Element.ICE:
				elements[i][c] = new IceElement(x, y);
				break;
			default:
				elements[i][c] = new ElectricityElement(x, y);
				break;
			}
			y += 50;
		}
	}

	public void reassign() {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) {
				int x = rnd.nextInt(rows);
				int y = rnd.nextInt(cols);
				int x1 = elements[i][j].getX();
				int y1 = elements[i][j].getY();
				int x2 = elements[x][y].getX();
				int y2 = elements[x][y].getY();
				Element tmp = elements[i][j];
				elements[i][j] = elements[x][y];
				elements[i][j].setX(x1);
				elements[i][j].setY(y1);
				elements[x][y] = tmp;
				elements[x][y].setX(x2);
				elements[x][y].setY(y2);
			}
	}

	//returns element of random type
	public Element generateElement(int x, int y) {
		int choice = rnd.nextInt(6);
		Element e;
		switch (choice) {
		case Element.FIRE:
			e = new FireElement(x, y);
			break;
		case Element.WATER:
			e = new WaterElement(x, y);
			break;
		case Element.EARTH:
			e = new EarthElement(x, y);
			break;
		case Element.WIND:
			e = new WindElement(x, y);
			break;
		case Element.ICE:
			e = new IceElement(x, y);
			break;
		default:
			e = new ElectricityElement(x, y);
			break;
		}
		return e;
	}

	// finding all lines for deletion
	public boolean findLine() {
		boolean isLine = false;
		int vLength = 1, hLength = 1; // length of vertical and horizontal lines
		int iStart = 0, iEnd = 0, jStart = 0, jEnd = 0;
		for (int i = 0; i < rows; i++)
			for (int j = 2; j < cols; j++)
				if (elements[i][j].equals(elements[i][j - 1])
						&& elements[i][j].equals(elements[i][j - 2])
						&& elements[i][j].getType() != -1) {
					isLine = true;
					jStart = j - 2;
					jEnd = j;
					while (jEnd < cols
							&& elements[i][jEnd].equals(elements[i][j]))
						jEnd++;
					hLength = jEnd - jStart;
					jEnd--;
					if (hLength > 3) {
						// creating a special element at the end of the line
						Element sub = elements[i][jEnd];
						int subT = sub.getType();
						if (elements[i][jEnd].getClass().getSimpleName() == "SpecialElement")
							specialElementAction(i, jEnd,
									elements[i][jEnd].getType());
						elements[i][jEnd] = new SpecialElement(sub.getX(),
								sub.getY(), subT);
						jEnd--;
					}
					for (int k = jStart; k <= jEnd; k++) {
						iStart = i;
						iEnd = i;
						if (i > 1 && elements[i][k].equals(elements[i - 1][k])
								&& elements[i][k].equals(elements[i - 2][k]))
							iStart = i - 2;
						if (i > 0 && i < rows - 1
								&& elements[i][k].equals(elements[i + 1][k])
								&& elements[i][k].equals(elements[i - 1][k])) {
							if (i - 1 < iStart)
								iStart = i - 1;
							iEnd = i + 1;
						}
						if (i < rows - 2
								&& elements[i][k].equals(elements[i + 1][k])
								&& elements[i][k].equals(elements[i + 2][k]))
							iEnd = i + 2;
						for (int s = iStart; s <= iEnd; s++) {
							if (elements[s][k].getClass().getSimpleName()
									.equals("SpecialElement"))
								specialElementAction(s, k,
										elements[s][k].getType());
							else
								elements[s][k].animation();
						}
					}
					for (int k = jStart; k <= jEnd; k++) {
						if (elements[i][k].getClass().getSimpleName()
								.equals("SpecialElement")) {
							specialElementAction(i, k, elements[i][k].getType());
						} else
							elements[i][k].animation();
					}
				}

		for (int j = 0; j < cols; j++)
			for (int i = 2; i < rows; i++) {
				if (elements[i][j].equals(elements[i - 1][j])
						&& elements[i][j].equals(elements[i - 2][j])
						&& elements[i][j].getType() != -1) {
					isLine = true;
					iStart = i - 2;
					iEnd = i + 1;
					while (iEnd < rows
							&& elements[iEnd][j].equals(elements[i][j]))
						iEnd++;
					vLength = iEnd - iStart;
					iEnd--;

					if (vLength > 3) {
						Element sub = elements[iEnd][j];
						int subT = sub.getType();
						if (sub.getClass().getSimpleName() == "SpecialElement")
							specialElementAction(iEnd, j, sub.getType());
						elements[iEnd][j] = new SpecialElement(sub.getX(),
								sub.getY(), subT);
						iEnd--;
					}
					for (int k = iStart; k <= iEnd; k++) {
						jStart = j;
						jEnd = j;
						if (j > 1 && elements[k][j].equals(elements[k][j - 1])
								&& elements[k][j].equals(elements[k][j - 2]))
							jStart = j - 2;
						if (j > 0 && j < cols - 1
								&& elements[k][j].equals(elements[k][j - 1])
								&& elements[i][j].equals(elements[k][j + 1])) {
							if (j - 1 < jStart)
								jStart = j - 1;
							jEnd = j + 1;
						}
						if (j < cols - 2
								&& elements[k][j].equals(elements[k][j + 1])
								&& elements[k][j].equals(elements[k][j + 2]))
							jEnd = j + 2;
						for (int s = jStart; s <= jEnd; s++) {
							if (elements[k][s].getClass().getSimpleName()
									.equals("SpecialElement"))
								specialElementAction(k, s,
										elements[k][s].getType());
							else
								elements[k][s].animation();
						}
					}
					for (int k = iStart; k <= iEnd; k++) {
						if (elements[k][j].getClass().getSimpleName()
								.equals("SpecialElement")) {
							specialElementAction(k, j, elements[k][j].getType());
						} else
							elements[k][j].animation();
					}
				}

			}

		if (hLength != 1)
			score += hLength;
		if (vLength != 1)
			score += vLength;
		return isLine;
	}

	public boolean findLine(int iMoved1, int jMoved1, int iMoved2, int jMoved2) {
		boolean isLine = false;
		boolean specialElement = false;
		int vLength = 1, hLength = 1;
		int iStart = 0, iEnd = 0, jStart = 0, jEnd = 0, iMoved = -1, jMoved = -1;

		for (int i = 0; i < rows; i++)
			for (int j = 2; j < cols; j++)
				// checking if there is a line
				if (elements[i][j].equals(elements[i][j - 1])
						&& elements[i][j].equals(elements[i][j - 2])
						&& elements[i][j].getType() != -1) {
					// the line is found
					isLine = true;
					jStart = j - 2;
					jEnd = j;
					// finding which one of the two elements that were swapped
					// is in the line
					if (elements[i][j].getType() == elements[iMoved1][jMoved1]
							.getType()) {
						iMoved = iMoved1;
						jMoved = jMoved1;
					} else {
						iMoved = iMoved2;
						jMoved = jMoved2;
					}
					// finding the last index of the line
					while (jEnd < cols
							&& elements[i][jEnd].equals(elements[i][j]))
						jEnd++;
					hLength = jEnd - jStart;
					jEnd--;
					if (hLength > 3) {
						// creating special element at the place where player
						// has made a swap
						specialElement = true;
						Element sub = elements[iMoved][jMoved];
						int subT = sub.getType();
						if (elements[iMoved][jMoved].getClass().getSimpleName() == "SpecialElement") {
							specialElementAction(iMoved, jMoved,
									elements[iMoved][jMoved].getType());
						}
						elements[iMoved][jMoved] = new SpecialElement(
								sub.getX(), sub.getY(), subT);
					}
					// finding any additional vertical line
					iStart = i;
					iEnd = i;
					if (iMoved > 1
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved - 1][jMoved])
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved - 2][jMoved]))
						iStart = iMoved - 2;
					if (iMoved > 0
							&& iMoved < rows - 1
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved + 1][jMoved])
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved - 1][jMoved])) {
						if (iMoved - 1 < iStart)
							iStart = iMoved - 1;
						iEnd = iMoved + 1;
					}
					if (iMoved < rows - 2
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved + 1][jMoved])
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved + 2][jMoved]))
						iEnd = iMoved + 2;

					vLength = iEnd - iStart + 1;
					if (vLength >= 3) {
						if (iMoved != -1 && jMoved != -1) {
							specialElement = true;
							Element sub = elements[iMoved][jMoved];
							elements[iMoved][jMoved] = new SpecialElement(
									sub.getX(), sub.getY(),
									elements[iMoved][jMoved].getType());
						}
					}
					for (int s = iStart; s <= iEnd; s++) {
						if (elements[s][jMoved].getClass().getSimpleName()
								.equals("SpecialElement")) {
							if (!specialElement) {
								specialElementAction(s, jMoved,
										elements[s][jMoved].getType());
							}
						} else
							elements[s][jMoved].animation();
					}
					for (int k = jStart; k <= jEnd; k++) {
						if (elements[i][k].getClass().getSimpleName()
								.equals("SpecialElement")) {
							if (!specialElement || jMoved != k) {
								specialElementAction(i, k,
										elements[i][k].getType());
							}
						} else
							elements[i][k].animation();
					}
				}

		for (int j = 0; j < cols; j++)
			for (int i = 2; i < rows; i++) {
				if (elements[i][j].equals(elements[i - 1][j])
						&& elements[i][j].equals(elements[i - 2][j])
						&& elements[i][j].getType() != -1) {
					isLine = true;
					iStart = i - 2;
					iEnd = i + 1;
					if (elements[i][j].getType() == elements[iMoved1][jMoved1]
							.getType()) {
						iMoved = iMoved1;
						jMoved = jMoved1;
					} else {
						iMoved = iMoved2;
						jMoved = jMoved2;
					}
					while (iEnd < rows
							&& elements[iEnd][j].equals(elements[i][j]))
						iEnd++;
					vLength = iEnd - iStart;
					iEnd--;

					if (vLength > 3) {
						specialElement = true;
						Element sub = elements[iMoved][jMoved];
						int subT = sub.getType();
						if (sub.getClass().getSimpleName() == "SpecialElement"
								&& !specialElement) {
							specialElementAction(iMoved, jMoved, sub.getType());
						}
						elements[iMoved][jMoved] = new SpecialElement(
								sub.getX(), sub.getY(), subT);
					}

					jStart = jMoved;
					jEnd = jMoved;
					if (jMoved > 1
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved][jMoved - 1])
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved][jMoved - 2]))
						jStart = jMoved - 2;
					if (jMoved > 0
							&& jMoved < cols - 1
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved][jMoved - 1])
							&& elements[i][jMoved]
									.equals(elements[iMoved][jMoved + 1])) {
						if (jMoved - 1 < jStart)
							jStart = jMoved - 1;
						jEnd = jMoved + 1;
					}
					if (jMoved < cols - 2
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved][jMoved + 1])
							&& elements[iMoved][jMoved]
									.equals(elements[iMoved][jMoved + 2]))
						jEnd = jMoved + 2;
					hLength = jEnd - jStart + 1;
					if (hLength >= 3) {
						if (iMoved != -1 && jMoved != -1) {
							specialElement = true;
							Element sub = elements[iMoved][jMoved];
							elements[iMoved][jMoved] = new SpecialElement(
									sub.getX(), sub.getY(),
									elements[iMoved][jMoved].getType());
						}
					}
					for (int s = jStart; s <= jEnd; s++) {
						if (elements[iMoved][s].getClass().getSimpleName()
								.equals("SpecialElement")) {
							if (!specialElement || jMoved != s) {
								specialElementAction(iMoved, s,
										elements[iMoved][s].getType());
							}
						} else
							elements[iMoved][s].animation();
					}

					for (int k = iStart; k <= iEnd; k++) {
						if (elements[k][j].getClass().getSimpleName()
								.equals("SpecialElement")) {
							if (!specialElement || iMoved != k) {
								specialElementAction(k, j,
										elements[k][j].getType());
							}
						} else
							elements[k][j].animation();
					}
				}
			}

		if (hLength != 1)
			score += hLength;
		if (vLength != 1)
			score += vLength;
		return isLine;
	}

	public Element getElement(int x, int y) {
		return elements[x][y];
	}

	public int getNumRows() {
		return rows;
	}

	public int getNumCols() {
		return cols;
	}

	public void setElement(int x, int y, Element e) {
		elements[x][y] = e;
	}

	public void specialElementAction(int i, int j, int t) {
		switch (t) {
		case Element.EARTH:
			for (int p = 0; p < cols; p++) {
				if (elements[i][p].getClass().getSimpleName() != "SpecialElement")
					elements[i][p] = new EarthElement(elements[i][p].getX(),
							elements[i][p].getY());
				else if (p != j) {
					specialElementAction(i, p, elements[i][p].getType());
				}
				elements[i][p].animation();
			}
			score += cols;
			break;
		case Element.ELECTRICITY:
			elements[i][j].animation();
			Random rnd = new Random();
			for (int p = 0; p < 8; p++) {
				int x1 = rnd.nextInt(rows);
				int y1 = rnd.nextInt(cols);
				Element tmp = elements[x1][y1];
				if (tmp.getClass().getSimpleName() != "SpecialElement")
					elements[x1][y1] = new ElectricityElement(tmp.getX(),
							tmp.getY());
				else if (x1 != i && y1 != j) {
					specialElementAction(x1, y1, tmp.getType());
				}
				elements[x1][y1].animation();

			}
			score += 8;
			break;
		case Element.FIRE:
			elements[i][j].animation();
			if (i > 0) {
				if (elements[i - 1][j].getClass().getSimpleName() != "SpecialElement") {
					int x1 = elements[i - 1][j].getX();
					int y1 = elements[i - 1][j].getY();
					Element tmp = new FireElement(x1, y1);
					elements[i - 1][j] = tmp;
					score++;
				} else
					specialElementAction(i - 1, j, elements[i - 1][j].getType());
				elements[i - 1][j].animation();
				if (j > 0) {
					if (elements[i - 1][j - 1].getClass().getSimpleName() != "SpecialElement") {
						int x2 = elements[i - 1][j - 1].getX();
						int y2 = elements[i - 1][j - 1].getY();
						Element tmp = new FireElement(x2, y2);
						elements[i - 1][j - 1] = tmp;
						score++;
					} else
						specialElementAction(i - 1, j - 1,
								elements[i - 1][j - 1].getType());
					elements[i - 1][j - 1].animation();
				}
				if (j < cols - 1) {
					if (elements[i - 1][j + 1].getClass().getSimpleName() != "SpecialElement") {
						int x2 = elements[i - 1][j + 1].getX();
						int y2 = elements[i - 1][j + 1].getY();
						Element tmp2 = new FireElement(x2, y2);
						elements[i - 1][j + 1] = tmp2;
						score++;
					} else
						specialElementAction(i - 1, j + 1,
								elements[i - 1][j + 1].getType());
					elements[i - 1][j + 1].animation();
				}
			}
			if (i < rows - 1) {
				if (elements[i + 1][j].getClass().getSimpleName() != "SpecialElement") {
					int x1 = elements[i + 1][j].getX();
					int y1 = elements[i + 1][j].getY();
					Element tmp = new FireElement(x1, y1);
					elements[i + 1][j] = tmp;
					score++;
				} else
					specialElementAction(i + 1, j, elements[i + 1][j].getType());
				elements[i + 1][j].animation();
				if (j > 0) {
					if (elements[i + 1][j - 1].getClass().getSimpleName() != "SpecialElement") {
						int x2 = elements[i + 1][j - 1].getX();
						int y2 = elements[i + 1][j - 1].getY();
						Element tmp2 = new FireElement(x2, y2);
						elements[i + 1][j - 1] = tmp2;
						score++;
					} else
						specialElementAction(i + 1, j - 1,
								elements[i + 1][j - 1].getType());
					elements[i + 1][j - 1].animation();
				}
				if (j < cols - 1) {
					if (elements[i + 1][j + 1].getClass().getSimpleName() != "SpecialElement") {
						int x2 = elements[i + 1][j + 1].getX();
						int y2 = elements[i + 1][j + 1].getY();
						Element tmp2 = new FireElement(x2, y2);
						elements[i + 1][j + 1] = tmp2;
						score++;
					} else
						specialElementAction(i + 1, j + 1,
								elements[i + 1][j + 1].getType());
					elements[i + 1][j + 1].animation();
				}
			}
			if (j > 0) {
				if (getElement(i, j - 1).getClass().getSimpleName() != "SpecialElement") {
					int x1 = getElement(i, j - 1).getX();
					int y1 = getElement(i, j - 1).getY();
					Element tmp = new FireElement(x1, y1);
					setElement(i, j - 1, tmp);
					score++;
				} else
					specialElementAction(i, j - 1, elements[i][j - 1].getType());
				getElement(i, j - 1).animation();
			}
			if (j < cols - 1) {
				if (getElement(i, j + 1).getClass().getSimpleName() != "SpecialElement") {
					int x1 = getElement(i, j + 1).getX();
					int y1 = getElement(i, j + 1).getY();
					Element tmp = new FireElement(x1, y1);
					setElement(i, j + 1, tmp);
					score++;
				} else
					specialElementAction(i, j + 1, elements[i][j + 1].getType());
				getElement(i, j + 1).animation();
			}
			break;
		case Element.ICE:
			for (int p = 0; p < rows; p++) {
				if (elements[p][j].getClass().getSimpleName() != "SpecialElement")
					elements[p][j] = new IceElement(elements[p][j].getX(),
							elements[p][j].getY());
				else if (p != i) {
					specialElementAction(p, j, elements[p][j].getType());
				}

				elements[p][j].animation();
			}

			score += rows;
			break;
		case Element.WATER:
			for (int p = 0; p < cols; p++) {
				if (elements[i][p].getClass().getSimpleName() != "SpecialElement")
					elements[i][p] = new WaterElement(elements[i][p].getX(),
							elements[i][p].getY());
				else if (p != j) {
					specialElementAction(i, p, elements[i][p].getType());
				}

				elements[i][p].animation();
			}
			score += cols;
			break;
		case Element.WIND:
			for (int p = 0; p < rows; p++) {
				if (elements[p][j].getClass().getSimpleName() != "SpecialElement")
					elements[p][j] = new WindElement(elements[p][j].getX(),
							elements[p][j].getY());
				else if (p != i) {
					specialElementAction(p, j, elements[p][j].getType());
				}

				elements[p][j].animation();
			}
			score += rows;
			break;
		}
	}

	public int getScore() {
		return score;
	}

	public void setScore(int s) {
		score = s;
	}
}