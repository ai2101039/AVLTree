public class AVLNode extends AVLTree.BaseNode<AVLNode.AVLComparable> {

    public AVLNode(int i) {
        key = new AVLComparable();
        key.value = i;
    }

    static class AVLComparable implements Comparable {
        int value;

        @Override
        public int compareTo(Object o) {
            AVLComparable temp = (AVLComparable) o;
            return value - temp.value;
        }

        @Override
        public String toString() {
            return value + "";
        }
    }
}
