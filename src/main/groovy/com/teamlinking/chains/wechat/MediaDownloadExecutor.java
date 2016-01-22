package com.teamlinking.chains.wechat;

import me.chanjar.weixin.common.bean.result.WxError;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.http.InputStreamResponseHandler;
import me.chanjar.weixin.common.util.http.RequestExecutor;
import me.chanjar.weixin.common.util.http.Utf8ResponseHandler;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by admin on 16/1/22.
 */
public class MediaDownloadExecutor implements RequestExecutor<byte[], String> {

    public MediaDownloadExecutor(){
        super();
    }

    @Override
    public byte[] execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, String queryParam) throws WxErrorException, ClientProtocolException, IOException {
        if (queryParam != null) {
            if (uri.indexOf('?') == -1) {
                uri += '?';
            }
            uri += uri.endsWith("?") ? queryParam : '&' + queryParam;
        }

        HttpGet httpGet = new HttpGet(uri);
        if (httpProxy != null) {
            RequestConfig config = RequestConfig.custom().setProxy(httpProxy).build();
            httpGet.setConfig(config);
        }

        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {

            Header[] contentTypeHeader = response.getHeaders("Content-Type");
            if (contentTypeHeader != null && contentTypeHeader.length > 0) {
                // 下载媒体文件出错
                if (ContentType.TEXT_PLAIN.getMimeType().equals(contentTypeHeader[0].getValue())) {
                    String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
                    throw new WxErrorException(WxError.fromJson(responseContent));
                }
            }
            InputStream inputStream = InputStreamResponseHandler.INSTANCE.handleResponse(response);
            return IOUtils.toByteArray(inputStream);
        }

    }
}
