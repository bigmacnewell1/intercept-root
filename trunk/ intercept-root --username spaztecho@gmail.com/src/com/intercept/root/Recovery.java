package com.intercept.root;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
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
		RunCmd.Exec("/system/bin/mount -o remount,rw /dev/block/stl5 /system");
		RunCmd.Exec("/system/bin/rm /system/recovery-from-boot.p");
		RunCmd.Exec("/system/bin/rm /system/etc/install-recovery.sh");
		RunCmd.Exec("/system/bin/chmod 755 /data/data/com.intercept.root/files/flash_image");
		RunCmd.Exec("/system/bin/chmod 755 /data/data/com.intercept.root/files/recovery.rfs");
		RunCmd.Exec("/data/data/com.intercept.root/files/flash_image recovery /data/data/com.intercept.root/files/recovery.rfs");
		RunCmd.Exec("/system/bin/mount -t rfs -o remount,ro /dev/block/stl5 /system");
		RunCmd.Exec("/system/bin/rm /data/data/com.intercept.root/files/recovery.rfs");
		RunCmd.Exec("/system/bin/rm /data/data/com.intercept.root/files/flash_image");
		if (new File("/sdcard/sdx").exists())
		{
			RunCmd.Exec("/system/bin/reboot recovery");
		}
		else
		{
			RunCmd.Exec("/system/bin/mkdir /sdcard/sdx");
			RunCmd.Exec("/system/bin/mkdir /sdcard/sdx/backup");
			RunCmd.Exec("/system/bin/mkdir /sdcard/sdx/updates");
			RunCmd.Exec("/system/bin/mkdir /sdcard/sdx/zip");
			RunCmd.Exec("/system/bin/reboot recovery");
		}
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
}
