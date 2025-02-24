package hw3;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import api.Cell;
import api.Direction;
import api.Exit;
import api.ScoreUpdateListener;
import api.ShowDialogListener;
import api.Wall;

/**
 * Class that models a game.
 * Author: Bavly Fayed
 * Section: D from 2:15 till 3:15
 * Instructor: Tancreti
 */
public class LizardGame {

    private ShowDialogListener dialogListener;
    private ScoreUpdateListener scoreListener;
    private int width;
    private int height;
    private Cell[][] grid;
    private ArrayList<Lizard> lizards;

    /**
     * Constructs a new LizardGame object with given grid dimensions.
     *
     * @param width  number of columns
     * @param height number of rows
     */
    public LizardGame(int width, int height) {
        lizards = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.grid = new Cell[width][height];
        // Initialize the grid with empty cells
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                grid[x][y] = new Cell(x, y);
            }
        }
    }

    // Getter methods
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    /**
     * Adds a wall to the grid.
     *
     * @param wall to add
     */
    public void addWall(Wall wall) {
        Cell wallCell = wall.getCell();
        int x = wallCell.getCol();
        int y = wallCell.getRow();
        grid[x][y].placeWall(wall);
    }

    /**
     * Adds an exit to the grid.
     *
     * @param exit to add
     */
    public void addExit(Exit exit) {
        exit.getCell().placeExit(exit);
    }

    /**
     * Gets a list of all lizards on the grid. Does not include lizards that have exited.
     *
     * @return lizards list of lizards
     */
    public ArrayList<Lizard> getLizards() {
        return lizards;
    }

    /**
     * Adds the given lizard to the grid.
     *
     * @param lizard to add
     */
    public void addLizard(Lizard lizard) {
        // Place the lizard on the grid cells
        lizard.getSegments().get(0).getCell().placeLizard(lizard);
        for (int i = 0; i < lizard.getSegments().size(); i++) {
            Cell lizz = lizard.getSegments().get(i).getCell();
            int x = lizz.getCol();
            int y = lizz.getRow();
            grid[x][y].placeLizard(lizard);
        }
        lizards.add(lizard);
        // Update score
        scoreListener.updateScore(getLizards().size());
    }

    /**
     * Removes the given lizard from the grid.
     *
     * @param lizard to remove
     */
    public void removeLizard(Lizard lizard) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = grid[x][y];
                if (cell.getLizard() == lizard) {
                    cell.removeLizard(); // Clear the lizard from the cell
                }
            }
        }
        lizards.remove(lizard);
        scoreListener.updateScore(getLizards().size());
        if (getLizards().isEmpty()) {
            dialogListener.showDialog("You win!"); // Display win message
        }
    }


	/**
	 * 
	 * Gets the cell for the given column and row.
	 * 
	 * <p>
	 * 
	 * If the column or row are outside of the boundaries of the grid the method
	 * 
	 * returns null.
	 *
	 * 
	 * 
	 * @param col column of the cell
	 * 
	 * @param row of the cell
	 * 
	 * @return the cell or null
	 * 
	 */

	public Cell getCell(int col, int row) {

		if (col >= 0 && col < width && row >= 0 && row < height) {
			return grid[col][row];

		}
		return null;
	}

	/**
	 * 
	 * Gets the cell that is adjacent to (one over from) the given column and row,
	 * 
	 * when moving in the given direction. For example (1, 4, UP) returns the cell
	 * 
	 * at (1, 3).
	 * 
	 * <p>
	 * 
	 * If the adjacent cell is outside of the boundaries of the grid, the method
	 * 
	 * returns null.
	 *
	 * 
	 * 
	 * @param col the given column
	 * 
	 * @param row the given row
	 * 
	 * @param dir the direction from the given column and row to the adjacent cell
	 * 
	 * @return the adjacent cell or null
	 * 
	 */

	public Cell getAdjacentCell(int col, int row, Direction dir) {
	    int nextCol = col;
	    int nextRow = row;
	    if (dir.equals(Direction.UP)) {
	        nextRow--;
	    } else if (dir.equals(Direction.DOWN)) {
	        nextRow++;
	    }
	    if (dir.equals(Direction.LEFT)) {
	        nextCol--;
	    } else if (dir.equals(Direction.RIGHT)) {
	        nextCol++;
	    }
	    if (nextCol >= 0 && nextCol < width && nextRow >= 0 && nextRow < height) {
	        return grid[nextCol][nextRow];
	    }
	    return null;
	}

	/**
	 * Resets the grid. After calling this method the game should have a grid of
	 * size width x height containing all empty cells. Empty means cells with no
	 * walls, exits, etc.
	 *
	 * @param width  number of columns of the resized grid
	 * @param height number of rows of the resized grid
	 */
	public void resetGrid(int width, int height) {
	    this.width = width;
	    this.height = height;
	    this.grid = new Cell[width][height];
	    // Initialize the grid with empty cells
	    for (int x = 0; x < width; x++) {
	        for (int y = 0; y < height; y++) {
	            grid[x][y] = new Cell(x, y);
	        }
	    }
	    lizards.clear(); // Clear the lizards from the grid
	}


	/**
	 * 
	 * Returns true if a given cell location (col, row) is available for a lizard to
	 * 
	 * move into. Specifically the cell cannot contain a wall or a lizard. Any other
	 * 
	 * type of cell, including an exit is available.
	 *
	 * 
	 * 
	 * @param row of the cell being tested
	 * 
	 * @param col of the cell being tested
	 * 
	 * @return true if the cell is available, false otherwise
	 * 
	 */

	public boolean isAvailable(int col, int row) {

		if (col < 0 || col >= width || row < 0 || row >= height) {
			return false; 

		}

		Cell cell = grid[col][row];
		return cell.getWall() == null && cell.getLizard() == null; // Example condition
	}

	/**
	 * 
	 * Move the lizard specified by its body segment at the given position (col,
	 * 
	 * row) one cell in the given direction. The entire body of the lizard must move
	 * 
	 * in a snake like fashion, in other words, each body segment pushes and pulls
	 * 
	 * the segments it is connected to forward or backward in the path of the
	 * 
	 * lizard's body. The given direction may result in the lizard moving its body
	 * 
	 * either forward or backward by one cell.
	 * 
	 * <p>
	 * 
	 * The segments of a lizard's body are linked together and movement must always
	 * 
	 * be "in-line" with the body. It is allowed to implement movement by either
	 * 
	 * shifting every body segment one cell over or by creating a new head or tail
	 * 
	 * segment and removing an existing head or tail segment to achieve the same
	 * 
	 * effect of movement in the forward or backward direction.
	 * 
	 * <p>
	 * 
	 * If any segment of the lizard moves over an exit cell, the lizard should be
	 * 
	 * removed from the grid.
	 * 
	 * <p>
	 * 
	 * If there are no lizards left on the grid the player has won the puzzle the
	 * 
	 * the dialog listener should be used to display (see showDialog) the message
	 * 
	 * "You win!".
	 * 
	 * <p>
	 * 
	 * It is possible that the given direction is not in-line with the body of the
	 * 
	 * lizard (as described above), in that case this method should do nothing.
	 * 
	 * <p>
	 * 
	 * It is possible that the given column and row are outside the bounds of the
	 * 
	 * grid, in that case this method should do nothing.
	 * 
	 * <p>
	 * 
	 * It is possible that there is no lizard at the given column and row, in that
	 * 
	 * case this method should do nothing.
	 * 
	 * <p>
	 * 
	 * It is possible that the lizard is blocked and cannot move in the requested
	 * 
	 * direction, in that case this method should do nothing.
	 * 
	 * <p>
	 * 
	 * <b>Developer's note: You may have noticed that there are a lot of details
	 * 
	 * that need to be considered when implement this method method. It is highly
	 * 
	 * recommend to explore how you can use the public API methods of this class,
	 * 
	 * Grid and Lizard (hint: there are many helpful methods in those classes that
	 * 
	 * will simplify your logic here) and also create your own private helper
	 * 
	 * methods. Break the problem into smaller parts are work on each part
	 * 
	 * individually.</b>
	 *
	 * 
	 * 
	 * @param col the given column of a selected segment
	 * 
	 * @param row the given row of a selected segment
	 * 
	 * @param dir the given direction to move the selected segment
	 * 
	 */

	public void move(int col, int row, Direction dir) {
	    // Check if the coordinates are within the grid bounds
	    if (col < 0 || col >= width || row < 0 || row >= height) {
	        return; // Ensure coordinates are within grid bounds.
	    }

	    // Get the current cell, lizard, and the next cell based on the direction
	    Cell currentCell = grid[col][row];
	    Lizard lizard = currentCell.getLizard();
	    Cell nextCell = getAdjacentCell(col, row, dir);

	    // If there is no lizard in the current cell, return
	    if (lizard == null) {
	        return;
	    }

	    // If the lizard can move ahead, recursively move the head segment
	    if (moveahead(col, row, lizard, nextCell)) {
	        move(lizard.getHeadSegment().getCell().getCol(), lizard.getHeadSegment().getCell().getRow(), dir);
	    }
	    // If the lizard can move behind, recursively move the tail segment
	    else if (movebehind(col, row, lizard, nextCell)) {
	        move(lizard.getTailSegment().getCell().getCol(), lizard.getTailSegment().getCell().getRow(), dir);
	    }
	    // If the lizard can move its body, recursively move the head segment
	    else if (movebody(col, row, lizard, nextCell)) {
	        move(lizard.getHeadSegment().getCell().getCol(), lizard.getHeadSegment().getCell().getRow(), dir);
	    }
	    // If the next cell is unavailable, return
	    else if (nextCell == null || !isAvailable(nextCell.getCol(), nextCell.getRow())) {
	        return;
	    }

	    // If the lizard cannot move in the specified direction, return
	    if (lizard == null || !canMove(lizard, dir, currentCell)) {
	        return;
	    }

	    // Obtain coordinates of head and tail segments
	    int x = lizard.getHeadSegment().getCell().getCol();
	    int y = lizard.getHeadSegment().getCell().getRow();
	    int x2 = lizard.getTailSegment().getCell().getCol();
	    int y2 = lizard.getTailSegment().getCell().getRow();

	    // Move the head segment based on direction
	    if (x == col && y == row && dir.equals(Direction.RIGHT)) {
	        // Move head segment to the right
	        Cell temp = lizard.getHeadSegment().getCell();
	        lizard.getHeadSegment().setCell(grid[x + 1][y]);

	        // Move body segments
	        for (int i = lizard.getSegments().size() - 2; i >= 0; i--) {
	            Cell anotherCell = lizard.getSegments().get(i).getCell();
	            lizard.getSegments().get(i).setCell(temp);
	            temp = anotherCell;
	        }

	        // Remove the tail segment from its previous cell
	        grid[x2][y2].removeLizard();
	    }

		else if (x == col && y == row && dir.equals(Direction.LEFT)) {
			Cell temp = lizard.getHeadSegment().getCell();
			lizard.getHeadSegment().setCell(grid[x - 1][y]);

			for (int i = lizard.getSegments().size() - 2; i >= 0; i--) {
				Cell anotherCell = lizard.getSegments().get(i).getCell();
				lizard.getSegments().get(i).setCell(temp);
				temp = anotherCell;

			}

			grid[x2][y2].removeLizard();

		}

		else if (x == col && y == row && dir.equals(Direction.UP)) {
			Cell temp = lizard.getHeadSegment().getCell();
			lizard.getHeadSegment().setCell(grid[x][y - 1]);

			for (int i = lizard.getSegments().size() - 2; i >= 0; i--) {
				Cell anotherCell = lizard.getSegments().get(i).getCell();
				lizard.getSegments().get(i).setCell(temp);
				temp = anotherCell;

			}

			grid[x2][y2].removeLizard();

		}

		else if (x == col && y == row && dir.equals(Direction.DOWN)) {
			Cell temp = lizard.getHeadSegment().getCell();
			lizard.getHeadSegment().setCell(grid[x][y + 1]);

			for (int i = lizard.getSegments().size() - 2; i >= 0; i--) {
				Cell anotherCell = lizard.getSegments().get(i).getCell();
				lizard.getSegments().get(i).setCell(temp);
				temp = anotherCell;

			}

			grid[x2][y2].removeLizard();

		}

		else if (x2 == col && y2 == row && dir.equals(Direction.RIGHT)) {
			Cell temp = lizard.getTailSegment().getCell();
			lizard.getTailSegment().setCell(grid[x2 + 1][y2]);

			for (int i = 1; i <= lizard.getSegments().size() - 1; i++) {
				Cell anotherCell = lizard.getSegments().get(i).getCell();
				lizard.getSegments().get(i).setCell(temp);
				temp = anotherCell;

			}

			grid[x][y].removeLizard();

		}

		else if (x2 == col && y2 == row && dir.equals(Direction.LEFT)) {
			Cell temp = lizard.getTailSegment().getCell();
			lizard.getTailSegment().setCell(grid[x2 - 1][y2]);

			for (int i = 1; i <= lizard.getSegments().size() - 1; i++) {
				Cell anotherCell = lizard.getSegments().get(i).getCell();
				lizard.getSegments().get(i).setCell(temp);
				temp = anotherCell;

			}

			grid[x][y].removeLizard();

		}

		else if (x2 == col && y2 == row && dir.equals(Direction.UP)) {
			Cell temp = lizard.getTailSegment().getCell();
			lizard.getTailSegment().setCell(grid[x2][y2 - 1]);

			for (int i = 1; i <= lizard.getSegments().size() - 1; i++) {
				Cell anotherCell = lizard.getSegments().get(i).getCell();
				lizard.getSegments().get(i).setCell(temp);
				temp = anotherCell;

			}

			grid[x][y].removeLizard();

		}

		else if (x2 == col && y2 == row && dir.equals(Direction.DOWN)) {
			Cell temp = lizard.getTailSegment().getCell();
			lizard.getTailSegment().setCell(grid[x2][y2 + 1]);

			for (int i = 1; i <= lizard.getSegments().size() - 1; i++) {
				Cell anotherCell = lizard.getSegments().get(i).getCell();
				lizard.getSegments().get(i).setCell(temp);
				temp = anotherCell;

			}

			grid[x][y].removeLizard();

		}

		checkAndHandleExitScenario(lizard, nextCell);

	}

	private boolean movebody(int col, int row, Lizard lizard, Cell nextCell) {

		if (nextCell == null)
			return false;

		for (int i = 1; i < lizard.getSegments().size() - 1; i++) {
			if (col == lizard.getSegments().get(i).getCell().getCol()
					&& row == lizard.getSegments().get(i).getCell().getRow()) {
				if (nextCell.getLizard() == null) {
					return false;

				}

				return true;

			}
		}
		return false;
	}

	/**
	 * 
	 * Determines if a lizard can move forward from its tail segment to the next
	 * cell.
	 * 
	 * This is used to validate whether the next cell in the direction of movement
	 * 
	 * corresponds to the second segment of the lizard's body, allowing for a
	 * forward
	 * 
	 * movement.
	 *
	 * 
	 * 
	 * @param col      the column of the tail segment
	 * 
	 * @param row      the row of the tail segment
	 * 
	 * @param lizard   the lizard to be moved
	 * 
	 * @param nextCell the cell to move into
	 * 
	 * @return true if the lizard can move forward; false otherwise
	 * 
	 */

	private boolean moveahead(int col, int row, Lizard lizard, Cell nextCell) {
	    // Check if the lizard's head is at the tail's position
	    if (col == lizard.getTailSegment().getCell().getCol() && row == lizard.getTailSegment().getCell().getRow()) {
	        // Check if the next cell is the next segment in the lizard's body
	        if (nextCell == lizard.getSegments().get(1).getCell()) {
	            return true; // Lizard can move its head forward
	        } else {
	            return false; // Lizard cannot move its head forward
	        }
	    }
	    return false; // Lizard cannot move its head forward
	}

	/**
	 * 
	 * Determines if a lizard can move backward from its head segment to the next
	 * cell.
	 * 
	 * This checks if the next cell in the opposite direction of movement
	 * corresponds
	 * 
	 * to the second-to-last segment of the lizard's body, allowing for a backward
	 * movement.
	 *
	 * 
	 * 
	 * @param col      the column of the head segment
	 * 
	 * @param row      the row of the head segment
	 * 
	 * @param lizard   the lizard to be moved
	 * 
	 * @param nextCell the cell to move into
	 * 
	 * @return true if the lizard can move backward; false otherwise
	 * 
	 */

	private boolean movebehind(int col, int row, Lizard lizard, Cell nextCell) {
	    // Check if the lizard's head is at the head's position
	    if (col == lizard.getHeadSegment().getCell().getCol() && row == lizard.getHeadSegment().getCell().getRow()) {
	        // Check if the next cell is the second-to-last segment in the lizard's body
	        if (nextCell == lizard.getSegments().get(lizard.getSegments().size() - 2).getCell()) {
	            return true; // Lizard can move its tail backward
	        } else {
	            return false; // Lizard cannot move its tail backward
	        }
	    }
	    return false; // Lizard cannot move its tail backward
	}

	/**
	 * 
	 * Checks if the lizard is able to move in the specified direction from the
	 * current cell.
	 * 
	 * This considers obstacles such as walls or other lizards in the next cell, as
	 * well as
	 * 
	 * the boundaries of the grid. It's a helper method used to ensure movements are
	 * valid
	 * 
	 * before executing them.
	 *
	 * 
	 * 
	 * @param lizard      the lizard attempting to move
	 * 
	 * @param dir         the direction in which the lizard is trying to move
	 * 
	 * @param currentCell the current position of the lizard
	 * 
	 * @return true if the lizard can move in the specified direction; false
	 *         otherwise
	 * 
	 */

	private boolean canMove(Lizard lizard, Direction dir, Cell currentCell) {

		Cell nextCell = getAdjacentCell(currentCell.getCol(), currentCell.getRow(), dir);
		if (nextCell == null || nextCell.getWall() != null || nextCell.getLizard() != null) {
			return false;
		}

		return true;

	}

	/**
	 * 
	 * Checks if moving a body segment triggers an exit scenario. If a segment
	 * enters
	 * 
	 * an exit cell, the lizard is removed. If all lizards have exited, a win
	 * condition is triggered.
	 *
	 * 
	 * 
	 * @param lizard     the lizard to check for exit conditions
	 * 
	 * @param targetCell the cell to check for an exit
	 * 
	 */

	private void checkAndHandleExitScenario(Lizard lizard, Cell targetCell) {

		if (targetCell.getExit() != null) {
			removeLizard(lizard);
			if (getLizards().isEmpty()) {
				showDialog("You win!");
			}
		}
	}

	/**
	 * 
	 * Displays a dialog with the specified message. This method is intended to be
	 * used
	 * 
	 * for game notifications, such as indicating a win condition.
	 *
	 * 
	 * 
	 * @param message the message to be displayed in the dialog
	 * 
	 */

	private void showDialog(String string) {

	}

	/**
	 * 
	 * Sets callback listeners for game events.
	 *
	 * 
	 * 
	 * @param dialogListener listener for creating a user dialog
	 * 
	 * @param scoreListener  listener for updating the player's score
	 * 
	 */

	public void setListeners(ShowDialogListener dialogListener, ScoreUpdateListener scoreListener) {

		this.setDialogListener(dialogListener);
		this.scoreListener = scoreListener;
	}

	/**
	 * 
	 * Load the game from the given file path
	 *
	 * 
	 * 
	 * @param filePath location of file to load
	 * 
	 * @throws FileNotFoundException
	 * 
	 */

	public void load(String filePath) {
		GameFileUtil.load(filePath, this);

	}

	@Override

	public String toString() {

		String str = "---------- GRID ----------\n";
		str += "Dimensions:\n";
		str += getWidth() + " " + getHeight() + "\n";
		str += "Layout:\n";
		for (int y = 0; y < getHeight(); y++) {
			if (y > 0) {
				str += "\n";
			}
			for (int x = 0; x < getWidth(); x++) {
				str += getCell(x, y);
			}
		}
		str += "\nLizards:\n";
		for (Lizard l : getLizards()) {
			str += l;
		}
		str += "\n--------------------------\n";
		return str;
	}
	/**
	 * 
	 * Assigns dialog and score update listeners to this game. These listeners are
	 * used
	 * 
	 * for displaying dialogs to the user and updating the score, respectively.
	 *
	 * 
	 * 
	 * @param dialogListener listener for dialog events
	 * 
	 * @param scoreListener  listener for score updates
	 * 
	 */

	private void setDialogListener(ShowDialogListener dialogListener) {
		this.dialogListener = dialogListener;

	}

}