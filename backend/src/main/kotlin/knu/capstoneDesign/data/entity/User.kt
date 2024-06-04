package knu.capstoneDesign.data.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class User(
    @Id
    var id:Long,

    @Column(length = 20)
    var name: String?,

    var refreshToken: String?
){
    constructor(id: Long): this(id=id, name=null, refreshToken = null)
}
