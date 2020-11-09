import java.util.*;
public class CSPPuzzle extends CSP {

    public boolean isGood(Object X, Object Y, Object x, Object y) {
        //rule 5
        if(X.equals("green") && Y.equals("ivory")) {
            if( (int)x != (int)y + 1) {
                return false;
            }
            return true;
        }
        // rule 10
        if(X.equals("Chesterfield") && Y.equals("fox")){
            if ((int)x != (int)y+1 && (int)x != (int)y-1){
                return false;
            }
            return true;
        }
        // rule 11
        if(X.equals("Kools") && Y.equals("horse")){
            if ((int)x != (int)y+1 && (int)x != (int)y-1){
                return false;
            }
            return true;
        }

        //if X is not even mentioned in by the constraints, just return true
        //as nothing can be violated
        if(!C.containsKey(X))
            return true;

        //check to see if there is an arc between X and Y
        //if there isn't an arc, then no constraint, i.e. it is good
        if(!C.get(X).contains(Y))
            return true;

        //not equal constraint
        if(!x.equals(y))
            return true;

        return false;
    }


    public static void main(String[] args) throws Exception {
        CSPPuzzle csp = new CSPPuzzle();

        Integer[] domains = {1, 2, 3, 4, 5};

        String[] varHouses = {"red", "ivory", "yellow", "blue", "green"};
        String[] varPets = {"dog", "horse", "fox", "zebra", "snails"};
        String[] varDrinks = {"orange juice", "milk", "coffee", "tea", "water"};
        String[] varCigarettes = {"Parliament", "Lucky-Strike", "Kools", "Chesterfield", "Old-Gold"};
        String[] varNationalities = {"English", "Spaniard", "Ukrainian", "Norwegian", "Japanese"};

        String[][] pairs = {{"English","blue"},{"English","green"},{"English","ivory"},{"English","yellow"},
                {"Spaniard","fox"},{"Spaniard","horse"},{"Spaniard","snails"},{"Spaniard","zebra"},
                {"coffee","blue"},{"coffee","ivory"},{"coffee","red"},{"coffee","yellow"},
                {"Ukrainian","coffee"}, {"Ukrainian","milk"}, {"Ukrainian","orange juice"}, {"Ukrainian","water"},
                {"Old-Gold", "dog"}, {"Old-Gold", "fox"},{"Old-Gold", "horse"},{"Old-Gold", "zebra"},
                {"Kools", "blue"}, {"Kools", "green"},{"Kools", "ivory"},{"Kools", "red"},
                {"Lucky-Strike","coffee"}, {"Lucky-Strike","milk"},{"Lucky-Strike","tea"},{"Lucky-Strike","water"},
                {"Japanese", "Chesterfield"},{"Japanese", "Kools"},{"Japanese", "Lucky-Strike"},{"Japanese", "Old-Gold"}
                };

        // add all possible domain
        for(Object X : varHouses) {
            csp.addDomain(X, domains);
        }
        for(Object X : varPets) {
            csp.addDomain(X, domains);
        }
        for(Object X : varDrinks) {
            csp.addDomain(X, domains);
        }
        for(Object X : varCigarettes) {
            csp.addDomain(X, domains);
        }
        for(Object X : varNationalities) {
            csp.addDomain(X, domains);
        }

        // Uniary constraints: remove values from domains
        // milk = 3, Norwegian = 1, blue == 2
        csp.D.get("milk").remove(1);
        csp.D.get("milk").remove(2);
        csp.D.get("milk").remove(4);
        csp.D.get("milk").remove(5);

        csp.D.get("Norwegian").remove(2);
        csp.D.get("Norwegian").remove(3);
        csp.D.get("Norwegian").remove(4);
        csp.D.get("Norwegian").remove(5);

        csp.D.get("blue").remove(1);
        csp.D.get("blue").remove(3);
        csp.D.get("blue").remove(4);
        csp.D.get("blue").remove(5);

        // Binary constraints: add constraint arcs
        for(Object[] p : pairs) {
            csp.addBidirectionalArc(p[0], p[1]);
        }

        // Uniqueness constraints
        for (Object X: varHouses) {
            for (Object Y: varHouses) {
                csp.addBidirectionalArc(X,Y);
            }
        }
        for (Object X: varPets) {
            for (Object Y: varPets) {
                csp.addBidirectionalArc(X,Y);
            }
        }
        for (Object X: varDrinks) {
            for (Object Y: varDrinks) {
                csp.addBidirectionalArc(X,Y);
            }
        }
        for (Object X: varCigarettes) {
            for (Object Y: varCigarettes) {
                csp.addBidirectionalArc(X,Y);
            }
        }
        for (Object X: varNationalities) {
            for (Object Y: varNationalities) {
                csp.addBidirectionalArc(X,Y);
            }
        }

        Search search = new Search(csp);
        System.out.println(search.BacktrackingSearch());
    }
}