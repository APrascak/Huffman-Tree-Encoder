/** Huffman tree node implementation: Base class */
interface HuffBaseNode {
  boolean isLeaf(); 
  int weight();
  HuffBaseNode left();
  HuffBaseNode right();
  char value();
}