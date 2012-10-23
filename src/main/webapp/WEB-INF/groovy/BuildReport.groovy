import org.joda.time.DateTime
import groovy.xml.MarkupBuilder


//backends.run { 
	logger.BuildReport.info "logging an info message"

	StringWriter writer = new StringWriter()  
	def build = new MarkupBuilder(writer)  
	build.html{  
		head{  
			title('Marshal Report')
		}  
		body {
			h1 "Marshal Report"

			p "Reporting Period: " + params["Report Type"]
			p "Marshal Type: " + params["Marshal Type"]
			p "SCA Name: " + params["SCA Name"]
			p "Modern First & Last Name: " + params["Modern Name"]
			p "Address: " + params["Address"]
			p "Phone Number: " + params["Phone Number"]
			p "Membership Number: " + params["SCA Membership No"]
			p "Membership Expires: " + params["Membership Expires"]
			p "Home Group: " + params["Group"]
			p "Number of Authorized Fighters: " + params["Active Fighters"]
			p "Number of Minors: " + params["Minor Fighters"]
			p "Activities: " + params["Activities"]
			p "Problems or Injuries: " + params["Injury"]
			p "Fighter Comments " + params["Fighter Comments"]
			p "Summary: " + params["Summary"]

		}
	}
	

	mail.send from: params["Email From"],
		to: params["Email To"],
		cc: ["riksca@gmail.com"],
		subject: "Marshal report for " + params["Report Type"],
		htmlBody: writer.toString()

//}