import java.util.*;

//the cell class represents a single cell in the 9 by 9 grid of Sudoku puzzle
public class Cell {

	private Set possi;
	private boolean solved;
	private int row, col, sol;
	
	
	public Cell(int x, int y) {
	
		row = x;
		col = y;
		solved = false;
		sol = 0;
		possi = new HashSet();
		for (int k=1; k < 10; k++) {
			possi.add(new Integer(k));
		}
	}
	
	//removes the given number as a possibility for this cell
	//returns true if a single possibility cell is created this way and the single is solved.
	//Single Possibility Cell - if there is only 1 possibility left for this cell. Then 
	//that has to be the solution for this cell
	public boolean remove(int k) {
		if (!isSolved()) {
			possi.remove(new Integer(k));
			if (possi.size() == 1) {
				Iterator itr = possi.iterator();
				int s = (Integer)itr.next();			
				solve(s);
				return true;
			}
		}
		return false;
	}
	
	//Retains the possibilities given in the list. All other possibilities are removed.
	//Used in the hiddenPair and hiddenTriple tricks.
	public void retain(ArrayList temp) {
		possi.retainAll(temp);
	}
	
	//returns wether a given number is still a possibility in this cell
	public boolean contains(int k) {
		return possi.contains(new Integer(k));
	}
	
	//turns the possibility Set of cells into an arraylist of those cells
	//HashSet provides much better complexity in general. However I cannot grab a random index
	//in a HashSet. So this is used to generate puzzles.
	public ArrayList toArrayList(){
		ArrayList temp = new ArrayList();
		Iterator itr = possi.iterator();
		while (itr.hasNext()) {
			temp.add(itr.next());
		}
		return temp;
	}
	
	//solves the cell with the given solution
	public void solve(int x) {
		solved = true;
		possi = new HashSet();
		possi.add((Integer) x);
		sol = x;
	}
	
	public boolean isSolved() {
		return solved;
	}
	
	public Set getPos() {
		return possi;		
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public int getSol() {
		return sol;
	}
	
	//the toString of the cell is three ints next to each other, row col and solution.
	//this was used for debugging purposes only. 
	public String toString() {
		return (row + "" +  col + sol);
	}
	
	
		
	
	
}