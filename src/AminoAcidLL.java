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
    //method call to get the amino acid character of an specific codon
    this.aminoAcid = AminoAcidResources.getAminoAcidFromCodon(inCodon);
    //method call to get the possible codons [array] of the amino acid
    this.codons = AminoAcidResources.getCodonListForAminoAcid(this.aminoAcid);
    //initializes the counts for each codon
    this.counts = new int[codons.length];
    //increments the count for the codon passed in the constructor
    incrementCount(inCodon);
    //creates the next link of the linked list [set to null]
    this.next = null;
  }

  /********************************************************************************************/
  /* Recursive method that increments the count for a specific codon:
   * If it should be at this node, increments it and stops,
   * if not passes the task to the next node.
   * If there is no next node, add a new node to the list that would contain the codon.
   */
  private void addCodon(String inCodon) {
    //compares the current amino acid with the amino acid that results from the codon passed
    //if it is the same, then it increments the count and does not create another node
    if (aminoAcid == AminoAcidResources.getAminoAcidFromCodon(inCodon)) {
      incrementCount(inCodon);
      return;
    }
    //the comparison did not pass and there are other nodes (amino acids), then we move to the next node to check it [recursive call]
    else if (next != null) {
      next.addCodon(inCodon);
    }
    //we reached the end and never found match, we create a new node
    else {
      this.next = new AminoAcidLL(inCodon);
      return;
    }
  }

  /********************************************************************************************/
  /* Helper method that increments the count of the codon usage */
  private void incrementCount(String inCodon) {
    for (int i = 0; i < this.codons.length; i++) {
      if (this.codons[i].equals(inCodon)) {
        this.counts[i]++;
        break;
      }
    }
  }

  /********************************************************************************************/
  /* Shortcut to find the total number of instances of this amino acid */
  private int totalCount() {
    int sum = 0;

    //iterates through the array to get each of the codons count
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

//    if(!inList.isSorted()){
//      sort(inList);
//    }


//    if(inList == null){
//      int temp = this.totalCount();
//      return this.aminoAcidCompare(this.next);
//    }

    //  if(this == null){
//      int a = this.aminoAcidCompare(inList.next);
//    }

    //aminoAcids matched
    if(this.aminoAcid == inList.aminoAcid){
      return this.totalDiff(inList) + this.next.aminoAcidCompare(inList.next);
    }

    //aminoAcids doesn't match
    if(this.aminoAcid < inList.aminoAcid){
      return this.totalCount() + this.next.aminoAcidCompare(inList);
    }

    //inList has an aminoacid that "this" doesn't
    return Math.abs(inList.totalCount());
  }

  /********************************************************************************************/
  /* Same as above, but counts the codon usage differences
   * Must be sorted. */
  public int codonCompare(AminoAcidLL inList) {

    if(this.aminoAcid == inList.aminoAcid){
      return this.codonDiff(inList) + this.next.aminoAcidCompare(inList.next);
    }

    //aminoAcids doesn't match
    if(this.aminoAcid < inList.aminoAcid){
      return this.totalCount() + this.next.aminoAcidCompare(inList);
    }

    //inList has an aminoacid that "this" doesn't
    return Math.abs(inList.totalCount());
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
      if(AminoAcidResources.getAminoAcidFromCodon(inSequence.substring(0,3)) == '*')
        break;
      if (i == 0) {
        head = new AminoAcidLL(inSequence.substring(0, 3));
        inSequence = inSequence.substring(3);
      }
      else {
        if(AminoAcidResources.getAminoAcidFromCodon(inSequence.substring(0,3)) == '*')
          break;
        head.addCodon(inSequence.substring(0, 3));
        inSequence = inSequence.substring(3);
        i = 1;
      }
    }

    return head;
  }


  /********************************************************************************************/
  /* sorts a list by amino acid character*/
  //Uses insertion sort
  public static AminoAcidLL sort(AminoAcidLL inList) {
    AminoAcidLL beforeCurrent = inList;
    AminoAcidLL curNode = inList.next;
    AminoAcidLL next;
    AminoAcidLL position;

    while(curNode != null){
      next = curNode.next;
      //method call to find the insertion position
      position = findInsertion(curNode.aminoAcid, inList);

      if(position == beforeCurrent){
        beforeCurrent = curNode;
      }
      else {
        beforeCurrent.next = null;

        //prepends the node
        if(position == null){
          AminoAcidLL temp = inList;
          inList = curNode;
          curNode.next = temp;
          beforeCurrent.next = next;
        }
        //inserts the node after "position"
        else{
          position.next = curNode;
          curNode.next = beforeCurrent;
          beforeCurrent.next = next;
        }
      }
      //moves to the next node
      curNode = next;
    }
    return inList;
  }

  /* helper method that searches the list from the head toward the current node to find the insertion position */
    private static AminoAcidLL findInsertion(char aminoAcid, AminoAcidLL inList){
    AminoAcidLL curNodeA = null;
    AminoAcidLL curNodeB = inList;

    while(curNodeB != null && aminoAcid > curNodeB.aminoAcid){
      curNodeA = curNodeB;
      curNodeB = curNodeB.next;
    }
    return curNodeA;
  }


  /********************************************************************************************/
  /* helper method to print the array */
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