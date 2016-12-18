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
        if (!empty()) {
            WAVLNode searchResult = searchRecursive(root, k);
            if (searchResult.key == k) {
                //check if node is unary/leaf/internal
                if (!(searchResult.isALeaf() || searchResult.isUnary())) {
                    searchResult = switchWithPredecessor(searchResult);
                }
                return handleStartDelete(searchResult);
            }
        }
        return -1;
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
     * @return number of rebalancing operations
     * (counting promote/demote/rotate as a single operation and double-rotate as two operations)
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
     * @return which case was found
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
     * rebalances the bottom sub-tree after deletion
     * and sends the problem up if needed
     *
     * @param node which we want to delete
     * @return number of rebalancing operations (counting promote/demote
     * /rotate as a single operation and double-rotate as two operations)
     */
    private int handleStartDelete(WAVLNode node) {
        int operationCount = 0;
        int startCase = checkStartCaseDelete(node);
        switch (startCase) {
            case 0:
                node = externalLeaf;
                return 0;
            case 1:
                node = externalLeaf;
                node.parent.demote();
                if (node.parent.parent.hasChildWithRankDiff(2)) {
                    return 1;
                } else {
                    operationCount = 1 + handleRebalanceDelete(node.parent); // complete infinite state
                }
            case 2:
                node = externalLeaf;
                operationCount = handleRebalanceDelete(node); // complete infinite state
            case 3:
                WAVLNode child = node.getChildWithRankDiff(1);
                node = child;
                return 0;
            case 4:
                WAVLNode child1 = node.getChildWithRankDiff(1);
                node = child1;
                operationCount = handleRebalanceDelete(node); // complete infinite state
        }
        return operationCount;
    }

    /**
     * checks if deleted node is leaf/unary node, and differs between the sub-tree cases.
     *
     * @param node which we want to delete
     * @return number that represents the case we need to deal with at the bottom sub-tree
     */
    private int checkStartCaseDelete(WAVLNode node) {
        WAVLNode parent = node.parent;
        if (node.isALeaf()) {
            // Check the rank differences the node's parent has from his children
            if (parent.getLeftChildRankDiff() == 1 && parent.getRightChildRankDiff() == 1) {
                // case 0 ,No rebalancing is needed
                return 0;
            } else {
                // separate between leaf deletion cases
                if (parent.getChildWithRankDiff(1) == node) {
                    // Case 1
                    return 1;
                } else
                    // Case 2
                    return 2;
            }
        }
        // if we got so far, the node must be an unary node
        else {
            // Check the rank differences the node's parent has from his children
            if (parent.getChildWithRankDiff(1) == node) {
                // case 3. combines to different cases ,No rebalancing is needed
                return 3;
            } else {
                // Case 4, the only case remains
                return 4;
            }
        }

    }

    /**
     * rebalances the whole tree after deletion with recursion, until the problem
     * comes to a finite state.
     *
     * @param node which we deleted
     * @return number of rebalancing operations (counting promote/demote
     * /rotate as a single operation and double-rotate as two operations)
     */
    private int handleRebalanceDelete(WAVLNode node) {
        int operationCount = 0;
        int rebalanceCase = checkRebalanceCaseDelete(node);
        switch (rebalanceCase) {
            case 0:
                return operationCount; //the tree is balanced
            case 1:
                node.parent.demote();
                operationCount = 1 + handleRebalanceDelete(node.parent);
            case 2:
                node.parent.demote();
                node.parent.getChildWithRankDiff(1).demote();
                operationCount = 2 + handleRebalanceDelete(node.parent);
            case 3:
                rotate(node.parent, node.parent.getChildWithRankDiff(1));
                //rank diff is 2 from both childs, node may be sentinal
                if (!node.parent.hasChildWithRankDiff(1)) {
                    node.parent.demote();
                    operationCount = 2;
                }
                operationCount = 1;
            case 4:
                WAVLNode brother = node.parent.getChildWithRankDiff(1);
                if (node.parent.leftChild == node) {
                    doubleRotate(node.parent, brother, brother.leftChild);
                } else {
                    doubleRotate(node.parent, brother, brother.rightChild);
                }
                operationCount = 2;
        }
        return operationCount;
    }


    /**
     * differs between rebalancing cases of internal nodes.
     *
     * @param node which we deleted
     * @return number that represents the rebalancing case we need to deal with
     */
    private int checkRebalanceCaseDelete(WAVLNode node) {
        WAVLNode parent = node.parent;
        if (!parent.hasChildWithRankDiff(3)) {
            return 0;
        } else {
            if (parent.hasChildWithRankDiff(2)) {
                return 1;
            } else {
                WAVLNode lowDiffChild = parent.getChildWithRankDiff(1);
                if (lowDiffChild.getLeftChildRankDiff() == 2 && lowDiffChild.getRightChildRankDiff() == 2) {
                    return 2;
                } else if (parent.leftChild == node && lowDiffChild.getRightChildRankDiff() == 1
                        || parent.rightChild == node && lowDiffChild.getLeftChildRankDiff() == 1) {
                    return 3;
                } else {
                    return 4;
                }
            }
        }

    }

    /**
     * Searches recursively for a node with the given key.
     * <p>
     * Precondition: root is not null
     *
     * @param root root of the current subtree
     * @param key  key of the node to look for
     * @return node with the specified key, or the last node that was reached if key was not found
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
     * perform an info and key switch between the given node and his predecessor.
     * returns the predecessor after the switch.
     *
     * @param node node we want to switch with his predecessor
     * @return the predecessor node with the given node's info and key
     */
    private WAVLNode switchWithPredecessor(WAVLNode node) {
        WAVLNode predecessor = findPredecessor(node);
        String tempInfo = node.info;
        int tempKey = node.key;
        node.info = predecessor.info;
        node.key = predecessor.key;
        predecessor.info = tempInfo;
        predecessor.key = tempKey;
        return predecessor;
    }

    private WAVLNode findPredecessor(WAVLNode node) {
        WAVLNode predecessor = node.leftChild;
        while (predecessor.rightChild != externalLeaf) {
            predecessor = predecessor.rightChild;
        }
        return predecessor;
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
         * @return child node with a rank of this.rank-rankDiff
         */
        private WAVLNode getChildWithRankDiff(int rankDiff) {
            if (leftChild.rank == this.rank - rankDiff) {
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
         * @return true iff a child node with the specified rank difference was found
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

        /**
         * Checks whether this node has no children that are not an external leaf.
         *
         * @return true iff this node is a leaf.
         */
        private boolean isALeaf() {
            return rightChild == externalLeaf
                    && leftChild == externalLeaf;
        }

        /**
         * Checks whether this node has exactly one child that is not an external leaf.
         *
         * @return true iff this node is a unary node.
         */
        private boolean isUnary() {
            return (rightChild == externalLeaf && leftChild != externalLeaf) ||
                    (rightChild != externalLeaf && leftChild == externalLeaf);
        }
    }
}
  
	