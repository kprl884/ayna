package com.techtactoe.ayna.di

import com.techtactoe.ayna.data.repository.MockAppointmentRepositoryImpl
import com.techtactoe.ayna.data.repository.MockProfileRepositoryImpl
import com.techtactoe.ayna.data.repository.MockSalonRepositoryV2Impl
import com.techtactoe.ayna.domain.repository.AppointmentRepository
import com.techtactoe.ayna.domain.repository.ProfileRepository
import com.techtactoe.ayna.domain.repository.SalonRepositoryV2
import com.techtactoe.ayna.domain.usecase.*
import com.techtactoe.ayna.presentation.viewmodel.HomeViewModelV2
import com.techtactoe.ayna.presentation.ui.screens.appointments.AppointmentsViewModel
import com.techtactoe.ayna.presentation.ui.screens.selecttime.SelectTimeViewModel
import com.techtactoe.ayna.presentation.ui.screens.waitlist.JoinWaitlistViewModel

/**
 * Dependency injection configuration for the Clean Architecture layers
 * This demonstrates how to wire up the repositories, use cases, and ViewModels
 */
object DataModule {
    
    // Repository instances (in real app, these would be provided by DI framework like Koin)
    private val salonRepository: SalonRepositoryV2 = MockSalonRepositoryV2Impl()
    private val appointmentRepository: AppointmentRepository = MockAppointmentRepositoryImpl()
    private val profileRepository: ProfileRepository = MockProfileRepositoryImpl()
    
    // Use case instances
    val getRecommendedSalonsUseCase = GetRecommendedSalonsUseCase(salonRepository)
    val getSalonDetailsUseCase = GetSalonDetailsUseCase(salonRepository)
    val searchSalonsUseCase = SearchSalonsUseCase(salonRepository)
    val getUserAppointmentsUseCase = GetUserAppointmentsUseCase(appointmentRepository)
    val bookAppointmentUseCase = BookAppointmentUseCase(appointmentRepository)
    val cancelAppointmentUseCase = CancelAppointmentUseCase(appointmentRepository)
    val getUserProfileUseCase = GetUserProfileUseCase(profileRepository)
    val getAvailableTimeSlotsUseCase = GetAvailableTimeSlotsUseCase(appointmentRepository)
    val joinWaitlistUseCase = JoinWaitlistUseCase(appointmentRepository)
    val createAppointmentUseCase = CreateAppointmentUseCase(appointmentRepository)
    
    // ViewModel factory functions
    fun createHomeViewModel(): HomeViewModelV2 {
        return HomeViewModelV2(getRecommendedSalonsUseCase)
    }

    fun createAppointmentsViewModel(): AppointmentsViewModel {
        return AppointmentsViewModel(getUserAppointmentsUseCase)
    }

    fun createSelectTimeViewModel(): SelectTimeViewModel {
        return SelectTimeViewModel(getAvailableTimeSlotsUseCase)
    }

    fun createJoinWaitlistViewModel(): JoinWaitlistViewModel {
        return JoinWaitlistViewModel(joinWaitlistUseCase)
    }
}
