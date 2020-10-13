import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class PointSETTest {

    void insertToPointSet(PointSET ps, String fileName) {
        String filename = fileName;
        In in = new In(filename);

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            ps.insert(p);
        }
    }

    @Test
    public void testPointSetCreation() {
        PointSET ps = new PointSET();
        insertToPointSet(ps, "D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\week5\\kdtree\\input10.txt");
        assertEquals(10, ps.size());
    }

    @Test
    public void testRectangle() {
        PointSET ps = new PointSET();
        insertToPointSet(ps, "D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\week5\\kdtree\\input10.txt");
        Iterable<Point2D> points = ps.range(new RectHV(0,0,0.5,0.5));
        assertNotNull(points);
    }

    @Test
    public void testRectangleMiddle() {
        PointSET ps = new PointSET();
        insertToPointSet(ps, "D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\week5\\kdtree\\input10.txt");
        Iterable<Point2D> points = ps.range(new RectHV(0.1,0.1,0.5,0.5));
        assertNotNull(points);
    }

    @Test
    public void testNearest(){
        PointSET ps = new PointSET();
        insertToPointSet(ps, "D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\week5\\kdtree\\input10.txt");
        Point2D points = ps.nearest(new Point2D(0.8,0.8));
        assertNotNull(points);

    }

    @Test
    public void testNearest1(){
        /*1.0 0.5
        B  0.5 0.25
        C  0.5 0.75
        D  0.25 1.0
        E  0.0 0.75
        F  0.0 1.0
        G  0.25 0.0
        H  0.5 0.75
        I  0.75 0.5
        J  0.75 1.0*/
        PointSET ps = new PointSET();
        ps.insert(new Point2D(0.5, 0.25));
        ps.insert(new Point2D(0.5, 0.75));
        ps.insert(new Point2D(0.25,1.0));
        ps.insert(new Point2D(0.0,0.75));
        ps.insert(new Point2D(0.0,1.0));
        ps.insert(new Point2D(0.25,0.0));
        ps.insert(new Point2D(0.5,0.75));
        ps.insert(new Point2D(0.75,0.5));
        ps.insert(new Point2D(0.75,1.0));

        Point2D point = ps.nearest(new Point2D(0.5, 0.5));
        assertEquals(new Point2D(0.5,0.25), point);
    }

    @Test
    public void testSize(){
        PointSET ps = new PointSET();
        /*A  0.0 1.0
        B  0.0 1.0
        C  1.0 1.0
        D  1.0 0.0*/
        ps.insert(new Point2D(0,1.0));
        ps.insert(new Point2D(0,1.0));
        ps.insert(new Point2D(1.0,1.0));
        ps.insert(new Point2D(1.0,0.0));

        assertEquals(3,ps.size());
    }

    @Test
    public void testContains(){
        PointSET ps = new PointSET();
/*
        A  0.0 0.5
        B  0.75 0.75
        C  1.0 1.0
        D  1.0 0.0
        E  0.5 0.75
        F  0.0 0.75
        G  0.75 0.25
        H  1.0 0.0
        I  0.5 0.0
        J  0.25 0.0
*/

        ps.insert(new Point2D(0.0, 0.5));
        ps.insert(new Point2D(0.75, 0.75));
        ps.insert(new Point2D(1.0,1.0));
        ps.insert(new Point2D(1.0,0.0));
        ps.insert(new Point2D(0.5,0.75));
        ps.insert(new Point2D(0.0,0.75));
        ps.insert(new Point2D(0.75,0.25));
        ps.insert(new Point2D(1.0,0.0));
        ps.insert(new Point2D(0.5,0.0));
        ps.insert(new Point2D(0.25,0.0));

        assertEquals(false,ps.contains(new Point2D(0.75,0.5)));
    }

}
