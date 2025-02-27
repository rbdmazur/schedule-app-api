package com.example.di

import com.example.auth.hashing.HashingService
import com.example.auth.token.TokenConfig
import com.example.auth.token.TokenService
import com.example.service.FacultyService
import com.example.service.ScheduleService
import com.example.service.UserService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface RouteComponent {

    fun getUserService(): UserService
    fun getScheduleService(): ScheduleService
    fun getFacultyService(): FacultyService
}