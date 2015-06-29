package com.dataart.contacts;

import java.io.File;

class Parser 
{
	public static void main(String[] args)
    {
	/*	if (args.length != 2)
		{
			System.out.println("Usage: Parser <source file> <target file>\n");
			return;
		}*/
		File inputFile = new File("inputs.zip");//args[0]);
		File outputFile = new File("outputs.zip");//args[1]);
		if (!(inputFile.getName().toLowerCase().endsWith(".zip")) ||
			!(outputFile.getName().toLowerCase().endsWith(".zip")))
		{
			System.out.println("You should specify zip files\n");
			return;
		}
		if (!(inputFile.exists()) || !(inputFile.isFile()))
		{
			System.out.println("Source file must exist and be a normal file\n");
		}
		
		FileProcessor processor = new FileProcessor(inputFile, outputFile);
		processor.process();
    }
}
