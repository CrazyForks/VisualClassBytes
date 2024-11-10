package com.liubs.vcb.ui.constpool;

import com.liubs.vcb.domain.assemblycode.MyAssemblyConst;
import com.liubs.vcb.ui.common.IndexLinkLabel;
import org.apache.bcel.classfile.ConstantCP;

/**
 * @author Liubsyy
 * @date 2024/11/10
 */
public class ConstantCPPanel extends ConstBasePanel{
    private IndexLinkLabel class_index;
    private IndexLinkLabel name_and_type_index;

    public ConstantCPPanel() {
        class_index = new IndexLinkLabel(()->{
            selectConstantNode(Integer.parseInt(class_index.getText()));
        });
        name_and_type_index = new IndexLinkLabel(()->{
            selectConstantNode(Integer.parseInt(name_and_type_index.getText()));
        });
        addLabeledComponent("Class Index : ",class_index);
        addLabeledComponent("Name and Type Index : ",name_and_type_index);
    }

    @Override
    public void refresh(MyAssemblyConst myAssemblyConst) {
        super.refresh(myAssemblyConst);
        ConstantCP constant = myAssemblyConst.getConstant();
        class_index.setText(String.valueOf(constant.getClassIndex()));
        name_and_type_index.setText(String.valueOf(constant.getNameAndTypeIndex()));
    }



}
