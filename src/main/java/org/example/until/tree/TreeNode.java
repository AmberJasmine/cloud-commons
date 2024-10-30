package org.example.until.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode implements Serializable {
    private String id;
    private String name;
    private String pId;
    private Integer sort;
    private List<TreeNode> child;
}
