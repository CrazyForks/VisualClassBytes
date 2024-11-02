package com.liubs.vcb.ui;

import com.liubs.vcb.entity.Result;
import com.liubs.vcb.tree.BaseTreeNode;

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
