package com.techtactoe.ayna.designsystem.theme

/**
 * Centralized string resources for the Ayna
 * All UI text must be managed here for future multi-language support
 */
object StringResources {
    
    // MARK: - Authentication
    val login_button_text = "Giriş Yap"
    val register_button_text = "Kayıt Ol"
    val email_placeholder = "E-posta"
    val password_placeholder = "Şifre"
    val forgot_password_text = "Şifremi Unuttum"
    val welcome_message = "Ayna'ya Hoş Geldiniz"
    val login_success_message = "Başarıyla giriş yaptınız"
    
    // MARK: - Navigation
    val home_tab_title = "Ana Sayfa"
    val search_tab_title = "Ara"
    val bookings_tab_title = "Randevularım"
    val profile_tab_title = "Profil"
    
    // MARK: - Search & Discovery
    val search_placeholder = "Salon, hizmet veya uzman ara..."
    val search_icon_description = "Arama ikonu"
    val filter_button_text = "Filtrele"
    val sort_button_text = "Sırala"
    val nearby_text = "Yakınımdakiler"
    val popular_text = "Popüler"
    val new_text = "Yeni"
    
    // MARK: - Business & Services
    val book_now_button = "Şimdi Rezervasyon Yap"
    val view_profile_button = "Profili Görüntüle"
    val follow_button = "Takip Et"
    val unfollow_button = "Takibi Bırak"
    val rating_text = "Değerlendirme"
    val reviews_text = "Yorumlar"
    val services_text = "Hizmetler"
    val about_text = "Hakkında"
    val contact_text = "İletişim"
    
    // MARK: - Booking
    val select_service_text = "Hizmet Seçin"
    val select_date_text = "Tarih Seçin"
    val select_time_text = "Saat Seçin"
    val select_staff_text = "Uzman Seçin"
    val confirm_booking_text = "Rezervasyonu Onayla"
    val booking_confirmed_text = "Rezervasyon Onaylandı"
    val booking_cancelled_text = "Rezervasyon İptal Edildi"
    val reschedule_text = "Yeniden Planla"
    val cancel_booking_text = "Rezervasyonu İptal Et"
    val continue_button_text = "Devam Et"
    val remove_service_text = "Hizmeti Kaldır"
    val no_preference_text = "Tercih Yok"
    val any_stylist_text = "Herhangi bir uzman"
    
    // MARK: - Payment
    val payment_method_text = "Ödeme Yöntemi"
    val total_amount_text = "Toplam Tutar"
    val pay_button_text = "Öde"
    val payment_successful_text = "Ödeme Başarılı"
    val payment_failed_text = "Ödeme Başarısız"
    val tip_text = "Bahşiş"
    val add_tip_text = "Bahşiş Ekle"
    
    // MARK: - Profile
    val edit_profile_text = "Profili Düzenle"
    val personal_info_text = "Kişisel Bilgiler"
    val preferences_text = "Tercihler"
    val notifications_text = "Bildirimler"
    val language_text = "Dil"
    val logout_text = "Çıkış Yap"
    val delete_account_text = "Hesabı Sil"
    
    // MARK: - Notifications
    val booking_reminder_24h = "Yarın saat %s'de randevunuz var"
    val booking_reminder_2h = "2 saat sonra randevunuz var"
    val booking_reminder_30min = "30 dakika sonra randevunuz var"
    val new_follower_text = "%s sizi takip etmeye başladı"
    val new_work_posted_text = "%s yeni çalışma paylaştı"
    
    // MARK: - Error Messages
    val network_error_text = "İnternet bağlantısı hatası"
    val general_error_text = "Bir hata oluştu, lütfen tekrar deneyin"
    val invalid_email_text = "Geçersiz e-posta adresi"
    val invalid_password_text = "Şifre en az 6 karakter olmalıdır"
    val booking_failed_text = "Rezervasyon yapılamadı"
    val payment_failed_generic_text = "Ödeme işlemi başarısız oldu"
    
    // MARK: - Loading & States
    val loading_text = "Yükleniyor..."
    val no_results_text = "Sonuç bulunamadı"
    val empty_bookings_text = "Henüz randevunuz yok"
    val empty_following_text = "Henüz kimseyi takip etmiyorsunuz"
    val retry_button_text = "Tekrar Dene"
    val refresh_text = "Yenile"
    
    // MARK: - Success Messages
    val profile_updated_text = "Profil güncellendi"
    val booking_created_text = "Rezervasyon oluşturuldu"
    val review_submitted_text = "Yorum gönderildi"
    val settings_saved_text = "Ayarlar kaydedildi"
    
    // MARK: - Business Categories
    val hair_salon_text = "Kuaför"
    val nail_salon_text = "Tırnak Salonu"
    val spa_text = "Spa"
    val massage_text = "Masaj"
    val barber_text = "Berber"
    val beauty_salon_text = "Güzellik Salonu"
    val dental_clinic_text = "Diş Kliniği"
    val fitness_center_text = "Spor Merkezi"
    
    // MARK: - Services
    val haircut_text = "Saç Kesimi"
    val hair_coloring_text = "Saç Boyama"
    val manicure_text = "Manikür"
    val pedicure_text = "Pedikür"
    val facial_text = "Cilt Bakımı"
    val massage_service_text = "Masaj"
    val waxing_text = "Ağda"
    val makeup_text = "Makyaj"
    
    // MARK: - Time & Date
    val today_text = "Bugün"
    val tomorrow_text = "Yarın"
    val this_week_text = "Bu Hafta"
    val next_week_text = "Gelecek Hafta"
    val morning_text = "Sabah"
    val afternoon_text = "Öğleden Sonra"
    val evening_text = "Akşam"
    
    // MARK: - Accessibility
    val close_button_description = "Kapat butonu"
    val back_button_description = "Geri butonu"
    val menu_button_description = "Menü butonu"
    val favorite_button_description = "Favori butonu"
    val rating_star_description = "Yıldız derecelendirme ikonu"
    val share_button_description = "Paylaş butonu"
    val calendar_button_description = "Takvim butonu"
    val location_button_description = "Konum butonu"
    val phone_button_description = "Telefon butonu"
} 