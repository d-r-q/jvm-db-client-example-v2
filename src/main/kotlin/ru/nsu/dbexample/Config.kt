package ru.nsu.dbexample

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.nsu.dbexample.db.Db
import ru.nsu.dbexample.db.UnivDataSource
import ru.nsu.dbexample.groups.GroupDao
import ru.nsu.dbexample.groups.GroupsController
import ru.nsu.dbexample.groups.GroupsService
import ru.nsu.dbexample.students.StudentDao
import ru.nsu.dbexample.students.StudentsController
import ru.nsu.dbexample.students.StudentsService
import javax.sql.DataSource

@Configuration
class Config {

    @Bean
    fun db(): Db {
        return Db()
    }

    @Bean
    fun univDs(): UnivDataSource {
        return UnivDataSource(db().dataSource)
    }

    @Bean
    fun studentsDao(univDs: DataSource): StudentDao {
        return StudentDao(univDs)
    }

    @Bean
    fun groupsDao(): GroupDao {
        return GroupDao(univDs())
    }

    @Bean
    fun groupsService(): GroupsService {
        return GroupsService(univDs(), groupsDao())
    }

    @Bean
    fun studentsService(): StudentsService {
        return StudentsService(univDs(), studentsDao(univDs()), groupsDao())
    }

}