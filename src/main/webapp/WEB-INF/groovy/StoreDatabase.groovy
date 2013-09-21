import org.sca.calontir.cmpe.utils.MarshalUtils
import org.sca.calontir.cmpe.common.*
import com.google.appengine.api.blobstore.BlobKey
import com.google.appengine.api.datastore.*
import org.sca.calontir.cmpe.common.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*


Kingdom.values().each { workingKingdom ->
    logger.StoreDatabase.info "Storing database for " + workingKingdom.toString()
    def fighters = datastore.iterate {
        select all from Fighter
        where status != "DELETED"
        and kingdom == workingKingdom.toString()
        restart automatically
    }

    def file = files.createNewBlobFile("text/json", workingKingdom.toString() + ".json")

    def json = new groovy.json.JsonBuilder()

    def mapList = []
    fighters.each { fighter ->
        logger.StoreDatabase.info "Storing " + fighter.scaName
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
        def name = workingKingdom.toString() + ".snapshotkey"
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
}

html.html {
    body {
        p "Done"
    }
}
