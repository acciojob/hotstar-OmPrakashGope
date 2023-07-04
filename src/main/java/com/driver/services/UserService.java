package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){
        user = userRepository.save(user);
        //Jut simply add the user to the Db and return the userId returned by the repository
        return user.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        User user = userRepository.findById(userId).get();
        SubscriptionType subscriptionType = user.getSubscription().getSubscriptionType();
        List<WebSeries> webSeriesList = webSeriesRepository.findAll();
        int viewableContent = 0;
        for(WebSeries series:webSeriesList)
        {
            if(user.getAge() >= series.getAgeLimit())
            {
                if(subscriptionType.equals(SubscriptionType.ELITE))
                {
                    viewableContent++;
                }
                else if(subscriptionType.equals(SubscriptionType.PRO) && (series.getSubscriptionType().equals(SubscriptionType.BASIC) ||
                        series.getSubscriptionType().equals(SubscriptionType.PRO)))
                {
                    viewableContent++;
                }
                else if(subscriptionType.equals(SubscriptionType.BASIC) && (series.getSubscriptionType().equals(SubscriptionType.BASIC))
                {
                    viewableContent++;
                }
            }
        }
        return viewableContent;
    }


}
