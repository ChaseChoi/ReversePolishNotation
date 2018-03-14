import java.util.concurrent.ThreadLocalRandom;

class TreeNode {
    String key;
    TreeNode left, right;

    public TreeNode(String value) {
        key = value;
        left = right = null;
    }
}

public class BinaryTree {
    private TreeNode root;
    private StringBuffer infixExpression = new StringBuffer();

    /**
     * Initialize an expression tree
     * @param numOfOperands indicate the number of operands
     */
    public BinaryTree(int numOfOperands) {
        buildTree(numOfOperands);
    }

    /**
     * This generate a random infix expression.
     * @return generated infix expression.
     */
    public String getRandomExpressions() {
        buildExpression(root);
        return infixExpression.toString();
    }

    /**
     * Convert the expression tree to a StringBuffer variable infixExpression
     * @param current indicates the current tree node
     */
    private void buildExpression(TreeNode current) {
        if (current.left == null && current.right == null) {
            infixExpression.append(String.format("%.2f", Double.parseDouble(current.key)));
            return;
        }
        infixExpression.append('(');
        buildExpression(current.left);
        infixExpression.append(current.key);
        buildExpression(current.right);
        infixExpression.append(')');
    }

    /**
     * This method generates a expression tree which consists of specific number of operands
     * @param numOfOperands indicate the number of the operands
     */
    private void buildTree(int numOfOperands) {
        if (numOfOperands == 1) {
            root = buildSubTree(1);
        } else {
            String operators = "+-*/";
            int randomIndex = ThreadLocalRandom.current().nextInt(0, 3);
            root = new TreeNode(""+operators.charAt(randomIndex));

            double result = numOfOperands/2.0;
            int left = (int) Math.ceil(result);
            int right = (int) Math.floor(result);

            root.left = buildSubTree(left);
            root.right = buildSubTree(right);
        }
    }

    /**
     * Use recursive method to generate subtree
     * @param numOfOperands indicates the number of operands
     * @return root node of a subtree
     */

    private TreeNode buildSubTree(int numOfOperands) {
        if (numOfOperands == 1) {
            double random = ThreadLocalRandom.current().nextDouble(-10, 10);
            return new TreeNode(Double.toString(random));
        }
        String operators = "+-*/";
        int randomIndex = ThreadLocalRandom.current().nextInt(0, 3);
        TreeNode operatorNode = new TreeNode(""+operators.charAt(randomIndex));
        double result = numOfOperands/2.0;
        int left = (int) Math.ceil(result);
        int right = (int) Math.floor(result);
        operatorNode.left = buildSubTree(left);
        operatorNode.right = buildSubTree(right);
        return operatorNode;
    }

}
