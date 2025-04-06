import java.util.ArrayList;
import java.util.List;

public abstract class Node {
    protected int order;
    protected boolean isLeaf;
    protected List<Object> keys;

    public Node(int order, boolean isLeaf) {
        this.order = order;
        this.isLeaf = isLeaf;
        this.keys = new ArrayList<>();
    }

    public abstract Node insert(Object key, int rid);

    public abstract List<Integer> search(Object key);
}