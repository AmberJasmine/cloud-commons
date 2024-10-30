package org.example.until.tree;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class TreeTest {
    public static void main(String[] args) {
        List<TreeNode> treeNodes = new ArrayList<>();
        TreeNode treeNode1 = new TreeNode("1", "一级菜单1", "-1", 1, Lists.newArrayList());
        TreeNode treeNode1_1 = new TreeNode("1-1", "二级菜单1-1", "1", 1, Lists.newArrayList());
        TreeNode treeNode1_1_1 = new TreeNode("1-1-1", "三级菜单1-1-1", "1-1", 1, Lists.newArrayList());
        TreeNode treeNode1_1_2 = new TreeNode("1-1-2", "三级菜单1-1-2", "1-1", 2, Lists.newArrayList());
        TreeNode treeNode1_2 = new TreeNode("1-2", "二级菜单1-2", "1", 2, Lists.newArrayList());

        TreeNode treeNode2 = new TreeNode("2", "一级菜单2", "-1", 2, Lists.newArrayList());
        TreeNode treeNode3 = new TreeNode("3", "一级菜单3", "-1", 3, Lists.newArrayList());

        treeNodes.add(treeNode1);
        treeNodes.add(treeNode1_1);
        treeNodes.add(treeNode1_1_1);
        treeNodes.add(treeNode1_1_2);
        treeNodes.add(treeNode1_2);
        treeNodes.add(treeNode2);
        treeNodes.add(treeNode3);
        System.out.println("treeNodes = " + treeNodes);
        // treeNodes
        TreeBuild treeBuild = new TreeBuild(treeNodes);

        treeNodes = treeBuild.buildTree();

        System.out.println("treeNodes = " + treeNodes);
        JSONArray array = JSONArray.fromObject(treeNodes);
        System.out.println(array);

        String json = JSON.toJSON(treeNodes).toString();
        System.out.println(json);

    }
}
