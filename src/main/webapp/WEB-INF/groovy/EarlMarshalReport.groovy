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

def hrct = datastore.execute {
    select single from AuthType
    where code == "HR/CT"
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
        where authType == hrct.key
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
build.html {
	head {
		title('Report')
	}
	body {
        p"Earl Marshal Report"
		p"Number of Active, Inactive, and Suspended ${fighterCount}"
		p"Number of active fighters ${activeCount}"
		p"Number of Active fighters with CR/HT Authorization ${ctCount}"
		p"Number of Active fighters with CT Marshal Authorization ${ctCount}"
		p"Number of Active fighters with Marshal Authorization ${marshalCount}"
		p"Number of Warranted Marshals ${warrantedMarshals}"
    }
}
println writer.toString()
