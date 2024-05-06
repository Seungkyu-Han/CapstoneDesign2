package knu.capstoneDesign.data.entity

import jakarta.persistence.*

@Entity
data class Analysis(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long?,

    @OneToOne
    private var diary:Diary,
    private var isPositive: Boolean,
    private var summary: String

)
