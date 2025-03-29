package com.shoptest.common.message

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

@Component
class MessageProvider(
    private val messageSource: MessageSource
) {

    fun get(code: String, vararg args: Any?): String {
        val locale = LocaleContextHolder.getLocale()
        return messageSource.getMessage(code, args, locale)
    }
}
