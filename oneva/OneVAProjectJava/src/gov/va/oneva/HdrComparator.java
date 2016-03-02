package gov.va.oneva;

import gov.va.med.cds.OutpatientMedicationPromise;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sorts the list of active prescriptions for a patient with the newest prescription being the first one.
 * 
 * @author tburleson
 */
public class HdrComparator implements Comparator<OutpatientMedicationPromise> {
	private static final Logger log = LoggerFactory.getLogger(HdrComparator.class);
	
	@Override
	public int compare(OutpatientMedicationPromise rx1, OutpatientMedicationPromise rx2) {
		// the dates will be in 1 of 2 formats: YYYYMMDD or YYYYMMDDhhmmss +- TZ offset
		// 20161110 (assumed to be GMT)
		// 20140418183901-0700
		// do I convert all dates to GMT?  A question to consider...
		// if comparing two different dates, they all must be the same timezone.
		
		// the dates will be in 1 of 2 formats: 
		// 1) YYYYMMDD
		// 2) YYYYMMDDhhmmss +-TZ
		// 20161110 (assumed to be GMT) -> 11/10/2016
		// 20140418183901-0700 -> 04/18/2014 18:39:01-0700
		String[] fmts = {
				"yyyyMMddHHmmssZ",
				"yyyyMMdd"
		};
		// new CompareToBuilder().append(p1.size, p2.size).append(p1.nrOfToppings, p2.nrOfToppings).append(p1.name, p2.name).toComparison();
		/*
		 * CompareToBuilder builder = new CompareToBuilder();
	builder.append(getCode(), o.getCode());
	builder.append(getTimestamp(), o.getTimestamp());
	builder.append(getSequence(), o.getSequence());
	return builder.toComparison();
		 */
		CompareToBuilder builder = new CompareToBuilder();
		
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
