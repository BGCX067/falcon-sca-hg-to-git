import com.google.appengine.api.datastore.Entity
import org.sca.calontir.cmpe.dto.Fighter
import org.sca.calontir.cmpe.db.FighterDAO
import org.sca.calontir.cmpe.print.CardMaker
import org.joda.time.DateTime
import com.google.appengine.api.datastore.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*

Entity fighter = new Entity("Fighter");

fighter << params

def dao = new FighterDAO()
def f = dao.getFighter(fighter.fighterId.toInteger())
def startDate
def endDate

namespace.of("system") {
	name = "calontir.validStart"
	def query = new Query("properties")
	query.addFilter("name", Query.FilterOperator.EQUAL, name)
	PreparedQuery preparedQuery = datastore.prepare(query)
	def entities = preparedQuery.asList( withLimit(10) )
	def entity = entities[0]
	startDate = new Date().parse("MM/dd/yyyy", entity.property)

	name = "calontir.validEnd"
	query = new Query("properties")
	query.addFilter("name", Query.FilterOperator.EQUAL, name)
	preparedQuery = datastore.prepare(query)
	entities = preparedQuery.asList( withLimit(10) )
	entity = entities[0]
	endDate = new Date().parse("MM/dd/yyyy", entity.property)
}

ByteArrayOutputStream baosPDF = new ByteArrayOutputStream()
CardMaker cardMaker = new CardMaker()
List<Fighter> flist = new ArrayList<Fighter>()
flist.add(f)
try {
	cardMaker.build(baosPDF, flist, new DateTime(startDate), new DateTime(endDate))
} catch (Exception ex) {
	throw new IOException("Error building the cards", ex)
}

response.headers.'Content-disposition' = "attachment; filename=FighterCard " + 
	f.getScaName() + "-" + String.format('%tF', new Date()) + ".pdf"

response.contentType = "application/pdf"
response.contentLength = baosPDF.size()

sout << baosPDF