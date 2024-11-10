package com.liubs.vcb.ui.constpool;

import com.liubs.vcb.domain.assemblycode.MyAssemblyConst;
import com.liubs.vcb.ui.common.EditableLabel;
import com.liubs.vcb.ui.validator.NumberInputValidator;
import org.apache.bcel.classfile.ConstantInteger;

/**
 * @author Liubsyy
 * @date 2024/11/10
 */
public class IntegerConstPanel extends ConstBasePanel{
    private EditableLabel bytes = new EditableLabel();

    public IntegerConstPanel() {

        addLabeledComponent("Bytes : ", bytes);

        bytes.onActionForInput("Class name", NumberInputValidator.INSTANCE, r->{
            constant.setBytes(Integer.parseInt(r));
        });
    }

    ConstantInteger constant;

    @Override
    public void refresh(MyAssemblyConst myAssemblyConst) {
        super.refresh(myAssemblyConst);
        constant = myAssemblyConst.getConstant();
        bytes.setText(String.valueOf(constant.getBytes()));
    }


}
