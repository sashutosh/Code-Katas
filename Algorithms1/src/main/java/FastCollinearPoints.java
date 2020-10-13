import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final int arraySize = 4;
    private final List<List<Point>> linePoints;
    private int segments;
    private LineSegment[] lineSegments = new LineSegment[arraySize];

    public FastCollinearPoints(Point[] points) {     // finds all line segments containing 4 or more points
        if (points == null) {
            throw new IllegalArgumentException("Null argument");
        }

        checkNullPoint(points);
        checkDuplicates(points);
        linePoints = new ArrayList<>();
        findLineSegment(points);
    }

    private void findLineSegment(Point[] points) {
        for (int i=0;i<points.length;i++) {
            Point[] tempPoint = Arrays.copyOf(points,points.length);
            Arrays.sort(tempPoint, points[i].slopeOrder());
            getLineSegmentPoints(tempPoint);

        }

    }

    private Point[] getSortedPointsArray(List<Point> pointOnLineList) {
        Point[] pointsOnLine = new Point[pointOnLineList.size()];
        int i = 0;
        for (Point point : pointOnLineList) {
            pointsOnLine[i++] = point;
        }
        Arrays.sort(pointsOnLine);
        return pointsOnLine;
    }

    private void getLineSegmentPoints(Point[] points) {
        List<Point> pointsOnLine = new ArrayList<>();
        for (int i = 1; i < points.length - 1; i++) {
            if (isCollinear(points, i)) {
                if (pointsOnLine.isEmpty()) {
                    pointsOnLine.add(points[0]);
                    pointsOnLine.add(points[i]);
                    pointsOnLine.add(points[i + 1]);
                } else {
                    pointsOnLine.add(points[i + 1]);
                }

            }
            else
            {
                addLine(pointsOnLine);
            }
        }
        addLine(pointsOnLine);
    }

    private boolean isCollinear(Point[] points, int i) {
        return points[0].slopeTo(points[i]) == points[0].slopeTo(points[i + 1]);
    }

    private void addLine(List<Point> pointsOnLine) {
        if (!pointsOnLine.isEmpty() && pointsOnLine.size() > 3) {
            Point[] pointsOnLineArraySorted = getSortedPointsArray(pointsOnLine);
            if (!isAlreadyPresent(pointsOnLineArraySorted)) {
                LineSegment ls = new LineSegment(pointsOnLineArraySorted[0], pointsOnLineArraySorted[pointsOnLine.size() - 1]);
                linePoints.add(new ArrayList<>(Arrays.asList(pointsOnLineArraySorted)));
                addToLineSegmentArray(ls);
            }

        }
        pointsOnLine.clear();
    }

    private boolean isAlreadyPresent(Point[] pointsOnLine) {
        boolean match = false;
        for (List<Point> linePoint : linePoints
        ) {
            Object[] tempLinePoint = linePoint.toArray();
            Arrays.sort(tempLinePoint);

            for (int i = 0; i < tempLinePoint.length-1; i++) {
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
        if (ls != null) {
            if (segments == lineSegments.length - 1) {
                doubleArrayLength();
            }
            lineSegments[segments++] = ls;
        }
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

    public int numberOfSegments() {        // the number of line segments
        return segments;
    }

    public LineSegment[] segments() {               // the line segments
        int i = 0;
        LineSegment[] ls = new LineSegment[segments];
        for (LineSegment lineSegment : lineSegments) {
            if (lineSegment != null) {
                ls[i++] = lineSegment;
            }
        }
        return ls;
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