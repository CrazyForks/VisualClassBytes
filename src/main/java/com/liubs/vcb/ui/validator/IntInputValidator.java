package com.liubs.vcb.ui.validator;

import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.util.NlsSafe;
import com.liubs.vcb.util.StringUtils;

/**
 * @author Liubsyy
 * @date 2024/10/26
 */
public class IntInputValidator implements InputValidator {
    public static IntInputValidator INSTANCE = new IntInputValidator();
    @Override
    public boolean checkInput(@NlsSafe String s) {
        return StringUtils.isNumeric(s);
    }

    @Override
    public boolean canClose(@NlsSafe String s) {
        return checkInput(s);
    }

}
