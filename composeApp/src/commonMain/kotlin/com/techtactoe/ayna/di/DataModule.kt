package com.techtactoe.ayna.di

import com.techtactoe.ayna.data.repository.MockAppointmentRepositoryImpl
import com.techtactoe.ayna.data.repository.MockProfileRepositoryImpl
import com.techtactoe.ayna.data.repository.MockSalonRepositoryImpl
import com.techtactoe.ayna.domain.repository.AppointmentRepository
import com.techtactoe.ayna.domain.repository.ProfileRepository
import com.techtactoe.ayna.domain.repository.SalonRepository
import com.techtactoe.ayna.domain.usecase.BookAppointmentUseCase
import com.techtactoe.ayna.domain.usecase.CancelAppointmentUseCase
import com.techtactoe.ayna.domain.usecase.CreateAppointmentUseCase
import com.techtactoe.ayna.domain.usecase.GetAvailableTimeSlotsUseCase
import com.techtactoe.ayna.domain.usecase.GetNearbySalonsUseCase
import com.techtactoe.ayna.domain.usecase.GetRecommendedSalonsUseCase
import com.techtactoe.ayna.domain.usecase.GetSalonDetailsUseCase
import com.techtactoe.ayna.domain.usecase.GetUserAppointmentsUseCase
import com.techtactoe.ayna.domain.usecase.GetUserProfileUseCase
import com.techtactoe.ayna.domain.usecase.JoinWaitlistUseCase
import com.techtactoe.ayna.domain.usecase.SearchSalonsUseCase
import com.techtactoe.ayna.presentation.ui.screens.appointments.AppointmentsViewModel
import com.techtactoe.ayna.presentation.ui.screens.booking.BookingConfirmationViewModel
import com.techtactoe.ayna.presentation.ui.screens.salon.SalonDetailViewModel
import com.techtactoe.ayna.presentation.ui.screens.selecttime.SelectTimeViewModel
import com.techtactoe.ayna.presentation.ui.screens.waitlist.JoinWaitlistViewModel
import com.techtactoe.ayna.presentation.viewmodel.HomeViewModel

/**
 * Dependency injection configuration for the Clean Architecture layers
 * This demonstrates how to wire up the repositories, use cases, and ViewModels
 */
object DataModule {

    // Repository instances (in real app, these would be provided by DI framework like Koin)
    private val salonRepository: SalonRepository = MockSalonRepositoryImpl()
    private val appointmentRepository: AppointmentRepository = MockAppointmentRepositoryImpl()
    private val profileRepository: ProfileRepository = MockProfileRepositoryImpl()

    // Use case instances
    val getRecommendedSalonsUseCase = GetRecommendedSalonsUseCase(salonRepository)
    val getNearbySalonsUseCase = GetNearbySalonsUseCase(salonRepository)
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
    fun createHomeViewModel(): HomeViewModel {
        return HomeViewModel(getRecommendedSalonsUseCase, getNearbySalonsUseCase)
    }

    fun createAppointmentsViewModel(): AppointmentsViewModel {
        return AppointmentsViewModel(getUserAppointmentsUseCase)
    }

    fun createSelectTimeViewModel(): SelectTimeViewModel {
        return SelectTimeViewModel(getAvailableTimeSlotsUseCase, createAppointmentUseCase)
    }

    fun createJoinWaitlistViewModel(): JoinWaitlistViewModel {
        return JoinWaitlistViewModel(joinWaitlistUseCase)
    }

    fun createSalonDetailViewModel(): SalonDetailViewModel {
        return SalonDetailViewModel()
    }

    fun createBookingConfirmationViewModel(): BookingConfirmationViewModel {
        return BookingConfirmationViewModel()
    }
}
