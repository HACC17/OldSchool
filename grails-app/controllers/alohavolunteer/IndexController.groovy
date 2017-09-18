package alohavolunteer

class IndexController {

    def index() {
        forward controller: 'volunteer', action: 'create'
    }
}
