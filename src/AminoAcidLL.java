class AminoAcidLL {
  char aminoAcid;
  String[] codons;
  int[] counts;
  AminoAcidLL next;

  AminoAcidLL() {
  }

  /********************************************************************************************/
  /* Creates a new node, with a given amino acid/codon
   * pair and increments the codon counter for that codon.
   * NOTE: Does not check for repeats!! */
  AminoAcidLL(String inCodon) {
    this.aminoAcid = AminoAcidResources.getAminoAcidFromCodon(inCodon);
    this.codons = AminoAcidResources.getCodonListForAminoAcid(this.aminoAcid);
    this.counts = new int[codons.length];
    incrementCount(inCodon);
    this.next = null;
  }

  /********************************************************************************************/
  /* Recursive method that increments the count for a specific codon:
   * If it should be at this node, increments it and stops,
   * if not passes the task to the next node.
   * If there is no next node, add a new node to the list that would contain the codon.
   */
  private void addCodon(String inCodon) {
    if (aminoAcid == AminoAcidResources.getAminoAcidFromCodon(inCodon)) {
      incrementCount(inCodon);
      return;
    } else if (next != null) {
      next.addCodon(inCodon);
    } else {
      this.next = new AminoAcidLL(inCodon);
      return;
    }
  }

  /********************************************************************************************/
  /* Helper method to increment counts array */
  private void incrementCount(String inCodon) {
    for (int i = 0; i < this.codons.length; i++) {
//      System.out.println("CODONS COMPARED = " + this.codons[i] + "vs" + inCodon);
      if (this.codons[i].equals(inCodon)) {
        this.counts[i]++;
//            for(int j = 0; j < this.counts.length; j++){
//              System.out.println(this.counts[j] + " ");
//            }
        break;
      }
    }
  }

  /********************************************************************************************/
  /* Shortcut to find the total number of instances of this amino acid */
  private int totalCount() {
    int sum = 0;

    for (int i = 0; i < counts.length; i++) {
      sum += counts[i];
    }
    return sum;
  }

  /********************************************************************************************/
  /* helper method for finding the list difference on two matching nodes
   *  must be matching, but this is not tracked */
  private int totalDiff(AminoAcidLL inList) {
    return Math.abs(totalCount() - inList.totalCount());
  }

  /********************************************************************************************/
  /* helper method for finding the list difference on two matching nodes
   *  must be matching, but this is not tracked */
  private int codonDiff(AminoAcidLL inList) {
    int diff = 0;
    for (int i = 0; i < codons.length; i++) {
      diff += Math.abs(counts[i] - inList.counts[i]);
    }
    return diff;
  }

  /********************************************************************************************/
  /* Recursive method that finds the differences in **Amino Acid** counts.
   * the list *must* be sorted to use this method */
  public int aminoAcidCompare(AminoAcidLL inList) {

    if(next == null){
      return 0;
    }


    return Math.abs(inList.totalCount());
  }

  /********************************************************************************************/
  /* Same as above, but counts the codon usage differences
   * Must be sorted. */
  public int codonCompare(AminoAcidLL inList) {

    return 0;
  }


  /********************************************************************************************/
  /* Recursively returns the total list of amino acids in the order that they are in in the linked list. */
  public char[] aminoAcidList() {

    if (next == null) {
      return new char[]{aminoAcid};
    }

    char[] temp = next.aminoAcidList();
    char[] ret = new char[temp.length+1];

    ret[0] = aminoAcid;
    for(int i = 0; i < temp.length; i++){
      ret[i+1] = temp[i];
    }

    return ret;
  }

  /********************************************************************************************/
  /* Recursively returns the total counts of amino acids in the order that they are in in the linked list. */
  public int[] aminoAcidCounts() {

   if(next == null){
      return new int[]{this.totalCount()};
   }

   int[] temp = next.aminoAcidCounts();
   int[] ret = new int[temp.length+1];

   ret[0] = totalCount();

   for(int i = 0; i < temp.length; i++){
      ret[i+1] = temp[i];
   }
   
    return ret;
  }

  public static void printInt(int[] aminoAcidCounts){
    for(int i = 0; i < aminoAcidCounts.length; i++){
      System.out.print(aminoAcidCounts[i] + " ");
    }
  }

  /********************************************************************************************/
  /* recursively determines if a linked list is sorted or not */
  public boolean isSorted() {
    if(next == null){
      return true;
    }

    if(aminoAcid > next.aminoAcid)
      return false;

    return next.isSorted();
  }


  /********************************************************************************************/
  /* Static method for generating a linked list from an RNA sequence */
  public static AminoAcidLL createFromRNASequence(String inSequence) {
    AminoAcidLL head = null;

    for (int i = 0; i < inSequence.length(); i++) {
      if (i == 0) {
        head = new AminoAcidLL(inSequence.substring(0, 3));
        inSequence = inSequence.substring(3);
      } else {
        head.addCodon(inSequence.substring(0, 3));
        inSequence = inSequence.substring(3);
        i = 1;
      }
    }

    return head;
  }


  /********************************************************************************************/
  /* sorts a list by amino acid character*/
  public static AminoAcidLL sort(AminoAcidLL inList) {
    return null;
  }

//  public static AminoAcidLL merge(AminoAcidLL left, AminoAcidLL right){
//
//  }

  public static void printLinkedList(AminoAcidLL head){
    AminoAcidLL temp = head;

    System.out.println("Current order of linked list");

    while(temp != null){
      System.out.print(temp.aminoAcid + " ");
      temp = temp.next;
    }

    System.out.println();
    }
}