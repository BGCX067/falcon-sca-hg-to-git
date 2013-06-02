import org.sca.calontir.cmpe.user.Security
import org.sca.calontir.cmpe.user.SecurityFactory
import org.sca.calontir.cmpe.dto.Fighter
import org.sca.calontir.cmpe.db.FighterDAO
import org.sca.calontir.cmpe.db.ScaGroupDAO
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.blobstore.BlobInfo
import com.google.appengine.api.blobstore.BlobKey
import org.sca.calontir.cmpe.dto.FighterListItem
import org.sca.calontir.cmpe.dto.Authorization
import org.sca.calontir.cmpe.utils.MarshalUtils
import org.sca.calontir.cmpe.common.*
import com.google.appengine.api.datastore.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*
import groovy.json.JsonSlurper
import org.joda.time.DateTime
import groovy.xml.MarkupBuilder

def getFighterList() {
	def jsonStr
	def blobKeyStr
	namespace.of("system") {
		name = "calontir.snapshotkey"
		def query = new Query("properties")
		query.addFilter("name", Query.FilterOperator.EQUAL, name)
		PreparedQuery preparedQuery = datastore.prepare(query)
		def entities = preparedQuery.asList( withLimit(10) )
		def entity = entities[0]
		blobKeyStr = entity.property
	}
	BlobKey blobKey = new BlobKey(blobKeyStr)
	blobKey.withReader { Reader reader ->
		jsonStr = reader.text
	}

	return jsonStr
}

groupsDao = new ScaGroupDAO()
def scaGroups = [:]
def groups = groupsDao.getScaGroup()
groups.each {group ->
	scaGroups[group.groupName] = []
}

Security security = SecurityFactory.getSecurity()
def jsonStr = getFighterList()

def jsonObj = new JsonSlurper().parseText(jsonStr)

dao = new FighterDAO()

dt = new DateTime(jsonObj.dateSaved)
def fighters = dao.getFighterListItems(dt)
mapList = jsonObj.scaNames
//Create map of names
scaNameMap = [:]
mapList.each {
	scaNameMap[it.scaName] = it
	it.status = FighterStatus.valueOf(it.status)
}

fighters.each {
	def fmap = [:]
	fmap.scaName = it.scaName
	fmap.id = it.fighterId
	fmap.authorizations = it.authorizations
	if(it.group) {
		fmap.group = it.group
	} else {
		fmap.group = "Unknown or Out of Kingdom"
	}
	fmap.status = it.status

	if(scaNameMap.containsKey(it.scaName)) {
		tempF = scaNameMap[it.scaName]
		mapList.remove(tempF)
		scaNameMap.remove(it.scaName)
	}

	mapList << fmap
}

mapList.each { f ->
	scaGroups[f.group]?.add(f)
}
scaGroups.sort()

StringWriter writer = new StringWriter()
def build = new MarkupBuilder(writer)
build.html{
	head{
		title('Report')
	}
	body{
		scaGroups.keySet().each { key ->
			p() {
				h2(key)
				fList = scaGroups[key]
				if(!fList.isEmpty()) {
					fList.sort{it.scaName}
					ol() {
						fList.each { f ->
							if(f.status.equals(FighterStatus.ACTIVE)) {
								li(f.scaName)
							}
						}
					}
				}
			}
		}
	}
}
println writer.toString()

def email = security.user.email[0]?.emailAddress

if(email != null) {
	mail.send from: email, to: email, subject: "report", htmlBody: writer.toString()
}