import com.google.appengine.api.backends.BackendServiceFactory

defaultQueue << [
	countdownMillis: 1000, url: "/" + params.task,
	taskName: "task_" + String.format('%tY%<tm%<td%<tH%<tM%<tS', new Date()),
    headers: ["Host": BackendServiceFactory.getBackendService().getBackendAddress("adminb")],
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