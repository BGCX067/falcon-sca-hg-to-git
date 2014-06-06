


def lookup() {
    def fighter = datastore.execute {
        select single from Fighter
        where scaName == params["name"]
    }
    println "Returned ${fighter?.scaName}"
    return fighter
}

def execute() {
    def fighter = lookup()
    json {
        scaName fighter?.scaName
    }
    return json
}



//def fighter = [:]
//fighter.scaName = params["name"]
//def service = new GetFighterImpl()
//service.params = params
//service.datastore = datastore
//service.init(datastore, params)
//execute()

/*
println params["name"]
def fighter = datastore.execute {
select single from Fighter
where scaName == params["name"]
}
json {
scaName fighter?.scaName
}
println "Returned ${fighter?.scaName}"
 */