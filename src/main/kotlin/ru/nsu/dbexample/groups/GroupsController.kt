package ru.nsu.dbexample.groups

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GroupsController(private val service: GroupsService) {

    @GetMapping("createGroup")
    fun createGroup(groupName: String): String {
        val toCreate = ActualGroup(null, groupName)
        return service.createGroup(toCreate).toString()
    }

}