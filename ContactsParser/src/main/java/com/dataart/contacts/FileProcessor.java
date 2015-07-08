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
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileProcessor
{
	public FileProcessor(File src, File target)
	{
		srcFile =  src;
		targetFile = target;
		phones = new TreeSet<>();
		emails = new TreeSet<>();
	}

	public void process()
	{
		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(srcFile));
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetFile)))
		{
			processZipFile(zis, zos);
			addContactList(zos, "phones.txt", phones);
			addContactList(zos, "emails.txt", emails);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
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
		String original;
		String modified;
		TextProcessor txtProc = new TextProcessor();
		while ((original = reader.readLine()) != null)
		{
			if (original.isEmpty())
				writer.write("");
			else
			{
				modified = txtProc.process(original);
				phones.add(txtProc.getPhone());
				emails.addAll(txtProc.getEmails());
				txtProc.reset();
				writer.write(modified);
			}
			writer.newLine();
		}
	}

	private void addContactList(ZipOutputStream zos, String filename,
		SortedSet<String> list) throws IOException
	{
		ZipEntry entry = new ZipEntry(filename);
		zos.putNextEntry(entry);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(zos));

		for (String item: list)
		{
			writer.write(item);
			writer.newLine();
		}
		writer.flush();
	}

	private File srcFile;
	private File targetFile;
	private SortedSet<String> phones;
	private SortedSet<String> emails;
	
	private static Logger logger = Logger.getLogger(FileProcessor.class.getName());
}
