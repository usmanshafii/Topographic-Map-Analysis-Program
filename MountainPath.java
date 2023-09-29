//Mohammad Usman
//25177


import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MountainPath {
    public static void main(String[] args) throws FileNotFoundException {
        // a class for maintaining a 2D array of ints
        // representing a topographic map

        init("Colorado_844cols_480rows.dat", 480, 844);
        System.out.println("Min: " + findMin());
        System.out.println("Max: " + findMax());
        StdDraw.setCanvasSize(844, 480);
        drawMap();
        StdDraw.setPenColor(Color.RED);
        int ChangeInElev = drawLowestElevPath(0);
        int LowestElev = indexOfLowestElevPath();
        StdDraw.setPenColor(Color.GREEN);
        ChangeInElev = drawLowestElevPath( LowestElev);
        System.out.println("Row with Lowest Elevation Change: " + LowestElev);
        System.out.println("Change in Elevation: "+ ChangeInElev);
    }

    public static int[][] grid;
    // elevation data will be stored here

    public static void init(String filename, int rows, int columns) throws FileNotFoundException {
        //reads data from the file into 2D array

        MountainPath.grid = new int[rows][columns];
        Scanner sc;
        sc = new Scanner(new File(filename));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                MountainPath.grid[i][j] = sc.nextInt();
            }
        }
    }

    public static int findMin() {
        // return the minimum value in the map

        int min = Integer.MAX_VALUE;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (MountainPath.grid[i][j] < min)
                    min = MountainPath.grid[i][j];
            }
        }
        return min;
    }

    public static int findMax() {
        // return the max value in the map
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (MountainPath.grid[i][j] > max)
                    max = MountainPath.grid[i][j];
            }
        }
        return max;
    }

    public static void drawMap() {
        // draw this map in B & W using StdDraw library

        StdDraw.setXscale(0, 844);
        StdDraw.setYscale(0, 480);
        int min= findMin();
        int max= findMax();
        StdDraw.enableDoubleBuffering();
        double scaleColors = 255.0 / (max - min);
        for (int i = 0; i < MountainPath.grid.length; i++) {
            for (int j = 0; j < MountainPath.grid[0].length; j++) {
                int c = (int) ((MountainPath.grid[i][j] - min) * scaleColors);
                StdDraw.setPenColor(new Color(c, c, c));
                StdDraw.filledSquare(j, MountainPath.grid.length - i - 1, 0.5);
            }
        }
        StdDraw.show();
    }

    public static int drawLowestElevPath(int rows) {
        // draw the lowest elevation path starting from the given row
        // return total elev change from the path

        int max = findMax();
        int ChangeInElev = 0;
        int col=0;
        StdDraw.enableDoubleBuffering();
        while (col < grid[0].length - 1) {
            int east = grid[rows][col + 1];
            int northEast = -1;
            int southEast = -1;
            int eastChange = Math.abs(grid[rows][col] - east);
            int northEastchange = max + 1;
            int southEastchange = max + 1;
            int Lowest = Math.abs(grid[rows][col] - east);
            StdDraw.filledSquare(col,grid.length - rows - 1, 1);
            if (rows != 0) {
                northEast = grid[rows - 1][col + 1];
            }
            if (rows != grid.length - 1) {
                southEast = grid[rows + 1][col + 1];
            }
            if (northEast > -1) {
                northEastchange = Math.abs(grid[rows][col] - northEast);
            }
            if (southEast > -1) {
                southEastchange = Math.abs(grid[rows][col] - southEast);
            }
            if (eastChange > northEastchange) {
                if (northEastchange > southEastchange) {
                    Lowest = southEastchange;
                    rows++;
                } else {
                    Lowest = northEastchange;
                    rows--;
                }
            } else {
                if (eastChange > southEastchange) {
                    Lowest = southEastchange;
                    rows++;
                } else {
                    Lowest = eastChange;
                }
            }
            ChangeInElev = ChangeInElev + Lowest;
            col++;
        }
        StdDraw.show();
        return ChangeInElev;
    }

    public static int indexOfLowestElevPath() {
        // find the lowest elev change path in the map
        // return the row it starts on

        int LowestElevPath = drawLowestElevPath(0);
        int k = 1;
        int RowIndex = 0;
        while (k < grid.length) {
            int NewPath = drawLowestElevPath(k);
            if (NewPath < LowestElevPath) {
                LowestElevPath = NewPath;
                RowIndex = k;
            }
            k++;
        }
        return RowIndex;
    }
}
