package org.sca.calontir.cmpe.utils;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rik
 */
public class MarshalUtils {

    public static boolean isMinor(Fighter fighter) {
        if (fighter == null || fighter.getDateOfBirth() == null) {
            return false;
        }
        DateMidnight birthday = new DateMidnight(fighter.getDateOfBirth());
        DateTime today = new DateTime();
        Years age = Years.yearsBetween(birthday, today);
        boolean retVal = false;
        if (age.isLessThan(Years.years(18))) {
            retVal = true;
        }
        return retVal;
    }
}
