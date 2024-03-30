package knu.capstoneDesign.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.LocalDate

@Entity
data class Diary(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Int?,

    @ManyToOne
    var user: User,

    var date: LocalDate,

    @Column(length = 1000)
    var content: String
){
    constructor(user: User, date: LocalDate, content: String):
            this(id = null, user = user, date = date, content = content)

    constructor(user: User, content: String):
            this(id = null, user = user, date = LocalDate.now(), content = content)
}
