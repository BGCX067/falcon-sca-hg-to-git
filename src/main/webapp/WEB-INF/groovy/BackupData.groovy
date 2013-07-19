import org.sca.calontir.cmpe.user.Security
import org.sca.calontir.cmpe.user.SecurityFactory
import org.sca.calontir.cmpe.dto.Fighter
import org.sca.calontir.cmpe.db.FighterDAO
import org.sca.calontir.cmpe.db.ReportDAO
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.blobstore.BlobKey
import com.google.appengine.api.datastore.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*


def runToday = false

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

// Remove the dao, it will not be able to scan the whole database anymore.
// also, only return active fighters. Inactive will have another getter.
dao = new FighterDAO()

fighters = dao.getFighters()

def mapList = []
fighters.each { f ->
	def fmap = [:]
	fmap.fighterId = f.fighterId
	fmap.scaName = f.scaName
	fmap.scaMemberNo = f.scaMemberNo
	fmap.modernName = f.modernName
	fmap.dateOfBirth = f.dateOfBirth
	fmap.googleId = f.googleId
	def emailList = []
	f.email.each {email ->
		def emailMap = [:]
		emailMap.emailAddress = email.emailAddress
		emailMap.type = email.type
		emailList << emailMap
	}
	fmap.email = emailList
	def addressList = []
	f.address.each { address->
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
	def phoneList = []
	f.phone.each {phone ->
		def phoneMap = [:]
		phoneMap.phoneNumber = phone.phoneNumber
		phoneMap.type = phone.type
		phoneList << phoneMap
	}
	fmap.phone = phoneList
	def authList = []
	f.authorization.each { auth ->
		def authMap = [:]
		authMap.code = auth.code
		authMap.description = auth.description
		authMap.date = auth.date
		authList << authMap
	}
	fmap.authorization = authList
	if(f.scaGroup)
    fmap.group = f.scaGroup.groupName
	else
    fmap.group = "Unknown or Out of Kingdom"
	fmap.role = f.role
	fmap.status = f.status
	fmap.treaty = f.treaty?.name
	mapList << fmap
}
def reportList = []
namespace.of("calontir") {
	dao = new ReportDAO();
	reports = dao.select()
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