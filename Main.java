public class Main {

    public static void main(String[] args) {
        int[] a = {3, 2, 1, 4, 5, 6, 7, 10, 9, 8};

        AVLTree avlTree = new AVLTree();

        for (int i = 0; i < a.length; i++) {
            avlTree.insert(new AVLNode(a[i]));
        }
        System.out.println("---------前序遍历，节点值----------");
        avlTree.preOrderTraverse(avlTree.getmRoot());
        System.out.println();
        System.out.println("---------前序遍历，节点高度----------");
        avlTree.preOrderHeightTraverse(avlTree.getmRoot());


    }
}
