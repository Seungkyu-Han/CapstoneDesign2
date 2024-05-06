package knu.capstoneDesign.data.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

@Entity
data class Analysis(
    @Id
    @OneToOne
    private var diary:Diary,
    private var isPositive: Boolean,
    private var summary: String

)
