import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class AminoAcidResourcesTester{

  @Test
  public void allCodons(){
    char[] rna = {'A','C','U','G'};
    char[] aa = {'A','C','D','E','F','G','H','I','K','L','M','N','P','Q','R','S','T','V','W'};
    for(int i=0; i<4; i++){
      for(int j=0; j<4; j++){
        for(int k=0; k<4;k++){
          String s = new String(new char[]{rna[i],rna[j],rna[k]});
          char aaOut = AminoAcidResources.getAminoAcidFromCodon(s);
          if(aaOut != '*'){
            String[] codonList = AminoAcidResources.getCodonListForAminoAcid(aaOut);
            boolean found = false;
            for(int l=0; l<codonList.length; l++){
              found |= (codonList[l].equals(s));
            }
            if(!found) System.err.println("Codon " + s + " not found, said AA was " + aaOut);
          }

          aaOut = AminoAcidResources.getAminoAcidFromCodon(s.toLowerCase());
          if(aaOut != '*'){
            String[] codonList = AminoAcidResources.getCodonListForAminoAcid(aaOut);
            boolean found = false;
            for(int l=0; l<codonList.length; l++){
              found |= (codonList[l].equals(s));
            }
            if(!found) System.err.println("Codon " + s + " not found, said AA was " + aaOut);
          }
        }
      }
    }

  }

  @Test
  public void allAAs(){

    char[] aa = {'A','C','D','E','F','G','H','I','K','L','M','N','P','Q','R','S','T','V','W'};
    for(int i=0; i<aa.length; i++){
      String[] codonList = AminoAcidResources.getCodonListForAminoAcid(aa[i]);
      for(int l=0; l<codonList.length; l++){
        if(aa[i] != AminoAcidResources.getAminoAcidFromCodon(codonList[l])){
          System.err.println("AA " + aa[i] + " not found, said codon was " + codonList[l]);
        }
      }

      codonList = AminoAcidResources.getCodonListForAminoAcid(Character.toLowerCase(aa[i]));
      for(int l=0; l<codonList.length; l++){
        if(aa[i] != AminoAcidResources.getAminoAcidFromCodon(codonList[l])){
          System.err.println("AA " + aa[i] + " not found, said codon was " + codonList[l]);
        }
      }
    }
  }

  /*
     * The purpose of this method is to check if the RNA sequence is converted correctly into a linked list by using the aminoAcidList method (also tests its correct implementation)
     * The string passed has an stop value (the test also check if it stops at the right position)
     * If the test passes it means that the linked list created the right amino acids and also stopped translating when a codon encoded "STOP"
     * If the test fails either the linked list was not correct (createRNASequence method), the aminoAcidList method does not work or the STOP was ignored
     * TEST PASSED
   */
  @Test
  public void sequence1(){
    AminoAcidLL head = AminoAcidLL.createFromRNASequence("GCUACGGAGCUUCGGAGCUAGAUG");
    char[] expected = {'A','T','E','L','R','S'};
    char[] actual = head.aminoAcidList();

    assertArrayEquals(expected, actual);
  }

  /*
     * The purpose of this method is to check if the RNA sequence is sorted correctly by using the aminoAcidList method (also tests its correct implementation)
     * If the test passes it means that the linked list created the right amino acids and sorted them in the right alphanumeric order
     * If the test fails either the linked list was not correct (createRNASequence method), the aminoAcidList method does not work or the list was not sorted correctly
     * TEST PASSED
   */
  @Test
  public void sequence2() {
    AminoAcidLL head = AminoAcidLL.createFromRNASequence("CGGGUGUCAGAUGCG"); //[R,V,S,D,A]
    AminoAcidLL sorted = AminoAcidLL.sort(head);
    char[] expected = {'A','D','R','S','V'};
    char[] actual = sorted.aminoAcidList();

    assertArrayEquals(expected, actual);
  }

  /*
     * The purpose of this method is to check if the RNA sequence is sorted correctly by using the isSorted method (tests its correct implementation)
     * If the test passes it means that the linked list created the right amino acids and sorted them in the right alphanumeric order by returning a boolean value (true/false)
     * If the test fails either the linked list was not correct (createRNASequence method), the list was not sorted correctly or the isSorted method is not working
     * TEST PASSED
   */
  @Test
  public void sequence3(){
    AminoAcidLL head = AminoAcidLL.createFromRNASequence("GCUUAUCACUGGCUG"); //[A,T,H,W,L]
    AminoAcidLL sorted = AminoAcidLL.sort(head);

    assertEquals(true, sorted.isSorted());
  }

  /*
   * The purpose of this method is to check if the total amino acid counts (in the order in which they are in the linked list) is correct
   * If the test passes it means the amino acids who were repeated incremented their counts correctly [some encode the same amino acid]
   * If the test fails either the private method for the counts[] parameter is wrong or the method aminoAcidCounts is wrong
   * TEST PASSED
   */
  @Test
  public void sequence4(){
    AminoAcidLL head = AminoAcidLL.createFromRNASequence("UGUGGUUGCCCAUUUCCCUUACCU"); //[C,G,C,P,F,P,L,P]
    AminoAcidLL.printLinkedList(head);
    int[] expected = {2,1,3,1,1};
    int[] actual = head.aminoAcidCounts();

    assertArrayEquals(expected, actual);
  }

  /*
   * The purpose of this method is to check if the aminoAcidCompare returns the expected value (the difference in counts between two lists of amino acids)
   * If the test passes it means the method to get difference in counts between two [matching] amino acids is correct, the sequences have different length and amino acid counts
   * If the test fails either the private method totalCount() is wrong or the method aminoAcidCompare() might not be behaving correctly
   * TEST PASSED
   */
  @Test
  public void sequence5(){
    AminoAcidLL head = AminoAcidLL.createFromRNASequence("AAGGCUGCACUUUAA"); //[R,A,A,P]
    AminoAcidLL sorted = AminoAcidLL.sort(head);
    AminoAcidLL inList = AminoAcidLL.createFromRNASequence("UACGCC"); //[A,T]
    AminoAcidLL sorted1 = AminoAcidLL.sort(inList);
    int expected = 4;
    int actual = sorted.aminoAcidCompare(sorted1);

    assertEquals(expected,actual);
  }

  /*
   * The purpose of this method is to check if the codonCompare returns the expected value (based on the difference in counts between two lists of amino acids by the individual codon counts)
   * If the test passes it means the method to get difference in counts between two [matching] amino acids is correct, the sequences have different length
   * If the test fails either the private method totalCount(), the codons[] attribute or the method codonCompare() might not be behaving correctly
   * TEST PASSED
   */
  @Test
  public void sequence6(){
    AminoAcidLL head = AminoAcidLL.createFromRNASequence("CUGGCACCGUUG"); //[L,A,P,L]
    AminoAcidLL sorted = AminoAcidLL.sort(head);
    AminoAcidLL inList = AminoAcidLL.createFromRNASequence("CCCUGG"); //[P,W]
    AminoAcidLL sorted1 = AminoAcidLL.sort(inList);
    int expected = 6;
    int actual = sorted.codonCompare(sorted1);

    assertEquals(expected,actual);
  }

  /*
   * The purpose of this method is to combine different methods and see if the result is what we expected (checks for the behavior of the program when multiple things are done to the list)
   * If the test passes it means the methods involve are behaving correctly [translating, sorting]
   * The sequence passed will be unsorted, then the method sort() will sort it, then we will call the method aminoAcidCounts to check for the counts when the list is sorted
   *  If the test fails either the private method for the counts[] parameter is wrong or the method aminoAcidCounts is wrong [considering the list is sorted]
   * TEST PASSED
   */
  @Test
  public void sequence7(){
    AminoAcidLL head = AminoAcidLL.createFromRNASequence("UGUUGCCCAUUUCCCUUACCU"); //[C,C,P,F,P,L,P]
    AminoAcidLL sorted = AminoAcidLL.sort(head);
    int[] expected = {2,1,1,3};
    int[] actual = sorted.aminoAcidCounts();

    assertArrayEquals(expected, actual);
  }


}