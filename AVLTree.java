/**
 *
 */
public class AVLTree {
    public BaseNode getmRoot() {
        return mRoot;
    }

    private BaseNode mRoot;

    /**
     * 插入
     *
     * @param root 需要和Node进行比较的根节点
     * @param node 需要插入的节点
     * @return
     */
    private BaseNode insertNode(BaseNode root, BaseNode node) {
        if (root != null) {
            /*
             * 如果root不为空，那么就需要比较 root 和 node 的大小
             * 以便于知道 node下一步 是和 root 的左子树做比较 还是右子树比较
             * 这里我们举个例子，如果 node 应该插入 root 的左侧，是不是下一步应该 root.left和node 做比较
             * 而如果 root.left 如果是null，那么 是不是 root.left = node，也就是把node赋值给 root.left
             */

            //比较 node 和 root 中 key 的值，这里的返回值是我们 实现接口Comparable，去做的，参考 AVLNode.AVLComparable
            int cmp = node.key.compareTo(root.key);

            //cmp < 0,说明root的key值小于node的key值，node 插入到 root 的左子树，也就是node 要和 root.left做比较
            if (cmp < 0) {
                /*
                 * 这里就开始使用递归了，同时需要看到有赋值的动作
                 * 这里需要好好理解一下，递归的核心思想是往能终结递归运算的方向进行，那我们想，如何才能结束
                 * 只有当root 为null 时，这时候递归才算走到终结方向。
                 */
                root.left = insertNode(root.left, node);
                /*
                 * ***********************   重要   ***************************
                 * 这里插入完成了，然后我们就看是否打破的平衡
                 * 因为是插入到左子树所以使用 height(root.left) - height(root.right)
                 * 如果等于2，那当前root 也就是最小不平衡子树。需要将 root节点进行旋转。然后再赋值给root
                 * 另外，（左子树 - 右子树 = 2） 的情况有两种。
                 * 所以需要判断 root 的左子树K1,看看 K2插入到 K1的那个地方（root,k1,k2 看文章内解释）
                 * 这时候如果 （K1 的右孩子 - K1的左孩子 == 1）
                 * 说明K2插入到K1的右侧了，这时候 tree  K1  K2 三者符合 左右关系图，所以调用 left_right_Rotation(root)
                 *
                 * else 使用 left_left_Rotation(root);
                 * 一定要记得无论怎么旋转都要给 root赋值
                 * ***********************   重要   ***************************
                 */
                if (height(root.left) - height(root.right) == 2) {
                    //符合 左子树的右子树导致不平衡
                    if (height(root.left.right) - height(root.left.left) == 1) {
                        root = left_right_Rotation(root);
                    } else {
                        root = left_left_Rotation(root);
                    }
                }
            } else if (cmp > 0) {
                /*
                 * 与上面解释异曲同工
                 */
                root.right = insertNode(root.right, node);
                //右子树的插入导致不平衡
                if (height(root.right) - height(root.left) == 2) {
                    //右子树的左子树导致不平衡
                    if (height(root.right.left) - height(root.right.right) == 1) {
                        root = right_left_Rotation(root);
                    } else {
                        root = right_right_Rotation(root);
                    }
                }
            } else {
                //节点相同
            }
        } else {
            //如果root为空，则将node赋值给root
            root = node;
        }
        /*
         * 这一步很重要
         * 我们说节点插入完成后，高度就需要有个改变，那高度怎么计算，当然是左子树的高和右子树的高，取最大值，然后 + 1
         */
        root.height = max(height(root.left), height(root.right)) + 1;
        return root;
    }

    /**
     * 外部使用，因为无论如何插入，首先肯定要和 mRoot进行比较，同时mRoot也要随时等待被重新赋值
     *
     * @param node
     */
    public void insert(BaseNode node) {
        mRoot = insertNode(mRoot, node);
    }


    /**
     * 前序遍历
     *
     * @param node
     */
    public void preOrderTraverse(BaseNode node) {
        if (node == null) {
            return;
        }
        System.out.print(node.key.toString() + " ");

        preOrderTraverse(node.left);
        preOrderTraverse(node.right);
    }

    /**
     * 前序遍历，节点高度
     *
     * @param node
     */
    public void preOrderHeightTraverse(BaseNode node) {
        if (node == null) {
            return;
        }
        System.out.print(node.height + " ");

        preOrderHeightTraverse(node.left);
        preOrderHeightTraverse(node.right);
    }

    //*********************  旋转函数  *************************
    /**
     * 函数命名原则：节点虚拟展现样式
     *
     * 举例：tree节点
     *       且：K1 = tree.left;   K2 = K1.left;
     *       K2 导致 tree成为最小不平衡子树;
     *       虚拟展示样式符合，左左样式，即函数 left_left_rotation();
     *       函数内部进行 右旋 操作
     *
     * */


    /**
     * 节点虚拟状态 --- 即 tree节点，其左子树的左子树导致不平衡
     * <p>
     * 此时需要右旋
     *
     * @param tree
     * @return
     */
    private BaseNode left_left_Rotation(BaseNode tree) {
        BaseNode temp = tree.left;
        tree.left = temp.right;
        temp.right = tree;

        //左子树高 和 右子树高，取最大值，然后再加 1 ，得当前节点高度
        tree.height = max(height(tree.left), height(tree.right)) + 1;
        temp.height = max(height(temp.left), height(temp.right)) + 1;

        return temp;
    }


    /**
     * 节点虚拟状态 --- 即 tree节点，其右子树的右子树导致不平衡
     * <p>
     * 此时需要左旋
     *
     * @param tree
     * @return
     */
    private BaseNode right_right_Rotation(BaseNode tree) {
        BaseNode temp = tree.right;
        tree.right = temp.left;
        temp.left = tree;

        //左子树高 和 右子树高，取最大值，然后再加 1 ，得当前节点高度
        tree.height = max(height(tree.left), height(tree.right)) + 1;
        temp.height = max(height(temp.left), height(temp.right)) + 1;

        return temp;
    }


    /**
     * 节点虚拟状态 --- 即 tree节点，其左子树 K1 的右子树 K2 导致不平衡
     * <p>
     * 此时 K1 与 K2的虚拟状态符合 right_right；
     * 旋转后，tree 与 k1 k2 状态符合，left_left；
     *
     * @param tree
     * @return
     */
    private BaseNode left_right_Rotation(BaseNode tree) {
        tree.left = right_right_Rotation(tree.left);
        return left_left_Rotation(tree);
    }

    /**
     * 节点虚拟状态 --- 即 tree节点，其右子树 K1 的左子树 K2 导致不平衡
     * <p>
     * 此时 K1 与 K2的虚拟状态符合 left_left，；
     * 旋转后，tree 与 k1 k2 状态符合，right_right;
     *
     * @param tree
     * @return
     */
    private BaseNode right_left_Rotation(BaseNode tree) {
        tree.right = left_left_Rotation(tree.right);
        return right_right_Rotation(tree);
    }


    //**********************************************


    /**
     * 获得当前tree节点的高度
     *
     * @param tree
     * @return
     */
    private int height(BaseNode tree) {
        if (tree != null) {
            return tree.height;
        }
        return 0;
    }

    /**
     * 左右子树最大值
     *
     * @param a
     * @param b
     * @return
     */
    private int max(int a, int b) {
        return (a >= b) ? a : b;
    }


    static class BaseNode<T extends Comparable> {
        BaseNode left;
        BaseNode right;
        int height;
        T key;
    }


}



