import org.sca.calontir.cmpe.common.UserRoles
import com.google.appengine.api.datastore.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*
import au.com.bytecode.opencsv.*


final int limitNum = 200
def kingdom = "Calontir"
def toEmail = params["email"]
def role = params["role"]

if (!(role == UserRoles.CARD_MARSHAL.toString() || role == UserRoles.EARL_MARSHAL.toString())) {
    logger.EmailList.warning "Cannot run unless Earl or Card Marshal"
    return
}

def fighterCount = datastore.execute {
    select count from Fighter
}

logger.EmailList.info "Count returns " + fighterCount

def loopTimes = fighterCount.div(limitNum).intValue() + 1

logger.EmailList.info "Looping " + loopTimes + " times"

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
                googeId text: fighter.googleId
            }
        }
        def line = []
        line << fighter.key.id
        line << fighter.scaName
        line << fighter.scaMemberNo
        line << fighter.modernName
        line << fighter.dateOfBirth
        line << fighter.googleId
        def emails = datastore.execute {
            select all from 'Email'
            ancestor fighter.key
        }
        line << emails[0]?.emailAddress
        def addresses = datastore.execute {
            select all from Address
            ancestor fighter.key
        }
        line << addresses[0]?.address1
        line << addresses[0]?.address2
        line << addresses[0]?.city
        line << addresses[0]?.district
        line << addresses[0]?.postalCode
        line << addresses[0]?.state
        def phones = datastore.execute {
            select all from Phone
            ancestor fighter.key
        }
        line << phones[0]?.phoneNumber
        def authorizations = datastore.execute {
            select all from Authorization
            ancestor fighter.key
        }
        authorizations.sort { it.authType.get().orderValue }
        def auths = ""
        authorizations.each { auth ->
            def authType = auth.authType.get()
            if(authType.code.contains(" ")) {
                auths += "'" + authType.code + "' "
            } else {
                auths += authType.code + " "
            }
        }
        line << auths.trim()
        if(fighter.scaGroup) {
            def group = fighter.scaGroup.get()
            line << group.groupName
        } else {
            line << "Unknown or Out of Kingdom"
        }
        line << fighter.role
        line << fighter.status
        line << fighter.treaty?.name
        mapList << line
        ++savedCount
    }
}

if (savedCount != fighterCount) {
    logger.EmailList.warning "Only found " + savedCount + " fighters. Ending"
    return
}

Writer writer = new StringWriter()
def w = new CSVWriter(writer)
mapList.each { fighter ->
    String[] line = fighter
    w.writeNext(line)

}

def from

namespace.of("system") {
	query = new Query("properties")
	query.addFilter("name", Query.FilterOperator.EQUAL, kingdom.toLowerCase() + ".from.email")
	preparedQuery = datastore.prepare(query)
	entities = preparedQuery.asList( withLimit(10) )
	if(entities != null && entities.size() > 0) {
		def entity = entities[0]
		from = entity.property
	}
}

logger.EmailList.info "Sending fighter list to ${toEmail}"

mail.send from: from,
to: toEmail,
subject: "Fighter List",
textBody: "Fighter List",
attachment: [data: writer.toString().bytes, fileName: "fighterlist.csv"]

html.html {
	body {
		p writer.toString()
	}
}
