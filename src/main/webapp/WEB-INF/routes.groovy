get "/EarlMarshalReport", forward: "EarlMarshalReport.groovy"
get "/QueueTask/@taskname", forward: "/WEB-INF/groovy/QueueTask.groovy?task=@taskname.groovy"
// RESTful paths
//post "/createFighter", forward: "/WEB-INF/groovy/post.groovy"
//get "/fighter/@name", forward: "/WEB-INF/groovy/GetFighter.groovy?name=@name"