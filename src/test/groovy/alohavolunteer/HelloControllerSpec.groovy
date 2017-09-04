package alohavolunteer

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class HelloControllerSpec extends Specification implements ControllerUnitTest<HelloController> {

    def setup() {
    }

    def cleanup() {
    }

    void "test action which renders text"() {

        when:
        controller.index()

        then:
        status == 200
        response.text == "Hello Grails!"
    }
}
