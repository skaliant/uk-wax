package de.skaliant.wax.upload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import de.skaliant.wax.util.MiscUtils;


/**
 * 
 *
 * @author Udo Kastilan
 */
@RunWith(JUnit4.class)
public class UploadTest
{
	@Test
	public void testUpload()
	{
		String contentType = "multipart/form-data; boundary=----WebKitFormBoundaryQHeIdeNIBwvWj0AE";
		InputStream in = null;
		MultipartParser mp = null;
		File targetDir = new File("src/test/resources/tempfiles");
		File[] ls = null;
		FileDesc[] expected = {
			new FileDesc("blindtext.txt", "ed4cc2b2cfd6f9005f0fc9deee0ca0f3", 5709),
			new FileDesc("PM5544_with_non-PAL_signals.png", "b0f7fb8b330f60cefa2c1ff129bd1bcf", 15118),
			new FileDesc("RCA_Indian_Head_test_pattern.JPG", "279dcec949579055f7238a2bf5eb00a4", 328169)
		};

		targetDir.mkdirs();
		ls = targetDir.listFiles();
		if ((ls != null) && (ls.length != 0))
		{
			for (File f : ls)
			{
				f.delete();
			}
		}
		try
		{
			in = new BufferedInputStream(new FileInputStream(new File(
					"src/test/resources/uploaded.bin")));
			mp = MultipartParser.create(contentType, in);

			for (Part p : mp.getAllParts())
			{
				if (p.isFile() && (p.getSize() > 0))
				{
					p.writeTo(new File(targetDir, p.getFileName()));
				}
			}
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
		finally
		{
			MiscUtils.close(in);
			if (mp != null)
			{
				mp.cleanup();
			}
		}
		
		for (FileDesc fd : expected)
		{
			File f = new File(targetDir, fd.name);
			
			Assert.assertTrue(f.isFile());
			Assert.assertEquals(fd.size, f.length());
			Assert.assertEquals(fd.md5, md5(f));
		}
		
		ls = targetDir.listFiles();
		if ((ls != null) && (ls.length != 0))
		{
			for (File f : ls)
			{
				f.delete();
			}
		}
		targetDir.delete();
	}
	
	
	@Test
	public void checkContentType()
	{
		Assert.assertTrue("is multipart", MultipartParser.isUpload("multipart/form-data; boundary=----WebKitFormBoundaryKP4H1OvhYs3RPtCf"));
		Assert.assertTrue("is not multipart", !MultipartParser.isUpload("text/plain"));
	}
	
	
	public static String md5(File f)
	{
		StringBuilder sb = new StringBuilder();
		InputStream in = null;
		int b = 0;
		
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			in = new DigestInputStream(new BufferedInputStream(new FileInputStream(f)), md);
			while (b != -1)
			{
				b = in.read();
			}
			for (byte d : md.digest())
			{
				String hx = Integer.toHexString(0x00ff & d);
				
				if (hx.length() == 1)
				{
					sb.append('0');
				}
				sb.append(hx);
			}
		}
		catch (Exception ex)
		{
			throw new RuntimeException(ex);
		}
		finally
		{
			MiscUtils.close(in);
		}
		return sb.toString();
	}
	
	
	private static class FileDesc
	{
		private String name = null;
		private String md5 = null;
		private long size = 0;
		
		
		public FileDesc(String name, String md5, long size)
		{
			this.name = name;
			this.md5 = md5;
			this.size = size;
		}
	}
}
