package ru.nsu.dbexample.students

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.nsu.dbexample.groups.GroupsService


@RestController
class StudentsController(private val studentsService: StudentsService,
                         private val groupsService: GroupsService) {

    @GetMapping("createStudent")
    fun createStudent(name: String, groupName: String): Student? {
        val group = groupsService.getGroupByNumber(groupName)
                ?: throw IllegalArgumentException("Group with number ${groupName} not found")
        val toCreate = ActualStudent(null, name, group)
        return studentsService.findStudent(studentsService.createStudent(toCreate))
    }

    @GetMapping("createStudents")
    fun createStudents(argsStr: String): String {
        val args = argsStr.split(",")
            .map { it.trim() }
        if (argsStr.isEmpty() || args.size % 2 != 0) {
            return "even args expected"
        }

        val toCreate = ArrayList<Student>()
        for (i in 0 until args.size step 2) {
            val group = groupsService.getGroupByNumber(args[i + 1]) ?: return "Group with number ${args[i + 1]} not found"
            toCreate.add(ActualStudent(null, args[i], group))

        }
        return studentsService.createStudents(toCreate).toString()
    }

    @GetMapping("moveStudent")
    fun moveStudent(id: Long, groupNumber: String): String {
        val toMove = studentsService.findStudent(id) ?: return "Student with $id does not exist"
        val targetGroup = groupsService.getGroupByNumber(groupNumber) ?: return "Group with number $groupNumber does not exist"
        studentsService.move(toMove, targetGroup)
        return "Moved"
    }

    @GetMapping("getStudents")
    fun getStudents(from: Int, size: Int): String {
        return studentsService.getStudents(Page(from, size)).map { "${it.name} ${it.group.number}" } .joinToString("\n")
    }

}