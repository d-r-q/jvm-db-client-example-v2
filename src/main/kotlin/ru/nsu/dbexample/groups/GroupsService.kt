package ru.nsu.dbexample.groups

import ru.nsu.dbexample.db.UnivDataSource
import ru.nsu.dbexample.students.StudentDao


class GroupsService(
        private val dataSource: UnivDataSource,
        private val groupDao: GroupDao
) {


    fun getGroupByNumber(groupNumber: String): Group? {
        return groupDao.getGroupByNumber(groupNumber)
    }

    fun createGroup(group: Group): Long {
        return groupDao.createGroup(group)
    }

}

