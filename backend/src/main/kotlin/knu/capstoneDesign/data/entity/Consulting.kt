package knu.capstoneDesign.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

@Entity
data class Consulting(

    @Id
    @OneToOne
    private var diary: Diary,

    @Column(length = 1000)
    private var content:String
)