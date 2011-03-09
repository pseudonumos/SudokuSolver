import java.util.*;


//The Sudoku class simulates a sudoku puzzle. 
public class Sudoku {
	
	private Cell[][] grid;
	private int[][] original, generated;
	private Selection[][] groups;
	private int count, turns, startCount, wipes;
	private Set solvedCells;
	private boolean stuck;
	
	
	public Sudoku() {
		this(new int[9][9]);
	}
	
	//can pass an 2d array of int as original puzzle. 
	public Sudoku( int[][] org) {
		turns = 0;
		original = new int[9][9];
		generated = new int[9][9];
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				if (org[r][c] != 0) {
					original[r][c] = org[r][c];
					generated[r][c] = org[r][c];

				}
			}			
		}
		wipe();
		wipes = 0;
	}	
	
	//cleans the entire grid system but retain the original puzzle
	//mainly allows to re-generate puzzle on the occasion a bad puzzle is generated.
	public void wipe() {
		wipes++;		
		solvedCells = new HashSet();
		grid = new Cell[9][9];
		groups = new Selection[3][9];
		generated = new int[9][9];
		count = 81;
		startCount = 0;
		stuck = false;
		
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				grid[r][c] = new Cell(r, c);
			}			
		}
		
		for (int c = 0; c < 9; c++) {
			groups[0][c] = new Selection("row", getRowSet(c), c);
			groups[1][c] = new Selection("col", getColSet(c), c);
			groups[2][c] = new Selection("box", getBoxSet(c), c);
		}
		
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				if (original[r][c] != 0) {
					if(!grid[r][c].isSolved()) {
						add(r,c, original[r][c]);
					}

				}
			}			
		}
	}
	
	//solves the sudoku puzzle!
	public void solveSudoku() {		
		if (count == 0) return;
		turns++;
		
		int tempCount = count;
		update();
		
		ArrayList hLone = hiddenLone();
		solvedCells.addAll(hLone);
		update();
		
		
		if (count != tempCount) {
			stuck = false;
			solveSudoku();
		}
	
		tempCount = count;
		
		boxToLine();
		update();
		
		if (count != tempCount) {
			stuck = false;
			solveSudoku();
		}
		tempCount = count;
		
		lineToBox();
		update();
		
		if (count != tempCount) {
			stuck = false;
			solveSudoku();
		}			
		
		if (hiddenPair()) {
			update();
			solveSudoku();
		}
		if (hiddenTriple()) {
			update();
			solveSudoku();
		}
		
		if (nakedTriple()) {
			System.out.println("naked");
			update();
			solveSudoku();
		}
		
		
		if (!stuck) {
			stuck = true;
			solveSudoku();
		}
		
		
		
		
	}
	
	//updates the grid and selections so it agrees with the cells it contains
	public void update() {
		Iterator itr = solvedCells.iterator();
		ArrayList temp2 = new ArrayList();
		while (itr.hasNext()) {
			Cell temp = (Cell)itr.next();
			itr.remove();
			temp2.add(temp);
		}
		
		while (!temp2.isEmpty()) {
			Cell temp = (Cell)temp2.remove(0);
			solve(temp, temp.getSol());
		}
		
		if (!solvedCells.isEmpty()) update();
		
	}
	
	//Finds Hidden Loners and solves that cell.
	//Hidden Loners - When there is only one cell in a given selection that contains
	//a given possibility, then the solution of that cell is the given possibility.
	public ArrayList hiddenLone() {
		
		ArrayList hiddens  = new ArrayList();
		
		for (int k = 0; k < 3; k++) {
			for (int j = 0; j < 9; j++) {
				Selection temp = groups[k][j];
				hiddens.addAll(temp.hLone());

			}
		}
		
		return hiddens;
	}
	
	//The Box to Line interaction algorithem eliminates possibilities and updates a list of cells solved.
	//Box To Line Interaction - If all the cells that contains a given possibility in the box 
	//is in the same row or column. Then any other cell in that row or column cannot contain that
	//possibility.
	public void boxToLine(Selection box) {
		if (!box.isSolved()) {
			ArrayList boxList = box.unsolvedList();
			ArrayList numList = box.getUnsolNums();
			for (int p = 0; p < numList.size(); p++) {
				int num = (Integer)numList.get(p);
				ArrayList temp = new ArrayList();				
				for (int i = 0; i < boxList.size(); i++) {
					if (((Cell)boxList.get(i)).contains(num)) {
						temp.add(boxList.get(i));							
					}
				}
				if (temp.size() > 1) {
					Cell current = (Cell)temp.remove(0);
					boolean commonRow = true;
					boolean commonCol = true;
					int cRow = current.getRow();
					int cCol = current.getCol();
					while (!temp.isEmpty()) {
						Cell next = (Cell)temp.remove(0);
						commonRow = commonRow && (current.getRow() == next.getRow());
						commonCol = commonCol && (current.getCol() == next.getCol());
					}
				
					ArrayList group1 = new ArrayList();
					if (commonRow)  {
						group1 = groups[0][cRow].unsolvedList();
					}
					if (commonCol) {						
						group1 = groups[1][cCol].unsolvedList();
					}
					
					ArrayList group = new ArrayList();
					for (int i = 0; i < group1.size(); i++) {
						group.add(group1.get(i));
					}
			
					while (!group.isEmpty()) {
						Cell c = (Cell)group.remove(0);
						if (!getBox(c).equals(box)) {
							if(c.remove(num)) {
								solvedCells.add(c);
							}
						}
					}
				}
			}
		}		
	}
	
	//grabs every box and calls box to line interaction on each of them.
	public void boxToLine() {
		for (int k = 0; k < 9; k++) {
			Selection box = groups[2][k];
			boxToLine(box);
		}
	}
	
	//Line to Box. Opposite of Box to Line
	//Line to Box interaction - if all cells that contains a given possibility in a given
	//line(row or column) is in the same box, then no other cells in that box can contain
	//that possibility.
	public void lineToBox(Selection line) {
		int a;
		if (line.getType().equals("row"))  {
			a = 0;
		}
		
		else a = 1;
		
		if (!line.isSolved()) {
			ArrayList lineList = line.unsolvedList();
			ArrayList numList = line.getUnsolNums();
			for (int p = 0; p < numList.size(); p++) {
				int num = (Integer)numList.get(p);
				ArrayList temp = new ArrayList();				
				for (int i = 0; i < lineList.size(); i++) {
					if (((Cell)lineList.get(i)).contains(num)) {
						temp.add(lineList.get(i));							
					}
				}
				if (temp.size() > 1) {
					Cell current = (Cell)temp.remove(0);
					boolean commonBox = true;
					Selection cBox = getBox(current);
					while (!temp.isEmpty()) {
						Cell next = (Cell)temp.remove(0);
						commonBox = commonBox && (cBox.equals(getBox(next)));
					}
					
					ArrayList group1 = new ArrayList();
					if (commonBox)  {
						group1 = cBox.unsolvedList();
					}
						
					ArrayList group = new ArrayList();
					for (int i = 0; i < group1.size(); i++) {
						group.add(group1.get(i));
					}
					
					while (!group.isEmpty()) {
						Cell c = (Cell)group.remove(0);
						if (a == 0) {
							if (!getRow(c).equals(line)) {
								if (c.remove(num)) {
									solvedCells.add(c);
								}
							}
						}
						
						if (a == 1) {							
							if (!getCol(c).equals(line)) {
								if(c.remove(num)) {
									solvedCells.add(c);
								}
							}
						}
					}
				}
			}
		}
	}
	
	//grabs all lines and calls line to box interaction on each of them.
	public void lineToBox() {
		for (int a = 0; a < 2; a++) {		
			for (int k = 0; k < 9; k++) {
				Selection line = groups[a][k];
				lineToBox(line);
			}
		}	
	}
	
	//hidden pair trick. returns wether any possibilities were removed via hiddenPair.
	//Hidden Pair - If only two cells contain any given pair of possibilities in a selection
	//then any other possibilities in those two cells are eliminated.
	public boolean hiddenPair() {
		boolean stopper = false;
		for (int a = 0; a < 3; a++) {
			for (int b = 0; b < 9; b++) {
				Selection group = groups[a][b];
				ArrayList posNums = group.getUnsolNums();
				for (int first = 0; first < posNums.size(); first++) {
					for (int second = first + 1; second < posNums.size(); second++) {
						ArrayList cellList = group.unsolvedList();
						ArrayList hiddenList = new ArrayList();
						for (int k = 0; k < cellList.size(); k++) {
							Cell cell = (Cell) cellList.get(k);
							if (cell.contains((int)(Integer)posNums.get(first)) || cell.contains((int)(Integer)posNums.get(second))) {
								hiddenList.add(cell);
							}							
						}
						if (hiddenList.size() == 2) {
							ArrayList temp = new ArrayList();
							temp.add(posNums.get(first));
							temp.add(posNums.get(second));
							for (int i = 0; i < hiddenList.size(); i++) {
								Cell cell = (Cell) hiddenList.get(i);
								HashSet copy = (HashSet)((HashSet)cell.getPos()).clone();
								cell.retain(temp);
								if (!copy.equals(cell.getPos())) {
									stopper = true;
								}
							}						
						}
					}
				}
			}
		}
		return stopper;
	}
	
	//Hidden Triple trick. Similiar to the hidden Pair but looks at three possibilities.
	//returns wether any possibilities were removed via hiddenTriple
	//Hiddden Triple - if only three cells contain any given combination of three possibilities
	//in a selection, then any other possibilities in those three cells are eliminated.
	public boolean hiddenTriple() {
		boolean stopper = false;
		for (int a = 0; a < 3; a++) {
			for (int b = 0; b < 9; b++) {
				Selection group = groups[a][b];
				ArrayList posNums = group.getUnsolNums();
				for (int first = 0; first < posNums.size(); first++) {
					for (int second = first + 1; second < posNums.size(); second++) {
						for (int third = second + 1; third < posNums.size(); third++) {							
							ArrayList cellList = group.unsolvedList();
							ArrayList hiddenList = new ArrayList();
							for (int k = 0; k < cellList.size(); k++) {
								Cell cell = (Cell) cellList.get(k);
								if (cell.contains((int)(Integer)posNums.get(first)) 
									|| cell.contains((int)(Integer)posNums.get(second))
									|| cell.contains((int)(Integer)posNums.get(third))) {
									hiddenList.add(cell);
								}							
							}
							if (hiddenList.size() == 3) {
								ArrayList temp = new ArrayList();
								temp.add(posNums.get(first));
								temp.add(posNums.get(second));
								temp.add(posNums.get(third));
								for (int i = 0; i < hiddenList.size(); i++) {
									Cell cell = (Cell) hiddenList.get(i);
									HashSet copy = (HashSet)((HashSet)cell.getPos()).clone();
									cell.retain(temp);
									if (!copy.equals(cell.getPos())) {
										stopper = true;
									}
								}						
							}
						}
					}
				}
			}
		}
		return stopper;
	}
	
	//Naked Triples Trick. returns wether any possibilities were removed via nakedTriple.
	//If three cells in a selection together all only contain a given combination of three possibilities
	//for example [34, 45, 35] or [345, 345, 45]. Then those three possibilities can be eliminated
	//from all other cells in that selection.
	public boolean nakedTriple() {
		boolean stopper = false;
		for (int a = 0; a < 3; a++) {
			for (int b = 0; b < 9; b++) {
				Selection group = groups[a][b];
				ArrayList posNums = group.getUnsolNums();
				for (int first = 0; first < posNums.size(); first++) {
					for (int second = first + 1; second < posNums.size(); second++) {
						for (int third = second + 1; third < posNums.size(); third++) {	
							ArrayList cellList = group.unsolvedList();
							ArrayList nakedList = new ArrayList();
							for (int k = 0; k < cellList.size(); k++) {
								Cell cell = (Cell) cellList.get(k);
								HashSet posList = (HashSet)cell.getPos();
								HashSet copy = (HashSet) posList.clone();
								HashSet temp = new HashSet();
								temp.add(posNums.get(first));
								temp.add(posNums.get(second));
								temp.add(posNums.get(third));
								copy.retainAll(temp);
								if (posList.equals(copy)) {
									nakedList.add(cell);
								}
							}
							
							if (nakedList.size() == 3) {
								for (int i = 0; i < cellList.size(); i++) {
									Cell c = (Cell)cellList.get(i);
									if (!nakedList.contains(c)) {
										if (c.remove((int)(Integer)posNums.get(first)) ||
											c.remove((int)(Integer)posNums.get(second)) ||
											c.remove((int)(Integer)posNums.get(third))) {
												stopper = true;
												solvedCells.add(c);
												update();
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		return stopper;	
	}
	
	//Generated a puzzle!
	//Randomly picks a location, randomly assign it to a valid possibility in that cell.
	//runs solveSudoku to eliminate possibilities as a result of the new solved cell.
	//does the same thing for its mirror location (to create symetrical puzzles)
	//repeat until solveSudoku is able to solve entire puzzle, which means a valid, solvable puzzle
	//was generated at the last addition.
	//the generated puzzle is stored in the 2d int array generated.
	//can generate puzzle based on a given original, partially filled sudoku grid.
	//Super special cases, known as X-Wing and Swordfish to the sudoku fanatic community may cause
	//generation of bad puzzles, in those cases a wipe is done and the generate process is repeated
	public void generate() {
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++) {
				if (original[r][c] != 0) {
					addGen(r,c, original[r][c]);
					count++;
					Cell cell = grid[r][c];
					solveSudoku();
					update();
					cell = grid[8 - cell.getRow()][8 - cell.getCol()];
		
					if (!cell.isSolved()) {
						Random rand = new Random();
						ArrayList temp = cell.toArrayList();
						addGen(cell, (int)(Integer)temp.get(rand.nextInt(temp.size())));
					}
					else {
						generated[cell.getRow()][cell.getCol()] = cell.getSol();
					}
				}
			}			
		}
				
		Random rand = new Random();
		while (count > 0) {
			solveSudoku();
			update();
			Cell cell = grid[rand.nextInt(9)][rand.nextInt(9)];
			if (!cell.isSolved()) {
				ArrayList temp = cell.toArrayList();
				addGen(cell, (int)(Integer)temp.get(rand.nextInt(temp.size())));
				solveSudoku();
				update();
				cell = grid[8 - cell.getRow()][8 - cell.getCol()];
	
				if (!cell.isSolved()) {
					temp = cell.toArrayList();
					addGen(cell, (int)(Integer)temp.get(rand.nextInt(temp.size())));
				}
				else {
					generated[cell.getRow()][cell.getCol()] = cell.getSol();
				}
			}
		}
	

		if (!isValid()) {
			wipe();
			generate();
		}

	}
		
	
	
	//returns the row selection based on index
	public Cell[] getRowSet(int x) {
		Cell[] temp = new Cell[9];
		for (int k = 0; k < 9; k ++) {
			temp[k] = grid[x][k];
		}
		
		return temp;
	}
	
	//returns the column selection based on index
	public Cell[] getColSet(int x) {
		Cell[] temp = new Cell[9];
		for (int k = 0; k < 9; k++) {
			temp[k] = grid[k][x];
		}
		
		return temp;
	}
	
	//returns the box selection based on index
	public Cell[] getBoxSet(int x) {
		Cell[] temp = new Cell[9];
		int a = x / 3;
		int b = x % 3;
		a = a * 3;
		b = b * 3;
		int index = 0;
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				temp[index] = grid[r+a][c+b];
				index++;
			}
		}	
		
		return temp;	
	}
	
	//adds a solution to a given location
	public void add (int x, int y, int num) {
		Cell c = grid[x][y];
		add(c, num);
	}
	
	//adds a solution to a given cell, add method is used to create original puzzle.
	public void add(Cell cell, int num) {
		original[cell.getRow()][cell.getCol()] = num;
		if (!cell.isSolved()) {
			solve(cell, num);
		}
			startCount++;
		
	}
	
	//adds a solution to a given cell, addGen method is used to create the generated puzzle.
	public void addGen(Cell cell, int num) {
		generated[cell.getRow()][cell.getCol()] = num;
		solve(cell, num);
	}
	
	//adds a solution to a given location during generation.
	public void addGen(int x, int y, int num) {
		Cell c = grid[x][y];
		addGen(c, num);
	}
	
	//solves a given cell with a given solution. Mainly used to update the grid.
	public void solve(Cell cell, int num) {
		
		cell.solve(num);
		count--;
		Selection r = getRow(cell);
		Selection c = getCol(cell);
		Selection b = getBox(cell);
		r.solve(cell);
		c.solve(cell);
		b.solve(cell);
		ArrayList temp = new ArrayList();
		temp.addAll(r.removePos(num));
		temp.addAll(c.removePos(num));
		temp.addAll(b.removePos(num)); 
		solvedCells.addAll(temp);	

	}
	
	//solve based on location
	public void solve(int row, int col, int num) {
		Cell c = grid[row][col];
		solve(c, num);
	}
	
	//gets the row selection this cell is in
	public Selection getRow(Cell c) {
		return groups[0][c.getRow()];
	}
	
	//gets the column selection this cell is in
	public Selection getCol(Cell c) {
		return groups[1][c.getCol()];
	}
	
	//gets the box selection this cell is in
	public Selection getBox(Cell c) {
		int row = c.getRow();
		int col = c.getCol();
		row = row / 3;
		col = col / 3;
		return groups[2][row*3 + col];
	}	
 	
 	//converts the grid into 2d array int for easy display
 	public int[][] toDisplay() {
 		int[][] temp = new int[9][9];
 		for (int r = 0; r < 9; r++) {
 			for ( int c = 0; c < 9; c++) {
 					temp[r][c] = grid[r][c].getSol(); 				
 			}
 		}
 		
 		return temp;
 	}
 	
 	//returns a 2d array of possibilities for each cell
 	 public HashSet[][] toDisplayPos() {
 		HashSet[][] temp = new HashSet[9][9];
 		for (int r = 0; r < 9; r++) {
 			for ( int c = 0; c < 9; c++) {
 					temp[r][c] = (HashSet) grid[r][c].getPos(); 				
 			}
 		}
 		
 		return temp;
 	}
 	
 	//these are so brilliant named they don't need individual comments	
	public int getCount() {
		return count;
	}
	
	public int[][] getOrg() {
		return original;
	}
	
	public int[][] getGen() {
		return generated;
	}
	
	public int getTurn() {
		return turns;
	}
	
	public int getStartCount() {
		return startCount;
	}
	
	public boolean isValid() {
		for (int a = 0; a < 3; a++) {
			for (int b = 0; b < 9; b++) {
				if (!groups[a][b].isValid()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public int getWipes() {
		return wipes;
	}
	
}