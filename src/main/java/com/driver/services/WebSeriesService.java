package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{
        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo
        WebSeries webSeries = webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());
        if(webSeries != null)
        {
            throw new Exception("Series is already present");
        }
        webSeries = new WebSeries();
        ProductionHouse productionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId()).get();
        webSeries.setSeriesName(webSeriesEntryDto.getSeriesName());
        webSeries.setAgeLimit(webSeriesEntryDto.getAgeLimit());
        webSeries.setRating(webSeriesEntryDto.getRating());
        webSeries.setSubscriptionType(webSeriesEntryDto.getSubscriptionType());
        int noOFWebSeries = productionHouse.getWebSeriesList().size();
        double productionRating = productionHouse.getRatings()*noOFWebSeries;
        productionRating = (productionRating + webSeriesEntryDto.getRating())/(noOFWebSeries+1);
        productionHouse.setRatings(productionRating);
        productionHouse.getWebSeriesList().add(webSeries);
        webSeries.setProductionHouse(productionHouse);
        webSeries = webSeriesRepository.save(webSeries);
        productionHouseRepository.save(productionHouse);
        return webSeries.getId();
    }

}
