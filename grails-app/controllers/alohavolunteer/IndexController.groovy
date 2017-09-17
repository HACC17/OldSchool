package alohavolunteer

class IndexController {

    def index() {
        render(view:"/index", model:[
            appId:grailsApplication.config['facebook.messenger.appId'],
            nonce:UUID.randomUUID().toString(),
            origin:grailsApplication.config['facebook.messenger.origin'],
            pageId:grailsApplication.config['facebook.messenger.pageId']
          ]
        )
    }
}
