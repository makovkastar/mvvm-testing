package com.melnykov.mvvmtesting.injection.module

import dagger.Binds
import dagger.Module
import com.melnykov.mvvmtesting.data.gateway.LoginGateway
import com.melnykov.mvvmtesting.data.gateway.LoginGatewayImpl

@Module
interface GatewayModule {
    @Binds
    fun bindLoginGateway(loginGateway: LoginGatewayImpl): LoginGateway
}
