// Alexander Prascak
// COP3530: Spring 2018
// Project 3

import java.util.*;
import java.io.*;


public class HuffmanEncoder implements HuffmanCoding {
	
	//take a file as input and create a table with characters and frequencies
    //print the characters and frequencies
    public String getFrequencies(File inputFile) {

		HashMap<Integer, Integer> test = new HashMap <Integer, Integer>();
		try {
			Scanner file = new Scanner(new FileReader(inputFile));
			while (file.hasNextLine() == true) {
				String eachLine = file.nextLine();
				for (int i = 0; i < eachLine.length(); i++) {
					Integer x = test.get((int) eachLine.charAt(i));
					if (test.get((int) eachLine.charAt(i)) == null) {
						test.put((int) eachLine.charAt(i), 1);
					} else {
						test.put((int) eachLine.charAt(i), ++x);
					}
				}
			}
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    String table = "";
	    for (Integer keys: test.keySet()) {
	    	int key = keys;
	    	char what = (char) key;
	    	String value = test.get(keys).toString();
	    	table += what + " " + value + "\n";
	    }
	    return table;

    }

    //take a file as input and create a Huffman Tree
    public HuffTree buildTree(File inputFile) {
    	String freqs = getFrequencies(inputFile);
    	int size = 0;


    	// Determines size of array
    	for (int i = 1; i<freqs.length(); i++) {
    		if (freqs.charAt(i) == ' ') {
    			size++;
    		}
    	}


    	int[][] organized = new int[size][2];
    	int location = 0;
    	String thisFrequency = "";

    	// Put frequencies into an array
    	for (int i = 1; i < freqs.length(); i++) {
    		if (freqs.charAt(i) == ' ') {
    			if (freqs.charAt(i+1) == ' ') {
    				organized[location][0] = ' ';
    				i+= 2;
    			} else {
    				organized[location][0] = (int) freqs.charAt(i-1);
    				i++;
    			}
    		}
    		while (freqs.charAt(i) != '\n') {
    			thisFrequency += freqs.charAt(i);
    			i++;
    		}
    		i++;
    		organized[location][1] = Integer.parseInt(thisFrequency);
    		location++;
    		thisFrequency = "";
    	}



    	// Bubble sort implementation from previous assignment to organize frequencies
		int toSwap = organized[0][1];
		int toSwapChar = organized[0][0];
		for (int j = 0; j < size-1; j++) {
			for (int i = 0; i < size-1; i++) {
				if (organized[i][1] > organized[i+1][1]) {
					toSwap = organized[i][1];
					toSwapChar = organized[i][0];
					organized[i][1] = organized[i+1][1];
					organized[i][0] = organized[i+1][0];
					organized[i+1][1] = toSwap;
					organized[i+1][0] = toSwapChar;
				}
			}
		}	


		// Put 1-node trees into min heap (priority queue)
		PriorityQueue<HuffTree> Hheap = new PriorityQueue<HuffTree>();
		for (int i = 0; i<size; i++) {
			HuffTree toAdd = new HuffTree((char) organized[i][0],organized[i][1]);
			Hheap.add(toAdd);
		}

		// Builds tree by taking 2 smallest trees
		HuffTree tmp1, tmp2, tmp3 = null;
		while (Hheap.size() > 1) {
			tmp1 = Hheap.remove();
			tmp2 = Hheap.remove();
			tmp3 = new HuffTree(tmp1.root(), tmp2.root(), tmp1.weight() + tmp2.weight());
			Hheap.add(tmp3);
		}
		tmp3.elements = new int[size];
		for (int i = 0; i < size; i++) {
			tmp3.elements[i] = organized[i][0];
		}
		return tmp3;
    }
    
    //take a file and a HuffTree and encode the file.  
    //output a string of 1's and 0's representing the file
    public String encodeFile(File inputFile, HuffTree huffTree) {



    	String traversal = traverseHuffmanTree(huffTree);

    	int[][] organizedToEncode = new int[huffTree.elements.length][2];
    	int location = 0;
    	String thisFrequency = "";
    	String encoded = "";
    	String binaryConversion = "";

    	// for loop to get frequencies
    	for (int i = 1; i < traversal.length(); i++) {
    		if (traversal.charAt(i) == ' ') {
    			organizedToEncode[location][0] = (int) traversal.charAt(i-1);
    			i++;
    		}
    		while (traversal.charAt(i) != '\n') {
    			thisFrequency += traversal.charAt(i);
    			i++;
    		}
    		i++;
    		location++;
    		thisFrequency = "";
    	}

		// Finds binary representations and adds them to string answer
    	try {
			Scanner file = new Scanner(new FileReader(inputFile));
			while (file.hasNextLine() == true) {
				String eachLine = file.nextLine();
				for (int i = 0; i < eachLine.length(); i++) {
					for (int j = 0; j < huffTree.elements.length; j++) {
						if (eachLine.charAt(i) == organizedToEncode[j][0]) {
							binaryConversion = findChar(huffTree.root(),(char)organizedToEncode[j][0]);
						}
					}
					encoded+=binaryConversion;
					binaryConversion = "";
				}
			}
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
    	return encoded;
    }

    //take a String and HuffTree and output the decoded words
    public String decodeFile(String code, HuffTree huffTree) {

    	// Finds answer by searching through tree based on 0s and 1s
    	String answer = "";
    	HuffBaseNode search = huffTree.root();
    	for (int i = 0; i < code.length(); i++) {
    		if (code.charAt(i) == '0') {
    			search = search.left();
    		}
    		if (code.charAt(i) == '1') {
    			search = search.right();
    		}
    		if (search.isLeaf()) {
    			answer+= search.value();
    			search = huffTree.root();
    		}
    	}
    	return answer;
    }

    //print the characters and their codes
    public String traverseHuffmanTree(HuffTree huffTree) {

    	// Sorts elements then finds elements by frequency
    	String traversed = "";
    	String bitsToAdd = "";
    	bubbleSort(huffTree.elements);
    	for (int i = 0; i < huffTree.elements.length; i++) {
    		bitsToAdd = findChar(huffTree.root(),(char) huffTree.elements[i]);
    		traversed += (char) huffTree.elements[i] + " " + bitsToAdd + "\n";
    	}
    	return traversed;
    }


    public String findChar(HuffBaseNode theNode,char thisChar) {

    	if (theNode !=null) {
    		// 2 if statements for the base cases
	    	if (theNode.left() != null) {
				if (theNode.left().isLeaf()) {
					if (theNode.left().value() == thisChar) {
						return "0";
					}
				}
			}
			if (theNode.right() != null) {
				if (theNode.right().isLeaf()) {
					if (theNode.right().value() == thisChar) {
						return "1";
					}
				}
			}
			// Recursive calls
			String leftString = findChar(theNode.left(), thisChar);
			if (leftString != null) return "0" + leftString;
			String rightString = findChar(theNode.right(), thisChar);
			if (rightString != null) return "1" + rightString;
		}

		return null;
	}

	// Bubble Sort algorithm
	public static void bubbleSort(int[] ints) {
		int toSwap = ints[0];
		for (int j = 0; j < ints.length-1; j++) {
			for (int i = 0; i < ints.length-1; i++) {
				if (ints[i] > ints[i+1]) {
					toSwap = ints[i];
					ints[i] = ints[i+1];
					ints[i+1] = toSwap;
				}
			}
		}
	}


}