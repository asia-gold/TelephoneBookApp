package ru.asia.mytelephonebookapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ContactsUtils {	

	public static String formatDateToString(Date date) {
		String dateString = null;
		if (date == null) {
			dateString = "";
		} else {
			String dateFormat = "dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat,
					Locale.ENGLISH);
			dateString = sdf.format(date);
		}
		return dateString;
	}

	public static Date formatStringToDate(String dateString)
			throws ParseException {
		Date date = new Date();
		if (dateString == null) {
			date = null;
		} else {
			String dateFormat = "dd/MM/yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat,
					Locale.ENGLISH);
			date = sdf.parse(dateString);
		}
		return date;
	}
	
}
