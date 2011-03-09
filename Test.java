import java.util.*;

public class Test {
	
	public static void main (String[] args) {
		
		Sudoku s = new Sudoku();
		Date before, after;
		double time;
		int[][] display;
		int[][] org;
		HashSet[][] displayPos;
	/*	
	 	//single possibility test with 1 recursion
	 	s.add(0,0,1);
		s.add(0,1,2);
		s.add(0,2,3);
		s.add(1,0,4);
		s.add(1,1,5);
		s.add(1,2,6);
		s.add(2,0,7);
		s.add(5,2,8);
	
		//hidden single test
		s.add(0,3,1);
		s.add(1,7,1);
		s.add(3,0,1);
		s.add(7,1,1);
		
		
		//easy puzzle thats solved by using only 2 methods.
		s.add(0,0,2);
		s.add(1,1,5);
		s.add(2,1,9);
		s.add(0,4,4);
		s.add(1,3,7);
		s.add(1,4,9);
		s.add(2,3,8);
		s.add(2,5,6);
		s.add(0,6,1);
		s.add(0,8,7);
		s.add(1,6,8);
		s.add(1,7,2);
		s.add(2,6,5);
		s.add(3,0,7);
		s.add(3,2,6);
		s.add(4,0,4);
		s.add(5,0,9);
		s.add(3,5,2);
		s.add(5,3,6);
		s.add(3,8,1);
		s.add(4,8,9);
		s.add(5,6,3);
		s.add(5,8,8);
		s.add(6,2,3);
		s.add(7,1,7);
		s.add(7,2,9);
		s.add(8,0,5);
		s.add(8,2,1);
		s.add(6,3,2);
		s.add(6,5,7);
		s.add(7,4,1);
		s.add(7,5,5);
		s.add(8,4,6);
		s.add(6,7,1);
		s.add(7,7,4);
		s.add(8,8,3);
	
	
		//BoxToLine Test
	 	s.add(0,0,1);
		s.add(1,0,4);
		s.add(1,6,2);
		s.add(1,8,3);
		s.add(2,0,7);
		s.add(2,1,8);
		s.add(2,2,9);
		s.add(0,3,4);
		s.add(1,3,9);
		s.add(1,4,7);
		s.add(1,5,8);
		s.add(2,3,2);
		s.add(2,4,1);
		s.add(5,4,5);
		s.add(4,2,6);
		
		
		// line to box Test
		s.add(0,0,1);
		s.add(0,1,2);
		s.add(0,2,3);
		s.add(0,3,4);
		s.add(0,4,5);
		s.add(0,5,6);
		s.add(1,3,1);
		s.add(1,4,2);
		s.add(1,5,3);
		s.add(1,6,5);
		s.add(3,7,4);
	*/	
	/*	//Evil 
		s.add(0,1,4);
		s.add(1,2,6);
		s.add(2,0,5);
		s.add(0,5,2);
		s.add(1,3,4);
		s.add(2,3,3);
		s.add(2,4,9);
		s.add(1,6,7);
		s.add(2,7,4);
		s.add(3,0,7);
		s.add(4,0,4);
		s.add(5,1,5);
		s.add(4,4,2);
		s.add(3,7,3);
		s.add(4,8,9);
		s.add(5,8,7);
		s.add(6,1,6);
		s.add(7,2,8);
		s.add(6,4,8);
		s.add(6,5,3);
		s.add(7,5,1);
		s.add(8,3,2);
		s.add(6,8,2);
		s.add(7,6,6);
		s.add(8,7,7);
	
	*/
	/*	//hard	
		s.add(0,2,4);
		s.add(1,0,7);
		s.add(1,2,8);
		s.add(2,2,9);
		s.add(0,4,1);
		s.add(2,3,2);
		s.add(2,4,3);
		s.add(0,7,6);
		s.add(1,7,3);
		s.add(4,2,7);
		s.add(3,4,8);
		s.add(3,5,2);
		s.add(4,3,1);
		s.add(4,5,3);
		s.add(5,3,5);
		s.add(5,4,4);
		s.add(4,6,9);
		s.add(7,1,9);
		s.add(8,1,1);
		s.add(6,4,2);
		s.add(6,5,6);
		s.add(8,4,7);
		s.add(6,6,4);
		s.add(7,6,6);
		s.add(7,8,2);
		s.add(8,6,3);
	
	*/
	/*	
	 	//medium	
		s.add(0,0,3);
		s.add(0,1,9);
		s.add(0,2,7);
		s.add(2,0,4);
		s.add(1,3,8);
		s.add(2,3,3);
		s.add(1,6,5);
		s.add(1,8,7);
		s.add(2,7,1);
		s.add(3,0,8);
		s.add(3,2,9);
		s.add(4,1,3);
		s.add(4,2,2);
		s.add(5,2,1);
		s.add(3,3,2);
		s.add(3,4,3);
		s.add(5,4,9);
		s.add(5,5,8);
		s.add(3,6,7);
		s.add(4,6,9);
		s.add(4,7,8);
		s.add(5,6,3);
		s.add(5,8,6);
		s.add(6,1,2);
		s.add(7,0,9);
		s.add(7,2,3);
		s.add(6,5,6);
		s.add(7,5,4);
		s.add(6,8,3);
		s.add(8,6,1);
		s.add(8,7,9);
		s.add(8,8,8);
			
	*/	
	
	/*	//Evil 2
		s.add(0,1,6);
		s.add(1,1,8);
		s.add(0,4,3);
		s.add(1,5,1);
		s.add(2,3,6);
		s.add(0,8,4);
		s.add(2,8,9);
		s.add(3,0,8);
		s.add(4,1,2);
		s.add(5,0,9);
		s.add(5,2,6);
		s.add(3,5,4);
		s.add(5,3,5);
		s.add(3,6,6);
		s.add(3,8,2);
		s.add(4,7,8);
		s.add(5,8,1);
		s.add(6,0,4);
		s.add(8,0,1);
		s.add(6,5,8);
		s.add(7,3,2);
		s.add(8,4,9);
		s.add(7,7,7);
		s.add(8,7,5);
	*/	
	
	/*	//zomg naked triple wtf.
		s.add(0,1,7);
		s.add(0,3,1);
		s.add(0,4,9);
		s.add(0,8,4);
		s.add(1,1,8);
		s.add(1,3,7);
		s.add(1,4,4);
		s.add(1,7,1);
		s.add(2,2,2);
		s.add(2,3,6);
		s.add(2,5,3);
		s.add(2,6,7);
		s.add(2,7,9);
		s.add(3,3,8);
		s.add(3,4,7);
		s.add(3,6,4);
		s.add(3,7,3);
		s.add(3,8,1);
		s.add(4,0,3);
		s.add(4,8,7);
		s.add(5,0,7);
		s.add(5,2,8);
		s.add(5,5,1);
		s.add(6,1,6);
		s.add(6,3,5);
		s.add(6,6,2);
		s.add(7,1,3);
		s.add(7,4,6);
		s.add(7,5,9);
		s.add(8,0,8);
		s.add(8,5,7);
	*/
	
	/*	s.add(0, 1, 1);
		s.add(0, 5, 2);
		s.add(0, 7, 6);
		s.add(1, 0, 3);
		s.add(1, 4, 7);
		s.add(1, 8, 4);
		s.add(2, 3, 6);
		s.add(2, 4, 4);
		s.add(2, 5, 8);
		s.add(3, 0, 4);
		s.add(3, 2, 2);
		s.add(3, 6, 9);
		s.add(4, 1, 9);
		s.add(4, 2, 6);
		s.add(4, 4, 5);
		s.add(4, 6, 7);
		s.add(4, 7, 2);
		s.add(5, 2, 5);
		s.add(5, 6, 8);
		s.add(5, 8, 6);
		s.add(6, 3, 7);
		s.add(6, 4, 6);
		s.add(6, 5, 4);
		s.add(7, 0, 9);
		s.add(7, 4, 8);
		s.add(7, 8, 3);
		s.add(8, 1, 5);
		s.add(8, 3, 1);
		s.add(8, 7, 8);	
	*/
	
	/*	s.add(1,0,1);
		s.add(1,1,4);
		s.add(2,0,8);
		s.add(1,5,3);
		s.add(0,6,1);
		s.add(0,7,3);
		s.add(1,7,8);
		s.add(2,8,6);
		s.add(3,0,7);
		s.add(4,1,1);
		s.add(3,3,9);
		s.add(3,4,6);
		s.add(4,3,8);
		s.add(4,5,7);
		s.add(5,4,2);
		s.add(5,5,4);
		s.add(4,7,2);
		s.add(5,8,7);
		s.add(6,0,9);
		s.add(7,1,8);
		s.add(8,1,6);
		s.add(8,2,7);
		s.add(7,3,7);
		s.add(6,8,4);
		s.add(7,7,9);
		s.add(7,8,2);	
	*/	
	/*
		s.add(1,6,3);
		s.add(1,7,6);
		s.add(1,8,7);
		s.add(2,5,4);
		s.add(2,6,1);
		s.add(2,8,9);
		s.add(3,5,7);
		s.add(3,7,2);
		s.add(3,8,8);
		s.add(4,1,7);
		s.add(4,2,4);
		s.add(4,3,1);
		s.add(4,5,9);
		s.add(4,6,6);
		s.add(4,7,5);
		s.add(5,0,5);
		s.add(5,1,6);
		s.add(5,3,3);
		s.add(6,0,3);
		s.add(6,2,9);
		s.add(6,3,5);
		s.add(7,0,4);
		s.add(7,1,8);
		s.add(7,2,2);
	*/	
	/*
		s.add(0,0,1);
		s.add(0,1,2);
		s.add(0,2,3);
		s.add(0,3,4);
		s.add(0,4,5);
		s.add(0,5,6);
		s.add(0,6,7);
		s.add(0,7,8);
		s.add(0,8,9);
	*/
		
		org = s.getOrg();
		//Records how long it took to generate puzzle
		before = new Date();
		//generates a puzzle
		s.generate();
		after = new Date();
		time = (double)after.getTime() - before.getTime();
		time = time / 1000;
		
		display = s.toDisplay();
		displayPos = s.toDisplayPos();
		
		//displays the original puzzle. even before generation. This way you can generate from
		//a partially filled grid.	
		System.out.println("Sudoku Original");
		display(org);
		
		//the final solved puzzle created by generation.
		System.out.println("Sudoku Solved");
		display(display);
		
		//stats
		System.out.println("Start count:  " + s.getStartCount());
		System.out.println("Start unsolved count:  "  + (81 - s.getStartCount()));
		System.out.println("Unsolved count:  " + s.getCount());
		System.out.println("Turns took:  " + s.getTurn());
		System.out.println("Valid Solution:  " + s.isValid());
		System.out.println("Wipe Count:  " + s.getWipes());
		System.out.println("Generate time:  " + time + "seconds\n\n");
		
		//passes the generated puzzle to a new Sudoku.
		s = new Sudoku(s.getGen());
		
	
		//solves it
		s.solveSudoku();
		display = s.toDisplay();
		org = s.getOrg();
		displayPos = s.toDisplayPos();
		
		//This is the puzzle that was generated
		System.out.println("Sudoku Generated");
		display(org);
		
		//Solution, should be same as before.
		System.out.println("Sudoku Solved");
		display(display);
		displayPos(displayPos);
		System.out.println("Start count:  " + s.getStartCount());
		System.out.println("Start unsolved count:  "  + (81 - s.getStartCount()));
		System.out.println("Unsolved count:  " + s.getCount());
		System.out.println("Turns took:  " + s.getTurn());
		System.out.println("Valid Solution:  " + s.isValid());
		
		
		
	}
	
	//crappy ASCII art display of the grid
	public static void display(int[][] temp) {
		
		for (int r = 0; r < 9; r ++) {			
			if (r % 3 == 0) 
				System.out.println("_____________________________________\n");
			for (int c = 0; c < 9; c++) {
				if (c % 3 == 0)
					System.out.print("|  ");
					
				if (temp[r][c] == 0) System.out.print("   ");
				else 
				System.out.print(temp[r][c] + "  ");
				
				if (c == 8)
					System.out.print("|");
			}
			System.out.println("\n");
		}
		
		System.out.println("_____________________________________\n");
		
		
	}
	
	//crappy ASCII art display of possibilities
	public static void displayPos(HashSet[][] temp) {
		
		for (int r = 0; r < 9; r ++) {			
			if (r % 3 == 0) 
				System.out.println("_____________________________________\n");
			for (int c = 0; c < 9; c++) {
				if (c % 3 == 0)
					System.out.print("|  ");
					
				System.out.print(temp[r][c] + "  ");
				
				if (c == 8)
					System.out.print("|");
			}
			System.out.println("\n");
		}
		
		System.out.println("_____________________________________\n");
		
		
	}
}

