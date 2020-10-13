import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class KdTreeTest {

    void insertToKdTree(KdTree tree, String fileName) {
        String filename = fileName;
        In in = new In(filename);

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            tree.insert(p);
        }
    }

    @Test
    public void testInsertDuplicate() {

        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(1.0, 1.0));
        kdTree.insert(new Point2D(1.0, 1.0));
        assertEquals(1, kdTree.size());
    }

    @Test
    public void testInsert() {
        KdTree kdTree = new KdTree();
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(5, 5));
        points.add(new Point2D(6, 3));
        points.add(new Point2D(4, 2));
        points.add(new Point2D(7, 8));
        points.add(new Point2D(7, 1));
        points.add(new Point2D(3, 8));
        points.add(new Point2D(2, 1));
        for (Point2D p : points
        ) {
            kdTree.insert(p);
        }
        assertEquals(points.size(), kdTree.size());
    }

    @Test
    public void testContains() {
        KdTree kdTree = new KdTree();
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(5, 5));
        points.add(new Point2D(6, 3));
        points.add(new Point2D(4, 2));
        points.add(new Point2D(7, 8));
        points.add(new Point2D(7, 1));
        points.add(new Point2D(3, 8));
        points.add(new Point2D(2, 1));

        for (Point2D p : points
        ) {
            kdTree.insert(p);
        }
        for (Point2D p : points
        ) {
            assertEquals(true, kdTree.contains(p));
        }

    }

    @Test
    public void testContainsDegenerate() {
        KdTree tree = new KdTree();
        insertToKdTree(tree, "D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\week5\\kdtree\\input10.txt");
        //Iterable<Point2D> points = tree.range(new RectHV(0,0,0.5,0.5));
        assertTrue(tree.contains(new Point2D(0.417, 0.362)));
    }

    @Test
    public void testRectangle() {
        KdTree tree = new KdTree();
        insertToKdTree(tree, "D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\week5\\kdtree\\input10.txt");
        Iterable<Point2D> points = tree.range(new RectHV(0, 0, 0.5, 0.5));
        assertNotNull(points);
    }

    @Test
    public void testNearest() {
        KdTree tree = new KdTree();
        insertToKdTree(tree, "D:\\CodeKata\\Classic_CS_Problems\\Algorithms\\week5\\kdtree\\input10.txt");
        Point2D nearest = tree.nearest(new Point2D(0.8, 0.8));
        assertNotNull(nearest);
    }

    @Test
    public void testNearest1() {
        KdTree tree = new KdTree();
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(0.7, 0.2));
        points.add(new Point2D(0.5, 0.4));
        points.add(new Point2D(0.2, 0.3));
        points.add(new Point2D(0.4, 0.7));
        points.add(new Point2D(0.9, 0.6));
        for (Point2D p : points
        ) {
            tree.insert(p);
        }
        Point2D nearest = tree.nearest(new Point2D(0.527, 0.619));
        assertTrue(nearest.equals(new Point2D(0.4, 0.7)));
    }

    @Test
    public void testNearest2() {
        KdTree tree = new KdTree();
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(0.7, 0.2));
        points.add(new Point2D(0.5, 0.4));
        points.add(new Point2D(0.2, 0.3));
        points.add(new Point2D(0.4, 0.7));
        points.add(new Point2D(0.9, 0.6));
        for (Point2D p : points
        ) {
            tree.insert(p);
        }
        Point2D nearest = tree.nearest(new Point2D(0.234, 0.51));
        assertTrue(nearest.equals(new Point2D(0.2, 0.3)));
    }

    @Test
    public void testNearest3() {
        KdTree tree = new KdTree();
        /*A  0.372 0.497
        B  0.564 0.413
        C  0.226 0.577
        D  0.144 0.179
        E  0.083 0.51
        F  0.32 0.708
        G  0.417 0.362
        H  0.862 0.825
        I  0.785 0.725
        J  0.499 0.208*/
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(0.372, 0.497));
        points.add(new Point2D(0.564, 0.413));
        points.add(new Point2D(0.226, 0.577));
        points.add(new Point2D(0.144, 0.179));
        points.add(new Point2D(0.083, 0.51));
        points.add(new Point2D(0.32, 0.708));
        points.add(new Point2D(0.417, 0.362));
        points.add(new Point2D(0.862, 0.825));
        points.add(new Point2D(0.785, 0.725));
        points.add(new Point2D(0.499, 0.208));
        for (Point2D p : points
        ) {
            tree.insert(p);
        }
        Point2D nearest = tree.nearest(new Point2D(0.0, 0.845));
        assertTrue(nearest.equals(new Point2D(0.083, 0.51)));
    }

    public void testNearest4() {
        KdTree tree = new KdTree();
        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(0.372, 0.497));
        points.add(new Point2D(0.564, 0.413));
        points.add(new Point2D(0.226, 0.577));
        points.add(new Point2D(0.144, 0.179));
        points.add(new Point2D(0.083, 0.51));
        points.add(new Point2D(0.32, 0.708));
        points.add(new Point2D(0.417, 0.362));
        points.add(new Point2D(0.862, 0.825));
        points.add(new Point2D(0.785, 0.725));
        points.add(new Point2D(0.499, 0.208));
        for (Point2D p : points
        ) {
            tree.insert(p);
        }
        Point2D nearest = tree.nearest(new Point2D(0.75, 0.625));
        assertTrue(nearest.equals(new Point2D(0.5625, 0.5)));
    }


    @Test
    public void testNearestOnEmptyTree() {
        KdTree tree = new KdTree();
        Point2D nearest = tree.nearest(new Point2D(0.8, 0.8));
        assertNull(nearest);
    }

    @Test
    public void testRangeOnEmptyTree() {
        KdTree tree = new KdTree();
        Iterable<Point2D> nearest = tree.range(new RectHV(0.8, 0.8, 1.0, 1.0));
        //assertEquals(null,nearest.iterator().next());
    }


}
