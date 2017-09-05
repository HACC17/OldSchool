# AlohaVolunteer

* hello world in [Grails 3.3.0](http://docs.grails.org/3.3.0/guide/gettingStarted.html)
* [deployed](https://guarded-atoll-72753.herokuapp.com/) to Heroku (in root context)
* changed prod db to in-memory create-drop, for development on Heroku
* added basic [Volunteer](https://guarded-atoll-72753.herokuapp.com/volunteer) domain w/ dynamic scaffolding and tests

<div class="alert alert-warning">
If you go to Heroku after the dyno has been unused for a while and gone to sleep,
it might take a while to restart, and even timeout with an error.
But, if you try again, it may finish restarting and work OK.
</div>