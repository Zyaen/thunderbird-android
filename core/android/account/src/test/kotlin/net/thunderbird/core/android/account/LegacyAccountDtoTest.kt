package net.thunderbird.core.android.account

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.fsck.k9.mail.Address
import com.fsck.k9.mail.AuthType
import com.fsck.k9.mail.ConnectionSecurity
import com.fsck.k9.mail.ServerSettings
import kotlin.test.Test

class LegacyAccountDtoTest {

    @Test
    fun `findIdentity returns matching identity for subaddressed address`() {
        val testSubject = createAccount(replyAsSubAddressed = false)

        val result = testSubject.findIdentity(Address("test+alias@example.com"))

        assertThat(result).isEqualTo(Identity(email = "test@example.com"))
    }

    @Test
    fun `findIdentity keeps recipient subaddress when enabled`() {
        val testSubject = createAccount(replyAsSubAddressed = true)

        val result = testSubject.findIdentity(Address("test+alias@example.com"))

        assertThat(result).isNotNull()
        assertThat(result?.email).isEqualTo("test+alias@example.com")
    }

    private fun createAccount(replyAsSubAddressed: Boolean): LegacyAccountDto {
        return LegacyAccountDto(uuid = "test").apply {
            identities = mutableListOf(Identity(email = "test@example.com"))
            outgoingServerSettings = ServerSettings(
                type = "smtp",
                host = "smtp.example.com",
                port = 587,
                connectionSecurity = ConnectionSecurity.STARTTLS_REQUIRED,
                authenticationType = AuthType.PLAIN,
                username = "test",
                password = "password",
                clientCertificateAlias = null,
            )
            this.replyAsSubAddressed = replyAsSubAddressed
        }
    }
}
