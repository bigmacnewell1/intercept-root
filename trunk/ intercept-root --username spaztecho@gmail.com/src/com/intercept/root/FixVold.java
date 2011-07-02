package com.intercept.root;

public class FixVold {
	
	public void FixIt()
	{
		try {
				RunCmd.Exec("/system/xbin/busybox killall vold");
				Thread.sleep(5000);
				RunCmd.Exec("/system/bin/vold");
			}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
