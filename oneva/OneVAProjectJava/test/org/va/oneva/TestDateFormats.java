package org.va.oneva;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDateFormats {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void test() throws ParseException {
		// the dates will be in 1 of 2 formats: 
		// 1) YYYYMMDD
		// 2) YYYYMMDDhhmmss +-TZ
		// 20161110 (assumed to be GMT) -> 11/10/2016
		// 20140418183901-0700 -> 04/18/2014 18:39:01-0700
		String[] fmts = {
				"yyyyMMddHHmmssZ",
				"yyyyMMdd"
		};
		Date d1 = DateUtils.parseDate("20161110", fmts);
		Date d2 = DateUtils.parseDate("20140418183901-0700", fmts);
		
		String fmtString = "MM/dd/yyyy HH:mm:ss Z";
		FastDateFormat fmt = FastDateFormat.getInstance(fmtString, TimeZone.getTimeZone("GMT"));
		System.out.println(fmt.format(d1));
		System.out.println(fmt.format(d2));
	}

}
