package alohavolunteer

import grails.test.hibernate.HibernateSpec
import grails.validation.ValidationException

import static alohavolunteer.VolunteerSpec.constructExampleVolunteer

/**
 * Unit testing Hibernate-specific functionality for Volunteer.
 * This is required to test the global grails.gorm.failOnError config,
 * which is ignored by DomainUnitTest's mock db.
 */
class VolunteerHibernateSpec extends HibernateSpec {

    // By not providing domainClasses here, HibernateSpec.setupSpec()
    // scans the package instead, and uses the global config, which we want to test.
    //    List<Class> getDomainClasses() { [Volunteer] }

    void "global failOnError triggers exception on saving non-unique email"() {

        given: "one persisted Volunteer"
        def v1 = constructExampleVolunteer()
        v1.save()

        when: "saving another Volunteer with a different email address"
        def v2 = constructExampleVolunteer()
        v2.email = 'bar@example.com'
        v2.save()

        then: "it was added"
        Volunteer.count() == old(Volunteer.count()) + 1
        Volunteer.findByEmail(v2.email)

        when: "trying to save another Volunteer with a duplicate email address"
        def v3 = constructExampleVolunteer()
        v3.save()

        then: "the global grails.gorm.failOnError config triggers an exception"
        ValidationException e = thrown()
        ['email', 'unique'].every {e.message.contains(it)}
    }
}
