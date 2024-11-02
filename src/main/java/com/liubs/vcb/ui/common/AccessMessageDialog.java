package com.liubs.vcb.ui.common;

import com.liubs.vcb.constant.AccessConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Liubsyy
 * @date 2024/10/26
 */
public class AccessMessageDialog extends MultiCheckedMessageDialog{
    private List<Integer> accessFlags;

    public AccessMessageDialog(String title, String message, Map<Integer, String> flags, int access) {
        super(title, message, AccessConstant.toDescriptions(flags),
                AccessConstant.splitAccess(access).stream()
                        .map(c-> String.format("0x%04x(%s)", c,flags.get(c)))
                        .collect(Collectors.toList()));
        this.accessFlags = new ArrayList<>(flags.keySet());
    }


    public int getAccess(){
        int access = 0;
        for (Integer select : getSelectIndexes()) {
            access += accessFlags.get(select);
        }
        return access;
    }
}
