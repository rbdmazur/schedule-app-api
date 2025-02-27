package com.example.di

import com.example.repository.repositories.FacultyRepository
import com.example.repository.repositories.ScheduleRepository
import com.example.repository.repositories.UsersRepository
import com.example.service.FacultyService
import com.example.service.ScheduleService
import com.example.service.UserService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataModule {

    @Provides
    @Singleton
    fun provideUsersRepository(): UsersRepository = UsersRepository()

    @Provides
    @Singleton
    fun provideScheduleRepository(): ScheduleRepository = ScheduleRepository()

    @Provides
    @Singleton
    fun provideFacultyRepository(): FacultyRepository = FacultyRepository()

    @Provides
    @Singleton
    fun provideUserService(usersRepository: UsersRepository): UserService = UserService(usersRepository)

    @Provides
    @Singleton
    fun provideScheduleService(scheduleRepository: ScheduleRepository): ScheduleService = ScheduleService(scheduleRepository)

    @Provides
    @Singleton
    fun provideFacultyService(facultyRepository: FacultyRepository): FacultyService = FacultyService(facultyRepository)
}