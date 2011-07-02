/*
 * Parts of GUI, XML layouts for the GUI and the code to extract the GunZiped resource
 * from res RAW were taken from RyanZA Z4root app
 */

package com.intercept.root;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Build;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.DialogInterface;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import java.io.IOException;

public class Root extends Activity {

	boolean disabled = false;
	Button rootbutton, unrootbutton, recoverybutton, temprootbutton;
	TextView detailtext;
	WakeLock wl;
	Bundle Saved;
	Boolean NeedsRoot = true;
	Boolean oldSchool = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Saved = savedInstanceState;
		setContentView(R.layout.root);
		rootbutton = (Button) findViewById(R.id.rootbutton);
		unrootbutton = (Button) findViewById(R.id.unrootbutton);
		recoverybutton = (Button) findViewById(R.id.recoverybutton);
		temprootbutton = (Button) findViewById(R.id.temprootbutton);
		detailtext = (TextView) findViewById(R.id.detailtext);
		if (Integer.parseInt(Build.VERSION.SDK) == Build.VERSION_CODES.ECLAIR_MR1)
		{
			oldSchool = true;
		}
		rootbutton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (disabled)
					return;
				disabled = true;;
				//cleanUp(getFilesDir());
				new File("/data/data/com.intercept.root/files/tempRoot").delete();
				root();
			}
		});
		
		recoverybutton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (disabled)
					return;
				disabled = true;
				doRecovery();
			}
		});

		unrootbutton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (disabled)
					return;
				disabled = true;
				doUnRoot();
			}
		});
		
		temprootbutton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (disabled)
					return;
				disabled = true;
				try 
				{
					new File("/data/data/com.intercept.root/files/tempRoot").createNewFile();
				}
				catch (IOException e) 
				{
					throw new RuntimeException(e);
				}
				root();
			}
		});
		dostuff();
	}

	public void dostuff() {
		cleanUp(getFilesDir());
		if (!Build.MODEL.equalsIgnoreCase("SPH-M910"))
		{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Warning Untested Phone!");
				builder.setMessage("Use of Temp Root is recommended. Please insure that your phone is compatible with Joey Krim's permanent root method, before attempting permanent Root.").setCancelable(true)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
				AlertDialog alert = builder.create();
				alert.setOwnerActivity(this);
				alert.show();
				recoverybutton.setVisibility(View.GONE);
				if (new File("/system/bin/su").exists() ||  new File("/system/xbin/su").exists())
				{
					unrootbutton.setVisibility(View.GONE);
					detailtext.setText("Your device is already rooted. You can remove the root using the Un-root button, which will delete all of the files installed to root your device. You can re-root your device if your root is malfunctioning.");
				}
				if (new File("/system/bin/joeykrim-root.sh").exists())
				{
					unrootbutton.setVisibility(View.VISIBLE);
				}
		}
		else
		{
			if (new File("/system/bin/su").exists() || new File("/system/xbin/su").exists())
			{
				NeedsRoot = false;
				unrootbutton.setVisibility(View.GONE);
				recoverybutton.setVisibility(View.VISIBLE);
				detailtext.setText("Your device is already rooted. You can remove the root using the Un-root button, which will delete all of the files installed to root your device. You can re-root your device if your root is malfunctioning. You can also install CM01 custom recovery.");
			}
			else
			{
				recoverybutton.setVisibility(View.VISIBLE);
				unrootbutton.setVisibility(View.GONE);
			}
			if (new File("/system/bin/joeykrim-root.sh").exists())
			{
				unrootbutton.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.about :
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage("Intercept Root " + "\nFor the Samsung Intercept." + "\nSpaztecho").setCancelable(true);
				AlertDialog alert = builder.create();
				alert.setOwnerActivity(this);
				alert.show();
				return true;
			case R.id.exit :
				int pid = android.os.Process.myPid();
				android.os.Process.killProcess(pid); 
				return true;
			default :
				return super.onOptionsItemSelected(item);
		}
	}
	

	public void switchView() {;
		setContentView(R.layout.p1);
		detailtext = (TextView) findViewById(R.id.infotext);
	}

	public void saystuff(final String stuff) 
	{
		runOnUiThread(new Runnable() 
		{
			public void run() 
			{
				detailtext.setText(stuff);
			}
		});
	}
	
	public void cleanUp(File dir)
	{
		
		if (dir.isDirectory()) 
		{
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) 
	        {
	        	if(!children[i].equals("tempRoot"))//don't delete tempRoot
	        	{
	        		new File(dir, children[i]).delete();
	        	}
	        }
	    }
	}
	
	public void root()
	{
		switchView();
		new Thread() 
		{
			public void run() 
			{
				try 
				{
					PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
					wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "z4root");
					wl.acquire();
					DoRoot root = new DoRoot();
					saystuff("Saving required files...");
					root.saveFiles(getApplicationContext());
					saystuff("Running exploit in order to obtain root access...");
					if(oldSchool){
						root.Zygote(getContentResolver());
					}
					else{
						root.exploit();
					}
					Thread.sleep(6000);
					if (new File("/data/data/com.intercept.root/files/tempRoot").exists())
					{
						while(!new File("/data/data/com.intercept.root/files/rooted").exists())
						{
							saystuff("Once phone is rooted, you can use this app to flash Inxane's CM01 recovery.");
							Thread.sleep(6000);
							saystuff("Temp Rooting in progress.");
							Thread.sleep(6000);
						}
						saystuff("Phone is Temp Rooted.");
						Thread.sleep(2000);
						cleanUp(getFilesDir());
						new File("/data/data/com.intercept.root/files/tempRoot").delete();
						if(!oldSchool){
							saystuff("Fixing Vold.");
							FixVold fixvold = new FixVold();
							fixvold.FixIt();
						}
						Intent i = new Intent(Root.this, Root.class);// restart app
						startActivity(i);
						finish();
					}
					else
					{
						while(true)
						{
							saystuff("Phone will reboot when complete.");
							Thread.sleep(6000);
							saystuff("If phone does not reboot within 10 minutes exploit has failed.");
							Thread.sleep(6000);;
							saystuff("Once phone is rooted, you can use this app to flash Inxane's CM01 recovery.");
							Thread.sleep(6000);
							saystuff("Rooting in progress.");
							Thread.sleep(6000);
						}
					}
				}
				catch (InterruptedException e) 
				{
					throw new RuntimeException(e);
				}
			};
		}.start();
	}
	
	public void doUnRoot()
	{
		switchView();
		new Thread() 
		{
			public void run() 
			{
				try 
				{
					PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
					wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "z4root");
					wl.acquire();
					saystuff("Unrooting in progress, once phone is unrooted, it should reboot.");
					Thread.sleep(4000);//just wasting time
					UnRoot unroot = new UnRoot();
					unroot.doUnRoot();
					saystuff("Error playlogo-orig not found.");
					wl.release();
				}
				catch (InterruptedException e) 
				{
					throw new RuntimeException(e);
				}
			};
		}.start();
	}
	
	public void doRecovery()
	{
		switchView();
		new Thread() 
		{
			public void run() 
			{
				try 
				{
					PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
					wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "z4root");
					wl.acquire();
					if (NeedsRoot)
					{
						new File("/data/data/com.intercept.root/files/tempRoot").createNewFile();
						DoRoot root = new DoRoot();
						saystuff("Saving required files...");
						root.saveFiles(getApplicationContext());
						saystuff("Running exploit in order to obtain root access...");
						if(oldSchool){
							root.Zygote(getContentResolver());
						}
						else {
							root.exploit();
						}
						Thread.sleep(6000);
						while(!new File("/data/data/com.intercept.root/files/rooted").exists())
						{
							saystuff("Once phone is rooted, this app will flash Inxane's CM01 recovery.");
							Thread.sleep(6000);
							saystuff("Temp Rooting in progress.");
							Thread.sleep(6000);
						}
						saystuff("Phone is Temp Rooted.");
						Thread.sleep(2000);
						cleanUp(getFilesDir());
						new File("/data/data/com.intercept.root/files/tempRoot").delete();
						if(!oldSchool){
							saystuff("Fixing Vold.");
							FixVold fixvold = new FixVold();
							fixvold.FixIt();
						}
						Thread.sleep(2000);
					}
					Recovery recovery = new Recovery();
					saystuff("Saving required files...");
					recovery.saveFiles(getApplicationContext());
					saystuff("Flashing CM01 recovery, phone should reboot into recovery when done");
					Thread.sleep(3000);
					recovery.doRecovery();
					wl.release();
				}
				catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			};
		}.start();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	}


}
