package com.fsck.k9.mail

/**
 * Container for incoming or outgoing server settings
 */
data class ServerSettings @JvmOverloads constructor(
    @JvmField val type: String,
    @JvmField val host: String?,
    @JvmField val port: Int,
    @JvmField val connectionSecurity: ConnectionSecurity,
    @JvmField val authenticationType: AuthType,
    @JvmField val username: String,
    @JvmField val password: String?,
    @JvmField val clientCertificateAlias: String?,
    val extra: Map<String, String?> = emptyMap(),
) {
    val isMissingCredentials: Boolean = when (authenticationType) {
        AuthType.NONE -> false
        AuthType.EXTERNAL -> clientCertificateAlias == null
        AuthType.XOAUTH2 -> username.isBlank()
        else -> username.isBlank() || password.isNullOrBlank()
    }

    init {
        require(type == type.lowercase()) { "type must be all lower case" }
        require(username.contains(LINE_BREAK).not()) { "username must not contain line break" }
        require(password?.contains(LINE_BREAK) != true) { "password must not contain line break" }
    }

    fun newPassword(newPassword: String?): ServerSettings {
        return this.copy(password = newPassword)
    }

    fun newAuthenticationType(authType: AuthType): ServerSettings {
        return this.copy(authenticationType = authType)
    }

    fun newRecipientDelimiter(delimiter: String): ServerSettings {
        val newExtra = extra.toMutableMap()
        newExtra["recipientDelimiter"] = delimiter.trim().takeIf { it.isNotBlank() }
        return this.copy(extra = newExtra)
    }

    fun getRecipientDelimiter(): String {
        return extra["recipientDelimiter"] ?: "+"
    }

    companion object {
        private val LINE_BREAK = "[\\r\\n]".toRegex()
    }
}
