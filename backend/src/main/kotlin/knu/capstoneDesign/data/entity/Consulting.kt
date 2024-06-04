package knu.capstoneDesign.data.entity

import jakarta.persistence.*

@Entity
data class Consulting(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int?,


    @OneToOne
    var diary: Diary,

    @Column(length = 1000)
    var content:String
)