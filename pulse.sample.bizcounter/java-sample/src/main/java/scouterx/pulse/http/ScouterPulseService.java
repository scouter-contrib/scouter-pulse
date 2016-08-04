package scouterx.pulse.http;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import scouterx.pulse.protocol.counter.ObjectCounterBean;
import scouterx.pulse.protocol.register.RegisterBean;

import java.util.List;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2016. 7. 31.
 */
public interface ScouterPulseService {
    @POST("/register")
    @Headers({"accept: application/json", "content-type: application/json"})
    Call<Void> register(@Body RegisterBean bean);

    @POST("/counter")
    @Headers({"accept: application/json", "content-type: application/json"})
    Call<Void> counter(@Body ObjectCounterBean[] objs);

    @POST("/counter")
    @Headers({"accept: application/json", "content-type: application/json"})
    Call<Void> counter(@Body List<ObjectCounterBean> objs);
}
