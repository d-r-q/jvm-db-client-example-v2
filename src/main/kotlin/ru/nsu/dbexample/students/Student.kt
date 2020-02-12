package ru.nsu.dbexample.students

import ru.nsu.dbexample.groups.Group


interface Student {
    val id: Long?
    val name: String
    var group: Group
}

data class ActualStudent(override val id: Long?, override val name: String, override var group: Group) : Student