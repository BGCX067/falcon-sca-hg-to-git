import groovy.xml.MarkupBuilder

def xml = new MarkupBuilder(response.getWriter())
xml.fighters(updated:new Date()){
  fighter("Brendan") {
      Authorizations {
          Authorization("WSH")
          Authorization("GS")
      }
  }
  fighter("Brendan") {
      Authorizations {
          Authorization("WSH")
          Authorization("GS")
      }
  }
  fighter("Brendan") {
      Authorizations {
          Authorization("WSH")
          Authorization("GS")
      }
  }
}