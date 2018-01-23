package structure;

import java.util.HashSet;
import java.util.Set;

public class Item {

    private Set<Integer> lookaheads;
    private int dotPos;
    private Production production;
    
    public Item(Set<Integer> lookaheads, int dotPos, Production production) {
        this( dotPos, production);
        this.lookaheads = lookaheads;
    }
    
    public Item(int dotPos, Production production) {
        this.dotPos = dotPos;
        this.production = production;
        lookaheads = new HashSet<>();
    }

    public void addPredictor(Integer predictor){
        lookaheads.add(predictor);
    }

    public void addPredictors(Set<Integer> predictors){
        this.lookaheads.addAll(predictors);
    }

    public Set<Integer> getLookaheads() {
        return lookaheads;
    }

    public int getProductionNum() {
        return production.getId();
    }

    public Production getProduction() {
        return production;
    }

    public int getPosition() {
        return dotPos;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Item){
            Item item = (Item)obj;
            boolean isEquals = (this.lookaheads.equals(item.lookaheads)
                    && (this.getProductionNum() == item.getProductionNum())
                    && (this.dotPos == item.dotPos) );
            return isEquals;
        }else {
            return super.equals(obj);
        }
    }

    public boolean equalsCore(Item item){
        boolean hasEqualCore = (this.dotPos == item.dotPos)
                && (this.getProductionNum() == item.getProductionNum());
        return hasEqualCore;
    }

    private boolean flag = true;
    public void print(){
        System.out.print(production.getName()+":");
        for (int i = 0; i < production.getSigns().size(); i++) {
            if (i == dotPos) {
                System.out.print(".");
            }
            System.out.print(production.getSigns().get(i)+" ");
        }

        lookaheads.iterator().forEachRemaining(i -> {
            if(flag){
                System.out.print(","+i);
                flag = false;
            }else {
                System.out.print("/"+i);
            }
        });
        System.out.println();
    }
}