package alohavolunteer

import geb.spock.GebReportingSpec
import grails.core.GrailsApplication
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback


/**
 * See http://www.gebish.org/manual/current/ for more instructions
 */
@Integration
@Rollback
class VolunteerControllerFunctionalSpec extends GebReportingSpec {

    Volunteer example
    GrailsApplication grailsApplication

    void setup() {
        // Apparently need to save example here, outside the @Rollback test transaction,
        // to have the app in the browser see it in the db.  Also, apparently need to
        // do this for each test, because Grails does not seem to be ready for setupSpec(),
        // even with @Stepwise.
        example = VolunteerSpec.constructExampleVolunteer()
        example.save()
    }

    void cleanup() {
        // Need to explicitly cleanup example,
        // because it was saved outside the @Rollback test transaction.
        example.delete()
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
