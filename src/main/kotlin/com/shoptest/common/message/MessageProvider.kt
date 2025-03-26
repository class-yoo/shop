package com.shoptest.common.message

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

@Component
class MessageProvider(
    private val messageSource: MessageSource
) {

    fun get(code: String, vararg args: Any?): String {
        val locale = LocaleContextHolder.getLocale() // 현재 요청의 Locale (기본 ko)
        return messageSource.getMessage(code, args, locale)
    }
}
