import java.util.ArrayList;
import java.util.List;

public class LeafNode extends Node {
    private List<List<Integer>> rids;
    private LeafNode next;

    public LeafNode(int order) {
        super(order, true);
        this.rids = new ArrayList<>();
        this.next = null;
    }

    @Override
    public Node insert(Object key, int rid) {
        int index = 0;

        // Trouver l'emplacement d'insertion
        while (index < keys.size() && ((Comparable<Object>) keys.get(index)).compareTo(key) < 0) {
            index++;
        }

        if (index < keys.size() && keys.get(index).equals(key)) {
            // Clé existe déjà, ajouter le rid
            rids.get(index).add(rid);
        } else {
            // Ajouter une nouvelle clé
            keys.add(index, key);
            List<Integer> ridList = new ArrayList<>();
            ridList.add(rid);
            rids.add(index, ridList);
        }

        // Si dépassement de capacité, diviser le nœud
        if (keys.size() > order * 2) {
            return split();
        }
        return this;
    }

    @Override
    public List<Integer> search(Object key) {
        int index = keys.indexOf(key);
        if (index != -1) {
            return rids.get(index);
        }
        return new ArrayList<>();
    }

    private Node split() {
        int midIndex = keys.size() / 2;

        // Créer un nouveau nœud
        LeafNode newLeaf = new LeafNode(order);
        newLeaf.keys.addAll(keys.subList(midIndex, keys.size()));
        newLeaf.rids.addAll(rids.subList(midIndex, rids.size()));

        // Ajuster les références
        keys = keys.subList(0, midIndex);
        rids = rids.subList(0, midIndex);
        newLeaf.next = this.next;
        this.next = newLeaf;

        // Créer un nouveau nœud interne
        InternalNode newParent = new InternalNode(order);
        newParent.keys.add(newLeaf.keys.get(0));
        newParent.children.add(this);
        newParent.children.add(newLeaf);

        return newParent;
    }
}