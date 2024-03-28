package knu.capstoneDesign.repository.queryDsl

import com.querydsl.jpa.impl.JPAQueryFactory
import knu.capstoneDesign.repository.custom.DiaryRepositoryCustom
import org.springframework.stereotype.Repository

@Repository
class DiaryQueryDsl(private val jpaQueryFactory: JPAQueryFactory): DiaryRepositoryCustom {
}