/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rikscarborough
 */

import org.sca.calontir.cmpe.db.AuthTypeDAO

dao = new AuthTypeDAO()

def authList = dao.getAuthType()

authList.each {
    dao.saveAuthType(it)
}
