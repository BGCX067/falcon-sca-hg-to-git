package org.sca.calontir.cmpe.db;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Text;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.NotImplementedException;
import org.joda.time.DateTime;
import org.sca.calontir.cmpe.dto.Report;

/**
 *
 * @author rikscarborough
 */
public class ReportDAO {

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    public List<Report> select() {
        List<Report> retList = new ArrayList<>();
        Query q = new Query("Report").addSort("dateEntered", SortDirection.DESCENDING);
        PreparedQuery pq = datastore.prepare(q);
        for (Entity entity : pq.asQueryResultIterable()) {
            Report report = new Report();
            report.setId(entity.getKey().getId());
            report.setGoogleId((String) entity.getProperty("googleId"));
            String dateEnteredString = (String) entity.getProperty("dateEntered");
            DateTime dt = new DateTime(dateEnteredString);
            report.setDateEntered(dt.toDate());
            report.setMarshalId((Long) entity.getProperty("marshalId"));
            report.setMarshalName((String) entity.getProperty("marshalName"));
            report.setReportType((String) entity.getProperty("reportType"));
            Filter reportKeyFilter = new FilterPredicate("reportKey", FilterOperator.EQUAL, entity.getKey());
            Query iQ = new Query("ReportParams").setFilter(reportKeyFilter);
            PreparedQuery iPq = datastore.prepare(iQ);
            Map<String, String> reportParams = new HashMap<>();
            for (Entity e : iPq.asQueryResultIterable()) {
                final Object value = e.getProperty("value");
                String strValue;
                if (value instanceof Text) {
                    strValue = ((Text) value).getValue();
                } else if (value instanceof String) {
                    strValue = (String) value;
                } else {
                    strValue = value.toString();
                }
                reportParams.put(e.getProperty("name").toString(), strValue);
            }
            report.setReportParams(reportParams);
            retList.add(report);
        }

        return retList;
    }

    public void insert(Report report) {
        throw new NotImplementedException("Not Implemented yet");
    }

    public void update(Report report) {
        throw new NotImplementedException("Not Implemented yet");
    }

    public void delete(Report report) {
        Key k = KeyFactory.createKey("Report", report.getId());
        Filter reportKeyFilter = new FilterPredicate("reportKey", FilterOperator.EQUAL, k);
        Query iQ = new Query("ReportParams").setFilter(reportKeyFilter);
        PreparedQuery iPq = datastore.prepare(iQ);
        for (Entity e : iPq.asQueryResultIterable()) {
            datastore.delete(e.getKey());
        }
        datastore.delete(k);
    }
}
