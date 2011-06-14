package com.intercept.root;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.io.IOException;

import android.content.Context;

public class Recovery {


	public void saveFiles(Context ApplicationContext)
	{
		try
		{
				SaveIncludedZippedFileIntoFilesFolder(R.raw.recovery, "recovery.rfs", ApplicationContext);
				SaveIncludedZippedFileIntoFilesFolder(R.raw.flash_image, "flash_image", ApplicationContext);
		}
		catch (Exception e1) 
		{
				e1.printStackTrace();
		}
	}
	
	public void doRecovery()
	{
		Boolean sdx = false;
		String[] Commands;
		if (new File("/sdcard/sdx").exists())
		{
			Commands = new String[10];
		}
		else
		{
			sdx = true;
			Commands = new String[14];
		}
		Commands[0] = "/system/bin/mount -o remount,rw /dev/block/stl5 /system";
		Commands[1] = "/system/bin/rm /system/recovery-from-boot.p";
		Commands[2] = "/system/bin/rm /system/etc/install-recovery.sh";
		Commands[3] = "/system/bin/chmod 755 /data/data/com.intercept.root/files/flash_image";
		Commands[4] = "/system/bin/chmod 755 /data/data/com.intercept.root/files/recovery.rfs";
		Commands[5] = "/data/data/com.intercept.root/files/flash_image recovery /data/data/com.intercept.root/files/recovery.rfs";
		Commands[6] = "/system/bin/mount -t rfs -o remount,ro /dev/block/stl5 /system";
		Commands[7] = "/system/bin/rm /data/data/com.intercept.root/files/recovery.rfs";
		Commands[8] = "/system/bin/rm /data/data/com.intercept.root/files/flash_image";
		if (sdx)
		{
			Commands[9] = "/system/bin/mkdir /sdcard/sdx";
			Commands[10] = "/system/bin/mkdir /sdcard/sdx/backup";
			Commands[11] = "/system/bin/mkdir /sdcard/sdx/updates";
			Commands[12] = "/system/bin/mkdir /sdcard/sdx/zip";
			Commands[13] = "/system/bin/reboot recovery";
		}
		else
		{
			Commands[9] = "/system/bin/reboot recovery";
		}
		exec(Commands);
	}
	
	// extracted gz files to files dir so we can use them
    public static void SaveIncludedZippedFileIntoFilesFolder(int resourceid, String filename, Context ApplicationContext) throws Exception {
		InputStream is = ApplicationContext.getResources().openRawResource(resourceid);
		FileOutputStream fos = ApplicationContext.openFileOutput(filename, Context.MODE_WORLD_READABLE);
		GZIPInputStream gzis = new GZIPInputStream(is);
		byte[] bytebuf = new byte[1024];
		int read;
		while ((read = gzis.read(bytebuf)) >= 0) {
			fos.write(bytebuf, 0, read);
		}
		gzis.close();
		fos.getChannel().force(true);
		fos.flush();
		fos.close();
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
