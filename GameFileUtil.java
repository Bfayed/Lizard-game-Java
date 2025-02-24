package hw3;

import java.io.File;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import api.BodySegment;
import api.Exit;
import api.Wall;

/**
 * Author and By Bavly Fayed section D from 2:15 till 3:15 Tancreti Utility
 * class with static methods for loading game files.
 * 
 */

public class GameFileUtil {

	/**
	 * 
	 * Loads the file at the given file path into the given game object. When the
	 * 
	 * method returns the game object has been modified to represent the loaded
	 * 
	 * game.
	 * 
	 * 
	 * 
	 * @param filePath the path of the file to load
	 * 
	 * @param game     the game to modify
	 * 
	 * @throws FileNotFoundException
	 * 
	 */

	public static void load(String filePath, LizardGame game) {
		Scanner scanner = null;

		try {
			scanner = new Scanner(new File(filePath));

			// Read grid dimensions
			String[] dimensions = scanner.nextLine().split("x");
			int width = Integer.parseInt(dimensions[0]);
			int height = Integer.parseInt(dimensions[1]);
			game.resetGrid(width, height);

			// Read walls and exits
			for (int y = 0; y < height; y++) {
				String line = scanner.nextLine();
				for (int x = 0; x < width; x++) {
					char cellType = line.charAt(x);
					if (cellType == 'W') {
						game.addWall(new Wall(game.getCell(x, y)));
					} else if (cellType == 'E') {
						game.addExit(new Exit(game.getCell(x, y)));
					}
				}
			}

			// Read lizards
			while (scanner.hasNextLine()) {
				Lizard lizard = new Lizard();
				ArrayList<BodySegment> bodySegments = new ArrayList<>();
				String lizardLine = scanner.nextLine().trim();
				String[] segmentCoordinates = lizardLine.substring(1).trim().split(" ");
				for (String segmentCoord : segmentCoordinates) {
					String[] coordinates = segmentCoord.split(",");
					int x = Integer.parseInt(coordinates[0]);
					int y = Integer.parseInt(coordinates[1]);
					BodySegment bodySegment = new BodySegment(lizard, game.getCell(x, y));
					bodySegments.add(bodySegment);
				}
				lizard.setSegments(bodySegments);
				game.addLizard(lizard);
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + filePath);
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}
}