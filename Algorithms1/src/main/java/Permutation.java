import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {

        if (args == null || args.length != 1) {
            throw new IllegalArgumentException("Usage - Input count of the strings to permute");
        }
        int count = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<>();

        for (int i = 0; i < count; i++) {
            String item = StdIn.readString();
            rq.enqueue(item);
        }

        for (String current :
                rq) {
            StdOut.println(current);
        }
    }
}
