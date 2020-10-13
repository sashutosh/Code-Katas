import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PointTest {


    @Test
    public void testSlopeToForHorizontal() {
        Point thisP= new Point(3,4);
        Point thatP= new Point(8,4);
        assertEquals(0.0,thisP.slopeTo(thatP), 0.00001);
    }

    @Test
    public void testSlopeToForVertical() {

        Point thisP= new Point(8,14);
        Point thatP= new Point(8,4);
        assertEquals(Double.POSITIVE_INFINITY,thisP.slopeTo(thatP), 0.00001);
    }

    @Test
    public void testSlopeToForEqual() {

        Point thisP= new Point(8,14);
        Point thatP= new Point(8,14);
        assertEquals(Double.NEGATIVE_INFINITY,thisP.slopeTo(thatP), 0.00001);
    }

    @Test
    public void testSlope() {

        Point thisP= new Point(3,3);
        Point thatP= new Point(11,14);
        double expectedSlope = ((double)(14-3)/(11-3));
        assertEquals(expectedSlope,thisP.slopeTo(thatP), 0.00001);
    }


    @Test
    public void testCompareForEqual() {
        Point thisP= new Point(3,4);
        Point thatP= new Point(3,4);
        assertEquals(0,thisP.compareTo(thatP));
    }

    @Test
    public void testCompareForYGreater() {

        Point thisP= new Point(9,14);
        Point thatP= new Point(8,4);
        assertEquals(1,thisP.compareTo(thatP));
        assertEquals(-1,thatP.compareTo(thisP));
    }

    @Test
    public void testCompareYEqualXGreater() {

        Point thisP= new Point(8,14);
        Point thatP= new Point(8,7);
        assertEquals(1,thisP.compareTo(thatP));
        assertEquals(-1,thatP.compareTo(thisP));
    }

    @Test
    public void comparatorTest() {
        Point origin = new Point(0,0);
        Point p1 = new Point(10,10);
        Point p2 = new Point(5,4);
        assertEquals(1,origin.slopeOrder().compare(p1,p2));
        assertEquals(-1,origin.slopeOrder().compare(p2,p1));
        assertEquals(0,origin.slopeOrder().compare(p1,p1));
    }


}
