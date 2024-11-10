package com.liubs.vcb.ui.constpool;

import com.liubs.vcb.domain.assemblycode.MyAssemblyConst;
import com.liubs.vcb.ui.common.EditableLabel;
import org.apache.bcel.classfile.ConstantUtf8;

/**
 * @author Liubsyy
 * @date 2024/11/10
 */
public class Utf8ConstPanel extends ConstBasePanel{
    private EditableLabel value = new EditableLabel();

    public Utf8ConstPanel() {
        addLabeledComponent("Value : ", value);

        value.onActionForInput("Class name", null, r->{
            ConstantUtf8 constantUtf8 = new ConstantUtf8(r);
            myAssemblyConst.setConstant(constantUtf8);
        });
    }

    MyAssemblyConst myAssemblyConst;

    @Override
    public void refresh(MyAssemblyConst myAssemblyConst) {
        super.refresh(myAssemblyConst);
        this.myAssemblyConst = myAssemblyConst;
        ConstantUtf8 constantUtf8 = myAssemblyConst.getConstant();
        value.setText(constantUtf8.getBytes());
    }
}
