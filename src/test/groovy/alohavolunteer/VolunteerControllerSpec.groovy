package alohavolunteer

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class VolunteerControllerSpec extends Specification
        implements ControllerUnitTest<VolunteerController>, DomainUnitTest<Volunteer> {

    def populateValidParams(params) {
        assert params != null

        params["lastName"] = 'Doe'
        params["firstName"] = 'Jane'
        params["email"] = 'bar@example.com'
        params["phoneNumber"] = '321-7654'
        params["recipientId"] = 999
        params["nonce"] = 'asdfasdf'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index(10)

        then:"The model is correct"
            !model.volunteerList
            model.volunteerCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.volunteer!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def volunteer = new Volunteer()
            volunteer.validate()
            controller.save(volunteer)

        then:"The create view is rendered again with the correct model"
            model.volunteer!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            volunteer = new Volunteer(params)

            controller.save(volunteer)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/volunteer/show/1'
            controller.flash.message != null
            Volunteer.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def volunteer = new Volunteer(params)
            controller.show(volunteer)

        then:"A model is populated containing the domain instance"
            model.volunteer == volunteer
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def volunteer = new Volunteer(params)
            controller.edit(volunteer)

        then:"A model is populated containing the domain instance"
            model.volunteer == volunteer
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/volunteer/index'
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def volunteer = new Volunteer()
            volunteer.validate()
            controller.update(volunteer)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.volunteer == volunteer

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            volunteer = new Volunteer(params).save(flush: true)
            controller.update(volunteer)

        then:"A redirect is issued to the show action"
            volunteer != null
            response.redirectedUrl == "/volunteer/show/$volunteer.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/volunteer/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def volunteer = new Volunteer(params).save(flush: true)

        then:"It exists"
            Volunteer.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(volunteer)

        then:"The instance is deleted"
            Volunteer.count() == 0
            response.redirectedUrl == '/volunteer/index'
            flash.message != null
    }
}
