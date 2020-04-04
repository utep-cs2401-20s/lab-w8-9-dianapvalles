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
      this.incrementCount(inCodon);
    }
    //the comparison did not pass and there are other nodes (amino acids), then we move to the next node to check it [recursive call]
    else if (next != null) {
      next.addCodon(inCodon);
    }
    //we reached the end and never found match, we create a new node
    else {
      this.next = new AminoAcidLL(inCodon);
    }
  }

  /********************************************************************************************/
  /* Helper method that increments the count of the codon usage */
  private void incrementCount(String inCodon) {
    for (int i = 0; i < this.codons.length; i++) {
      if (this.codons[i].equals(inCodon)) {
        this.counts[i]++;
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
    //both lists are at the end
    if(inList.next == null && this.next == null){
      if(this.aminoAcid == inList.aminoAcid)
        return this.totalDiff(inList);

      return inList.totalCount() + this.totalCount();
    }

    //both list have the same aminoacid
    if(this.aminoAcid == inList.aminoAcid){
      //the next in "this" is null
      if(this.next == null) {
        return this.totalDiff(inList) + sum(inList.next);
      }
      //the next in inList is null
      if(inList.next == null)
        return this.totalDiff(inList) + sum(this.next);

      //no next null values
      return this.totalDiff(inList) + this.next.aminoAcidCompare(inList.next);
    }

    //compares the characters
    if(this.next != null && this.aminoAcid < inList.aminoAcid){
      return this.totalCount() + this.next.aminoAcidCompare(inList);
    }

    return inList.totalCount() + this.aminoAcidCompare(inList.next);
  }

  //helper method
  public int sum(AminoAcidLL node){
    AminoAcidLL temp = node;
    int sum = 0;

    while(temp  != null){
      sum += temp.totalCount();
      temp = temp.next;
    }

    return sum;
  }

  /********************************************************************************************/
  /* Same as above, but counts the codon usage differences
   * Must be sorted. */
  public int codonCompare(AminoAcidLL inList) {
    //both lists are at the end
    if(inList.next == null && this.next == null){
      if(this.aminoAcid == inList.aminoAcid)
        return this.codonDiff(inList);

      return inList.totalCount() + this.totalCount();
    }

    //both list have the same aminoacid
    if(this.aminoAcid == inList.aminoAcid){
      //the next in "this" is null
      if(this.next == null) {
        return this.codonDiff(inList) + sum(inList.next);
      }
      //the next in inList is null
      if(inList.next == null)
        return this.codonDiff(inList) + sum(this.next);

      //no next null values
      return this.codonDiff(inList) + this.next.codonCompare(inList.next);
    }

    //compare the characters
    if(this.next != null && this.aminoAcid < inList.aminoAcid){
      return this.totalCount() + this.next.codonCompare(inList);
    }

    return inList.totalCount() + this.codonCompare(inList.next);
  }


  /********************************************************************************************/
  /* Recursively returns the total list of amino acids in the order that they are in in the linked list. */
  public char[] aminoAcidList() {
    //base case
    if (next == null) {
      return new char[]{aminoAcid};
    }

    //recursive call to "append" the aminoacids
    char[] temp = next.aminoAcidList();
    //array that stores each of the amino acid characters
    char[] ret = new char[temp.length+1];

    //copies temp values in ret
    ret[0] = aminoAcid;
    for(int i = 0; i < temp.length; i++){
      ret[i+1] = temp[i];
    }

    return ret;
  }

  /********************************************************************************************/
  /* Recursively returns the total counts of amino acids in the order that they are in in the linked list. */
  public int[] aminoAcidCounts() {
  //base case
   if(next == null){
      return new int[]{this.totalCount()};
   }

   //recursive call to get the total counts of the specific aminoacid
   int[] temp = next.aminoAcidCounts();
   //array that stores each of the counts
   int[] ret = new int[temp.length+1];

   //copies temp values in ret
   ret[0] = totalCount();
   for(int i = 0; i < temp.length; i++){
      ret[i+1] = temp[i];
   }
    return ret;
  }

  public static void printAminoAcidCounts(int[] array){

    for(int i = 0; i < array.length; i++){
      System.out.print(array[i]);
    }
    System.out.println();
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
    //initializes the head to null
    AminoAcidLL head = null;

    for (int i = 0; i < inSequence.length(); i++) {
      //if the codon encodes a STOP, then we break out of the loop
      if(AminoAcidResources.getAminoAcidFromCodon(inSequence.substring(0,3)) == '*')
        break;
      //The head is initialized since it is the first iteration
      if (i == 0) {
        head = new AminoAcidLL(inSequence.substring(0, 3));
        inSequence = inSequence.substring(3);
      }
      //the rest of the nodes are created by separating the string in substring of length 3
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

    //iterates through the list until the amino acid is less than another amino acid
    while(curNodeB != null && aminoAcid > curNodeB.aminoAcid){
      curNodeA = curNodeB;
      curNodeB = curNodeB.next;
    }
    return curNodeA;
  }

  /********************************************************************************************/
  /* IGNORE --  I tried to implement merge sort */
  /* sorts a list by amino acid character*/
  public static AminoAcidLL sort1(AminoAcidLL inList) {

    if(inList == null || inList.next == null)
      return inList;

    AminoAcidLL middle = getMiddle(inList);
    AminoAcidLL middleNext = middle.next;

    middle.next = null;

    AminoAcidLL leftSide = sort1(middle);
    AminoAcidLL rightSide = sort1(middleNext);

    AminoAcidLL head = merge(leftSide,rightSide);

    return head;
  }

  /* helper method to get the middle node */
  public static AminoAcidLL getMiddle(AminoAcidLL inList){

    if(inList == null)
      return inList;

    AminoAcidLL slow = inList;
    AminoAcidLL fast = inList;

    while(fast.next != null && fast.next.next != null){
      slow = slow.next;
      fast = fast.next.next;
    }

    return slow;
  }

  public static AminoAcidLL merge(AminoAcidLL left, AminoAcidLL right){
    AminoAcidLL result = null;

    if(left == null)
      return right;

    if(right == null)
      return left;

    if(left.aminoAcid <= right.aminoAcid){
      result = left;
      result.next = merge(left.next, right);
    }
    else{
      result = right;
      result.next = merge(left, right.next);
    }
    return result;
  }

  /********************************************************************************************/
  /* helper method to print the linked list */
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