import org.sca.calontir.cmpe.user.Security
import org.sca.calontir.cmpe.user.SecurityFactory
import org.sca.calontir.cmpe.dto.Fighter
import org.sca.calontir.cmpe.db.FighterDAO
import org.sca.calontir.cmpe.groovy.Storage
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.blobstore.BlobInfo
import com.google.appengine.api.blobstore.BlobKey
import org.sca.calontir.cmpe.dto.FighterListItem
import org.sca.calontir.cmpe.dto.Authorization
import org.sca.calontir.cmpe.utils.MarshalUtils
import com.google.appengine.api.datastore.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*

def getFighterList() {
	def xml
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
		xml = reader.text
	}

	def xmlData = new XmlSlurper().parseText(xml) 

	def fighterList = []
	def dateSaved = Date.parse("E MMM dd HH:mm:ss z yyyy", xmlData["@updated"].text())
	def timeStamp = dateSaved.time
	xmlData.fighter.each { 
		FighterListItem fli = new FighterListItem();
		fli.fighterId = it.fighterId.text().toLong()
		fli.scaName = it.scaName.text()
		fli.group = it.scaGroup.groupName.text()
		List<Authorization> authList = new ArrayList<Authorization>()
		it.Authorizations.authorization.each { authorization ->
			Authorization auth = new Authorization()
			auth.code = authorization.code.text()
			auth.description = authorization.description.text()
			authList << auth
		}
		fli.authorizations = MarshalUtils.getAuthsAsString(authList)
		if(it.dateOfBirth.text()) {
			def dob = Date.parse("E MMM dd HH:mm:ss z yyyy", it.dateOfBirth.text())
			fli.minor = MarshalUtils.isMinor(dob)
		}

		fighterList << fli
	}
		
	return ["saveDate" : timeStamp , "fighterList" : fighterList]
}

def json = new groovy.json.JsonBuilder()
Security security = SecurityFactory.getSecurity()
def retMap = getFighterList()
def flist = retMap["fighterList"]
def saved = retMap["saveDate"]

def mapList = []
flist.each {
	def fmap = [:]
	fmap.scaName = it.scaName
	fmap.id = it.fighterId
	fmap.authorizations = it.authorizations
	fmap.group = it.group
	mapList << fmap
}


def root = json {
	dateSaved saved
	scaNames mapList
}

out <<  json.toString()
