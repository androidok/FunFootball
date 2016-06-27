package com.qysports.funfootball.net;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.boredream.bdcodehelper.entity.AppUpdateInfo;
import com.boredream.bdcodehelper.entity.FileUploadResponse;
import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.entity.Pointer;
import com.boredream.bdcodehelper.entity.Relation;
import com.boredream.bdcodehelper.entity.RelationTo;
import com.boredream.bdcodehelper.entity.UpdatePswRequest;
import com.boredream.bdcodehelper.entity.Where;
import com.boredream.bdcodehelper.net.ObservableDecorator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.facebook.stetho.okhttp.StethoInterceptor;
import com.google.gson.Gson;
import com.qysports.funfootball.base.BaseEntity;
import com.qysports.funfootball.constants.CommonConstants;
import com.qysports.funfootball.entity.Act;
import com.qysports.funfootball.entity.ActOrder;
import com.qysports.funfootball.entity.Ad;
import com.qysports.funfootball.entity.CoachComment;
import com.qysports.funfootball.entity.Course;
import com.qysports.funfootball.entity.CourseOrder;
import com.qysports.funfootball.entity.FeedBack;
import com.qysports.funfootball.entity.Match;
import com.qysports.funfootball.entity.OfficeActivity;
import com.qysports.funfootball.entity.OfficeMatch;
import com.qysports.funfootball.entity.User;
import com.qysports.funfootball.utils.UserInfoKeeper;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Streaming;
import retrofit.http.Url;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class HttpRequest {
    // my python
//    public static final String HOST = "http://10.0.2.2:4999";
//
//    private static Retrofit retrofit;
//    private static OkHttpClient httpClient;
//
//    public static OkHttpClient getHttpClient() {
//        return httpClient;
//    }
//
//    static {
//        // OkHttpClient
//        httpClient = new OkHttpClient();
//
//        // 统一添加的Header
//        httpClient.networkInterceptors().add(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request request = chain.request().newBuilder()
//                        .addHeader("Content-Type", "application/json")
//                        .load();
//                return chain.proceed(request);
//            }
//        });
//        httpClient.networkInterceptors().add(new StethoInterceptor()); // stetho 浏览器调试工具
//
//        // log
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        httpClient.interceptors().add(interceptor);
//
//        // Retrofit
//        retrofit = new Retrofit.Builder()
//                .baseUrl(HOST)
//                .addConverterFactory(GsonConverterFactory.create()) // gson
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // rxjava 响应式编程
//                .client(httpClient)
//                .callbackExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
//                .load();
//    }
//
//    public interface BmobService {
//        // 手机验证注册
//        @POST("/1/users")
//        Observable<User> userRegist(
//                @Body User user);
//
//        // 登录用户
//        @POST("/1/login")
//        Observable<User> login(
//                @Body Map<String, Object> loginInfo);
//
//        // 获取用户详情
//        @GET("/1/users/{objectId}")
//        Observable<User> getUserById(
//                @Path("objectId") String userId);
//
//        // 查询课程
//        @GET("/1/classes/Course")
//        Observable<ListResponse<Course>> getCourse(
//                @Query("limit") int perPageCount,
//                @Query("skip") int page);
//
//        // 手机获取验证码
//        @POST("/1/requestSmsCode")
//        Observable<Object> requestSmsCode(
//                @Body Map<String, Object> mobilePhoneNumber);
//
//        // 忘记密码重置
//        @PUT("/1/resetPasswordBySmsCode")
//        Observable<BaseResponse> resetPasswordBySmsCode(
//                @Body Map<String, Object> password);
//
//        @GET("/1/classes/Activity")
//        Observable<ListResponse<ActivityEntity>> getActivities(
//                @Query("limit") int perPageCount,
//                @Query("skip") int page);
//    }
//
//    public static BmobService getApiService() {
//        BmobService service = retrofit.create(BmobService.class);
//        return service;
//    }
//
//    /**
//     * 登录用户
//     *
//     * @param username 用户名
//     * @param password 密码
//     */
//    public static Observable<User> login(String username, String password) {
//        BmobService service = getApiService();
//        Map<String, Object> loginInfo = new HashMap<>();
//        loginInfo.put("username", username);
//        loginInfo.put("password", password);
//        return service.login(loginInfo)
//                .doOnNext(new Action1<User>() {
//                    @Override
//                    public void call(User user) {
//                        // 保存登录用户数据以及token信息
//                        UserInfoKeeper.setCurrentUser(user);
//                        // 保存自动登录使用的信息
//                        UserInfoKeeper.saveLoginData(user.getObjectId(), user.getSessionToken());
//                    }
//                });
//    }
//
//    /**
//     * 获取课程列表,分页(默认每页数量为CommonConstants.COUNT_OF_PAGE)
//     *
//     * @param page 从1开始
//     */
//    public static Observable<ListResponse<Course>> getCourse(int page) {
//        BmobService service = getApiService();
//        return service.getCourse(CommonConstants.COUNT_OF_PAGE,
//                (page - 1) * CommonConstants.COUNT_OF_PAGE);
//    }

    //////////////////////////////

    // Bmob
    public static final String HOST = "https://api.bmob.cn";
    public static final String FILE_HOST = "http://file.bmob.cn/";

    public static final String APP_ID_NAME = "X-Bmob-Application-Id";
    public static final String API_KEY_NAME = "X-Bmob-REST-API-Key";
    public static final String SESSION_TOKEN_KEY = "X-Bmob-Session-Token";

    public static final String APP_ID_VALUE = "06e70a02d2950057ac4c5153460b06b2";
    public static final String API_KEY_VALUE = "9d2e47ead033bdbe516b3b7277f31f5a";
    //
//    // LeanCloud
////    public static final String HOST = "https://api.leancloud.cn";
////    public static final String FILE_HOST = "";
////
////    private static final String APP_ID_NAME = "X-LC-Id";
////    private static final String API_KEY_NAME = "X-LC-Key";
////    public static final String SESSION_TOKEN_KEY = "X-LC-Session";
////
////    private static final String APP_ID_VALUE = "9NeOaI8euYTFQUhRS4otJXfl-gzGzoHsz";
////    private static final String API_KEY_VALUE = "YqBoRRF2QsCiDmcQgCvToAPo";
//
//
    private static Retrofit retrofit;
    private static OkHttpClient httpClient;

    public static OkHttpClient getHttpClient() {
        return httpClient;
    }

    static {
        // OkHttpClient
        httpClient = new OkHttpClient();

        // 统一添加的Header
        httpClient.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader(APP_ID_NAME, APP_ID_VALUE)
                        .addHeader(API_KEY_NAME, API_KEY_VALUE)
                        .addHeader(SESSION_TOKEN_KEY, UserInfoKeeper.getToken())
                        .build();
                return chain.proceed(request);
            }
        });
        httpClient.networkInterceptors().add(new StethoInterceptor()); // stetho 浏览器调试工具

        // log
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.interceptors().add(interceptor);

        // Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create()) // gson
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // rxjava 响应式编程
                .client(httpClient)
                .callbackExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                .build();
    }

    public interface BmobService {
        // 登录用户
        @GET("/1/login")
        Observable<User> login(
                @Query("username") String username,
                @Query("password") String password);

        // 手机获取验证码
        @POST("/1/requestSmsCode")
        Observable<Object> requestSmsCode(
                @Body Map<String, Object> mobilePhoneNumber);

        // 手机验证注册
        @POST("/1/users")
        Observable<User> userRegist(
                @Body User user);

        // 忘记密码重置
        @PUT("/1/resetPasswordBySmsCode/{smsCode}")
        Observable<Object> resetPasswordBySmsCode(
                @Path("smsCode") String smsCode,
                @Body Map<String, Object> password);

        // 旧密码修改新密码
        @POST(" /1/updateUserPassword/{objectId}")
        Observable<User> updateUserPassword(
                @Path("smsCode") String smsCode,
                @Body UpdatePswRequest updatePswRequest);

        // 根据昵称搜索用户
        @GET("/1/classes/_User")
        Observable<ListResponse<User>> getUserByName(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where);

        // 动态图收藏用户列表
        @GET("/1/classes/_User")
        Observable<ListResponse<User>> getGifFavUsers(
                @Query("where") String where);

        // 获取用户详情
        @GET("/1/users/{objectId}")
        Observable<User> getCurrentUser(
                @Path("objectId") String userId);

        // 获取用户详情
        @GET("/1/users/{objectId}")
        Observable<User> getUserById(
                @Path("objectId") String userId);

        // 修改用户详情(注意, 提交什么参数修改什么参数)
        @PUT("/1/users/{objectId}")
        Observable<BaseEntity> updateUserById(
                @Path("objectId") String userId,
                @Body Map<String, Object> updateInfo);

        // 上传图片接口
        @POST("/1/files/{fileName}")
        Observable<FileUploadResponse> fileUpload(
                @Path("fileName") String fileName,
                @Body RequestBody image);

        // 提交意见反馈
        @POST("/1/classes/FeedBack")
        Observable<BaseEntity> addFeedBack(
                @Body FeedBack feedBack);

        ////////

        // 添加课程
        @POST("/1/classes/Course")
        Observable<BaseEntity> addCourse(
                @Body Course course);

        // 课程订单添加到课程中
        @PUT("/1/classes/Course/{objectId}")
        Observable<BaseEntity> addOrder2Course(
                @Path("objectId") String orderId,
                @Body Map<String, Relation> relation);

        // 收藏/取消收藏课程
        @PUT("/1/classes/Course/{objectId}")
        Observable<BaseEntity> collectCourse(
                @Path("objectId") String orderId,
                @Body Map<String, Relation> relation);

        // 用户收藏列表
        @GET("/1/classes/Course")
        Observable<ListResponse<Course>> getCollectCourse(
                @Query("where") String where);

        // 活动订单添加到活动中
        @PUT("/1/classes/Activity/{objectId}")
        Observable<BaseEntity> addOrder2Act(
                @Path("objectId") String orderId,
                @Body Map<String, Relation> relation);

        // 添加课程订单
        @POST("/1/classes/CourseOrder")
        Observable<BaseEntity> addCourseOrder(
                @Body CourseOrder order);

        // 添加活动订单
        @POST("/1/classes/ActivityOrder")
        Observable<BaseEntity> addActOrder(
                @Body ActOrder order);

        // 添加活动
        @POST("/1/classes/Activity")
        Observable<BaseEntity> addAct(
                @Body Act act);

        // 添加比赛
        @POST("/1/classes/Match")
        Observable<BaseEntity> addMatch(
                @Body Match match);

        // 查询广告
        @GET("/1/classes/Ad")
        Observable<ListResponse<Ad>> getAd(
                @Query("where") String where);

        // 课程报名用户列表
        @GET("/1/classes/CourseOrder")
        Observable<ListResponse<CourseOrder>> getOrderOfCourse(
                @Query("where") String where);

        // 活动报名用户列表
        @GET("/1/classes/ActivityOrder")
        Observable<ListResponse<ActOrder>> getOrderOfAct(
                @Query("where") String where);

        // 查询课程
        @GET("/1/classes/Course")
        Observable<ListResponse<Course>> getCourse(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where,
                @Query("include") String include);

        // 查询活动
        @GET("/1/classes/Activity")
        Observable<ListResponse<Act>> getAct(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where,
                @Query("include") String include);

        // 查询赛事
        @GET("/1/classes/Match")
        Observable<ListResponse<Match>> getMatch(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where,
                @Query("include") String include);

        // 查询官方活动
        @GET("/1/classes/OfficeActivity")
        Observable<ListResponse<OfficeActivity>> getOfficeAct(
                @Query("limit") int perPageCount,
                @Query("skip") int page);

        // 查询官方赛事
        @GET("/1/classes/OfficeMatch")
        Observable<ListResponse<OfficeMatch>> getOfficeMatch(
                @Query("limit") int perPageCount,
                @Query("skip") int page);

        // 添加教练评价
        @POST("/1/classes/CoachComment")
        Observable<BaseEntity> addCoachComment(
                @Body CoachComment comment);

        // 查询教练评价
        @GET("/1/classes/CoachComment")
        Observable<ListResponse<CoachComment>> getCoachComments(
                @Query("limit") int perPageCount,
                @Query("skip") int page,
                @Query("where") String where,
                @Query("include") String include);

        // 查询app更新信息
        @GET("/1/classes/AppUpdateInfo")
        Observable<ListResponse<AppUpdateInfo>> getAppUpdateInfo();

        @Streaming
        @GET
        Observable<ResponseBody> downloadFile(@Url String fileUrl);
    }

    public static BmobService getApiService() {
        BmobService service = retrofit.create(BmobService.class);
        return service;
    }

    /**
     * 登录用户
     *
     * @param username 用户名
     * @param password 密码
     */
    public static Observable<User> login(String username, String password) {
        BmobService service = getApiService();
        return service.login(username, password)
                .doOnNext(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        // 保存登录用户数据以及token信息
                        UserInfoKeeper.setCurrentUser(user);
                        // 保存自动登录使用的信息
                        UserInfoKeeper.saveLoginData(user.getObjectId(), user.getSessionToken());
                    }
                });
    }

    /**
     * 使用token自动登录
     *
     * @param loginData size为2的数组, 第一个为当前用户id, 第二个为当前用户token
     */
    public static Observable<User> loginByToken(final String[] loginData) {
        BmobService service = getApiService();
        // 这种自动登录方法其实是使用token去再次获取当前账号数据
        return service.getUserById(loginData[0])
                .doOnNext(new Action1<User>() {
                    @Override
                    public void call(User user) {
                        // TODO 获取用户信息接口不会返回token
                        user.setSessionToken(loginData[1]);
                        // 保存登录用户数据以及token信息
                        UserInfoKeeper.setCurrentUser(user);
                        // 保存自动登录使用的信息
                        UserInfoKeeper.saveLoginData(user.getObjectId(), user.getSessionToken());
                    }
                });
    }

    /**
     * 根据类型获取广告
     *
     * @param type
     */
    public static Observable<ListResponse<Ad>> getAdByType(int type) {
        BmobService service = getApiService();
        String where = "{\"type\":" + type + "}";
        return service.getAd(where);
    }

    /**
     * 获取课程列表,分页(默认每页数量为CommonConstants.COUNT_OF_PAGE)
     *
     * @param page 从1开始
     */
    public static Observable<ListResponse<Course>> getCourse(String city, int page) {
        return getCourseByTypeAndAge(city, page, null, null);
    }

    /**
     * 根据类型和年龄获取课程列表,分页(默认每页数量为CommonConstants.COUNT_OF_PAGE)
     *
     * @param page 从1开始
     * @param type 课程类型，不限时传入null
     * @param age 年龄，不限时传入null
     */
    public static Observable<ListResponse<Course>> getCourseByTypeAndAge(String city, int page, String type, String age) {
        BmobService service = getApiService();
        String where;
        if(!TextUtils.isEmpty(type) && TextUtils.isEmpty(age)) {
            // 只筛选类型
            where = "{\"$and\":[" +
                    "{\"city\":\"" + city + "\"}," +
                    "{\"type\":{\"$regex\":\".*" + type + ".*\"}}," +
                    "]}";
        } else if(TextUtils.isEmpty(type) && !TextUtils.isEmpty(age)) {
            // 只筛选年龄
            where = "{\"$and\":[" +
                    "{\"city\":\"" + city + "\"}," +
                    "{\"range\":{\"$regex\":\".*" + age + ".*\"}}" +
                    "]}";
        } else if(!TextUtils.isEmpty(type) && !TextUtils.isEmpty(age)) {
            // 条件都不空，合并条件筛选
            where = "{\"$and\":[" +
                    "{\"city\":\"" + city + "\"}," +
                    "{\"type\":{\"$regex\":\".*" + type + ".*\"}}," +
                    "{\"range\":{\"$regex\":\".*" + age + ".*\"}}" +
                    "]}";
        } else {
            // 条件都为空，则查询全部课程
            where = "{\"city\":\"" + city + "\"}";
        }

        return service.getCourse(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where, "coach");
    }

    /**
     * 收藏/取消收藏课程
     * @param course
     */
    public static Observable<BaseEntity> collectCourse(Course course, boolean isCollect) {
        User currentUser = UserInfoKeeper.getCurrentUser();
        Pointer pointer = new Pointer("_User", currentUser.getObjectId());
        Relation relation = new Relation();
        if(isCollect) {
            // 收藏
            relation.add(pointer);
        } else {
            // 取消收藏
            relation.remove(pointer);
        }
        Map<String, Relation> relationMap = new HashMap<>();
        relationMap.put("collectUsers", relation);

        return getApiService().collectCourse(course.getObjectId(), relationMap);
    }

    /**
     * 用户收藏列表,分页(默认每页数量为CommonConstants.COUNT_OF_PAGE)
     */
    public static Observable<ListResponse<Course>> getCollectCourse() {
        BmobService service = getApiService();

        User currentUser = UserInfoKeeper.getCurrentUser();

        Where userIdEqaulWhere = new Where();
        userIdEqaulWhere.setClassName("_User");
        HashMap<String, String> userIdMap = new HashMap<>();
        userIdMap.put("objectId", currentUser.getObjectId());
        userIdEqaulWhere.setWhere(userIdMap);

        Map<String, Where> inQuerymap = new HashMap<>();
        inQuerymap.put(Where.OP_INQUERY, userIdEqaulWhere);

        Map<String, Map<String, Where>> whereMap = new HashMap<>();
        whereMap.put("collectUsers", inQuerymap);

        String where = new Gson().toJson(whereMap);

        return service.getCollectCourse(where);
    }

    /**
     * 获取活动
     *
     * @param page        从1开始
     */
    public static Observable<ListResponse<Act>> getPrivateAct(String city, int page) {
        BmobService service = getApiService();
        String where = "{\"city\":\"" + city + "\"}";
        return service.getAct(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where, "coach");
    }

    /**
     * 获取赛事
     *
     * @param page        从1开始
     */
    public static Observable<ListResponse<Match>> getPrivateMatch(String city, int page) {
        BmobService service = getApiService();
        String where = "{\"city\":\"" + city + "\"}";
        return service.getMatch(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where, "coach");
    }

    /**
     * 获取官方活动
     *
     * @param page        从1开始
     */
    public static Observable<ListResponse<OfficeActivity>> getOfficeAct(int page) {
        BmobService service = getApiService();
        return service.getOfficeAct(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE);
    }

    /**
     * 获取官方赛事
     *
     * @param page        从1开始
     */
    public static Observable<ListResponse<OfficeMatch>> getOfficeMatch(int page) {
        BmobService service = getApiService();
        return service.getOfficeMatch(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE);
    }

    /**
     * 获取教练评价列表,分页(默认每页数量为CommonConstants.COUNT_OF_PAGE)
     *
     * @param coachUserId 教练用户id
     * @param type
     * @param page        从1开始
     */
    public static Observable<ListResponse<CoachComment>> getCoachComments(String coachUserId, int type, int page) {
        BmobService service = getApiService();
        String where;
        if(type != 0) {
            where = "{\"$and\":[" +
                    "{\"coachUserId\":\"" + coachUserId + "\"}," +
                    "{\"type\":" + type + "}" +
                    "]}";
        } else {
            where = "{\"coachUserId\":\"" + coachUserId + "\"}";
        }
        return service.getCoachComments(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where, "user");
    }

    /**
     * 根据昵称模糊搜索用户,分页(默认每页数量为CommonConstants.COUNT_OF_PAGE)
     *
     * @param searchKey 搜索昵称
     * @param page      页数,从1开始
     */
    public static Observable<ListResponse<User>> getUserByName(String searchKey, int page) {
        BmobService service = getApiService();
        String where = "{\"username\":{\"$regex\":\"" + searchKey + ".*\"}}";
        return service.getUserByName(CommonConstants.COUNT_OF_PAGE,
                (page - 1) * CommonConstants.COUNT_OF_PAGE, where);
    }

    /**
     * 课程报名
     *
     * @param order 报名订单
     */
    public static Observable<BaseEntity> signUpCourse(final CourseOrder order) {
        final BmobService service = getApiService();

        return service.addCourseOrder(order)
                .flatMap(new Func1<BaseEntity, Observable<BaseEntity>>() {
                    @Override
                    public Observable<BaseEntity> call(BaseEntity baseEntity) {
                        Relation relation = new Relation(new Pointer("CourseOrder", baseEntity.getObjectId()));

                        Map<String, Relation> relationMap = new HashMap<>();
                        relationMap.put("signUpOrders", relation);

                        return service.addOrder2Course(order.getCourse().getObjectId(), relationMap);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 活动报名
     *
     * @param order 报名订单
     */
    public static Observable<BaseEntity> signUpAct(final ActOrder order) {
        final BmobService service = getApiService();

        return service.addActOrder(order)
                .flatMap(new Func1<BaseEntity, Observable<BaseEntity>>() {
                    @Override
                    public Observable<BaseEntity> call(BaseEntity baseEntity) {
                        Relation relation = new Relation(new Pointer("ActivityOrder", baseEntity.getObjectId()));

                        Map<String, Relation> relationMap = new HashMap<>();
                        relationMap.put("signUpOrders", relation);

                        return service.addOrder2Act(order.getAct().getObjectId(), relationMap);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 课程报名用户列表
     *
     * @param course 课程
     */
    public static Observable<ListResponse<CourseOrder>> getOrderOfCourse(Course course) {
        BmobService service = getApiService();

        RelationTo relationTo = new RelationTo();
        relationTo.setKey("signUpOrders");
        Pointer pointer = new Pointer("Course", course.getObjectId());
        relationTo.setObject(pointer);

        Map<String, RelationTo> relationToMap = new HashMap<>();
        relationToMap.put(RelationTo.OP_RELATEDTO, relationTo);

        String where = new Gson().toJson(relationToMap);
        return service.getOrderOfCourse(where);
    }

    /**
     * 活动报名用户列表
     *
     * @param act 活动
     */
    public static Observable<ListResponse<ActOrder>> getOrderOfAct(Act act) {
        BmobService service = getApiService();

        RelationTo relationTo = new RelationTo();
        relationTo.setKey("signUpOrders");
        Pointer pointer = new Pointer("Activity", act.getObjectId());
        relationTo.setObject(pointer);

        Map<String, RelationTo> relationToMap = new HashMap<>();
        relationToMap.put(RelationTo.OP_RELATEDTO, relationTo);

        String where = new Gson().toJson(relationToMap);
        return service.getOrderOfAct(where);
    }

    /**
     * 上传图片
     *
     * @param call    上传成功回调
     * @param context
     * @param uri     图片uri
     * @param reqW    上传图片需要压缩的宽度
     * @param reqH    上传图片需要压缩的高度
     * @param call
     */
    public static void fileUpload(final Context context, Uri uri, int reqW, int reqH, final Subscriber<FileUploadResponse> call) {
        final BmobService service = getApiService();
        final String filename = "avatar_" + System.currentTimeMillis() + ".jpg";

        // 先从本地获取图片,利用Glide压缩图片后获取byte[]
        Glide.with(context).load(uri).asBitmap().toBytes().into(
                new SimpleTarget<byte[]>(reqW, reqH) {
                    @Override
                    public void onResourceReady(final byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                        // 上传图片
                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), resource);

                        Observable<FileUploadResponse> observable = service.fileUpload(filename, requestBody);
                        ObservableDecorator.decorate(context, observable)
                                .subscribe(call);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        call.onError(new Throwable("图片解析失败"));
                    }
                });


    }

}
