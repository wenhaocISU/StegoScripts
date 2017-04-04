package common;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class P {


	public static Process exec(String cmd)
	{
		System.out.println("[cmd]" + cmd);
		try
		{
			Process p = Runtime.getRuntime().exec(cmd);
			Thread.sleep(300);
			return p;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public static Process adb(String cmd)
	{
		Process p = exec(Settings.adbPath+" " + cmd);
		try {
			p.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//printInputStream(p, "\t");
		//printErrorStream(p, "\t");
		return p;
	}
	
	public static void printInputStream(Process p, String prefix)
	{
		System.out.println("-- input stream");
		printInputStream(p.getInputStream(), prefix);
		System.out.println("-- end of input stream\n");
	}
	
	public static void printErrorStream(Process p, String prefix)
	{
		System.out.println("-- error stream");
		printInputStream(p.getErrorStream(), prefix);
		System.out.println("-- end of error stream\n");
	}
	
	private static void printInputStream(InputStream s, String prefix)
	{
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(s));
			String line;
			while ((line=in.readLine())!=null)
			{
				String txt = prefix==null? line : prefix+line;
				System.out.println(txt);
			}
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
