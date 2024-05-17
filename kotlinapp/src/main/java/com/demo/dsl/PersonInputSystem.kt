package com.demo.dsl

class Worker(info: String) {

    init {
        println("【${info}】")
    }

    fun inputWorker(workerInfo: String) {
        println("你的工作信息是:$workerInfo")
    }

    fun inputSalary(salary: Double) {
        println("你的工作薪资是:$salary")
    }
}

class Teacher(info: String) {

    init {
        println("【${info}】")
    }

    fun myCourse(course: String) {
        println("你的授课内容是:$course")
    }
}

class Student(info: String) { // 中转站2

    init {
        println("【${info}】")
    }

    fun study(studyInfo: String) {
        println("你的学习内容是:$studyInfo")
    }

    fun fraction(fraction: Double) {
        println("你学习的分数是:$fraction")
    }
}

class Person { // 中转站1

    init {
        println("模拟人员录入系统")
    }

    fun student(lambda: Student.() -> Unit): Student {
        val student = Student("学生录入启动了")
        student.lambda()
        return student
    }

    fun teacher(lambda: Teacher.() -> Unit): Teacher {
        val teacher = Teacher("老师录入启动了")
        lambda(teacher)
        return teacher
    }

    fun worker(lambda: Worker.() -> Unit): Worker {
        val worker = Worker("工人录入启动了")
        lambda(worker)
        return worker
    }
}

fun person(lambda: Person.() -> Unit): Person {
    val person = Person()
    person.lambda()
    return person
}

// 人员录入系统
fun main() {
    person { // 持有this 中转站1 == { student， teacher， worker }
        student { // 持有this 中转站2 == { study， fraction }
            study("数学")
            fraction(98.3)
        }
        teacher {
            myCourse("语文")
        }
        worker {
            inputWorker("汽修工程师")
            inputSalary(20000.0)
        }
    }
}