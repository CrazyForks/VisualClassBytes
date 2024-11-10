package com.liubs.vcb.ui.constpool;

import com.liubs.vcb.domain.assemblycode.MyAssemblyConst;
import com.liubs.vcb.ui.common.IndexLinkLabel;
import org.apache.bcel.classfile.ConstantClass;

/**
 * @author Liubsyy
 * @date 2024/11/10
 */
public class ClassConstPanel extends ConstBasePanel{

    private IndexLinkLabel nameIndex;

    public ClassConstPanel() {
        nameIndex = new IndexLinkLabel(()->{
            selectConstantNode(Integer.parseInt(nameIndex.getText()));
        });
        addLabeledComponent("Name Index : ",nameIndex);
    }

    @Override
    public void refresh(MyAssemblyConst myAssemblyConst) {
        super.refresh(myAssemblyConst);
        ConstantClass constant = myAssemblyConst.getConstant();

        nameIndex.setText(String.valueOf(constant.getNameIndex()));
    }


}
