
logger.StoreDatabase.info "Storing database Test"

def fighterCount = datastore.execute {
    select count from Fighter
    where status != "DELETED"
}

logger.StoreDatabase.info "Count returns " + fighterCount


def fighters = datastore.iterate {
    select all from Fighter
    where status != "DELETED"
    restart automatically
}

logger.StoreDatabase.info "Query returns count " + fighters.size()