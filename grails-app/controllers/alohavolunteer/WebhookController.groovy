package alohavolunteer

import javax.servlet.http.HttpServletResponse

class WebhookController {

    def index() {
        def vt = grailsApplication.config['facebook.messenger.verifyToken']
        if (params['hub.mode'] == 'subscribe' && params['hub.verify_token'] == vt) {
            render text: params['hub.challenge']
        } else {
            render status: HttpServletResponse.SC_FORBIDDEN // 403
        }
    }
}
