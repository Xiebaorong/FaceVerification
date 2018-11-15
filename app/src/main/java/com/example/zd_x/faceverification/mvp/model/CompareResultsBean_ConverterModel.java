package com.example.zd_x.faceverification.mvp.model;

import com.google.gson.Gson;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CompareResultsBean_ConverterModel implements PropertyConverter<List<HistoryVerificationResultModel.CompareResultsBean>, String> {
    @Override
    public List<HistoryVerificationResultModel.CompareResultsBean> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        List<String> list_str = Arrays.asList(databaseValue.split(","));
        List<HistoryVerificationResultModel.CompareResultsBean> list_com = new ArrayList<>();
        for (String s: list_str){
            list_com.add(new Gson().fromJson(s, HistoryVerificationResultModel.CompareResultsBean.class));
        }
        return list_com;
    }

    @Override
    public String convertToDatabaseValue(List<HistoryVerificationResultModel.CompareResultsBean> entityProperty) {
       if (entityProperty ==null){
           return null;
       }else {
           StringBuilder sb = new StringBuilder();
           for (HistoryVerificationResultModel.CompareResultsBean entity : entityProperty){
               String str = new Gson().toJson(entity);
               sb.append(str);
               sb.append(",");
           }

           return sb.toString();
       }
    }
}
