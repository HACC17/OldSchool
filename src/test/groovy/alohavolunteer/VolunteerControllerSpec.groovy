package alohavolunteer

import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class VolunteerControllerSpec extends Specification implements ControllerUnitTest<VolunteerController>, DataTest {

    void setupSpec() {
        mockDomain Volunteer
    }

    def setup() {
    }

    def cleanup() {
    }

    // When the unit test framework mocks the controller,
    // it doesn't look like the mock implements dynamic scaffolding,
    // so there's nothing we can test at this level yet.
    void "uses dynamic scaffolding"() {

        expect:
        controller.scaffold == Volunteer
    }
}
