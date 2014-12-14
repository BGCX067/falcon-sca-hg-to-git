package org.sca.calontir.cmpe.db;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        Query q = new Query("Report").addSort("dateEntered", SortDirection.DESCENDING);

        return executeQuery(q);
    }

    public List<Report> selectByRegion(List<String> groupNames) {
        final Query reportIdQuery = new Query("ReportParams");
        reportIdQuery.setFilter(new FilterPredicate("value", FilterOperator.IN, groupNames));
        final PreparedQuery reportIdPQ = datastore.prepare(reportIdQuery);
        final List<Report> reports = new ArrayList<>();
        for (final Entity reportParamsEntity : reportIdPQ.asQueryResultIterable()) {
            final Key reportKey = (Key) reportParamsEntity.getProperty("reportKey");
            Entity entity;
            try {
                entity = datastore.get(reportKey);
                reports.add(buildReportFromEntity(entity));
            } catch (EntityNotFoundException ex) {
                Logger.getLogger(ReportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return reports;
    }

    public List<Report> getForDays(Integer days) {
        final Query q = new Query("Report").addSort("dateEntered", SortDirection.DESCENDING);
        final DateTime dt = new DateTime().minusDays(days);
        final String dateEnteredString = dt.toString();
        final Filter reportKeyFilter = new FilterPredicate("dateEntered", FilterOperator.GREATER_THAN_OR_EQUAL, dateEnteredString);
        q.setFilter(reportKeyFilter);

        return executeQuery(q);
    }

    public List<Report> getForRegionAndDays(Integer days, List<String> groupNames) {
        final Query reportIdQuery = new Query("ReportParams");
        final Filter valueFilter = new FilterPredicate("value", FilterOperator.IN, groupNames);
        final DateTime dt = new DateTime().minusDays(days);
        final String dateEnteredString = dt.toString();
        final Filter reportKeyFilter = new FilterPredicate("dateEntered", FilterOperator.GREATER_THAN_OR_EQUAL, dateEnteredString);
        final Filter filter = new Query.CompositeFilter(Query.CompositeFilterOperator.AND, Arrays.asList(valueFilter, reportKeyFilter));
        reportIdQuery.setFilter(filter);

        final PreparedQuery reportIdPQ = datastore.prepare(reportIdQuery);
        final List<Report> reports = new ArrayList<>();
        for (final Entity reportParamsEntity : reportIdPQ.asQueryResultIterable()) {
            final Key reportKey = (Key) reportParamsEntity.getProperty("reportKey");
            Entity entity;
            try {
                entity = datastore.get(reportKey);
                reports.add(buildReportFromEntity(entity));
            } catch (EntityNotFoundException ex) {
                Logger.getLogger(ReportDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return reports;
    }

    private Report buildReportFromEntity(Entity entity) {
        final Report report = new Report();
        report.setId(entity.getKey().getId());
        report.setGoogleId((String) entity.getProperty("googleId"));
        final String dateEnteredString = (String) entity.getProperty("dateEntered");
        final DateTime dt = new DateTime(dateEnteredString);
        report.setDateEntered(dt.toDate());
        report.setMarshalId((Long) entity.getProperty("marshalId"));
        report.setMarshalName((String) entity.getProperty("marshalName"));
        report.setReportType((String) entity.getProperty("reportType"));
        final Filter reportKeyFilter = new FilterPredicate("reportKey", FilterOperator.EQUAL, entity.getKey());
        final Query iQ = new Query("ReportParams").setFilter(reportKeyFilter);
        final PreparedQuery iPq = datastore.prepare(iQ);
        final Map<String, String> reportParams = new HashMap<>();
        for (final Entity e : iPq.asQueryResultIterable()) {
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

        return report;
    }

    private List<Report> executeQuery(Query q) {
        final List<Report> retList = new ArrayList<>();
        final PreparedQuery pq = datastore.prepare(q);
        for (final Entity entity : pq.asQueryResultIterable()) {
            retList.add(buildReportFromEntity(entity));
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
