
import java.io.*;

public class StringTrie {

    private static int R = 26;  // number of different characters
    private static int N = 0;   // number of words in trie
    private Node root;  // root of trie

    /* trie node */
    private static class Node {
        private boolean mark;  // true if a word ends in this node
        private int count;     // number of occurences of word
        private Node[] next = new Node[R];  // links to R children
    }

    /* auxiliary class: stores a word s and its number of occurrences */
    private static class Item {
         private String s;
         private int count;
    }
    
    // returns true if trie contains string s
    public boolean contains(String s) {
        Node x = contains(root, s, 0);
        if (x == null) {
            return false;
        }
        return x.mark;
    }

    // search in the subtree of x for the substring of s starting at position d
    private Node contains(Node x, String s, int d) { 
        if (x == null) {
            return null;
        }
        if (d == s.length()) {
            return x;  // end of word
        }
        char c = s.charAt(d);   // next character
        int j = (int) c - 'a';  // index of next character
        return contains(x.next[j], s, d + 1);
    }

    // insert string s in trie
    public void insert(String s) {
        if (contains(s)) {
            Node x = contains(root, s, 0);
            x.count++;
        } else {
            root = insert(root, s, 0);
        }
    }

    // insert into the subtree of x the substring of s starting at position d
    private Node insert(Node x, String s, int d) {
        if (x == null) {
            x = new Node();
        }
        if (d == s.length()) {
            x.mark = true;
            N++;
            x.count++;
            return x;
        }  // end of word
        char c = s.charAt(d);   // next character
        int j = (int) c - 'a';  // index of next character
        x.next[j] = insert(x.next[j], s, d + 1);
        return x;
    }

    // delete string s from trie
    public void delete(String s) {
        // lower the number of occurrences of s
        Node x = contains(root, s, 0); 
        if (x != null) {
            root = delete(root, s, 0);
            x.count--;
        }
        // if s has no more occurrences, delete it
        if (x.count == 0) {
            root = delete(root, s, 0);
        }
    }

    // delete from the subtree of x the substring of s starting at position d
    private Node delete(Node x, String s, int d) {
        if (x == null) {
            return null;
        }
        if (d == s.length()) {
            if (x.count == 0) {
                x.mark = false;
            }
            N--;
        } else {
            char c = s.charAt(d);   // next character
            int j = (int) c - 'a';  // index of next character
            x.next[j] = delete(x.next[j], s, d + 1);
        }

        if (x.mark) {
            return x; // x is marked so it is not deleted
        }
        for (int j = 0; j < R; j++) {
            if (x.next[j] != null) {
                return x; // x has non-null child so it is not deleted
            }
        }
        return null;
    }

    // collect all the words stored in the trie
    public Iterable<String> words() {
        Queue<String> q = new Queue<String>(N); // stores the words of the trie
        collect(root, "", q);
        return q;
    }

    // store the words with prefix pre stored in the subtree of x
    private void collect(Node x, String pre, Queue<String> q) {   
        if (x == null) {
            return;
        }
        if (x.mark) {
            q.put(pre); // x is marked so pre is a word stored in the trie
        }
        for (int j = 0; j < R; j++) {
            char c = (char) ('a' + j);
            collect(x.next[j], pre + c, q);
        }
    }

    private void collect(Node x, String pre, String s, Queue<String> q) {   
        if (x == null) {  // no more characters in s
            return;
        }
        int d = pre.length();  // number of characters in pre
        if (d == s.length() && x.mark) {  // end of s
            q.put(pre); // x is marked so pre is a word stored in the trie
        }
        if (d == s.length()) {  // end of s
            return;
        }
        char c = s.charAt(d);   // next character
        if (c == '.' || c == '?') { // wildcard
            for (int j = 0; j < R; j++) {  // iterate over all possible characters
                char c1 = (char) ('a' + j);  // next character
                collect(x.next[j], pre + c1, s, q);  // collect all the words with prefix pre + c1
            }
        } else {
            int j = (int) c - 'a';  // index of next character
            collect(x.next[j], pre + c, s, q); // collect all the words with prefix pre + c
        }
    }

    // collect all the words with prefix s stored in the trie
    public Iterable<String> wordsWithPrefix(String s) {
        Queue<String> q = new Queue<String>(N); // stores the words of the trie together with their number of occurrences
        // find the node corresponding to s
        Node x = contains(root, s, 0);
        // collect the words with prefix s
        collect(x, s, q);
        // return the words with prefix s
        return q;
    }

    // collect all the words that match s stored in the trie
    public Iterable<String> wordsThatMatch(String s) {
        Queue<String> q = new Queue<String>(N); // stores the words of the trie together with their number of occurrences
        // collect the words that match s
        collect(root, "", s, q);
        // return the words that match s
        return q;
    }

    // find the word stored in the trie that is the longest prefix of s 
    public String longestPrefixOf(String s) {
        int length = 0; // length of longest prefix
        // find the length of the longest prefix
        Node x = root;
        int i = 0;
        while (i < s.length() && x != null) { // s is not exhausted and x is not null
            char c = s.charAt(i);  // next character
            int j = (int) c - 'a'; // index of next character
            x = x.next[j];       // go to next node
            if (x != null && x.mark) { // x is marked so it is a word
                length = i + 1; // update length
            }
            i++; // go to next character
        }
        return s.substring(0, length); // return the longest prefix
    }
    
    // return the most frequent word
    public Item mostFrequent() {
        Item I = new Item();
        mostFrequent(root, "", I);
        return I;
    }
   
    private void mostFrequent(Node x, String pre, Item I) {
        if (x == null) {  // no more characters in s
            return;
        }
        if (x.mark && x.count > I.count) {  // end of s
            I.s = pre;
            I.count = x.count;
        }
        for (int j = 0; j < R; j++) {  // iterate over all possible characters
            char c = (char) ('a' + j);  // next character
            mostFrequent(x.next[j], pre + c, I);  // collect all the words with prefix pre + c1
        }
    }

    // return number of words stored in the trie
    public int size() {
        return N;
    }

    // method to count the number of occurrences of a word
    public int count(String s) {
        Node x = contains(root, s, 0);
        if (x == null) {
            return 0;
        }
        return x.count;
    }

    // print all the words stored in the trie
    public void printWords(Iterable<String> words) {
        for (String s : words) {
            System.out.println(s + " " + count(s));
        }
    }

    public static void main(String[] args) {
        System.out.println("Test String Trie");

        StringTrie T = new StringTrie();

        In.init();
        long startTime = System.currentTimeMillis();
        while (!In.empty()) {
            String s = In.getString();
            T.insert(s);
        }
        System.out.println("" + T.size() + " words");
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("construction time = " + totalTime);
        System.out.println("memory KB = " + (double) (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024);
        //T.printWords(T.words());
        
        String s = "astonished";
        System.out.println("contains " + s + " = " + T.count(s));
        
        s = "carol";
        System.out.println("contains " + s + " = " + T.count(s));
                
        s = "pigeon";
        System.out.println("contains " + s + " = " + T.count(s));
        
        s = "wondered";
        System.out.println("contains " + s + " = " + T.count(s));
      
        s = "governmental";
        System.out.println("longest prefix of " + s + " = " + T.longestPrefixOf(s));

        s = "caro";
        System.out.println("words with prefix " + s + " : ");
        T.printWords(T.wordsWithPrefix(s));
        
        s = "sc????e";
        System.out.println("words that match " + s + " : ");
        T.printWords(T.wordsThatMatch(s));

        Item I = T.mostFrequent();
        System.out.println("most frequent word = " + I.s + " " + I.count);
        
        T.delete("carol");
        T.delete("carouse");
        s = "caro";
        System.out.println("words with prefix " + s + " : ");
        T.printWords(T.wordsWithPrefix(s));
    }
}
