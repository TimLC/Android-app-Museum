package fr.app.service;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface RequeteMusee {
    @GET("5c637e3c61e55c808b31e1ae12a57fc5c4842b4b/")
    Call<Post> getMusee();

    @GET("{id}/")
    Call<Post> getMusee(@Path("id") String postId);

    @GET("{id}/pictures/")
    Call<List<String>> getListePhoto(@Path("id") String postId);

    @GET("{id}/pictures/{idPhoto}")
    Call<ResponseBody> getImageMusee(@Path("id") String postId, @Path("idPhoto") String postPhoto);

    @Multipart
    @POST("{id}/pictures/")
    Call<String> uploadImage(@Part MultipartBody.Part image, @Path("id") String postId);
}
