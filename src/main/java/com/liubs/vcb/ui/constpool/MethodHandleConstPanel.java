package com.liubs.vcb.ui.constpool;

import com.liubs.vcb.domain.assemblycode.MyAssemblyConst;
import com.liubs.vcb.ui.common.EditableLabel;
import com.liubs.vcb.ui.common.IndexLinkLabel;
import org.apache.bcel.classfile.ConstantMethodHandle;


/**
 * @author Liubsyy
 * @date 2024/11/10
 */
public class MethodHandleConstPanel extends ConstBasePanel{
    private IndexLinkLabel referenceKind ;
    private IndexLinkLabel referenceIndex ;

    public MethodHandleConstPanel() {
        referenceKind = new IndexLinkLabel(()->{
            selectConstantNode(Integer.parseInt(referenceKind.getText()));
        });
        referenceIndex = new IndexLinkLabel(()->{
            selectConstantNode(Integer.parseInt(referenceIndex.getText()));
        });
        addLabeledComponent("Reference Kind : ",referenceKind);
        addLabeledComponent("Reference Index : ",referenceIndex);
    }

    @Override
    public void refresh(MyAssemblyConst myAssemblyConst) {
        super.refresh(myAssemblyConst);

        ConstantMethodHandle constant = myAssemblyConst.getConstant();
        referenceKind.setText(String.valueOf(constant.getReferenceKind()));
        referenceIndex.setText(String.valueOf(constant.getReferenceIndex()));
    }
}
