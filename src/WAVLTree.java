/**
 * WAVLTree
 * <p>
 * An implementation of a WAVL Tree with
 * distinct integer keys and info
 */

public class WAVLTree {

    private WAVLNode root;
    private final WAVLNode externalLeaf; // assigned to be the bottom node of every route in the tree

    public WAVLTree() {
        this.root = null;
        this.externalLeaf = new WAVLNode();
    }

    /**
     * public boolean empty()
     * <p>
     * returns true if and only if the tree is empty
     */
    public boolean empty() {
        return this.root == null;
    }

    /**
     * public String search(int k)
     * <p>
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     */
    public String search(int k) {
        if (empty()) {
            return null;
        }

        WAVLNode result = searchRecursive(root, k);
        if (result != null) {
            return result.info;
        } else {
            return null;
        }
    }

    /**
     * public int insert(int k, String i)
     * <p>
     * inserts an item with key k and info i to the WAVL tree.
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
     * returns -1 if an item with key k already exists in the tree.
     */
    public int insert(int k, String i) {
        return 42;    // to be replaced by student code
    }

    /**
     * public int delete(int k)
     * <p>
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
     * returns -1 if an item with key k was not found in the tree.
     */
    public int delete(int k) {
        return 42;    // to be replaced by student code
    }

    /**
     * public String min()
     * <p>
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     */
    public String min() {
        if (empty()) {
            return null;
        }

        WAVLNode iterator = root;
        while (iterator.leftChild != null) {
            iterator = iterator.leftChild;
        }
        return iterator.info;
    }

    /**
     * public String max()
     * <p>
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     */
    public String max() {
        if (empty()) {
            return null;
        }

        WAVLNode iterator = root;
        while (iterator.rightChild != null) {
            iterator = iterator.rightChild;
        }
        return iterator.info;
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        if (this.empty()) {
            return new int[0];
        }

        WAVLNode iter = root;


        int[] arr = new int[42]; // to be replaced by student code
        return arr;              // to be replaced by student code
    }

    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() {
        String[] arr = new String[42]; // to be replaced by student code
        return arr;                    // to be replaced by student code
    }

    /**
     * public int size()
     * <p>
     * Returns the number of nodes in the tree.
     * <p>
     * precondition: none
     * postcondition: none
     */
    public int size() {
        return 42; // to be replaced by student code
    }


    // ************************************* Helper functions *************************************************


    /**
     * Searches recursively for a node with the given key.
     *
     * @param root root of the current subtree
     * @param key  key of the node to look for
     * @return     node with the specified key, or null if key was not found
     */
    private WAVLNode searchRecursive(WAVLNode root, int key) {
        if (root == null) {
            return null;
        } else if (root.key == key) {
            return root;
        } else if (root.key < key) {
            return searchRecursive(root.leftChild, key);
        } else if (root.key > key) {
            return searchRecursive(root.rightChild, key);
        }
        return null;
    }

    /**
     * Rotates subtree around the edge connecting node1 and node2.
     * Assumes node1 is node2's parent.
     *
     * @param node1 parent node to rotate around
     * @param node2 child node that would become parent
     */
    private void rotate(WAVLNode node1, WAVLNode node2) {
        WAVLNode node1Parent = node1.parent; // temporarily save so it's not lost on rotation

        if (node2 == node1.leftChild) {
            rotateRight(node1, node2);
        } else {
            rotateLeft(node1, node2);
        }

        // Fix parent pointers
        node1.parent = node2;
        node2.parent = node1Parent;

        // If not at the tree's root, fix node1's child pointer
        if (node1Parent != null) {
            if (node1Parent.leftChild == node1) {
                node1Parent.leftChild = node2;
            } else {
                node1Parent.rightChild = node2;
            }
        }
    }

    /**
     * Performs a left-rotation on the subtree around the edge connecting node1 and node2.
     *
     * @param node1 parent node to rotate around
     * @param node2 child node that would become parent
     */
    private void rotateLeft(WAVLNode node1, WAVLNode node2) {
        WAVLNode node2LeftChild = node2.leftChild; // temporarily save so it's not lost on rotation

        // Reassign pointers
        node2.leftChild = node1;
        node1.rightChild = node2LeftChild;
    }

    /**
     * Performs a left-rotation on the subtree around the edge connecting node1 and node2.
     *
     * @param node1 parent node to rotate around
     * @param node2 child node that would become parent
     */
    private void rotateRight(WAVLNode node1, WAVLNode node2) {
        WAVLNode node2RightChild = node2.rightChild; // temporarily save so it's not lost on rotation

        // Reassign pointers
        node2.rightChild = node1;
        node1.leftChild = node2RightChild;
    }

    /**
     * Performs a double rotation, first on node2 and node3, and then on node1 and node2.
     *
     * @param node1 highest node
     * @param node2 middle node
     * @param node3 lowest node
     */
    private void doubleRotate(WAVLNode node1, WAVLNode node2, WAVLNode node3) {
        rotate(node2, node3);
        rotate(node1, node2);
    }

    /**
     * A single tree-node that holds given String info
     */
    public class WAVLNode {

        private WAVLNode parent;
        private WAVLNode leftChild;
        private WAVLNode rightChild;
        private int key;
        private String info;
        private int rankDiff; // difference in rank between current node and its parent

        private WAVLNode(WAVLNode parent, WAVLNode rightChild, WAVLNode leftChild, int key, String info) {
            this.parent = parent;
            this.rightChild = rightChild;
            this.leftChild = leftChild;
            this.key = key;
            this.info = info;
            this.rankDiff = 1;
        }

        private WAVLNode() {
            this.parent = null;
            this.rightChild = null;
            this.leftChild = null;
            this.key = -1;
            this.info = null;
            this.rankDiff = 1;
        }

        /**
         * Increases current node's rank by 1 by decreasing rank difference to parent's rank.
         */
        public void promote() {
            rankDiff--;
        }

        /**
         * Decreases current node's rank by 1 by increasing rank difference to parent's rank.
         */
        public void demote() {
            rankDiff++;
        }
    }


}
  
	