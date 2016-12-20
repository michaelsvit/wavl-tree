/**
 * WAVLTree
 * <p>
 * An implementation of a WAVL Tree with
 * distinct integer keys and info
 */

public class WAVLTree {

    public WAVLNode root;
    private final WAVLNode externalLeaf; // assigned to be the bottom node of every route in the tree
    private WAVLNode min; // node with minimum key in the tree
    private WAVLNode max; // node with maximum key in the tree
    private int size; // number of nodes in the tree

    public WAVLTree() {
        this.root = null;
        this.externalLeaf = new WAVLNode();
        this.min = null;
        this.max = null;
        this.size = 0;
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
        WAVLNode searchResult;
        if (!empty()) {
            searchResult = searchRecursive(root, k);
            if (searchResult.key == k) {
                return -1; // key already exists in the tree
            }
        } else {
            root = new WAVLNode(null, externalLeaf, externalLeaf, k, i);

            updateClassMembersInsert(root);

            return 0;
        }

        WAVLNode newNode = new WAVLNode(searchResult, externalLeaf, externalLeaf, k, i);
        // Insert newNode into the tree as the left or right child of searchResult
        if (k < searchResult.key) {
            searchResult.left = newNode;
        } else {
            searchResult.right = newNode;
        }

        updateClassMembersInsert(newNode);

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
        if (empty()) {
            return -1;
        }

        WAVLNode searchResult = searchRecursive(root, k);
        if (searchResult.key != k) {
            return -1;
        }

        updateClassMembersDelete(searchResult);
        // Eliminate root case
        if (searchResult == root && root.isALeaf()) {
            root = null;
            return 0;
        }

        if (searchResult.isInnerNode()) {
            searchResult = switchWithPredecessor(searchResult);
        }

        deleteNode(searchResult);

        WAVLNode parent = searchResult.parent;
        if (parent.isALeaf() && parent.getLeftChildRankDiff() == 2 && parent.getRightChildRankDiff() == 2) {
            // This is a 2-2 leaf
            parent.demote();
            if (parent != root) {
                return 1 + rebalanceDeleteRecursive(parent.parent);
            } else {
                return 1;
            }
        }
        return rebalanceDeleteRecursive(parent);
    }

    private void deleteNode(WAVLNode node) {
        if (node.isALeaf()) {
            swapNodes(node, externalLeaf);
        } else {
            // Unary node
            swapNodes(node, node.getChildWithRankDiff(1));
        }
    }

    private int rebalanceDeleteRecursive(WAVLNode node) {
        int rebalanceCase = checkCaseDelete(node);

        switch (rebalanceCase) {
            case 0:
                return 0; // no rebalancing is needed
            case 1:
                node.demote();
                return 1 + rebalanceDeleteRecursive(node.parent);
            case 2:
                node.getChildWithRankDiff(1).demote();
                node.demote();
                return 2 + rebalanceDeleteRecursive(node.parent);
            case 3:
                WAVLNode diffOneChild3 = node.getChildWithRankDiff(1);
                rotate(node, diffOneChild3);

                node.demote();
                diffOneChild3.promote();
                if (node.isALeaf() && node.getLeftChildRankDiff() == 2 && node.getRightChildRankDiff() == 2) {
                    node.demote();
                    return 4;
                } else {
                    return 3;
                }
            case 4:
                WAVLNode diffOneChild4 = node.getChildWithRankDiff(1);
                WAVLNode diffOneGrandChild = diffOneChild4.getChildWithRankDiff(1);

                doubleRotate(node, diffOneChild4, diffOneGrandChild);

                node.demote();
                node.demote();
                diffOneChild4.demote();
                diffOneGrandChild.promote();
                diffOneGrandChild.promote();
                return 7;
        }
        return 0; // unreachable code
    }

    private int checkCaseDelete(WAVLNode node) {
        if (node == null) {
            return 0; // we reached the root of the tree, no rebalancing needed
        } else if (!node.hasChildWithRankDiff(3)) {
            return 0; // no rebalancing is needed
        }

        if (node.hasChildWithRankDiff(2)) {
            // Case 1
            return 1;
        }

        WAVLNode diffOneChild = node.getChildWithRankDiff(1);
        int leftDiff = diffOneChild.getLeftChildRankDiff(); // difference from diffOneChild
        int rightDiff = diffOneChild.getRightChildRankDiff(); // difference from diffOneChild

        if (leftDiff == 2 && rightDiff == 2) {
            return 2;
        }

        if (!diffOneChild.isLeftChild()) {
            if (leftDiff == 1 && rightDiff == 2) {
                return 4;
            } else {
                return 3;
            }
        } else {
            if (rightDiff == 1 && leftDiff == 2) {
                return 4;
            } else {
                return 3;
            }
        }
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

        return min.info;
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

        return max.info;
    }

    /**
     * public int[] keysToArray()
     * <p>
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     */
    public int[] keysToArray() {
        int[] arr = new int[size];
        if (root != null) {
            keysToArrayRecursive(arr, 0, root);
        }
        return arr;
    }

    /**
     * public String[] infoToArray()
     * <p>
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     */
    public String[] infoToArray() {
        String[] arr = new String[size];
        if (root != null) {
            infoToArrayRecursive(arr, 0, root);
        }
        return arr;
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
        return size;
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

        if (rebalanceCase == 0) {
            return 0; // no rebalancing needed
        }

        while (rebalanceCase == 1) {
            node.promote();
            node = node.parent; // this code is reached iff node != null
            rebalanceCase = checkCaseInsert(node);
            operationCount++;
        }

        switch (rebalanceCase) {
            case 0:
                return operationCount; // no rebalancing needed
            case 2:
                WAVLNode child = node.getChildWithRankDiff(0);
                node.demote();
                rotate(node, child);
                operationCount += 2;
                break;
            case 3:
                // Fix ranks
                WAVLNode middleNode = node.getChildWithRankDiff(0);
                WAVLNode bottomNode = middleNode.getChildWithRankDiff(1);
                node.demote();
                middleNode.demote();
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
        boolean hasZeroDiffChild = (zeroDiffChild != null); // defined for consistency in code
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
            if (node.left == zeroDiffChild) {
                //TODO: check why zeroDiffChild can be null
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
     * Swap pointers to node1 with pointers to node2 and update node2's parent.
     *
     * @param node1 node to be swapped
     * @param node2 node to swap to
     */
    private void swapNodes(WAVLNode node1, WAVLNode node2) {
        // Swap parent's child pointer
        if (node1.isLeftChild()) {
            node1.parent.left = node2;
        } else {
            node1.parent.right = node2;
        }

        if (node2 != externalLeaf) {
            // Swap node2's parent pointer
            node2.parent = node1.parent;
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
            if (root.left != externalLeaf) {
                return searchRecursive(root.left, key);
            } else {
                return root;
            }
        } else if (key > root.key) {
            if (root.right != externalLeaf) {
                return searchRecursive(root.right, key);
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

        if (node2.isLeftChild()) {
            rotateRight(node1, node2);
        } else {
            rotateLeft(node1, node2);
        }

        // Fix parent pointers
        node1.parent = node2;
        if (node2 != externalLeaf) {
            node2.parent = node1Parent;
        }

        // If not at the tree's root, fix node1's child pointer
        if (node1Parent != null) {
            if (node1Parent.left == node1) {
                node1Parent.left = node2;
            } else {
                node1Parent.right = node2;
            }
        }

        // If node1 was the tree root, update root pointer
        if (root == node1) {
            root = node2;
        }
    }

    /**
     * Performs a left-rotation on the subtree around the edge connecting node1 and node2.
     *
     * @param node1 parent node to rotate around
     * @param node2 child node that would become parent
     */
    private void rotateLeft(WAVLNode node1, WAVLNode node2) {
        WAVLNode node2LeftChild = node2.left; // temporarily save so it's not lost on rotation

        // Reassign pointers
        node2.left = node1;
        node1.right = node2LeftChild;
        node2LeftChild.parent = node1;
    }

    /**
     * Performs a right-rotation on the subtree around the edge connecting node1 and node2.
     *
     * @param node1 parent node to rotate around
     * @param node2 child node that would become parent
     */
    private void rotateRight(WAVLNode node1, WAVLNode node2) {
        WAVLNode node2RightChild = node2.right; // temporarily save so it's not lost on rotation

        // Reassign pointers
        node2.right = node1;
        node1.left = node2RightChild;
        node2RightChild.parent = node1;
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
        rotate(node1, node3);
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
        WAVLNode predecessor = node.left;
        while (predecessor.right != externalLeaf) {
            predecessor = predecessor.right;
        }
        return predecessor;
    }

    /**
     * Updates tree minimum and maximum pointers if needed, and increases tree size by 1.
     *
     * @param newNode the newly inserted node
     */
    private void updateClassMembersInsert(WAVLNode newNode) {
        if (newNode == root) {
            // Update both tree minimum and maximum to the new root
            min = root;
            max = root;
        } else {
            // Check if tree maximum or minimum need to be updated
            if (newNode.key < min.key) {
                min = newNode;
            }
            if (newNode.key > max.key) {
                max = newNode;
            }
        }

        // Update tree size
        size++;
    }

    /**
     * Updates tree minimum and maximum pointers if needed, and decreases tree size by 1.
     *
     * @param searchResult node to be deleted
     */
    private void updateClassMembersDelete(WAVLNode searchResult) {
        // Check if tree maximum or minimum need to be updated
        if (searchResult == min) {
            if (min == root) {
                min = null;
            } else {
                min = min.parent;
            }
        }
        if (searchResult == max) {
            if (max == root) {
                max = null;
            } else {
                max = max.parent;
            }
        }

        // Update tree size
        size--;
    }

    /**
     * Recursively inserts tree keys into an array in sorted order.
     *
     * @param arr          array to which the keys are inserted
     * @param keysInserted number of keys already inserted into the array
     * @param node         root of current sub-tree
     * @return             number of keys that were inserted to the array in current sub-tree
     */
    private int keysToArrayRecursive(int[] arr, int keysInserted, WAVLNode node) {
        // Insert left sub-tree to the array in order
        int leftSubTreeSize = 0;
        if (node.left != externalLeaf) {
            leftSubTreeSize = keysToArrayRecursive(arr, keysInserted, node.left);
        }

        // Insert current node to the array
        arr[keysInserted + leftSubTreeSize] = node.key;

        // Insert right sub-tree to the array in order
        int rightSubTreeSize = 0;
        if (node.right != externalLeaf) {
            rightSubTreeSize = keysToArrayRecursive(arr, keysInserted + leftSubTreeSize + 1, node.right);
        }

        return leftSubTreeSize + 1 + rightSubTreeSize;
    }

    /**
     * Recursively inserts tree info into an array in sorted order (by keys).
     *
     * @param arr             array to which the info is inserted
     * @param stringsInserted number of info strings that were already inserted into the array
     * @param node            root of current sub-tree
     * @return                number of info strings that were inserted to the array in current sub-tree
     */
    private int infoToArrayRecursive(String[] arr, int stringsInserted, WAVLNode node) {
        // Insert left sub-tree to the array in order
        int leftSubTreeSize = 0;
        if (node.left != externalLeaf) {
            leftSubTreeSize = infoToArrayRecursive(arr, stringsInserted, node.left);
        }

        // Insert current node to the array
        arr[stringsInserted + leftSubTreeSize] = node.info;

        // Insert right sub-tree to the array in order
        int rightSubTreeSize = 0;
        if (node.right != externalLeaf) {
            rightSubTreeSize = infoToArrayRecursive(arr, stringsInserted + leftSubTreeSize + 1, node.right);
        }

        return leftSubTreeSize + 1 + rightSubTreeSize;
    }

    /**
     * A single tree-node that holds given String info
     */
    public class WAVLNode {

        public WAVLNode parent;
        public WAVLNode left;
        public WAVLNode right;
        public int key;
        public String info;
        public int rank;

        private WAVLNode(WAVLNode parent, WAVLNode right, WAVLNode left, int key, String info) {
            this.parent = parent;
            this.right = right;
            this.left = left;
            this.key = key;
            this.info = info;
            this.rank = 0;
        }

        /**
         * External leaf constructor
         */
        private WAVLNode() {
            this.parent = null;
            this.right = null;
            this.left = null;
            this.key = -1;
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
            if (left.rank == this.rank - rankDiff) {
                return left;
            } else if (right.rank == this.rank - rankDiff) {
                return right;
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
            return this.rank - left.rank;
        }

        /**
         * Gets the rank difference between this node and its right child.
         *
         * @return difference in rank
         */
        private int getRightChildRankDiff() {
            return this.rank - right.rank;
        }

        /**
         * Checks whether this node has no children that are not an external leaf.
         *
         * @return true iff this node is a leaf.
         */
        private boolean isALeaf() {
            return right == externalLeaf
                    && left == externalLeaf;
        }

        /**
         * Checks whether this node is the left child of its parent.
         *
         * @return true iff this is the left child of parent
         */
        private boolean isLeftChild() {
            return parent.left == this;
        }

        private boolean isInnerNode() {
            return left != externalLeaf && right != externalLeaf;
        }
    }
}
  
	