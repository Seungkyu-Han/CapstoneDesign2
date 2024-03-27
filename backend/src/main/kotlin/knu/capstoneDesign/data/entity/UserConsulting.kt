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
    private var id:Int,

    @ManyToOne
    private var diary: Diary,

    private var localDateTime: LocalDateTime,

    @Column(length = 1000)
    private var question: String,

    @Column(length = 1000)
    private var answer: String
)