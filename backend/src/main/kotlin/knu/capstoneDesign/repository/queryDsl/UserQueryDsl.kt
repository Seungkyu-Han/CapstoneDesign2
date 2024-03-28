package knu.capstoneDesign.repository.queryDsl

import com.querydsl.jpa.impl.JPAQueryFactory
import knu.capstoneDesign.repository.custom.UserRepositoryCustom
import org.springframework.stereotype.Repository

@Repository
class UserQueryDsl(private val jpaQueryFactory: JPAQueryFactory): UserRepositoryCustom {
}