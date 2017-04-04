package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import common.P;

public class Main {

	public static void main(String[] args)
	{
		String apkPath = "C:/wenhaoc/pixelknot-latest.apk";
		String pkg = "info.guardianproject.pixelknot";
		String mainActivity = ".SendActivity";
		String em_inputDir = "/sdcard/PixelKnot/";
		String em_outputDir = "/data/user/0/info.guardianproject.pixelknot/cache/";
		String pc_outputDir = "C:\\Users\\xjtuj\\Downloads\\pixelknot_out\\";

		try {
			P.adb("root").waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (File f : new File("C:/Users/xjtuj/Downloads/Wallpapers/").listFiles())
		{
			if (!f.getName().endsWith(".jpg") && !f.getName().endsWith(".jpeg"))
				continue;
			System.out.println(f.getName());
			P.adb("shell rm -rf " + em_inputDir);
			P.adb("shell mkdir " + em_inputDir);
			P.adb("shell rm -rf " + em_outputDir);
			P.adb("push \""+f.getAbsolutePath()+"\" " + em_inputDir);
			P.adb("shell am broadcast -a android.intent.action.BOOT_COMPLETED");
			P.adb("shell am start -W -S " + pkg + "/" + mainActivity);
			P.adb("shell input tap 900 630");
			wait(1000);
			P.adb("shell input text Hello");
			wait(1000);
			P.adb("shell input tap 1340 170");
			wait(1000);
			P.adb("shell input text 123456");
			wait(1000);
			P.adb("shell input tap 1192 1102");
			wait(40000);
			String outFile = findOutputImage(em_outputDir);
			System.out.println("generated image: " + outFile);
			String name = f.getName().replace(".", "_pixelknot.");
			P.adb("pull " + em_outputDir+outFile + " \"" + pc_outputDir + name + "\"");
			wait(2000);
			//P.adb("shell input tap 1220 1450");
		}
	}

	static String findOutputImage(String dir)
	{
		Process p = P.adb("shell ls " + dir);
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line=in.readLine())!=null)
			{
				if (line.endsWith(".jpg") || line.endsWith(".jpeg"))
				{
					if (line.startsWith("pixelknot_") && !line.startsWith("pixelknot_i_"))
						return line;
				}
			}
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	static void wait(int ms)
	{
		try {
			Thread.sleep(ms);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
