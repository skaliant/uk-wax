package de.skaliant.wax.upload;

import java.io.File;


/**
 * 
 *
 * @author Udo Kastilan
 */
public class MD5Collector
{
	public static void main(String[] args)
	{
		File[] fs = {
			new File("C:\\Users\\Zaphod\\Desktop\\blindtext.txt"),
			new File("C:\\Users\\Zaphod\\Desktop\\PM5544_with_non-PAL_signals.png"),
			new File("C:\\Users\\Zaphod\\Desktop\\RCA_Indian_Head_test_pattern.JPG")
		};
		
		for (File f : fs)
		{
			System.out.println("[" + f.getName() + "]: " + UploadTest.md5(f));
		}
	}
}
