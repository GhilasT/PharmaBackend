import java.util.ArrayList;
import java.util.List;

public class InternalNode extends Node {
    protected List<Node> children;

    public InternalNode(int order) {
        super(order, false);
        this.children = new ArrayList<>();
    }

    @Override
    public Node insert(Object key, int rid) {
        // Trouver le bon enfant pour insérer la clé
        int index = 0;
        while (index < keys.size() && ((Comparable<Object>) keys.get(index)).compareTo(key) < 0) {
            index++;
        }

        Node child = children.get(index);
        Node newChild = child.insert(key, rid);

        if (newChild != child) {
            // Split du nœud enfant, ajuster les clés et les pointeurs
            Object newKey = newChild.keys.get(0);
            keys.add(index, newKey);
            children.add(index + 1, newChild);

            if (keys.size() > order * 2) {
                return split();
            }
        }
        return this;
    }

    @Override
    public List<Integer> search(Object key) {
        // Trouver le bon enfant pour continuer la recherche
        int index = 0;
        while (index < keys.size() && ((Comparable<Object>) keys.get(index)).compareTo(key) < 0) {
            index++;
        }
        return children.get(index).search(key);
    }

    private Node split() {
        int midIndex = keys.size() / 2;

        // Créer un nouveau nœud interne
        InternalNode newParent = new InternalNode(order);
        newParent.keys.addAll(keys.subList(midIndex + 1, keys.size()));
        newParent.children.addAll(children.subList(midIndex + 1, children.size()));

        // Ajuster les clés et les enfants actuels
        keys = keys.subList(0, midIndex);
        children = children.subList(0, midIndex + 1);

        // Retourner un nouveau parent
        InternalNode parent = new InternalNode(order);
        parent.keys.add(keys.get(midIndex));
        parent.children.add(this);
        parent.children.add(newParent);

        return parent;
    }
}