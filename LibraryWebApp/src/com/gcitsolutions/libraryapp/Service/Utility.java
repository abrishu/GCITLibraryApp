package com.gcitsolutions.libraryapp.Service;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.util.HtmlUtils;

import com.gcitsolutions.libraryapp.LibraryException.LibraryException;

public class Utility {

	public Utility() {
		// TODO Auto-generated constructor stub
	}

	/*Utility Functions*/
	
	public static String encodeParameter(String s) throws UnsupportedEncodingException{
				return HtmlUtils.htmlEscape(s);
	}
	
	public static Date parseStringToDate(String strDate) throws LibraryException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=null;
		try {
			date=sdf.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			throw new LibraryException("The date was not in the correct format");
		}
		return date;
	}
}
