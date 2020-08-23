package com.devrock.beautyappv2.net

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import javax.sql.StatementEvent

data class Status(
    val info: Info
)

data class Info(
    val status: String,
    val trackingId: String
)

data class SessionResponse(
    val info: Info,
    val payload: SessionPayload
)

data class SessionPayload(
    val text: String?,
    val session: String?,
    val registered: Boolean?
)

data class AuthConfirmBody(
    val phone: String,
    val code: String
)

data class AccountBody (
    val name: String,
    val surname: String,
    val gender: String = "Female"
)

data class Geo (
    val city: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)

data class SalonListItem (
    val id: Int,
    val name: String,
    val geo: Geo,
    val hourRentEnd: String?,
    val hourRentStart: String?,
    val rating: Int,
    val thumbnailPhoto: String,
    val daysRentStart: String?,
    val daysType: List<String>?,
    val distance: Double?
)

data class SalonListResponse (
    val payload: SalonListPayload,
    val info: Info
)

data class SalonListPayload (
        val list: List<SalonListItem>?,
        val text: String?
)

data class SalonByIdResponse (
    val payload: SalonByIdPayload,
    val info: Info
)

data class SalonByIdPayload (
    val info: SalonListItem,
    val description: String?,
    val mainPhoto: String,
    val photos: List<String>,
    val contacts: List<Contact>,
    val salonSchedule: List<ScheduleItem>,
    val additionalServices: List<Service>?,
    val hourPrices: List<HourPrice>,
    val text: String?
)

data class Contact (
    val contactType: String,
    val value: String
)

data class ScheduleItem (
    val weekday: Int,
    val startTime: String,
    val endTime: String
)

data class CurrentAccountResponse (
    val payload: AccountPayload,
    val info: Info
)

data class  AccountPayload (
    val phone: String?,
    val name: String?,
    val surname: String?,
    val patronymic: String?,
    val gender: String?,
    val havePhoto: Boolean?,
    val birthDate: String?,
    val email: String?,
    val inn: String?,
    val text: String?
)

data class FormattedWeekday (
    val weekday: String,
    val startTime: String,
    val endTime: String
)

data class FormattedWeekday1 (
    val date: String,
    val time: String
)

data class SalonPhoto (
    val id: Int,
    val imgUrl: String
)

data class TimeslotsResponse (
    val payload: List<TimeSlot>,
    val info: Info
)

data class TimeSlot (
    val id: Int,
    val workplaceId: Int?,
    val start: String?,
    val end: String?,
    val status: String?,
    val date: String?,
    val text: String?
)

data class HourPricesResponse (
        val payload: List<HourPrice>,
        val info: Info
)

data class HourPrice (
    val hours: Int,
    val price: Int
)

data class AddHourEntryBody (
    val workplaceId: Int,
    val price: Int,
    val slots: List<Int>,
    val comment: String
)

data class Offer (
    val id: Int?,
    val salonId: Int?,
    val workplaceId: Int?,
    val daysType: String?,
    val price: Int?,
    val description: String?,
    val workdays: List<String>?,
    val fromTime: String?,
    val toTime: String?,
    val startAfter: String?
)

data class OffersResponse (
    val payload: List<Offer>,
    val info: Info
)

data class OfferDaysResponse (
    val payload: List<String>,
    val info: Info
)

data class OfferDays (
    val offerId: Int,
    val days: List<String>
)

data class Service (
    val id: Int,
    val serviceType: String,
    val title: String
)

data class Workplace (
    val id: Int,
    val name: String,
    val havePhoto: Boolean?,
    val rentType: String
)

data class WorkplacesResponse (
    val payload: List<Workplace>,
    val info: Info
)

data class OfferTimeslot (
    val timeSlot: TimeSlot,
    val price: Int
)

data class HourOfferResponse (
    val payload: List<HourOfferItem>,
    val info: Info
)

data class HourOfferItem (
    val timeSlot: TimeSlot,
    val price: Int
)

data class MasterEntry (
    val id: Int,
    val rentType: String,
    val price: Int,
    val salon: EntrySalon,
    val startDate: String,
    val status: String,
    val timeSlots: List<TimeSlot>?

)



data class EntrySalon (
    val id: Int,
    val name: String,
    val geo: Geo,
    val rating: Int,
    val thumbnailPhoto: String,
    val daysRentStart: String,
    val hourRentStart: String,
    val daysType: List<String>
)

data class MasterEntriesResponse (
    val payload: List<MasterEntry>,
    val info: Info
)








