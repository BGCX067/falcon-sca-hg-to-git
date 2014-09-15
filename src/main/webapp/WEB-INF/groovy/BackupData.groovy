import org.sca.calontir.cmpe.utils.MarshalUtils
import org.sca.calontir.cmpe.user.Security
import org.sca.calontir.cmpe.user.SecurityFactory
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.blobstore.BlobKey
import com.google.appengine.api.datastore.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*


def runToday = false
final int limitNum = 200

namespace.of("system") {
	name = "calontir.lastbackup"
	def query = new Query("properties")
	query.addFilter("name", Query.FilterOperator.EQUAL, name)
	PreparedQuery preparedQuery = datastore.prepare(query)
	def entities = preparedQuery.asList( withDefaults() )
	if(entities != null && entities.size() > 0) {
		def entity = entities[0]
		Date savedDate = new Date(entity.property).clearTime()
		Date today = new Date().clearTime()
		runToday = savedDate == today
	}
}

if(runToday) {
	html.html {
		body {
			p "Back already run today"
		}
	}
	return
}

Security security = SecurityFactory.getSecurity()

def fighterCount = datastore.execute {
    select count from Fighter
}

logger.StoreDatabase.info "Count returns " + fighterCount

def loopTimes = fighterCount.div(limitNum).intValue() + 1

logger.StoreDatabase.info "Looping " + loopTimes + " times"

def index = search.index("fighters")

def mapList = []
long savedCount = 0L
for (i in 0..loopTimes) {

    def fighters = datastore.iterate {
        select all from Fighter
        prefetchSize fighterCount
        chunkSize limitNum
        limit limitNum
        offset i == 0 ? 0 : limitNum * i
        restart automatically
    }

    for (fighter in fighters) {
        def response = index.put {
            document(id: fighter.key.id) {
                scaName text: fighter.scaName
                modernName text: fighter.modernName
            }
        }
        def fmap = [:]
        fmap.fighterId = fighter.key.id
        fmap.scaName = fighter.scaName
        fmap.scaMemberNo = fighter.scaMemberNo
        fmap.modernName = fighter.modernName
        fmap.dateOfBirth = fighter.dateOfBirth
        fmap.googleId = fighter.googleId
        def emailList = []
        def emails = datastore.execute {
            select all from 'Email'
            ancestor fighter.key
        }
        emails.each {email ->
            def emailMap = [:]
            emailMap.emailAddress = email.emailAddress
            emailMap.type = email.type
            emailList << emailMap
        }
        fmap.email = emailList
        def addresses = datastore.execute {
            select all from Address
            ancestor fighter.key
        }
        def addressList = []
        addresses.each { address->
            def addressMap = [:]
            addressMap.address1 = address.address1
            addressMap.address2 = address.address2
            addressMap.city = address.city
            addressMap.district = address.district
            addressMap.postalCode = address.postalCode
            addressMap.state = address.state
            addressMap.type = address.type
            addressList << addressMap
        }
        fmap.address = addressList
        def phones = datastore.execute {
            select all from Phone
            ancestor fighter.key
        }
        def phoneList = []
        phones.each {phone ->
            def phoneMap = [:]
            phoneMap.phoneNumber = phone.phoneNumber
            phoneMap.type = phone.type
            phoneList << phoneMap
        }
        fmap.phone = phoneList
        def authList = []
        def authorizations = datastore.execute {
            select all from Authorization
            ancestor fighter.key
        }
        authorizations.each { auth ->
            def authMap = [:]
            def authType = auth.authType.get()
            authMap.code = authType.code
            authMap.description = authType.description
            authMap.date = auth.date
            authMap.orderValue = authType.orderValue
            authList << authMap
        }
        fmap.authorization = authList
        if(fighter.scaGroup) {
            def group = fighter.scaGroup.get()
            fmap.group = group.groupName
        } else {
            fmap.group = "Unknown or Out of Kingdom"
        }
        fmap.role = fighter.role
        fmap.status = fighter.status
        fmap.treaty = fighter.treaty?.name
        mapList << fmap
        ++savedCount
    }
}

logger.StoreDatabase.info "mapList size " + mapList.size()

if (savedCount == fighterCount) {
    logger.BackupData.info "Saved " + savedCount + " fighters"
} else {
    logger.BackupData.warning "Only found " + savedCount + " fighters. Ending"
    return
}

def reportList = []
namespace.of("calontir") {
    def reports = datastore.execute {
        select all from Report
    }
	reports.each { r ->
		def rmap = [:]
		rmap.id = r.id
		rmap.dateEntered = r.dateEntered
		rmap.reportType = r.reportType
		rmap.marshalName = r.marshalName
		rmap.marshalId = r.marshalId
		rmap.googleId = r.googleId
		rmap.reportParams = r.reportParams
		reportList << rmap
	}
}

def json = new groovy.json.JsonBuilder()
def now = new Date()
def root = json {
	dateBackedUp String.format('%tF %<tT', now.time)
	fighters mapList
	reports reportList
}

def file = files.createNewBlobFile("text/json", "backup.json")

file.withWriter {writer ->
	writer << json.toString()
}

namespace.of("system") {
	def name = "calontir.backupkey"
	def query = new Query("properties")
	query.addFilter("name", Query.FilterOperator.EQUAL, name)
	PreparedQuery preparedQuery = datastore.prepare(query)
	def entities = preparedQuery.asList( withDefaults() )
	entities.each {
		BlobKey blobKey = new BlobKey(it.property)
		blobKey.delete()
		it.delete()
	}

	Entity sysTable = new Entity("properties")
	sysTable.name = name
	sysTable.property = file.blobKey.keyString

	sysTable.save()

	def lastbackupKey = "calontir.lastbackup"
    query = new Query("properties")
	query.addFilter("name", Query.FilterOperator.EQUAL, lastbackupKey)
	preparedQuery = datastore.prepare(query)
	entities = preparedQuery.asList( withDefaults() )
	entities.each {
		it.delete()
	}

	sysTable = new Entity("properties")
	sysTable.name = lastbackupKey
	sysTable.property = new Date().time

	sysTable.save()
}

html.html {
	body {
		p "Done"
		p file.toString()
		p file.blobKey.keyString
	}
}
