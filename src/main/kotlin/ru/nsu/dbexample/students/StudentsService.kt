package ru.nsu.dbexample.students

import ru.nsu.dbexample.db.UnivDataSource
import ru.nsu.dbexample.db.transaction
import ru.nsu.dbexample.groups.Group
import ru.nsu.dbexample.groups.GroupDao


class StudentsService(
        private val dataSource: UnivDataSource,
        private val studentDao: StudentDao,
        private val groupDao: GroupDao
) {

    fun createStudent(toCreate: Student): Long {
        return transaction(dataSource) {
            val studentId = studentDao.createStudent(toCreate)
            groupDao.addStudents(toCreate.group.id!!, 1)
            studentId
        }
    }

    fun createStudents(toCreate: Iterable<Student>): List<Long> {
        return transaction(dataSource) {

            val studentIds = studentDao.createStudents(toCreate)

            val counts = toCreate.groupBy { it.group.id }
                    .map { it.key to it.value.size }

            for ((group, cnt) in counts) {
                if (group != null) {
                    groupDao.addStudents(group, cnt)
                }
            }
            studentIds
        }
    }

    fun findStudent(id: Long): Student? {
        return studentDao.findStudent(id)
    }

    fun move(toMove: Student, targetGroup: Group) {
        return transaction(dataSource) {
            val srcGroup = toMove.group
            toMove.group = targetGroup
            studentDao.updateStudent(toMove)
            groupDao.addStudents(srcGroup.id!!, -1)
            groupDao.addStudents(targetGroup.id!!, 1)
        }
    }

    fun getStudents(page: Page): List<Student> {
        val res = studentDao.getStudents(page)
        return res.map { it.group.number; it }
    }
}
