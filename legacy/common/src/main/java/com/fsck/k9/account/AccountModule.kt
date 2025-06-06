package com.fsck.k9.account

import app.k9mail.feature.account.common.AccountCommonExternalContract
import app.k9mail.feature.account.edit.AccountEditExternalContract
import app.k9mail.feature.account.setup.AccountSetupExternalContract
import app.k9mail.feature.settings.import.SettingsImportExternalContract
import org.koin.dsl.module

val newAccountModule = module {
    factory<AccountSetupExternalContract.AccountOwnerNameProvider> {
        AccountOwnerNameProvider(
            preferences = get(),
        )
    }

    factory<AccountCommonExternalContract.AccountStateLoader> {
        AccountStateLoader(
            accountManager = get(),
        )
    }

    factory<AccountEditExternalContract.AccountServerSettingsUpdater> {
        AccountServerSettingsUpdater(
            accountManager = get(),
        )
    }

    factory<SettingsImportExternalContract.AccountActivator> {
        AccountActivator(
            context = get(),
            preferences = get(),
            messagingController = get(),
        )
    }

    factory<DeletePolicyProvider> { DefaultDeletePolicyProvider() }
}
