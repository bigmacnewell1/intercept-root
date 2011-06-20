package com.intercept.root;


import java.io.File;
import java.io.IOException;
import java.io.DataOutputStream;

public class UnRoot {
	//remove su root priv
	public void doUnRoot()
	{
		if (new File("/system/bin/playlogo-orig").exists())
		{
			String[] Commands = new String[11];
			Commands[0] = "/system/bin/mount -t rfs -o remount,rw /dev/block/stl9 /system";
			Commands[1] = "/system/bin/rm /system/bin/joeykrim-root.sh";
			Commands[2] = "/system/bin/rm /system/bin/jk-su";
			Commands[3] = "/system/bin/rm /system/bin/playlogo";
			Commands[4] = "/system/bin/rm /etc/passwd";
			Commands[5] = "/system/bin/rm /etc/group";
			Commands[6] = "/system/bin/rm /system/bin/busybox";
			Commands[7] = "/system/bin/mv /system/bin/playlogo-orig /system/bin/playlogo";
			Commands[8] = "/system/bin/chmod 755 /system/bin/playlogo";
			Commands[9] = "/system/bin/mount -t rfs -o remount,ro /dev/block/stl9 /system";
			Commands[10] = "/system/bin/reboot";
			exec(Commands);
		}
	}
    
	// Executes UNIX commands with root priv.
    protected void exec(String[] Commands){
    try {
    	Process process = Runtime.getRuntime().exec("su");
    	DataOutputStream os = new DataOutputStream(process.getOutputStream());
    	for (String single : Commands) {
    		os.writeBytes(single + "\n");
    		os.flush();
    	}
    	os.writeBytes("exit\n");
    	os.flush();
    	process.waitFor();
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	} catch (InterruptedException e) {
    		throw new RuntimeException(e);
    	}
    }
    
}

