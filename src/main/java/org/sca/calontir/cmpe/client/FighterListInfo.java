package org.sca.calontir.cmpe.client;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author rikscarborough
 */
public class FighterListInfo implements Serializable {
    
    //TODO: Change to containing a list of the following information
    // and a flag to represent if the client should update or replace the data.

    List<FighterInfo> fighterInfo;
    boolean updateInfo;

    public List<FighterInfo> getFighterInfo() {
        return fighterInfo;
    }

    public void setFighterInfo(List<FighterInfo> fighterInfo) {
        this.fighterInfo = fighterInfo;
    }

    public boolean isUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(boolean updateInfo) {
        this.updateInfo = updateInfo;
    }
    

}
