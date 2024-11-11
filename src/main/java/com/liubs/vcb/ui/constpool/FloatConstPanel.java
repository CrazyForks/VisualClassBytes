package com.liubs.vcb.ui.constpool;

import com.liubs.vcb.domain.assemblycode.MyAssemblyConst;
import com.liubs.vcb.ui.common.EditableLabel;
import org.apache.bcel.classfile.ConstantFloat;

/**
 * @author Liubsyy
 * @date 2024/11/10
 */
public class FloatConstPanel extends ConstBasePanel{
    private EditableLabel bytes = new EditableLabel();

    public FloatConstPanel() {

        addLabeledComponent("Bytes : ", bytes);

        bytes.onActionForInput("Float value", null, r->{
            constant.setBytes(Float.parseFloat(r));
        });
    }
    private ConstantFloat constant;

    @Override
    public void refresh(MyAssemblyConst myAssemblyConst) {
        super.refresh(myAssemblyConst);
        constant = myAssemblyConst.getConstant();
        bytes.setText(String.valueOf(constant.getBytes()));
    }


}
