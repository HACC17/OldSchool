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

    void "global failOnError triggers exception on saving invalid email"() {

        when: "saving a Volunteer with an invalid email address"
        def v1 = constructExampleVolunteer()
        v1.email = 'bad'
        v1.save(flush: true)

        then: "the global grails.gorm.failOnError config triggers an exception"
        ValidationException e = thrown()
        e.message.contains('not a valid e-mail address')
    }
}
