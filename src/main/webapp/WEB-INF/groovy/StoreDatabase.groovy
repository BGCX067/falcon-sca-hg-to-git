import groovy.xml.MarkupBuilder
import org.sca.calontir.cmpe.user.Security
import org.sca.calontir.cmpe.user.SecurityFactory
import org.sca.calontir.cmpe.dto.Fighter
import org.sca.calontir.cmpe.db.FighterDAO
import com.google.appengine.api.datastore.Entity


Security security = SecurityFactory.getSecurity()

// Remove the dao, it will not be able to scan the whole database anymore.
// also, only return active fighters. Inactive will have another getter.
dao = new FighterDAO()

fighters = dao.getFighters()

def file = files.createNewBlobFile("text/xml", "fighters.xml")

file.withWriter {writer ->
    def xml = new MarkupBuilder(writer)
    xml.fighters(updated:new Date()){
        fighters.each { f ->
            xml.fighter() {
                xml.fighterId(f.fighterId)
                xml.scaName(f.scaName)
                xml.scaMemberNo(f.scaMemberNo)
                xml.modernName(f.modernName)
                xml.dateOfBirth(f.dateOfBirth)
                xml.googleId(f.googleId)
                xml.emails {
                    f.email.each {e ->
                        xml.email {
                            xml.emailAddress(e.emailAddress)
                            xml.type(e.type)
                        }
                    }
                }
                xml.addresses {
                    f.address.each {add ->
                        xml.address {
                            xml.address1(add.address1)
                            xml.address2(add.address2)
                            xml.city(add.city)
                            xml.district(add.district)
                            xml.postalCode(add.postalCode)
                            xml.state(add.state)
                            xml.type(add.type)
                        }
                    }
                }
                xml.phones {
                    f.phone.each { p ->
                        xml.phone {
                            xml.phoneNumber(p.phoneNumber)
                            xml.type(p.type)
                        }
                    }
                }
                xml.Authorizations {
                    f.authorization.each { auth ->
                        xml.authorization { 
                            xml.code(auth.code)
                            xml.description(auth.description)
                            xml.date(auth.date)
                        }
                    }
                }
                xml.scaGroup {
                    xml.groupName(f.scaGroup?.groupName)
                    xml.groupLocation(f.scaGroup?.groupLocation)
                }
                xml.role(f.role)
                xml.status(f.status)
                xml.treaty {
                    if(f.treaty != null) {
                        xml.treatyId(f.treaty.treatyId.id)
                        xml.name(f.treaty.name)
                    }
                }
            }
        }
    }
}

Entity sysTable = new Entity("SystemTable")
sysTable.name = "snapshotkey"
sysTable.property = file.blobKey.keyString

html.html {
    body {
        p "Done"
        p file.toString()
        p file.blobKey.keyString
    }
}
