package knu.capstoneDesign.repository.queryDsl

import com.querydsl.jpa.impl.JPAQueryFactory
import knu.capstoneDesign.data.dto.diary.res.DiaryGetListRes
import knu.capstoneDesign.data.dto.diary.res.QDiaryGetListRes
import knu.capstoneDesign.data.entity.QDiary.diary
import knu.capstoneDesign.repository.custom.DiaryRepositoryCustom
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class DiaryQueryDsl(private val jpaQueryFactory: JPAQueryFactory): DiaryRepositoryCustom {

    override fun findByUserIdAndDateBetween(
        userId: Long,
        startDate: LocalDate,
        endDate: LocalDate
    ): List<DiaryGetListRes> {
        return jpaQueryFactory.select(QDiaryGetListRes(diary.id, diary.date, diary.title, diary.content))
            .from(diary)
            .where(
                diary.user.id.eq(userId),
                diary.date.between(startDate, endDate)
            )
            .fetch()
    }

    override fun findByUserId(userId: Long): List<DiaryGetListRes> {
        return jpaQueryFactory.select(QDiaryGetListRes(diary.id, diary.date, diary.title, diary.content))
            .from(diary)
            .where(
                diary.user.id.eq(userId)
            )
            .fetch()
    }
}