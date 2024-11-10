package com.liubs.vcb.ui.constpool;

import com.liubs.vcb.domain.assemblycode.MyAssemblyConst;
import com.liubs.vcb.ui.common.IndexLinkLabel;
import org.apache.bcel.classfile.ConstantString;


/**
 * @author Liubsyy
 * @date 2024/11/10
 */
public class StringConstPanel extends ConstBasePanel{
    private IndexLinkLabel stringIndex;

    public StringConstPanel() {
        stringIndex = new IndexLinkLabel(()->{
            selectConstantNode(Integer.parseInt(stringIndex.getText()));
        });
        addLabeledComponent("String Index : ",stringIndex);
    }

    @Override
    public void refresh(MyAssemblyConst myAssemblyConst) {
        super.refresh(myAssemblyConst);

        ConstantString constant = myAssemblyConst.getConstant();
        stringIndex.setText(String.valueOf(constant.getStringIndex()));
    }
}
