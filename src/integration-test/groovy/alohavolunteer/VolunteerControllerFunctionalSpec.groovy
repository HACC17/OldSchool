package alohavolunteer

import geb.spock.GebReportingSpec
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback


/**
 * See http://www.gebish.org/manual/current/ for more instructions
 */
@Integration
@Rollback
class VolunteerControllerFunctionalSpec extends GebReportingSpec {

    Volunteer example

    def setup() {
        example = VolunteerSpec.constructExampleVolunteer()
        example.save()
    }

    def cleanup() {
    }

    void "names appear in list"() {

        when:
        to VolunteerListPage

        then:
        cell(1, 'First Name').text() == example.firstName
    }
}
