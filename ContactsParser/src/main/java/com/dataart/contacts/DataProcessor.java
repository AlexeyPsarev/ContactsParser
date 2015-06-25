package com.dataart.contacts;

import java.io.File;
import java.util.TreeSet;

class DataProcessor
{
	public DataProcessor(File src, File target)
	{
		srcFile =  src;
		targetFile = target;
		phones = new TreeSet<String>();
		emails = new TreeSet<String>();
	}

	public void process()
	{
		
	}

	private void processDir(File f)
	{
		
	}

	private void processZipFile(File f)
	{
		
	}

	private void processGZipFile(File f)
	{
		
	}

	private void processTxtFile(File f)
	{
		
	}

	private void addContactLists()
	{
		
	}

	private File srcFile;
	private File targetFile;
	private TreeSet<String> phones;
	private TreeSet<String> emails;
}
