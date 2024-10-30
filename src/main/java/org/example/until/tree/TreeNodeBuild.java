package org.example.until.tree;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TreeNodeBuild {

    private List<TreeNode> nodeList;

    private String id;

    public TreeNodeBuild(List<TreeNode> nodeList, String id) {
        this.nodeList = nodeList;
        this.id = id;
    }

    public List<TreeNode> getRootNode() {
        return nodeList.stream().filter(p -> Objects.equals(id, p.getId())).collect(Collectors.toList());
    }

    public List<TreeNode> buildTree() {
        // 保存一个根节点构造出来的完整树结构
        List<TreeNode> treeNodes = new ArrayList<>();
        // getRootNode()所有根节点
        for (TreeNode treeRootNode : getRootNode()) {
            // 顶级节点构建树
            treeRootNode = buildChildTree(treeRootNode);
            // 完成一个根节点构建的树，添加到集合
            treeNodes.add(treeRootNode);
        }
        return treeNodes;
    }

    /**
     * 构建子树结构
     *
     * @param pNode 根节点
     * @return 整棵树
     */
    private TreeNode buildChildTree(TreeNode pNode) {
        List<TreeNode> childTree = new ArrayList<>();
        // 过滤下级数据
        List<TreeNode> nodeListTemp = nodeList.stream()
                .filter(p -> Objects.equals(p.getPId(), pNode.getId()))
                .sorted(Comparator.comparing(TreeNode::getSort))
                .collect(Collectors.toList());
        // 下级数据再次构建
        for (TreeNode treeNode : nodeListTemp) {
            // 递归判断当前节点情况，调用自身
            childTree.add(buildChildTree(treeNode));
        }
        pNode.setChild(childTree);
        return pNode;
    }

}
