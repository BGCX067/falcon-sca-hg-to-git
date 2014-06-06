package org.sca.calontir.cmpe.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.sca.calontir.cmpe.dto.Authorization;
import org.sca.calontir.cmpe.dto.Fighter;

/**
 *
 * @author rik
 */
public class MarshalUtils {

    public static boolean isMinor(Fighter fighter) {
        if (fighter == null) {
            return false;
        }
        return isMinor(fighter.getDateOfBirth());
    }

    public static boolean isMinor(String fightersBirthDate) {
        if (StringUtils.isBlank(fightersBirthDate)) {
            return false;
        }
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy");
        DateTime dt = dtf.parseDateTime(fightersBirthDate);
        return isMinor(dt.withTimeAtStartOfDay());
    }

    public static boolean isMinor(Date fightersBirthDate) {
        DateTime birthday = new DateTime(fightersBirthDate).withTimeAtStartOfDay();
        return isMinor(birthday);
    }

    public static boolean isMinor(DateTime fightersBirthDate) {
        if (fightersBirthDate == null) {
            return false;
        }
        DateTime today = new DateTime().withTimeAtStartOfDay();
        Years age = Years.yearsBetween(fightersBirthDate, today);
        boolean retVal = false;
        if (age.isLessThan(Years.years(18))) {
            retVal = true;
        }
        return retVal;
    }

    public static boolean hasAll(Fighter fighter) {
        boolean wsh = false,
                pa = false,
                ths = false,
                tw = false,
                sp = false;

        for (Authorization a : fighter.getAuthorization()) {
            switch (a.getCode()) {
                case "WSH":
                    wsh = true;
                    break;
                case "PA":
                    pa = true;
                    break;
                case "THS":
                    ths = true;
                    break;
                case "TW":
                    tw = true;
                    break;
                case "SP":
                    sp = true;
                    break;
            }
        }
        return wsh && pa && ths && tw && sp;
    }

    public static boolean hasAuth(String auth, Fighter fighter) {
        for (Authorization a : fighter.getAuthorization()) {
            if (a.getCode().equals(auth)) {
                return true;
            }
        }
        return false;
    }

    public static String getAuthsAsString(final List<Authorization> authorizations) {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        Collections.sort(authorizations, new ComparatorImpl());
        if (authorizations != null) {
            for (Authorization a : authorizations) {
                if (first) {
                    first = false;
                } else {
                    sb.append(" ; ");
                }
                sb.append(a.getCode());
            }
        }

        return sb.toString();
    }

    public static String getAuthDescriptionAsString(final List<Authorization> authorizations) {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        boolean marshal = false;
        Collections.sort(authorizations, new ComparatorImpl());
        if (authorizations != null) {
            for (Authorization a : authorizations) {
                if (a.getCode().equals("Marshal")) {
                    marshal = true;
                } else {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(", ");
                    }
                    sb.append(a.getDescription());
                }
            }
            if (marshal) {
                if (!first) {
                    sb.append(", and ");
                }
                sb.append("is a Marshal");
            }
        }

        return sb.toString();
    }

    private static class ComparatorImpl implements Comparator<Authorization> {

        public ComparatorImpl() {
        }

        @Override
        public int compare(Authorization l, Authorization r) {
            long left = l.getOrderValue() == null ? 99 : l.getOrderValue();
            long right = r.getOrderValue() == null ? 99 : r.getOrderValue();
            return (left < right ? -1 : (left == right ? 0 : 1));
        }
    }
}
