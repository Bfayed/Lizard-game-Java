package hw3;

import java.util.ArrayList;


import api.BodySegment;

import api.Cell;

import api.Direction;

/**
 * Author and By Bavly Fayed section D from 2:15 till 3:15 Tancreti
 * 
 * Represents a Lizard as a collection of body segments.
 * 
 */

public class Lizard {

    private ArrayList<BodySegment> Liza;

    /**
     * Constructs a Lizard object with an empty list of segments.
     */
    public Lizard() {
        Liza = new ArrayList<>();
    }

    /**
     * Sets the segments of the lizard.
     * Segments should be ordered from tail to head.
     *
     * @param segments list of segments ordered from tail to head
     */
    public void setSegments(ArrayList<BodySegment> segments) {
        Liza = segments;
    }

    /**
     * Gets the segments of the lizard.
     * Segments are ordered from tail to head.
     *
     * @return a list of segments ordered from tail to head
     */
    public ArrayList<BodySegment> getSegments() {
        return Liza;
    }

    /**
     * Gets the head segment of the lizard.
     * Returns null if the segments have not been initialized or there are no segments.
     *
     * @return the head segment
     */
    public BodySegment getHeadSegment() {
        if (!Liza.isEmpty()) {
            return Liza.get(Liza.size() - 1);
        }
        return null;
    }

    /**
     * Gets the tail segment of the lizard.
     * Returns null if the segments have not been initialized or there are no segments.
     *
     * @return the tail segment
     */
    public BodySegment getTailSegment() {
        if (!Liza.isEmpty()) {
            return Liza.get(0);
        }
        return null;
    }

    /**
     * Gets the segment that is located at a given cell.
     *
     * @param cell to look for the segment
     * @return the segment that is on the cell or null if there is none
     */
    public BodySegment getSegmentAt(Cell cell) {
        for (BodySegment segment : Liza) {
            if (segment.getCell().equals(cell)) {
                return segment;
            }
        }
        return null;
    }

	/**
	 * 
	 * Get the segment that is in front of (closer to the head segment than) the
	 * 
	 * given segment. Returns null if there is no segment ahead.
	 * 
	 * 
	 * 
	 * @param segment the starting segment
	 * 
	 * @return the segment in front of the given segment or null
	 * 
	 */

	public BodySegment getSegmentAhead(BodySegment segment) {

		int index = Liza.indexOf(segment);
		if (index != -1 && index < Liza.size() - 1) {
			return Liza.get(index + 1);

		}

		return null;

	}

	/**
	 * 
	 * Get the segment that is behind (closer to the tail segment than) the given
	 * 
	 * segment. Returns null if there is not segment behind.
	 * 
	 * 
	 * 
	 * @param segment the starting segment
	 * 
	 * @return the segment behind of the given segment or null
	 * 
	 */

	public BodySegment getSegmentBehind(BodySegment segment) {

		int index = Liza.indexOf(segment);
		if (index > 0) {
			return Liza.get(index - 1);

		}
		return null;
	}

	/**
	 * 
	 * Gets the direction from the perspective of the given segment point to the
	 * 
	 * segment ahead (in front of) of it. Returns null if there is no segment ahead
	 * 
	 * of the given segment.
	 * 
	 * 
	 * 
	 * @param segment the starting segment
	 * 
	 * @return the direction to the segment ahead of the given segment or null
	 * 
	 */

	public Direction getDirectionToSegmentAhead(BodySegment segment) {

		BodySegment nextSegment = getSegmentAhead(segment);
		if (nextSegment == null) {
		
			return null;

		}

		int X = nextSegment.getCell().getRow() - segment.getCell().getRow();
		int Y = nextSegment.getCell().getCol() - segment.getCell().getCol();

		if (X > 0) {
			return Direction.DOWN;
		} else if (X < 0) {
			return Direction.UP;
		} else if (Y > 0) {
			return Direction.RIGHT;
		} else if (Y < 0) {
			return Direction.LEFT;

		}

		return null; // If the segments are in the same position or direction is not applicable.

	}

	/**
	 * 
	 * Gets the direction from the perspective of the given segment point to the
	 * 
	 * segment behind it. Returns null if there is no segment behind of the given
	 * 
	 * segment.
	 * 
	 * 
	 * 
	 * @param segment the starting segment
	 * 
	 * @return the direction to the segment behind of the given segment or null
	 * 
	 */

	public Direction getDirectionToSegmentBehind(BodySegment segment) {

		BodySegment segmentBehind = getSegmentBehind(segment);
		if (segmentBehind == null) {
			return null; // No segment behind, so no direction to calculate.

		}

		// Assuming Cell class has methods getX() and getY() to provide coordinates.

		int X = segment.getCell().getRow() - segmentBehind.getCell().getRow();
		int Y = segment.getCell().getCol() - segmentBehind.getCell().getCol();
		if (Y > 0) {
			return Direction.LEFT; // The current segment is to the right of the segment behind.
		} else if (Y < 0) {
			return Direction.RIGHT; // The current segment is to the left of the segment behind.
		} else if (X > 0) {
			return Direction.UP; // The current segment is below the segment behind.
		} else if (X < 0) {
			return Direction.DOWN; // The current segment is above the segment behind.

		}

		return null; // If the segments are in the same position or direction is not applicable.

	}

	/**
	 * 
	 * Gets the direction in which the head segment is pointing. This is the
	 * 
	 * direction formed by going from the segment behind the head segment to the
	 * 
	 * head segment. A lizard that does not have more than one segment has no
	 * 
	 * defined head direction and returns null.
	 * 
	 * 
	 * 
	 * @return the direction in which the head segment is pointing or null
	 * 
	 */

	public Direction getHeadDirection() {

		if (Liza.size() < 2) {
			return null;

		}

		BodySegment tailSegment = Liza.get(Liza.size() - 1);
		// The segment just ahead of the tail is the one before the last in the list.
		BodySegment segmentAheadOfTail = Liza.get(Liza.size() - 2);

		// Calculate the direction from the segment ahead of the tail

		int deltaX = tailSegment.getCell().getRow() - segmentAheadOfTail.getCell().getRow();
		int deltaY = tailSegment.getCell().getCol() - segmentAheadOfTail.getCell().getCol();
		if (deltaY > 0) {
			return Direction.RIGHT; 
		} else if (deltaY < 0) {
			return Direction.LEFT;
		} else if (deltaX > 0) {
			return Direction.DOWN; // The tail is below the segment ahead of it.
		} else if (deltaX < 0) {
			return Direction.UP; // The tail is above the segment ahead of it.
		}

		return null; // If deltaX and deltaY are both 0, or if the direction is not applicable.

	}

	public Direction getTailDirection() {

		if (Liza.size() < 2) {
			return null;

		}

	
		BodySegment headSegment = Liza.get(0);
		// The segment directly behind the head is the next one in the list.
		BodySegment segmentBehindHead = Liza.get(1);
		// Calculate the direction from the segment behind the head to the head segment.
		int X = headSegment.getCell().getRow() - segmentBehindHead.getCell().getRow();
		int Y = headSegment.getCell().getCol() - segmentBehindHead.getCell().getCol();
		if (Y > 0) {
			return Direction.RIGHT; // Next segment is to the right.

		} else if (Y < 0) {
			return Direction.LEFT; // Next segment is to the left.

		} else if (X > 0) {
			return Direction.DOWN; // Next segment is below.

		} else if (X < 0) {
			return Direction.UP; // Next segment is above.
		}

		return null;
	}

	@Override
	public String toString() {
		String result = "";
		for (BodySegment seg : getSegments()) {
			result += seg + " ";
		}
		return result;
	}
}