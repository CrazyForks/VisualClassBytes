package com.liubs.vcb.ui.constpool;

import com.liubs.vcb.domain.assemblycode.MyAssemblyConst;
import com.liubs.vcb.ui.common.EditableLabel;
import com.liubs.vcb.ui.validator.NumberInputValidator;
import org.apache.bcel.classfile.ConstantDouble;

/**
 * @author Liubsyy
 * @date 2024/11/10
 */
public class DoubleConstPanel extends ConstBasePanel{
    private EditableLabel bytes = new EditableLabel();

    public DoubleConstPanel() {
        addLabeledComponent("Bytes : ", bytes);

        bytes.onActionForInput("Class name", NumberInputValidator.INSTANCE, r->{
            constant.setBytes(Double.parseDouble(r));
        });
    }

    ConstantDouble constant;
    @Override
    public void refresh(MyAssemblyConst myAssemblyConst) {
        super.refresh(myAssemblyConst);
        constant = myAssemblyConst.getConstant();
        bytes.setText(String.valueOf(constant.getBytes()));
    }


}