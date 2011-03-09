import java.util.*;

//The Selection class represents a group of 9 cells in the sudoku puzzle. 
//the numbers 1-9 must be obtained in every selection with no repeats when the sudoku is solved
public class Selection{
	private Cell[] group;
	private HashSet full;
	private ArrayList unsol , unsolNums;
	private String type;
	private int index;
	private boolean solved;
	
	public Selection(String name, Cell[] set, int i){
		group = set;
		unsol = new ArrayList();
		unsolNums = new ArrayList();
		full = new HashSet();
		for (int k = 0; k < 9; k++) {
			unsol.add(group[k]);
			unsolNums.add(new Integer(k+1));
			full.add(new Integer(k+1));
		}
		type = name;
		index = i;
		solved = false;
	}
	
	public String getType() {
		return type;
	}
	
	public boolean isValid() {
		HashSet temp = new HashSet();
		for (int k = 0; k < 9; k++) {
			temp.add((Integer)(group[k].getSol()));
		}
		return (temp.equals(full));

	}
	
	public Cell[] getCells() {
		return group;
	}
	
	
	public ArrayList unsolvedList() {
		return unsol;
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean isSolved() {
		return solved;
	}
	
	public ArrayList getUnsolNums() {
		return unsolNums;
	}
	
	//removes a given possibility from every cell in the selection. 
	//Used whenever a new cell is solved, therefore eliminating the solution as a possibility
	//from all cells in the selections that cell belongs to.
	//returns a list of single possibility cells created through this method. So they can be updated.
	public ArrayList removePos(int num) {
		ArrayList lones = new ArrayList();
		for (int k = 0; k < 9; k++) {
			Cell temp = group[k];
			if (temp.remove(num)) {
				solve(temp);
				lones.add(temp);
			}
		}
		
		return lones;			
	}
	
	//updates the unsolved cells list and unsolved numbers list whenever a cell is solved.
	public boolean solve(Cell c) {
		if (c.isSolved()) {
			unsol.remove(c);
			unsolNums.remove((Integer)c.getSol());
			if (unsol.isEmpty())
				solved = true;
			return true;
		}
		
		return false;
	}
	
	//hidden Loner. See hiddenLone() in Sudoku class for description of trick
	//solves hidden loner and return a list of the solved cells to be udpated.
	public ArrayList hLone(){
		ArrayList hiddens = new ArrayList();
		for (int k = 0; k < unsol.size(); k++) {
			Cell current = (Cell)unsol.get(k);
			Set currSet1 = current.getPos();
			Set currSet = new HashSet();
			Iterator itr = currSet1.iterator();
			while (itr.hasNext()) {
				currSet.add(itr.next());
			}
			for (int j = 0; j < unsol.size(); j++) {
				Cell temp = (Cell) unsol.get(j);
				if (!temp.equals(current)) {
					Set tempSet1 = temp.getPos();
					Set tempSet = new HashSet();
					itr = tempSet1.iterator();
					while (itr.hasNext()) {
						tempSet.add(itr.next());
					}
					currSet.removeAll(tempSet);
				}
			}
			if (currSet.size() == 1) {
				itr = currSet.iterator();
				int sol = (Integer) itr.next();
				current.solve(sol);
				hiddens.add(current);
			}
		}
		
		return hiddens;
	}

	
}
