package alohavolunteer

import grails.core.GrailsApplication
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
@Rollback
class WebhookControllerIntegrationSpec extends Specification {

    GrailsApplication grailsApplication

    void "external-config plugin works with @Integration Specification"() {

        given:
        def expectedCheck = [:]
        def expectedPageToken = [:]
        if (System.properties['user.name'] == 'jbeutel') {
            expectedCheck = 'from alohavolunteer-secret-config.yml'
            expectedPageToken = 'XXX'
        }

        expect:
        grailsApplication.config['secretConfigCheck'] == expectedCheck
        grailsApplication.config['facebook.messenger.pageToken'] == expectedPageToken
    }
}
