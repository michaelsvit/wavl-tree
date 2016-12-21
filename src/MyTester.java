import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Michael on 12/20/2016.
 */
public class MyTester {
    private WAVLTree tree;

    public static void main(String args[]) {
        MyTester tester = new MyTester();
        tester.run();
    }

    private void run() {
        int[] keysArr1 = {50, 30, 20, 10, 25, 23, 27, 5, 60, 28};
        tree = new WAVLTree();
        for (int key : keysArr1) {
            tree.insert(key, String.valueOf(key));
        }
        printTree();

        testSearch(keysArr1);


        int[] sortedKeysArr = Arrays.copyOf(keysArr1, keysArr1.length);
        Arrays.sort(sortedKeysArr);

        testKeysToArray(sortedKeysArr);
        testInfoToArray(sortedKeysArr);

        // Test delete
        int[] keysToDelete1 = {5, 10, 23 /*case 3*/, 60 /*case 4*/};
        for (int key : keysToDelete1) {
            tree.delete(key);
        }

        // Insert more keys
        int[] keysArr2 = {80, 40, 65 /*case 2*/};
        for (int key : keysArr2) {
            tree.insert(key, String.valueOf(key));
        }

        // Delete more keys
        int[] keysToDelete2 = {65, 40, 28, 20 /*case 2*/};
        for (int key : keysToDelete2) {
            tree.delete(key);
        }
        printTree();

        System.out.print("Done");
    }

    private void testInfoToArray(int[] sortedKeysArr) {
        // Test infoToArray
        String[] sortedInfoArray = new String[sortedKeysArr.length];
        for (int i = 0; i < sortedInfoArray.length; i++) {
            sortedInfoArray[i] = String.valueOf(sortedKeysArr[i]);
        }
        String[] infoToArrayResult = tree.infoToArray();
        if (!Arrays.equals(sortedInfoArray, infoToArrayResult)) {
            System.out.print("Error with infoToArray");
        }
    }

    private void testKeysToArray(int[] sortedKeysArr) {
        // Test keysToArray
        int[] keysToArrResult = tree.keysToArray();
        if (!Arrays.equals(sortedKeysArr, keysToArrResult)) {
            System.out.print("Error with keysToArray");
        }
    }

    private void testSearch(int[] keysArr) {
        // Test searching for keys
        for (int key : keysArr) {
            if (!tree.search(key).equals(String.valueOf(key))) {
                System.out.print("Error searching for key " + key);
            }
        }
    }

    public void printTree(){
        TreePrint printer=new TreePrint();
        printer.printNode(tree.root);
    }

    class TreePrint {

        public <T extends Comparable<?>> void printNode(
                WAVLTree.WAVLNode root) {
            int maxLevel = maxLevel(root);

            printNodeInternal(Collections.singletonList(root), 1, maxLevel);
        }

        private <T extends Comparable<?>> void printNodeInternal(
                List<WAVLTree.WAVLNode> list, int level, int maxLevel) {
            if (list.isEmpty() || isAllElementsNull(list))
                return;

            int floor = maxLevel - level;
            int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
            int firstSpaces = (int) Math.pow(2, (floor)) - 1;
            int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

            printWhitespaces(firstSpaces);

            List<WAVLTree.WAVLNode> newNodes = new ArrayList<WAVLTree.WAVLNode>();
            for (WAVLTree.WAVLNode node : list) {
                if (node != null) {
                    System.out.print(node.key + " " + node.rank);
                    newNodes.add(node.left);
                    newNodes.add(node.right);
                } else {
                    newNodes.add(null);
                    newNodes.add(null);
                    System.out.print(" ");
                }

                printWhitespaces(betweenSpaces);
            }
            System.out.println("");

            for (int i = 1; i <= endgeLines; i++) {
                for (int j = 0; j < list.size(); j++) {
                    printWhitespaces(firstSpaces - i);
                    if (list.get(j) == null) {
                        printWhitespaces(endgeLines + endgeLines + i
                                + 1);
                        continue;
                    }

                    if (list.get(j).left != null)
                        System.out.print("/");
                    else
                        printWhitespaces(1);

                    printWhitespaces(i + i - 1);

                    if (list.get(j).right != null)
                        System.out.print("\\");
                    else
                        printWhitespaces(1);

                    printWhitespaces(endgeLines + endgeLines - i);
                }

                System.out.println("");
            }

            printNodeInternal(newNodes, level + 1, maxLevel);
        }

        private void printWhitespaces(int count) {
            for (int i = 0; i < count; i++)
                System.out.print(" ");
        }

        private <T extends Comparable<?>> int maxLevel(WAVLTree.WAVLNode root) {
            if (root == null)
                return 0;

            return Math.max(maxLevel(root.left),
                    maxLevel(root.right)) + 1;
        }

        private <T> boolean isAllElementsNull(List<T> list) {
            for (Object object : list) {
                if (object != null)
                    return false;
            }

            return true;
        }

    }
}
