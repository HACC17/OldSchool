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
            example.delete(flush: true)     // flush is required to actually delete, although I don't know why
        }
    }
    
    void "config for @Integration specs is consistent, with profiles overriding"() {

        given:
        def profileOverride = System.properties['user.name'] == 'jbeutel' ? '-jbeutel' : ''

        expect:
        grailsApplication.config['configCheck'] == "from application${profileOverride}.yml"
    }

    void "names appear in list"() {

        when:
        to VolunteerListPage

        then:
        cell(1, 'First Name').text() == example.firstName
    }
}
