package com.example.android.architecture.blueprints.todoapp;

import com.fxi.sunset.common.util.MaskUtil;
import com.fxi.sunset.task.data.TaskScheduler;

/**
 * Created by seki on 16/8/9.
 */
public class UtilTester {
	public static void main(String[] args) {
	}

	public static void testMaskUtil() {
		System.out.println(Integer.valueOf("1101111", 2));
		System.out.println(Integer.toBinaryString(32));
		System.out.println(MaskUtil.getRepeatDay(Integer.valueOf("1101111", 2)));
	}


}
