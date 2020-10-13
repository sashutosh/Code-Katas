/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> treeSet;

    public PointSET()                               // construct an empty set of points
    {
        treeSet = new TreeSet<>(new PointsComparator());
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return treeSet.isEmpty();
    }

    public int size()                         // number of points in the set
    {
        return treeSet.size();
    }

    public void insert(
            Point2D p)              // add the point to the set (if it is not already in the set)
    {
        checkNull(p);
        treeSet.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        checkNull(p);
        return treeSet.contains(p);
    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        checkNull(rect);
        List<Point2D> rangePoints = new ArrayList<>();
        NavigableSet<Point2D> topPoints = treeSet
                .headSet(new Point2D(rect.xmax(), rect.ymax()), true);
        NavigableSet<Point2D> bottomPoints = treeSet
                .tailSet(new Point2D(rect.xmax(), rect.ymax()), true);
        for (Point2D point : topPoints
        ) {
            if (rect.contains(point)) {
                rangePoints.add(point);
            }
        }
        for (Point2D point : bottomPoints
        ) {
            if (rect.contains(point)) {
                rangePoints.add(point);
            }
        }

        return rangePoints;
    }

    private void checkNull(Object any) {
        if (any == null) {
            throw new IllegalArgumentException("Null parameter");
        }
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        checkNull(p);
        return treeSet.ceiling(p);

    }

    private static class PointsComparator implements Comparator<Point2D> {
        public int compare(Point2D p, Point2D q) {
            /*double delta = (p.x()*p.x() + p.y()*p.y()) - (q.x()*q.x() + q.y()*q.y());
            if (delta < 0) return -1;
            if (delta > 0) return +1;
            return 0;*/
            if (p.x() == q.x()) {
                if (p.y() == q.y()) {
                    return 0;
                } else {
                    if (p.y() < q.y()) {
                        return -1;
                    }
                    return 1;
                }
            } else {
                if (p.x() < q.x()) {
                    return -1;
                }
                return 1;
            }
        }
    }
}
