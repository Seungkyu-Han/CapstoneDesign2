package knu.capstoneDesign.data.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

@Entity
data class Analysis(
    @Id
    @OneToOne
    private var diary:Diary,

    private var negative:Float,

    private var positive:Float,

    private var neutral:Float,

    private var total: Float

)
