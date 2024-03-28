package knu.capstoneDesign.repository

import knu.capstoneDesign.data.entity.User
import knu.capstoneDesign.repository.custom.UserRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Int>, UserRepositoryCustom {
}