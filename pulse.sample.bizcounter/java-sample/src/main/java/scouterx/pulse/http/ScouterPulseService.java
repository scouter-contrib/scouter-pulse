package scouterx.pulse.http;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import scouterx.pulse.protocol.counter.ObjectValue;
import scouterx.pulse.protocol.register.RegisterBean;

import java.util.List;

/**
 * @author Gun Lee (gunlee01@gmail.com) on 2016. 7. 31.
 */
public interface ScouterPulseService {
    @POST("/register")
    @Headers({"Accept: application/json", "Content-Yype: application/json"})
    Response register(@Body RegisterBean bean);

    @POST("/counter")
    @Headers({"Accept: application/json", "Content-Yype: application/json"})
    Response counter(@Body ObjectValue[] objs);

    @POST("/counter")
    @Headers({"Accept: application/json", "Content-Yype: application/json"})
    Response counter(@Body List<ObjectValue> objs);
}
