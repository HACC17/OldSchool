package alohavolunteer

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class OptinSpec extends Specification implements DomainUnitTest<Optin> {

    def setup() {
    }

    def cleanup() {
    }

    void "a blank userRef does not validate"() {

        when:
        domain.userRef = ''

        then:
        !domain.validate()
    }
}