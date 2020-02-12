package ru.nsu.dbexample.groups

import ru.nsu.dbexample.db.Db
import ru.nsu.dbexample.students.Student
import ru.nsu.dbexample.students.StudentDao
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.sql.Connection

interface Group {
    val id: Long?
    val number: String
    val studentsCount: Int
}

data class ActualGroup(override val id: Long?, override val number: String, override val studentsCount: Int = 0) : Group

class GroupProxy(val conn: Connection, override val id: Long) : Group {

    private val actualGroup by lazy {
        val stmt = conn.prepareStatement("SELECT * FROM groups WHERE id = ?")
        stmt.setLong(1, id)
        val rs = stmt.executeQuery()
        if (rs.next()) {
            ActualGroup(id, rs.getString("number"), rs.getInt("students_count"))
        } else {
            throw IllegalStateException("Group with id $id not found")
        }
    }

    override val number: String
        get() = actualGroup.number

    override val studentsCount: Int
        get() = actualGroup.studentsCount

}

class JdkProxy(fetch: () -> Any?) : InvocationHandler {

    private val value by lazy { fetch() }

    override fun invoke(proxy: Any?, method: Method, args: Array<out Any>?): Any? {
        value ?: throw RuntimeException("Object not found")
        val find = value!!.javaClass.methods.find { it.name == method.name } ?: throw IllegalArgumentException(method.name)
        if (args == null) {
            return find.invoke(value)
        } else{
            return find.invoke(value, args)
        }
    }

}

inline fun <reified T> createProxy(noinline fetch: () -> Any?): T {

    return Proxy.newProxyInstance(
            T::class.java.classLoader,
            arrayOf(T::class.java),
            JdkProxy(fetch)) as T

}

fun main(args: Array<String>) {
    val groupDao = GroupDao(Db().dataSource)
    val studentDao = StudentDao(Db().dataSource)
    val group = createProxy<Group> { groupDao.getGroupById(0) }
    println(group.number)


    val student = createProxy<Student> { studentDao.findStudent(2) }
    println(student)
}
