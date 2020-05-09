//Author: Harry Sivasubramaniam
//Email: hsivasub@uwaterloo.ca
//Instructions: this program will read a file called "grid.txt" stored in the directory indicated 
//in the main function. If "grid.txt" corresponds to a valid sudoku puzzle then this program will
//print out the solution else it will print out "Invalid Sudoku Puzzle"

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class solver {
	public class Position {// used to return x,y positions of a cell on our grid
		int row;
		int col;
	}

	public static boolean solveSudoku(int[][] grid) { // returns true if solutions is found
		Position pos = new solver().new Position();

		// findCell will take the grid and set pos to the position of the first empty
		// cell
		if (findCell(grid, pos) == false) {// findCell returns false if the grid is full(has been solved)
			return true; // yay! our puzzle has a solution
		}

		for (int i = 1; i <= 9; i++) {
			grid[pos.row][pos.col] = i; // BRUTE FORCE!!!!
			if (isValid(grid, pos)) {
				if (solveSudoku(grid)) {
					return true; // yay our grid is solved
				}
			}
		}
		grid[pos.row][pos.col] = 0; // resets the current position
		return false; // returns false if current state of grid is not solvable(requires backtrack)
	}

	public static boolean isValid(int[][] grid, Position pos) {// checks if grid is valid when pos is inserted
		// grid is valid if numbers in rows,column and boxes are unique or 0
		for (int i = 0; i < grid.length; i++) {
			// loops through columns of given row of the pos and checks if valid
			if (grid[pos.row][i] == grid[pos.row][pos.col] && i != pos.col) {
				return false;
			}
			// loops through rows of given column of pos in grid and checks if valid
			if (grid[i][pos.col] == grid[pos.row][pos.col] && i != pos.row) {
				return false;
			}
		}
		// checks if the 3x3 boxes are valid
		int factor = (int)Math.sqrt(grid.length);
		int r = (pos.row / factor) * factor;
		int c = (pos.col / factor) * factor;
		for (int i = 0; i < factor; i++) {
			for (int j = 0; j < factor; j++) {
				if ((i + r) == pos.row && (j + c) == pos.col) {
					continue;// we skip pos
				}
				if (grid[i + r][j + c] == grid[pos.row][pos.col]) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean findCell(int[][] grid, Position pos) { // returns
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] == 0) {// sets the pos of empty cell in our grid
					pos.row = i;
					pos.col = j;
					return true; // returns true to indicate empty cell exists
				}
			}
		}
		return false; // if we loop through grid and no empty cell is found then return false
	}

	public static void printGrid(int[][] grid) {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				System.out.print(grid[i][j] + " "); // prints each cell of grid
			}
			System.out.println(); // prints a new line
		}
	}

	static int[][] matrix;
	static int[] numbers;
	public static void main(String args[]) {
		try {
			// reads text file from directory (change this to where your text file is
			// located)

			List<String> list = new ArrayList<>();
			File file = new File("test.in");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while (line != null) {
				list.add(line);
				line = br.readLine();
			}
			int size = Integer.parseInt(list.get(0));
			matrix = new int[size][size];
			String[] nums = list.get(1).split(" ");
			numbers = new int[nums.length];
			for (int i = 0; i < nums.length; i++) {
				numbers[i] = Integer.parseInt(nums[i]);
			}
			if (!validate(matrix, numbers)) {
				throw new IOException();
			}
			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					matrix[i][j] = 0;
				}
			}
			for (int i = 2; i < list.size(); i++) {
				String[] hint = list.get(i).split(" ");
				String[] coordinate = hint[0].split(",");

				int row = Integer.parseInt(coordinate[0]);
				int col = Integer.parseInt(coordinate[1]);
				int fill = Integer.parseInt(hint[1]);

				matrix[row][col] = fill;
			}

			// prints original puzzle
			printGrid(matrix);

			if (solveSudoku(matrix)) { // solveSudoku(grid) attempts to solve grid and returns true if solution is
										// found
				System.out.println("Solution:");
				printGrid(matrix);
			} else { // if solveSudoku(grid) returns false then no solution exists for given puzzle
				System.out.println("Invalid Sudoku Puzzle");
			}
		} catch (IOException e) {
			System.out.println("File I/O Error");
		}

	}

	public static boolean validate(int[][] matrix, int[] numbers) {
		// TODO
		double rows = matrix.length;
		double cols = matrix[0].length;
		double root = Math.sqrt(rows);
		boolean condition1 = (rows == cols && root == Math.floor(root));

		List<Integer> tries;
		boolean condition2 = true;
		for (int i = 0; i < rows; i++) {
			tries = new ArrayList<>();
			for (int j = 0; j < cols; j++) {
				int value = matrix[i][j];
				if (value != 0) {
					if (hasNum(numbers, value)) {
						tries.add(value);
					} else {
						condition2 = false;
					}
				}
				tries = new ArrayList<>();
			}
		}

		boolean condition3 = true;
		for (int i = 0; i < cols; i++) {
			tries = new ArrayList<>();
			for (int j = 0; j < rows; j++) {
				int value = matrix[i][j];
				if (value != 0) {
					if (hasNum(numbers, value)) {
						tries.add(value);
					} else {
						condition3 = false;
					}
				}
				tries = new ArrayList<>();
			}
		}

		// TODO

		// placeholder
		return condition1 && condition2 && condition3;
	} // end of validate()

	private static boolean hasNum(int[] ints, int n) {
		for (int i = 0; i < ints.length; i++) {
			if (ints[i] == n) {
				return true;
			}
		}
		return false;
	}

}
