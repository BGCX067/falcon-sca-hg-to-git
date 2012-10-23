import org.joda.time.DateTime
import groovy.xml.MarkupBuilder


//backends.run { 
	logger.BuildReport.info "logging an info message"

	StringWriter writer = new StringWriter()  
	def build = new MarkupBuilder(writer)  
	build.html{  
		head{  
			title('Marshal Report')
			style(type:"text/css", '''  
			.sect_title {
				text-decoration:underline;
			}
			.sect_body {
			}
			''')
		}  
		body {
			h1 "Marshal Report"

			p {
				h3 ('class':'sect_title', 'style':'display: inline;', "Reporting Period: " )
				span ('class':'sect_body', params["Report Type"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Marshal Type: "  )
				span ('class':'sect_body', params["Marshal Type"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "SCA Name: "  )
				span ('class':'sect_body', params["SCA Name"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Modern First & Last Name: "  )
				span ('class':'sect_body', 'style':'display: inline;', params["Modern Name"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Address: "  )
				span ('class':'sect_body', params["Address"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Phone Number: "  )
				span ('class':'sect_body', params["Phone Number"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Membership Number: "  )
				span ('class':'sect_body', params["SCA Membership No"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Membership Expires: "  )
				span ('class':'sect_body', params["Membership Expires"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Home Group: "  )
				span ('class':'sect_body', params["Group"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Number of Authorized Fighters: "  )
				span ('class':'sect_body', params["Active Fighters"])
			}

			p {
				h3 ('class':'sect_title', 'style':'display: inline;',  "Number of Minors: "  )
				span ('class':'sect_body', params["Minor Fighters"])
			}

			h3 ('class':'sect_title',  "Activities: "  )
			p ('class':'sect_body', params["Activities"])

			h3 ('class':'sect_title',  "Problems or Injuries: "  )
			p ('class':'sect_body', params["Injury"])

			h3 ('class':'sect_title',  "Fighter Comments "  )
			p ('class':'sect_body', params["Fighter Comments"])

			h3 ('class':'sect_title',  "Summary: "  )
			p ('class':'sect_body', params["Summary"])
		}
	}
	

	mail.send from: params["Email From"],
		to: params["Email To"],
		cc: ["riksca@gmail.com"],
		subject: "Marshal report for " + params["Report Type"],
		htmlBody: writer.toString()

//}