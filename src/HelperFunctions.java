/**
 * Created by Michael on 12/3/2016.
 */
public class HelperFunctions {

    /**
     * Rotates subtree around the edge connecting parent and child.
     *
     * @param parent parent node to rotate around
     * @param child child node that would become parent
     */
    private void rotate(WAVLNode parent, WAVLNode child) {
        if (child == parent.leftChild) {
            rotateRight(parent, child);
        } else if (child == parent.rightChild) {
            rotateLeft(parent, child);
        }
    }

    private void rotateLeft(WAVLNode parent, WAVLNode child) {
        WAVLNode childLeftChild = child.leftChild;
        child.leftChild = parent;
        parent.rightChild = childLeftChild;
    }

    private void rotateRight(WAVLNode parent, WAVLNode child) {
        WAVLNode childRightChild = child.rightChild;
        child.rightChild = parent;
        parent.leftChild = childRightChild;
    }

    public class WAVLNode {
        private WAVLNode parent;
        private WAVLNode leftChild;
        private WAVLNode rightChild;
        private int key;
        private String info;
        private int rankDiff;
    }
}
