package com.harvestasm.apm.reporter;

import com.harvestasm.apm.repository.model.ApmDeviceMicsItem;
import com.harvestasm.apm.repository.model.ApmSourceConnect;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmBaseSearchResponse.ApmBaseUnit;
import com.harvestasm.apm.repository.model.search.ApmCommonSearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yangfeng on 2018/3/20.
 */

public class SearchDataParser {
    private static final String INDEX = "mobile";
    private static final String TYPE_DATA = "data";
    private static final String TYPE_CONNECT = "connect";
    private static final String TAG = SearchDataParser.class.getSimpleName();

    public static SearchResult parse(ApmCommonSearchResponse apmData) {
        Map<String, List<String>> unknownDataMap = new HashMap<>();

        Map<String, List<ApmBaseUnit>> idAllIndexMap = new HashMap<>();
        Map<String, List<ApmBaseUnit>> idConnectIndexMap = new HashMap<>();
        Map<String, List<ApmBaseUnit>> idDataIndexMap = new HashMap<>();

        Map<String, List<ApmBaseUnit>> deviceIdAllIndexMap = new HashMap<>();
        Map<String, List<ApmBaseUnit>> deviceIdConnectIndexMap = new HashMap<>();
        Map<String, List<ApmBaseUnit>> deviceIdDataIndexMap = new HashMap<>();
        Map<String, List<ApmBaseUnit>> timestampAllIndexMap = new HashMap<>();
        Map<String, List<ApmBaseUnit>> timestampConnectIndexMap = new HashMap<>();
        Map<String, List<ApmBaseUnit>> timestampDataIndexMap = new HashMap<>();

        SearchResult searchResult = new SearchResult();
        for (ApmBaseUnit unit : apmData.getHits().getHits()) {
            String index = unit.get_index();
            String type = unit.get_type();

            String id = unit.get_id();
            int score = unit.get_score();

            ApmBaseSearchResponse.ApmBaseSourceType sourceTypeData = unit.get_source();
            addToMap(deviceIdAllIndexMap, unit, sourceTypeData.getDeviceId());
            addToMap(timestampAllIndexMap, unit, sourceTypeData.getTimestamp());

            addToMap(idAllIndexMap, unit, id);

            if (isEquals(index, INDEX)) {
                if (isEquals(type, TYPE_CONNECT)) {
                    addToMap(idConnectIndexMap, unit, id);
                    addToMap(deviceIdConnectIndexMap, unit, sourceTypeData.getDeviceId());
                    addToMap(timestampConnectIndexMap, unit, sourceTypeData.getTimestamp());
                } else if (isEquals(type, TYPE_DATA)) {
                    addToMap(idDataIndexMap, unit, id);
                    addToMap(deviceIdDataIndexMap, unit, sourceTypeData.getDeviceId());
                    addToMap(timestampDataIndexMap, unit, sourceTypeData.getTimestamp());
                } else {
                    handleUnknownType(unknownDataMap, index, type);
                }
            } else {
                handleUnknownType(unknownDataMap, index, type);
            }
        }

        return searchResult;
    }

    /*
              "deviceID": "e7a11a14-7409-4141-9f58-79230a0beb74",
          "@timestamp": "2017-12-11T18:27:59.577Z",
          "tmDelta": 0,
          "transaction": [
            {
              "url": "https://graph.facebook.com/v2.9/275932369410927",
              "httpMethod": "GET",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 0.4830000102519989,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 0,
              "bytesReceived": 1075,
              "appData": null,
              "timestamp": 1512988019915
            },
            {
              "url": "https://graph.facebook.com/v2.9/275932369410927/activities",
              "httpMethod": "POST",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 0.2849999964237213,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 1513,
              "bytesReceived": 0,
              "appData": null,
              "timestamp": 1512988020555
            },
            {
              "url": "https://graph.facebook.com/v2.9/275932369410927/activities",
              "httpMethod": "POST",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 0.3019999861717224,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 1814,
              "bytesReceived": 0,
              "appData": null,
              "timestamp": 1512988020866
            },
            {
              "url": "https://t.appsflyer.com/api/v4/androidevent",
              "httpMethod": "POST",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 1.1720000505447388,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 967,
              "bytesReceived": 0,
              "appData": null,
              "timestamp": 1512988021605
            },
            {
              "url": "http://get.pinyin.sogou.com/q",
              "httpMethod": "POST",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 0.19099999964237213,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 1979,
              "bytesReceived": 0,
              "appData": null,
              "timestamp": 1512988023052
            },
            {
              "url": "http://get.pinyin.sogou.com/q",
              "httpMethod": "POST",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 0.20399999618530273,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 1827,
              "bytesReceived": 0,
              "appData": null,
              "timestamp": 1512988023307
            },
            {
              "url": "http://127.0.0.1:50615/ping",
              "httpMethod": "GET",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 0.08100000023841858,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 0,
              "bytesReceived": 0,
              "appData": null,
              "timestamp": 1512988026130
            },
            {
              "url": "http://www.typany.com/api/getlatestapk",
              "httpMethod": "GET",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 1.0049999952316284,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 0,
              "bytesReceived": 365,
              "appData": null,
              "timestamp": 1512988026640
            },
            {
              "url": "http://www.typany.com/api/latestTopPickTime",
              "httpMethod": "GET",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 1.0010000467300415,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 0,
              "bytesReceived": 13,
              "appData": null,
              "timestamp": 1512988026647
            },
            {
              "url": "http://net.rayjump.com/openapi/ad/v3",
              "httpMethod": "GET",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 0.597000002861023,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 0,
              "bytesReceived": 5949,
              "appData": null,
              "timestamp": 1512988026986
            },
            {
              "url": "https://stats.appsflyer.com/stats",
              "httpMethod": "POST",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 1.2029999494552612,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 303,
              "bytesReceived": 0,
              "appData": null,
              "timestamp": 1512988027090
            },
            {
              "url": "http://www.typany.com/api/adsstrategy",
              "httpMethod": "GET",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 0.26600000262260437,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 0,
              "bytesReceived": 3568,
              "appData": null,
              "timestamp": 1512988027174
            },
            {
              "url": "https://graph.facebook.com/network_ads_common",
              "httpMethod": "POST",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 0.27000001072883606,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 777,
              "bytesReceived": 0,
              "appData": null,
              "timestamp": 1512988027365
            },
            {
              "url": "https://t.appsflyer.com/api/v4/androidevent",
              "httpMethod": "POST",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 1.1770000457763672,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 967,
              "bytesReceived": 0,
              "appData": null,
              "timestamp": 1512988027535
            },
            {
              "url": "http://www.typany.com/api/getallfeaturetheme",
              "httpMethod": "GET",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 0.8690000176429749,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 0,
              "bytesReceived": 37311,
              "appData": null,
              "timestamp": 1512988034238
            },
            {
              "url": "https://graph.facebook.com/network_ads_common",
              "httpMethod": "POST",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 0.296999990940094,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 777,
              "bytesReceived": 0,
              "appData": null,
              "timestamp": 1512988043001
            },
            {
              "url": "http://d2ezgnxmilyqe4.cloudfront.net/media/Theme/LGBT.webp",
              "httpMethod": "GET",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 0.531000018119812,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 0,
              "bytesReceived": 14674,
              "appData": null,
              "timestamp": 1512988043259
            },
            {
              "url": "http://d2ezgnxmilyqe4.cloudfront.net/media/Theme/Blue_Cat_Face.webp",
              "httpMethod": "GET",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 0.3499999940395355,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 0,
              "bytesReceived": 18078,
              "appData": null,
              "timestamp": 1512988043262
            },
            {
              "url": "http://d2ezgnxmilyqe4.cloudfront.net/media/Theme/1001002066.webp",
              "httpMethod": "GET",
              "carrier": "wifi",
              "wanType": "wifi",
              "totalTime": 0.6420000195503235,
              "statusCode": 200,
              "errorCode": 0,
              "bytesSent": 0,
              "bytesReceived": 13892,
              "appData": null,
              "timestamp": 1512988043263
            }
          ],
          "measurement": [
            {
              "count": 1,
              "total": 0.0010001659393310547,
              "min": 0.0010001659393310547,
              "max": 0.0010001659393310547,
              "sum_of_squares": 0.000001000331906197971,
              "exclusive": 0.001,
              "name": "Method/StickerFragment/onCreate",
              "scope": "Mobile/Activity/Name/Display StickerFragment"
            },
            {
              "count": 1,
              "total": 0.31599998474121094,
              "min": 0.31599998474121094,
              "max": 0.31599998474121094,
              "sum_of_squares": 0.09985599035644555,
              "exclusive": 0,
              "name": "Mobile/Activity/Background/Name/Display SetupWizardActivity",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.0559999942779541,
              "min": 0.0559999942779541,
              "max": 0.0559999942779541,
              "sum_of_squares": 0.003135999359130892,
              "exclusive": 0,
              "name": "Mobile/Activity/Name/Display EmojiMakerFragment",
              "scope": ""
            },
            {
              "count": 19,
              "total": 0.0009999275207519531,
              "min": 0,
              "max": 0.0009999275207519531,
              "sum_of_squares": 9.998550467571476e-7,
              "exclusive": 0.001,
              "name": "Method/JSONObject/toString",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.013000011444091797,
              "min": 0.013000011444091797,
              "max": 0.013000011444091797,
              "sum_of_squares": 0.00016900029754651769,
              "exclusive": 0.013,
              "name": "Method/SlashAdsActivity/onCreate",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.003000020980834961,
              "min": 0.003000020980834961,
              "max": 0.003000020980834961,
              "sum_of_squares": 0.000009000125885449961,
              "exclusive": 0.003,
              "name": "Method/ThemeFragment/onCreateView",
              "scope": ""
            },
            {
              "count": 4,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/JSONArray/<init>",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.006000041961669922,
              "min": 0.006000041961669922,
              "max": 0.006000041961669922,
              "sum_of_squares": 0.000036000503541799844,
              "exclusive": 0.006,
              "name": "Method/EditorFragment/onCreateView",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.0010001659393310547,
              "min": 0.0010001659393310547,
              "max": 0.0010001659393310547,
              "sum_of_squares": 0.000001000331906197971,
              "exclusive": 0.001,
              "name": "Method/StickerFragment/onCreate",
              "scope": ""
            },
            {
              "count": 1,
              "total": 1,
              "min": 1,
              "max": 1,
              "sum_of_squares": 1,
              "name": "Mobile/Activity/Network/Display SetupWizardActivity/Count",
              "scope": ""
            },
            {
              "count": 1,
              "total": 1.4730000495910645,
              "min": 1.4730000495910645,
              "max": 1.4730000495910645,
              "sum_of_squares": 1.4641851179027583,
              "name": "Mobile/Activity/Network/Display SlashAdsActivity/Time",
              "scope": ""
            },
            {
              "count": 5,
              "total": 0.021000146865844727,
              "min": 0,
              "max": 0.016000032424926758,
              "sum_of_squares": 0.0002670013504371127,
              "exclusive": 0.021,
              "name": "Method/LoadingFragment/onCreateView",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.08100000023841858,
              "min": 0.08100000023841858,
              "max": 0.08100000023841858,
              "sum_of_squares": 0.00656100003862381,
              "name": "Mobile/Activity/Network/Display StickerFragment/Time",
              "scope": ""
            },
            {
              "count": 1,
              "name": "Supportability/AgentHealth/UncaughtExceptionHandler/com.android.internal.os.RuntimeInit$UncaughtHandler",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.004999876022338867,
              "min": 0.004999876022338867,
              "max": 0.004999876022338867,
              "sum_of_squares": 0.000024998760238759132,
              "exclusive": 0.005,
              "name": "Method/VolumeEditorFragment/onCreateView",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.12700000405311584,
              "min": 0.12700000405311584,
              "max": 0.12700000405311584,
              "sum_of_squares": 0.01612900102949144,
              "name": "Supportability/AgentHealth/Collector/Harvest",
              "scope": ""
            },
            {
              "count": 6,
              "total": 2.371146411037977,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 2.371146411037977,
              "name": "Mobile/Summary/View Loading",
              "scope": ""
            },
            {
              "count": 1,
              "total": 3.208999991416931,
              "min": 3.208999991416931,
              "max": 3.208999991416931,
              "sum_of_squares": 3.4592349623584795,
              "name": "Mobile/Activity/Network/Display NewSettingActivity/Time",
              "scope": ""
            },
            {
              "count": 37,
              "total": 0.014999866485595703,
              "min": 0,
              "max": 0.0010001659393310547,
              "sum_of_squares": 0.000014999733139120508,
              "exclusive": 0.015000000000000006,
              "name": "Method/SQLiteDatabase/delete",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.08700013160705566,
              "min": 0.08700013160705566,
              "max": 0.08700013160705566,
              "sum_of_squares": 0.007569022899645006,
              "exclusive": 0.084,
              "name": "Method/ThemeFragment/onCreate",
              "scope": ""
            },
            {
              "count": 1,
              "total": 1,
              "min": 1,
              "max": 1,
              "sum_of_squares": 1,
              "name": "Mobile/Activity/Network/Display StickerFragment/Count",
              "scope": ""
            },
            {
              "count": 1,
              "total": 3,
              "min": 3,
              "max": 3,
              "sum_of_squares": 3,
              "name": "Mobile/Activity/Network/Display NewSettingActivity/Count",
              "scope": ""
            },
            {
              "count": 70,
              "total": 0.01399993896484375,
              "min": 0,
              "max": 0.002000093460083008,
              "sum_of_squares": 0.00001600006498847506,
              "exclusive": 0.014000000000000005,
              "name": "Method/SQLiteDatabase/query",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.08800005912780762,
              "min": 0.08800005912780762,
              "max": 0.08800005912780762,
              "sum_of_squares": 0.007744010406497637,
              "exclusive": 0,
              "name": "Mobile/Activity/Name/Display ThemeFragment",
              "scope": ""
            },
            {
              "count": 1,
              "total": 2,
              "min": 2,
              "max": 2,
              "sum_of_squares": 2,
              "name": "Mobile/Activity/Network/Display SlashAdsActivity/Count",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.5520000457763672,
              "min": 0.5520000457763672,
              "max": 0.5520000457763672,
              "sum_of_squares": 0.30470405053711147,
              "exclusive": 0,
              "name": "Mobile/Activity/Name/Display NewSettingActivity",
              "scope": ""
            },
            {
              "count": 3,
              "total": 1.25401568627451,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 1.25401568627451,
              "name": "Mobile/Summary/Network",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.08800005912780762,
              "min": 0.08800005912780762,
              "max": 0.08800005912780762,
              "sum_of_squares": 0.007744010406497637,
              "exclusive": 0,
              "name": "Mobile/Activity/Background/Name/Display ThemeFragment",
              "scope": ""
            },
            {
              "count": 10,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/JSONArray/toString",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.31599998474121094,
              "min": 0.31599998474121094,
              "max": 0.31599998474121094,
              "sum_of_squares": 0.09985599035644555,
              "exclusive": 0,
              "name": "Mobile/Activity/Name/Display SetupWizardActivity",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.2349998950958252,
              "min": 0.2349998950958252,
              "max": 0.2349998950958252,
              "sum_of_squares": 0.05522495069504885,
              "exclusive": 0.235,
              "name": "Method/SetupWizardActivity/onCreate",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.006999969482421875,
              "min": 0.006999969482421875,
              "max": 0.006999969482421875,
              "sum_of_squares": 0.00004899957275483757,
              "exclusive": 0,
              "name": "Mobile/Activity/Background/Name/Display StickerFragment",
              "scope": ""
            },
            {
              "count": 2,
              "total": 0.5710000991821289,
              "min": 0.27300000190734863,
              "max": 0.2980000972747803,
              "sum_of_squares": 0.16333305901719086,
              "exclusive": 0.005,
              "name": "Method/h/doInBackground",
              "scope": ""
            },
            {
              "count": 2,
              "total": 0.006000041961669922,
              "min": 0.0019998550415039062,
              "max": 0.004000186920166016,
              "sum_of_squares": 0.000020000915583295864,
              "exclusive": 0.006,
              "name": "Method/SQLiteDatabase/insert",
              "scope": ""
            },
            {
              "count": 2,
              "total": 0.004246220249105931,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0.004246220249105931,
              "name": "Mobile/Summary/Images",
              "scope": ""
            },
            {
              "count": 1,
              "total": 1.2739999294281006,
              "min": 1.2739999294281006,
              "max": 1.2739999294281006,
              "sum_of_squares": 1.6230758201828053,
              "exclusive": 0,
              "name": "Mobile/Activity/Name/Display SlashAdsActivity",
              "scope": ""
            },
            {
              "count": 38,
              "total": 0.001427450980392157,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0.001427450980392157,
              "name": "Mobile/Summary/JSON",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/BackgroundHttpTask/onPostExecute",
              "scope": ""
            },
            {
              "count": 72,
              "total": 0.7680008411407471,
              "min": 0,
              "max": 0.06999993324279785,
              "sum_of_squares": 0.02404800734774426,
              "exclusive": 0.7680000000000003,
              "name": "Method/BitmapFactory/decodeStream",
              "scope": ""
            },
            {
              "count": 18,
              "total": 0.007999897003173828,
              "min": 0,
              "max": 0.0010001659393310547,
              "sum_of_squares": 0.000007999794092938828,
              "exclusive": 0.008,
              "name": "Method/SQLiteDatabase/insertOrThrow",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.12700000405311584,
              "min": 0.12700000405311584,
              "max": 0.12700000405311584,
              "sum_of_squares": 0.01612900102949144,
              "name": "Supportability/AgentHealth/Collector/Connect",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.0009999275207519531,
              "min": 0.0009999275207519531,
              "max": 0.0009999275207519531,
              "sum_of_squares": 9.998550467571476e-7,
              "exclusive": 0.001,
              "name": "Method/BitmapFactory/decodeFile",
              "scope": ""
            },
            {
              "count": 2,
              "total": 0.009999990463256836,
              "min": 0.003000020980834961,
              "max": 0.006999969482421875,
              "sum_of_squares": 0.000057999698640287534,
              "exclusive": 0.009000000000000001,
              "name": "Method/h/onPostExecute",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.0559999942779541,
              "min": 0.0559999942779541,
              "max": 0.0559999942779541,
              "sum_of_squares": 0.003135999359130892,
              "exclusive": 0,
              "name": "Mobile/Activity/Background/Name/Display EmojiMakerFragment",
              "scope": ""
            },
            {
              "count": 125,
              "total": 0.04216423145801517,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0.04216423145801517,
              "name": "Mobile/Summary/Database",
              "scope": ""
            },
            {
              "count": 1,
              "total": 1.2039999961853027,
              "min": 1.2039999961853027,
              "max": 1.2039999961853027,
              "sum_of_squares": 1.449615990814209,
              "exclusive": 0.001,
              "name": "Method/BackgroundHttpTask/doInBackground",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/SoundFragment/onCreate",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.003999948501586914,
              "min": 0.003999948501586914,
              "max": 0.003999948501586914,
              "sum_of_squares": 0.0000159995880153474,
              "exclusive": 0.004,
              "name": "Method/StickerFragment/onCreateView",
              "scope": ""
            },
            {
              "count": 1,
              "total": 246.09375,
              "min": 246.09375,
              "max": 246.09375,
              "sum_of_squares": 60562.1337890625,
              "name": "Memory/Used",
              "scope": ""
            },
            {
              "count": 1,
              "total": 1.2739999294281006,
              "min": 1.2739999294281006,
              "max": 1.2739999294281006,
              "sum_of_squares": 1.6230758201828053,
              "exclusive": 0,
              "name": "Mobile/Activity/Background/Name/Display SlashAdsActivity",
              "scope": ""
            },
            {
              "count": 73,
              "total": 0.01600027084350586,
              "min": 0,
              "max": 0.0010001659393310547,
              "sum_of_squares": 0.000016000541904759302,
              "exclusive": 0.016000000000000007,
              "name": "Method/SQLiteDatabase/rawQuery",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.2849999964237213,
              "min": 0.2849999964237213,
              "max": 0.2849999964237213,
              "sum_of_squares": 0.08122499796152116,
              "name": "Mobile/Activity/Network/Display SetupWizardActivity/Time",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/EmojiMakerFragment/onCreate",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.0009999275207519531,
              "min": 0.0009999275207519531,
              "max": 0.0009999275207519531,
              "sum_of_squares": 9.998550467571476e-7,
              "exclusive": 0.001,
              "name": "Method/SoundFragment/onCreateView",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.006999969482421875,
              "min": 0.006999969482421875,
              "max": 0.006999969482421875,
              "sum_of_squares": 0.00004899957275483757,
              "exclusive": 0,
              "name": "Mobile/Activity/Name/Display StickerFragment",
              "scope": ""
            },
            {
              "count": 27,
              "total": 0.2349991798400879,
              "min": 0.0009999275207519531,
              "max": 0.01399993896484375,
              "sum_of_squares": 0.002514983520882197,
              "exclusive": 0.2350000000000001,
              "name": "Method/BitmapFactory/decodeResource",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/SecondaryCodeMgr$BuildCode/doInBackground",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.5520000457763672,
              "min": 0.5520000457763672,
              "max": 0.5520000457763672,
              "sum_of_squares": 0.30470405053711147,
              "exclusive": 0,
              "name": "Mobile/Activity/Background/Name/Display NewSettingActivity",
              "scope": ""
            },
            {
              "count": 3,
              "total": 0.03399991989135742,
              "min": 0.003999948501586914,
              "max": 0.026000022888183594,
              "sum_of_squares": 0.0007080003662167655,
              "exclusive": 0.034,
              "name": "Method/MessageFragment/onCreateView",
              "scope": ""
            },
            {
              "count": 20,
              "total": 0.020999908447265625,
              "min": 0,
              "max": 0.012000083923339844,
              "sum_of_squares": 0.00016100145728614734,
              "exclusive": 0.021,
              "name": "Method/JSONObject/<init>",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.46899986267089844,
              "min": 0.46899986267089844,
              "max": 0.46899986267089844,
              "sum_of_squares": 0.2199608711853216,
              "exclusive": 0.468,
              "name": "Method/NewSettingActivity/onCreate",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.009999990463256836,
              "min": 0.009999990463256836,
              "max": 0.009999990463256836,
              "sum_of_squares": 0.00009999980926522767,
              "exclusive": 0.01,
              "name": "Method/EmojiMakerFragment/onCreateView",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/SecondaryCodeMgr$BuildCode/onPostExecute",
              "scope": ""
            },
            {
              "count": 1,
              "total": 0.004999876022338867,
              "min": 0.004999876022338867,
              "max": 0.004999876022338867,
              "sum_of_squares": 0.000024998760238759132,
              "exclusive": 0.005,
              "name": "Method/VolumeEditorFragment/onCreateView",
              "scope": "Mobile/Activity/Name/Display NewSettingActivity"
            },
            {
              "count": 1,
              "total": 0.006000041961669922,
              "min": 0.006000041961669922,
              "max": 0.006000041961669922,
              "sum_of_squares": 0.000036000503541799844,
              "exclusive": 0.006,
              "name": "Method/EditorFragment/onCreateView",
              "scope": "Mobile/Activity/Name/Display NewSettingActivity"
            },
            {
              "count": 1,
              "total": 0.0009999275207519531,
              "min": 0.0009999275207519531,
              "max": 0.0009999275207519531,
              "sum_of_squares": 9.998550467571476e-7,
              "exclusive": 0.001,
              "name": "Method/BitmapFactory/decodeResource",
              "scope": "Mobile/Activity/Name/Display NewSettingActivity"
            },
            {
              "count": 1,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/JSONObject/toString",
              "scope": "Mobile/Activity/Name/Display NewSettingActivity"
            },
            {
              "count": 1,
              "total": 0.46899986267089844,
              "min": 0.46899986267089844,
              "max": 0.46899986267089844,
              "sum_of_squares": 0.2199608711853216,
              "exclusive": 0.468,
              "name": "Method/NewSettingActivity/onCreate",
              "scope": "Mobile/Activity/Name/Display NewSettingActivity"
            },
            {
              "count": 1,
              "total": 0.016000032424926758,
              "min": 0.016000032424926758,
              "max": 0.016000032424926758,
              "sum_of_squares": 0.0002560010375987076,
              "exclusive": 0.016,
              "name": "Method/LoadingFragment/onCreateView",
              "scope": "Mobile/Activity/Name/Display NewSettingActivity"
            },
            {
              "count": 1,
              "total": 0.0009999275207519531,
              "min": 0.0009999275207519531,
              "max": 0.0009999275207519531,
              "sum_of_squares": 9.998550467571476e-7,
              "exclusive": 0.001,
              "name": "Method/SQLiteDatabase/insertOrThrow",
              "scope": "Mobile/Activity/Background/Name/Display SetupWizardActivity"
            },
            {
              "count": 4,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/JSONObject/<init>",
              "scope": "Mobile/Activity/Background/Name/Display SetupWizardActivity"
            },
            {
              "count": 5,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/JSONArray/toString",
              "scope": "Mobile/Activity/Background/Name/Display SetupWizardActivity"
            },
            {
              "count": 5,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/JSONObject/toString",
              "scope": "Mobile/Activity/Background/Name/Display SetupWizardActivity"
            },
            {
              "count": 1,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/SQLiteDatabase/query",
              "scope": "Mobile/Activity/Background/Name/Display SetupWizardActivity"
            },
            {
              "count": 2,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/SQLiteDatabase/rawQuery",
              "scope": "Mobile/Activity/Background/Name/Display SetupWizardActivity"
            },
            {
              "count": 1,
              "total": 0.0009999275207519531,
              "min": 0.0009999275207519531,
              "max": 0.0009999275207519531,
              "sum_of_squares": 9.998550467571476e-7,
              "exclusive": 0.001,
              "name": "Method/SQLiteDatabase/insertOrThrow",
              "scope": "Mobile/Activity/Background/Name/Display SlashAdsActivity"
            },
            {
              "count": 12,
              "total": 0.003999948501586914,
              "min": 0,
              "max": 0.0010001659393310547,
              "sum_of_squares": 0.000003999897046469414,
              "exclusive": 0.004,
              "name": "Method/SQLiteDatabase/delete",
              "scope": "Mobile/Activity/Background/Name/Display SlashAdsActivity"
            },
            {
              "count": 2,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/JSONArray/toString",
              "scope": "Mobile/Activity/Background/Name/Display SlashAdsActivity"
            },
            {
              "count": 6,
              "total": 0.0009999275207519531,
              "min": 0,
              "max": 0.0009999275207519531,
              "sum_of_squares": 9.998550467571476e-7,
              "exclusive": 0.001,
              "name": "Method/JSONObject/toString",
              "scope": "Mobile/Activity/Background/Name/Display SlashAdsActivity"
            },
            {
              "count": 7,
              "total": 0.0009999275207519531,
              "min": 0,
              "max": 0.0009999275207519531,
              "sum_of_squares": 9.998550467571476e-7,
              "exclusive": 0.001,
              "name": "Method/JSONObject/<init>",
              "scope": "Mobile/Activity/Background/Name/Display SlashAdsActivity"
            },
            {
              "count": 17,
              "total": 0.003999948501586914,
              "min": 0,
              "max": 0.002000093460083008,
              "sum_of_squares": 0.000006000083942581114,
              "exclusive": 0.004,
              "name": "Method/SQLiteDatabase/query",
              "scope": "Mobile/Activity/Background/Name/Display SlashAdsActivity"
            },
            {
              "count": 1,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/SecondaryCodeMgr$BuildCode/doInBackground",
              "scope": "Mobile/Activity/Background/Name/Display SlashAdsActivity"
            },
            {
              "count": 10,
              "total": 0.0030002593994140625,
              "min": 0,
              "max": 0.0010001659393310547,
              "sum_of_squares": 0.0000030005188591530896,
              "exclusive": 0.003,
              "name": "Method/SQLiteDatabase/rawQuery",
              "scope": "Mobile/Activity/Background/Name/Display SlashAdsActivity"
            },
            {
              "count": 14,
              "total": 0.0030344827586206895,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0.0030344827586206895,
              "name": "Mobile/Summary/Database",
              "scope": "Mobile/Activity/Summary/Name/Display ThemeFragment"
            },
            {
              "count": 2,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Mobile/Summary/JSON",
              "scope": "Mobile/Activity/Summary/Name/Display ThemeFragment"
            },
            {
              "count": 1,
              "total": 0.0849655172413793,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0.0849655172413793,
              "name": "Mobile/Summary/View Loading",
              "scope": "Mobile/Activity/Summary/Name/Display ThemeFragment"
            },
            {
              "count": 41,
              "total": 0.008564705882352945,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0.008564705882352945,
              "name": "Mobile/Summary/Database",
              "scope": "Mobile/Activity/Summary/Name/Display SlashAdsActivity"
            },
            {
              "count": 1,
              "total": 0.0007137254901960785,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0.0007137254901960785,
              "name": "Mobile/Summary/Images",
              "scope": "Mobile/Activity/Summary/Name/Display SlashAdsActivity"
            },
            {
              "count": 16,
              "total": 0.001427450980392157,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0.001427450980392157,
              "name": "Mobile/Summary/JSON",
              "scope": "Mobile/Activity/Summary/Name/Display SlashAdsActivity"
            },
            {
              "count": 3,
              "total": 1.25401568627451,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 1.25401568627451,
              "name": "Mobile/Summary/Network",
              "scope": "Mobile/Activity/Summary/Name/Display SlashAdsActivity"
            },
            {
              "count": 1,
              "total": 0.009278431372549021,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0.009278431372549021,
              "name": "Mobile/Summary/View Loading",
              "scope": "Mobile/Activity/Summary/Name/Display SlashAdsActivity"
            },
            {
              "count": 8,
              "total": 0.003999948501586914,
              "min": 0,
              "max": 0.0010001659393310547,
              "sum_of_squares": 0.000003999897046469414,
              "exclusive": 0.004,
              "name": "Method/SQLiteDatabase/insertOrThrow",
              "scope": "Mobile/Activity/Background/Name/Display SoundFragment"
            },
            {
              "count": 8,
              "total": 0.0060002803802490234,
              "min": 0,
              "max": 0.0010001659393310547,
              "sum_of_squares": 0.000006000560858865356,
              "exclusive": 0.006,
              "name": "Method/SQLiteDatabase/delete",
              "scope": "Mobile/Activity/Background/Name/Display SoundFragment"
            },
            {
              "count": 1,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/JSONObject/toString",
              "scope": "Mobile/Activity/Background/Name/Display SoundFragment"
            },
            {
              "count": 1,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/JSONArray/toString",
              "scope": "Mobile/Activity/Background/Name/Display SoundFragment"
            },
            {
              "count": 4,
              "total": 0.017999887466430664,
              "min": 0.0009999275207519531,
              "max": 0.012000083923339844,
              "sum_of_squares": 0.00015800141528643508,
              "exclusive": 0.018000000000000002,
              "name": "Method/JSONObject/<init>",
              "scope": "Mobile/Activity/Background/Name/Display SoundFragment"
            },
            {
              "count": 72,
              "total": 0.7680008411407471,
              "min": 0,
              "max": 0.06999993324279785,
              "sum_of_squares": 0.02404800734774426,
              "exclusive": 0.7680000000000003,
              "name": "Method/BitmapFactory/decodeStream",
              "scope": "Mobile/Activity/Background/Name/Display SoundFragment"
            },
            {
              "count": 2,
              "total": 0.5710000991821289,
              "min": 0.27300000190734863,
              "max": 0.2980000972747803,
              "sum_of_squares": 0.16333305901719086,
              "exclusive": 0.005,
              "name": "Method/h/doInBackground",
              "scope": "Mobile/Activity/Background/Name/Display SoundFragment"
            },
            {
              "count": 26,
              "total": 0.0069997310638427734,
              "min": 0,
              "max": 0.0010001659393310547,
              "sum_of_squares": 0.000006999462186740857,
              "exclusive": 0.007,
              "name": "Method/SQLiteDatabase/query",
              "scope": "Mobile/Activity/Background/Name/Display SoundFragment"
            },
            {
              "count": 24,
              "total": 0.006999969482421875,
              "min": 0,
              "max": 0.0010001659393310547,
              "sum_of_squares": 0.00000699993904618168,
              "exclusive": 0.007,
              "name": "Method/SQLiteDatabase/rawQuery",
              "scope": "Mobile/Activity/Background/Name/Display SoundFragment"
            },
            {
              "count": 1,
              "total": 0.082,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0.082,
              "name": "Mobile/Summary/View Loading",
              "scope": "Mobile/Activity/Summary/Name/Display StickerFragment"
            },
            {
              "count": 1,
              "total": 1.2039999961853027,
              "min": 1.2039999961853027,
              "max": 1.2039999961853027,
              "sum_of_squares": 1.449615990814209,
              "exclusive": 0.001,
              "name": "Method/BackgroundHttpTask/doInBackground",
              "scope": "Mobile/Activity/Background/Name/Display NewSettingActivity"
            },
            {
              "count": 8,
              "total": 0.002000093460083008,
              "min": 0,
              "max": 0.0010001659393310547,
              "sum_of_squares": 0.0000020001869529551186,
              "exclusive": 0.002,
              "name": "Method/SQLiteDatabase/insertOrThrow",
              "scope": "Mobile/Activity/Background/Name/Display NewSettingActivity"
            },
            {
              "count": 8,
              "total": 0.0009999275207519531,
              "min": 0,
              "max": 0.0009999275207519531,
              "sum_of_squares": 9.998550467571476e-7,
              "exclusive": 0.001,
              "name": "Method/SQLiteDatabase/delete",
              "scope": "Mobile/Activity/Background/Name/Display NewSettingActivity"
            },
            {
              "count": 3,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/JSONObject/toString",
              "scope": "Mobile/Activity/Background/Name/Display NewSettingActivity"
            },
            {
              "count": 26,
              "total": 0.0030002593994140625,
              "min": 0,
              "max": 0.0010001659393310547,
              "sum_of_squares": 0.0000030005188591530896,
              "exclusive": 0.003,
              "name": "Method/SQLiteDatabase/query",
              "scope": "Mobile/Activity/Background/Name/Display NewSettingActivity"
            },
            {
              "count": 24,
              "total": 0.002000093460083008,
              "min": 0,
              "max": 0.0010001659393310547,
              "sum_of_squares": 0.0000020001869529551186,
              "exclusive": 0.002,
              "name": "Method/SQLiteDatabase/rawQuery",
              "scope": "Mobile/Activity/Background/Name/Display NewSettingActivity"
            },
            {
              "count": 1,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/EmojiMakerFragment/onCreate",
              "scope": "Mobile/Activity/Name/Display EmojiMakerFragment"
            },
            {
              "count": 1,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Mobile/Summary/View Loading",
              "scope": "Mobile/Activity/Summary/Name/Display EmojiMakerFragment"
            },
            {
              "count": 16,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Mobile/Summary/JSON",
              "scope": "Mobile/Activity/Summary/Name/Display SetupWizardActivity"
            },
            {
              "count": 4,
              "total": 0.002305084745762712,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0.002305084745762712,
              "name": "Mobile/Summary/Database",
              "scope": "Mobile/Activity/Summary/Name/Display SetupWizardActivity"
            },
            {
              "count": 1,
              "total": 0.5416949152542373,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0.5416949152542373,
              "name": "Mobile/Summary/View Loading",
              "scope": "Mobile/Activity/Summary/Name/Display SetupWizardActivity"
            },
            {
              "count": 1,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/JSONObject/toString",
              "scope": "Mobile/Activity/Name/Display SlashAdsActivity"
            },
            {
              "count": 1,
              "total": 0.0009999275207519531,
              "min": 0.0009999275207519531,
              "max": 0.0009999275207519531,
              "sum_of_squares": 9.998550467571476e-7,
              "exclusive": 0.001,
              "name": "Method/BitmapFactory/decodeFile",
              "scope": "Mobile/Activity/Name/Display SlashAdsActivity"
            },
            {
              "count": 1,
              "total": 0.013000011444091797,
              "min": 0.013000011444091797,
              "max": 0.013000011444091797,
              "sum_of_squares": 0.00016900029754651769,
              "exclusive": 0.013,
              "name": "Method/SlashAdsActivity/onCreate",
              "scope": "Mobile/Activity/Name/Display SlashAdsActivity"
            },
            {
              "count": 1,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/SQLiteDatabase/rawQuery",
              "scope": "Mobile/Activity/Name/Display SlashAdsActivity"
            },
            {
              "count": 1,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/SecondaryCodeMgr$BuildCode/onPostExecute",
              "scope": "Mobile/Activity/Name/Display SlashAdsActivity"
            },
            {
              "count": 4,
              "total": 0.0039997100830078125,
              "min": 0.0009999275207519531,
              "max": 0.0009999275207519531,
              "sum_of_squares": 0.000003999420187028591,
              "exclusive": 0.004,
              "name": "Method/SQLiteDatabase/delete",
              "scope": "Mobile/Activity/Name/Display SoundFragment"
            },
            {
              "count": 2,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/JSONObject/toString",
              "scope": "Mobile/Activity/Name/Display SoundFragment"
            },
            {
              "count": 1,
              "total": 0.0009999275207519531,
              "min": 0.0009999275207519531,
              "max": 0.0009999275207519531,
              "sum_of_squares": 9.998550467571476e-7,
              "exclusive": 0.001,
              "name": "Method/SoundFragment/onCreateView",
              "scope": "Mobile/Activity/Name/Display SoundFragment"
            },
            {
              "count": 1,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/BackgroundHttpTask/onPostExecute",
              "scope": "Mobile/Activity/Name/Display SoundFragment"
            },
            {
              "count": 1,
              "total": 0.003000020980834961,
              "min": 0.003000020980834961,
              "max": 0.003000020980834961,
              "sum_of_squares": 0.000009000125885449961,
              "exclusive": 0.003,
              "name": "Method/ThemeFragment/onCreateView",
              "scope": "Mobile/Activity/Name/Display SoundFragment"
            },
            {
              "count": 26,
              "total": 0.23399925231933594,
              "min": 0.003000020980834961,
              "max": 0.01399993896484375,
              "sum_of_squares": 0.0025139836658354398,
              "exclusive": 0.2340000000000001,
              "name": "Method/BitmapFactory/decodeResource",
              "scope": "Mobile/Activity/Name/Display SoundFragment"
            },
            {
              "count": 2,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/JSONArray/<init>",
              "scope": "Mobile/Activity/Name/Display SoundFragment"
            },
            {
              "count": 2,
              "total": 0.009999990463256836,
              "min": 0.003000020980834961,
              "max": 0.006999969482421875,
              "sum_of_squares": 0.000057999698640287534,
              "exclusive": 0.009000000000000001,
              "name": "Method/h/onPostExecute",
              "scope": "Mobile/Activity/Name/Display SoundFragment"
            },
            {
              "count": 3,
              "total": 0.03399991989135742,
              "min": 0.003999948501586914,
              "max": 0.026000022888183594,
              "sum_of_squares": 0.0007080003662167655,
              "exclusive": 0.034,
              "name": "Method/MessageFragment/onCreateView",
              "scope": "Mobile/Activity/Name/Display SoundFragment"
            },
            {
              "count": 4,
              "total": 0.005000114440917969,
              "min": 0,
              "max": 0.003000020980834961,
              "sum_of_squares": 0.00001100031283840508,
              "exclusive": 0.005,
              "name": "Method/LoadingFragment/onCreateView",
              "scope": "Mobile/Activity/Name/Display SoundFragment"
            },
            {
              "count": 1,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/SoundFragment/onCreate",
              "scope": "Mobile/Activity/Name/Display SoundFragment"
            },
            {
              "count": 5,
              "total": 0.002000093460083008,
              "min": 0,
              "max": 0.0010001659393310547,
              "sum_of_squares": 0.0000020001869529551186,
              "exclusive": 0.002,
              "name": "Method/JSONObject/<init>",
              "scope": "Mobile/Activity/Name/Display SoundFragment"
            },
            {
              "count": 2,
              "total": 0.006000041961669922,
              "min": 0.0019998550415039062,
              "max": 0.004000186920166016,
              "sum_of_squares": 0.000020000915583295864,
              "exclusive": 0.006,
              "name": "Method/SQLiteDatabase/insert",
              "scope": "Mobile/Activity/Name/Display SoundFragment"
            },
            {
              "count": 1,
              "total": 0.003999948501586914,
              "min": 0.003999948501586914,
              "max": 0.003999948501586914,
              "sum_of_squares": 0.0000159995880153474,
              "exclusive": 0.004,
              "name": "Method/StickerFragment/onCreateView",
              "scope": "Mobile/Activity/Name/Display SoundFragment"
            },
            {
              "count": 3,
              "total": 0.0009999275207519531,
              "min": 0,
              "max": 0.0009999275207519531,
              "sum_of_squares": 9.998550467571476e-7,
              "exclusive": 0.001,
              "name": "Method/SQLiteDatabase/rawQuery",
              "scope": "Mobile/Activity/Name/Display SoundFragment"
            },
            {
              "count": 1,
              "total": 0.009999990463256836,
              "min": 0.009999990463256836,
              "max": 0.009999990463256836,
              "sum_of_squares": 0.00009999980926522767,
              "exclusive": 0.01,
              "name": "Method/EmojiMakerFragment/onCreateView",
              "scope": "Mobile/Activity/Name/Display SoundFragment"
            },
            {
              "count": 1,
              "total": 0.0035324947589098527,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0.0035324947589098527,
              "name": "Mobile/Summary/Images",
              "scope": "Mobile/Activity/Summary/Name/Display NewSettingActivity"
            },
            {
              "count": 4,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Mobile/Summary/JSON",
              "scope": "Mobile/Activity/Summary/Name/Display NewSettingActivity"
            },
            {
              "count": 66,
              "total": 0.02825995807127882,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0.02825995807127882,
              "name": "Mobile/Summary/Database",
              "scope": "Mobile/Activity/Summary/Name/Display NewSettingActivity"
            },
            {
              "count": 1,
              "total": 1.6532075471698113,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 1.6532075471698113,
              "name": "Mobile/Summary/View Loading",
              "scope": "Mobile/Activity/Summary/Name/Display NewSettingActivity"
            },
            {
              "count": 7,
              "total": 0.003000020980834961,
              "min": 0,
              "max": 0.0010001659393310547,
              "sum_of_squares": 0.0000030000419997122663,
              "exclusive": 0.003,
              "name": "Method/SQLiteDatabase/rawQuery",
              "scope": "Mobile/Activity/Name/Display ThemeFragment"
            },
            {
              "count": 3,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/SQLiteDatabase/delete",
              "scope": "Mobile/Activity/Name/Display ThemeFragment"
            },
            {
              "count": 1,
              "total": 0.08700013160705566,
              "min": 0.08700013160705566,
              "max": 0.08700013160705566,
              "sum_of_squares": 0.007569022899645006,
              "exclusive": 0.084,
              "name": "Method/ThemeFragment/onCreate",
              "scope": "Mobile/Activity/Name/Display ThemeFragment"
            },
            {
              "count": 1,
              "total": 0.2349998950958252,
              "min": 0.2349998950958252,
              "max": 0.2349998950958252,
              "sum_of_squares": 0.05522495069504885,
              "exclusive": 0.235,
              "name": "Method/SetupWizardActivity/onCreate",
              "scope": "Mobile/Activity/Name/Display SetupWizardActivity"
            },
            {
              "count": 2,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/JSONArray/<init>",
              "scope": "Mobile/Activity/Name/Display SetupWizardActivity"
            },
            {
              "count": 2,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/SQLiteDatabase/delete",
              "scope": "Mobile/Activity/Background/Name/Display ThemeFragment"
            },
            {
              "count": 2,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/SQLiteDatabase/rawQuery",
              "scope": "Mobile/Activity/Background/Name/Display ThemeFragment"
            },
            {
              "count": 2,
              "total": 0,
              "min": 0,
              "max": 0,
              "sum_of_squares": 0,
              "exclusive": 0,
              "name": "Method/JSONArray/toString",
              "scope": "Mobile/Activity/Background/Name/Display ThemeFragment"
            }
          ],
          "httpError": [],
          "activity": [
            {
              "traceVersion": "1.0",
              "type": "ACTIVITY",
              "entryTimestamp": 1512988020011,
              "exitTimestamp": 1512988020555,
              "displayName": "Display SetupWizardActivity",
              "rootTrace": {
                "type": "TRACE",
                "paramType": "TRACE",
                "entryTimestamp": 1512988020011,
                "exitTimestamp": 1512988020555,
                "displayName": "Display SetupWizardActivity",
                "trdId": 0,
                "trdName": "main",
                "child": [
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020309,
                    "exitTimestamp": 1512988020309,
                    "displayName": "JSONObject#<init>",
                    "trdId": 2237,
                    "trdName": "AsyncTask #1",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020013,
                    "exitTimestamp": 1512988020248,
                    "displayName": "SetupWizardActivity#onCreate",
                    "trdId": 1,
                    "trdName": "main",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020276,
                    "exitTimestamp": 1512988020276,
                    "displayName": "JSONObject#<init>",
                    "trdId": 2292,
                    "trdName": "pool-13-thread-2",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020326,
                    "exitTimestamp": 1512988020327,
                    "displayName": "SQLiteDatabase#insertOrThrow",
                    "trdId": 2236,
                    "trdName": "Measurement Worker",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020326,
                    "exitTimestamp": 1512988020326,
                    "displayName": "SQLiteDatabase#rawQuery",
                    "trdId": 2236,
                    "trdName": "Measurement Worker",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020015,
                    "exitTimestamp": 1512988020015,
                    "displayName": "JSONObject#toString",
                    "trdId": 2287,
                    "trdName": "pool-3-thread-1",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020320,
                    "exitTimestamp": 1512988020320,
                    "displayName": "JSONArray#<init>",
                    "trdId": 1,
                    "trdName": "main",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020311,
                    "exitTimestamp": 1512988020311,
                    "displayName": "JSONArray#<init>",
                    "trdId": 1,
                    "trdName": "main",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020308,
                    "exitTimestamp": 1512988020308,
                    "displayName": "JSONObject#<init>",
                    "trdId": 2241,
                    "trdName": "AsyncTask #4",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020016,
                    "exitTimestamp": 1512988020016,
                    "displayName": "JSONObject#toString",
                    "trdId": 2287,
                    "trdName": "pool-3-thread-1",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020325,
                    "exitTimestamp": 1512988020325,
                    "displayName": "SQLiteDatabase#rawQuery",
                    "trdId": 2236,
                    "trdName": "Measurement Worker",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020016,
                    "exitTimestamp": 1512988020016,
                    "displayName": "JSONObject#toString",
                    "trdId": 2287,
                    "trdName": "pool-3-thread-1",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020269,
                    "exitTimestamp": 1512988020269,
                    "displayName": "JSONArray#toString",
                    "trdId": 2244,
                    "trdName": "pool-2-thread-1",
                    "child": []
                  },
                  {
                    "carrier": "wifi",
                    "http_method": "POST",
                    "status_code": "200",
                    "wan_type": "wifi",
                    "uri": "https://graph.facebook.com/v2.9/275932369410927/activities",
                    "bytes_sent": "1513",
                    "paramType": "NETWORK",
                    "entryTimestamp": 1512988020270,
                    "exitTimestamp": 1512988020555,
                    "displayName": "External/graph.facebook.com",
                    "trdId": 2244,
                    "trdName": "pool-2-thread-1",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020324,
                    "exitTimestamp": 1512988020324,
                    "displayName": "SQLiteDatabase#query",
                    "trdId": 2236,
                    "trdName": "Measurement Worker",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020269,
                    "exitTimestamp": 1512988020269,
                    "displayName": "JSONObject#<init>",
                    "trdId": 2240,
                    "trdName": "AsyncTask #3",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020269,
                    "exitTimestamp": 1512988020269,
                    "displayName": "JSONArray#toString",
                    "trdId": 2240,
                    "trdName": "AsyncTask #3",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020253,
                    "exitTimestamp": 1512988020253,
                    "displayName": "JSONObject#toString",
                    "trdId": 2244,
                    "trdName": "pool-2-thread-1",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020269,
                    "exitTimestamp": 1512988020269,
                    "displayName": "JSONArray#toString",
                    "trdId": 2244,
                    "trdName": "pool-2-thread-1",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020309,
                    "exitTimestamp": 1512988020309,
                    "displayName": "JSONArray#toString",
                    "trdId": 2237,
                    "trdName": "AsyncTask #1",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020308,
                    "exitTimestamp": 1512988020308,
                    "displayName": "JSONArray#toString",
                    "trdId": 2241,
                    "trdName": "AsyncTask #4",
                    "child": []
                  },
                  {
                    "type": "TRACE",
                    "paramType": "TRACE",
                    "entryTimestamp": 1512988020015,
                    "exitTimestamp": 1512988020015,
                    "displayName": "JSONObject#toString",
                    "trdId": 2287,
                    "trdName": "pool-3-thread-1",
                    "child": []
                  }
                ]
              },
              "vitals": """[{"type":"VITALS","MEMORY":[[1512988020050,54.7373046875],[1512988020183,71.9794921875],[1512988020320,87.638671875]],"CPU":[[1512988020184,16.19047619047619],[1512988020321,13.20754716981132]]}]"""
            }
          ],
          "health": [],
          "session": "{}",
          "events": []
     */
    public static void parseDataSummary(ApmBaseSearchResponse<ApmSourceData> apmData) {
        // 数组字段的个数
        Set<Integer> transactionLengthSet = new HashSet<>();
        Set<Integer> measurementLengthSet = new HashSet<>();
        Set<Integer> httpErrorLengthSet = new HashSet<>();
        Set<Integer> activityLengthSet = new HashSet<>();
        Set<Integer> healthLengthSet = new HashSet<>();
        Set<Integer> eventsLengthSet = new HashSet<>();

        // 字段与数据映射
        HashMap<String, List<ApmBaseUnit<ApmSourceData>>> deviceIdIndexMap = new HashMap<>();
        HashMap<String, List<ApmBaseUnit<ApmSourceData>>> timestampIndexMap = new HashMap<>();

        HashMap<String, List<ApmBaseUnit<ApmSourceData>>> transactionUrlIndexMap = new HashMap<>();
        HashMap<String, List<ApmBaseUnit<ApmSourceData>>> measureNameIndexMap = new HashMap<>();
        HashMap<String, List<ApmBaseUnit<ApmSourceData>>> measureScopeIndexMap = new HashMap<>();
        HashMap<String, List<ApmBaseUnit<ApmSourceData>>> activityDisplayNameIndexMap = new HashMap<>();
        HashMap<String, List<ApmBaseUnit<ApmSourceData>>> activityVitalsIndexMap = new HashMap<>();
        HashMap<String, List<ApmBaseUnit<ApmSourceData>>> sessionIndexMap = new HashMap<>();

        for (ApmBaseUnit<ApmSourceData> unit : apmData.getHits().getHits()) {
            ApmSourceData ctc = unit.get_source();

            String deviceId = ctc.getDeviceId();
            String timestamp = ctc.getTimestamp();

            addListSizeSet(transactionLengthSet, ctc.getTransaction());
            addListSizeSet(measurementLengthSet, ctc.getMeasurement());
            addListSizeSet(httpErrorLengthSet, ctc.getHttpError());
            addListSizeSet(activityLengthSet, ctc.getActivity());
            addListSizeSet(healthLengthSet, ctc.getHealth());
//            eventsLengthSet.add(ctc.getEvents().size());

            addToMap(deviceIdIndexMap, unit, deviceId);
            addToMap(timestampIndexMap, unit, timestamp);

            // todo: build map.
//            addToMap(appIndexMap, unit, apps.toString());
//            addToMap(deviceIndexMap, unit, devices.toString());
//            addPartToMap(deviceMicsItemListHashMap, unit, deviceMicsItems);
        }

        deviceIdIndexMap.size();
    }

    private static void addListSizeSet(Set<Integer> lengthSet, List list) {
        lengthSet.add(null == list ? 0 : list.size());
    }

    public static void parseConnectionSummary(ApmBaseSearchResponse<ApmSourceConnect> apmConnect) {
        Set<Integer> appLengthSet = new HashSet<>();
        Set<Integer> deviceLengthSet = new HashSet<>();
        Set<Integer> devicemicsLengthSet = new HashSet<>();

        HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> deviceIdIndexMap = new HashMap<>();
        HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> timestampIndexMap = new HashMap<>();
        HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> appIndexMap = new HashMap<>();
        HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> deviceIndexMap = new HashMap<>();
        HashMap<ApmDeviceMicsItem, List<ApmBaseUnit<ApmSourceConnect>>> deviceMicsItemListHashMap = new HashMap<>();

        for (ApmBaseUnit<ApmSourceConnect> unit : apmConnect.getHits().getHits()) {
            ApmSourceConnect ctc = unit.get_source();

            String deviceId = ctc.getDeviceId();
            String timestamp = ctc.getTimestamp();

            List<String> apps = ctc.getApp();
            List<String> devices = ctc.getDevice();
            List<ApmDeviceMicsItem> deviceMicsItems = ctc.getDevicemics();

            appLengthSet.add(apps.size());
            deviceLengthSet.add(devices.size());
            devicemicsLengthSet.add(deviceMicsItems.size());

            addToMap(deviceIdIndexMap, unit, deviceId);
            addToMap(timestampIndexMap, unit, timestamp);

            addToMap(appIndexMap, unit, apps.toString());
            addToMap(deviceIndexMap, unit, devices.toString());
            addPartToMap(deviceMicsItemListHashMap, unit, deviceMicsItems);
        }

        apmConnect.isTimed_out();
    }

    private static<K, V> void addPartToMap(Map<K, List<V>> indexMap, V unit, List<K> keyList) {
        for (K key : keyList) {
            addToMap(indexMap, unit, key);
        }
    }

    private static<K, V> void addToMap(Map<K, List<V>> indexMap, V value, K key) {
        List<V> valueList = indexMap.get(key);
        if (null == valueList) {
            valueList = new ArrayList<>();
            indexMap.put(key, valueList);
        }
        valueList.add(value);
    }

    private static boolean isEquals(String text, String other) {
        return null == text && null == other || null != text && text.equals(other);
    }

    private static void handleUnknownType(Map<String, List<String>> unknownData, String index, String type) {
        addToMap(unknownData, type, index);
    }
}
