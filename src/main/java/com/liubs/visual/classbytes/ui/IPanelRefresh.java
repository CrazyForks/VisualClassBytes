package com.liubs.visual.classbytes.ui;

import com.liubs.visual.classbytes.entity.Result;
import com.liubs.visual.classbytes.tree.BaseTreeNode;

/**
 * @author Liubsyy
 * @date 2024/10/21
 */
public interface IPanelRefresh<T extends BaseTreeNode> {
    void refresh(T treeNode);

    default Result<Void> savePanel(T treeNode){
        Result<Void> result = new Result<>();
        result.setSuccess(true);
        return result;
    }
}
