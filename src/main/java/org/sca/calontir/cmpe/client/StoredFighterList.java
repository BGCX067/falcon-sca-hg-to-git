/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sca.calontir.cmpe.client;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author rikscarborough
 */
public class StoredFighterList implements Serializable {
	List<FighterInfo> fighterInfo;	

	Date dateSaved;

	public Date getDateSaved() {
		return dateSaved;
	}

	public void setDateSaved(Date dateSaved) {
		this.dateSaved = dateSaved;
	}

	public List<FighterInfo> getFighterInfo() {
		return fighterInfo;
	}

	public void setFighterInfo(List<FighterInfo> fighterInfo) {
		this.fighterInfo = fighterInfo;
	}

}
