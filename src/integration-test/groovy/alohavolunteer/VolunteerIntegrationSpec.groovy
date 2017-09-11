package alohavolunteer

import grails.core.GrailsApplication
import grails.validation.ValidationException
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification

import static alohavolunteer.VolunteerSpec.constructExampleVolunteer

/**
 * Integration test of Hibernate-specific functionality for Volunteer.
 * This is required to test the global grails.gorm.failOnError config,
 * which is ignored by DomainUnitTest's mock db, despite the whole config
 * being loaded properly.  While HibernateSpec doesn't ignore the global
 * grails.gorm.failOnError config setting (as long as this spec doesn't
 * define getDomainClasses(), that is), it did fail to load the config properly,
 * ignoring external config files, and profiles, e.g., application-jbeutel.yml.
 */
@Integration
@Rollback
class VolunteerIntegrationSpec extends Specification {

    GrailsApplication grailsApplication

    void "config for @Integration specs is consistent, with profiles overriding"() {

        given:
        def profileOverride = System.properties['user.name'] == 'jbeutel' ? '-jbeutel' : ''

        expect:
        grailsApplication.config['configCheck'] == "from application${profileOverride}.yml"
    }

    void "global failOnError triggers exception on saving non-unique email"() {

        given: "one persisted Volunteer"
        def v1 = constructExampleVolunteer()
        v1.save(flush: true)

        when: "saving another Volunteer with a different email address"
        def v2 = constructExampleVolunteer()
        v2.email = 'bar@example.com'
        v2.save(flush: true)

        then: "it was added"
        Volunteer.count() == old(Volunteer.count()) + 1
        Volunteer.findByEmail(v2.email)

        when: "trying to save another Volunteer with a duplicate email address"
        def v3 = constructExampleVolunteer()
        v3.save(flush: true)

        then: "the global grails.gorm.failOnError config triggers an exception"
        ValidationException e = thrown()
        ['email', 'unique'].every {e.message.contains(it)}
    }
}
