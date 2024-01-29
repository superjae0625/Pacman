package assignment07;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InvalidObjectException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class PathFinder {
    // Member variables
    private static class Table {
        private String data;
        private int row, col;
        private boolean visited = false;
        private Table cameFrom = null;

        // Constructor
        Table(String data, int row, int col) {
            this.data = data;
            this.row = row;
            this.col = col;
        }

        // Helper method and returning adjacent vertices
        public Table[] getNeighbors(Table[][] maze, int rows, int cols) {
            Table[] neighbors = new Table[4]; //all directions
            int index = 0;
            for (int i = -1; i <= 1; i++) { // row
                for (int j = -1; j <= 1; j++) { // col
                    if (i == 0 && j == 0) continue;
                    else if (i == 0 || j == 0) { // adjacent to that Table
                        Table neighbor;
                        if (this.row + i > rows || this.row + i < 0 || this.col + j >= cols || this.col + j < 0) {
                            neighbor = null;
                        } else {
                            neighbor = maze[this.row + i][this.col + j];
                        }
                        if (neighbor == null || neighbor.data.equals("X")) {
                            neighbors[index] = null;
                        } else {
                            neighbors[index] = neighbor;
                        }
                        index++;
                    }
                }
            }
            return neighbors;
        }
    }

    private static class MazeGraph {
        private Table[][] maze;
        private int rows, cols; // rows and columns of the maze
        private Table start, end;

        // MazeGraph constructor using maze text file
        MazeGraph(String filename) throws FileNotFoundException {
            try {
                Scanner sc = new Scanner(new File(filename));
                // First row to identify number of rows and cols
                String dimension = sc.nextLine();
                String[] dimensions = dimension.split(" ");
                try {
                    this.rows = Integer.parseInt(dimensions[0]);
                    this.cols = Integer.parseInt(dimensions[1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    throw new NumberFormatException("Invalid number of rows or columns");
                }
                this.maze = new Table[this.rows][this.cols];
                for (int i = 0; i < this.rows; i++) {
                    // Read through all index in a row
                    String[] rowI = sc.nextLine().split("");
                    for (int j = 0; j< this.cols; j++) {
                        this.maze[i][j] = new Table(rowI[j], i, j);
                        // Start point
                        if (rowI[j].equals("S")) {
                            if (this.start != null) throw new InvalidObjectException("Start point already exists");
                            this.start = this.maze[i][j];
                        }
                        // End point
                        if (rowI[j].equals("G")) {
                            if (this.end != null) throw new InvalidObjectException("end point already exists");
                            this.end = this.maze[i][j];
                        }
                    }
                }
                if (this.end == null || this.start == null) throw new InvalidObjectException("No start or end point");
            } catch (FileNotFoundException | InvalidObjectException e) {
                e.printStackTrace();
                throw new FileNotFoundException("File does not exist");
            }
        }

        // Helper method to find the shortest path
        public void findPath(Table start, Table end) {
            start.visited = true;
            LinkedList<Table> vertices = new LinkedList<>();
            vertices.add(start);
            while (!vertices.isEmpty()) {
                Table current = vertices.remove();
                if (current.equals(end)) { // go back
                    while (!current.cameFrom.equals(start)) {
                        current.cameFrom.data = ".";
                        current = current.cameFrom;
                    }
                    return;
                }
                Table[] currentNeighbors = current.getNeighbors(this.maze, this.rows, this.cols);
                for (int i = 0; i < currentNeighbors.length; i++) {
                    if (currentNeighbors[i] != null && !currentNeighbors[i].visited) {
                        Table nextTable = currentNeighbors[i];
                        nextTable.visited = true;
                        nextTable.cameFrom = current;
                        vertices.add(nextTable);
                    }
                }
            }
        }

        // Write the output to the output file
        public void writeFile(String filename) throws FileNotFoundException {
            PrintWriter pw = new PrintWriter(filename);
            pw.println(this.rows + " " + this.cols); // first row
            for (int i = 0; i < this.rows; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < this.cols; j++) {
                    sb.append(this.maze[i][j].data);
                }
                pw.println(sb);
            }
            pw.flush();
            pw.close();
        }
    }

    // Default template to execute the program
    // Read a maze from a file with the given input name,
    // and output the solved maze to a file with the given output name.
    public static void solveMaze(String inputFile, String outputFile) throws FileNotFoundException {
        MazeGraph mazeMazeGraph = new MazeGraph(inputFile);
        mazeMazeGraph.findPath(mazeMazeGraph.start, mazeMazeGraph.end);
        mazeMazeGraph.writeFile(outputFile);
    }
}