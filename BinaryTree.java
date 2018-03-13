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
    TreeNode root;
    StringBuffer infixExpression = new StringBuffer();

    public BinaryTree(int numOfOperands) {
        buildTree(numOfOperands);
    }

    public String getRandromExpression() {
        buildExpression(root);
        return infixExpression.toString();
    }

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


    private void buildTree(int numOfOperands) {
        String operators = "+-*/";
        int randomIndex = ThreadLocalRandom.current().nextInt(0, 3);
        root = new TreeNode(""+operators.charAt(randomIndex));

        double result = numOfOperands/2.0;
        int left = (int) Math.ceil(result);
        int right = (int) Math.floor(result);

        root.left = buildSubTree(left);
        root.right = buildSubTree(right);
    }

    /**
     * Use recursive method to generate subtree
     * @param numOfOperands
     * @return root node of a subtree
     */

    private TreeNode buildSubTree(int numOfOperands) {
        if (numOfOperands == 1) {
            double random = ThreadLocalRandom.current().nextDouble(-10, 10);
            TreeNode operandNode = new TreeNode(Double.toString(random));
            return operandNode;
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
