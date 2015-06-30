package com.dataart.contacts;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

class FileProcessor
{
	public FileProcessor(File src, File target)
	{
		srcFile =  src;
		targetFile = target;
		phones = new TreeSet<String>();
		emails = new TreeSet<String>();
	}

	public void process()
	{
		try {
			ZipInputStream zis = new ZipInputStream(new FileInputStream(srcFile));
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetFile));
			
			processZipFile(zis, zos);
			zis.close();
			zos.close();
		} catch (IOException ex) {
			Logger.getLogger(FileProcessor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void processZipFile(ZipInputStream zis,	ZipOutputStream zos) throws IOException
	{
		ZipEntry entry;
		while ((entry = zis.getNextEntry()) != null)
		{
			String entryName = entry.getName();
			zos.putNextEntry(new ZipEntry(entryName));
			if (!entry.isDirectory())
			{
				if (entryName.toLowerCase().endsWith(".zip"))
				{
					ZipInputStream inSubstream = new ZipInputStream(zis);
					ZipOutputStream outSubstream = new ZipOutputStream(zos);
					processZipFile(inSubstream, outSubstream);
					outSubstream.finish();
				}
				else if (entryName.toLowerCase().endsWith(".gz"))
				{
					BufferedReader reader = new BufferedReader(
						new InputStreamReader(new GZIPInputStream(zis)));
					GZIPOutputStream outSubstream = new GZIPOutputStream(zos);
					BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(outSubstream));
					processText(reader, writer);
					outSubstream.finish();
				}
				else
				{
					BufferedReader reader = new BufferedReader(
						new InputStreamReader(new BufferedInputStream(zis)));
					BufferedOutputStream outSubStream = new BufferedOutputStream(zos);
					BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(outSubStream));
					processText(reader, writer);
					writer.flush();
				}
			}
		}
	}

	private void processText(BufferedReader reader, BufferedWriter writer) throws IOException
	{
		String s;
		while ((s = reader.readLine()) != null)
		{
			writer.write(s);
			writer.newLine();
		}
	}

	private void addContactLists()
	{
		
	}

	private File srcFile;
	private File targetFile;
	private TreeSet<String> phones;
	private TreeSet<String> emails;
}
