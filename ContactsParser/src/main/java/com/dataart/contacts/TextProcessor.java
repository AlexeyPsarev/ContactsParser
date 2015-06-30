package com.dataart.contacts;

import java.util.StringTokenizer;
import java.util.TreeSet;

public class TextProcessor
{
	public TextProcessor()
	{
		phone = new String();
		emails = new TreeSet<String>();
		phoneBegin = -1;
		phoneEnd = -1;
		emailBegin = -1;
	}
	
	public String process(final String str)
	{
		String result;
		result = findAndEditPhone(str);
		savePhone(result);
		emailParse(result);
		return result;
	}
	
	public String getPhone()
	{
		return phone;
	}
	
	public TreeSet<String> getEmails()
	{
		return emails;
	}
	
	public void reset()
	{
		phone = "";
		emails.clear();
		phoneBegin = -1;
		phoneEnd = -1;
		emailBegin = -1;
	}
	
	private String findAndEditPhone(final String str)
	{
		int indexOfAt = str.indexOf('@');
		if (indexOfAt == -1)
			return str;
		int lastPhoneSpace = str.lastIndexOf(' ', indexOfAt);
		if (lastPhoneSpace == -1)
			return str;
		emailBegin = lastPhoneSpace + 1;
		phoneBegin = str.substring(0, lastPhoneSpace).indexOf('+');
		if (phoneBegin == -1)
			return str;
		
		int cur = phoneBegin + 1;
		int codeBegin = phoneBegin;
		int codeEnd = phoneBegin;
		boolean isPhoneFound = false;
		while (!isPhoneFound && cur < lastPhoneSpace)
		{			
			char c = str.charAt(cur);
			if (c == '(')
				codeBegin = cur;
			else if (c == ')')
				codeEnd = cur + 1;
			isPhoneFound = (!Character.isDigit(c)) && (c != ' ') && (c != '(') && (c != ')');
			boolean[] arr = {
				!Character.isDigit(c),
				(c != ' '),
				(c != '('),
				(c != ')')
			};
			++cur;
		}
		phoneEnd = cur;
		
		String code = str.substring(codeBegin, codeEnd);
		code = code.replace("101", "401");
		code = code.replace("202", "802");
		code = code.replace("301", "321");
		
		return str.substring(0, codeBegin) + code + str.substring(codeEnd);
	}
	
	private void savePhone(String str)
	{
		StringBuilder builder = new StringBuilder();
		char c;
		for (int i = phoneBegin; i < phoneEnd; ++i)
		{
			c = str.charAt(i);
			if (Character.isDigit(c) || (c == '+') || (c == '(') || (c == ')'))
				builder.append(c);
		}
		phone = builder.toString();
	}
	
	private void emailParse(final String str)
	{
		if (emailBegin == -1)
			return;
		StringTokenizer tokenizer =
			new StringTokenizer(str.substring(emailBegin), " \t,;");
		while (tokenizer.hasMoreTokens())
			emails.add(tokenizer.nextToken());
	}
	
	private String phone;
	private TreeSet<String> emails;
	private int phoneBegin;
	private int phoneEnd;
	private int emailBegin;
}
