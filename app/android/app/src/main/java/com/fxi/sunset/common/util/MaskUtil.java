package com.fxi.sunset.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seki on 16/8/9.
 */
public class MaskUtil {
    public static final List<Integer> MASKS = new ArrayList<Integer>() {
        {
            this.add(0x000001);
            this.add(0x000002);
            this.add(0x000004);
            this.add(0x000008);
            this.add(0x000010);
            this.add(0x000020);
            this.add(0x000040);
            this.add(0x000080);
            this.add(0x000100);
            this.add(0x000200);
            this.add(0x000400);
            this.add(0x000800);
            this.add(0x001000);
        }
    };

    public static List<Integer> getMaskList(Integer timeRepeat) {
        List<Integer> rsList = new ArrayList<>();
        for (int i = 0; i < MASKS.size(); i++) {
            if ((timeRepeat & MASKS.get(i)) == MASKS.get(i)) {
                rsList.add(MASKS.get(i));
            }
        }
        return rsList;
    }

}

