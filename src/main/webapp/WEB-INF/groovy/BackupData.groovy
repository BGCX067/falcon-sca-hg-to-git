import org.sca.calontir.cmpe.user.Security
import org.sca.calontir.cmpe.user.SecurityFactory
import org.sca.calontir.cmpe.dto.Fighter
import org.sca.calontir.cmpe.db.FighterDAO
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
	def entities = preparedQuery.asList( withLimit(10) )
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

def json = new groovy.json.JsonBuilder()
def root = json {
	fighters {
		fighters.each { f ->
			fighter {
				fighterId f.fighterId
				scaName f.scaName
				scaMemberNo f.scaMemberNo
				modernName f.modernName
				dateOfBirth f.dateOfBirth
				googleId f.googleId
				emails {
					f.email.each {e ->
						email {
							emailAddress e.emailAddress
							type e.type
						}
					}
				}
				addresses {
					f.address.each {add ->
						address {
							address1 add.address1
							address2 add.address2
							city add.city
							district add.district
							postalCode add.postalCode
							state add.state
							type add.type
						}
					}
				}
				phones {
					f.phone.each { p ->
						phone {
							phoneNumber p.phoneNumber
							type p.type
						}
					}
				}
				Authorizations {
					authorization { 
						f.authorization.each { auth ->
							code auth.code
							description auth.description
							date auth.date
						}
					}
				}
				scaGroup {
					groupName f.scaGroup?.groupName
					groupLocation f.scaGroup?.groupLocation
				}
				role f.role
				status f.status
				treaty {
					if(f.treaty != null) {
						treatyId f.treaty.treatyId.id
						name f.treaty.name
					}
				}
			}
		}
	}
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