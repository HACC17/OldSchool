package alohavolunteer

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class VolunteerController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Volunteer.list(params), model:[volunteerCount: Volunteer.count()]
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
