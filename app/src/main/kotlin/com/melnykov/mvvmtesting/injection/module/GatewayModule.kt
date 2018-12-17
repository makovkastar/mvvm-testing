package com.melnykov.mvvmtesting.injection.module

import com.melnykov.mvvmtesting.data.gateway.LoginGateway
import com.melnykov.mvvmtesting.data.gateway.LoginGatewayImpl
import dagger.Binds
import dagger.Module

@Module
interface GatewayModule {
    @Binds
    fun bindLoginGateway(loginGateway: LoginGatewayImpl): LoginGateway
}
