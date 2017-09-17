package alohavolunteer

import static org.springframework.http.HttpStatus.*
import grails.gorm.transactions.Transactional
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

@Transactional(readOnly = true)
class VolunteerController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    final String PAGE_TOKEN = grailsApplication.config['facebook.messenger.pageToken']
    final String PAGE_ID = grailsApplication.config['facebook.messenger.pageId']

    def index(Integer max) {
      if (request.post) {
        def http = new HTTPBuilder('https://graph.facebook.com')
        def path = "/v2.10/${PAGE_ID}/messages"
        def result = http.request(POST) {
          uri.path = path
          requestContentType = JSON
          body = [
            recipient : [ user_ref:params.user_ref ],
            message : [
              text: "Hi! Thanks for signing up to volunteer. Feel free to respond here and a staffer will get back to you!",
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
            access_token : PAGE_TOKEN
          ]
          response.success = { resp ->
            //println resp
            redirect(url:grailsApplication.config['facebook.messenger.origin'] + '?thanks=true')
          }
          response.failure = { resp, reader ->
            println "\n\nTHERE WAS AN ERROR!"
            println reader
            redirect(url:grailsApplication.config['facebook.messenger.origin'] + '?error=true')
          }
        }
      } else {
        params.max = Math.min(max ?: 10, 100)
        respond Volunteer.list(params), model:[volunteerCount: Volunteer.count()]
      }
    }

    def show(Volunteer volunteer) {
        respond volunteer
    }

    def create() {
        respond new Volunteer(params)
    }

    @Transactional
    def save(Volunteer volunteer) {
        if (volunteer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (volunteer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond volunteer.errors, view:'create'
            return
        }

        volunteer.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'volunteer.label', default: 'Volunteer'), volunteer.id])
                redirect volunteer
            }
            '*' { respond volunteer, [status: CREATED] }
        }
    }

    def edit(Volunteer volunteer) {
        respond volunteer
    }

    @Transactional
    def update(Volunteer volunteer) {
        if (volunteer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (volunteer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond volunteer.errors, view:'edit'
            return
        }

        volunteer.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'volunteer.label', default: 'Volunteer'), volunteer.id])
                redirect volunteer
            }
            '*'{ respond volunteer, [status: OK] }
        }
    }

    @Transactional
    def delete(Volunteer volunteer) {

        if (volunteer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        volunteer.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'volunteer.label', default: 'Volunteer'), volunteer.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'volunteer.label', default: 'Volunteer'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
