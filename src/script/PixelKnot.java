package script;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import common.Command;

public class PixelKnot {

	
	
	// adb shell am broadcast -a android.intent.action.BOOT_COMPLETED

	
	public static void run()
	{
		AppInfo appInfo = new AppInfo("info.guardianproject.pixelknot", ".SendActivity");
		List<String> deviceIDs = Command.getDeviceIDs();
		List<EmulatorJob> jobs = new ArrayList<>();
		for (String id : deviceIDs)
		{
			EmulatorJob job = new EmulatorJob(id, appInfo);
			jobs.add(job);
		}
		File[] images = new File[] {new File("c:/wenhaoc/small.jpg")};
		//int index = 0;
		for (File image : images)
		{
			if (!isJPG(image.getName()))
				continue;
			for (EmulatorJob job : jobs)
			{
				job.addImage(image);
			}
			//jobs.get(index++).addImage(image);
			//if (index>=jobs.size())
			//	index = 0;
		}
		for (EmulatorJob job : jobs)
			job.execute();
	}
	
	public static boolean isJPG(String fileName)
	{
		return (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"));
	}
	
}
