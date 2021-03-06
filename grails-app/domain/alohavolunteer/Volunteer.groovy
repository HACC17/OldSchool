package alohavolunteer

class Volunteer {

    String lastName
    String firstName
    String email
    String phoneNumber

    // for Facebook
    String recipientId
    String nonce

    // GORM auto-timestamps
    @SuppressWarnings("GroovyUnusedDeclaration")
    Date dateCreated
    @SuppressWarnings("GroovyUnusedDeclaration")
    Date lastUpdated

    static constraints = {
        lastName blank: false
        firstName blank: false
        email email: true, blank: false
        phoneNumber blank: false, matches: /((\(\d{3}\) ?)|(\d{3}-))?\d{3}-\d{4}/
        recipientId display: false, nullable: true  // null until Facebook provides
        nonce display: false
    }
}
