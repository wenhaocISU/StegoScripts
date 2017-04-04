package common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Command {

	
	public static void listEmulators()
	{
		exec(Settings.emulatorPath + " -list-avds", true, true);
	}
	
	public static List<String> getDeviceIDs()
	{
		List<String> result = new ArrayList<>();
		Process p = P.exec(Settings.adbPath+ " devices");
		try
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line;
			while ((line=in.readLine())!=null)
			{
				if (line.endsWith("\tdevice"))
				{
					result.add(line.substring(0, line.indexOf("\tdevice")));
				}
			}
			in.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	public static void startEmulator(String id, boolean printDebugInfo)
	{
		boolean pI = printDebugInfo&&false;
		boolean pE = printDebugInfo&&true;
		exec(Settings.emulatorPath + " -netdelay none -netspeed full -avd " + id, pI, pE);
	}

	public static void installApp(String apkPath, boolean printDebugInfo)
	{
		boolean pI = printDebugInfo&&true;
		boolean pE = printDebugInfo&&true;
		exec(Settings.adbPath + " install -r " + apkPath, pI, pE);
	}
	
	public static void startActivity(String packageName, String activityName, boolean printDebugInfo)
	{
		boolean pI = printDebugInfo&&true;
		boolean pE = printDebugInfo&&true;
		exec(Settings.adbPath + " shell am start -S -W -n " + packageName + "/" + activityName, pI, pE);
	}
	
	public static void exec(String cmd, boolean printI, boolean printE)
	{
		Process p = P.exec(cmd);
		if (printI)
			P.printInputStream(p, "\t");
		if (printE)
			P.printErrorStream(p, "\t");
	}
}
