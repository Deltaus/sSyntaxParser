package structure;

import parser.SParser;
import java.util.ArrayList;
import java.util.List;

public class PPT {

    private List<int[]> action = new ArrayList<>();
    private List<int[]> goTo = new ArrayList<>();

    public PPT() {
        action.add(new int[1]);
        goTo.add(new int[1]);
    }

    public void addState(int[] action, int[] goTo){
        if(action != null)
            this.action.add(action);
        else
            this.action.add(new int[SParser.terminator.size()]);
        if(goTo != null)
            this.goTo.add(goTo);
        else
            this.goTo.add(new int[SParser.nonTerminator.size()]);
    }

    public int action(int nowState, int terminator){
        return action.get(nowState)[terminator];
    }

    public int goTo(int nowState, int nonTerminator) {
        return goTo.get(nowState)[nonTerminator];
    }

    public void print(){
        String tab = "\t";
        System.out.print("   |");
        for (String s : SParser.terminator) {
            if(s.equals("")){
                System.out.print("$R"+tab);
            }else {
                System.out.print(s+tab);
            }
        }
        System.out.print("|");
        for (int i = 1; i < SParser.nonTerminator.size(); i++) {
            System.out.print(SParser.nonTerminator.get(i)+tab);
        }
        System.out.println();
        for (int i = 1; i < action.size(); i++) {
            if(i < 10){
                System.out.print(i + "  |");
            }else {
                System.out.print(i + " |");
            }
            for (int j = 0; j < action.get(i).length; j++) {
                if(action.get(i)[j] == Integer.MIN_VALUE){
                    System.out.print("acc");
                } else if (action.get(i)[j] < 0) {
                    System.out.print("r"+(-action.get(i)[j]));
                } else if(action.get(i)[j] > 0){
                    System.out.print("s"+action.get(i)[j]);
                }
                System.out.print(tab);
            }
            System.out.print("|");
            for (int j = 1; j < goTo.get(i).length; j++) {
                if(goTo.get(i)[j] != 0){
                    System.out.print(goTo.get(i)[j]);
                }
                System.out.print(tab);
            }
            System.out.println();
        }
    }
}