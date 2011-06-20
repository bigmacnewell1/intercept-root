package com.intercept.root;

public class FixVold {
	
	public void FixIt()
	{
		try {
				Runtime.getRuntime().exec("/system/xbin/su /system/xbin/busybox killall vold");
				Thread.sleep(5000);
				Runtime.getRuntime().exec("/system/xbin/su /system/bin/vold");
			}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
