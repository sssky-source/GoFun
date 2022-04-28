# GoFun
GoFun源码



**Retrofit2**

**Service接口**

```java
    @GET("Good/getGoodsByType?page=1&size=6&type=1")
    Call<List<Good>> getGoodData();

    @POST("User/Login")
    Call<ResponseBody> createData(@Body UserSign userSign);
	//UserSign为数据类
```



**MainActivity**

```java
        Service service = HttpRequest.create(GoodService.class);
		UserSign userSign =new UserSign();
        userSign.setEmail("510992113@qq.com");
        userSign.setPassword("510992113");
        goodService.createData(userSign).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("111","code:" + response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
```

**打印返回response.code = 200为正确**
