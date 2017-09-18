package alohavolunteer

import geb.Page

class VolunteerCreatePage extends Page {
    static url = 'volunteer/create'
    static at = { title == 'Aloha Volunteer' }
    static content = {
        errors { $(".errors") }
        submitButton { $('button[type="submit"]') }
    }
}