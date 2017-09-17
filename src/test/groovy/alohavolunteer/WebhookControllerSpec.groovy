package alohavolunteer

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class WebhookControllerSpec extends Specification implements ControllerUnitTest<WebhookController> {

    def setup() {
    }

    def cleanup() {
    }

    void "external-config plugin does not work with GrailsUnitTest"() {

        expect:
        config['secretConfigCheck'] == [:]
    }

    void "index without the proper params returns status 403"() {

        when:
        controller.index()

        then:
        status == 403
    }

    void "subscribe request with proper params returns status 200 and challenge"() {

        given:
        final CHALLENGE = 'foo'
        params << [
                'hub.mode': 'subscribe',
                'hub.verify_token': [:],
                'hub.challenge': CHALLENGE
        ]

        when:
        controller.index()

        then:
        status == 200
        response.text == CHALLENGE
    }
}
