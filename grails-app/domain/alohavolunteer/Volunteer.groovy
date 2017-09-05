package alohavolunteer

class Volunteer {

    String lastName
    String firstName
    String email
    String phoneNumber

    // for Facebook (or use something from plugin instead?)
    Long recipientId
    String nonce

    // GORM auto-timestamps
    Date dateCreated
    Date lastUpdated

    static constraints = {
        lastName blank: false
        firstName blank: false
        email email: true, unique: true, blank: false
        phoneNumber blank: false, matches: /((\(\d{3}\) ?)|(\d{3}-))?\d{3}-\d{4}/
    }
}
