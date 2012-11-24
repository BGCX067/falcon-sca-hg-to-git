import org.sca.calontir.cmpe.user.Security
import org.sca.calontir.cmpe.user.SecurityFactory
import org.joda.time.DateTime
import groovy.xml.MarkupBuilder
import com.google.appengine.api.datastore.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*
import org.sca.calontir.cmpe.db.FighterDAO

def now = new DateTime()
def dao = new FighterDAO()
def kingdom = "Calontir" 
def user = dao.getFighterByGoogleId(params["user.googleid"])
logger.BuildReport.info "Generating report for ${user.scaName}"
def from = user?.email[0].emailAddress
def ccs = params["Email Cc"]?.split(",")
if(!ccs) {
	ccs = []
}
ccs += user?.email[0].emailAddress
def to
def group = user?.scaGroup?.groupName

namespace.of("system") {
	def query = new Query("properties")
	query.addFilter("name", Query.FilterOperator.EQUAL, kingdom.toLowerCase() + ".earlmarshal.email")
	PreparedQuery preparedQuery = datastore.prepare(query)
	def entities = preparedQuery.asList( withLimit(10) )
	if(entities != null && entities.size() > 0) {
		def entity = entities[0]
		ccs += entity.property
	}

	query = new Query("properties")
	query.addFilter("name", Query.FilterOperator.EQUAL, kingdom.toLowerCase() + "." + group?.toLowerCase() + ".email")
	preparedQuery = datastore.prepare(query)
	entities = preparedQuery.asList( withLimit(10) )
	if(entities != null && entities.size() > 0) {
		def entity = entities[0]
		to = entity.property
	}
}

namespace.of(kingdom.toLowerCase()) {
	def reportInfo = new Entity ("Report")
	reportInfo.dateEntered = now.toString()
	reportInfo.reportType = params["Report Type"]
	reportInfo.marshalName = user?.scaName
	reportInfo.marshalId = user?.fighterId
	reportInfo.googleId = user?.googleId
	
	def reportInfoId = reportInfo.save()
	
	params.keySet().each {
		def reportParams = new Entity("ReportParams")
		reportParams.reportKey = reportInfoId
		reportParams.name = it
		reportParams.value = params[it]

		reportParams.save()
	}
}

//backends.run { 

	StringWriter writer = new StringWriter()  
	def build = new MarkupBuilder(writer)  
	build.html{  
		head{  
			title('Marshal Report')
			style(type:"text/css", '''  
			.sect_title {
				text-decoration:underline;
			}
			.sect_body {
			}
			''')
		}  
		body {
			h1 "Marshal Report"

			p {
				h3 ('class':'sect_title', 'style':'display: inline;', "Reporting Period: " )
				span ('class':'sect_body', params["Report Type"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Marshal Type: "  )
				span ('class':'sect_body', params["Marshal Type"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "SCA Name: "  )
				span ('class':'sect_body', params["SCA Name"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Modern First & Last Name: "  )
				span ('class':'sect_body', 'style':'display: inline;', params["Modern Name"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Address: "  )
				span ('class':'sect_body', params["Address"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Phone Number: "  )
				span ('class':'sect_body', params["Phone Number"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Membership Number: "  )
				span ('class':'sect_body', params["SCA Membership No"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Membership Expires: "  )
				span ('class':'sect_body', params["Membership Expires"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Home Group: "  )
				span ('class':'sect_body', params["Group"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Number of Authorized Fighters: "  )
				span ('class':'sect_body', params["Active Fighters"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Number of Minors: "  )
				span ('class':'sect_body', params["Minor Fighters"])
			}

			h3 ('class':'sect_title',  "Activities: "  )
			p ('class':'sect_body', params["Activities"])

			h3 ('class':'sect_title',  "Problems or Injuries: "  )
			p ('class':'sect_body', params["Injury"])

			h3 ('class':'sect_title',  "Fighter Comments "  )
			p ('class':'sect_body', params["Fighter Comments"])

			h3 ('class':'sect_title',  "Summary: "  )
			p ('class':'sect_body', params["Summary"])
		}
	}

logger.BuildReport.info "Sending report for ${params["Report Type"]}: to ${to}, from ${from}, cc ${ccs}"
	
	mail.send from: from,
		to: to,
		cc: ccs,
		subject: "Marshal report for " + params["Report Type"],
		htmlBody: writer.toString()

//}