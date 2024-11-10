package com.liubs.vcb.ui.constpool;

import com.liubs.vcb.domain.assemblycode.MyAssemblyConst;
import com.liubs.vcb.ui.common.IndexLinkLabel;
import org.apache.bcel.classfile.ConstantMethodType;


/**
 * @author Liubsyy
 * @date 2024/11/10
 */
public class MethodTypeConstPanel extends ConstBasePanel{
    private IndexLinkLabel descriptorIndex;// = new EditableLabel(true);

    public MethodTypeConstPanel() {
        descriptorIndex = new IndexLinkLabel(()->{
            selectConstantNode(Integer.parseInt(descriptorIndex.getText()));
        });
        addLabeledComponent("Descriptor Index : ",descriptorIndex);
    }

    @Override
    public void refresh(MyAssemblyConst myAssemblyConst) {
        super.refresh(myAssemblyConst);

        ConstantMethodType constant = myAssemblyConst.getConstant();
        descriptorIndex.setText(String.valueOf(constant.getDescriptorIndex()));
    }
}

