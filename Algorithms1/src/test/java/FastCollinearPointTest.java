import edu.princeton.cs.algs4.In;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FastCollinearPointTest {
    @Test(expected = IllegalArgumentException.class)
    public void testInitialization(){
        BruteCollinearPoints bc = new BruteCollinearPoints(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializationNullPoints() {
        Point[] points = {new Point(3,4),null, new Point(5,6) };
        BruteCollinearPoints bc = new BruteCollinearPoints(points);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializationDuplicatePoints() {
        Point[] points = {new Point(3,4), new Point(3,4) };
        BruteCollinearPoints bc = new BruteCollinearPoints(points);
    }

    @Test
    public void testSingleSegment(){
        Point[] points= {new Point(9,5),new Point(3,3), new Point(5,5), new Point(7,7), new Point(9,9),new Point(11,13)};
        FastCollinearPoints fc = new FastCollinearPoints(points);
        assertEquals(1,fc.numberOfSegments());
        //assertEquals(1,fc.segments().length);
    }

    @Test
    public void testInput6(){
        Point[] points= {
                new Point(19000,10000),
                new Point(18000,10000),
                new Point(32000,10000),
                new Point(21000,10000),
                new Point(1234,5678),
                new Point(2345,9987)};
        FastCollinearPoints bc = new FastCollinearPoints(points);
        assertEquals(1,bc.numberOfSegments());
        assertEquals(1,bc.segments().length);
    }

    @Test
    public void testInput8(){
        Point[] points= {
                new Point(10000,0),
                new Point(0,10000),
                new Point(3000,7000),
                new Point(7000,3000),
                new Point(20000,21000),
                new Point(3000,4000),
                new Point(14000,15000),
                new Point(6000,7000),
        };
        FastCollinearPoints bc = new FastCollinearPoints(points);
        assertEquals(2,bc.numberOfSegments());
        assertEquals(2,bc.segments().length);
    }

    @Test
    public void testEquidistant() throws FileNotFoundException {
        Point[] points = readPointsFile("D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\weeks3\\collinear\\equidistant.txt");
        FastCollinearPoints fc = new FastCollinearPoints(points);
        assertEquals(4, fc.numberOfSegments());
    }

    @Test
    public void testInput48() throws FileNotFoundException {
        Point[] points = readPointsFile("D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\weeks3\\collinear\\input48.txt");
        FastCollinearPoints fc = new FastCollinearPoints(points);
        assertEquals(6, fc.numberOfSegments());
    }

    @Test
    public void horizontal25() throws FileNotFoundException {
        Point[] points = readPointsFile("D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\weeks3\\collinear\\horizontal25.txt");
        FastCollinearPoints fc = new FastCollinearPoints(points);
        assertEquals(25, fc.numberOfSegments());
    }


    private Point[] readPointsFile(String path) throws FileNotFoundException {
        In in = new In(path);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        return points;
    }

    private Point parsePoint(String line) {
        String [] values = line.split(" ");
        if(values.length==2) {
            Point point = new Point(Integer.parseInt(values[0]),
                    Integer.parseInt(values[1]));

            return point;
        }
        return null;
    }
}
