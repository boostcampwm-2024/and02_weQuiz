package kr.boostcamp_2024.course.data.di

import kr.boostcamp_2024.course.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AddHeadInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-NCP-CLOVASTUDIO-API-KEY", BuildConfig.X_NCP_CLOVASTUDIO_API_KEY)
            .addHeader("X-NCP-APIGW-API-KEY", BuildConfig.X_NCP_APIGW_API_KEY)
            .addHeader("X-NCP-CLOVASTUDIO-REQUEST-ID", BuildConfig.X_NCP_CLOVASTUDIO_REQUEST_ID)
            .build()
        return chain.proceed(request)
    }
}
