package structure;


import parser.SParser;
import java.util.ArrayList;
import java.util.List;

public class Production {

    private static int count = 0;
    private int id;
    private String name;
    private List<Integer> signs;
    public Production(String name) {
        this.id = count;
        this.name = name;
        signs = new ArrayList<>();
        count ++;
    }

    public void addSign(int sign){
        signs.add(sign);
    }

    public void print(boolean hasNum){
        if(hasNum)
            System.out.print("("+id+")");
        System.out.print(name + "->");
        signs.forEach(i -> {
            if(i < 0){
                System.out.print(SParser.nonTerminator.get(-i)+" ");
            }else if(i > 0){
                System.out.print("\""+ SParser.terminator.get(i)+"\" ");
            }
        });
        System.out.println();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getSigns() {
        return signs;
    }
}