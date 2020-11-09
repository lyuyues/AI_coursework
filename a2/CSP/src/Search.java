import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Search {

	CSP csp;
	
	public Search(CSP csp) { this.csp = csp; }
	
	//returns an assignment
	public Map<Object, Object> BacktrackingSearch() {
		//create an empty assignment, the return result
		Map<Object, Object> assignment = new TreeMap<Object, Object>();
		return Backtrack(assignment);
	}
	
	Map<Object, Object> Backtrack(Map<Object, Object> assignment) {
		if( isComplete(assignment) )
			return assignment;
		// MRV get a variable with least valid domain to tie
		Object X = SelectUnassignedVariable(assignment);
		// for every valid domain variable X has
		for(Object x : csp.D.get(X)) 
			if(isConsistent(X,x,assignment)) { // there is no violation between X and x
				assign(X,x,assignment); // assign x to variable X

				//recursion
				Map<Object, Object> result = Backtrack(assignment);
				if(result!=null) 
					return result;
				
				assignUndo(X,assignment);
			}
		return null;
	}

	// the stack to store the deleted
	Deque<Map<Object, Set<Object>>> stack = new ArrayDeque<Map<Object, Set<Object>>>();
	
	//assigns x to X and does inferencing
	void assign(Object X, Object x, Map<Object, Object> assignment) {
		assignment.put(X, x);
		
		//now do forward checking and record deleted values 
		//var-set of deleted values
		if(!csp.C.containsKey(X))
			return;
		Map<Object, Set<Object>> deletedValues = new TreeMap<Object, Set<Object>>();
		for(Object Y : csp.C.get(X)) {
			if(assignment.containsKey(Y))
				continue;
			
			for(Object y : csp.D.get(Y))
				if(!csp.isGood(X, Y, x, y)) {  //isGood return false, then means XY has constrain deleted Y value y
					
					if(!deletedValues.containsKey(Y))
						deletedValues.put(Y, new TreeSet<Object>());
					
					deletedValues.get(Y).add(y);
				}
		}
		
		stack.push(deletedValues);
		//do the real value deletions from variable domains in csp
		for(Object Y : deletedValues.keySet()) 
			csp.D.get(Y).removeAll(deletedValues.get(Y));
	}

	// remove the variable X from assigned list
	void assignUndo(Object X, Map<Object, Object> assignment) {
		assignment.remove(X);
		
		//Now undo value deletions by forward checking
		if (stack.isEmpty())
			return;

		// give back the valid domain value to the neighbour of X
		// (neighbour of X) variable , the corresponding domain value
		Map<Object, Set<Object>> deletedValues = stack.pop();
		for(Object Y : deletedValues.keySet()) 
			csp.D.get(Y).addAll(deletedValues.get(Y));
	}

	// check if any violation, return true for no violation
	boolean isConsistent(Object X, Object x, Map<Object, Object> assignment) {
		for(Object Y : assignment.keySet()) {
			Object y = assignment.get(Y);
		
			if(!csp.isGood(X,Y,x,y))
				return false;
			
			if(!csp.isGood(Y,X,y,x))
				return false;
		}
		return true;
	}

	// using MRV to select a variable (to give a domain), return the selected variable
	Object SelectUnassignedVariable(Map<Object, Object> assignment) {
		//Implements minimum remaining values (MRV) 
		int min = Integer.MAX_VALUE;
		Object Xmin = null;
		
		for(Object X : csp.D.keySet()) {
			if (assignment.containsKey(X))
				continue;
		
			if (csp.D.get(X).size() < min) {
				min = csp.D.get(X).size();
				Xmin = X;
			}
		}
		
		return Xmin;
	}

	// check if the assignment is a solution
	boolean isComplete(Map<Object, Object> assignment) {
		if(assignment.size() == csp.D.size())
			return true;
		
		return false;
	}
}