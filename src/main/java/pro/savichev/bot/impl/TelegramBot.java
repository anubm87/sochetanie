package pro.savichev.bot.impl;

import com.google.gson.Gson;
import com.pengrad.telegrambot.impl.FileApi;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import io.reactivex.Single;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Map;

public class TelegramBot {
    private static final String API_URL = "https://api.telegram.org/bot";

    private String baseUrl;
    private OkHttpClient client;
    private Gson gson;
    private final FileApi fileApi;

    public TelegramBot(String botToken) {
        baseUrl = API_URL + botToken + "/";
        fileApi = new FileApi(botToken);
    }

    <T extends BaseRequest, R extends BaseResponse> Single<R> send(final BaseRequest<T, R> request){
        return Single.create(emitter -> {
            Response response = client.newCall(createRequest(request)).execute();
            R result = gson.fromJson(response.body().string(), request.getResponseType());
            emitter.onSuccess(result);
        });
    }

    public String getFullFilePath(com.pengrad.telegrambot.model.File file) {
        return fileApi.getFullFilePath(file.filePath());
    }

    private Request createRequest(BaseRequest request) {
        return new Request.Builder()
                .url(baseUrl + request.getMethod())
                .post(createRequestBody(request))
                .build();
    }

    private RequestBody createRequestBody(BaseRequest<?, ?> request) {
        if (request.isMultipart()) {
            MediaType contentType = MediaType.parse(request.getContentType());

            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            for (Map.Entry<String, Object> parameter : request.getParameters().entrySet()) {
                String name = parameter.getKey();
                Object value = parameter.getValue();
                if (value instanceof byte[]) {
                    builder.addFormDataPart(name, request.getFileName(), RequestBody.create(contentType, (byte[]) value));
                } else if (value instanceof File) {
                    builder.addFormDataPart(name, request.getFileName(), RequestBody.create(contentType, (File) value));
                } else {
                    builder.addFormDataPart(name, String.valueOf(value));
                }
            }

            return builder.build();
        } else {
            FormBody.Builder builder = new FormBody.Builder();
            for (Map.Entry<String, Object> parameter : request.getParameters().entrySet()) {
                builder.add(parameter.getKey(), String.valueOf(parameter.getValue()));
            }
            return builder.build();
        }
    }

    @Autowired
    public void setClient(OkHttpClient client) {
        this.client = client;
    }

    @Autowired
    public void setGson(Gson gson) {
        this.gson = gson;
    }

}
