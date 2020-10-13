import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private final TreeNode<Point2D> treeHead = new TreeNode<>();
    private int nodeCounts = 0;

    public boolean isEmpty()                      // is the set empty?
    {
        return nodeCounts == 0;
    }

    public int size()                         // number of points in the set
    {
        return nodeCounts;
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        checkNull(p);
        if (treeHead.value == null) {
            treeHead.value = p;
            treeHead.horizontal = true;
            nodeCounts++;
        } else {
            insertToNode(treeHead, p);
        }


    }

    private void insertToNode(TreeNode<Point2D> current, Point2D p) {
        if (!current.value.equals(p)) {
            if (current.horizontal) {
                addChildNode(current, p, p.x() < current.value.x(), false);
            } else {
                addChildNode(current, p, p.y() < current.value.y(), true);
            }

        }
    }

    private void addChildNode(TreeNode<Point2D> current, Point2D p, boolean isLess, boolean isHorizontal) {
        if (isLess) {
            if (current.left == null) {
                addLeftNode(current, initNode(p, isHorizontal));
                nodeCounts++;
            } else {
                insertToNode(current.left, p);
            }
        } else {
            if (current.right == null) {
                addRightNode(current, initNode(p, isHorizontal));
                nodeCounts++;
            } else {
                insertToNode(current.right, p);
            }
        }
    }

    private void addRightNode(TreeNode<Point2D> current, TreeNode<Point2D> newNode) {
        current.right = newNode;
    }

    private void addLeftNode(TreeNode<Point2D> current, TreeNode<Point2D> newNode) {
        current.left = newNode;
    }

    private TreeNode<Point2D> initNode(Point2D p, boolean b) {
        TreeNode<Point2D> treeNode = new TreeNode<>();
        treeNode.value = p;
        treeNode.horizontal = b;
        return treeNode;
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        checkNull(p);
        if (isEmpty()) {
            return false;
        }
        TreeNode<Point2D> current = treeHead;
        while (current != null) {
            if (current.value.equals(p)) {
                return true;
            }
            if (shouldTraverseLeft(current, p)) {
                current = current.left;
            } else {
                current = current.right;
            }

        }
        return false;
    }

    private boolean shouldTraverseLeft(TreeNode<Point2D> current, Point2D p) {
        if (current.horizontal) {
            return (p.x() < current.value.x());
        } else {
            return (p.y() < current.value.y());
        }
    }

    public void draw()                         // draw all points to standard draw
    {

    }

    public Iterable<Point2D> range(
            RectHV rect)             // all points that are inside the rectangle (or on the boundary)
    {
        List<Point2D> points = new ArrayList<>();
        checkNull(rect);
        if (size() > 0) {
            searchPoints(treeHead, rect, new RectHV(0, 0, 1, 1), points);
        }
        return points;
    }

    private void searchPoints(TreeNode<Point2D> current, RectHV searchRect, RectHV parentRect, List<Point2D> points) {
        if (searchRect.contains(current.value)) {
            if (!points.contains(current.value)) {
                points.add(current.value);
            }

        }
        if (!current.horizontal) {
            RectHV bottomRectHV = new RectHV(parentRect.xmin(), parentRect.ymin(), parentRect.xmax(), current.value.y());
            RectHV topRectHV = new RectHV(parentRect.xmin(), current.value.y(), parentRect.xmax(), parentRect.ymax());
            if (searchRect.intersects(bottomRectHV)) {
                if (current.left != null) {
                    searchPoints(current.left, searchRect, bottomRectHV, points);
                }
            }
            if (searchRect.intersects(topRectHV)) {
                if (current.right != null) {
                    searchPoints(current.right, searchRect, topRectHV, points);
                }

            }
        } else {
            RectHV leftRectHV = new RectHV(parentRect.xmin(), parentRect.ymin(), current.value.x(), parentRect.ymax());
            RectHV rightRectHV = new RectHV(current.value.x(), parentRect.ymin(), parentRect.xmax(), parentRect.ymax());
            if (searchRect.intersects(leftRectHV)) {
                if (current.left != null) {
                    searchPoints(current.left, searchRect, leftRectHV, points);
                }
            }
            if (searchRect.intersects(rightRectHV)) {
                if (current.right != null) {
                    searchPoints(current.right, searchRect, rightRectHV, points);
                }
            }
        }
    }

    private void checkNull(Object any) {
        if (any == null) {
            throw new IllegalArgumentException("Null parameter");
        }
    }

    public Point2D nearest(
            Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (size() == 0) {
            return null;
        }
        double distance = treeHead.value.distanceSquaredTo(p);
        List<Pair<Point2D, Double>> nearest = new ArrayList<>();
        nearest.add(new Pair<>(treeHead.value, distance));
        RectHV parentRect = new RectHV(0, 0, 1, 1);
        //searchNearest(treeHead, p, parentRect, 1.0, nearest);
        TreeNode<Point2D> currentNode = treeHead;
        RectHV nodeRectLeft = new RectHV(parentRect.xmin(), parentRect.ymin(), currentNode.value.x(), parentRect.ymax());
        RectHV nodeRectRight = new RectHV(currentNode.value.x(), parentRect.ymin(), parentRect.xmax(), parentRect.ymax());
        searchNearest(treeHead.left, p, nodeRectLeft, nearest);
        searchNearest(treeHead.right, p, nodeRectRight, nearest);
        return nearest.get((nearest.size() - 1)).getKey();


    }


    private double getMinDistance(List<Pair<Point2D, Double>> nearest, double distance) {
        double min = distance;
        if (nearest.get(nearest.size() - 1).getValue() < distance) {
            min = nearest.get(nearest.size() - 1).getValue();
        }
        return min;
    }

    private void searchNearest(TreeNode<Point2D> currentNode, Point2D point, RectHV parentRect, List<Pair<Point2D, Double>> nearest) {
        if (currentNode != null && currentNode.value != null) {
            double distance = nearest.get(nearest.size() - 1).getValue();
            double rectDist = parentRect.distanceSquaredTo(point);
            if (rectDist < distance) {
                double currentDist = currentNode.value.distanceSquaredTo(point);
                if (currentDist < distance) {
                    nearest.add(new Pair<>(currentNode.value, currentDist));
                }
                if (currentNode.horizontal) {
                    RectHV nodeRectLeft = new RectHV(parentRect.xmin(), parentRect.ymin(), currentNode.value.x(), parentRect.ymax());
                    searchNearest(currentNode.left, point, nodeRectLeft, nearest);
                    RectHV nodeRectRight = new RectHV(currentNode.value.x(), parentRect.ymin(), parentRect.xmax(), parentRect.ymax());
                    searchNearest(currentNode.right, point, nodeRectRight, nearest);
                } else {
                    RectHV bottomRectHV = new RectHV(parentRect.xmin(), parentRect.ymin(), parentRect.xmax(), currentNode.value.y());
                    searchNearest(currentNode.left, point, bottomRectHV, nearest);
                    RectHV topRectHV = new RectHV(parentRect.xmin(), currentNode.value.y(), parentRect.xmax(), parentRect.ymax());
                    searchNearest(currentNode.right, point, topRectHV, nearest);
                }
            }

        }

    }

    private static class TreeNode<E> {
        TreeNode<E> left;
        TreeNode<E> right;
        E value;
        boolean horizontal;
    }

    private static class Pair<K, V> {
        K key;
        V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}
