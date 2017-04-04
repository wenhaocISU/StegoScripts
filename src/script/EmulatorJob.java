package script;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import common.Settings;

public class EmulatorJob extends SwingWorker<Void, Void>{

	String id, em_inputDir, em_outputDir, pc_outputDir, message, password;
	List<File> images;
	AppInfo appInfo;
	
	public EmulatorJob(String id, AppInfo appInfo)
	{
		this.id = id;
		this.appInfo = appInfo;
		this.images = new ArrayList<File>();
		em_inputDir = "/sdcard/PixelKnot/";
		em_outputDir = "/data/user/0/info.guardianproject.pixelknot/cache/";
		pc_outputDir = Settings.outputDir;
		message = "Hello";
		password = "123456";
		File pcOutDir = new File(pc_outputDir);
		if (!pcOutDir.exists())
			pcOutDir.mkdirs();
	}
	
	public void addImage(File image)
	{
		images.add(image);
	}

	@Override
	protected Void doInBackground() throws Exception
	{
		adb("root");
		for (File image : images)
		{
			for (int i = 1; i < 10; i++)
			{
				long startTime = System.currentTimeMillis();
				adb("shell rm -rf " + em_inputDir);
				adb("shell mkdir " + em_inputDir);
				adb("shell rm -rf " + em_outputDir);
				adb("push \""+image.getAbsolutePath()+"\" " + em_inputDir);
				adb("shell am broadcast -a android.intent.action.BOOT_COMPLETED");
				adb("shell am start -W -S " + appInfo.packageName + "/" + appInfo.mainActivity);
				forTinyPhone();
				String outFile = findOutputImage(em_outputDir);
				//System.out.println("generated image: " + outFile);
				String name = image.getName().replace(".", "old"+i+".");
				adb("pull " + em_outputDir+outFile + " \"" + pc_outputDir + name + "\"");
				wait(3000);
				long endTime = System.currentTimeMillis();
				int elapsedTime = (int)(endTime-startTime)/1000;
				System.out.println("["+id+"] time for this iteration: " + elapsedTime + " seconds");
				//break;
			}
		}
		return null;
	}
	
	private void forTinyPhone()
	{
		adb("shell input tap 195 137");
		wait(1000);
		adb("shell input text "+message);
		wait(1000);
		adb("shell input tap 215 39");
		wait(1000);
		adb("shell input text "+password);
		wait(1000);
		adb("shell input tap 184 188");
		wait(5000);
	}
	
	private void forNexus6p()
	{
		adb("shell input tap 900 630");
		wait(1000);
		adb("shell input text "+message);
		wait(1000);
		adb("shell input tap 1340 170");
		wait(1000);
		adb("shell input text "+password);
		wait(1000);
		adb("shell input tap 1192 1102");
		wait(10000);
	}
	
	private String findOutputImage(String dir)
	{
		int waitTime = 5000;
		while (true)
		{
			Process p = adb("shell ls -s -1 " + dir);
			ArrayList<String> output = readOutput(p);
			for (String s : output)
			{
				if (s.startsWith("total "))
					continue;
				String[] fileInfo = s.trim().split(" ");
				String length = fileInfo[0];
				String name = fileInfo[1];
				if (Integer.parseInt(length)==0)
					continue;
				if (name.startsWith("pixelknot_") &&
						!name.startsWith("pixelknot_i_"))
				{
					wait(1000);
					return name;
				}
			}
			wait(waitTime);
		}
	}

	

	
	ArrayList<String> readOutput(Process p)
	{
		return readStream(p.getInputStream());
	}
	
	ArrayList<String> readError(Process p)
	{
		return readStream(p.getErrorStream());
	}
	
	ArrayList<String> readStream(InputStream s)
	{
		ArrayList<String> result = new ArrayList<>();
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(s));
			String line;
			while ((line=in.readLine())!=null)
			{
				result.add(line);
			}
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	private Process adb(String cmd, boolean waitFor)
	{
		Process p = exec(Settings.adbPath + " -s " + id + " " + cmd, waitFor);
		return p;
	}
	
	private Process adb(String cmd)
	{
		return adb(cmd, true);
	}
	
	private Process exec(String cmd, boolean waitFor)
	{
		try{
			System.out.println("[cmd]adb" + cmd.substring(cmd.indexOf(" ")));
			Process p = Runtime.getRuntime().exec(cmd);
			if (waitFor)
				p.waitFor();
			return p;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private Process exec(String cmd)
	{
		return exec(cmd, true);
	}
	
	private void wait(int ms)
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
