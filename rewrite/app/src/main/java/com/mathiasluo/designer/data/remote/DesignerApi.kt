package com.mathiasluo.designer.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 路线 B：自建后端契约示例。
 * 路线 A 可另建 DribbbleApi，字段映射到同一 domain 模型。
 */
interface DesignerApi {
    @GET("shots")
    suspend fun getShots(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<ShotDto>

    @GET("shots/{id}")
    suspend fun getShot(@Path("id") id: String): ShotDto
}
