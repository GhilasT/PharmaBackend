import java.util.List;

public class BPlusTree {
    private int order;
    private Node root;

    public BPlusTree(int order) {
        this.order = order;
        this.root = new LeafNode(order);
    }

    public void insert(Object key, int rid) {
        root = root.insert(key, rid);
    }

    public List<Integer> search(Object key) {
        return root.search(key);
    }


}