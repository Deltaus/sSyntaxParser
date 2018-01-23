package parser;

import structure.PPT;
import structure.Production;
import exception.GRAException;
import exception.PROException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

public class SParser {

    private static SParser parser;
    public static List<String> terminator = new ArrayList<>();     
    public static List<String> nonTerminator = new ArrayList<>();  
    public static List<Production> productionList = new ArrayList<>();
    private Map<Integer, List<Production>> productions = new HashMap<>();
    private PPT ppt;

    private SParser(){
        terminator.add("");
        nonTerminator.add("");
        Production firstProduction = new Production("");
        firstProduction.addSign(-1);
        firstProduction.addSign(0);
        productionList.add(firstProduction);
        productions.put(0, new ArrayList<>());
        productions.get(0).add(firstProduction);
    }

    public static SParser getParser(){
        if(parser == null){
            parser = new SParser();
        }
        return parser;
    }

    public void getInput(){
        System.out.print("Please enter the rule file: ");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        File input = new File(path);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(input));
            String line;

            StringBuilder names = new StringBuilder();
            boolean begin = false;
            while ((line = reader.readLine()) != null){
                if(line.trim().length() > 0) {
                    if (line.trim().contains("{") && !begin) {
                        begin = true;
                    } else if (line.trim().contains("{") && begin) {
                        throw new PROException();
                    }
                    if(begin){
                        names.append(line);
                    }
                    if (line.trim().contains("}") && begin) {
                        break;
                    }else if(line.trim().contains("}") && !begin){
                        throw new PROException();
                    }
                }
            }
            for (String s : names.substring(names.indexOf("{") + 1, names.indexOf("}")).split(",")) {
                if(s.trim().length() > 0){
                    nonTerminator.add(s.trim());
                }
            }

            String name = "";
            while ((line = reader.readLine()) != null){
                if(line.trim().length() > 0){
                    char[] c = line.trim().toCharArray();
                    StringBuilder each = new StringBuilder();
                    int i = 0;
                    if(c[0] == '|'){
                        if(name.length() > 0){
                            i = 1;
                        }else {
                            throw new PROException();
                        }
                    }else {
                        for(; i < c.length; i ++){
                            if(c[i] == ':') break;
                            if(c[i] != ' ') {
                                each.append(c[i]);
                            }
                        }
                        i++;
                        name = each.toString();
                        if(!nonTerminator.contains(name)){
                            nonTerminator.add(name);
                        }
                    }
                    Production production = new Production(name);
                    if(!productions.containsKey(nonTerminator.indexOf(name))){
                        productions.put(nonTerminator.indexOf(name), new ArrayList<>());
                    }
                    productions.get(nonTerminator.indexOf(name)).add(production);

                    for(; i < c.length; i ++){
                        if(c[i] == ' ') continue;
                        if(c[i] == '"'){
                            i ++;
                            each = new StringBuilder();
                            while (c[i] != '"'){
                                if(i == c.length) throw new PROException();
                                each.append(c[i]);
                                i++;
                            }
                            if(!terminator.contains(each.toString())){
                                terminator.add(each.toString());
                            }
                            production.addSign(terminator.indexOf(each.toString()));
                        }else {
                            each = new StringBuilder();
                            while (i < c.length && c[i] != ' ' && c[i] != '"'){
                                each.append(c[i]);
                                i ++;
                            }
                            if(!nonTerminator.contains(each.toString())){
                                throw new PROException(each.toString());
                            }
                            production.addSign(- nonTerminator.indexOf(each.toString()));
                            i --;
                        }
                    }
                    production.addSign(0);
                    productionList.add(production);
                    production.print(true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void compile() throws GRAException{
        ppt = new RE2DFA(productions).createPPT();
    }

    public void analyze(){
        System.out.println("Please enter what you want to analyze (enter 'exit' to exit): ");
        Scanner scanner = new Scanner(System.in);
        String line;
        Stack<Integer> state = new Stack<>();
        while (!(line = scanner.nextLine()).equals("exit")){
            List<String> inputs = new ArrayList<>();
            for (String s : line.trim().split(" +")) {
                inputs.add(s);
            }
            inputs.add("");
            
            state.add(1);
            try {
                for (int i = 0; i < inputs.size(); i++) {
                    int index = terminator.indexOf(inputs.get(i));
                    int thisState;
                    if (index >= 0) {
                        thisState = ppt.action(state.peek(), index);
                        if (thisState > 0) {
                            state.push(thisState);
                        } else if (thisState < 0) {
                            if (thisState == Integer.MIN_VALUE) {
                                System.out.println("Finish!");
                                state.removeAllElements();
                                break;
                            } else {
                                thisState = -thisState;
                                for (int j = 0; j < productionList.get(thisState).getSigns().size() - 1; j++) {
                                    state.pop();
                                }
                                int nonTerminatorNum = nonTerminator.indexOf(productionList.get(thisState).getName());
                                int actualState = ppt.goTo(state.peek(), nonTerminatorNum);
                                if (actualState != 0) {
                                    state.push(actualState);
                                    productionList.get(thisState).print(false);
                                } else {
                                    System.out.println("Cannot interpret this string!");
                                    break;
                                }
                                i--;
                            }
                        }
                    } else {
                        System.out.println("There is no terminator {" + inputs.get(i) + "}");
                        break;
                    }
                }
                if (state.size() != 0) {
                    System.out.println("Cannot interpret this string!");
                }
            } catch (Exception e) {
                System.out.println("Wrong string");
            }
        }
    }

}