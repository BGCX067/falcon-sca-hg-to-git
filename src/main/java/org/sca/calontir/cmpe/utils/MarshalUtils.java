package org.sca.calontir.cmpe.utils;

import java.util.Date;
import java.util.List;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Years;
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

    public static boolean isMinor(Date fightersBirthDate) {
        if (fightersBirthDate == null) {
            return false;
        }
        DateMidnight birthday = new DateMidnight(fightersBirthDate);
        DateTime today = new DateTime();
        Years age = Years.yearsBetween(birthday, today);
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
            if (a.getCode().equals("WSH")) {
                wsh = true;
            } else if (a.getCode().equals("PA")) {
                pa = true;
            } else if (a.getCode().equals("THS")) {
                ths = true;
            } else if (a.getCode().equals("TW")) {
                tw = true;
            } else if (a.getCode().equals("SP")) {
                sp = true;
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

    public static String getAuthsAsString(List<Authorization> authorizations) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
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

    public static String getAuthDescriptionAsString(List<Authorization> authorizations) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        boolean marshal = false;
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
}
