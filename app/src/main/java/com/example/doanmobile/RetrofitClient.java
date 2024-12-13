package com.example.doanmobile;

import android.util.Log;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.logging.HttpLoggingInterceptor; // Đảm bảo import đúng gói

public class RetrofitClient {
    private static final String BASE_URL = "https:/192.168.238.87:5000";
    private static Retrofit retrofit = null;

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
            builder.addInterceptor(logging);  // Thêm logging interceptor
            builder.addInterceptor(new Interceptor() {  // Thêm interceptor để set Content-Type
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()

                            .addHeader("Content-Type", "application/json")
                            .build();
                    Log.d("Request123", "Request URL: " + newRequest.url());
                    Log.d("Request123123", "body: " + newRequest.body().toString());
                    return chain.proceed(newRequest);
                }
            });

            builder.connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS); // Connection timeout
            builder.readTimeout(30, java.util.concurrent.TimeUnit.SECONDS);    // Read timeout
            builder.writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS);   // Write timeout

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Api getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(Api.class);
    }
}
