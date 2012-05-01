/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sca.calontir.cmpe.groovy

import org.sca.calontir.cmpe.dto.Fighter
import org.sca.calontir.cmpe.dto.FighterListItem
import org.sca.calontir.cmpe.dto.Authorization
import com.google.appengine.api.blobstore.BlobInfo
import com.google.appengine.api.blobstore.BlobKey
import org.sca.calontir.cmpe.utils.MarshalUtils

/**
 *
 * @author rikscarborough
 */
class Storage {
	public Map getFighterList() {
		println("in getFighterList")
		def xml
		BlobKey blobKey = new BlobKey("WVObekwKG0NAohMCz5hERQ")
		blobKey.withReader { Reader reader ->
			xml = reader.text
		}

		def xmlData = new XmlSlurper().parseText(xml) 

		def fighterList = []
		def dateSaved = Date.parse("E MMM dd HH:mm:ss z yyyy", xmlData["@updated"].text())
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
		
		return ["saveDate" : dateSaved , "fighterList" : fighterList]
	}
}

