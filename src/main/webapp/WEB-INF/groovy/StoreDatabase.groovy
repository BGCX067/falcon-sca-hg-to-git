import org.sca.calontir.cmpe.user.Security
import org.sca.calontir.cmpe.user.SecurityFactory
import org.sca.calontir.cmpe.dto.Fighter
import org.sca.calontir.cmpe.db.FighterDAO
import org.sca.calontir.cmpe.utils.MarshalUtils
import org.sca.calontir.cmpe.common.*
import com.google.appengine.api.blobstore.BlobKey
import com.google.appengine.api.datastore.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*


Security security = SecurityFactory.getSecurity()

// Remove the dao, it will not be able to scan the whole database anymore.
// also, only return active fighters. Inactive will have another getter.
dao = new FighterDAO()

fighters = dao.getFighters()

def file = files.createNewBlobFile("text/json", "fighters.json")


def json = new groovy.json.JsonBuilder()

def mapList = []
fighters.each {
	if(it.status != FighterStatus.DELETED) {
		def fmap = [:]
		fmap.scaName = it.scaName
		fmap.id = it.fighterId
		fmap.authorizations = MarshalUtils.getAuthsAsString(it.authorization)
		fmap.group = it.scaGroup ? it.scaGroup.groupName : "Unknown or Out of Kingdom"
		fmap.status = it.status
        fmap.role = it.role ?  it.role : "USER"
		mapList << fmap
	}
}

def now = new Date()
def root = json {
	dateSaved now.time
	scaNames mapList
}

file.withWriter {writer ->
	writer << json.toString()
}

namespace.of("system") {
	def name = "calontir.snapshotkey"
	def query = new Query("properties")
	query.addFilter("name", Query.FilterOperator.EQUAL, name)
	PreparedQuery preparedQuery = datastore.prepare(query)
	def entities = preparedQuery.asList( withLimit(10) )
	entities.each {
		BlobKey blobKey = new BlobKey(it.property)
		blobKey.delete()
		it.delete()
	}

	Entity sysTable = new Entity("properties")
	sysTable.name = name
	sysTable.property = file.blobKey.keyString

	sysTable.save()
}

html.html {
    body {
        p "Done"
        p file.toString()
        p file.blobKey.keyString
    }
}
