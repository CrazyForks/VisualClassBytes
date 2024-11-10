package com.liubs.vcb.ui.constpool;

import com.liubs.vcb.domain.assemblycode.MyAssemblyConst;
import com.liubs.vcb.ui.common.IndexLinkLabel;
import org.apache.bcel.classfile.ConstantNameAndType;

/**
 * @author Liubsyy
 * @date 2024/11/10
 */
public class NameAndTypeConstPanel extends ConstBasePanel{
    private IndexLinkLabel nameIndex;// = new EditableLabel(true);
    private IndexLinkLabel signatureIndex;// = new EditableLabel(true);


    public NameAndTypeConstPanel() {
        nameIndex = new IndexLinkLabel(()->{
            selectConstantNode(Integer.parseInt(nameIndex.getText()));
        });
        signatureIndex = new IndexLinkLabel(()->{
            selectConstantNode(Integer.parseInt(signatureIndex.getText()));
        });

        addLabeledComponent("Name Index : ",nameIndex);
        addLabeledComponent("Signature Index : ",signatureIndex);
    }

    @Override
    public void refresh(MyAssemblyConst myAssemblyConst) {
        super.refresh(myAssemblyConst);

        ConstantNameAndType constant = myAssemblyConst.getConstant();
        nameIndex.setText(String.valueOf(constant.getNameIndex()));
        signatureIndex.setText(String.valueOf(constant.getSignatureIndex()));
    }
}
