package knu.capstoneDesign.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

@Entity
data class UserConsulting(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Int?,

    @ManyToOne
    var diary: Diary,

    var localDateTime: LocalDateTime,

    @Column(length = 1000)
    var question: String,

    @Column(length = 1000)
    var answer: String
)