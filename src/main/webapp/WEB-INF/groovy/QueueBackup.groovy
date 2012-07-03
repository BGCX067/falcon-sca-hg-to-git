//defaultQueue.add countdownMillis: 1000, url: "/StoreDatabase.groovy",
//		taskName: "storeDatabase",
//		method: 'GET', retryOptions: RetryOptions.Builder.withDefaults()

defaultQueue << [
	countdownMillis: 1000, url: "http://adminb.calon-marshal-project-engine.appspot.com/BackupData.groovy",
	taskName: "backup" + String.format('%tY%<tm%<td%<tH%<tM%<tS', new Date()),
	method: 'GET',
	retryOptions: [
		taskRetryLimit: 1,
		taskAgeLimitSeconds: 100,
		minBackoffSeconds: 40,
		maxBackoffSeconds: 50,
		maxDoublings: 15
	]
]


html.html {
    body {
        p "Done"
    }
}