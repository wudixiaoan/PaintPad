package com.xctech.paintpad;

import java.util.Stack;

/**
 * Created by an.pan on 2017/5/19.
 */
public class RedoStack {
    private static Stack<BitmapStack> instance = null;

    public synchronized static Stack<BitmapStack> getInstanc(){
        if(instance == null) {
            instance = new Stack<BitmapStack>();
        }
        return instance;
    }

    public static void clearStack() {
        if (instance != null) {
            instance.clear();
            instance = null;
        }
    }
}
