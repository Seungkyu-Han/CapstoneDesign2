package knu.capstoneDesign.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class User(
    @Id
    private var id:Int,

    @Column(length = 20)
    private var name: String
)
