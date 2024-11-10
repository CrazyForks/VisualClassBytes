package com.liubs.vcb.ui.validator;

import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.util.NlsSafe;
import com.liubs.vcb.util.StringUtils;

/**
 * @author Liubsyy
 * @date 2024/10/26
 */
public class NumberInputValidator implements InputValidator {
    public static NumberInputValidator INSTANCE = new NumberInputValidator();
    @Override
    public boolean checkInput(@NlsSafe String s) {
        return StringUtils.isNumeric(s);
    }

    @Override
    public boolean canClose(@NlsSafe String s) {
        return checkInput(s);
    }

}
