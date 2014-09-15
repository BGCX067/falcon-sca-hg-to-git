import org.sca.calontir.cmpe.utils.MarshalUtils
import org.sca.calontir.cmpe.common.*
import com.google.appengine.api.blobstore.BlobKey
import com.google.appengine.api.datastore.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*
import groovy.xml.MarkupBuilder


logger.StoreDatabase.info "Earl Marshal Report"

def fighterCount = datastore.execute {
    select count from Fighter
    where status != FighterStatus.DELETED.toString()
}

def activeCount = datastore.execute {
    select count from Fighter
    where status == FighterStatus.ACTIVE.toString()
}

def warrantedMarshals = datastore.execute {
    select count from Fighter
    where status == FighterStatus.ACTIVE.toString()
    and role != null
    and role != UserRoles.USER.toString()
}

def fighterKeys = datastore.iterate {
    select keys from Fighter
    where status == FighterStatus.ACTIVE.toString()
}

def ct = datastore.execute {
    select single from AuthType
    where code == "CT"
}

def ctMarshal = datastore.execute {
    select single from AuthType
    where code == "CT Marshal"
}

def marshal = datastore.execute {
    select single from AuthType
    where code == "Marshal"
}

def ctCount = 0
def ctMarshalCount = 0
def marshalCount = 0
for (key in fighterKeys) {
    ctCount += datastore.execute {
        select count from Authorization
        where authType == ct.key
        ancestor key
    }
    ctMarshalCount += datastore.execute {
        select count from Authorization
        where authType == ctMarshal.key
        ancestor key
    }
    marshalCount += datastore.execute {
        select count from Authorization
        where authType == marshal.key
        ancestor key
    }
}

StringWriter writer = new StringWriter()
def build = new MarkupBuilder(writer)
def row = 1
build.html {
	head {
		title('Report')
        link(href:'css/default.css', type:'text/css', rel:'stylesheet')
        link(href:'css/cmp_007.css', type:'text/css', rel:'stylesheet')
        link(href:'css/report.css', type:'text/css', rel:'stylesheet')
    }

    body {
        div (class: "dataBox") {
            table {
                tr {
                    td(class: "header", colspan: "2", "Earl Marshal Report")
                }
                tr {
                    td(class: "${row%2 == 0 ? 'row_even': 'row_odd'}", "Number of Active, Inactive, and Suspended")
                    td(class: "${row%2 == 0 ? 'row_even': 'row_odd'}", fighterCount)
                    row++
                }
                tr {
                    td(class: "${row%2 == 0 ? 'row_even': 'row_odd'}", "Number of active fighters")
                    td(class: "${row%2 == 0 ? 'row_even': 'row_odd'}", activeCount)
                    row++
                }
                tr {
                    td(class: "${row%2 == 0 ? 'row_even': 'row_odd'}", "Number of Active fighters with CT Authorization")
                    td(class: "${row%2 == 0 ? 'row_even': 'row_odd'}", ctCount)
                    row++
                }
                tr {
                    td(class: "${row%2 == 0 ? 'row_even': 'row_odd'}", "Number of Active fighters with CT Marshal Authorization")
                    td(class: "${row%2 == 0 ? 'row_even': 'row_odd'}", ctMarshalCount)
                    row++
                }
                tr {
                    td(class: "${row%2 == 0 ? 'row_even': 'row_odd'}", "Number of Active fighters with Marshal Authorization")
                    td(class: "${row%2 == 0 ? 'row_even': 'row_odd'}", marshalCount)
                    row++
                }
                tr {
                    td(class: "${row%2 == 0 ? 'row_even': 'row_odd'}", "Number of Warranted Marshals")
                    td(class: "${row%2 == 0 ? 'row_even': 'row_odd'}", warrantedMarshals)
                    row++
                }
            }
        }
        div (style:'width: 90%;') {
            a(class: "buttonLink", href:'/', style:'float: right; padding-right: 1em;', "Home")
        }
    }
}
println writer.toString()
