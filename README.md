# Aloha Volunteer

This is a demo of the [Facebook Messenger checkbox](https://developers.facebook.com/docs/messenger-platform/discovery/checkbox-plugin)
on a signup page, to provide another channel for communicating
with volunteers.

## Usage

1. A volunteer checks "Send to Messenger" on [a signup page (app)](https://www.alohavolunteer.org/).
2. The app:
    * saves the volunteer's info in a database.
    * automatically sends the volunteer a welcome message from [a Facebook Page](https://www.facebook.com/AlohaVolunteer-480155449028959/).
3. The volunteer can reply to that message, using Facebook Messenger,
and send new messages to that Facebook Page.
4. The managers of the Facebook Page can receive the messages from the volunteer, and reply.
5. In the future, the app can automatically send Facebook Messages to the volunteer, from the Facebook Page.

## Implementation

* [Facebook Messenger checkbox](https://developers.facebook.com/docs/messenger-platform/discovery/checkbox-plugin)
* [Grails 3.3.0](http://docs.grails.org/3.3.0/guide/gettingStarted.html)
* deployed to Heroku, with Postgres
