package alohavolunteer

/**
 * If the user checks the Send to Messenger checkbox,
 * then Facebook will callback to WebhookController with the nonce that opted in.
 * The Facebook app needs to be subscribed to this callback,
 * by selecting the {@code messaging_optins} field on its webhooks config.
 * This informs our app that it can send a message to the nonce (only once?).
 */
class Optin {

    String userRef  // a nonce that has opted in to receiving Facebook messages from us

    static constraints = {
        userRef blank: false
    }
}
