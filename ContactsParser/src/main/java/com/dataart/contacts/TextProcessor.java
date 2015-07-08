package com.dataart.contacts;

import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

class TextProcessor
{
	public TextProcessor()
	{
		phone = "";
		emails = new TreeSet<>();
		phoneBegin = -1;
		phoneEnd = -1;
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

	public SortedSet<String> getEmails()
	{
		return emails;
	}

	public void reset()
	{
		phone = "";
		emails.clear();
		phoneBegin = -1;
		phoneEnd = -1;
	}

	private String findAndEditPhone(final String str)
	{
		int indexOfAt = str.indexOf('@');
		phoneEnd = (indexOfAt != -1) ? str.lastIndexOf(' ', indexOfAt) : (str.length());
		if (phoneEnd == -1)
			return str;
		phoneBegin = str.substring(0, phoneEnd).indexOf('+');
		if (phoneBegin == -1)
			return str;

		int cur = phoneBegin + 1;
		int codeBegin = -1;
		int codeEnd = -1;
		boolean isCodeFound = false;
		while (!isCodeFound && cur < phoneEnd)
		{
			char c = str.charAt(cur);
			if (c == '(')
				codeBegin = cur;
			else if (c == ')')
				codeEnd = cur + 1;
			isCodeFound = (codeBegin != -1) && (codeEnd != -1);
			++cur;
		}
		
		if (isCodeFound && (codeBegin < codeEnd))
		{
			String code = str.substring(codeBegin, codeEnd);
			code = code.replace("(101)", "(401)");
			code = code.replace("(202)", "(802)");
			code = code.replace("(301)", "(321)");
			return str.substring(0, codeBegin) + code + str.substring(codeEnd);
		}
		else
			return str;		
	}

	private void savePhone(final String str)
	{
		if (phoneBegin == -1)
			return;
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
		if (phoneEnd == -1)
			return;
		StringTokenizer tokenizer =
			new StringTokenizer(str.substring(phoneEnd), " \t,;");
		String token;
		while (tokenizer.hasMoreTokens())
		{
			token = tokenizer.nextToken();
			if (token.contains("@") && token.toLowerCase().endsWith(".org"))
				emails.add(token);
		}
	}

	private String phone;
	private SortedSet<String> emails;
	private int phoneBegin;
	private int phoneEnd;
}
