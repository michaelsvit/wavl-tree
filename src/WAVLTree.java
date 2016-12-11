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
        if (result.key == k) {
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
        WAVLNode searchResult = null;
        if (!empty()) {
            searchResult = searchRecursive(root, k);
            if (searchResult.key == k) {
                return -1; // key already exists in the tree
            }
        } else {
            root = new WAVLNode(null, externalLeaf, externalLeaf, k, i);
            return 0;
        }

        WAVLNode newNode = new WAVLNode(searchResult, externalLeaf, externalLeaf, k, i);
        // Insert newNode into the tree as the left or right child of searchResult
        if (k < searchResult.key) {
            searchResult.leftChild = newNode;
        } else {
            searchResult.rightChild = newNode;
        }

        return rebalanceInsert(searchResult);
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
     * Rebalances the tree after insertion by the WAVL algorithm rules
     *
     * @param node parent of the newly inserted node
     * @return     number of rebalancing operations
     *             (counting promote/demote/rotate as a single operation and double-rotate as two operations)
     */
    private int rebalanceInsert(WAVLNode node) {
        int operationCount = 0;
        int rebalanceCase = checkCaseInsert(node);

        switch (rebalanceCase) {
            case 0:
                return 0; // no rebalancing needed
            case 1:
                do {
                    node.promote();
                    node = node.parent; // this code is reached iff node.parent != null
                    rebalanceCase = checkCaseInsert(node);
                    operationCount++;
                } while (rebalanceCase == 1);
                break;
            case 2:
                node.demote();
                WAVLNode child = node.getChildWithRankDiff(0);
                rotate(node, child);
                operationCount += 2;
                break;
            case 3:
                // Fix ranks
                node.demote();
                WAVLNode middleNode = node.getChildWithRankDiff(0);
                middleNode.demote();
                WAVLNode bottomNode = middleNode.getChildWithRankDiff(1);
                bottomNode.promote();

                // Perform double rotation
                doubleRotate(node, middleNode, bottomNode);

                operationCount += 5;
                break;
        }
        return operationCount;
    }

    /**
     * Checks which case of rebalancing is needed to fix the sub-tree starting at the given node.
     *
     * @param node root of the given sub-tree
     * @return     which case was found
     */
    private int checkCaseInsert(WAVLNode node) {
        if (node == null) {
            // We have reached the root of the whole tree, no rebalancing is needed
            return 0;
        }

        // Check what rank differences the child nodes have from node parameter
        WAVLNode zeroDiffChild = node.getChildWithRankDiff(0); // saved for cases 2,3 to avoid retrieving again
        boolean hasZeroDiffChild = zeroDiffChild != null; // defined for consistency in code
        if (!hasZeroDiffChild) {
            // No rebalancing is needed
            return 0;
        }
        boolean hasOneDiffChild = node.hasChildWithRankDiff(1);

        if (hasOneDiffChild) {
            // Case 1
            return 1;
        } else {
            // Node has a child with 0 rank diff, and doesn't have a child with 1 rank diff,
            // so it must have a child with 2 rank diff

            // Case 2 or 3
            // Check which direction of case 2 or 3 it is
            if (zeroDiffChild == node.leftChild) {
                if (zeroDiffChild.getLeftChildRankDiff() == 1) {
                    // Case 2
                    return 2;
                } else {
                    // Case 3
                    return 3;
                }
            } else {
                if (zeroDiffChild.getRightChildRankDiff() == 1) {
                    // Case 2
                    return 2;
                } else {
                    // Case 3
                    return 3;
                }
            }
        }
    }

    /**
     * Searches recursively for a node with the given key.
     *
     * Precondition: root is not null
     * @param root root of the current subtree
     * @param key  key of the node to look for
     * @return     node with the specified key, or the last node that was reached if key was not found
     */
    private WAVLNode searchRecursive(WAVLNode root, int key) {
         if (key == root.key) {
            return root;
        } else if (key < root.key) {
             if (root.leftChild != null) {
                 return searchRecursive(root.leftChild, key);
             } else {
                 return root;
             }
         } else if (key > root.key) {
             if (root.rightChild != null) {
                 return searchRecursive(root.rightChild, key);
             } else {
                 return root;
             }
         }
        return null; // unreachable code
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
    private class WAVLNode {

        private WAVLNode parent;
        private WAVLNode leftChild;
        private WAVLNode rightChild;
        private int key;
        private String info;
        private int rank;

        private WAVLNode(WAVLNode parent, WAVLNode rightChild, WAVLNode leftChild, int key, String info) {
            this.parent = parent;
            this.rightChild = rightChild;
            this.leftChild = leftChild;
            this.key = key;
            this.info = info;
            this.rank = 0;
        }

        /**
         * External leaf constructor
         */
        private WAVLNode() {
            this.parent = null;
            this.rightChild = null;
            this.leftChild = null;
            this.key = Integer.MAX_VALUE;
            this.info = null;
            this.rank = -1;
        }

        /**
         * Increases current node's rank by 1.
         */
        private void promote() {
            rank++;
        }

        /**
         * Decreases current node's rank by 1.
         */
        private void demote() {
            rank--;
        }

        /**
         * Returns the child node with the specified rank difference, if one exists.
         *
         * @param rankDiff rank difference to look for in child nodes
         * @return         child node with a rank of this.rank-rankDiff
         */
        private WAVLNode getChildWithRankDiff(int rankDiff) {
            if (leftChild.rank ==  this.rank - rankDiff) {
                return leftChild;
            } else if (rightChild.rank == this.rank - rankDiff) {
                return rightChild;
            }
            return null; // the requested child was not found
        }

        /**
         * Returns whether a child node with the specified rank difference exists.
         *
         * @param rankDiff rank difference to look for in child nodes
         * @return         true iff a child node with the specified rank difference was found
         */
        private boolean hasChildWithRankDiff(int rankDiff) {
            return getChildWithRankDiff(rankDiff) != null;
        }

        /**
         * Gets the rank difference between this node and its left child.
         *
         * @return difference in rank
         */
        private int getLeftChildRankDiff() {
            return this.rank - leftChild.rank;
        }

        /**
         * Gets the rank difference between this node and its right child.
         *
         * @return difference in rank
         */
        private int getRightChildRankDiff() {
            return this.rank - rightChild.rank;
        }
    }


}
  
	