package parser;

import structure.*;
import exception.GRAException;
import java.util.*;

public class RE2DFA {

    private Map<Integer, List<Production>> productions;
    private DFA<ItemSet> itemSets;
    private PPT ppt;
    private int[] thisAction;
    private int[] thisGoto;

    public RE2DFA(Map<Integer, List<Production>> productions){
        this.productions = productions;
        this.itemSets = new DFA<>();
        this.ppt = new PPT();
    }

    public PPT createPPT() throws GRAException{
        ItemSet firstSet = new ItemSet();
        Set<Integer> firstPre = new HashSet<>();
        firstPre.add(0);
        Item firstItem = new Item(firstPre, 0, SParser.productionList.get(0));
        firstSet.add(firstItem);
        itemSets.add(closure(firstSet));

        for (int i = 0; i < itemSets.size(); i++) {
            thisAction = new int[SParser.terminator.size()];
            thisGoto = new int[SParser.nonTerminator.size()];
            addReduction(itemSets.get(i));
            goTo(itemSets.get(i));
            ppt.addState(thisAction, thisGoto);
        }
        ppt.print();

        return ppt;
    }

    private ItemSet closure(ItemSet items){
        List<Item> closure = items.getItems();
        for (int i = 0; i < closure.size(); i++) {
            List<Integer> production = closure.get(i).getProduction().getSigns();
            if(production.get(closure.get(i).getPosition()) < 0){
                Set<Integer> predictor = first(production.subList(closure.get(i).getPosition()+1, production.size()));
                if(predictor.size() == 0) {
                    predictor.addAll(closure.get(i).getLookaheads());
                }
                List<Production> gamma = productions.get(- production.get(closure.get(i).getPosition()));
                for (int j = 0; j < gamma.size(); j++) {
                    items.add( new Item(predictor, 0, gamma.get(j)) );
                }
            }
        }

        return items;
    }
    
    private void goTo(ItemSet target) throws GRAException{
        DFA<ItemSet> gotoSet = new DFA<>();

        List<Integer> possibleX = new ArrayList<>();
        target.getItems().forEach(item -> {
            int next = item.getProduction().getSigns().get(item.getPosition());
            if(!possibleX.contains(next) && next != 0){
                possibleX.add(next);
            }
        });

        for (int i = 0; i < possibleX.size(); i++) {
            ItemSet newItemSet = new ItemSet();
            for (Item item : target.getItems()) {
                if (item.getProduction().getSigns().get(item.getPosition()) == possibleX.get(i)) {
                    Item newItem = new Item(item.getLookaheads(), item.getPosition()+1, item.getProduction());
                    newItemSet.add(newItem);
                }
            }
            int index = itemSets.addFromIndex(closure(newItemSet)) + 1;
            if(possibleX.get(i) < 0){
                thisGoto[ - possibleX.get(i)] = index ;
            }else {
                if (thisAction[possibleX.get(i)] == 0) {
                    thisAction[possibleX.get(i)] = index;
                }else {
                    throw new GRAException();
                }
            }
        }

    }

    private void addReduction(ItemSet items) throws GRAException{
        for (Item item : items.getItems()) {
            if (item.getProduction().getSigns().get(item.getPosition()) == 0) {
                for (Integer i : item.getLookaheads()) {
                    if (thisAction[i] != 0) {
                        throw new GRAException();
                    } else {
                        if (item.getProductionNum() == 0 && i == 0) {
                            thisAction[i] = Integer.MIN_VALUE;
                        }else {
                            thisAction[i] = -item.getProductionNum();
                        }
                    }
                }
            }
        }
    }
    
    private Set<Integer> first(List<Integer> beta){
        Set<Integer> first = new HashSet<>();
        for (int i = 0; i < beta.size(); i++) {
            if(beta.get(i) < 0){
                productions.get(- beta.get(i)).forEach(production -> {
                    first.addAll(production.getSigns());
                });
                break;
            }else if(beta.get(i) > 0){
                first.add(beta.get(i));
                break;
            } else if(beta.get(i) == 0){
                break;
            }
        }
        return first;
    }

}
