package alohavolunteer

import grails.testing.gorm.DomainUnitTest
import grails.validation.ValidationException
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Unit test of Volunteer domain with a mock db.
 * This is more of a demo of a Spock test, because Volunteer doesn't have much specific code yet.
 * Volunteer's functionality is mainly that of the Grails framework, but a unit test
 * within that framework can actually test only a limited amount of it.
 * VolunteerIntegrationSpec can test more.
 */
class VolunteerSpec extends Specification implements DomainUnitTest<Volunteer> {

    def setup() {
        setValidProperties(domain)  // on the default Volunteer domain instance
    }

    private static void setValidProperties(Volunteer v) {
        v.firstName = 'John'
        v.lastName = 'Doe'
        v.email = 'foo@example.com'
        v.phoneNumber = '123-4567'
        v.nonce = 'fake nonce'
        v.recipientId = '42'
    }

    static Volunteer constructExampleVolunteer() {
        def v = new Volunteer()
        setValidProperties(v)
        v
    }

    def cleanup() {
    }

    void "config for GrailsUnitTest is consistent, with profiles overriding"() {

        given:
        def profileOverride = System.properties['user.name'] == 'jbeutel' ? '-jbeutel' : ''

        expect:
        config['configCheck'] == "from application${profileOverride}.yml"
    }

    @Unroll
    void "email constraint validation '#email' has errors #errors"() {

        when:
        domain.email = email
        domain.validate()

        then:
        domain.errors.allErrors*.code == errors

        where:
        email               || errors
        'foo@example.com'   || []
        'foo@bar'           || ['email.invalid']
        'example.com'       || ['email.invalid']
        'foo'               || ['email.invalid']
        ''                  || ['blank']
        null                || ['nullable']
    }

    @Unroll
    void "phoneNumber constraint validation '#phoneNumber' has errors #errors"() {

        when:
        domain.phoneNumber = phoneNumber
        domain.validate()

        then:
        domain.errors.allErrors*.code == errors

        where:
        phoneNumber         || errors
        '123-4567'          || []
        '(123) 456-7890'    || []
        '123-456-7890'      || []
        '1234567'           || ['matches.invalid']
        '1234567890'        || ['matches.invalid']
        'abc-defg'          || ['matches.invalid']
        ''                  || ['blank']
        null                || ['nullable']
    }

    void "email is not unique"() {

        when: "persisting a Volunteer"
        def v1 = constructExampleVolunteer()
        v1.save()

        then: "one Volunteer was added"
        Volunteer.count() == old(Volunteer.count()) + 1


        when: "trying to create another Volunteer with the same email address"
        def v2 = constructExampleVolunteer()

        then: "the duplicate email is valid"
        v2.validate()

        and: "can save"
        v2.save()

        and: "one more Volunteer was added"
        Volunteer.count() == old(Volunteer.count()) + 1
    }
}
