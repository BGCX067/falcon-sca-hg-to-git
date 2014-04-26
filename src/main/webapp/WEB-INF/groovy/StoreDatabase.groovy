import org.sca.calontir.cmpe.utils.MarshalUtils
import org.sca.calontir.cmpe.common.*
import com.google.appengine.api.blobstore.BlobKey
import com.google.appengine.api.datastore.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*


logger.StoreDatabase.info "Storing database"

def fighterCount = datastore.execute {
    select count from Fighter
    where status != FighterStatus.DELETED.toString()
}

logger.StoreDatabase.info "Count returns " + fighterCount

def fighters = datastore.iterate {
    select all from Fighter
    prefetchSize fighterCount
    chunkSize 100
    restart automatically
}

def file = files.createNewBlobFile("text/json", "fighters.json")

def json = new groovy.json.JsonBuilder()

def mapList = []
long savedCount = 0L
for (fighter in fighters) {
    if(fighter.status == FighterStatus.DELETED.toString()) {
        continue
    }
    def fmap = [:]
    fmap.scaName = fighter.scaName
    fmap.id = fighter.key.id
    def authorizations = datastore.execute {
        select all from Authorization
        ancestor fighter.key
    }
    def auths = []
    authorizations.each { a ->
        def auth = new org.sca.calontir.cmpe.dto.Authorization()
        def authType = a.authType.get()
        auth.code = authType.code
        auth.description = authType.description
        auth.date = a.date
        auth.orderValue = authType.orderValue
        auths << auth
    }
    fmap.authorizations = MarshalUtils.getAuthsAsString(auths)
    def group = fighter.scaGroup.get()
    fmap.group = group.groupName
    fmap.status = fighter.status
    fmap.role = fighter.role ?  fighter.role : "USER"
    mapList << fmap
    ++savedCount
}
logger.StoreDatabase.info "mapList size " + mapList.size()

if (savedCount == fighterCount) {
    logger.StoreDatabase.info "Saved " + savedCount + " fighters"
} else {
    logger.StoreDatabase.info "Only found " + savedCount + " fighters. Ending"
    return
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
