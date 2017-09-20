package alohavolunteer

import geb.spock.GebReportingSpec
import grails.core.GrailsApplication
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import grails.testing.spock.OnceBefore
import spock.lang.Shared


/**
 * See http://www.gebish.org/manual/current/ for more instructions
 */
@Integration
@Rollback
class VolunteerControllerFunctionalSpec extends GebReportingSpec {

    @Shared Volunteer example
    GrailsApplication grailsApplication

    @OnceBefore
    void setupData() {
        // Setup the data once for the whole spec, just to run a little faster.
        // We can't do this in setupSpec(), because GORM isn't initialized by then
        // (even with @Stepwise, which is often used with Geb).  This is more of
        // a demo of the typical use with Geb @Stepwise (without @Rollback).

        // This could also be done in setup(), for each feature.  However, because of this
        // spec's GORM @Rollback, it cannot be done in a feature, even in the setup: clause.
        // With @Rollback, the feature's test transaction cannot be committed,
        // so the app in the browser could not see its data.

        example = VolunteerSpec.constructExampleVolunteer()
        example.save()
    }

    void cleanupSpec() {
        // Need to explicitly cleanup example,
        // because it was saved outside the @Rollback test transaction.
        // But, Grails doesn't provide a session to cleanupSpec(), so we create our own.
        Volunteer.withNewSession {
            Volunteer.list().each {
                it.delete(flush: true)     // flush is required to actually delete, although I don't know why
            }
        }
    }
    
    void "config for @Integration specs is consistent, with profiles overriding"() {

        given:
        def profileOverride = System.properties['user.name'] == 'jbeutel' ? '-jbeutel' : ''

        expect:
        grailsApplication.config['configCheck'] == "from application${profileOverride}.yml"
    }

    void "can save a volunteer in the db"() {

        expect: "setup data is already in the db"
        Volunteer.count() == 1

        when: "signing up a new volunteer"
        to VolunteerCreatePage
        $("form").firstName = 'Jane'
        $("form").lastName = 'Doe'
        $("form").email = 'jane@example.com'
        $("form").phoneNumber = '555-1212'
        submitButton.click()

        then: "Facebook returns an error, because the test isn't configured to successfully use Facebook"
        waitFor {currentUrl.endsWith('error=true')}

        and:
        Volunteer.count() == 2
        def saved = Volunteer.findByFirstName('Jane')
        saved.phoneNumber == '555-1212'
    }

    void "can save a duplicate email in the db"() {

        given: "a volunteer already in the db"
        assert Volunteer.count() == 2       // XXX this should be 1
        def existing = Volunteer.list().first()

        when: "signing up a new volunteer with a duplicate email"
        to VolunteerCreatePage
        $("form").firstName = 'Jack'
        $("form").lastName = 'Doe'
        $("form").email = existing.email
        $("form").phoneNumber = '555-6666'

        and:
        // hack to scroll submitButton up to be fully visible in the window,
        // since it is lower than in the previous feature,
        // because of the error message from the previous feature
        // (but then scroll down to firstName, because submitButton is obscured
        // by the absolute banner at the top of the page)
        interact {
            moveToElement(submitButton)
            moveToElement($("#firstName"))
        }
        submitButton.click()

        then: "Facebook returns an error, because the test isn't configured to successfully use Facebook"
        waitFor {currentUrl.endsWith('error=true')}

        and: "the duplicate email was saved"
        Volunteer.count() == 3       // XXX this should be 2
        Volunteer.findByFirstName('Jack')
    }
}
