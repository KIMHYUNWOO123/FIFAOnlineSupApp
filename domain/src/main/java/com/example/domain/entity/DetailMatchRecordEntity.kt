package com.example.domain.entity

import androidx.annotation.Keep

@Keep
data class DetailMatchRecordEntity(
    val matchId: String,
    val matchDate: String,
    val matchType: Int,
    val matchInfo: List<MatchInfo>
)

data class MatchInfo(
    val accessId: String,
    val nickname: String,
    val matchDetail: MatchDetail,
    val shoot: Shoot,
    val shootDetail: List<ShootDetail>,
    val pass: Pass,
    val defence: Defence,
    val player: List<Player>
)

data class MatchDetail(
    val seasonId: Int,
    val matchResult: String,
    val matchEndType: Int,
    val systemPause: Int,
    val foul: Int,
    val injury: Int,
    val redCards: Int,
    val yellowCards: Int,
    val dribble: Int,
    val cornerKick: Int,
    val possession: Int,
    val offsideCount: Int,
    val averageRating: Double,
    val controller: String
)

data class Shoot(
    val shootTotal: Int,
    val effectiveShootTotal: Int,
    val shootOutScore: Int,
    val goalTotal: Int,
    val goalTotalDisplay: Int,
    val ownGoal: Int,
    val shootHeading: Int,
    val goalHeading: Int,
    val shootFreekick: Int,
    val goalFreekick: Int,
    val shootInPenalty: Int,
    val goalInPenalty: Int,
    val shootOutPenalty: Int,
    val goalOutPenalty: Int,
    val shootPenaltyKick: Int,
    val goalPenaltyKick: Int,
)

data class ShootDetail(
    val goalTime: Int,
    val x: Double,
    val y: Double,
    val type: Int,
    val result: Int,
    val spId: Int,
    val spGrade: Int,
    val spLevel: Int,
    val spIdType: Boolean,
    val assist: Boolean,
    val assistSpId: Int,
    val assistX: Double,
    val assistY: Double,
    val hitPost: Boolean,
    val inPenalty: Boolean
)

data class Pass(
    val passTry: Int,
    val passSuccess: Int,
    val shortPassTry: Int,
    val shortPassSuccess: Int,
    val longPassTry: Int,
    val longPassSuccess: Int,
    val bouncingLobPassTry: Int,
    val bouncingLobPassSuccess: Int,
    val drivenGroundPassTry: Int,
    val drivenGroundPassSuccess: Int,
    val throughPassTry: Int,
    val throughPassSuccess: Int,
    val lobbedThroughPassTry: Int,
    val lobbedThroughPassSuccess: Int,
)

data class Defence(
    val blockTry: Int,
    val blockSuccess: Int,
    val tackleTry: Int,
    val tackleSuccess: Int,
)

data class Player(
    val spId: Int,
    val spPosition: Int,
    val spGrade: Int,
    val status: Status,
)

data class Status(
    val shoot: Int,
    val effectiveShoot: Int,
    val assist: Int,
    val goal: Int,
    val dribble: Int,
    val intercept: Int,
    val defending: Int,
    val passTry: Int,
    val passSuccess: Int,
    val dribbleTry: Int,
    val dribbleSuccess: Int,
    val ballPossesionTry: Int,
    val ballPossesionSuccess: Int,
    val aerialTry: Int,
    val aerialSuccess: Int,
    val lockTry: Int,
    val block: Int,
    val tackleTry: Int,
    val tackle: Int,
    val yellowCards: Int,
    val redCards: Int,
    val spRating: Float,
)