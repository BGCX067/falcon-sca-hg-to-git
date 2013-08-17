import org.sca.calontir.cmpe.user.Security
import org.sca.calontir.cmpe.user.SecurityFactory
import org.sca.calontir.cmpe.dto.Fighter
import org.sca.calontir.cmpe.db.FighterDAO
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.blobstore.BlobInfo
import com.google.appengine.api.blobstore.BlobKey
import org.sca.calontir.cmpe.dto.FighterListItem
import org.sca.calontir.cmpe.dto.Authorization
import org.sca.calontir.cmpe.utils.MarshalUtils
import org.sca.calontir.cmpe.common.*
import com.google.appengine.api.datastore.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*



def scaGroup = new Entity ("ScaGroup")
scaGroup.groupName = "Lonely Tower"
scaGroup.groupLocation = "North"
scaGroup.save()

scaGroup = new Entity ("ScaGroup")
scaGroup.groupName = "Forgotten Sea"
scaGroup.groupLocation = "Central"
scaGroup.save()

def i = 0
def wsh = new Entity("AuthType")
wsh.code = "WSH"
wsh.description = "Weapon and Shield"
wsh.orderValue = ++i
wsh.save()

def tw = new Entity("AuthType")
tw.code = "TW"
tw.description = "Two Weapons"
tw.orderValue = ++i
tw.save()

def ths = new Entity("AuthType")
ths.code = "THS"
ths.description = "Two Handed Sword"
ths.orderValue = ++i
ths.save()

def pa = new Entity("AuthType")
pa.code = "PA"
pa.description = "Pole Arm"
pa.orderValue = ++i
pa.save()

def sp = new Entity("AuthType")
sp.code = "SP"
sp.description = "Spear"
sp.orderValue = ++i
sp.save()

def marshal = new Entity("AuthType")
marshal.code = "Marshal"
marshal.description = "Marshal"
marshal.orderValue = ++i
marshal.save()

def ct = new Entity("AuthType")
ct.code = "HR/CT"
ct.description = "Heavy Rapier/Cut and Thrust"
ct.orderValue = ++i
ct.save()

def ca = new Entity("AuthType")
ca.code = "CA"
ca.description = "Combat Archery"
ca.orderValue = ++i
ca.save()

def se = new Entity("AuthType")
se.code = "SE"
se.description = "Siege Engines"
se.orderValue = ++i
se.save()

def json = """
"""

namespace.of("system") {
	def name = "calontir.validStart"

	Entity sysTable = new Entity("properties")
	sysTable.name = name
	sysTable.property = "06/16/2012"

	sysTable.save()

	name = "calontir.validEnd"

	sysTable = new Entity("properties")
	sysTable.name = name
	sysTable.property = "08/31/2012"

	sysTable.save()

	sysTable = new Entity("properties")
	sysTable.name = "calontir.earlmarshal.email"
	sysTable.property = "riksca@gmail.com"

	sysTable.save()

	sysTable = new Entity("properties")
	sysTable.name = "calontir.central.email"
	sysTable.property = "brendanmacantsaoir@gmail.com"

	sysTable.save()

	sysTable = new Entity("properties")
	sysTable.name = "calontir.from.email"
	sysTable.property = "brendanmacantsaoir@gmail.com"

	sysTable.save()
}

// add myself so I can use the system
def fighter = new Entity("Fighter")
fighter.scaName = "Brendan Mac an tSaoir"
fighter.scaMemberNo = "22222"
fighter.modernName = "Rik Scarborough"
fighter.googleId = "riksca@gmail.com"
//fighter.authorization = [wsh.key, tw.key, ths.key, pa.key, sp.key, marshal.key]
//fighter.email = ["riksca@gmail.com"]
fighter.scaGroup = scaGroup.key
fighter.role = UserRoles.CARD_MARSHAL.toString()
fighter.status = "ACTIVE"
fighter.lastUpdated = new Date();

fighter.save()


forward "/StoreDatabase.groovy"