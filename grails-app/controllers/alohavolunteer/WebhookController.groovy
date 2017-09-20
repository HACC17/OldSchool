package alohavolunteer

import javax.servlet.http.HttpServletResponse

class WebhookController {

    def index() {
        def vt = grailsApplication.config['facebook.messenger.verifyToken']
        if (params['hub.mode'] == 'subscribe' && params['hub.verify_token'] == vt) {
            render text: params['hub.challenge']
        } else if (params['optin.user_ref']) {
            new Optin(userRef: params['optin.user_ref']).save()
        } else {
            render status: HttpServletResponse.SC_FORBIDDEN // 403
        }
    }
}
