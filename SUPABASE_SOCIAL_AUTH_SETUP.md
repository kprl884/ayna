# Supabase Social Authentication Setup

Bu dokümanda Google ve Apple ile giriş özelliklerini Supabase'de nasıl aktif edeceğinizi açıklayacağım.

## 1. Google OAuth Setup

### 1.1 Google Cloud Console'da OAuth 2.0 Client ID oluşturma

1. [Google Cloud Console](https://console.cloud.google.com/)'a gidin
2. Proje seçin veya yeni proje oluşturun
3. "APIs & Services" > "Credentials" bölümüne gidin
4. "Create Credentials" > "OAuth 2.0 Client IDs" seçin
5. Application type olarak "Web application" seçin
6. Authorized redirect URIs'e şunu ekleyin:
   ```
   https://[YOUR_PROJECT_REF].supabase.co/auth/v1/callback
   ```
7. Client ID'yi kopyalayın

### 1.2 Supabase Dashboard'da Google Provider'ı aktif etme

1. [Supabase Dashboard](https://supabase.com/dashboard)'a gidin
2. Projenizi seçin
3. "Authentication" > "Providers" bölümüne gidin
4. "Google" provider'ını bulun ve "Enable" yapın
5. Client ID ve Client Secret'ı girin:
   - Client ID: Google Cloud Console'dan aldığınız ID
   - Client Secret: Google Cloud Console'dan aldığınız secret

### 1.3 Android uygulamasında kullanım

`composeApp/src/androidMain/kotlin/com/techtactoe/ayna/data/auth/PlatformAuthManager.kt` dosyasında:

```kotlin
.requestIdToken("YOUR_WEB_CLIENT_ID") // Buraya Google Cloud Console'dan aldığınız Client ID'yi yazın
```

## 2. Apple Sign-In Setup

### 2.1 Apple Developer Console'da Sign in with Apple aktif etme

1. [Apple Developer Console](https://developer.apple.com/)'a gidin
2. "Certificates, Identifiers & Profiles" bölümüne gidin
3. "Identifiers" > "App IDs" seçin
4. Uygulamanızı seçin ve "Sign in with Apple" capability'sini aktif edin
5. "Services IDs" bölümünde yeni bir Service ID oluşturun:
   - Description: "Sign in with Apple for [Your App]"
   - Identifier: `com.yourcompany.yourapp.signin`
   - "Sign in with Apple" checkbox'ını işaretleyin
   - Primary App ID olarak uygulamanızı seçin

### 2.2 Supabase Dashboard'da Apple Provider'ı aktif etme

1. Supabase Dashboard'da "Authentication" > "Providers" bölümüne gidin
2. "Apple" provider'ını bulun ve "Enable" yapın
3. Gerekli bilgileri girin:
   - Service ID: Apple Developer Console'dan aldığınız Service ID
   - Team ID: Apple Developer Console'dan aldığınız Team ID
   - Key ID: Apple Developer Console'dan oluşturduğunuz key'in ID'si
   - Private Key: Apple Developer Console'dan oluşturduğunuz private key

## 3. Environment Variables

Supabase URL ve anon (public) key nerede bulunur?
- Supabase Dashboard > Project Settings > API
- Project URL: https://[YOUR_PROJECT_REF].supabase.co
- anon (public) key: "Project API keys" bölümündeki "anon public" anahtar

### 3.1 Android

Artık Supabase bilgileriniz Gradle üzerinden BuildConfig'e enjekte ediliyor. Değerleri projenize şu yöntemlerden biriyle sağlayın:

- Yöntem A: local.properties dosyasına ekleyin (commit etmeyin):
```
SUPABASE_URL=https://[YOUR_PROJECT_REF].supabase.co
SUPABASE_ANON_KEY=[YOUR_ANON_KEY]
```

- Yöntem B: Build öncesi ortam değişkeni olarak ayarlayın:
- macOS/Linux:
```
export SUPABASE_URL=https://[YOUR_PROJECT_REF].supabase.co
export SUPABASE_ANON_KEY=[YOUR_ANON_KEY]
```
- Windows (PowerShell):
```
$Env:SUPABASE_URL="https://[YOUR_PROJECT_REF].supabase.co"
$Env:SUPABASE_ANON_KEY="[YOUR_ANON_KEY]"
```

Kod tarafında `composeApp/src/androidMain/kotlin/com/techtactoe/ayna/data/supabase/SupabaseEnv.android.kt` dosyası bu değerleri `BuildConfig.SUPABASE_URL` ve `BuildConfig.SUPABASE_ANON_KEY` üzerinden okur. Değerler boşsa çalışma zamanı anlaşılır bir hata verir.

### 3.2 iOS

iOS tarafında değerleri Info.plist'e ekleyin (`iosApp/iosApp/Info.plist`):
```xml
<key>SUPABASE_URL</key>
<string>https://[YOUR_PROJECT_REF].supabase.co</string>
<key>SUPABASE_ANON_KEY</key>
<string>[YOUR_ANON_KEY]</string>
```

Paylaşımlı modülde `composeApp/src/iosMain/kotlin/com/techtactoe/ayna/data/supabase/SupabaseEnv.ios.kt` bu anahtarları NSBundle üzerinden okur ve eksikse anlaşılır bir hata üretir.

## 4. Test Etme

1. Uygulamayı çalıştırın
2. Auth ekranında "Continue with Google" veya "Continue with Apple" butonlarına tıklayın
3. Giriş işleminin başarılı olduğunu kontrol edin
4. Supabase Dashboard'da "Authentication" > "Users" bölümünde yeni kullanıcının oluştuğunu kontrol edin

## 5. Troubleshooting

### Google Sign-In hatası
- Client ID'nin doğru olduğundan emin olun
- Redirect URI'nin Supabase projenizle eşleştiğinden emin olun
- Google Cloud Console'da OAuth consent screen'in yapılandırıldığından emin olun

### Apple Sign-In hatası
- Service ID'nin doğru olduğundan emin olun
- Private key'in doğru formatta olduğundan emin olun
- Team ID'nin doğru olduğundan emin olun

## 6. Güvenlik Notları

- Client ID ve Secret'ları asla public repository'de paylaşmayın
- Production ortamında environment variables kullanın
- Apple private key'ini güvenli bir şekilde saklayın
- OAuth consent screen'de gerekli izinleri belirtin
