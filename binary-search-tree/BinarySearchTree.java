
import java.io.*;
import java.util.Random;

public class BinarySearchTree<Key extends Comparable<Key>, Item> {

    BSTreeNode root;        // root of binary search tree

    class BSTreeNode {

        Key key;            // key associated with the item stored at node
        Item item;          // item stored at node
        BSTreeNode left;    // left child
        BSTreeNode right;   // right child
        BSTreeNode parent;  // node's parent
        int height;         // node's height
        int N;              // number of descendants

        // create new node
        BSTreeNode(Key key, Item item, BSTreeNode parent) {
            this.key = key;
            this.item = item;
            this.parent = parent;
            this.height = 1;
            this.N = 1;
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
        BSTreeNode v = insertNode(key, item);
        updatePath(v); 
    }

    // Rotate the subtree rooted at node x to the left
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

    // Rotate the subtree rooted at node x to the right
    private void rotateRight(BSTreeNode x) {
        BSTreeNode y = x.left;
        x.left = y.right;
        if (y.right != null) {
            y.right.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x == x.parent.right) {
            x.parent.right = y;
        } else {
            x.parent.left = y;
        }
        y.right = x;
        x.parent = y;
        updateNode(x);
        updateNode(y);
    }

    // rebalance the tree after insertion
    private void rebalance(BSTreeNode x) {
        while (x != null) {
            updateNode(x);
            int leftHeight = getHeight(x.left);
            int rightHeight = getHeight(x.right);
            int bf = leftHeight - rightHeight; // balance factor
            if (bf == 2) { // left subtree is too high
                int leftLeftHeight = getHeight(x.left.left);
                int leftRightHeight = getHeight(x.left.right);
                if (leftRightHeight > leftLeftHeight) {
                    rotateLeft(x.left);
                }
                rotateRight(x);
            } else if (bf == -2) { // right subtree is too high
                int rightLeftHeight = getHeight(x.right.left);
                int rightRightHeight = getHeight(x.right.right);
                if (rightLeftHeight > rightRightHeight) {
                    rotateRight(x.right);
                }
                rotateLeft(x);
            }
            x = x.parent;
        }
    }

    // delete node v from the tree and rebalance the tree
    private void deleteRebalance(BSTreeNode v) {
        BSTreeNode u = v.parent;
        if (u == null) { // v is the root
            root = null;
        } else {
            if (v == u.left) {
                u.left = null;
            } else {
                u.right = null;
            }
        }
        rebalance(u);
    }

    // delete node v from the tree
    private void deleteNode(BSTreeNode v) {
        if (v.left == null && v.right == null) { // v is a leaf
            deleteRebalance(v);
        } else if (v.left == null) { // v has only a right child
            v.right.parent = v.parent;
            if (v.parent == null) { // v is the root
                root = v.right;
            } else if (v == v.parent.left) {
                v.parent.left = v.right;
            } else {
                v.parent.right = v.right;
            }
            rebalance(v.right);
        } else if (v.right == null) { // v has only a left child
            v.left.parent = v.parent;
            if (v.parent == null) { // v is the root
                root = v.left;
            } else if (v == v.parent.left) {
                v.parent.left = v.left;
            } else {
                v.parent.right = v.left;
            }
            rebalance(v.left);
        } else { // v has two children
            BSTreeNode u = v.right;
            while (u.left != null) {
                u = u.left;
            }
            v.key = u.key;
            v.item = u.item;
            deleteNode(u);
        }
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
        System.out.println("" + v.key + "[" + v.height + "," + v.N + "]");
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
        System.out.println("Test Binary Search Tree");
        int n = Integer.parseInt(args[0]);
        System.out.println("number of keys n = " + n);

        BinarySearchTree T = new BinarySearchTree<Integer, String>();

        Random rand = new Random(0);
        int[] keys = new int[n];
        for (int i = 0; i < n; i++) { // store n random numbers in [0,2n)
        	keys[i] = rand.nextInt(2*n);
        }

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            String item = "item" + i;
            T.insert(keys[i], item);
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("construction time = " + totalTime);
        T.print();
        System.out.println("tree height = " + T.getTreeHeight());
        
        startTime = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            if (T.search(keys[i]) == null) {
                System.out.println("key " + keys[i] + " not found!");
            }
        }
        endTime = System.currentTimeMillis();
        totalTime = endTime - startTime;
        System.out.println("search time = " + totalTime);
    }
}
