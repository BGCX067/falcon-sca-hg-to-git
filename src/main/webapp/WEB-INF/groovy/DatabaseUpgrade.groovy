import org.sca.calontir.cmpe.db.*;
import org.sca.calontir.cmpe.dto.*;
import com.google.appengine.api.datastore.*
import org.sca.calontir.cmpe.common.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*

def fightersInterater = datastore.iterate {
    select all from 'Fighter'
    restart automatically
}


fightersInterater.each { f ->
    if(!f.kingdom) {
        if(f.scaName.toUpperCase().startsWith("TEST")) {
            log.info f.scaName + " to " + Kingdom.Test.toString()
            f.kingdom = Kingdom.Test.toString()
        } else {
            log.info f.scaName + " to " + Kingdom.Calontir.toString()
            f.kingdom = Kingdom.Calontir.toString()
        }
        f.save()
    } else {
        log.info f.scaName + " already in " + f.kingdom
    }
}

FighterCache.getInstance().clear()


def authTypeInterater = datastore.iterate {
    select all from 'AuthType'
    restart automatically
}

authTypeInterater.each { authType ->
    if(!authType.kingdom) {
        authType.kingdom = Kingdom.Calontir.toString()
        authType.save()
    }
}
