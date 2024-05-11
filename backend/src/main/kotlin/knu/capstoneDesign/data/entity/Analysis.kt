package knu.capstoneDesign.data.entity

import jakarta.persistence.*
import knu.capstoneDesign.data.enum.Emotion

@Entity
data class Analysis(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @OneToOne
    var diary:Diary,
    @Enumerated(EnumType.STRING)
    var emotion: Emotion,
    var summary: String

)
