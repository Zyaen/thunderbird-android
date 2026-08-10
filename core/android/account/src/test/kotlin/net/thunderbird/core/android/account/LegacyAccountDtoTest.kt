package net.thunderbird.core.android.account

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.fsck.k9.mail.Address
import com.fsck.k9.mail.AuthType
import com.fsck.k9.mail.ConnectionSecurity
import com.fsck.k9.mail.ServerSettings
import kotlin.test.Test

class LegacyAccountDtoTest {

    @Test
    fun `findIdentity returns exact matching identity when present`() {
        val testSubject = createAccount(replyAsSubAddressed = false)

        val result = testSubject.findIdentity(Address("test@example.com"))

        assertThat(result).isEqualTo(Identity(email = "test@example.com"))
    }

    @Test
    fun `findIdentity returns matching base identity for subaddressed address`() {
        val testSubject = createAccount(replyAsSubAddressed = false)

        val result = testSubject.findIdentity(Address("test+alias@example.com"))

        assertThat(result).isEqualTo(Identity(email = "test@example.com"))
    }

    @Test
    fun `findIdentity keeps recipient subaddress when replyAsSubAddressed is enabled`() {
        val testSubject = createAccount(replyAsSubAddressed = true)

        val result = testSubject.findIdentity(Address("test+alias@example.com"))

        assertThat(result).isNotNull()
        assertThat(result?.email).isEqualTo("test+alias@example.com")
    }

    @Test
    fun `findIdentity respects custom recipient delimiter`() {
        val testSubject = createAccount(replyAsSubAddressed = true, delimiter = "-")

        val result = testSubject.findIdentity(Address("test-alias@example.com"))

        assertThat(result).isNotNull()
        assertThat(result?.email).isEqualTo("test-alias@example.com")
    }

    @Test
    fun `findIdentity returns null when no matching identity exists`() {
        val testSubject = createAccount(replyAsSubAddressed = true)

        val result = testSubject.findIdentity(Address("unknown@example.com"))

        assertThat(result).isNull()
    }

    @Test
    fun `findIdentity handles case-insensitive matching`() {
        val testSubject = createAccount(replyAsSubAddressed = false)

        val result = testSubject.findIdentity(Address("Test+Alias@EXAMPLE.com"))

        assertThat(result).isEqualTo(Identity(email = "test@example.com"))
    }

    @Test
    fun `findIdentity prefers exact identity match over subaddressed base match`() {
        val baseIdentity = Identity(email = "test@example.com", name = "Base")
        val exactIdentity = Identity(email = "test+alias@example.com", name = "Exact")
        val testSubject = createAccount(replyAsSubAddressed = true).apply {
            identities = mutableListOf(baseIdentity, exactIdentity)
        }

        val result = testSubject.findIdentity(Address("test+alias@example.com"))

        assertThat(result).isEqualTo(exactIdentity)
    }

    @Test
    fun `findIdentity returns null for invalid subaddress with empty base local part`() {
        val testSubject = createAccount(replyAsSubAddressed = true)

        val result = testSubject.findIdentity(Address("+alias@example.com"))

        assertThat(result).isNull()
    }

    private fun createAccount(
        replyAsSubAddressed: Boolean,
        delimiter: String = "+",
    ): LegacyAccountDto {
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
            ).newRecipientDelimiter(delimiter)
            this.replyAsSubAddressed = replyAsSubAddressed
        }
    }
}
