package gov.va.oneva;

import gov.va.med.cds.OutpatientMedicationPromise;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

/**
 * Sorts the list of active prescriptions for a patient with the newest prescription being the first one.
 * 
 * @author tburleson
 */
public class HdrComparator implements Comparator<OutpatientMedicationPromise> {

	@Override
	public int compare(OutpatientMedicationPromise rx1, OutpatientMedicationPromise rx2) {
		// the dates will be in 1 of 2 formats: YYYYMMDD or YYYYMMDDhhmmss +- TZ offset
		// 20161110
		// 20140418183901-0700
		// do I convert all dates to GMT?  A question to consider...
		// if comparing two different dates, they all must be the same timezone.
		String[] fmts = {
			""	// got the formats from Narasa
		};
		Date rx1Date = new Date();
		Date rx2Date = new Date();
		try {
			rx1Date = DateUtils.parseDate(rx1.getLastDispenseDate().getLiteral(), fmts);
			rx2Date = DateUtils.parseDate(rx2.getLastDispenseDate().getLiteral(), fmts);
			return DateUtils.truncatedCompareTo(rx1Date, rx2Date, Calendar.SECOND);
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

}
