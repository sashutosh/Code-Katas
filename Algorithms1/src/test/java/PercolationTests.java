import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PercolationTests {

    Percolation percolation;
    int n;
    @Before
    public void init(){


    }

    @Test
    public void testGridIsEmptyOnStart() {
        n=3;
        percolation= new Percolation(n);
        assertEquals(0,percolation.numberOfOpenSites());

    }

    @Test
    public void testIsOpenIsFullOnStart() {
        n=3;
        percolation= new Percolation(n);
        assertEquals(percolation.isOpen(1,1),false);
        assertEquals(percolation.isFull(1,1),false);
    }
    @Test
    public void testIsOpenIsFullOnStartAfterOpening() {
        n=3;
        percolation= new Percolation(n);
        percolation.open(1,1);
        assertEquals(percolation.isOpen(1,1),true);
        assertEquals(percolation.isFull(1,1),true);
    }

    @Test
    public void testOpenSite() {
        n=3;
        percolation= new Percolation(n);
        percolation.open(2,2);
        percolation.open(3,3);
        assertEquals(true, percolation.isOpen(2,2));
        assertEquals(true, percolation.isOpen(3,3));
        assertEquals(2,percolation.numberOfOpenSites());
    }

    @Test
    public void testIsFull() {
        n=3;
        percolation= new Percolation(n);
        percolation.open(1,1);
        percolation.open(2,1);
        percolation.open(2,2);
        assertEquals(true,percolation.isFull(2,2));
    }

    @Test
    public void testIsFullNoPath() {
        n=3;
        percolation= new Percolation(n);
        percolation.open(1,1);
        percolation.open(2,2);
        assertEquals(false,percolation.isFull(2,2));
    }

    @Test
    public void doesNotPercolateOnStart() {
        n=3;
        percolation= new Percolation(n);
        assertEquals(false, percolation.percolates());

    }

    @Test
    public void openingDirectSitesMakePercolation() {
        n=3;
        percolation= new Percolation(n);
        percolation.open(1,2);
        percolation.open(2,2);
        percolation.open(3,2);
        assertEquals(true,percolation.percolates());

    }

    @Test
    public void openingSelectiveSitesMakePercolation() {
        n=3;
        percolation= new Percolation(n);
        percolation.open(1,1);
        percolation.open(2,1);
        percolation.open(2,2);
        percolation.open(2,3);
        percolation.open(3,3);
        assertEquals(true,percolation.percolates());
        assertEquals(5,percolation.numberOfOpenSites());

    }
}
