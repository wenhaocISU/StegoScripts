package script;

import java.io.File;
import java.io.IOException;

import common.Settings;

public class Nexus5X {

	public static void main(String[] args)
	{
		AppInfo appInfo = new AppInfo("info.guardianproject.pixelknot", ".SendActivity");
		File[] images = new File("C:/Users/xjtuj/Downloads/Wallpapers/").listFiles();
		
		
		for (File image : images)
		{
			adb("push \"" + image.getAbsolutePath() + "\" /sdcard/PixelKnot/");
			//adb("shell am start -W -S " + appInfo.packageName + "/" + appInfo.mainActivity);
			//adb("shell input tap 420 490");
			//adb("shell input tap 278 491");
			//adb("shell input tap 80 141"); // click drop down menu on top left
			//adb("shell input tap 312 609"); // select "Nexus 5X"
			//adb("shell input tap 287 1244");// click the image
			//wait(3000);
			//adb("shell input text Hello");
			//adb("shell input tap 1000 154");
			//adb("shell input text 123456");
			//adb("shell input tap 914 777");
			//wait(3000);
			//adb("shell rm \"/sdcard/"+image.getName()+"\"");
			////break;
		}
	}
	
	static Process adb(String cmd)
	{
		Process p = exec(Settings.adbPath + " -d " + cmd);
		if (cmd.startsWith("shell input "))
			wait(2000);
		return p;
	}
	
	static Process exec(String cmd)
	{
		System.out.println("[cmd] " + cmd.replace(Settings.adbPath, "adb"));
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			return p;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	static void wait(int ms)
	{
		try
		{
			Thread.sleep(ms);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
