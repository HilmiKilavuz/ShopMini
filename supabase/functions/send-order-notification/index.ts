import { serve } from "https://deno.land/std@0.168.0/http/server.ts";
import { createClient } from "npm:@supabase/supabase-js@2";
import { JWT } from "npm:google-auth-library@9";

// Firebase Proje ID'ni buraya yazman gerekir. JSON dosyasından da okunabilir.
// JSON formatındaki gizli anahtarı Deno.env'den (Supabase Secrets) okuyoruz.
const serviceAccountKey = Deno.env.get("FIREBASE_SERVICE_ACCOUNT");

// Google OAuth2 Token almak için yardımcı fonksiyon
async function getAccessToken(clientEmail: string, privateKey: string): Promise<string> {
  const jwtClient = new JWT({
    email: clientEmail,
    key: privateKey,
    scopes: ["https://www.googleapis.com/auth/firebase.messaging"],
  });
  const tokens = await jwtClient.authorize();
  return tokens.access_token!;
}

serve(async (req) => {
  try {
    // 1. Webhook'tan gelen veriyi (yeni sipariş) oku
    const payload = await req.json();
    console.log("Webhook tetiklendi. Payload:", payload);

    const record = payload.record; // Yeni eklenen sipariş kaydı (order tablosundan)
    if (!record || !record.user_id) {
      throw new Error("Geçersiz sipariş verisi (user_id bulunamadı).");
    }

    const userId = record.user_id;
    const totalAmount = record.total_amount;

    // 2. Supabase Veritabanına bağlan
    // Bu değişkenler Supabase Edge Functions ortamında otomatik olarak bulunur
    const supabaseUrl = Deno.env.get("SUPABASE_URL") ?? "";
    const supabaseServiceKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY") ?? "";
    const supabase = createClient(supabaseUrl, supabaseServiceKey);

    // 3. Siparişi veren kullanıcının FCM Token'ını profiles tablosundan çek
    const { data: profile, error: profileError } = await supabase
      .from("profiles")
      .select("fcm_token")
      .eq("id", userId)
      .single();

    if (profileError || !profile || !profile.fcm_token) {
      console.log(`Kullanıcı ${userId} için FCM token bulunamadı.`);
      return new Response("Bildirim gönderilmedi, FCM token yok.", { status: 200 });
    }

    const fcmToken = profile.fcm_token;

    // 4. Firebase Service Account JSON'ı ayrıştır
    if (!serviceAccountKey) {
      throw new Error("FIREBASE_SERVICE_ACCOUNT gizli anahtarı bulunamadı.");
    }
    const serviceAccount = JSON.parse(serviceAccountKey);

    // 5. Firebase için geçerli bir erişim token'ı (OAuth2) al
    const accessToken = await getAccessToken(
      serviceAccount.client_email,
      serviceAccount.private_key
    );

    // 6. FCM v1 API'sine bildirimi gönder
    const fcmMessage = {
      message: {
        token: fcmToken,
        notification: {
          title: "✅ Siparişiniz Alındı!",
          body: `₺${Number(totalAmount).toFixed(2)} tutarındaki siparişiniz başarıyla oluşturuldu.`,
        },
        android: {
          priority: "HIGH",
        }
      },
    };

    const projectId = serviceAccount.project_id;
    const fcmResponse = await fetch(
      `https://fcm.googleapis.com/v1/projects/${projectId}/messages:send`,
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${accessToken}`,
          "Content-Type": "application/json",
        },
        body: JSON.stringify(fcmMessage),
      }
    );

    if (!fcmResponse.ok) {
      const errorText = await fcmResponse.text();
      throw new Error(`FCM Hatası: ${errorText}`);
    }

    console.log("Bildirim başarıyla gönderildi!");
    return new Response(JSON.stringify({ success: true }), {
      headers: { "Content-Type": "application/json" },
      status: 200,
    });

  } catch (error) {
    console.error("Hata oluştu:", error);
    return new Response(JSON.stringify({ error: error.message }), {
      headers: { "Content-Type": "application/json" },
      status: 400,
    });
  }
});
