import org.sca.calontir.cmpe.db.*;
import org.sca.calontir.cmpe.dto.*;
import com.google.appengine.api.datastore.*
import static com.google.appengine.api.datastore.FetchOptions.Builder.*

def wsh = datastore.execute  {
    select single from 'AuthType'
    where code == 'WSH'
}

def se = datastore.execute  {
    select single from 'AuthType'
    where code == 'SE'
}

def ca = datastore.execute  {
    select single from 'AuthType'
    where code == 'CA'
}

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

    def wshSaved = datastore.execute {
        select single from 'Authorization'
        ancestor f.key
        where authType == wsh.key
    }

    if(!wshSaved) {
        log.info "No wsh auth for " + f.scaName
    }

    if(wshSaved) {
        log.info "Updating auths"
        def seSaved = datastore.execute {
            select single from 'Authorization'
            ancestor f.key
            where authType == se.key
        }


        if(!seSaved) {
            def seAuth = new Entity("Authorization", f.key);
            seAuth.authType = se.key
            seAuth.date = new Date()
            seAuth.save()
        }

        def caSaved = datastore.execute {
            select single from 'Authorization'
            ancestor f.key
            where authType == ca.key
        }

        if(!caSaved) {
            def caAuth = new Entity("Authorization", f.key);
            caAuth.authType = ca.key
            caAuth.date = new Date()
            caAuth.save()
        }
    }
}

FighterCache.getInstance().clear()