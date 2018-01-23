package structure;

import java.util.ArrayList;


public class DFA<ItemSet> extends ArrayList<ItemSet> {

    @Override
    public int indexOf(Object o) {
        if (!(o instanceof structure.ItemSet)) return super.indexOf(o);
        int index = -1;
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).equals(o)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public int addFromIndex(ItemSet itemSet) {
        int index = indexOf(itemSet);
        if (index < 0) {
            super.add(itemSet);
            return this.size() - 1;
        } else {
            return index;
        }
    }
}