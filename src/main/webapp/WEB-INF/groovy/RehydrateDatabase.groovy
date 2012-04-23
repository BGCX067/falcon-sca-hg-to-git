import org.sca.calontir.cmpe.user.Security
import org.sca.calontir.cmpe.user.SecurityFactory
import org.sca.calontir.cmpe.dto.Fighter
import org.sca.calontir.cmpe.db.FighterDAO
import org.sca.calontir.cmpe.groovy.Storage
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.blobstore.BlobInfo
import com.google.appengine.api.blobstore.BlobKey


html.html {
    body {
		Security security = SecurityFactory.getSecurity()
		def storage = new Storage()
		def retMap = storage.fighterList
		def flist = retMap["fighterList"]
		def saved = retMap["saveDate"]
        p "Done"
        p "Number of fighters " + flist.size()
		p "Date Saved " + saved
		flist.each {
			p it.fighterId + ", " + it.scaName + ", " + it.authorizations + ", " +
			it.group + ", " + it.minor
		}
    }
}
