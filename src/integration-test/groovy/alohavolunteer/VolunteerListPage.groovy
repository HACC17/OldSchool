package alohavolunteer

import geb.Page

class VolunteerListPage extends Page {
    static url = 'volunteer/index'
    static at = { title == 'Volunteer List' }
    static content = {
        columnNames { $("th")*.text() }
        columnIndex { String name -> columnNames.indexOf(name) }
        row { int idx -> $("tr")[idx] }
        cell { int idx, String columnName -> row(idx).find("td", columnIndex(columnName)) }
    }
}