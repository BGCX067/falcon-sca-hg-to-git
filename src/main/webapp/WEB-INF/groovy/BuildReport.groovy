/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


logger.BuildReport.info "logging an info message"
def text = ""
params.keySet().each {
	logger.BuildReport.info it + ":" + params[it]
	text += it + ":" + params[it]
}


mail.send from: "riksca@gmail.com",
	to: "riksca@gmail.com",
	subject: "Hello",
	textBody: text
