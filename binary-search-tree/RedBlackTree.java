
import java.io.*;
import java.util.Random;
import java.util.TreeMap;
public class RedBlackTree<Key extends Comparable<Key>, Item> {

    BSTreeNode root;        // root of binary search tree

    class BSTreeNode {

        Key key;            // key associated with the item stored at node
        Item item;          // item stored at node
        BSTreeNode left;    // left child
        BSTreeNode right;   // right child
        BSTreeNode parent;  // node's parent
        int height;         // node's height
        int N;              // number of descendants
		boolean isRed;

        // create new node
        BSTreeNode(Key key, Item item, BSTreeNode parent) {
            this.key = key;
            this.item = item;
            this.parent = parent;
            this.height = 1;
            this.N = 1;
			this.isRed = true;
        }
    }

    // search for item with key; returns the last node on the search path 
    BSTreeNode searchNode(Key key) {
        BSTreeNode v = root;
        BSTreeNode pv = null; // parent of v
        while (v != null) {
            int c = key.compareTo(v.key);  // compare with the key of node v
            pv = v;
            if (c < 0) {
                v = v.left;
            } else if (c > 0) {
                v = v.right;
            } else {
                return v; // item found; return node that contains it
            }
        }
        return pv; // item not found; return last node on the search path
    }

    // search for item with key
    public Item search(Key key) {
        if (root == null) {
            return null; // tree is empty
        }
        BSTreeNode v = searchNode(key);
        int c = key.compareTo(v.key);
        if (c == 0) {
            return v.item;    // item found
        } else {
            return null;      // item not found
        }
    }

    // return the height of a node x; if x is null return 0
    private int getHeight(BSTreeNode x) {
        if (x == null) {
            return 0;
        } else {
            return x.height;
        }
    }

    // return the number of descendants of a node x; if x is null return 0
    private int getN(BSTreeNode x) {
        if (x == null) {
            return 0;
        } else {
            return x.N;
        }
    }
    
    // update the height and the number of descendants of a node
    private void updateNode(BSTreeNode x) {
        int leftHeight = getHeight(x.left);
        int rightHeight = getHeight(x.right);
        int bf = leftHeight - rightHeight; // balance factor
        if (bf < 0) {
            x.height = rightHeight + 1;
        } else {
            x.height = leftHeight + 1;
        }
        
        int leftN = getN(x.left);
        int rightN = getN(x.right);
        x.N = leftN + rightN + 1;
    }
    
    // update the height v's ancestors
    private void updatePath(BSTreeNode v) {
        BSTreeNode u = v;
        while (u != null) {
            updateNode(u);
            u = u.parent;
        }
    }
    
    // return the height of the binary search tree
    int getTreeHeight() {
        return getHeight(root);
    }

    // insert item with key and return inserted node
    BSTreeNode insertNode(Key key, Item item) {
        if (root == null) { // tree is empty
            root = new BSTreeNode(key, item, null);
            return root;
        }

        BSTreeNode v = searchNode(key); // v is the last node on the search path
        int c = key.compareTo(v.key);
        if (c == 0) { // key already exists in v; overwrite node's item with new item
            v.item = item;
            return v;
        }

        BSTreeNode u = new BSTreeNode(key, item, v); // new node becomes child of v
        if (c < 0) {
            v.left = u;
        } else {
            v.right = u;
        }
        
        return u;
    }

    // insert item with key
    public void insert(Key key, Item item) {
        if (search(key) != null) {
            return; // key already exists
        }
        BSTreeNode v = insertNode(key, item);
		fix(v);
        updatePath(v);
    }

    private BSTreeNode successor(BSTreeNode v) {
        if (v.right != null) {
            return minimum(v.right);
        }
        BSTreeNode u = v.parent;
        while (u != null && v == u.right) {
            v = u;
            u = u.parent;
        }
        return u;
    }

    private BSTreeNode minimum(BSTreeNode right) {
        while (right.left != null) {
            right = right.left;
        }
        return right;
    }

    private void rotateLeft(BSTreeNode x) {
        BSTreeNode y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.left) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;
        updateNode(x);
        updateNode(y);
    }
	
	private void rotateRight(BSTreeNode y) {
        BSTreeNode x = y.left;
        y.left = x.right;
        if (x.right != null) {
            x.right.parent = y;
        }
        x.right = y;
        x.parent = y.parent;
        if (y.parent == null) {
            root = x;
        } else if (y.parent.left == y) {
            y.parent.left = x;
        } else {
            y.parent.right = x;
        }

        y.parent = x;
        updateNode(y);
        updateNode(x);
    }
	
	private void fix(BSTreeNode x) {
	    BSTreeNode y;
        while (x != root && x.parent.isRed) {
            if (x.parent == x.parent.parent.left) {
                y = x.parent.parent.right;
                if (y != null && y.isRed) {
                    x.parent.isRed = false;
                    y.isRed = false;
                    x.parent.parent.isRed = true;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.right) {
                        x = x.parent;
                        rotateLeft(x);
                    }
                    x.parent.isRed = false;
                    x.parent.parent.isRed = true;
                    rotateRight(x.parent.parent);
                }
            } else {
                y = x.parent.parent.left;
                if (y != null && y.isRed) {
                    x.parent.isRed = false;
                    y.isRed = false;
                    x.parent.parent.isRed = true;
                    x = x.parent.parent;
                } else {
                    if (x == x.parent.left) {
                        x = x.parent;
                        rotateRight(x);
                    }
                    x.parent.isRed = false;
                    x.parent.parent.isRed = true;
                    rotateLeft(x.parent.parent);
                }
            }
        }
        root.isRed = false;
    }

    // inorder traversal: prints the key of each node
    void printNode(BSTreeNode v, int level) {
        if (v == null) {
            return;
        }
        printNode(v.right, level + 1);
        for (int i = 0; i < level; i++) {
            System.out.print("\t");
        }
		String color;
		if(v.isRed){
			color = "red";
		}else{
			color = "black";
		}
        System.out.println("" + v.key + "(" + v.height + "," + v.N + ")" + "[" + color + "]");
        printNode(v.left, level + 1);
    }

    // print binary tree
    public void print() {
        System.out.println("Printing binary search tree");
        System.out.println("");
        printNode(root, 0);
        System.out.println("");
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Test Binary Search Tree");
        System.out.println("========================================");

        int n = Integer.parseInt(args[0]);
        System.out.println("Number of keys n = " + n);

        RedBlackTree T = new RedBlackTree<Integer, String>();

        Random rand = new Random(0);
        int[] keys = new int[n];
        for (int i = 0; i < n; i++) { // store n random numbers in [0,2n)
        	keys[i] = rand.nextInt(2*n);
        }

        long startTime = System.currentTimeMillis();

        System.out.println("========================================");
        System.out.println("Inserting " + n + " random numbers in [0,2n)");
        long startTime1 = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            String item = "item" + i;
            T.insert(keys[i], item);
        }
        long endTime1 = System.currentTimeMillis();
        long totalTime1 = endTime1 - startTime1;
        System.out.println("Insertion time = " + totalTime1);
        System.out.println("Tree height = " + T.getTreeHeight());

        System.out.println("Getting " + n + " random numbers in [0,2n)");
        long startTime2 = System.currentTimeMillis();
        int low = n/4;
        int high = 3*n/4;
        int count = 0;
        for (int i = low; i <= high; i++) {
            if (T.search(i) != null) {
                count++;
            }
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("Number of keys in [" + low + ", " + high + "] = " + count);
        long totalTime2 = endTime2 - startTime2;
        System.out.println("Key search time = " + totalTime2);

        // Print only for small trees
        if (n <= 16) {
            T.print();
        }

        // Print the number of keys in the range [n/4, 3n/4]
        low = n/4;
        high = 3*n/4;
        count = 0;
        for (int i = low; i <= high; i++) {
            if (T.search(i) != null) {
                count++;
            }
        }
        System.out.println("Number of keys in [" + low + ", " + high + "] = " + count);

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Search time = " + totalTime);
    }


}
