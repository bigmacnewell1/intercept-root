package com.intercept.root;

public class RunCmd
{
    static {
        System.loadLibrary("RunCmd");
    }
    public static native void Exec(String Cmd);
}

