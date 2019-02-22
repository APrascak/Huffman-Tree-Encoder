class HuffTree implements Comparable{
  private HuffBaseNode root;  
  public int[] elements;

  /** Constructors */
  HuffTree(char el, int wt)
    { root = new HuffLeafNode(el, wt); }
  HuffTree(HuffBaseNode l, HuffBaseNode r, int wt)
    { root = new HuffInternalNode(l, r, wt); }

  HuffBaseNode root() { return root; }
  int weight() // Weight of tree is weight of root
    { return root.weight(); }
  public int compareTo(Object t) {
    HuffTree that = (HuffTree)t;
    if (root.weight() < that.weight()) return -1;
    else if (root.weight() == that.weight()) return 0;
    else return 1;
  }

}