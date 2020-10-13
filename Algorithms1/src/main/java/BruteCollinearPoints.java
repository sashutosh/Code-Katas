import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteCollinearPoints {

    private final int arraySize = 4;
    private final Point[] pointsOnLine;
    private final List<List<Point>> linePoints;
    private double[][] slopes;
    private LineSegment[] lineSegments = new LineSegment[arraySize];
    private int segments = 0;

    public BruteCollinearPoints(Point[] points) {   // finds all line segments containing 4 points
        if (points == null) {
            throw new IllegalArgumentException("Null argument");
        }
        pointsOnLine = new Point[4];
        linePoints = new ArrayList<>();
        checkNullPoint(points);
        checkDuplicates(points);
        Arrays.sort(points);
        findSlopes(points);
        findLineSegments(points);

    }

    public int numberOfSegments()        // the number of line segments
    {
        return segments;
    }

    public LineSegment[] segments() {
        // return lineSegments
        int i = 0;
        LineSegment[] ls = new LineSegment[segments];
        for (LineSegment lineSegment : lineSegments) {
            if (lineSegment != null) {
                ls[i++] = lineSegment;
            }
        }
        return ls;
    }

    private void findSlopes(Point[] points) {
        slopes = new double[points.length][points.length];
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (i != j) {
                    slopes[i][j] = points[i].slopeTo(points[j]);

                }
            }
        }
    }

    private void findLineSegments(Point[] points) {
        int count;
        int index = 0;
        // For each point
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) { //Comparison point in a row
                count = 0;
                pointsOnLine[0] = points[i];
                for (int k = 0; k < points.length; k++) { // iterate over whole row
                    if (slopes[i][j] == slopes[i][k] && !(i == k)) {
                        count++;
                        pointsOnLine[count] = points[k];
                        index = k;
                    }
                }
                if (count >= 3) {
                    if (!alreadyPresent(pointsOnLine)) {
                        LineSegment ls = new LineSegment(points[i], points[index]);
                        linePoints.add(new ArrayList<>(Arrays.asList(pointsOnLine)));
                        addToLineSegmentArray(ls);
                        count = 0;
                    }

                }
                clearPointArray(pointsOnLine);

            }
        }
    }

    private void clearPointArray(Point[] points) {
        if (points != null) {
            Arrays.fill(points, null);
        }
    }

    private boolean alreadyPresent(Point[] pointsOnLine) {
        boolean match = false;
        Arrays.sort(pointsOnLine);
        for (List<Point> linePoint : linePoints
        ) {
            Object[] tempLinePoint = linePoint.toArray();
            Arrays.sort(tempLinePoint);

            for (int i = 0; i < tempLinePoint.length; i++) {
                if (((Point) tempLinePoint[i]).compareTo(pointsOnLine[i]) != 0) {
                    match = false;
                    break;
                }
                match = true;
            }
            if (match) {
                return true;
            }
        }
        return false;
    }

    private void addToLineSegmentArray(LineSegment ls) {
        if (segments == lineSegments.length - 1) {
            doubleArrayLength();
        }
        lineSegments[segments++] = ls;
    }

    private void doubleArrayLength() {
        int size = lineSegments.length * 2;
        int i = 0;
        LineSegment[] temp = new LineSegment[size];
        for (LineSegment ls :
                lineSegments) {
            temp[i++] = ls;
        }
        lineSegments = temp;
    }

    private void checkDuplicates(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Duplicate points");
                }
            }

        }
    }

    private void checkNullPoint(Point[] points) {
        for (Point point : points
        ) {
            if (point == null) {
                throw new IllegalArgumentException("Null point");
            }
        }
    }
}
