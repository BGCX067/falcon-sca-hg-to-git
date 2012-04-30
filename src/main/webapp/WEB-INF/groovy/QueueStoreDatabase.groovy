/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//defaultQueue.add countdownMillis: 1000, url: "/StoreDatabase.groovy",
//		taskName: "storeDatabase",
//		method: 'GET', retryOptions: RetryOptions.Builder.withDefaults()

defaultQueue << [
	countdownMillis: 1000, url: "/StoreDatabase.groovy",
	taskName: "storeDatabase",
	method: 'GET',
	retryOptions: [
		taskRetryLimit: 10,
		taskAgeLimitSeconds: 100,
		minBackoffSeconds: 40,
		maxBackoffSeconds: 50,
		maxDoublings: 15
	]
]


html.html {
    body {
        p "Done"
        p file.toString()
        p file.blobKey.keyString
    }
}