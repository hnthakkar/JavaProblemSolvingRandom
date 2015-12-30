import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PossibleAdjacent {
	
	public static int range = 200;
	public static boolean checkForEvenstart = true;
	public static final int primeArray[] = { 2, 3, 5, 7, 11, 13, 17, 19, 23,
			29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97,
			101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163,
			167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233,
			239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311,
			313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389,
			397 };

	public static HashMap<Integer, List<Integer>> possibleAdjOfEven = new HashMap<Integer, List<Integer>>();
	public static HashMap<Integer, List<Integer>> possibleAdjOfOdd = new HashMap<Integer, List<Integer>>();
	
	static {
		boolean isOdd = true;
		List<Integer> adjList = null;
		for (int i = 1; i <= range; i++) {
			adjList = new LinkedList<Integer>();
			int adj;
			for (int j = 0; j < primeArray.length; j++) {
				adj = primeArray[j] - i;
				if (adj > 0 && adj != i && adj <= range) {
					adjList.add(primeArray[j] - i);
				}
			}
			if (isOdd) {
				possibleAdjOfOdd.put(i, adjList);
				isOdd = false;
			} else {
				possibleAdjOfEven.put(i, adjList);
				isOdd = true;
			}
		}
		if((range%2)!=0)
			checkForEvenstart = false;
	}
	
	public List<Integer> checkIfUsed = null;
	public long possibleListCount = 0L;
	
	public void findAllArrangements() {
		checkIfUsed = new LinkedList<Integer>();
		boolean isOdd = true;
		for (int i = 1; i <= range; i++) {
			checkIfUsed.add(i);
			if (isOdd) {
				findAdjOfOdd(i);
				isOdd = false;
			} else {
				if(checkForEvenstart)
					findAdjOfEven(i);
				isOdd = true;
			}
			checkIfUsed.remove((Object) i);
		}
	}

	public boolean findAdjOfOdd(int lastNum) {
		if (checkIfUsed.size() == range) {
			System.out.println(checkIfUsed);
			possibleListCount++;
			return false;
		}

		List<Integer> possibleAdj = possibleAdjOfOdd.get(lastNum);
		for (Integer adj : possibleAdj) {
			if (checkIfUsed.contains(adj))
				continue;
			checkIfUsed.add(adj);
			if (findAdjOfEven(adj)) {
				return true;
			} else {
				checkIfUsed.remove(adj);
				continue;
			}
		}
		return false;
	}

	public boolean findAdjOfEven(int lastNum) {
		if (checkIfUsed.size() == range) {
			System.out.println(checkIfUsed);
			possibleListCount++;
			return false;
		}

		List<Integer> possibleAdj = possibleAdjOfEven.get(lastNum);
		for (Integer adj : possibleAdj) {
			if (checkIfUsed.contains(adj))
				continue;
			checkIfUsed.add(adj);
			if (findAdjOfOdd(adj)) {
				return true;
			} else {
				checkIfUsed.remove(adj);
				continue;
			}
		}
		return false;
	}

	public static void main(String[] arg) {
		long startTime = System.currentTimeMillis();
		PossibleAdjacent obj = new PossibleAdjacent();
		obj.findAllArrangements();
		long endTime = System.currentTimeMillis();
		System.out.println("Time taken ## " + (endTime - startTime) + " ## milliseconds");
		System.out.println("Number of Possible for range "+ range +" arrangements --> " + obj.possibleListCount);
	}
}