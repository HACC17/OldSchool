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
            view == '/volunteer/create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            volunteer = new Volunteer(params)

            controller.save(volunteer)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == config['facebook.messenger.origin'] + '?error=true'
            controller.flash.message != null
            Volunteer.count() == 1
    }
}
