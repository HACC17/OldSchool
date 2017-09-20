package alohavolunteer

import grails.gorm.transactions.Transactional
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

@Transactional(readOnly = true)
class VolunteerController {

    static allowedMethods = [create: "GET", save: "POST"]

    private Map<String, Object> facebookConfig() {
        def fm = grailsApplication.config['facebook.messenger']
        [
                appId : fm.appId,
                origin: fm.origin,
                pageId: fm.pageId,
                nonce: UUID.randomUUID().toString(),
        ]
    }

    def create() {
        def model = facebookConfig() + [volunteer: new Volunteer(params)]
        render view: 'create', model: model
    }

    @Transactional
    def save(Volunteer volunteer) {

        if (volunteer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            render view: 'create', model: facebookConfig() + [volunteer: volunteer]
            return
        }

        def fm = grailsApplication.config['facebook.messenger']
        def http = new HTTPBuilder('https://graph.facebook.com')
        def path = "/v2.10/${fm.pageId}/messages"
        println "sending to [${volunteer.nonce}]"
        def result = http.request(POST) {
          uri.path = path
          requestContentType = JSON
          body = [
            recipient : [ user_ref:volunteer.nonce ],
            message : [
              text: "Aloha! Thanks for signing up to volunteer. Feel free to respond here and a staffer will get back to you!",
              quick_replies: [
                [
                  content_type:"text",
                  title:"Thanks!",
                  payload:"1"
                ],
                [
                  content_type:"text",
                  title:"No Thanks",
                  payload:"0"
                ]
              ]
            ],
            access_token : fm.pageToken
          ]
          response.success = { resp ->
            //println resp
            redirect(url: "${fm.origin}?thanks=true")
          }
          response.failure = { resp, reader ->
            println "\n\nTHERE WAS AN ERROR!"
            println reader
            redirect(url: "${fm.origin}?error=true")
          }
        }
        volunteer.save flush:true
        flash.message = 'Saved'
    }
}
