import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BruteCollinearPointTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInitialization() {
        BruteCollinearPoints bc = new BruteCollinearPoints(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializationNullPoints() {
        Point[] points = {new Point(3, 4), null, new Point(5, 6)};
        BruteCollinearPoints bc = new BruteCollinearPoints(points);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializationDuplicatePoints() {
        Point[] points = {new Point(3, 4), new Point(3, 4)};
        BruteCollinearPoints bc = new BruteCollinearPoints(points);
    }

    @Test
    public void testSingleSegment() {
        Point[] points = {new Point(3, 3), new Point(5, 5), new Point(7, 7), new Point(9, 9), new Point(11, 13)};
        BruteCollinearPoints bc = new BruteCollinearPoints(points);
        assertEquals(1, bc.numberOfSegments());
        //assertEquals(1,bc.segments().length);
    }

    @Test
    public void testNonCollinear() {
        Point[] points = {new Point(3, 3), new Point(5, 5), new Point(7, 7), new Point(9, 8)};
        BruteCollinearPoints bc = new BruteCollinearPoints(points);
        assertEquals(0, bc.numberOfSegments());
        //assertEquals(1,bc.segments().length);
    }

    @Test
    public void testSingleSegmentWithRandomPoints() {
        Point[] points = {new Point(3, 3), new Point(3, 4), new Point(5, 5), new Point(7, 3), new Point(7, 7), new Point(19, 8), new Point(9, 9)};
        BruteCollinearPoints bc = new BruteCollinearPoints(points);
        assertEquals(1, bc.numberOfSegments());
        //assertEquals(1,bc.segments().length);
    }

    @Test
    public void testInput6() {
        Point[] points = {
                new Point(19000, 10000),
                new Point(18000, 10000),
                new Point(32000, 10000),
                new Point(21000, 10000),
                new Point(1234, 5678),
                new Point(2345, 9987)};
        BruteCollinearPoints bc = new BruteCollinearPoints(points);
        assertEquals(1, bc.numberOfSegments());
    }

    @Test
    public void testInput8() {
        Point[] points = {
                new Point(10000, 0),
                new Point(0, 10000),
                new Point(3000, 7000),
                new Point(7000, 3000),
                new Point(20000, 21000),
                new Point(3000, 4000),
                new Point(14000, 15000),
                new Point(6000, 7000),
        };
        BruteCollinearPoints bc = new BruteCollinearPoints(points);
        assertEquals(2, bc.numberOfSegments());
        assertEquals(2, bc.segments().length);
    }

}
